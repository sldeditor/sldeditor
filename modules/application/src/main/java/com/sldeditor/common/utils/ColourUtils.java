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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.geotools.styling.SLD;
import org.opengis.filter.expression.Expression;

/**
 * Methods to convert from #rrggbb colours to Java Color objects.
 * 
 * @author Robert Ward (SCISYS)
 */
public class ColourUtils {

    /** The Constant HEX_PATTERN. */
    private static final String HEX_PATTERN = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$";

    /** The html colour pattern. */
    private static Pattern htmlColourPattern;

    /** The random number generator. */
    private static Random rand = new Random();

    /**
     * Create a #rrggbb string From colour.
     * 
     * <p>Returns null if colour is null.
     *
     * @param colour the colour
     * @return the string
     */
    public static String fromColour(Color colour) {
        if (colour == null) {
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
    public static Color toColour(String htmlColour) {
        Color colour = null;

        if (validColourString(htmlColour)) {
            colour = SLD.toColor(htmlColour);
        }

        return colour;
    }

    /**
     * Gets the colour as an int value, i.e remove '#' symbol and convert 
     * the remaining hex values as a decimal.
     * 
     * <p>Returns 0 if colourExpression is null.
     *
     * @param colourExpression the colour expression
     * @return the int colour value
     */
    public static int getIntColour(Expression colourExpression) {
        if (colourExpression == null) {
            return 0;
        }

        String tmpColour = colourExpression.toString();

        if (tmpColour.startsWith("#")) {
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

    /**
     * Gets the text colour.
     *
     * @param colour the colour
     * @return the text colour
     */
    public static Color getTextColour(Color colour) {
        // Counting the perceptive luminance - human eye favours green colour...
        double a = 1.0
                - (0.299 * colour.getRed() + 0.587 * colour.getGreen() + 0.114 * colour.getBlue())
                        / 255.0;

        if (a < 0.5) {
            return Color.black;
        } else {
            return Color.white;
        }
    }

    /**
     * Check to if string is a valid html colour.
     *
     * @param htmlColour the html colour
     * @return true, if successful
     */
    public static boolean validColourString(String htmlColour) {
        if (htmlColour == null) {
            return false;
        }

        if (htmlColourPattern == null) {
            htmlColourPattern = Pattern.compile(HEX_PATTERN);
        }

        Matcher matcher = htmlColourPattern.matcher(htmlColour);
        return matcher.matches();
    }
}
