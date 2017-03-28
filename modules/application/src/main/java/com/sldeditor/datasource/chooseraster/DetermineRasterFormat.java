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

package com.sldeditor.datasource.chooseraster;

import java.io.File;
import java.util.Iterator;
import java.util.Set;

import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridFormatFinder;
import org.geotools.coverage.grid.io.UnknownFormat;
import org.geotools.factory.GeoTools;

/**
 * The Class DetermineRasterFormat.
 *
 * @author Robert Ward (SCISYS)
 */
public class DetermineRasterFormat {

    /**
     * Get user to choose raster format.
     *
     * @param rasterFile the raster file
     * @param selectionPanel the selection panel
     * @return the abstract grid format
     */
    public static AbstractGridFormat choose(File rasterFile,
            ChooseRasterFormatInterface selectionPanel) {
        if (rasterFile != null) {
            final Set<AbstractGridFormat> formats = GridFormatFinder.findFormats(rasterFile,
                    GeoTools.getDefaultHints());

            if (formats.size() > 1) {
                if (selectionPanel != null) {
                    AbstractGridFormat selectedFormat = selectionPanel.showPanel(formats);
                    if (selectedFormat != null) {
                        return selectedFormat;
                    }
                }
            }

            // otherwise just pick the first
            final Iterator<AbstractGridFormat> it = formats.iterator();
            if (it.hasNext()) {
                return it.next();
            }
        }
        return new UnknownFormat();
    }

}
