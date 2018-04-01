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

package com.sldeditor.filter.v2.function.misc;

import java.util.List;

import org.geotools.filter.IsNullImpl;
import org.opengis.filter.Filter;
import org.opengis.filter.expression.Expression;

import com.sldeditor.filter.v2.expression.ExpressionTypeEnum;
import com.sldeditor.filter.v2.function.FilterConfigInterface;
import com.sldeditor.filter.v2.function.FilterExtendedInterface;
import com.sldeditor.filter.v2.function.FilterName;
import com.sldeditor.filter.v2.function.FilterNameParameter;

/**
 * The Class IsNull.
 *
 * @author Robert Ward (SCISYS)
 */
public class IsNull implements FilterConfigInterface {

    /**
     * The Class IsNullExtended.
     */
    public class IsNullExtended extends IsNullImpl implements FilterExtendedInterface {

        /**
         * Instantiates a new checks if is null extended.
         */
        public IsNullExtended() {
            super(null);
        }

        /**
         * Instantiates a new checks if is null extended.
         *
         * @param expression1 the expression 1
         */
        public IsNullExtended(Expression expression1) {
            super(expression1);
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.geotools.filter.CompareFilterImpl#toString()
         */
        public String toString() {
            return "[ " + getExpression() + " IS NULL ]";
        }

        /*
         * (non-Javadoc)
         * 
         * @see com.sldeditor.filter.v2.function.FilterExtendedInterface#getOriginalFilter()
         */
        @Override
        public Class<?> getOriginalFilter() {
            return IsNullImpl.class;
        }
    }

    /**
     * Default constructor.
     */
    public IsNull() {
    }

    /**
     * Gets the filter configuration.
     *
     * @return the filter configuration
     */
    @Override
    public FilterName getFilterConfiguration() {
        FilterName filterName = new FilterName("IsNull", Boolean.class);
        filterName.addParameter(
                new FilterNameParameter("expression", ExpressionTypeEnum.EXPRESSION, Object.class));

        return filterName;
    }

    /**
     * Gets the filter class.
     *
     * @return the filter class
     */
    @Override
    public Class<?> getFilterClass() {
        return IsNullImpl.class;
    }

    /**
     * Creates the filter.
     *
     * @return the filter
     */
    @Override
    public Filter createFilter() {
        return new IsNullExtended();
    }

    /**
     * Creates the filter.
     *
     * @param parameterList the parameter list
     * @return the filter
     */
    @Override
    public Filter createFilter(List<Expression> parameterList) {

        IsNullImpl filter = null;

        if ((parameterList == null) || parameterList.isEmpty()) {
            filter = new IsNullExtended();
        } else {
            filter = new IsNullExtended(parameterList.get(0));
        }
        return filter;
    }

    /**
     * Creates the logic filter.
     *
     * @param filterList the filter list
     * @return the filter
     */
    @Override
    public Filter createLogicFilter(List<Filter> filterList) {
        // Not supported
        return null;
    }
}
