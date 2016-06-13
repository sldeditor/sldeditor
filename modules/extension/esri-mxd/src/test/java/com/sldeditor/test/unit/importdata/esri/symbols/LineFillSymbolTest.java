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

import java.util.List;

import org.geotools.filter.LiteralExpressionImpl;
import org.geotools.styling.Graphic;
import org.geotools.styling.Mark;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.Stroke;
import org.geotools.styling.Symbolizer;
import org.junit.Test;

import com.google.gson.JsonElement;
import com.sldeditor.importdata.esri.symbols.LineFillSymbol;
import com.sldeditor.test.unit.importdata.esri.ParserUtils;

/**
 * Unit test for LineFillSymbol class.
 * 
 * @author Robert Ward (SCISYS)
 *
 */
public class LineFillSymbolTest {

    @Test
    public void testConvertJsonElement() {
        LineFillSymbol symbol = new LineFillSymbol();

        String layerName = "test layer";
        int transparency = 1;

        JsonElement json = ParserUtils.readTestData("/LineFillSymbol.json");

        assertNull(symbol.convertToFill(null, null, transparency));

        List<Symbolizer> symbolizerList = symbol.convertToFill(layerName, json, transparency);

        assertEquals(1, symbolizerList.size());

        SupportingUtils.outputSymbolizer(symbolizerList);

        PolygonSymbolizer polygon = (PolygonSymbolizer) symbolizerList.get(0);

        Graphic graphicFill = polygon.getFill().getGraphicFill();
        Mark mark = (Mark) graphicFill.graphicalSymbols().get(0);

        assertNull(mark.getFill());
        Stroke stroke = mark.getStroke();
        String strokeColour = ((LiteralExpressionImpl) stroke.getColor()).toString();
        assertEquals("#E60000", strokeColour);

        assertEquals("shape://slash", (String)SupportingUtils.getExpressionValue(mark.getWellKnownName()));
        assertEquals("#00FF00", (String)SupportingUtils.getExpressionValue(polygon.getStroke().getColor()));
    }

    @Test
    public void testConvertRuleJsonElementStringInt() {
        // Does nothing
    }
}
