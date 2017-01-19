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
package com.sldeditor.test.unit.extension.filesystem;

import static org.junit.Assert.assertEquals;

import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JPopupMenu;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.junit.Test;

import com.sldeditor.common.NodeInterface;
import com.sldeditor.common.SLDDataInterface;
import com.sldeditor.common.filesystem.FileSystemInterface;
import com.sldeditor.common.filesystem.SelectedFiles;
import com.sldeditor.datasource.extension.filesystem.node.FSTree;
import com.sldeditor.extension.filesystem.FileSystemExtensionFactory;

/**
 * Unit test for FileSystemExtensionFactory class.
 * <p>{@link com.sldeditor.extension.filesystem.FileSystemExtensionFactory}
 * 
 * @author Robert Ward (SCISYS)
 *
 */
public class FileSystemExtensionFactoryTest {

    /**
     * The Class DummyExtension.
     */
    class DummyExtension implements FileSystemInterface
    {

        /** The Constant serialVersionUID. */
        private static final long serialVersionUID = 1L;

        /**
         * Populate.
         *
         * @param tree the tree
         * @param model the model
         * @param rootNode the root node
         */
        @Override
        public void populate(FSTree tree, DefaultTreeModel model, DefaultMutableTreeNode rootNode) {
        }

        /**
         * Tree expanded.
         *
         * @param selectedItem the selected item
         * @return true, if successful
         */
        @Override
        public boolean treeExpanded(Object selectedItem) {
            return false;
        }

        /**
         * Right mouse button.
         *
         * @param popupMenu the popup menu
         * @param selectedItem the selected item
         * @param e the e
         */
        @Override
        public void rightMouseButton(JPopupMenu popupMenu, Object selectedItem, MouseEvent e) {
        }

        /**
         * Gets the SLD contents.
         *
         * @param node the node
         * @return the SLD contents
         */
        @Override
        public SelectedFiles getSLDContents(NodeInterface node) {
            return null;
        }

        /**
         * Open.
         *
         * @param url the url
         * @return the list
         */
        @Override
        public List<SLDDataInterface> open(URL url) {
            if(url.toExternalForm().endsWith(".tst"))
            {
                List<SLDDataInterface> list = new ArrayList<SLDDataInterface>();
                return list;
            }
            return null;
        }

        /**
         * Save.
         *
         * @param sldData the sld data
         * @return true, if successful
         */
        @Override
        public boolean save(SLDDataInterface sldData) {
            return (sldData != null);
        }

        /**
         * Gets the node types.
         *
         * @return the node types
         */
        @Override
        public List<NodeInterface> getNodeTypes() {
            return null;
        }

        /**
         * Copy nodes.
         *
         * @param destinationTreeNode the destination tree node
         * @param droppedDataMap the dropped data map
         * @return true, if successful
         */
        @Override
        public boolean copyNodes(NodeInterface destinationTreeNode,
                Map<NodeInterface, List<SLDDataInterface>> droppedDataMap) {
            return false;
        }

        /**
         * Delete nodes.
         *
         * @param nodeToTransfer the node to transfer
         * @param sldDataList the sld data list
         */
        @Override
        public void deleteNodes(NodeInterface nodeToTransfer, List<SLDDataInterface> sldDataList) {
        }

        /**
         * Gets the destination text.
         *
         * @param destinationTreeNode the destination tree node
         * @return the destination text
         */
        @Override
        public String getDestinationText(NodeInterface destinationTreeNode) {
            return null;
        }
    }

    /**
     * Test method for {@link com.sldeditor.extension.filesystem.FileSystemExtensionFactory#override(java.util.List)}.
     * Test method for {@link com.sldeditor.extension.filesystem.FileSystemExtensionFactory#getFileExtensionList(com.sldeditor.common.ToolSelectionInterface)}.
     */
    @Test
    public void testGetFileExtensionList() {
        // Put test class back into empty state
        FileSystemExtensionFactory.override(null);

        // Should return the default
        List<FileSystemInterface> fileExtensionList = FileSystemExtensionFactory.getFileExtensionList(null);
        assertEquals(2, fileExtensionList.size());

        List<FileSystemInterface> extList = new ArrayList<FileSystemInterface>();
        extList.add(new DummyExtension());
        FileSystemExtensionFactory.override(extList);
        // Should return the dummy extension
        fileExtensionList = FileSystemExtensionFactory.getFileExtensionList(null);
        assertEquals(1, fileExtensionList.size());

        extList.clear();

        FileSystemExtensionFactory.override(extList);

        // Should return the defaults
        fileExtensionList = FileSystemExtensionFactory.getFileExtensionList(null);
        assertEquals(2, fileExtensionList.size());
    }

}
