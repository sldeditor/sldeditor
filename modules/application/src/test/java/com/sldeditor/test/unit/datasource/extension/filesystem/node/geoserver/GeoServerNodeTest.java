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
package com.sldeditor.test.unit.datasource.extension.filesystem.node.geoserver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.sldeditor.common.data.GeoServerConnection;
import com.sldeditor.common.filesystem.FileSystemInterface;
import com.sldeditor.datasource.extension.filesystem.dataflavour.BuiltInDataFlavour;
import com.sldeditor.datasource.extension.filesystem.node.geoserver.GeoServerNode;

/**
 * Unit test for GeoServerNode class.
 * 
 * <p>{@link com.sldeditor.datasource.extension.filesystem.node.geoserver.GeoServerNode}
 * @author Robert Ward (SCISYS)
 *
 */
public class GeoServerNodeTest {

    /**
     * Test method for {@link com.sldeditor.datasource.extension.filesystem.node.geoserver.GeoServerLayerNode#GeoServerLayerNode(com.sldeditor.common.filesystem.FileSystemInterface, com.sldeditor.common.data.GeoServerLayer)}.
     */
    @Test
    public void testGeoServerLayerNode() {
        FileSystemInterface fileHandler = new DummyFileSystemInput();

        GeoServerConnection connection = new GeoServerConnection();
        connection.setConnectionName("test connection");
        connection.setUserName("test user name");

        GeoServerNode node = new GeoServerNode(fileHandler, connection);
        
        assertEquals(connection, node.getConnection());
        assertEquals(fileHandler, node.getHandler());
        assertEquals(BuiltInDataFlavour.GEOSERVER_DATAITEM_FLAVOUR, node.getDataFlavour());

        assertNull(node.getDestinationText());
    }
}
