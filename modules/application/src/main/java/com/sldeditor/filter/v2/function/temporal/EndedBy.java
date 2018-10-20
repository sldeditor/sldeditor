/*
 * SLD Editor - The Open Source Java SLD Editor
 *
 * Copyright (C) 2018, SCISYS UK Limited
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

package com.sldeditor.filter.v2.function.temporal;

import com.sldeditor.filter.v2.expression.ExpressionTypeEnum;
import com.sldeditor.filter.v2.function.FilterBase;
import com.sldeditor.filter.v2.function.FilterConfigInterface;
import com.sldeditor.filter.v2.function.FilterExtendedInterface;
import com.sldeditor.filter.v2.function.FilterName;
import com.sldeditor.filter.v2.function.FilterNameParameter;
import java.util.Date;
import java.util.List;
import org.geotools.filter.temporal.EndedByImpl;
import org.opengis.filter.Filter;
import org.opengis.filter.expression.Expression;

/**
 * The Class Ends.
 *
 * @author Robert Ward (SCISYS)
 */
public class EndedBy extends FilterBase implements FilterConfigInterface {

    /** The Class EndByExtended. */
    public class EndByExtended extends EndedByImpl implements FilterExtendedInterface {

        /** Instantiates a new after extended. */
        public EndByExtended() {
            super(null, null);
        }

        /**
         * Instantiates a new after extended.
         *
         * @param expression1 the expression 1
         * @param expression2 the expression 2
         */
        public EndByExtended(Expression expression1, Expression expression2) {
            super(expression1, expression2);
        }

        /*
         * (non-Javadoc)
         *
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            return "[ " + getExpression1() + " EndedBy " + getExpression2() + " ]";
        }

        /*
         * (non-Javadoc)
         *
         * @see com.sldeditor.filter.v2.function.FilterExtendedInterface#getOriginalFilter()
         */
        @Override
        public Class<?> getOriginalFilter() {
            return EndedByImpl.class;
        }
    }

    /** Default constructor. */
    public EndedBy(String category) {
        super(category);
    }

    /**
     * Gets the filter configuration.
     *
     * @return the filter configuration
     */
    @Override
    public FilterName getFilterConfiguration() {
        FilterName filterName = new FilterName("EndedBy", Boolean.class);
        filterName.addParameter(
                new FilterNameParameter("property", ExpressionTypeEnum.PROPERTY, Date.class));
        filterName.addParameter(
                new FilterNameParameter("datetime", ExpressionTypeEnum.LITERAL, Date.class));

        return filterName;
    }

    /**
     * Gets the filter class.
     *
     * @return the filter class
     */
    @Override
    public Class<?> getFilterClass() {
        return EndedByImpl.class;
    }

    /**
     * Creates the filter.
     *
     * @return the filter
     */
    @Override
    public Filter createFilter() {
        return new EndByExtended();
    }

    /**
     * Creates the filter.
     *
     * @param parameterList the parameter list
     * @return the filter
     */
    @Override
    public Filter createFilter(List<Expression> parameterList) {

        EndedByImpl filter = null;

        if ((parameterList == null) || (parameterList.size() != 2)) {
            filter = new EndByExtended();
        } else {
            filter = new EndByExtended(parameterList.get(0), parameterList.get(1));
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
