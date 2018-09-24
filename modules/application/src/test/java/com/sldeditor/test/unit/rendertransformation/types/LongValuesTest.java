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
import com.sldeditor.rendertransformation.types.LongValues;
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
 * Unit test for LongValues class.
 *
 * <p>{@link com.sldeditor.rendertransformation.test.LongValues}
 *
 * @author Robert Ward (SCISYS)
 */
class LongValuesTest {

    private FilterFactory ff = CommonFactoryFinder.getFilterFactory();

    class TestLongValues extends LongValues {

        /* (non-Javadoc)
         * @see com.sldeditor.rendertransformation.types.LongValues#populateSymbolType(com.sldeditor.ui.detail.config.symboltype.SymbolTypeConfig)
         */
        @Override
        protected void populateSymbolType(SymbolTypeConfig symbolTypeConfig) {
            super.populateSymbolType(symbolTypeConfig);
        }
    }

    /** Test method for {@link com.sldeditor.rendertransformation.types.LongValues#LongValues()}. */
    @Test
    void testLongValues() {
        LongValues testObj = new LongValues();
        testObj.createInstance();

        assertEquals(Arrays.asList(long.class, Long.class), testObj.getType());

        testObj.setDefaultValue(Long.valueOf("1"));
        assertEquals(testObj.getExpression().toString(), "1");

        // Long value
        testObj.setValue(Long.valueOf("9"));
        assertEquals(testObj.getExpression().toString(), "9");

        // Literal expression
        Expression expectedExpression = ff.literal(Long.valueOf("3"));
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
                                LongValues.class,
                                FieldIdEnum.INITIAL_GAP,
                                "label",
                                true,
                                false,
                                false));
        assertEquals(FieldConfigInteger.class, field.getClass());

        // Increase code coverage
        TestLongValues testObj2 = new TestLongValues();
        testObj2.populateSymbolType(null);
    }
}
