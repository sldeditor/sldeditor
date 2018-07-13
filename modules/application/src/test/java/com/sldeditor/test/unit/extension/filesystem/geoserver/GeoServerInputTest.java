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

package com.sldeditor.test.unit.extension.filesystem.geoserver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import com.sldeditor.common.NodeInterface;
import com.sldeditor.common.SLDDataInterface;
import com.sldeditor.common.ToolSelectionInterface;
import com.sldeditor.common.connection.GeoServerConnectionManager;
import com.sldeditor.common.data.GeoServerConnection;
import com.sldeditor.common.data.GeoServerLayer;
import com.sldeditor.common.data.SLDData;
import com.sldeditor.common.data.StyleWrapper;
import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.common.property.PropertyManagerFactory;
import com.sldeditor.datasource.extension.filesystem.node.FSTree;
import com.sldeditor.datasource.extension.filesystem.node.file.FileTreeNode;
import com.sldeditor.datasource.extension.filesystem.node.geoserver.GeoServerNode;
import com.sldeditor.datasource.extension.filesystem.node.geoserver.GeoServerStyleHeadingNode;
import com.sldeditor.datasource.extension.filesystem.node.geoserver.GeoServerStyleNode;
import com.sldeditor.datasource.extension.filesystem.node.geoserver.GeoServerWorkspaceNode;
import com.sldeditor.extension.filesystem.geoserver.GeoServerInput;
import com.sldeditor.extension.filesystem.geoserver.client.GeoServerClientInterface;
import com.sldeditor.test.unit.extension.filesystem.file.sld.SLDFileHandlerTest;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.MenuElement;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit test for GeoServerInput class.
 *
 * <p>{@link com.sldeditor.extension.filesystem.geoserver.GeoServerInput}
 *
 * @author Robert Ward (SCISYS)
 */
public class GeoServerInputTest {

    /** The config properties file. */
    private File configPropertiesFile = new File("./GeoServerInputTest.properties");

    /** The Class DummyGeoServerInput. */
    class DummyGeoServerInput extends GeoServerInput {

        /** The Constant serialVersionUID. */
        private static final long serialVersionUID = 1L;

        /**
         * Instantiates a new dummy geo server input.
         *
         * @param toolMgr the tool mgr
         */
        public DummyGeoServerInput(ToolSelectionInterface toolMgr) {
            super(toolMgr);
        }

        /**
         * Test remove style file extension.
         *
         * @param styleWrapper the style wrapper
         */
        public void testRemoveStyleFileExtension(StyleWrapper styleWrapper) {
            super.removeStyleFileExtension(styleWrapper);
        }
    }

    /** Called before each test. */
    @BeforeEach
    public void beforeEachTest() {
        GeoServerConnectionManager.destroyInstance();
        PropertyManagerFactory.getInstance().setPropertyFile(configPropertiesFile);
    }

    /** Called after each test. */
    @AfterEach
    public void afterEachTest() {
        configPropertiesFile.delete();
        GeoServerConnectionManager.destroyInstance();
        PropertyManagerFactory.destroyInstance();
    }

    /**
     * Test method for {@link
     * com.sldeditor.extension.filesystem.geoserver.GeoServerInput#GeoServerInput(com.sldeditor.common.ToolSelectionInterface)}.
     */
    @Test
    public void testGeoServerInput() {
        GeoServerInput input = new GeoServerInput(null);
        GeoServerInput.overrideGeoServerClientClass(DummyGeoServerClient.class);

        FSTree tree = new FSTree();

        DefaultMutableTreeNode rootNode;
        try {
            rootNode = new DefaultMutableTreeNode("Root");

            DefaultTreeModel model = new DefaultTreeModel(rootNode);

            input.populate(tree, model, rootNode);

            URL url = SLDFileHandlerTest.class.getResource("/sld/point_attribute.sld");

            List<SLDDataInterface> sldDataList = input.open(url);

            assertNull(sldDataList);

            GeoServerConnection connection1 = new GeoServerConnection();
            connection1.setConnectionName("test connection 1");
            StyleWrapper styleWrapper = new StyleWrapper("workspace", "layer1");
            GeoServerStyleNode styleTreeNode =
                    new GeoServerStyleNode(input, connection1, styleWrapper);

            // Try with no known GeoServer connections
            assertNull(input.getSLDContents(styleTreeNode));

            // Add some GeoServer connections
            input.addNewConnection(connection1);

            GeoServerConnection connection2 = new GeoServerConnection();
            connection2.setConnectionName("test connection 2");
            input.addNewConnection(connection2);

            List<SLDDataInterface> sldDataContentsList =
                    input.getSLDContents(styleTreeNode).getSldData();
            assertEquals(1, sldDataContentsList.size());

            SLDData sldData = (SLDData) sldDataContentsList.get(0);

            // Try saving a null object
            assertFalse(input.save(null));

            // Save valid sld data
            assertTrue(input.save(sldData));

            // Try and save to a connection that does not exist
            GeoServerConnection connection3 = new GeoServerConnection();
            connection2.setConnectionName("test connection 3");

            sldData.setConnectionData(connection3);

            assertFalse(input.save(sldData));

            // Check how many connections we have
            assertEquals(2, input.getConnectionDetails().size());

            // Try the other tree node types
            GeoServerWorkspaceNode workspaceNode =
                    new GeoServerWorkspaceNode(input, connection1, "test workspace", true);
            assertNotNull(input.getSLDContents(workspaceNode));

            GeoServerStyleHeadingNode styleHeadingNode =
                    new GeoServerStyleHeadingNode(null, null, "test");
            assertNotNull(input.getSLDContents(styleHeadingNode));

            GeoServerNode geoserverNode = new GeoServerNode(input, connection1);
            assertNotNull(input.getSLDContents(geoserverNode));
        } catch (SecurityException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    /**
     * Test method for {@link
     * com.sldeditor.extension.filesystem.geoserver.GeoServerInput#treeExpanded(java.lang.Object)}.
     */
    @Test
    public void testTreeExpanded() {
        // Hard to test
    }

    /**
     * Test method for {@link
     * com.sldeditor.extension.filesystem.geoserver.GeoServerInput#rightMouseButton(java.lang.Object,
     * java.awt.event.MouseEvent)}.
     */
    @Test
    public void testRightMouseButton() {
        GeoServerInput input = new GeoServerInput(null);
        GeoServerInput.overrideGeoServerClientClass(DummyGeoServerClient.class);

        FSTree tree = new FSTree();

        DefaultMutableTreeNode rootNode;
        try {
            rootNode = new DefaultMutableTreeNode("Root");

            DefaultTreeModel model = new DefaultTreeModel(rootNode);

            input.populate(tree, model, rootNode);

            GeoServerConnection connection1 = new GeoServerConnection();
            String expectedConnectionName = "test connection 1";
            connection1.setConnectionName(expectedConnectionName);

            GeoServerNode geoserverNode = new GeoServerNode(input, connection1);

            // Add some GeoServer connections
            input.addNewConnection(connection1);

            input.rightMouseButton(null, null, null);

            // GeoServer node -- connected to client
            JPopupMenu menu = new JPopupMenu();
            input.rightMouseButton(menu, geoserverNode, null);

            int actual = menu.getComponentCount();
            assertEquals(1, actual);
            JMenuItem menuItem = (JMenuItem) menu.getComponent(0);
            String actualString = menuItem.getText();
            assertEquals(
                    Localisation.getString(GeoServerInput.class, "GeoServerInput.disconnect"),
                    actualString);

            // GeoServer node -- not connected to client
            GeoServerClientInterface client =
                    GeoServerConnectionManager.getInstance().getConnectionMap().get(connection1);
            client.disconnect();

            menu = new JPopupMenu();
            input.rightMouseButton(menu, geoserverNode, null);

            actual = menu.getComponentCount();
            assertEquals(1, actual);
            actualString = menuItem.getText();
            assertEquals(
                    Localisation.getString(GeoServerInput.class, "GeoServerInput.disconnect"),
                    actualString);

            // FileTreeNode
            try {
                // File tree node with unsupported file name
                FileTreeNode ftn = new FileTreeNode(new File(""), "unknown file");

                menu = new JPopupMenu();
                input.rightMouseButton(menu, ftn, null);

                // File tree node with sld file -- disconnected from GeoServer
                ftn = new FileTreeNode(new File(""), "test.sld");

                menu = new JPopupMenu();
                input.rightMouseButton(menu, ftn, null);

                actual = menu.getComponentCount();
                menuItem = (JMenuItem) menu.getComponent(0);

                assertEquals(1, actual);
                actualString = menuItem.getText();
                assertEquals(
                        Localisation.getString(
                                GeoServerInput.class, "GeoServerInput.uploadToGeoServer"),
                        actualString);

                MenuElement[] children = menuItem.getSubElements();
                assertEquals(1, children.length);
                JMenuItem menuItem2 = (JMenuItem) ((JPopupMenu) children[0]).getComponent(0);
                actualString = menuItem2.getText();
                assertEquals(expectedConnectionName, actualString);

                // File tree node with sld file -- connected to GeoServer
                client.connect();
                String expectedWorkspace = "workspace1";
                ((DummyGeoServerClient) client).workspaceList.add(expectedWorkspace);
                menu = new JPopupMenu();
                input.rightMouseButton(menu, ftn, null);

                actual = menu.getComponentCount();
                menuItem = (JMenuItem) menu.getComponent(0);

                assertEquals(1, actual);
                actualString = menuItem.getText();
                assertEquals(
                        Localisation.getString(
                                GeoServerInput.class, "GeoServerInput.uploadToGeoServer"),
                        actualString);

                children = menuItem.getSubElements();
                assertEquals(1, children.length);
                menuItem2 = (JMenuItem) ((JPopupMenu) children[0]).getComponent(0);
                actualString = menuItem2.getText();
                assertEquals(expectedConnectionName, actualString);
                // Check for workspace names
                children = menuItem2.getSubElements();
                assertEquals(1, children.length);
                JMenuItem menuItem3 = (JMenuItem) ((JPopupMenu) children[0]).getComponent(0);
                actualString = menuItem3.getText();
                assertEquals(expectedWorkspace, actualString);

                // File tree node with sld file -- no GeoServer connections
                GeoServerConnectionManager.getInstance().getConnectionMap().clear();
                menu = new JPopupMenu();
                input.rightMouseButton(menu, ftn, null);

                actual = menu.getComponentCount();
                menuItem = (JMenuItem) menu.getComponent(0);

                assertEquals(1, actual);
                actualString = menuItem.getText();
                assertEquals(
                        Localisation.getString(
                                GeoServerInput.class, "GeoServerInput.uploadToGeoServer"),
                        actualString);

                children = menuItem.getSubElements();
                assertEquals(1, children.length);
                menuItem2 = (JMenuItem) ((JPopupMenu) children[0]).getComponent(0);
                actualString = menuItem2.getText();
                assertEquals(
                        Localisation.getString(
                                GeoServerInput.class, "GeoServerInput.noGeoServerConnections"),
                        actualString);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } catch (SecurityException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    /**
     * Test method for {@link
     * com.sldeditor.extension.filesystem.geoserver.GeoServerInput#getNodeTypes()}.
     */
    @Test
    public void testGetNodeTypes() {
        GeoServerInput input = new GeoServerInput(null);

        assertTrue(input.getNodeTypes().isEmpty());
    }

    /**
     * Test method for {@link
     * com.sldeditor.extension.filesystem.geoserver.GeoServerInput#connect(java.util.List)}.
     */
    @Test
    public void testConnect() {
        GeoServerInput input = new GeoServerInput(null);
        GeoServerInput.overrideGeoServerClientClass(DummyGeoServerClient.class);

        // Add some GeoServer connections
        GeoServerConnection connection1 = new GeoServerConnection();
        connection1.setConnectionName("test connection 1");
        input.addNewConnection(connection1);

        GeoServerConnection connection2 = new GeoServerConnection();
        connection2.setConnectionName("test connection 2");
        input.addNewConnection(connection2);

        // Try passing null
        input.connect(null);

        // Try connecting to one GeoServer
        List<GeoServerConnection> connectionList = new ArrayList<GeoServerConnection>();
        connectionList.add(connection1);

        // Try passing something
        input.connect(connectionList);

        // Try connecting to 2
        connectionList.add(connection2);

        input.connect(connectionList);

        assertTrue(input.isConnected(connection1));
        assertTrue(input.isConnected(connection2));
        assertFalse(input.isConnected(null));
    }

    /**
     * Test method for {@link
     * com.sldeditor.extension.filesystem.geoserver.GeoServerInput#disconnect(java.util.List)}.
     */
    @Test
    public void testDisconnect() {
        PropertyManagerFactory.getInstance().setPropertyFile(configPropertiesFile);
        GeoServerInput input = new GeoServerInput(null);
        GeoServerInput.overrideGeoServerClientClass(DummyGeoServerClient.class);

        // Add some GeoServer connections
        GeoServerConnection connection1 = new GeoServerConnection();
        connection1.setConnectionName("test connection 1");
        input.addNewConnection(connection1);

        GeoServerConnection connection2 = new GeoServerConnection();
        connection2.setConnectionName("test connection 2");
        input.addNewConnection(connection2);

        // Try passing null
        input.disconnect(null);

        // Try disconnecting from one GeoServer
        List<GeoServerConnection> connectionList = new ArrayList<GeoServerConnection>();
        connectionList.add(connection1);

        input.disconnect(connectionList);

        // Try disconnecting from to 2
        connectionList.add(connection2);

        input.disconnect(connectionList);
    }

    /**
     * Test method for {@link
     * com.sldeditor.extension.filesystem.geoserver.GeoServerInput#getStyleMap(com.sldeditor.common.data.GeoServerConnection)}.
     */
    @Test
    public void testGetStyleMap() {
        GeoServerInput input = new GeoServerInput(null);
        GeoServerInput.overrideGeoServerClientClass(DummyGeoServerClient.class);

        // Add some GeoServer connections
        GeoServerConnection connection1 = new GeoServerConnection();
        connection1.setConnectionName("test connection 1");
        input.addNewConnection(connection1);

        GeoServerConnection connection2 = new GeoServerConnection();
        connection2.setConnectionName("test connection 2");
        input.addNewConnection(connection2);

        // Try a null objects
        Map<String, List<StyleWrapper>> actualStyleMap = input.getStyleMap(null);

        assertNull(actualStyleMap);

        actualStyleMap = input.getStyleMap(connection2);
        assertNull(actualStyleMap);

        Map<String, List<StyleWrapper>> expectedStyleMap =
                new HashMap<String, List<StyleWrapper>>();
        // CHECKSTYLE:OFF
        StyleWrapper[] styleWrappers = {
            new StyleWrapper("workspace", "style1"), new StyleWrapper("workspace", "style2")
        };
        // CHECKSTYLE:ON
        expectedStyleMap.put("style1", Arrays.asList(styleWrappers));
        Map<String, List<GeoServerLayer>> expectedLayerMap =
                new HashMap<String, List<GeoServerLayer>>();
        // CHECKSTYLE:OFF
        GeoServerLayer[] geoServerLayers = {
            new GeoServerLayer("workspace", "style1"), new GeoServerLayer("workspace", "style2")
        };
        // CHECKSTYLE:ON
        expectedLayerMap.put("style1", Arrays.asList(geoServerLayers));

        input.populateComplete(connection1, expectedStyleMap, expectedLayerMap);
        actualStyleMap = input.getStyleMap(connection1);

        assertEquals(expectedStyleMap, actualStyleMap);
    }

    /**
     * Test method for {@link
     * com.sldeditor.extension.filesystem.geoserver.GeoServerInput#updateLayerStyle(com.sldeditor.common.data.StyleWrapper,
     * java.util.List)}.
     */
    @Test
    public void testUpdateLayerStyle() {
        GeoServerInput input = new GeoServerInput(null);
        GeoServerInput.overrideGeoServerClientClass(DummyGeoServerClient.class);

        // Add some GeoServer connections
        GeoServerConnection connection1 = new GeoServerConnection();
        connection1.setConnectionName("test connection 1");
        input.addNewConnection(connection1);

        GeoServerConnection connection2 = new GeoServerConnection();
        connection2.setConnectionName("test connection 2");
        input.addNewConnection(connection2);

        // Try with null objects
        input.updateLayerStyle(null);

        // CHECKSTYLE:OFF
        GeoServerLayer[] geoServerLayers = {
            new GeoServerLayer("workspace", "style1"), new GeoServerLayer("workspace", "style2")
        };
        // CHECKSTYLE:ON
        List<GeoServerLayer> layerList = Arrays.asList(geoServerLayers);
        StyleWrapper updatedStyle = new StyleWrapper("workspace", "layer1");

        for (GeoServerLayer layer : layerList) {
            layer.setStyle(updatedStyle);
        }
        input.updateLayerStyle(layerList);
    }

    /**
     * Test method for {@link
     * com.sldeditor.extension.filesystem.geoserver.GeoServerInput#updateConnectionDetails(com.sldeditor.common.data.GeoServerConnection,
     * com.sldeditor.common.data.GeoServerConnection)}.
     */
    @Test
    public void testUpdateConnectionDetails() {
        GeoServerInput input = new GeoServerInput(null);
        GeoServerInput.overrideGeoServerClientClass(DummyGeoServerClient.class);

        // Add some GeoServer connections
        GeoServerConnection connection1 = new GeoServerConnection();
        connection1.setConnectionName("test connection 1");
        input.addNewConnection(connection1);

        GeoServerConnection connection2 = new GeoServerConnection();
        connection2.setConnectionName("test connection 2");
        input.addNewConnection(connection2);

        // Try null parameters
        input.updateConnectionDetails(null, null);

        // Delete connection details
        List<GeoServerConnection> listToDelete = new ArrayList<GeoServerConnection>();
        listToDelete.add(connection1);
        input.deleteConnections(listToDelete);

        GeoServerConnection connection1Updated = new GeoServerConnection();
        connection1Updated.setConnectionName("update test connection 1");
        input.addNewConnection(connection1Updated);

        // Update the connection details
        input.updateConnectionDetails(connection1, connection1Updated);

        StyleWrapper styleWrapper = new StyleWrapper("workspace", "layer1");

        SLDData sldData = new SLDData(styleWrapper, "sld contents");
        sldData.setConnectionData(connection1);

        // Try and save with the old GeoServer connection details
        assertFalse(input.save(sldData));

        // Try and save with the new GeoServer connection details
        sldData.setConnectionData(connection1Updated);
        assertTrue(input.save(sldData));
    }

    /**
     * Test method for {@link
     * com.sldeditor.extension.filesystem.geoserver.GeoServerInput#deleteConnections(java.util.List)}.
     */
    @Test
    public void testDeleteConnections() {
        GeoServerInput input = new GeoServerInput(null);
        GeoServerInput.overrideGeoServerClientClass(DummyGeoServerClient.class);

        // Add some GeoServer connections
        GeoServerConnection connection1 = new GeoServerConnection();
        connection1.setConnectionName("test connection 1");
        input.addNewConnection(connection1);

        GeoServerConnection connection2 = new GeoServerConnection();
        connection2.setConnectionName("test connection 2");
        input.addNewConnection(connection2);

        // Try null parameters
        input.deleteConnections(null);

        // Populate some styles
        Map<String, List<StyleWrapper>> expectedStyleMap =
                new HashMap<String, List<StyleWrapper>>();
        // CHECKSTYLE:OFF
        StyleWrapper[] styleWrappers = {
            new StyleWrapper("workspace", "style1"), new StyleWrapper("workspace", "style2")
        };
        // CHECKSTYLE:ON
        expectedStyleMap.put("style1", Arrays.asList(styleWrappers));
        Map<String, List<GeoServerLayer>> expectedLayerMap =
                new HashMap<String, List<GeoServerLayer>>();
        // CHECKSTYLE:OFF
        GeoServerLayer[] geoServerLayers = {
            new GeoServerLayer("workspace", "style1"), new GeoServerLayer("workspace", "style2")
        };
        // CHECKSTYLE:ON
        expectedLayerMap.put("style1", Arrays.asList(geoServerLayers));

        input.populateComplete(connection1, expectedStyleMap, expectedLayerMap);
        input.populateComplete(connection2, expectedStyleMap, expectedLayerMap);
        Map<String, List<StyleWrapper>> actualStyleMap = input.getStyleMap(connection1);
        assertEquals(expectedStyleMap, actualStyleMap);

        // Delete a GeoServer connection
        List<GeoServerConnection> connectionList = new ArrayList<GeoServerConnection>();
        connectionList.add(connection1);

        input.deleteConnections(connectionList);

        actualStyleMap = input.getStyleMap(connection1);
        assertTrue(actualStyleMap == null);

        // Check the other connection wasn't deleted
        actualStyleMap = input.getStyleMap(connection2);
        assertEquals(1, actualStyleMap.size());
    }

    /**
     * Test method for {@link
     * com.sldeditor.extension.filesystem.geoserver.GeoServerInput#copyNodes(com.sldeditor.common.NodeInterface,
     * java.util.Map)}.
     */
    @Test
    public void testCopyNodes() {
        GeoServerInput input = new GeoServerInput(null);
        GeoServerInput.overrideGeoServerClientClass(DummyGeoServerClient.class);

        FSTree tree = new FSTree();

        DefaultMutableTreeNode rootNode;

        rootNode = new DefaultMutableTreeNode("Root");

        DefaultTreeModel model = new DefaultTreeModel(rootNode);

        input.populate(tree, model, rootNode);

        URL url = SLDFileHandlerTest.class.getResource("/sld/point_attribute.sld");

        List<SLDDataInterface> sldDataList = input.open(url);

        assertNull(sldDataList);

        GeoServerConnection connection1 = new GeoServerConnection();
        connection1.setConnectionName("test connection 1");

        // Add some GeoServer connections
        input.addNewConnection(connection1);

        // Try null parameters
        assertFalse(input.copyNodes(null, null));

        // Try with valid parameters
        GeoServerWorkspaceNode workspaceTreeNode =
                new GeoServerWorkspaceNode(input, connection1, "test workspace", false);

        // Create test data
        Map<NodeInterface, List<SLDDataInterface>> copyDataMap =
                new HashMap<NodeInterface, List<SLDDataInterface>>();
        List<SLDDataInterface> sldToCopyList = new ArrayList<SLDDataInterface>();
        StyleWrapper styleWrapper = new StyleWrapper("workspace", "layer1");

        SLDData sldData = new SLDData(styleWrapper, "sld contents");
        sldData.setConnectionData(connection1);
        sldToCopyList.add(sldData);
        copyDataMap.put(workspaceTreeNode, sldToCopyList);

        assertFalse(input.copyNodes(workspaceTreeNode, null));
        assertFalse(input.copyNodes(null, copyDataMap));

        // Try with valid parameters
        assertTrue(input.copyNodes(workspaceTreeNode, copyDataMap));
    }

    /**
     * Test method for {@link
     * com.sldeditor.extension.filesystem.geoserver.GeoServerInput#deleteNodes(com.sldeditor.common.NodeInterface,
     * java.util.List)}.
     */
    @Test
    public void testDeleteNodes() {
        GeoServerInput input = new GeoServerInput(null);
        GeoServerInput.overrideGeoServerClientClass(DummyGeoServerClient.class);

        FSTree tree = new FSTree();

        DefaultMutableTreeNode rootNode;

        rootNode = new DefaultMutableTreeNode("Root");

        DefaultTreeModel model = new DefaultTreeModel(rootNode);

        input.populate(tree, model, rootNode);

        URL url = SLDFileHandlerTest.class.getResource("/sld/point_attribute.sld");

        List<SLDDataInterface> sldDataList = input.open(url);

        assertNull(sldDataList);

        GeoServerConnection connection1 = new GeoServerConnection();
        connection1.setConnectionName("test connection 1");

        // Add some GeoServer connections
        input.addNewConnection(connection1);

        // Try null parameters
        assertFalse(input.copyNodes(null, null));

        // Try with valid parameters
        // CHECKSTYLE:OFF
        GeoServerWorkspaceNode workspaceTreeNode =
                new GeoServerWorkspaceNode(input, connection1, "test workspace", false);
        // CHECKSTYLE:ON

        // Create test data
        List<SLDDataInterface> sldToDeleteList = new ArrayList<SLDDataInterface>();
        StyleWrapper styleWrapper = new StyleWrapper("workspace", "layer1");

        SLDData sldData = new SLDData(styleWrapper, "sld contents");
        sldData.setConnectionData(connection1);
        sldToDeleteList.add(sldData);

        input.deleteNodes(null, null);
        input.deleteNodes(workspaceTreeNode, null);
        input.deleteNodes(null, sldToDeleteList);

        // Try with valid parameters
        input.deleteNodes(workspaceTreeNode, sldToDeleteList);
    }

    @Test
    public void testRemoveStyleFileExtension() {
        DummyGeoServerInput obj = new DummyGeoServerInput(null);
        obj.testRemoveStyleFileExtension(null);

        StyleWrapper wrapper = new StyleWrapper();
        obj.testRemoveStyleFileExtension(wrapper);

        String expected = "test.sld";
        wrapper.setStyle(expected);
        obj.testRemoveStyleFileExtension(wrapper);

        assertEquals("test", wrapper.getStyle());
    }
}
