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
import com.sldeditor.datasource.connector.instance.DataSourceConnectorShapeFile;

/**
 * Unit test for DataSourceConnectorShapeFileTest.java class.
 * <p>{@link com.sldeditor.datasource.connector.instance.DataSourceConnectorShapeFile}
 * 
 * @author Robert Ward (SCISYS)
 *
 */
public class DataSourceConnectorShapeFileTest {

    /**
     * Test method for {@link com.sldeditor.datasource.connector.instance.DataSourceConnectorShapeFile#getDisplayName()}.
     */
    @Test
    public void testGetDisplayName() {
        DataSourceConnectorShapeFile dsc = new DataSourceConnectorShapeFile();

        assertEquals("Shape File", dsc.getDisplayName());
    }

    /**
     * Test method for {@link com.sldeditor.datasource.connector.instance.DataSourceConnectorShapeFile#getPanel()}.
     */
    @Test
    public void testGetPanel() {
        DataSourceConnectorShapeFile dsc = new DataSourceConnectorShapeFile();

        assertTrue(dsc.getPanel() != null);
    }

    /**
     * Test method for {@link com.sldeditor.datasource.connector.instance.DataSourceConnectorShapeFile#accept(java.util.Map)}.
     */
    @Test
    public void testAccept() {
        DataSourceConnectorShapeFile dsc = new DataSourceConnectorShapeFile();

        assertNull(dsc.accept((String)null));
        assertFalse(dsc.accept((Map<String,String>)null));

        Map<String, String> propertyMap = new HashMap<String, String>();

        propertyMap.put("test", "filename");
        assertFalse(dsc.accept(propertyMap));

        // Valid file
        propertyMap.put("url", "test.shp");
        assertTrue(dsc.accept(propertyMap));

        // Invalid file
        propertyMap.put("url", "test.tif");
        assertFalse(dsc.accept(propertyMap));
    }

    /**
     * Test method for {@link com.sldeditor.datasource.connector.instance.DataSourceConnectorShapeFile#getDataSourceProperties(java.util.Map)}.
     */
    @Test
    public void testGetDataSourceProperties() {
        DataSourceConnectorShapeFile dsc = new DataSourceConnectorShapeFile();

        Map<String, String> propertyMap = new HashMap<String, String>();

        assertTrue(dsc.getDataSourceProperties(propertyMap) != null);
        assertTrue(dsc.getDataSourceProperties(null) != null);
    }

    /**
     * Test method for {@link com.sldeditor.datasource.connector.instance.DataSourceConnectorShapeFile#populate(com.sldeditor.common.DataSourcePropertiesInterface)}.
     */
    @Test
    public void testPopulate() {
        DataSourceConnectorShapeFile dsc = new DataSourceConnectorShapeFile();
        dsc.populate(null);

        // Does nothing
    }

    /**
     * Test method for {@link com.sldeditor.datasource.connector.instance.DataSourceConnectorShapeFile#isEmpty()}.
     */
    @Test
    public void testIsEmpty() {
        DataSourceConnectorShapeFile dsc = new DataSourceConnectorShapeFile();

        assertFalse(dsc.isEmpty());
    }

    /**
     * Test method for {@link com.sldeditor.datasource.connector.instance.DataSourceConnectorShapeFile#getConnectionProperties(com.sldeditor.common.DataSourcePropertiesInterface)}.
     */
    @Test
    public void testGetConnectionProperties() {
        DataSourceConnectorShapeFile dsc = new DataSourceConnectorShapeFile();

        assertNull(dsc.getConnectionProperties(null));

        String expectedFilename = "test.shp";
        DataSourcePropertiesInterface dataSource = DataSourceConnectorFactory.getDataSourceProperties(expectedFilename);

        Map<String, String> expectedPropertyMap = new HashMap<String, String>();
        expectedPropertyMap.put("url", expectedFilename);
        assertEquals(expectedPropertyMap, dsc.getConnectionProperties(dataSource));
    }

}
