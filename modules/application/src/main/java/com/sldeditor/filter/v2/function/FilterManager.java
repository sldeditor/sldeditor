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
package com.sldeditor.filter.v2.function;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.geotools.filter.function.Classifier;
import org.geotools.filter.function.DefaultFunctionFactory;
import org.geotools.filter.function.RangedClassifier;
import org.opengis.filter.Filter;
import org.opengis.filter.capability.FunctionName;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.Literal;
import org.opengis.parameter.Parameter;

import com.sldeditor.common.console.ConsoleManager;
import com.sldeditor.filter.v2.function.geometry.BBox;
import com.sldeditor.filter.v2.function.geometry.Contains;
import com.sldeditor.filter.v2.function.geometry.Crosses;
import com.sldeditor.filter.v2.function.geometry.DWithin;
import com.sldeditor.filter.v2.function.geometry.Disjoint;
import com.sldeditor.filter.v2.function.geometry.Equals;
import com.sldeditor.filter.v2.function.geometry.Intersects;
import com.sldeditor.filter.v2.function.geometry.Overlaps;
import com.sldeditor.filter.v2.function.geometry.Touches;
import com.sldeditor.filter.v2.function.geometry.Within;
import com.sldeditor.filter.v2.function.logic.And;
import com.sldeditor.filter.v2.function.logic.Not;
import com.sldeditor.filter.v2.function.logic.Or;
import com.sldeditor.filter.v2.function.misc.IsLike;
import com.sldeditor.filter.v2.function.misc.IsNull;
import com.sldeditor.filter.v2.function.property.IsBetween;
import com.sldeditor.filter.v2.function.property.IsEqualTo;
import com.sldeditor.filter.v2.function.property.IsGreaterThan;
import com.sldeditor.filter.v2.function.property.IsGreaterThanEqualTo;
import com.sldeditor.filter.v2.function.property.IsLessThan;
import com.sldeditor.filter.v2.function.property.IsLessThanEqualTo;
import com.sldeditor.filter.v2.function.property.IsNotEqualTo;
import com.sldeditor.filter.v2.function.temporal.After;
import com.sldeditor.filter.v2.function.temporal.Before;
import com.sldeditor.filter.v2.function.temporal.Beyond;
import com.sldeditor.filter.v2.function.temporal.During;
import com.sldeditor.filter.v2.function.temporal.TContains;
import com.sldeditor.filter.v2.function.temporal.TEquals;
import com.sldeditor.filter.v2.function.temporal.TOverlaps;
import com.sldeditor.ui.detail.config.FieldConfigBase;
import com.sldeditor.ui.detail.config.FieldConfigBoolean;
import com.sldeditor.ui.detail.config.FieldConfigDate;
import com.sldeditor.ui.detail.config.FieldConfigDouble;
import com.sldeditor.ui.detail.config.FieldConfigGeometry;
import com.sldeditor.ui.detail.config.FieldConfigInteger;
import com.sldeditor.ui.detail.config.FieldConfigString;
import com.sldeditor.ui.detail.config.FieldId;
import com.sldeditor.ui.detail.config.base.GroupConfig;
import com.sldeditor.ui.detail.config.base.GroupConfigInterface;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.Point;

/**
 * The Class FilterManager.
 *
 * @author Robert Ward (SCISYS)
 */
public class FilterManager implements FilterNameInterface {

    /** The singleton instance. */
    private static FilterNameInterface instance = null;

    /** The function name list. */
    private List<FilterName> filterNameList = new ArrayList<FilterName>();

    /** The function name map. */
    private Map<String, FilterName> functionNameMap = new HashMap<String, FilterName>();

    /** The filter map. */
    private Map<String, FilterConfigInterface> filterMap = new HashMap<String, FilterConfigInterface>();

    /** The filter type map. */
    private Map<Class<?>, FilterConfigInterface> filterTypeMap = new HashMap<Class<?>, FilterConfigInterface>();

    /** The function factory. */
    private DefaultFunctionFactory functionFactory = new DefaultFunctionFactory();

    /** The allowed type map. */
    private Map<Class<?>, List<Class<?>>> allowedTypeMap = new HashMap<Class<?>, List<Class<?>>>();

    /**
     * Gets the single instance of FilterManager.
     *
     * @return single instance of FilterManager
     */
    public static FilterNameInterface getInstance()
    {
        if(instance == null)
        {
            instance = new FilterManager();
        }

        return instance;
    }

    /**
     * Instantiates a new filter manager.
     */
    public FilterManager()
    {
        initialise();
    }

    /**
     * Initialise.
     */
    private void initialise() {
        List<FilterConfigInterface> filterConfigList = getFilterConfigList();

        filterNameList = new ArrayList<FilterName>();

        for(FilterConfigInterface filterConfig : filterConfigList)
        {
            filterNameList.add(filterConfig.getFilterConfiguration());
            String key = filterConfig.getFilterConfiguration().getFilterName();
            filterMap.put(key, filterConfig);
            filterTypeMap.put(filterConfig.getFilterClass(), filterConfig);
        }

        List<Class<?>> classList = new ArrayList<Class<?>>();

        Logger logger = Logger.getLogger(getClass());

        for(FilterName function : filterNameList)
        {
            logger.debug(function.getFilterName());

            functionNameMap.put(function.getFilterName(), function);

            for(FilterNameParameter parameter : function.getParameterList())
            {
                if(!classList.contains(parameter.getDataType()))
                {
                    classList.add(parameter.getDataType());
                }
                logger.debug("\t" + parameter.getName() + "\t" + parameter.getDataType().getName());
            }

            if(!classList.contains(function.getReturnType()))
            {
                classList.add(function.getReturnType());
            }
            logger.debug("\tRet : " + function.getReturnType().getName());
        }

        logger.debug("\nClasses");
        for(Class<?> className : classList)
        {
            logger.debug(className.getName());
        }

        Class<?>[] allowedNumberTypes = {Number.class, Double.class, Float.class, Integer.class, Long.class};
        Class<?>[] allowedDoubleTypes = {Number.class, Double.class, Float.class, Integer.class, Long.class};
        Class<?>[] allowedFloatTypes = {Number.class, Double.class, Float.class, Integer.class, Long.class};
        Class<?>[] allowedIntegerTypes = {Number.class, Double.class, Float.class, Integer.class, Long.class};
        Class<?>[] allowedLongTypes = {Number.class, Double.class, Float.class, Integer.class, Long.class};
        Class<?>[] allowedBooleanTypes = {Boolean.class};
        Class<?>[] allowedStringTypes = {String.class};
        Class<?>[] allowedGeometryTypes = {Geometry.class, LineString.class, Point.class, MultiPoint.class, LinearRing.class};
        Class<?>[] allowedDateTypes = {Date.class};
        Class<?>[] allowedClassifierTypes = {RangedClassifier.class, Classifier.class};

        populateAllowedTypes(Number.class, allowedNumberTypes);
        populateAllowedTypes(Double.class, allowedDoubleTypes);
        populateAllowedTypes(Float.class, allowedFloatTypes);
        populateAllowedTypes(Integer.class, allowedIntegerTypes);
        populateAllowedTypes(Long.class, allowedLongTypes);
        populateAllowedTypes(Boolean.class, allowedBooleanTypes);
        populateAllowedTypes(String.class, allowedStringTypes);
        populateAllowedTypes(Geometry.class, allowedGeometryTypes);
        populateAllowedTypes(Date.class, allowedDateTypes);
        populateAllowedTypes(Classifier.class, allowedClassifierTypes);
    }

    /**
     * Gets the filter config list.
     *
     * @return the filter config list
     */
    @Override
    public List<FilterConfigInterface> getFilterConfigList() {
        List<FilterConfigInterface> filterConfigList = new ArrayList<FilterConfigInterface>();

        // Logic filters
        filterConfigList.add(new And());
        filterConfigList.add(new Or());
        filterConfigList.add(new Not());

        filterConfigList.add(new IsEqualTo());
        filterConfigList.add(new IsNotEqualTo());
        filterConfigList.add(new IsLessThan());
        filterConfigList.add(new IsLessThanEqualTo());
        filterConfigList.add(new IsGreaterThan());
        filterConfigList.add(new IsGreaterThanEqualTo());

        filterConfigList.add(new IsBetween());
        filterConfigList.add(new IsNull());
        filterConfigList.add(new IsLike());

        // Temporal
        filterConfigList.add(new After());
        filterConfigList.add(new Before());
        filterConfigList.add(new During());
        filterConfigList.add(new TEquals());
        filterConfigList.add(new TOverlaps());
        filterConfigList.add(new TContains());

        // Geometry
        filterConfigList.add(new BBox());
        filterConfigList.add(new Beyond());
        filterConfigList.add(new Contains());
        filterConfigList.add(new Crosses());
        filterConfigList.add(new Disjoint());
        filterConfigList.add(new DWithin());
        filterConfigList.add(new Equals());
        filterConfigList.add(new Intersects());
        filterConfigList.add(new Overlaps());
        filterConfigList.add(new Touches());
        filterConfigList.add(new Within());

        return filterConfigList;
    }

    /**
     * Populate allowed types.
     *
     * @param key the key
     * @param allowedTypeArray the allowed type array
     */
    private void populateAllowedTypes(Class<?> key, Class<?>[] allowedTypeArray)
    {
        allowedTypeMap.put(key, Arrays.asList(allowedTypeArray));
    }

    /**
     * Creates the expression.
     *
     * @param functionName the function name
     * @return the expression
     */
    @Override
    public Expression createExpression(FunctionName functionName) {
        if(functionName == null)
        {
            return null;
        }

        List<Expression> parameters = null;
        Literal fallback = null;
        Function function = functionFactory.function(functionName.getFunctionName(), parameters, fallback);

        return function;
    }

    /**
     * Convert function parameters to ui components.
     *
     * @param panelId the panel id
     * @param fieldId the field id
     * @param functionName the function name
     * @return the list of ui components to display
     */
    @Override
    public List<GroupConfigInterface> convertParameters(Class<?> panelId, FieldId fieldId, FunctionName functionName) {
        List<GroupConfigInterface> groupConfigList = new ArrayList<GroupConfigInterface>();
        GroupConfig groupConfig = new GroupConfig();

        StringBuilder funcPrototypeStringBuilder = new StringBuilder();
        funcPrototypeStringBuilder.append(functionName.getName());
        funcPrototypeStringBuilder.append("(");

        for(int index = 0; index < functionName.getArgumentCount(); index ++)
        {
            String label = functionName.getArgumentNames().get(index);
            Parameter<?> parameterType = functionName.getArguments().get(index);

            boolean multipleValues = (parameterType.getMaxOccurs() > 1);
            boolean valueOnly = false;
            FieldId id = FieldId.getUnknownValue();

            if(index > 0)
            {
                funcPrototypeStringBuilder.append(", ");
            }
            Class<?> type = parameterType.getType();
            funcPrototypeStringBuilder.append(type.getSimpleName());

            FieldConfigBase fieldConfig = null;
            if(type == java.lang.Number.class)
            {
                fieldConfig = new FieldConfigDouble(panelId, id, label, valueOnly, multipleValues);
            }
            else if(type == Double.class)
            {
                fieldConfig = new FieldConfigDouble(panelId, id, label, valueOnly, multipleValues);
            }
            else if(type == Float.class)
            {
                fieldConfig = new FieldConfigDouble(panelId, id, label, valueOnly, multipleValues);
            }
            else if(type == Integer.class)
            {
                fieldConfig = new FieldConfigInteger(panelId, id, label, valueOnly, multipleValues);
            }
            else if(type == Long.class)
            {
                fieldConfig = new FieldConfigInteger(panelId, id, label, valueOnly, multipleValues);
            }
            else if(type == String.class)
            {
                fieldConfig = new FieldConfigString(panelId, id, label, valueOnly, null, multipleValues);
            }
            else if(type == Object.class)
            {
                fieldConfig = new FieldConfigString(panelId, id, label, valueOnly, null, multipleValues);
            }
            else if(type == Boolean.class)
            {
                fieldConfig = new FieldConfigBoolean(panelId, id, label, valueOnly, multipleValues);
            }
            else if(type == Geometry.class)
            {
                fieldConfig = new FieldConfigGeometry(panelId, id, label, valueOnly, null, multipleValues);
            }
            else if(type == LineString.class)
            {
                fieldConfig = new FieldConfigGeometry(panelId, id, label, valueOnly, null, multipleValues);
            }
            else if(type == Date.class)
            {
                fieldConfig = new FieldConfigDate(panelId, id, label, valueOnly, multipleValues);
            }
            else if(type == Class.class)
            {
                fieldConfig = new FieldConfigString(panelId, id, label, valueOnly, null, multipleValues);
            }
            else if(type == Classifier.class)
            {
                fieldConfig = new FieldConfigString(panelId, id, label, valueOnly, null, multipleValues);
            }
            else
            {
                ConsoleManager.getInstance().error(this, "Unknown function type : " + type.getName());
            }

            groupConfig.addField(fieldConfig);
        }

        funcPrototypeStringBuilder.append(")");

        groupConfig.setLabel(funcPrototypeStringBuilder.toString());

        groupConfigList.add(groupConfig);

        return groupConfigList;
    }

    /**
     * Gets the function type.
     *
     * @param functionName the function name
     * @return the function type
     */
    @Override
    public Class<?> getFunctionType(String functionName) {
        FilterName filterName = functionNameMap.get(functionName);

        if(filterName == null)
        {
            return null;
        }

        return filterName.getReturnType();
    }

    /**
     * Gets the filter config.
     *
     * @param filterName the filter name
     * @return the filter config
     */
    @Override
    public FilterConfigInterface getFilterConfig(String filterName) {
        return filterMap.get(filterName);
    }

    /**
     * Gets the filter config.
     *
     * @param filter the filter
     * @return the filter config
     */
    @Override
    public FilterConfigInterface getFilterConfig(Filter filter) {
        Class<?> filterClassTypeName = null;

        if(filter instanceof FilterExtendedInterface)
        {
            filterClassTypeName = ((FilterExtendedInterface)filter).getOriginalFilter();
        }
        else
        {
            filterClassTypeName = filter.getClass();
        }

        return filterTypeMap.get(filterClassTypeName);
    }
}
