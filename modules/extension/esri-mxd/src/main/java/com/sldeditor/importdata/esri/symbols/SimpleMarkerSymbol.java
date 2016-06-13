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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geotools.styling.Displacement;
import org.geotools.styling.ExternalGraphic;
import org.geotools.styling.Fill;
import org.geotools.styling.Graphic;
import org.geotools.styling.Mark;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.Stroke;
import org.geotools.styling.Symbol;
import org.geotools.styling.Symbolizer;
import org.opengis.filter.expression.Expression;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sldeditor.importdata.esri.keys.symbols.CommonSymbolKeys;
import com.sldeditor.importdata.esri.keys.symbols.SimpleMarkerSymbolKeys;

/**
 * Converts an Esri SimpleMarkerSymbol to its SLD equivalent.
 * 
 * @author Robert Ward (SCISYS)
 */
public class SimpleMarkerSymbol extends BaseSymbol implements EsriSymbolInterface,
EsriMarkSymbolInterface {

    /** The style map. */
    private static Map<Integer, String> styleMap = new HashMap<Integer, String>();

    // com.esri.arcgis.display.esriSimpleMarkerStyle constants
    private static final int esriSMSCircle   = 0;
    private static final int esriSMSSquare   = 1;
    private static final int esriSMSCross    = 2;
    private static final int esriSMSX        = 3;
    private static final int esriSMSDiamond  = 4;

    /**
     * Instantiates a new simple marker symbol.
     */
    public SimpleMarkerSymbol()
    {
        if(styleMap.isEmpty())
        {
            styleMap.put(esriSMSCircle, "circle");
            styleMap.put(esriSMSCross, "cross");
            styleMap.put(esriSMSDiamond, "circle");
            styleMap.put(esriSMSSquare, "square");
            styleMap.put(esriSMSX, "x");
        }
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    @Override
    public String getName() {
        return SimpleMarkerSymbolKeys.SIMPLE_MARKER_SYMBOL;
    }

    /**
     * Convert.
     *
     * @param element the element
     * @return the graphic marker list
     */
    @Override
    public List<Graphic> convert(JsonElement element) {

        if(element == null) return null;

        JsonObject obj = element.getAsJsonObject();

        List<Graphic> markList = new ArrayList<Graphic>();

        double angle = getDouble(obj, CommonSymbolKeys.ANGLE);
        double outlineSize = getDouble(obj, SimpleMarkerSymbolKeys.OUTLINE_SIZE);
        double size = getDouble(obj, CommonSymbolKeys.SIZE);
        int style = getInt(obj, CommonSymbolKeys.STYLE);
        double xOffset = getDouble(obj, CommonSymbolKeys.X_OFFSET);
        double yOffset = getDouble(obj, CommonSymbolKeys.Y_OFFSET);
        Expression markerColour = getColour(obj.get(CommonSymbolKeys.COLOUR));
        Expression outlineColour = getColour(obj.get(SimpleMarkerSymbolKeys.OUTLINE_COLOUR));

        Expression wellKnownName = ff.literal(styleMap.get(style));
        Stroke stroke = null;

        if(outlineSize > 0.0)
        {
            stroke = styleFactory.createStroke(outlineColour, ff.literal(outlineSize));
        }

        Fill fill = styleFactory.createFill(markerColour);

        Mark mark = styleFactory.createMark(wellKnownName, stroke, fill, ff.literal(size), ff.literal(angle));

        Expression expressionOpacity = null;
        ExternalGraphic[] externalGraphics = null;
        Symbol[] symbols = null;

        Mark[] marks = new Mark[1];
        marks[0] = mark;

        Graphic graphic = styleFactory.createGraphic(externalGraphics, marks, symbols, expressionOpacity, ff.literal(size), ff.literal(angle));

        // Set offset
        if((xOffset > 0.0) && (yOffset > 0.0))
        {
            Displacement displacement = styleFactory.displacement(ff.literal(xOffset), ff.literal(yOffset));
            graphic.setDisplacement(displacement);
        }
        
        markList.add(graphic);

        return markList;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.convert.esri.symbols.EsriSymbolInterface#convert(org.geotools.styling.Rule, com.google.gson.JsonElement, java.lang.String, int)
     */
    @Override
    public void convert(Rule rule, JsonElement element, String layerName, int transparency) {
        if(element == null) return;
        if(rule == null) return;

        List<Symbolizer> symbolizerList = rule.symbolizers();

        List<Graphic> markerList = convert(element);

        if(markerList != null)
        {
            for(Graphic marker : markerList)
            {
                PointSymbolizer pointSymbolizer = styleFactory.createPointSymbolizer(marker, null);

                symbolizerList.add(pointSymbolizer);
            }
        }
    }
}
