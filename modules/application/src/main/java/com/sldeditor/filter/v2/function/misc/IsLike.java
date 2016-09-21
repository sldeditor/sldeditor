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

import org.geotools.filter.LikeFilterImpl;
import org.geotools.filter.LiteralExpressionImpl;
import org.opengis.filter.Filter;
import org.opengis.filter.expression.Expression;

import com.sldeditor.filter.v2.expression.ExpressionTypeEnum;
import com.sldeditor.filter.v2.function.FilterConfigInterface;
import com.sldeditor.filter.v2.function.FilterExtendedInterface;
import com.sldeditor.filter.v2.function.FilterName;
import com.sldeditor.filter.v2.function.FilterNameParameter;

/**
 * The Class IsLike.
 *
 * @author Robert Ward (SCISYS)
 */
public class IsLike implements FilterConfigInterface {

    /**
     * The Class IsLikeExtended.
     */
    public class IsLikeExtended extends LikeFilterImpl implements FilterExtendedInterface
    {

        /**
         * Instantiates a new checks if is like extended.
         */
        public IsLikeExtended()
        {
            super(null);
        }

        /**
         * Instantiates a new checks if is like extended.
         *
         * @param expr the expr
         * @param pattern the pattern
         * @param wildcardMulti the wildcard multi
         * @param wildcardSingle the wildcard single
         * @param escape the escape
         * @param matchCase the match case
         */
        public IsLikeExtended(Expression expr, String pattern, String wildcardMulti, String wildcardSingle, String escape, boolean matchCase)
        {
            super(expr, pattern, wildcardMulti, wildcardSingle, escape);
            setMatchCase(matchCase);
        }

        /* (non-Javadoc)
         * @see org.geotools.filter.LikeFilterImpl#toString()
         */
        public String toString() {
            return "[ Like " + getExpression() + " ]";
        }

        /* (non-Javadoc)
         * @see com.sldeditor.filter.v2.function.FilterExtendedInterface#getOriginalFilter()
         */
        @Override
        public Class<?> getOriginalFilter() {
            return LikeFilterImpl.class;
        }
    }

    /**
     * Default constructor.
     */
    public IsLike()
    {
    }

    /**
     * Gets the filter configuration.
     *
     * @return the filter configuration
     */
    @Override
    public FilterName getFilterConfiguration() {
        FilterName filterName = new FilterName("Like", Boolean.class);
        filterName.addParameter(new FilterNameParameter("expression", ExpressionTypeEnum.EXPRESSION, String.class));
        filterName.addParameter(new FilterNameParameter("pattern", ExpressionTypeEnum.LITERAL, String.class));
        filterName.addParameter(new FilterNameParameter("wildcardMulti", ExpressionTypeEnum.LITERAL, String.class));
        filterName.addParameter(new FilterNameParameter("wildcardSingle", ExpressionTypeEnum.LITERAL, String.class));
        filterName.addParameter(new FilterNameParameter("escape", ExpressionTypeEnum.LITERAL, String.class));
        filterName.addParameter(new FilterNameParameter("matchCase", ExpressionTypeEnum.LITERAL, Boolean.class));

        return filterName;
    }

    /**
     * Creates the filter.
     *
     * @return the filter
     */
    @Override
    public Filter createFilter() {
        return new IsLikeExtended();
    }

    /**
     * Gets the filter class.
     *
     * @return the filter class
     */
    @Override
    public Class<?> getFilterClass() {
        return LikeFilterImpl.class;
    }

    /**
     * Creates the filter.
     *
     * @param parameterList the parameter list
     * @return the filter
     */
    @Override
    public Filter createFilter(List<Expression> parameterList) {

        LikeFilterImpl filter = null;
        if((parameterList == null) || parameterList.size() != 6)
        {
            filter = new IsLikeExtended();
        }
        else
        {
            LiteralExpressionImpl pattern = (LiteralExpressionImpl)parameterList.get(1);
            LiteralExpressionImpl wildcardMulti = (LiteralExpressionImpl)parameterList.get(2);
            LiteralExpressionImpl wildcardSingle = (LiteralExpressionImpl)parameterList.get(3);
            LiteralExpressionImpl escape = (LiteralExpressionImpl)parameterList.get(4);
            LiteralExpressionImpl matchCase = (LiteralExpressionImpl)parameterList.get(5);

            filter = new IsLikeExtended(parameterList.get(0),
                    (String)pattern.getValue(),
                    (String)wildcardMulti.getValue(),
                    (String)wildcardSingle.getValue(),
                    (String)escape.getValue(),
                    (Boolean)matchCase.getValue());
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
