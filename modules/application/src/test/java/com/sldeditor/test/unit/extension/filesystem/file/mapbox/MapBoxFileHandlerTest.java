/*
 * SLD Editor - The Open Source Java SLD Editor
 *
 * Copyright (C) 2017, SCISYS UK Limited
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

package com.sldeditor.test.unit.extension.filesystem.file.mapbox;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
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

import org.junit.Ignore;
import org.junit.Test;

import com.sldeditor.common.SLDDataInterface;
import com.sldeditor.common.data.SLDData;
import com.sldeditor.common.data.StyleWrapper;
import com.sldeditor.datasource.extension.filesystem.node.file.FileTreeNode;
import com.sldeditor.extension.filesystem.file.mapbox.MapBoxFileHandler;

/**
 * Unit test for MapBoxFileHandler class.
 * 
 * <p>{@link com.sldeditor.extension.filesystem.file.mapbox.MapBoxFileHandler}
 * 
 * @author Robert Ward (SCISYS)
 *
 */
public class MapBoxFileHandlerTest {

    /**
     * Test method for {@link com.sldeditor.extension.filesystem.file.mapbox.MapBoxFileHandler#getFileExtensionList()}.
     */
    @Test
    public void testGetFileExtensionList() {
        assertEquals(Arrays.asList("json"), new MapBoxFileHandler().getFileExtensionList());
    }

    /**
     * Test method for {@link com.sldeditor.extension.filesystem.file.mapbox.MapBoxFileHandler#populate(com.sldeditor.common.filesystem.FileSystemInterface, javax.swing.tree.DefaultTreeModel, com.sldeditor.datasource.extension.filesystem.node.file.FileTreeNode)}.
     */
    @Test
    public void testPopulate() {
        assertFalse(new MapBoxFileHandler().populate(null, null, null));
    }

    /**
     * Test method for {@link com.sldeditor.extension.filesystem.file.mapbox.MapBoxFileHandler#getSLDContents(com.sldeditor.common.NodeInterface)}.
     */
    @Test
    @Ignore
    public void testGetSLDContents() {
        assertNull(new MapBoxFileHandler().getSLDContents(null));

        URL url = MapBoxFileHandlerTest.class.getResource("/point/mapbox");

        File parent = null;
        try {
            parent = new File(url.toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }

        try {
            FileTreeNode fileTreeNode = new FileTreeNode(parent, "circleStyleTest.json");

            MapBoxFileHandler handler = new MapBoxFileHandler();

            List<SLDDataInterface> sldDataList = handler.getSLDContents(fileTreeNode);

            assertEquals(1, sldDataList.size());

            // Changes where the file is to be saved to
            File saveFile = File.createTempFile(getClass().getSimpleName(), ".json");

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
     * Test method for {@link com.sldeditor.extension.filesystem.file.mapbox.MapBoxFileHandler#open(java.io.File)}.
     */
    @Test
    public void testOpen() {
        // Do nothing
    }

    /**
     * Test method for {@link com.sldeditor.extension.filesystem.file.mapbox.MapBoxFileHandler#save(com.sldeditor.common.SLDDataInterface)}.
     */
    @Test
    public void testSave() {
        // Do nothing
    }

    /**
     * Test method for {@link com.sldeditor.extension.filesystem.file.mapbox.MapBoxFileHandler#getSLDName(com.sldeditor.common.SLDDataInterface)}.
     */
    @Test
    public void testGetSLDName() {
        MapBoxFileHandler handler = new MapBoxFileHandler();

        assertTrue(handler.getSLDName(null).compareTo("") == 0);

        SLDData sldData = new SLDData(new StyleWrapper("workspace", "layer.sld"), "sldContents");
        String sldName = handler.getSLDName(sldData);
        assertTrue(sldName.compareTo("layer.json") == 0);
    }

    /**
     * Test method for {@link com.sldeditor.extension.filesystem.file.mapbox.MapBoxFileHandler#isDataSource()}.
     */
    @Test
    public void testIsDataSource() {
        assertFalse(new MapBoxFileHandler().isDataSource());
    }

    /**
     * Test method for {@link com.sldeditor.extension.filesystem.file.mapbox.MapBoxFileHandler#getIcon(java.lang.String, java.lang.String)}.
     */
    @Test
    public void testGetIcon() {
        assertNotNull(new MapBoxFileHandler().getIcon(null, null));
        assertNotNull(new MapBoxFileHandler().getIcon(null, null));
    }

}
