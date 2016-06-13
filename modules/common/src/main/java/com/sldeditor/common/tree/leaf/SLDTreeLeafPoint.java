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

import java.util.List;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.styling.Fill;
import org.geotools.styling.Graphic;
import org.geotools.styling.Mark;
import org.geotools.styling.MarkImpl;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.PointSymbolizerImpl;
import org.geotools.styling.RasterSymbolizer;
import org.geotools.styling.Stroke;
import org.geotools.styling.StyleFactoryImpl;
import org.opengis.style.GraphicalSymbol;
import org.opengis.style.Symbolizer;

/**
 * Class that represents a point in the SLD structure tree.
 * 
 * @author Robert Ward (SCISYS)
 */
public class SLDTreeLeafPoint implements SLDTreeLeafInterface
{

    /** The style factory. */
    private static StyleFactoryImpl styleFactory = (StyleFactoryImpl) CommonFactoryFinder.getStyleFactory();

    /* (non-Javadoc)
     * @see com.sldeditor.ui.tree.SLDTreeLeafInterface#getSymbolizer()
     */
    @Override
    public Class<?> getSymbolizer()
    {
        return PointSymbolizerImpl.class;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.ui.tree.SLDTreeLeafInterface#hasFill(org.opengis.style.Symbolizer)
     */
    @Override
    public boolean hasFill(Symbolizer symbolizer)
    {
        if(symbolizer instanceof PointSymbolizer)
        {
            PointSymbolizer point = (PointSymbolizer)symbolizer;
            if(point != null)
            {
                return(point.getGraphic() != null);
            }
        }

        return false;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.ui.tree.SLDTreeLeafInterface#hasStroke(org.opengis.style.Symbolizer)
     */
    @Override
    public boolean hasStroke(Symbolizer symbolizer)
    {
        return false;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.ui.tree.SLDTreeLeafInterface#getFill(org.opengis.style.Symbolizer)
     */
    @Override
    public Fill getFill(Symbolizer symbolizer)
    {
        if(symbolizer instanceof PointSymbolizer)
        {
            PointSymbolizer point = (PointSymbolizer)symbolizer;
            if(point != null)
            {
                Graphic graphic = point.getGraphic();

                if(graphic != null)
                {
                    List<GraphicalSymbol> symbolList = graphic.graphicalSymbols();

                    if((symbolList != null) && !symbolList.isEmpty())
                    {
                        GraphicalSymbol obj = symbolList.get(0);

                        if(obj != null)
                        {
                            if(obj instanceof MarkImpl)
                            {
                                MarkImpl mark = (MarkImpl)obj;

                                return mark.getFill();
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.ui.tree.SLDTreeLeafInterface#getStroke(org.opengis.style.Symbolizer)
     */
    @Override
    public Stroke getStroke(Symbolizer symbolizer)
    {
        return null;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.ui.tree.leaf.SLDTreeLeafInterface#removeStroke(org.opengis.style.Symbolizer)
     */
    @Override
    public void removeStroke(Symbolizer symbolizer)
    {
        // Do nothing
    }

    /* (non-Javadoc)
     * @see com.sldeditor.ui.tree.leaf.SLDTreeLeafInterface#createStroke(org.opengis.style.Symbolizer)
     */
    @Override
    public void createStroke(Symbolizer symbolizer)
    {
        // Do nothing
    }

    /* (non-Javadoc)
     * @see com.sldeditor.ui.tree.leaf.SLDTreeLeafInterface#createFill(org.opengis.style.Symbolizer)
     */
    @Override
    public void createFill(Symbolizer symbolizer)
    {
        if(symbolizer instanceof PointSymbolizer)
        {
            PointSymbolizer point = (PointSymbolizer)symbolizer;

            if(point != null)
            {
                Graphic graphic = point.getGraphic();
                if(graphic == null)
                {
                    graphic = styleFactory.createDefaultGraphic();
                    point.setGraphic(graphic);
                }

                if(graphic != null)
                {
                    if(graphic.graphicalSymbols().isEmpty())
                    {
                        Mark mark = styleFactory.getDefaultMark();

                        graphic.graphicalSymbols().add(mark);
                    }
                }
            }
        }
    }

    /* (non-Javadoc)
     * @see com.sldeditor.ui.tree.leaf.SLDTreeLeafInterface#removeFill(org.opengis.style.Symbolizer)
     */
    @Override
    public void removeFill(Symbolizer symbolizer)
    {
        if(symbolizer instanceof PointSymbolizer)
        {
            PointSymbolizer point = (PointSymbolizer)symbolizer;

            if(point != null)
            {
                point.setGraphic(null);
            }
        }
    }

    /**
     * Checks for raster.
     *
     * @param symbolizer the symbolizer
     * @return true, if successful
     */
    @Override
    public boolean hasRaster(Symbolizer symbolizer) {
        return false;
    }

    /**
     * Gets the raster.
     *
     * @param symbolizer the symbolizer
     * @return the raster
     */
    @Override
    public RasterSymbolizer getRaster(Symbolizer symbolizer) {
        return null;
    }

    /**
     * Creates the raster.
     *
     * @param symbolizer the symbolizer
     */
    @Override
    public void createRaster(Symbolizer symbolizer) {
        // Do nothing
    }

    /**
     * Removes the raster.
     *
     * @param symbolizer the symbolizer
     */
    @Override
    public void removeRaster(Symbolizer symbolizer) {
        // Do nothing
    }
}
