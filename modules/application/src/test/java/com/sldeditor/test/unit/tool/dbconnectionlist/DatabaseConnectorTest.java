/*
 *    SLDEditor - SLD Editor application
 *
 *    (C) 2016, SCISYS
 *
 */

package com.sldeditor.test.unit.tool.dbconnectionlist;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.sldeditor.common.data.DatabaseConnection;
import com.sldeditor.tool.dbconnectionlist.DatabaseConnectionFactory;
import com.sldeditor.tool.dbconnectionlist.DatabaseConnector;

/**
 * A factory for creating DatabaseConnector objects.
 *
 * @author Robert Ward (SCISYS)
 */
public class DatabaseConnectorTest {

    /**
     * Test method for {@link com.sldeditor.tool.dbconnectionlist.DatabaseConnector#DatabaseConnector()}.
     */
    @Test
    public void testDatabaseConnector() {
        DatabaseConnector testObj = new DatabaseConnector();

        DatabaseConnection connection = DatabaseConnectionFactory.createPostgres();

        testObj.setConnection(connection);

        DatabaseConnection connection2 = testObj.getConnection();

        assertNotNull(connection2);
        assertNotNull(testObj.getPanel());
    }

}
