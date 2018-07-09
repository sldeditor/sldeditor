/*
 * SLD Editor - The Open Source Java SLD Editor
 *
 * Copyright (C) 2017, SCISYS UK Limited
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

package com.sldeditor.rendertransformation.types;

import com.sldeditor.ui.detail.config.FieldConfigBase;
import com.sldeditor.ui.detail.config.FieldConfigCommonData;
import java.util.List;
import org.opengis.filter.expression.Expression;

/**
 * The Interface RenderTransformValueInterface.
 *
 * @author Robert Ward (SCISYS)
 */
public interface RenderTransformValueInterface {

    /**
     * Gets the type.
     *
     * @return the type
     */
    List<Class<?>> getType();

    /**
     * Sets the default value.
     *
     * @param defaultValue the new default value
     */
    void setDefaultValue(Object defaultValue);

    /**
     * Gets the expression.
     *
     * @return the expression
     */
    Expression getExpression();

    /**
     * Sets the value.
     *
     * @param aValue the new value
     */
    void setValue(Object aValue);

    /**
     * Gets the field for the data type.
     *
     * @param commonData the common data
     * @return the field
     */
    FieldConfigBase getField(FieldConfigCommonData commonData);

    /**
     * Creates a new instance.
     *
     * @return the render transform value interface
     */
    RenderTransformValueInterface createInstance();
}
