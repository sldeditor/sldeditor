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

package com.sldeditor.ui.detail.config.transform;

import com.sldeditor.common.console.ConsoleManager;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import org.geotools.process.function.ProcessFunction;
import org.opengis.filter.expression.Expression;

/**
 * The Class ParameterFunctionUtils. @TODO Isolated functionality to provide a workaround to the
 * org.geotools.process.function.ParameterFunction class not having public scope.
 *
 * @author Robert Ward (SCISYS)
 */
public class ParameterFunctionUtils {

    /** The Constant GET_PARAMETERS. */
    private static final String GET_PARAMETERS = "getParameters";

    /** The Constant PARAMETER_NOT_SET. */
    private static final String PARAMETER_NOT_SET = ":<not set>";

    /**
     * Gets the expression list. @TODO This gets round the issue where the
     * org.geotools.process.function.ParameterFunction does not have a public accessor. Using
     * reflection and changing the accessibility flag we can read the process function data.
     *
     * @param parameter the parameter
     * @return the expression list
     */
    @SuppressWarnings("unchecked")
    public static List<Expression> getExpressionList(Expression parameter) {
        List<Expression> parameterList = null;

        if (parameter != null) {
            for (Method method : parameter.getClass().getMethods()) {
                if (method.getName().compareTo(GET_PARAMETERS) == 0) {
                    try {
                        method.setAccessible(true);
                        Object[] args = null;
                        parameterList = (List<Expression>) method.invoke(parameter, args);

                        return parameterList;
                    } catch (IllegalAccessException e) {
                        ConsoleManager.getInstance().exception(ParameterFunctionUtils.class, e);
                    } catch (IllegalArgumentException e) {
                        ConsoleManager.getInstance().exception(ParameterFunctionUtils.class, e);
                    } catch (InvocationTargetException e) {
                        ConsoleManager.getInstance().exception(ParameterFunctionUtils.class, e);
                    }
                }
            }
        }
        return parameterList;
    }

    /**
     * Gets the process function data as a string. Each parameter is on a new line.
     *
     * @param processFunction the process function
     * @return the string
     */
    public static String getString(ProcessFunction processFunction) {
        StringBuilder sb = new StringBuilder();
        if (processFunction != null) {
            sb.append(processFunction.getFunctionName().toString());
            sb.append("(");
            int count = 0;
            for (Expression expression : processFunction.getParameters()) {
                if (count > 0) {
                    sb.append(", ");
                }
                sb.append("\n  ");
                List<Expression> subParameterList = getExpressionList(expression);

                if ((subParameterList != null) && !subParameterList.isEmpty()) {
                    if (subParameterList.size() == 1) {
                        sb.append(subParameterList.get(0).toString());
                        sb.append(PARAMETER_NOT_SET);
                    } else if (subParameterList.size() == 2) {
                        sb.append(subParameterList.get(0).toString());
                        sb.append(":");
                        Expression expression2 = subParameterList.get(1);
                        if (expression2 != null) {
                            sb.append(expression2.toString());
                        } else {
                            sb.append(PARAMETER_NOT_SET);
                        }
                    }
                }

                count++;
            }
            sb.append("\n)");
        }
        return sb.toString();
    }
}
