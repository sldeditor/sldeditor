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
import org.opengis.filter.expression.Expression;

/**
 * Filter configuration interface.
 *
 * @author Robert Ward (SCISYS)
 */
public interface FilterConfigInterface {

    /**
     * Gets the filter configuration.
     *
     * @return the filter configuration
     */
    FilterName getFilterConfiguration();

    /**
     * Gets the filter class.
     *
     * @return the filter class
     */
    Class<?> getFilterClass();

    /**
     * Creates the filter.
     *
     * @return the filter
     */
    Filter createFilter();

    /**
     * Creates the filter.
     *
     * @param parameterList the parameter list
     * @return the filter
     */
    Filter createFilter(List<Expression> parameterList);

    /**
     * Creates the filter.
     *
     * @param filterList the filter list
     * @return the filter
     */
    Filter createLogicFilter(List<Filter> filterList);
}
