/*
 * SLD Editor - The Open Source Java SLD Editor
 *
 * Copyright (C) 2018, SCISYS UK Limited
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

package com.sldeditor.test.unit.rendertransformation.types;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.rendertransformation.types.InterpolationValues;
import com.sldeditor.ui.detail.config.FieldConfigBase;
import com.sldeditor.ui.detail.config.FieldConfigCommonData;
import com.sldeditor.ui.detail.config.FieldConfigEnum;
import com.sldeditor.ui.detail.config.symboltype.SymbolTypeConfig;
import it.geosolutions.jaiext.interpolators.InterpolationBilinear;
import it.geosolutions.jaiext.interpolators.InterpolationNearest;
import java.util.Arrays;
import javax.media.jai.Interpolation;
import javax.media.jai.InterpolationBicubic;
import javax.media.jai.InterpolationBicubic2;
import org.geotools.factory.CommonFactoryFinder;
import org.junit.jupiter.api.Test;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;

/**
 * Unit test for InterpolationValues class.
 *
 * <p>{@link com.sldeditor.rendertransformation.test.InterpolationValues}
 *
 * @author Robert Ward (SCISYS)
 */
class InterpolationValuesTest {

    private FilterFactory ff = CommonFactoryFinder.getFilterFactory();

    class TestInterpolationValues extends InterpolationValues {

        /*
         * (non-Javadoc)
         *
         * @see com.sldeditor.rendertransformation.types.InterpolationValues#populateSymbolType(com.
         * sldeditor.ui.detail.config.symboltype.SymbolTypeConfig)
         */
        @Override
        protected void populateSymbolType(SymbolTypeConfig symbolTypeConfig) {
            super.populateSymbolType(symbolTypeConfig);
        }
    }

    /**
     * Test method for {@link
     * com.sldeditor.rendertransformation.types.InterpolationValues#InterpolationValues()}.
     */
    @Test
    void testInterpolationValues() {
        InterpolationValues testObj = new InterpolationValues();
        testObj.createInstance();

        assertEquals(Arrays.asList(Interpolation.class), testObj.getType());

        Interpolation interpolation = new InterpolationBicubic2(3);
        testObj.setDefaultValue(interpolation);
        assertEquals(
                String.format("%s(%d)", interpolation.getClass().getSimpleName(), 8),
                testObj.getExpression().toString());

        // Interpolation value
        testObj.setValue(ff.literal(interpolation.getClass().getSimpleName()));
        assertEquals(
                String.format("%s(%d)", InterpolationBicubic2.class.getSimpleName(), 8),
                testObj.getExpression().toString());

        interpolation = new InterpolationBilinear(8, null, false, 1.0, 1);
        testObj.setValue(ff.literal(interpolation.getClass().getSimpleName()));
        assertEquals(
                InterpolationBilinear.class.getSimpleName(), testObj.getExpression().toString());

        testObj.setValue(
                ff.literal(
                        String.format("%s(%d)", InterpolationBicubic.class.getSimpleName(), 16)));
        assertEquals(
                String.format("%s(%d)", InterpolationBicubic.class.getSimpleName(), 16),
                testObj.getExpression().toString());

        // Literal expression
        interpolation = new InterpolationNearest(null, false, 1.0, 1);
        Expression expectedExpression = ff.literal(interpolation.getClass().getSimpleName());
        testObj.setValue(expectedExpression);
        assertEquals(
                testObj.getExpression().toString(), InterpolationNearest.class.getSimpleName());

        // Attribute expression
        expectedExpression = ff.property("test");
        testObj.setValue(expectedExpression);
        assertNull(testObj.getExpression());

        // Not set
        testObj.setValue("");
        assertNull(testObj.getExpression());

        FieldConfigBase field =
                testObj.getField(
                        new FieldConfigCommonData(
                                InterpolationValues.class,
                                FieldIdEnum.INITIAL_GAP,
                                "label",
                                true,
                                false,
                                false));
        assertEquals(FieldConfigEnum.class, field.getClass());

        // Increase code coverage
        TestInterpolationValues testObj2 = new TestInterpolationValues();
        testObj2.populateSymbolType(null);

        SymbolTypeConfig config = new SymbolTypeConfig(String.class);
        testObj2.populateSymbolType(config);
        assertTrue(config.getKeyOrderList().size() > 0);
    }
}
