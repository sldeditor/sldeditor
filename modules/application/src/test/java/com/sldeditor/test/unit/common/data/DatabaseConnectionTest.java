/*
 * SLD Editor - The Open Source Java SLD Editor
 *
 * Copyright (C) 2017, SCISYS UK Limited
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

package com.sldeditor.test.unit.common.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.sldeditor.common.data.DatabaseConnection;
import com.sldeditor.common.data.DatabaseConnectionField;
import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.tool.dbconnectionlist.DatabaseConnectionName;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.geotools.data.DataAccessFactory.Param;
import org.geotools.geopkg.GeoPkgDataStoreFactory;
import org.junit.Test;

/**
 * The unit test for DatabaseConnection.
 *
 * <p>{@link com.sldeditor.common.data.DatabaseConnection}
 *
 * @author Robert Ward (SCISYS)
 */
public class DatabaseConnectionTest {

    /**
     * Test method for {@link
     * com.sldeditor.common.data.DatabaseConnection#DatabaseConnection(com.sldeditor.common.data.DatabaseConnection)}.
     */
    @Test
    public void testDatabaseConnection() {
        Param param = GeoPkgDataStoreFactory.DBTYPE;
        List<DatabaseConnectionField> expectedDetailList = new ArrayList<DatabaseConnectionField>();

        expectedDetailList.add(new DatabaseConnectionField(GeoPkgDataStoreFactory.DATABASE));
        expectedDetailList.add(new DatabaseConnectionField(GeoPkgDataStoreFactory.USER));
        String expectedDatabaseTypeLabel = "GeoPackage";
        boolean expectedSupportsDuplication = false;

        DatabaseConnection test =
                new DatabaseConnection(
                        param,
                        expectedDatabaseTypeLabel,
                        expectedSupportsDuplication,
                        expectedDetailList,
                        new DatabaseConnectionName() {

                            @Override
                            public String getConnectionName(
                                    String duplicatePrefix,
                                    int noOfDuplicates,
                                    Map<String, String> properties) {
                                String connectionName =
                                        Localisation.getString(
                                                DatabaseConnectionTest.class, "common.notSet");
                                String databaseName =
                                        properties.get(GeoPkgDataStoreFactory.DATABASE.key);
                                if (databaseName != null) {
                                    File f = new File(databaseName);
                                    if (f.isFile()) {
                                        connectionName = f.getName();
                                    }
                                }

                                for (int i = 0; i < noOfDuplicates; i++) {
                                    connectionName = duplicatePrefix + connectionName;
                                }
                                return connectionName;
                            }
                        });

        Map<String, String> connectionDataMap = new HashMap<String, String>();
        connectionDataMap.put(GeoPkgDataStoreFactory.DATABASE.key, "test datatbase");
        test.setConnectionDataMap(connectionDataMap);
        assertEquals(test.isSupportsDuplication(), expectedSupportsDuplication);
        assertNotNull(test.getConnectionName());
        assertNotNull(test.getDatabaseConnectionName());
        assertEquals(test.getDetailList(), expectedDetailList);
        assertEquals(test.getDatabaseTypeLabel(), expectedDatabaseTypeLabel);
        assertTrue(test.hashCode() != 0);
        assertEquals(1, test.getExpectedKeys().size());
        assertEquals(GeoPkgDataStoreFactory.DATABASE.key, test.getExpectedKeys().get(0));
        assertEquals(test.getDatabaseType(), param.sample);
        assertEquals(
                test.getConnectionDataMap().get(GeoPkgDataStoreFactory.DATABASE.key),
                connectionDataMap.get(GeoPkgDataStoreFactory.DATABASE.key));
        assertEquals(
                test.getConnectionDataMap().get(GeoPkgDataStoreFactory.DBTYPE.key),
                GeoPkgDataStoreFactory.DBTYPE.sample);

        String expectedUserName = "test user";
        test.setUserName(expectedUserName);
        assertEquals(test.getUserName(), expectedUserName);
        assertNotNull(test.getDBConnectionParams());

        String expectedPassword = "password123";
        test.setPassword(expectedPassword);
        assertEquals(test.getPassword(), expectedPassword);

        // Duplicate
        DatabaseConnection testCopy = test.duplicate();

        assertTrue(testCopy.compareTo(test) < 0);

        testCopy.update(test);
        assertEquals(testCopy, test);
        assertEquals(testCopy, testCopy);

        testCopy.setUserName("new username");
        assertFalse(testCopy.equals(test));

        testCopy.update(test);
        assertTrue(testCopy.equals(test));

        String actualString = test.encodeAsString();
        DatabaseConnection newTest = DatabaseConnection.decodeString(actualString);
        assertTrue(newTest.equals(test));
    }

    /**
     * Test method for {@link
     * com.sldeditor.common.data.DatabaseConnection#decodeString(java.lang.String)}.
     */
    @Test
    public void testDecodeString() {}

    /** Test method for {@link com.sldeditor.common.data.DatabaseConnection#getConnectionName()}. */
    @Test
    public void testGetConnectionName() {}

    /**
     * Test method for {@link
     * com.sldeditor.common.data.DatabaseConnection#compareTo(com.sldeditor.common.data.DatabaseConnection)}.
     */
    @Test
    public void testCompareTo() {}

    /**
     * Test method for {@link
     * com.sldeditor.common.data.DatabaseConnection#update(com.sldeditor.common.data.DatabaseConnection)}.
     */
    @Test
    public void testUpdate() {}

    /** Test method for {@link com.sldeditor.common.data.DatabaseConnection#encodeAsString()}. */
    @Test
    public void testEncodeAsString() {}

    /**
     * Test method for {@link com.sldeditor.common.data.DatabaseConnection#getConnectionDataMap()}.
     */
    @Test
    public void testGetConnectionDataMap() {}

    /**
     * Test method for {@link
     * com.sldeditor.common.data.DatabaseConnection#setConnectionDataMap(java.util.Map)}.
     */
    @Test
    public void testSetConnectionDataMap() {}

    /** Test method for {@link com.sldeditor.common.data.DatabaseConnection#duplicate()}. */
    @Test
    public void testDuplicate() {}

    /**
     * Test method for {@link
     * com.sldeditor.common.data.DatabaseConnection#getDatabaseConnectionName()}.
     */
    @Test
    public void testGetDatabaseConnectionName() {}

    /** Test method for {@link com.sldeditor.common.data.DatabaseConnection#getExpectedKeys()}. */
    @Test
    public void testGetExpectedKeys() {}

    /**
     * Test method for {@link com.sldeditor.common.data.DatabaseConnection#getDBConnectionParams()}.
     */
    @Test
    public void testGetDBConnectionParams() {}
}
