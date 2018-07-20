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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import com.sldeditor.common.NodeInterface;
import com.sldeditor.common.SLDDataInterface;
import com.sldeditor.common.data.SLDData;
import com.sldeditor.common.data.StyleWrapper;
import com.sldeditor.datasource.extension.filesystem.node.database.DatabaseFeatureClassNode;
import com.sldeditor.datasource.extension.filesystem.node.file.FileTreeNode;
import com.sldeditor.datasource.extension.filesystem.node.file.FileTreeNodeTypeEnum;
import com.sldeditor.datasource.extension.filesystem.node.geoserver.GeoServerStyleHeadingNode;
import com.sldeditor.datasource.extension.filesystem.node.geoserver.GeoServerStyleNode;
import com.sldeditor.datasource.extension.filesystem.node.geoserver.GeoServerWorkspaceNode;
import com.sldeditor.tool.savesld.SaveSLDTool;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * Unit test for ScaleTool class.
 *
 * <p>{@link com.sldeditor.tool.savesld.SaveSLDTool}
 *
 * @author Robert Ward (SCISYS)
 */
public class SaveSLDToolTest {

    class TestSaveSLDTool extends SaveSLDTool {

        /** Instantiates a new test save sld tool. */
        public TestSaveSLDTool() {
            super();
        }

        /**
         * Checks if button enabled.
         *
         * @return the bool
         */
        public boolean isButtonEnabled() {
            return saveAllSLD.isEnabled();
        }
    }

    /** Test method for {@link com.sldeditor.tool.savesld.SaveSLDTool#getPanel()}. */
    @Test
    public void testGetPanel() {
        SaveSLDTool tool = new SaveSLDTool();
        assertNotNull(tool.getPanel());
    }

    /**
     * Test method for {@link
     * com.sldeditor.tool.savesld.SaveSLDTool#setSelectedItems(java.util.List, java.util.List)}.
     */
    @Test
    public void testSetSelectedItems() {}

    /** Test method for {@link com.sldeditor.tool.savesld.SaveSLDTool#getToolName()}. */
    @Test
    public void testGetToolName() {
        SaveSLDTool tool = new SaveSLDTool();
        assertNotNull(tool.getToolName());
    }

    /**
     * Test method for {@link com.sldeditor.tool.savesld.SaveSLDTool#supports(java.util.List,
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

            FileTreeNode sldTreeNode = new FileTreeNode(new File("/test"), "test.sld");
            sldTreeNode.setFileCategory(FileTreeNodeTypeEnum.SLD);

            List<Class<?>> uniqueNodeTypeList = new ArrayList<Class<?>>();
            List<NodeInterface> nodeTypeList = new ArrayList<NodeInterface>();
            List<SLDDataInterface> sldDataList = new ArrayList<SLDDataInterface>();

            // Try vector file
            nodeTypeList.add(vectorTreeNode);
            SaveSLDTool tool = new SaveSLDTool();
            assertFalse(tool.supports(uniqueNodeTypeList, nodeTypeList, sldDataList));

            // Try raster file
            nodeTypeList.clear();
            nodeTypeList.add(rasterTreeNode);
            assertFalse(tool.supports(uniqueNodeTypeList, nodeTypeList, sldDataList));

            // Try SLD file
            nodeTypeList.clear();
            nodeTypeList.add(sldTreeNode);
            assertTrue(tool.supports(uniqueNodeTypeList, nodeTypeList, sldDataList));

            // Try database feature class
            nodeTypeList.clear();
            DatabaseFeatureClassNode databaseFeatureClassNode =
                    new DatabaseFeatureClassNode(null, null, "db fc");
            nodeTypeList.add(databaseFeatureClassNode);
            assertFalse(tool.supports(uniqueNodeTypeList, nodeTypeList, sldDataList));

            // Try GeoServerStyleHeadingNode node class
            nodeTypeList.clear();
            nodeTypeList.add(new GeoServerStyleHeadingNode(null, null, "test"));
            assertTrue(tool.supports(uniqueNodeTypeList, nodeTypeList, sldDataList));

            // Try GeoServerStyleNode node class
            nodeTypeList.clear();
            nodeTypeList.add(new GeoServerStyleNode(null, null, new StyleWrapper("test", "")));
            assertTrue(tool.supports(uniqueNodeTypeList, nodeTypeList, sldDataList));

            // Try GeoServerWorkspaceNode node class -- not style
            nodeTypeList.clear();
            nodeTypeList.add(new GeoServerWorkspaceNode(null, null, "test", false));
            assertFalse(tool.supports(uniqueNodeTypeList, nodeTypeList, sldDataList));

            // Try GeoServerWorkspaceNode node class -- style
            nodeTypeList.clear();
            nodeTypeList.add(new GeoServerWorkspaceNode(null, null, "test", true));
            assertTrue(tool.supports(uniqueNodeTypeList, nodeTypeList, sldDataList));

            // Try with no nodes
            nodeTypeList.clear();
            assertFalse(tool.supports(uniqueNodeTypeList, nodeTypeList, sldDataList));

            // Try with null
            assertFalse(tool.supports(uniqueNodeTypeList, null, sldDataList));
        } catch (SecurityException e) {
            fail(e.getStackTrace().toString());
        } catch (FileNotFoundException e) {
            fail(e.getStackTrace().toString());
        }
    }

    /**
     * Test method for {@link com.sldeditor.tool.scale.ScaleTool#setSelectedItems(java.util.List,
     * java.util.List)}.
     */
    @Test
    public void testSetSelected() {

        List<NodeInterface> nodeTypeList = new ArrayList<NodeInterface>();
        List<SLDDataInterface> sldDataList = new ArrayList<SLDDataInterface>();

        try {
            FileTreeNode sldTreeNode = new FileTreeNode(new File("/test"), "test.sld");
            sldTreeNode.setFileCategory(FileTreeNodeTypeEnum.SLD);
            nodeTypeList.add(sldTreeNode);

            sldDataList.add(new SLDData(null, ""));
            TestSaveSLDTool scaleTool = new TestSaveSLDTool();
            assertFalse(scaleTool.isButtonEnabled());

            scaleTool.setSelectedItems(nodeTypeList, sldDataList);
            assertTrue(scaleTool.isButtonEnabled());
        } catch (SecurityException e) {
            fail(e.getStackTrace().toString());
        } catch (FileNotFoundException e) {
            fail(e.getStackTrace().toString());
        }
    }
}
