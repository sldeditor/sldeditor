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

package com.sldeditor.ui.detail.vendor.geoserver.raster;

import com.sldeditor.common.vendoroption.VendorOptionManager;
import com.sldeditor.common.vendoroption.VendorOptionUpdateInterface;
import com.sldeditor.common.vendoroption.VersionData;
import com.sldeditor.common.vendoroption.info.VendorOptionInfo;
import com.sldeditor.common.vendoroption.info.VendorOptionInfoManager;
import com.sldeditor.common.vendoroption.minversion.VendorOptionPresent;
import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.common.xml.ui.GroupIdEnum;
import com.sldeditor.ui.detail.GraphicPanelFieldManager;
import com.sldeditor.ui.detail.RasterSymbolizerDetails;
import com.sldeditor.ui.detail.vendor.VendorOptionFactoryInterface;
import com.sldeditor.ui.detail.vendor.geoserver.VendorOptionInterface;
import com.sldeditor.ui.iface.PopulateDetailsInterface;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.styling.ContrastEnhancement;
import org.geotools.styling.RasterSymbolizer;
import org.geotools.styling.SelectedChannelType;
import org.geotools.styling.StyleFactoryImpl;
import org.opengis.filter.expression.Expression;

/**
 * A factory for creating VendorOptionRaster objects.
 *
 * @author Robert Ward (SCISYS)
 */
public class VendorOptionRasterFactory
        implements VendorOptionFactoryInterface, VendorOptionUpdateInterface {

    // CHECKSTYLE:OFF
    private StyleFactoryImpl styleFactory =
            (StyleFactoryImpl) CommonFactoryFinder.getStyleFactory();

    /** The vendor option geo server contrast enhancement normalize (red). */
    private VOGeoServerContrastEnhancementNormalizeRed
            vendorOptionGeoServerContrastEnhancementNormalizeRed = null;

    /** The vendor option geo server contrast enhancement normalize (green). */
    private VOGeoServerContrastEnhancementNormalizeGreen
            vendorOptionGeoServerContrastEnhancementNormalizeGreen = null;

    /** The vendor option geo server contrast enhancement normalize (blue). */
    private VOGeoServerContrastEnhancementNormalizeBlue
            vendorOptionGeoServerContrastEnhancementNormalizeBlue = null;

    /** The vendor option geo server contrast enhancement normalize (grey). */
    private VOGeoServerContrastEnhancementNormalizeGrey
            vendorOptionGeoServerContrastEnhancementNormalizeGrey = null;

    /** The vendor option geo server contrast enhancement normalize (overall). */
    private VOGeoServerContrastEnhancementNormalizeOverall
            vendorOptionGeoServerContrastEnhancementNormalizeOverall = null;

    /** The channel name map. */
    private Map<GroupIdEnum, ChannelName> channelNameMap = new HashMap<GroupIdEnum, ChannelName>();

    // CHECKSTYLE:ON

    /** The vendor option list. */
    private List<VendorOptionInterface> vendorOptionList = new ArrayList<VendorOptionInterface>();

    /** The vendor option versions list. */
    private List<VersionData> vendorOptionVersionsList = new ArrayList<VersionData>();

    /**
     * Instantiates a new vendor option text factory.
     *
     * @param panelId the panel id
     * @param parentPanel the parent panel
     */
    public VendorOptionRasterFactory(Class<?> panelId, RasterSymbolizerDetails parentPanel) {
        // CHECKSTYLE:OFF
        vendorOptionGeoServerContrastEnhancementNormalizeRed =
                new VOGeoServerContrastEnhancementNormalizeRed(panelId, parentPanel);
        vendorOptionGeoServerContrastEnhancementNormalizeGreen =
                new VOGeoServerContrastEnhancementNormalizeGreen(panelId, parentPanel);
        vendorOptionGeoServerContrastEnhancementNormalizeBlue =
                new VOGeoServerContrastEnhancementNormalizeBlue(panelId, parentPanel);
        vendorOptionGeoServerContrastEnhancementNormalizeGrey =
                new VOGeoServerContrastEnhancementNormalizeGrey(panelId, parentPanel);
        vendorOptionGeoServerContrastEnhancementNormalizeOverall =
                new VOGeoServerContrastEnhancementNormalizeOverall(panelId, parentPanel);

        ChannelName redChannel =
                new ChannelName(
                        new VOChannelNameRedExpression(
                                panelId, FieldIdEnum.RASTER_RGB_RED_NAME_EXPRESSION),
                        new VOChannelNameRedNoExpression(
                                panelId, FieldIdEnum.RASTER_RGB_RED_NAME_STRING),
                        ChannelName.RED);

        ChannelName greenChannel =
                new ChannelName(
                        new VOChannelNameGreenExpression(
                                panelId, FieldIdEnum.RASTER_RGB_GREEN_NAME_EXPRESSION),
                        new VOChannelNameGreenNoExpression(
                                panelId, FieldIdEnum.RASTER_RGB_GREEN_NAME_STRING),
                        ChannelName.GREEN);

        ChannelName blueChannel =
                new ChannelName(
                        new VOChannelNameBlueExpression(
                                panelId, FieldIdEnum.RASTER_RGB_BLUE_NAME_EXPRESSION),
                        new VOChannelNameBlueNoExpression(
                                panelId, FieldIdEnum.RASTER_RGB_BLUE_NAME_STRING),
                        ChannelName.BLUE);

        ChannelName greyChannel =
                new ChannelName(
                        new VOChannelNameGreyExpression(
                                panelId, FieldIdEnum.RASTER_RGB_GREY_NAME_EXPRESSION),
                        new VOChannelNameGreyNoExpression(
                                panelId, FieldIdEnum.RASTER_RGB_GREY_NAME_STRING),
                        ChannelName.GREY);
        // CHECKSTYLE:ON

        vendorOptionList.add(vendorOptionGeoServerContrastEnhancementNormalizeRed);
        vendorOptionList.add(vendorOptionGeoServerContrastEnhancementNormalizeGreen);
        vendorOptionList.add(vendorOptionGeoServerContrastEnhancementNormalizeBlue);
        vendorOptionList.add(vendorOptionGeoServerContrastEnhancementNormalizeGrey);
        vendorOptionList.add(vendorOptionGeoServerContrastEnhancementNormalizeOverall);
        redChannel.addToList(vendorOptionList);
        greenChannel.addToList(vendorOptionList);
        blueChannel.addToList(vendorOptionList);
        greyChannel.addToList(vendorOptionList);

        channelNameMap.put(GroupIdEnum.RASTER_RGB_CHANNEL_RED, redChannel);
        channelNameMap.put(GroupIdEnum.RASTER_RGB_CHANNEL_GREEN, greenChannel);
        channelNameMap.put(GroupIdEnum.RASTER_RGB_CHANNEL_BLUE, blueChannel);
        channelNameMap.put(GroupIdEnum.RASTER_GREY_CHANNEL, greyChannel);

        VendorOptionManager.getInstance().addVendorOptionListener(this);
        VendorOptionInfoManager.getInstance().addVendorOptionInfo(this);
    }

    /**
     * Gets the vendor option list.
     *
     * @return the vendor option list
     */
    @Override
    public List<VendorOptionInterface> getVendorOptionList() {
        return vendorOptionList;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.sldeditor.ui.detail.vendor.VendorOptionFactoryInterface#getVendorOptionList(java.lang.
     * String)
     */
    @Override
    public List<VendorOptionInterface> getVendorOptionList(String className) {
        List<VendorOptionInterface> matchingList = new ArrayList<VendorOptionInterface>();

        for (VendorOptionInterface vendorOption : vendorOptionList) {
            if (vendorOption.getClass().getName().compareTo(className) == 0) {
                matchingList.add(vendorOption);
            }
        }
        return matchingList;
    }

    /**
     * Populate.
     *
     * @param rasterSymbolizer the raster symbolizer
     */
    public void populate(RasterSymbolizer rasterSymbolizer) {
        for (VendorOptionInterface vendorOption : vendorOptionList) {
            vendorOption.populate(rasterSymbolizer);
        }
    }

    /**
     * Update symbol.
     *
     * @param rasterSymbolizer the raster symbolizer
     */
    public void updateSymbol(RasterSymbolizer rasterSymbolizer) {
        for (VendorOptionInterface vendorOption : vendorOptionList) {
            boolean displayVendorOption =
                    VendorOptionManager.getInstance()
                            .isAllowed(vendorOptionVersionsList, vendorOption.getVendorOption());

            if (displayVendorOption) {
                vendorOption.updateSymbol(rasterSymbolizer);
            }
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.sldeditor.preferences.iface.PrefUpdateVendorOptionInterface#vendorOptionsUpdated(java.
     * util.List)
     */
    @Override
    public void vendorOptionsUpdated(List<VersionData> vendorOptionVersionsList) {
        this.vendorOptionVersionsList = vendorOptionVersionsList;
    }

    /**
     * Updates the field data manager.
     *
     * @param fieldConfigManager the field config manager
     */
    public void updateFieldDataManager(GraphicPanelFieldManager fieldConfigManager) {
        for (VendorOptionInterface vendorOption : vendorOptionList) {
            if (vendorOption != null) {
                PopulateDetailsInterface populateInterface =
                        (PopulateDetailsInterface) vendorOption;
                fieldConfigManager.add(populateInterface.getFieldDataManager());
            }
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.ui.detail.vendor.VendorOptionFactoryInterface#getVendorOptionInfoList()
     */
    @Override
    public List<VendorOptionInfo> getVendorOptionInfoList() {
        List<VendorOptionInfo> vendorOptionInfoList = new ArrayList<VendorOptionInfo>();

        for (VendorOptionInterface vendorOption : vendorOptionList) {
            VendorOptionInfo vendorOptionInfo = vendorOption.getVendorOptionInfo();
            if (vendorOptionInfo != null) {
                // Ensure no duplicates
                boolean found = false;
                for (VendorOptionInfo info : vendorOptionInfoList) {
                    if (info.compareTo(vendorOptionInfo) == 0) {
                        found = true;
                    }
                }
                if (!found) {
                    vendorOptionInfoList.add(vendorOptionInfo);
                }
            }
        }
        return vendorOptionInfoList;
    }

    /**
     * Gets the minimum version.
     *
     * @param parentObj the parent obj
     * @param sldObj the sld obj
     * @param vendorOptionsPresentList the vendor options present list
     */
    public void getMinimumVersion(
            Object parentObj, Object sldObj, List<VendorOptionPresent> vendorOptionsPresentList) {
        for (VendorOptionInterface vo : vendorOptionList) {
            vo.getMinimumVersion(parentObj, sldObj, vendorOptionsPresentList);
        }
    }

    /**
     * Sets the channel name.
     *
     * @param channelGroup the channel group
     * @param channelType the channel type
     */
    public void setChannelName(GroupIdEnum channelGroup, SelectedChannelType channelType) {

        ChannelName channelName = channelNameMap.get(channelGroup);

        if (channelName != null) {
            channelName.populate(vendorOptionVersionsList, channelType);
        }
    }

    /**
     * Gets the channel name.
     *
     * @param channelGroup the channel group
     * @return the channel name
     */
    public Expression getChannelName(GroupIdEnum channelGroup) {

        Expression channelNameExpression = Expression.NIL;

        ChannelName channelName = channelNameMap.get(channelGroup);

        if (channelName != null) {
            SelectedChannelType channelType =
                    styleFactory.createSelectedChannelType(
                            Expression.NIL, (ContrastEnhancement) null);

            channelName.update(vendorOptionVersionsList, channelType);

            channelNameExpression = channelType.getChannelName();
        }
        return channelNameExpression;
    }
}
