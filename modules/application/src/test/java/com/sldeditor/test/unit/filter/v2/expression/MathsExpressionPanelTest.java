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

package com.sldeditor.test.unit.filter.v2.expression;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.sldeditor.filter.v2.expression.MathsExpressionPanel;
import com.sldeditor.ui.attribute.SubPanelUpdatedInterface;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.expression.AddImpl;
import org.geotools.filter.expression.DivideImpl;
import org.geotools.filter.expression.MultiplyImpl;
import org.geotools.filter.expression.SubtractImpl;
import org.junit.jupiter.api.Test;
import org.opengis.filter.FilterFactory;

/**
 * The unit test for MathsExpressionPanel.
 *
 * @author Robert Ward (SCISYS)
 */
class MathsExpressionPanelTest {

    /**
     * Test method for {@link
     * com.sldeditor.filter.v2.expression.MathsExpressionPanel#MathsExpressionPanel(com.sldeditor.ui.attribute.SubPanelUpdatedInterface)}.
     */
    @Test
    void testMathsExpressionPanel() {
        FilterFactory ff = CommonFactoryFinder.getFilterFactory();

        MathsExpressionPanel testObj =
                new MathsExpressionPanel(
                        new SubPanelUpdatedInterface() {

                            @Override
                            public void updateSymbol() {}

                            @Override
                            public void parameterAdded() {}
                        });

        // Invalid
        testObj.setExpression(null);
        assertEquals(null, testObj.getExpression());

        // Add
        testObj.setExpression(ff.add(null, null));
        assertEquals(AddImpl.class, testObj.getExpression().getClass());
        assertEquals("Add", testObj.getSelectedItem());

        // Subtract
        testObj.setExpression(ff.subtract(null, null));
        assertEquals(SubtractImpl.class, testObj.getExpression().getClass());

        // Multiply
        testObj.setExpression(ff.multiply(null, null));
        assertEquals(MultiplyImpl.class, testObj.getExpression().getClass());

        // Divide
        testObj.setExpression(ff.divide(null, null));
        assertEquals(DivideImpl.class, testObj.getExpression().getClass());

        testObj.setPanelEnabled(true);
    }
}
