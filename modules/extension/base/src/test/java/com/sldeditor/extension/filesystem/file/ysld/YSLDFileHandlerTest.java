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
package com.sldeditor.extension.filesystem.file.ysld;

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
import com.sldeditor.datasource.extension.filesystem.node.file.FileTreeNode;

/**
 * Unit test for YSLDFileHandler class.
 * <p>{@link com.sldeditor.extension.filesystem.file.sld.YSLDFileHandler}
 * 
 * @author Robert Ward (SCISYS)
 *
 */
public class YSLDFileHandlerTest {

    /**
     * Test method for {@link com.sldeditor.extension.filesystem.file.sld.SLDFileHandler#getFileExtensionList()}.
     */
    @Test
    public void testGetFileExtension() {
        assertEquals(Arrays.asList("ysld"), new YSLDFileHandler().getFileExtensionList());
    }

    /**
     * Test method for {@link com.sldeditor.extension.filesystem.file.sld.SLDFileHandler#populate(com.sldeditor.common.filesystem.FileSystemInterface, javax.swing.tree.DefaultTreeModel, com.sldeditor.datasource.extension.filesystem.node.file.FileTreeNode)}.
     */
    @Test
    public void testPopulate() {
        assertFalse(new YSLDFileHandler().populate(null, null, null));
    }

    /**
     * Test method for {@link com.sldeditor.extension.filesystem.file.sld.SLDFileHandler#getSLDContents(com.sldeditor.common.NodeInterface)}.
     */
    @Test
    public void testGetSLDContents() {
        assertNull(new YSLDFileHandler().getSLDContents(null));

        URL url = YSLDFileHandlerTest.class.getResource("/ysld");

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

            // Try a folder
            FileTreeNode folderTreeNode = new FileTreeNode(parent.getParentFile(), parent.getName());
            assertEquals(null, handler.getSLDContents(folderTreeNode));

            // Changes where the file is to be saved to
            File saveFile = File.createTempFile("test", ".ysld");
            saveFile.deleteOnExit();

            SLDData sldData = (SLDData) sldDataList.get(0);

            sldData.setSLDFile(saveFile);

            assertFalse(handler.save(null));
            assertTrue(handler.save(sldData));
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

}
