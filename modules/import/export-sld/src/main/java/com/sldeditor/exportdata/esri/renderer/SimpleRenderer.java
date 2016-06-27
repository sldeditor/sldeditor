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
package com.sldeditor.exportdata.esri.renderer;

import java.util.List;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.NamedLayer;
import org.geotools.styling.Rule;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactoryImpl;
import org.geotools.styling.StyledLayerDescriptor;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sldeditor.exportdata.esri.keys.renderer.CommonRendererKeys;
import com.sldeditor.exportdata.esri.keys.renderer.SimpleRendererKeys;
import com.sldeditor.exportdata.esri.symbols.SymbolManager;

/**
 * Converts an Esri SimpleRenderer to its SLD equivalent.
 * 
 * @author Robert Ward (SCISYS)
 */
public class SimpleRenderer implements EsriRendererInterface {

    private static StyleFactoryImpl styleFactory = (StyleFactoryImpl) CommonFactoryFinder.getStyleFactory();

    /* (non-Javadoc)
     * @see com.sldeditor.convert.esri.EsriRendererInterface#getName()
     */
    @Override
    public String getName() {
        return SimpleRendererKeys.RENDERER_SIMPLE;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.convert.esri.renderer.EsriRendererInterface#convert(com.google.gson.JsonObject, java.lang.String, double, double, int)
     */
    @Override
    public StyledLayerDescriptor convert(JsonObject json, String layerName, double minScale, double maxScale, int transparency) {
        StyledLayerDescriptor sld = styleFactory.createStyledLayerDescriptor();

        NamedLayer namedLayer = styleFactory.createNamedLayer();

        String label = json.get(CommonRendererKeys.LABEL).getAsString();
        namedLayer.setName(label);
        sld.addStyledLayer(namedLayer);

        Style style = styleFactory.createStyle();
        namedLayer.addStyle(style);

        List<FeatureTypeStyle> ftsList = style.featureTypeStyles();

        // style.setAbstract(json.get(IntermediateFileKeys.DESCRIPTION).getAsString());

        FeatureTypeStyle fts = styleFactory.createFeatureTypeStyle();

        ftsList.add(fts);

        Rule rule = styleFactory.createRule();

        if(minScale > 0.0)
        {
            rule.setMinScaleDenominator(minScale);
        }

        if(maxScale > 0.0)
        {
            rule.setMaxScaleDenominator(maxScale);
        }

        JsonElement jsonElement = json.get(CommonRendererKeys.SYMBOL);
        rule.setName(label);
        SymbolManager.getInstance().convertSymbols(rule, layerName, transparency, jsonElement);

        fts.rules().add(rule);

        return sld;
    }
}
