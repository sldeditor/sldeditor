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

import java.awt.Component;
import java.awt.Graphics;
import javax.swing.Icon;
import javax.swing.SwingConstants;
import javax.swing.plaf.basic.BasicArrowButton;

/**
 * Displays an arrow icon in the menu combo box.
 *
 * @author Robert Ward (SCISYS)
 */
public class ArrowIcon implements Icon, SwingConstants {

    /** The Constant DEFAULT_SIZE. */
    private static final int DEFAULT_SIZE = 11;

    /** The size. */
    private int size;

    /** The icon size. */
    private int iconSize;

    /** The direction. */
    private int direction;

    /** The is enabled. */
    private boolean isEnabled;

    /** The icon renderer. */
    private BasicArrowButton iconRenderer;

    /**
     * Instantiates a new arrow icon.
     *
     * @param direction the direction
     * @param isPressedView the is pressed view
     */
    public ArrowIcon(int direction, boolean isPressedView) {
        this(DEFAULT_SIZE, direction, isPressedView);
    }

    /**
     * Instantiates a new arrow icon.
     *
     * @param iconSize the icon size
     * @param direction the direction
     * @param isEnabled the is enabled
     */
    public ArrowIcon(int iconSize, int direction, boolean isEnabled) {
        this.size = iconSize / 2;
        this.iconSize = iconSize;
        this.direction = direction;
        this.isEnabled = isEnabled;
        iconRenderer = new BasicArrowButton(direction);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.swing.Icon#paintIcon(java.awt.Component, java.awt.Graphics, int, int)
     */
    public void paintIcon(Component c, Graphics g, int x, int y) {
        iconRenderer.paintTriangle(g, x, y, size, direction, isEnabled);
    }

    /**
     * (non-Javadoc)
     *
     * @see javax.swing.Icon#getIconWidth()
     */
    public int getIconWidth() {
        // int retCode;
        switch (direction) {
            case NORTH:
            case SOUTH:
                return iconSize;
            case EAST:
            case WEST:
                return size;
            default:
                break;
        }
        return iconSize;
    }

    /**
     * (non-Javadoc)
     *
     * @see javax.swing.Icon#getIconHeight()
     */
    public int getIconHeight() {
        switch (direction) {
            case NORTH:
            case SOUTH:
                return size;
            case EAST:
            case WEST:
                return iconSize;
            default:
                break;
        }
        return size;
    }
}
