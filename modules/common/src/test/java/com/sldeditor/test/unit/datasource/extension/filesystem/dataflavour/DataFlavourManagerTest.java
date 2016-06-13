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

package com.sldeditor.test.unit.datasource.extension.filesystem.dataflavour;

import static org.junit.Assert.*;

import java.awt.datatransfer.DataFlavor;

import org.junit.Test;

import com.sldeditor.datasource.extension.filesystem.dataflavour.BuiltInDataFlavour;
import com.sldeditor.datasource.extension.filesystem.dataflavour.DataFlavourManager;

/**
 * Unit test for DataFlavourManager class.
 * <p>{@link com.sldeditor.datasource.extension.filesystem.dataflavour.DataFlavourManager}
 * 
 * @author Robert Ward (SCISYS)
 *
 */
public class DataFlavourManagerTest {

    /**
     * Test method for {@link com.sldeditor.datasource.extension.filesystem.dataflavour.DataFlavourManager#getDataFlavourArray()}.
     */
    @Test
    public void testGetDataFlavourArray() {
        DataFlavor[] actualDataFlavour = DataFlavourManager.getDataFlavourArray();

        assertTrue(actualDataFlavour.length > 0);
    }

    /**
     * Test method for {@link com.sldeditor.datasource.extension.filesystem.dataflavour.DataFlavourManager#isSupported(java.awt.datatransfer.DataFlavor, java.awt.datatransfer.DataFlavor)}.
     */
    @Test
    public void testIsSupported() {
        assertTrue(DataFlavourManager.isSupported(BuiltInDataFlavour.FILE_DATAITEM_FLAVOR, DataFlavourManager.FOLDER_DATAITEM_FLAVOR));
        assertFalse(DataFlavourManager.isSupported(DataFlavourManager.FOLDER_DATAITEM_FLAVOR, BuiltInDataFlavour.FILE_DATAITEM_FLAVOR));

        assertTrue(DataFlavourManager.isSupported(BuiltInDataFlavour.FILE_DATAITEM_FLAVOR, DataFlavourManager.GEOSERVER_WORKSPACE_DATAITEM_FLAVOUR));
        assertTrue(DataFlavourManager.isSupported(BuiltInDataFlavour.GEOSERVER_STYLE_DATAITEM_FLAVOUR, DataFlavourManager.GEOSERVER_WORKSPACE_DATAITEM_FLAVOUR));

        assertFalse(DataFlavourManager.isSupported(BuiltInDataFlavour.FILE_DATAITEM_FLAVOR, BuiltInDataFlavour.GEOSERVER_STYLE_DATAITEM_FLAVOUR));
        assertFalse(DataFlavourManager.isSupported(BuiltInDataFlavour.FILE_DATAITEM_FLAVOR, BuiltInDataFlavour.GEOSERVER_OVERALL_DATAITEM_FLAVOUR));
    }

    /**
     * Test method for {@link com.sldeditor.datasource.extension.filesystem.dataflavour.DataFlavourManager#copy(javax.swing.tree.DefaultTreeModel, com.sldeditor.common.NodeInterface, com.sldeditor.datasource.extension.filesystem.dataflavour.TransferredData)}.
     */
    @Test
    public void testCopy() {
        assertFalse(DataFlavourManager.copy(null, null));
    }

    /**
     * Test method for {@link com.sldeditor.datasource.extension.filesystem.dataflavour.DataFlavourManager#deleteNodes(javax.swing.tree.DefaultTreeModel, com.sldeditor.datasource.extension.filesystem.dataflavour.TransferredData)}.
     */
    @Test
    public void testDeleteNodes() {
        DataFlavourManager.deleteNodes(null, null);
    }

    /**
     * Test method for {@link com.sldeditor.datasource.extension.filesystem.dataflavour.DataFlavourManager#displayMessages(com.sldeditor.common.NodeInterface, com.sldeditor.datasource.extension.filesystem.dataflavour.TransferredData, int)}.
     */
    @Test
    public void testDisplayMessages() {
        DataFlavourManager.displayMessages(null, null, 0);
    }

}
