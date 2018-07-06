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

package com.sldeditor.ui.detail.vendor.geoserver.raster;

import java.util.List;

import org.geotools.styling.SelectedChannelType;

import com.sldeditor.common.vendoroption.VendorOptionManager;
import com.sldeditor.common.vendoroption.VersionData;
import com.sldeditor.ui.detail.vendor.geoserver.VendorOptionInterface;

/**
 * The Class ChannelName.
 * 
 * @author Robert Ward (SCISYS)
 */
class ChannelName {

    public static String RED = "Red";
    public static String GREEN = "Green";
    public static String BLUE = "Blue";
    public static String GREY = "Grey";
    
    /** The panel config expression. */
    private String panelConfigExpression = "symbol/raster/PanelConfig_ChannelName%sExpression.xml";

    /** The panel config no expression. */
    private String panelConfigNoExpression = "symbol/raster/PanelConfig_ChannelName%sNoExpression.xml";

    /** The raster channel name string. */
    private VOChannelNameNoExpression rasterChannelNameString = null;

    /** The raster channel name expression. */
    private VOChannelNameExpression rasterChannelNameExpression = null;

    /**
     * Instantiates a new channel name.
     *
     * @param expression the expression
     * @param noExpression the no expression
     * @param colourName the colour name
     */
    public ChannelName(VOChannelNameExpression expression, VOChannelNameNoExpression noExpression,
            String colourName) {
        rasterChannelNameExpression = expression;
        rasterChannelNameString = noExpression;

        rasterChannelNameString.setUp(String.format(panelConfigNoExpression, colourName));
        rasterChannelNameExpression.setUp(String.format(panelConfigExpression, colourName));
    }

    /**
     * Adds the to list.
     *
     * @param vendorOptionList the vendor option list
     */
    public void addToList(List<VendorOptionInterface> vendorOptionList) {
        vendorOptionList.add(rasterChannelNameString);
        vendorOptionList.add(rasterChannelNameExpression);
    }

    /**
     * Populate the field with the channel name.
     *
     * @param vendorOptionVersionsList the vendor option versions list
     * @param channelType the channel type
     */
    public void populate(List<VersionData> vendorOptionVersionsList,
            SelectedChannelType channelType) {
        if (VendorOptionManager.getInstance().isAllowed(vendorOptionVersionsList,
                rasterChannelNameString.getVendorOption())) {
            rasterChannelNameString.populate(channelType);
        } else if (VendorOptionManager.getInstance().isAllowed(vendorOptionVersionsList,
                rasterChannelNameExpression.getVendorOption())) {
            rasterChannelNameExpression.populate(channelType);
        }
    }

    /**
     * Extract the channel name.
     *
     * @param vendorOptioúnVersionsList the vendor option versions list
     * @param channelType the channel type
     */
    public void update(List<VersionData> vendorOptionVersionsList,
            SelectedChannelType channelType) {
        if (VendorOptionManager.getInstance().isAllowed(vendorOptionVersionsList,
                rasterChannelNameString.getVendorOption())) {
            rasterChannelNameString.updateSymbol(channelType);
        } else if (VendorOptionManager.getInstance().isAllowed(vendorOptionVersionsList,
                rasterChannelNameExpression.getVendorOption())) {
            rasterChannelNameExpression.updateSymbol(channelType);
        }
    }
}
