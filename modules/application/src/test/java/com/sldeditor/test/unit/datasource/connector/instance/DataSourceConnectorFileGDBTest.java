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
import com.sldeditor.datasource.connector.DataSourceConnectorFactory;
import com.sldeditor.datasource.connector.instance.DataSourceConnectorFileGDB;

/**
 * Unit test for DataSourceConnectorFileGDB class.
 * <p>{@link com.sldeditor.datasource.connector.instance.DataSourceConnectorFileGDB}
 * 
 * @author Robert Ward (SCISYS)
 *
 */
public class DataSourceConnectorFileGDBTest {

    /**
     * Test method for {@link com.sldeditor.datasource.connector.instance.DataSourceConnectorFileGDB#getDisplayName()}.
     */
    @Test
    public void testGetDisplayName() {
        DataSourceConnectorFileGDB dsc = new DataSourceConnectorFileGDB();

        assertEquals("Esri File GDB", dsc.getDisplayName());
    }

    /**
     * Test method for {@link com.sldeditor.datasource.connector.instance.DataSourceConnectorFileGDB#getPanel()}.
     */
    @Test
    public void testGetPanel() {
        DataSourceConnectorFileGDB dsc = new DataSourceConnectorFileGDB();

        assertTrue(dsc.getPanel() != null);
    }

    /**
     * Test method for {@link com.sldeditor.datasource.connector.instance.DataSourceConnectorFileGDB#accept(java.util.Map)}.
     */
    @Test
    public void testAccept() {
        DataSourceConnectorFileGDB dsc = new DataSourceConnectorFileGDB();

        assertNull(dsc.accept((String)null));
        assertFalse(dsc.accept((Map<String,Object>)null));

        Map<String, Object> propertyMap = new HashMap<String, Object>();

        propertyMap.put("test", "filename");
        assertFalse(dsc.accept(propertyMap));

        // Valid file
        propertyMap.put("database", "test.gdb");
        assertTrue(dsc.accept(propertyMap));
        propertyMap.clear();
        
        // Invalid file
        propertyMap.put("url", "test.shp");
        assertFalse(dsc.accept(propertyMap));
    }

    /**
     * Test method for {@link com.sldeditor.datasource.connector.instance.DataSourceConnectorFileGDB#getDataSourceProperties(java.util.Map)}.
     */
    @Test
    public void testGetDataSourceProperties() {
        DataSourceConnectorFileGDB dsc = new DataSourceConnectorFileGDB();

        Map<String, Object> propertyMap = new HashMap<String, Object>();

        assertTrue(dsc.getDataSourceProperties(propertyMap) != null);
        assertTrue(dsc.getDataSourceProperties(null) != null);
    }

    /**
     * Test method for {@link com.sldeditor.datasource.connector.instance.DataSourceConnectorFileGDB#populate(com.sldeditor.DataSourcePropertiesInterface)}.
     */
    @Test
    public void testPopulate() {
        DataSourceConnectorFileGDB dsc = new DataSourceConnectorFileGDB();
        dsc.populate(null);

        // Does nothing
    }

    /**
     * Test method for {@link com.sldeditor.datasource.connector.instance.DataSourceConnectorFileGDB#isEmpty()}.
     */
    @Test
    public void testIsEmpty() {
        DataSourceConnectorFileGDB dsc = new DataSourceConnectorFileGDB();

        assertFalse(dsc.isEmpty());
    }

    /**
     * Test method for {@link com.sldeditor.datasource.connector.instance.DataSourceConnectorFileGDB#getConnectionProperties(com.sldeditor.DataSourcePropertiesInterface)}.
     */
    @Test
    public void testGetConnectionProperties() {
        DataSourceConnectorFileGDB dsc = new DataSourceConnectorFileGDB();

        assertNull(dsc.getConnectionProperties(null));

        String expectedFilename = "test.gdb";
        DataSourcePropertiesInterface dataSource = DataSourceConnectorFactory.getDataSourceProperties(expectedFilename);

        Map<String, String> expectedPropertyMap = new HashMap<String, String>();
        expectedPropertyMap.put("database", expectedFilename);
        assertEquals(expectedPropertyMap, dsc.getConnectionProperties(dataSource));
    }

}
