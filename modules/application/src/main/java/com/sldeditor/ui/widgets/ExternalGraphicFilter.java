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

import java.io.File;

import javax.swing.filechooser.FileFilter;

import com.sldeditor.common.localisation.Localisation;

/**
 * External image file filter class, allows gif, jpg, tiff, or png files.
 * 
 * @author Robert Ward (SCISYS)
 */
public class ExternalGraphicFilter extends FileFilter {

    /** The Constant jpeg. */
    public static final String jpeg = "jpeg";

    /** The Constant jpg. */
    public static final String jpg = "jpg";

    /** The Constant gif. */
    public static final String gif = "gif";

    /** The Constant svg. */
    public static final String svg = "svg";

    /** The Constant png. */
    public static final String png = "png";

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
     * Accept all directories and all gif, jpg, tiff, or png files.
     * (non-Javadoc)
     * 
     * @see javax.swing.filechooser.FileFilter#accept(java.io.File)
     */
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }

        String extension = getExtension(f);
        if (extension != null) {
            if (extension.equals(ExternalGraphicFilter.svg)
                    || extension.equals(ExternalGraphicFilter.gif)
                    || extension.equals(ExternalGraphicFilter.jpeg)
                    || extension.equals(ExternalGraphicFilter.jpg)
                    || extension.equals(ExternalGraphicFilter.png)) {
                return true;
            } else {
                return false;
            }
        }

        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.filechooser.FileFilter#getDescription()
     */
    public String getDescription() {
        return Localisation.getString(ExternalGraphicFilter.class,
                "ExternalGraphicFilter.description");
    }
}
