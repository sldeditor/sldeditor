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

package com.sldeditor.colourramp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sldeditor.colourramp.ramp.ColourRampPanel;
import com.sldeditor.common.xml.ParseXML;
import com.sldeditor.common.xml.ui.ColourRampPresets;

/**
 * A factory for creating ColourRamp objects.
 *
 * @author Robert Ward (SCISYS)
 */
public class ColourRampFactory {

    /** The Constant COLOUR_RAMP_XML. */
    private static final String COLOUR_RAMP_XML = "/colourramp/ColourRamp.xml";

    /** The Constant OUTPUT_SCHEMA_RESOURCE. */
    private static final String OUTPUT_SCHEMA_RESOURCE = "/xsd/colourramp.xsd";

    /** The colour ramp map. */
    private static Map<ColourRampPanelInterface, List<ColourRamp>> colourRampMap = 
            new HashMap<ColourRampPanelInterface, List<ColourRamp>>();

    /**
     * Populate.
     */
    private static void populate() {
        if (colourRampMap.isEmpty()) {
            ColourRampPresets colourRampXML = (ColourRampPresets) ParseXML.parseFile("",
                    ColourRampFactory.COLOUR_RAMP_XML, OUTPUT_SCHEMA_RESOURCE,
                    ColourRampPresets.class);

            ColourRampPanel panel = new ColourRampPanel(colourRampXML.getTwoColourRampList());

            colourRampMap.put(panel, panel.getColourRampList());
        }
    }

    /**
     * Gets the colour ramp map.
     *
     * @return the colourRampMap
     */
    public static Map<ColourRampPanelInterface, List<ColourRamp>> getColourRampMap() {
        populate();

        return colourRampMap;
    }
}
