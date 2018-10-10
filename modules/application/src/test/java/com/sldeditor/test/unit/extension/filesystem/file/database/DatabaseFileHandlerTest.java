/*
 * SLD Editor - The Open Source Java SLD Editor
 *
 * Copyright (C) 2018, SCISYS UK Limited
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

package com.sldeditor.test.unit.extension.filesystem.file.database;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import com.sldeditor.common.data.SLDData;
import com.sldeditor.common.data.StyleWrapper;
import com.sldeditor.datasource.extension.filesystem.node.file.FileTreeNode;
import com.sldeditor.extension.filesystem.file.database.DatabaseFileHandler;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * Unit test for DatbaseFileHandler class.
 *
 * <p>{@link com.sldeditor.extension.filesystem.file.database.DatabaseFileHandler}
 *
 * @author Robert Ward (SCISYS)
 */
public class DatabaseFileHandlerTest {

    /**
     * Test method for {@link
     * com.sldeditor.extension.filesystem.file.database.DatabaseFileHandler#getFileExtensionList()}.
     */
    @Test
    public void testGetFileExtension() {
        List<String> extensionList = Arrays.asList(".geopkg");
        DatabaseFileHandler handler =
                new DatabaseFileHandler("ui/filesystemicons/geopackage.png", extensionList);

        assertEquals(extensionList, handler.getFileExtensionList());
    }

    /**
     * Test method for {@link
     * com.sldeditor.extension.filesystem.file.database.DatabaseFileHandler#populate(com.sldeditor.common.filesystem.FileSystemInterface,
     * javax.swing.tree.DefaultTreeModel,
     * com.sldeditor.datasource.extension.filesystem.node.file.FileTreeNode)}.
     */
    @Test
    public void testPopulate() {
        List<String> extensionList = Arrays.asList(".geopkg");
        DatabaseFileHandler handler =
                new DatabaseFileHandler("ui/filesystemicons/geopackage.png", extensionList);

        assertFalse(handler.populate(null, null, null));

        try {
            assertFalse(handler.populate(null, null, new FileTreeNode(new File(".").toPath())));
        } catch (SecurityException e) {
            e.printStackTrace();
            fail(e.getMessage());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    /**
     * Single file
     *
     * <p>Test method for {@link
     * com.sldeditor.extension.filesystem.file.database.DatabaseFileHandler#getSLDContents(com.sldeditor.common.NodeInterface)}.
     */
    @Test
    public void testGetSLDContentsFile() {
        List<String> extensionList = Arrays.asList(".geopkg");
        DatabaseFileHandler handler =
                new DatabaseFileHandler("ui/filesystemicons/geopackage.png", extensionList);

        assertNull(handler.getSLDContents(null));
    }

    /**
     * Check SLD name
     *
     * <p>Test method for {@link
     * com.sldeditor.extension.filesystem.file.database.DatabaseFileHandler#getSLDName(com.sldeditor.common.NodeInterface)}.
     */
    @Test
    public void testGetSLDName() {
        List<String> extensionList = Arrays.asList(".geopkg");
        DatabaseFileHandler handler =
                new DatabaseFileHandler("ui/filesystemicons/geopackage.png", extensionList);

        assertTrue(handler.getSLDName(null).compareTo("") == 0);

        SLDData sldData = new SLDData(new StyleWrapper("workspace", "layer.sld"), "sldContents");
        String sldName = handler.getSLDName(sldData);
        assertTrue(sldName.compareTo("layer.sld") == 0);
    }

    @Test
    public void test() {
        List<String> extensionList = Arrays.asList(".geopkg");
        DatabaseFileHandler handler =
                new DatabaseFileHandler("ui/filesystemicons/geopackage.png", extensionList);

        assertNotNull(handler.open(null));
        assertFalse(handler.save(null));
        assertTrue(handler.isDataSource());
    }
}
