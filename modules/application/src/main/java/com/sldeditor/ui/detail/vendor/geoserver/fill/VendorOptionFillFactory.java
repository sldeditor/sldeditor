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
package com.sldeditor.ui.detail.vendor.geoserver.fill;

import java.util.ArrayList;
import java.util.List;

import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.PolygonSymbolizer;

import com.sldeditor.common.preferences.PrefManager;
import com.sldeditor.common.preferences.iface.PrefUpdateVendorOptionInterface;
import com.sldeditor.common.vendoroption.VendorOptionManager;
import com.sldeditor.common.vendoroption.VersionData;
import com.sldeditor.filter.v2.function.FunctionNameInterface;
import com.sldeditor.ui.detail.GraphicPanelFieldManager;
import com.sldeditor.ui.detail.vendor.VendorOptionFactoryInterface;
import com.sldeditor.ui.detail.vendor.geoserver.VendorOptionInterface;
import com.sldeditor.ui.iface.PopulateDetailsInterface;

/**
 * A factory for creating VendorOptionFillFactory objects.
 * 
 * @author Robert Ward (SCISYS)
 */
public class VendorOptionFillFactory implements VendorOptionFactoryInterface, PrefUpdateVendorOptionInterface {

    /** The vendor option GeoServer random fill. */
    private VOGeoServerRandomFill vendorOptionGeoServerRandomFill = null;

    /** The list of all the extensions. */
    private List<VendorOptionInterface> vendorOptionList = new ArrayList<VendorOptionInterface>();

    /** The vendor option list allowed to be used. */
    private List<VersionData> vendorOptionVersionList = new ArrayList<VersionData>();

    /**
     * Instantiates a new vendor option label option factory.
     *
     * @param panelId the panel id
     * @param functionManager the function manager
     */
    public VendorOptionFillFactory(Class<?> panelId, FunctionNameInterface functionManager)
    {
        vendorOptionGeoServerRandomFill = new VOGeoServerRandomFill(panelId, functionManager);

        vendorOptionList.add(vendorOptionGeoServerRandomFill);

        PrefManager.getInstance().addVendorOptionListener(this);
    }

    /**
     * Return the list of labelling vendor options.
     *
     * @return the vendor options
     */
    @Override
    public List<VendorOptionInterface> getVendorOptionList()
    {
        return vendorOptionList;
    }

    /**
     * Sets the value.
     *
     * @param polygonSymbolizer the polygon symbolizer
     */
    public void populate(PolygonSymbolizer polygonSymbolizer)
    {
        for(VendorOptionInterface extension : vendorOptionList)
        {
            extension.populate(polygonSymbolizer);
        }
    }

    /**
     * Update symbol but only if allowed to by the vendor options configuration.
     *
     * @param polygonSymbolizer the polygon symbolizer
     */
    public void updateSymbol(PolygonSymbolizer polygonSymbolizer)
    {
        for(VendorOptionInterface extension : vendorOptionList)
        {
            boolean displayVendorOption = VendorOptionManager.getInstance().isAllowed(vendorOptionVersionList, extension.getVendorOption());

            if(displayVendorOption)
            {
                extension.updateSymbol(polygonSymbolizer);
            }
        }
    }

    /* (non-Javadoc)
     * @see com.sldeditor.preferences.iface.PrefUpdateVendorOptionInterface#vendorOptionsUpdated(java.util.List)
     */
    @Override
    public void vendorOptionsUpdated(List<VersionData> vendorOptionVersionsList)
    {
        this.vendorOptionVersionList = vendorOptionVersionsList;
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

    /**
     * Populate point fill vendor options.
     *
     * @param point the point
     */
    public void populate(PointSymbolizer point) {
        // No vendor options
    }
}
