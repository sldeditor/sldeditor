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

import javax.swing.Box;

import com.sldeditor.ui.detail.config.FieldConfigBase;

/**
 * The Interface MultipleFieldInterface.
 * 
 * @author Robert Ward (SCISYS)
 */
public interface MultipleFieldInterface {

    /**
     * Adds the field.
     *
     * @param parentBox the parent box
     * @param index the index to add the field at, -1 is at the end
     * @param parentField the parent field
     * @param field the field
     */
    void addField(Box parentBox, int index, FieldConfigBase parentField, FieldConfigBase field);

    /**
     * Removes the field.
     *
     * @param parentBox the parent box
     * @param field the field
     */
    void removeField(Box parentBox, FieldConfigBase field);

    /**
     * Refresh panel.
     */
    void refreshPanel();
}
