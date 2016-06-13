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

package com.sldeditor.test.unit.common.data;

import static org.junit.Assert.*;

import org.junit.Test;

import com.sldeditor.common.data.GeoServerConnection;
import com.sldeditor.common.data.GeoServerLayer;
import com.sldeditor.common.data.StyleWrapper;

/**
 * The unit test for GeoServerLayer.
 * <p>{@link com.sldeditor.common.data.GeoServerLayer}
 *
 * @author Robert Ward (SCISYS)
 */
public class GeoServerLayerTest {

    /**
     * Test method for {@link com.sldeditor.common.data.GeoServerLayer#getLayerWorkspace()}.
     * Test method for {@link com.sldeditor.common.data.GeoServerLayer#setLayerWorkspace(java.lang.String)}.
     */
    @Test
    public void testGetLayerWorkspace() {
        String layerWorkspace = "test workspace";

        GeoServerLayer layer = new GeoServerLayer();
        layer.setLayerWorkspace(layerWorkspace);

        assertEquals(layer.getLayerWorkspace(), layerWorkspace);
    }

    /**
     * Test method for {@link com.sldeditor.common.data.GeoServerLayer#getLayerName()}.
     * Test method for {@link com.sldeditor.common.data.GeoServerLayer#setLayerName(java.lang.String)}.
     */
    @Test
    public void testGetLayerName() {
        String layerName = "test layer";

        GeoServerLayer layer = new GeoServerLayer();
        layer.setLayerName(layerName);

        assertEquals(layer.getLayerName(), layerName);
    }

    /**
     * Test method for {@link com.sldeditor.common.data.GeoServerLayer#getStyle()}.
     * Test method for {@link com.sldeditor.common.data.GeoServerLayer#setStyle(com.sldeditor.common.data.StyleWrapper)}.
     */
    @Test
    public void testGetStyle() {
        StyleWrapper styleWrapper = new StyleWrapper();
        styleWrapper.setStyle("style");
        styleWrapper.setStyle("workspace");
        GeoServerLayer layer = new GeoServerLayer();
        layer.setStyle(styleWrapper);

        assertEquals(styleWrapper, layer.getStyle());
    }

    /**
     * Test method for {@link com.sldeditor.common.data.GeoServerLayer#getStyleString()}.
     * Test method for {@link com.sldeditor.common.data.GeoServerLayer#setDefaultWorkspaceName(java.lang.String)}.
     */
    @Test
    public void testGetStyleString() {

        StyleWrapper styleWrapper = new StyleWrapper();
        String style = "style";
        styleWrapper.setStyle(style);
        String workspace = "workspace";
        styleWrapper.setWorkspace(workspace);
        GeoServerLayer layer = new GeoServerLayer();
        layer.setStyle(styleWrapper);

        String styleString = layer.getStyleString();

        assertEquals(styleString, style);

        String defaultWorkspaceName = "Default workspace";
        styleWrapper.setWorkspace(defaultWorkspaceName);
        layer.setStyle(styleWrapper);

        GeoServerLayer.setDefaultWorkspaceName(defaultWorkspaceName);
        String styleString2 = layer.getStyleString();
        assertEquals(styleString2, style);

        styleWrapper.setWorkspace(workspace);
        layer.setStyle(styleWrapper);

        String styleString3 = layer.getStyleString();
        assertEquals(styleString3.compareTo(workspace + ":" + style), 0);
    }

    /**
     * Test method for {@link com.sldeditor.common.data.GeoServerLayer#getConnection()}.
     * Test method for {@link com.sldeditor.common.data.GeoServerLayer#setConnection(com.sldeditor.common.data.GeoServerConnection)}.
     */
    @Test
    public void testGetConnection() {
        GeoServerConnection connection = new GeoServerConnection();
        GeoServerLayer layer = new GeoServerLayer();
        layer.setConnection(connection);

        assertEquals(connection, layer.getConnection());
    }

}
