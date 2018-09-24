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
import com.sldeditor.rendertransformation.types.JAIToolsRangeValues;
import com.sldeditor.ui.detail.config.FieldConfigBase;
import com.sldeditor.ui.detail.config.FieldConfigCommonData;
import com.sldeditor.ui.detail.config.FieldConfigRange;
import com.sldeditor.ui.detail.config.symboltype.SymbolTypeConfig;
import java.util.Arrays;
import org.geotools.factory.CommonFactoryFinder;
import org.jaitools.numeric.Range;
import org.junit.jupiter.api.Test;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;

/**
 * Unit test for JAIToolsRangeValues class.
 *
 * <p>{@link com.sldeditor.rendertransformation.test.JAIToolsRangeValues}
 *
 * @author Robert Ward (SCISYS)
 */
class JAIToolsRangeValuesTest {

    private FilterFactory ff = CommonFactoryFinder.getFilterFactory();

    class TestJAIToolsRangeValues extends JAIToolsRangeValues {

        /* (non-Javadoc)
         * @see com.sldeditor.rendertransformation.types.JAIToolsRangeValues#populateSymbolType(com.sldeditor.ui.detail.config.symboltype.SymbolTypeConfig)
         */
        @Override
        protected void populateSymbolType(SymbolTypeConfig symbolTypeConfig) {
            super.populateSymbolType(symbolTypeConfig);
        }
    }

    /**
     * Test method for {@link
     * com.sldeditor.rendertransformation.types.JAIToolsRangeValues#JAIToolsRangeValues()}.
     */
    @Test
    void testJAIToolsRangeValues() {
        JAIToolsRangeValues testObj = new JAIToolsRangeValues();
        testObj.createInstance();

        assertEquals(Arrays.asList(Range.class), testObj.getType());

        @SuppressWarnings("rawtypes")
        Range range = Range.create(0.0, true, 50.0, true);
        testObj.setDefaultValue(range);
        assertNull(testObj.getExpression());

        // Range value
        testObj.setValue(range);
        assertNull(testObj.getExpression());

        // Literal expression
        Expression expectedExpression = ff.literal(range);
        testObj.setValue(expectedExpression);
        assertEquals(expectedExpression, testObj.getExpression());

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
                                JAIToolsRangeValues.class,
                                FieldIdEnum.INITIAL_GAP,
                                "label",
                                true,
                                false,
                                false));
        assertEquals(FieldConfigRange.class, field.getClass());

        // Increase code coverage
        TestJAIToolsRangeValues testObj2 = new TestJAIToolsRangeValues();
        testObj2.populateSymbolType(null);
    }
}
