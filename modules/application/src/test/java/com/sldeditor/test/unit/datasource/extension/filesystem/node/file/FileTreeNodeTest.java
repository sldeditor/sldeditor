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

package com.sldeditor.test.unit.datasource.extension.filesystem.node.file;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import com.sldeditor.datasource.extension.filesystem.dataflavour.DataFlavourManager;
import com.sldeditor.datasource.extension.filesystem.node.file.FileHandlerInterface;
import com.sldeditor.datasource.extension.filesystem.node.file.FileTreeNode;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * Unit test for FileTreeNode class.
 *
 * <p>{@link com.sldeditor.datasource.extension.filesystem.node.file.FileTreeNode}
 *
 * @author Robert Ward (SCISYS)
 */
public class FileTreeNodeTest {

    /** Test. */
    @Disabled
    @Test
    public void test() {
        Map<String, FileHandlerInterface> fileHandlerMap =
                new LinkedHashMap<String, FileHandlerInterface>();

        FileHandlerInterface fileHandler = new DummyFileHandler();
        for (String fileExtension : fileHandler.getFileExtensionList()) {
            fileHandlerMap.put(fileExtension, fileHandler);
        }

        Path tmpFolder = null;
        try {
            tmpFolder = Files.createTempDirectory(getClass().getSimpleName());
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }

        File folder = tmpFolder.toFile();

        File parent = folder.getParentFile();
        String name = folder.getName();

        try {
            FileTreeNode.setFileHandlerMap(fileHandlerMap);
            FileTreeNode node = new FileTreeNode(parent, name);

            assertTrue(node.isDir());
            assertTrue(node.getAllowsChildren());
            assertFalse(node.isLeaf());
            assertNull(node.getHandler());
            assertEquals(name, node.getName());
            assertEquals(DataFlavourManager.FOLDER_DATAITEM_FLAVOR, node.getDataFlavour());

            // CHECKSTYLE:OFF
            File tmpFile1 = File.createTempFile("sldeditor", ".abc", folder);
            // CHECKSTYLE:ON

            Thread.sleep(1000);

            // Has a file extension not expecting
            assertEquals(0, node.getChildCount());

            File tmpFile2 = File.createTempFile("sldeditor", ".test", folder);

            Thread.sleep(10000);
            assertEquals(1, node.getChildCount());

            tmpFile2.delete();

            Thread.sleep(10000);
            assertEquals(0, node.getChildCount());

            assertEquals(folder.getAbsolutePath(), node.getDestinationText());
            tmpFile1.delete();
            folder.delete();
        } catch (SecurityException e) {
            e.printStackTrace();
            fail(e.getMessage());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            fail(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.getMessage());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
