/*
 * SLD Editor - The Open Source Java SLD Editor
 *
 * Copyright (C) 2016, SCISYS UK Limited
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.geotools.filter.FunctionExpression;
import org.geotools.filter.function.DefaultFunctionFactory;
import org.junit.Test;
import org.opengis.filter.capability.FunctionName;
import org.opengis.filter.expression.Expression;

import com.sldeditor.filter.v2.function.FunctionManager;

/**
 * Unit test for FunctionManager class.
 * <p>{@link com.sldeditor.filter.v2.function.FunctionManager}
 * 
 * @author Robert Ward (SCISYS)
 *
 */
public class FunctionManagerTest {

    /**
     * Test method for {@link com.sldeditor.filter.v2.function.FunctionManager#getFunctionNameList(java.lang.Class)}.
     */
    @Test
    public void testGetFunctionNameList() {
        DefaultFunctionFactory functionFactory = new DefaultFunctionFactory();
        List<FunctionName> functionNameList = functionFactory.getFunctionNames();

        Class<?>[] allowedNumberTypes = {Number.class, Double.class, Float.class, Integer.class, Long.class, Object.class};
        List<Class<?>> allowedNumberTypesList = Arrays.asList(allowedNumberTypes);

        int count = 0;
        for(FunctionName functionName : functionNameList)
        {
            Class<?> type = functionName.getReturn().getType();
            if(allowedNumberTypesList.contains(type))
            {
                count ++;
            }
        }
        List<FunctionName> actualList = FunctionManager.getInstance().getFunctionNameList(Number.class);

        assertEquals(count, actualList.size());

        // Try with null
        actualList = FunctionManager.getInstance().getFunctionNameList(null);
        assertEquals(functionNameList.size(), actualList.size());

        // Try with non-matching class
        actualList = FunctionManager.getInstance().getFunctionNameList(FunctionManagerTest.class);
        assertTrue(actualList.isEmpty());
    }

    /**
     * Test method for {@link com.sldeditor.filter.v2.function.FunctionManager#createExpression(org.opengis.filter.capability.FunctionName)}.
     */
    @Test
    public void testCreateExpression() {
        DefaultFunctionFactory functionFactory = new DefaultFunctionFactory();
        List<FunctionName> functionNameList = functionFactory.getFunctionNames();
        FunctionName functionName = null;
        Expression expression = FunctionManager.getInstance().createExpression(functionName);
        assertNull(expression);

        functionName = functionNameList.get(0);
        expression = FunctionManager.getInstance().createExpression(functionName);
        assertNotNull(expression);
        FunctionExpression funcExpression = (FunctionExpression)expression;
        assertTrue(functionName.getName().compareTo(funcExpression.getName()) == 0);
    }

    /**
     * Test method for {@link com.sldeditor.filter.v2.function.FunctionManager#getFunctionType(java.lang.String)}.
     */
    @Test
    public void testGetFunctionType() {
        Class<?> returnType = FunctionManager.getInstance().getFunctionType(null);
        assertNull(returnType);

        List<FunctionName> functionNameList = FunctionManager.getInstance().getFunctionNameList(null);
        for(FunctionName functionName : functionNameList)
        {
            returnType = FunctionManager.getInstance().getFunctionType(functionName.getName());
            assertEquals(functionName.getName(), functionName.getReturn().getType(), returnType);
        }
    }
}
