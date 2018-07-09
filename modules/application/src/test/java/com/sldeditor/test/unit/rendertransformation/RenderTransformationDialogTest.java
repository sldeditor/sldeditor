/*
 * SLD Editor - The Open Source Java SLD Editor
 *
 * Copyright (C) 2017, SCISYS UK Limited
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

package com.sldeditor.test.unit.rendertransformation;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.sldeditor.common.connection.GeoServerConnectionManagerInterface;
import com.sldeditor.rendertransformation.RenderTransformationDialog;
import org.geotools.process.function.ProcessFunction;
import org.geotools.process.function.ProcessFunctionFactory;
import org.junit.Test;
import org.opengis.filter.capability.FunctionName;

/**
 * Unit test for RenderTransformationDialog class.
 *
 * <p>{@link com.sldeditor.rendertransformation.RenderTransformationDialog}
 *
 * @author Robert Ward (SCISYS)
 */
public class RenderTransformationDialogTest {

    class TestRenderTransformationDialog extends RenderTransformationDialog {

        /** The Constant serialVersionUID. */
        private static final long serialVersionUID = 1L;

        /**
         * Instantiates a new test render transformation dialog.
         *
         * @param geoServerConnectionManager the geo server connection manager
         */
        public TestRenderTransformationDialog(
                GeoServerConnectionManagerInterface geoServerConnectionManager) {
            super(geoServerConnectionManager);
        }

        /**
         * Test internal show dialog.
         *
         * @param existingProcessFunction the existing process function
         */
        public void test_internal_showDialog(ProcessFunction existingProcessFunction) {
            internal_showDialog(existingProcessFunction);
        }

        public void testDisplayFunction(String functionName) {
            displayFunction(functionName);
        }
    }

    /**
     * Test method for {@link
     * com.sldeditor.rendertransformation.RenderTransformationDialog#RenderTransformationDialog(com.sldeditor.common.connection.GeoServerConnectionManagerInterface)}.
     */
    @Test
    public void testRenderTransformationDialog() {
        TestRenderTransformationDialog testObj = new TestRenderTransformationDialog(null);

        testObj.test_internal_showDialog(null);
        ProcessFunction actualResult = testObj.getTransformationProcessFunction();
        assertNull(actualResult);

        ProcessFunctionFactory factory = new ProcessFunctionFactory();
        FunctionName functionName = factory.getFunctionNames().get(0);
        testObj.testDisplayFunction(functionName.getName());

        actualResult = testObj.getTransformationProcessFunction();
        assertNotNull(actualResult);
        testObj.test_internal_showDialog(actualResult);
    }
}
