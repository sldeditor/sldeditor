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

import com.sldeditor.importdata.esri.datasource.EsriOracleArcSDE;

/**
 * Unit test for EsriOracleArcSDE class.
 * <p>{@link com.sldeditor.importdata.esri.datasource.EsriOracleArcSDE}
 * 
 * @author Robert Ward (SCISYS)
 *
 */
public class EsriOracleArcSDETest {

    /**
     * Test method for {@link com.sldeditor.importdata.esri.datasource.EsriOracleArcSDE#EsriOracleArcSDE()}.
     * Test method for {@link com.sldeditor.importdata.esri.datasource.EsriOracleArcSDE#getName()}.
     * Test method for {@link com.sldeditor.importdata.esri.datasource.EsriOracleArcSDE#getType()}.
     * Test method for {@link com.sldeditor.importdata.esri.datasource.EsriOracleArcSDE#convert(java.util.Map)}.
     */
    @Test
    public void testEsriFileGDB() {
        EsriOracleArcSDE obj = new EsriOracleArcSDE();

        assertEquals("EsriOracleArcSDE", obj.getName());
        assertEquals(2, obj.getType());

        Map<String, String> propertyMap = new LinkedHashMap<String, String>();
        propertyMap.put("INSTANCE", "SDE:ORACLE11G:/;LOCAL\u003dTEST");
        propertyMap.put("DBCLIENT", "oracle");
        propertyMap.put("DB_CONNECTION_PROPERTIES", "/");
        propertyMap.put("PROJECT_INSTANCE", "sde");
        propertyMap.put("ENVIRONMENT", "LOCAL\u003dTEST");
        propertyMap.put("IS_GEODATABASE", "true");
        propertyMap.put("AUTHENTICATION_MODE", "DBMS");
        propertyMap.put("USER", "GISVIEWER");
        propertyMap.put("CONNPROP-REV", "Rev1.0");
        propertyMap.put("VERSION", "SDE.DEFAULT");
        propertyMap.put("path", "test.sde");
        assertTrue(obj.convert(propertyMap) != null);
    }
}
