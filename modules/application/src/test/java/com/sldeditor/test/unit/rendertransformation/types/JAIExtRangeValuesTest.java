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
import com.sldeditor.rendertransformation.types.JAIExtRangeValues;
import com.sldeditor.ui.detail.config.FieldConfigBase;
import com.sldeditor.ui.detail.config.FieldConfigCommonData;
import com.sldeditor.ui.detail.config.FieldConfigRange;
import com.sldeditor.ui.detail.config.symboltype.SymbolTypeConfig;
import it.geosolutions.jaiext.range.Range;
import it.geosolutions.jaiext.range.RangeFactory;
import java.util.Arrays;
import org.geotools.factory.CommonFactoryFinder;
import org.junit.jupiter.api.Test;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;

/**
 * Unit test for JAIExtRangeValues class.
 *
 * <p>{@link com.sldeditor.rendertransformation.test.JAIExtRangeValues}
 *
 * @author Robert Ward (SCISYS)
 */
class JAIExtRangeValuesTest {

    private FilterFactory ff = CommonFactoryFinder.getFilterFactory();

    class TestJAIExtRangeValues extends JAIExtRangeValues {

        /* (non-Javadoc)
         * @see com.sldeditor.rendertransformation.types.JAIExtRangeValues#populateSymbolType(com.sldeditor.ui.detail.config.symboltype.SymbolTypeConfig)
         */
        @Override
        protected void populateSymbolType(SymbolTypeConfig symbolTypeConfig) {
            super.populateSymbolType(symbolTypeConfig);
        }
    }

    /**
     * Test method for {@link
     * com.sldeditor.rendertransformation.types.JAIExtRangeValues#JAIExtRangeValues()}.
     */
    @Test
    void testJAIExtRangeValues() {
        JAIExtRangeValues testObj = new JAIExtRangeValues();
        testObj.createInstance();

        assertEquals(Arrays.asList(Range.class), testObj.getType());

        Range range = RangeFactory.create(0.0, true, 50.0, true, false);
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
                                JAIExtRangeValues.class,
                                FieldIdEnum.INITIAL_GAP,
                                "label",
                                true,
                                false,
                                false));
        assertEquals(FieldConfigRange.class, field.getClass());

        // Increase code coverage
        TestJAIExtRangeValues testObj2 = new TestJAIExtRangeValues();
        testObj2.populateSymbolType(null);
    }
}
