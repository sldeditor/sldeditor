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

import org.geotools.styling.ChannelSelection;
import org.geotools.styling.ContrastEnhancement;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.RasterSymbolizer;
import org.geotools.styling.SelectedChannelType;

import com.sldeditor.common.vendoroption.info.VendorOptionInfo;
import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.common.xml.ui.GroupIdEnum;
import com.sldeditor.ui.detail.RasterSymbolizerDetails;

/**
 * The Class VOGeoServerContrastEnhancementNormalizeGreen.
 *
 * @author Robert Ward (SCISYS)
 */
public class VOGeoServerContrastEnhancementNormalizeGreen
        extends VOGeoServerContrastEnhancementNormalize {

    /** The Constant PANEL_CONFIG. */
    private static final String PANEL_CONFIG =
            "symbol/raster/PanelConfig_ContrastEnhancementNormalizeGreen.xml";

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /**
     * Instantiates a new VO geo server contrast enhancement normalize green.
     *
     * @param panelId the panel id
     * @param parentPanel the parent panel
     */
    public VOGeoServerContrastEnhancementNormalizeGreen(Class<?> panelId,
            RasterSymbolizerDetails parentPanel) {
        super(panelId, PANEL_CONFIG, parentPanel, FieldIdEnum.VO_RASTER_NORMALIZE_ALGORITHM_GREEN,
                FieldIdEnum.VO_RASTER_NORMALIZE_MIN_VALUE_GREEN,
                FieldIdEnum.VO_RASTER_NORMALIZE_MAX_VALUE_GREEN);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.ui.detail.vendor.geoserver.raster.VOGeoServerContrastEnhancementNormalize#getContrastEnhancement(com.sldeditor.common.xml.ui.
     * GroupIdEnum, org.geotools.styling.ChannelSelection)
     */
    @Override
    protected ContrastEnhancement getContrastEnhancement(GroupIdEnum id,
            ChannelSelection channelSelection) {
        if (id == GroupIdEnum.RASTER_RGB_CHANNEL_OPTION) {
            SelectedChannelType[] channelTypes = channelSelection.getRGBChannels();

            return channelTypes[1].getContrastEnhancement();
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.ui.detail.vendor.geoserver.raster.VOGeoServerContrastEnhancementNormalize#getContrastEnhancement(org.geotools.styling.
     * RasterSymbolizer)
     */
    @Override
    protected ContrastEnhancement getContrastEnhancement(RasterSymbolizer rasterSymbolizer) {
        if (rasterSymbolizer != null) {
            ChannelSelection channelSelection = rasterSymbolizer.getChannelSelection();
            if (channelSelection != null) {
                SelectedChannelType[] rgbChannels = channelSelection.getRGBChannels();
                if (rgbChannels != null) {
                    if (rgbChannels[1] != null) {
                        return rgbChannels[1].getContrastEnhancement();
                    }
                }
            }
        }
        return null;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.ui.detail.vendor.geoserver.VendorOptionInterface#getVendorOptionInfo()
     */
    @Override
    public VendorOptionInfo getVendorOptionInfo() {
        return null;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.ui.detail.vendor.geoserver.VendorOptionInterface#populate(org.geotools.styling.FeatureTypeStyle)
     */
    @Override
    public void populate(FeatureTypeStyle featureTypeStyle) {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see com.sldeditor.ui.detail.vendor.geoserver.VendorOptionInterface#updateSymbol(org.geotools.styling.FeatureTypeStyle)
     */
    @Override
    public void updateSymbol(FeatureTypeStyle featureTypeStyle) {
        // TODO Auto-generated method stub
        
    }
}
