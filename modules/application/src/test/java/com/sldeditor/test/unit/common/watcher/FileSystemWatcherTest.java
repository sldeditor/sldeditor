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

package com.sldeditor.test.unit.common.watcher;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import com.sldeditor.common.watcher.FileSystemWatcher;
import com.sldeditor.common.watcher.FileWatcherUpdateInterface;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * Unit test for FileSystemWatcher.
 *
 * <p>{@link com.sldeditor.common.watcher.FileSystemWatcher}
 *
 * @author Robert Ward (SCISYS)
 */
public class FileSystemWatcherTest {

    /**
     * Test method for {@link
     * com.sldeditor.common.watcher.FileSystemWatcher#addWatch(com.sldeditor.common.watcher.FileWatcherUpdateInterface,
     * java.io.File)}.
     */
    @Test
    @Disabled
    public void testAddWatch() {

        Path tempFolder = null;
        try {
            tempFolder = Files.createTempDirectory(getClass().getSimpleName());
            System.out.println("Created temp test folder : " + tempFolder);
        } catch (IOException e) {
            e.printStackTrace();
            fail("Failed to create temp folder in temp folder!");
        }

        List<String> resultList = new ArrayList<String>();

        FileSystemWatcher.getInstance()
                .addWatch(
                        new FileWatcherUpdateInterface() {

                            @Override
                            public void fileAdded(Path f) {
                                String message = "Added " + f.toString();
                                resultList.add(message);
                                System.out.println(message);
                            }

                            @Override
                            public void fileModified(Path f) {
                                // Ignore
                            }

                            @Override
                            public void fileDeleted(Path f) {
                                String message = "Del " + f.toString();
                                resultList.add(message);
                                System.out.println(message);
                            }
                        },
                        tempFolder);

        File tmpFile = null;
        try {
            tmpFile = File.createTempFile(getClass().getSimpleName(), ".tmp", tempFolder.toFile());
            System.out.println("Created temp test file : " + tmpFile);
        } catch (IOException e) {
            e.printStackTrace();
            fail("Failed to create temp file in temp folder");
        }
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String msg = "hello";
        try {
            Files.write(Paths.get(tmpFile.toURI()), msg.getBytes());
            System.out.println("Modified temp test file : " + tmpFile);
        } catch (IOException e) {
            e.printStackTrace();
            fail("Failed to modify temp file");
        }
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        tmpFile.delete();
        System.out.println("Deleted temp test file : " + tmpFile);

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertTrue(resultList.size() >= 2);

        String resultingTempFilename = tmpFile.getName();

        assertEquals("Added " + resultingTempFilename, resultList.get(0));
        assertEquals("Del " + resultingTempFilename, resultList.get(resultList.size() - 1));

        try {
            FileUtils.deleteDirectory(tempFolder.toFile());
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }
}
