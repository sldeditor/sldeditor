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
package com.sldeditor.datasource.extension.filesystem.node;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import com.sldeditor.common.console.ConsoleManager;
import com.sldeditor.common.utils.ExternalFilenames;
import com.sldeditor.datasource.extension.filesystem.node.file.FileTreeNode;

/**
 * Manages the access between the file system tree and the nodes.
 * 
 * @author Robert Ward (SCISYS)
 */
public class FileSystemNodeManager {

    /** The file system tree. */
    private static FSTree fileSystemTreeComponent;

    /** The tree model. */
    private static DefaultTreeModel treeModel;

    /**
     * Private default constructor
     */
    private FileSystemNodeManager()
    {
    }

    /**
     * Instantiates a new file system node manager.
     *
     * @param tree the tree
     * @param model the model
     */
    public static void create(FSTree tree, DefaultTreeModel model) {
        fileSystemTreeComponent = tree;
        treeModel = model;
    }

    /**
     * Show node in tree.
     *
     * @param url the url
     * @param allowFiles the allow files
     */
    public static void showNodeInTree(URL url, boolean allowFiles)
    {
        getTreePath(url, allowFiles, true);
    }

    /**
     * Gets the tree path for the given folder/file.
     *
     * @param url the url
     * @param allowFiles the allow files
     * @param showInTree the show in tree
     * @return the tree path
     */
    private static DefaultMutableTreeNode getTreePath(URL url, boolean allowFiles, boolean showInTree)
    {
        if(url == null)
        {
            return null;
        }

        String urlFilename = ExternalFilenames.convertURLToFile(url);
        File file = new File(urlFilename);
        File folder = getFolder(file);

        if(folder != null)
        {
            File parent = folder;

            boolean finished = false;

            List<String> folderList = new ArrayList<String>();

            while(!finished)
            {
                if(parent.getParentFile() == null)
                {
                    finished = true;
                }
                else
                {
                    folderList.add(0, parent.getName());
                    parent = parent.getParentFile();
                }
            }

            if(parent != null)
            {
                folderList.add(0, parent.getPath());
                folderList.add(0, "Root");

                DefaultMutableTreeNode node = ((DefaultMutableTreeNode)treeModel.getRoot());

                boolean isRoot = true;

                for(String subFolder : folderList)
                {
                    node = searchNode(isRoot, node, subFolder);

                    if(node == null)
                    {
                        break;
                    }
                    isRoot = false;
                }

                if(node != null)
                {
                    TreeNode[] nodes = treeModel.getPathToRoot(node);
                    TreePath path = new TreePath(nodes);

                    if(showInTree)
                    {
                        fileSystemTreeComponent.scrollPathToVisible(path);
                        fileSystemTreeComponent.expandPath(path);
                    }

                    // Select file
                    if(file.isFile() && allowFiles)
                    {
                        node = searchNode(isRoot, node, file.getName());
                        if(node != null)
                        {
                            nodes = treeModel.getPathToRoot(node);
                            path = new TreePath(nodes);

                            if(showInTree)
                            {
                                fileSystemTreeComponent.setSelectionPath(path);

                                if(!node.isLeaf())
                                {
                                    fileSystemTreeComponent.expandPath(path);
                                }
                            }
                        }
                    }
                    else
                    {
                        if(showInTree)
                        {
                            fileSystemTreeComponent.setSelectionPath(path);
                        }
                    }

                    return node;
                }
            }
            else
            {
                ConsoleManager.getInstance().error(FileSystemNodeManager.class, "Failed to find parent path for folder : " + folder.getAbsolutePath());
            }
        }

        return null;
    }

    /**
     * Workout whether file is a folder or a file.
     *
     * @param file the file or folder
     * @return the folder
     */
    private static File getFolder(File file)
    {
        File folder = null;

        if(file.isFile())
        {
            folder = file.getParentFile();
        }
        else if(file.isDirectory())
        {
            folder = file;
        }
        else
        {
            ConsoleManager.getInstance().error(FileSystemNodeManager.class, "Unknown file : " + file.getAbsolutePath());
        }

        return folder;
    }

    /**
     * Search node.
     *
     * @param isRoot the is root
     * @param rootNode the root node
     * @param nodeStr the node str
     * @return the default mutable tree node
     */
    private static DefaultMutableTreeNode searchNode(boolean isRoot, DefaultMutableTreeNode rootNode, String nodeStr) {
        DefaultMutableTreeNode node = null;
        @SuppressWarnings("rawtypes")
        Enumeration e = rootNode.breadthFirstEnumeration();
        while (e.hasMoreElements()) {
            node = (DefaultMutableTreeNode) e.nextElement();

            if(isRoot || (node != rootNode))
            {
                Object userObject = node.getUserObject();

                if (nodeStr.equals(userObject.toString())) {
                    if(node instanceof FileTreeNode)
                    {
                        if (((FileTreeNode)node).populateDirectories(true)) {
                            ((DefaultTreeModel)fileSystemTreeComponent.getModel()).nodeStructureChanged(node);
                        }
                    }
                    return node;
                }
            }
        }
        return null;
    }

    /**
     * Gets the node.
     *
     * @param file the file to get the node for
     * @return the node
     */
    public static DefaultMutableTreeNode getNode(File file) {
        URL url = null;
        try {
            url = file.toURI().toURL();
        } catch (MalformedURLException e) {
            ConsoleManager.getInstance().exception(FileSystemNodeManager.class, e);
        }
        boolean allowFiles = file.isFile();
        boolean showInTree = false;

        DefaultMutableTreeNode node = getTreePath(url, allowFiles, showInTree);

        return node;
    }

    /**
     * Refresh node.
     *
     * @param parentNode the parent node
     */
    public static void refreshNode(DefaultMutableTreeNode parentNode) {
        if(fileSystemTreeComponent != null)
        {
            ((DefaultTreeModel)fileSystemTreeComponent.getModel()).nodeStructureChanged(parentNode);
        }
    }
}