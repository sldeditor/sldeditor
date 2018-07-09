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

package com.sldeditor.ui.detail.config.colourmap;

import com.sldeditor.common.utils.ColourUtils;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * The Class ColourTableCellRenderer.
 *
 * @author Robert Ward (SCISYS)
 */
public class ColourTableCellRenderer extends DefaultTableCellRenderer {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The colour map model. */
    private ColourMapModel colourMapModel = null;

    /**
     * Instantiates a new colour table cell renderer.
     *
     * @param colourMapModel the colour map model
     */
    public ColourTableCellRenderer(ColourMapModel colourMapModel) {
        this.colourMapModel = colourMapModel;
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
    @Override
    public Component getTableCellRendererComponent(
            JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Color colour = colourMapModel.getColour(row);

        JLabel label =
                (JLabel)
                        super.getTableCellRendererComponent(
                                table, value, isSelected, hasFocus, row, column);

        label.setBackground(colour);
        label.setForeground(ColourUtils.getTextColour(colour));

        return label;
    }
}
