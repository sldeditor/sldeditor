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

import com.sldeditor.common.data.traverse.TraverseSymbolizersInterface;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.geotools.styling.Fill;
import org.geotools.styling.LineSymbolizer;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.RasterSymbolizer;
import org.geotools.styling.Stroke;
import org.geotools.styling.StyledLayerDescriptor;
import org.geotools.styling.Symbolizer;

/**
 * The Class SLDExternalImages.
 *
 * @author Robert Ward (SCISYS)
 */
public class SLDExternalImages {

    /** The update graphic symbol. */
    private static ProcessGraphicSymbolInterface updateGraphicSymbol = new UpdateGraphicalSymbol();

    /** Instantiates a new SLD external images. */
    private SLDExternalImages() {
        // Default constructor
    }

    /**
     * Update online resources.
     *
     * @param resourceLocator the resource locator
     * @param sld the sld
     */
    public static void updateOnlineResources(URL resourceLocator, StyledLayerDescriptor sld) {

        List<String> externalImageList = new ArrayList<>();

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

        List<String> externalImageList = new ArrayList<>();

        externalGraphicSymbolVisitor(resourceLocator, sld, externalImageList, updateGraphicSymbol);

        return externalImageList;
    }

    /**
     * Find the SLD graphical symbols.
     *
     * @param resourceLocator the resource locator
     * @param sld the sld
     * @param externalImageList the external image list
     * @param process the process
     */
    private static void externalGraphicSymbolVisitor(
            URL resourceLocator,
            StyledLayerDescriptor sld,
            List<String> externalImageList,
            ProcessGraphicSymbolInterface process) {

        if (sld != null) {
            SLDUtils.traverseSymbolizers(
                    sld,
                    new TraverseSymbolizersInterface() {

                        @Override
                        public void rasterSymbolizerCallback(RasterSymbolizer symbolizer) {
                            processSymbolizer(
                                    resourceLocator, externalImageList, process, symbolizer);
                        }

                        @Override
                        public void pointSymbolizerCallback(PointSymbolizer symbolizer) {
                            processSymbolizer(
                                    resourceLocator, externalImageList, process, symbolizer);
                        }

                        @Override
                        public void lineSymbolizerCallback(LineSymbolizer symbolizer) {
                            processSymbolizer(
                                    resourceLocator, externalImageList, process, symbolizer);
                        }

                        @Override
                        public void polygonSymbolizerCallback(PolygonSymbolizer symbolizer) {
                            processSymbolizer(
                                    resourceLocator, externalImageList, process, symbolizer);
                        }
                    });
        }
    }

    /**
     * Process symbolizer.
     *
     * @param resourceLocator the resource locator
     * @param externalImageList the external image list
     * @param process the process
     * @param symbolizer the symbolizer
     */
    private static void processSymbolizer(
            URL resourceLocator,
            List<String> externalImageList,
            ProcessGraphicSymbolInterface process,
            Symbolizer symbolizer) {
        if (process == null) {
            return;
        }

        if (symbolizer instanceof PointSymbolizer) {
            PointSymbolizer point = (PointSymbolizer) symbolizer;

            if (point.getGraphic() != null) {
                process.processGraphicalSymbol(
                        resourceLocator, point.getGraphic().graphicalSymbols(), externalImageList);
            }
        } else if (symbolizer instanceof LineSymbolizer) {
            LineSymbolizer line = (LineSymbolizer) symbolizer;

            updateStroke(resourceLocator, line.getStroke(), externalImageList, process);
        } else if (symbolizer instanceof PolygonSymbolizer) {
            PolygonSymbolizer polygon = (PolygonSymbolizer) symbolizer;

            updateStroke(resourceLocator, polygon.getStroke(), externalImageList, process);
            updateFill(resourceLocator, polygon.getFill(), externalImageList, process);
        }
    }

    /**
     * Update fill.
     *
     * @param resourceLocator the resource locator
     * @param fill the fill
     * @param externalImageList the external image list
     */
    private static void updateFill(
            URL resourceLocator,
            Fill fill,
            List<String> externalImageList,
            ProcessGraphicSymbolInterface process) {
        if ((fill != null) && (fill.getGraphicFill() != null)) {
            process.processGraphicalSymbol(
                    resourceLocator, fill.getGraphicFill().graphicalSymbols(), externalImageList);
        }
    }

    /**
     * Update stroke.
     *
     * @param resourceLocator the resource locator
     * @param stroke the stroke
     * @param externalImageList the external image list
     */
    private static void updateStroke(
            URL resourceLocator,
            Stroke stroke,
            List<String> externalImageList,
            ProcessGraphicSymbolInterface process) {
        if (stroke != null) {
            if (stroke.getGraphicFill() != null) {
                process.processGraphicalSymbol(
                        resourceLocator,
                        stroke.getGraphicFill().graphicalSymbols(),
                        externalImageList);
            }

            if (stroke.getGraphicStroke() != null) {
                process.processGraphicalSymbol(
                        resourceLocator,
                        stroke.getGraphicStroke().graphicalSymbols(),
                        externalImageList);
            }
        }
    }
}
