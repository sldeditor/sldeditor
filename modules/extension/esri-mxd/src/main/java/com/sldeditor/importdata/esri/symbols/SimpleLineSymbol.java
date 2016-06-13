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
import org.opengis.filter.expression.Expression;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sldeditor.importdata.esri.keys.symbols.CommonSymbolKeys;
import com.sldeditor.importdata.esri.keys.symbols.SimpleLineSymbolKeys;

/**
 * Converts an Esri SimpleLineSymbol to its SLD equivalent.
 * 
 * @author Robert Ward (SCISYS)
 */
public class SimpleLineSymbol extends BaseSymbol implements EsriSymbolInterface,
EsriLineSymbolInterface {

    /**
     * Gets the name.
     *
     * @return the name
     */
    @Override
    public String getName() {
        return SimpleLineSymbolKeys.SIMPLE_LINE_SYMBOL;
    }

    /**
     * Convert.
     *
     * @param element the element
     * @return the list of strokes
     */
    @Override
    public List<Stroke> convert(JsonElement element) {

        if(element == null) return null;

        JsonObject obj = element.getAsJsonObject();

        List<Stroke> strokeList = new ArrayList<Stroke>();

        double width = getDouble(obj, CommonSymbolKeys.WIDTH);

        Expression colour = getColour(obj.get(CommonSymbolKeys.COLOUR));

        if(colour != null)
        {
            Stroke stroke = styleFactory.createStroke(colour, ff.literal(width));

            strokeList.add(stroke);
        }
        return strokeList;
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

        List<Stroke> strokeList = convert(obj);

        Stroke stroke = null;

        if(!strokeList.isEmpty())
        {
            stroke = strokeList.get(0);
        }

        LineSymbolizer lineSymbolizer = styleFactory.createLineSymbolizer(stroke, null);

        symbolizerList.add(lineSymbolizer);
    }
}
