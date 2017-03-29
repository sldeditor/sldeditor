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
import com.sldeditor.common.data.StyleWrapper;
import com.sldeditor.common.filesystem.FileSystemInterface;
import com.sldeditor.datasource.extension.filesystem.dataflavour.BuiltInDataFlavour;
import com.sldeditor.datasource.extension.filesystem.node.geoserver.GeoServerStyleNode;

/**
 * Unit test for GeoServerStyleNode class.
 * 
 * <p>{@link com.sldeditor.datasource.extension.filesystem.node.geoserver.GeoServerStyleNode}
 * @author Robert Ward (SCISYS)
 *
 */
public class GeoServerStyleNodeTest {

    /**
     * Test method for {@link com.sldeditor.datasource.extension.filesystem.node.geoserver.GeoServerStyleNode#GeoServerStyleNode(com.sldeditor.common.filesystem.FileSystemInterface)}.
     */
    @Test
    public void testGeoServerLayerNode() {
        FileSystemInterface fileHandler = new DummyFileSystemInput();

        GeoServerConnection connection = new GeoServerConnection();
        connection.setConnectionName("test connection");
        connection.setUserName("test user name");

        StyleWrapper styleWrapper = new StyleWrapper("workspace", "layer");

        GeoServerStyleNode node = new GeoServerStyleNode(fileHandler, connection, styleWrapper);

        assertEquals(fileHandler, node.getHandler());
        assertEquals(connection, node.getConnectionData());
        assertEquals(styleWrapper, node.getStyle());
        assertEquals(BuiltInDataFlavour.GEOSERVER_STYLE_DATAITEM_FLAVOUR, node.getDataFlavour());

        assertNull(node.getDestinationText());
    }
}
