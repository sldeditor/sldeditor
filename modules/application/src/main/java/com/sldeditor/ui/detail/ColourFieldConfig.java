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

import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.ui.detail.config.FieldId;

/**
 * The Class ColourFieldConfig.
 *
 * @author Robert Ward (SCISYS)
 */
public class ColourFieldConfig {

    /** The colour field. */
    private FieldId colour;
    
    /** The opacity field. */
    private FieldId opacity;
    
    /** The stroke width field. */
    private FieldId width;

    /**
     * Instantiates a new colour field config.
     *
     * @param colour the colour
     * @param opacity the opacity
     * @param width the width
     */
    public ColourFieldConfig(FieldIdEnum colour, FieldIdEnum opacity, FieldIdEnum width) {
        super();
        this.colour = new FieldId(colour);
        this.opacity = new FieldId(opacity);
        this.width = new FieldId(width);
    }

    /**
     * Gets the colour.
     *
     * @return the colour
     */
    public FieldId getColour() {
        return colour;
    }

    /**
     * Gets the opacity.
     *
     * @return the opacity
     */
    public FieldId getOpacity() {
        return opacity;
    }

    /**
     * Gets the width.
     *
     * @return the width
     */
    public FieldId getWidth() {
        return width;
    }
}
