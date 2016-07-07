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
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.filter.Filter;
import org.opengis.filter.capability.FunctionName;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.Literal;
import org.opengis.parameter.Parameter;

import com.sldeditor.common.console.ConsoleManager;
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
 * The Class FunctionManager.
 *
 * @author Robert Ward (SCISYS)
 */
public class FunctionManager implements FunctionNameInterface {

    /** The singleton instance. */
    private static FunctionNameInterface instance = null;

    /** The function name list. */
    private List<FunctionName> functionNameList = new ArrayList<FunctionName>();

    /** The function name map. */
    private Map<String, FunctionName> functionNameMap = new HashMap<String, FunctionName>();

    /** The function class name map. */
    private Map<String, FunctionName> functionClassNameMap = new HashMap<String, FunctionName>();

    /** The function factory. */
    private DefaultFunctionFactory functionFactory = new DefaultFunctionFactory();

    /** The allowed type map. */
    private Map<Class<?>, List<Class<?>>> allowedTypeMap = new HashMap<Class<?>, List<Class<?>>>();

    /**
     * Gets the single instance of FunctionManager.
     *
     * @return single instance of FunctionManager
     */
    public static FunctionNameInterface getInstance()
    {
        if(instance == null)
        {
            instance = new FunctionManager();
        }

        return instance;
    }

    /**
     * Instantiates a new function manager.
     */
    public FunctionManager()
    {
        initialise();
    }

    /**
     * Initialise.
     */
    private void initialise() {
        functionNameList = functionFactory.getFunctionNames();
        List<Class<?>> classList = new ArrayList<Class<?>>();

        Logger logger = Logger.getLogger(getClass());

        for(FunctionName function : functionNameList)
        {
            logger.debug(function.getName());

            functionNameMap.put(function.getName(), function);
            functionClassNameMap.put(function.getName(), function);

            for(int index = 0; index < function.getArgumentCount(); index ++)
            {
                if(!classList.contains(function.getArguments().get(index).getType()))
                {
                    classList.add(function.getArguments().get(index).getType());
                }
                logger.debug("\t" + function.getArgumentNames().get(index) + "\t" + function.getArguments().get(index).getType().getName() + "\t" + function.getArguments().get(index).getMinOccurs() + "\t" + function.getArguments().get(index).getMaxOccurs());
            }

            if(!classList.contains(function.getReturn().getType()))
            {
                classList.add(function.getReturn().getType());
            }
            logger.debug("\tRet : " + function.getReturn().getType().getName());
        }

        logger.debug("\nClasses");
        for(Class<?> className : classList)
        {
            logger.debug(className.getName());
        }

        Class<?>[] allowedNumberTypes = {Number.class, Double.class, Float.class, Integer.class, Long.class, Object.class};
        Class<?>[] allowedDoubleTypes = {Number.class, Double.class, Float.class, Integer.class, Long.class, Object.class};
        Class<?>[] allowedFloatTypes = {Number.class, Double.class, Float.class, Integer.class, Long.class, Object.class};
        Class<?>[] allowedIntegerTypes = {Number.class, Double.class, Float.class, Integer.class, Long.class, Object.class};
        Class<?>[] allowedLongTypes = {Number.class, Double.class, Float.class, Integer.class, Long.class, Object.class};
        Class<?>[] allowedBooleanTypes = {Boolean.class, Object.class};
        Class<?>[] allowedStringTypes = {String.class, Object.class};
        Class<?>[] allowedGeometryTypes = {Geometry.class, LineString.class, Point.class, MultiPoint.class, LinearRing.class, Object.class};
        Class<?>[] allowedDateTypes = {Date.class, Object.class};
        Class<?>[] allowedClassifierTypes = {RangedClassifier.class, Classifier.class, Object.class};
        Class<?>[] allowedBBoxTypes = {ReferencedEnvelope.class, Geometry.class};
        Class<?>[] allowedObjectTypes = {Number.class, Double.class, Float.class, Integer.class, Long.class,
                Boolean.class, String.class, Date.class, Geometry.class, LineString.class, Point.class, MultiPoint.class, LinearRing.class,
                RangedClassifier.class, Classifier.class, ReferencedEnvelope.class, Object.class};

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
        populateAllowedTypes(ReferencedEnvelope.class, allowedBBoxTypes);
        populateAllowedTypes(Object.class, allowedObjectTypes);
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
     * Gets the function name list for the given parameter type
     * A expectedType of null returns all functions.
     *
     * @param expectedType the expected type, restrict functions with this return type 
     * @return the function name list
     */
    @Override
    public List<FunctionName> getFunctionNameList(Class<?> expectedType)
    {
        if(expectedType == null)
        {
            return functionNameList;
        }

        List<FunctionName> list = new ArrayList<FunctionName>();

        List<Class<?>> allowedTypes = allowedTypeMap.get(expectedType);
        if(allowedTypes != null)
        {
            for(FunctionName functionName : functionNameList)
            {
                Class<?> returnType = functionName.getReturn().getType();

                if(allowedTypes.contains(returnType))
                {
                    list.add(functionName);
                }
            }
        }
        return list;
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
                fieldConfig = new FieldConfigDouble(panelId, id, label, valueOnly);
            }
            else if(type == Double.class)
            {
                fieldConfig = new FieldConfigDouble(panelId, id, label, valueOnly);
            }
            else if(type == Float.class)
            {
                fieldConfig = new FieldConfigDouble(panelId, id, label, valueOnly);
            }
            else if(type == Integer.class)
            {
                fieldConfig = new FieldConfigInteger(panelId, id, label, valueOnly);
            }
            else if(type == Long.class)
            {
                fieldConfig = new FieldConfigInteger(panelId, id, label, valueOnly);
            }
            else if(type == String.class)
            {
                fieldConfig = new FieldConfigString(panelId, id, label, valueOnly, null);
            }
            else if(type == Object.class)
            {
                fieldConfig = new FieldConfigString(panelId, id, label, valueOnly, null);
            }
            else if(type == Boolean.class)
            {
                fieldConfig = new FieldConfigBoolean(panelId, id, label, valueOnly);
            }
            else if(type == Geometry.class)
            {
                fieldConfig = new FieldConfigGeometry(panelId, id, label, valueOnly, null);
            }
            else if(type == LineString.class)
            {
                fieldConfig = new FieldConfigGeometry(panelId, id, label, valueOnly, null);
            }
            else if(type == Date.class)
            {
                fieldConfig = new FieldConfigDate(panelId, id, label, valueOnly);
            }
            else if(type == Class.class)
            {
                fieldConfig = new FieldConfigString(panelId, id, label, valueOnly, null);
            }
            else if(type == Classifier.class)
            {
                fieldConfig = new FieldConfigString(panelId, id, label, valueOnly, null);
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
        FunctionName function = functionNameMap.get(functionName);

        if(function == null)
        {
            return null;
        }

        return function.getReturn().getType();
    }

    /**
     * Creates the filter.
     *
     * @param functionName the function name
     * @return the filter
     */
    @Override
    public Filter createFilter(FunctionName functionName) {
        // Does nothing
        return null;
    }

}
