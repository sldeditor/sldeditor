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

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.TransferHandler;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import com.sldeditor.common.NodeInterface;
import com.sldeditor.common.console.ConsoleManager;
import com.sldeditor.datasource.extension.filesystem.dataflavour.DataFlavourManager;
import com.sldeditor.datasource.extension.filesystem.dataflavour.TransferredData;

/**
 * The file system tree transfer handler.
 * 
 * @author Robert Ward (SCISYS)
 */
public class TreeTransferHandler extends TransferHandler {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The transferred data item. */
    private TransferableDataItem transferredDataItem;

    /** The is dragging. */
    private boolean isDragging = false;

    /* (non-Javadoc)
     * @see javax.swing.TransferHandler#createTransferable(javax.swing.JComponent)
     */
    @Override
    protected Transferable createTransferable(JComponent c) {
        transferredDataItem = null;

        if(!(c instanceof JTree))
        {
            return null;
        }

        JTree tree = (JTree)c;
        if(tree.isEditing()) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
            TreePath editPath = tree.getEditingPath();
            DefaultMutableTreeNode editNode = (DefaultMutableTreeNode)editPath.getLastPathComponent();
            if(node == editNode)
            {
                tree.stopEditing();
            }
        }

        TreePath[] paths = tree.getSelectionPaths();
        if (paths == null)
        {
            return null;
        }
        else
        {
            Map<NodeInterface, TreePath> map = new LinkedHashMap<NodeInterface, TreePath>();

            for(TreePath path : paths)
            {
                if(path.getLastPathComponent() instanceof NodeInterface)
                {
                    NodeInterface selection = (NodeInterface)path.getLastPathComponent();

                    map.put(selection, path);
                }
            }

            if(map.isEmpty())
            {
                return null;
            }
            transferredDataItem = new TransferableDataItem(map);
            setDragging(true);
            return transferredDataItem;
        }
    }

    /**
     * Sets the is dragging flag
     *
     * @param b the new dragging
     */
    private synchronized void setDragging(boolean b)
    {
        isDragging = b;
    }

    /* (non-Javadoc)
     * @see javax.swing.TransferHandler#exportDone(javax.swing.JComponent, java.awt.datatransfer.Transferable, int)
     */
    @Override
    protected void exportDone(JComponent source, Transferable data, int action) {
        if(!(source instanceof JTree) || (data == null))
        {
            return;
        }

        JTree tree = (JTree)source;
        NodeInterface destinationTreeNode = (NodeInterface)tree.getLastSelectedPathComponent();

        DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
        DataFlavor destDataFlavour = destinationTreeNode.getDataFlavour();

        TransferredData transferredData = null;
        try
        {
            transferredData = (TransferredData)data.getTransferData(destDataFlavour);
        }
        catch (UnsupportedFlavorException e)
        {
            ConsoleManager.getInstance().exception(this, e);
        }
        catch (IOException e)
        {
            ConsoleManager.getInstance().exception(this, e);
        }

        if(action == MOVE) {
            DataFlavourManager.deleteNodes(model, transferredData);
        }

        if(action != NONE) {
            DataFlavourManager.displayMessages(destinationTreeNode, transferredData, action);
        }

        setDragging(false);
    }

    /* (non-Javadoc)
     * @see javax.swing.TransferHandler#getSourceActions(javax.swing.JComponent)
     */
    @Override
    public int getSourceActions(JComponent c) {
        return TransferHandler.COPY;
    }

    /* (non-Javadoc)
     * @see javax.swing.TransferHandler#importData(javax.swing.JComponent, java.awt.datatransfer.Transferable)
     */
    @Override
    public boolean importData(JComponent comp, Transferable t)
    {
        if(!(comp instanceof JTree) || (t == null))
        {
            return false;
        }

        boolean result = false;
        JTree tree = (JTree)comp;
        NodeInterface destinationTreeNode = (NodeInterface)tree.getLastSelectedPathComponent();

        DataFlavor destDataFlavour = destinationTreeNode.getDataFlavour();

        TransferredData transferredData = null;
        try
        {
            transferredData = (TransferredData)t.getTransferData(destDataFlavour);
            result = DataFlavourManager.copy(destinationTreeNode, transferredData);
        }
        catch (UnsupportedFlavorException e)
        {
            ConsoleManager.getInstance().exception(this, e);
        }
        catch (IOException e)
        {
            ConsoleManager.getInstance().exception(this, e);
        }

        return result;
    }

    /**
     * Checks if a dragging operation is currently happening
     *
     * @return true, if is dragging
     */
    public synchronized boolean isDragging()
    {
        return isDragging;
    }

    /* (non-Javadoc)
     * @see javax.swing.TransferHandler#canImport(javax.swing.TransferHandler.TransferSupport)
     */
    @Override
    public boolean canImport(TransferSupport support)
    {
        if(support == null)
        {
            return false;
        }

        JTree.DropLocation dropLocation = (JTree.DropLocation)support.getDropLocation();
        TreePath destinationPath = dropLocation.getPath();

        NodeInterface destinationTreeNode = null;

        try
        {
            destinationTreeNode = (NodeInterface)destinationPath.getLastPathComponent();
        }
        catch(ClassCastException e)
        {
            return false;
        }

        Transferable transferable = support.getTransferable();
        DataFlavor destDataFlavour = destinationTreeNode.getDataFlavour();
        TransferredData transferredData = null;
        try
        {
            transferredData = (TransferredData)transferable.getTransferData(destDataFlavour);
        }
        catch (UnsupportedFlavorException e)
        {
            ConsoleManager.getInstance().exception(this, e);
        }
        catch (IOException e)
        {
            ConsoleManager.getInstance().exception(this, e);
        }

        if(transferredData == null)
        {
            return false;
        }

        TreePath treePath = transferredData.getTreePath(0);
        if(isSameTreePath(treePath, destinationPath))
        {
            return false;
        }

        return DataFlavourManager.isSupported(transferredData.getDataFlavour(0), destDataFlavour);
    }

    /**
     * Checks if is same tree path.
     *
     * @param tp1 the first tree path
     * @param tp2 the second tree path
     * @return true, if is same tree path
     */
    private static boolean isSameTreePath(TreePath tp1, TreePath tp2) {
        if((tp1 == null) || (tp2 == null))
        {
            return false;
        }
        return (tp1.toString().compareTo(tp2.toString()) == 0);
    }
}
