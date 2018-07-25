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

package com.sldeditor.test.unit.extension.filesystem.file.ysld;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import com.google.common.io.Files;
import com.sldeditor.common.SLDDataInterface;
import com.sldeditor.common.data.SLDData;
import com.sldeditor.common.data.StyleWrapper;
import com.sldeditor.datasource.extension.filesystem.node.file.FileTreeNode;
import com.sldeditor.extension.filesystem.file.ysld.YSLDFileHandler;
import com.sldeditor.test.unit.extension.filesystem.file.sld.SLDFileHandlerTest;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import javax.swing.Icon;
import org.junit.jupiter.api.Test;

/**
 * Unit test for YSLDFileHandler class.
 *
 * <p>{@link com.sldeditor.extension.filesystem.file.sld.YSLDFileHandler}
 *
 * @author Robert Ward (SCISYS)
 */
public class YSLDFileHandlerTest {

    /**
     * Test method for {@link
     * com.sldeditor.extension.filesystem.file.ysld.YSLDFileHandler#getFileExtensionList()}.
     */
    @Test
    public void testGetFileExtension() {
        assertEquals(Arrays.asList("ysld"), new YSLDFileHandler().getFileExtensionList());
    }

    /**
     * Test method for {@link
     * com.sldeditor.extension.filesystem.file.ysld.YSLDFileHandler#populate(com.sldeditor.common.filesystem.FileSystemInterface,
     * javax.swing.tree.DefaultTreeModel,
     * com.sldeditor.datasource.extension.filesystem.node.file.FileTreeNode)}.
     */
    @Test
    public void testPopulate() {
        assertFalse(new YSLDFileHandler().populate(null, null, null));
    }

    /**
     * Single file Test method for {@link
     * com.sldeditor.extension.filesystem.file.ysld.YSLDFileHandler#getSLDContents(com.sldeditor.common.NodeInterface)}.
     */
    @Test
    public void testGetSLDContentsFile() {
        assertNull(new YSLDFileHandler().getSLDContents(null));
        assertNull(new YSLDFileHandler().open(null));
        File tempFolder = Files.createTempDir();
        assertNull(new YSLDFileHandler().open(tempFolder));
        tempFolder.delete();

        URL url = YSLDFileHandlerTest.class.getResource("/point/ysld");

        File parent = null;
        try {
            parent = new File(url.toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }

        try {
            FileTreeNode fileTreeNode = new FileTreeNode(parent, "point_simplepoint.ysld");

            YSLDFileHandler handler = new YSLDFileHandler();

            List<SLDDataInterface> sldDataList = handler.getSLDContents(fileTreeNode);

            assertEquals(1, sldDataList.size());

            // Changes where the file is to be saved to
            File saveFile = File.createTempFile(getClass().getSimpleName(), ".ysld");

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
     * Is data source Test method for {@link
     * com.sldeditor.extension.filesystem.file.ysld.YSLDFileHandler#isDataSource()}.
     */
    @Test
    public void testIsDataSource() {
        assertFalse(new YSLDFileHandler().isDataSource());
    }

    /**
     * Check SLD name
     *
     * <p>Test method for {@link
     * com.sldeditor.extension.filesystem.file.sld.SLDFileHandler#getSLDContents(com.sldeditor.common.NodeInterface)}.
     */
    @Test
    public void testGetSLDName() {
        YSLDFileHandler handler = new YSLDFileHandler();

        assertTrue(handler.getSLDName(null).compareTo("") == 0);

        SLDData sldData = new SLDData(new StyleWrapper("workspace", "layer.ysld"), "sldContents");
        String sldName = handler.getSLDName(sldData);
        assertTrue(sldName.compareTo("layer.ysld") == 0);
    }

    /**
     * Supply a folder name and retrieve all the ysld files in it
     *
     * <p>Test method for {@link
     * com.sldeditor.extension.filesystem.file.ysld.YSLDFileHandler#getSLDContents(com.sldeditor.common.NodeInterface)}.
     */
    @Test
    public void testGetSLDContentsFolder() {
        assertNull(new YSLDFileHandler().getSLDContents(null));

        URL url = SLDFileHandlerTest.class.getResource("/point/ysld");

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

            YSLDFileHandler handler = new YSLDFileHandler();

            List<SLDDataInterface> sldDataList = handler.getSLDContents(fileTreeNode);

            List<String> expectedLayerNameList = Arrays.asList("point_simplepoint.ysld");
            assertEquals(expectedLayerNameList.size(), sldDataList.size());

            for (SLDDataInterface sldData : sldDataList) {
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

    @Test
    public void testGetIcon() {
        YSLDFileHandler handler = new YSLDFileHandler();

        Icon actualValue = handler.getIcon(null, null);

        assertNotNull(actualValue);

        Icon actualValue2 = handler.getIcon(null, null);

        assertEquals(actualValue, actualValue2);
    }
}
