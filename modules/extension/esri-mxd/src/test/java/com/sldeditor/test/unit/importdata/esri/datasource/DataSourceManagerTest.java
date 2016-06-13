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
package com.sldeditor.test.unit.importdata.esri.datasource;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Test;

import com.sldeditor.importdata.esri.datasource.DataSourceManager;
import com.sldeditor.importdata.esri.keys.DatasourceKeys;

/**
 * Unit test for DataSourceManager class.
 * <p>{@link com.sldeditor.importdata.esri.datasource.DataSourceManager}
 * 
 * @author Robert Ward (SCISYS)
 *
 */
public class DataSourceManagerTest {

    /**
     * Test method for {@link com.sldeditor.importdata.esri.datasource.DataSourceManager#getInstance()}.
     * Test method for {@link com.sldeditor.importdata.esri.datasource.DataSourceManager#convert(java.lang.String, java.util.Map)}.
     */
    @Test
    public void testConvert() {

        Map<String, String> propertyMap = new LinkedHashMap<String, String>();

        propertyMap.put(DatasourceKeys.TYPE, "42");
        assertNull(DataSourceManager.getInstance().convert(propertyMap));

        propertyMap.put(DatasourceKeys.TYPE, null);
        assertNull(DataSourceManager.getInstance().convert(propertyMap));

        propertyMap.put(DatasourceKeys.TYPE, "invalid");
        assertNull(DataSourceManager.getInstance().convert(propertyMap));

        // Should be FileGDB - type = 1
        propertyMap.put(DatasourceKeys.TYPE, "1");
        assertNull(DataSourceManager.getInstance().convert(propertyMap));

        propertyMap.put("DATABASE", "test.gdb");
        assertTrue(DataSourceManager.getInstance().convert(propertyMap) != null);
    }

}
