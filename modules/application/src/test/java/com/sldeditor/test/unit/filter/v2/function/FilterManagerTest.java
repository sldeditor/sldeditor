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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.measure.unit.Unit;

import org.geotools.filter.FunctionExpression;
import org.geotools.filter.function.Classifier;
import org.geotools.filter.function.DefaultFunctionFactory;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opengis.filter.Filter;
import org.opengis.filter.capability.FunctionName;
import org.opengis.filter.expression.Expression;
import org.opengis.parameter.Parameter;

import com.sldeditor.filter.v2.function.FilterConfigInterface;
import com.sldeditor.filter.v2.function.FilterManager;
import com.sldeditor.filter.v2.function.FilterName;
import com.sldeditor.ui.detail.config.FieldConfigBase;
import com.sldeditor.ui.detail.config.FieldConfigBoolean;
import com.sldeditor.ui.detail.config.FieldConfigDate;
import com.sldeditor.ui.detail.config.FieldConfigDouble;
import com.sldeditor.ui.detail.config.FieldConfigGeometry;
import com.sldeditor.ui.detail.config.FieldConfigInteger;
import com.sldeditor.ui.detail.config.FieldConfigMapUnits;
import com.sldeditor.ui.detail.config.FieldConfigString;
import com.sldeditor.ui.detail.config.FieldConfigPopulate;
import com.sldeditor.ui.detail.config.base.GroupConfig;
import com.sldeditor.ui.detail.config.base.GroupConfigInterface;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;

/**
 * Unit test for FilterManager class.
 * <p>{@link com.sldeditor.filter.v2.function.FilterManager}
 * 
 * @author Robert Ward (SCISYS)
 *
 */
public class FilterManagerTest {

    /** The type map. */
    private static Map<Class<?>, Class<?> > typeMap = new HashMap<Class<?>, Class<?>>();

    /**
     * Sets the up class.
     */
    @BeforeClass
    public static void setUpClass() {
        typeMap.put(Number.class, FieldConfigDouble.class);
        typeMap.put(Double.class, FieldConfigDouble.class);
        typeMap.put(Float.class, FieldConfigDouble.class);
        typeMap.put(Integer.class, FieldConfigInteger.class);
        typeMap.put(Long.class, FieldConfigInteger.class);
        typeMap.put(String.class, FieldConfigString.class);
        typeMap.put(Object.class, FieldConfigString.class);
        typeMap.put(Boolean.class, FieldConfigBoolean.class);
        typeMap.put(Geometry.class, FieldConfigGeometry.class);
        typeMap.put(org.opengis.geometry.Geometry.class, FieldConfigGeometry.class);
        typeMap.put(LineString.class, FieldConfigGeometry.class);
        typeMap.put(Date.class, FieldConfigDate.class);
        typeMap.put(Class.class, FieldConfigString.class);
        typeMap.put(Classifier.class, FieldConfigString.class);
        typeMap.put(Unit.class, FieldConfigMapUnits.class);
        typeMap.put(Comparable.class, FieldConfigString.class);
    }

    /**
     * Test method for {@link com.sldeditor.filter.v2.function.FilterManager#getFilterConfigList()}.
     */
    @Test
    public void testGetInstance() {
        List<FilterConfigInterface> filterConfigList = FilterManager.getInstance().getFilterConfigList();

        assertEquals(29, filterConfigList.size());
    }

    /**
     * Test method for {@link com.sldeditor.filter.v2.function.FilterManager#createExpression(org.opengis.filter.capability.FunctionName)}.
     */
    @Test
    public void testCreateExpression() {
        DefaultFunctionFactory functionFactory = new DefaultFunctionFactory();
        List<FunctionName> functionNameList = functionFactory.getFunctionNames();
        FunctionName functionName = null;

        Expression expression = FilterManager.getInstance().createExpression(functionName);
        assertNull(expression);

        functionName = functionNameList.get(0);
        expression = FilterManager.getInstance().createExpression(functionName);
        assertNotNull(expression);
        FunctionExpression funcExpression = (FunctionExpression)expression;
        assertTrue(functionName.getName().compareTo(funcExpression.getName()) == 0);
    }

    /**
     * Test method for {@link com.sldeditor.filter.v2.function.FilterManager#convertParameters(java.lang.Class, com.sldeditor.ui.detail.config.FieldId, org.opengis.filter.capability.FunctionName)}.
     */
    @Test
    public void testConvertParameters() {
        DefaultFunctionFactory functionFactory = new DefaultFunctionFactory();
        List<FunctionName> functionNameList = functionFactory.getFunctionNames();

        // Try with empty parameters
        Class<?> panelId = null;
        List<GroupConfigInterface> list = FilterManager.getInstance().convertParameters(panelId, null);
        assertTrue(list.isEmpty());

        // Find a known function
        for(FunctionName functionName : functionNameList)
        {
            list = FilterManager.getInstance().convertParameters(panelId, functionName);
            System.out.println(functionName.getName());
            assertEquals(1, list.size());
            GroupConfig groupConfig = (GroupConfig) list.get(0);
            String prototype = groupConfig.getLabel();
            List<FieldConfigBase> fieldList = groupConfig.getFieldConfigList();

            assertEquals(functionName.getName(), fieldList.size(), Math.abs(functionName.getArgumentCount()));

            List<String> argList = new ArrayList<String>();
            for(int fieldIndex = 0; fieldIndex < fieldList.size(); fieldIndex ++)
            {
                checkFieldType(fieldIndex, fieldList, functionName, argList);
            }

            StringBuilder sb = new StringBuilder();
            sb.append(functionName.getName());
            sb.append("(");

            for(int index = 0; index < argList.size(); index ++)
            {
                if(index > 0)
                {
                    sb.append(", ");
                }
                sb.append(argList.get(index));
            }
            sb.append(")");

            assertNotNull(functionName.getName(), prototype);
            assertNotNull(functionName.getName(), sb.toString());
            assertTrue(prototype.compareTo(sb.toString()) == 0);
        }
    }

    /**
     * Check field type.
     *
     * @param fieldIndex the field index
     * @param fieldList the field list
     * @param functionName the function name
     * @param argList the arg list
     */
    private void checkFieldType(int fieldIndex, List<FieldConfigBase> fieldList, FunctionName functionName, List<String> argList)
    {
        int adjustedIndex = (fieldIndex >= functionName.getArgumentNames().size()) ? functionName.getArgumentNames().size() - 1 : fieldIndex;
        String label = functionName.getArgumentNames().get(adjustedIndex);

        String debugMessage = String.format("%s/%d %s", functionName.getName(), fieldIndex, label);
        FieldConfigPopulate field = fieldList.get(fieldIndex);

        assertNotNull(debugMessage, field);
        Parameter<?> parameterType = functionName.getArguments().get(adjustedIndex);

        Class<?> actual = typeMap.get(parameterType.getType());
        assertEquals(debugMessage, field.getClass(), actual);
        assertTrue(debugMessage, field.getLabel().compareTo(label) == 0);
        argList.add(parameterType.getType().getSimpleName());
    }

    /**
     * Test method for {@link com.sldeditor.filter.v2.function.FilterManager#getFunctionType(java.lang.String)}.
     * Test method for {@link com.sldeditor.filter.v2.function.FilterManager#getFilterConfig(java.lang.String)}.
     */
    @Test
    public void testGetFunctionType() {
        List<FilterConfigInterface> filterConfigList = FilterManager.getInstance().getFilterConfigList();

        Class<?> returnType = FilterManager.getInstance().getFunctionType(null);
        assertNull(returnType);

        // Find a known function
        for(FilterConfigInterface filterConfig : filterConfigList)
        {
            FilterName filterName = filterConfig.getFilterConfiguration();
            returnType = FilterManager.getInstance().getFunctionType(filterName.getFilterName());
            assertEquals(filterName.getFilterName(), filterName.getReturnType(), returnType);

            FilterConfigInterface actualFilterConfig = FilterManager.getInstance().getFilterConfig(filterName.getFilterName());

            assertEquals(filterConfig.getClass(), actualFilterConfig.getClass());
        }
    }

    /**
     * Test method for {@link com.sldeditor.filter.v2.function.FilterManager#getFilterConfig(org.opengis.filter.Filter)}.
     */
    @Test
    public void testGetFilterConfigFilter() {
        assertNull(FilterManager.getInstance().getFilterConfig((Filter)null));

        List<FilterConfigInterface> filterConfigList = FilterManager.getInstance().getFilterConfigList();
        for(FilterConfigInterface filterConfig : filterConfigList)
        {
            Filter filter = filterConfig.createFilter();
            FilterConfigInterface actualFilterConfig = FilterManager.getInstance().getFilterConfig(filter);

            assertEquals(filterConfig.getClass(), actualFilterConfig.getClass());
        }
    }

}
