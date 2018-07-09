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

package com.sldeditor.datasource.config;

import java.awt.Component;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 * Draws JCheckboxes in JTable cells.
 *
 * @author Robert Ward (SCISYS)
 */
public class ComboBoxCellRenderer implements TableCellRenderer {

    /** The combo. */
    JComboBox<String> combo;

    /**
     * Instantiates a new check box cell renderer.
     *
     * @param comboBox the combo box
     */
    public ComboBoxCellRenderer(JComboBox<String> comboBox) {
        this.combo = new JComboBox<String>();
        for (int i = 0; i < comboBox.getItemCount(); i++) {
            combo.addItem(comboBox.getItemAt(i));
        }
    }

    /**
     * (non-Javadoc)
     *
     * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable,
     *     java.lang.Object, boolean, boolean, int, int)
     */
    @Override
    public Component getTableCellRendererComponent(
            JTable jtable,
            Object value,
            boolean isSelected,
            boolean hasFocus,
            int row,
            int column) {
        combo.setSelectedItem(value);
        return combo;
    }
}
