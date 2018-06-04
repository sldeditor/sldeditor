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
import org.opengis.filter.capability.FunctionName;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.Literal;

import com.sldeditor.filter.v2.function.namefilter.FunctionNameFilterAll;
import com.sldeditor.filter.v2.function.namefilter.FunctionNameFilterInterface;
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

    /** The function factory. */
    private DefaultFunctionFactory functionFactory = new DefaultFunctionFactory();

    /** The allowed type map. */
    private Map<Class<?>, List<Class<?>>> allowedTypeMap = new HashMap<Class<?>, List<Class<?>>>();

    /**
     * Gets the single instance of FunctionManager.
     *
     * @return single instance of FunctionManager
     */
    public static FunctionNameInterface getInstance() {
        if (instance == null) {
            instance = new FunctionManager();
        }

        return instance;
    }

    /**
     * Instantiates a new function manager.
     */
    public FunctionManager() {
        initialise();
    }

    /**
     * Initialise.
     */
    private void initialise() {
        functionNameList = functionFactory.getFunctionNames();
        List<Class<?>> classList = new ArrayList<Class<?>>();

        Logger logger = Logger.getLogger(getClass());

        for (FunctionName function : functionNameList) {
            logger.debug(function.getName());

            functionNameMap.put(function.getName(), function);

            for (int index = 0; index < function.getArgumentCount(); index++) {
                if (!classList.contains(function.getArguments().get(index).getType())) {
                    classList.add(function.getArguments().get(index).getType());
                }
                logger.debug("\t" + function.getArgumentNames().get(index) + "\t"
                        + function.getArguments().get(index).getType().getName() + "\t\t"
                        + function.getArguments().get(index).getMinOccurs() + "\t"
                        + function.getArguments().get(index).getMaxOccurs());
            }

            if (!classList.contains(function.getReturn().getType())) {
                classList.add(function.getReturn().getType());
            }
            logger.debug("\tRet : " + function.getReturn().getType().getName());
        }

        logger.debug("\nClasses");
        for (Class<?> className : classList) {
            logger.debug(className.getName());
        }

        // CHECKSTYLE:OFF
        Class<?>[] allowedNumberTypes = { Number.class, Double.class, Float.class, Integer.class,
                Long.class, Object.class };
        Class<?>[] allowedDoubleTypes = { Number.class, Double.class, Float.class, Integer.class,
                Long.class, Object.class };
        Class<?>[] allowedFloatTypes = { Number.class, Double.class, Float.class, Integer.class,
                Long.class, Object.class };
        Class<?>[] allowedIntegerTypes = { Number.class, Double.class, Float.class, Integer.class,
                Long.class, Object.class };
        Class<?>[] allowedLongTypes = { Number.class, Double.class, Float.class, Integer.class,
                Long.class, Object.class };
        Class<?>[] allowedBooleanTypes = { Boolean.class, Object.class };
        Class<?>[] allowedStringTypes = { String.class, Object.class };
        Class<?>[] allowedGeometryTypes = { Geometry.class, LineString.class, Point.class,
                MultiPoint.class, LinearRing.class, Object.class };
        Class<?>[] allowedDateTypes = { Date.class, Object.class };
        Class<?>[] allowedClassifierTypes = { RangedClassifier.class, Classifier.class,
                Object.class };
        Class<?>[] allowedBBoxTypes = { ReferencedEnvelope.class, Geometry.class };
        Class<?>[] allowedObjectTypes = { Number.class, Double.class, Float.class, Integer.class,
                Long.class, Boolean.class, String.class, Date.class, Geometry.class,
                LineString.class, Point.class, MultiPoint.class, LinearRing.class,
                RangedClassifier.class, Classifier.class, ReferencedEnvelope.class, Object.class };
        // CHECKSTYLE:ON

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
    private void populateAllowedTypes(Class<?> key, Class<?>[] allowedTypeArray) {
        allowedTypeMap.put(key, Arrays.asList(allowedTypeArray));
    }

    /**
     * Gets the function name list for the given parameter type A expectedType of null returns all functions.
     *
     * @param expectedType the expected type, restrict functions with this return type
     * @param functionNameFilterList the function name filter list
     * @return the function name list
     */
    @Override
    public List<FunctionName> getFunctionNameList(Class<?> expectedType,
            List<FunctionNameFilterInterface> functionNameFilterList) {
        if (expectedType == null) {
            return functionNameList;
        }

        if (functionNameFilterList == null) {
            functionNameFilterList = new ArrayList<FunctionNameFilterInterface>();
            functionNameFilterList.add(new FunctionNameFilterAll());
        }

        List<FunctionName> list = new ArrayList<FunctionName>();

        List<Class<?>> allowedTypes = allowedTypeMap.get(expectedType);
        if (allowedTypes != null) {
            for (FunctionName functionName : functionNameList) {
                Class<?> returnType = functionName.getReturn().getType();

                if (allowedTypes.contains(returnType)
                        && matchesFilter(functionName, functionNameFilterList)) {
                    list.add(functionName);
                }
            }
        }
        return list;
    }

    /**
     * Checks to see if FunctionName matches filter.
     *
     * @param functionName the function name
     * @param functionNameFilterList the function name filter list
     * @return true, if successful
     */
    private boolean matchesFilter(FunctionName functionName,
            List<FunctionNameFilterInterface> functionNameFilterList) {
        for (FunctionNameFilterInterface filter : functionNameFilterList) {
            if (filter.accept(functionName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Creates the expression.
     *
     * @param functionName the function name
     * @return the expression
     */
    @Override
    public Expression createExpression(FunctionName functionName) {
        if (functionName == null) {
            return null;
        }

        List<Expression> parameters = new ArrayList<Expression>();
        Literal fallback = null;

        FunctionExpressionInterface.createNewFunction(functionName, parameters);

        Function function = functionFactory.function(functionName.getFunctionName(), parameters,
                fallback);

        return function;
    }

    /**
     * Creates the expression.
     *
     * @param functionName the function name
     * @param argumentList the argument list
     * @return the expression
     */
    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.filter.v2.function.FunctionNameInterface#createExpression(org.opengis.filter.capability.FunctionName, java.util.List)
     */
    @Override
    public Expression createExpression(FunctionName functionName, List<Expression> argumentList) {
        if (functionName == null) {
            return null;
        }

        Literal fallback = null;
        Function function = functionFactory.function(functionName.getFunctionName(), argumentList,
                fallback);

        return function;
    }

    /**
     * Gets the function type.
     *
     * @param functionName the function name
     * @return the function type
     */
    @Override
    public Class<?> getFunctionType(String functionName) {
        FunctionName function = getFunction(functionName);

        if (function == null) {
            return null;
        }

        return function.getReturn().getType();
    }

    /**
     * Gets the function type.
     *
     * @param functionName the function name
     * @return the function type
     */
    @Override
    public FunctionName getFunction(String functionName) {
        FunctionName function = functionNameMap.get(functionName);

        if (function == null) {
            return null;
        }

        return function;
    }

}
