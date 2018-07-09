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
import java.util.ArrayList;
import java.util.List;
import org.geotools.filter.OrImpl;
import org.opengis.filter.Filter;
import org.opengis.filter.expression.Expression;

/**
 * The Class Or.
 *
 * @author Robert Ward (SCISYS)
 */
public class Or extends FilterBase implements FilterConfigInterface {

    /** The Class OrExtended. */
    public class OrExtended extends OrImpl implements FilterExtendedInterface {

        /** Instantiates a new or extended. */
        public OrExtended() {
            super(new ArrayList<Filter>());
        }

        /**
         * Instantiates a new or extended.
         *
         * @param children the children
         */
        public OrExtended(List<Filter> children) {
            super(children);
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.filter.LogicFilterImpl#toString()
         */
        public String toString() {
            List<Filter> filterList = getChildren();
            List<String> filterStringList = new ArrayList<String>();

            for (Filter f : filterList) {
                filterStringList.add(f.toString());
            }

            return "[" + String.join(" OR ", filterStringList) + "]";
        }

        /*
         * (non-Javadoc)
         *
         * @see com.sldeditor.filter.v2.function.FilterExtendedInterface#getOriginalFilter()
         */
        @Override
        public Class<?> getOriginalFilter() {
            return OrImpl.class;
        }
    }

    /** Default constructor. */
    public Or(String category) {
        super(category);
    }

    /**
     * Gets the filter configuration.
     *
     * @return the filter configuration
     */
    @Override
    public FilterName getFilterConfiguration() {
        FilterName filterName = new FilterName("Or", Boolean.class);
        filterName.addParameter(
                new FilterNameParameter("filter", ExpressionTypeEnum.FILTER, Filter.class));
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
        return OrImpl.class;
    }

    /**
     * Creates the filter.
     *
     * @return the filter
     */
    @Override
    public Filter createFilter() {
        return new OrExtended();
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
        OrImpl filter = null;

        if ((filterList == null) || (filterList.size() < 2)) {
            filter = new OrExtended();
        } else {
            filter = new OrExtended(filterList);
        }

        return filter;
    }
}
