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

import com.sldeditor.filter.v2.function.namefilter.FunctionNameFilterInterface;
import java.util.List;
import org.opengis.filter.capability.FunctionName;
import org.opengis.filter.expression.Expression;

/**
 * The Interface FunctionNameInterface.
 *
 * @author Robert Ward (SCISYS)
 */
public interface FunctionNameInterface {

    /**
     * Gets the function name list.
     *
     * @param expectedType the expected type, restrict functions with this return type
     * @param functionNameFilterList the function name filter list
     * @return the function name list
     */
    List<FunctionName> getFunctionNameList(
            Class<?> expectedType, List<FunctionNameFilterInterface> functionNameFilterList);

    /**
     * Creates the expression.
     *
     * @param functionName the function name
     * @return the expression
     */
    Expression createExpression(FunctionName functionName);

    /**
     * Creates the expression.
     *
     * @param function the function
     * @param argumentList the argument list
     * @return the expression
     */
    Expression createExpression(FunctionName function, List<Expression> argumentList);

    /**
     * Gets the function type for the given function name.
     *
     * @param functionName the function name
     * @return the function type
     */
    Class<?> getFunctionType(String functionName);

    /**
     * Gets the function.
     *
     * @param functionName the function name
     * @return the function
     */
    FunctionName getFunction(String functionName);
}
