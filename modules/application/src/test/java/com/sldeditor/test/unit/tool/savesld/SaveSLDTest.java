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

package com.sldeditor.test.unit.tool.savesld;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import com.google.common.io.Files;
import com.sldeditor.common.SLDDataInterface;
import com.sldeditor.common.data.SLDData;
import com.sldeditor.common.data.StyleWrapper;
import com.sldeditor.test.unit.datasource.impl.DummyInternalSLDFile2;
import com.sldeditor.test.unit.tool.scale.DummyScaleSLDFile;
import com.sldeditor.tool.savesld.SaveSLD;
import com.sldeditor.tool.savesld.SaveSLDDestinationInterface;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Unit test class for SaveSLD.
 *
 * @author Robert Ward (SCISYS)
 */
class SaveSLDTest {

    private static final String PREFIX = "extracted";

    class TestSaveSLD extends SaveSLD {

        public void setTestClass(SaveSLDDestinationInterface overwriteDestinationDlg) {
            super.setOverwriteDestinationDlg(overwriteDestinationDlg);
        }
    }

    private static File rootFolder;

    @BeforeAll
    static void setUp() {
        rootFolder = Files.createTempDir();
    }

    @AfterAll
    static void tidyUp() {
        try {
            FileUtils.deleteDirectory(rootFolder);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Test method for {@link com.sldeditor.tool.savesld.SaveSLD#saveAllSLDToFolder(java.util.List,
     * java.io.File, boolean)}.
     */
    @Test
    void testSaveAllSLDToFolderNoImages() {
        File destFolder = new File(rootFolder, "noimages");
        TestSaveSLDDestination testDlg = new TestSaveSLDDestination();

        TestSaveSLD testObj = new TestSaveSLD();

        testObj.setTestClass(testDlg);

        List<SLDDataInterface> sldDataList = new ArrayList<SLDDataInterface>();

        DummyScaleSLDFile testSLD1 = new DummyScaleSLDFile();
        sldDataList.add(testSLD1.getSLDData());

        DummyInternalSLDFile2 testSLD2 = new DummyInternalSLDFile2();
        sldDataList.add(testSLD2.getSLDData());

        testObj.saveAllSLDToFolder(sldDataList, destFolder, false);

        File f = new File(destFolder, testSLD1.getSLDData().getLayerName());
        assertTrue(f.exists());

        f = new File(destFolder, testSLD2.getSLDData().getLayerName() + ".sld");
        assertTrue(f.exists());

        assertTrue(destFolder.listFiles().length == sldDataList.size());
    }

    /**
     * Test method for {@link com.sldeditor.tool.savesld.SaveSLD#saveAllSLDToFolder(java.util.List,
     * java.io.File, boolean)}.
     */
    @Test
    void testSaveAllSLDToFolderImages() {
        File destFolder = new File(rootFolder, "images");
        TestSaveSLDDestination testDlg = new TestSaveSLDDestination();

        TestSaveSLD testObj = new TestSaveSLD();

        testObj.setTestClass(testDlg);

        List<SLDDataInterface> sldDataList = new ArrayList<SLDDataInterface>();

        DummyScaleSLDFile testSLD1 = new DummyScaleSLDFile();
        sldDataList.add(testSLD1.getSLDData());

        DummyInternalSLDFile2 testSLD2 = new DummyInternalSLDFile2();
        sldDataList.add(testSLD2.getSLDData());

        // Copy SLD file
        InputStream inputStream =
                SaveSLD.class.getResourceAsStream("/point/sld/point_pointasgraphic.sld");
        File sldFile = null;

        if (inputStream == null) {
            fail("Failed to find point_pointasgraphic.sld");
        } else {
            try {
                sldFile = stream2file(inputStream, ".sld");

                String sldContents = readFile(sldFile, Charset.defaultCharset());

                SLDDataInterface sldData =
                        new SLDData(new StyleWrapper(sldFile.getName()), sldContents);
                sldData.setSLDFile(sldFile);
                sldData.setReadOnly(false);
                sldDataList.add(sldData);
                sldFile.deleteOnExit();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Copy graphic file
        File graphicFile = null;
        inputStream = SaveSLD.class.getResourceAsStream("/point/sld/smileyface.png");

        if (inputStream == null) {
            fail("Failed to find smileyface.png");
        } else {
            try {
                graphicFile = stream2file(inputStream, ".png");
                graphicFile.renameTo(new File(graphicFile.getParentFile(), "smileyface.png"));
                graphicFile.deleteOnExit();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        testObj.saveAllSLDToFolder(sldDataList, destFolder, true);

        File f = new File(destFolder, testSLD1.getSLDData().getLayerName());
        assertTrue(f.exists());

        f = new File(destFolder, testSLD2.getSLDData().getLayerName() + ".sld");
        assertTrue(f.exists());

        f = new File(destFolder, sldFile.getName());
        assertTrue(f.exists());

        f = new File(destFolder, "smileyface.png");
        assertTrue(f.exists());

        assertEquals(destFolder.listFiles().length, 4);

        // Don't allow overwrite
        testDlg.setWriteOutputFile(false);
        testObj.saveAllSLDToFolder(sldDataList, destFolder, true);

        // Overwrite everything
        testDlg.setYesToAll(true);
        testDlg.setWriteOutputFile(true);
        testObj.saveAllSLDToFolder(sldDataList, destFolder, true);
    }

    /**
     * Read file.
     *
     * @param file the file
     * @param encoding the encoding
     * @return the string
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private static String readFile(File file, Charset encoding) throws IOException {
        return FileUtils.readFileToString(Paths.get(file.getAbsolutePath()).toFile(), encoding);
    }

    /**
     * Writes an InputStream to a temporary file.
     *
     * @param in the in
     * @param suffix the suffix
     * @return the file
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private static File stream2file(InputStream in, String suffix) throws IOException {
        final File tempFile = File.createTempFile(PREFIX, suffix);
        try (FileOutputStream out = new FileOutputStream(tempFile)) {
            IOUtils.copy(in, out);
        }
        return tempFile;
    }
}
