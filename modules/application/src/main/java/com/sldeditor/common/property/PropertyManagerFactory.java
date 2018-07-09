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

import java.io.File;

/**
 * Class to create PropertyManagerInterface objects.
 *
 * @author Robert Ward (SCISYS)
 */
public class PropertyManagerFactory {

    /** The Constant CONFIG_PROPERTIES. */
    private static final String CONFIG_PROPERTIES = "./config.properties";

    /** The singleton instance. */
    private static PropertyManagerInterface instance = null;

    /** Private default constructor. */
    private PropertyManagerFactory() {
        // Does nothing
    }

    /**
     * Return the Property Manager.
     *
     * @return singleton instance of PropertyManagerInterface
     */
    public static synchronized PropertyManagerInterface getInstance() {
        if (instance == null) {
            instance = new PropertyManager();
            instance.setPropertyFile(new File(CONFIG_PROPERTIES));
        }

        return instance;
    }

    /** Destroy instance. */
    public static synchronized void destroyInstance() {
        instance = null;
    }
}
