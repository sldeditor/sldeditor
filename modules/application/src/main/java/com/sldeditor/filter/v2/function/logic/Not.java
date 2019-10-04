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

package com.sldeditor.filter.v2.function.logic;

import com.sldeditor.filter.v2.expression.ExpressionTypeEnum;
import com.sldeditor.filter.v2.function.FilterBase;
import com.sldeditor.filter.v2.function.FilterConfigInterface;
import com.sldeditor.filter.v2.function.FilterExtendedInterface;
import com.sldeditor.filter.v2.function.FilterName;
import com.sldeditor.filter.v2.function.FilterNameParameter;
import java.util.List;
import org.geotools.filter.NotImpl;
import org.opengis.filter.Filter;
import org.opengis.filter.expression.Expression;

/**
 * The Class Not.
 *
 * @author Robert Ward (SCISYS)
 */
public class Not extends FilterBase implements FilterConfigInterface {

    /** The Class NotExtended. */
    public class NotExtended extends NotImpl implements FilterExtendedInterface {

        /** Instantiates a new not extended. */
        @SuppressWarnings("deprecation")
        public NotExtended() {
            super(null);
        }

        /**
         * Instantiates a new not extended.
         *
         * @param filter the filter
         */
        public NotExtended(Filter filter) {
            super(filter);
        }

        /*
         * (non-Javadoc)
         *
         * @see com.sldeditor.filter.v2.function.FilterExtendedInterface#getOriginalFilter()
         */
        @Override
        public Class<?> getOriginalFilter() {
            return NotImpl.class;
        }
    }

    /** Default constructor. */
    public Not(String category) {
        super(category);
    }

    /**
     * Gets the filter configuration.
     *
     * @return the filter configuration
     */
    @Override
    public FilterName getFilterConfiguration() {
        FilterName filterName = new FilterName("Not", Boolean.class);
        filterName.addParameter(
                new FilterNameParameter("filter", ExpressionTypeEnum.FILTER, Filter.class));

        return filterName;
    }

    /**
     * Gets the filter class.
     *
     * @return the filter class
     */
    @Override
    public Class<?> getFilterClass() {
        return NotImpl.class;
    }

    /**
     * Creates the filter.
     *
     * @return the filter
     */
    @Override
    public Filter createFilter() {
        return new NotExtended();
    }

    /**
     * Creates the filter.
     *
     * @param parameterList the parameter list
     * @return the filter
     */
    @Override
    public Filter createFilter(List<Expression> parameterList) {

        return null;
    }

    /**
     * Creates the logic filter.
     *
     * @param filterList the filter list
     * @return the filter
     */
    @Override
    public Filter createLogicFilter(List<Filter> filterList) {
        NotImpl filter = null;
        if ((filterList == null) || filterList.isEmpty()) {
            filter = new NotExtended();
        } else {
            filter = new NotExtended(filterList.get(0));
        }
        return filter;
    }
}
