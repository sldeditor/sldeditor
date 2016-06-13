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
package com.sldeditor.test.unit.extension.filesystem.file.esri;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.sldeditor.common.filesystem.FileSystemInterface;
import com.sldeditor.extension.filesystem.file.FileSystemInput;
import com.sldeditor.extension.filesystem.file.esri.EsriDataFlavour;
import com.sldeditor.extension.filesystem.file.esri.MXDInfo;
import com.sldeditor.extension.filesystem.file.esri.MXDLayerNode;

/**
 * Unit test for MXDLayerNode class.
 * <p>{@link com.sldeditor.extension.filesystem.file.esri.MXDLayerNode}
 * 
 * @author Robert Ward (SCISYS)
 *
 */
public class MXDLayerNodeTest {

    /**
     * Test method for {@link com.sldeditor.extension.filesystem.file.esri.MXDLayerNode#MXDLayerNode(com.sldeditor.common.filesystem.FileSystemInterface, com.sldeditor.extension.filesystem.file.esri.MXDInfo, java.lang.String)}.
     * Test method for {@link com.sldeditor.extension.filesystem.file.esri.MXDLayerNode#getMXDInfo()}.
     * Test method for {@link com.sldeditor.extension.filesystem.file.esri.MXDLayerNode#getLayerName()}.
     * Test method for {@link com.sldeditor.extension.filesystem.file.esri.MXDLayerNode#getHandler()}.
     * Test method for {@link com.sldeditor.extension.filesystem.file.esri.MXDLayerNode#getDataFlavour()}.
     * Test method for {@link com.sldeditor.extension.filesystem.file.esri.MXDLayerNode#getDestinationText()}.
     */
    @Test
    public void testMXDLayerNode() {
        FileSystemInterface handler = new FileSystemInput(null);
        String layerName = "layer name";
        MXDInfo mxdInfo = new MXDInfo();
        
        MXDLayerNode layerNode = new MXDLayerNode(handler, mxdInfo, layerName);
        
        assertEquals(handler, layerNode.getHandler());
        assertEquals(layerName, layerNode.getLayerName());
        assertEquals(mxdInfo, layerNode.getMXDInfo());
        assertEquals(EsriDataFlavour.MXDLAYER_NODE_DATAITEM_FLAVOUR, layerNode.getDataFlavour());
        assertEquals(layerName, layerNode.getDestinationText());
    }
}
