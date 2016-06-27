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
import com.sldeditor.exportdata.esri.keys.symbols.CartographicLineSymbolKeys;
import com.sldeditor.exportdata.esri.keys.symbols.CommonSymbolKeys;

/**
 * Converts an Esri CartographicLineSymbol to its SLD equivalent.
 * 
 * @author Robert Ward (SCISYS)
 */
public class CartographicLineSymbol extends BaseSymbol implements EsriSymbolInterface,
EsriLineSymbolInterface {

    /**
     * Gets the name.
     *
     * @return the name
     */
    @Override
    public String getName() {
        return CartographicLineSymbolKeys.CARTOGRAPHIC_LINE_SYMBOL;
    }

    /**
     * Convert.
     *
     * @param element the element
     * @return the list of strokes
     */
    @SuppressWarnings("deprecation")
    @Override
    public List<Stroke> convert(JsonElement element) {

        if(element == null) return null;

        JsonObject obj = element.getAsJsonObject();

        List<Stroke> strokeList = new ArrayList<Stroke>();

        double width = getDouble(obj, CommonSymbolKeys.WIDTH);

        Stroke stroke = styleFactory.createStroke(getColour(obj.get(CommonSymbolKeys.COLOUR)), ff.literal(width));

        // Line dash pattern
        JsonElement templateElement = obj.get(CartographicLineSymbolKeys.TEMPLATE);
        if(templateElement != null)
        {
            JsonArray templateArray = templateElement.getAsJsonArray();
            List<Float> patternList = new ArrayList<Float>();

            for(int index = 0; index < templateArray.size(); index ++)
            {
                JsonObject patternElement = templateArray.get(index).getAsJsonObject();
                float mark = 0.0f;
                JsonElement markElement = patternElement.get(CartographicLineSymbolKeys.MARK);
                if(markElement != null)
                {
                    mark = markElement.getAsFloat();
                }
                patternList.add(mark);

                float gap = 0.0f;
                JsonElement gapElement = patternElement.get(CartographicLineSymbolKeys.GAP);
                if(gapElement != null)
                {
                    gap = gapElement.getAsFloat();
                }
                patternList.add(gap);
            }

            float[] dashArray = new float[patternList.size()];

            int index = 0;
            for(Float value : patternList)
            {
                dashArray[index] = value;
                index ++;
            }

            stroke.setDashArray(dashArray);

            // Start of line offset
            JsonElement lineStartOffsetElement = obj.get(CartographicLineSymbolKeys.LINE_START_OFFSET);

            if(lineStartOffsetElement != null)
            {
                stroke.setDashOffset(ff.literal(lineStartOffsetElement.getAsDouble()));
            }
        }

        strokeList.add(stroke);

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

        JsonObject obj = element.getAsJsonObject();

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
