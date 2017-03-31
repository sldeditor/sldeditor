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

package com.sldeditor.ui.widgets;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 * The Class ComboBoxRenderer, allows text and images to appear in the combobox.
 *
 * @author Robert Ward (SCISYS)
 */
@SuppressWarnings("rawtypes")
public class ComboBoxRenderer extends JLabel implements ListCellRenderer {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /**
     * Instantiates a new combo box renderer.
     */
    public ComboBoxRenderer() {
        setOpaque(true);
    }

    /**
     * Gets the list cell renderer component.
     * This method finds the image and text corresponding to the selected value and 
     * returns the label, set up to display the text and image.
     *
     * @param list the list
     * @param value the value
     * @param index the index
     * @param isSelected the is selected
     * @param cellHasFocus the cell has focus
     * @return the list cell renderer component
     */
    public Component getListCellRendererComponent(JList list, Object value, int index,
            boolean isSelected, boolean cellHasFocus) {

        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }

        ValueComboBoxData data = (ValueComboBoxData) value;
        if (data != null) {
            if (data.getImageIcon() != null) {
                setIcon(data.getImageIcon());
            } else {
                setText(data.getText());
            }
        }

        return this;
    }
}
