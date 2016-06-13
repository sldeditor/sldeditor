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
package com.sldeditor.test.unit.importdata.esri.symbols;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.geotools.filter.LiteralExpressionImpl;
import org.geotools.styling.Displacement;
import org.geotools.styling.Fill;
import org.geotools.styling.Graphic;
import org.geotools.styling.MarkImpl;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.Stroke;
import org.junit.Test;
import org.opengis.style.GraphicalSymbol;

import com.google.gson.JsonElement;
import com.sldeditor.common.defaultsymbol.DefaultSymbols;
import com.sldeditor.importdata.esri.symbols.SimpleMarkerSymbol;
import com.sldeditor.test.unit.importdata.esri.ParserUtils;

/**
 * Unit test for SimpleMarkerSymbol class.
 * 
 * @author Robert Ward (SCISYS)
 *
 */
public class SimpleMarkerSymbolTest {

    @Test
    public void testConvertJsonElement() {
        // Tested by calling SimpleMarkerSymbol.convert(org.geotools.styling.Rule, com.google.gson.JsonElement, java.lang.String, int)
    }

    @Test
    public void testConvertRuleJsonElementStringInt() {
        SimpleMarkerSymbol symbol = new SimpleMarkerSymbol();

        String layerName = "test layer";
        int transparency = 1;

        JsonElement json = ParserUtils.readTestData("/SimpleMarkerSymbol.json");

        Rule rule = null;

        symbol.convert(rule, json, layerName, transparency);

        rule = DefaultSymbols.createNewRule();
        symbol.convert(rule, json, layerName, transparency);

        PointSymbolizer point = (PointSymbolizer) rule.symbolizers().get(0);
        SupportingUtils.outputSymbolizer(point);

        Graphic graphic = point.getGraphic();

        assertTrue(Math.abs(Double.valueOf(14.0) - (Double)SupportingUtils.getExpressionValue(graphic.getSize())) < 0.001);
        assertTrue(Math.abs(Double.valueOf(5.6) - (Double)SupportingUtils.getExpressionValue(graphic.getRotation())) < 0.001);

        Displacement displacement = graphic.getDisplacement();
        assertTrue(Math.abs(Double.valueOf(3.0) - (Double)SupportingUtils.getExpressionValue(displacement.getDisplacementX())) < 0.001);
        assertTrue(Math.abs(Double.valueOf(5.0) - (Double)SupportingUtils.getExpressionValue(displacement.getDisplacementY())) < 0.001);

        GraphicalSymbol graphicalSymbol = graphic.graphicalSymbols().get(0);
        
        MarkImpl mark = (MarkImpl) graphicalSymbol;
        assertEquals("circle", mark.getWellKnownName().toString());
        Fill fill = mark.getFill();
        String fillColour = ((LiteralExpressionImpl)fill.getColor()).toString();
        assertEquals("#FF0000", fillColour);
        Stroke stroke = mark.getStroke();
        String strokeColour = ((LiteralExpressionImpl)stroke.getColor()).toString();
        assertEquals("#00FF00", strokeColour);
        assertTrue(Math.abs(Double.valueOf(1.1) - (Double)SupportingUtils.getExpressionValue(stroke.getWidth())) < 0.001);
    }
}
