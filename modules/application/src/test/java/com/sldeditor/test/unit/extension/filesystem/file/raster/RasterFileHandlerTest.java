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

package com.sldeditor.test.unit.extension.filesystem.file.raster;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import com.sldeditor.common.SLDDataInterface;
import com.sldeditor.common.data.SLDData;
import com.sldeditor.common.data.StyleWrapper;
import com.sldeditor.datasource.extension.filesystem.node.file.FileTreeNode;
import com.sldeditor.extension.filesystem.file.raster.RasterFileHandler;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * Unit test for RasterFileHandler class.
 *
 * <p>{@link com.sldeditor.extension.filesystem.file.raster.RasterFileHandler}
 *
 * @author Robert Ward (SCISYS)
 */
public class RasterFileHandlerTest {

    /**
     * Test method for {@link
     * com.sldeditor.extension.filesystem.file.raster.RasterFileHandler#getFileExtensionList()}.
     */
    @Test
    public void testGetFileExtension() {
        assertEquals(
                Arrays.asList("tif", "jpg", "gif", "tiff"),
                new RasterFileHandler().getFileExtensionList());
    }

    /**
     * Test method for {@link
     * com.sldeditor.extension.filesystem.file.raster.RasterFileHandler#isDataSource()}.
     */
    @Test
    public void testIsDataSource() {
        assertTrue(new RasterFileHandler().isDataSource());
    }

    /**
     * Test method for {@link
     * com.sldeditor.extension.filesystem.file.raster.RasterFileHandler#populate(com.sldeditor.common.filesystem.FileSystemInterface,
     * javax.swing.tree.DefaultTreeModel,
     * com.sldeditor.datasource.extension.filesystem.node.file.FileTreeNode)}.
     */
    @Test
    public void testPopulate() {
        assertFalse(new RasterFileHandler().populate(null, null, null));
    }

    /**
     * Single file
     *
     * <p>Test method for {@link
     * com.sldeditor.extension.filesystem.file.raster.RasterFileHandler#getSLDContents(com.sldeditor.common.NodeInterface)}.
     */
    @Test
    public void testGetSLDContentsFile() {
        assertNull(new RasterFileHandler().getSLDContents(null));

        URL url = RasterFileHandlerTest.class.getResource("/point/sld");

        File parent = null;
        try {
            parent = new File(url.toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }

        try {
            FileTreeNode fileTreeNode = new FileTreeNode(parent, "point_attribute.sld");

            RasterFileHandler handler = new RasterFileHandler();

            List<SLDDataInterface> sldDataList = handler.getSLDContents(fileTreeNode);

            assertNull(sldDataList);

            // Try with valid vector file
            FileTreeNode fileTreeNode2 = new FileTreeNode(parent, "point_attribute.tif");

            sldDataList = handler.getSLDContents(fileTreeNode2);

            assertTrue(sldDataList.isEmpty());

            // Changes where the file is to be saved to
            File saveFile = File.createTempFile(getClass().getSimpleName(), ".sld");

            SLDData sldData = new SLDData(null, "");

            sldData.setSLDFile(saveFile);

            assertFalse(handler.save(null));
            assertFalse(handler.save(sldData));

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
     * <p>Test method for {@link
     * com.sldeditor.extension.filesystem.file.raster.RasterFileHandler#getSLDContents(com.sldeditor.common.NodeInterface)}.
     */
    @Test
    public void testGetSLDName() {
        RasterFileHandler handler = new RasterFileHandler();

        assertTrue(handler.getSLDName(null).compareTo("") == 0);

        SLDData sldData = new SLDData(new StyleWrapper("workspace", "layer.sld"), "sldContents");
        String sldName = handler.getSLDName(sldData);
        assertTrue(sldName.compareTo("layer.sld") == 0);
    }
}
