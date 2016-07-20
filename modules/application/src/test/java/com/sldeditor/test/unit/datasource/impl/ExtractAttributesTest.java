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

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.PropertyDescriptor;

import com.sldeditor.datasource.impl.ExtractAttributes;
import com.vividsolutions.jts.geom.Point;

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

        DummyInternalSLDEditorFile dummy = new DummyInternalSLDEditorFile();

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
        extract.extractDefaultFields(b, dummy.getSLDData().getSld());

        SimpleFeatureType featureType = b.buildFeatureType();

        Collection<PropertyDescriptor> fieldList = featureType.getDescriptors();
        assertTrue(fieldList != null);

        List<String> actualFieldnameList = new ArrayList<String>();
        for(PropertyDescriptor field : fieldList)
        {
            actualFieldnameList.add(field.getName().getLocalPart());
        }

        // Check fields extracted ok
        List<String> expectedFieldList = dummy.getExpectedFieldList();
        assertTrue(expectedFieldList.size() == actualFieldnameList.size());

        // Not assuming fields are in the same order
        int count = 0;
        for(String fieldName : actualFieldnameList)
        {
            if(expectedFieldList.contains(fieldName))
            {
                count ++;
            }
        }
        assertTrue(expectedFieldList.size() == count);
    }

}
