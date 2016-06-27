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

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

/**
 * The Class ColourEditor, allows a colour to be edited from a table cell.
 *
 * @author Robert Ward (SCISYS)
 */
public class ColourEditor extends AbstractCellEditor implements TableCellEditor {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The current colour. */
    private Color currentColour;

    /** The button. */
    private JButton button;

    /** The colour map model. */
    private ColourMapModel colourMapModel = null;

    /**
     * Instantiates a new colour editor.
     *
     * @param colourMapModel the colour map model
     */
    public ColourEditor(ColourMapModel colourMapModel) {
        this.colourMapModel = colourMapModel;

        button = new JButton();
        button.setBorderPainted(false);
        button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                
                button.setBackground(currentColour);

                // Set up the dialog that the button brings up.
                Color newColour = JColorChooser.showDialog(button, "Pick color", currentColour);

                if(newColour != null)
                {
                    currentColour = newColour;
                }

                // Make the renderer reappear.
                fireEditingStopped();
            }});
    }

    /* (non-Javadoc)
     * @see javax.swing.CellEditor#getCellEditorValue()
     */
    public Object getCellEditorValue() {
        return currentColour;
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableCellEditor#getTableCellEditorComponent(javax.swing.JTable, java.lang.Object, boolean, int, int)
     */
    public Component getTableCellEditorComponent(JTable table,
            Object value,
            boolean isSelected,
            int row,
            int column) {
        if(colourMapModel != null)
        {
            currentColour = colourMapModel.getColour(row);
        }
        return button;
    }
}
