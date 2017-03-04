/**
 * Unit test for DatabaseConnectionFactory class.
 * <p>{@link com.sldeditor.test.unit.tool.dbconnectionlist.DatabaseConnectionFactory}
 * 
 * @author Robert Ward (SCISYS)
 *
 */
package com.sldeditor.test.unit.tool.dbconnectionlist;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geotools.data.teradata.TeradataDataStoreFactory;
import org.junit.Test;

import com.sldeditor.common.data.DatabaseConnection;
import com.sldeditor.tool.dbconnectionlist.DatabaseConnectionFactory;

public class DatabaseConnectionFactoryTest {

    @Test
    public void testCreateGeoPackage() {
        assertNotNull(DatabaseConnectionFactory.createGeoPackage());
    }

    @Test
    public void testCreateSpatiaLite() {
        assertNotNull(DatabaseConnectionFactory.createSpatiaLite());
    }

    @Test
    public void testCreateDB2() {
        assertNotNull(DatabaseConnectionFactory.createDB2());
    }

    @Test
    public void testCreateH2() {
        assertNotNull(DatabaseConnectionFactory.createH2());
    }

    @Test
    public void testCreateMySQL() {
        assertNotNull(DatabaseConnectionFactory.createMySQL());
    }

    @Test
    public void testCreateOracle() {
        assertNotNull(DatabaseConnectionFactory.createOracle());
    }

    @Test
    public void testCreateSQLServer() {
        assertNotNull(DatabaseConnectionFactory.createSQLServer());
    }

    @Test
    public void testCreateTeradata() {
        assertNotNull(DatabaseConnectionFactory.createTeradata());
    }

    @Test
    public void testCreatePostgres() {
        assertNotNull(DatabaseConnectionFactory.createPostgres());
    }

    @Test
    public void testGetNames() {
        List<String> nameList = DatabaseConnectionFactory.getNames();

        assertFalse(nameList.isEmpty());

        List<String> externalDrivers = new ArrayList<String>();
        externalDrivers.add("Microsoft SQL Server (JNDI)");
        externalDrivers.add("DB2 NG (JNDI)");

        for (String name : nameList) {
            if (!externalDrivers.contains(name)) {
                System.out.println(name);

                DatabaseConnection databaseConnection = DatabaseConnectionFactory
                        .getNewConnection(name);

                assertNotNull(databaseConnection);
                databaseConnection.setConnectionDataMap(new HashMap<String, String>());
                System.out.println(databaseConnection.getConnectionName());

                DatabaseConnection databaseConnection2 = DatabaseConnectionFactory
                        .getNewConnection(databaseConnection);
                assertNotNull(databaseConnection2);
            }
        }

        DatabaseConnection databaseConnection3 = DatabaseConnectionFactory
                .getConnection("test.gpkg");
        assertNotNull(databaseConnection3);
    }

    @Test
    public void testGetConnection() {
        assertNull(DatabaseConnectionFactory.getConnection(null));
        assertNull(DatabaseConnectionFactory.getConnection("test.test"));
    }

    @Test
    public void testDecodeString() {
        Map<String, String> localConnectionDataMap = null;
        assertNull(DatabaseConnectionFactory.decodeString(localConnectionDataMap));

        localConnectionDataMap = new HashMap<String, String>();
        localConnectionDataMap.put("test", "a.db");
        assertNull(DatabaseConnectionFactory.decodeString(localConnectionDataMap));

        localConnectionDataMap.put(DatabaseConnectionFactory.DATABASE_TYPE_KEY,
                (String) TeradataDataStoreFactory.DBTYPE.sample);
        assertNotNull(DatabaseConnectionFactory.decodeString(localConnectionDataMap));
    }

}
