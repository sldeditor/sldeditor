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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.sldeditor.extension.filesystem.file.esri.MXDInfo;

/**
 * Unit test for MXDInfo class.
 * <p>{@link com.sldeditor.extension.filesystem.file.esri.MXDInfo}
 * 
 * @author Robert Ward (SCISYS)
 *
 */
public class MXDInfoTest {

    /**
     * Test method for {@link com.sldeditor.extension.filesystem.file.esri.MXDInfo#getMxdFilename()}.
     * Test method for {@link com.sldeditor.extension.filesystem.file.esri.MXDInfo#setMxdFilename(java.lang.String)}.
     */
    @Test
    public void testMxdFilename() {
        MXDInfo mxdInfo = new MXDInfo();
        String mxdFilename = "test.mxd";

        mxdInfo.setMxdFilename(mxdFilename);

        assertEquals(mxdFilename, mxdInfo.getMxdFilename());
    }

    /**
     * Test method for {@link com.sldeditor.extension.filesystem.file.esri.MXDInfo#getMxdName()}.
     * Test method for {@link com.sldeditor.extension.filesystem.file.esri.MXDInfo#setMxdName(java.lang.String)}.
     */
    @Test
    public void testMxdName() {
        MXDInfo mxdInfo = new MXDInfo();
        String mxdName = "test mxd";

        mxdInfo.setMxdName(mxdName);

        assertEquals(mxdName, mxdInfo.getMxdName());
    }

    /**
     * Test method for {@link com.sldeditor.extension.filesystem.file.esri.MXDInfo#getLayerList()}.
     * Test method for {@link com.sldeditor.extension.filesystem.file.esri.MXDInfo#setLayerList(java.util.List)}.
     */
    @Test
    public void testLayerList() {
        MXDInfo mxdInfo = new MXDInfo();
        List<String> layerList = new ArrayList<String>();
        layerList.add("Layer 1");
        layerList.add("Layer 2");
        
        mxdInfo.setLayerList(layerList);
        assertEquals(layerList, mxdInfo.getLayerList());
    }

    /**
     * Test method for {@link com.sldeditor.extension.filesystem.file.esri.MXDInfo#getIntermediateFile()}.
     * Test method for {@link com.sldeditor.extension.filesystem.file.esri.MXDInfo#setIntermediateFile(java.io.File)}.
     */
    @Test
    public void testIntermediateFile() {
        MXDInfo mxdInfo = new MXDInfo();
        File intermediateFile = new File("test.file");

        mxdInfo.setIntermediateFile(intermediateFile);

        assertEquals(intermediateFile.getAbsolutePath(), mxdInfo.getIntermediateFile().getAbsolutePath());
    }
}
