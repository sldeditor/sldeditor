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
package com.sldeditor.exportdata.esri.symbols;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.styling.StyleFactoryImpl;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sldeditor.common.utils.ColourUtils;
import com.sldeditor.exportdata.esri.keys.symbols.ColourKeys;

/**
 * Base class for symbol objects.
 * 
 * @author Robert Ward (SCISYS)
 */
public class BaseSymbol {

    /** The style factory. */
    protected static StyleFactoryImpl styleFactory = (StyleFactoryImpl) CommonFactoryFinder.getStyleFactory();

    /** The filter factory. */
    protected static FilterFactory ff = CommonFactoryFinder.getFilterFactory();

    /**
     * Instantiates a new base symbol.
     */
    public BaseSymbol() {
        super();
    }

    /**
     * Gets the value as a double.
     *
     * @param obj the obj
     * @param field the field
     * @return the double
     */
    protected static double getDouble(JsonObject obj, String field) {
        double value = 0.0;

        if(obj != null)
        {
            JsonElement element = obj.get(field);

            if(element != null)
            {
                value = element.getAsDouble();
            }
        }
        return value;
    }

    /**
     * Normalise angle.
     *
     * @param value the value
     * @return the double
     */
    protected static double normaliseAngle(double value)
    {
        double angle = value;

        while(angle < 0.0)
        {
            angle += 360.0;
        }

        while(angle > 360.0)
        {
            angle -= 360.0;
        }

        return angle;
    }

    /**
     * Check if data exists.
     *
     * @param obj the obj
     * @param field the field
     * @return true, if successful
     */
    protected static boolean exists(JsonObject obj, String field) {
        JsonElement element = obj.get(field);

        return (element != null);
    }

    /**
     * Gets the boolean value.
     *
     * @param obj the obj
     * @param field the field
     * @return the boolean value
     */
    protected static boolean getBoolean(JsonObject obj, String field) {
        boolean value = false;

        if(obj != null)
        {
            JsonElement element = obj.get(field);

            if(element != null)
            {
                value = element.getAsBoolean();
            }
        }
        return value;
    }

    /**
     * Gets the value as a string.
     *
     * @param obj the obj
     * @param field the field
     * @return the string
     */
    protected static String getString(JsonObject obj, String field) {
        String value = "";

        if(obj != null)
        {
            JsonElement element = obj.get(field);

            if(element != null)
            {
                value = element.getAsString();
            }
        }
        return value;
    }

    /**
     * Gets the value as an integer.
     *
     * @param obj the obj
     * @param field the field
     * @return the double
     */
    protected static int getInt(JsonObject obj, String field) {
        int value = 0;

        if(obj != null)
        {
            JsonElement element = obj.get(field);

            if(element != null)
            {
                value = element.getAsInt();
            }
        }
        return value;
    }

    /**
     * Gets the transparency.
     *
     * @param transparency the transparency
     * @return the transparency
     */
    public static Expression getTransparency(int transparency) {
        if(transparency == 0)
        {
            return null;
        }
        
        double convertedValue = 1.0 - (transparency / 100.0);
        return ff.literal(convertedValue);
    }

    /**
     * Gets the colour.
     *
     * @param element the element
     * @return the colour
     */
    public static Expression getColour(JsonElement element)
    {
        if(element == null) return null;

        JsonObject obj = element.getAsJsonObject();

        int red = obj.get(ColourKeys.RED).getAsInt();
        int green = obj.get(ColourKeys.GREEN).getAsInt();
        int blue = obj.get(ColourKeys.BLUE).getAsInt();

        String colourString = ColourUtils.toHex(red, green, blue);

        return ff.literal(colourString); 
    }
}