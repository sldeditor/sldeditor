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

package com.sldeditor.ui.detail;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.Box;

/**
 * The Class BasePanelPadding.
 *
 * @author Robert Ward (SCISYS)
 */
public class BasePanelPadding {

    /** The Constant PANEL_HEIGHT. */
    private static final int PANEL_HEIGHT = 750;

    /** The padding component. */
    private Component padding = null;

    /** The box. */
    private Box box = null;

    /**
     * Instantiates a new base panel padding.
     *
     * @param box the box
     */
    public BasePanelPadding(Box box) {
        this.box = box;
    }

    /**
     * Adds the padding.
     *
     * @return the dimension
     */
    public Dimension addPadding() {
        Dimension boxSize = null;

        if(box != null)
        {
            boxSize = box.getPreferredSize();
            int paddingSpace = PANEL_HEIGHT - (int)boxSize.getHeight();
            if(paddingSpace > 0)
            {
                padding = Box.createVerticalStrut(paddingSpace);
                box.add(padding);
            }
            else
            {
                padding = null;
                boxSize = new Dimension((int) box.getPreferredSize().getWidth(), PANEL_HEIGHT);
            }
        }
        return boxSize;
    }

    /**
     * Removes the padding.
     */
    public void removePadding()
    {
        if(padding != null)
        {
            box.remove(padding);
        }
    }

    /**
     * Gets the panel height.
     *
     * @return the panelHeight
     */
    public static int getPanelHeight() {
        return PANEL_HEIGHT;
    }
}
