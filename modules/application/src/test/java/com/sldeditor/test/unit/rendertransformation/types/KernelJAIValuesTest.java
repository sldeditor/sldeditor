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
import com.sldeditor.rendertransformation.types.KernelJAIValues;
import com.sldeditor.ui.detail.config.FieldConfigBase;
import com.sldeditor.ui.detail.config.FieldConfigCommonData;
import com.sldeditor.ui.detail.config.FieldConfigEnum;
import com.sldeditor.ui.detail.config.symboltype.SymbolTypeConfig;
import java.util.Arrays;
import javax.media.jai.KernelJAI;
import org.geotools.factory.CommonFactoryFinder;
import org.junit.jupiter.api.Test;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;

/**
 * Unit test for KernelJAIValues class.
 *
 * <p>{@link com.sldeditor.rendertransformation.test.KernelJAIValues}
 *
 * @author Robert Ward (SCISYS)
 */
class KernelJAIValuesTest {

    private FilterFactory ff = CommonFactoryFinder.getFilterFactory();

    class TestKernelJAIValues extends KernelJAIValues {

        /* (non-Javadoc)
         * @see com.sldeditor.rendertransformation.types.KernelJAIValues#populateSymbolType(com.sldeditor.ui.detail.config.symboltype.SymbolTypeConfig)
         */
        @Override
        protected void populateSymbolType(SymbolTypeConfig symbolTypeConfig) {
            super.populateSymbolType(symbolTypeConfig);
        }
    }

    /**
     * Test method for {@link
     * com.sldeditor.rendertransformation.types.KernelJAIValues#KernelJAIValues()}.
     */
    @Test
    void testKernelJAIValues() {
        KernelJAIValues testObj = new KernelJAIValues();
        testObj.createInstance();

        assertEquals(Arrays.asList(KernelJAI.class), testObj.getType());

        KernelJAI kernelJAI = KernelJAI.GRADIENT_MASK_SOBEL_HORIZONTAL;
        testObj.setDefaultValue(kernelJAI);
        assertEquals(KernelJAI.class.getSimpleName(), testObj.getExpression().toString());

        // KernelJAI value
        testObj.setValue(kernelJAI);
        assertEquals(KernelJAI.class.getSimpleName(), testObj.getExpression().toString());

        // Literal expression
        Expression expectedExpression = ff.literal(kernelJAI);
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
                                KernelJAIValues.class,
                                FieldIdEnum.INITIAL_GAP,
                                "label",
                                true,
                                false,
                                false));
        assertEquals(FieldConfigEnum.class, field.getClass());

        // Increase code coverage
        TestKernelJAIValues testObj2 = new TestKernelJAIValues();
        testObj2.populateSymbolType(null);

        SymbolTypeConfig config = new SymbolTypeConfig(String.class);
        testObj2.populateSymbolType(config);
        assertTrue(config.getKeyOrderList().size() > 0);
    }
}
