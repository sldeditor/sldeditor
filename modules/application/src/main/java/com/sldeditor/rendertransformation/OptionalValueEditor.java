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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EventObject;
import javax.swing.AbstractCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

/**
 * Table model editor for optional function parameter values.
 *
 * @author Robert Ward (SCISYS)
 */
public class OptionalValueEditor extends AbstractCellEditor implements TableCellEditor {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The check box. */
    private JCheckBox checkBox = new JCheckBox();

    /** The current editor. */
    private Object currentEditor = null;

    /** The table model. */
    private FunctionTableModel tableModel = null;

    /** The selected index. */
    private int selectedIndex = -1;

    /**
     * Instantiates a new value editor.
     *
     * @param tableModel the table model
     */
    public OptionalValueEditor(FunctionTableModel tableModel) {
        this.tableModel = tableModel;

        checkBox.setHorizontalAlignment(JLabel.CENTER);
        checkBox.addActionListener(
                new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        tableModel.setValueAt(
                                checkBox.isSelected(),
                                selectedIndex,
                                FunctionTableModel.COL_OPTIONAL);
                    }
                });
    }

    /**
     * Gets the cell editor value.
     *
     * @return the cell editor value
     */
    @Override
    public Object getCellEditorValue() {
        if (currentEditor == checkBox) {
            return checkBox.isSelected();
        }
        return null;
    }

    /**
     * Checks if is cell editable.
     *
     * @param evt the an event
     * @return true, if is cell editable
     */
    @Override
    public boolean isCellEditable(EventObject evt) {
        return true;
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
    public Component getTableCellEditorComponent(
            JTable table, Object value, boolean isSelected, int row, int column) {

        ProcessFunctionParameterValue currentValue = tableModel.getValue(row);

        if (currentValue.optional) {
            selectedIndex = row;
            currentEditor = checkBox;
            checkBox.setSelected(currentValue.included);
            return checkBox;
        } else {
            selectedIndex = -1;
            currentEditor = null;
        }

        return null;
    }
}
