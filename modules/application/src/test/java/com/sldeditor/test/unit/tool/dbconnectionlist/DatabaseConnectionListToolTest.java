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

package com.sldeditor.test.unit.tool.dbconnectionlist;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sldeditor.common.NodeInterface;
import com.sldeditor.common.SLDDataInterface;
import com.sldeditor.common.data.DatabaseConnection;
import com.sldeditor.datasource.extension.filesystem.DatabaseConnectUpdateInterface;
import com.sldeditor.datasource.extension.filesystem.node.database.DatabaseFeatureClassNode;
import com.sldeditor.datasource.extension.filesystem.node.database.DatabaseNode;
import com.sldeditor.datasource.extension.filesystem.node.database.DatabaseOverallNode;
import com.sldeditor.tool.dbconnectionlist.DBConnectorDetailsPanel;
import com.sldeditor.tool.dbconnectionlist.DatabaseConnectionFactory;
import com.sldeditor.tool.dbconnectionlist.DatabaseConnectionListTool;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * The Class DatabaseConnectionListToolTest, unit test for DatabaseConnectionListTool
 *
 * @author Robert Ward (SCISYS)
 */
class DatabaseConnectionListToolTest {

    class TestDatabaseConnectUpdateInterface implements DatabaseConnectUpdateInterface {

        public List<DatabaseConnection> dbList = new ArrayList<DatabaseConnection>();

        /* (non-Javadoc)
         * @see com.sldeditor.datasource.extension.filesystem.DatabaseConnectUpdateInterface#getConnectionDetails()
         */
        @Override
        public List<DatabaseConnection> getConnectionDetails() {
            return dbList;
        }

        /* (non-Javadoc)
         * @see com.sldeditor.datasource.extension.filesystem.DatabaseConnectUpdateInterface#addNewConnection(com.sldeditor.common.data.DatabaseConnection)
         */
        @Override
        public void addNewConnection(DatabaseConnection newConnectionDetails) {
            dbList.add(newConnectionDetails);
        }

        /* (non-Javadoc)
         * @see com.sldeditor.datasource.extension.filesystem.DatabaseConnectUpdateInterface#updateConnectionDetails(com.sldeditor.common.data.DatabaseConnection, com.sldeditor.common.data.DatabaseConnection)
         */
        @Override
        public void updateConnectionDetails(
                DatabaseConnection originalConnectionDetails,
                DatabaseConnection newConnectionDetails) {
            dbList.remove(originalConnectionDetails);
            dbList.add(newConnectionDetails);
        }

        /* (non-Javadoc)
         * @see com.sldeditor.datasource.extension.filesystem.DatabaseConnectUpdateInterface#deleteConnections(java.util.List)
         */
        @Override
        public void deleteConnections(List<DatabaseConnection> connectionList) {
            for (DatabaseConnection dbConnection : connectionList) {
                dbList.remove(dbConnection);
            }
        }
    }

    class TestDatabaseConnectionListTool extends DatabaseConnectionListTool {

        /**
         * Instantiates a new test database connection list tool.
         *
         * @param databaseConnectUpdate the database connect update
         */
        public TestDatabaseConnectionListTool(
                DatabaseConnectUpdateInterface databaseConnectUpdate) {
            super(databaseConnectUpdate);
        }

        public boolean isNewButtonEnabled() {
            return btnNew.isEnabled();
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

        /* (non-Javadoc)
         * @see com.sldeditor.tool.dbconnectionlist.DatabaseConnectionListTool#newButtonPressed()
         */
        @Override
        protected void newButtonPressed() {
            super.newButtonPressed();
        }

        /* (non-Javadoc)
         * @see com.sldeditor.tool.dbconnectionlist.DatabaseConnectionListTool#duplicateButtonPressed()
         */
        @Override
        protected void duplicateButtonPressed() {
            super.duplicateButtonPressed();
        }

        /* (non-Javadoc)
         * @see com.sldeditor.tool.dbconnectionlist.DatabaseConnectionListTool#editButtonPressed()
         */
        @Override
        protected void editButtonPressed() {
            super.editButtonPressed();
        }

        /* (non-Javadoc)
         * @see com.sldeditor.tool.dbconnectionlist.DatabaseConnectionListTool#deleteButtonPressed()
         */
        @Override
        protected void deleteButtonPressed() {
            super.deleteButtonPressed();
        }
    }

    /**
     * Test method for {@link
     * com.sldeditor.tool.dbconnectionlist.DatabaseConnectionListTool#getPanel()}.
     */
    @Test
    void testGetPanel() {
        TestDatabaseConnectionListTool testObj = new TestDatabaseConnectionListTool(null);
        assertNotNull(testObj.getPanel());
    }

    /**
     * Test method for {@link
     * com.sldeditor.tool.dbconnectionlist.DatabaseConnectionListTool#setSelectedItems(java.util.List,
     * java.util.List)}.
     */
    @Test
    void testSetSelectedItems() {
        List<NodeInterface> nodeTypeList = new ArrayList<NodeInterface>();
        List<SLDDataInterface> sldDataList = new ArrayList<SLDDataInterface>();

        // Try database feature class
        DatabaseFeatureClassNode databaseFeatureClassNode =
                new DatabaseFeatureClassNode(null, null, "db fc");
        nodeTypeList.add(databaseFeatureClassNode);

        TestDatabaseConnectionListTool testTool = new TestDatabaseConnectionListTool(null);
        assertFalse(testTool.isEditButtonEnabled());
        assertFalse(testTool.isDuplicateButtonEnabled());
        assertFalse(testTool.isDeleteButtonEnabled());

        testTool.setSelectedItems(nodeTypeList, sldDataList);
        assertFalse(testTool.isEditButtonEnabled());
        assertFalse(testTool.isDuplicateButtonEnabled());
        assertFalse(testTool.isDeleteButtonEnabled());

        // Try a DatabaseOverallNode
        nodeTypeList.clear();
        nodeTypeList.add(new DatabaseOverallNode(null, "GeoPackage", null));
        testTool.setSelectedItems(nodeTypeList, sldDataList);
        assertFalse(testTool.isEditButtonEnabled());
        assertFalse(testTool.isDuplicateButtonEnabled());
        assertFalse(testTool.isDeleteButtonEnabled());

        // Try a DatabaseNode -- supports duplication
        nodeTypeList.clear();
        DatabaseConnection dbConnection = DatabaseConnectionFactory.createPostgres();
        nodeTypeList.add(new DatabaseNode(null, dbConnection));
        testTool.setSelectedItems(nodeTypeList, sldDataList);
        assertFalse(testTool.isEditButtonEnabled());
        assertFalse(testTool.isDuplicateButtonEnabled());
        assertFalse(testTool.isDeleteButtonEnabled());

        // Try a DatabaseNode -- does not support duplication
        nodeTypeList.clear();
        dbConnection = DatabaseConnectionFactory.createH2();
        nodeTypeList.add(new DatabaseNode(null, dbConnection));
        testTool.setSelectedItems(nodeTypeList, sldDataList);
        assertFalse(testTool.isEditButtonEnabled());
        assertFalse(testTool.isDuplicateButtonEnabled());
        assertFalse(testTool.isDeleteButtonEnabled());

        // Try multiple DatabaseNodes
        nodeTypeList.clear();
        nodeTypeList.add(new DatabaseNode(null, DatabaseConnectionFactory.createH2()));
        nodeTypeList.add(new DatabaseNode(null, DatabaseConnectionFactory.createPostgres()));
        testTool.setSelectedItems(nodeTypeList, sldDataList);
        assertFalse(testTool.isEditButtonEnabled());
        assertFalse(testTool.isDuplicateButtonEnabled());
        assertFalse(testTool.isDeleteButtonEnabled());
    }

    /**
     * Test method for {@link
     * com.sldeditor.tool.dbconnectionlist.DatabaseConnectionListTool#getToolName()}.
     */
    @Test
    void testGetToolName() {
        TestDatabaseConnectionListTool testObj = new TestDatabaseConnectionListTool(null);
        assertNotNull(testObj.getToolName());
    }

    /**
     * Test method for {@link
     * com.sldeditor.tool.dbconnectionlist.DatabaseConnectionListTool#supports(java.util.List,
     * java.util.List, java.util.List)}.
     */
    @Test
    void testSupports() {
        TestDatabaseConnectionListTool testObj = new TestDatabaseConnectionListTool(null);

        List<Class<?>> uniqueNodeTypeList = new ArrayList<Class<?>>();
        List<NodeInterface> nodeTypeList = new ArrayList<NodeInterface>();
        List<SLDDataInterface> sldDataList = new ArrayList<SLDDataInterface>();

        assertFalse(testObj.supports(uniqueNodeTypeList, nodeTypeList, sldDataList));

        uniqueNodeTypeList.add(String.class);

        assertTrue(testObj.supports(uniqueNodeTypeList, nodeTypeList, sldDataList));
    }

    @Test
    void testButtons() {
        DBConnectorDetailsPanel.setInTestMode(true);
        TestDatabaseConnectUpdateInterface receiver = new TestDatabaseConnectUpdateInterface();

        List<NodeInterface> nodeTypeList = new ArrayList<NodeInterface>();
        List<SLDDataInterface> sldDataList = new ArrayList<SLDDataInterface>();

        TestDatabaseConnectionListTool testTool = new TestDatabaseConnectionListTool(receiver);

        // Try a DatabaseNode -- supports duplication
        nodeTypeList.add(new DatabaseOverallNode(null, "PostGIS (JNDI)", null));
        testTool.setSelectedItems(nodeTypeList, sldDataList);

        // New buttons
        assertTrue(receiver.dbList.isEmpty());
        testTool.newButtonPressed();
        assertEquals(1, receiver.dbList.size());

        // Duplicate
        nodeTypeList.clear();
        nodeTypeList.add(new DatabaseNode(null, receiver.dbList.get(0)));
        testTool.setSelectedItems(nodeTypeList, sldDataList);

        testTool.duplicateButtonPressed();
        assertEquals(1, receiver.dbList.size());

        // Edit
        nodeTypeList.clear();
        assertEquals(1, receiver.dbList.size());
        nodeTypeList.add(new DatabaseNode(null, receiver.dbList.get(0)));
        testTool.setSelectedItems(nodeTypeList, sldDataList);
        testTool.editButtonPressed();

        // Delete
        nodeTypeList.clear();
        nodeTypeList.add(new DatabaseNode(null, receiver.dbList.get(0)));
        testTool.setSelectedItems(nodeTypeList, sldDataList);
        testTool.deleteButtonPressed();
        assertEquals(1, receiver.dbList.size());
        DBConnectorDetailsPanel.setInTestMode(false);
    }
}
