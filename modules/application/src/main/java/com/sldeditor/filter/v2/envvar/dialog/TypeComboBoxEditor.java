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

package com.sldeditor.filter.v2.envvar.dialog;

import java.awt.Component;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;

/**
 * The Class TypeComboBoxEditor.
 *
 * @author Robert Ward (SCISYS)
 */
public class TypeComboBoxEditor extends DefaultCellEditor {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /**
     * Instantiates a new type combo box editor.
     */
    public TypeComboBoxEditor() {
        super(new JComboBox<Class<?>>());
    }

    /**
     * Gets the table cell editor component.
     *
     * @param table the table
     * @param value the value
     * @param isSelected the is selected
     * @param row the row
     * @param column the column
     * @return the table cell editor component
     */
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected,
            int row, int column) {
        return super.getTableCellEditorComponent(table, value, isSelected, row, column);
    }
}
