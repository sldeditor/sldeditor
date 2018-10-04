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
import com.sldeditor.filter.v2.function.temporal.TimePeriod;
import com.sldeditor.rendertransformation.types.TimePeriodValues;
import com.sldeditor.ui.detail.config.FieldConfigBase;
import com.sldeditor.ui.detail.config.FieldConfigCommonData;
import com.sldeditor.ui.detail.config.FieldConfigTimePeriod;
import com.sldeditor.ui.detail.config.symboltype.SymbolTypeConfig;
import java.util.Arrays;
import org.geotools.factory.CommonFactoryFinder;
import org.junit.jupiter.api.Test;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;

/**
 * Unit test for TimerPeriodValues class.
 *
 * <p>{@link com.sldeditor.rendertransformation.test.TimerPeriodValues}
 *
 * @author Robert Ward (SCISYS)
 */
class TimePeriodValuesTest {

    private FilterFactory ff = CommonFactoryFinder.getFilterFactory();

    class TestTimePeriodValues extends TimePeriodValues {

        /* (non-Javadoc)
         * @see com.sldeditor.rendertransformation.types.TimePeriodValues#populateSymbolType(com.sldeditor.ui.detail.config.symboltype.SymbolTypeConfig)
         */
        @Override
        protected void populateSymbolType(SymbolTypeConfig symbolTypeConfig) {
            super.populateSymbolType(symbolTypeConfig);
        }
    }

    /**
     * Test method for {@link
     * com.sldeditor.rendertransformation.types.TimePeriodValues#TimePeriodValues()}.
     */
    @Test
    void testTimePeriodValues() {
        TimePeriodValues testObj = new TimePeriodValues();
        testObj.createInstance();

        assertEquals(Arrays.asList(TimePeriod.class), testObj.getType());

        String timePeriodString = "2016-07-07T17:42:27+01:00 / 2016-08-07T17:42:27+01:00";

        TimePeriod expectedTimePeriod = new TimePeriod();
        expectedTimePeriod.decode(timePeriodString);

        testObj.setDefaultValue(expectedTimePeriod);
        assertEquals(testObj.getExpression().toString(), expectedTimePeriod.toString());

        // TimePeriod value
        testObj.setValue(expectedTimePeriod);
        assertEquals(testObj.getExpression().toString(), expectedTimePeriod.toString());

        // Literal expression
        Expression expectedExpression = ff.literal(expectedTimePeriod);
        testObj.setValue(expectedExpression);
        assertEquals(testObj.getExpression().toString(), expectedTimePeriod.toString());

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
                                TimePeriodValues.class,
                                FieldIdEnum.INITIAL_GAP,
                                "label",
                                true,
                                false,
                                false));
        assertEquals(FieldConfigTimePeriod.class, field.getClass());

        // Increase code coverage
        TestTimePeriodValues testObj2 = new TestTimePeriodValues();
        testObj2.populateSymbolType(null);
    }
}
