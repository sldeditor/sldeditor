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

import org.geotools.styling.Mark;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.Rule;
import org.junit.Test;

import com.google.gson.JsonElement;
import com.sldeditor.common.defaultsymbol.DefaultSymbols;
import com.sldeditor.importdata.esri.symbols.MultiLayerMarkerSymbol;
import com.sldeditor.test.unit.importdata.esri.ParserUtils;

/**
 * Unit test for MultiLayerMarkerSymbolTest class.
 * 
 * @author Robert Ward (SCISYS)
 *
 */
public class MultiLayerMarkerSymbolTest {

    @Test
    public void testConvertRuleJsonElementStringInt() {
        MultiLayerMarkerSymbol symbol = new MultiLayerMarkerSymbol();

        String layerName = "test layer";
        int transparency = 1;

        JsonElement json = ParserUtils.readTestData("/MultiLayerMarkerSymbol.json");

        Rule rule = null;

        symbol.convert(rule, json, layerName, transparency);

        rule = DefaultSymbols.createNewRule();
        symbol.convert(rule, json, layerName, transparency);

        assertEquals(2, rule.symbolizers().size());

        PointSymbolizer point1 = (PointSymbolizer) rule.symbolizers().get(0);
        SupportingUtils.outputSymbolizer(point1);
        PointSymbolizer point2 = (PointSymbolizer) rule.symbolizers().get(1);
        SupportingUtils.outputSymbolizer(point2);

        // Point symbol 1
        Mark mark = (Mark) point1.getGraphic().graphicalSymbols().get(0);
        assertEquals("ttf://Symbol#221", (String) SupportingUtils.getExpressionValue(mark.getWellKnownName()));
        assertEquals("#E60000", (String) SupportingUtils.getExpressionValue(mark.getFill().getColor()));

        // Point symbol 2
        mark = (Mark) point2.getGraphic().graphicalSymbols().get(0);
        assertEquals("ttf://Symbol#190", (String) SupportingUtils.getExpressionValue(mark.getWellKnownName()));
        assertEquals("#E600E6", (String) SupportingUtils.getExpressionValue(mark.getFill().getColor()));
    }
}
