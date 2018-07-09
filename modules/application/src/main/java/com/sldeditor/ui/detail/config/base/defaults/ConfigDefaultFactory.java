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

package com.sldeditor.ui.detail.config.base.defaults;

import com.sldeditor.common.console.ConsoleManager;
import java.util.ArrayList;
import java.util.List;

/**
 * A factory for creating ConfigDefault objects.
 *
 * @author Robert Ward (SCISYS)
 */
public class ConfigDefaultFactory {

    /** The default list. */
    private static List<DefaultBase> defaultList = new ArrayList<DefaultBase>();

    /**
     * Gets the boolean default value.
     *
     * @param defaultValue the default value
     * @return the boolean
     */
    public static Boolean getBoolean(String defaultValue) {
        DefaultBase obj = getObject(defaultValue);

        if (obj != null) {
            Object value = obj.getValue(defaultValue);

            try {
                return (Boolean) value;
            } catch (ClassCastException e) {
                ConsoleManager.getInstance()
                        .error(
                                ConfigDefaultFactory.class,
                                "Incorrect type configured for : "
                                        + defaultValue
                                        + " expecting Boolean");
            }
        }

        return null;
    }

    /**
     * Gets the integer default value.
     *
     * @param defaultValue the default value
     * @return the integer
     */
    public static Integer getInteger(String defaultValue) {
        DefaultBase obj = getObject(defaultValue);

        if (obj != null) {
            Object value = obj.getValue(defaultValue);

            try {
                return (Integer) value;
            } catch (ClassCastException e) {
                ConsoleManager.getInstance()
                        .error(
                                ConfigDefaultFactory.class,
                                "Incorrect type configured for : "
                                        + defaultValue
                                        + " expecting Integer");
            }
        }

        return null;
    }

    /**
     * Gets the integer default value.
     *
     * @param defaultValue the default value
     * @return the double
     */
    public static Double getDouble(String defaultValue) {
        DefaultBase obj = getObject(defaultValue);

        if (obj != null) {
            Object value = obj.getValue(defaultValue);

            try {
                return (Double) value;
            } catch (ClassCastException e) {
                ConsoleManager.getInstance()
                        .error(
                                ConfigDefaultFactory.class,
                                "Incorrect type configured for : "
                                        + defaultValue
                                        + " expecting Double");
            }
        }

        return null;
    }

    /**
     * Gets the string default value.
     *
     * @param defaultValue the default value
     * @return the string
     */
    public static String getString(String defaultValue) {
        DefaultBase obj = getObject(defaultValue);

        if (obj != null) {
            Object value = obj.getValue(defaultValue);

            try {
                return (String) value;
            } catch (ClassCastException e) {
                ConsoleManager.getInstance()
                        .error(
                                ConfigDefaultFactory.class,
                                "Incorrect type configured for : "
                                        + defaultValue
                                        + " expecting String");
            }
        }

        return null;
    }

    /**
     * Gets the object.
     *
     * @param defaultValue the default value
     * @return the object
     */
    private static synchronized DefaultBase getObject(String defaultValue) {
        if (defaultList.isEmpty()) {
            defaultList.add(new DefaultInteger());
            defaultList.add(new DefaultDouble());
            defaultList.add(new DefaultBoolean());
            defaultList.add(new DefaultTextSymbols());

            // Add last
            defaultList.add(new DefaultDefault());
        }

        if (defaultValue != null) {
            for (DefaultBase defObj : defaultList) {
                if (defObj.accepts(defaultValue)) {
                    return defObj;
                }
            }
        }
        return null;
    }
}
