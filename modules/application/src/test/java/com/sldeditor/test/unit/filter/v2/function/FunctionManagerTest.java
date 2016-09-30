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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.geotools.filter.FunctionExpression;
import org.geotools.filter.function.DefaultFunctionFactory;
import org.junit.Test;
import org.opengis.filter.capability.FunctionName;
import org.opengis.filter.expression.Expression;

import com.sldeditor.filter.v2.function.FunctionManager;
import com.sldeditor.filter.v2.function.namefilter.FunctionNameFilterAll;
import com.sldeditor.filter.v2.function.namefilter.FunctionNameFilterInterface;
import com.sldeditor.filter.v2.function.namefilter.FunctionNameFilterRaster;

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
        List<FunctionNameFilterInterface> functionNameFilterList = new ArrayList<FunctionNameFilterInterface>();
        functionNameFilterList.add(new FunctionNameFilterAll());

        List<FunctionName> actualList = FunctionManager.getInstance().getFunctionNameList(Number.class, functionNameFilterList);

        assertEquals(count, actualList.size());

        // Try with null
        actualList = FunctionManager.getInstance().getFunctionNameList(null, null);
        assertEquals(functionNameList.size(), actualList.size());

        // Try with non-matching class
        actualList = FunctionManager.getInstance().getFunctionNameList(FunctionManagerTest.class, functionNameFilterList);
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

        List<FunctionNameFilterInterface> functionNameFilterList = new ArrayList<FunctionNameFilterInterface>();
        FunctionNameFilterAll allFilter = new FunctionNameFilterAll();
        allFilter.accept(null);
        functionNameFilterList.add(allFilter);

        List<FunctionName> functionNameList = FunctionManager.getInstance().getFunctionNameList(null, null);
        for(FunctionName functionName : functionNameList)
        {
            returnType = FunctionManager.getInstance().getFunctionType(functionName.getName());
            assertEquals(functionName.getName(), functionName.getReturn().getType(), returnType);
        }
    }

    /**
     * Test method for {@link com.sldeditor.filter.v2.function.FunctionManager#getFunctionNameList(java.lang.Class)}.
     * Testing FunctionNameFilterRaster
     */
    @Test
    public void testGetFunctionNameList2() {
        Class<?> returnType = FunctionManager.getInstance().getFunctionType(null);
        assertNull(returnType);

        List<FunctionName> functionNameList = FunctionManager.getInstance().getFunctionNameList(Object.class, null);
        FunctionName propertyFunction = null;
        FunctionName idFunction = null;
        FunctionName areaFunction = null;
        FunctionName ceilFunction = null;

        for(FunctionName functionName : functionNameList)
        {
            if(functionName.getName().compareTo("property") == 0)
            {
                propertyFunction = functionName;
            }
            else if(functionName.getName().compareTo("id") == 0)
            {
                idFunction = functionName;
            }
            else if(functionName.getName().compareTo("Area") == 0)
            {
                areaFunction = functionName;
            }
            else if(functionName.getName().compareTo("ceil") == 0)
            {
                ceilFunction = functionName;
            }
        }

        assertNotNull(propertyFunction);
        assertNotNull(areaFunction);
        assertNotNull(ceilFunction);
        assertNotNull(idFunction);

        List<FunctionNameFilterInterface> functionNameFilterList = new ArrayList<FunctionNameFilterInterface>();
        FunctionNameFilterRaster rasterFilter = new FunctionNameFilterRaster();
        rasterFilter.accept(null);
        functionNameFilterList.add(rasterFilter);

        List<FunctionName> rasterFunctionNameList = FunctionManager.getInstance().getFunctionNameList(Object.class, functionNameFilterList);

        assertFalse(rasterFunctionNameList.contains(propertyFunction));
        assertFalse(rasterFunctionNameList.contains(areaFunction));
        assertFalse(rasterFunctionNameList.contains(idFunction));
        assertTrue(rasterFunctionNameList.contains(ceilFunction));
    }
}
