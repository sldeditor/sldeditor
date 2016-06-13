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
import org.geotools.styling.LineSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.Stroke;
import org.junit.Test;

import com.google.gson.JsonElement;
import com.sldeditor.common.defaultsymbol.DefaultSymbols;
import com.sldeditor.importdata.esri.symbols.SimpleLineSymbol;
import com.sldeditor.test.unit.importdata.esri.ParserUtils;

/**
 * Unit test for SimpleLineSymbol class.
 * 
 * @author Robert Ward (SCISYS)
 *
 */
public class SimpleLineSymbolTest {

    @Test
    public void testConvertJsonElement() {
        // Tested by calling SimpleLineSymbol.convert(org.geotools.styling.Rule, com.google.gson.JsonElement, java.lang.String, int)
    }

    @Test
    public void testConvertRuleJsonElementStringInt() {
        SimpleLineSymbol symbol = new SimpleLineSymbol();

        String layerName = "test layer";
        int transparency = 1;

        JsonElement json = ParserUtils.readTestData("/SimpleLineSymbol.json");

        Rule rule = null;

        symbol.convert(rule, json, layerName, transparency);

        rule = DefaultSymbols.createNewRule();
        symbol.convert(rule, json, layerName, transparency);

        LineSymbolizer line = (LineSymbolizer) rule.symbolizers().get(0);
        SupportingUtils.outputSymbolizer(line);

        Stroke stroke = line.getStroke();
        String strokeColour = ((LiteralExpressionImpl)stroke.getColor()).toString();
        assertEquals("#00A9E6", strokeColour);
        assertTrue(Math.abs(Double.valueOf(0.4) - (Double)SupportingUtils.getExpressionValue(stroke.getWidth())) < 0.001);
    }
}
