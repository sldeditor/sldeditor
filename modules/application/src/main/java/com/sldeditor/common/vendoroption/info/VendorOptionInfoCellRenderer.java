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
package com.sldeditor.common.vendoroption.info;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * Table cell editor that allows the viewing of vendor option info data.
 * 
 * @author Robert Ward (SCISYS)
 */
public class VendorOptionInfoCellRenderer extends DefaultTableCellRenderer {

    /** The Constant NOT_AVAILABLE_COLOUR. */
    private static final Color NOT_AVAILABLE_COLOUR = Color.LIGHT_GRAY;

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The model. */
    private VendorOptionInfoModel model = null;

    /**
     * Instantiates a new vendor option info cell renderer.
     *
     * @param model the model
     */
    public VendorOptionInfoCellRenderer(VendorOptionInfoModel model) {
        this.model = model;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.DefaultTableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
     */
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
            boolean hasFocus, int row, int column) {
        setText((String) value);

        boolean available = true;

        if (this.model != null) {
            available = model.isVendorOptionAvailable(row);
        }

        if (isSelected) {
            setBackground(table.getSelectionBackground());
        } else {
            setBackground(table.getSelectionForeground());
        }

        if (available) {
            setForeground(table.getForeground());
        } else {
            setForeground(NOT_AVAILABLE_COLOUR);
        }
        return this;
    }

}