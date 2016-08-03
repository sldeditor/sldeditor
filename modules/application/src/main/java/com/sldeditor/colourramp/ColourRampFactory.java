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

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sldeditor.colourramp.ramp.ColourRampPanel;

/**
 * A factory for creating ColourRamp objects.
 *
 * @author Robert Ward (SCISYS)
 */
public class ColourRampFactory {

    /** The colour ramp map. */
    private static Map<ColourRampPanelInterface, List<ColourRamp>> colourRampMap = new HashMap<ColourRampPanelInterface, List<ColourRamp>>();

    /**
     * Populate.
     */
    private static void populate()
    {
        if(colourRampMap.isEmpty())
        {
            List<ColourRamp> dataList = new ArrayList<ColourRamp>();

            ColourRamp ramp1 = new ColourRamp();
            ramp1.setColourRamp(Color.BLUE, Color.RED);
            dataList.add(ramp1);

            ColourRamp ramp2 = new ColourRamp();
            ramp2.setColourRamp(Color.WHITE, Color.GREEN);
            dataList.add(ramp2);

            ColourRamp ramp3 = new ColourRamp();
            ramp3.setColourRamp(Color.CYAN, Color.RED);
            dataList.add(ramp3);

            ColourRampPanel panel = new ColourRampPanel(dataList);

            colourRampMap.put(panel, dataList);
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
