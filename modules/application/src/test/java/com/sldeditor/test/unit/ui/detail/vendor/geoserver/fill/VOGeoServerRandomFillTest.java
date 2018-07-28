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

package com.sldeditor.test.unit.ui.detail.vendor.geoserver.fill;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sldeditor.common.data.SelectedSymbol;
import com.sldeditor.common.defaultsymbol.DefaultSymbols;
import com.sldeditor.common.vendoroption.minversion.VendorOptionPresent;
import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.ui.detail.PointFillDetails;
import com.sldeditor.ui.detail.vendor.geoserver.fill.VOGeoServerRandomFill;
import com.sldeditor.ui.iface.UpdateSymbolInterface;
import java.util.ArrayList;
import java.util.List;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.RasterSymbolizer;
import org.geotools.styling.SelectedChannelType;
import org.geotools.styling.TextSymbolizer;
import org.junit.jupiter.api.Test;

/**
 * The Class VOGeoServerRandomFillTest.
 *
 * @author Robert Ward (SCISYS)
 */
class VOGeoServerRandomFillTest {

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.fill.VOGeoServerRandomFill#getVendorOption()}.
     */
    @Test
    void testGetVendorOption() {
        VOGeoServerRandomFill testObj = new VOGeoServerRandomFill(PointFillDetails.class);
        assertNotNull(testObj.getVendorOption());
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.fill.VOGeoServerRandomFill#dataChanged(com.sldeditor.common.xml.ui.FieldIdEnum)}.
     */
    @Test
    void testDataChanged() {
        VOGeoServerRandomFill testObj = new VOGeoServerRandomFill(PointFillDetails.class);
        testObj.dataChanged(FieldIdEnum.ANGLE);
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.fill.VOGeoServerRandomFill#populate(org.geotools.styling.RasterSymbolizer)}.
     */
    @Test
    void testPopulateRasterSymbolizer() {
        VOGeoServerRandomFill testObj = new VOGeoServerRandomFill(PointFillDetails.class);
        RasterSymbolizer symbolizer = null;
        testObj.populate(symbolizer);
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.fill.VOGeoServerRandomFill#populate(com.sldeditor.common.data.SelectedSymbol)}.
     */
    @Test
    void testPopulateSelectedSymbol() {
        VOGeoServerRandomFill testObj = new VOGeoServerRandomFill(PointFillDetails.class);
        SelectedSymbol symbol = null;
        testObj.populate(symbol);
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.fill.VOGeoServerRandomFill#populate(org.geotools.styling.TextSymbolizer)}.
     */
    @Test
    void testPopulateTextSymbolizer() {
        VOGeoServerRandomFill testObj = new VOGeoServerRandomFill(PointFillDetails.class);
        TextSymbolizer symbolizer = null;
        testObj.populate(symbolizer);
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.fill.VOGeoServerRandomFill#populate(org.geotools.styling.FeatureTypeStyle)}.
     */
    @Test
    void testPopulateFeatureTypeStyle() {
        VOGeoServerRandomFill testObj = new VOGeoServerRandomFill(PointFillDetails.class);
        FeatureTypeStyle symbol = null;
        testObj.populate(symbol);
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.fill.VOGeoServerRandomFill#populate(org.geotools.styling.PolygonSymbolizer)}.
     */
    @Test
    void testPopulatePolygonSymbolizer() {
        VOGeoServerRandomFill testObj = new VOGeoServerRandomFill(PointFillDetails.class);
        PolygonSymbolizer symbolizer = null;
        testObj.populate(symbolizer);
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.fill.VOGeoServerRandomFill#getFieldDataManager()}.
     */
    @Test
    void testGetFieldDataManager() {
        VOGeoServerRandomFill testObj = new VOGeoServerRandomFill(PointFillDetails.class);
        assertNotNull(testObj.getFieldDataManager());
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.fill.VOGeoServerRandomFill#getPanel()}.
     */
    @Test
    void testGetPanel() {
        VOGeoServerRandomFill testObj = new VOGeoServerRandomFill(PointFillDetails.class);
        assertNotNull(testObj.getPanel());
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.fill.VOGeoServerRandomFill#setParentPanel(com.sldeditor.ui.iface.UpdateSymbolInterface)}.
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
        VOGeoServerRandomFill testObj = new VOGeoServerRandomFill(PointFillDetails.class);
        testObj.setParentPanel(receiver);
        testObj.dataChanged(FieldIdEnum.ANGLE);
        assertTrue(receiver.called);

        assertEquals(receiver, testObj.getParentPanel());
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.fill.VOGeoServerRandomFill#isDataPresent()}.
     */
    @Test
    void testIsDataPresent() {
        VOGeoServerRandomFill testObj = new VOGeoServerRandomFill(PointFillDetails.class);
        assertTrue(testObj.isDataPresent());
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.fill.VOGeoServerRandomFill#updateSymbol(org.geotools.styling.PolygonSymbolizer)}.
     */
    @Test
    void testUpdateSymbolPolygonSymbolizer() {
        VOGeoServerRandomFill testObj = new VOGeoServerRandomFill(PointFillDetails.class);
        PolygonSymbolizer symbolizer = null;
        testObj.updateSymbol(symbolizer);

        symbolizer = DefaultSymbols.createDefaultPolygonSymbolizer();

        symbolizer.getOptions().put("random", "free");
        symbolizer.getOptions().put("random-tile-size", "512");
        symbolizer.getOptions().put("random-rotation", "free");
        symbolizer.getOptions().put("random-symbol-count", "5");
        symbolizer.getOptions().put("random-seed", "67");

        testObj.populate(symbolizer);

        PolygonSymbolizer newSymbolizer = DefaultSymbols.createDefaultPolygonSymbolizer();

        testObj.updateSymbol(newSymbolizer);

        assertEquals(symbolizer.getOptions().size(), newSymbolizer.getOptions().size());
        for (String key : symbolizer.getOptions().keySet()) {
            assertEquals(symbolizer.getOptions().get(key), newSymbolizer.getOptions().get(key));
        }
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.fill.VOGeoServerRandomFill#updateSymbol(org.geotools.styling.TextSymbolizer)}.
     */
    @Test
    void testUpdateSymbolTextSymbolizer() {
        VOGeoServerRandomFill testObj = new VOGeoServerRandomFill(PointFillDetails.class);
        TextSymbolizer symbolizer = null;
        testObj.updateSymbol(symbolizer);
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.fill.VOGeoServerRandomFill#updateSymbol(org.geotools.styling.RasterSymbolizer)}.
     */
    @Test
    void testUpdateSymbolRasterSymbolizer() {
        VOGeoServerRandomFill testObj = new VOGeoServerRandomFill(PointFillDetails.class);
        RasterSymbolizer symbolizer = null;
        testObj.updateSymbol(symbolizer);
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.fill.VOGeoServerRandomFill#updateSymbol(org.geotools.styling.FeatureTypeStyle)}.
     */
    @Test
    void testUpdateSymbolFeatureTypeStyle() {
        VOGeoServerRandomFill testObj = new VOGeoServerRandomFill(PointFillDetails.class);
        FeatureTypeStyle symbolizer = null;
        testObj.updateSymbol(symbolizer);
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.fill.VOGeoServerRandomFill#preLoadSymbol()}.
     */
    @Test
    void testPreLoadSymbol() {
        VOGeoServerRandomFill testObj = new VOGeoServerRandomFill(PointFillDetails.class);
        testObj.preLoadSymbol();
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.fill.VOGeoServerRandomFill#getVendorOptionInfo()}.
     */
    @Test
    void testGetVendorOptionInfo() {
        VOGeoServerRandomFill testObj = new VOGeoServerRandomFill(PointFillDetails.class);
        assertNotNull(testObj.getVendorOptionInfo());
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.fill.VOGeoServerRandomFill#getMinimumVersion(java.lang.Object,
     * java.lang.Object, java.util.List)}.
     */
    @Test
    void testGetMinimumVersion() {
        VOGeoServerRandomFill testObj = new VOGeoServerRandomFill(PointFillDetails.class);
        PolygonSymbolizer symbolizer = null;
        testObj.updateSymbol(symbolizer);

        symbolizer = DefaultSymbols.createDefaultPolygonSymbolizer();

        symbolizer.getOptions().put("random", "free");
        symbolizer.getOptions().put("random-tile-size", "256");
        symbolizer.getOptions().put("random-rotation", "123");
        symbolizer.getOptions().put("random-symbol-count", "5");
        symbolizer.getOptions().put("random-seed", "67");

        List<VendorOptionPresent> vendorOptionsPresentList = null;
        testObj.getMinimumVersion(symbolizer, "", vendorOptionsPresentList);

        vendorOptionsPresentList = new ArrayList<VendorOptionPresent>();
        testObj.getMinimumVersion(symbolizer, "", vendorOptionsPresentList);
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.fill.VOGeoServerRandomFill#populate(org.geotools.styling.SelectedChannelType)}.
     */
    @Test
    void testPopulateSelectedChannelType() {
        VOGeoServerRandomFill testObj = new VOGeoServerRandomFill(PointFillDetails.class);
        SelectedChannelType channelType = null;
        testObj.populate(channelType);
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.fill.VOGeoServerRandomFill#updateSymbol(org.geotools.styling.SelectedChannelType)}.
     */
    @Test
    void testUpdateSymbolSelectedChannelType() {
        VOGeoServerRandomFill testObj = new VOGeoServerRandomFill(PointFillDetails.class);
        SelectedChannelType channelType = null;
        testObj.updateSymbol(channelType);
    }
}
