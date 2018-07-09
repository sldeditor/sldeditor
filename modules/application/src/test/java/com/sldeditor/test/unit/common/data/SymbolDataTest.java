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

package com.sldeditor.test.unit.common.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.sldeditor.common.data.SymbolData;
import com.sldeditor.common.defaultsymbol.DefaultSymbols;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.LineSymbolizer;
import org.geotools.styling.NamedLayer;
import org.geotools.styling.Rule;
import org.geotools.styling.Style;
import org.junit.Test;

/**
 * The unit test for SymbolData.
 *
 * <p>{@link com.sldeditor.common.data.SymbolData}
 *
 * @author Robert Ward (SCISYS)
 */
public class SymbolDataTest {

    @Test
    public void testResetData() {
        SymbolData symbolData = new SymbolData();
        symbolData.setSymbolizer(DefaultSymbols.createDefaultLineSymbolizer());
        symbolData.setRule(DefaultSymbols.createNewRule());
        symbolData.setStyle(DefaultSymbols.createNewStyle());
        symbolData.setFeatureTypeStyle(DefaultSymbols.createNewFeatureTypeStyle());
        symbolData.setStyledLayer(DefaultSymbols.createNewNamedLayer());
        symbolData.initialiseSelectedSymbolizerIndex();
        symbolData.initialiseSelectedFTSIndex();
        symbolData.initialiseSelectedRuleIndex();
        symbolData.initialiseSelectedStyledLayerIndex();
        symbolData.initialiseSelectedStyleIndex();

        symbolData.resetData();

        assertNull(symbolData.getSymbolizer());
        assertNull(symbolData.getFeatureTypeStyle());
        assertNull(symbolData.getStyle());
        assertNull(symbolData.getStyledLayer());
        assertNull(symbolData.getRule());
        assertEquals(-1, symbolData.getSelectedFTSIndex());
        assertEquals(-1, symbolData.getSelectedRuleIndex());
        assertEquals(-1, symbolData.getSelectedStyledLayerIndex());
        assertEquals(-1, symbolData.getSelectedStyleIndex());
        assertEquals(-1, symbolData.getSelectedSymbolizerIndex());
    }

    @Test
    public void testUpdate() {
        SymbolData symbolData = new SymbolData();
        symbolData.setSymbolizer(DefaultSymbols.createDefaultLineSymbolizer());
        symbolData.setRule(DefaultSymbols.createNewRule());
        symbolData.setStyle(DefaultSymbols.createNewStyle());
        symbolData.setFeatureTypeStyle(DefaultSymbols.createNewFeatureTypeStyle());
        symbolData.setStyledLayer(DefaultSymbols.createNewNamedLayer());
        symbolData.initialiseSelectedSymbolizerIndex();
        symbolData.initialiseSelectedFTSIndex();
        symbolData.initialiseSelectedRuleIndex();
        symbolData.initialiseSelectedStyledLayerIndex();
        symbolData.initialiseSelectedStyleIndex();

        SymbolData newSymbolData = new SymbolData();
        newSymbolData.update(symbolData);

        assertEquals(newSymbolData.getSymbolizer(), symbolData.getSymbolizer());
        assertEquals(newSymbolData.getFeatureTypeStyle(), symbolData.getFeatureTypeStyle());
        assertEquals(newSymbolData.getStyle(), symbolData.getStyle());
        assertEquals(newSymbolData.getStyledLayer(), symbolData.getStyledLayer());
        assertEquals(newSymbolData.getRule(), symbolData.getRule());
        assertEquals(newSymbolData.getSelectedFTSIndex(), symbolData.getSelectedFTSIndex());
        assertEquals(newSymbolData.getSelectedRuleIndex(), symbolData.getSelectedRuleIndex());
        assertEquals(
                newSymbolData.getSelectedStyledLayerIndex(),
                symbolData.getSelectedStyledLayerIndex());
        assertEquals(newSymbolData.getSelectedStyleIndex(), symbolData.getSelectedStyleIndex());
        assertEquals(
                newSymbolData.getSelectedSymbolizerIndex(),
                symbolData.getSelectedSymbolizerIndex());
    }

    @Test
    public void testSetSymbolizer() {
        SymbolData symbolData = new SymbolData();
        LineSymbolizer symbolizer = DefaultSymbols.createDefaultLineSymbolizer();
        symbolData.setSymbolizer(symbolizer);
        assertEquals(symbolizer, symbolData.getSymbolizer());
    }

    @Test
    public void testSetRule() {
        SymbolData symbolData = new SymbolData();
        Rule rule = DefaultSymbols.createNewRule();
        symbolData.setRule(rule);
        assertEquals(rule, symbolData.getRule());
    }

    @Test
    public void testSetStyle() {
        SymbolData symbolData = new SymbolData();
        Style style = DefaultSymbols.createNewStyle();
        symbolData.setStyle(style);
        assertEquals(style, symbolData.getStyle());
    }

    @Test
    public void testSetFeatureTypeStyle() {
        SymbolData symbolData = new SymbolData();
        FeatureTypeStyle fts = DefaultSymbols.createNewFeatureTypeStyle();
        symbolData.setFeatureTypeStyle(fts);
        assertEquals(fts, symbolData.getFeatureTypeStyle());
    }

    @Test
    public void testSetStyledLayer() {
        SymbolData symbolData = new SymbolData();
        NamedLayer namedLayer = DefaultSymbols.createNewNamedLayer();
        symbolData.setStyledLayer(namedLayer);
        assertEquals(namedLayer, symbolData.getStyledLayer());
    }

    @Test
    public void testSelectedSymbolizerIndex() {
        SymbolData symbolData = new SymbolData();

        assertEquals(-1, symbolData.getSelectedSymbolizerIndex());
        symbolData.initialiseSelectedSymbolizerIndex();
        assertEquals(0, symbolData.getSelectedSymbolizerIndex());
        symbolData.incrementSelectedSymbolizerIndex();
        symbolData.incrementSelectedSymbolizerIndex();
        assertEquals(2, symbolData.getSelectedSymbolizerIndex());
    }

    @Test
    public void testSelectedFTSIndex() {
        SymbolData symbolData = new SymbolData();

        assertEquals(-1, symbolData.getSelectedFTSIndex());
        symbolData.initialiseSelectedFTSIndex();
        assertEquals(0, symbolData.getSelectedFTSIndex());
        symbolData.incrementSelectedFTSIndex();
        symbolData.incrementSelectedFTSIndex();
        assertEquals(2, symbolData.getSelectedFTSIndex());
    }

    @Test
    public void testSelectedRuleIndex() {
        SymbolData symbolData = new SymbolData();

        assertEquals(-1, symbolData.getSelectedRuleIndex());
        symbolData.initialiseSelectedRuleIndex();
        assertEquals(0, symbolData.getSelectedRuleIndex());
        symbolData.incrementSelectedRuleIndex();
        symbolData.incrementSelectedRuleIndex();
        assertEquals(2, symbolData.getSelectedRuleIndex());
    }

    @Test
    public void testSelectedStyledLayerIndex() {
        SymbolData symbolData = new SymbolData();

        assertEquals(-1, symbolData.getSelectedStyledLayerIndex());
        symbolData.initialiseSelectedStyledLayerIndex();
        assertEquals(0, symbolData.getSelectedStyledLayerIndex());
        symbolData.incrementSelectedStyledLayerIndex();
        symbolData.incrementSelectedStyledLayerIndex();
        assertEquals(2, symbolData.getSelectedStyledLayerIndex());
    }

    @Test
    public void testSelectedStyleIndex() {
        SymbolData symbolData = new SymbolData();

        assertEquals(-1, symbolData.getSelectedStyleIndex());
        symbolData.initialiseSelectedStyleIndex();
        assertEquals(0, symbolData.getSelectedStyleIndex());
        symbolData.incrementSelectedStyleIndex();
        symbolData.incrementSelectedStyleIndex();
        assertEquals(2, symbolData.getSelectedStyleIndex());
    }
}
