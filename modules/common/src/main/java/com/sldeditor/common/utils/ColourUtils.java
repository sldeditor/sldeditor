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
     * Returns a web browser-friendly HEX value representing the colour in the default sRGB
     * ColorModel.
     * <p>Returns null if r,g,b and inputs are not in the range 0-255.
     *
     * @param r red
     * @param g green
     * @param b blue
     * @return a browser-friendly HEX value
     */
    public static String toHex(int r, int g, int b) {
        if((r >= 0) && (r <= 255) && (g >= 0) && (g <= 255) && (b >= 0) && (b <= 255))
        {
            return "#" + toBrowserHexValue(r) + toBrowserHexValue(g) + toBrowserHexValue(b);
        }
        else
        {
            return null;
        }
    }

    /**
     * To browser hex value.
     *
     * @param number the number
     * @return the string
     */
    private static String toBrowserHexValue(int number) {
        StringBuilder builder = new StringBuilder(Integer.toHexString(number & 0xff));
        while (builder.length() < 2) {
            builder.insert(0, "0");
        }
        return builder.toString().toUpperCase();
    }

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
        String colourString = toHex(colour.getRed(), colour.getGreen(), colour.getBlue());
        return colourString;
    }
    
    /**
     * Converts colour string to colour.
     *
     * @param hexString the hex string
     * @return the colour
     */
    public static Color toColour(String hexString)
    {
        Color colour = null;

        if((hexString != null) && (hexString.length() == 7) && (hexString.startsWith("#")))
        {
            String redSubString = hexString.substring(1, 3);
            int red = Integer.parseInt(redSubString, 16);
            String greeSubString = hexString.substring(3, 5);
            int green = Integer.parseInt(greeSubString, 16);
            String blueSubString = hexString.substring(5, 7);
            int blue = Integer.parseInt(blueSubString, 16);

            colour = new Color(red, green, blue);
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
