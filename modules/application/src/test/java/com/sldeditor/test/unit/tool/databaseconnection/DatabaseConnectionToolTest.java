/*
 *    SLDEditor - SLD Editor application
 *
 *    (C) 2016, SCISYS
 *
 */

package com.sldeditor.test.unit.tool.databaseconnection;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import com.sldeditor.common.NodeInterface;
import com.sldeditor.common.SLDDataInterface;
import com.sldeditor.common.data.DatabaseConnection;
import com.sldeditor.common.data.StyleWrapper;
import com.sldeditor.datasource.extension.filesystem.node.database.DatabaseFeatureClassNode;
import com.sldeditor.datasource.extension.filesystem.node.database.DatabaseNode;
import com.sldeditor.datasource.extension.filesystem.node.file.FileTreeNode;
import com.sldeditor.datasource.extension.filesystem.node.file.FileTreeNodeTypeEnum;
import com.sldeditor.datasource.extension.filesystem.node.geoserver.GeoServerStyleHeadingNode;
import com.sldeditor.datasource.extension.filesystem.node.geoserver.GeoServerStyleNode;
import com.sldeditor.datasource.extension.filesystem.node.geoserver.GeoServerWorkspaceNode;
import com.sldeditor.extension.filesystem.database.DatabaseInput;
import com.sldeditor.tool.ToolManager;
import com.sldeditor.tool.databaseconnection.DatabaseConnectStateInterface;
import com.sldeditor.tool.databaseconnection.DatabaseConnectionTool;
import com.sldeditor.tool.dbconnectionlist.DatabaseConnectionFactory;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

/**
 * Unit test for DatabaseConnectionTool class.
 *
 * <p>{@link com.sldeditor.test.unit.tool.databaseconnection.DatabaseConnectionTool}
 *
 * @author Robert Ward (SCISYS)
 */
public class DatabaseConnectionToolTest {

    /** The Class TestDatabaseConnectState. */
    class TestDatabaseConnectState implements DatabaseConnectStateInterface {
        public Map<DatabaseConnection, Boolean> connectionMap =
                new HashMap<DatabaseConnection, Boolean>();

        private boolean failConnection = false;

        /*
         * (non-Javadoc)
         *
         * @see com.sldeditor.tool.databaseconnection.DatabaseConnectStateInterface#isConnected(com.sldeditor.common.data.DatabaseConnection)
         */
        @Override
        public boolean isConnected(DatabaseConnection connection) {
            if (connectionMap.containsKey(connection)) {
                return connectionMap.get(connection);
            }
            return false;
        }

        /*
         * (non-Javadoc)
         *
         * @see com.sldeditor.tool.databaseconnection.DatabaseConnectStateInterface#connect(java.util.List)
         */
        @Override
        public void connect(List<DatabaseConnection> connectionList) {
            connectionMap.clear();

            for (DatabaseConnection connection : connectionList) {

                boolean state = true;

                if (failConnection) {
                    state = false;
                    failConnection = false;
                }
                connectionMap.put(connection, state);
            }
        }

        /*
         * (non-Javadoc)
         *
         * @see com.sldeditor.tool.databaseconnection.DatabaseConnectStateInterface#disconnect(java.util.List)
         */
        @Override
        public void disconnect(List<DatabaseConnection> connectionList) {
            for (DatabaseConnection connection : connectionList) {
                connectionMap.put(connection, false);
            }
        }

        /** Fail connection. */
        public void failConnection() {
            failConnection = true;
        }
    }

    /** The Class TestDatabaseConnectionTool. */
    class TestDatabaseConnectionTool extends DatabaseConnectionTool {

        /**
         * Instantiates a new test database connection tool.
         *
         * @param databaseConnectState the database connect state
         */
        public TestDatabaseConnectionTool(DatabaseConnectStateInterface databaseConnectState) {
            super(databaseConnectState);
        }

        /** Test connect. */
        public void testConnect() {
            super.connect();
        }

        /** Test disconnect. */
        public void testDisconnect() {
            super.disconnect();
        }

        public boolean isConnectButtonEnabled() {
            return connectButton.isEnabled();
        }

        public boolean isDisconnectButtonEnabled() {
            return disconnectButton.isEnabled();
        }
    }

    /**
     * Test method for {@link
     * com.sldeditor.tool.databaseconnection.DatabaseConnectionTool#DatabaseConnectionTool(com.sldeditor.tool.databaseconnection.DatabaseConnectStateInterface)}.
     */
    @Test
    public void testDatabaseConnectionTool() {
        TestDatabaseConnectState state = new TestDatabaseConnectState();

        TestDatabaseConnectionTool testObj = new TestDatabaseConnectionTool(state);

        testObj.setSelectedItems(null, null);

        List<NodeInterface> nodeTypeList = new ArrayList<NodeInterface>();
        DatabaseConnection connection = DatabaseConnectionFactory.createGeoPackage();

        nodeTypeList.add(
                new DatabaseNode(new DatabaseInput(ToolManager.getInstance()), connection));
        File parent = new File("");
        String filename = "test.gpkg";
        FileTreeNode fileTreeNode = null;
        try {
            fileTreeNode = new FileTreeNode(parent, filename);
            fileTreeNode.setFileCategory(FileTreeNodeTypeEnum.DATABASE);
            nodeTypeList.add(fileTreeNode);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        testObj.setSelectedItems(nodeTypeList, null);

        testObj.populateComplete(null);
        assertNotNull(testObj.getPanel());

        List<SLDDataInterface> sldDataList = null;

        testObj.setSelectedItems(nodeTypeList, sldDataList);
        assertTrue(testObj.isConnectButtonEnabled());
        assertFalse(testObj.isDisconnectButtonEnabled());

        // Press Connect button
        testObj.testConnect();
        assertFalse(testObj.isConnectButtonEnabled());
        assertFalse(testObj.isDisconnectButtonEnabled());

        testObj.populateComplete(null);

        assertFalse(testObj.isConnectButtonEnabled());
        assertTrue(testObj.isDisconnectButtonEnabled());

        // Press Disconnect button
        testObj.testDisconnect();

        assertFalse(testObj.isConnectButtonEnabled());
        assertFalse(testObj.isDisconnectButtonEnabled());

        testObj.populateComplete(null);

        assertTrue(testObj.isConnectButtonEnabled());
        assertFalse(testObj.isDisconnectButtonEnabled());

        // Connect - fail connection
        state.failConnection();
        testObj.testConnect();
        assertTrue(testObj.isConnectButtonEnabled());
        assertFalse(testObj.isDisconnectButtonEnabled());

        testObj.populateComplete(null);

        assertTrue(testObj.isConnectButtonEnabled());
        assertTrue(testObj.isDisconnectButtonEnabled());
    }

    /**
     * Test method for {@link
     * com.sldeditor.tool.atabaseconnection.DatabaseConnectionTool#supports(java.util.List,
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

            FileTreeNode dbTreeNode = new FileTreeNode(new File("/test"), "test.gpkg");
            dbTreeNode.setFileCategory(FileTreeNodeTypeEnum.DATABASE);

            List<Class<?>> uniqueNodeTypeList = new ArrayList<Class<?>>();
            List<NodeInterface> nodeTypeList = new ArrayList<NodeInterface>();
            List<SLDDataInterface> sldDataList = new ArrayList<SLDDataInterface>();

            uniqueNodeTypeList.add(String.class);

            // Try vector file
            nodeTypeList.add(vectorTreeNode);
            TestDatabaseConnectionTool testObj = new TestDatabaseConnectionTool(null);
            assertFalse(testObj.supports(uniqueNodeTypeList, nodeTypeList, sldDataList));

            // Try raster file
            nodeTypeList.clear();
            nodeTypeList.add(rasterTreeNode);
            assertFalse(testObj.supports(uniqueNodeTypeList, nodeTypeList, sldDataList));

            // Try SLD file
            nodeTypeList.clear();
            nodeTypeList.add(sldTreeNode);
            assertFalse(testObj.supports(uniqueNodeTypeList, nodeTypeList, sldDataList));

            // Try database
            nodeTypeList.clear();
            nodeTypeList.add(dbTreeNode);
            assertTrue(testObj.supports(uniqueNodeTypeList, nodeTypeList, sldDataList));

            // Try database feature class
            nodeTypeList.clear();
            DatabaseFeatureClassNode databaseFeatureClassNode =
                    new DatabaseFeatureClassNode(null, null, "db fc");
            nodeTypeList.add(databaseFeatureClassNode);
            assertFalse(testObj.supports(uniqueNodeTypeList, nodeTypeList, sldDataList));

            // Try GeoServerStyleHeading node class
            nodeTypeList.clear();
            nodeTypeList.add(new GeoServerStyleHeadingNode(null, null, "test"));
            assertFalse(testObj.supports(uniqueNodeTypeList, nodeTypeList, sldDataList));

            // Try GeoServerStyleNode node class
            nodeTypeList.clear();
            nodeTypeList.add(new GeoServerStyleNode(null, null, new StyleWrapper("test", "")));
            assertFalse(testObj.supports(uniqueNodeTypeList, nodeTypeList, sldDataList));

            // Try GeoServerWorkspaceNode node class -- not style
            nodeTypeList.clear();
            nodeTypeList.add(new GeoServerWorkspaceNode(null, null, "test", false));
            assertFalse(testObj.supports(uniqueNodeTypeList, nodeTypeList, sldDataList));

            // Try GeoServerWorkspaceNode node class -- style
            nodeTypeList.clear();
            nodeTypeList.add(new GeoServerWorkspaceNode(null, null, "test", true));
            assertFalse(testObj.supports(uniqueNodeTypeList, nodeTypeList, sldDataList));

            // Try with no nodes
            nodeTypeList.clear();
            assertFalse(testObj.supports(uniqueNodeTypeList, nodeTypeList, sldDataList));

            uniqueNodeTypeList.clear();
            assertFalse(testObj.supports(uniqueNodeTypeList, nodeTypeList, sldDataList));

            // Try with null
            assertFalse(testObj.supports(null, null, sldDataList));
        } catch (SecurityException e) {
            fail(e.getStackTrace().toString());
        } catch (FileNotFoundException e) {
            fail(e.getStackTrace().toString());
        }
    }
}
