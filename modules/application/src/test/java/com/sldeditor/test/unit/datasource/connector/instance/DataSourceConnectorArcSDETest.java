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
package com.sldeditor.test.unit.datasource.connector.instance;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.sldeditor.common.DataSourcePropertiesInterface;
import com.sldeditor.datasource.connector.instance.DataSourceConnectorArcSDE;
import com.sldeditor.datasource.impl.DataSourceProperties;

/**
 * Unit test for DataSourceConnectorArcSDE class.
 * <p>{@link com.sldeditor.datasource.connector.instance.DataSourceConnectorArcSDE}
 * 
 * @author Robert Ward (SCISYS)
 *
 */
public class DataSourceConnectorArcSDETest {

    /**
     * Test method for {@link com.sldeditor.datasource.connector.instance.DataSourceConnectorArcSDE#getDisplayName()}.
     */
    @Test
    public void testGetDisplayName() {
        DataSourceConnectorArcSDE dsc = new DataSourceConnectorArcSDE();

        assertEquals("ArcSDE", dsc.getDisplayName());
    }

    /**
     * Test method for {@link com.sldeditor.datasource.connector.instance.DataSourceConnectorArcSDE#getPanel()}.
     */
    @Test
    public void testGetPanel() {
        DataSourceConnectorArcSDE dsc = new DataSourceConnectorArcSDE();

        assertTrue(dsc.getPanel() != null);
    }

    /**
     * Test method for {@link com.sldeditor.datasource.connector.instance.DataSourceConnectorArcSDE#accept(java.util.Map)}.
     */
    @Test
    public void testAccept() {
        DataSourceConnectorArcSDE dsc = new DataSourceConnectorArcSDE();

        assertNull(dsc.accept((String)null));
        assertFalse(dsc.accept((Map<String,String>)null));

        Map<String, String> propertyMap = new HashMap<String, String>();

        propertyMap.put("test", "filename");
        assertFalse(dsc.accept(propertyMap));

        // Valid file - with default version
        propertyMap.put("server", "localhost");
        propertyMap.put("port", "5432");
        propertyMap.put("instance", "testdb");
        propertyMap.put("user", "testuser");
        propertyMap.put("user", "testuser");
        propertyMap.put("dbtype", "ORACLE");
        propertyMap.put("password", "pasword123");
        assertTrue(dsc.accept(propertyMap));

        assertTrue(dsc.accept(propertyMap));
        propertyMap.put("version", "version1");
        
        propertyMap.clear();


        // Invalid file
        propertyMap.put("server", "localhost");
        propertyMap.put("database", "testdb");
        propertyMap.put("user", "testuser");
        propertyMap.put("password", "pasword123");
        assertFalse(dsc.accept(propertyMap));
    }

    /**
     * Test method for {@link com.sldeditor.datasource.connector.instance.DataSourceConnectorArcSDE#getDataSourceProperties(java.util.Map)}.
     */
    @Test
    public void testGetDataSourceProperties() {
        DataSourceConnectorArcSDE dsc = new DataSourceConnectorArcSDE();

        Map<String, String> propertyMap = new HashMap<String, String>();

        assertTrue(dsc.getDataSourceProperties(propertyMap) != null);
        assertTrue(dsc.getDataSourceProperties(null) != null);
    }

    /**
     * Test method for {@link com.sldeditor.datasource.connector.instance.DataSourceConnectorArcSDE#populate(com.sldeditor.DataSourcePropertiesInterface)}.
     */
    @Test
    public void testPopulate() {
        DataSourceConnectorArcSDE dsc = new DataSourceConnectorArcSDE();
        dsc.populate(null);

        // Does nothing
    }

    /**
     * Test method for {@link com.sldeditor.datasource.connector.instance.DataSourceConnectorArcSDE#isEmpty()}.
     */
    @Test
    public void testIsEmpty() {
        DataSourceConnectorArcSDE dsc = new DataSourceConnectorArcSDE();

        assertFalse(dsc.isEmpty());
    }

    /**
     * Test method for {@link com.sldeditor.datasource.connector.instance.DataSourceConnectorArcSDE#getConnectionProperties(com.sldeditor.DataSourcePropertiesInterface)}.
     */
    @Test
    public void testGetConnectionProperties() {
        DataSourceConnectorArcSDE dsc = new DataSourceConnectorArcSDE();

        assertNull(dsc.getConnectionProperties(null));

        DataSourcePropertiesInterface dataSource = new DataSourceProperties(dsc);

        Map<String, String> expectedPropertyMap = new HashMap<String, String>();
        expectedPropertyMap.put("server", "localhost");
        expectedPropertyMap.put("port", "5432");
        expectedPropertyMap.put("instance", "testdb");
        expectedPropertyMap.put("user", "testuser");
        expectedPropertyMap.put("user", "testuser");
        expectedPropertyMap.put("dbtype", "ORACLE");
        expectedPropertyMap.put("password", "pasword123");
        
        dataSource.setPropertyMap(expectedPropertyMap);
        assertEquals(expectedPropertyMap, dsc.getConnectionProperties(dataSource));
    }

}
