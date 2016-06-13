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
package com.sldeditor.importdata.esri.symbols;

import java.util.ArrayList;
import java.util.List;

import org.geotools.styling.LineSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.Stroke;
import org.geotools.styling.Symbolizer;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sldeditor.importdata.esri.keys.symbols.MultiLayerFillSymbolKeys;

/**
 * Converts an Esri MultiLayerFillSymbol to its SLD equivalent.
 * 
 * @author Robert Ward (SCISYS)
 */
public class MultiLayerFillSymbol extends BaseSymbol implements EsriSymbolInterface {

    /* (non-Javadoc)
     * @see com.sldeditor.convert.esri.symbols.EsriSymbolInterface#getName()
     */
    @Override
    public String getName() {
        return MultiLayerFillSymbolKeys.MULTI_LAYER_FILL_SYMBOL;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.convert.esri.symbols.EsriSymbolInterface#convert(Rule, JsonElement)
     */
    @Override
    public void convert(Rule rule, JsonElement element, String layerName, int transparency) {
        if(rule == null) return;
        if(element == null) return;

        JsonArray layerArray = element.getAsJsonArray();

        List<Symbolizer> symbolizerList = rule.symbolizers();

        if(layerArray.size() > 0)
        {
            for(int index = 0; index < layerArray.size(); index ++)
            {
                JsonObject obj = layerArray.get(index).getAsJsonObject();

                // Handle fills
                List<Symbolizer> createdSymbolizerList = SymbolManager.getInstance().getFillSymbol(layerName, obj.get(MultiLayerFillSymbolKeys.FILL), transparency);

                if(symbolizerList != null)
                {
                    symbolizerList.addAll(createdSymbolizerList);
                }

                // Handle strokes
                JsonElement jsonOutlineElement = obj.get(MultiLayerFillSymbolKeys.OUTLINE);

                List<Stroke> strokeList = SymbolManager.getInstance().getStrokeList(jsonOutlineElement);

                if(strokeList != null)
                {
                    List<Symbolizer> createdLineSymbolizerList = new ArrayList<Symbolizer>();
                    for(Stroke stroke : strokeList)
                    {
                        LineSymbolizer lineSymbol = styleFactory.createLineSymbolizer();
                        
                        lineSymbol.setStroke(stroke);
                        
                        createdLineSymbolizerList.add(lineSymbol);
                    }
                    symbolizerList.addAll(createdLineSymbolizerList);
                }
            }
        }
    }
}
