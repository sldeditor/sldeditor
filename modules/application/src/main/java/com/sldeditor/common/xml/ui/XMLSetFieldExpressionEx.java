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
package com.sldeditor.common.xml.ui;

import com.sldeditor.common.xml.TestValueVisitor;
import org.geotools.factory.CommonFactoryFinder;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;

/**
 * The Class XMLSetFieldExpressionEx.
 *
 * @author Robert Ward (SCISYS)
 */
public class XMLSetFieldExpressionEx extends XMLSetFieldExpression
        implements XMLSetFieldLiteralInterface {

    /** The filter factory. */
    private static FilterFactory ff = CommonFactoryFinder.getFilterFactory();

    /**
     * Method to part of the visitor pattern.
     *
     * @param visitor the visitor
     * @param fieldId the field id
     */
    @Override
    public void accept(TestValueVisitor visitor, FieldIdEnum fieldId) {

        Expression expression = ff.property(this.expression);

        visitor.setTestValue(fieldId, expression);
    }
}
