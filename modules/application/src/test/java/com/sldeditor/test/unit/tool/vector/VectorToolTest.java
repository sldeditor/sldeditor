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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import com.google.common.io.Files;
import com.sldeditor.SLDEditor;
import com.sldeditor.SLDEditorDlgInterface;
import com.sldeditor.common.Controller;
import com.sldeditor.common.NodeInterface;
import com.sldeditor.common.SLDDataInterface;
import com.sldeditor.common.SLDEditorInterface;
import com.sldeditor.common.connection.DatabaseConnectionManager;
import com.sldeditor.common.connection.GeoServerConnectionManager;
import com.sldeditor.common.coordinate.CoordManager;
import com.sldeditor.common.data.DatabaseConnection;
import com.sldeditor.common.data.SelectedSymbol;
import com.sldeditor.common.preferences.PrefManager;
import com.sldeditor.common.undo.UndoManager;
import com.sldeditor.common.utils.ExternalFilenames;
import com.sldeditor.common.utils.OSValidator;
import com.sldeditor.common.vendoroption.VendorOptionManager;
import com.sldeditor.common.watcher.ReloadManager;
import com.sldeditor.datasource.DataSourceInterface;
import com.sldeditor.datasource.SLDEditorFile;
import com.sldeditor.datasource.SLDEditorFileInterface;
import com.sldeditor.datasource.attribute.DataSourceAttributeData;
import com.sldeditor.datasource.checks.CheckAttributeFactory;
import com.sldeditor.datasource.checks.CheckAttributeInterface;
import com.sldeditor.datasource.extension.filesystem.node.database.DatabaseFeatureClassNode;
import com.sldeditor.datasource.extension.filesystem.node.file.FileTreeNode;
import com.sldeditor.datasource.extension.filesystem.node.file.FileTreeNodeTypeEnum;
import com.sldeditor.datasource.extension.filesystem.node.geoserver.GeoServerStyleHeadingNode;
import com.sldeditor.datasource.impl.DataSourceFactory;
import com.sldeditor.datasource.impl.ExtractAttributes;
import com.sldeditor.filter.v2.envvar.EnvironmentVariableManager;
import com.sldeditor.map.MapRender;
import com.sldeditor.render.RenderPanelImpl;
import com.sldeditor.tool.dbconnectionlist.DatabaseConnectionFactory;
import com.sldeditor.tool.vector.VectorTool;
import com.sldeditor.ui.menu.SLDEditorMenus;
import java.awt.event.WindowEvent;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.swing.JFrame;
import org.apache.commons.io.IOUtils;
import org.geotools.styling.StyledLayerDescriptor;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.feature.type.PropertyDescriptor;

/**
 * Unit test for VectorTool class.
 *
 * <p>{@link com.sldeditor.tool.vector.VectorTool}
 *
 * @author Robert Ward (SCISYS)
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

    @BeforeAll
    public static void startUp() {
        clearDown();
    }

    /** Clean up. */
    @AfterAll
    public static void cleanUp() {
        List<CheckAttributeInterface> checkList = new ArrayList<CheckAttributeInterface>();
        CheckAttributeFactory.setOverideCheckList(checkList);

        RenderPanelImpl.setUnderTest(false);
        clearDown();
    }

    /** The Class TestVectorTool. */
    class TestVectorTool extends VectorTool {

        /**
         * Instantiates a new test vector tool.
         *
         * @param sldEditorInterface the sld editor interface
         */
        public TestVectorTool(SLDEditorInterface sldEditorInterface) {
            super(sldEditorInterface);
        }

        /**
         * Test import file.
         *
         * @param fileTreeNode the file tree node
         * @return true, if successful
         */
        public boolean testImportFile(FileTreeNode fileTreeNode) {
            return super.importFile(fileTreeNode);
        }

        /**
         * Test import feature class.
         *
         * @param featureClassNode the feature class node
         * @return true, if successful
         */
        public boolean testImportFeatureClass(DatabaseFeatureClassNode featureClassNode) {
            return super.importFeatureClass(featureClassNode);
        }

        /**
         * Test set data source.
         *
         * @param fileTreeNode the file tree node
         */
        public void testSetDataSource(FileTreeNode fileTreeNode) {
            super.setDataSource(fileTreeNode);
        }

        /**
         * Test set data source.
         *
         * @param featureClassNode the feature class node
         */
        public void testSetDataSource(DatabaseFeatureClassNode featureClassNode) {
            super.setDataSource(featureClassNode);
        }

        public boolean isImportButtonSelected() {
            return importVectorButton.isEnabled();
        }

        public boolean isDataSourceButtonSelected() {
            return dataSourceButton.isEnabled();
        }
    }

    /** The Class TestMissingSLDAttributes. */
    class TestMissingSLDAttributes implements CheckAttributeInterface {

        /** The missing field list. */
        private List<String> missingFieldList = new ArrayList<String>();

        /*
         * (non-Javadoc)
         *
         * @see com.sldeditor.datasource.impl.CheckAttributeInterface#checkAttributes(com.sldeditor.
         * datasource.SLDEditorFileInterface)
         */
        @Override
        public void checkAttributes(SLDEditorFileInterface editorFile) {
            missingFieldList.clear();

            ExtractAttributes extract = new ExtractAttributes();
            StyledLayerDescriptor sld = editorFile.getSLD();
            extract.extractDefaultFields(sld);
            List<DataSourceAttributeData> sldFieldList = extract.getFields();

            List<DataSourceAttributeData> dataSourceList = editorFile.getSLDData().getFieldList();

            for (DataSourceAttributeData sldField : sldFieldList) {
                if (!dataSourceList.contains(sldField)) {
                    missingFieldList.add(sldField.getName());
                }
            }
        }

        /**
         * Gets the missing field list.
         *
         * @return the missingFieldList
         */
        public List<String> getMissingFieldList() {
            return missingFieldList;
        }
    }

    /** The Class TestSLDEditor. */
    static class TestSLDEditor extends SLDEditor {

        /** The Constant serialVersionUID. */
        private static final long serialVersionUID = 1L;

        /**
         * Instantiates a new test SLD editor.
         *
         * @param filename the filename
         * @param extensionArgList the extension arg list
         * @param overrideSLDEditorDlg the override SLD editor dlg
         */
        public TestSLDEditor(
                String filename,
                List<String> extensionArgList,
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

        public static TestSLDEditor createAndShowGUI2(
                String filename,
                List<String> extensionArgList,
                boolean underTest,
                SLDEditorDlgInterface overrideSLDEditorDlg) {
            SLDEditor.underTestFlag = underTest;

            frame = new JFrame("test");

            CoordManager.getInstance().populateCRSList();
            Controller.getInstance().setFrame(frame);

            MapRender.setUnderTest(underTest);
            RenderPanelImpl.setUnderTest(underTest);
            ReloadManager.setUnderTest(underTest);

            frame.setDefaultCloseOperation(
                    underTest ? JFrame.DISPOSE_ON_CLOSE : JFrame.EXIT_ON_CLOSE);

            // Add contents to the window.
            TestSLDEditor sldEditor =
                    new TestSLDEditor(filename, extensionArgList, overrideSLDEditorDlg);

            // Display the window.
            frame.pack();

            return sldEditor;
        }
    }

    /**
     * Test method for {@link
     * com.sldeditor.tool.vector.VectorTool#VectorTool(com.sldeditor.common.SLDEditorInterface)}.
     */
    @Test
    public void testVectorToolFileDataSource() {
        TestMissingSLDAttributes testAttribute = new TestMissingSLDAttributes();
        List<CheckAttributeInterface> checkList = new ArrayList<CheckAttributeInterface>();
        checkList.add(testAttribute);
        CheckAttributeFactory.setOverideCheckList(checkList);

        String testsldfile = "/polygon/sld/polygon_polygonwithdefaultlabel.sld";
        TestSLDEditor testSLDEditor = null;
        try {
            testSLDEditor = TestSLDEditor.createAndShowGUI2(null, null, true, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        InputStream inputStream = VectorToolTest.class.getResourceAsStream(testsldfile);

        if (inputStream == null) {
            assertNotNull(inputStream, "Failed to find sld test file : " + testsldfile);
        } else {
            File f = null;
            try {
                f = stream2file(inputStream);
                try {
                    testSLDEditor.openFile(f.toURI().toURL());
                } catch (NullPointerException nullException) {
                    nullException.printStackTrace();
                    StackTraceElement[] stackTraceElements = nullException.getStackTrace();

                    System.out.println(stackTraceElements[0].getMethodName());
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
        TestVectorTool vectorTool = new TestVectorTool(testSLDEditor);
        try {
            // Set a shape file as a data source - that matches the SLD
            File matchingShpFile = extractShapeFile(tempFolder, "/test/sld_cookbook_polygon.zip");

            FileTreeNode fileTreeNode =
                    new FileTreeNode(matchingShpFile.getParentFile(), matchingShpFile.getName());

            vectorTool.testSetDataSource(fileTreeNode);

            dataSource = DataSourceFactory.createDataSource(null);
            propertyList = dataSource.getPropertyDescriptorList();

            assertEquals(3, propertyList.size());
            map.clear();

            for (PropertyDescriptor property : propertyList) {
                map.put(property.getName().getLocalPart(), property);
            }
            name = (AttributeDescriptor) map.get("name");
            assertNotNull(name);
            geometry = (GeometryDescriptor) map.get("the_geom");
            assertNotNull(geometry);
            AttributeDescriptor pop = (AttributeDescriptor) map.get("pop");
            assertNotNull(pop);

            // Set a shape file as a data source - that does not match the SLD
            File nonMatchingShpFile = extractShapeFile(tempFolder, "/test/states.zip");

            FileTreeNode fileTreeNode2 =
                    new FileTreeNode(
                            nonMatchingShpFile.getParentFile(), nonMatchingShpFile.getName());

            vectorTool.testSetDataSource(fileTreeNode2);

            dataSource = DataSourceFactory.createDataSource(null);
            propertyList = dataSource.getPropertyDescriptorList();

            assertEquals(23, propertyList.size());
            map.clear();

            for (PropertyDescriptor property : propertyList) {
                map.put(property.getName().getLocalPart(), property);
            }
            name = (AttributeDescriptor) map.get("name");
            assertNull(name);
            geometry = (GeometryDescriptor) map.get("the_geom");
            assertNotNull(geometry);
            pop = (AttributeDescriptor) map.get("pop");
            assertNull(pop);

            assertEquals(1, testAttribute.getMissingFieldList().size());
            assertEquals("name", testAttribute.getMissingFieldList().get(0));

            // Create SLD from shape file
            vectorTool.testImportFile(fileTreeNode);
            dataSource = DataSourceFactory.createDataSource(null);
            propertyList = dataSource.getPropertyDescriptorList();

            assertEquals(3, propertyList.size());
            map.clear();

            for (PropertyDescriptor property : propertyList) {
                map.put(property.getName().getLocalPart(), property);
            }
            name = (AttributeDescriptor) map.get("name");
            assertNotNull(name);
            geometry = (GeometryDescriptor) map.get("the_geom");
            assertNotNull(geometry);
            pop = (AttributeDescriptor) map.get("pop");
            assertNotNull(pop);

            // Release locks
            dataSource.reset();
        } catch (IOException e) {
            fail(e.getStackTrace().toString());
        }

        // Tidy up so the remaining unit tests are ok
        JFrame frame = testSLDEditor.getApplicationFrame();
        frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
        testSLDEditor = null;
        clearDown();

        // Delete the shape files we extracted
        purgeDirectory(tempFolder);
    }

    /** Clear down. */
    private static void clearDown() {
        DataSourceFactory.reset();
        SelectedSymbol.destroyInstance();
        SLDEditorFile.destroyInstance();
        SLDEditorMenus.destroyInstance();
        DatabaseConnectionManager.destroyInstance();
        GeoServerConnectionManager.destroyInstance();
        PrefManager.destroyInstance();
        VendorOptionManager.destroyInstance();
        EnvironmentVariableManager.destroyInstance();
        UndoManager.destroyInstance();
    }

    @Test
    public void testVectorToolDBDataSource() {
        TestMissingSLDAttributes testAttribute = new TestMissingSLDAttributes();
        List<CheckAttributeInterface> checkList = new ArrayList<CheckAttributeInterface>();
        checkList.add(testAttribute);
        CheckAttributeFactory.setOverideCheckList(checkList);

        String testsldfile = "/polygon/sld/polygon_polygonwithdefaultlabel.sld";
        TestSLDEditor testSLDEditor = null;
        try {
            testSLDEditor = TestSLDEditor.createAndShowGUI2(null, null, true, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        RenderPanelImpl.setUnderTest(true);
        InputStream inputStream = VectorToolTest.class.getResourceAsStream(testsldfile);

        if (inputStream == null) {
            assertNotNull(inputStream, "Failed to find sld test file : " + testsldfile);
        } else {
            File f = null;
            try {
                f = stream2file(inputStream);
                try {
                    testSLDEditor.openFile(f.toURI().toURL());
                } catch (NullPointerException nullException) {
                    nullException.printStackTrace();
                    StackTraceElement[] stackTraceElements = nullException.getStackTrace();

                    System.out.println(stackTraceElements[0].getMethodName());
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
        TestVectorTool vectorTool = new TestVectorTool(testSLDEditor);
        try {
            InputStream gpkgInputStream =
                    VectorToolTest.class.getResourceAsStream("/test/sld_cookbook_polygon.gpkg");

            final File gpkgFile = new File(tempFolder, "sld_cookbook_polygon.gpkg");
            try (FileOutputStream out = new FileOutputStream(gpkgFile)) {
                IOUtils.copy(gpkgInputStream, out);
            }

            DatabaseConnection databaseConnection =
                    DatabaseConnectionFactory.getConnection(gpkgFile.getAbsolutePath());
            DatabaseFeatureClassNode dbFCTreeNode =
                    new DatabaseFeatureClassNode(null, databaseConnection, "sld_cookbook_polygon");

            DatabaseConnectionManager.getInstance().addNewConnection(null, databaseConnection);
            vectorTool.testSetDataSource(dbFCTreeNode);

            dataSource = DataSourceFactory.createDataSource(null);
            propertyList = dataSource.getPropertyDescriptorList();

            assertEquals(3, propertyList.size());
            map.clear();

            for (PropertyDescriptor property : propertyList) {
                map.put(property.getName().getLocalPart(), property);
            }
            name = (AttributeDescriptor) map.get("name");
            assertNotNull(name);
            geometry = (GeometryDescriptor) map.get("geometry");
            assertNotNull(geometry);
            AttributeDescriptor pop = (AttributeDescriptor) map.get("pop");
            assertNotNull(pop);

            // Create SLD from geopackage layer
            vectorTool.testImportFeatureClass(dbFCTreeNode);
            dataSource = DataSourceFactory.createDataSource(null);
            propertyList = dataSource.getPropertyDescriptorList();

            assertEquals(3, propertyList.size());
            map.clear();

            for (PropertyDescriptor property : propertyList) {
                map.put(property.getName().getLocalPart(), property);
            }
            name = (AttributeDescriptor) map.get("name");
            assertNotNull(name);
            geometry = (GeometryDescriptor) map.get("geometry");
            assertNotNull(geometry);
            pop = (AttributeDescriptor) map.get("pop");
            assertNotNull(pop);

            // Release locks
            dataSource.reset();
        } catch (IOException e) {
            fail(e.getStackTrace().toString());
        }

        // Tidy up so the remaining unit tests are ok
        JFrame frame = testSLDEditor.getApplicationFrame();
        frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
        testSLDEditor = null;
        clearDown();

        // Delete the shape files we extracted
        purgeDirectory(tempFolder);
    }

    /**
     * Purge directory.
     *
     * @param dir the dir
     */
    private void purgeDirectory(File dir) {
        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                purgeDirectory(file);
            }
            file.delete();
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

    /**
     * Extract shape file.
     *
     * @param tempFolder the temp folder
     * @param shapeFileZip the shape file zip
     * @return the file
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private static File extractShapeFile(File tempFolder, String shapeFileZip) throws IOException {
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

    /**
     * Unzip a zip file containing shp file.
     *
     * @param zipFilePath the zip file path
     * @param destDirectory the dest directory
     * @throws IOException Signals that an I/O exception has occurred.
     */
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
     * Extracts a zip entry (file entry).
     *
     * @param zipIn the zip in
     * @param filePath the file path
     * @throws IOException Signals that an I/O exception has occurred.
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

    /** Test method for {@link com.sldeditor.tool.vector.VectorTool#getPanel()}. */
    @Test
    public void testGetPanel() {
        VectorTool vectorTool = new VectorTool(null);
        assertNotNull(vectorTool.getPanel());
    }

    /**
     * Test method for {@link com.sldeditor.tool.vector.VectorTool#setSelectedItems(java.util.List,
     * java.util.List)}.
     */
    @Test
    public void testSetSelectedItems() {}

    /** Test method for {@link com.sldeditor.tool.vector.VectorTool#getToolName()}. */
    @Test
    public void testGetToolName() {
        VectorTool vectorTool = new VectorTool(null);
        assertNotNull(vectorTool.getToolName());
    }

    /**
     * Test method for {@link com.sldeditor.tool.vector.VectorTool#supports(java.util.List,
     * java.util.List, java.util.List)}.
     */
    @Test
    public void testSupports() {

        try {
            FileTreeNode vectorTreeNode =
                    new FileTreeNode(new File("/test"), "sld_cookbook_polygon.shp");
            vectorTreeNode.setFileCategory(FileTreeNodeTypeEnum.VECTOR);
            FileTreeNode rasterTreeNode =
                    new FileTreeNode(new File("/test"), "sld_cookbook_polygon.tif");
            rasterTreeNode.setFileCategory(FileTreeNodeTypeEnum.RASTER);
            List<Class<?>> uniqueNodeTypeList = new ArrayList<Class<?>>();
            List<NodeInterface> nodeTypeList = new ArrayList<NodeInterface>();
            List<SLDDataInterface> sldDataList = new ArrayList<SLDDataInterface>();

            // Try vector file
            nodeTypeList.add(vectorTreeNode);
            VectorTool vectorTool = new VectorTool(null);
            assertTrue(vectorTool.supports(uniqueNodeTypeList, nodeTypeList, sldDataList));

            // Try raster file
            nodeTypeList.clear();
            nodeTypeList.add(rasterTreeNode);
            assertFalse(vectorTool.supports(uniqueNodeTypeList, nodeTypeList, sldDataList));

            // Try database feature class
            nodeTypeList.clear();
            DatabaseFeatureClassNode databaseFeatureClassNode =
                    new DatabaseFeatureClassNode(null, null, "db fc");
            nodeTypeList.add(databaseFeatureClassNode);
            assertTrue(vectorTool.supports(uniqueNodeTypeList, nodeTypeList, sldDataList));

            // Try invalid node class
            nodeTypeList.clear();
            nodeTypeList.add(new GeoServerStyleHeadingNode(null, null, "test"));
            assertFalse(vectorTool.supports(uniqueNodeTypeList, nodeTypeList, sldDataList));

            // Try with no nodes
            nodeTypeList.clear();
            assertFalse(vectorTool.supports(uniqueNodeTypeList, nodeTypeList, sldDataList));

            // Try with null
            assertFalse(vectorTool.supports(uniqueNodeTypeList, null, sldDataList));
        } catch (SecurityException e) {
            fail(e.getStackTrace().toString());
        } catch (FileNotFoundException e) {
            fail(e.getStackTrace().toString());
        }
    }

    /**
     * Test method for {@link com.sldeditor.tool.vector.VectorTool#setSelected( java.util.List,
     * java.util.List)}.
     */
    @Test
    public void testSetSelected() {

        List<NodeInterface> nodeTypeList = new ArrayList<NodeInterface>();
        List<SLDDataInterface> sldDataList = new ArrayList<SLDDataInterface>();

        TestVectorTool vectorTool = new TestVectorTool(null);
        assertFalse(vectorTool.isImportButtonSelected());
        assertFalse(vectorTool.isDataSourceButtonSelected());

        vectorTool.setSelectedItems(nodeTypeList, sldDataList);
        assertFalse(vectorTool.isImportButtonSelected());
        assertFalse(vectorTool.isDataSourceButtonSelected());

        // Can only have one file selected
        nodeTypeList.add(new DatabaseFeatureClassNode(null, null, "db fc"));
        vectorTool.setSelectedItems(nodeTypeList, sldDataList);
        assertTrue(vectorTool.isImportButtonSelected());
        assertTrue(vectorTool.isDataSourceButtonSelected());

        nodeTypeList.add(new DatabaseFeatureClassNode(null, null, "db fc"));
        vectorTool.setSelectedItems(nodeTypeList, sldDataList);
        assertFalse(vectorTool.isImportButtonSelected());
        assertFalse(vectorTool.isDataSourceButtonSelected());
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
