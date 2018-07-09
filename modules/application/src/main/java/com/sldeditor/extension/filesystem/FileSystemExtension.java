/*
 * SLD Editor - The Open Source Java SLD Editor
 *
 * Copyright (C) 2016, SCISYS UK Limited
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.sldeditor.extension.filesystem;

import com.sldeditor.common.LoadSLDInterface;
import com.sldeditor.common.NodeInterface;
import com.sldeditor.common.RecursiveUpdateInterface;
import com.sldeditor.common.SLDDataInterface;
import com.sldeditor.common.ToolSelectionInterface;
import com.sldeditor.common.connection.GeoServerConnectionManager;
import com.sldeditor.common.console.ConsoleManager;
import com.sldeditor.common.data.GeoServerConnection;
import com.sldeditor.common.filesystem.FileSystemInterface;
import com.sldeditor.common.filesystem.SelectedFiles;
import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.common.preferences.PrefData;
import com.sldeditor.common.preferences.PrefDataLastViewedEnum;
import com.sldeditor.datasource.extension.filesystem.node.FSTree;
import com.sldeditor.datasource.extension.filesystem.node.FileSystemNodeManager;
import com.sldeditor.extension.ExtensionFactory;
import com.sldeditor.extension.ExtensionInterface;
import com.sldeditor.extension.filesystem.file.FileSystemInput;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import org.apache.log4j.Logger;

/**
 * Application extension that presents a file system to the user.
 *
 * @author Robert Ward (SCISYS)
 */
public class FileSystemExtension
        implements ExtensionInterface, FileSelectionInterface, RecursiveUpdateInterface {
    /** The Constant ROOT_NODE. */
    public static final String ROOT_NODE =
            Localisation.getString(FileSystemExtension.class, "FileSystemExtension.root");

    /** The Constant FOLDER_ARG. */
    private static final String FOLDER_ARG = "folder";

    /** The Constant GEOSERVER_ARG. */
    private static final String GEOSERVER_ARG = "geoserver";

    /** The Constant EXTENSION_ARG_PREFIX. */
    private static final String EXTENSION_ARG_PREFIX = "file";

    /** The parent obj. */
    private LoadSLDInterface parentObj = null;

    /** The file system tree. */
    private FSTree tree;

    /** The extension panel. */
    private JPanel extensionPanel = null;

    /** The model. */
    private DefaultTreeModel model;

    /** The logger. */
    private static Logger logger = Logger.getLogger(FileSystemExtension.class);

    /** The extension list. */
    private List<FileSystemInterface> extensionList = new ArrayList<FileSystemInterface>();

    /** The tool mgr. */
    private ToolSelectionInterface toolMgr = null;

    /** The sld saved. */
    private boolean sldSaved = false;

    /**
     * Initialise.
     *
     * @param parent the parent
     * @param toolMgr the tool manager
     */
    @Override
    public void initialise(LoadSLDInterface parent, ToolSelectionInterface toolMgr) {
        this.toolMgr = toolMgr;

        this.parentObj = parent;
        if (toolMgr != null) {
            toolMgr.addRecursiveListener(this);
        }

        // Add extensions
        extensionList = FileSystemExtensionFactory.getFileExtensionList(toolMgr);

        extensionPanel = new JPanel();
        extensionPanel.setLayout(new BorderLayout());
        extensionPanel.setPreferredSize(new Dimension(450, 400));

        JPanel toolPanel = null;

        if (toolMgr != null) {
            toolPanel = toolMgr.getPanel();
        }

        if (toolPanel != null) {
            extensionPanel.add(toolPanel, BorderLayout.NORTH);
        }

        tree = new FSTree();
        tree.setRootVisible(true);

        DefaultMutableTreeNode rootNode;
        try {
            rootNode = new DefaultMutableTreeNode(ROOT_NODE);

            model = new DefaultTreeModel(rootNode);

            FileSystemNodeManager.create(tree, model);

            for (FileSystemInterface extension : extensionList) {
                extension.populate(tree, model, rootNode);
            }
        } catch (SecurityException e1) {
            ConsoleManager.getInstance().exception(this, e1);
        }

        tree.setModel(model);
        tree.setDragEnabled(true);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);

        tree.addMouseListener(
                new MouseListener() {

                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (SwingUtilities.isRightMouseButton(e)) {

                            int row = tree.getClosestRowForLocation(e.getX(), e.getY());
                            tree.setSelectionRow(row);

                            Object selectedItem = tree.getLastSelectedPathComponent();

                            Thread t1 =
                                    new Thread(
                                            new Runnable() {
                                                public void run() {

                                                    JPopupMenu popupMenu = new JPopupMenu();

                                                    for (FileSystemInterface extension :
                                                            extensionList) {
                                                        extension.rightMouseButton(
                                                                popupMenu, selectedItem, e);
                                                    }

                                                    if ((popupMenu.getComponentCount() > 0)
                                                            && (e != null)) {
                                                        popupMenu.show(
                                                                e.getComponent(),
                                                                e.getX(),
                                                                e.getY());
                                                    }
                                                }
                                            });
                            t1.start();
                        }
                    }

                    @Override
                    public void mousePressed(MouseEvent e) {}

                    @Override
                    public void mouseReleased(MouseEvent e) {}

                    @Override
                    public void mouseEntered(MouseEvent e) {}

                    @Override
                    public void mouseExited(MouseEvent e) {}
                });

        // Listen for Tree Selection Events
        tree.addTreeExpansionListener(
                new TreeExpansionListener() {

                    /*
                     * (non-Javadoc)
                     *
                     * @see javax.swing.event.TreeExpansionListener#treeExpanded(javax.swing.event.TreeExpansionEvent)
                     */
                    public void treeExpanded(TreeExpansionEvent evt) {
                        TreePath path = evt.getPath();

                        Object selectedItem = path.getLastPathComponent();

                        for (FileSystemInterface extension : extensionList) {
                            if (extension.treeExpanded(selectedItem)) {
                                ((DefaultTreeModel) tree.getModel())
                                        .nodeStructureChanged((TreeNode) selectedItem);
                            }
                        }
                    }

                    /*
                     * (non-Javadoc)
                     *
                     * @see javax.swing.event.TreeExpansionListener#treeCollapsed(javax.swing.event.TreeExpansionEvent)
                     */
                    public void treeCollapsed(TreeExpansionEvent evt) {
                        // Nothing to do
                    }
                });

        // Tree selection listener
        tree.setTreeSelection(this);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
        panel.add(new JScrollPane(tree));

        extensionPanel.add(panel, BorderLayout.CENTER);
        logger.debug("FileSystem initialise finished");
    }

    /**
     * Called when the file system tree selection has changed.
     *
     * @param e the e
     */
    @Override
    public void treeSelection(TreeSelectionEvent e) {
        if (!tree.isDragging()) {
            if (sldSaved) {
                // Reset flag
                sldSaved = false;
            }

            if (parentObj != null) {
                parentObj.preLoad();
            }

            SelectedFiles combinedFiles = treeItemSelected();

            if ((parentObj != null) && !parentObj.loadSLDString(combinedFiles)) {
                tree.revertSelection(e.getOldLeadSelectionPath());
            }
        }
    }

    /**
     * Called to determine the selected items from the tree nodes selected.
     *
     * @return the selected files
     */
    private SelectedFiles treeItemSelected() {
        List<SLDDataInterface> sldDataList = new ArrayList<SLDDataInterface>();
        List<NodeInterface> nodeList = new ArrayList<NodeInterface>();

        SelectedFiles combinedFiles = new SelectedFiles();

        TreePath[] selectedPaths = tree.getSelectionPaths();

        if (selectedPaths != null) {
            for (TreePath selectedPath : selectedPaths) {
                Object o = selectedPath.getLastPathComponent();

                if (o instanceof NodeInterface) {
                    NodeInterface handler = (NodeInterface) o;

                    FileSystemInterface input = handler.getHandler();
                    SelectedFiles selectedFiles = input.getSLDContents(handler);

                    if (selectedFiles != null) {
                        combinedFiles.setDataSource(selectedFiles.isDataSource());
                        combinedFiles.setFolderName(selectedFiles.getFolderName());
                        combinedFiles.setConnectionData(selectedFiles.getConnectionData());
                        combinedFiles.setIsFolder(
                                combinedFiles.isFolder() | selectedFiles.isFolder());

                        List<SLDDataInterface> handlerDataList = selectedFiles.getSldData();
                        if (handlerDataList != null) {
                            combinedFiles.appendSLDData(handlerDataList);
                            sldDataList.addAll(handlerDataList);
                        }
                    }

                    nodeList.add(handler);
                }
            }
        }

        if (toolMgr != null) {
            toolMgr.setSelectedItems(nodeList, sldDataList);
        }
        return combinedFiles;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.batch.ExtensionInterface#setArguments(java.util.List)
     */
    @Override
    public String setArguments(List<String> extensionArgList) {
        String acceptArgument = null;

        if (extensionArgList != null) {
            for (String arg : extensionArgList) {
                logger.debug(arg);

                String[] components = arg.split("=");

                if (components.length == 2) {
                    String field = components[0];
                    String value = components[1];

                    if (field.compareToIgnoreCase(FOLDER_ARG) == 0) {
                        // Check to if the stored string is a folder,
                        // expecting it to be stored as an URL
                        File folder = new File(value);

                        if (folder.isDirectory()) {
                            // Folder was stored as an URL
                            if (folder.exists()) {
                                try {
                                    FileSystemExtensionFactory.getFileSystemInput()
                                            .setFolder(folder.toURI().toURL(), false);
                                    acceptArgument = arg;
                                } catch (MalformedURLException e) {
                                    ConsoleManager.getInstance().exception(this, e);
                                }
                            } else {
                                ConsoleManager.getInstance()
                                        .error(
                                                this,
                                                Localisation.getField(
                                                                getClass(),
                                                                "FileSystemExtension.folderDoesNotExist")
                                                        + value);
                            }
                        }
                    } else if (field.compareToIgnoreCase(GEOSERVER_ARG) == 0) {
                        // It wasn't a folder, check to see if it is a GeoServer connection
                        GeoServerConnection connectionData =
                                GeoServerConnectionManager.getInstance().getConnection(value);
                        if (connectionData != null) {
                            FileSystemExtensionFactory.getGeoServerInput()
                                    .setFolder(connectionData, true);
                            acceptArgument = arg;
                        } else {
                            // Don't recognise the string
                            ConsoleManager.getInstance()
                                    .error(
                                            this,
                                            Localisation.getField(
                                                            getClass(),
                                                            "FileSystemExtension.geoServerDoesNotExist")
                                                    + value);
                        }
                    } else {
                        // Don't recognise the string
                        ConsoleManager.getInstance()
                                .error(
                                        this,
                                        Localisation.getField(
                                                        getClass(),
                                                        "FileSystemExtension.unknownStartUp")
                                                + value);
                    }
                }
            }

            treeItemSelected();
        }
        return acceptArgument;
    }

    /**
     * Gets the extension arg prefix.
     *
     * @return the extension arg prefix
     */
    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.batch.ExtensionInterface#getExtensionArgPrefix()
     */
    @Override
    public String getExtensionArgPrefix() {
        return EXTENSION_ARG_PREFIX;
    }

    /**
     * Gets the panel.
     *
     * @return the panel
     */
    @Override
    public JPanel getPanel() {
        return extensionPanel;
    }

    /**
     * Creates the menus.
     *
     * @param mnTools the mn tools
     */
    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.batch.ExtensionInterface#createMenus(javax.swing.JMenu)
     */
    @Override
    public void createMenus(JMenu mnTools) {
        // Do nothing
    }

    /**
     * Gets the tooltip.
     *
     * @return the tooltip
     */
    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.batch.ExtensionInterface#getTooltip()
     */
    @Override
    public String getTooltip() {
        return "Input Manager";
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.batch.ExtensionInterface#getName()
     */
    @Override
    public String getName() {
        return "File System Manager";
    }

    /**
     * Open.
     *
     * @param url the url
     * @return the list
     */
    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.batch.ExtensionInterface#open(java.lang.String)
     */
    @Override
    public List<SLDDataInterface> open(URL url) {

        FileSystemInput fileSystemInput = FileSystemExtensionFactory.getFileSystemInput();
        if (fileSystemInput != null) {
            fileSystemInput.setFolder(url, true);

            for (FileSystemInterface extension : extensionList) {
                List<SLDDataInterface> sldDataList = extension.open(url);
                if (sldDataList != null) {
                    return sldDataList;
                }
            }
        }
        return null;
    }

    /**
     * Save.
     *
     * @param sldData the sld data
     * @return true, if successful
     */
    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.extension.ExtensionInterface#save(java.net.URL, com.sldeditor.ui.iface.SLDDataInterface)
     */
    @Override
    public boolean save(SLDDataInterface sldData) {
        boolean saved = false;
        for (FileSystemInterface extension : extensionList) {
            if (saved == false) {
                saved = extension.save(sldData);
            }
        }

        if (saved) {
            FileSystemInput fileSystemInput = FileSystemExtensionFactory.getFileSystemInput();
            if (fileSystemInput != null) {
                fileSystemInput.setFolder(sldData.getSLDURL(), true);
            }
        }
        sldSaved = true;

        return saved;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.extension.ExtensionInterface#updateForPreferences(com.sldeditor.common.preferences.PrefData, java.util.List)
     */
    @Override
    public void updateForPreferences(PrefData prefData, List<String> extensionArgList) {
        if ((prefData != null) && (extensionArgList != null)) {
            if (prefData.isSaveLastFolderView()) {
                String folderName = prefData.getLastFolderViewed();

                if (folderName != null) {
                    String suffix =
                            (prefData.getLastViewedKey() == PrefDataLastViewedEnum.GEOSERVER)
                                    ? GEOSERVER_ARG
                                    : FOLDER_ARG;
                    String prefix =
                            String.format(
                                    "%s.%s.%s=",
                                    ExtensionFactory.EXTENSION_PREFIX,
                                    getExtensionArgPrefix(),
                                    suffix);

                    // Check to see if the argument already exists
                    boolean found = false;
                    for (String arg : extensionArgList) {
                        if (arg.startsWith(prefix)) {
                            found = true;
                        }
                    }

                    // If the argument doesn't already exists then use
                    if (!found) {
                        String arg = prefix + folderName;

                        extensionArgList.add(arg);
                    }
                }
            }
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.common.RecursiveUpdateInterface#recursiveValuesUpdated(boolean)
     */
    @Override
    public void recursiveValuesUpdated(boolean recurse) {
        treeItemSelected();
    }
}
