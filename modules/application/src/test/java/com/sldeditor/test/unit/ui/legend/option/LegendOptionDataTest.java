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

package com.sldeditor.test.unit.ui.legend.option;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.sldeditor.ui.legend.option.LegendOptionData;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JLabel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * The unit test for LegendOptionData.
 *
 * <p>{@link com.sldeditor.ui.legend.option.LegendOptionData}
 *
 * @author Robert Ward (SCISYS)
 */
public class LegendOptionDataTest {

    /**
     * Test method for {@link com.sldeditor.ui.legend.option.LegendOptionData#setImageWidth(int)}.
     * Test method for {@link com.sldeditor.ui.legend.option.LegendOptionData#getImageWidth()}.
     */
    @Test
    public void testImageWidth() {
        LegendOptionData data = new LegendOptionData();
        int imageWidth = 42;

        data.setImageWidth(imageWidth);

        assertEquals(imageWidth, data.getImageWidth());
    }

    /**
     * Test method for {@link com.sldeditor.ui.legend.option.LegendOptionData#getImageHeight()}.
     * Test method for {@link com.sldeditor.ui.legend.option.LegendOptionData#setImageHeight(int)}.
     */
    @Test
    public void testImageHeight() {
        LegendOptionData data = new LegendOptionData();
        int imageHeight = 21;

        data.setImageHeight(imageHeight);

        assertEquals(imageHeight, data.getImageHeight());
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.legend.option.LegendOptionData#isMaintainAspectRatio()}. Test method for
     * {@link com.sldeditor.ui.legend.option.LegendOptionData#setMaintainAspectRatio(boolean)}.
     */
    @Test
    public void testMaintainAspectRatio() {
        LegendOptionData data = new LegendOptionData();

        int expectedWidth = 10;
        int expectedheight = 20;

        data.setMaintainAspectRatio(false);
        data.setImageWidth(expectedWidth);
        data.setImageHeight(expectedheight);
        assertFalse(data.isMaintainAspectRatio());
        assertEquals(expectedWidth, data.getImageWidth());
        assertEquals(expectedheight, data.getImageHeight());

        expectedWidth++;
        expectedheight++;
        data.setImageWidth(expectedWidth);
        data.setImageHeight(expectedheight);
        assertEquals(expectedWidth, data.getImageWidth());
        assertEquals(expectedheight, data.getImageHeight());

        data.setMaintainAspectRatio(true);
        assertTrue(data.isMaintainAspectRatio());
        data.setImageWidth(expectedWidth);
        assertEquals(expectedWidth, data.getImageWidth());
        assertEquals(expectedWidth, data.getImageHeight());

        data.setImageWidth(expectedheight);
        assertEquals(expectedheight, data.getImageWidth());
        assertEquals(expectedheight, data.getImageHeight());
    }

    /**
     * Test method for {@link com.sldeditor.ui.legend.option.LegendOptionData#getDpi()}. Test method
     * for {@link com.sldeditor.ui.legend.option.LegendOptionData#setDpi(int)}.
     */
    @Test
    public void testDpi() {
        LegendOptionData data = new LegendOptionData();
        int dpi = 201;

        data.setDpi(dpi);

        assertEquals(dpi, data.getDpi());
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.legend.option.LegendOptionData#getBackgroundColour()}. Test method for
     * {@link com.sldeditor.ui.legend.option.LegendOptionData#setBackgroundColour(java.awt.Color)}.
     */
    @Test
    public void testBackgroundColour() {
        LegendOptionData data = new LegendOptionData();
        Color expectedColor = Color.YELLOW;

        data.setBackgroundColour(expectedColor);

        assertEquals(expectedColor, data.getBackgroundColour());
    }

    /**
     * Test method for {@link com.sldeditor.ui.legend.option.LegendOptionData#isShowLabels()}. Test
     * method for {@link com.sldeditor.ui.legend.option.LegendOptionData#setShowLabels(boolean)}.
     */
    @Test
    public void testLabels() {
        LegendOptionData data = new LegendOptionData();

        data.setShowLabels(true);
        assertTrue(data.isShowLabels());

        data.setShowLabels(false);
        assertFalse(data.isShowLabels());
    }

    /**
     * Test method for {@link com.sldeditor.ui.legend.option.LegendOptionData#getImageSize()}. Test
     * method for {@link com.sldeditor.ui.legend.option.LegendOptionData#setImageSize(int)}.
     */
    @Test
    public void testImageSize() {
        LegendOptionData data = new LegendOptionData();
        int imageSize = 76;

        data.setImageSize(imageSize);

        assertEquals(imageSize, data.getImageSize());
    }

    /**
     * Test method for {@link com.sldeditor.ui.legend.option.LegendOptionData#splitSymbolizers()}.
     * Test method for {@link
     * com.sldeditor.ui.legend.option.LegendOptionData#setSplitSymbolizers(boolean)}.
     */
    @Test
    public void testSplitSymbolizers() {
        LegendOptionData data = new LegendOptionData();

        data.setSplitSymbolizers(true);
        assertTrue(data.splitSymbolizers());

        data.setSplitSymbolizers(false);
        assertFalse(data.splitSymbolizers());
    }

    /**
     * Test method for {@link com.sldeditor.ui.legend.option.LegendOptionData#isShowTitle()}. Test
     * method for {@link com.sldeditor.ui.legend.option.LegendOptionData#setShowTitle(boolean)}.
     */
    @Test
    public void testShowTitle() {
        LegendOptionData data = new LegendOptionData();

        data.setShowTitle(true);
        assertTrue(data.isShowTitle());

        data.setShowTitle(false);
        assertFalse(data.isShowTitle());
    }

    /**
     * Test method for {@link com.sldeditor.ui.legend.option.LegendOptionData#getLabelFont()}. Test
     * method for {@link
     * com.sldeditor.ui.legend.option.LegendOptionData#setLabelFont(java.awt.Font)}.
     */
    @Test
    public void testLabelFont() {
        LegendOptionData data = new LegendOptionData();
        Font expectedFont = new Font(new JLabel().getFont().getName(), Font.BOLD, 24);

        data.setLabelFont(expectedFont);

        assertEquals(expectedFont, data.getLabelFont());
    }

    /**
     * Test method for {@link com.sldeditor.ui.legend.option.LegendOptionData#getLabelFontColour()}.
     * Test method for {@link
     * com.sldeditor.ui.legend.option.LegendOptionData#setLabelFontColour(java.awt.Color)}.
     */
    @Test
    public void testLabelFontColour() {
        LegendOptionData data = new LegendOptionData();
        Color expectedColor = Color.CYAN;

        data.setLabelFontColour(expectedColor);

        assertEquals(expectedColor, data.getLabelFontColour());
    }

    /**
     * Test method for {@link com.sldeditor.ui.legend.option.LegendOptionData#getBorderColour()}.
     * Test method for {@link
     * com.sldeditor.ui.legend.option.LegendOptionData#setBorderColour(java.awt.Color)}.
     */
    @Test
    public void testBorderColour() {
        LegendOptionData data = new LegendOptionData();
        Color expectedColor = Color.LIGHT_GRAY;

        data.setBorderColour(expectedColor);

        assertEquals(expectedColor, data.getBorderColour());
    }

    /**
     * Test method for {@link com.sldeditor.ui.legend.option.LegendOptionData#isFontAntiAliasing()}.
     * Test method for {@link
     * com.sldeditor.ui.legend.option.LegendOptionData#setFontAntiAliasing(boolean)}.
     */
    @Test
    public void testFontAntiAliasing() {
        LegendOptionData data = new LegendOptionData();

        data.setFontAntiAliasing(true);
        assertTrue(data.isFontAntiAliasing());

        data.setFontAntiAliasing(false);
        assertFalse(data.isFontAntiAliasing());
    }

    /**
     * Test method for {@link com.sldeditor.ui.legend.option.LegendOptionData#isBorder()}. Test
     * method for {@link com.sldeditor.ui.legend.option.LegendOptionData#setBorder(boolean)}.
     */
    @Test
    public void testBorder() {
        LegendOptionData data = new LegendOptionData();

        data.setBorder(true);
        assertTrue(data.isBorder());

        data.setBorder(false);
        assertFalse(data.isBorder());
    }

    /**
     * Test method for {@link com.sldeditor.ui.legend.option.LegendOptionData#isBandInformation()}.
     * Test method for {@link
     * com.sldeditor.ui.legend.option.LegendOptionData#setBandInformation(boolean)}.
     */
    @Test
    public void testBandInformation() {
        LegendOptionData data = new LegendOptionData();

        data.setBandInformation(true);
        assertTrue(data.isBandInformation());

        data.setBandInformation(false);
        assertFalse(data.isBandInformation());
    }

    /**
     * Test method for {@link com.sldeditor.ui.legend.option.LegendOptionData#isTransparent()}. Test
     * method for {@link com.sldeditor.ui.legend.option.LegendOptionData#setTransparent(boolean)}.
     */
    @Test
    public void testTransparent() {
        LegendOptionData data = new LegendOptionData();

        data.setTransparent(true);
        assertTrue(data.isTransparent());

        data.setTransparent(false);
        assertFalse(data.isTransparent());
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.legend.option.LegendOptionData#encodeXML(org.w3c.dom.Document,
     * org.w3c.dom.Element, java.lang.String)}.
     */
    @Test
    public void testEncodeXML() {
        LegendOptionData data = new LegendOptionData();

        data.encodeXML(null, null, null);

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder;
        try {
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document doc = documentBuilder.newDocument();
            Element root = doc.createElement("root");
            doc.appendChild(root);

            data.setBackgroundColour(Color.GREEN);
            data.setBorder(true);
            data.setDpi(320);
            String elementName = "legend";
            data.encodeXML(doc, root, elementName);

            LegendOptionData newData = LegendOptionData.decodeXML(doc, elementName);

            assertEquals(newData.getBackgroundColour(), data.getBackgroundColour());
            assertEquals(newData.getDpi(), data.getDpi());
            assertEquals(newData.isBorder(), data.isBorder());

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.legend.option.LegendOptionData#decodeXML(org.w3c.dom.Document,
     * java.lang.String)}.
     */
    @Test
    public void testDecodeXML() {
        LegendOptionData.decodeXML(null, null);
        LegendOptionData.decodeXML(null, "");

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder;
        try {
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document doc = documentBuilder.newDocument();
            LegendOptionData.decodeXML(doc, "");
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }
}
