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

package com.sldeditor.ui.detail;

import org.geotools.filter.ConstantExpression;
import org.geotools.filter.LiteralExpressionImpl;
import org.geotools.styling.AnchorPoint;
import org.geotools.styling.Displacement;
import org.opengis.filter.expression.Expression;

/**
 * The Class DetailsUtilities provides utility methods to check if the following objects are the
 * same:
 *
 * <p>- Displacement
 *
 * <p>- Anchor point
 *
 * <p>GeoTools objects can't compare expressions if are of different types even though the values
 * may be the same.
 *
 * @author Robert Ward (SCISYS)
 */
public class DetailsUtilities {

    /** Default private constructor */
    private DetailsUtilities() {
        // Default private constructor
    }

    /**
     * Checks if displacement objects are the same.
     *
     * @param defaultDisplacement the default displacement
     * @param displacement the displacement
     * @return true, if is same
     */
    public static boolean isSame(Displacement defaultDisplacement, Displacement displacement) {
        if ((defaultDisplacement != null) && (displacement != null)) {
            return areDoubleValuesSame(
                            defaultDisplacement.getDisplacementX(), displacement.getDisplacementX())
                    && areDoubleValuesSame(
                            defaultDisplacement.getDisplacementY(),
                            displacement.getDisplacementY());
        }
        return false;
    }

    /**
     * Checks if anchor point objects are the same.
     *
     * @param defaultAnchorPoint the default anchor point
     * @param anchorPoint the anchor point
     * @return true, if is same
     */
    public static boolean isSame(AnchorPoint defaultAnchorPoint, AnchorPoint anchorPoint) {
        if ((defaultAnchorPoint != null) && (anchorPoint != null)) {
            return areDoubleValuesSame(
                            defaultAnchorPoint.getAnchorPointX(), anchorPoint.getAnchorPointX())
                    && areDoubleValuesSame(
                            defaultAnchorPoint.getAnchorPointY(), anchorPoint.getAnchorPointY());
        }
        return false;
    }

    /**
     * Checks if expression values are the same same.
     *
     * @param expression1 the expression 1
     * @param expression2 the expression 2
     * @return true, if is same
     */
    private static boolean areDoubleValuesSame(Expression expression1, Expression expression2) {
        double value1 = getValue(expression1);
        double value2 = getValue(expression2);

        return (Math.abs(value1 - value2) < 0.001);
    }

    /**
     * Gets the value from an expression.
     *
     * @param expression the expression
     * @return the value
     */
    private static double getValue(Expression expression) {
        Object objValue = null;
        if (expression instanceof ConstantExpression) {
            objValue = ((ConstantExpression) expression).getValue();
        } else if (expression instanceof LiteralExpressionImpl) {
            objValue = ((LiteralExpressionImpl) expression).getValue();
        }

        if (objValue instanceof Double) {
            return ((Double) objValue).doubleValue();
        } else if (objValue instanceof Long) {
            return ((Long) objValue).doubleValue();
        } else if (objValue instanceof Integer) {
            return ((Integer) objValue).doubleValue();
        } else if (objValue instanceof String) {
            return Double.parseDouble((String) objValue);
        }

        return 0.0;
    }
}
