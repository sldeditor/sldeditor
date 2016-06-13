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

import org.geotools.filter.LiteralExpressionImpl;
import org.geotools.styling.Fill;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.Stroke;
import org.junit.Test;

import com.google.gson.JsonElement;
import com.sldeditor.common.defaultsymbol.DefaultSymbols;
import com.sldeditor.importdata.esri.symbols.PictureFillSymbol;
import com.sldeditor.test.unit.importdata.esri.ParserUtils;

/**
 * Unit test for PictureFillSymbolTest class.
 * 
 * @author Robert Ward (SCISYS)
 *
 */
public class PictureFillSymbolTest {

    @Test
    public void testConvertJsonElement() {
        // Tested by calling SimpleFillSymbol.convert(org.geotools.styling.Rule, com.google.gson.JsonElement, java.lang.String, int)
    }

    @Test
    public void testConvertRuleJsonElementStringInt() {
        PictureFillSymbol symbol = new PictureFillSymbol();

        String layerName = "test layer";
        int transparency = 1;

        JsonElement json = ParserUtils.readTestData("/PictureFillSymbol.json");

        Rule rule = null;

        symbol.convert(rule, json, layerName, transparency);

        rule = DefaultSymbols.createNewRule();
        symbol.convert(rule, json, layerName, transparency);

        PolygonSymbolizer polygon = (PolygonSymbolizer) rule.symbolizers().get(0);
        SupportingUtils.outputSymbolizer(polygon);

        Fill fill = polygon.getFill();
        String fillColour = ((LiteralExpressionImpl)fill.getColor()).toString();
        assertEquals("#828282", fillColour);
        Stroke stroke = polygon.getStroke();
        String strokeColour = ((LiteralExpressionImpl)stroke.getColor()).toString();
        assertEquals("#828282", strokeColour);
    }

}
