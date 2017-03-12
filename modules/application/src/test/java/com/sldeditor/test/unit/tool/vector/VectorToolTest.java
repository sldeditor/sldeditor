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

package com.sldeditor.test.unit.tool.vector;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.feature.type.PropertyDescriptor;

import com.google.common.io.Files;
import com.sldeditor.SLDEditor;
import com.sldeditor.SLDEditorDlgInterface;
import com.sldeditor.common.SLDDataInterface;
import com.sldeditor.common.SLDEditorInterface;
import com.sldeditor.common.utils.ExternalFilenames;
import com.sldeditor.common.utils.OSValidator;
import com.sldeditor.datasource.DataSourceInterface;
import com.sldeditor.datasource.extension.filesystem.node.database.DatabaseFeatureClassNode;
import com.sldeditor.datasource.extension.filesystem.node.file.FileTreeNode;
import com.sldeditor.datasource.impl.DataSourceFactory;
import com.sldeditor.tool.vector.VectorTool;

/**
 * Unit test for VectorTool class.
 * <p>
 * {@link com.sldeditor.tool.vector.VectorTool}
 * 
 * @author Robert Ward (SCISYS)
 *
 */
public class VectorToolTest {

    /** The Constant DEFAULT_FONT. */
    private static final String DEFAULT_FONT = "Arial";

    /** The Constant DEFAULT_UNIX_FONT. */
    private static final String DEFAULT_UNIX_FONT = "Century Schoolbook L";

    /** The Constant PREFIX. */
    private static final String PREFIX = "extracted";

    /** The Constant SUFFIX. */
    private static final String SUFFIX = ".sld";

    class TestVectorTool extends VectorTool
    {
        public TestVectorTool(SLDEditorInterface sldEditorInterface) {
            super(sldEditorInterface);
        }

        public boolean testImportFile(FileTreeNode fileTreeNode) {
            return super.importFile(fileTreeNode);
        }

        public boolean testImportFeatureClass(DatabaseFeatureClassNode featureClassNode) {
            return super.importFeatureClass(featureClassNode);
        }

        public void testSetDataSource(FileTreeNode fileTreeNode) {
            super.setDataSource(fileTreeNode);
        }

        public void testSetDataSource(DatabaseFeatureClassNode featureClassNode) {
            super.setDataSource(featureClassNode);
        }

    }
    /**
     * The Class TestSLDEditor.
     */
    class TestSLDEditor extends SLDEditor {

        /** The Constant serialVersionUID. */
        private static final long serialVersionUID = 1L;

        /**
         * Instantiates a new test SLD editor.
         *
         * @param filename the filename
         * @param extensionArgList the extension arg list
         * @param overrideSLDEditorDlg the override SLD editor dlg
         */
        public TestSLDEditor(String filename, List<String> extensionArgList,
                SLDEditorDlgInterface overrideSLDEditorDlg) {
            super(filename, extensionArgList, overrideSLDEditorDlg);
        }

        /*
         * (non-Javadoc)
         * 
         * @see com.sldeditor.SLDEditor#populate(com.sldeditor.common.SLDDataInterface)
         */
        @Override
        protected void populate(SLDDataInterface sldData) {
            super.populate(sldData);
        }

    }

    /**
     * Test method for {@link com.sldeditor.tool.vector.VectorTool#VectorTool(com.sldeditor.common.SLDEditorInterface)}.
     */
    @Test
    public void testVectorTool() {
        String testsldfile = "/polygon/sld/polygon_polygonwithdefaultlabel.sld";
        TestSLDEditor testSLDEditor = new TestSLDEditor(null, null, null);
        InputStream inputStream = VectorToolTest.class.getResourceAsStream(testsldfile);

        if (inputStream == null) {
            Assert.assertNotNull("Failed to find sld test file : " + testsldfile, inputStream);
        } else {
            File f = null;
            try {
                f = stream2file(inputStream);

                int noOfRetries = 3;
                int attempt = 0;

                while (attempt < noOfRetries) {
                    try {
                        testSLDEditor.openFile(f.toURI().toURL());
                        break;
                    } catch (NullPointerException nullException) {
                        nullException.printStackTrace();
                        StackTraceElement[] stackTraceElements = nullException.getStackTrace();

                        System.out.println(stackTraceElements[0].getMethodName());

                        System.out.println("Attempt : " + attempt + 1);
                        attempt++;
                    }
                }

                f.delete();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

        // Fields extracted from the SLD file
        DataSourceInterface dataSource = DataSourceFactory.createDataSource(null);
        Collection<PropertyDescriptor> propertyList = dataSource.getPropertyDescriptorList();

        assertEquals(2, propertyList.size());
        Map<String, PropertyDescriptor> map = new HashMap<String, PropertyDescriptor>();

        for (PropertyDescriptor property : propertyList) {
            map.put(property.getName().getLocalPart(), property);
        }
        AttributeDescriptor name = (AttributeDescriptor) map.get("name");
        assertNotNull(name);
        GeometryDescriptor geometry = (GeometryDescriptor) map.get("geom");
        assertNotNull(geometry);

        File tempFolder = Files.createTempDir();
        try {
            File shpFile = extractShapeFile(tempFolder, "/test/sld_cookbook_polygon.zip");

            TestVectorTool vectorTool = new TestVectorTool(testSLDEditor);

            FileTreeNode fileTreeNode = new FileTreeNode(shpFile.getParentFile(), shpFile.getName());
            
            vectorTool.testSetDataSource(fileTreeNode);
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }

        try {
            FileUtils.deleteDirectory(tempFolder);

        } catch (IOException e) {
            e.printStackTrace();
        } 

    }

    /**
     * Stream 2 file.
     *
     * @param in the in
     * @return the file
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private static File stream2file(InputStream in) throws IOException {
        final File tempFile = File.createTempFile(PREFIX, SUFFIX);
        try (FileOutputStream out = new FileOutputStream(tempFile)) {
            IOUtils.copy(in, out);
        }

        // Update the font for the operating system
        String newFont = getFontForOS();
        if (newFont.compareToIgnoreCase(DEFAULT_FONT) != 0) {
            BufferedReader br = new BufferedReader(new FileReader(tempFile));
            try {
                StringBuilder sb = new StringBuilder();
                String line = br.readLine();

                while (line != null) {
                    sb.append(line.replace(DEFAULT_FONT, newFont));
                    sb.append("\n");
                    line = br.readLine();
                }
                try {
                    FileWriter fileWriter = new FileWriter(tempFile);
                    fileWriter.write(sb.toString());
                    fileWriter.flush();
                    fileWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } finally {
                br.close();
            }
        }
        return tempFile;
    }

    private static File extractShapeFile (File tempFolder, String shapeFileZip) throws IOException {
        InputStream inputStream = VectorToolTest.class.getResourceAsStream(shapeFileZip);

        File f = new File(shapeFileZip);

        final File tempFile = new File(tempFolder, f.getName());
        try (FileOutputStream out = new FileOutputStream(tempFile)) {
            IOUtils.copy(inputStream, out);
        }

        unzip(tempFile.getAbsolutePath(), tempFolder.getAbsolutePath());

        String fName = ExternalFilenames.removeSuffix(f.getName());
        File shpFile = new File(tempFolder + File.separator + fName, fName + ".shp");
        return shpFile;
    }

    private static void unzip(String zipFilePath, String destDirectory) throws IOException {
        File destDir = new File(destDirectory);
        if (!destDir.exists()) {
            destDir.mkdir();
        }
        ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath));
        ZipEntry entry = zipIn.getNextEntry();
        // iterates over entries in the zip file
        while (entry != null) {
            String filePath = destDirectory + File.separator + entry.getName();
            if (!entry.isDirectory()) {
                // if the entry is a file, extracts it
                extractFile(zipIn, filePath);
            } else {
                // if the entry is a directory, make the directory
                File dir = new File(filePath);
                dir.mkdir();
            }
            zipIn.closeEntry();
            entry = zipIn.getNextEntry();
        }
        zipIn.close();
    }
    /**
     * Extracts a zip entry (file entry)
     * @param zipIn
     * @param filePath
     * @throws IOException
     */
    private static void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
        byte[] bytesIn = new byte[4096];
        int read = 0;
        while ((read = zipIn.read(bytesIn)) != -1) {
            bos.write(bytesIn, 0, read);
        }
        bos.close();
    }

    /**
     * Test method for {@link com.sldeditor.tool.vector.VectorTool#getPanel()}.
     */
    @Test
    public void testGetPanel() {
    }

    /**
     * Test method for {@link com.sldeditor.tool.vector.VectorTool#setSelectedItems(java.util.List, java.util.List)}.
     */
    @Test
    public void testSetSelectedItems() {
    }

    /**
     * Test method for {@link com.sldeditor.tool.vector.VectorTool#getToolName()}.
     */
    @Test
    public void testGetToolName() {
    }

    /**
     * Test method for {@link com.sldeditor.tool.vector.VectorTool#supports(java.util.List, java.util.List, java.util.List)}.
     */
    @Test
    public void testSupports() {
    }

    /**
     * Test method for {@link com.sldeditor.tool.vector.VectorTool#importFile(com.sldeditor.datasource.extension.filesystem.node.file.FileTreeNode)}.
     */
    @Test
    public void testImportFile() {
    }

    /**
     * Test method for
     * {@link com.sldeditor.tool.vector.VectorTool#importFeatureClass(com.sldeditor.datasource.extension.filesystem.node.database.DatabaseFeatureClassNode)}.
     */
    @Test
    public void testImportFeatureClass() {
    }

    /**
     * Test method for
     * {@link com.sldeditor.tool.vector.VectorTool#setDataSource(com.sldeditor.datasource.extension.filesystem.node.file.FileTreeNode)}.
     */
    @Test
    public void testSetDataSourceFileTreeNode() {
    }

    /**
     * Test method for
     * {@link com.sldeditor.tool.vector.VectorTool#setDataSource(com.sldeditor.datasource.extension.filesystem.node.database.DatabaseFeatureClassNode)}.
     */
    @Test
    public void testSetDataSourceDatabaseFeatureClassNode() {
    }

    /**
     * Gets the font for the operating system.
     *
     * @return the new font
     */
    private static String getFontForOS() {
        if (OSValidator.isUnix()) {
            return DEFAULT_UNIX_FONT;
        }
        return DEFAULT_FONT;
    }

}
