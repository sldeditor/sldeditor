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

package com.sldeditor.filter.v2.function.geometry;

import com.sldeditor.filter.v2.expression.ExpressionTypeEnum;
import com.sldeditor.filter.v2.function.FilterBase;
import com.sldeditor.filter.v2.function.FilterConfigInterface;
import com.sldeditor.filter.v2.function.FilterExtendedInterface;
import com.sldeditor.filter.v2.function.FilterName;
import com.sldeditor.filter.v2.function.FilterNameParameter;
import java.util.List;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.spatial.BeyondImpl;
import org.locationtech.jts.geom.Geometry;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;

/**
 * The Class Beyond.
 *
 * @author Robert Ward (SCISYS)
 */
public class Beyond extends FilterBase implements FilterConfigInterface {

    private static FilterFactory ff = CommonFactoryFinder.getFilterFactory();

    /** The Class BeyondExtended. */
    public class BeyondExtended extends BeyondImpl implements FilterExtendedInterface {

        /** Instantiates a new d within extended. */
        public BeyondExtended() {
            super(null, null);
        }

        /**
         * Instantiates a new d within extended.
         *
         * @param expression1 the expression 1
         * @param expression2 the expression 2
         * @param distanceExp the distance exp
         * @param unitsExp the units exp
         */
        public BeyondExtended(
                Expression expression1,
                Expression expression2,
                Expression distanceExp,
                Expression unitsExp) {
            super(expression1, expression2);

            double distance = Double.parseDouble(distanceExp.toString());
            setDistance(distance);
            setUnits(unitsExp.toString());
        }

        /*
         * (non-Javadoc)
         *
         * @see org.geotools.filter.CartesianDistanceFilter#toString()
         */
        public String toString() {
            String operator = " beyond ";

            String distStr = ", distance: " + getDistance();

            org.opengis.filter.expression.Expression leftGeometry = getExpression1();
            org.opengis.filter.expression.Expression rightGeometry = getExpression2();

            if ((leftGeometry == null) && (rightGeometry == null)) {
                return "[ " + "null" + operator + "null" + distStr + " ]";
            } else if (leftGeometry == null) {
                return "[ " + "null" + operator + rightGeometry.toString() + distStr + " ]";
            } else if (rightGeometry == null) {
                return "[ " + leftGeometry.toString() + operator + "null" + distStr + " ]";
            }

            return "[ "
                    + leftGeometry.toString()
                    + operator
                    + rightGeometry.toString()
                    + distStr
                    + " ]";
        }

        /*
         * (non-Javadoc)
         *
         * @see com.sldeditor.filter.v2.function.FilterExtendedInterface#getOriginalFilter()
         */
        @Override
        public Class<?> getOriginalFilter() {
            return BeyondImpl.class;
        }
    }

    /** Default constructor. */
    public Beyond(String category) {
        super(category);
    }

    /**
     * Gets the filter configuration.
     *
     * @return the filter configuration
     */
    @Override
    public FilterName getFilterConfiguration() {
        FilterName filterName = new FilterName("Beyond", Boolean.class);
        filterName.addParameter(
                new FilterNameParameter("property", ExpressionTypeEnum.PROPERTY, Geometry.class));
        filterName.addParameter(
                new FilterNameParameter(
                        "expression", ExpressionTypeEnum.EXPRESSION, Geometry.class));
        filterName.addParameter(
                new FilterNameParameter("distance", ExpressionTypeEnum.EXPRESSION, Double.class));
        filterName.addParameter(
                new FilterNameParameter("units", ExpressionTypeEnum.EXPRESSION, String.class));

        return filterName;
    }

    /**
     * Gets the filter class.
     *
     * @return the filter class
     */
    @Override
    public Class<?> getFilterClass() {
        return BeyondImpl.class;
    }

    /**
     * Creates the filter.
     *
     * @return the filter
     */
    @Override
    public Filter createFilter() {
        return new BeyondExtended();
    }

    /**
     * Creates the filter.
     *
     * @param parameterList the parameter list
     * @return the filter
     */
    @Override
    public Filter createFilter(List<Expression> parameterList) {

        BeyondImpl filter = null;

        if ((parameterList == null)
                || ((parameterList.size() != 2) && (parameterList.size() != 4))) {
            filter = new BeyondExtended();
        } else {

            if (parameterList.size() == 2) {
                parameterList.add(ff.literal(0.0));
                parameterList.add(ff.literal(""));
            }
            filter =
                    new BeyondExtended(
                            parameterList.get(0),
                            parameterList.get(1),
                            parameterList.get(2),
                            parameterList.get(3));
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
