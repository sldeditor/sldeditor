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
import com.sldeditor.rendertransformation.types.NumberValues;
import com.sldeditor.ui.detail.config.FieldConfigBase;
import com.sldeditor.ui.detail.config.FieldConfigCommonData;
import com.sldeditor.ui.detail.config.FieldConfigInteger;
import com.sldeditor.ui.detail.config.symboltype.SymbolTypeConfig;
import java.util.Arrays;
import org.geotools.factory.CommonFactoryFinder;
import org.junit.jupiter.api.Test;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;

/**
 * Unit test for NumberValues class.
 *
 * <p>{@link com.sldeditor.rendertransformation.test.NumberValues}
 *
 * @author Robert Ward (SCISYS)
 */
class NumberValuesTest {

    private FilterFactory ff = CommonFactoryFinder.getFilterFactory();

    class TestNumberValues extends NumberValues {

        /* (non-Javadoc)
         * @see com.sldeditor.rendertransformation.types.NumberValues#populateSymbolType(com.sldeditor.ui.detail.config.symboltype.SymbolTypeConfig)
         */
        @Override
        protected void populateSymbolType(SymbolTypeConfig symbolTypeConfig) {
            super.populateSymbolType(symbolTypeConfig);
        }
    }

    /**
     * Test method for {@link com.sldeditor.rendertransformation.types.NumberValues#NumberValues()}.
     */
    @Test
    void testNumberValues() {
        NumberValues testObj = new NumberValues();
        testObj.createInstance();

        assertEquals(Arrays.asList(Number.class), testObj.getType());

        testObj.setDefaultValue(Integer.valueOf(1));
        assertEquals(testObj.getExpression().toString(), "1");

        // Number value
        testObj.setValue(Integer.valueOf(9));
        assertEquals(testObj.getExpression().toString(), "9");

        // Literal expression
        Expression expectedExpression = ff.literal(Integer.valueOf(3));
        testObj.setValue(expectedExpression);
        assertEquals(testObj.getExpression().toString(), "3");

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
                                NumberValues.class,
                                FieldIdEnum.INITIAL_GAP,
                                "label",
                                true,
                                false,
                                false));
        assertEquals(FieldConfigInteger.class, field.getClass());

        // Increase code coverage
        TestNumberValues testObj2 = new TestNumberValues();
        testObj2.populateSymbolType(null);
    }
}
