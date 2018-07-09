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

import com.sldeditor.filter.ExpressionPanelFactory;
import com.sldeditor.filter.ExpressionPanelInterface;
import java.awt.Component;
import java.awt.event.MouseEvent;
import java.util.EventObject;
import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

/**
 * Table model editor used to edit expressions.
 *
 * @author Robert Ward (SCISYS)
 */
public class ExpressionEditor extends AbstractCellEditor implements TableCellEditor {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The expression panel. */
    private static ExpressionPanelInterface expressionPanel = null;

    /** The table model. */
    @SuppressWarnings("unused")
    private FunctionTableModel tableModel = null;

    /**
     * Instantiates a new value editor.
     *
     * @param tableModel the table model
     */
    public ExpressionEditor(FunctionTableModel tableModel) {
        this.tableModel = tableModel;

        if (expressionPanel == null) {
            expressionPanel = ExpressionPanelFactory.getExpressionPanel("v2");
        }
    }

    /**
     * Gets the cell editor value.
     *
     * @return the cell editor value
     */
    @Override
    public Object getCellEditorValue() {
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
        if (evt instanceof MouseEvent) {
            return ((MouseEvent) evt).getClickCount() >= 2;
        }
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

        return null;
    }
}
