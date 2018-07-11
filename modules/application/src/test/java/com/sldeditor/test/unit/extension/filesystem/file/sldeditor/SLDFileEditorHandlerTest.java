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

package com.sldeditor.test.unit.extension.filesystem.file.sldeditor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import com.sldeditor.common.SLDDataInterface;
import com.sldeditor.common.data.SLDData;
import com.sldeditor.datasource.extension.filesystem.node.file.FileTreeNode;
import com.sldeditor.extension.filesystem.file.sld.SLDFileHandler;
import com.sldeditor.extension.filesystem.file.sldeditor.SLDEditorFileHandler;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * Unit test for SLDEditorFileHandler class.
 *
 * <p>{@link com.sldeditor.extension.filesystem.file.sldeditor.SLDEditorFileHandler}
 *
 * @author Robert Ward (SCISYS)
 */
public class SLDFileEditorHandlerTest {

    /**
     * Test method for {@link
     * com.sldeditor.extension.filesystem.file.sldeditor.SLDEditorFileHandler#getFileExtensionList()}.
     */
    @Test
    public void testGetFileExtension() {
        assertEquals(Arrays.asList("sldeditor"), new SLDEditorFileHandler().getFileExtensionList());
    }

    /**
     * Test method for {@link
     * com.sldeditor.extension.filesystem.file.sldeditor.SLDEditorFileHandler#populate(com.sldeditor.common.filesystem.FileSystemInterface,
     * javax.swing.tree.DefaultTreeModel,
     * com.sldeditor.datasource.extension.filesystem.node.file.FileTreeNode)}.
     */
    @Test
    public void testPopulate() {
        assertFalse(new SLDEditorFileHandler().populate(null, null, null));
    }

    /**
     * Test method for {@link
     * com.sldeditor.extension.filesystem.file.sldeditor.SLDEditorFileHandler#getSLDContents(com.sldeditor.common.NodeInterface)}.
     */
    @Test
    public void testGetSLDContents() {
        assertNull(new SLDEditorFileHandler().getSLDContents(null));

        URL url = SLDFileEditorHandlerTest.class.getResource("/point/sld");

        File parent = null;
        try {
            parent = new File(url.toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }

        System.out.println(url.toString());
        try {
            FileTreeNode fileTreeNode = new FileTreeNode(parent, "point_attribute.sld");

            SLDFileHandler handler = new SLDFileHandler();

            List<SLDDataInterface> sldDataList = handler.getSLDContents(fileTreeNode);

            assertEquals(1, sldDataList.size());

            // Changes where the file is to be saved to
            File sldEditorFile = File.createTempFile(getClass().getSimpleName(), ".sldeditor");

            SLDData sldData = (SLDData) sldDataList.get(0);

            sldData.setSldEditorFile(sldEditorFile);

            SLDEditorFileHandler editorFileHandler = new SLDEditorFileHandler();

            assertFalse(handler.save(null));
            assertTrue(editorFileHandler.save(sldData));

            SLDEditorFileHandler editorFileHandler2 = new SLDEditorFileHandler();
            List<SLDDataInterface> actualSldDataList = editorFileHandler2.open(sldEditorFile);

            assertEquals(1, actualSldDataList.size());

            FileTreeNode editorFileTreeNode =
                    new FileTreeNode(sldEditorFile.getParentFile(), sldEditorFile.getName());
            List<SLDDataInterface> actualSldDataList2 =
                    editorFileHandler2.getSLDContents(editorFileTreeNode);
            assertEquals(1, actualSldDataList2.size());

            sldEditorFile.delete();
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
