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

import com.sldeditor.filter.v2.expression.ExpressionTypeEnum;

/**
 * The Class FilterNameParameter.
 *
 * @author Robert Ward (SCISYS)
 */
public class FilterNameParameter {

    /** The name. */
    private String name;

    /** The expression type. */
    private ExpressionTypeEnum expressionType;

    /** The data type. */
    private Class<?> dataType;

    /**
     * Instantiates a new filter name parameter.
     *
     * @param name the name
     * @param expressionType the expression type
     * @param dataType the data type
     */
    public FilterNameParameter(String name, ExpressionTypeEnum expressionType, Class<?> dataType) {
        super();
        this.name = name;
        this.expressionType = expressionType;
        this.dataType = dataType;
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the expression type.
     *
     * @return the expressionType
     */
    public ExpressionTypeEnum getExpressionType() {
        return expressionType;
    }

    /**
     * Gets the data type.
     *
     * @return the dataType
     */
    public Class<?> getDataType() {
        return dataType;
    }
}
