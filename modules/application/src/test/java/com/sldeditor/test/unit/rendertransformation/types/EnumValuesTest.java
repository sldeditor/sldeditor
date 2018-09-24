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
import com.sldeditor.rendertransformation.types.EnumValues;
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
 * Unit test for EnumValues class.
 *
 * <p>{@link com.sldeditor.rendertransformation.test.EnumValues}
 *
 * @author Robert Ward (SCISYS)
 */
class EnumValuesTest {

    private FilterFactory ff = CommonFactoryFinder.getFilterFactory();

    class TestEnumValues extends EnumValues {

        /** @param classType */
        public TestEnumValues(Class<?> classType) {
            super(classType);
        }

        /* (non-Javadoc)
         * @see com.sldeditor.rendertransformation.types.EnumValues#populateSymbolType(com.sldeditor.ui.detail.config.symboltype.SymbolTypeConfig)
         */
        @Override
        protected void populateSymbolType(SymbolTypeConfig symbolTypeConfig) {
            super.populateSymbolType(symbolTypeConfig);
        }
    }

    /** Test method for {@link com.sldeditor.rendertransformation.types.EnumValues#EnumValues()}. */
    @Test
    void testEnumValues() {
        EnumValues testObj = new EnumValues(String.class);
        testObj.createInstance();

        assertEquals(Arrays.asList(String.class), testObj.getType());

        testObj.populate(null);
        testObj.populate(Arrays.asList("E1", "E2", "E3"));

        String defaultValue = "test string";
        testObj.setDefaultValue(defaultValue);
        assertEquals(testObj.getExpression().toString(), defaultValue);

        // String value
        String testValue = "test string2";
        testObj.setValue(testValue);
        assertEquals(testObj.getExpression().toString(), testValue);

        // Other value
        testObj.setValue(Boolean.TRUE);
        assertNull(testObj.getExpression());

        // Literal expression
        Expression expectedExpression = ff.literal(testValue);
        testObj.setValue(expectedExpression);
        assertEquals(testObj.getExpression().toString(), testValue);

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
                                EnumValues.class,
                                FieldIdEnum.INITIAL_GAP,
                                "label",
                                true,
                                false,
                                false));
        assertEquals(FieldConfigEnum.class, field.getClass());

        // Increase code coverage
        @SuppressWarnings("unused")
        EnumValues testObj3 = new EnumValues(testObj);

        TestEnumValues testObj2 = new TestEnumValues(String.class);
        testObj2.populateSymbolType(null);
        SymbolTypeConfig config = new SymbolTypeConfig(String.class);
        testObj2.populateSymbolType(config);
        assertTrue(config.getKeyOrderList().size() > 0);
    }
}
