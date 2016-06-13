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

import java.util.List;

import org.geotools.filter.LiteralExpressionImpl;
import org.geotools.styling.Fill;
import org.geotools.styling.Graphic;
import org.geotools.styling.MarkImpl;
import org.geotools.styling.Stroke;
import org.junit.Test;
import org.opengis.style.GraphicalSymbol;

import com.google.gson.JsonElement;
import com.sldeditor.importdata.esri.symbols.CharacterMarkerSymbol;
import com.sldeditor.test.unit.importdata.esri.ParserUtils;

/**
 * Unit test for CharacterMarkerSymbol class.
 * 
 * @author Robert Ward (SCISYS)
 *
 */
public class CharacterMarkerSymbolTest {

    @Test
    public void testConvertJsonElement() {
        CharacterMarkerSymbol symbol = new CharacterMarkerSymbol();

        JsonElement json = ParserUtils.readTestData("/CharacterMarkerSymbol.json");

        assertNull(symbol.convert(null));

        List<Graphic> graphicList = symbol.convert(json);

        Graphic graphic = graphicList.get(0);
        assertTrue(Math.abs(
                Double.valueOf(16.0) - (Double) SupportingUtils.getExpressionValue(graphic.getSize())) < 0.001);
        assertTrue(Math.abs(Double.valueOf(135.0)
                - (Double) SupportingUtils.getExpressionValue(graphic.getRotation())) < 0.001);
        assertTrue(Math.abs(Double.valueOf(2.0) - (Double) SupportingUtils.getExpressionValue(
                graphic.getDisplacement().getDisplacementX())) < 0.001);
        assertTrue(Math.abs(Double.valueOf(6.0) - (Double) SupportingUtils.getExpressionValue(
                graphic.getDisplacement().getDisplacementY())) < 0.001);

        GraphicalSymbol graphicalSymbol = graphic.graphicalSymbols().get(0);

        MarkImpl mark = (MarkImpl) graphicalSymbol;

        assertEquals("ttf://Symbol#221", (String) SupportingUtils.getExpressionValue(mark.getWellKnownName()));

        Stroke stroke = mark.getStroke();
        assertNull(stroke);

        Fill fill = mark.getFill();

        String fillColour = ((LiteralExpressionImpl) fill.getColor()).toString();
        assertEquals("#E60000", fillColour);
    }

    @Test
    public void testConvertRuleJsonElementStringInt() {
        // Does nothing
    }

}
