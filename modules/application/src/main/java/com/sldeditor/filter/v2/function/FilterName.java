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
import java.util.List;

/**
 * The Class FilterName.
 *
 * @author Robert Ward (SCISYS)
 */
public class FilterName {

    /** The filter name. */
    private String filterName;

    /** The return type. */
    private Class<?> returnType;

    /** The parameter list. */
    private List<FilterNameParameter> parameterList = new ArrayList<FilterNameParameter>();

    /**
     * Instantiates a new filter name.
     *
     * @param filterName the filter name
     * @param returnType the return type
     */
    public FilterName(String filterName, Class<?> returnType) {
        super();
        this.filterName = filterName;
        this.returnType = returnType;
    }

    /**
     * Adds the parameter.
     *
     * @param parameter the parameter
     */
    public void addParameter(FilterNameParameter parameter) {
        parameterList.add(parameter);
    }

    /**
     * Gets the filter name.
     *
     * @return the filterName
     */
    public String getFilterName() {
        return filterName;
    }

    /**
     * Gets the return type.
     *
     * @return the returnType
     */
    public Class<?> getReturnType() {
        return returnType;
    }

    /**
     * Gets the parameter list.
     *
     * @return the parameterList
     */
    public List<FilterNameParameter> getParameterList() {
        return parameterList;
    }

    /**
     * Gets the parameter at the specified index.
     *
     * @param index the index
     * @return the parameter
     */
    public FilterNameParameter getParameter(int index) {
        if ((index < 0) || (index >= parameterList.size())) {
            return null;
        }
        return parameterList.get(index);
    }
}
