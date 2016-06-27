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
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.apache.log4j.Logger;

import com.sldeditor.common.LoadSLDInterface;
import com.sldeditor.common.NodeInterface;
import com.sldeditor.common.SLDDataInterface;
import com.sldeditor.common.ToolSelectionInterface;
import com.sldeditor.common.console.ConsoleManager;
import com.sldeditor.common.filesystem.FileSystemInterface;
import com.sldeditor.common.filesystem.SelectedFiles;
import com.sldeditor.datasource.extension.filesystem.node.FSTree;
import com.sldeditor.datasource.extension.filesystem.node.FileSystemNodeManager;
import com.sldeditor.extension.ExtensionInterface;

/**
 * Application extension that presents a file system to the user.
 * 
 * @author Robert Ward (SCISYS)
 */
public class FileSystemExtension implements ExtensionInterface
{
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
    /**
     * Initialise.
     *
     * @param parent the parent
     * @param toolMgr the tool manager
     */
    @Override
    public void initialise(LoadSLDInterface parent, ToolSelectionInterface toolMgr)
    {
        this.toolMgr = toolMgr;

        if(toolMgr == null)
        {
            return;
        }

        this.parentObj = parent;

        // Add extensions
        extensionList = FileSystemExtensionFactory.getFileExtensionList(toolMgr);

        extensionPanel = new JPanel();
        extensionPanel.setLayout(new BorderLayout());
        extensionPanel.setPreferredSize(new Dimension(400, 400));

        JPanel toolPanel = toolMgr.getPanel();
        if(toolPanel != null)
        {
            extensionPanel.add(toolPanel, BorderLayout.NORTH);
        }

        tree = new FSTree();
        tree.setRootVisible(true);

        DefaultMutableTreeNode rootNode;
        try
        {
            rootNode = new DefaultMutableTreeNode("Root");

            model = new DefaultTreeModel(rootNode);

            for(FileSystemInterface extension : extensionList)
            {
                extension.populate(tree, model, rootNode);
            }
        }
        catch (SecurityException e1)
        {
            e1.printStackTrace();
        }

        FileSystemNodeManager.create(tree, model);

        tree.setModel(model);
        tree.setDragEnabled(true);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);

        tree.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e)
            {
                if (SwingUtilities.isRightMouseButton(e)) {

                    int row = tree.getClosestRowForLocation(e.getX(), e.getY());
                    tree.setSelectionRow(row);

                    Object selectedItem = tree.getLastSelectedPathComponent();

                    Thread t1 = new Thread(new Runnable() {
                        public void run() {

                            for(FileSystemInterface extension : extensionList)
                            {
                                extension.rightMouseButton(selectedItem, e);
                            }
                        }
                    });  
                    t1.start();
                }
            }

            @Override
            public void mousePressed(MouseEvent e)
            {
            }

            @Override
            public void mouseReleased(MouseEvent e)
            {
            }

            @Override
            public void mouseEntered(MouseEvent e)
            {
            }

            @Override
            public void mouseExited(MouseEvent e)
            {
            }

        });

        // Listen for Tree Selection Events
        tree.addTreeExpansionListener(new TreeExpansionListener()
        {
            public void treeExpanded(TreeExpansionEvent evt) { 
                TreePath path = evt.getPath(); 

                Object selectedItem = path.getLastPathComponent();

                for(FileSystemInterface extension : extensionList)
                {
                    if(extension.treeExpanded(selectedItem))
                    {
                        ((DefaultTreeModel)tree.getModel()).nodeStructureChanged((TreeNode)selectedItem);
                    }
                }
            } 

            public void treeCollapsed(TreeExpansionEvent evt) {
                // Nothing to do 
            } 

        }); 

        // Tree selection listener
        tree.addTreeSelectionListener(new TreeSelectionListener()
        {
            @Override
            public void valueChanged(TreeSelectionEvent e)
            {
                if(!tree.isDragging())
                {
                    List<SLDDataInterface> sldDataList = new ArrayList<SLDDataInterface>();
                    List<NodeInterface> nodeList = new ArrayList<NodeInterface>();
                    boolean isDataSource = false;
                    boolean isFolder = false;

                    TreePath[] selectedPaths = tree.getSelectionPaths();

                    if(selectedPaths != null)
                    {
                        for(TreePath selectedPath : selectedPaths)
                        {
                            Object o = selectedPath.getLastPathComponent();

                            if(o instanceof NodeInterface)
                            {
                                NodeInterface handler = (NodeInterface)o;

                                FileSystemInterface input = handler.getHandler();
                                SelectedFiles selectedFiles = input.getSLDContents(handler);

                                if(selectedFiles != null)
                                {
                                    isDataSource = selectedFiles.isDataSource();
                                    isFolder |= selectedFiles.isFolder();
                                    List<SLDDataInterface> handlerDataList = selectedFiles.getSldData();
                                    if(handlerDataList != null)
                                    {
                                        sldDataList.addAll(handlerDataList);
                                    }
                                }

                                nodeList.add(handler);
                            }
                        }

                        toolMgr.setSelectedItems(nodeList, sldDataList);

                        parentObj.loadSLDString(isFolder, isDataSource, sldDataList);
                    }
                    else
                    {
                        toolMgr.setSelectedItems(nodeList, sldDataList);
                    }
                }
            }
        });

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
        panel.add(new JScrollPane(tree));

        extensionPanel.add(panel, BorderLayout.CENTER);
        logger.debug("FileSystem initialise finished");
    }

    /**
     * Sets the arguments.
     *
     * @param extensionArgList the new arguments
     */
    /* (non-Javadoc)
     * @see com.sldeditor.batch.ExtensionInterface#setArguments(java.util.List)
     */
    public void setArguments(List<String> extensionArgList)
    {
        boolean folderSet = false;

        for(String arg : extensionArgList)
        {
            logger.debug(arg);

            String[] components = arg.split("=");
            String field = components[0];
            String value = components[1];

            if(components.length == 2)
            {
                if(field.compareToIgnoreCase("folder") == 0)
                {
                    File folder = new File(value);
                    if(folder.exists())
                    {
                        try
                        {
                            setFolder(folder.toURI().toURL(), false);
                            folderSet = true;
                        }
                        catch (MalformedURLException e)
                        {
                            ConsoleManager.getInstance().exception(this, e);
                        }
                    }
                    else
                    {
                        ConsoleManager.getInstance().error(this, "Extension start up folder does not exist : " + value);
                    }
                }
            }
        }

        if(!folderSet)
        {
            if(toolMgr != null)
            {
                toolMgr.setSelectedItems(null, null);
            }
        }
    }

    /**
     * Sets the folder.
     *
     * @param url the url
     * @param allowFiles the allow files
     */
    private void setFolder(URL url, boolean allowFiles)
    {
        FileSystemNodeManager.showNodeInTree(url, allowFiles);
    }

    /**
     * Gets the extension arg prefix.
     *
     * @return the extension arg prefix
     */
    /* (non-Javadoc)
     * @see com.sldeditor.batch.ExtensionInterface#getExtensionArgPrefix()
     */

    public String getExtensionArgPrefix()
    {
        return "file";
    }

    /**
     * The main method.
     *
     * @param args the arguments
     */
    public static void main(String[] args) {

        FileSystemExtension mgr = new FileSystemExtension();

        mgr.initialise(null, null);

        JPanel panel = mgr.getPanel();

        JFrame frame = new JFrame();

        frame.getContentPane().add(panel);

        frame.setVisible(true);

        frame.setSize(400, 600);
    }

    /**
     * Gets the panel.
     *
     * @return the panel
     */
    @Override
    public JPanel getPanel()
    {
        return extensionPanel;
    }

    /**
     * Creates the menus.
     *
     * @param mnTools the mn tools
     */
    /* (non-Javadoc)
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
    /* (non-Javadoc)
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
    /* (non-Javadoc)
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
    /* (non-Javadoc)
     * @see com.sldeditor.batch.ExtensionInterface#open(java.lang.String)
     */
    @Override
    public List<SLDDataInterface> open(URL url) {

        setFolder(url, true);

        for(FileSystemInterface extension : extensionList)
        {
            List<SLDDataInterface> sldDataList = extension.open(url);
            if(sldDataList != null)
            {
                return sldDataList;
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
    /* (non-Javadoc)
     * @see com.sldeditor.extension.ExtensionInterface#save(java.net.URL, com.sldeditor.ui.iface.SLDDataInterface)
     */
    @Override
    public boolean save(SLDDataInterface sldData)
    {
        boolean saved = false;
        for(FileSystemInterface extension : extensionList)
        {
            if(saved == false)
            {
                saved = extension.save(sldData);
            }
        }

        return saved;
    }
}
