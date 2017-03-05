/*
 *    SLDEditor - SLD Editor application
 *
 *    (C) 2016, SCISYS
 *
 */

package com.sldeditor.test.unit.datasource.impl;

import static org.junit.Assert.assertEquals;

import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;

import com.sldeditor.datasource.impl.ExtendedSimpleFeatureTypeBuilder;
import com.vividsolutions.jts.geom.Point;

/**
 * Unit test for ExtendedSimpleFeatureTypeBuilder.
 * <p>
 * {@link com.sldeditor.datasource.impl.ExtendedSimpleFeatureTypeBuilder}
 * 
 * @author Robert Ward (SCISYS)
 *
 */
public class ExtendedSimpleFeatureTypeBuilderTest {

    /**
     * Test method for
     * {@link com.sldeditor.datasource.impl.ExtendedSimpleFeatureTypeBuilder#createAttributeDescriptor(java.lang.String, java.lang.Class)}.
     */
    @Test
    public void testCreateAttributeDescriptor() {

        ExtendedSimpleFeatureTypeBuilder testObj = new ExtendedSimpleFeatureTypeBuilder();
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();

        builder.setName("simplefeaturetype");
        builder.add("field1", String.class);
        builder.add("field2", Point.class);
        builder.add("field3", Integer.class);
        builder.add("field4", Double.class);
        builder.setDefaultGeometry("field2");
        SimpleFeatureType featureType = builder.buildFeatureType();

        AttributeDescriptor actual = testObj.createAttributeDescriptor("field1", String.class);
        assertEquals(featureType.getAttributeDescriptors().get(0), actual);

        actual = testObj.createAttributeDescriptor("field2", Point.class);
        assertEquals(featureType.getAttributeDescriptors().get(1), actual);

        actual = testObj.createAttributeDescriptor("field3", Integer.class);
        assertEquals(featureType.getAttributeDescriptors().get(2), actual);

        actual = testObj.createAttributeDescriptor("field4", Double.class);
        assertEquals(featureType.getAttributeDescriptors().get(3), actual);
    }

}
