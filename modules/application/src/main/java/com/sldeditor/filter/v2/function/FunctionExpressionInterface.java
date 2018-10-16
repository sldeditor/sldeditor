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

package com.sldeditor.filter.v2.function;

import com.sldeditor.common.console.ConsoleManager;
import java.awt.Color;
import java.util.Arrays;
import java.util.List;
import javax.measure.Unit;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.function.CategorizeFunction;
import org.geotools.filter.function.Classifier;
import org.geotools.filter.function.Collection_AverageFunction;
import org.geotools.filter.function.Collection_BoundsFunction;
import org.geotools.filter.function.Collection_CountFunction;
import org.geotools.filter.function.Collection_MaxFunction;
import org.geotools.filter.function.Collection_MedianFunction;
import org.geotools.filter.function.Collection_MinFunction;
import org.geotools.filter.function.Collection_NearestFunction;
import org.geotools.filter.function.Collection_SumFunction;
import org.geotools.filter.function.Collection_UniqueFunction;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LineString;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.capability.FunctionName;
import org.opengis.filter.expression.Expression;
import org.opengis.parameter.Parameter;

/** The Class FunctionExpressionInterface. */
public class FunctionExpressionInterface {

    /** The filter factory. */
    private static FilterFactory ff = CommonFactoryFinder.getFilterFactory();

    /** The collection function list. */
    private static List<String> collectionFunctionList =
            Arrays.asList(
                    Collection_AverageFunction.NAME.getName(),
                    Collection_BoundsFunction.NAME.getName(),
                    Collection_CountFunction.NAME.getName(),
                    Collection_MaxFunction.NAME.getName(),
                    Collection_MedianFunction.NAME.getName(),
                    Collection_MinFunction.NAME.getName(),
                    Collection_NearestFunction.NAME.getName(),
                    Collection_SumFunction.NAME.getName(),
                    Collection_UniqueFunction.NAME.getName());

    /**
     * Creates the new function.
     *
     * @param functionName the function name
     * @param parameters the parameters
     */
    public static void createNewFunction(FunctionName functionName, List<Expression> parameters) {
        String name = functionName.getName();

        if (name.compareToIgnoreCase("darken") == 0) {
            System.out.println("");
        }

        if (collectionFunctionList.contains(name)) {
            parameters.add(ff.property("geom"));
        } else if (name.compareToIgnoreCase("Categorize") == 0) {
            // CategorizeFunction needs all the fields populated
            for (int index = 0; index < functionName.getArguments().size() - 1; index++) {
                parameters.add(index, ff.literal(""));
            }

            parameters.remove(parameters.size() - 1);
            parameters.add(ff.literal(CategorizeFunction.PRECEDING));
        } else {
            List<Parameter<?>> functionParamList = functionName.getArguments();

            for (int paramIndex = 0; paramIndex < functionParamList.size(); paramIndex++) {
                Parameter<?> param = functionParamList.get(paramIndex);

                Class<?> type = param.getType();
                if (type == Object.class) {
                    parameters.add(ff.literal(""));
                } else if (type == String.class) {
                    parameters.add(ff.literal(""));
                } else if ((type == Number.class) || (type == Double.class)) {
                    parameters.add(ff.literal(0.0));
                } else if (type == Float.class) {
                    parameters.add(ff.literal(0.0f));
                } else if ((type == Integer.class) || (type == Long.class)) {
                    parameters.add(ff.literal(0));
                } else if (type == Boolean.class) {
                    parameters.add(ff.literal(false));
                } else if (type == Unit.class) {
                    parameters.add(null);
                } else if (type == Color.class) {
                    parameters.add(null);
                } else if (type == Geometry.class) {
                    parameters.add(null);
                } else if (type == org.opengis.geometry.Geometry.class) {
                    parameters.add(null);
                } else if (type == LineString.class) {
                    parameters.add(null);
                } else if (type == Classifier.class) {
                    parameters.add(null);
                } else if (type == Class.class) {
                    parameters.add(null);
                } else if (type.getName()
                                .compareToIgnoreCase(
                                        "org.geotools.filter.function.color.AbstractHSLFunction$Method")
                        == 0) {
                    parameters.add(null);
                } else if (type.getName()
                                .compareToIgnoreCase("org.geotools.styling.visitor.RescalingMode")
                        == 0) {
                    parameters.add(ff.literal(0));
                } else {
                    Object newObj = null;
                    try {
                        newObj = type.newInstance();
                    } catch (InstantiationException e) {
                        ConsoleManager.getInstance()
                                .exception(FunctionExpressionInterface.class, e);
                    } catch (IllegalAccessException e) {
                        ConsoleManager.getInstance()
                                .exception(FunctionExpressionInterface.class, e);
                    }

                    parameters.add(ff.literal(newObj));
                }
            }
        }
    }
}
