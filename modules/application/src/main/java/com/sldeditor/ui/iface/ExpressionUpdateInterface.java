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
package com.sldeditor.ui.iface;

import org.opengis.filter.expression.Expression;

/**
 * The Interface ExpressionUpdateInterface.
 * 
 * @author Robert Ward (SCISYS)
 */
public interface ExpressionUpdateInterface {

    /**
     * Value updated.
     */
    void valueUpdated();

    /**
     * Attribute updated.
     *
     * @param attributeName the attribute name
     */
    void attributeUpdated(String attributeName);

    /**
     * Expression updated.
     *
     * @param updatedExpression the updated expression
     */
    void expressionUpdated(Expression updatedExpression);

    /**
     * Function updated.
     *
     * @param updatedExpression the updated expression
     */
    void functionUpdated(Expression updatedExpression);
}
