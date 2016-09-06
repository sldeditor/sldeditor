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
package com.sldeditor.common.output.impl;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import javax.xml.transform.TransformerException;

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
import org.geotools.styling.SLDTransformer;
import org.geotools.styling.Stroke;
import org.geotools.styling.Style;
import org.geotools.styling.StyledLayer;
import org.geotools.styling.StyledLayerDescriptor;
import org.geotools.styling.Symbolizer;
import org.geotools.styling.UserLayer;
import org.geotools.styling.UserLayerImpl;
import org.geotools.styling.visitor.DuplicatingStyleVisitor;
import org.opengis.style.GraphicalSymbol;

import com.sldeditor.common.console.ConsoleManager;
import com.sldeditor.common.output.SLDWriterInterface;

/**
 * Class that converts an SLD stored as a StyledLayerDescriptor to a SLD formatted string.
 * 
 * @author Robert Ward (SCISYS)
 */
public class SLDWriterImpl implements SLDWriterInterface {

    /**
     * Default constructor.
     */
    public SLDWriterImpl()
    {
    }

    /**
     * Encode sld to a string.
     *
     * @param resourceLocator the resource locator
     * @param sld the sld
     * @return the string
     */
    @Override
    public String encodeSLD(URL resourceLocator, StyledLayerDescriptor sld)
    {
        String xml = "";

        if(sld != null)
        {
            DuplicatingStyleVisitor duplicator = new DuplicatingStyleVisitor();
            sld.accept(duplicator);
            StyledLayerDescriptor sldCopy = (StyledLayerDescriptor)duplicator.getCopy();

            if(resourceLocator != null)
            {
                updateOnlineResources(resourceLocator, sldCopy);
            }

            SLDTransformer transformer = new SLDTransformer();
            transformer.setIndentation(2);
            try {
                xml = transformer.transform(sldCopy);
            } catch (TransformerException e) {
                ConsoleManager.getInstance().exception(this, e);
            }
        }

        return xml;
    }

    /**
     * Update online resources.
     *
     * @param resourceLocator the resource locator
     * @param sldCopy the sld copy
     */
    private void updateOnlineResources(URL resourceLocator, StyledLayerDescriptor sldCopy) {
        for(StyledLayer styledLayer : sldCopy.layers())
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
                                        updateGraphicalSymbol(resourceLocator, point.getGraphic().graphicalSymbols());
                                    }

                                }
                                else if(symbolizer instanceof LineSymbolizer)
                                {
                                    LineSymbolizer line = (LineSymbolizer) symbolizer;

                                    updateStroke(resourceLocator, line.getStroke());
                                }
                                else if(symbolizer instanceof PolygonSymbolizer)
                                {
                                    PolygonSymbolizer polygon = (PolygonSymbolizer) symbolizer;

                                    updateStroke(resourceLocator, polygon.getStroke());
                                    updateFill(resourceLocator, polygon.getFill());
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
     */
    private void updateFill(URL resourceLocator, Fill fill) {
        if(fill != null)
        {
            if(fill.getGraphicFill() != null)
            {
                updateGraphicalSymbol(resourceLocator, fill.getGraphicFill().graphicalSymbols());
            }
        }
    }

    /**
     * Update stroke.
     *
     * @param resourceLocator the resource locator
     * @param stroke the stroke
     */
    private void updateStroke(URL resourceLocator, Stroke stroke) {
        if(stroke != null)
        {
            if(stroke.getGraphicFill() != null)
            {
                updateGraphicalSymbol(resourceLocator, stroke.getGraphicFill().graphicalSymbols());
            }

            if(stroke.getGraphicStroke() != null)
            {
                updateGraphicalSymbol(resourceLocator, stroke.getGraphicStroke().graphicalSymbols());
            }
        }
    }

    /**
     * Update graphical symbol.
     *
     * @param resourceLocator the resource locator
     * @param graphicalSymbolList the graphical symbol list
     */
    private void updateGraphicalSymbol(URL resourceLocator, List<GraphicalSymbol> graphicalSymbolList) {
        for(GraphicalSymbol symbol : graphicalSymbolList)
        {
            if(symbol instanceof ExternalGraphic)
            {
                ExternalGraphicImpl externalGraphic = (ExternalGraphicImpl) symbol;
                OnLineResourceImpl onlineResource = (OnLineResourceImpl) externalGraphic.getOnlineResource();

                String prefix = resourceLocator.toExternalForm();
                try {
                    String currentValue = onlineResource.getLinkage().toURL().toExternalForm();

                    if(currentValue.startsWith(prefix))
                    {
                        currentValue = currentValue.substring(prefix.length());

                        OnLineResourceImpl updatedOnlineResource = new OnLineResourceImpl();
                        URI uri = new URI(currentValue);
                        updatedOnlineResource.setLinkage(uri);
                        externalGraphic.setOnlineResource(updatedOnlineResource);

                        externalGraphic.setURI(uri.toASCIIString());
                    }
                } catch (MalformedURLException e) {
                    ConsoleManager.getInstance().exception(this, e);
                } catch (URISyntaxException e) {
                    ConsoleManager.getInstance().exception(this, e);
                }
            }
        }
    }
}
