/*
 *    SLDEditor - SLD Editor application
 *
 *    (C) 2016, SCISYS
 *
 */

package com.sldeditor.ui.tree.item;

import org.geotools.styling.LineSymbolizer;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.RasterSymbolizer;
import org.geotools.styling.Symbolizer;

/**
 * The Class RasterSymbolizerImageOutline.
 *
 * @author Robert Ward (SCISYS)
 */
public class RasterSymbolizerImageOutline {

    /** The raste symbolizerr. */
    private RasterSymbolizer raster = null;

    /**
     * Instantiates a new raster symbolizer image outline.
     *
     * @param raster the raster
     */
    public RasterSymbolizerImageOutline(RasterSymbolizer raster) {
        this.raster = raster;
    }

    /**
     * Gets the symbolizer.
     *
     * @return the symbolizer
     */
    public Symbolizer getSymbolizer()
    {
        Symbolizer symbolizer = null;

        if(raster != null)
        {

            symbolizer = raster.getImageOutline();
        }

        return symbolizer;
    }

    /**
     * Gets the line symbolizer.
     *
     * @return the line symbolizer
     */
    public LineSymbolizer getLineSymbolizer()
    {
        Symbolizer symbolizer = getSymbolizer();

        if(symbolizer instanceof LineSymbolizer)
        {
            return (LineSymbolizer) symbolizer;
        }

        return null;
    }

    /**
     * Gets the polygon symbolizer.
     *
     * @return the polygon symbolizer
     */
    public PolygonSymbolizer getPolygonSymbolizer()
    {
        Symbolizer symbolizer = getSymbolizer();

        if(symbolizer instanceof PolygonSymbolizer)
        {
            return (PolygonSymbolizer) symbolizer;
        }

        return null;
    }
}
