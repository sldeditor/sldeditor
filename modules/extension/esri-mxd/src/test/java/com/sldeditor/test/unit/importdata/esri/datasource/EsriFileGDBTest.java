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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Test;

import com.sldeditor.importdata.esri.datasource.EsriFileGDB;

/**
 * Unit test for EsriFileGDB class.
 * <p>{@link com.sldeditor.importdata.esri.datasource.EsriFileGDB}
 * 
 * @author Robert Ward (SCISYS)
 *
 */
public class EsriFileGDBTest {

    /**
     * Test method for {@link com.sldeditor.importdata.esri.datasource.EsriFileGDB#EsriFileGDB()}.
     * Test method for {@link com.sldeditor.importdata.esri.datasource.EsriFileGDB#getName()}.
     * Test method for {@link com.sldeditor.importdata.esri.datasource.EsriFileGDB#getType()}.
     * Test method for {@link com.sldeditor.importdata.esri.datasource.EsriFileGDB#convert(java.util.Map)}.
     */
    @Test
    public void testEsriFileGDB() {
        EsriFileGDB obj = new EsriFileGDB();
        
        assertEquals("EsriFileGDB", obj.getName());
        assertEquals(1, obj.getType());
        
        Map<String, String> propertyMap = new LinkedHashMap<String, String>();
        propertyMap.put("DATABASE", "test.gdb");
        assertTrue(obj.convert(propertyMap) != null);
    }
}
