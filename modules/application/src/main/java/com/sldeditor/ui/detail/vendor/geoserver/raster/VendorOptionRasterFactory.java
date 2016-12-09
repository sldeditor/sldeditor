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

import java.util.ArrayList;
import java.util.List;

import org.geotools.styling.RasterSymbolizer;

import com.sldeditor.common.vendoroption.VendorOptionManager;
import com.sldeditor.common.vendoroption.VendorOptionUpdateInterface;
import com.sldeditor.common.vendoroption.VersionData;
import com.sldeditor.common.vendoroption.info.VendorOptionInfo;
import com.sldeditor.common.vendoroption.info.VendorOptionInfoManager;
import com.sldeditor.common.vendoroption.minversion.VendorOptionPresent;
import com.sldeditor.filter.v2.function.FunctionNameInterface;
import com.sldeditor.ui.detail.GraphicPanelFieldManager;
import com.sldeditor.ui.detail.RasterSymbolizerDetails;
import com.sldeditor.ui.detail.vendor.VendorOptionFactoryInterface;
import com.sldeditor.ui.detail.vendor.geoserver.VendorOptionInterface;
import com.sldeditor.ui.iface.PopulateDetailsInterface;

/**
 * A factory for creating VendorOptionRaster objects.
 * 
 * @author Robert Ward (SCISYS)
 */
public class VendorOptionRasterFactory implements VendorOptionFactoryInterface, VendorOptionUpdateInterface {

    /** The vendor option geo server contrast enhancement normalize (red). */
    private VOGeoServerContrastEnhancementNormalizeRed vendorOptionGeoServerContrastEnhancementNormalizeRed = null;

    /** The vendor option geo server contrast enhancement normalize (green). */
    private VOGeoServerContrastEnhancementNormalizeGreen vendorOptionGeoServerContrastEnhancementNormalizeGreen = null;

    /** The vendor option geo server contrast enhancement normalize (blue). */
    private VOGeoServerContrastEnhancementNormalizeBlue vendorOptionGeoServerContrastEnhancementNormalizeBlue = null;

    /** The vendor option geo server contrast enhancement normalize (grey). */
    private VOGeoServerContrastEnhancementNormalizeGrey vendorOptionGeoServerContrastEnhancementNormalizeGrey = null;

    /** The vendor option geo server contrast enhancement normalize (overall). */
    private VOGeoServerContrastEnhancementNormalizeOverall vendorOptionGeoServerContrastEnhancementNormalizeOverall = null;

    /** The vendor option list. */
    private List<VendorOptionInterface> vendorOptionList = new ArrayList<VendorOptionInterface>();

    /** The vendor option versions list. */
    private List<VersionData> vendorOptionVersionsList = new ArrayList<VersionData>();

    /**
     * Instantiates a new vendor option text factory.
     *
     * @param panelId the panel id
     * @param functionManager the function manager
     * @param parentPanel the parent panel
     */
    public VendorOptionRasterFactory(Class<?> panelId, FunctionNameInterface functionManager, RasterSymbolizerDetails parentPanel)
    {
        vendorOptionGeoServerContrastEnhancementNormalizeRed = new VOGeoServerContrastEnhancementNormalizeRed(panelId, parentPanel);
        vendorOptionGeoServerContrastEnhancementNormalizeGreen = new VOGeoServerContrastEnhancementNormalizeGreen(panelId, parentPanel);
        vendorOptionGeoServerContrastEnhancementNormalizeBlue = new VOGeoServerContrastEnhancementNormalizeBlue(panelId, parentPanel);
        vendorOptionGeoServerContrastEnhancementNormalizeGrey = new VOGeoServerContrastEnhancementNormalizeGrey(panelId, parentPanel);
        vendorOptionGeoServerContrastEnhancementNormalizeOverall = new VOGeoServerContrastEnhancementNormalizeOverall(panelId, parentPanel);

        vendorOptionList.add(vendorOptionGeoServerContrastEnhancementNormalizeRed);
        vendorOptionList.add(vendorOptionGeoServerContrastEnhancementNormalizeGreen);
        vendorOptionList.add(vendorOptionGeoServerContrastEnhancementNormalizeBlue);
        vendorOptionList.add(vendorOptionGeoServerContrastEnhancementNormalizeGrey);
        vendorOptionList.add(vendorOptionGeoServerContrastEnhancementNormalizeOverall);

        VendorOptionManager.getInstance().addVendorOptionListener(this);
        VendorOptionInfoManager.getInstance().addVendorOptionInfo(this);
    }

    /**
     * Gets the vendor option list.
     *
     * @return the vendor option list
     */
    @Override
    public List<VendorOptionInterface> getVendorOptionList()
    {
        return vendorOptionList;
    }

    /**
     * Populate.
     *
     * @param rasterSymbolizer the raster symbolizer
     */
    public void populate(RasterSymbolizer rasterSymbolizer)
    {
        for(VendorOptionInterface vendorOption : vendorOptionList)
        {
            vendorOption.populate(rasterSymbolizer);
        }
    }

    /**
     * Update symbol.
     *
     * @param rasterSymbolizer the raster symbolizer
     */
    public void updateSymbol(RasterSymbolizer rasterSymbolizer)
    {
        for(VendorOptionInterface vendorOption : vendorOptionList)
        {
            boolean displayVendorOption = VendorOptionManager.getInstance().isAllowed(vendorOptionVersionsList, vendorOption.getVendorOption());

            if(displayVendorOption)
            {
                vendorOption.updateSymbol(rasterSymbolizer);
            }
        }
    }

    /* (non-Javadoc)
     * @see com.sldeditor.preferences.iface.PrefUpdateVendorOptionInterface#vendorOptionsUpdated(java.util.List)
     */
    @Override
    public void vendorOptionsUpdated(List<VersionData> vendorOptionVersionsList)
    {
        this.vendorOptionVersionsList = vendorOptionVersionsList;
    }

    /**
     * Updates the field data manager.
     *
     * @param fieldConfigManager the field config manager
     */
    public void updateFieldDataManager(GraphicPanelFieldManager fieldConfigManager)
    {
        for(VendorOptionInterface vendorOption : vendorOptionList)
        {
            if(vendorOption != null)
            {
                PopulateDetailsInterface populateInterface = (PopulateDetailsInterface)vendorOption;
                fieldConfigManager.add(populateInterface.getFieldDataManager());
            }
        }
    }

    /* (non-Javadoc)
     * @see com.sldeditor.ui.detail.vendor.VendorOptionFactoryInterface#getVendorOptionList(java.lang.String)
     */
    @Override
    public List<VendorOptionInterface> getVendorOptionList(String className) {
        List<VendorOptionInterface> matchingList = new ArrayList<VendorOptionInterface>();

        for(VendorOptionInterface vendorOption : vendorOptionList)
        {
            if(vendorOption.getClass().getName().compareTo(className) == 0)
            {
                matchingList.add(vendorOption);
            }
        }
        return matchingList;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.ui.detail.vendor.VendorOptionFactoryInterface#getVendorOptionInfoList()
     */
    @Override
    public List<VendorOptionInfo> getVendorOptionInfoList() {
        List<VendorOptionInfo> vendorOptionInfoList = new ArrayList<VendorOptionInfo>();

        for(VendorOptionInterface vendorOption : vendorOptionList)
        {
            VendorOptionInfo vendorOptionInfo = vendorOption.getVendorOptionInfo();
            if(vendorOptionInfo != null)
            {
                vendorOptionInfoList.add(vendorOptionInfo);
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
    public void getMinimumVersion(Object parentObj, Object sldObj,
            List<VendorOptionPresent> vendorOptionsPresentList) {
        for(VendorOptionInterface vo : vendorOptionList)
        {
            vo.getMinimumVersion(parentObj, sldObj, vendorOptionsPresentList);
        }
    }
}
