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

import com.sldeditor.common.xml.ui.XMLColourMapEntry;
import java.util.List;
import org.geotools.styling.ColorMap;
import org.geotools.styling.ColorMapEntry;

/**
 * The Class EncodeColourMap.
 *
 * @author Robert Ward (SCISYS)
 */
public class EncodeColourMap {

    /** The Constant SEPARATOR. */
    private static final String SEPARATOR = ";";

    /** The Constant ENTRY_SEPARATOR. */
    private static final String ENTRY_SEPARATOR = "/";

    /** Private default constructor */
    private EncodeColourMap() {
        // Private default constructor
    }

    /**
     * Encode colour map entries into a string.
     *
     * @param colourMap the colour map
     * @return the string
     */
    public static String encode(ColorMap colourMap) {

        StringBuilder sb = new StringBuilder();

        for (ColorMapEntry entry : colourMap.getColorMapEntries()) {
            sb.append((entry.getLabel() == null) ? "" : entry.getLabel());
            sb.append(SEPARATOR);
            sb.append(entry.getColor());
            sb.append(SEPARATOR);
            sb.append(Double.valueOf(entry.getOpacity().toString()));
            sb.append(SEPARATOR);
            sb.append(entry.getQuantity());
            sb.append(ENTRY_SEPARATOR);
        }

        return sb.toString();
    }

    /**
     * Encode test colour map entries into a string.
     *
     * @param colourMap the colour map
     * @return the string
     */
    public static String encode(List<XMLColourMapEntry> colourMap) {
        StringBuilder sb = new StringBuilder();

        for (XMLColourMapEntry entry : colourMap) {
            sb.append((entry.getLabel() == null) ? "" : entry.getLabel());
            sb.append(SEPARATOR);
            sb.append(entry.getColour());
            sb.append(SEPARATOR);
            sb.append(entry.getOpacity());
            sb.append(SEPARATOR);
            sb.append(entry.getQuantity());
            sb.append(ENTRY_SEPARATOR);
        }

        return sb.toString();
    }
}
