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
package com.sldeditor.test.unit.extension.filesystem.file.sld;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.sldeditor.common.SLDDataInterface;
import com.sldeditor.common.data.SLDData;
import com.sldeditor.common.data.StyleWrapper;
import com.sldeditor.datasource.extension.filesystem.node.file.FileTreeNode;
import com.sldeditor.extension.filesystem.file.sld.SLDFileHandler;

/**
 * Unit test for SLDFileHandler class.
 * <p>{@link com.sldeditor.extension.filesystem.file.sld.SLDFileHandler}
 * 
 * @author Robert Ward (SCISYS)
 *
 */
public class SLDFileHandlerTest {

    /**
     * Test method for {@link com.sldeditor.extension.filesystem.file.sld.SLDFileHandler#getFileExtensionList()}.
     */
    @Test
    public void testGetFileExtension() {
        assertEquals(Arrays.asList("sld"), new SLDFileHandler().getFileExtensionList());
    }

    /**
     * Test method for {@link com.sldeditor.extension.filesystem.file.sld.SLDFileHandler#populate(com.sldeditor.common.filesystem.FileSystemInterface, javax.swing.tree.DefaultTreeModel, com.sldeditor.datasource.extension.filesystem.node.file.FileTreeNode)}.
     */
    @Test
    public void testPopulate() {
        assertFalse(new SLDFileHandler().populate(null, null, null));
    }

    /**
     * Single file
     * 
     * Test method for {@link com.sldeditor.extension.filesystem.file.sld.SLDFileHandler#getSLDContents(com.sldeditor.common.NodeInterface)}.
     */
    @Test
    public void testGetSLDContentsFile() {
        assertNull(new SLDFileHandler().getSLDContents(null));

        URL url = SLDFileHandlerTest.class.getResource("/point/sld");

        File parent = null;
        try {
            parent = new File(url.toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }

        try {
            FileTreeNode fileTreeNode = new FileTreeNode(parent, "point_attribute.sld");

            SLDFileHandler handler = new SLDFileHandler();

            List<SLDDataInterface> sldDataList = handler.getSLDContents(fileTreeNode);

            assertEquals(1, sldDataList.size());

            // Changes where the file is to be saved to
            File saveFile = File.createTempFile(getClass().getSimpleName(), ".sld");

            SLDData sldData = (SLDData) sldDataList.get(0);

            sldData.setSLDFile(saveFile);

            assertFalse(handler.save(null));
            assertTrue(handler.save(sldData));
            saveFile.delete();
        } catch (SecurityException e) {
            e.printStackTrace();
            fail(e.getMessage());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            fail(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    /**
     * Supply a folder name and retrieve all the sld files in it
     * 
     * Test method for {@link com.sldeditor.extension.filesystem.file.sld.SLDFileHandler#getSLDContents(com.sldeditor.common.NodeInterface)}.
     */
    @Test
    public void testGetSLDContentsFolder() {
        assertNull(new SLDFileHandler().getSLDContents(null));

        URL url = SLDFileHandlerTest.class.getResource("/point/sld");

        String folderName = "";
        File parent = null;
        try {
            parent = new File(url.toURI());
            folderName = parent.getName();
            parent = parent.getParentFile();
        } catch (URISyntaxException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }

        try {
            FileTreeNode fileTreeNode = new FileTreeNode(parent, folderName);

            SLDFileHandler handler = new SLDFileHandler();

            List<SLDDataInterface> sldDataList = handler.getSLDContents(fileTreeNode);

            List<String> expectedLayerNameList = Arrays.asList("point_attribute.sld", "point_pointasgraphic.sld",
                    "point_pointasgraphichttp.sld",
                    "point_pointwithdefaultlabel.sld", "point_pointwithrotatedlabel.sld",
                    "point_pointwithstyledlabel.sld","point_rotatedsquare.sld",
                    "point_simplepoint.sld", "point_simplepointwithstroke.sld",
                    "point_transparenttriangle.sld", "point_zoom.sld", "point_wkt.sld", "point_pointwithenhancedlabel.sld");
            assertEquals(expectedLayerNameList.size(), sldDataList.size());

            for(SLDDataInterface sldData : sldDataList)
            {
                assertTrue(expectedLayerNameList.contains(sldData.getLayerName()));
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
     * Check SLD name
     * 
     * Test method for {@link com.sldeditor.extension.filesystem.file.sld.SLDFileHandler#getSLDContents(com.sldeditor.common.NodeInterface)}.
     */
    @Test
    public void testGetSLDName() {
        SLDFileHandler handler = new SLDFileHandler();

        assertTrue(handler.getSLDName(null).compareTo("") == 0);

        SLDData sldData = new SLDData(new StyleWrapper("workspace", "layer.sld"), "sldContents");
        String sldName = handler.getSLDName(sldData);
        assertTrue(sldName.compareTo("layer.sld") == 0);
    }
}
