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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.process.function.ProcessFunction;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.capability.FunctionName;
import org.opengis.filter.expression.Expression;
import org.opengis.parameter.Parameter;

import com.sldeditor.ui.detail.config.transform.ParameterFunctionUtils;

/**
 * Class that provides the built in process functions present in GeoTools.
 *
 * @author Robert Ward (SCISYS)
 */
public class BuiltInProcessFunction {

    /** The filter factory. */
    private static FilterFactory ff = CommonFactoryFinder.getFilterFactory();

    /**
     * Extract parameters.
     *
     * @param functionName the function name
     * @param selectedProcessFunctionData the selected process function data
     * @return the list
     */
    public List<ProcessFunctionParameterValue> extractParameters(FunctionName functionName, ProcessFunction selectedProcessFunctionData) {

        List<ProcessFunctionParameterValue> valueList = new ArrayList<ProcessFunctionParameterValue>();

        // Populate the parameter definitions first.
        // This ensures if there is parameter data missing we 
        // don't miss populating the parameter definition.
        for(Parameter<?> parameter : functionName.getArguments())
        {
            ProcessFunctionParameterValue value = new ProcessFunctionParameterValue();

            populateParameterDefinition(parameter, value);

            valueList.add(value);
        }

        // Now populate any parameter values we have
        if(selectedProcessFunctionData != null)
        {
            for(Expression parameter : selectedProcessFunctionData.getParameters())
            {
                List<Expression> parameterList = ParameterFunctionUtils.getExpressionList(parameter);

                if((parameterList != null) && !parameterList.isEmpty())
                {
                    Expression paramName = parameterList.get(0);

                    ProcessFunctionParameterValue value = findParameterValue(valueList, paramName.toString());

                    if(parameterList.size() > 1)
                    {
                        Expression paramValue = parameterList.get(1);

                        value.value = paramValue;
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
    private ProcessFunctionParameterValue findParameterValue(List<ProcessFunctionParameterValue> valueList,
            String parameterName) {
        for(ProcessFunctionParameterValue paramValue : valueList)
        {
            if(paramValue.name.compareTo(parameterName) == 0)
            {
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
    private void populateParameterDefinition(Parameter<?> parameter,
            ProcessFunctionParameterValue value) {
        if(parameter != null)
        {
            value.name = parameter.getName();
            if(parameter.getType().isEnum())
            {
                List<?> enumList = Arrays.asList(parameter.getType().getEnumConstants());
                value.enumValueList = new ArrayList<String>();

                for(Object enumValue : enumList)
                {
                    String stringValue = enumValue.toString();

                    value.enumValueList.add(stringValue);
                }
                value.type = StringBuilder.class;
                value.value = ff.literal(parameter.getDefaultValue().toString());
            }
            else
            {
                value.type = parameter.getType();
                
                if(parameter.getDefaultValue() != null)
                {
                    value.value = ff.literal(parameter.getDefaultValue());
                }
            }
            value.optional = !parameter.isRequired();
            value.dataType = parameter.getType().getSimpleName();

            value.minOccurences = parameter.getMinOccurs();
            value.maxOccurences = parameter.getMaxOccurs();
        }
    }
}
