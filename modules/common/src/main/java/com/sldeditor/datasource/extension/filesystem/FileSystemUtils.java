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
package com.sldeditor.datasource.extension.filesystem;

import java.io.File;
import java.util.List;

/**
 * The Class FileSystemUtils.
 *
 * @author Robert Ward (SCISYS)
 */
public class FileSystemUtils {

    /**
     * Checks if is file extension supported.
     *
     * @param file the file
     * @param fileExtensionList the file extension list
     * @return true, if is file extension supported
     */
    public static boolean isFileExtensionSupported(File file, List<String> fileExtensionList) {
        if(file == null)
        {
            return false;
        }

        for(String fileExtension : fileExtensionList)
        {
            if(file.getAbsolutePath().endsWith(fileExtension))
            {
                return true;
            }
        }
        return false;
    }


}
