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

package com.sldeditor.filter.v2.expression;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.function.CategorizeFunction;
import org.geotools.filter.function.EqualIntervalFunction;
import org.geotools.filter.function.InterpolateFunction;
import org.geotools.filter.function.JenksNaturalBreaksFunction;
import org.geotools.filter.function.QuantileFunction;
import org.geotools.filter.function.RecodeFunction;
import org.geotools.filter.function.StandardDeviationFunction;
import org.geotools.filter.function.StringTemplateFunction;
import org.geotools.filter.function.UniqueIntervalFunction;
import org.geotools.filter.function.color.ConstrastFunction;
import org.geotools.filter.function.color.GrayscaleFunction;
import org.geotools.filter.function.color.LightenFunction;
import org.geotools.filter.function.math.ModuloFunction;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Function;

/**
 * The Class FunctionInterfaceUtils.
 *
 * <p>Handles the functions that inherit from org.geotools.filter.function.Function interface
 */
public class FunctionInterfaceUtils {

    /** Private default constructor */
    private FunctionInterfaceUtils() {
        // Private default constructor
    }

    /** The filter factory. */
    private static FilterFactory ff = CommonFactoryFinder.getFilterFactory();

    /**
     * Handle function interface.
     *
     * @param parentNode the parent node
     * @param index the index
     * @param expression the expression
     */
    public static void handleFunctionInterface(
            ExpressionNode parentNode, int index, Expression expression) {
        Function function = (Function) parentNode.getExpression();

        List<Expression> parameterList = new ArrayList<>();
        for (Expression exp : function.getParameters()) {
            parameterList.add(exp);
        }

        if (index < parameterList.size()) {
            parameterList.remove(index);
        }

        if (expression != null) {
            parameterList.add(index, expression);
        }

        if (parentNode.getExpression() instanceof InterpolateFunction) {
            parentNode.setExpression(
                    new InterpolateFunction(parameterList, function.getFallbackValue()));
        } else if (parentNode.getExpression() instanceof CategorizeFunction) {
            parentNode.setExpression(
                    new CategorizeFunction(parameterList, function.getFallbackValue()));
        } else if (parentNode.getExpression() instanceof RecodeFunction) {
            parentNode.setExpression(
                    new RecodeFunction(parameterList, function.getFallbackValue()));
        } else if (parentNode.getExpression() instanceof StringTemplateFunction) {
            parentNode.setExpression(
                    new StringTemplateFunction(parameterList, function.getFallbackValue()));
        }
    }

    /**
     * Handle function interface.
     *
     * @param parentNode the parent node
     * @param index the index
     */
    public static void handleFunctionInterface(ExpressionNode parentNode, int index) {
        handleFunctionInterface(parentNode, index, null);
    }

    /**
     * Creates the new function.
     *
     * @param newExpression the new expression
     * @param params the params
     * @return the expression
     */
    public static Expression createNewFunction(Expression newExpression, List<Expression> params) {
        if (newExpression instanceof InterpolateFunction) {
            return new InterpolateFunction(params, null);
        } else if (newExpression instanceof CategorizeFunction) {

            // CategorizeFunction needs all the fields populated
            for (int index = 0; index < params.size() - 1; index++) {
                if (params.get(index) == null) {
                    params.remove(index);
                    params.add(index, ff.literal(""));
                }
            }

            params.remove(params.size() - 1);
            params.add(ff.literal(CategorizeFunction.PRECEDING));

            return new CategorizeFunction(params, null);
        } else if (newExpression instanceof RecodeFunction) {
            return new RecodeFunction(params, null);
        } else if (newExpression instanceof StringTemplateFunction) {
            return new StringTemplateFunction(params, null);
        }
        return null;
    }

    /**
     * The missing toString() method.
     *
     * @param expression the expression
     * @return the string
     */
    private static String missingToString(Function expression) {
        StringBuilder sb = new StringBuilder();
        sb.append(expression.getName());
        sb.append("(");
        List<org.opengis.filter.expression.Expression> params = expression.getParameters();
        if (params != null) {
            org.opengis.filter.expression.Expression exp;
            for (Iterator<org.opengis.filter.expression.Expression> it = params.iterator();
                    it.hasNext(); ) {
                exp = it.next();
                sb.append("[");
                sb.append(exp);
                sb.append("]");
                if (it.hasNext()) {
                    sb.append(", ");
                }
            }
        }
        sb.append(")");
        return sb.toString();
    }

    /**
     * To string.
     *
     * @param expression the expression
     * @return the string
     */
    public static String toString(Expression expression) {
        if (expression instanceof RecodeFunction) {
            return missingToString((RecodeFunction) expression);
        } else if (expression instanceof StringTemplateFunction) {
            return missingToString((StringTemplateFunction) expression);
        } else if (expression instanceof ConstrastFunction) {
            return missingToString((ConstrastFunction) expression);
        } else if (expression instanceof LightenFunction) {
            return missingToString((LightenFunction) expression);
        } else if (expression instanceof JenksNaturalBreaksFunction) {
            return missingToString((JenksNaturalBreaksFunction) expression);
        } else if (expression instanceof CategorizeFunction) {
            return missingToString((CategorizeFunction) expression);
        } else if (expression instanceof UniqueIntervalFunction) {
            return missingToString((UniqueIntervalFunction) expression);
        } else if (expression instanceof GrayscaleFunction) {
            return missingToString((GrayscaleFunction) expression);
        } else if (expression instanceof QuantileFunction) {
            return missingToString((QuantileFunction) expression);
        } else if (expression instanceof ModuloFunction) {
            return missingToString((ModuloFunction) expression);
        } else if (expression instanceof StandardDeviationFunction) {
            return missingToString((StandardDeviationFunction) expression);
        } else if (expression instanceof EqualIntervalFunction) {
            return missingToString((EqualIntervalFunction) expression);
        } else {
            return expression.toString();
        }
    }
}
