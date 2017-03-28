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

package com.sldeditor.common.tree.leaf;

import org.geotools.styling.Fill;
import org.geotools.styling.RasterSymbolizer;
import org.geotools.styling.Stroke;
import org.opengis.style.Symbolizer;

/**
 * The Interface SLDTreeLeafInterface.
 * 
 * @author Robert Ward (SCISYS)
 */
public interface SLDTreeLeafInterface {

    /**
     * Gets the symbolizer.
     *
     * @return the symbolizer
     */
    public Class<?> getSymbolizer();

    /**
     * Checks for fill.
     *
     * @param symbolizer the symbolizer
     * @return true, if successful
     */
    public boolean hasFill(Symbolizer symbolizer);

    /**
     * Checks for stroke.
     *
     * @param symbolizer the symbolizer
     * @return true, if successful
     */
    public boolean hasStroke(Symbolizer symbolizer);

    /**
     * Checks for raster.
     *
     * @param symbolizer the symbolizer
     * @return true, if successful
     */
    public boolean hasRaster(Symbolizer symbolizer);

    /**
     * Gets the fill.
     *
     * @param symbolizer the symbolizer
     * @return the fill
     */
    public Fill getFill(Symbolizer symbolizer);

    /**
     * Gets the stroke.
     *
     * @param symbolizer the symbolizer
     * @return the stroke
     */
    public Stroke getStroke(Symbolizer symbolizer);

    /**
     * Gets the raster.
     *
     * @param symbolizer the symbolizer
     * @return the raster
     */
    public RasterSymbolizer getRaster(Symbolizer symbolizer);

    /**
     * Removes the stroke.
     *
     * @param symbolizer the symbolizer
     */
    public void removeStroke(Symbolizer symbolizer);

    /**
     * Creates the stroke.
     *
     * @param symbolizer the symbolizer
     */
    public void createStroke(Symbolizer symbolizer);

    /**
     * Creates the fill.
     *
     * @param symbolizer the symbolizer
     */
    public void createFill(Symbolizer symbolizer);

    /**
     * Removes the fill.
     *
     * @param symbolizer the symbolizer
     */
    public void removeFill(Symbolizer symbolizer);

    /**
     * Creates the raster.
     *
     * @param symbolizer the symbolizer
     */
    public void createRaster(Symbolizer symbolizer);

    /**
     * Removes the raster.
     *
     * @param symbolizer the symbolizer
     */
    public void removeRaster(Symbolizer symbolizer);
}
