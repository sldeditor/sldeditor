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

import com.sldeditor.common.DataSourcePropertiesInterface;
import com.sldeditor.datasource.connector.instance.DataSourceConnector;
import com.sldeditor.datasource.impl.DataSourceProperties;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

/**
 * Unit test for DataSourceConnector class.
 *
 * <p>{@link com.sldeditor.datasource.connector.instance.DataSourceConnector}
 *
 * @author Robert Ward (SCISYS)
 */
public class DataSourceConnectorTest {

    /**
     * Test method for {@link
     * com.sldeditor.datasource.connector.instance.DataSourceConnector#getDisplayName()}.
     */
    @Test
    public void testGetDisplayName() {
        DataSourceConnector dsc = new DataSourceConnector();

        assertEquals("Database", dsc.getDisplayName());
    }

    /**
     * Test method for {@link
     * com.sldeditor.datasource.connector.instance.DataSourceConnector#getPanel()}.
     */
    @Test
    public void testGetPanel() {
        DataSourceConnector dsc = new DataSourceConnector();

        assertTrue(dsc.getPanel() != null);
    }

    /**
     * Test method for {@link
     * com.sldeditor.datasource.connector.instance.DataSourceConnector#accept(java.util.Map)}.
     */
    @Test
    public void testAccept() {
        DataSourceConnector dsc = new DataSourceConnector();

        assertNull(dsc.accept((String) null));
        assertFalse(dsc.accept((Map<String, Object>) null));

        Map<String, Object> propertyMap = new HashMap<String, Object>();

        propertyMap.put("test", "filename");
        assertTrue(dsc.accept(propertyMap));

        // Valid file
        propertyMap.put("host", "localhost");
        propertyMap.put("port", "5432");
        propertyMap.put("database", "testdb");
        propertyMap.put("user", "testuser");
        propertyMap.put("schema", "public");
        propertyMap.put("passwd", "pasword123");
        propertyMap.put("featureClass", "testfc");
        assertTrue(dsc.accept(propertyMap));
        propertyMap.clear();
    }

    /**
     * Test method for {@link
     * com.sldeditor.datasource.connector.instance.DataSourceConnector#getDataSourceProperties(java.util.Map)}.
     */
    @Test
    public void testGetDataSourceProperties() {
        DataSourceConnector dsc = new DataSourceConnector();

        Map<String, Object> propertyMap = new HashMap<String, Object>();

        assertTrue(dsc.getDataSourceProperties(propertyMap) != null);
        assertTrue(dsc.getDataSourceProperties(null) != null);
    }

    /**
     * Test method for {@link
     * com.sldeditor.datasource.connector.instance.DataSourceConnector#populate(com.sldeditor.DataSourcePropertiesInterface)}.
     */
    @Test
    public void testPopulate() {
        DataSourceConnector dsc = new DataSourceConnector();
        dsc.populate(null);

        // Does nothing
    }

    /**
     * Test method for {@link
     * com.sldeditor.datasource.connector.instance.DataSourceConnector#isEmpty()}.
     */
    @Test
    public void testIsEmpty() {
        DataSourceConnector dsc = new DataSourceConnector();

        assertFalse(dsc.isEmpty());
    }

    /**
     * Test method for {@link
     * com.sldeditor.datasource.connector.instance.DataSourceConnector#getConnectionProperties(com.sldeditor.DataSourcePropertiesInterface)}.
     */
    @Test
    public void testGetConnectionProperties() {
        DataSourceConnector dsc = new DataSourceConnector();

        assertNull(dsc.getConnectionProperties(null));

        Map<String, Object> expectedPropertyMap = new HashMap<String, Object>();
        expectedPropertyMap.put("server", "localhost");
        expectedPropertyMap.put("port", "5432");
        expectedPropertyMap.put("database", "testdb");
        expectedPropertyMap.put("user", "testuser");
        expectedPropertyMap.put("password", "pasword123");

        DataSourcePropertiesInterface dataSource = new DataSourceProperties(dsc);
        dataSource.setPropertyMap(expectedPropertyMap);
        assertEquals(expectedPropertyMap, dsc.getConnectionProperties(dataSource));
    }
}
