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

import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.common.xml.ui.GroupIdEnum;
import com.sldeditor.ui.detail.RasterSymbolizerDetails;
import org.geotools.styling.ChannelSelection;
import org.geotools.styling.ContrastEnhancement;
import org.geotools.styling.RasterSymbolizer;

/**
 * The Class VOGeoServerContrastEnhancementNormalizeOverall.
 *
 * @author Robert Ward (SCISYS)
 */
public class VOGeoServerContrastEnhancementNormalizeOverall
        extends VOGeoServerContrastEnhancementNormalize {

    /** The Constant PANEL_CONFIG. */
    private static final String PANEL_CONFIG =
            "symbol/raster/PanelConfig_ContrastEnhancementNormalizeOverall.xml";

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /**
     * Instantiates a new VO geo server contrast enhancement normalize overall.
     *
     * @param panelId the panel id
     * @param parentPanel the parent panel
     */
    public VOGeoServerContrastEnhancementNormalizeOverall(
            Class<?> panelId, RasterSymbolizerDetails parentPanel) {
        super(
                panelId,
                PANEL_CONFIG,
                parentPanel,
                FieldIdEnum.VO_RASTER_NORMALIZE_ALGORITHM_OVERALL,
                FieldIdEnum.VO_RASTER_NORMALIZE_MIN_VALUE_OVERALL,
                FieldIdEnum.VO_RASTER_NORMALIZE_MAX_VALUE_OVERALL);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.ui.detail.vendor.geoserver.raster.VOGeoServerContrastEnhancementNormalize#getContrastEnhancement(com.sldeditor.common.xml.ui.
     * GroupIdEnum, org.geotools.styling.ChannelSelection)
     */
    @Override
    protected ContrastEnhancement getContrastEnhancement(
            GroupIdEnum id, ChannelSelection channelSelection) {
        // Does nothing
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
            return rasterSymbolizer.getContrastEnhancement();
        }
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.ui.detail.vendor.geoserver.VendorOptionInterface#updateSymbol(org.geotools.styling.RasterSymbolizer)
     */
    @Override
    public void updateSymbol(RasterSymbolizer rasterSymbolizer) {

        if (rasterSymbolizer != null) {
            ContrastEnhancement contrastEnhancement = rasterSymbolizer.getContrastEnhancement();

            if (contrastEnhancement != null) {
                extractNormalizeVendorOption(contrastEnhancement);
            }
        }
    }
}
