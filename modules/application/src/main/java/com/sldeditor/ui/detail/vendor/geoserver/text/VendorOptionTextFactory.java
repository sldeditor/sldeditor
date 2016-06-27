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

import com.sldeditor.common.preferences.PrefManager;
import com.sldeditor.common.preferences.iface.PrefUpdateVendorOptionInterface;
import com.sldeditor.common.vendoroption.VendorOptionManager;
import com.sldeditor.common.vendoroption.VersionData;
import com.sldeditor.filter.v2.function.FunctionNameInterface;
import com.sldeditor.ui.detail.GraphicPanelFieldManager;
import com.sldeditor.ui.detail.vendor.geoserver.VendorOptionInterface;
import com.sldeditor.ui.iface.PopulateDetailsInterface;

/**
 * A factory for creating VendorOptionText objects.
 * 
 * @author Robert Ward (SCISYS)
 */
public class VendorOptionTextFactory implements PrefUpdateVendorOptionInterface {

    /** The vendor option geo server labelling. */
    private VOGeoServerLabelling vendorOptionGeoServerLabelling = null;

    /** The vendor option list. */
    private List<VendorOptionInterface> vendorOptionList = new ArrayList<VendorOptionInterface>();

    /** The vendor option versions list. */
    private List<VersionData> vendorOptionVersionsList = new ArrayList<VersionData>();

    /**
     * Instantiates a new vendor option text factory.
     *
     * @param panelId the panel id
     * @param functionManager the function manager
     */
    public VendorOptionTextFactory(Class<?> panelId, FunctionNameInterface functionManager)
    {
        vendorOptionGeoServerLabelling = new VOGeoServerLabelling(panelId, functionManager);
        
        vendorOptionList.add(vendorOptionGeoServerLabelling);

        PrefManager.getInstance().addVendorOptionListener(this);
    }

    /**
     * Gets the vendor option list.
     *
     * @return the vendor option list
     */
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
    public void vendorOptionsUpdated(List<VersionData> vendorOptionList)
    {
        this.vendorOptionVersionsList = vendorOptionList;
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
}
