/*
 * SLD Editor - The Open Source Java SLD Editor
 *
 * Copyright (C) 2017, SCISYS UK Limited
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

package com.sldeditor.test.unit.ui.detail.vendor.geoserver.text;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sldeditor.common.Controller;
import com.sldeditor.common.data.SelectedSymbol;
import com.sldeditor.common.defaultsymbol.DefaultSymbols;
import com.sldeditor.common.vendoroption.minversion.VendorOptionPresent;
import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.ui.detail.TextSymbolizerDetails;
import com.sldeditor.ui.detail.vendor.geoserver.text.VOGeoServerTextSymbolizer2;
import com.sldeditor.ui.iface.UpdateSymbolInterface;
import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Graphic;
import org.geotools.styling.OtherText;
import org.geotools.styling.OtherTextImpl;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.RasterSymbolizer;
import org.geotools.styling.SelectedChannelType;
import org.geotools.styling.StyleFactoryImpl;
import org.geotools.styling.TextSymbolizer;
import org.geotools.styling.TextSymbolizer2;
import org.junit.jupiter.api.Test;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Literal;

/**
 * The unit test for VOGeoServerTextSymbolizer2.
 *
 * <p>{@link com.sldeditor.ui.detail.vendor.geoserver.text.VOGeoServerTextSymbolizer2}
 *
 * @author Robert Ward (SCISYS)
 */
public class VOGeoServerTextSymbolizer2Test {

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.text.VOGeoServerTextSymbolizer2#VOGeoServerTextSymbolizer2(java.lang.Class,
     * com.sldeditor.filter.v2.function.FunctionNameInterface)}.
     */
    @Test
    public void testVOGeoServerTextSymbolizer2() {
        TextSymbolizerDetails panel = new TextSymbolizerDetails();

        TextSymbolizer2 textSymbolizer = null;
        VOGeoServerTextSymbolizer2 testObj = new VOGeoServerTextSymbolizer2(panel.getClass());
        testObj.setParentPanel(panel);
        testObj.populate(textSymbolizer);
        testObj.updateSymbol(textSymbolizer);

        StyleFactoryImpl styleFactory = (StyleFactoryImpl) CommonFactoryFinder.getStyleFactory();
        textSymbolizer = (TextSymbolizer2) styleFactory.createTextSymbolizer();

        FilterFactory ff = CommonFactoryFinder.getFilterFactory();

        Literal featureDescription = ff.literal("feature description");
        textSymbolizer.setFeatureDescription(featureDescription);

        OtherText otherText = new OtherTextImpl();
        otherText.setTarget("target");
        Literal otherTextExpression = ff.literal("other text");
        otherText.setText(otherTextExpression);
        textSymbolizer.setOtherText(otherText);
        Literal snippet = ff.literal("snippet");
        textSymbolizer.setSnippet(snippet);

        // Try with marker symbol
        Graphic graphic = styleFactory.createDefaultGraphic();
        graphic.graphicalSymbols().add(styleFactory.createMark());
        textSymbolizer.setGraphic(graphic);
        Controller.getInstance().setPopulating(true);
        testObj.populate(textSymbolizer);
        Controller.getInstance().setPopulating(false);
        testObj.updateSymbol(textSymbolizer);

        // Try with external graphic
        graphic = styleFactory.createDefaultGraphic();
        try {
            graphic.graphicalSymbols()
                    .add(
                            styleFactory.createExternalGraphic(
                                    new File("test.png").toURI().toURL(), "png"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        textSymbolizer.setGraphic(graphic);
        Controller.getInstance().setPopulating(true);
        testObj.populate(textSymbolizer);
        Controller.getInstance().setPopulating(false);
        testObj.updateSymbol(textSymbolizer);

        // Find minimum version with textSymbolizer2 values set
        List<VendorOptionPresent> vendorOptionsPresentList = new ArrayList<VendorOptionPresent>();
        testObj.getMinimumVersion(null, textSymbolizer, vendorOptionsPresentList);
        assertEquals(1, vendorOptionsPresentList.size());

        // Find minimum version with no textSymbolizer2 values set
        vendorOptionsPresentList.clear();
        testObj.getMinimumVersion(
                null, styleFactory.createTextSymbolizer(), vendorOptionsPresentList);
        assertEquals(0, vendorOptionsPresentList.size());

        // Get the code coverage values up
        testObj.populate(SelectedSymbol.getInstance());

        PolygonSymbolizer polygon = null;
        testObj.populate(polygon);
        testObj.updateSymbol(polygon);

        RasterSymbolizer raster = null;
        testObj.populate(raster);
        testObj.updateSymbol(raster);
        testObj.preLoadSymbol();
        assertTrue(testObj.isDataPresent());
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.text.VOGeoServerTextSymbolizer2#getVendorOption()}.
     */
    @Test
    public void testGetVendorOption() {
        TextSymbolizerDetails panel = new TextSymbolizerDetails();
        VOGeoServerTextSymbolizer2 testObj = new VOGeoServerTextSymbolizer2(panel.getClass());
        assertNotNull(testObj.getVendorOption());
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.text.VOGeoServerTextSymbolizer2#dataChanged(com.sldeditor.common.xml.ui.FieldIdEnum)}.
     */
    @Test
    public void testDataChanged() {
        TextSymbolizerDetails panel = new TextSymbolizerDetails();
        VOGeoServerTextSymbolizer2 testObj = new VOGeoServerTextSymbolizer2(panel.getClass());
        testObj.dataChanged(FieldIdEnum.ANGLE);
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.text.VOGeoServerTextSymbolizer2#populate(com.sldeditor.common.data.SelectedSymbol)}.
     */
    @Test
    public void testPopulateSelectedSymbol() {
        TextSymbolizerDetails panel = new TextSymbolizerDetails();
        VOGeoServerTextSymbolizer2 testObj = new VOGeoServerTextSymbolizer2(panel.getClass());
        SelectedSymbol selectedSymbol = null;
        testObj.populate(selectedSymbol);
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.text.VOGeoServerTextSymbolizer2#getFieldDataManager()}.
     */
    @Test
    public void testGetFieldDataManager() {
        TextSymbolizerDetails panel = new TextSymbolizerDetails();
        VOGeoServerTextSymbolizer2 testObj = new VOGeoServerTextSymbolizer2(panel.getClass());
        assertNotNull(testObj.getFieldDataManager());
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.text.VOGeoServerTextSymbolizer2#populate(org.geotools.styling.TextSymbolizer)}.
     */
    @Test
    public void testPopulateTextSymbolizer() {
        TextSymbolizerDetails panel = new TextSymbolizerDetails();
        VOGeoServerTextSymbolizer2 testObj = new VOGeoServerTextSymbolizer2(panel.getClass());
        TextSymbolizer symbolizer = null;
        testObj.populate(symbolizer);
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.text.VOGeoServerTextSymbolizer2#updateSymbol(org.geotools.styling.TextSymbolizer)}.
     */
    @Test
    public void testUpdateSymbolTextSymbolizer() {
        TextSymbolizerDetails panel = new TextSymbolizerDetails();
        VOGeoServerTextSymbolizer2 testObj = new VOGeoServerTextSymbolizer2(panel.getClass());
        TextSymbolizer symbolizer = null;
        testObj.updateSymbol(symbolizer);
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.text.VOGeoServerTextSymbolizer2#updateSymbol(org.geotools.styling.FeatureTypeStyle)}.
     */
    @Test
    public void testUpdateSymbolFeatureTypeStyle() {
        TextSymbolizerDetails panel = new TextSymbolizerDetails();
        VOGeoServerTextSymbolizer2 testObj = new VOGeoServerTextSymbolizer2(panel.getClass());
        FeatureTypeStyle style = null;
        testObj.updateSymbol(style);
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.text.VOGeoServerTextSymbolizer2#getPanel()}.
     */
    @Test
    public void testGetPanel() {
        TextSymbolizerDetails panel = new TextSymbolizerDetails();
        VOGeoServerTextSymbolizer2 testObj = new VOGeoServerTextSymbolizer2(panel.getClass());
        assertNotNull(testObj.getPanel());
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.text.VOGeoServerTextSymbolizer2#setParentPanel(com.sldeditor.ui.iface.UpdateSymbolInterface)}.
     */
    @Test
    public void testSetParentPanel() {
        class TestUpdateSymbolInterface implements UpdateSymbolInterface {
            public boolean called = false;

            @Override
            public void dataChanged(FieldIdEnum changedField) {
                called = true;
            }
        }

        TestUpdateSymbolInterface receiver = new TestUpdateSymbolInterface();
        TextSymbolizerDetails panel = new TextSymbolizerDetails();
        VOGeoServerTextSymbolizer2 testObj = new VOGeoServerTextSymbolizer2(panel.getClass());
        testObj.setParentPanel(receiver);
        testObj.dataChanged(FieldIdEnum.ANGLE);
        assertTrue(receiver.called);

        assertEquals(receiver, testObj.getParentPanel());
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.text.VOGeoServerTextSymbolizer2#isDataPresent()}.
     */
    @Test
    public void testIsDataPresent() {
        TextSymbolizerDetails panel = new TextSymbolizerDetails();
        VOGeoServerTextSymbolizer2 testObj = new VOGeoServerTextSymbolizer2(panel.getClass());
        assertTrue(testObj.isDataPresent());
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.text.VOGeoServerTextSymbolizer2#updateSymbol(org.geotools.styling.PolygonSymbolizer)}.
     */
    @Test
    public void testUpdateSymbolPolygonSymbolizer() {
        TextSymbolizerDetails panel = new TextSymbolizerDetails();
        VOGeoServerTextSymbolizer2 testObj = new VOGeoServerTextSymbolizer2(panel.getClass());
        PolygonSymbolizer symbolizer = null;
        testObj.updateSymbol(symbolizer);
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.text.VOGeoServerTextSymbolizer2#populate(org.geotools.styling.PolygonSymbolizer)}.
     */
    @Test
    public void testPopulatePolygonSymbolizer() {
        TextSymbolizerDetails panel = new TextSymbolizerDetails();
        VOGeoServerTextSymbolizer2 testObj = new VOGeoServerTextSymbolizer2(panel.getClass());
        PolygonSymbolizer symbolizer = null;
        testObj.updateSymbol(symbolizer);
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.text.VOGeoServerTextSymbolizer2#preLoadSymbol()}.
     */
    @Test
    public void testPreLoadSymbol() {
        TextSymbolizerDetails panel = new TextSymbolizerDetails();
        VOGeoServerTextSymbolizer2 testObj = new VOGeoServerTextSymbolizer2(panel.getClass());
        testObj.preLoadSymbol();
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.text.VOGeoServerTextSymbolizer2#populate(org.geotools.styling.RasterSymbolizer)}.
     */
    @Test
    public void testPopulateRasterSymbolizer() {
        TextSymbolizerDetails panel = new TextSymbolizerDetails();
        VOGeoServerTextSymbolizer2 testObj = new VOGeoServerTextSymbolizer2(panel.getClass());
        RasterSymbolizer symbolizer = null;
        testObj.updateSymbol(symbolizer);
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.text.VOGeoServerTextSymbolizer2#updateSymbol(org.geotools.styling.RasterSymbolizer)}.
     */
    @Test
    public void testUpdateSymbolRasterSymbolizer() {
        TextSymbolizerDetails panel = new TextSymbolizerDetails();
        VOGeoServerTextSymbolizer2 testObj = new VOGeoServerTextSymbolizer2(panel.getClass());
        RasterSymbolizer symbolizer = null;
        testObj.updateSymbol(symbolizer);
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.text.VOGeoServerTextSymbolizer2#getVendorOptionInfo()}.
     */
    @Test
    public void testGetVendorOptionInfo() {
        TextSymbolizerDetails panel = new TextSymbolizerDetails();
        VOGeoServerTextSymbolizer2 testObj = new VOGeoServerTextSymbolizer2(panel.getClass());
        assertNotNull(testObj.getVendorOptionInfo());
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.text.VOGeoServerTextSymbolizer2#getMinimumVersion(java.lang.Object,
     * java.lang.Object, java.util.List)}.
     */
    @Test
    public void testGetMinimumVersion() {
        TextSymbolizerDetails panel = new TextSymbolizerDetails();
        VOGeoServerTextSymbolizer2 testObj = new VOGeoServerTextSymbolizer2(panel.getClass());
        TextSymbolizer2 symbolizer = null;
        testObj.updateSymbol(symbolizer);

        symbolizer = (TextSymbolizer2) DefaultSymbols.createDefaultTextSymbolizer();

        FilterFactory ff = CommonFactoryFinder.getFilterFactory();
        symbolizer.setSnippet(ff.literal("Test snippet"));

        List<VendorOptionPresent> vendorOptionsPresentList = null;
        testObj.getMinimumVersion(symbolizer, "", vendorOptionsPresentList);

        vendorOptionsPresentList = new ArrayList<VendorOptionPresent>();
        testObj.getMinimumVersion(symbolizer, "", vendorOptionsPresentList);
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.text.VOGeoServerTextSymbolizer2#optionSelected(java.lang.Class,
     * java.lang.String)}.
     */
    @Test
    public void testOptionSelected() {
        TextSymbolizerDetails panel = new TextSymbolizerDetails();
        VOGeoServerTextSymbolizer2 testObj = new VOGeoServerTextSymbolizer2(panel.getClass());
        testObj.optionSelected(null, "test");
        testObj.optionSelected(String.class, "test");
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.text.VOGeoServerTextSymbolizer2#populate(org.geotools.styling.SelectedChannelType)}.
     */
    @Test
    void testPopulateSelectedChannelType() {
        TextSymbolizerDetails panel = new TextSymbolizerDetails();
        VOGeoServerTextSymbolizer2 testObj = new VOGeoServerTextSymbolizer2(panel.getClass());
        SelectedChannelType channelType = null;
        testObj.populate(channelType);
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.text.VOGeoServerTextSymbolizer2#updateSymbol(org.geotools.styling.SelectedChannelType)}.
     */
    @Test
    void testUpdateSymbolSelectedChannelType() {
        TextSymbolizerDetails panel = new TextSymbolizerDetails();
        VOGeoServerTextSymbolizer2 testObj = new VOGeoServerTextSymbolizer2(panel.getClass());
        SelectedChannelType channelType = null;
        testObj.updateSymbol(channelType);
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.text.VOGeoServerTextSymbolizer2#populate(org.geotools.styling.FeatureTypeStyle)}.
     */
    @Test
    void testPopulateFeatureTypeStyle() {
        TextSymbolizerDetails panel = new TextSymbolizerDetails();
        VOGeoServerTextSymbolizer2 testObj = new VOGeoServerTextSymbolizer2(panel.getClass());
        FeatureTypeStyle symbol = null;
        testObj.populate(symbol);
    }
}
