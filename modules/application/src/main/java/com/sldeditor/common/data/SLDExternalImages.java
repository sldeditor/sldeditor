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

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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

/**
 * The Class SLDExternalImages.
 *
 * @author Robert Ward (SCISYS)
 */
public class SLDExternalImages {

    /** The update graphic symbol. */
    private static ProcessGraphicSymbolInterface updateGraphicSymbol = new UpdateGraphicalSymbol();

    /**
     * Update online resources.
     *
     * @param resourceLocator the resource locator
     * @param sld the sld
     */
    public static void updateOnlineResources(URL resourceLocator, StyledLayerDescriptor sld) {

        List<String> externalImageList = new ArrayList<String>();

        externalGraphicSymbolVisitor(resourceLocator, sld, externalImageList, updateGraphicSymbol);
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

        externalGraphicSymbolVisitor(resourceLocator, sld, externalImageList, updateGraphicSymbol);

        return externalImageList;
    }

    /**
     * Find the SLD graphical symbols
     *
     * @param resourceLocator the resource locator
     * @param sld the sld
     * @param externalImageList the external image list
     * @param process the process
     */
    private static void externalGraphicSymbolVisitor(URL resourceLocator,
            StyledLayerDescriptor sld, List<String> externalImageList,
            ProcessGraphicSymbolInterface process) {
        if (sld == null) {
            return;
        }

        if (process == null) {
            return;
        }

        for (StyledLayer styledLayer : sld.layers()) {
            List<Style> styles = null;
            if (styledLayer instanceof NamedLayer) {
                NamedLayerImpl namedLayer = (NamedLayerImpl) styledLayer;
                styles = namedLayer.styles();
            } else if (styledLayer instanceof UserLayer) {
                UserLayerImpl userLayer = (UserLayerImpl) styledLayer;
                styles = userLayer.userStyles();
            }

            if (styles != null) {
                for (Style style : styles) {
                    for (FeatureTypeStyle fts : style.featureTypeStyles()) {
                        for (Rule rule : fts.rules()) {
                            for (Symbolizer symbolizer : rule.symbolizers()) {
                                if (symbolizer instanceof PointSymbolizer) {
                                    PointSymbolizer point = (PointSymbolizer) symbolizer;

                                    if (point.getGraphic() != null) {
                                        process.processGraphicalSymbol(resourceLocator,
                                                point.getGraphic().graphicalSymbols(),
                                                externalImageList);
                                    }
                                } else if (symbolizer instanceof LineSymbolizer) {
                                    LineSymbolizer line = (LineSymbolizer) symbolizer;

                                    updateStroke(resourceLocator, line.getStroke(),
                                            externalImageList,
                                            process);
                                } else if (symbolizer instanceof PolygonSymbolizer) {
                                    PolygonSymbolizer polygon = (PolygonSymbolizer) symbolizer;

                                    updateStroke(resourceLocator, polygon.getStroke(),
                                            externalImageList,
                                            process);
                                    updateFill(resourceLocator, polygon.getFill(),
                                            externalImageList,
                                            process);
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
    private static void updateFill(URL resourceLocator, Fill fill, List<String> externalImageList,
            ProcessGraphicSymbolInterface process) {
        if (fill != null) {
            if (fill.getGraphicFill() != null) {
                process.processGraphicalSymbol(resourceLocator, fill.getGraphicFill().graphicalSymbols(),
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
    private static void updateStroke(URL resourceLocator, Stroke stroke,
            List<String> externalImageList,
            ProcessGraphicSymbolInterface process) {
        if (stroke != null) {
            if (stroke.getGraphicFill() != null) {
                process.processGraphicalSymbol(resourceLocator, stroke.getGraphicFill().graphicalSymbols(),
                        externalImageList);
            }

            if (stroke.getGraphicStroke() != null) {
                process.processGraphicalSymbol(resourceLocator, stroke.getGraphicStroke().graphicalSymbols(),
                        externalImageList);
            }
        }
    }

}
