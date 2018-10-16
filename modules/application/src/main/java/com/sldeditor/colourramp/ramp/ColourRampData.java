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

package com.sldeditor.colourramp.ramp;

import com.sldeditor.colourramp.ColourRamp;

/**
 * The Class ColourRampData.
 *
 * @author Robert Ward (SCISYS)
 */
public class ColourRampData {

    /** The colour ramp. */
    private ColourRamp colourRamp;

    /** The min value. */
    private int minValue = 0;

    /** The max value. */
    private int maxValue = 0;

    /** The reverse colours flag. */
    private boolean reverseColours = false;

    /** Instantiates a new colour ramp data. */
    public ColourRampData() {
        // Default constructor
    }

    /**
     * Gets the colour ramp.
     *
     * @return the colourRamp
     */
    public ColourRamp getColourRamp() {
        return colourRamp;
    }

    /**
     * Sets the colour ramp.
     *
     * @param colourRamp the colourRamp to set
     */
    public void setColourRamp(ColourRamp colourRamp) {
        this.colourRamp = colourRamp;
    }

    /**
     * Gets the min value.
     *
     * @return the minValue
     */
    public int getMinValue() {
        return minValue;
    }

    /**
     * Sets the min value.
     *
     * @param minValue the minValue to set
     */
    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }

    /**
     * Gets the max value.
     *
     * @return the maxValue
     */
    public int getMaxValue() {
        return maxValue;
    }

    /**
     * Sets the max value.
     *
     * @param maxValue the maxValue to set
     */
    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    /**
     * Checks if is reverse colours.
     *
     * @return the reverseColours
     */
    public boolean reverseColours() {
        return reverseColours;
    }

    /**
     * Sets the reverse colours.
     *
     * @param reverseColours the reverseColours to set
     */
    public void setReverseColours(boolean reverseColours) {
        this.reverseColours = reverseColours;
    }
}
