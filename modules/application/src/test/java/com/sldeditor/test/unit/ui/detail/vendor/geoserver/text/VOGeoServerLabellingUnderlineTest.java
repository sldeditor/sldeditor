/*
 * SLD Editor - The Open Source Java SLD Editor
 *
 * Copyright (C) 2018, SCISYS UK Limited
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

import com.sldeditor.common.data.SelectedSymbol;
import com.sldeditor.common.defaultsymbol.DefaultSymbols;
import com.sldeditor.common.vendoroption.minversion.VendorOptionPresent;
import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.ui.detail.TextSymbolizerDetails;
import com.sldeditor.ui.detail.vendor.geoserver.text.VOGeoServerLabellingUnderline;
import com.sldeditor.ui.iface.UpdateSymbolInterface;
import java.util.ArrayList;
import java.util.List;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.RasterSymbolizer;
import org.geotools.styling.SelectedChannelType;
import org.geotools.styling.TextSymbolizer;
import org.geotools.styling.TextSymbolizer2;
import org.junit.jupiter.api.Test;

/**
 * The Class VOGeoServerLabellingUnderlineTest.
 *
 * @author Robert Ward (SCISYS)
 */
class VOGeoServerLabellingUnderlineTest {

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.text.VOGeoServerLabellingUnderline#getVendorOption()}.
     */
    @Test
    void testGetVendorOption() {
        VOGeoServerLabellingUnderline testObj =
                new VOGeoServerLabellingUnderline(TextSymbolizerDetails.class);
        assertNotNull(testObj.getVendorOption());
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.text.VOGeoServerLabellingUnderline#dataChanged(com.sldeditor.common.xml.ui.FieldIdEnum)}.
     */
    @Test
    void testDataChanged() {
        VOGeoServerLabellingUnderline testObj =
                new VOGeoServerLabellingUnderline(TextSymbolizerDetails.class);
        testObj.dataChanged(FieldIdEnum.ANGLE);
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.text.VOGeoServerLabellingUnderline#populate(org.geotools.styling.RasterSymbolizer)}.
     */
    @Test
    void testPopulateRasterSymbolizer() {
        VOGeoServerLabellingUnderline testObj =
                new VOGeoServerLabellingUnderline(TextSymbolizerDetails.class);
        RasterSymbolizer symbolizer = null;
        testObj.populate(symbolizer);
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.text.VOGeoServerLabellingUnderline#populate(com.sldeditor.common.data.SelectedSymbol)}.
     */
    @Test
    void testPopulateSelectedSymbol() {
        VOGeoServerLabellingUnderline testObj =
                new VOGeoServerLabellingUnderline(TextSymbolizerDetails.class);
        SelectedSymbol symbol = null;
        testObj.populate(symbol);
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.text.VOGeoServerLabellingUnderline#populate(org.geotools.styling.TextSymbolizer)}.
     */
    @Test
    void testPopulateTextSymbolizer() {
        VOGeoServerLabellingUnderline testObj =
                new VOGeoServerLabellingUnderline(TextSymbolizerDetails.class);
        TextSymbolizer symbolizer = null;
        testObj.populate(symbolizer);
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.text.VOGeoServerLabellingUnderline#populate(org.geotools.styling.FeatureTypeStyle)}.
     */
    @Test
    void testPopulateFeatureTypeStyle() {
        VOGeoServerLabellingUnderline testObj =
                new VOGeoServerLabellingUnderline(TextSymbolizerDetails.class);
        FeatureTypeStyle symbol = null;
        testObj.populate(symbol);
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.text.VOGeoServerLabellingUnderline#populate(org.geotools.styling.PolygonSymbolizer)}.
     */
    @Test
    void testPopulatePolygonSymbolizer() {
        VOGeoServerLabellingUnderline testObj =
                new VOGeoServerLabellingUnderline(TextSymbolizerDetails.class);
        PolygonSymbolizer symbolizer = null;
        testObj.populate(symbolizer);
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.text.VOGeoServerLabellingUnderline#getFieldDataManager()}.
     */
    @Test
    void testGetFieldDataManager() {
        VOGeoServerLabellingUnderline testObj =
                new VOGeoServerLabellingUnderline(TextSymbolizerDetails.class);
        assertNotNull(testObj.getFieldDataManager());
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.text.VOGeoServerLabellingUnderline#getPanel()}.
     */
    @Test
    void testGetPanel() {
        VOGeoServerLabellingUnderline testObj =
                new VOGeoServerLabellingUnderline(TextSymbolizerDetails.class);
        assertNotNull(testObj.getPanel());
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.text.VOGeoServerLabellingUnderline#setParentPanel(com.sldeditor.ui.iface.UpdateSymbolInterface)}.
     */
    @Test
    void testSetParentPanel() {
        class TestUpdateSymbolInterface implements UpdateSymbolInterface {
            public boolean called = false;

            @Override
            public void dataChanged(FieldIdEnum changedField) {
                called = true;
            }
        }

        TestUpdateSymbolInterface receiver = new TestUpdateSymbolInterface();
        VOGeoServerLabellingUnderline testObj =
                new VOGeoServerLabellingUnderline(TextSymbolizerDetails.class);
        testObj.setParentPanel(receiver);
        testObj.dataChanged(FieldIdEnum.ANGLE);
        assertTrue(receiver.called);

        assertEquals(receiver, testObj.getParentPanel());
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.text.VOGeoServerLabellingUnderline#isDataPresent()}.
     */
    @Test
    void testIsDataPresent() {
        VOGeoServerLabellingUnderline testObj =
                new VOGeoServerLabellingUnderline(TextSymbolizerDetails.class);
        assertTrue(testObj.isDataPresent());
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.text.VOGeoServerLabellingUnderline#updateSymbol(org.geotools.styling.PolygonSymbolizer)}.
     */
    @Test
    void testUpdateSymbolPolygonSymbolizer() {
        VOGeoServerLabellingUnderline testObj =
                new VOGeoServerLabellingUnderline(TextSymbolizerDetails.class);
        PolygonSymbolizer symbolizer = null;
        testObj.updateSymbol(symbolizer);
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.text.VOGeoServerLabellingUnderline#updateSymbol(org.geotools.styling.TextSymbolizer)}.
     */
    @Test
    void testUpdateSymbolTextSymbolizer() {
        VOGeoServerLabellingUnderline testObj =
                new VOGeoServerLabellingUnderline(TextSymbolizerDetails.class);
        TextSymbolizer symbolizer = null;
        testObj.updateSymbol(symbolizer);

        symbolizer = DefaultSymbols.createDefaultTextSymbolizer();

        symbolizer.getOptions().put(TextSymbolizer2.UNDERLINE_TEXT_KEY, "true");

        testObj.populate(symbolizer);

        TextSymbolizer newSymbolizer = DefaultSymbols.createDefaultTextSymbolizer();

        testObj.updateSymbol(newSymbolizer);

        assertEquals(symbolizer.getOptions().size(), newSymbolizer.getOptions().size());
        for (String key : symbolizer.getOptions().keySet()) {
            assertEquals(symbolizer.getOptions().get(key), newSymbolizer.getOptions().get(key));
        }
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.text.VOGeoServerLabellingUnderline#updateSymbol(org.geotools.styling.RasterSymbolizer)}.
     */
    @Test
    void testUpdateSymbolRasterSymbolizer() {
        VOGeoServerLabellingUnderline testObj =
                new VOGeoServerLabellingUnderline(TextSymbolizerDetails.class);
        RasterSymbolizer symbolizer = null;
        testObj.updateSymbol(symbolizer);
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.text.VOGeoServerLabellingUnderline#updateSymbol(org.geotools.styling.FeatureTypeStyle)}.
     */
    @Test
    void testUpdateSymbolFeatureTypeStyle() {
        VOGeoServerLabellingUnderline testObj =
                new VOGeoServerLabellingUnderline(TextSymbolizerDetails.class);
        FeatureTypeStyle symbolizer = null;
        testObj.updateSymbol(symbolizer);
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.text.VOGeoServerLabellingUnderline#preLoadSymbol()}.
     */
    @Test
    void testPreLoadSymbol() {
        VOGeoServerLabellingUnderline testObj =
                new VOGeoServerLabellingUnderline(TextSymbolizerDetails.class);
        testObj.preLoadSymbol();
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.text.VOGeoServerLabellingUnderline#getVendorOptionInfo()}.
     */
    @Test
    void testGetVendorOptionInfo() {
        VOGeoServerLabellingUnderline testObj =
                new VOGeoServerLabellingUnderline(TextSymbolizerDetails.class);
        assertNotNull(testObj.getVendorOptionInfo());
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.text.VOGeoServerLabellingUnderline#getMinimumVersion(java.lang.Object,
     * java.lang.Object, java.util.List)}.
     */
    @Test
    void testGetMinimumVersion() {
        VOGeoServerLabellingUnderline testObj =
                new VOGeoServerLabellingUnderline(TextSymbolizerDetails.class);
        TextSymbolizer symbolizer = null;
        testObj.updateSymbol(symbolizer);

        symbolizer = DefaultSymbols.createDefaultTextSymbolizer();

        symbolizer.getOptions().put(TextSymbolizer2.UNDERLINE_TEXT_KEY, "true");

        List<VendorOptionPresent> vendorOptionsPresentList = null;
        testObj.getMinimumVersion(symbolizer, "", vendorOptionsPresentList);

        vendorOptionsPresentList = new ArrayList<VendorOptionPresent>();
        testObj.getMinimumVersion(symbolizer, "", vendorOptionsPresentList);
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.text.VOGeoServerLabellingUnderline#populate(org.geotools.styling.SelectedChannelType)}.
     */
    @Test
    void testPopulateSelectedChannelType() {
        VOGeoServerLabellingUnderline testObj =
                new VOGeoServerLabellingUnderline(TextSymbolizerDetails.class);
        SelectedChannelType channelType = null;
        testObj.populate(channelType);
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.text.VOGeoServerLabellingUnderline#updateSymbol(org.geotools.styling.SelectedChannelType)}.
     */
    @Test
    void testUpdateSymbolSelectedChannelType() {
        VOGeoServerLabellingUnderline testObj =
                new VOGeoServerLabellingUnderline(TextSymbolizerDetails.class);
        SelectedChannelType channelType = null;
        testObj.updateSymbol(channelType);
    }
}
