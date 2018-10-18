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

package com.sldeditor.common.localisation;

import com.sldeditor.common.console.ConsoleManager;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * The Class Localisation, the central point for getting localised strings.
 *
 * <p>The localisation is implemented as resource bundles. The root folder is <code>
 * src/main/resources/i18n</code>
 *
 * <p>Underneath is a folder for each of the top level folders in the source tree containing all the
 * package's classes.
 *
 * <p>Calls are made to <code>getString()</code> supplying a class name. The string is looked up in
 * the correspondingly named resource bundle.
 *
 * @author Robert Ward (SCISYS)
 */
public class Localisation {

    private static final String DEFAULT_BASE_NAME = "sldeditor";

    private static final String COMMON_PREFIX = "common.";

    private static final String RESOURCE_FOLDER = "i18n";

    /** The Constant DEFAULT_COUNTRY. */
    private static final String DEFAULT_COUNTRY = "UK";

    /** The Constant DEFAULT_LANGUAGE. */
    private static final String DEFAULT_LANGUAGE = "en";

    /** The Constant ARG_STRING. */
    private static final String ARG_STRING = "-locale=";

    /** The instance. */
    private static Localisation instance = null;

    /** The current locale. */
    private Locale currentLocale = null;

    /** The resource bundle map. */
    private Map<String, ResourceBundle> resourceBundleMap = new HashMap<String, ResourceBundle>();

    /** Instantiates a new localisation. */
    private Localisation() {
        // Default constructor
    }

    /**
     * Gets the single instance of Localisation.
     *
     * @return single instance of Localisation
     */
    public static Localisation getInstance() {
        if (instance == null) {
            instance = new Localisation();
        }

        return instance;
    }

    /**
     * Parses the command line.
     *
     * @param args the args
     */
    public void parseCommandLine(String[] args) {
        String language = DEFAULT_LANGUAGE;
        String country = DEFAULT_COUNTRY;

        if (args != null) {
            for (String arg : args) {
                if (arg.startsWith(ARG_STRING)) {
                    String locale = arg.substring(ARG_STRING.length());

                    String[] components = locale.split("\\:");

                    if (components.length == 2) {
                        language = components[0];
                        country = components[1];
                    }
                }
            }
        }

        currentLocale = new Locale(language, country);
    }

    /**
     * Gets the resource bundle.
     *
     * @param baseName the base name
     * @return the resource bundle
     */
    public ResourceBundle getResourceBundle(String baseName) {
        ResourceBundle resourceBundle = resourceBundleMap.get(baseName);

        if (resourceBundle == null) {
            if (currentLocale == null) {
                currentLocale = new Locale(DEFAULT_LANGUAGE, DEFAULT_COUNTRY);
            }

            try {
                resourceBundle = ResourceBundle.getBundle(baseName, currentLocale);
                resourceBundleMap.put(baseName, resourceBundle);
            } catch (MissingResourceException e) {
                ConsoleManager.getInstance().error(this, "Missing resource : " + baseName);
            }
        }
        return resourceBundle;
    }

    /**
     * Gets the string for the supplied class and key.
     *
     * @param clazz the class to get the strings for
     * @param key the key
     * @return the string
     */
    public static String getString(Class<?> clazz, String key) {

        ResourceBundle resourceBundle = findResourceBundle(clazz, key);

        if ((resourceBundle == null) || (key == null)) {
            return key;
        } else {
            if (resourceBundle.containsKey(key)) {
                return resourceBundle.getString(key);
            } else {
                return key;
            }
        }
    }

    /**
     * Find resource bundle.
     *
     * @param clazz the clazz
     * @param key the key
     * @return the resource bundle
     */
    private static ResourceBundle findResourceBundle(Class<?> clazz, String key) {
        String baseName = null;
        String className;

        if ((key != null) && key.startsWith(COMMON_PREFIX)) {
            baseName = DEFAULT_BASE_NAME;
            className = "SLDEditor";
        } else {
            String fullPackageName = clazz.getPackage().getName();
            String[] packages = fullPackageName.split("\\.");

            if (packages.length == 2) {
                baseName = DEFAULT_BASE_NAME;
            } else {
                baseName = packages[2];
            }

            className = clazz.getSimpleName();
        }

        ResourceBundle resourceBundle =
                getInstance()
                        .getResourceBundle(
                                String.format("%s/%s/%s", RESOURCE_FOLDER, baseName, className));
        return resourceBundle;
    }

    /**
     * Gets the field.
     *
     * @param clazz the class to get the strings for
     * @param key the key
     * @return the field
     */
    public static String getField(Class<?> clazz, String key) {
        return getString(clazz, key) + " :";
    }

    /**
     * Preload a resource bundle.
     *
     * @param clazz the class to preload
     */
    public static void preload(Class<?> clazz) {
        getString(clazz, null);
    }

    /**
     * Gets the current locale.
     *
     * @return the currentLocale
     */
    public Locale getCurrentLocale() {
        return currentLocale;
    }
}
