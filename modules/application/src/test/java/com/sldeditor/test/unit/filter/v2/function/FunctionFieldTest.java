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

package com.sldeditor.test.unit.filter.v2.function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sldeditor.filter.v2.expression.ExpressionNode;
import com.sldeditor.filter.v2.function.FunctionField;
import com.sldeditor.filter.v2.function.FunctionManager;
import com.sldeditor.filter.v2.function.FunctionNameInterface;
import com.sldeditor.ui.attribute.SubPanelUpdatedInterface;
import java.util.List;
import org.geotools.factory.CommonFactoryFinder;
import org.junit.jupiter.api.Test;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;

/**
 * Unit test for FunctionField
 *
 * @author Robert Ward (SCISYS)
 */
class FunctionFieldTest {

    class TestFunctionField extends FunctionField {

        /** The Constant serialVersionUID. */
        private static final long serialVersionUID = 1L;

        /**
         * Instantiates a new test function field.
         *
         * @param parentObj the parent obj
         * @param functionNameMgr the function name mgr
         */
        public TestFunctionField(
                SubPanelUpdatedInterface parentObj, FunctionNameInterface functionNameMgr) {
            super(parentObj, functionNameMgr);
        }

        /*
         * (non-Javadoc)
         *
         * @see com.sldeditor.filter.v2.function.FunctionField#getFunctionList()
         */
        @Override
        protected List<String> getFunctionList() {
            return super.getFunctionList();
        }

        /*
         * (non-Javadoc)
         *
         * @see com.sldeditor.filter.v2.function.FunctionField#addButtonPressed(com.sldeditor.ui.
         * attribute.SubPanelUpdatedInterface)
         */
        @Override
        protected void addButtonPressed(SubPanelUpdatedInterface parentObj) {
            super.addButtonPressed(parentObj);
        }

        /*
         * (non-Javadoc)
         *
         * @see com.sldeditor.filter.v2.function.FunctionField#setSelectedFunction(java.lang.String)
         */
        @Override
        protected void setSelectedFunction(String selectedFunction) {
            super.setSelectedFunction(selectedFunction);
        }
    }

    /**
     * Test method for {@link
     * com.sldeditor.filter.v2.function.FunctionField#FunctionField(com.sldeditor.ui.attribute.SubPanelUpdatedInterface,
     * com.sldeditor.filter.v2.function.FunctionNameInterface)}.
     */
    @Test
    void testFunctionField() {
        assertEquals("FunctionField", FunctionField.getPanelName());

        TestFunctionField obj =
                new TestFunctionField(
                        new SubPanelUpdatedInterface() {

                            @Override
                            public void updateSymbol() {}

                            @Override
                            public void parameterAdded() {}
                        },
                        FunctionManager.getInstance());

        List<String> functionList = obj.getFunctionList();
        assertTrue(functionList.isEmpty());
        assertNull(obj.getSelectedItem());

        obj.setPanelEnabled(true);
        obj.setDataType(String.class);
        functionList = obj.getFunctionList();

        Expression expression = obj.getExpression();
        assertNull(expression);
        assertTrue(!functionList.isEmpty());

        // FunctionExpression
        String selectedFunction1 = "Collection_Unique";
        obj.setSelectedFunction(selectedFunction1);
        assertEquals(selectedFunction1, obj.getSelectedItem());
        Expression uniqueExpression = obj.getExpression();
        System.out.println(uniqueExpression.toString());
        obj.addButtonPressed(null);

        // ConcatenateFunction
        String selectedFunction2 = "Concatenate";
        obj.setSelectedFunction(selectedFunction2);
        assertEquals(selectedFunction2, obj.getSelectedItem());
        Expression concatenateExpression = obj.getExpression();
        obj.addButtonPressed(null);
        System.out.println(concatenateExpression.toString());

        // Function
        String selectedFunction3 = "Interpolate";
        obj.setSelectedFunction(selectedFunction3);
        assertEquals(selectedFunction3, obj.getSelectedItem());
        Expression interpolateExpression = obj.getExpression();
        System.out.println(interpolateExpression.toString());
        obj.addButtonPressed(null);

        // Use setFunction

        // FunctionExpression
        obj.setFunction(uniqueExpression, new ExpressionNode());
        assertEquals(selectedFunction1, obj.getSelectedItem());
        expression = obj.getExpression();
        System.out.println(expression.toString());
        obj.addButtonPressed(null);

        // ConcatenateFunction
        obj.setFunction(concatenateExpression, new ExpressionNode());
        assertEquals(selectedFunction2, obj.getSelectedItem());
        expression = obj.getExpression();
        obj.addButtonPressed(null);
        System.out.println(expression.toString());

        // Function
        obj.setFunction(interpolateExpression, new ExpressionNode());
        assertEquals(selectedFunction3, obj.getSelectedItem());
        expression = obj.getExpression();
        System.out.println(expression.toString());
        obj.addButtonPressed(null);

        // Raster
        String selectedFunction4 = "strConcat";

        obj.setIsRasterSymbol(true);
        obj.setDataType(String.class);
        functionList = obj.getFunctionList();
        obj.setSelectedFunction(selectedFunction4);
        assertEquals(selectedFunction4, obj.getSelectedItem());
        Expression rasterExpression = obj.getExpression();
        System.out.println(rasterExpression.toString());
        obj.addButtonPressed(null);

        // Function
        obj.setFunction(rasterExpression, new ExpressionNode());
        assertEquals(selectedFunction4, obj.getSelectedItem());
        expression = obj.getExpression();
        System.out.println(expression.toString());
        obj.addButtonPressed(null);

        // Try a different expression
        FilterFactory ff = CommonFactoryFinder.getFilterFactory();
        obj.setFunction(ff.literal(0), new ExpressionNode());
        obj.setFunction(ff.property("test"), new ExpressionNode());
    }
}
