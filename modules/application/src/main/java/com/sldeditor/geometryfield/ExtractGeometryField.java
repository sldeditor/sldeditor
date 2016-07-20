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

package com.sldeditor.geometryfield;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.LiteralExpressionImpl;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;

import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.ui.detail.config.FieldConfigPopulation;

/**
 * Class to extract the contents of a geometry field and provide an Expression.
 * 
 * @author Robert Ward (SCISYS)
 *
 */
public class ExtractGeometryField {

    /** The filter factory. */
    private static FilterFactory ff = CommonFactoryFinder.getFilterFactory();

    /** The default geometry field. */
    private static Expression defaultGeometryField = ff.property(null);

    /**
     * Gets the geometry field.
     *
     * @param fieldConfigVisitor the field configuration visitor
     * @return the geometry field
     */
    public static Expression getGeometryField(FieldConfigPopulation fieldConfigVisitor) {

        Expression geometryExpression = null;
        if(fieldConfigVisitor != null)
        {
            geometryExpression = fieldConfigVisitor.getExpression(FieldIdEnum.GEOMETRY);
            if(geometryExpression instanceof LiteralExpressionImpl)
            {
                if((geometryExpression == null) || (geometryExpression.toString() == null) || geometryExpression.toString().isEmpty())
                {
                    return defaultGeometryField;
                }

                return geometryExpression;
            }
        }

        return geometryExpression;
    }

}
