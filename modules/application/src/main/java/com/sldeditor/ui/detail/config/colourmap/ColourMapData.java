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

import com.sldeditor.common.utils.ColourUtils;

/**
 * The Class ColourMapData.
 *
 * @author Robert Ward (SCISYS)
 */
public class ColourMapData {

    /** The colour. */
    private Color colour;

    /** The colour string. */
    private String colourString;

    /** The opacity. */
    private double opacity = 1.0;

    /** The quantity. */
    private int quantity = -1;

    /** The label. */
    private String label;

    /**
     * Instantiates a new colour map data.
     */
    public ColourMapData()
    {

    }

    /**
     * Gets the colour.
     *
     * @return the colour
     */
    public Color getColour() {
        return colour;
    }

    /**
     * Sets the colour.
     *
     * @param colour the colour to set
     */
    public void setColour(Color colour) {
        this.colour = colour;
        colourString = ColourUtils.fromColour(colour);
    }

    /**
     * Gets the quantity.
     *
     * @return the quantity
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Sets the quantity.
     *
     * @param quantity the quantity to set
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * Gets the colour string.
     *
     * @return the colourString
     */
    public String getColourString() {
        return colourString;
    }

    /**
     * Gets the opacity.
     *
     * @return the opacity
     */
    public double getOpacity() {
        return opacity;
    }

    /**
     * Sets the opacity.
     *
     * @param opacity the opacity to set
     */
    public void setOpacity(double opacity) {
        this.opacity = opacity;
    }

    /**
     * Gets the label.
     *
     * @return the label
     */
    public String getLabel() {
        return label;
    }

    /**
     * Sets the label.
     *
     * @param label the new label
     */
    public void setLabel(String label) {
        this.label = label;
    }
}
