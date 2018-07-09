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
import com.sldeditor.common.xml.ui.GroupIdEnum;

/**
 * The Class ColourFieldConfig.
 *
 * @author Robert Ward (SCISYS)
 */
public class ColourFieldConfig {

    /** The group. */
    private GroupIdEnum group;

    /** The colour field. */
    private FieldIdEnum colour;

    /** The opacity field. */
    private FieldIdEnum opacity;

    /** The stroke width field. */
    private FieldIdEnum width;

    /**
     * Instantiates a new colour field config.
     *
     * @param group the group
     * @param colour the colour
     * @param opacity the opacity
     * @param width the width
     */
    public ColourFieldConfig(
            GroupIdEnum group, FieldIdEnum colour, FieldIdEnum opacity, FieldIdEnum width) {
        super();
        this.group = group;
        this.colour = colour;
        this.opacity = opacity;
        this.width = width;
    }

    /**
     * Gets the colour.
     *
     * @return the colour
     */
    public FieldIdEnum getColour() {
        return colour;
    }

    /**
     * Gets the opacity.
     *
     * @return the opacity
     */
    public FieldIdEnum getOpacity() {
        return opacity;
    }

    /**
     * Gets the width.
     *
     * @return the width
     */
    public FieldIdEnum getWidth() {
        return width;
    }

    /**
     * Gets the group.
     *
     * @return the group
     */
    public GroupIdEnum getGroup() {
        return group;
    }
}
