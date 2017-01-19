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
package com.sldeditor.common.filesystem;

import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.net.URL;
import java.util.List;
import java.util.Map;

import javax.swing.JPopupMenu;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import com.sldeditor.common.NodeInterface;
import com.sldeditor.common.SLDDataInterface;
import com.sldeditor.datasource.extension.filesystem.node.FSTree;

/**
 * The Interface FileSystemInterface is implemented by nodes that
 * are displayed in the file system tree.
 * 
 * @author Robert Ward (SCISYS)
 */
public interface FileSystemInterface extends Serializable
{

    /**
     * Populate the tree.
     *
     * @param tree the tree
     * @param model the model
     * @param rootNode the root node
     */
    void populate(FSTree tree, DefaultTreeModel model, DefaultMutableTreeNode rootNode);

    /**
     * Tree expanded.
     *
     * @param selectedItem the selected item
     * @return true, if successful
     */
    boolean treeExpanded(Object selectedItem);

    /**
     * Right mouse button pressed.
     *
     * @param popupMenu the popup menu
     * @param selectedItem the selected item
     * @param e the e
     */
    void rightMouseButton(JPopupMenu popupMenu, Object selectedItem, MouseEvent e);

    /**
     * Gets the SLD contents.
     *
     * @param node the node
     * @return the SLD contents
     */
    SelectedFiles getSLDContents(NodeInterface node);

    /**
     * Open url
     *
     * @param url the url
     * @return the list
     */
    List<SLDDataInterface> open(URL url);

    /**
     * Save url.
     *
     * @param sldData the sld data
     * @return true, if successful
     */
    boolean save(SLDDataInterface sldData);

    /**
     * Gets the node types list
     *
     * @return the node types
     */
    List<NodeInterface> getNodeTypes();

    /**
     * Called when the data has been dropped.
     *
     * @param destinationTreeNode the destination tree node
     * @param droppedDataMap the dropped data map
     * @return true, if successful
     */
    boolean copyNodes(NodeInterface destinationTreeNode, Map<NodeInterface, List<SLDDataInterface>> droppedDataMap);

    /**
     * Delete nodes.
     *
     * @param nodeToTransfer the node to transfer
     * @param sldDataList the sld data list
     */
    void deleteNodes(NodeInterface nodeToTransfer, List<SLDDataInterface> sldDataList);

    /**
     * Gets the text representation of the destination node
     *
     * @param destinationTreeNode the destination tree node
     * @return the destination text
     */
    String getDestinationText(NodeInterface destinationTreeNode);

}
