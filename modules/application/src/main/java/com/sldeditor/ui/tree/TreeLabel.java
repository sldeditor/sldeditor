/*
 * SLD Editor - The Open Source Java SLD Editor
 *
 * Copyright (C) 2017, SCISYS UK Limited
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

package com.sldeditor.ui.tree;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;

/**
 * The Class TreeLabel.
 *
 * @author Robert Ward (SCISYS)
 */
/**
 * A TreeCellRenderer displays each node of a tree. The default renderer displays arbitrary Object nodes by calling their toString() method. The
 * Component.toString() method returns long strings with extraneous information. Therefore, we use this "wrapper" implementation of
 * TreeCellRenderer to convert nodes from Component objects to useful String values before passing those String values on to the default renderer.
 * 
 * @author Robert Ward (SCISYS)
 */

public class TreeLabel extends JLabel {
    
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The is selected. */
    boolean isSelected;

    /** The has focus. */
    boolean hasFocus;

    /**
     * Instantiates a new tree label.
     */
    public TreeLabel() {
    }

    /* (non-Javadoc)
     * @see javax.swing.JComponent#setBackground(java.awt.Color)
     */
    public void setBackground(Color color) {
        if (color instanceof ColorUIResource)
            color = null;
        super.setBackground(color);
    }

    /* (non-Javadoc)
     * @see javax.swing.JComponent#paint(java.awt.Graphics)
     */
    public void paint(Graphics g) {
        String str;
        if ((str = getText()) != null) {
            if (0 < str.length()) {
                if (isSelected) {
                    g.setColor(UIManager.getColor("Tree.selectionBackground"));
                } else {
                    g.setColor(UIManager.getColor("Tree.textBackground"));
                }
                Dimension d = getPreferredSize();
                int imageOffset = 0;
                Icon currentI = getIcon();
                if (currentI != null) {
                    imageOffset = currentI.getIconWidth() + Math.max(0, getIconTextGap() - 1);
                }
                g.fillRect(imageOffset,  1, d.width - 1 - imageOffset, d.height);
                if (hasFocus) {
                    g.setColor(UIManager.getColor("Tree.selectionBorderColor"));
                    g.drawRect(imageOffset, 1, d.width - 1 - imageOffset, d.height - 1);
                }
            }
        }

        super.paint(g);
    }

    /* (non-Javadoc)
     * @see javax.swing.JComponent#getPreferredSize()
     */
    public Dimension getPreferredSize() {
        Dimension retDimension = super.getPreferredSize();
        if (retDimension != null) {
            retDimension = new Dimension(retDimension.width + 3, retDimension.height);
        }
        return retDimension;
    }

    /**
     * Sets the selected.
     *
     * @param isSelected the new selected
     */
    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    /**
     * Sets the focus.
     *
     * @param hasFocus the new focus
     */
    public void setFocus(boolean hasFocus) {
        this.hasFocus = hasFocus;
    }
};