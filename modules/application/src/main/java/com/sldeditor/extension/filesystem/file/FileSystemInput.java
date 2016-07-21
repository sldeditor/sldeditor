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
package com.sldeditor.extension.filesystem.file;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

import org.apache.log4j.Logger;

import com.sldeditor.common.NodeInterface;
import com.sldeditor.common.SLDDataInterface;
import com.sldeditor.common.ToolSelectionInterface;
import com.sldeditor.common.console.ConsoleManager;
import com.sldeditor.common.filesystem.FileSystemInterface;
import com.sldeditor.common.filesystem.SelectedFiles;
import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.datasource.connector.DataSourceConnectorFactory;
import com.sldeditor.datasource.extension.filesystem.node.file.FileHandlerInterface;
import com.sldeditor.datasource.extension.filesystem.node.file.FileTreeNode;
import com.sldeditor.extension.filesystem.file.raster.RasterFileHandler;
import com.sldeditor.extension.filesystem.file.sld.SLDFileHandler;
import com.sldeditor.extension.filesystem.file.sldeditor.SLDEditorFileHandler;
import com.sldeditor.extension.filesystem.file.vector.VectorFileHandler;
import com.sldeditor.extension.filesystem.file.ysld.YSLDFileHandler;
import com.sldeditor.tool.ToolManager;
import com.sldeditor.tool.legend.LegendTool;
import com.sldeditor.tool.raster.RasterTool;
import com.sldeditor.tool.scale.ScaleTool;
import com.sldeditor.tool.vector.VectorTool;
import com.sldeditor.tool.ysld.YSLDTool;

/**
 * Class that makes the underlying file system appear as a file system!
 * 
 * @author Robert Ward (SCISYS)
 */
public class FileSystemInput implements FileSystemInterface
{
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 7208594826312684233L;

    /** The tree model. */
    private DefaultTreeModel treeModel = null;

    /** The file handler map. */
    private Map<String, FileHandlerInterface> fileHandlerMap = new LinkedHashMap<String, FileHandlerInterface>();

    /** The logger. */
    private static Logger logger = Logger.getLogger(FileSystemInput.class);

    /**
     * Instantiates a new file system input.
     *
     * @param toolMgr the tool manager
     */
    public FileSystemInput(ToolSelectionInterface toolMgr)
    {
        List<String> fileHandlerClassList = new ArrayList<String>();
        fileHandlerClassList.add(SLDFileHandler.class.getName());
        fileHandlerClassList.add(SLDEditorFileHandler.class.getName());
        fileHandlerClassList.add(RasterFileHandler.class.getName());
        fileHandlerClassList.add(VectorFileHandler.class.getName());
        fileHandlerClassList.add(YSLDFileHandler.class.getName());

        for(String fileHandlerClass : fileHandlerClassList)
        {
            try {
                FileHandlerInterface fileHandler = (FileHandlerInterface) Class.forName(fileHandlerClass).newInstance();
                for(String fileExtension : fileHandler.getFileExtensionList())
                {
                    fileHandlerMap.put(fileExtension, fileHandler);
                }
                logger.debug("Added FileSystemInput extension : " + fileHandlerClass);

            } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
                ConsoleManager.getInstance().exception(FileSystemInput.class, e);
            }
        }

        FileTreeNode.setFileHandlerMap(fileHandlerMap);

        if(toolMgr != null)
        {
            ToolManager.getInstance().registerTool(FileTreeNode.class, new LegendTool());
            ToolManager.getInstance().registerTool(FileTreeNode.class, new ScaleTool(toolMgr.getApplication()));
            ToolManager.getInstance().registerTool(FileTreeNode.class, new RasterTool(toolMgr.getApplication()));
            ToolManager.getInstance().registerTool(FileTreeNode.class, new VectorTool(toolMgr.getApplication()));
            ToolManager.getInstance().registerTool(FileTreeNode.class, new YSLDTool());
        }
    }

    /* (non-Javadoc)
     * @see com.sldeditor.extension.input.FileSystemInterface#populate(javax.swing.JTree, javax.swing.tree.DefaultTreeModel, javax.swing.tree.DefaultMutableTreeNode)
     */
    @Override
    public void populate(JTree tree, DefaultTreeModel model, DefaultMutableTreeNode rootNode)
    {
        treeModel = model;

        FileTreeNode.setTreeModel(model);
        FileTreeNode.setInputInterface(this);

        try
        {
            if(rootNode != null)
            {
                boolean prePopulateFirstLevelFolders = false;

                for(Path path : FileSystems.getDefault().getRootDirectories())
                {
                    logger.debug("Adding root folder : " + path.toString());

                    FileTreeNode fileSystemRootNode = new FileTreeNode(path);
                    fileSystemRootNode.populateDirectories(prePopulateFirstLevelFolders);

                    rootNode.add(fileSystemRootNode);
                }
            }
        }
        catch (SecurityException e)
        {
            ConsoleManager.getInstance().exception(this, e);
        }
        catch (FileNotFoundException e)
        {
            ConsoleManager.getInstance().exception(this, e);
        }
    }

    /* (non-Javadoc)
     * @see com.sldeditor.extension.input.FileSystemInterface#treeExpanded(java.lang.Object)
     */
    @Override
    public boolean treeExpanded(Object selectedItem)
    {
        boolean changed = false;
        if(selectedItem instanceof FileTreeNode)
        {
            // Get the last component of the path and
            // arrange to have it fully populated.
            FileTreeNode node = (FileTreeNode)selectedItem;
            if (node.populateDirectories(true)) {
                changed = true;
            } 
        }

        return changed;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.extension.input.FileSystemInterface#rightMouseButton(java.lang.Object, java.awt.event.MouseEvent)
     */
    @Override
    public void rightMouseButton(Object selectedItem, MouseEvent e)
    {
        if(selectedItem instanceof FileTreeNode)
        {
            FileTreeNode fileTreeNode = (FileTreeNode)selectedItem;

            File file = fileTreeNode.getFile();

            JPopupMenu popupMenu = new JPopupMenu();

            JMenuItem connectMenuItem = new JMenuItem("Copy path to clipboard");
            connectMenuItem.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent event)
                {
                    StringSelection selection = new StringSelection(file.getAbsolutePath());
                    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                    clipboard.setContents(selection, selection);
                }
            });
            popupMenu.add(connectMenuItem);

            if(e != null)
            {
                popupMenu.show(e.getComponent(), e.getX(), e.getY());
            }
        }
    }

    /**
     * Gets the SLD contents.
     *
     * @param node the node
     * @return the SLD contents
     */
    @Override
    public SelectedFiles getSLDContents(NodeInterface node)
    {
        SelectedFiles selectedFiles = new SelectedFiles();

        for(FileHandlerInterface handler : fileHandlerMap.values())
        {
            List<SLDDataInterface> sldContentList = handler.getSLDContents(node);

            if(sldContentList != null)
            {
                selectedFiles.setSldData(sldContentList);
                selectedFiles.setDataSource(handler.isDataSource());

                if(node instanceof FileTreeNode)
                {
                    FileTreeNode fileTreeNode = (FileTreeNode)node;

                    selectedFiles.setIsFolder(fileTreeNode.isDir());
                }
                return selectedFiles;
            }
        }
        return selectedFiles;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.extension.input.FileSystemInterface#open(java.net.URL)
     */
    @Override
    public List<SLDDataInterface> open(URL url)
    {
        if(url != null)
        {
            File file = new File(url.getFile());
            FileHandlerInterface fileHandler = getFileHandler(file.getAbsolutePath());

            if(fileHandler != null)
            {
                return fileHandler.open(file);
            }
        }
        return null;
    }

    /**
     * Gets the file handler for the given file extension
     *
     * @param filename the filename
     * @return the file handler
     */
    private FileHandlerInterface getFileHandler(String filename)
    {
        String fileExtensionToCheck = DataSourceConnectorFactory.getFileExtension(filename);

        if(fileExtensionToCheck != null)
        {
            for(String fileExtension : fileHandlerMap.keySet())
            {
                if(fileExtension.compareToIgnoreCase(fileExtensionToCheck) == 0)
                {
                    return fileHandlerMap.get(fileExtension);
                }
            }
        }
        return null;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.extension.input.FileSystemInterface#save(com.sldeditor.ui.iface.SLDDataInterface)
     */
    @Override
    public boolean save(SLDDataInterface sldData)
    {
        if(sldData == null)
        {
            return false;
        }

        boolean result1 = false;
        boolean result2 = false;

        // Save SLD file
        String sldFilename = sldData.getSLDFile().getAbsolutePath();

        FileHandlerInterface fileHandler = getFileHandler(sldFilename);

        if(fileHandler != null)
        {
            result1 = fileHandler.save(sldData);

            if(result1)
            {
                ConsoleManager.getInstance().information(this, "SLD file saved : " + sldFilename);
            }
        }

        // Save SLD editor file
        File sldEditorFile = sldData.getSldEditorFile();

        if(sldEditorFile != null)
        {
            fileHandler = getFileHandler(sldEditorFile.getAbsolutePath());

            if(fileHandler != null)
            {
                result2 = fileHandler.save(sldData);

                if(result2)
                {
                    ConsoleManager.getInstance().information(this, "SLD Editor file saved : " + sldEditorFile.getAbsolutePath());
                }
            }
        }

        return result1 || result2;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.extension.input.FileSystemInterface#getNodeTypes()
     */
    @Override
    public List<NodeInterface> getNodeTypes()
    {
        List<NodeInterface> nodeTypeList = new ArrayList<NodeInterface>();

        return nodeTypeList;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.extension.input.FileSystemInterface#drop(com.sldeditor.extension.input.NodeInterface, java.util.Map)
     */
    @Override
    public boolean copyNodes(NodeInterface destinationTreeNode, Map<NodeInterface, List<SLDDataInterface>> copyDataMap)
    {
        if((destinationTreeNode == null) || (copyDataMap == null))
        {
            return false;
        }

        FileTreeNode destinationNode = (FileTreeNode)destinationTreeNode;

        if(!destinationNode.isDir())
        {
            destinationNode = (FileTreeNode)destinationNode.getParent();
        }

        File destinationFolder = destinationNode.getFile();

        for(NodeInterface key : copyDataMap.keySet())
        {
            List<SLDDataInterface> sldDataList = copyDataMap.get(key);

            for(SLDDataInterface sldData : sldDataList)
            {
                String sldFilename = sldData.getSLDFile().getAbsolutePath();

                FileHandlerInterface handler = getFileHandler(sldFilename);

                if(handler != null)
                {
                    String sldName = handler.getSLDName(sldData);

                    File existingFolder = new File(sldFilename).getParentFile();

                    if(existingFolder.equals(destinationFolder))
                    {
                        sldName = "Copy of " + sldName;
                    }

                    File updateFile = new File(destinationFolder, sldName);

                    sldData.setSLDFile(updateFile);

                    save(sldData);
                }
            }

            destinationNode.refreshFolder();

            if(treeModel != null)
            {
                treeModel.reload(destinationNode);
            }
        }

        return true;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.extension.input.FileSystemInterface#deleteNodes(com.sldeditor.extension.input.NodeInterface, java.util.List)
     */
    @Override
    public void deleteNodes(NodeInterface nodeToTransfer, List<SLDDataInterface> sldDataList)
    {
        if(sldDataList == null)
        {
            return;
        }

        if(nodeToTransfer instanceof FileTreeNode)
        {
            for(SLDDataInterface sldData : sldDataList)
            {
                String sldFilename = sldData.getSLDFile().getAbsolutePath();

                File file = new File(sldFilename);

                file.delete();
            }

            DefaultMutableTreeNode destinationNode = (DefaultMutableTreeNode)nodeToTransfer;

            TreeNode parent = destinationNode.getParent();

            destinationNode.removeFromParent();

            if(treeModel != null)
            {
                treeModel.reload(parent);
            }
        }
    }

    /* (non-Javadoc)
     * @see com.sldeditor.extension.input.FileSystemInterface#getDestinationText(com.sldeditor.extension.input.NodeInterface)
     */
    @Override
    public String getDestinationText(NodeInterface destinationTreeNode)
    {
        if(destinationTreeNode != null)
        {
            String text = destinationTreeNode.getDestinationText();

            if(text != null)
            {
                return text;
            }
        }

        return Localisation.getString(FileSystemInput.class, "FileSystemInput.unknown");
    }
}