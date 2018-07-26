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

package com.sldeditor.test.unit.tool.legend;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import com.google.common.io.Files;
import com.sldeditor.common.NodeInterface;
import com.sldeditor.common.SLDDataInterface;
import com.sldeditor.common.data.GeoServerLayer;
import com.sldeditor.common.data.SLDData;
import com.sldeditor.common.data.StyleWrapper;
import com.sldeditor.datasource.extension.filesystem.node.database.DatabaseFeatureClassNode;
import com.sldeditor.datasource.extension.filesystem.node.file.FileTreeNode;
import com.sldeditor.datasource.extension.filesystem.node.file.FileTreeNodeTypeEnum;
import com.sldeditor.datasource.extension.filesystem.node.geoserver.GeoServerLayerHeadingNode;
import com.sldeditor.datasource.extension.filesystem.node.geoserver.GeoServerLayerNode;
import com.sldeditor.datasource.extension.filesystem.node.geoserver.GeoServerStyleHeadingNode;
import com.sldeditor.datasource.extension.filesystem.node.geoserver.GeoServerStyleNode;
import com.sldeditor.datasource.extension.filesystem.node.geoserver.GeoServerWorkspaceNode;
import com.sldeditor.test.unit.datasource.impl.DummyInternalSLDFile;
import com.sldeditor.test.unit.datasource.impl.DummyInternalSLDFile2;
import com.sldeditor.tool.legend.LegendTool;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;

/**
 * Unit test class for LegendTool.
 *
 * @author Robert Ward (SCISYS)
 */
class LegendToolTest {

    class TestLegendTool extends LegendTool {

        /* (non-Javadoc)
         * @see com.sldeditor.tool.legend.LegendTool#saveAllHTMLToFolder(java.io.File)
         */
        @Override
        protected void saveAllHTMLToFolder(File destinationFolder) {
            super.saveAllHTMLToFolder(destinationFolder);
        }

        /* (non-Javadoc)
         * @see com.sldeditor.tool.legend.LegendTool#saveAllLegendToFolder(java.io.File)
         */
        @Override
        protected void saveAllLegendToFolder(File destinationFolder) {
            super.saveAllLegendToFolder(destinationFolder);
        }

        public boolean isSaveAllLegendEnabled() {
            return saveAllLegend.isEnabled();
        }

        public boolean isExportAllHTMLEnabled() {
            return exportAllHTML.isEnabled();
        }
    }

    /** Test method for {@link com.sldeditor.tool.legend.LegendTool#getPanel()}. */
    @Test
    void testGetPanel() {
        TestLegendTool testObj = new TestLegendTool();
        assertNotNull(testObj.getPanel());
    }

    /**
     * Test method for {@link
     * com.sldeditor.tool.legend.LegendTool#saveAllHTMLToFolder(java.io.File)}.
     */
    @Test
    void testSaveAllHTMLToFolder() {
        TestLegendTool testObj = new TestLegendTool();

        List<NodeInterface> nodeTypeList = new ArrayList<NodeInterface>();
        List<SLDDataInterface> sldDataList = new ArrayList<SLDDataInterface>();

        DummyInternalSLDFile sld1 = new DummyInternalSLDFile();
        sldDataList.add(sld1.getSLDData());

        DummyInternalSLDFile2 sld2 = new DummyInternalSLDFile2();
        sldDataList.add(sld2.getSLDData());

        try {
            FileTreeNode sldTreeNode = new FileTreeNode(new File("/test"), "test.sld");
            sldTreeNode.setFileCategory(FileTreeNodeTypeEnum.SLD);
            nodeTypeList.add(sldTreeNode);
        } catch (SecurityException e) {
            fail(e.getStackTrace().toString());
        } catch (FileNotFoundException e) {
            fail(e.getStackTrace().toString());
        }

        testObj.setSelectedItems(nodeTypeList, sldDataList);

        File destinationFolder = Files.createTempDir();
        testObj.saveAllHTMLToFolder(destinationFolder);

        class FileFilter implements FilenameFilter {

            /* (non-Javadoc)
             * @see java.io.FilenameFilter#accept(java.io.File, java.lang.String)
             */
            @Override
            public boolean accept(File dir, String name) {
                if (name.equals("index.html")) {
                    return true;
                }
                return false;
            }
        }

        assertNotNull(destinationFolder.listFiles(new FileFilter()));
        try {
            FileUtils.deleteDirectory(destinationFolder);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test method for {@link
     * com.sldeditor.tool.legend.LegendTool#saveAllLegendToFolder(java.io.File)}.
     */
    @Test
    void testSaveAllLegendToFolder() {
        TestLegendTool testObj = new TestLegendTool();

        List<NodeInterface> nodeTypeList = new ArrayList<NodeInterface>();
        List<SLDDataInterface> sldDataList = new ArrayList<SLDDataInterface>();

        DummyInternalSLDFile sld1 = new DummyInternalSLDFile();
        sldDataList.add(sld1.getSLDData());

        DummyInternalSLDFile2 sld2 = new DummyInternalSLDFile2();
        sldDataList.add(sld2.getSLDData());

        try {
            FileTreeNode sldTreeNode = new FileTreeNode(new File("/test"), "test.sld");
            sldTreeNode.setFileCategory(FileTreeNodeTypeEnum.SLD);
            nodeTypeList.add(sldTreeNode);
        } catch (SecurityException e) {
            fail(e.getStackTrace().toString());
        } catch (FileNotFoundException e) {
            fail(e.getStackTrace().toString());
        }

        testObj.setSelectedItems(nodeTypeList, sldDataList);

        File destinationFolder = Files.createTempDir();
        testObj.saveAllLegendToFolder(destinationFolder);

        assertEquals(2, destinationFolder.listFiles().length);
        try {
            FileUtils.deleteDirectory(destinationFolder);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    /**
     * Test method for {@link com.sldeditor.tool.legend.LegendTool#setSelectedItems(java.util.List,
     * java.util.List)}.
     */
    @Test
    void testSetSelectedItems() {
        List<NodeInterface> nodeTypeList = new ArrayList<NodeInterface>();
        List<SLDDataInterface> sldDataList = new ArrayList<SLDDataInterface>();

        try {
            FileTreeNode sldTreeNode = new FileTreeNode(new File("/test"), "test.sld");
            sldTreeNode.setFileCategory(FileTreeNodeTypeEnum.SLD);
            nodeTypeList.add(sldTreeNode);

            sldDataList.add(new SLDData(null, ""));
            TestLegendTool testTool = new TestLegendTool();
            assertFalse(testTool.isSaveAllLegendEnabled());
            assertFalse(testTool.isExportAllHTMLEnabled());

            // Try one items
            testTool.setSelectedItems(nodeTypeList, sldDataList);
            assertTrue(testTool.isSaveAllLegendEnabled());
            assertTrue(testTool.isExportAllHTMLEnabled());

            // Try two items
            testTool.setSelectedItems(nodeTypeList, sldDataList);
            assertTrue(testTool.isSaveAllLegendEnabled());
            assertTrue(testTool.isExportAllHTMLEnabled());
        } catch (SecurityException e) {
            fail(e.getStackTrace().toString());
        } catch (FileNotFoundException e) {
            fail(e.getStackTrace().toString());
        }
    }

    /** Test method for {@link com.sldeditor.tool.legend.LegendTool#getToolName()}. */
    @Test
    void testGetToolName() {
        TestLegendTool testObj = new TestLegendTool();
        assertNotNull(testObj.getToolName());
    }

    /**
     * Test method for {@link com.sldeditor.tool.legend.LegendTool#supports(java.util.List,
     * java.util.List, java.util.List)}.
     */
    @Test
    void testSupports() {
        try {
            FileTreeNode vectorTreeNode =
                    new FileTreeNode(new File("/test"), "sld_cookbook_polygon.shp");
            vectorTreeNode.setFileCategory(FileTreeNodeTypeEnum.VECTOR);

            FileTreeNode rasterTreeNode =
                    new FileTreeNode(new File("/test"), "sld_cookbook_polygon.tif");
            rasterTreeNode.setFileCategory(FileTreeNodeTypeEnum.RASTER);

            FileTreeNode sldTreeNode = new FileTreeNode(new File("/test"), "test.sld");
            sldTreeNode.setFileCategory(FileTreeNodeTypeEnum.SLD);

            List<Class<?>> uniqueNodeTypeList = new ArrayList<Class<?>>();
            List<NodeInterface> nodeTypeList = new ArrayList<NodeInterface>();
            List<SLDDataInterface> sldDataList = new ArrayList<SLDDataInterface>();

            // Try vector file
            nodeTypeList.add(vectorTreeNode);
            TestLegendTool TestTool = new TestLegendTool();
            assertFalse(TestTool.supports(uniqueNodeTypeList, nodeTypeList, sldDataList));

            uniqueNodeTypeList.add(String.class);

            // Try raster file
            nodeTypeList.clear();
            nodeTypeList.add(rasterTreeNode);
            assertFalse(TestTool.supports(uniqueNodeTypeList, nodeTypeList, sldDataList));

            // Try SLD file
            nodeTypeList.clear();
            nodeTypeList.add(sldTreeNode);
            assertTrue(TestTool.supports(uniqueNodeTypeList, nodeTypeList, sldDataList));

            // Try database feature class
            nodeTypeList.clear();
            DatabaseFeatureClassNode databaseFeatureClassNode =
                    new DatabaseFeatureClassNode(null, null, "db fc");
            nodeTypeList.add(databaseFeatureClassNode);
            assertFalse(TestTool.supports(uniqueNodeTypeList, nodeTypeList, sldDataList));

            // Try GeoServerLayerHeadingNode node class
            nodeTypeList.clear();
            nodeTypeList.add(new GeoServerLayerHeadingNode(null, null, "test"));
            assertFalse(TestTool.supports(uniqueNodeTypeList, nodeTypeList, sldDataList));

            // Try GeoServerLayerNode node class
            nodeTypeList.clear();
            nodeTypeList.add(new GeoServerLayerNode(null, new GeoServerLayer()));
            assertFalse(TestTool.supports(uniqueNodeTypeList, nodeTypeList, sldDataList));

            // Try GeoServerStyleHeading node class
            nodeTypeList.clear();
            nodeTypeList.add(new GeoServerStyleHeadingNode(null, null, "test"));
            assertFalse(TestTool.supports(uniqueNodeTypeList, nodeTypeList, sldDataList));

            // Try GeoServerStyleNode node class
            nodeTypeList.clear();
            nodeTypeList.add(new GeoServerStyleNode(null, null, new StyleWrapper("test", "")));
            assertFalse(TestTool.supports(uniqueNodeTypeList, nodeTypeList, sldDataList));

            // Try GeoServerWorkspaceNode node class -- not style
            nodeTypeList.clear();
            nodeTypeList.add(new GeoServerWorkspaceNode(null, null, "test", false));
            assertFalse(TestTool.supports(uniqueNodeTypeList, nodeTypeList, sldDataList));

            // Try GeoServerWorkspaceNode node class -- style
            nodeTypeList.clear();
            nodeTypeList.add(new GeoServerWorkspaceNode(null, null, "test", true));
            assertFalse(TestTool.supports(uniqueNodeTypeList, nodeTypeList, sldDataList));

            // Try with no nodes
            nodeTypeList.clear();
            uniqueNodeTypeList.clear();
            assertFalse(TestTool.supports(uniqueNodeTypeList, nodeTypeList, sldDataList));

            // Try with null
            assertFalse(TestTool.supports(uniqueNodeTypeList, null, sldDataList));
        } catch (SecurityException e) {
            fail(e.getStackTrace().toString());
        } catch (FileNotFoundException e) {
            fail(e.getStackTrace().toString());
        }
    }
}
