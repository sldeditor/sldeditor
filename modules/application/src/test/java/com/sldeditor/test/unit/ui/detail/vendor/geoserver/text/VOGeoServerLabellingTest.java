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
import com.sldeditor.ui.detail.vendor.geoserver.text.VOGeoServerLabelling;
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
 * The Class VOGeoServerLabellingTest.
 *
 * @author Robert Ward (SCISYS)
 */
class VOGeoServerLabellingTest {

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.text.VOGeoServerLabelling#getVendorOption()}.
     */
    @Test
    void testGetVendorOption() {
        VOGeoServerLabelling testObj = new VOGeoServerLabelling(TextSymbolizerDetails.class);
        assertNotNull(testObj.getVendorOption());
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.text.VOGeoServerLabelling#dataChanged(com.sldeditor.common.xml.ui.FieldIdEnum)}.
     */
    @Test
    void testDataChanged() {
        VOGeoServerLabelling testObj = new VOGeoServerLabelling(TextSymbolizerDetails.class);
        testObj.dataChanged(FieldIdEnum.ANGLE);
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.text.VOGeoServerLabelling#populate(org.geotools.styling.RasterSymbolizer)}.
     */
    @Test
    void testPopulateRasterSymbolizer() {
        VOGeoServerLabelling testObj = new VOGeoServerLabelling(TextSymbolizerDetails.class);
        RasterSymbolizer symbolizer = null;
        testObj.populate(symbolizer);
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.text.VOGeoServerLabelling#populate(com.sldeditor.common.data.SelectedSymbol)}.
     */
    @Test
    void testPopulateSelectedSymbol() {
        VOGeoServerLabelling testObj = new VOGeoServerLabelling(TextSymbolizerDetails.class);
        SelectedSymbol symbol = null;
        testObj.populate(symbol);
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.text.VOGeoServerLabelling#populate(org.geotools.styling.TextSymbolizer)}.
     */
    @Test
    void testPopulateTextSymbolizer() {
        VOGeoServerLabelling testObj = new VOGeoServerLabelling(TextSymbolizerDetails.class);
        TextSymbolizer symbolizer = null;
        testObj.populate(symbolizer);
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.text.VOGeoServerLabelling#populate(org.geotools.styling.FeatureTypeStyle)}.
     */
    @Test
    void testPopulateFeatureTypeStyle() {
        VOGeoServerLabelling testObj = new VOGeoServerLabelling(TextSymbolizerDetails.class);
        FeatureTypeStyle symbol = null;
        testObj.populate(symbol);
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.text.VOGeoServerLabelling#populate(org.geotools.styling.PolygonSymbolizer)}.
     */
    @Test
    void testPopulatePolygonSymbolizer() {
        VOGeoServerLabelling testObj = new VOGeoServerLabelling(TextSymbolizerDetails.class);
        PolygonSymbolizer symbolizer = null;
        testObj.populate(symbolizer);
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.text.VOGeoServerLabelling#getFieldDataManager()}.
     */
    @Test
    void testGetFieldDataManager() {
        VOGeoServerLabelling testObj = new VOGeoServerLabelling(TextSymbolizerDetails.class);
        assertNotNull(testObj.getFieldDataManager());
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.text.VOGeoServerLabelling#getPanel()}.
     */
    @Test
    void testGetPanel() {
        VOGeoServerLabelling testObj = new VOGeoServerLabelling(TextSymbolizerDetails.class);
        assertNotNull(testObj.getPanel());
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.text.VOGeoServerLabelling#setParentPanel(com.sldeditor.ui.iface.UpdateSymbolInterface)}.
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
        VOGeoServerLabelling testObj = new VOGeoServerLabelling(TextSymbolizerDetails.class);
        testObj.setParentPanel(receiver);
        testObj.dataChanged(FieldIdEnum.ANGLE);
        assertTrue(receiver.called);

        assertEquals(receiver, testObj.getParentPanel());
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.text.VOGeoServerLabelling#isDataPresent()}.
     */
    @Test
    void testIsDataPresent() {
        VOGeoServerLabelling testObj = new VOGeoServerLabelling(TextSymbolizerDetails.class);
        assertTrue(testObj.isDataPresent());
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.text.VOGeoServerLabelling#updateSymbol(org.geotools.styling.PolygonSymbolizer)}.
     */
    @Test
    void testUpdateSymbolPolygonSymbolizer() {
        VOGeoServerLabelling testObj = new VOGeoServerLabelling(TextSymbolizerDetails.class);
        PolygonSymbolizer symbolizer = null;
        testObj.updateSymbol(symbolizer);
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.text.VOGeoServerLabelling#updateSymbol(org.geotools.styling.TextSymbolizer)}.
     */
    @Test
    void testUpdateSymbolTextSymbolizer() {
        VOGeoServerLabelling testObj = new VOGeoServerLabelling(TextSymbolizerDetails.class);
        TextSymbolizer symbolizer = null;
        testObj.updateSymbol(symbolizer);

        symbolizer = DefaultSymbols.createDefaultTextSymbolizer();

        symbolizer.getOptions().put(TextSymbolizer2.ALLOW_OVERRUNS_KEY, "false");
        symbolizer.getOptions().put(TextSymbolizer2.AUTO_WRAP_KEY, "11");
        symbolizer.getOptions().put(TextSymbolizer2.CONFLICT_RESOLUTION_KEY, "false");
        symbolizer.getOptions().put(TextSymbolizer2.FOLLOW_LINE_KEY, "true");
        symbolizer.getOptions().put(TextSymbolizer2.FORCE_LEFT_TO_RIGHT_KEY, "false");
        symbolizer.getOptions().put(TextSymbolizer2.GOODNESS_OF_FIT_KEY, "1.234");
        symbolizer.getOptions().put(TextSymbolizer2.GRAPHIC_MARGIN_KEY, "23");
        symbolizer.getOptions().put(TextSymbolizer2.GRAPHIC_RESIZE_KEY, "stretch");
        symbolizer.getOptions().put(TextSymbolizer2.GROUP_KEY, "true");
        symbolizer.getOptions().put(TextSymbolizer2.LABEL_ALL_GROUP_KEY, "true");
        symbolizer.getOptions().put(TextSymbolizer2.LABEL_REPEAT_KEY, "56");
        symbolizer.getOptions().put(TextSymbolizer2.MAX_ANGLE_DELTA_KEY, "23.0");
        symbolizer.getOptions().put(TextSymbolizer2.MAX_DISPLACEMENT_KEY, "76");
        symbolizer.getOptions().put(TextSymbolizer2.MIN_GROUP_DISTANCE_KEY, "2");
        symbolizer.getOptions().put(TextSymbolizer2.PARTIALS_KEY, "true");
        symbolizer.getOptions().put(TextSymbolizer2.POLYGONALIGN_KEY, "mbr");
        symbolizer.getOptions().put(TextSymbolizer2.SPACE_AROUND_KEY, "7");

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
     * com.sldeditor.ui.detail.vendor.geoserver.text.VOGeoServerLabelling#updateSymbol(org.geotools.styling.RasterSymbolizer)}.
     */
    @Test
    void testUpdateSymbolRasterSymbolizer() {
        VOGeoServerLabelling testObj = new VOGeoServerLabelling(TextSymbolizerDetails.class);
        RasterSymbolizer symbolizer = null;
        testObj.updateSymbol(symbolizer);
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.text.VOGeoServerLabelling#updateSymbol(org.geotools.styling.FeatureTypeStyle)}.
     */
    @Test
    void testUpdateSymbolFeatureTypeStyle() {
        VOGeoServerLabelling testObj = new VOGeoServerLabelling(TextSymbolizerDetails.class);
        FeatureTypeStyle symbolizer = null;
        testObj.updateSymbol(symbolizer);
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.text.VOGeoServerLabelling#preLoadSymbol()}.
     */
    @Test
    void testPreLoadSymbol() {
        VOGeoServerLabelling testObj = new VOGeoServerLabelling(TextSymbolizerDetails.class);
        testObj.preLoadSymbol();
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.text.VOGeoServerLabelling#getVendorOptionInfo()}.
     */
    @Test
    void testGetVendorOptionInfo() {
        VOGeoServerLabelling testObj = new VOGeoServerLabelling(TextSymbolizerDetails.class);
        assertNotNull(testObj.getVendorOptionInfo());
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.text.VOGeoServerLabelling#getMinimumVersion(java.lang.Object,
     * java.lang.Object, java.util.List)}.
     */
    @Test
    void testGetMinimumVersion() {
        VOGeoServerLabelling testObj = new VOGeoServerLabelling(TextSymbolizerDetails.class);
        TextSymbolizer symbolizer = null;
        testObj.updateSymbol(symbolizer);

        symbolizer = DefaultSymbols.createDefaultTextSymbolizer();

        symbolizer.getOptions().put(TextSymbolizer2.ALLOW_OVERRUNS_KEY, "false");
        symbolizer.getOptions().put(TextSymbolizer2.AUTO_WRAP_KEY, "11");
        symbolizer.getOptions().put(TextSymbolizer2.CONFLICT_RESOLUTION_KEY, "false");
        symbolizer.getOptions().put(TextSymbolizer2.FOLLOW_LINE_KEY, "true");
        symbolizer.getOptions().put(TextSymbolizer2.FORCE_LEFT_TO_RIGHT_KEY, "false");
        symbolizer.getOptions().put(TextSymbolizer2.GOODNESS_OF_FIT_KEY, "1.234");
        symbolizer.getOptions().put(TextSymbolizer2.GRAPHIC_MARGIN_KEY, "23");
        symbolizer.getOptions().put(TextSymbolizer2.GRAPHIC_RESIZE_KEY, "stretch");
        symbolizer.getOptions().put(TextSymbolizer2.GROUP_KEY, "true");
        symbolizer.getOptions().put(TextSymbolizer2.LABEL_ALL_GROUP_KEY, "true");
        symbolizer.getOptions().put(TextSymbolizer2.LABEL_REPEAT_KEY, "56");
        symbolizer.getOptions().put(TextSymbolizer2.MAX_ANGLE_DELTA_KEY, "23.0");
        symbolizer.getOptions().put(TextSymbolizer2.MAX_DISPLACEMENT_KEY, "76");
        symbolizer.getOptions().put(TextSymbolizer2.MIN_GROUP_DISTANCE_KEY, "2");
        symbolizer.getOptions().put(TextSymbolizer2.PARTIALS_KEY, "true");
        symbolizer.getOptions().put(TextSymbolizer2.POLYGONALIGN_KEY, "mbr");
        symbolizer.getOptions().put(TextSymbolizer2.SPACE_AROUND_KEY, "7");

        List<VendorOptionPresent> vendorOptionsPresentList = null;
        testObj.getMinimumVersion(symbolizer, "", vendorOptionsPresentList);

        vendorOptionsPresentList = new ArrayList<VendorOptionPresent>();
        testObj.getMinimumVersion(symbolizer, "", vendorOptionsPresentList);
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.text.VOGeoServerLabelling#populate(org.geotools.styling.SelectedChannelType)}.
     */
    @Test
    void testPopulateSelectedChannelType() {
        VOGeoServerLabelling testObj = new VOGeoServerLabelling(TextSymbolizerDetails.class);
        SelectedChannelType channelType = null;
        testObj.populate(channelType);
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.text.VOGeoServerLabelling#updateSymbol(org.geotools.styling.SelectedChannelType)}.
     */
    @Test
    void testUpdateSymbolSelectedChannelType() {
        VOGeoServerLabelling testObj = new VOGeoServerLabelling(TextSymbolizerDetails.class);
        SelectedChannelType channelType = null;
        testObj.updateSymbol(channelType);
    }
}
