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

import java.util.List;

import org.opengis.filter.Filter;
import org.opengis.filter.capability.FunctionName;
import org.opengis.filter.expression.Expression;

import com.sldeditor.ui.detail.config.base.GroupConfigInterface;

/**
 * The Interface FilterNameInterface.
 *
 * @author Robert Ward (SCISYS)
 */
public interface FilterNameInterface {
    
    /**
     * Gets the filter config list.
     *
     * @return the filter config list
     */
    List<FilterConfigInterface> getFilterConfigList();

    /**
     * Creates the expression.
     *
     * @param functionName the function name
     * @return the expression
     */
    Expression createExpression(FunctionName functionName);     

    /**
     * Convert function parameters to ui components.
     *
     * @param panelId the panel id
     * @param functionName the function name
     * @return the list of ui components to display
     */
    List<GroupConfigInterface> convertParameters(Class<?> panelId, FunctionName functionName);

    /**
     * Gets the function type for the given function name.
     *
     * @param functionName the function name
     * @return the function type
     */
    Class<?> getFunctionType(String functionName);

    /**
     * Gets the filter config.
     *
     * @param filterClassName the filter class name
     * @return the filter config
     */
    FilterConfigInterface getFilterConfig(String filterClassName);

    /**
     * Gets the filter config.
     *
     * @param filter the filter
     * @return the filter config
     */
    FilterConfigInterface getFilterConfig(Filter filter);
}
