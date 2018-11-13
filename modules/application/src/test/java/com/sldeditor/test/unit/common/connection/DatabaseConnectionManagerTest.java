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

package com.sldeditor.test.unit.common.connection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sldeditor.common.connection.DatabaseConnectionManager;
import com.sldeditor.common.data.DatabaseConnection;
import com.sldeditor.common.property.PropertyManagerFactory;
import com.sldeditor.tool.dbconnectionlist.DatabaseConnectionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * The unit test for DatabaseConnectionManager.
 *
 * @author Robert Ward (SCISYS)
 */
class DatabaseConnectionManagerTest {

    @BeforeEach
    void beforeEachTest() {
        DatabaseConnectionManager.destroyInstance();
        PropertyManagerFactory.destroyInstance();
    }

    @AfterAll
    static void afterAllTest() {
        DatabaseConnectionManager.destroyInstance();
        PropertyManagerFactory.destroyInstance();
    }

    /**
     * Test method for {@link
     * com.sldeditor.common.connection.DatabaseConnectionManager#addNewConnection(com.sldeditor.extension.filesystem.database.DatabaseReadProgressInterface,
     * com.sldeditor.common.data.DatabaseConnection)}.
     */
    @Test
    void testAddNewConnection() {
        DatabaseConnectionManager.getInstance().addNewConnection(null, null);
        assertTrue(DatabaseConnectionManager.getInstance().getConnectionMap().isEmpty());
        assertNull(DatabaseConnectionManager.getInstance().getMatchingConnection(null));
        assertNull(DatabaseConnectionManager.getInstance().getDBConnectionParams(null));

        DatabaseConnection dbConnection = DatabaseConnectionFactory.createGeoPackage();
        dbConnection.setConnectionName("connection 1");
        DatabaseConnectionManager.getInstance().addNewConnection(null, dbConnection);
        DatabaseConnectionManager.getInstance()
                .addNewConnection(null, DatabaseConnectionFactory.createGeoPackage());

        DatabaseConnectionManager.getInstance()
                .addNewConnection(null, DatabaseConnectionFactory.createPostgres());
        assertEquals(3, DatabaseConnectionManager.getInstance().getConnectionMap().size());

        DatabaseConnectionManager.getInstance().updateList();

        // Remove connection
        DatabaseConnectionManager.getInstance().removeConnection(dbConnection);
        assertEquals(2, DatabaseConnectionManager.getInstance().getConnectionMap().size());

        // Reread value from property file
        DatabaseConnection actual = DatabaseConnectionManager.getInstance().getConnection(null);
        assertNull(actual);
        assertEquals(
                dbConnection,
                DatabaseConnectionManager.getInstance()
                        .getConnection(dbConnection.getConnectionName()));

        actual = DatabaseConnectionManager.getInstance().getConnection("Invalid");
        assertNull(actual);
    }

    /**
     * Test method for {@link
     * com.sldeditor.common.connection.DatabaseConnectionManager#getDBConnectionParams(com.sldeditor.common.data.DatabaseConnection)}.
     */
    @Test
    void testGetDBConnectionParams() {}

    /**
     * Test method for {@link
     * com.sldeditor.common.connection.DatabaseConnectionManager#getMatchingConnection(com.sldeditor.common.data.DatabaseConnection)}.
     */
    @Test
    void testGetMatchingConnection() {}
}
