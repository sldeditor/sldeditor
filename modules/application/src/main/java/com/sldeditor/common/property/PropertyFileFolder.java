/*
 * SLD Editor - The Open Source Java SLD Editor
 *
 * Copyright (C) 2018, SCISYS UK Limited
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

import com.sldeditor.common.utils.OSValidator;
import java.io.File;

/**
 * Class determine where the property file should be stored based on operating system.
 *
 * @author Robert Ward (SCISYS)
 */
public class PropertyFileFolder {

    /** The Constant FORMAT_STRING. */
    private static final String FORMAT_STRING = "%s/%s";

    /** The Constant WINDOWS_PATH. */
    private static final String WINDOWS_PATH = "AppData/Roaming/SLDEditor";

    /** The Constant LINUX_PATH. */
    private static final String LINUX_PATH = ".config/SLDEditor";

    /** The Constant MAC_PATH. */
    private static final String MAC_PATH = ".config/SLDEditor";

    /** Default constructor */
    private PropertyFileFolder() {
        // Default constructor
    }

    /**
     * Gets the folder.
     *
     * @param userHome the user home
     * @param oldConfigProperties the old config properties
     * @param newConfigProperties the new config properties
     * @return the folder
     */
    public static String getFolder(
            String userHome, String oldConfigProperties, String newConfigProperties) {
        String folder = userHome;

        if (OSValidator.isWindows()) {
            folder = String.format(FORMAT_STRING, userHome, WINDOWS_PATH);

            File f = new File(folder);
            if (!f.exists()) {
                f.mkdirs();
            }
        } else if (OSValidator.isUnix() || OSValidator.isSolaris()) {
            folder = String.format(FORMAT_STRING, userHome, LINUX_PATH);

            File f = new File(folder);
            if (!f.exists()) {
                f.mkdirs();
            }
        } else if (OSValidator.isMac()) {
            folder = String.format(FORMAT_STRING, userHome, MAC_PATH);

            File f = new File(folder);
            if (!f.exists()) {
                f.mkdirs();
            }
        }

        File newConfigFile = new File(folder, newConfigProperties);

        if (!newConfigFile.exists()) {
            // Migrate if the file does not exist
            File oldConfigFile = new File(oldConfigProperties);
            if (oldConfigFile.exists()) {
                boolean result = oldConfigFile.renameTo(newConfigFile);
                if(!result)
                {
                    // Do nothing
                }
            }
        }
        return newConfigFile.getAbsolutePath();
    }
}
