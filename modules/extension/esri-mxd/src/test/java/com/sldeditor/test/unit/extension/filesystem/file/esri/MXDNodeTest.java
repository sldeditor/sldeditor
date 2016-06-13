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
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.sldeditor.common.filesystem.FileSystemInterface;
import com.sldeditor.extension.filesystem.file.FileSystemInput;
import com.sldeditor.extension.filesystem.file.esri.EsriDataFlavour;
import com.sldeditor.extension.filesystem.file.esri.MXDInfo;
import com.sldeditor.extension.filesystem.file.esri.MXDNode;

/**
 * Unit test for MXDNode class.
 * <p>{@link com.sldeditor.extension.filesystem.file.esri.MXDLayerNode}
 * 
 * @author Robert Ward (SCISYS)
 *
 */
public class MXDNodeTest {

    /**
     * Test method for {@link com.sldeditor.extension.filesystem.file.esri.MXDNode#MXDNode(com.sldeditor.common.filesystem.FileSystemInterface, com.sldeditor.extension.filesystem.file.esri.MXDInfo)}.
     * Test method for {@link com.sldeditor.extension.filesystem.file.esri.MXDNode#getMXDInfo()}.
     * Test method for {@link com.sldeditor.extension.filesystem.file.esri.MXDNode#getHandler()}.
     * Test method for {@link com.sldeditor.extension.filesystem.file.esri.MXDNode#getDataFlavour()}.
     * Test method for {@link com.sldeditor.extension.filesystem.file.esri.MXDNode#getDestinationText()}.
     */
    @Test
    public void testMXDNode() {
        FileSystemInterface handler = new FileSystemInput(null);
        MXDInfo mxdInfo = new MXDInfo();
        
        MXDNode layerNode = new MXDNode(handler, mxdInfo);
        
        assertEquals(handler, layerNode.getHandler());
        assertEquals(mxdInfo, layerNode.getMXDInfo());
        assertEquals(EsriDataFlavour.MXD_NODE_DATAITEM_FLAVOUR, layerNode.getDataFlavour());
        assertNull(layerNode.getDestinationText());
    }
}
