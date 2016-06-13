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

package com.sldeditor.test.unit.datasource.extension.filesystem.node;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import org.junit.Test;

import com.sldeditor.common.NodeInterface;
import com.sldeditor.datasource.extension.filesystem.dataflavour.BuiltInDataFlavour;
import com.sldeditor.datasource.extension.filesystem.dataflavour.DataFlavourManager;
import com.sldeditor.datasource.extension.filesystem.dataflavour.TransferredData;
import com.sldeditor.datasource.extension.filesystem.node.TransferableDataItem;
import com.sldeditor.datasource.extension.filesystem.node.file.FileTreeNode;

/**
 * Unit test for TransferableDataItem class.
 * <p>{@link com.sldeditor.datasource.extension.filesystem.node.TransferableDataItem}
 * 
 * @author Robert Ward (SCISYS)
 *
 */
public class TransferableDataItemTest {

    /**
     * Test method for {@link com.sldeditor.datasource.extension.filesystem.node.TransferableDataItem#TransferableDataItem()}.
     */
    @Test
    public void testTransferableDataItem() {
        Map<NodeInterface, TreePath> selectedData = new HashMap<NodeInterface, TreePath>();

        FileTreeNode fileTreeNode1 = null;
        FileTreeNode fileTreeNode2 = null;
        FileTreeNode fileTreeNode3 = null;
        FileTreeNode fileTreeNode4 = null;
        try {
            fileTreeNode1 = new FileTreeNode(new File(System.getProperty("user.dir")), "file1.txt");
            fileTreeNode2 = new FileTreeNode(new File(System.getProperty("user.dir")), "file2.txt");
            fileTreeNode3 = new FileTreeNode(new File(System.getProperty("user.dir")), "file3.txt");
            fileTreeNode4 = new FileTreeNode(new File(System.getProperty("user.dir")), "file4.txt");
            selectedData.put(fileTreeNode1, new TreePath(new DefaultMutableTreeNode("branch1")));
            selectedData.put(fileTreeNode2, new TreePath(new DefaultMutableTreeNode("branch2")));
            selectedData.put(fileTreeNode3, new TreePath(new DefaultMutableTreeNode("branch3")));
            selectedData.put(fileTreeNode4, new TreePath(new DefaultMutableTreeNode("branch4")));

            TransferableDataItem dataItem = new TransferableDataItem(selectedData);

            DataFlavor dataFlavour = dataItem.getDataFlavour();
            assertEquals(FileTreeNode.class, dataFlavour.getRepresentationClass());

            try {
                Object data = dataItem.getTransferData(DataFlavourManager.FOLDER_DATAITEM_FLAVOR);

                assertTrue(data instanceof TransferredData);
                assertEquals(selectedData.size(), ((TransferredData)data).getDataListSize());

                assertFalse(dataItem.isDataFlavorSupported(BuiltInDataFlavour.GEOSERVER_DATAITEM_FLAVOUR));
                assertTrue(dataItem.isDataFlavorSupported(DataFlavourManager.FOLDER_DATAITEM_FLAVOR));
            } catch (UnsupportedFlavorException e) {
                e.printStackTrace();
                fail(e.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
                fail(e.getMessage());
            }
        } catch (SecurityException e) {
            e.printStackTrace();
            fail(e.getMessage());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    /**
     * Test method for {@link com.sldeditor.datasource.extension.filesystem.node.TransferableDataItem#getTransferDataFlavors()}.
     */
    @SuppressWarnings("deprecation")
    @Test
    public void testGetTransferDataFlavors() {
        assertEquals(DataFlavourManager.getDataFlavourArray(), new TransferableDataItem(null).getTransferDataFlavors());
    }

}
