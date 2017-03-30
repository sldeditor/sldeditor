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

package com.sldeditor.ui.detail.vendor.geoserver;

import java.util.List;

import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.RasterSymbolizer;
import org.geotools.styling.TextSymbolizer;

import com.sldeditor.common.vendoroption.VendorOptionVersion;
import com.sldeditor.common.vendoroption.info.VendorOptionInfo;
import com.sldeditor.common.vendoroption.minversion.VendorOptionPresent;
import com.sldeditor.ui.detail.BasePanel;
import com.sldeditor.ui.iface.UpdateSymbolInterface;

/**
 * The Interface VendorOptionInterface.
 * 
 * @author Robert Ward (SCISYS)
 */
public interface VendorOptionInterface {

    /**
     * Gets the vendor option.
     *
     * @return the vendor option
     */
    public VendorOptionVersion getVendorOption();

    /**
     * Populate for a text symbolizer.
     *
     * @param textSymbolizer the text symbolizer
     */
    public void populate(TextSymbolizer textSymbolizer);

    /**
     * Populate for a polygon symbolizer.
     *
     * @param polygonSymbolizer the polygon symbolizer
     */
    public void populate(PolygonSymbolizer polygonSymbolizer);

    /**
     * Populate for a raster symbolizer.
     *
     * @param rasterSymbolizer the raster symbolizer
     */
    public void populate(RasterSymbolizer rasterSymbolizer);

    /**
     * Populate.
     *
     * @param featureTypeStyle the feature type style
     */
    public void populate(FeatureTypeStyle featureTypeStyle);

    /**
     * Update symbol for a polygon symbolizer.
     *
     * @param polygonSymbolizer the polygon symbolizer
     */
    public void updateSymbol(PolygonSymbolizer polygonSymbolizer);
    
    /**
     * Update symbol for a text symbolizer.
     *
     * @param textSymbolizer the text symbolizer
     */
    public void updateSymbol(TextSymbolizer textSymbolizer);

    /**
     * Update symbol for a raster symbolizer.
     *
     * @param rasterSymbolizer the raster symbolizer
     */
    public void updateSymbol(RasterSymbolizer rasterSymbolizer);

    /**
     * Update symbol.
     *
     * @param featureTypeStyle the feature type style
     */
    public void updateSymbol(FeatureTypeStyle featureTypeStyle);

    /**
     * Gets the panel.
     *
     * @return the panel
     */
    public BasePanel getPanel();

    /**
     * Sets the parent panel.
     *
     * @param parent the new parent panel
     */
    public void setParentPanel(UpdateSymbolInterface parent);

    /**
     * Gets the parent panel.
     *
     * @return the parent panel
     */
    UpdateSymbolInterface getParentPanel();

    /**
     * Gets the vendor option info.
     *
     * @return the vendor option info
     */
    public VendorOptionInfo getVendorOptionInfo();

    /**
     * Gets the minimum version for the SLD symbol.
     *
     * @param parentObj the parent obj
     * @param sldObj the sld obj
     * @param vendorOptionsPresentList the vendor options present list
     */
    public void getMinimumVersion(Object parentObj, Object sldObj,
            List<VendorOptionPresent> vendorOptionsPresentList);
}
