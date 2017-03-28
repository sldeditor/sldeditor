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

/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.sldeditor.ui.legend.filechooser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.filechooser.FileFilter;

/**
 * Image file filter class, allows gif, jpg, tiff, or png files.
 * 
 * @author Robert Ward (SCISYS)
 */
public class ImageFilter extends FileFilter {

    /** The filter string. */
    private String filterString;

    /**
     * Constructor.
     *
     * @param filterString the filter string
     */
    public ImageFilter(String filterString) {
        this.filterString = filterString;
    }

    /**
     * Accept all directories and all gif, jpg, tiff, or png files.
     * 
     * @see javax.swing.filechooser.FileFilter#accept(java.io.File)
     */
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }

        String extension = ImageFileExtensionUtils.getExtension(f);
        if (extension != null) {
            if (extension.equals(filterString)) {
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
        return ImageFileExtensionUtils.getDescription(filterString);
    }

    /**
     * Gets the list of available filters.
     *
     * @return the filters
     */
    public static List<FileFilter> getFilters() {
        List<FileFilter> filterlist = new ArrayList<FileFilter>();

        filterlist.add(new ImageFilter(ImageFileExtensionUtils.jpg));
        filterlist.add(new ImageFilter(ImageFileExtensionUtils.gif));
        filterlist.add(new ImageFilter(ImageFileExtensionUtils.tif));
        filterlist.add(new ImageFilter(ImageFileExtensionUtils.png));

        return filterlist;
    }

    /**
     * Gets the file extension.
     *
     * @return the file extension
     */
    public String getFileExtension() {
        return filterString;
    }

    /**
     * Default extension.
     *
     * @return the string
     */
    public static String defaultExtension() {
        return ImageFileExtensionUtils.png;
    }
}
