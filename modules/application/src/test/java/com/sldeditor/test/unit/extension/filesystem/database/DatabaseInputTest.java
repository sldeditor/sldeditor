/*
 *    SLDEditor - SLD Editor application
 *
 *    (C) 2016, SCISYS
 *
 */

package com.sldeditor.test.unit.extension.filesystem.database;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.sldeditor.common.SLDDataInterface;
import com.sldeditor.common.connection.DatabaseConnectionManager;
import com.sldeditor.common.data.DatabaseConnection;
import com.sldeditor.common.filesystem.SelectedFiles;
import com.sldeditor.common.property.PropertyManagerFactory;
import com.sldeditor.datasource.extension.filesystem.node.FSTree;
import com.sldeditor.datasource.extension.filesystem.node.database.DatabaseFeatureClassNode;
import com.sldeditor.extension.filesystem.database.DatabaseInput;
import com.sldeditor.tool.ToolManager;
import com.sldeditor.tool.dbconnectionlist.DatabaseConnectionFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import org.geotools.geopkg.GeoPkgDataStoreFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit test for DatabaseInput class.
 *
 * <p>{@link com.sldeditor.extension.filesystem.database.DatabaseInput}
 *
 * @author Robert Ward (SCISYS)
 */
public class DatabaseInputTest {

    /** The config properties file. */
    private File configPropertiesFile = new File("./DatabaseInputTest.properties");

    /** Called before each test. */
    @Before
    public void beforeEachTest() {
        DatabaseConnectionManager.destroyInstance();
        PropertyManagerFactory.getInstance().setPropertyFile(configPropertiesFile);
    }

    /** Called after each test. */
    @After
    public void afterEachTest() {
        configPropertiesFile.delete();

        File f = new File("test.gpkg");
        f.delete();
        f = new File("test2.gpkg");
        f.delete();
    }

    /**
     * Test method for {@link
     * com.sldeditor.extension.filesystem.database.DatabaseInput#DatabaseInput(com.sldeditor.common.ToolSelectionInterface)}.
     */
    @Test
    public void testDatabaseInput() {
        DatabaseInput input = new DatabaseInput(ToolManager.getInstance());
        input.readPropertyFile();
        FSTree tree = new FSTree();

        DefaultMutableTreeNode rootNode;
        try {
            rootNode = new DefaultMutableTreeNode("Root");

            DefaultTreeModel model = new DefaultTreeModel(rootNode);

            input.populate(tree, model, rootNode);

            List<SLDDataInterface> sldDataList = input.open(null);

            assertNull(sldDataList);

            DatabaseConnection connection1 = DatabaseConnectionFactory.createGeoPackage();
            String featureClassName = "test feature class";
            DatabaseFeatureClassNode fcTreeNode =
                    new DatabaseFeatureClassNode(input, connection1, featureClassName);
            Map<String, String> connectionDataMap = new HashMap<String, String>();
            connectionDataMap.put(GeoPkgDataStoreFactory.DATABASE.key, "test.gpkg");
            connection1.setConnectionDataMap(connectionDataMap);
            // Try with no known database connections
            SelectedFiles actualSLDContents = input.getSLDContents(fcTreeNode);
            assertNotNull(actualSLDContents);
            assertTrue(actualSLDContents.isDataSource());

            // Add some database connections
            input.addNewConnection(connection1);

            DatabaseConnection connection2 = DatabaseConnectionFactory.createGeoPackage();
            Map<String, String> connectionDataMap2 = new HashMap<String, String>();
            connectionDataMap2.put(GeoPkgDataStoreFactory.DATABASE.key, "test2.gpkg");
            connection2.setConnectionDataMap(connectionDataMap2);
            input.addNewConnection(connection2);
            input.addNewConnection(null);

            List<SLDDataInterface> sldDataContentsList =
                    input.getSLDContents(fcTreeNode).getSldData();
            assertEquals(0, sldDataContentsList.size());

            // Try saving a null object
            assertFalse(input.save(null));

            // Check how many connections we have
            assertEquals(2, input.getConnectionDetails().size());
        } catch (SecurityException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    /**
     * Test method for {@link
     * com.sldeditor.extension.filesystem.database.DatabaseInput#readPropertyFile()}.
     */
    @Test
    public void testReadPropertyFile() {}

    /**
     * Test method for {@link
     * com.sldeditor.extension.filesystem.database.DatabaseInput#populate(com.sldeditor.datasource.extension.filesystem.node.FSTree,
     * javax.swing.tree.DefaultTreeModel, javax.swing.tree.DefaultMutableTreeNode)}.
     */
    @Test
    public void testPopulate() {}

    /**
     * Test method for {@link
     * com.sldeditor.extension.filesystem.database.DatabaseInput#treeExpanded(java.lang.Object)}.
     */
    @Test
    public void testTreeExpanded() {
        DatabaseInput input = new DatabaseInput(null);

        assertFalse(input.treeExpanded(null));
    }

    /**
     * Test method for {@link
     * com.sldeditor.extension.filesystem.database.DatabaseInput#rightMouseButton(javax.swing.JPopupMenu,
     * java.lang.Object, java.awt.event.MouseEvent)}.
     */
    @Test
    public void testRightMouseButton() {
        DatabaseInput input = new DatabaseInput(null);
        input.rightMouseButton(null, null, null);
    }

    /**
     * Test method for {@link
     * com.sldeditor.extension.filesystem.database.DatabaseInput#getNodeTypes()}.
     */
    @Test
    public void testGetNodeTypes() {
        DatabaseInput input = new DatabaseInput(null);

        assertTrue(input.getNodeTypes().isEmpty());
    }

    /**
     * Test method for {@link
     * com.sldeditor.extension.filesystem.database.DatabaseInput#connect(java.util.List)}.
     */
    @Test
    public void testConnect() {
        DatabaseInput input = new DatabaseInput(null);
        DatabaseConnection connection1 = DatabaseConnectionFactory.createGeoPackage();
        Map<String, String> connectionDataMap = new HashMap<String, String>();
        connectionDataMap.put(GeoPkgDataStoreFactory.DATABASE.key, "test.gpkg");
        connection1.setConnectionDataMap(connectionDataMap);

        // Add some database connections
        input.addNewConnection(connection1);

        DatabaseConnection connection2 = DatabaseConnectionFactory.createGeoPackage();
        Map<String, String> connectionDataMap2 = new HashMap<String, String>();
        connectionDataMap2.put(GeoPkgDataStoreFactory.DATABASE.key, "test2.gpkg");
        connection2.setConnectionDataMap(connectionDataMap2);
        input.addNewConnection(connection2);
        input.addNewConnection(null);

        input.connect(null);

        // Try connecting to one database
        List<DatabaseConnection> connectionList = new ArrayList<DatabaseConnection>();
        connectionList.add(connection1);

        // Try passing null
        input.connect(connectionList);

        // Try connecting to 2
        connectionList.add(connection2);

        input.connect(connectionList);

        assertTrue(input.isConnected(connection1));
    }

    /**
     * Test method for {@link
     * com.sldeditor.extension.filesystem.database.DatabaseInput#disconnect(java.util.List)}.
     */
    @Test
    public void testDisconnect() {
        PropertyManagerFactory.getInstance().setPropertyFile(configPropertiesFile);
        DatabaseInput input = new DatabaseInput(null);

        // Add some database connections
        DatabaseConnection connection1 = DatabaseConnectionFactory.createGeoPackage();
        Map<String, String> connectionDataMap = new HashMap<String, String>();
        connectionDataMap.put(GeoPkgDataStoreFactory.DATABASE.key, "test.gpkg");
        connection1.setConnectionDataMap(connectionDataMap);
        input.addNewConnection(connection1);

        DatabaseConnection connection2 = DatabaseConnectionFactory.createGeoPackage();
        Map<String, String> connectionDataMap2 = new HashMap<String, String>();
        connectionDataMap2.put(GeoPkgDataStoreFactory.DATABASE.key, "test2.gpkg");
        connection2.setConnectionDataMap(connectionDataMap2);
        input.addNewConnection(connection2);

        // Try passing null
        input.disconnect(null);

        // Try disconnecting from one database
        List<DatabaseConnection> connectionList = new ArrayList<DatabaseConnection>();
        connectionList.add(connection1);

        input.disconnect(connectionList);

        // Try disconnecting from to 2
        connectionList.add(connection2);

        input.disconnect(connectionList);
    }

    /**
     * Test method for {@link
     * com.sldeditor.extension.filesystem.database.DatabaseInput#populateComplete(com.sldeditor.common.data.DatabaseConnection,
     * java.util.List)}.
     */
    @Test
    public void testPopulateComplete() {}

    /**
     * Test method for {@link
     * com.sldeditor.extension.filesystem.database.DatabaseInput#addNewConnection(com.sldeditor.common.data.DatabaseConnection)}.
     */
    @Test
    public void testAddNewConnection() {}

    /**
     * Test method for {@link
     * com.sldeditor.extension.filesystem.database.DatabaseInput#updateConnectionDetails(com.sldeditor.common.data.DatabaseConnection,
     * com.sldeditor.common.data.DatabaseConnection)}.
     */
    @Test
    public void testUpdateConnectionDetails() {
        DatabaseInput input = new DatabaseInput(null);

        // Add some database connections
        DatabaseConnection connection1 = DatabaseConnectionFactory.createGeoPackage();
        Map<String, String> connectionDataMap = new HashMap<String, String>();
        connectionDataMap.put(GeoPkgDataStoreFactory.DATABASE.key, "test.gpkg");
        connection1.setConnectionDataMap(connectionDataMap);
        input.addNewConnection(connection1);

        DatabaseConnection connection2 = DatabaseConnectionFactory.createGeoPackage();
        Map<String, String> connectionDataMap2 = new HashMap<String, String>();
        connectionDataMap2.put(GeoPkgDataStoreFactory.DATABASE.key, "test2.gpkg");
        connection2.setConnectionDataMap(connectionDataMap2);
        input.addNewConnection(connection2);

        // Try null parameters
        input.updateConnectionDetails(null, null);

        // Delete connection details
        List<DatabaseConnection> listToDelete = new ArrayList<DatabaseConnection>();
        listToDelete.add(connection1);
        input.deleteConnections(listToDelete);

        DatabaseConnection connection1Updated = DatabaseConnectionFactory.createGeoPackage();
        Map<String, String> connectionDataMap3 = new HashMap<String, String>();
        connectionDataMap3.put(GeoPkgDataStoreFactory.DATABASE.key, "updated test.gpkg");
        connection1Updated.setConnectionDataMap(connectionDataMap3);
        input.addNewConnection(connection1Updated);

        // Update the connection details
        input.updateConnectionDetails(connection1, connection1Updated);
    }

    /**
     * Test method for {@link
     * com.sldeditor.extension.filesystem.database.DatabaseInput#deleteConnections(java.util.List)}.
     */
    @Test
    public void testDeleteConnections() {
        DatabaseInput input = new DatabaseInput(null);

        // Add some database connections
        DatabaseConnection connection1 = DatabaseConnectionFactory.createGeoPackage();
        Map<String, String> connectionDataMap = new HashMap<String, String>();
        connectionDataMap.put(GeoPkgDataStoreFactory.DATABASE.key, "test.gpkg");
        connection1.setConnectionDataMap(connectionDataMap);
        input.addNewConnection(connection1);

        DatabaseConnection connection2 = DatabaseConnectionFactory.createGeoPackage();
        Map<String, String> connectionDataMap2 = new HashMap<String, String>();
        connectionDataMap2.put(GeoPkgDataStoreFactory.DATABASE.key, "test2.gpkg");
        connection2.setConnectionDataMap(connectionDataMap2);
        input.addNewConnection(connection2);

        // Try null parameters
        input.deleteConnections(null);

        // Populate some styles
        List<String> featureClassList = new ArrayList<String>();
        featureClassList.add("Feature Class 1");
        featureClassList.add("Feature Class 2");

        input.populateComplete(connection1, featureClassList);

        List<String> featureClassList2 = new ArrayList<String>();
        featureClassList2.add("Feature Class 3");
        featureClassList2.add("Feature Class 4");
        input.populateComplete(connection2, featureClassList2);

        // Delete a delete connection
        List<DatabaseConnection> connectionList = new ArrayList<DatabaseConnection>();
        connectionList.add(connection1);

        input.deleteConnections(connectionList);

        List<DatabaseConnection> actual = input.getConnectionDetails();
        assertEquals(1, actual.size());
    }

    /**
     * Test method for {@link
     * com.sldeditor.extension.filesystem.database.DatabaseInput#copyNodes(com.sldeditor.common.NodeInterface,
     * java.util.Map)}.
     */
    @Test
    public void testCopyNodes() {
        DatabaseInput input = new DatabaseInput(null);
        assertFalse(input.copyNodes(null, null));
    }

    /**
     * Test method for {@link
     * com.sldeditor.extension.filesystem.database.DatabaseInput#deleteNodes(com.sldeditor.common.NodeInterface,
     * java.util.List)}.
     */
    @Test
    public void testDeleteNodes() {
        DatabaseInput input = new DatabaseInput(null);

        input.deleteNodes(null, null);
    }

    /**
     * Test method for {@link
     * com.sldeditor.extension.filesystem.database.DatabaseInput#getDestinationText(com.sldeditor.common.NodeInterface)}.
     */
    @Test
    public void testGetDestinationText() {
        DatabaseInput input = new DatabaseInput(null);

        assertEquals("Unknown", input.getDestinationText(null));
    }

    /**
     * Test method for {@link
     * com.sldeditor.extension.filesystem.database.DatabaseInput#setFolder(com.sldeditor.common.data.DatabaseConnection,
     * boolean)}.
     */
    @Test
    public void testSetFolder() {
        DatabaseInput input = new DatabaseInput(null);

        // Add some database connections
        DatabaseConnection connection1 = DatabaseConnectionFactory.createGeoPackage();
        Map<String, String> connectionDataMap = new HashMap<String, String>();
        connectionDataMap.put(GeoPkgDataStoreFactory.DATABASE.key, "test.gpkg");
        connection1.setConnectionDataMap(connectionDataMap);
        input.setFolder(connection1, true);
    }
}
