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

import java.text.NumberFormat;

import org.opengis.style.Rule;

/**
 * The Class ScaleUtil contains methods to handle map scales in strings.
 * 
 * @author Robert Ward (SCISYS)
 */
public class ScaleUtil {

    /** The Constant SCALE_PREFIX. */
    private static final String SCALE_PREFIX = "1:";

    /**
     * Gets the value.
     *
     * @param value the value
     * @return the value
     */
    public static String getValue(double value)
    {
        if(isNotSet(value))
        {
            return "";
        }

        return format(value);
    }

    /**
     * Checks if double value is not set.
     *
     * @param value the value to check
     * @return true, if value is not zero or +/- infinity
     */
    public static boolean isNotSet(double value) {
        return Double.isInfinite(value) || (Math.abs(value) < 0.001);
    }

    /**
     * Format a map scale into a string
     *
     * @param value the value
     * @return the string
     */
    private static String format(double value)
    {
        return String.format("%s%s", SCALE_PREFIX, NumberFormat.getIntegerInstance().format((int)value));
    }

    /**
     * Checks if rule has minimum or maximum scale denominator set.
     * Returns false if rule is null.
     *
     * @param rule the rule
     * @return true, if minimum or maximum scale denominator set
     */
    public static boolean isPresent(Rule rule) {
        if(rule == null)
        {
            return false;
        }

        return (!isNotSet(rule.getMinScaleDenominator()) || !isNotSet(rule.getMaxScaleDenominator()));
    }

    /**
     * Extract scale value from string.
     *
     * @param stringValue the scale string value
     * @return the scale expressed as a double
     */
    public static double extractValue(String stringValue)
    {
        double value = 0.0;

        if(stringValue != null)
        {
            String sValue = stringValue.replace(",", "").replace(" ", "").trim();
            try
            {
                if(sValue.startsWith(SCALE_PREFIX))
                {
                    String substring = sValue.substring(SCALE_PREFIX.length());
                    value = Double.valueOf(substring);
                }
                else
                {
                    // Try and decode anyway
                    value = Double.valueOf(sValue);
                }
            }
            catch(NumberFormatException e)
            {

            }
        }
        return value;
    }
}
