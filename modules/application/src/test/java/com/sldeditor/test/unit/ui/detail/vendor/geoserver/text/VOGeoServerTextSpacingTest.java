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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.sldeditor.common.Controller;
import com.sldeditor.common.data.SelectedSymbol;
import com.sldeditor.common.vendoroption.minversion.VendorOptionPresent;
import com.sldeditor.ui.detail.TextSymbolizerDetails;
import com.sldeditor.ui.detail.vendor.geoserver.text.VOGeoServerTextSpacing;
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
import org.geotools.styling.StyleFactoryImpl;
import org.geotools.styling.TextSymbolizer2;
import org.junit.Test;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Literal;

/**
 * The unit test for VOGeoServerTextSpacing.
 *
 * <p>{@link com.sldeditor.ui.detail.vendor.geoserver.text.VOGeoServerTextSpacing}
 *
 * @author Robert Ward (SCISYS)
 */
public class VOGeoServerTextSpacingTest {

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.text.VOGeoServerTextSpacingTest#VOGeoServerTextSpacingTest(java.lang.Class,
     * com.sldeditor.filter.v2.function.FunctionNameInterface)}.
     */
    @Test
    public void testVOGeoServerTextSpacingTest() {
        TextSymbolizerDetails panel = new TextSymbolizerDetails();

        TextSymbolizer2 textSymbolizer = null;
        VOGeoServerTextSpacing testObj = new VOGeoServerTextSpacing(panel.getClass());
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

        textSymbolizer.getOptions().put("wordSpacing", String.valueOf(42));
        textSymbolizer.getOptions().put("charSpacing", String.valueOf(21));

        Controller.getInstance().setPopulating(true);
        testObj.populate(textSymbolizer);
        Controller.getInstance().setPopulating(false);
        testObj.updateSymbol(textSymbolizer);

        // Find minimum version with textSymbolizer2 values set
        List<VendorOptionPresent> vendorOptionsPresentList = new ArrayList<VendorOptionPresent>();
        testObj.getMinimumVersion(null, textSymbolizer, vendorOptionsPresentList);
        assertEquals(2, vendorOptionsPresentList.size());

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

        FeatureTypeStyle fts = null;
        testObj.populate(fts);
        testObj.updateSymbol(fts);

        RasterSymbolizer raster = null;
        testObj.populate(raster);
        testObj.updateSymbol(raster);
        testObj.preLoadSymbol();
        assertTrue(testObj.isDataPresent());
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.text.VOGeoServerTextSpacing#getVendorOption()}.
     */
    @Test
    public void testGetVendorOption() {}

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.text.VOGeoServerTextSpacing#dataChanged(com.sldeditor.common.xml.ui.FieldIdEnum)}.
     */
    @Test
    public void testDataChanged() {}

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.text.VOGeoServerTextSpacing#populate(com.sldeditor.common.data.SelectedSymbol)}.
     */
    @Test
    public void testPopulateSelectedSymbol() {}

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.text.VOGeoServerTextSpacing#populate(org.geotools.styling.TextSymbolizer)}.
     */
    @Test
    public void testPopulateTextSymbolizer() {}

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.text.VOGeoServerTextSpacing#populate(org.geotools.styling.RasterSymbolizer)}.
     */
    @Test
    public void testPopulateRasterSymbolizer() {}

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.text.VOGeoServerTextSpacing#populate(org.geotools.styling.FeatureTypeStyle)}.
     */
    @Test
    public void testPopulateFeatureTypeStyle() {}

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.text.VOGeoServerTextSpacing#populate(org.geotools.styling.PolygonSymbolizer)}.
     */
    @Test
    public void testPopulatePolygonSymbolizer() {}

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.text.VOGeoServerTextSpacing#getFieldDataManager()}.
     */
    @Test
    public void testGetFieldDataManager() {}

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.text.VOGeoServerTextSpacing#updateSymbol(org.geotools.styling.RasterSymbolizer)}.
     */
    @Test
    public void testUpdateSymbolRasterSymbolizer() {}

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.text.VOGeoServerTextSpacing#updateSymbol(org.geotools.styling.PolygonSymbolizer)}.
     */
    @Test
    public void testUpdateSymbolPolygonSymbolizer() {}

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.text.VOGeoServerTextSpacing#updateSymbol(org.geotools.styling.TextSymbolizer)}.
     */
    @Test
    public void testUpdateSymbolTextSymbolizer() {}

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.text.VOGeoServerTextSpacing#updateSymbol(org.geotools.styling.FeatureTypeStyle)}.
     */
    @Test
    public void testUpdateSymbolFeatureTypeStyle() {}

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.text.VOGeoServerTextSpacing#getPanel()}.
     */
    @Test
    public void testGetPanel() {}

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.text.VOGeoServerTextSpacing#setParentPanel(com.sldeditor.ui.iface.UpdateSymbolInterface)}.
     */
    @Test
    public void testSetParentPanel() {}

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.text.VOGeoServerTextSpacing#isDataPresent()}.
     */
    @Test
    public void testIsDataPresent() {}

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.text.VOGeoServerTextSpacing#preLoadSymbol()}.
     */
    @Test
    public void testPreLoadSymbol() {}

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.text.VOGeoServerTextSpacing#getParentPanel()}.
     */
    @Test
    public void testGetParentPanel() {}

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.text.VOGeoServerTextSpacing#getVendorOptionInfo()}.
     */
    @Test
    public void testGetVendorOptionInfo() {}

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.text.VOGeoServerTextSpacing#getMinimumVersion(java.lang.Object,
     * java.lang.Object, java.util.List)}.
     */
    @Test
    public void testGetMinimumVersion() {}
}
