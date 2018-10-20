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

package com.sldeditor.ui.widgets;

import com.sldeditor.common.localisation.Localisation;
import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 * External image file filter class, allows gif, jpg, tiff, or png files.
 *
 * @author Robert Ward (SCISYS)
 */
public class ExternalGraphicFilter extends FileFilter {

    /** The Constant JPEG. */
    public static final String JPEG = "jpeg";

    /** The Constant JPG. */
    public static final String JPG = "jpg";

    /** The Constant GIF. */
    public static final String GIF = "gif";

    /** The Constant SVG. */
    public static final String SVG = "svg";

    /** The Constant PNG. */
    public static final String PNG = "png";

    /*
     * Get the extension of a file.
     */
    /**
     * Gets the extension.
     *
     * @param f the f
     * @return the extension
     */
    public static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 && i < s.length() - 1) {
            ext = s.substring(i + 1).toLowerCase();
        }
        return ext;
    }

    /**
     * Accept all directories and all gif, jpg, tiff, or png files. (non-Javadoc)
     *
     * @param f the f
     * @return true, if successful
     * @see javax.swing.filechooser.FileFilter#accept(java.io.File)
     */
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }

        String extension = getExtension(f);
        return ((extension != null)
                && (extension.equals(ExternalGraphicFilter.SVG)
                        || extension.equals(ExternalGraphicFilter.GIF)
                        || extension.equals(ExternalGraphicFilter.JPEG)
                        || extension.equals(ExternalGraphicFilter.JPG)
                        || extension.equals(ExternalGraphicFilter.PNG)));
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.swing.filechooser.FileFilter#getDescription()
     */
    public String getDescription() {
        return Localisation.getString(
                ExternalGraphicFilter.class, "ExternalGraphicFilter.description");
    }
}
