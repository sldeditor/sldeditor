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
import org.geotools.styling.Font;
import org.geotools.styling.TextSymbolizer;
import org.junit.Test;

import com.google.gson.JsonElement;
import com.sldeditor.common.defaultsymbol.DefaultSymbols;
import com.sldeditor.importdata.esri.options.MXDOptions;
import com.sldeditor.importdata.esri.symbols.TextSymbol;
import com.sldeditor.test.unit.importdata.esri.ParserUtils;

/**
 * Unit test for TextSymbol class.
 * 
 * @author Robert Ward (SCISYS)
 *
 */
public class TextSymbolTest {

    @Test
    public void testConvertJsonElement() {
        // Tested by calling SimpleMarkerSymbol.convert(org.geotools.styling.Rule, com.google.gson.JsonElement, java.lang.String, int)
    }

    @Test
    public void testConvertRuleJsonElementStringInt() {
        MXDOptions.getInstance().setFontSizeFactor(0);
        TextSymbol symbol = new TextSymbol();

        int transparency = 1;

        JsonElement json = ParserUtils.readTestData("/TextSymbol.json");

        TextSymbolizer text = null;

        symbol.convert(text, json, transparency);

        text = DefaultSymbols.createDefaultTextSymbolizer();
        
        symbol.convert(text, json, transparency);

        SupportingUtils.outputSymbolizer(text);

        Font font = text.getFont();
        assertEquals("Arial", SupportingUtils.getExpressionValue(font.getFamily().get(0)));
        assertEquals(Double.valueOf(8.0), (Double)SupportingUtils.getExpressionValue(font.getSize()));
        assertEquals("normal", (String)SupportingUtils.getExpressionValue(font.getWeight()));
        Fill fill = text.getFill();
        String strokeColour = ((LiteralExpressionImpl)fill.getColor()).toString();
        assertEquals("#005CE6", strokeColour);
    }
}
