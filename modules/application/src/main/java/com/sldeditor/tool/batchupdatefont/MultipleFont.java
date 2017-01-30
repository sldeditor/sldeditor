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

package com.sldeditor.tool.batchupdatefont;

import java.util.ArrayList;
import java.util.List;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.styling.Font;
import org.geotools.styling.StyleFactoryImpl;
import org.opengis.filter.expression.Expression;

/**
 * The Class MultipleFont.
 *
 * @author Robert Ward (SCISYS)
 */
public class MultipleFont {

    /** The first entry. */
    private Font firstEntry = null;

    /** The font name multiple value. */
    private boolean familyMultipleValue = true;

    /** The style multiple value. */
    private boolean styleMultipleValue = true;

    /** The weight multiple value. */
    private boolean weightMultipleValue = true;

    /** The size multiple value. */
    private boolean sizeMultipleValue = true;

    /** The style factory. */
    private static StyleFactoryImpl styleFactory = (StyleFactoryImpl) CommonFactoryFinder
            .getStyleFactory();

    /**
     * Parses the list.
     *
     * @param entries the entries
     */
    public void parseList(List<Font> entries) {
        familyMultipleValue = true;
        styleMultipleValue = true;
        weightMultipleValue = true;
        sizeMultipleValue = true;

        if ((entries == null) || entries.isEmpty()) {
            firstEntry = null;
            familyMultipleValue = false;
            styleMultipleValue = false;
            weightMultipleValue = false;
            sizeMultipleValue = false;
        } else {
            firstEntry = entries.get(0);

            String fontFamilyValue = firstEntry.getFamily().get(0).toString();
            String styleValue = firstEntry.getStyle().toString();
            String weightValue = firstEntry.getWeight().toString();
            String sizeValue = firstEntry.getSize().toString();

            for (Font entry : entries) {
                if (fontFamilyValue.compareTo(entry.getFamily().get(0).toString()) != 0) {
                    familyMultipleValue = false;
                }
                if (styleValue.compareTo(entry.getStyle().toString()) != 0) {
                    styleMultipleValue = false;
                }
                if (weightValue.compareTo(entry.getWeight().toString()) != 0) {
                    weightMultipleValue = false;
                }
                if (sizeValue.compareTo(entry.getSize().toString()) != 0) {
                    sizeMultipleValue = false;
                }
            }
        }
    }

    /**
     * Gets the font.
     *
     * @return the font
     */
    public Font getFont() {

        List<Expression> family = new ArrayList<Expression>();
        Expression style = null;
        Expression weight = null;
        Expression size = null;

        if (firstEntry != null) {
            family = (familyMultipleValue ? firstEntry.getFamily() : family);
            style = (styleMultipleValue ? firstEntry.getStyle() : null);
            weight = (weightMultipleValue ? firstEntry.getWeight() : null);
            size = (sizeMultipleValue ? firstEntry.getSize() : null);
        }

        Font entry = styleFactory.font(family, style, weight, size);
        return entry;
    }
}
