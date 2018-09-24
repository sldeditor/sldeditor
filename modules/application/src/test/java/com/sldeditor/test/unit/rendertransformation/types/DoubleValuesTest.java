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

import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.rendertransformation.types.DoubleValues;
import com.sldeditor.ui.detail.config.FieldConfigBase;
import com.sldeditor.ui.detail.config.FieldConfigCommonData;
import com.sldeditor.ui.detail.config.FieldConfigDouble;
import com.sldeditor.ui.detail.config.symboltype.SymbolTypeConfig;
import java.util.Arrays;
import org.geotools.factory.CommonFactoryFinder;
import org.junit.jupiter.api.Test;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;

/**
 * Unit test for DoubleValues class.
 *
 * <p>{@link com.sldeditor.rendertransformation.test.DoubleValues}
 *
 * @author Robert Ward (SCISYS)
 */
class DoubleValuesTest {

    private FilterFactory ff = CommonFactoryFinder.getFilterFactory();

    class TestDoubleValues extends DoubleValues {

        /* (non-Javadoc)
         * @see com.sldeditor.rendertransformation.types.DoubleValues#populateSymbolType(com.sldeditor.ui.detail.config.symboltype.SymbolTypeConfig)
         */
        @Override
        protected void populateSymbolType(SymbolTypeConfig symbolTypeConfig) {
            super.populateSymbolType(symbolTypeConfig);
        }
    }

    /**
     * Test method for {@link com.sldeditor.rendertransformation.types.DoubleValues#DoubleValues()}.
     */
    @Test
    void testDoubleValues() {
        DoubleValues testObj = new DoubleValues();
        testObj.createInstance();

        assertEquals(Arrays.asList(double.class, Double.class), testObj.getType());

        testObj.setDefaultValue(1.234);
        assertEquals(testObj.getExpression().toString(), "1.234");

        // Double value
        testObj.setValue(9.8);
        assertEquals(testObj.getExpression().toString(), "9.8");

        // Literal expression
        Expression expectedExpression = ff.literal(3.5);
        testObj.setValue(expectedExpression);
        assertEquals(testObj.getExpression().toString(), "3.5");

        // Attribute expression
        expectedExpression = ff.property("test");
        testObj.setValue(expectedExpression);
        assertEquals(expectedExpression, testObj.getExpression());

        // Not set
        testObj.setValue("");
        assertNull(testObj.getExpression());

        FieldConfigBase field =
                testObj.getField(
                        new FieldConfigCommonData(
                                DoubleValues.class,
                                FieldIdEnum.INITIAL_GAP,
                                "label",
                                true,
                                false,
                                false));
        assertEquals(FieldConfigDouble.class, field.getClass());

        // Increase code coverage
        TestDoubleValues testObj2 = new TestDoubleValues();
        testObj2.populateSymbolType(null);
    }
}
