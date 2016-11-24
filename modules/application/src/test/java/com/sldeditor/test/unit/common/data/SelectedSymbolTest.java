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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Fill;
import org.geotools.styling.Graphic;
import org.geotools.styling.LineSymbolizer;
import org.geotools.styling.NamedLayer;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.PointSymbolizerImpl;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.PolygonSymbolizerImpl;
import org.geotools.styling.RasterSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactoryImpl;
import org.geotools.styling.StyledLayer;
import org.geotools.styling.StyledLayerDescriptor;
import org.geotools.styling.Symbolizer;
import org.junit.Test;
import org.opengis.filter.FilterFactory;
import org.opengis.style.GraphicalSymbol;
import org.opengis.style.Stroke;

import com.sldeditor.common.data.SLDTreeUpdatedInterface;
import com.sldeditor.common.data.SelectedSymbol;
import com.sldeditor.common.defaultsymbol.DefaultSymbols;

/**
 * The unit test for SelectedSymbol.
 * <p>{@link com.sldeditor.common.data.SelectedSymbol}
 *
 * @author Robert Ward (SCISYS)
 */
public class SelectedSymbolTest {

    class DummySLDTreeUpdated implements SLDTreeUpdatedInterface
    {
        public Object objectOld = null;
        public Object objectNew = null;

        @Override
        public void textUpdated() {
            // Does nothing for this unit test
        }

        @Override
        public void updateNode(Object objectOld, Object objectNew) {
            this.objectOld = objectOld;
            this.objectNew = objectNew;
        }

        @Override
        public void leafSelected() {
            // Does nothing for this unit test
        }

    }

    @Test
    public void testGetSLD() {
        SelectedSymbol.destroyInstance();
        SelectedSymbol instance = SelectedSymbol.getInstance();

        Rule rule = DefaultSymbols.createNewRule();
        FeatureTypeStyle fts = DefaultSymbols.createNewFeatureTypeStyle();
        Style style = DefaultSymbols.createNewStyle();
        NamedLayer namedLayer = DefaultSymbols.createNewNamedLayer();
        StyledLayerDescriptor sld = DefaultSymbols.createNewSLD();
        PolygonSymbolizer symbolizer = DefaultSymbols.createDefaultPolygonSymbolizer();

        instance.createNewSLD(sld);

        instance.setSld(sld);

        System.out.println("Select named layer");
        instance.addNewStyledLayer(namedLayer);

        instance.setStyledLayer(namedLayer);
        assertEquals(namedLayer, instance.getStyledLayer());
        assertNull(instance.getRule());
        assertNull(instance.getStyle());
        assertNull(instance.getFeatureTypeStyle());
        assertNull(instance.getSymbolizer());

        System.out.println("Select style");
        instance.addNewStyle(style);

        instance.setStyle(style);
        assertEquals(namedLayer, instance.getStyledLayer());
        assertEquals(style, instance.getStyle());
        assertNull(instance.getFeatureTypeStyle());
        assertNull(instance.getRule());
        assertNull(instance.getSymbolizer());

        System.out.println("Select feature type style");
        instance.addNewFeatureTypeStyle(fts);

        instance.setFeatureTypeStyle(fts);
        assertEquals(namedLayer, instance.getStyledLayer());
        assertEquals(style, instance.getStyle());
        assertEquals(fts, instance.getFeatureTypeStyle());
        assertNull(instance.getRule());
        assertNull(instance.getSymbolizer());

        System.out.println("Select rule");
        instance.addNewRule(rule);

        instance.setRule(rule);
        assertEquals(namedLayer, instance.getStyledLayer());
        assertEquals(style, instance.getStyle());
        assertEquals(fts, instance.getFeatureTypeStyle());
        assertEquals(rule, instance.getRule());
        assertNull(instance.getSymbolizer());
        assertTrue(instance.hasOnlyOneRule());

        System.out.println("Select symbolizer");
        instance.addSymbolizerToRule(symbolizer);
        instance.addNewRule(DefaultSymbols.createNewRule());
        assertFalse(instance.hasOnlyOneRule());

        assertEquals(-1, instance.getSymbolIndex());
        instance.setSymbolizer(symbolizer);
        assertEquals(namedLayer, instance.getStyledLayer());
        assertEquals(style, instance.getStyle());
        assertEquals(fts, instance.getFeatureTypeStyle());
        assertEquals(rule, instance.getRule());
        assertEquals(symbolizer, instance.getSymbolizer());
        assertEquals(0, instance.getSymbolIndex());

        // Get SLD structure

        StyledLayerDescriptor actualSLD = instance.getSld();
        assertEquals(sld, actualSLD);
        StyledLayer[] actualStyledLayers = actualSLD.getStyledLayers();
        assertEquals(1, actualStyledLayers.length);

        NamedLayer actualNamedLayer = (NamedLayer) actualStyledLayers[0];
        assertEquals(namedLayer, actualNamedLayer);
        Style[] actualStyle = actualNamedLayer.getStyles();
        assertEquals(1, actualStyle.length);
        assertEquals(style, actualStyle[0]);
        assertEquals(1, style.featureTypeStyles().size());

        FeatureTypeStyle actualFeatureTypeStyle = style.featureTypeStyles().get(0);
        assertEquals(fts, actualFeatureTypeStyle);
        List<Rule> actualRules = actualFeatureTypeStyle.rules();
        assertEquals(2, actualRules.size());
        Rule actualRule = actualRules.get(0);
        assertEquals(rule, actualRule);

        assertEquals(1, actualRule.symbolizers().size());
        assertEquals(symbolizer, actualRule.symbolizers().get(0));
    }

    /**
     * Test method for {@link com.sldeditor.common.data.SelectedSymbol#getSymbolList(org.opengis.style.GraphicalSymbol)}.
     */
    @Test
    public void testGetSymbolList() {

        StyleFactoryImpl styleFactory = (StyleFactoryImpl) CommonFactoryFinder.getStyleFactory();
        FilterFactory ff = CommonFactoryFinder.getFilterFactory();

        Stroke stroke = null;
        Fill fill = styleFactory.createFill(ff.literal(DefaultSymbols.defaultColour()));
        GraphicalSymbol symbolToAdd = styleFactory.mark(ff.literal("circle"), fill, stroke);

        List<GraphicalSymbol> symbolList = SelectedSymbol.getInstance().getSymbolList(symbolToAdd);

        assertEquals(1, symbolList.size());

        assertEquals(symbolToAdd, symbolList.get(0));
    }

    /**
     * Test method for {@link com.sldeditor.common.data.SelectedSymbol#getGraphic()}.
     */
    @Test
    public void testGetGraphic() {
        SelectedSymbol.destroyInstance();
        SelectedSymbol instance = SelectedSymbol.getInstance();

        Rule rule = DefaultSymbols.createNewRule();
        FeatureTypeStyle fts = DefaultSymbols.createNewFeatureTypeStyle();
        Style style = DefaultSymbols.createNewStyle();
        NamedLayer namedLayer = DefaultSymbols.createNewNamedLayer();
        StyledLayerDescriptor sld = DefaultSymbols.createNewSLD();
        PolygonSymbolizer symbolizer = DefaultSymbols.createDefaultPolygonSymbolizer();

        instance.createNewSLD(sld);

        instance.setSld(sld);

        System.out.println("Select named layer");
        instance.addNewStyledLayer(namedLayer);
        instance.setStyledLayer(namedLayer);
        instance.addNewStyle(style);
        instance.setStyle(style);
        instance.addNewFeatureTypeStyle(fts);
        instance.setFeatureTypeStyle(fts);
        instance.addNewRule(rule);
        instance.setRule(rule);
        instance.addSymbolizerToRule(symbolizer);
        instance.setSymbolizer(symbolizer);

        Graphic graphic = getGraphic(symbolizer);
        assertNull(graphic);

        Fill graphicFill = DefaultSymbols.createDefaultGraphicFill();

        PolygonSymbolizer newSymbolizer = DefaultSymbols.createDefaultPolygonSymbolizer();
        newSymbolizer.setFill(graphicFill);
        instance.addSymbolizerToRule(newSymbolizer);
        instance.setSymbolizer(newSymbolizer);

        graphic = getGraphic(newSymbolizer);
        assertNotNull(graphic);
        assertTrue(instance.hasFill());
        assertTrue(instance.hasStroke());

        LineSymbolizer lineSymbolizer = DefaultSymbols.createDefaultLineSymbolizer();
        instance.addSymbolizerToRule(lineSymbolizer);
        instance.setSymbolizer(lineSymbolizer);

        graphic = getGraphic(lineSymbolizer);
        assertNull(graphic);
        assertFalse(instance.hasFill());
        assertTrue(instance.hasStroke());

        PointSymbolizer pointSymbolizer = DefaultSymbols.createDefaultPointSymbolizer();
        instance.addSymbolizerToRule(pointSymbolizer);
        instance.setSymbolizer(pointSymbolizer);

        graphic = getGraphic(pointSymbolizer);
        assertNotNull(graphic);

        assertTrue(instance.hasFill());
        assertFalse(instance.hasStroke());
    }

    /**
     * Test method for {@link com.sldeditor.common.data.SelectedSymbol#setTreeUpdateListener(com.sldeditor.common.data.SLDTreeUpdatedInterface)}.
     */
    @Test
    public void testSetTreeUpdateListener() {
    }

    /**
     * Test method for {@link com.sldeditor.common.data.SelectedSymbol#removeRule(org.geotools.styling.Rule)}.
     */
    @Test
    public void testReplacment() {
        SelectedSymbol.destroyInstance();
        SelectedSymbol instance = SelectedSymbol.getInstance();

        Rule rule = DefaultSymbols.createNewRule();
        FeatureTypeStyle fts = DefaultSymbols.createNewFeatureTypeStyle();
        Style style = DefaultSymbols.createNewStyle();
        NamedLayer namedLayer = DefaultSymbols.createNewNamedLayer();
        StyledLayerDescriptor sld = DefaultSymbols.createNewSLD();
        PolygonSymbolizer polygonSymbolizer = DefaultSymbols.createDefaultPolygonSymbolizer();

        instance.createNewSLD(sld);

        instance.setSld(sld);

        instance.addNewStyledLayer(namedLayer);
        instance.setStyledLayer(namedLayer);
        instance.addNewStyle(style);
        instance.setStyle(style);
        instance.addNewFeatureTypeStyle(fts);
        instance.setFeatureTypeStyle(fts);
        instance.addNewRule(rule);
        instance.setRule(rule);
        instance.addSymbolizerToRule(polygonSymbolizer);
        instance.addNewRule(DefaultSymbols.createNewRule());
        instance.setSymbolizer(polygonSymbolizer);

        LineSymbolizer lineSymbolizer = DefaultSymbols.createDefaultLineSymbolizer();
        DummySLDTreeUpdated dummyTreeUpdated = new DummySLDTreeUpdated();

        instance.setTreeUpdateListener(dummyTreeUpdated);
        instance.replaceSymbolizer(lineSymbolizer);

        assertEquals(lineSymbolizer, instance.getSymbolizer());

        assertEquals(dummyTreeUpdated.objectOld, polygonSymbolizer);
        assertEquals(dummyTreeUpdated.objectNew, lineSymbolizer);

        Rule newRule = DefaultSymbols.createNewRule();
        newRule.setTitle("Replacement rule");

        instance.replaceRule(newRule);

        assertEquals(newRule, instance.getRule());
        assertNull(instance.getSymbolizer());

        FeatureTypeStyle newFts = DefaultSymbols.createNewFeatureTypeStyle();
        newFts.setName("Replacement fts");
        instance.replaceFeatureTypeStyle(newFts);

        assertEquals(newFts, instance.getFeatureTypeStyle());
        assertNull(instance.getRule());

        Style newStyle = DefaultSymbols.createNewStyle();
        newStyle.setName("Replacement style");
        instance.replaceStyle(newStyle);

        assertEquals(newStyle, instance.getStyle());
        assertNull(instance.getRule());

        NamedLayer newNamedLayer = DefaultSymbols.createNewNamedLayer();
        newNamedLayer.setName("Replacement named layer");
        instance.replaceStyledLayer(newNamedLayer);

        assertEquals(newNamedLayer, instance.getStyledLayer());
        assertNull(instance.getStyle());
    }

    /**
     * Test method for {@link com.sldeditor.common.data.SelectedSymbol#removeFeatureTypeStyle(org.geotools.styling.FeatureTypeStyle)}.
     */
    @Test
    public void testRemove() {
        SelectedSymbol.destroyInstance();
        SelectedSymbol instance = SelectedSymbol.getInstance();

        Rule rule1 = DefaultSymbols.createNewRule();
        Rule rule2 = DefaultSymbols.createNewRule();
        FeatureTypeStyle fts1 = DefaultSymbols.createNewFeatureTypeStyle();
        FeatureTypeStyle fts2 = DefaultSymbols.createNewFeatureTypeStyle();
        Style style1 = DefaultSymbols.createNewStyle();
        Style style2 = DefaultSymbols.createNewStyle();
        Style style3 = DefaultSymbols.createNewStyle();
        NamedLayer namedLayer = DefaultSymbols.createNewNamedLayer();
        StyledLayerDescriptor sld = DefaultSymbols.createNewSLD();
        PolygonSymbolizer polygonSymbolizer1 = DefaultSymbols.createDefaultPolygonSymbolizer();
        PolygonSymbolizer polygonSymbolizer2 = DefaultSymbols.createDefaultPolygonSymbolizer();
        PolygonSymbolizer polygonSymbolizer3 = DefaultSymbols.createDefaultPolygonSymbolizer();

        instance.createNewSLD(sld);

        instance.setSld(sld);

        instance.addNewStyledLayer(namedLayer);
        instance.setStyledLayer(namedLayer);
        instance.addNewStyledLayer(namedLayer);

        instance.addNewStyle(style1);
        instance.addNewStyle(style2);
        instance.addNewStyle(style3);
        instance.setStyle(style1);
        instance.addNewFeatureTypeStyle(fts1);
        instance.addNewFeatureTypeStyle(fts2);
        instance.setFeatureTypeStyle(fts1);
        instance.addNewRule(rule1);
        instance.setRule(rule1);
        instance.addSymbolizerToRule(polygonSymbolizer1);
        instance.addSymbolizerToRule(polygonSymbolizer2);
        instance.addSymbolizerToRule(polygonSymbolizer3);
        instance.addNewRule(rule2);

        Rule ruleToTest = instance.getRule();
        List<Symbolizer> symbolizerList = ruleToTest.symbolizers();

        assertEquals(3, symbolizerList.size());
        instance.removeSymbolizer(polygonSymbolizer3);
        assertEquals(2, symbolizerList.size());
        instance.removeSymbolizer(polygonSymbolizer3);
        assertEquals(2, symbolizerList.size());
        instance.removeSymbolizer(polygonSymbolizer2);
        assertEquals(1, symbolizerList.size());
        instance.removeSymbolizer(polygonSymbolizer1);
        assertEquals(0, symbolizerList.size());

        List<Rule> ruleList = instance.getFeatureTypeStyle().rules();
        assertEquals(2, ruleList.size());
        instance.removeRule(rule2);
        assertEquals(1, ruleList.size());
        instance.removeRule(rule1);
        assertEquals(0, ruleList.size());
        instance.removeRule(rule2);
        assertEquals(0, ruleList.size());

        List<FeatureTypeStyle> ftsList = instance.getStyle().featureTypeStyles();
        assertEquals(2, ftsList.size());
        instance.removeFeatureTypeStyle(fts2);
        assertEquals(1, ftsList.size());
        instance.removeFeatureTypeStyle(fts2);
        assertEquals(1, ftsList.size());
        instance.removeFeatureTypeStyle(fts1);
        assertEquals(0, ftsList.size());

        List<Style> styleList = ((NamedLayer)instance.getStyledLayer()).styles();
        assertEquals(3, styleList.size());
        instance.removeStyle(style3);
        assertEquals(2, styleList.size());
        instance.removeStyle(style2);
        assertEquals(1, styleList.size());
        instance.removeStyle(style2);
        assertEquals(1, styleList.size());
        instance.removeStyle(style1);
        assertEquals(0, styleList.size());

        instance.removeStyledLayerDescriptor(sld);
        assertEquals(sld, instance.getSld());

        instance.removeUserNamedLayer(namedLayer);
        assertEquals(1, instance.getSld().layers().size());
        instance.removeUserNamedLayer(namedLayer);
        assertEquals(0, instance.getSld().layers().size());
        instance.removeUserNamedLayer(namedLayer);
        assertEquals(0, instance.getSld().layers().size());
    }

    /**
     * Test method for {@link com.sldeditor.common.data.SelectedSymbol#setName(java.lang.String)}.
     */
    @Test
    public void testName() {
        SelectedSymbol.destroyInstance();

        String name = "test name";
        SelectedSymbol.getInstance().setName(name);
        assertEquals(name, SelectedSymbol.getInstance().getName());

        String filename = "test filename";
        SelectedSymbol.getInstance().setFilename(filename);
        assertEquals(filename, SelectedSymbol.getInstance().getFilename());
    }

    @Test
    public void testIsRasterSymbol()
    {
        SelectedSymbol.destroyInstance();
        SelectedSymbol instance = SelectedSymbol.getInstance();

        // Create point symbolizer
        StyledLayerDescriptor sld = DefaultSymbols.createNewPoint();
        instance.createNewSLD(sld);

        instance.setSld(sld);

        // No selection made
        assertFalse(instance.isRasterSymbol());

        Style style = ((NamedLayer)sld.layers().get(0)).styles().get(0);

        instance.setStyle(style);
        assertFalse(instance.isRasterSymbol());

        // Create polygon symbolizer
        SelectedSymbol.destroyInstance();
        instance = SelectedSymbol.getInstance();

        sld = DefaultSymbols.createNewPolygon();
        instance.createNewSLD(sld);

        instance.setSld(sld);

        // No selection made
        assertFalse(instance.isRasterSymbol());

        style = ((NamedLayer)sld.layers().get(0)).styles().get(0);

        instance.setStyle(style);
        assertFalse(instance.isRasterSymbol());

        // Create line symbolizer
        SelectedSymbol.destroyInstance();
        instance = SelectedSymbol.getInstance();

        sld = DefaultSymbols.createNewLine();
        instance.createNewSLD(sld);

        instance.setSld(sld);

        // No selection made
        assertFalse(instance.isRasterSymbol());

        style = ((NamedLayer)sld.layers().get(0)).styles().get(0);

        instance.setStyle(style);
        assertFalse(instance.isRasterSymbol());

        // Create raster symbolizer
        SelectedSymbol.destroyInstance();
        instance = SelectedSymbol.getInstance();

        sld = DefaultSymbols.createNewRaster();
        instance.createNewSLD(sld);

        instance.setSld(sld);

        // No selection made
        assertFalse(instance.isRasterSymbol());

        style = ((NamedLayer)sld.layers().get(0)).styles().get(0);

        instance.setStyle(style);
        assertTrue(instance.isRasterSymbol());
    }

    @Test
    public void testRemoveRasterImageOutline()
    {
        SelectedSymbol.destroyInstance();
        SelectedSymbol instance = SelectedSymbol.getInstance();

        instance.removeRasterImageOutline(null);

        RasterSymbolizer rasterSymbol = DefaultSymbols.createDefaultRasterSymbolizer();
        rasterSymbol.setImageOutline(DefaultSymbols.createDefaultLineSymbolizer());
        instance.removeRasterImageOutline(rasterSymbol);

        assertNull(rasterSymbol.getImageOutline());
    }

    /**
     * Gets the graphic.
     *
     * @param symbolizer the symbolizer
     * @return the graphic
     */
    private Graphic getGraphic(Symbolizer symbolizer) { 
        Graphic graphic = null; 
 
        if(symbolizer instanceof PointSymbolizerImpl) 
        { 
            PointSymbolizer pointSymbolizer = (PointSymbolizer) symbolizer; 
            graphic = pointSymbolizer.getGraphic(); 
        } 
        else if(symbolizer instanceof PolygonSymbolizerImpl) 
        { 
            PolygonSymbolizer polygonSymbolizer = (PolygonSymbolizer) symbolizer; 
            if(polygonSymbolizer != null) 
            { 
                Fill fill = polygonSymbolizer.getFill(); 
 
                if(fill != null) 
                { 
                    graphic = fill.getGraphicFill(); 
                } 
            } 
        } 
 
        return graphic; 
    } 
}
