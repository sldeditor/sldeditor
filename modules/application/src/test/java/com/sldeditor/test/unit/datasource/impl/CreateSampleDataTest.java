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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.geotools.data.DataStore;
import org.geotools.feature.NameImpl;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeatureType;

import com.sldeditor.datasource.impl.CreateSampleData;
import com.sldeditor.datasource.impl.GeometryTypeEnum;
import com.vividsolutions.jts.geom.Polygon;

/**
 * Unit test for CreateSampleData.
 * <p>{@link com.sldeditor.datasource.impl.CreateSampleData}
 * 
 * @author Robert Ward (SCISYS)
 *
 */
public class CreateSampleDataTest {

    /**
     * Test method for {@link com.sldeditor.datasource.impl.CreateSampleData#getDataStore()}.
     */
    @Test
    public void testGetDataStore() {

        CreateSampleData sampleData = new CreateSampleData();

        SimpleFeatureTypeBuilder b = new SimpleFeatureTypeBuilder();

        String typeName = "test type name";
        b.setName( typeName );

        String namespace = null;
        b.setNamespaceURI(namespace);

        //add a geometry property
        b.setCRS( DefaultGeographicCRS.WGS84 ); // set crs first

        b.add( "the_geom", Polygon.class );

        b.setDefaultGeometry( "the_geom" );

        // Build the feature type
        SimpleFeatureType schema = b.buildFeatureType();

        sampleData.create(schema);
        
        DataStore dataStore = sampleData.getDataStore();
        
        assertTrue(dataStore != null);
        assertEquals(GeometryTypeEnum.POLYGON, sampleData.getGeometryType());
    }

    /**
     * Test method for {@link com.sldeditor.datasource.impl.CreateSampleData#getFieldTypeValue(int, org.opengis.feature.type.Name, java.lang.Class)}.
     */
    @Test
    public void testGetFieldTypeValue() {
        String expectedString = "String field";
        assertEquals(expectedString, CreateSampleData.getFieldTypeValue(0, new NameImpl(expectedString), String.class));
        assertEquals(Long.valueOf(1), CreateSampleData.getFieldTypeValue(1, new NameImpl(expectedString), Long.class));
        assertEquals(Short.valueOf((short)2), CreateSampleData.getFieldTypeValue(2, new NameImpl(expectedString), Short.class));
        assertEquals(Integer.valueOf(3), CreateSampleData.getFieldTypeValue(3, new NameImpl(expectedString), Integer.class));
        assertEquals(Float.valueOf(4), CreateSampleData.getFieldTypeValue(4, new NameImpl(expectedString), Float.class));
        assertEquals(Double.valueOf(5), CreateSampleData.getFieldTypeValue(5, new NameImpl(expectedString), Double.class));
        assertNull(CreateSampleData.getFieldTypeValue(6, new NameImpl(expectedString), SimpleFeatureType.class));
    }

}
