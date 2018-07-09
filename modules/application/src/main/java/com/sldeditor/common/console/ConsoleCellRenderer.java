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

package com.sldeditor.common.console;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 * The Class ConsoleCellRenderer.
 *
 * @author Robert Ward (SCISYS)
 */
public class ConsoleCellRenderer extends JLabel implements ListCellRenderer<ConsoleData> {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /*
     * (non-Javadoc)
     *
     * @see javax.swing.ListCellRenderer#getListCellRendererComponent(javax.swing.JList, java.lang.Object, int, boolean, boolean)
     */
    @Override
    public Component getListCellRendererComponent(
            JList<? extends ConsoleData> list,
            ConsoleData value,
            int index,
            boolean isSelected,
            boolean cellHasFocus) {
        setText(value.getMessage());

        Color textColour = null;
        switch (value.getType()) {
            case ERROR:
            case EXCEPTION:
                textColour = Color.RED;
                break;
            case INFORMATION:
            default:
                textColour = Color.BLACK;
                break;
        }
        setForeground(textColour);

        return this;
    }
}
