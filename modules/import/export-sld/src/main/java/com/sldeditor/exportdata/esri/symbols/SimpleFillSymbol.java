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

import org.geotools.styling.Fill;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.Stroke;
import org.geotools.styling.Symbolizer;
import org.opengis.filter.expression.Expression;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sldeditor.exportdata.esri.keys.symbols.CommonSymbolKeys;
import com.sldeditor.exportdata.esri.keys.symbols.SimpleFillSymbolKeys;

/**
 * Converts an Esri SimpleFillSymbol to its SLD equivalent.
 * 
 * @author Robert Ward (SCISYS)
 */
public class SimpleFillSymbol extends BaseSymbol implements EsriSymbolInterface, EsriFillSymbolInterface {

    /* (non-Javadoc)
     * @see com.sldeditor.convert.esri.symbols.EsriSymbolInterface#getName()
     */
    @Override
    public String getName() {
        return SimpleFillSymbolKeys.SIMPLE_FILL_SYMBOL;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.convert.esri.symbols.EsriSymbolInterface#convert(org.geotools.styling.Rule, com.google.gson.JsonElement, java.lang.String, int)
     */
    @Override
    public void convert(Rule rule, JsonElement element, String layerName, int transparency) {
        if(element == null) return;
        if(rule == null) return;

        JsonObject obj = element.getAsJsonObject();
        @SuppressWarnings("unused")
        int style = getInt(obj, CommonSymbolKeys.STYLE);

        List<Symbolizer> symbolizerList = rule.symbolizers();

        JsonElement jsonOutlineElement = obj.get(SimpleFillSymbolKeys.OUTLINE);
        List<Stroke> strokeList = SymbolManager.getInstance().getStrokeList(jsonOutlineElement);

        Stroke stroke = null;

        if(strokeList != null)
        {
            if(!strokeList.isEmpty())
            {
                stroke = strokeList.get(0);
            }
        }

        Expression fillColour = getColour(obj.get(SimpleFillSymbolKeys.FILL_COLOUR));

        Fill fill = null;

        if(fillColour != null)
        {
            fill = styleFactory.createFill(fillColour, getTransparency(transparency));
        }

        PolygonSymbolizer polygonSymbolizer = styleFactory.createPolygonSymbolizer(stroke, fill, null);

        symbolizerList.add(polygonSymbolizer);
    }

    /* (non-Javadoc)
     * @see com.sldeditor.convert.esri.symbols.EsriFillSymbolInterface#convertToFill(java.lang.String, com.google.gson.JsonElement, int)
     */
    @Override
    public List<Symbolizer> convertToFill(String layerName, JsonElement element, int transparency) {
        if(element == null) return null;

        JsonObject obj = element.getAsJsonObject();

        List<Symbolizer> symbolizerList = new ArrayList<Symbolizer>();
        Expression fillColour = getColour(obj.get(SimpleFillSymbolKeys.FILL_COLOUR));
        Expression transparencyExpression = getTransparency(transparency);
        Fill fill = null;

        if(fillColour != null)
        {
            fill = styleFactory.createFill(fillColour, transparencyExpression);
        }

        PolygonSymbolizer polygon = styleFactory.createPolygonSymbolizer();
        polygon.setStroke(null);
        polygon.setFill(fill);
        symbolizerList.add(polygon);

        return symbolizerList;
    }
}
