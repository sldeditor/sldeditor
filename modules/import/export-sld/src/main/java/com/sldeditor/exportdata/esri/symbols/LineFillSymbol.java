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

import org.geotools.styling.AnchorPoint;
import org.geotools.styling.Displacement;
import org.geotools.styling.Fill;
import org.geotools.styling.Mark;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.Stroke;
import org.geotools.styling.Symbolizer;
import org.opengis.filter.expression.Expression;
import org.opengis.style.GraphicFill;
import org.opengis.style.GraphicalSymbol;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sldeditor.common.console.ConsoleManager;
import com.sldeditor.exportdata.esri.keys.symbols.CommonSymbolKeys;
import com.sldeditor.exportdata.esri.keys.symbols.LineFillSymbolKeys;

/**
 * Converts an Esri LineFillSystem to its SLD equivalent.
 * 
 * @author Robert Ward (SCISYS)
 */
public class LineFillSymbol extends BaseSymbol implements EsriSymbolInterface,
EsriFillSymbolInterface {

    /**
     * Gets the name.
     *
     * @return the name
     */
    @Override
    public String getName() {
        return LineFillSymbolKeys.LINE_FILL_SYMBOL;
    }

    /**
     * Convert to fill.
     *
     * @param layerName the layer name
     * @param element the element
     * @param transparency the transparency
     * @return the list
     */
    /* (non-Javadoc)
     * @see com.sldeditor.convert.esri.symbols.EsriFillSymbolInterface#convertToFill(java.lang.String, com.google.gson.JsonElement, int)
     */
    @Override
    public List<Symbolizer> convertToFill(String layerName, JsonElement element, int transparency) {

        if (layerName == null)
        {
            return null;
        }

        if (element == null)
        {
            return null;
        }

        List<Symbolizer> symbolizerList = new ArrayList<Symbolizer>();

        JsonObject obj = element.getAsJsonObject();

        Expression size = ff.literal(getDouble(obj, LineFillSymbolKeys.SEPARATION));
        Expression opacity = null;
        double lineAngle = normaliseAngle(getDouble(obj, CommonSymbolKeys.ANGLE));

        Expression rotation = null;
        AnchorPoint anchorPoint = null;
        Displacement displacement = null;

        Expression fillColour = getColour(obj.get(LineFillSymbolKeys.FILL_COLOUR));
        Expression fillColourOpacity = null;

        Expression join = null;
        Expression cap = null;
        float[] dashes = null;
        Expression offset = null;

        Expression width = ff.literal(1.0);

        Stroke outlineStroke = null;
        
        List<Stroke> strokeList = SymbolManager.getInstance().getStrokeList(obj.get(LineFillSymbolKeys.OUTLINE));
        // TODO
        if((strokeList != null) && (strokeList.size() == 1))
        {
            outlineStroke = strokeList.get(0);

            width = outlineStroke.getWidth();
        }

        Expression wellKnownName = null;

        if(isDoubleEqual(lineAngle, 0.0) || isDoubleEqual(lineAngle, 180.0))
        {
            wellKnownName = ff.literal("shape://horline"); 
        }
        else if(isDoubleEqual(lineAngle, 90.0) || isDoubleEqual(lineAngle, 270.0))
        {
            wellKnownName = ff.literal("shape://vertline");
        }
        else if(isDoubleEqual(lineAngle, 45.0) || isDoubleEqual(lineAngle, 225.0))
        {
            wellKnownName = ff.literal("shape://slash");
        }
        else if(isDoubleEqual(lineAngle, 135.0) || isDoubleEqual(lineAngle, 315.0))
        {
            wellKnownName = ff.literal("shape://backslash");
        }
        else
        {
            wellKnownName = ff.literal("shape://vertline");
            rotation = ff.literal(lineAngle);
        }

        Fill fill = null;
        Stroke markStroke = styleFactory.stroke(fillColour, fillColourOpacity, width, join, cap, dashes, offset);
        Mark mark = styleFactory.createMark(wellKnownName, markStroke, fill, size, rotation);

        List<GraphicalSymbol> symbolList = new ArrayList<GraphicalSymbol>();

        symbolList.add(mark);

        GraphicFill graphicFill = styleFactory.graphicFill(symbolList, opacity, size, rotation, anchorPoint, displacement);

        Fill completeFill = styleFactory.fill(graphicFill, null, null);

        PolygonSymbolizer polygonSymbolizer = styleFactory.createPolygonSymbolizer();
        polygonSymbolizer.setFill(completeFill);
        polygonSymbolizer.setStroke(outlineStroke);

        symbolizerList.add(polygonSymbolizer);

        return symbolizerList;
    }

    /**
     * Checks if is double values are equal.
     *
     * @param value the value
     * @param valueToCompare the value to compare
     * @return true, if is double equal
     */
    private static boolean isDoubleEqual(double value, double valueToCompare) {
        return (Math.abs(value - valueToCompare) < 0.001);
    }

    /**
     * Convert.
     *
     * @param rule the rule
     * @param element the element
     * @param layerName the layer name
     * @param transparency the transparency
     */
    /* (non-Javadoc)
     * @see com.sldeditor.convert.esri.symbols.EsriSymbolInterface#convert(org.geotools.styling.Rule, com.google.gson.JsonElement, java.lang.String, int)
     */
    @Override
    public void convert(Rule rule, JsonElement element, String layerName, int transparency) {
        ConsoleManager.getInstance().error(this, this.getClass().getName() + " : convert() not implemented yet");
    }

}
