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
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.geotools.styling.ExternalGraphic;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Fill;
import org.geotools.styling.Graphic;
import org.geotools.styling.LineSymbolizer;
import org.geotools.styling.NamedLayer;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.Stroke;
import org.geotools.styling.Style;
import org.geotools.styling.StyleBuilder;
import org.geotools.styling.StyleFactory;
import org.geotools.styling.StyledLayerDescriptor;
import org.junit.Test;
import org.opengis.style.GraphicalSymbol;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.sldeditor.common.data.SLDExternalImages;
import com.sldeditor.common.output.impl.SLDWriterImpl;

/**
 * The unit test for SLDExternalImages.
 * <p>{@link com.sldeditor.common.data.SLDExternalImages}
 * 
 * @author Robert Ward (SCISYS)
 */
public class SLDExternalImagesTest {

    /**
     * Test method for {@link com.sldeditor.common.data.SLDExternalImages#getExternalImages(java.net.URL, org.geotools.styling.StyledLayerDescriptor)}.
     */
    @Test
    public void testGetExternalImagesPolygon() {
        SLDWriterImpl writer = new SLDWriterImpl();

        File f = new File("D:/temp/test.png");
        URL url = null;
        URL resourceLocator = null;
        try {
            url = f.toURI().toURL();
            resourceLocator = new URL(f.getParentFile().toURI().toURL().toExternalForm() + "/");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        StyledLayerDescriptor polygonSLD = createTestPolygon(url);

        List<String> imageList = SLDExternalImages.getExternalImages(resourceLocator, polygonSLD);

        String result = writer.encodeSLD(resourceLocator, polygonSLD);
        assertTrue(checkResult(f, result, 3));

        assertEquals(3, imageList.size());
    }

    /**
     * Test method for {@link com.sldeditor.common.data.SLDExternalImages#getExternalImages(java.net.URL, org.geotools.styling.StyledLayerDescriptor)}.
     */
    @Test
    public void testGetExternalImagesLine() {
        SLDWriterImpl writer = new SLDWriterImpl();

        File f = new File("D:/temp/test.png");
        URL url = null;
        URL resourceLocator = null;
        try {
            url = f.toURI().toURL();
            resourceLocator = new URL(f.getParentFile().toURI().toURL().toExternalForm() + "/");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        StyledLayerDescriptor lineSLD = createTestLine(url);

        List<String> imageList = SLDExternalImages.getExternalImages(resourceLocator, lineSLD);

        String result = writer.encodeSLD(resourceLocator, lineSLD);
        assertTrue(checkResult(f, result, 2));

        assertEquals(2, imageList.size());
    }

    /**
     * Test method for {@link com.sldeditor.common.data.SLDExternalImages#getExternalImages(java.net.URL, org.geotools.styling.StyledLayerDescriptor)}.
     */
    @Test
    public void testGetExternalImagesPoint() {
        SLDWriterImpl writer = new SLDWriterImpl();

        File f = new File("D:/temp/test.png");
        URL url = null;
        URL resourceLocator = null;
        try {
            url = f.toURI().toURL();
            resourceLocator = new URL(f.getParentFile().toURI().toURL().toExternalForm() + "/");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        StyledLayerDescriptor pointSLD = createTestPoint(url);

        List<String> imageList = SLDExternalImages.getExternalImages(resourceLocator, null);
        assertEquals(0, imageList.size());

        imageList = SLDExternalImages.getExternalImages(resourceLocator, pointSLD);

        String result = writer.encodeSLD(resourceLocator, pointSLD);
        assertTrue(checkResult(f, result, 1));

        assertEquals(1, imageList.size());
    }

    /**
     * Check result.
     *
     * @param f the f
     * @param result the result
     * @param expectedNoOfExternalGraphics the expected no of external graphics
     * @return true, if successful
     */
    private boolean checkResult(File f, String result, int expectedNoOfExternalGraphics) {
        boolean ok = false;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            InputSource is = new InputSource(new StringReader(result));
            builder = factory.newDocumentBuilder();
            Document doc = builder.parse(is);

            NodeList nodeList = doc.getDocumentElement().getElementsByTagName("sld:OnlineResource");

            assertEquals(expectedNoOfExternalGraphics, nodeList.getLength());

            int noFound = 0;
            for(int index = 0; index < nodeList.getLength(); index ++)
            {
                Element node = (Element) nodeList.item(index);

                String href = node.getAttribute("xlink:href");

                assertTrue(f.getName().compareTo(href) == 0);

                noFound ++;
            }

            assertEquals(noFound, expectedNoOfExternalGraphics);

            ok = true;
        } catch (SAXException | IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        return ok;
    }

    /**
     * Creates the test polygon.
     *
     * @param url the url
     * @return the styled layer descriptor
     */
    private StyledLayerDescriptor createTestPolygon(URL url) {
        StyleBuilder sb = new StyleBuilder();
        StyleFactory styleFactory = sb.getStyleFactory();

        StyledLayerDescriptor sld = styleFactory.createStyledLayerDescriptor();

        NamedLayer namedLayer = styleFactory.createNamedLayer();

        sld.addStyledLayer(namedLayer);

        Style style = styleFactory.createStyle();
        namedLayer.addStyle(style);

        List<FeatureTypeStyle> ftsList = style.featureTypeStyles();

        FeatureTypeStyle fts = styleFactory.createFeatureTypeStyle();

        ftsList.add(fts);

        Rule rule = styleFactory.createRule();

        fts.rules().add(rule);

        PolygonSymbolizer polygon = styleFactory.createPolygonSymbolizer();

        rule.symbolizers().add(polygon);

        Graphic graphicFill1 = createGraphic(url, styleFactory);
        Graphic graphicFill2 = createGraphic(url, styleFactory);
        Graphic graphicStroke = createGraphic(url, styleFactory);
        Fill fill = styleFactory.createFill(null, null, null, graphicFill1);

        polygon.setFill(fill);

        Stroke stroke = styleFactory.createStroke(null, null, null, null, null, null, null, graphicFill2, graphicStroke);
        polygon.setStroke(stroke);

        return sld;
    }

    /**
     * Creates the graphic.
     *
     * @param url the url
     * @param styleFactory the style factory
     * @return the graphic
     */
    private Graphic createGraphic(URL url, StyleFactory styleFactory) {
        List<GraphicalSymbol> symbolList = new ArrayList<GraphicalSymbol>();
        ExternalGraphic externalGraphic = styleFactory.createExternalGraphic(url, "image/png");
        symbolList.add(externalGraphic);
        Graphic graphicFill = styleFactory.graphicFill(symbolList, null, null, null, null, null);
        return graphicFill;
    }

    /**
     * Creates the test line.
     *
     * @param url the url
     * @return the styled layer descriptor
     */
    private StyledLayerDescriptor createTestLine(URL url) {
        StyleBuilder sb = new StyleBuilder();
        StyleFactory styleFactory = sb.getStyleFactory();

        StyledLayerDescriptor sld = styleFactory.createStyledLayerDescriptor();

        NamedLayer namedLayer = styleFactory.createNamedLayer();

        sld.addStyledLayer(namedLayer);

        Style style = styleFactory.createStyle();
        namedLayer.addStyle(style);

        List<FeatureTypeStyle> ftsList = style.featureTypeStyles();

        FeatureTypeStyle fts = styleFactory.createFeatureTypeStyle();

        ftsList.add(fts);

        Rule rule = styleFactory.createRule();

        fts.rules().add(rule);

        LineSymbolizer line = styleFactory.createLineSymbolizer();

        rule.symbolizers().add(line);

        Graphic graphicFill = createGraphic(url, styleFactory);
        Graphic graphicStroke = createGraphic(url, styleFactory);

        Stroke stroke = styleFactory.createStroke(null, null, null, null, null, null, null, graphicFill, graphicStroke);
        line.setStroke(stroke);

        return sld;
    }

    /**
     * Creates the test point.
     *
     * @param url the url
     * @return the styled layer descriptor
     */
    private StyledLayerDescriptor createTestPoint(URL url) {
        StyleBuilder sb = new StyleBuilder();
        StyleFactory styleFactory = sb.getStyleFactory();

        StyledLayerDescriptor sld = styleFactory.createStyledLayerDescriptor();

        NamedLayer namedLayer = styleFactory.createNamedLayer();

        sld.addStyledLayer(namedLayer);

        Style style = styleFactory.createStyle();
        namedLayer.addStyle(style);

        List<FeatureTypeStyle> ftsList = style.featureTypeStyles();

        FeatureTypeStyle fts = styleFactory.createFeatureTypeStyle();

        ftsList.add(fts);

        Rule rule = styleFactory.createRule();

        fts.rules().add(rule);

        PointSymbolizer point = styleFactory.createPointSymbolizer();

        rule.symbolizers().add(point);

        Graphic graphic = createGraphic(url, styleFactory);

        point.setGraphic(graphic);

        return sld;
    }
}
