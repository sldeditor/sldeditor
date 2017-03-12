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

package com.sldeditor.test.unit.tool.dbconnectionlist;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.sldeditor.common.data.DatabaseConnection;
import com.sldeditor.tool.dbconnectionlist.DatabaseConnectionFactory;
import com.sldeditor.tool.dbconnectionlist.DatabaseConnector;

/**
 * Unit test for DatabaseConnector class.
 * <p>{@link com.sldeditor.tool.dbconnectionlist.DatabaseConnector}
 * 
 * @author Robert Ward (SCISYS)
 *
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
