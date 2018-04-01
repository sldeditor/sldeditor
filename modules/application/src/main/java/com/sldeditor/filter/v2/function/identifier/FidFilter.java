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

package com.sldeditor.filter.v2.function.identifier;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.geotools.filter.FidFilterImpl;
import org.geotools.filter.identity.FeatureIdImpl;
import org.opengis.filter.Filter;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.identity.Identifier;

import com.sldeditor.filter.v2.expression.ExpressionTypeEnum;
import com.sldeditor.filter.v2.function.FilterConfigInterface;
import com.sldeditor.filter.v2.function.FilterExtendedInterface;
import com.sldeditor.filter.v2.function.FilterName;
import com.sldeditor.filter.v2.function.FilterNameParameter;

/**
 * The Class FidFilter.
 *
 * @author Robert Ward (SCISYS)
 */
public class FidFilter implements FilterConfigInterface {

    /**
     * The Class FidFilterExtended.
     */
    public class FidFilterExtended extends FidFilterImpl implements FilterExtendedInterface {

        /**
         * Instantiates a new fid filter extended.
         */
        public FidFilterExtended() {
            super(new HashSet<Identifier>());
        }

        /**
         * Instantiates a new within extended.
         *
         * @param fids the fids
         */
        public FidFilterExtended(Set<Identifier> fids) {
            super(fids);
        }

        /*
         * (non-Javadoc)
         * 
         * @see org.geotools.filter.GeometryFilterImpl#toString()
         */
        public String toString() {
            return String.format("%s", getIDs().toString());
        }

        /*
         * (non-Javadoc)
         * 
         * @see com.sldeditor.filter.v2.function.FilterExtendedInterface#getOriginalFilter()
         */
        @Override
        public Class<?> getOriginalFilter() {
            return FidFilterImpl.class;
        }
    }

    /**
     * Default constructor.
     */
    public FidFilter() {
    }

    /**
     * Gets the filter configuration.
     *
     * @return the filter configuration
     */
    @Override
    public FilterName getFilterConfiguration() {
        FilterName filterName = new FilterName("FidFilter", String.class);
        filterName.addParameter(
                new FilterNameParameter("identifier", ExpressionTypeEnum.LITERAL, String.class));

        return filterName;
    }

    /**
     * Gets the filter class.
     *
     * @return the filter class
     */
    @Override
    public Class<?> getFilterClass() {
        return FidFilterImpl.class;
    }

    /**
     * Creates the filter.
     *
     * @return the filter
     */
    @Override
    public Filter createFilter() {
        return new FidFilterExtended();
    }

    /**
     * Creates the filter.
     *
     * @param parameterList the parameter list
     * @return the filter
     */
    @Override
    public Filter createFilter(List<Expression> parameterList) {

        FidFilterImpl filter = null;

        if ((parameterList == null) || (parameterList.size() == 0)) {
            filter = new FidFilterExtended();
        } else {
            Set<Identifier> fidList = new HashSet<Identifier>();

            for (Expression exp : parameterList) {
                fidList.add(new FeatureIdImpl(exp.toString()));
            }
            filter = new FidFilterExtended(fidList);
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
