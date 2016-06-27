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

import java.util.HashMap;
import java.util.Map;

import javax.measure.quantity.Length;
import javax.measure.unit.NonSI;
import javax.measure.unit.SI;
import javax.measure.unit.Unit;

/**
 * The Class UnitsOfMeasure handles the units of measurement combo box items
 * <p>Implemented as a singleton.
 * 
 * @author Robert Ward (SCISYS)
 */
public class UnitsOfMeasure {

    /** The instance. */
    private static UnitsOfMeasure instance = null;

    /** The unit map. */
    private Map<String, Unit<Length> > unitMap = new HashMap<String, Unit<Length> >();

    /**
     * Instantiates a new units of measure.
     */
    private UnitsOfMeasure()
    {
        unitMap.put("pixel", NonSI.PIXEL);
        unitMap.put("metre", SI.METRE);
        unitMap.put("foot", NonSI.FOOT);
    }

    /**
     * Gets the single instance of UnitsOfMeasure.
     *
     * @return single instance of UnitsOfMeasure
     */
    public static UnitsOfMeasure getInstance()
    {
        if(instance == null)
        {
            instance = new UnitsOfMeasure();
        }

        return instance;
    }

    /**
     * Convert.
     *
     * @param uom the uom
     * @return the string
     */
    public String convert(Unit<Length> uom) {
        String uomString = "";
        for(String key : unitMap.keySet())
        {
            if(unitMap.get(key) == uom)
            {
                return key;
            }
        }
        return uomString;
    }

    /**
     * Convert.
     *
     * @param uomString the uom string
     * @return the unit
     */
    public Unit<Length> convert(String uomString) {
        Unit<Length> unit = null;

        if(unitMap.containsKey(uomString))
        {
            unit = unitMap.get(uomString);
        }

        return unit;
    }
}
