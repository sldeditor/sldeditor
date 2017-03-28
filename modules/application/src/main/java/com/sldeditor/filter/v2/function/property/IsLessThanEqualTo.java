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

package com.sldeditor.filter.v2.function.property;

import java.util.List;

import org.geotools.filter.IsLessThenOrEqualToImpl;
import org.geotools.filter.LiteralExpressionImpl;
import org.opengis.filter.Filter;
import org.opengis.filter.expression.Expression;

import com.sldeditor.filter.v2.expression.ExpressionTypeEnum;
import com.sldeditor.filter.v2.function.FilterConfigInterface;
import com.sldeditor.filter.v2.function.FilterExtendedInterface;
import com.sldeditor.filter.v2.function.FilterName;
import com.sldeditor.filter.v2.function.FilterNameParameter;

/**
 * The Class IsLessThan.
 *
 * @author Robert Ward (SCISYS)
 */
public class IsLessThanEqualTo implements FilterConfigInterface {

    /**
     * The Class IsLessThanOrEqualToExtended.
     */
    public class IsLessThanOrEqualToExtended extends IsLessThenOrEqualToImpl
            implements FilterExtendedInterface {

        /**
         * Instantiates a new checks if is less than or equal to extended.
         */
        public IsLessThanOrEqualToExtended() {
            super(null, null);
        }

        /**
         * Instantiates a new checks if is less than or equal to extended.
         *
         * @param expression1 the expression 1
         * @param expression2 the expression 2
         * @param matchCase the match case
         */
        public IsLessThanOrEqualToExtended(Expression expression1, Expression expression2,
                boolean matchCase) {
            super(expression1, expression2, matchCase);
        }

        /*
         * (non-Javadoc)
         * 
         * @see com.sldeditor.filter.v2.function.FilterExtendedInterface#getOriginalFilter()
         */
        @Override
        public Class<?> getOriginalFilter() {
            return IsLessThenOrEqualToImpl.class;
        }
    }

    /**
     * Default constructor.
     */
    public IsLessThanEqualTo() {
    }

    /**
     * Gets the filter configuration.
     *
     * @return the filter configuration
     */
    @Override
    public FilterName getFilterConfiguration() {
        FilterName filterName = new FilterName("PropertyIsLessThanEqualTo", Boolean.class);
        filterName.addParameter(
                new FilterNameParameter("property", ExpressionTypeEnum.PROPERTY, Object.class));
        filterName.addParameter(
                new FilterNameParameter("expression", ExpressionTypeEnum.EXPRESSION, Object.class));
        filterName.addParameter(
                new FilterNameParameter("matchCase", ExpressionTypeEnum.LITERAL, Boolean.class));

        return filterName;
    }

    /**
     * Gets the filter class.
     *
     * @return the filter class
     */
    @Override
    public Class<?> getFilterClass() {
        return IsLessThenOrEqualToImpl.class;
    }

    /**
     * Creates the filter.
     *
     * @return the filter
     */
    @Override
    public Filter createFilter() {
        return new IsLessThanOrEqualToExtended();
    }

    /**
     * Creates the filter.
     *
     * @param parameterList the parameter list
     * @return the filter
     */
    @Override
    public Filter createFilter(List<Expression> parameterList) {
        IsLessThenOrEqualToImpl filter = null;

        if ((parameterList == null) || (parameterList.size() != 3)) {
            filter = new IsLessThanOrEqualToExtended();
        } else {
            LiteralExpressionImpl matchCase = (LiteralExpressionImpl) parameterList.get(2);

            filter = new IsLessThanOrEqualToExtended(parameterList.get(0), parameterList.get(1),
                    (Boolean) matchCase.getValue());
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
