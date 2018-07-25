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

package com.sldeditor.test.unit.tool.connectionlist;

import static org.junit.jupiter.api.Assertions.*;

import com.sldeditor.common.NodeInterface;
import com.sldeditor.common.SLDDataInterface;
import com.sldeditor.common.data.GeoServerConnection;
import com.sldeditor.common.data.SLDData;
import com.sldeditor.common.data.StyleWrapper;
import com.sldeditor.datasource.extension.filesystem.GeoServerConnectUpdateInterface;
import com.sldeditor.datasource.extension.filesystem.node.database.DatabaseFeatureClassNode;
import com.sldeditor.datasource.extension.filesystem.node.file.FileTreeNode;
import com.sldeditor.datasource.extension.filesystem.node.file.FileTreeNodeTypeEnum;
import com.sldeditor.datasource.extension.filesystem.node.geoserver.GeoServerNode;
import com.sldeditor.datasource.extension.filesystem.node.geoserver.GeoServerStyleHeadingNode;
import com.sldeditor.datasource.extension.filesystem.node.geoserver.GeoServerStyleNode;
import com.sldeditor.datasource.extension.filesystem.node.geoserver.GeoServerWorkspaceNode;
import com.sldeditor.tool.connectionlist.ConnectorDetailsPanel;
import com.sldeditor.tool.connectionlist.GeoServerConnectionListTool;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * The Class GeoServerConnectionListToolTest.
 *
 * @author Robert Ward (SCISYS)
 */
class GeoServerConnectionListToolTest {

    class TestTool extends GeoServerConnectionListTool {

        /**
         * Instantiates a new test scale tool.
         *
         * @param application the application
         */
        public TestTool(GeoServerConnectUpdateInterface updateInterface) {
            super(updateInterface);
        }

        /* (non-Javadoc)
         * @see com.sldeditor.tool.connectionlist.GeoServerConnectionListTool#addNewButtonPressed()
         */
        @Override
        protected void addNewButtonPressed() {
            super.addNewButtonPressed();
        }

        /* (non-Javadoc)
         * @see com.sldeditor.tool.connectionlist.GeoServerConnectionListTool#duplicateButtonPressed()
         */
        @Override
        protected void duplicateButtonPressed() {
            super.duplicateButtonPressed();
        }

        /* (non-Javadoc)
         * @see com.sldeditor.tool.connectionlist.GeoServerConnectionListTool#editButtonPressed()
         */
        @Override
        protected void editButtonPressed() {
            super.editButtonPressed();
        }

        /* (non-Javadoc)
         * @see com.sldeditor.tool.connectionlist.GeoServerConnectionListTool#deleteButtonPressed()
         */
        @Override
        protected void deleteButtonPressed() {
            super.deleteButtonPressed();
        }

        public boolean isDuplicateButtonEnabled() {
            return btnDuplicate.isEnabled();
        }

        public boolean isDeleteButtonEnabled() {
            return btnDelete.isEnabled();
        }

        public boolean isEditButtonEnabled() {
            return btnEdit.isEnabled();
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
            assertFalse(TestTool.supports(uniqueNodeTypeList, nodeTypeList, sldDataList));

            // Try with null
            assertFalse(TestTool.supports(uniqueNodeTypeList, null, sldDataList));

            uniqueNodeTypeList.add(String.class);
            assertTrue(TestTool.supports(uniqueNodeTypeList, null, sldDataList));

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
            assertFalse(testTool.isEditButtonEnabled());
            assertFalse(testTool.isDuplicateButtonEnabled());
            assertFalse(testTool.isDeleteButtonEnabled());

            testTool.setSelectedItems(nodeTypeList, sldDataList);
            assertFalse(testTool.isEditButtonEnabled());
            assertFalse(testTool.isDuplicateButtonEnabled());
            assertFalse(testTool.isDeleteButtonEnabled());

            // Try a GeoServerNode - it should all come alive
            nodeTypeList.clear();
            nodeTypeList.add(new GeoServerNode(null, null));
            testTool.setSelectedItems(nodeTypeList, sldDataList);
            assertTrue(testTool.isEditButtonEnabled());
            assertTrue(testTool.isDuplicateButtonEnabled());
            assertTrue(testTool.isDeleteButtonEnabled());

            // Try another GeoServerNode
            nodeTypeList.add(new GeoServerNode(null, null));
            testTool.setSelectedItems(nodeTypeList, sldDataList);
            assertFalse(testTool.isEditButtonEnabled());
            assertFalse(testTool.isDuplicateButtonEnabled());
            assertTrue(testTool.isDeleteButtonEnabled());

        } catch (SecurityException e) {
            fail(e.getStackTrace().toString());
        } catch (FileNotFoundException e) {
            fail(e.getStackTrace().toString());
        }
    }

    class TestReceiver implements GeoServerConnectUpdateInterface {
        public List<GeoServerConnection> currentConnectionList =
                new ArrayList<GeoServerConnection>();

        /* (non-Javadoc)
         * @see com.sldeditor.datasource.extension.filesystem.GeoServerConnectUpdateInterface#getConnectionDetails()
         */
        @Override
        public List<GeoServerConnection> getConnectionDetails() {
            return currentConnectionList;
        }

        /* (non-Javadoc)
         * @see com.sldeditor.datasource.extension.filesystem.GeoServerConnectUpdateInterface#addNewConnection(com.sldeditor.common.data.GeoServerConnection)
         */
        @Override
        public void addNewConnection(GeoServerConnection newConnectionDetails) {
            currentConnectionList.add(newConnectionDetails);
        }

        /* (non-Javadoc)
         * @see com.sldeditor.datasource.extension.filesystem.GeoServerConnectUpdateInterface#updateConnectionDetails(com.sldeditor.common.data.GeoServerConnection, com.sldeditor.common.data.GeoServerConnection)
         */
        @Override
        public void updateConnectionDetails(
                GeoServerConnection originalConnectionDetails,
                GeoServerConnection newConnectionDetails) {
            currentConnectionList.remove(originalConnectionDetails);
            currentConnectionList.add(newConnectionDetails);
        }

        /* (non-Javadoc)
         * @see com.sldeditor.datasource.extension.filesystem.GeoServerConnectUpdateInterface#deleteConnections(java.util.List)
         */
        @Override
        public void deleteConnections(List<GeoServerConnection> connectionList) {
            for (GeoServerConnection connection : connectionList) {
                if (currentConnectionList.contains(connection)) {
                    currentConnectionList.remove(connection);
                }
            }
        }
    }

    @Test
    public void testButtons() {
        ConnectorDetailsPanel.setInTestMode(true);

        TestTool testTool = new TestTool(null);
        testTool.addNewButtonPressed();
        testTool.editButtonPressed();
        testTool.deleteButtonPressed();
        testTool.duplicateButtonPressed();

        TestReceiver receiver = new TestReceiver();
        testTool = new TestTool(receiver);

        assertTrue(receiver.currentConnectionList.isEmpty());

        testTool.addNewButtonPressed();
        assertEquals(1, receiver.currentConnectionList.size());

        List<NodeInterface> nodeTypeList = new ArrayList<NodeInterface>();

        // Try duplicating
        nodeTypeList.clear();
        GeoServerNode geoserverNode =
                new GeoServerNode(null, receiver.currentConnectionList.get(0));
        nodeTypeList.add(geoserverNode);
        testTool.setSelectedItems(nodeTypeList, null);

        testTool.duplicateButtonPressed();
        assertEquals(2, receiver.currentConnectionList.size());

        // Editing
        nodeTypeList.clear();
        nodeTypeList.add(new GeoServerNode(null, receiver.currentConnectionList.get(0)));
        nodeTypeList.add(new GeoServerNode(null, receiver.currentConnectionList.get(1)));
        testTool.setSelectedItems(nodeTypeList, null);

        testTool.editButtonPressed();
        assertEquals(2, receiver.currentConnectionList.size());

        // Delete
        nodeTypeList.clear();
        nodeTypeList.add(new GeoServerNode(null, receiver.currentConnectionList.get(0)));
        nodeTypeList.add(new GeoServerNode(null, receiver.currentConnectionList.get(1)));
        testTool.setSelectedItems(nodeTypeList, null);

        testTool.deleteButtonPressed();
        assertEquals(0, receiver.currentConnectionList.size());

        ConnectorDetailsPanel.setInTestMode(false);
    }
}
