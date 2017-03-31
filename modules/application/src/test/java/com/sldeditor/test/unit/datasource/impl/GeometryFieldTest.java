/**
 * 
 */

package com.sldeditor.test.unit.datasource.impl;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.sldeditor.datasource.impl.GeometryField;

/**
 * The unit test for GeometryField.
 * 
 * <p>{@link com.sldeditor.datasource.impl.GeometryField}
 *
 * @author Robert Ward (SCISYS)
 */
public class GeometryFieldTest {

    /**
     * Test method for {@link com.sldeditor.datasource.impl.GeometryField#GeometryField()}.
     * Test method for {@link com.sldeditor.datasource.impl.GeometryField#reset()}.
     * Test method for {@link com.sldeditor.datasource.impl.GeometryField#setGeometryFieldName(java.lang.String)}.
     * Test method for {@link com.sldeditor.datasource.impl.GeometryField#getGeometryFieldName()}.
     */
    @Test
    public void testGeometryField() {
        GeometryField geometryField = new GeometryField();

        String expecedtDefaultValue = "geom";
        assertTrue(geometryField.getGeometryFieldName().compareTo(expecedtDefaultValue) == 0);

        String expectedValue = "test geometry string";

        geometryField.setGeometryFieldName(expectedValue);
        assertTrue(geometryField.getGeometryFieldName().compareTo(expectedValue) == 0);

        geometryField.reset();
        assertTrue(geometryField.getGeometryFieldName().compareTo(expecedtDefaultValue) == 0);
    }

}
