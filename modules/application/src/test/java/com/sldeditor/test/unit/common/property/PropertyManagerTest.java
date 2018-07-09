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

package com.sldeditor.test.unit.common.property;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.sldeditor.common.property.PropertyManager;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

/**
 * Unit test for PropertyManager.
 *
 * <p>{@link com.sldeditor.common.property.PropertyManager}
 *
 * @author Robert Ward (SCISYS)
 */
public class PropertyManagerTest {

    /**
     * Test method for {@link
     * com.sldeditor.common.property.PropertyManager#updateValue(java.lang.String,
     * java.lang.String)}.
     */
    @Test
    public void testUpdateValueString() {
        PropertyManager propertyManager = new PropertyManager();
        propertyManager.setPropertyFile(null);
        String stringKey = "test string key";
        String stringValue = "string test value";
        propertyManager.updateValue(stringKey, stringValue);

        String defaultStringValue = "default string value";
        String actualStringResult = propertyManager.getStringValue(stringKey, defaultStringValue);
        assertEquals(stringValue, actualStringResult);

        String newStringKey = "test string key 2";

        actualStringResult = propertyManager.getStringValue(newStringKey, defaultStringValue);
        assertEquals(defaultStringValue, actualStringResult);
    }

    /**
     * Test method for {@link
     * com.sldeditor.common.property.PropertyManager#updateValue(java.lang.String, boolean)}.
     */
    @Test
    public void testUpdateValueStringBoolean() {
        PropertyManager propertyManager = new PropertyManager();
        propertyManager.setPropertyFile(null);
        String key = "test boolean key";
        boolean value = true;
        propertyManager.updateValue(key, value);

        boolean defaultValue = false;
        boolean actualResult = propertyManager.getBooleanValue(key, defaultValue);
        assertEquals(value, actualResult);

        String newKey = "test boolean key 2";

        actualResult = propertyManager.getBooleanValue(newKey, defaultValue);
        assertEquals(defaultValue, actualResult);
    }

    /** Test method for {@link com.sldeditor.common.property.PropertyManager#readConfig()}. */
    @Test
    public void testReadConfig() {
        PropertyManager nullPropertyManager = new PropertyManager();
        nullPropertyManager.setPropertyFile(null);
        nullPropertyManager.readConfig();

        File file = null;
        try {
            file = File.createTempFile(getClass().getSimpleName(), ".properties");
        } catch (IOException e) {
            e.printStackTrace();
            fail("Failed to create test property temporary file");
        }
        PropertyManager propertyManager = new PropertyManager();
        propertyManager.setPropertyFile(file);

        String stringKey = "string key";
        String stringValue = "test string value";
        propertyManager.updateValue(stringKey, stringValue);

        String colourKey = "colour key";
        Color colourValue = Color.DARK_GRAY;
        propertyManager.updateValue(colourKey, colourValue);

        String booleanKey = "boolean key";
        boolean booleanValue = true;
        propertyManager.updateValue(booleanKey, booleanValue);

        List<String> stringList = new ArrayList<String>();
        stringList.add("item 1");
        stringList.add("item 2");
        stringList.add("item 3");
        String stringListKey = "string list key";
        propertyManager.updateValue(stringListKey, stringList);

        String stringMultipleKey = "multiple string key";
        int index = 0;
        for (String value : stringList) {
            propertyManager.updateValue(stringMultipleKey, index, value);
            index++;
        }

        PropertyManager testPropertyManager = new PropertyManager();
        testPropertyManager.setPropertyFile(file);
        testPropertyManager.readConfig();

        Color actualColourResult = testPropertyManager.getColourValue(colourKey, Color.black);
        assertEquals(colourValue, actualColourResult);

        boolean actualBooleanResult = testPropertyManager.getBooleanValue(booleanKey, false);
        assertEquals(booleanValue, actualBooleanResult);

        String actualStringResult = testPropertyManager.getStringValue(stringKey, "");
        assertEquals(stringValue, actualStringResult);

        List<String> actualStringList = testPropertyManager.getStringListValue(stringListKey);
        assertEquals(stringList, actualStringList);

        actualStringList = testPropertyManager.getMultipleValues(stringMultipleKey);
        assertEquals(stringList, actualStringList);

        file.delete();
    }

    /**
     * Test method for {@link
     * com.sldeditor.common.property.PropertyManager#getDoubleValue(java.lang.String, double)}.
     */
    @Test
    public void testGetDoubleValue() {
        PropertyManager propertyManager = new PropertyManager();
        propertyManager.setPropertyFile(null);
        String stringKey = "test double key";
        double expectedValue = 42.0;
        String stringValue = Double.toString(expectedValue);
        propertyManager.updateValue(stringKey, stringValue);

        double defaultValue = 0.0;
        double actualResult = propertyManager.getDoubleValue(stringKey, defaultValue);
        assertTrue(Math.abs(actualResult - expectedValue) < 0.001);

        String newKey = "test double key 2";

        actualResult = propertyManager.getDoubleValue(newKey, defaultValue);
        assertTrue(Math.abs(actualResult - defaultValue) < 0.001);

        propertyManager.updateValue(stringKey, "not a double");
        actualResult = propertyManager.getDoubleValue(stringKey, defaultValue);
        assertTrue(Math.abs(actualResult - defaultValue) < 0.001);
    }

    /**
     * Test method for {@link
     * com.sldeditor.common.property.PropertyManager#updateValue(java.lang.String, java.util.List)}.
     */
    @Test
    public void testUpdateValueStringListOfString() {
        PropertyManager propertyManager = new PropertyManager();
        propertyManager.setPropertyFile(null);
        List<String> stringList = new ArrayList<String>();
        stringList.add("item 1");
        stringList.add("item 2");
        stringList.add("item 3");

        String stringKey = "test string list key";
        propertyManager.updateValue(stringKey, stringList);

        List<String> actualStringResult = propertyManager.getStringListValue(stringKey);
        assertEquals(stringList, actualStringResult);

        String newStringKey = "test string key 2";

        actualStringResult = propertyManager.getStringListValue(newStringKey);
        assertNull(actualStringResult);
    }

    /**
     * Test method for {@link
     * com.sldeditor.common.property.PropertyManager#updateValue(java.lang.String, int,
     * java.lang.String)}.
     */
    @Test
    public void testUpdateValueStringIntString() {
        PropertyManager propertyManager = new PropertyManager();
        propertyManager.setPropertyFile(null);
        String stringKey = "test string int list key";
        List<String> stringList = new ArrayList<String>();
        stringList.add("item 1");
        stringList.add("item 2");
        stringList.add("item 3");

        int index = 0;
        for (String value : stringList) {
            propertyManager.updateValue(stringKey, index, value);
            index++;
        }

        List<String> actualStringResult = propertyManager.getMultipleValues(stringKey);
        assertEquals(stringList, actualStringResult);

        String newStringKey = "test string key 2";

        actualStringResult = propertyManager.getStringListValue(newStringKey);
        assertNull(actualStringResult);
    }

    /**
     * Test method for {@link
     * com.sldeditor.common.property.PropertyManager#updateValue(java.lang.String, java.awt.Color)}.
     */
    @Test
    public void testUpdateValueStringColor() {
        PropertyManager propertyManager = new PropertyManager();
        propertyManager.setPropertyFile(null);
        String key = "test colour key";
        Color value = Color.CYAN;
        propertyManager.updateValue(key, value);

        Color defaultValue = Color.MAGENTA;
        Color actualResult = propertyManager.getColourValue(key, defaultValue);
        assertEquals(value, actualResult);

        String newKey = "test colour key 2";

        actualResult = propertyManager.getColourValue(newKey, defaultValue);
        assertEquals(defaultValue, actualResult);
    }
}
