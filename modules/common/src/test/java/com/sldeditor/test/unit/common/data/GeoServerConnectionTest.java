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

package com.sldeditor.test.unit.common.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Test;

import com.sldeditor.common.data.GeoServerConnection;

/**
 * The unit test for GeoServerConnection.
 * <p>{@link com.sldeditor.common.data.GeoServerConnection}
 *
 * @author Robert Ward (SCISYS)
 */
public class GeoServerConnectionTest {

    /** The connection name. */
    private String connectionName = "my geoserver";

    /** The password. */
    private String password = "not secret password";

    /** The user name. */
    private String userName = "test user name";

    /** The url. */
    private URL url = null;

    /**
     * Test method for {@link com.sldeditor.common.data.GeoServerConnection#getUrl()}.
     * Test method for {@link com.sldeditor.common.data.GeoServerConnection#setUrl(java.net.URL)}.
     */
    @Test
    public void testGetUrl() {
        try {
            URL url = new URL("http://www.example.com/dummy");

            GeoServerConnection connection = new GeoServerConnection();

            connection.setUrl(url);

            assertEquals(connection.getUrl().toExternalForm(), url.toExternalForm());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            fail();
        }
    }

    /**
     * Test method for {@link com.sldeditor.common.data.GeoServerConnection#getUserName()}.
     * Test method for {@link com.sldeditor.common.data.GeoServerConnection#setUserName(java.lang.String)}.
     */
    @Test
    public void testGetUserName() {
        GeoServerConnection connection = new GeoServerConnection();
        String userName = "test user name";
        connection.setUserName(userName);

        assertEquals(connection.getUserName(), userName);
    }

    /**
     * Test method for {@link com.sldeditor.common.data.GeoServerConnection#getPassword()}.
     * Test method for {@link com.sldeditor.common.data.GeoServerConnection#setPassword(java.lang.String)}.
     */
    @Test
    public void testGetPassword() {
        GeoServerConnection connection = new GeoServerConnection();
        String password = "not secret password";
        connection.setPassword(password);

        assertEquals(connection.getPassword(), password);
    }

    /**
     * Test method for {@link com.sldeditor.common.data.GeoServerConnection#getConnectionName()}.
     * Test method for {@link com.sldeditor.common.data.GeoServerConnection#setConnectionName(java.lang.String)}.
     */
    @Test
    public void testGetConnectionName() {
        GeoServerConnection connection = new GeoServerConnection();
        String connectionName = "my geoserver";
        connection.setConnectionName(connectionName);

        assertEquals(connection.getConnectionName(), connectionName);
    }

    /**
     * Test method for {@link com.sldeditor.common.data.GeoServerConnection#compareTo(com.sldeditor.common.data.GeoServerConnection)}.
     */
    @Test
    public void testCompareTo() {
        GeoServerConnection connection1 = getTestData();

        GeoServerConnection connection2 = new GeoServerConnection();
        connection2.setConnectionName(connectionName);
        connection2.setUrl(url);
        connection2.setUserName(userName);
        connection2.setPassword(password);

        assertEquals(connection1.compareTo(connection2), 0);

        connection2.setConnectionName("new connection");

        assertEquals(connection1.compareTo(connection2), -1);

        connection2.setConnectionName(connectionName);

        assertEquals(connection1.compareTo(connection2), 0);

        // Setting password does nothing
        connection2.setPassword("different password");

        assertEquals(connection1.compareTo(connection2), 0);
    }

    /**
     * Test method for {@link com.sldeditor.common.data.GeoServerConnection#update(com.sldeditor.common.data.GeoServerConnection)}.
     */
    @Test
    public void testUpdate() {
        GeoServerConnection connection1 = getTestData();

        GeoServerConnection connection2 = new GeoServerConnection();
        connection2.update(connection1);

        assertEquals(connection1.getConnectionName(), connection2.getConnectionName());
        assertEquals(connection1.getPassword(), connection2.getPassword());
        assertEquals(connection1.getUserName(), connection2.getUserName());
        assertEquals(connection1.getUrl().toExternalForm(), connection2.getUrl().toExternalForm());
    }

    /**
     * Test method for {@link com.sldeditor.common.data.GeoServerConnection#duplicate()}.
     */
    @Test
    public void testDuplicate() {
        GeoServerConnection connection1 = getTestData();

        GeoServerConnection connection2 = connection1.duplicate();
        assertEquals("Copy of " + connection1.getConnectionName(), connection2.getConnectionName());
        assertEquals(connection1.getPassword(), connection2.getPassword());
        assertEquals(connection1.getUserName(), connection2.getUserName());
        assertEquals(connection1.getUrl().toExternalForm(), connection2.getUrl().toExternalForm());
    }

    /**
     * Gets the test data.
     *
     * @return the test data
     */
    private GeoServerConnection getTestData() {
        try {
            url = new URL("http://www.example.com/dummy");

        } catch (MalformedURLException e) {
            e.printStackTrace();
            fail();
        }

        GeoServerConnection connection1 = new GeoServerConnection();
        connection1.setConnectionName(connectionName);
        connection1.setUrl(url);
        connection1.setUserName(userName);
        connection1.setPassword(password);
        return connection1;
    }

    /**
     * Test method for {@link com.sldeditor.common.data.GeoServerConnection#encodeAsString()}.
     * Test method for {@link com.sldeditor.common.data.GeoServerConnection#GeoServerConnection(java.lang.String)}.
     */
    @Test
    public void testEncodeAsString() {
        GeoServerConnection connection1 = getTestData();

        String connectionString = connection1.encodeAsString();

        GeoServerConnection connection2 = new GeoServerConnection(connectionString);

        assertEquals(connection1.getConnectionName(), connection2.getConnectionName());
        assertEquals(connection1.getPassword(), connection2.getPassword());
        assertEquals(connection1.getUserName(), connection2.getUserName());
        assertEquals(connection1.getUrl().toExternalForm(), connection2.getUrl().toExternalForm());
    }

}
