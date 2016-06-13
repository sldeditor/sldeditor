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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.geotools.filter.LiteralExpressionImpl;
import org.geotools.styling.Fill;
import org.geotools.styling.Graphic;
import org.geotools.styling.LineSymbolizer;
import org.geotools.styling.Mark;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.Stroke;
import org.junit.Test;

import com.google.gson.JsonElement;
import com.sldeditor.common.defaultsymbol.DefaultSymbols;
import com.sldeditor.importdata.esri.symbols.MultiLayerFillSymbol;
import com.sldeditor.test.unit.importdata.esri.ParserUtils;

/**
 * Unit test for MultiLayerFillSymbol class.
 * 
 * @author Robert Ward (SCISYS)
 *
 */
public class MultiLayerFillSymbolTest {

    @Test
    public void testConvertRuleJsonElementStringInt() {
        MultiLayerFillSymbol symbol = new MultiLayerFillSymbol();

        String layerName = "test layer";
        int transparency = 1;

        JsonElement json = ParserUtils.readTestData("/MultiLayerFillSymbol.json");

        Rule rule = null;

        symbol.convert(rule, json, layerName, transparency);

        rule = DefaultSymbols.createNewRule();
        symbol.convert(rule, json, layerName, transparency);

        assertEquals(4, rule.symbolizers().size());

        PolygonSymbolizer polygon1 = (PolygonSymbolizer) rule.symbolizers().get(0);
        SupportingUtils.outputSymbolizer(polygon1);
        LineSymbolizer line1 = (LineSymbolizer) rule.symbolizers().get(1);
        SupportingUtils.outputSymbolizer(line1);
        PolygonSymbolizer polygon2 = (PolygonSymbolizer) rule.symbolizers().get(2);
        SupportingUtils.outputSymbolizer(polygon2);
        LineSymbolizer line2 = (LineSymbolizer) rule.symbolizers().get(3);
        SupportingUtils.outputSymbolizer(line2);

        Graphic graphicFill = polygon1.getFill().getGraphicFill();
        Mark mark = (Mark) graphicFill.graphicalSymbols().get(0);

        assertNull(mark.getFill());
        Stroke stroke = mark.getStroke();
        String strokeColour = ((LiteralExpressionImpl) stroke.getColor()).toString();
        assertEquals("#00C5FF", strokeColour);

        assertEquals("shape://horline", (String)SupportingUtils.getExpressionValue(mark.getWellKnownName()));
        assertEquals("#00A9E6", (String)SupportingUtils.getExpressionValue(polygon1.getStroke().getColor()));
        assertTrue(Math.abs(Double.valueOf(10.0) - (Double)SupportingUtils.getExpressionValue(graphicFill.getSize())) < 0.001);

        Fill fill = polygon1.getFill();
        assertNull(fill.getColor());

        stroke = polygon1.getStroke();
        strokeColour = ((LiteralExpressionImpl)stroke.getColor()).toString();
        assertEquals("#00A9E6", strokeColour);
        assertTrue(Math.abs(Double.valueOf(0.4) - (Double)SupportingUtils.getExpressionValue(stroke.getWidth())) < 0.001);
    }

}
