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
package com.sldeditor.exportdata.esri.symbols;

import java.util.ArrayList;
import java.util.List;

import org.geotools.styling.LineSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.Stroke;
import org.geotools.styling.Symbolizer;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sldeditor.exportdata.esri.keys.symbols.MultiLayerLineSymbolKeys;

/**
 * Converts an Esri MultiLayerLineSymbol to its SLD equivalent.
 * 
 * @author Robert Ward (SCISYS)
 */
public class MultiLayerLineSymbol extends BaseSymbol implements EsriSymbolInterface,
EsriLineSymbolInterface, EsriFillSymbolInterface {

    /* (non-Javadoc)
     * @see com.sldeditor.convert.esri.symbols.EsriSymbolInterface#getName()
     */
    @Override
    public String getName() {
        return MultiLayerLineSymbolKeys.MULTI_LAYER_LINE_SYMBOL;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.convert.esri.symbols.EsriLineSymbolInterface#convert(com.google.gson.JsonElement)
     */
    @Override
    public List<Stroke> convert(JsonElement element) {

        if(element == null) return null;

        List<Stroke> strokeList = null;

        JsonArray layerList = element.getAsJsonArray();
        if(layerList.size() > 0)
        {
            strokeList = new ArrayList<Stroke>();

            for(int index = 0; index < layerList.size(); index ++)
            {
                JsonObject obj = layerList.get(index).getAsJsonObject();

                List<Stroke> strokeSymbolList = SymbolManager.getInstance().getStrokeList(obj.get(MultiLayerLineSymbolKeys.LINE));
                if(strokeSymbolList != null)
                {
                    strokeList.addAll(strokeSymbolList);
                }
            }
        }
        return strokeList;
    }

    /**
     * Convert.
     *
     * @param rule the rule
     * @param element the element
     * @param layerName the layer name
     * @param transparency the transparency
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

                List<Stroke> strokeList = null;
                JsonElement jsonOutlineElement = obj.get(MultiLayerLineSymbolKeys.LINE);

                strokeList = SymbolManager.getInstance().getStrokeList(jsonOutlineElement);

                if(strokeList != null)
                {
                    for(Stroke stroke : strokeList)
                    {
                        LineSymbolizer lineSymbolizer = styleFactory.createLineSymbolizer(stroke, null);

                        symbolizerList.add(lineSymbolizer);
                    }
                }
            }
        }
    }

    /* (non-Javadoc)
     * @see com.sldeditor.convert.esri.symbols.EsriFillSymbolInterface#convertToFill(java.lang.String, com.google.gson.JsonElement, int)
     */
    @Override
    public List<Symbolizer> convertToFill(String layerName, JsonElement element, int transparency) {
        if(element == null) return null;

        List<Symbolizer> symbolizerList = null;

        JsonArray layerList = element.getAsJsonArray();
        if(layerList.size() > 0)
        {
            symbolizerList = new ArrayList<Symbolizer>();

            for(int index = 0; index < layerList.size(); index ++)
            {
                JsonElement layerElement = layerList.get(index);

                symbolizerList.addAll(SymbolManager.getInstance().getFillSymbol(layerName, layerElement, transparency));
            }
        }
        return symbolizerList;
    }

}
