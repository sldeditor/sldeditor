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

import org.geotools.filter.IsBetweenImpl;
import org.opengis.filter.Filter;
import org.opengis.filter.expression.Expression;

import com.sldeditor.filter.v2.expression.ExpressionTypeEnum;
import com.sldeditor.filter.v2.function.FilterConfigInterface;
import com.sldeditor.filter.v2.function.FilterExtendedInterface;
import com.sldeditor.filter.v2.function.FilterName;
import com.sldeditor.filter.v2.function.FilterNameParameter;

/**
 * The Class IsBetween.
 *
 * @author Robert Ward (SCISYS)
 */
public class IsBetween implements FilterConfigInterface {

    /**
     * The Class IsBetweenExtended.
     */
    public class IsBetweenExtended extends IsBetweenImpl implements FilterExtendedInterface
    {
        
        /**
         * Instantiates a new checks if is between extended.
         */
        public IsBetweenExtended()
        {
            super(null, null, null);
        }

        /**
         * Instantiates a new checks if is between extended.
         *
         * @param lower the lower
         * @param expression the expression
         * @param upper the upper
         */
        public IsBetweenExtended(Expression lower, Expression expression, Expression upper)
        {
            super(lower, expression, upper);
        }

        /* (non-Javadoc)
         * @see com.sldeditor.filter.v2.function.FilterExtendedInterface#getOriginalFilter()
         */
        @Override
        public Class<?> getOriginalFilter() {
            return IsBetweenImpl.class;
        }
    }

    /**
     * Default constructor.
     */
    public IsBetween()
    {
    }

    /**
     * Gets the filter configuration.
     *
     * @return the filter configuration
     */
    @Override
    public FilterName getFilterConfiguration() {
        FilterName filterName = new FilterName("PropertyIsBetween", Boolean.class);
        filterName.addParameter(new FilterNameParameter("lower", ExpressionTypeEnum.EXPRESSION, Object.class));
        filterName.addParameter(new FilterNameParameter("property", ExpressionTypeEnum.PROPERTY, Object.class));
        filterName.addParameter(new FilterNameParameter("upper", ExpressionTypeEnum.EXPRESSION, Object.class));

        return filterName;
    }

    /**
     * Creates the filter.
     *
     * @return the filter
     */
    @Override
    public Filter createFilter() {
        return new IsBetweenExtended();
    }

    /**
     * Gets the filter class.
     *
     * @return the filter class
     */
    @Override
    public Class<?> getFilterClass() {
        return IsBetweenImpl.class;
    }

    /**
     * Creates the filter.
     *
     * @param parameterList the parameter list
     * @return the filter
     */
    @Override
    public Filter createFilter(List<Expression> parameterList) {
        IsBetweenImpl filter = null;

        if((parameterList == null) || (parameterList.size() != 3))
        {
            filter = new IsBetweenExtended();
        }
        else
        {
            filter = new IsBetweenExtended(parameterList.get(0), parameterList.get(1), parameterList.get(2));
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
