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
package com.sldeditor.ui.detail.vendor.geoserver.text;

import java.util.ArrayList;
import java.util.List;

import org.geotools.styling.TextSymbolizer;

import com.sldeditor.common.vendoroption.VendorOptionManager;
import com.sldeditor.common.vendoroption.VendorOptionUpdateInterface;
import com.sldeditor.common.vendoroption.VersionData;
import com.sldeditor.common.vendoroption.info.VendorOptionInfo;
import com.sldeditor.common.vendoroption.info.VendorOptionInfoManager;
import com.sldeditor.common.vendoroption.minversion.VendorOptionPresent;
import com.sldeditor.ui.detail.GraphicPanelFieldManager;
import com.sldeditor.ui.detail.vendor.VendorOptionFactoryInterface;
import com.sldeditor.ui.detail.vendor.geoserver.VendorOptionInterface;
import com.sldeditor.ui.iface.PopulateDetailsInterface;

/**
 * A factory for creating VendorOptionText objects.
 * 
 * @author Robert Ward (SCISYS)
 */
public class VendorOptionTextFactory implements VendorOptionFactoryInterface, VendorOptionUpdateInterface {

    /** The vendor option geo server labelling. */
    private VOGeoServerLabelling vendorOptionGeoServerLabelling = null;

    /** The vendor option geo server labelling underline. */
    private VOGeoServerLabellingUnderline vendorOptionGeoServerLabellingUnderline = null;

    /** The vendor option geo server text symbolizer 2 */
    private VOGeoServerTextSymbolizer2 vendorOptionGeoServerTextSymbolizer2 = null;

    /** The vendor option list. */
    private List<VendorOptionInterface> vendorOptionList = new ArrayList<VendorOptionInterface>();

    /** The vendor option versions list. */
    private List<VersionData> vendorOptionVersionsList = new ArrayList<VersionData>();

    /**
     * Instantiates a new vendor option text factory.
     *
     * @param panelId the panel id
     */
    public VendorOptionTextFactory(Class<?> panelId)
    {
        vendorOptionGeoServerLabelling = new VOGeoServerLabelling(panelId);
        vendorOptionList.add(vendorOptionGeoServerLabelling);

        vendorOptionGeoServerLabellingUnderline = new VOGeoServerLabellingUnderline(panelId);
        vendorOptionList.add(vendorOptionGeoServerLabellingUnderline);

        vendorOptionGeoServerTextSymbolizer2 = new VOGeoServerTextSymbolizer2(panelId);
        vendorOptionList.add(vendorOptionGeoServerTextSymbolizer2);

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
     * @param textSymbolizer the text symbolizer
     */
    public void populate(TextSymbolizer textSymbolizer)
    {
        for(VendorOptionInterface vendorOption : vendorOptionList)
        {
            vendorOption.populate(textSymbolizer);
        }
    }

    /**
     * Update symbol.
     *
     * @param textSymbolizer the text symbolizer
     */
    public void updateSymbol(TextSymbolizer textSymbolizer)
    {
        for(VendorOptionInterface vendorOption : vendorOptionList)
        {
            boolean displayVendorOption = VendorOptionManager.getInstance().isAllowed(vendorOptionVersionsList, vendorOption.getVendorOption());

            if(displayVendorOption)
            {
                vendorOption.updateSymbol(textSymbolizer);
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
     * Gets the field data manager.
     *
     * @param fieldConfigManager the field config manager
     */
    public void getFieldDataManager(GraphicPanelFieldManager fieldConfigManager)
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

        for(VendorOptionInterface vo : vendorOptionList)
        {
            VendorOptionInfo vendorOptionInfo = vo.getVendorOptionInfo();
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
