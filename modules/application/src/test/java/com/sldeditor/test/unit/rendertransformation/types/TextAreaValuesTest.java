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
import com.sldeditor.filter.v2.expression.StringList;
import com.sldeditor.rendertransformation.types.TextAreaValues;
import com.sldeditor.ui.detail.config.FieldConfigBase;
import com.sldeditor.ui.detail.config.FieldConfigCommonData;
import com.sldeditor.ui.detail.config.FieldConfigTextArea;
import com.sldeditor.ui.detail.config.symboltype.SymbolTypeConfig;
import java.util.Arrays;
import org.geotools.factory.CommonFactoryFinder;
import org.junit.jupiter.api.Test;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;

/**
 * Unit test for TextAreaValues class.
 *
 * <p>{@link com.sldeditor.rendertransformation.test.TextAreaValues}
 *
 * @author Robert Ward (SCISYS)
 */
class TextAreaValuesTest {

    private FilterFactory ff = CommonFactoryFinder.getFilterFactory();

    class TestTextAreaValues extends TextAreaValues {

        /* (non-Javadoc)
         * @see com.sldeditor.rendertransformation.types.TextAreaValues#populateSymbolType(com.sldeditor.ui.detail.config.symboltype.SymbolTypeConfig)
         */
        @Override
        protected void populateSymbolType(SymbolTypeConfig symbolTypeConfig) {
            super.populateSymbolType(symbolTypeConfig);
        }
    }

    /**
     * Test method for {@link
     * com.sldeditor.rendertransformation.types.TextAreaValues#TextAreaValues()}.
     */
    @Test
    void testTextAreaValues() {
        TextAreaValues testObj = new TextAreaValues();
        testObj.createInstance();

        assertEquals(Arrays.asList(StringList.class), testObj.getType());

        String defaultValue = "test string";
        testObj.setDefaultValue(defaultValue);
        assertEquals(testObj.getExpression().toString(), defaultValue);

        // String value
        String testValue = "test string2";
        testObj.setValue(testValue);
        assertEquals(testObj.getExpression().toString(), testValue);

        // Other value
        testObj.setValue(Boolean.TRUE);
        assertEquals(testObj.getExpression().toString().toLowerCase(), "true");

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
                                TextAreaValues.class,
                                FieldIdEnum.INITIAL_GAP,
                                "label",
                                true,
                                false,
                                false));
        assertEquals(FieldConfigTextArea.class, field.getClass());

        // Increase code coverage
        TestTextAreaValues testObj2 = new TestTextAreaValues();
        testObj2.populateSymbolType(null);
    }
}
