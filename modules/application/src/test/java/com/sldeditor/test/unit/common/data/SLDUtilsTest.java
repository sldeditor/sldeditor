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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import com.sldeditor.common.data.GeoServerConnection;
import com.sldeditor.common.data.SLDData;
import com.sldeditor.common.data.SLDUtils;
import com.sldeditor.common.data.StyleWrapper;
import com.sldeditor.common.defaultsymbol.DefaultSymbols;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Font;
import org.geotools.styling.MarkImpl;
import org.geotools.styling.NamedLayer;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactoryImpl;
import org.geotools.styling.StyledLayer;
import org.geotools.styling.StyledLayerDescriptor;
import org.geotools.styling.Symbolizer;
import org.geotools.styling.TextSymbolizer;
import org.geotools.styling.visitor.DuplicatingStyleVisitor;
import org.junit.jupiter.api.Test;
import org.opengis.filter.FilterFactory;

/**
 * The unit test for SLDUtils.
 *
 * <p>{@link com.sldeditor.common.data.SLDUtils}
 *
 * @author Robert Ward (SCISYS)
 */
public class SLDUtilsTest {

    private String expectedSld =
            "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><StyledLayerDescriptor version=\"1.0.0\" xsi:schemaLocation=\"http://www.opengis.net/sld StyledLayerDescriptor.xsd\" xmlns=\"http://www.opengis.net/sld\" xmlns:ogc=\"http://www.opengis.net/ogc\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">  <NamedLayer><Name>Simple Point</Name><UserStyle><Title>SLD Cook Book: Simple Point</Title><FeatureTypeStyle><Rule><PointSymbolizer><Graphic><Mark><WellKnownName>circle</WellKnownName><Fill><CssParameter name=\"fill\">#FF0000</CssParameter></Fill></Mark><Size>6</Size></Graphic></PointSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer></StyledLayerDescriptor>";

    @Test
    public void testCreateSLDFromStringFile() {

        StyleWrapper wrapper = new StyleWrapper();

        SLDData sldData = new SLDData(wrapper, expectedSld);

        String filename = "D:/tmp/test.sld";
        File file = new File(filename);
        sldData.setSLDFile(file);
        StyledLayerDescriptor sld = SLDUtils.createSLDFromString(null);

        assertNull(sld);
        sld = SLDUtils.createSLDFromString(sldData);

        StyledLayer[] styledLayers = sld.getStyledLayers();
        NamedLayer namedLayer = (NamedLayer) styledLayers[0];
        Style[] actualStyles = namedLayer.getStyles();
        PointSymbolizer pointSymbolizer =
                (PointSymbolizer)
                        actualStyles[0]
                                .featureTypeStyles()
                                .get(0)
                                .rules()
                                .get(0)
                                .symbolizers()
                                .get(0);

        MarkImpl mark = (MarkImpl) pointSymbolizer.getGraphic().graphicalSymbols().get(0);
        assertEquals("circle", mark.getWellKnownName().toString());

        // Check resource locator
        try {
            URL url = file.getParentFile().toURI().toURL();
            String actualResourceLocator = sldData.getResourceLocator().toExternalForm();
            String expectedResourcelocator = url.toExternalForm();
            assertTrue(expectedResourcelocator.compareTo(actualResourceLocator) == 0);
        } catch (MalformedURLException e) {
            fail(e.getStackTrace().toString());
        }
    }

    @Test
    public void testCreateSLDFromStringGeoServer() {

        SLDData sldData = new SLDData(null, expectedSld);
        String geoserverUrl = "http://localhost:8080/geoserver";
        GeoServerConnection connectionData = new GeoServerConnection();
        try {
            connectionData.setUrl(new URL(geoserverUrl));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        sldData.setConnectionData(connectionData);
        StyledLayerDescriptor sld = SLDUtils.createSLDFromString(null);

        assertNull(sld);
        sld = SLDUtils.createSLDFromString(sldData);

        StyledLayer[] styledLayers = sld.getStyledLayers();
        NamedLayer namedLayer = (NamedLayer) styledLayers[0];
        Style[] actualStyles = namedLayer.getStyles();
        PointSymbolizer pointSymbolizer =
                (PointSymbolizer)
                        actualStyles[0]
                                .featureTypeStyles()
                                .get(0)
                                .rules()
                                .get(0)
                                .symbolizers()
                                .get(0);

        MarkImpl mark = (MarkImpl) pointSymbolizer.getGraphic().graphicalSymbols().get(0);
        assertEquals("circle", mark.getWellKnownName().toString());

        // Check resource locator
        geoserverUrl = geoserverUrl + "/styles/";
        assertTrue(geoserverUrl.compareTo(sldData.getResourceLocator().toExternalForm()) == 0);
    }

    @Test
    public void testReadSLDFile() {
        try {
            File tmpFile = File.createTempFile(getClass().getSimpleName(), ".sld");

            FileWriter fileWriter = new FileWriter(tmpFile);
            fileWriter.write(expectedSld);
            fileWriter.flush();
            fileWriter.close();

            StyledLayerDescriptor sld = SLDUtils.readSLDFile(null);

            assertNull(sld);
            sld = SLDUtils.readSLDFile(tmpFile);
            StyledLayer[] styledLayers = sld.getStyledLayers();
            NamedLayer namedLayer = (NamedLayer) styledLayers[0];
            Style[] actualStyles = namedLayer.getStyles();
            PointSymbolizer pointSymbolizer =
                    (PointSymbolizer)
                            actualStyles[0]
                                    .featureTypeStyles()
                                    .get(0)
                                    .rules()
                                    .get(0)
                                    .symbolizers()
                                    .get(0);

            MarkImpl mark = (MarkImpl) pointSymbolizer.getGraphic().graphicalSymbols().get(0);
            assertEquals("circle", mark.getWellKnownName().toString());

            tmpFile.delete();
        } catch (IOException e) {
            e.printStackTrace();
            fail("Failed to create test file");
        }
    }

    @Test
    public void testFindSymbolizer() {
        StyledLayerDescriptor sld = DefaultSymbols.createNewSLD();
        NamedLayer namedLayer = DefaultSymbols.createNewNamedLayer();
        sld.layers().add(DefaultSymbols.createNewNamedLayer());
        sld.layers().add(namedLayer);
        String expectedNamedLayer = "namedLayer";
        namedLayer.setName(expectedNamedLayer);
        Style style = DefaultSymbols.createNewStyle();
        String expectedStyleLayer = "style";
        style.setName(expectedStyleLayer);
        namedLayer.addStyle(DefaultSymbols.createNewStyle());
        namedLayer.addStyle(style);
        FeatureTypeStyle fts = DefaultSymbols.createNewFeatureTypeStyle();
        String expectedFeatureTypeStyleLayer = "feature type style";
        fts.setName(expectedFeatureTypeStyleLayer);
        style.featureTypeStyles().add(DefaultSymbols.createNewFeatureTypeStyle());
        style.featureTypeStyles().add(fts);
        Rule rule = DefaultSymbols.createNewRule();
        fts.rules().add(DefaultSymbols.createNewRule());
        fts.rules().add(rule);
        String expectedRule = "rule";
        rule.setName(expectedRule);
        String expectedSymbolizer = "text symbolizer";
        TextSymbolizer symbolizer = DefaultSymbols.createDefaultTextSymbolizer();
        symbolizer.setName(expectedSymbolizer);
        rule.symbolizers().add(DefaultSymbols.createDefaultPolygonSymbolizer());
        rule.symbolizers().add(symbolizer);
        StyleFactoryImpl styleFactory = (StyleFactoryImpl) CommonFactoryFinder.getStyleFactory();
        FilterFactory ff = CommonFactoryFinder.getFilterFactory();
        Font font =
                styleFactory.createFont(
                        ff.literal("abc"),
                        ff.literal("normal"),
                        ff.literal("normal"),
                        ff.literal(10));
        symbolizer.setFont(font);

        DuplicatingStyleVisitor duplicate = new DuplicatingStyleVisitor();
        duplicate.visit(sld);
        StyledLayerDescriptor sldCopy = (StyledLayerDescriptor) duplicate.getCopy();

        Symbolizer actualSymbolizer = SLDUtils.findSymbolizer(sld, symbolizer, sldCopy);

        assertNotNull(actualSymbolizer);
        assertEquals(
                symbolizer.getLabel().toString(),
                ((TextSymbolizer) actualSymbolizer).getLabel().toString());

        actualSymbolizer = SLDUtils.findSymbolizer(sld, null, sldCopy);
        assertNull(actualSymbolizer);

        actualSymbolizer = SLDUtils.findSymbolizer(sld, symbolizer, null);
        assertNull(actualSymbolizer);
    }

    @Test
    public void testFindRule() {
        StyledLayerDescriptor sld = DefaultSymbols.createNewSLD();
        NamedLayer namedLayer = DefaultSymbols.createNewNamedLayer();
        sld.layers().add(DefaultSymbols.createNewNamedLayer());
        sld.layers().add(namedLayer);
        String expectedNamedLayer = "namedLayer";
        namedLayer.setName(expectedNamedLayer);
        Style style = DefaultSymbols.createNewStyle();
        String expectedStyleLayer = "style";
        style.setName(expectedStyleLayer);
        namedLayer.addStyle(DefaultSymbols.createNewStyle());
        namedLayer.addStyle(style);
        FeatureTypeStyle fts = DefaultSymbols.createNewFeatureTypeStyle();
        String expectedFeatureTypeStyleLayer = "feature type style";
        fts.setName(expectedFeatureTypeStyleLayer);
        style.featureTypeStyles().add(DefaultSymbols.createNewFeatureTypeStyle());
        style.featureTypeStyles().add(fts);
        Rule rule = DefaultSymbols.createNewRule();
        fts.rules().add(DefaultSymbols.createNewRule());
        fts.rules().add(rule);
        String expectedRule = "rule";
        rule.setName(expectedRule);
        String expectedSymbolizer = "text symbolizer";
        TextSymbolizer symbolizer = DefaultSymbols.createDefaultTextSymbolizer();
        symbolizer.setName(expectedSymbolizer);
        rule.symbolizers().add(DefaultSymbols.createDefaultPolygonSymbolizer());
        rule.symbolizers().add(symbolizer);
        StyleFactoryImpl styleFactory = (StyleFactoryImpl) CommonFactoryFinder.getStyleFactory();
        FilterFactory ff = CommonFactoryFinder.getFilterFactory();
        Font font =
                styleFactory.createFont(
                        ff.literal("abc"),
                        ff.literal("normal"),
                        ff.literal("normal"),
                        ff.literal(10));
        symbolizer.setFont(font);

        DuplicatingStyleVisitor duplicate = new DuplicatingStyleVisitor();
        duplicate.visit(sld);
        StyledLayerDescriptor sldCopy = (StyledLayerDescriptor) duplicate.getCopy();

        Rule actualRule = SLDUtils.findRule(sld, rule, sldCopy);

        assertNotNull(actualRule);
        assertEquals(rule.getName(), actualRule.getName());

        actualRule = SLDUtils.findRule(sld, null, sldCopy);
        assertNull(actualRule);

        actualRule = SLDUtils.findRule(sld, rule, null);
        assertNull(actualRule);
    }
}
