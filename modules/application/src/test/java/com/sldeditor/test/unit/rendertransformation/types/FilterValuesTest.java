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
import com.sldeditor.rendertransformation.types.FilterValues;
import com.sldeditor.ui.detail.config.FieldConfigBase;
import com.sldeditor.ui.detail.config.FieldConfigCommonData;
import com.sldeditor.ui.detail.config.FieldConfigString;
import com.sldeditor.ui.detail.config.symboltype.SymbolTypeConfig;
import java.util.Arrays;
import org.geotools.factory.CommonFactoryFinder;
import org.junit.jupiter.api.Test;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;

/**
 * Unit test for FilterValues class.
 *
 * <p>{@link com.sldeditor.rendertransformation.test.FilterValues}
 *
 * @author Robert Ward (SCISYS)
 */
class FilterValuesTest {

    private FilterFactory ff = CommonFactoryFinder.getFilterFactory();

    class TestFilterValues extends FilterValues {

        /* (non-Javadoc)
         * @see com.sldeditor.rendertransformation.types.FilterValues#populateSymbolType(com.sldeditor.ui.detail.config.symboltype.SymbolTypeConfig)
         */
        @Override
        protected void populateSymbolType(SymbolTypeConfig symbolTypeConfig) {
            super.populateSymbolType(symbolTypeConfig);
        }
    }

    /**
     * Test method for {@link com.sldeditor.rendertransformation.types.FilterValues#FilterValues()}.
     */
    @Test
    void testFilterValues() {
        FilterValues testObj = new FilterValues();
        testObj.createInstance();

        assertEquals(Arrays.asList(Filter.class), testObj.getType());

        Filter filter = ff.equals(ff.literal(1), ff.literal(2));

        testObj.setDefaultValue(filter);
        assertNull(testObj.getExpression());

        // Filter value
        testObj.setValue(filter);
        assertNull(testObj.getExpression());

        // Literal expression
        Expression expectedExpression = ff.literal(filter);
        testObj.setValue(expectedExpression);
        assertEquals(testObj.getExpression(), expectedExpression);

        // Attribute expression
        expectedExpression = ff.property("test");
        testObj.setValue(expectedExpression);
        assertEquals(expectedExpression, testObj.getExpression());

        // Not set
        testObj.setValue(null);
        assertNull(testObj.getExpression());

        FieldConfigBase field =
                testObj.getField(
                        new FieldConfigCommonData(
                                FilterValues.class,
                                FieldIdEnum.INITIAL_GAP,
                                "label",
                                true,
                                false,
                                false));
        assertEquals(FieldConfigString.class, field.getClass());

        // Increase code coverage
        TestFilterValues testObj2 = new TestFilterValues();
        testObj2.populateSymbolType(null);
    }
}
