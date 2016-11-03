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
package com.sldeditor.test.unit.datasource.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.junit.Test;

import com.sldeditor.common.data.SLDUtils;
import com.sldeditor.datasource.attribute.DataSourceAttributeData;
import com.sldeditor.datasource.impl.ExtractAttributes;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

/**
 * Unit test for ExtractAttributes class.
 * <p>{@link com.sldeditor.datasource.impl.ExtractAttributes}
 * 
 * @author Robert Ward (SCISYS)
 *
 */
public class ExtractAttributesTest {

    /**
     * Test method for {@link com.sldeditor.datasource.impl.ExtractAttributes#addDefaultFields(org.geotools.feature.simple.SimpleFeatureTypeBuilder, java.lang.String)}.
     */
    @Test
    public void testAddDefaultFields() {

        DummyInternalSLDFile dummy = new DummyInternalSLDFile();

        SimpleFeatureTypeBuilder b = new SimpleFeatureTypeBuilder();

        String typeName = "test type name";
        b.setName( typeName );

        String namespace = null;
        b.setNamespaceURI(namespace);

        //add a geometry property
        b.setCRS( DefaultGeographicCRS.WGS84 ); // set crs first

        b.add( "geom", Point.class );

        b.setDefaultGeometry( "geom" );

        ExtractAttributes extract = new ExtractAttributes();
        extract.extractDefaultFields(b, SLDUtils.createSLDFromString(dummy.getSLDData()));

        // Check fields extracted ok
        List<String> expectedFieldList = dummy.getExpectedFieldList();
        List<DataSourceAttributeData> actualFieldnameList = extract.getFields();
        assertTrue(expectedFieldList.size() == actualFieldnameList.size());

        // Not assuming fields are in the same order
        int count = 0;
        for(DataSourceAttributeData dataSourceField : actualFieldnameList)
        {
            if(expectedFieldList.contains(dataSourceField.getName()))
            {
                count ++;
            }
        }
        assertTrue(expectedFieldList.size() == count);

        // Check geometry fields extracted ok
        List<String> actualGeometryFields = extract.getGeometryFields();
        assertEquals(0, actualGeometryFields.size());
    }

    /**
     * Test sld symbol contains non-default geometry field.
     * 
     * Test method for {@link com.sldeditor.datasource.impl.ExtractAttributes#addDefaultFields(org.geotools.feature.simple.SimpleFeatureTypeBuilder, java.lang.String)}.
     */
    @Test
    public void testNonStandardGeometryField() {

        DummyInternalSLDFile2 dummy = new DummyInternalSLDFile2();

        SimpleFeatureTypeBuilder b = new SimpleFeatureTypeBuilder();

        String typeName = "test type name";
        b.setName( typeName );

        String namespace = null;
        b.setNamespaceURI(namespace);

        String expectedGeometryFieldName = dummy.getExpectedGeometryFieldList().get(0);
        //add a geometry property
        b.setCRS( DefaultGeographicCRS.WGS84 ); // set crs first

        b.add( expectedGeometryFieldName, Polygon.class );

        b.setDefaultGeometry( expectedGeometryFieldName );

        ExtractAttributes extract = new ExtractAttributes();
        extract.extractDefaultFields(b, SLDUtils.createSLDFromString(dummy.getSLDData()));

        // Check fields extracted ok - should be none
        List<String> expectedFieldList = dummy.getExpectedFieldList();
        List<DataSourceAttributeData> actualFieldnameList = extract.getFields();
        assertTrue(expectedFieldList.size() == actualFieldnameList.size());
        assertEquals(0, actualFieldnameList.size());

        // Check geometry fields extracted ok
        List<String> actualGeometryFields = extract.getGeometryFields();
        assertEquals(1, actualGeometryFields.size());
        assertTrue(expectedGeometryFieldName.compareTo(actualGeometryFields.get(0)) == 0);
    }

    /**
     * Test sld symbol contains non-default geometry field and non-standard xml namespace.
     * 
     * Test method for {@link com.sldeditor.datasource.impl.ExtractAttributes#addDefaultFields(org.geotools.feature.simple.SimpleFeatureTypeBuilder, java.lang.String)}.
     */
    @Test
    public void testNonStandardGeometryNamespace() {

        DummyInternalSLDFile3 dummy = new DummyInternalSLDFile3();

        SimpleFeatureTypeBuilder b = new SimpleFeatureTypeBuilder();

        String typeName = "test type name";
        b.setName( typeName );

        String namespace = null;
        b.setNamespaceURI(namespace);

        String expectedGeometryFieldName = dummy.getExpectedGeometryFieldList().get(0);
        //add a geometry property
        b.setCRS( DefaultGeographicCRS.WGS84 ); // set crs first

        b.add( expectedGeometryFieldName, Point.class );

        b.setDefaultGeometry( expectedGeometryFieldName );

        ExtractAttributes extract = new ExtractAttributes();
        extract.extractDefaultFields(b, SLDUtils.createSLDFromString(dummy.getSLDData()));

        // Check fields extracted ok - should be none
        List<String> expectedFieldList = dummy.getExpectedFieldList();
        List<DataSourceAttributeData> actualFieldnameList = extract.getFields();
        assertTrue(expectedFieldList.size() == actualFieldnameList.size());

        // Not assuming fields are in the same order
        int count = 0;
        for(DataSourceAttributeData dataSourceField : actualFieldnameList)
        {
            if(expectedFieldList.contains(dataSourceField.getName()))
            {
                count ++;
            }
        }
        assertTrue(expectedFieldList.size() == count);

        // Check geometry fields extracted ok
        List<String> actualGeometryFields = extract.getGeometryFields();
        assertEquals(1, actualGeometryFields.size());
        assertTrue(expectedGeometryFieldName.compareTo(actualGeometryFields.get(0)) == 0);
    }
}
