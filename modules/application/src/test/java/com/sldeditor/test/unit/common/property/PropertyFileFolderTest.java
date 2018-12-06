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

package com.sldeditor.test.unit.common.property;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import com.sldeditor.common.property.PropertyFileFolder;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import org.junit.jupiter.api.Test;

/**
 * Unit test for PropertyFileFolder.
 *
 * <p>{@link com.sldeditor.common.property.PropertyFileFolder}
 *
 * @author Robert Ward (SCISYS)
 */
class PropertyFileFolderTest {

    private final String testString = "#SLD Editor configuration data";
    private final String testString2 = "#Not the same file";

    private final String oldConfigFilename = "config.properties";

    private final String newConfigFilename = "sldeditor.properties";

    /**
     * Test method for {@link
     * com.sldeditor.common.property.PropertyFileFolder#getFolder(java.lang.String,
     * java.lang.String, java.lang.String)}.
     */
    @Test
    void testGetFolderMigrate() {
        Path tempFolder = null;
        try {
            tempFolder = Files.createTempDirectory("sldeditor_testGetFolderMigrate");
        } catch (IOException e) {
            e.printStackTrace();
            fail("Failed to create temp folder in temp folder!");
        }

        File oldFolder = new File(tempFolder.toFile(), "old");
        oldFolder.mkdirs();

        File newFolder = new File(tempFolder.toFile(), "new");
        newFolder.mkdirs();

        File oldConfigFile = new File(oldFolder, oldConfigFilename);
        FileWriter writer;
        try {
            writer = new FileWriter(oldConfigFile.getAbsolutePath());
            writer.write(testString);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            fail("failed to write old config file");
        }

        File newConfigFile = new File(newFolder, newConfigFilename);

        assertTrue(oldConfigFile.exists());
        assertTrue(!newConfigFile.exists());

        String actual =
                PropertyFileFolder.getFolder(
                        newFolder.getAbsolutePath(),
                        oldConfigFile.getAbsolutePath(),
                        newConfigFilename);

        // Check it has migrated
        assertTrue(!oldConfigFile.exists());
        assertTrue(new File(actual).exists());

        try {
            BufferedReader reader = new BufferedReader(new FileReader(actual));
            assertEquals(testString, reader.readLine());
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            fail("failed to read new config file");
        } catch (IOException e) {
            e.printStackTrace();
            fail("failed to read new config file");
        }

        try {
            Files.walk(tempFolder)
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    /**
     * Test method for {@link
     * com.sldeditor.common.property.PropertyFileFolder#getFolder(java.lang.String,
     * java.lang.String, java.lang.String)}.
     */
    @Test
    void testGetFolderMigrationFails() {
        Path tempFolder = null;
        try {
            tempFolder = Files.createTempDirectory("sldeditor_testGetFolderMigrationFails");
        } catch (IOException e) {
            e.printStackTrace();
            fail("Failed to create temp folder in temp folder!");
        }

        File oldFolder = new File(tempFolder.toFile(), "old");
        oldFolder.mkdirs();

        File newFolder = new File(tempFolder.toFile(), "new");
        newFolder.mkdirs();

        File oldConfigFile = new File(oldFolder, oldConfigFilename);

        File newConfigFile = new File(newFolder, newConfigFilename);

        assertTrue(!oldConfigFile.exists());
        assertTrue(!newConfigFile.exists());

        String actual =
                PropertyFileFolder.getFolder(
                        newFolder.getAbsolutePath(),
                        oldConfigFile.getAbsolutePath(),
                        newConfigFilename);

        // Check it has migrated
        assertTrue(!oldConfigFile.exists());
        assertTrue(!new File(actual).exists());

        try {
            Files.walk(tempFolder)
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Test method for {@link
     * com.sldeditor.common.property.PropertyFileFolder#getFolder(java.lang.String,
     * java.lang.String, java.lang.String)}.
     */
    @Test
    void testGetFolderNoMigration() {
        Path tempFolder = null;
        try {
            tempFolder = Files.createTempDirectory("sldeditor_testGetFolderNoMigration");
        } catch (IOException e) {
            e.printStackTrace();
            fail("Failed to create temp folder in temp folder!");
        }

        File oldFolder = new File(tempFolder.toFile(), "old");
        oldFolder.mkdirs();

        File newFolder = new File(tempFolder.toFile(), "new");
        newFolder.mkdirs();

        File oldConfigFile = new File(oldFolder, oldConfigFilename);
        FileWriter writer;
        try {
            writer = new FileWriter(oldConfigFile.getAbsolutePath());
            writer.write(testString);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            fail("failed to write old config file");
        }

        File newConfigFile = new File(newFolder, newConfigFilename);

        assertTrue(oldConfigFile.exists());
        assertTrue(!newConfigFile.exists());

        String actual =
                PropertyFileFolder.getFolder(
                        newFolder.getAbsolutePath(),
                        oldConfigFile.getAbsolutePath(),
                        newConfigFilename);

        // Check it has migrated
        assertTrue(!oldConfigFile.exists());
        assertTrue(new File(actual).exists());

        try {
            BufferedReader reader = new BufferedReader(new FileReader(actual));
            assertEquals(testString, reader.readLine());
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            fail("failed to read new config file");
        } catch (IOException e) {
            e.printStackTrace();
            fail("failed to read new config file");
        }

        // Put the old config file back but make sure it does not get migrated
        try {
            writer = new FileWriter(oldConfigFile.getAbsolutePath());
            writer.write(testString2);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            fail("failed to write new old config file");
        }

        actual =
                PropertyFileFolder.getFolder(
                        newFolder.getAbsolutePath(),
                        oldConfigFile.getAbsolutePath(),
                        newConfigFilename);

        // Check it has not migrated
        assertTrue(oldConfigFile.exists());
        assertTrue(new File(actual).exists());

        // The new property file should not have been overwritten
        try {
            BufferedReader reader = new BufferedReader(new FileReader(actual));
            assertEquals(testString, reader.readLine());
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            fail("failed to read new config file");
        } catch (IOException e) {
            e.printStackTrace();
            fail("failed to read new config file");
        }

        try {
            Files.walk(tempFolder)
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
