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

package com.sldeditor.ui.detail.config;

import java.util.List;

import com.sldeditor.common.preferences.PrefManager;
import com.sldeditor.common.preferences.iface.PrefUpdateVendorOptionInterface;
import com.sldeditor.common.vendoroption.VendorOptionManager;
import com.sldeditor.common.vendoroption.VersionData;
import com.sldeditor.ui.detail.BasePanel;
import com.sldeditor.ui.detail.vendor.VendorOptionFactoryInterface;
import com.sldeditor.ui.detail.vendor.geoserver.VendorOptionInterface;

/**
 * The Class FieldConfigVendorOption.
 *
 * @author Robert Ward (SCISYS)
 */
public class FieldConfigVendorOption extends FieldConfigEnum implements PrefUpdateVendorOptionInterface {

    /** The vendor option factory. */
    private VendorOptionFactoryInterface vendorOptionFactory = null;

    /** The vendor option class name. */
    private String vendorOptionClassName;

    /**
     * Instantiates a new field config map units.
     *
     * @param vendorOptionFactory the vendor option factory
     * @param vendorOptionClassName the vendor option class name
     */
    public FieldConfigVendorOption(VendorOptionFactoryInterface vendorOptionFactory, String vendorOptionClassName) {
        super(null);

        this.vendorOptionFactory = vendorOptionFactory;
        this.vendorOptionClassName = vendorOptionClassName;

        PrefManager.getInstance().addVendorOptionListener(this);
    }

    /* (non-Javadoc)
     * @see com.sldeditor.common.preferences.iface.PrefUpdateVendorOptionInterface#vendorOptionsUpdated(java.util.List)
     */
    @Override
    public void vendorOptionsUpdated(List<VersionData> vendorOptionList) {

        updateVendorOptionPanels(vendorOptionList);
    }

    /**
     * Update vendor option panels.
     *
     * @param vendorOptionList the vendor option list
     */
    private void updateVendorOptionPanels(List<VersionData> vendorOptionList)
    {
        if(vendorOptionFactory != null)
        {
            List<VendorOptionInterface> veList = vendorOptionFactory.getVendorOptionList(vendorOptionClassName);
            if(veList != null)
            {
                for(VendorOptionInterface vendorOption : veList)
                {
                    boolean displayVendorOption = VendorOptionManager.getInstance().isAllowed(vendorOptionList, vendorOption.getVendorOption());

                    BasePanel extensionPanel = vendorOption.getPanel();
                    if(extensionPanel != null)
                    {
                        extensionPanel.removePanel(vendorOption.getPanel());

                        if(displayVendorOption)
                        {
                            extensionPanel.appendPanelNoPadding(vendorOption.getPanel());
                        }
                    }
                }
            }
        }
    }

}
