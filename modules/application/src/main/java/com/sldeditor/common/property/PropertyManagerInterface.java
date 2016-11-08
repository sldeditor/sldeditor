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
package com.sldeditor.common.property;

import java.awt.Color;
import java.util.List;

/**
 * interface for class implementing peoprty manager
 * 
 * @author Robert Ward (SCISYS)
 */
public interface PropertyManagerInterface {

    /**
     * Update property value.
     *
     * @param key the key
     * @param value the value
     */
    void updateValue(String key, String value);

    /**
     * Update property value.
     *
     * @param key the key
     * @param value the value
     */
    void updateValue(String key, boolean value);

    /**
     * Read config.
     */
    void readConfig();

    /**
     * Gets the double value of a property
     *
     * @param field the field
     * @param defaultValue the default value
     * @return the double value
     */
    double getDoubleValue(String field, double defaultValue);

    /**
     * Gets the string value.
     *
     * @param field the field
     * @param defaultValue the default value
     * @return the string value
     */
    String getStringValue(String field, String defaultValue);

    /**
     * Gets the colour value.
     *
     * @param field the field
     * @param defaultValue the default value
     * @return the colour value
     */
    Color getColourValue(String field, Color defaultValue);

    /**
     * Gets the boolean value.
     *
     * @param field the field
     * @param defaultValue the default value
     * @return the string value
     */
    boolean getBooleanValue(String field, boolean defaultValue);

    /**
     * Gets the string list value.
     *
     * @param field the field
     * @return the string value
     */
    List<String> getStringListValue(String field);

    /**
     * Update value.
     *
     * @param key the key
     * @param stringList the string list
     */
    void updateValue(String key, List<String> stringList);

    /**
     * Update value. (Multiple)
     *
     * @param key the key
     * @param count the count
     * @param value the value
     */
    void updateValue(String key, int count, String value);

    /**
     * Gets values where the key is a prefix
     *
     * @param key the key
     * @return the multiple values
     */
    List<String> getMultipleValues(String key);

    /**
     * Update value.
     *
     * @param key the key
     * @param backgroundColour the background colour
     */
    void updateValue(String key, Color backgroundColour);

    /**
     * Clear list value.
     *
     * @param key the key
     */
    void clearValue(String key);

}