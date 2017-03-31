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

package com.sldeditor.ui.detail.config.colourmap;

import java.util.List;

import org.geotools.styling.ColorMapEntry;
import org.geotools.styling.ColorMapEntryImpl;

/**
 * The Class MultipleColourMapEntry.
 *
 * @author Robert Ward (SCISYS)
 */
public class MultipleColourMapEntry {

    /** The first entry. */
    private ColorMapEntry firstEntry = null;

    /** The label multiple value. */
    private boolean labelMultipleValue = true;

    /** The opacity multiple value. */
    private boolean opacityMultipleValue = true;

    /** The quantity multiple value. */
    private boolean quantityMultipleValue = true;

    /** The colour multiple value. */
    private boolean colourMultipleValue = true;

    /**
     * Parses the list.
     *
     * @param entries the entries
     */
    public void parseList(List<ColorMapEntry> entries) {
        firstEntry = entries.get(0);
        String labelValue = firstEntry.getLabel();
        String opacityValue = firstEntry.getOpacity().toString();
        String quantityValue = firstEntry.getQuantity().toString();
        String colourValue = firstEntry.getColor().toString();

        for (ColorMapEntry entry : entries) {
            if (labelValue.compareTo(entry.getLabel().toString()) != 0) {
                labelMultipleValue = false;
            }
            if (opacityValue.compareTo(entry.getOpacity().toString()) != 0) {
                opacityMultipleValue = false;
            }
            if (quantityValue.compareTo(entry.getQuantity().toString()) != 0) {
                quantityMultipleValue = false;
            }
            if (colourValue.compareTo(entry.getColor().toString()) != 0) {
                colourMultipleValue = false;
            }
        }
    }

    /**
     * Gets the colour map entry.
     *
     * @return the colour map entry
     */
    public ColorMapEntry getColourMapEntry() {
        ColorMapEntry entry = new ColorMapEntryImpl();

        if (firstEntry != null) {
            entry.setLabel(labelMultipleValue ? firstEntry.getLabel() : null);
            entry.setOpacity(opacityMultipleValue ? firstEntry.getOpacity() : null);
            entry.setQuantity(quantityMultipleValue ? firstEntry.getQuantity() : null);
            entry.setColor(colourMultipleValue ? firstEntry.getColor() : null);
        }

        return entry;
    }
}
