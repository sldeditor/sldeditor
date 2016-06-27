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
package com.sldeditor.common.utils;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.sldeditor.common.console.ConsoleManager;

/**
 * Utility methods to handle external filenames, e.g for external graphics.
 * 
 * @author Robert Ward (SCISYS)
 */
public class ExternalFilenames {

    /** The Constant UTF_8_ENCODING. */
    private static final String UTF_8_ENCODING = "UTF-8";

    /** The Constant WINDOWS_FILE_PREFIX - URL file prefix. */
    private static final String WINDOWS_FILE_PREFIX = "file:/";

    /** The Constant FILE_PREFIX - URL file prefix. */
    private static final String FILE_PREFIX = "file:";

    /** The format map. */
    private static Map<String, String> formatMap = new HashMap<String, String>();

    /**
     * Default constructor
     */
    public ExternalFilenames() {
    }

    /**
     * Initialise.
     */
    private static void initialise() {
        formatMap.put("svg", "image/svg+xml");
        formatMap.put("png", "image/png");
    }

    /**
     * Gets the file extension.
     * <p>Returns null if fileName is null
     *
     * @param fileName the file name
     * @return the file extension
     */
    public static String getFileExtension(String fileName)
    {
        if(fileName == null)
        {
            return null;
        }

        String extension = "";
        int i = fileName.lastIndexOf('.');
        int p = Math.max(fileName.lastIndexOf('/'), fileName.lastIndexOf('\\'));

        if (i > p) {
            extension = fileName.substring(i+1);
        }

        return extension;
    }

    /**
     * Gets the image format.
     * <p>If fileExtension is null then method returns null.
     *
     * @param fileExtension the file extension
     * @return the image format
     */
    public static String getImageFormat(String fileExtension) {
        if(fileExtension == null)
        {
            return null;
        }

        String imageFormat = fileExtension;

        if(formatMap.isEmpty())
        {
            initialise();
        }

        if(formatMap.containsKey(fileExtension))
        {
            imageFormat = formatMap.get(fileExtension);
        }
        else
        {
            ConsoleManager.getInstance().error(ExternalFilenames.class,
                    String.format("%s : %s", "Unknown image format ", fileExtension));
        }
        return imageFormat;
    }

    /**
     * Convert url to file path.
     *
     * @param url the url
     * @return the string
     */
    public static String convertURLToFile(URL url) {
        try {
            int length = FILE_PREFIX.length();

            if(OSValidator.isWindows())
            {
                length = WINDOWS_FILE_PREFIX.length();
            }

            String urlString = url.toString().substring(length);
            return java.net.URLDecoder.decode(urlString, UTF_8_ENCODING);
        } catch (UnsupportedEncodingException e) {
            ConsoleManager.getInstance().exception(ExternalFilenames.class, e);
            return null;
        }
    }

    /**
     * Adds the file extension separator.
     *
     * @param fileExtension the file extension
     * @return the string
     */
    public static String addFileExtensionSeparator(String fileExtension)
    {
        return "." + fileExtension;
    }
}
