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

package com.sldeditor.test.unit.datasource.connector;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.sldeditor.common.DataSourceConnectorInterface;
import com.sldeditor.common.DataSourcePropertiesInterface;
import com.sldeditor.datasource.connector.DataSourceConnectorFactory;
import com.sldeditor.datasource.connector.instance.DataSourceConnectorEmpty;
import com.sldeditor.datasource.connector.instance.DataSourceConnectorFileGDB;
import com.sldeditor.datasource.connector.instance.DataSourceConnectorPostgres;
import com.sldeditor.datasource.connector.instance.DataSourceConnectorShapeFile;

/**
 * Unit test for DataSourceConnectorFactory class.
 * <p>{@link com.sldeditor.datasource.connector.DataSourceConnectorFactory}
 * 
 * @author Robert Ward (SCISYS)
 *
 */
public class DataSourceConnectorFactoryTest {

    /**
     * Test method for {@link com.sldeditor.datasource.connector.DataSourceConnectorFactory#getDataSourceConnectorList()}.
     */
    @Test
    public void testGetDataSourceConnectorList() {

        Map<String, DataSourceConnectorInterface> actual = DataSourceConnectorFactory.getDataSourceConnectorList();

        assertFalse(actual.isEmpty());
    }

    /**
     * Test method for {@link com.sldeditor.datasource.connector.DataSourceConnectorFactory#getDataSourceProperties(java.lang.String)}.
     */
    @Test
    public void testGetDataSourcePropertiesString() {

        DataSourcePropertiesInterface dsp = DataSourceConnectorFactory.getDataSourceProperties("filename.shp");

        assertEquals(DataSourceConnectorShapeFile.class, dsp.getDataSourceConnector().getClass());

        dsp = DataSourceConnectorFactory.getDataSourceProperties("file.gdb");

        assertEquals(DataSourceConnectorFileGDB.class, dsp.getDataSourceConnector().getClass());
    }

    /**
     * Test method for {@link com.sldeditor.datasource.connector.DataSourceConnectorFactory#getNoDataSource()}.
     */
    @Test
    public void testGetNoDataSource() {
        DataSourcePropertiesInterface dsp = DataSourceConnectorFactory.getNoDataSource();

        assertEquals(DataSourceConnectorEmpty.class, dsp.getDataSourceConnector().getClass());
    }

    /**
     * Test method for {@link com.sldeditor.datasource.connector.DataSourceConnectorFactory#getDataSourceProperties(java.util.Map)}.
     */
    @Test
    public void testGetDataSourcePropertiesMapOfStringString() {
        Map<String, String> propertyMap = new HashMap<String,String>();

        propertyMap.put("url", "filename.shp");
        DataSourcePropertiesInterface dsp = DataSourceConnectorFactory.getDataSourceProperties(propertyMap);

        assertEquals(DataSourceConnectorShapeFile.class, dsp.getDataSourceConnector().getClass());

        propertyMap.clear();
        propertyMap.put("server", "localhost");
        propertyMap.put("port", "5432");
        propertyMap.put("database", "testdb");
        propertyMap.put("user", "testuser");
        propertyMap.put("password", "pasword123");

        dsp = DataSourceConnectorFactory.getDataSourceProperties(propertyMap);

        assertEquals(DataSourceConnectorPostgres.class, dsp.getDataSourceConnector().getClass());
    }

    /**
     * Test method for {@link com.sldeditor.datasource.connector.DataSourceConnectorFactory#getFileExtension(java.lang.String)}.
     */
    @Test
    public void testGetFileExtension() {
        String extension = "shp";

        assertEquals(extension, DataSourceConnectorFactory.getFileExtension("testfilename." + extension));
    }

}
