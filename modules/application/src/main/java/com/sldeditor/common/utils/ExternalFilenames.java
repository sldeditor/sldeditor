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

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.sldeditor.common.SLDDataInterface;
import com.sldeditor.common.console.ConsoleManager;
import com.sldeditor.common.localisation.Localisation;

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

    /** The Constant SLD_FILE_EXTENSION. */
    private static final String SLD_FILE_EXTENSION = "sld";

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
     * Returns a File from the supplied path.
     * Strips <code>file://</code> from URLs.
     *
     * @param sldDataInterface the sld data interface
     * @param filePath the supplied file path
     * @return the file
     */
    public static File getFile(SLDDataInterface sldDataInterface, String filePath) {
        if((filePath == null) || (sldDataInterface == null))
        {
            return null;
        }

        String filename = null;

        if(filePath.startsWith(FILE_PREFIX))
        {
            filename = filePath.substring(FILE_PREFIX.length());
        }
        else
        {
            File f = new File(filePath);
            if(!f.isAbsolute())
            {
                if(sldDataInterface.getSLDFile() != null)
                {
                    String parentFolder = sldDataInterface.getSLDFile().getParent();
                    filename = parentFolder + File.separator + filePath;
                }
                else
                {
                    ConsoleManager.getInstance().error(ExternalFilenames.class, "No SLD filename set");
                    return null;
                }
            }
            else
            {
                filename = f.getAbsolutePath();
            }
        }

        File file = new File(filename);

        return file;
    }

    /**
     * Gets the text.
     *
     * @param sldDataInterface the sld data interface
     * @param location the location
     * @return the text
     */
    public static String getText(SLDDataInterface sldDataInterface, URL location) {

        if(location == null)
        {
            return null;
        }

        // Remove file:/ prefix
        String text = location.getFile();

        text = new File(text).getAbsolutePath();

        if(sldDataInterface != null)
        {
            if(sldDataInterface.getSLDFile() != null)
            {
                String parentFolder = sldDataInterface.getSLDFile().getParent();

                if(parentFolder != null)
                {
                    if(text.startsWith(parentFolder))
                    {
                        text = text.substring(parentFolder.length());
                    }
                }
            }
            else
            {
                ConsoleManager.getInstance().error(ExternalFilenames.class, "No SLD filename set");
                return null;
            }
        }

        return text;
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
                    String.format("%s : %s", Localisation.getString(ExternalFilenames.class, "ExternalFilenames.error"), fileExtension));
        }
        return imageFormat;
    }

    /**
     * Convert url to file path.
     *
     * @param url the url as an URL object
     * @return the string
     */
    public static String convertURLToFile(URL url) {
        if(url == null)
        {
            return "";
        }
        return convertURLToFile(url.toString());
    }

    /**
     * Convert url to file path.
     *
     * @param url the url as a string
     * @return the string
     */
    public static String convertURLToFile(String url) {
        if(url == null)
        {
            return "";
        }

        String prefix = FILE_PREFIX;
        if(OSValidator.isWindows())
        {
            prefix = WINDOWS_FILE_PREFIX;
        }

        if(url.startsWith(prefix))
        {
            try {
                int length = prefix.length();

                String urlString = url.substring(length);
                return java.net.URLDecoder.decode(urlString, UTF_8_ENCODING);
            } catch (UnsupportedEncodingException e) {
                ConsoleManager.getInstance().exception(ExternalFilenames.class, e);
                return null;
            }
        }
        return url;
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

    /**
     * Creates the SLD filename.
     *
     * @param file the file
     * @return the file
     */
    public static File createSLDFilename(File file) {
        File newFile = null;
        if(file != null)
        {
            String filename = file.getAbsolutePath();

            String fileExtension = getFileExtension(filename);
            if(SLD_FILE_EXTENSION.compareToIgnoreCase(fileExtension) == 0)
            {
                // The filename already has the correct SLD file extension
                return file;
            }
            int endIndex = filename.length() - fileExtension.length();

            // Remove existing file extension
            filename = filename.substring(0, endIndex);

            // Create new filename with sld file extension
            newFile = new File(filename + SLD_FILE_EXTENSION);
        }
        return newFile;
    }
}
