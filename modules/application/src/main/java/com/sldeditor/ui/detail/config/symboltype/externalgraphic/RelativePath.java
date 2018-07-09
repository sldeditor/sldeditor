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

package com.sldeditor.ui.detail.config.symboltype.externalgraphic;

import com.sldeditor.common.SLDDataInterface;
import com.sldeditor.datasource.SLDEditorFile;
import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.commons.io.FilenameUtils;

/**
 * The Class RelativePath.
 *
 * @author Robert Ward (SCISYS)
 */
public class RelativePath {

    /**
     * Checks if is file is a relative path.
     *
     * @param path the path
     * @return true if the path is relative
     */
    public static Boolean isRelativePath(String path) {
        if (path == null) {
            return false;
        }

        int prefixLen = FilenameUtils.getPrefixLength(path);
        return !(testPathWin(path, prefixLen) || testPathLinux(prefixLen));
    }

    /**
     * Test path (Windows).
     *
     * @param path the path
     * @param prefixLen the prefix length
     * @return true, if successful
     */
    private static boolean testPathWin(String path, int prefixLen) {
        if (prefixLen == 3) {
            return true;
        }
        File f = new File(path);
        if ((prefixLen == 2) && (f.getPath().charAt(0) == '/')) {
            return true;
        }
        return false;
    }

    /**
     * Test path (Linux(.
     *
     * @param prefixLen the prefix length
     * @return true, if successful
     */
    private static boolean testPathLinux(int prefixLen) {
        return (prefixLen != 0);
    }

    /**
     * Convert URL to absolute/relative path.
     *
     * @param externalFileURL the external file URL
     * @param useRelativePaths the use relative paths
     * @return the string
     */
    public static String convert(URL externalFileURL, boolean useRelativePaths) {
        String path = "";
        if (externalFileURL != null) {
            if (isLocalFile(externalFileURL)) {
                if (useRelativePaths) {
                    File f = new File(externalFileURL.getFile());
                    File folder = null;
                    SLDDataInterface sldData = SLDEditorFile.getInstance().getSLDData();
                    if ((sldData != null) && (sldData.getSLDFile() != null)) {
                        folder = sldData.getSLDFile().getParentFile();
                    }

                    if (folder == null) {
                        folder = new File(System.getProperty("user.dir"));
                    }
                    path = getRelativePath(f, folder);
                } else {
                    path = externalFileURL.toExternalForm();
                }
            } else {
                path = externalFileURL.toExternalForm();
            }
        }
        return path;
    }

    /**
     * Checks if is local file.
     *
     * @param url the url
     * @return true, if is local file
     */
    public static boolean isLocalFile(URL url) {
        if (url == null) {
            return false;
        }
        String scheme = url.getProtocol();
        return "file".equalsIgnoreCase(scheme) && !hasHost(url);
    }

    /**
     * Checks for host.
     *
     * @param url the url
     * @return true, if successful
     */
    public static boolean hasHost(URL url) {
        if (url == null) {
            return false;
        }
        String host = url.getHost();
        return host != null && !"".equals(host);
    }

    /**
     * Gets the relative path.
     *
     * @param file the file
     * @param folder the folder
     * @return the relative path
     */
    public static String getRelativePath(File file, File folder) {
        if (file == null) {
            return null;
        }

        if (folder == null) {
            return null;
        }

        Path filePath = Paths.get(file.getAbsolutePath());
        Path folderPath = Paths.get(folder.getAbsolutePath());
        Path path = null;
        try {
            path = folderPath.relativize(filePath);
        } catch (Exception e) {
            path = filePath;
        }

        return path.toString();
    }
}
