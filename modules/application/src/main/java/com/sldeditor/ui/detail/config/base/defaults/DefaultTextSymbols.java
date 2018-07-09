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

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.styling.StyleFactoryImpl;
import org.geotools.styling.TextSymbolizer;

/**
 * Class for configuration file text symbolizer default values.
 *
 * @author Robert Ward (SCISYS)
 */
public class DefaultTextSymbols extends DefaultBase {

    /** The Constant EXPECTED_PREFIX_LIST. */
    private static final List<String> EXPECTED_PREFIX_LIST =
            Arrays.asList(
                    "org.geotools.styling.TextSymbolizer2", "org.geotools.styling.TextSymbolizer");

    /** The style factory. */
    private static StyleFactoryImpl styleFactory =
            (StyleFactoryImpl) CommonFactoryFinder.getStyleFactory();

    /** The expected interface. */
    private static Class<?> expectedInterface = null;

    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.ui.detail.config.base.defaults.DefaultBase#accepts(java.lang.String)
     */
    @Override
    public boolean accepts(String defaultValue) {
        for (String expectedPrefix : EXPECTED_PREFIX_LIST) {
            if (defaultValue.startsWith(expectedPrefix)) {
                return true;
            }
        }
        return false;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.ui.detail.config.base.defaults.DefaultBase#getValue(java.lang.String)
     */
    @Override
    public Object getValue(String defaultValue) {
        for (String expectedPrefix : EXPECTED_PREFIX_LIST) {
            if (defaultValue.startsWith(expectedPrefix)) {
                int index = defaultValue.lastIndexOf(".");

                if ((index < 0) || (index >= defaultValue.length())) {
                    return null;
                }

                String fieldName = defaultValue.substring(index + 1);

                return getDefaultValue(fieldName);
            }
        }
        return null;
    }

    /**
     * Gets the default value.
     *
     * @param fieldName the field name
     * @return the default value
     */
    private static Object getDefaultValue(String fieldName) {
        // Instantiate the interface once and then cache it
        if (expectedInterface == null) {
            TextSymbolizer textObj = styleFactory.createTextSymbolizer();
            Class<?>[] interfaceArray = textObj.getClass().getInterfaces();

            for (Class<?> interfaceObj : interfaceArray) {
                for (String expectedPrefix : EXPECTED_PREFIX_LIST) {
                    if (interfaceObj.getTypeName().compareTo(expectedPrefix) == 0) {
                        expectedInterface = interfaceObj;
                        break;
                    }
                }
            }
        }

        if (expectedInterface != null) {
            try {
                Field f = expectedInterface.getField(fieldName);
                Object obj = f.get(expectedInterface);

                return obj;
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return null;
    }
}
