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

import com.sldeditor.common.NodeInterface;
import com.sldeditor.common.SLDDataInterface;
import com.sldeditor.common.data.DatabaseConnection;
import com.sldeditor.datasource.extension.filesystem.node.database.DatabaseNode;
import com.sldeditor.datasource.extension.filesystem.node.file.FileTreeNode;
import com.sldeditor.datasource.extension.filesystem.node.file.FileTreeNodeTypeEnum;
import com.sldeditor.extension.filesystem.database.DatabaseInput;
import com.sldeditor.tool.ToolManager;
import com.sldeditor.tool.databaseconnection.DatabaseConnectStateInterface;
import com.sldeditor.tool.databaseconnection.DatabaseConnectionTool;
import com.sldeditor.tool.dbconnectionlist.DatabaseConnectionFactory;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
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

        /*
         * (non-Javadoc)
         *
         * @see com.sldeditor.tool.databaseconnection.DatabaseConnectStateInterface#isConnected(com.sldeditor.common.data.DatabaseConnection)
         */
        @Override
        public boolean isConnected(DatabaseConnection connection) {
            return false;
        }

        /*
         * (non-Javadoc)
         *
         * @see com.sldeditor.tool.databaseconnection.DatabaseConnectStateInterface#connect(java.util.List)
         */
        @Override
        public void connect(List<DatabaseConnection> connectionList) {}

        /*
         * (non-Javadoc)
         *
         * @see com.sldeditor.tool.databaseconnection.DatabaseConnectStateInterface#disconnect(java.util.List)
         */
        @Override
        public void disconnect(List<DatabaseConnection> connectionList) {}
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

        List<Class<?>> uniqueNodeTypeList = new ArrayList<Class<?>>();
        List<SLDDataInterface> sldDataList = null;
        assertFalse(testObj.supports(uniqueNodeTypeList, nodeTypeList, sldDataList));
        uniqueNodeTypeList.add(String.class);
        assertTrue(testObj.supports(uniqueNodeTypeList, nodeTypeList, sldDataList));

        nodeTypeList.remove(0);
        assertTrue(testObj.supports(uniqueNodeTypeList, nodeTypeList, sldDataList));

        testObj.testConnect();
        testObj.testDisconnect();
    }
}
