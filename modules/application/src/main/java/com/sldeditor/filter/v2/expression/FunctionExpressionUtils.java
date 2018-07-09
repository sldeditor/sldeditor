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

import java.util.Iterator;
import java.util.List;
import org.geotools.filter.FunctionExpressionImpl;
import org.geotools.filter.function.Collection_UniqueFunction;
import org.opengis.filter.expression.Expression;

/**
 * The Class FunctionExpressionUtils.
 *
 * <p>Handles the functions that inherit from org.geotools.filter.FunctionExpressionImpl interface
 */
public class FunctionExpressionUtils {

    /**
     * The missing toString() method.
     *
     * @param expression the expression
     * @return the string
     */
    private static String missing_toString(FunctionExpressionImpl expression) {
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
        if (expression instanceof Collection_UniqueFunction) {
            return missing_toString((Collection_UniqueFunction) expression);
        } else {
            return expression.toString();
        }
    }
}
