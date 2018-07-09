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

package com.sldeditor.ui.legend.option;

import org.geotools.filter.LiteralExpressionImpl;
import org.geotools.styling.Font;

/**
 * The Class FontUtils provides utilities methods for converting fonts.
 *
 * @author Robert Ward (SCISYS)
 */
public class FontUtils {

    /**
     * Converts a org.geotools.styling.Font to java.awt.Font
     *
     * @param font the GeoTools font
     * @return the Java font
     */
    public static java.awt.Font getFont(Font font) {
        LiteralExpressionImpl sizeExpression = ((LiteralExpressionImpl) font.getSize());
        Object obj = sizeExpression.getValue();
        int size = 10;
        if (obj instanceof String) {
            size = Integer.valueOf((String) obj);
        } else if (obj instanceof Double) {
            size = ((Double) obj).intValue();
        } else {
            size = Integer.valueOf(((String) obj).toString());
        }
        int styleMask = java.awt.Font.PLAIN;

        String styleName = font.getStyle().toString();

        if (styleName != null) {
            if (styleName.compareToIgnoreCase("ITALIC") == 0) {
                styleMask |= java.awt.Font.ITALIC;
            }
        }
        String weightName = font.getWeight().toString();
        if (weightName != null) {
            if (weightName.compareToIgnoreCase("BOLD") == 0) {
                styleMask |= java.awt.Font.BOLD;
            }
        }

        String name = font.getFamily().get(0).toString();

        java.awt.Font newFont = new java.awt.Font(name, styleMask, size);

        return newFont;
    }
}
