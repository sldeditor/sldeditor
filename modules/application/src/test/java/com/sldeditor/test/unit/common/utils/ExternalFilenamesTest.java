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

package com.sldeditor.test.unit.common.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import com.sldeditor.common.data.SLDData;
import com.sldeditor.common.data.StyleWrapper;
import com.sldeditor.common.utils.ExternalFilenames;
import com.sldeditor.common.utils.OSValidator;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import org.junit.jupiter.api.Test;

/**
 * Unit test for ExternalFilenames class.
 *
 * <p>{@link com.sldeditor.common.utils.ExternalFilenames}
 *
 * @author Robert Ward (SCISYS)
 */
public class ExternalFilenamesTest {

    /**
     * Test method for {@link
     * com.sldeditor.common.utils.ExternalFilenames#getFile(java.lang.String)}.
     */
    @Test
    public void testGetFile() {
        assertNull(ExternalFilenames.getFile(null, null));

        SLDData sldData = new SLDData(new StyleWrapper("workspace", "style"), "contents");
        File tempSLDFile = null;
        try {
            tempSLDFile = File.createTempFile(getClass().getSimpleName(), ".sld");
        } catch (IOException e1) {
            e1.printStackTrace();
            fail("Couldn't create temp test file");
        }

        String expectedGraphicsFile = "test.png";

        // Try it with no SLD file set
        assertNull(ExternalFilenames.getFile(sldData, expectedGraphicsFile));

        // Now set the SLD file
        sldData.setSLDFile(tempSLDFile);

        // The graphics file is relative to the sld file so add the parent folder
        // of the sld file
        String absolutePath =
                ExternalFilenames.getFile(sldData, expectedGraphicsFile).getAbsolutePath();
        assertEquals(tempSLDFile.getParent() + File.separator + expectedGraphicsFile, absolutePath);

        // Now a graphics file with an absolute file path
        File absolutePathGraphicsFile = null;
        try {
            absolutePathGraphicsFile = File.createTempFile(getClass().getSimpleName(), ".png");
        } catch (IOException e) {
            e.printStackTrace();
            fail("Couldn't create temp test file");
        }

        String actualResult =
                ExternalFilenames.getFile(sldData, absolutePathGraphicsFile.getAbsolutePath())
                        .getAbsolutePath();
        String expectedResult = absolutePathGraphicsFile.getAbsolutePath();
        assertEquals(expectedResult, actualResult);

        // Now try a URL
        try {
            String absolutePathGraphicsFileURL =
                    absolutePathGraphicsFile.toURI().toURL().toString();
            actualResult =
                    ExternalFilenames.getFile(sldData, absolutePathGraphicsFileURL)
                            .getAbsolutePath();

            String s = OSValidator.isWindows() ? "file:/" : "file:";

            expectedResult =
                    new File(absolutePathGraphicsFileURL.substring(s.length())).getAbsolutePath();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
        assertEquals(expectedResult, actualResult);

        tempSLDFile.delete();
        absolutePathGraphicsFile.delete();
    }

    /**
     * Test method for {@link com.sldeditor.common.utils.ExternalFilenames#getText(java.net.URL)}.
     */
    @Test
    public void testGetText() {
        assertNull(ExternalFilenames.getText(null, null));

        SLDData sldData = new SLDData(new StyleWrapper("workspace", "style"), "contents");
        File tempSLDFile = null;
        try {
            tempSLDFile = File.createTempFile(getClass().getSimpleName(), ".sld");
        } catch (IOException e1) {
            e1.printStackTrace();
            fail("Couldn't create temp test file");
        }

        String expectedFilename = "test.tst";

        // Try it with no SLD file set
        assertNull(ExternalFilenames.getFile(sldData, expectedFilename));

        // Now set the SLD file
        sldData.setSLDFile(tempSLDFile);

        String rootFolder = tempSLDFile.getParent();

        String expectedResult = File.separator + expectedFilename;
        File file = new File(rootFolder + expectedResult);
        try {
            String actualResult = ExternalFilenames.getText(sldData, file.toURI().toURL());
            assertEquals(expectedResult, actualResult);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }

        String actualFilename2 = new File(expectedFilename).getAbsolutePath();
        File file2 = new File(actualFilename2);

        try {
            assertEquals(
                    actualFilename2, ExternalFilenames.getText(sldData, file2.toURI().toURL()));
        } catch (MalformedURLException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }

        tempSLDFile.delete();
    }

    /**
     * Test method for {@link
     * com.sldeditor.common.utils.ExternalFilenames#getFileExtension(java.lang.String)}.
     */
    @Test
    public void testGetFileExtension() {
        assertNull(ExternalFilenames.getFileExtension(null));

        String actualTest1 = "test1.txt";
        assertEquals("txt", ExternalFilenames.getFileExtension(actualTest1));
        String actualTest2 = "test2";
        assertEquals("", ExternalFilenames.getFileExtension(actualTest2));
    }

    /**
     * Test method for {@link
     * com.sldeditor.common.utils.ExternalFilenames#getImageFormat(java.lang.String)}.
     */
    @Test
    public void testGetImageFormat() {
        assertNull(ExternalFilenames.getImageFormat(null));
        assertEquals("image/svg+xml", ExternalFilenames.getImageFormat("svg"));
        assertEquals("image/png", ExternalFilenames.getImageFormat("png"));
        assertEquals("jpg", ExternalFilenames.getImageFormat("jpg"));
        assertEquals("", ExternalFilenames.getImageFormat(""));
    }

    /**
     * Test method for {@link
     * com.sldeditor.common.utils.ExternalFilenames#addFileExtensionSeparator(java.lang.String)}.
     */
    @Test
    public void testAddFileExtensionSeparator() {
        String testValue = "abcxyz";
        String actual = ExternalFilenames.addFileExtensionSeparator(testValue);

        assertEquals("." + testValue, actual);
    }

    /**
     * Test method for {@link
     * com.sldeditor.common.utils.ExternalFilenames#convertURLToFile(java.lang.String)}.
     */
    @Test
    public void testConvertURLToFile_String() {
        assertEquals("", ExternalFilenames.convertURLToFile((String) null));
        String prefix;

        String testFilenameValue = "";
        if (OSValidator.isWindows()) {
            testFilenameValue = "C:/tmp/test.txt";
            prefix = "file:/";
        } else {
            testFilenameValue = "/tmp/test.txt";
            prefix = "file:";
        }
        String actual = ExternalFilenames.convertURLToFile(testFilenameValue);

        assertEquals(testFilenameValue, actual);

        String testFullFilenameValue = prefix + testFilenameValue;
        actual = ExternalFilenames.convertURLToFile(testFullFilenameValue);

        assertEquals(testFilenameValue, actual);
    }

    /**
     * Test method for {@link
     * com.sldeditor.common.utils.ExternalFilenames#convertURLToFile(java.net.URL)}.
     */
    @Test
    public void testConvertURLToFile_URL() {
        assertEquals("", ExternalFilenames.convertURLToFile((URL) null));
        String prefix;

        String testFilenameStringValue = null;
        if (OSValidator.isWindows()) {
            testFilenameStringValue = "C:/tmp/test.txt";
            prefix = "file:/";
        } else {
            testFilenameStringValue = "/tmp/test.txt";
            prefix = "file:";
        }

        URL testFilenameValue = null;
        try {
            testFilenameValue = new URL(prefix + testFilenameStringValue);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        String actual = ExternalFilenames.convertURLToFile(testFilenameValue);

        assertEquals(testFilenameStringValue, actual);
    }

    /**
     * Test method for {@link
     * com.sldeditor.common.utils.ExternalFilenames#createSLDFilename(java.io.File)}.
     */
    @Test
    public void testCreateSLDFilename() {
        assertNull(ExternalFilenames.createSLDFilename(null));

        String initialFilename = "D:/tmp/test/out.shp";
        String expectedFilename = new File("D:/tmp/test/out.sld").getAbsolutePath();
        File initialFile = new File(initialFilename);

        File actualFile = ExternalFilenames.createSLDFilename(initialFile);

        assertTrue(actualFile.getAbsolutePath().compareTo(expectedFilename) == 0);

        // Now try where filename already has the correct file extension
        actualFile = ExternalFilenames.createSLDFilename(new File(expectedFilename));

        assertTrue(actualFile.getAbsolutePath().compareTo(expectedFilename) == 0);
    }
}
