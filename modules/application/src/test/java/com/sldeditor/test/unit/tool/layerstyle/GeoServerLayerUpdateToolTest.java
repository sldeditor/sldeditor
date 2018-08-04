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

package com.sldeditor.test.unit.tool.layerstyle;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import com.sldeditor.common.NodeInterface;
import com.sldeditor.common.SLDDataInterface;
import com.sldeditor.common.data.GeoServerConnection;
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
import com.sldeditor.tool.layerstyle.ConfigureLayerStyleDialog;
import com.sldeditor.tool.layerstyle.GeoServerLayerUpdateInterface;
import com.sldeditor.tool.layerstyle.GeoServerLayerUpdateTool;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

/**
 * The Class GeoServerLayerUpdateToolTest, unit test for GeoServerLayerUpdateTool
 *
 * @author Robert Ward (SCISYS)
 */
class GeoServerLayerUpdateToolTest {

    class TestGeoServerLayerUpdateInterface implements GeoServerLayerUpdateInterface {
        public List<GeoServerLayer> updatedLayerList = null;

        /* (non-Javadoc)
         * @see com.sldeditor.tool.layerstyle.GeoServerLayerUpdateInterface#getStyleMap(com.sldeditor.common.data.GeoServerConnection)
         */
        @Override
        public Map<String, List<StyleWrapper>> getStyleMap(GeoServerConnection connection) {
            Map<String, List<StyleWrapper>> map = new HashMap<String, List<StyleWrapper>>();

            ArrayList<StyleWrapper> list = new ArrayList<StyleWrapper>();
            list.add(new StyleWrapper("workspace", "a.sld"));
            list.add(new StyleWrapper("workspace", "b.sld"));
            list.add(new StyleWrapper("workspace", "c.sld"));

            map.put("workspace", list);

            ArrayList<StyleWrapper> list2 = new ArrayList<StyleWrapper>();
            list2.add(new StyleWrapper("workspace2", "x.sld"));
            list2.add(new StyleWrapper("workspace2", "y.sld"));
            list2.add(new StyleWrapper("workspace2", "z.sld"));

            map.put("workspace2", list2);

            return map;
        }

        /* (non-Javadoc)
         * @see com.sldeditor.tool.layerstyle.GeoServerLayerUpdateInterface#updateLayerStyle(java.util.List)
         */
        @Override
        public void updateLayerStyle(List<GeoServerLayer> layerList) {
            updatedLayerList = layerList;
        }
    }

    class TestTool extends GeoServerLayerUpdateTool {

        /**
         * Instantiates a new test GeoServer Layer Update Tool tool.
         *
         * @param application the application
         */
        public TestTool(GeoServerLayerUpdateInterface updateInterface) {
            super(updateInterface);
        }

        public List<GeoServerLayer> getLayerList() {
            return layerList;
        }

        public GeoServerConnection getConnection() {
            return connection;
        }

        /* (non-Javadoc)
         * @see com.sldeditor.tool.layerstyle.GeoServerLayerUpdateTool#layerUpdateButtonPressed()
         */
        @Override
        protected void layerUpdateButtonPressed() {
            super.layerUpdateButtonPressed();
        }
    }

    /**
     * Test method for {@link
     * com.sldeditor.tool.connectionlist.GeoServerConnectionListTool#getPanel()}.
     */
    @Test
    public void testGetPanel() {
        TestTool tool = new TestTool(null);
        assertNotNull(tool.getPanel());
    }

    /**
     * Test method for {@link
     * com.sldeditor.tool.connectionlist.GeoServerConnectionListTool#getToolName()}.
     */
    @Test
    public void testGetToolName() {
        TestTool tool = new TestTool(null);
        assertNotNull(tool.getToolName());
    }

    /**
     * Test method for {@link
     * com.sldeditor.tool.connectionlist.GeoServerConnectionListTool#supports(java.util.List,
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
            TestTool TestTool = new TestTool(null);
            assertFalse(TestTool.supports(uniqueNodeTypeList, nodeTypeList, sldDataList));

            uniqueNodeTypeList.add(String.class);

            // Try raster file
            nodeTypeList.clear();
            nodeTypeList.add(rasterTreeNode);
            assertFalse(TestTool.supports(uniqueNodeTypeList, nodeTypeList, sldDataList));

            // Try SLD file
            nodeTypeList.clear();
            nodeTypeList.add(sldTreeNode);
            assertFalse(TestTool.supports(uniqueNodeTypeList, nodeTypeList, sldDataList));

            // Try database feature class
            nodeTypeList.clear();
            DatabaseFeatureClassNode databaseFeatureClassNode =
                    new DatabaseFeatureClassNode(null, null, "db fc");
            nodeTypeList.add(databaseFeatureClassNode);
            assertFalse(TestTool.supports(uniqueNodeTypeList, nodeTypeList, sldDataList));

            // Try GeoServerLayerHeadingNode node class
            nodeTypeList.clear();
            nodeTypeList.add(new GeoServerLayerHeadingNode(null, null, "test"));
            assertTrue(TestTool.supports(uniqueNodeTypeList, nodeTypeList, sldDataList));

            // Try GeoServerLayerNode node class
            nodeTypeList.clear();
            nodeTypeList.add(new GeoServerLayerNode(null, new GeoServerLayer()));
            assertTrue(TestTool.supports(uniqueNodeTypeList, nodeTypeList, sldDataList));

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
            assertTrue(TestTool.supports(uniqueNodeTypeList, nodeTypeList, sldDataList));

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

    /**
     * Test method for {@link
     * com.sldeditor.tool.connectionlist.GeoServerConnectionListTool#setSelectedItems(java.util.List,
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
            TestTool testTool = new TestTool(null);
            assertNull(testTool.getConnection());
            assertTrue(testTool.getLayerList().isEmpty());

            testTool.setSelectedItems(nodeTypeList, sldDataList);
            assertNull(testTool.getConnection());
            assertTrue(testTool.getLayerList().isEmpty());
        } catch (SecurityException e) {
            fail(e.getStackTrace().toString());
        } catch (FileNotFoundException e) {
            fail(e.getStackTrace().toString());
        }
    }

    /**
     * Test method for {@link
     * com.sldeditor.tool.connectionlist.GeoServerConnectionListTool#setSelectedItems(java.util.List,
     * java.util.List)}.
     */
    @Test
    public void testSetSelectedWorkspace() {

        List<NodeInterface> nodeTypeList = new ArrayList<NodeInterface>();
        List<SLDDataInterface> sldDataList = new ArrayList<SLDDataInterface>();

        TestTool testTool = new TestTool(null);
        assertNull(testTool.getConnection());
        assertTrue(testTool.getLayerList().isEmpty());

        GeoServerLayer expectedLayer = new GeoServerLayer("workspace", "layer name");
        GeoServerConnection expectedConnection = new GeoServerConnection();
        expectedConnection.setConnectionName("test");
        expectedLayer.setConnection(expectedConnection);

        // Try a GeoServerWorkspaceNode -- not style
        nodeTypeList.clear();
        GeoServerWorkspaceNode expectedWorkspaceNode =
                new GeoServerWorkspaceNode(null, expectedConnection, "workspace", false);

        GeoServerLayerNode expectedLayerNode1 = new GeoServerLayerNode(null, expectedLayer);
        expectedWorkspaceNode.add(expectedLayerNode1);
        GeoServerLayer expectedLayer2 = new GeoServerLayer("workspace", "layer name 2");
        GeoServerLayerNode expectedLayerNode2 = new GeoServerLayerNode(null, expectedLayer2);
        expectedWorkspaceNode.add(expectedLayerNode2);

        GeoServerLayer expectedLayer3 = new GeoServerLayer("workspace", "layer name 3");
        GeoServerLayerNode expectedLayerNode3 = new GeoServerLayerNode(null, expectedLayer3);
        expectedWorkspaceNode.add(expectedLayerNode3);

        nodeTypeList.add(expectedWorkspaceNode);
        testTool.setSelectedItems(nodeTypeList, sldDataList);
        assertEquals(expectedConnection, testTool.getConnection());
        assertEquals(3, testTool.getLayerList().size());

        // Try a GeoServerWorkspaceNode -- style
        nodeTypeList.clear();
        expectedWorkspaceNode =
                new GeoServerWorkspaceNode(null, expectedConnection, "workspace", true);

        expectedWorkspaceNode.add(expectedLayerNode1);
        expectedWorkspaceNode.add(expectedLayerNode2);
        expectedWorkspaceNode.add(expectedLayerNode3);

        nodeTypeList.add(expectedWorkspaceNode);
        testTool.setSelectedItems(nodeTypeList, sldDataList);
        assertNull(testTool.getConnection());
        assertEquals(0, testTool.getLayerList().size());
    }

    /**
     * Test method for {@link
     * com.sldeditor.tool.connectionlist.GeoServerConnectionListTool#setSelectedItems(java.util.List,
     * java.util.List)}.
     */
    @Test
    public void testSetSelectedLayerNode() {

        List<NodeInterface> nodeTypeList = new ArrayList<NodeInterface>();
        List<SLDDataInterface> sldDataList = new ArrayList<SLDDataInterface>();

        TestTool testTool = new TestTool(null);
        assertNull(testTool.getConnection());
        assertTrue(testTool.getLayerList().isEmpty());

        // Try a GeoServerLayerNode - it should all come alive
        GeoServerLayer expectedLayer = new GeoServerLayer("workspace", "layer name");
        GeoServerConnection expectedConnection = new GeoServerConnection();
        expectedConnection.setConnectionName("test");
        expectedLayer.setConnection(expectedConnection);
        nodeTypeList.add(new GeoServerLayerNode(null, expectedLayer));
        testTool.setSelectedItems(nodeTypeList, sldDataList);
        assertEquals(expectedConnection, testTool.getConnection());
        assertEquals(1, testTool.getLayerList().size());
    }

    /**
     * Test method for {@link
     * com.sldeditor.tool.connectionlist.GeoServerConnectionListTool#setSelectedItems(java.util.List,
     * java.util.List)}.
     */
    @Test
    public void testSetSelectedHeadingNode() {

        List<NodeInterface> nodeTypeList = new ArrayList<NodeInterface>();
        List<SLDDataInterface> sldDataList = new ArrayList<SLDDataInterface>();

        TestTool testTool = new TestTool(null);
        assertNull(testTool.getConnection());
        assertTrue(testTool.getLayerList().isEmpty());

        GeoServerLayer expectedLayer = new GeoServerLayer("workspace", "layer name");
        GeoServerConnection expectedConnection = new GeoServerConnection();
        expectedConnection.setConnectionName("test");
        expectedLayer.setConnection(expectedConnection);

        // Try a GeoServerWorkspaceNode -- not style
        GeoServerWorkspaceNode expectedWorkspaceNode =
                new GeoServerWorkspaceNode(null, expectedConnection, "workspace", false);

        GeoServerLayerNode expectedLayerNode1 = new GeoServerLayerNode(null, expectedLayer);
        expectedWorkspaceNode.add(expectedLayerNode1);
        GeoServerLayer expectedLayer2 = new GeoServerLayer("workspace", "layer name 2");
        GeoServerLayerNode expectedLayerNode2 = new GeoServerLayerNode(null, expectedLayer2);
        expectedWorkspaceNode.add(expectedLayerNode2);

        GeoServerLayer expectedLayer3 = new GeoServerLayer("workspace", "layer name 3");
        GeoServerLayerNode expectedLayerNode3 = new GeoServerLayerNode(null, expectedLayer3);
        expectedWorkspaceNode.add(expectedLayerNode3);

        // Try a GeoServerLayerHeadingNode
        GeoServerLayerHeadingNode headingNode =
                new GeoServerLayerHeadingNode(null, expectedConnection, "Heading");

        GeoServerWorkspaceNode expectedWorkspaceNode2 =
                new GeoServerWorkspaceNode(null, expectedConnection, "workspace 2", false);

        GeoServerLayer expectedLayer4 = new GeoServerLayer("workspace 2", "layer name 4");
        GeoServerLayerNode expectedLayerNode4 = new GeoServerLayerNode(null, expectedLayer4);
        expectedWorkspaceNode2.add(expectedLayerNode4);

        GeoServerLayer expectedLayer5 = new GeoServerLayer("workspace 2", "layer name 5");
        GeoServerLayerNode expectedLayerNode5 = new GeoServerLayerNode(null, expectedLayer5);
        expectedWorkspaceNode2.add(expectedLayerNode5);

        headingNode.add(expectedWorkspaceNode);
        headingNode.add(expectedWorkspaceNode2);

        nodeTypeList.add(headingNode);

        testTool.setSelectedItems(nodeTypeList, sldDataList);
        assertEquals(expectedConnection, testTool.getConnection());
        assertEquals(5, testTool.getLayerList().size());
    }

    @Test
    public void testButtonPressed() {

        ConfigureLayerStyleDialog.setInTestMode(true);
        TestTool testTool = new TestTool(null);
        testTool.layerUpdateButtonPressed();

        TestGeoServerLayerUpdateInterface receiver = new TestGeoServerLayerUpdateInterface();

        testTool = new TestTool(receiver);
        testTool.layerUpdateButtonPressed();
        assertTrue(receiver.updatedLayerList == null);
        ConfigureLayerStyleDialog.setInTestMode(false);
    }
}
