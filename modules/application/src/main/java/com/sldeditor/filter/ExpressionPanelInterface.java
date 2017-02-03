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
package com.sldeditor.filter;

import org.opengis.filter.expression.Expression;

/**
 * The Interface ExpressionPanelInterface.
 *
 * @author Robert Ward (SCISYS)
 */
public interface ExpressionPanelInterface {

    /**
     * Configure the panel.
     *
     * @param title the title
     * @param fieldType the field type
     * @param isRasterSymbol the is raster symbol flag
     */
    public void configure(String title, Class<?> fieldType, boolean isRasterSymbol);

    /**
     * Show dialog.
     *
     * @return true, if Ok button pressed
     */
    boolean showDialog();

    /**
     * Gets the expression string.
     *
     * @return the expression string
     */
    public String getExpressionString();

    /**
     * Gets the expression.
     *
     * @return the expression
     */
    public Expression getExpression();

    /**
     * Populate using string.
     *
     * @param expressionString the expression string
     */
    public void populate(String expressionString);

    /**
     * Populate using expression
     *
     * @param storedExpression the stored expression
     */
    public void populate(Expression storedExpression);
}
