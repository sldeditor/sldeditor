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
import com.sldeditor.datasource.connector.instance.DataSourceConnectorEmpty;

/**
 * Unit test for DataSourceConnectorEmpty class.
 * 
 * <p>{@link com.sldeditor.datasource.connector.instance.DataSourceConnectorEmpty}
 * 
 * @author Robert Ward (SCISYS)
 *
 */
public class DataSourceConnectorEmptyTest {

    /**
     * Test method for {@link com.sldeditor.datasource.connector.instance.DataSourceConnectorEmpty#getDisplayName()}.
     */
    @Test
    public void testGetDisplayName() {
        DataSourceConnectorEmpty dsc = new DataSourceConnectorEmpty();

        assertEquals("No data source", dsc.getDisplayName());
    }

    /**
     * Test method for {@link com.sldeditor.datasource.connector.instance.DataSourceConnectorEmpty#getPanel()}.
     */
    @Test
    public void testGetPanel() {
        DataSourceConnectorEmpty dsc = new DataSourceConnectorEmpty();

        assertTrue(dsc.getPanel() != null);
    }

    /**
     * Test method for {@link com.sldeditor.datasource.connector.instance.DataSourceConnectorEmpty#accept(java.util.Map)}.
     */
    @Test
    public void testAccept() {
        DataSourceConnectorEmpty dsc = new DataSourceConnectorEmpty();

        assertNull(dsc.accept((String)null));
        assertFalse(dsc.accept((Map<String, Object>)null));

        Map<String, Object> propertyMap = new HashMap<String, Object>();
        assertFalse(dsc.accept(propertyMap));
    }

    /**
     * Test method for {@link com.sldeditor.datasource.connector.instance.DataSourceConnectorEmpty#getDataSourceProperties(java.util.Map)}.
     */
    @Test
    public void testGetDataSourceProperties() {
        DataSourceConnectorEmpty dsc = new DataSourceConnectorEmpty();

        Map<String, Object> propertyMap = new HashMap<String, Object>();

        assertNull(dsc.getDataSourceProperties(propertyMap));
        assertNull(dsc.getDataSourceProperties(null));
    }

    /**
     * Test method for {@link com.sldeditor.datasource.connector.instance.DataSourceConnectorEmpty#populate(com.sldeditor.DataSourcePropertiesInterface)}.
     */
    @Test
    public void testPopulate() {
        DataSourceConnectorEmpty dsc = new DataSourceConnectorEmpty();
        dsc.populate(null);

        // Does nothing
    }

    /**
     * Test method for {@link com.sldeditor.datasource.connector.instance.DataSourceConnectorEmpty#isEmpty()}.
     */
    @Test
    public void testIsEmpty() {
        DataSourceConnectorEmpty dsc = new DataSourceConnectorEmpty();

        assertTrue(dsc.isEmpty());
    }

    /**
     * Test method for {@link com.sldeditor.datasource.connector.instance.DataSourceConnectorEmpty#getConnectionProperties(com.sldeditor.DataSourcePropertiesInterface)}.
     */
    @Test
    public void testGetConnectionProperties() {
        DataSourceConnectorEmpty dsc = new DataSourceConnectorEmpty();

        assertNull(dsc.getConnectionProperties(null));
        
        DataSourcePropertiesInterface noDataSource = DataSourceConnectorFactory.getNoDataSource();
        
        Map<String, String> expectedPropertyMap = new HashMap<String, String>();

        assertEquals(expectedPropertyMap, dsc.getConnectionProperties(noDataSource));
    }

}
