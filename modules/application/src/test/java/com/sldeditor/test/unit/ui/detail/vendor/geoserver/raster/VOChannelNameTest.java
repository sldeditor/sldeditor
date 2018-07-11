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

package com.sldeditor.test.unit.ui.detail.vendor.geoserver.raster;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sldeditor.common.data.SelectedSymbol;
import com.sldeditor.common.vendoroption.GeoServerVendorOption;
import com.sldeditor.common.vendoroption.VendorOptionManager;
import com.sldeditor.common.vendoroption.VersionData;
import com.sldeditor.common.vendoroption.minversion.VendorOptionPresent;
import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.common.xml.ui.GroupIdEnum;
import com.sldeditor.ui.detail.RasterSymbolizerDetails;
import com.sldeditor.ui.detail.vendor.geoserver.raster.VOChannelNameRedExpression;
import com.sldeditor.ui.detail.vendor.geoserver.raster.VOChannelNameRedNoExpression;
import com.sldeditor.ui.detail.vendor.geoserver.raster.VendorOptionRasterFactory;
import java.util.ArrayList;
import java.util.List;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.styling.ContrastEnhancement;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.RasterSymbolizer;
import org.geotools.styling.SelectedChannelType;
import org.geotools.styling.StyleFactoryImpl;
import org.geotools.styling.TextSymbolizer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.opengis.filter.expression.Expression;

/**
 * The unit test for VOChannelNameTest.
 *
 * <p>{@link com.sldeditor.ui.detail.vendor.geoserver.raster.ChannelName}
 *
 * @author Robert Ward (SCISYS)
 */
public class VOChannelNameTest {

    /** Called after each test. */
    @AfterEach
    public void afterEachTest() {
        VendorOptionManager.destroyInstance();
    }

    /**
     * Test method for populate {@link
     * com.sldeditor.ui.detail.vendor.geoserver.raster.VOGeoServerContrastEnhancementNormalizeRed#VOGeoServerContrastEnhancementNormalizeRed(java.lang.Class,
     * com.sldeditor.ui.detail.RasterSymbolizerDetails)}.
     */
    @Test
    public void testPopulate() {
        StyleFactoryImpl styleFactory = (StyleFactoryImpl) CommonFactoryFinder.getStyleFactory();

        RasterSymbolizerDetails panel = new RasterSymbolizerDetails();
        VendorOptionRasterFactory vendorOptionRasterFactory =
                new VendorOptionRasterFactory(getClass(), panel);

        List<VersionData> stringVersionDataList = new ArrayList<VersionData>();
        stringVersionDataList.add(VersionData.decode(GeoServerVendorOption.class, "2.3.1"));

        List<VersionData> expressionVersionDataList = new ArrayList<VersionData>();
        expressionVersionDataList.add(VersionData.decode(GeoServerVendorOption.class, "2.14.0"));

        // Change vendor option to string
        VendorOptionManager.getInstance().setSelectedVendorOptions(stringVersionDataList);

        String expectedNameString = "test grey name string";
        SelectedChannelType channelTypeString =
                styleFactory.createSelectedChannelType(
                        expectedNameString, (ContrastEnhancement) null);

        vendorOptionRasterFactory.setChannelName(
                GroupIdEnum.RASTER_GREY_CHANNEL, channelTypeString);
        Expression actual =
                vendorOptionRasterFactory.getChannelName(GroupIdEnum.RASTER_GREY_CHANNEL);

        assertTrue(actual.toString().equals(expectedNameString));

        // Change vendor option to expression
        VendorOptionManager.getInstance().setSelectedVendorOptions(expressionVersionDataList);

        String expectedNameExpression = "test grey name expression";
        SelectedChannelType channelTypeExpression =
                styleFactory.createSelectedChannelType(
                        expectedNameExpression, (ContrastEnhancement) null);

        vendorOptionRasterFactory.setChannelName(
                GroupIdEnum.RASTER_GREY_CHANNEL, channelTypeExpression);
        actual = vendorOptionRasterFactory.getChannelName(GroupIdEnum.RASTER_GREY_CHANNEL);
        assertTrue(actual.toString().equals(expectedNameExpression));
    }

    /** Increase coverage. */
    @Test
    public void increaseCoverage() {
        StyleFactoryImpl styleFactory = (StyleFactoryImpl) CommonFactoryFinder.getStyleFactory();

        VOChannelNameRedNoExpression noExpression =
                new VOChannelNameRedNoExpression(
                        getClass(), FieldIdEnum.VO_RASTER_RGB_RED_NAME_NO_EXPRESSION);
        noExpression.setUp("symbol/raster/PanelConfig_ChannelNameRedNoExpression.xml");
        noExpression.preLoadSymbol();
        noExpression.populate((FeatureTypeStyle) null);
        noExpression.populate((PolygonSymbolizer) null);
        noExpression.populate((TextSymbolizer) null);
        noExpression.populate((RasterSymbolizer) null);
        noExpression.populate((SelectedSymbol) null);
        noExpression.populate((SelectedSymbol) null);
        noExpression.updateSymbol((SelectedChannelType) null);
        noExpression.updateSymbol((PolygonSymbolizer) null);
        noExpression.updateSymbol((TextSymbolizer) null);
        noExpression.updateSymbol((RasterSymbolizer) null);
        noExpression.updateSymbol((SelectedChannelType) null);
        assertTrue(noExpression.isDataPresent());

        List<VendorOptionPresent> vendorOptionsPresentList = null;
        noExpression.getMinimumVersion(null, null, vendorOptionsPresentList);

        String expectedNameExpression = "test string";
        SelectedChannelType channelTypeExpression =
                styleFactory.createSelectedChannelType(
                        expectedNameExpression, (ContrastEnhancement) null);
        noExpression.getMinimumVersion(null, channelTypeExpression, vendorOptionsPresentList);

        VOChannelNameRedExpression expression =
                new VOChannelNameRedExpression(
                        getClass(), FieldIdEnum.VO_RASTER_RGB_RED_NAME_EXPRESSION);
        noExpression.setUp("symbol/raster/PanelConfig_ChannelNameRedExpression.xml");
        expression.preLoadSymbol();
        expression.populate((FeatureTypeStyle) null);
        expression.populate((PolygonSymbolizer) null);
        expression.populate((TextSymbolizer) null);
        expression.populate((RasterSymbolizer) null);
        expression.populate((SelectedSymbol) null);
        expression.updateSymbol((SelectedChannelType) null);
        expression.updateSymbol((FeatureTypeStyle) null);
        expression.updateSymbol((PolygonSymbolizer) null);
        expression.updateSymbol((TextSymbolizer) null);
        expression.updateSymbol((RasterSymbolizer) null);
        expression.updateSymbol((SelectedChannelType) null);
        assertTrue(expression.isDataPresent());
        expression.getMinimumVersion(null, null, vendorOptionsPresentList);
        expression.getMinimumVersion(null, channelTypeExpression, vendorOptionsPresentList);

        // Check minimum version
        vendorOptionsPresentList = new ArrayList<VendorOptionPresent>();
        noExpression.getMinimumVersion(null, channelTypeExpression, vendorOptionsPresentList);
        assertTrue(vendorOptionsPresentList.size() == 1);
        expression.getMinimumVersion(null, channelTypeExpression, vendorOptionsPresentList);
        assertTrue(vendorOptionsPresentList.size() == 2);
    }
}
