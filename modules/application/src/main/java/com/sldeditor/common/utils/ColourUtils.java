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
package com.sldeditor.common.utils;

import java.awt.Color;
import java.util.Random;

import org.geotools.styling.SLD;
import org.opengis.filter.expression.Expression;

/**
 * Methods to convert from #rrggbb colours to Java Color objects.
 * 
 * @author Robert Ward (SCISYS)
 */
public class ColourUtils
{

    /** The random number generator. */
    private static Random rand = new Random();

    /**
     * Create a #rrggbb string From colour.
     * <p>Returns null if colour is null.
     *
     * @param colour the colour
     * @return the string
     */
    public static String fromColour(Color colour)
    {
        if(colour == null)
        {
            return null;
        }

        return SLD.toHTMLColor(colour);
    }

    /**
     * Converts colour string to colour.
     *
     * @param htmlColour the html colour
     * @return the colour
     */
    public static Color toColour(String htmlColour)
    {
        Color colour = null;

        if((htmlColour != null) && (htmlColour.length() == 7) && (htmlColour.startsWith("#")))
        {
            colour = SLD.toColor(htmlColour);
        }

        return colour;
    }

    /**
     * Gets the colour as an int value, i.e remove '#' symbol and 
     * convert the remaining hex values as a decimal.
     * <p>Returns 0 if colourExpression is null.
     *
     * @param colourExpression the colour expression
     * @return the int colour value
     */
    public static int getIntColour(Expression colourExpression)
    {
        if(colourExpression == null)
        {
            return 0;
        }

        String tmpColour = colourExpression.toString();

        if(tmpColour.startsWith("#"))
        {
            tmpColour = colourExpression.toString().substring(1);
        }

        int colour = Integer.parseInt(tmpColour, 16);

        return colour;
    }

    /**
     * Creates a random colour.
     *
     * @return the colour
     */
    public static Color createRandomColour() {
        float r = rand.nextFloat();
        float g = rand.nextFloat();
        float b = rand.nextFloat();

        Color randomColor = new Color(r, g, b);

        return randomColor;
    }
}
