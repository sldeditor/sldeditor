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

package com.sldeditor.datasource.impl;

import com.sldeditor.common.data.traverse.TraverseSymbolizersInterface;
import org.geotools.styling.LineSymbolizer;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.RasterSymbolizer;

/**
 * The Class SymbolizerCount, counts number of different symbolizers.
 *
 * @author ward_r
 */
public class SymbolizerCount implements TraverseSymbolizersInterface {

    /** The point symbolizer count. */
    private int pointSymbolizerCount = 0;

    /** The line symbolizer count. */
    private int lineSymbolizerCount = 0;

    /** The polygon symbolizer count. */
    private int polygonSymbolizerCount = 0;

    /** The raster symbolizer count. */
    private int rasterSymbolizerCount = 0;

    /**
     * Gets the point symbolizer count.
     *
     * @return the pointSymbolizerCount
     */
    public int getPointSymbolizerCount() {
        return pointSymbolizerCount;
    }

    /**
     * Gets the line symbolizer count.
     *
     * @return the lineSymbolizerCount
     */
    public int getLineSymbolizerCount() {
        return lineSymbolizerCount;
    }

    /**
     * Gets the polygon symbolizer count.
     *
     * @return the polygonSymbolizerCount
     */
    public int getPolygonSymbolizerCount() {
        return polygonSymbolizerCount;
    }

    /**
     * Gets the raster symbolizer count.
     *
     * @return the rasterSymbolizerCount
     */
    public int getRasterSymbolizerCount() {
        return rasterSymbolizerCount;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.common.data.traverse.TraverseSymbolizersInterface#rasterSymbolizerCallback(org.geotools.styling.RasterSymbolizer)
     */
    @Override
    public void rasterSymbolizerCallback(RasterSymbolizer symbolizer) {
        rasterSymbolizerCount++;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.common.data.traverse.TraverseSymbolizersInterface#pointSymbolizerCallback(org.geotools.styling.PointSymbolizer)
     */
    @Override
    public void pointSymbolizerCallback(PointSymbolizer symbolizer) {
        pointSymbolizerCount++;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.common.data.traverse.TraverseSymbolizersInterface#lineSymbolizerCallback(org.geotools.styling.LineSymbolizer)
     */
    @Override
    public void lineSymbolizerCallback(LineSymbolizer symbolizer) {
        lineSymbolizerCount++;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.common.data.traverse.TraverseSymbolizersInterface#polygonSymbolizerCallback(org.geotools.styling.PolygonSymbolizer)
     */
    @Override
    public void polygonSymbolizerCallback(PolygonSymbolizer symbolizer) {
        polygonSymbolizerCount++;
    }
}
