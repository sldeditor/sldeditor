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
package com.sldeditor.rendertransformation;

import java.awt.Component;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 * A checkbox renderer used in a table.
 *
 * @author Robert Ward (SCISYS)
 */
public class CheckBoxRenderer extends JCheckBox implements TableCellRenderer {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The table model. */
    private FunctionTableModel tableModel = null;

    /** The label to show when no checkbox. */
    private JLabel label = new JLabel();

    /**
     * Instantiates a new check box renderer.
     *
     * @param tableModel the table model
     */
    CheckBoxRenderer(FunctionTableModel tableModel) {
        this.tableModel = tableModel;
        setHorizontalAlignment(JLabel.CENTER);
    }

    /**
     * Gets the table cell renderer component.
     *
     * @param table the table
     * @param value the value
     * @param isSelected the is selected
     * @param hasFocus the has focus
     * @param row the row
     * @param column the column
     * @return the table cell renderer component
     */
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
            boolean hasFocus, int row, int column) {

        ProcessFunctionParameterValue valueObj = tableModel.getValue(row);

        if (valueObj.optional) {
            if (isSelected) {
                setForeground(table.getSelectionForeground());
                setBackground(table.getSelectionBackground());
            } else {
                setForeground(table.getForeground());
                setBackground(table.getBackground());
            }

            setSelected((value != null && ((Boolean) value).booleanValue()));

            return this;
        }

        if (isSelected) {
            label.setForeground(table.getSelectionForeground());
            label.setBackground(table.getSelectionBackground());
        } else {
            label.setForeground(table.getForeground());
            label.setBackground(table.getBackground());
        }
        return label;
    }
}
