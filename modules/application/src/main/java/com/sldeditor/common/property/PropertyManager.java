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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Class that manages reading and writing to the SLD Editor config.properties file.
 * 
 * @author Robert Ward (SCISYS)
 */
public class PropertyManager implements PropertyManagerInterface {
    /** The Constant FALSE. */
    private static final String FALSE = "false";

    /** The Constant TRUE. */
    private static final String TRUE = "true";

    /** The configuration properties file. */
    private File configPropertiesFile = null;

    /** The Constant DELIMETER. */
    private static final char DELIMETER = '.';

    /** The Constant ESCAPED_DELIMETER. */
    private static final String ESCAPED_DELIMETER = "\\.";

    /** The Constant LIST_DELIMETER. */
    private static final String LIST_DELIMETER = ",";

    /** The map of property values. */
    private Map<String, String> fieldValueMap = new HashMap<String, String>();

    /**
     * Default constructor.
     */
    public PropertyManager() {
    }

    /**
     * Sets the property file.
     *
     * @param configPropertiesFile the new property file
     */
    @Override
    public void setPropertyFile(File configPropertiesFile) {
        this.configPropertiesFile = configPropertiesFile;
    }

    /**
     * Update property value.
     *
     * @param key the key
     * @param value the value
     */
    @Override
    public void updateValue(String key, String value) {
        boolean dataUpdated = false;

        if (fieldValueMap.containsKey(key)) {
            String stringValue = fieldValueMap.get(key);
            if (stringValue != null) {
                if (value != null) {
                    if (stringValue.compareTo(value) != 0) {
                        dataUpdated = true;
                    }
                } else if (stringValue.compareTo(value) != 0) {
                    dataUpdated = true;
                }
            }
        } else {
            dataUpdated = true;
        }

        if (dataUpdated) {
            if (key != null) {
                fieldValueMap.put(key, value);
            }
            writeConfigFile();
        }
    }

    /**
     * Update property value.
     *
     * @param key the key
     * @param value the value
     */
    @Override
    public void updateValue(String key, boolean value) {
        updateValue(key, value ? TRUE : FALSE);
    }

    /**
     * Update value.
     *
     * @param key the key
     * @param stringList the string list
     */
    @Override
    public void updateValue(String key, List<String> stringList) {
        StringBuilder sb = new StringBuilder();

        for (String string : stringList) {
            if (sb.length() > 0) {
                sb.append(LIST_DELIMETER);
            }
            sb.append(string);
        }

        updateValue(key, sb.toString());
    }

    /**
     * Update value.
     *
     * @param key the key
     * @param backgroundColour the background colour
     */
    @Override
    public void updateValue(String key, Color backgroundColour) {
        String value = String.format("%03d%s%03d%s%03d%s%03d", backgroundColour.getRed(), DELIMETER,
                backgroundColour.getGreen(), DELIMETER, backgroundColour.getBlue(), DELIMETER,
                backgroundColour.getAlpha());

        updateValue(key, value);
    }

    /**
     * Update value. (Multiple)
     *
     * @param key the key
     * @param count the count
     * @param value the value
     */
    @Override
    public void updateValue(String key, int count, String value) {
        String updatedKey = String.format("%s%s%d", key, DELIMETER, count);

        updateValue(updatedKey, value);
    }

    /**
     * Write configuration file.
     */
    private void writeConfigFile() {
        if (configPropertiesFile != null) {
            try {
                Properties props = new Properties();

                for (String key : fieldValueMap.keySet()) {
                    String value = fieldValueMap.get(key);
                    if (value != null) {
                        props.setProperty(key, value);
                    }
                }

                OutputStream out = new FileOutputStream(configPropertiesFile);
                props.store(out, "SLD Editor configuration data");
                out.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Read configuration.
     */
    @Override
    public void readConfig() {

        // to load application's properties, we use this class
        Properties mainProperties = new Properties();

        if (configPropertiesFile != null) {
            if (configPropertiesFile.exists()) {
                // load the file handle for main.properties
                try {
                    FileInputStream file = new FileInputStream(configPropertiesFile);
                    // load all the properties from this file
                    mainProperties.load(file);
                    // we have loaded the properties, so close the file handle
                    file.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        fieldValueMap.clear();
        for (Object key : mainProperties.keySet()) {
            String fieldName = (String) key;
            fieldValueMap.put(fieldName, mainProperties.getProperty(fieldName));
        }
    }

    /**
     * Gets the double value of a property.
     *
     * @param field the field
     * @param defaultValue the default value
     * @return the double value
     */
    @Override
    public double getDoubleValue(String field, double defaultValue) {
        if (fieldValueMap.containsKey(field)) {
            try {
                return Double.valueOf(fieldValueMap.get(field));
            } catch (NumberFormatException e) {
                return defaultValue;
            }
        } else {
            return defaultValue;
        }
    }

    /**
     * Gets the string value.
     *
     * @param field the field
     * @param defaultValue the default value
     * @return the string value
     */
    @Override
    public String getStringValue(String field, String defaultValue) {
        if (fieldValueMap.containsKey(field)) {
            return fieldValueMap.get(field);
        } else {
            return defaultValue;
        }
    }

    /**
     * Gets the colour value.
     *
     * @param field the field
     * @param defaultValue the default value
     * @return the colour value
     */
    @Override
    public Color getColourValue(String field, Color defaultValue) {
        if (fieldValueMap.containsKey(field)) {
            String value = fieldValueMap.get(field);

            String[] components = value.split(ESCAPED_DELIMETER);

            if (components.length == 4) {
                int red = Integer.valueOf(components[0]);
                int green = Integer.valueOf(components[1]);
                int blue = Integer.valueOf(components[2]);
                int alpha = Integer.valueOf(components[3]);

                return new Color(red, green, blue, alpha);
            }
        }

        return defaultValue;
    }

    /**
     * Gets the boolean value.
     *
     * @param field the field
     * @param defaultValue the default value
     * @return the string value
     */
    @Override
    public boolean getBooleanValue(String field, boolean defaultValue) {
        if (fieldValueMap.containsKey(field)) {
            String value = fieldValueMap.get(field);

            if (value != null) {
                return (value.compareToIgnoreCase(TRUE) == 0);
            }
        }

        return defaultValue;
    }

    /**
     * Gets the string list value.
     *
     * @param field the field
     * @return the string value
     */
    @Override
    public List<String> getStringListValue(String field) {
        List<String> valueList = null;

        if (fieldValueMap.containsKey(field)) {
            String value = fieldValueMap.get(field);

            if (value != null) {
                String[] components = value.split(LIST_DELIMETER);

                valueList = Arrays.asList(components);
            }
        }

        return valueList;
    }

    /**
     * Gets values where the key is a prefix.
     *
     * @param key the key
     * @return the multiple values
     */
    @Override
    public List<String> getMultipleValues(String key) {
        String updatedKey = key + DELIMETER;
        List<String> valueList = new ArrayList<String>();

        List<Integer> indexList = new ArrayList<Integer>();

        for (String storedKey : fieldValueMap.keySet()) {
            if (storedKey.startsWith(updatedKey)) {
                String[] components = storedKey.split(ESCAPED_DELIMETER);

                if (components.length > 1) {
                    Integer index = Integer.valueOf(components[components.length - 1]);

                    indexList.add(index);
                }
            }
        }

        Collections.sort(indexList);

        for (Integer index : indexList) {
            String newKey = updatedKey + index;
            valueList.add(fieldValueMap.get(newKey));
        }
        return valueList;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.common.property.PropertyManagerInterface#clearValue(java.lang.String)
     */
    @Override
    public void clearValue(String key, boolean useDelimeter) {
        StringBuilder sb = new StringBuilder();
        sb.append(key);
        if (useDelimeter) {
            sb.append(DELIMETER);
        }

        String prefix = sb.toString();

        List<String> keyToRemove = new ArrayList<String>();
        for (String existingKey : fieldValueMap.keySet()) {
            if (existingKey.startsWith(prefix)) {
                keyToRemove.add(existingKey);
            }
        }

        for (String existingKey : keyToRemove) {
            fieldValueMap.remove(existingKey);
        }
    }
}
