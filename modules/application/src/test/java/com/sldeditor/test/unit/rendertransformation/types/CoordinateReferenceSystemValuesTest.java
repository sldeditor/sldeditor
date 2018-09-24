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
import com.sldeditor.rendertransformation.types.CoordinateReferenceSystemValues;
import com.sldeditor.ui.detail.config.FieldConfigBase;
import com.sldeditor.ui.detail.config.FieldConfigCommonData;
import com.sldeditor.ui.detail.config.FieldConfigEnum;
import com.sldeditor.ui.detail.config.symboltype.SymbolTypeConfig;
import java.util.Arrays;
import org.geotools.factory.CommonFactoryFinder;
import org.junit.jupiter.api.Test;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;

/**
 * Unit test for CoordinateReferenceSystemValues class.
 *
 * <p>{@link com.sldeditor.rendertransformation.test.CoordinateReferenceSystemValues}
 *
 * @author Robert Ward (SCISYS)
 */
class CoordinateReferenceSystemValuesTest {

    private FilterFactory ff = CommonFactoryFinder.getFilterFactory();

    class TestCoordinateReferenceSystemValues extends CoordinateReferenceSystemValues {

        /*
         * (non-Javadoc)
         *
         * @see com.sldeditor.rendertransformation.types.CoordinateReferenceSystemValues#
         * populateSymbolType(com.sldeditor.ui.detail.config.symboltype.SymbolTypeConfig)
         */
        @Override
        protected void populateSymbolType(SymbolTypeConfig symbolTypeConfig) {
            super.populateSymbolType(symbolTypeConfig);
        }
    }

    /**
     * Test method for {@link
     * com.sldeditor.rendertransformation.types.CoordinateReferenceSystemValues#CoordinateReferenceSystemValues()}.
     */
    @Test
    void testCoordinateReferenceSystemValues() {
        CoordinateReferenceSystemValues testObj = new CoordinateReferenceSystemValues();
        testObj.createInstance();

        assertEquals(
                Arrays.asList(org.opengis.referencing.crs.CoordinateReferenceSystem.class),
                testObj.getType());

        String expectedString = "test string";
        testObj.setDefaultValue(expectedString);
        assertEquals(testObj.getExpression().toString(), expectedString);

        // String value
        testObj.setValue(expectedString);
        assertEquals(testObj.getExpression().toString(), expectedString);

        // Literal expression
        Expression expectedExpression = ff.literal(expectedString);
        testObj.setValue(expectedExpression);
        assertEquals(testObj.getExpression().toString(), expectedString);

        // Attribute expression
        expectedExpression = ff.property("test");
        testObj.setValue(expectedExpression);
        assertEquals(expectedExpression, testObj.getExpression());

        // Not set
        testObj.setValue(Boolean.TRUE);
        assertNull(testObj.getExpression());

        testObj.setValue(ff.literal(null));
        assertNull(testObj.getExpression());

        FieldConfigBase field =
                testObj.getField(
                        new FieldConfigCommonData(
                                CoordinateReferenceSystemValues.class,
                                FieldIdEnum.INITIAL_GAP,
                                "label",
                                true,
                                false,
                                false));
        assertEquals(FieldConfigEnum.class, field.getClass());

        // Increase code coverage
        TestCoordinateReferenceSystemValues testObj2 = new TestCoordinateReferenceSystemValues();
        testObj2.populateSymbolType(null);

        SymbolTypeConfig symbolTypeConfig = new SymbolTypeConfig(SymbolTypeConfig.class);
        testObj2.populateSymbolType(symbolTypeConfig);
        assertTrue(symbolTypeConfig.getKeyOrderList().size() > 0);
    }
}
