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

package com.sldeditor.rendertransformation;

import com.sldeditor.rendertransformation.types.RenderTransformValueFactory;
import com.sldeditor.ui.detail.config.transform.ParameterFunctionUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.geotools.process.function.ProcessFunction;
import org.opengis.filter.capability.FunctionName;
import org.opengis.filter.expression.Expression;
import org.opengis.parameter.Parameter;

/**
 * Class that provides the built in process functions present in GeoTools.
 *
 * @author Robert Ward (SCISYS)
 */
public class BuiltInProcessFunction {

    /** The value factory. */
    private RenderTransformValueFactory valueFactory = RenderTransformValueFactory.getInstance();

    /**
     * Extract parameters.
     *
     * @param functionName the function name
     * @param selectedProcessFunctionData the selected process function data
     * @return the list
     */
    public List<ProcessFunctionParameterValue> extractParameters(
            FunctionName functionName, ProcessFunction selectedProcessFunctionData) {

        List<ProcessFunctionParameterValue> valueList =
                new ArrayList<ProcessFunctionParameterValue>();

        // Populate the parameter definitions first.
        // This ensures if there is parameter data missing we
        // don't miss populating the parameter definition.
        if (functionName != null) {
            for (Parameter<?> parameter : functionName.getArguments()) {
                ProcessFunctionParameterValue value = new ProcessFunctionParameterValue();

                populateParameterDefinition(parameter, value);

                valueList.add(value);
            }
        }

        // Now populate any parameter values we have
        if (selectedProcessFunctionData != null) {
            for (Expression parameter : selectedProcessFunctionData.getParameters()) {
                List<Expression> parameterList =
                        ParameterFunctionUtils.getExpressionList(parameter);

                if ((parameterList != null) && !parameterList.isEmpty()) {
                    Expression paramName = parameterList.get(0);

                    ProcessFunctionParameterValue value =
                            findParameterValue(valueList, paramName.toString());

                    if ((parameterList.size() > 1) && (value != null)) {
                        Expression paramValue = parameterList.get(1);

                        value.getObjectValue().setValue(paramValue);
                        if (value.isOptional()) {
                            value.setIncluded(true);
                        }
                    }
                }
            }
        }

        return valueList;
    }

    /**
     * Find parameter value.
     *
     * @param valueList the value list
     * @param parameterName the parameter name
     * @return the process function parameter value
     */
    private ProcessFunctionParameterValue findParameterValue(
            List<ProcessFunctionParameterValue> valueList, String parameterName) {
        for (ProcessFunctionParameterValue paramValue : valueList) {
            if (paramValue.getName().compareTo(parameterName) == 0) {
                return paramValue;
            }
        }
        return null;
    }

    /**
     * Populate parameter definition.
     *
     * @param parameter the parameter
     * @param value the value
     */
    private void populateParameterDefinition(
            Parameter<?> parameter, ProcessFunctionParameterValue value) {
        if (parameter != null) {
            value.setName(parameter.getName());
            value.setType(parameter.getType());
            if (parameter.getType().isEnum()) {
                value.setObjectValue(
                        valueFactory.getEnum(
                                parameter.getType(),
                                Arrays.asList(parameter.getType().getEnumConstants())));

                if (parameter.getDefaultValue() != null) {
                    value.getObjectValue().setDefaultValue(parameter.getDefaultValue().toString());
                }
            } else {

                value.setObjectValue(valueFactory.getValue(value.getType()));

                if (value.getObjectValue() != null) {
                    value.getObjectValue().setDefaultValue(parameter.getDefaultValue());
                }
            }
            value.setOptional(!parameter.isRequired());
            value.setDataType(parameter.getType().getSimpleName());

            value.setMinOccurences(parameter.getMinOccurs());
            value.setMaxOccurences(parameter.getMaxOccurs());
        }
    }
}
