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
package com.sldeditor.ui.menucombobox;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.Icon;

/**
 * Class to draw a blank icon in the menu combo box.
 * 
 * @author Robert Ward (SCISYS)
 */
class BlankIcon implements Icon {

    private static final int DEFAULT_SIZE = 11;

    /** The fill colour. */
    private Color fillColour;

    /** The size. */
    private int size;

    /**
     * Instantiates a new blank icon.
     */
    public BlankIcon() {
        this(null, DEFAULT_SIZE);
    }

    /**
     * Instantiates a new blank icon.
     *
     * @param colour the colour
     * @param size the size
     */
    public BlankIcon(Color colour, int size) {
        fillColour = colour;

        this.size = size;
    }

    /* (non-Javadoc)
     * @see javax.swing.Icon#paintIcon(java.awt.Component, java.awt.Graphics, int, int)
     */
    public void paintIcon(Component c, Graphics g, int x, int y) {
        if (fillColour != null) {
            g.setColor(fillColour);
            g.drawRect(x, y, size - 1, size - 1);
        }
    }

    /* (non-Javadoc)
     * @see javax.swing.Icon#getIconWidth()
     */
    public int getIconWidth() {
        return size;
    }

    /* (non-Javadoc)
     * @see javax.swing.Icon#getIconHeight()
     */
    public int getIconHeight() {
        return size;
    } 
}