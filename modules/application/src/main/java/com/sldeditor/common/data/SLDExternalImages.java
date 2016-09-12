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

package com.sldeditor.common.data;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.geotools.metadata.iso.citation.OnLineResourceImpl;
import org.geotools.styling.ExternalGraphic;
import org.geotools.styling.ExternalGraphicImpl;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Fill;
import org.geotools.styling.LineSymbolizer;
import org.geotools.styling.NamedLayer;
import org.geotools.styling.NamedLayerImpl;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.Stroke;
import org.geotools.styling.Style;
import org.geotools.styling.StyledLayer;
import org.geotools.styling.StyledLayerDescriptor;
import org.geotools.styling.Symbolizer;
import org.geotools.styling.UserLayer;
import org.geotools.styling.UserLayerImpl;
import org.opengis.style.GraphicalSymbol;

import com.sldeditor.common.console.ConsoleManager;

/**
 * The Class SLDExternalImages.
 *
 * @author Robert Ward (SCISYS)
 */
public class SLDExternalImages {

    /**
     * Update online resources.
     *
     * @param resourceLocator the resource locator
     * @param sld the sld
     */
    public static void updateOnlineResources(URL resourceLocator, StyledLayerDescriptor sld) {

        List<String> externalImageList = new ArrayList<String>();

        internal_updateOnlineResources(resourceLocator, sld, externalImageList);
    }

    /**
     * Gets the external images from the SLD symbol.
     *
     * @param resourceLocator the resource locator
     * @param sld the sld
     * @return the external images
     */
    public static List<String> getExternalImages(URL resourceLocator, StyledLayerDescriptor sld) {

        List<String> externalImageList = new ArrayList<String>();

        internal_updateOnlineResources(resourceLocator, sld, externalImageList);

        return externalImageList;
    }

    /**
     * Internal update online resources.
     *
     * @param resourceLocator the resource locator
     * @param sld the sld
     * @param externalImageList the external image list
     */
    private static void internal_updateOnlineResources(URL resourceLocator,
            StyledLayerDescriptor sld,
            List<String> externalImageList)
    {
        if(sld == null)
        {
            return;
        }

        for(StyledLayer styledLayer : sld.layers())
        {
            List<Style> styles = null;
            if(styledLayer instanceof NamedLayer)
            {
                NamedLayerImpl namedLayer = (NamedLayerImpl) styledLayer;
                styles = namedLayer.styles();
            }
            else if(styledLayer instanceof UserLayer)
            {
                UserLayerImpl userLayer = (UserLayerImpl) styledLayer;
                styles = userLayer.userStyles();
            }

            if(styles != null)
            {
                for(Style style : styles)
                {
                    for(FeatureTypeStyle fts : style.featureTypeStyles())
                    {
                        for(Rule rule : fts.rules())
                        {
                            for(Symbolizer symbolizer : rule.symbolizers())
                            {
                                if(symbolizer instanceof PointSymbolizer)
                                {
                                    PointSymbolizer point = (PointSymbolizer) symbolizer;

                                    if(point.getGraphic() != null)
                                    {
                                        updateGraphicalSymbol(resourceLocator,
                                                point.getGraphic().graphicalSymbols(), 
                                                externalImageList);
                                    }
                                }
                                else if(symbolizer instanceof LineSymbolizer)
                                {
                                    LineSymbolizer line = (LineSymbolizer) symbolizer;

                                    updateStroke(resourceLocator, line.getStroke(), externalImageList);
                                }
                                else if(symbolizer instanceof PolygonSymbolizer)
                                {
                                    PolygonSymbolizer polygon = (PolygonSymbolizer) symbolizer;

                                    updateStroke(resourceLocator, polygon.getStroke(), externalImageList);
                                    updateFill(resourceLocator, polygon.getFill(), externalImageList);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Update fill.
     *
     * @param resourceLocator the resource locator
     * @param fill the fill
     * @param externalImageList the external image list
     */
    private static void updateFill(URL resourceLocator, Fill fill, List<String> externalImageList) {
        if(fill != null)
        {
            if(fill.getGraphicFill() != null)
            {
                updateGraphicalSymbol(resourceLocator,
                        fill.getGraphicFill().graphicalSymbols(),
                        externalImageList);
            }
        }
    }

    /**
     * Update stroke.
     *
     * @param resourceLocator the resource locator
     * @param stroke the stroke
     * @param externalImageList the external image list
     */
    private static void updateStroke(URL resourceLocator, Stroke stroke, List<String> externalImageList) {
        if(stroke != null)
        {
            if(stroke.getGraphicFill() != null)
            {
                updateGraphicalSymbol(resourceLocator, stroke.getGraphicFill().graphicalSymbols(), externalImageList);
            }

            if(stroke.getGraphicStroke() != null)
            {
                updateGraphicalSymbol(resourceLocator, stroke.getGraphicStroke().graphicalSymbols(), externalImageList);
            }
        }
    }

    /**
     * Update graphical symbol.
     *
     * @param resourceLocator the resource locator
     * @param graphicalSymbolList the graphical symbol list
     * @param externalImageList the external image list
     */
    private static void updateGraphicalSymbol(URL resourceLocator,
            List<GraphicalSymbol> graphicalSymbolList,
            List<String> externalImageList)
    {
        for(GraphicalSymbol symbol : graphicalSymbolList)
        {
            if(symbol instanceof ExternalGraphic)
            {
                ExternalGraphicImpl externalGraphic = (ExternalGraphicImpl) symbol;
                OnLineResourceImpl onlineResource = (OnLineResourceImpl) externalGraphic.getOnlineResource();

                String currentValue = null;
                try {
                    currentValue = onlineResource.getLinkage().toURL().toExternalForm();
                } catch (MalformedURLException e) {
                    ConsoleManager.getInstance().exception(SLDExternalImages.class, e);
                }

                if(resourceLocator == null)
                {
                    // Just report back the external image
                    URI uri = null;
                    try {
                        uri = new URI(currentValue);
                    } catch (URISyntaxException e) {
                        ConsoleManager.getInstance().exception(SLDExternalImages.class, e);
                    }
                    externalImageList.add(uri.toASCIIString());
                }
                else
                {
                    String prefix = resourceLocator.toExternalForm();
                    try {
                        if(currentValue.startsWith(prefix))
                        {
                            currentValue = currentValue.substring(prefix.length());

                            OnLineResourceImpl updatedOnlineResource = new OnLineResourceImpl();
                            URI uri = new URI(currentValue);
                            updatedOnlineResource.setLinkage(uri);
                            externalGraphic.setOnlineResource(updatedOnlineResource);

                            externalGraphic.setURI(uri.toASCIIString());
                            externalImageList.add(uri.toASCIIString());
                        }
                    } catch (URISyntaxException e) {
                        ConsoleManager.getInstance().exception(SLDExternalImages.class, e);
                    }
                }
            }
        }
    }
}
