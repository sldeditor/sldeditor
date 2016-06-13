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
package com.sldeditor.ui.attribute;

/**
 * The Class AttributeUtils.
 *
 * @author Robert Ward (SCISYS)
 */
public class AttributeUtils {

    /** The Constant ATTRIBUTE_START. */
    private static final String ATTRIBUTE_START = "<ogc:PropertyName>";

    /** The Constant ATTRIBUTE_END. */
    private static final String ATTRIBUTE_END = "</ogc:PropertyName>";
    
    /**
     * Checks if is string is actually an attribute.
     *
     * @param objValue the obj value
     * @return true, if is attribute
     */
    public static boolean isAttribute(Object objValue) {
        if(objValue instanceof String)
        {
            String stringValue = (String) objValue;
            if(stringValue.startsWith(ATTRIBUTE_START) && stringValue.endsWith(ATTRIBUTE_END))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Extract attribute name from string.
     *
     * @param value the value
     * @return the string
     */
    public static String extract(String value) {
        String extractedValue = value;
        
        if(isAttribute(value))
        {
            extractedValue = value.substring(ATTRIBUTE_START.length(), value.length() - ATTRIBUTE_END.length()); 
        }
        return extractedValue;
    }
}
