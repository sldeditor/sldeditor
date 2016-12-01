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

package com.sldeditor.common.vendoroption.info;

import java.util.ArrayList;
import java.util.List;

import com.sldeditor.ui.detail.vendor.VendorOptionFactoryInterface;

/**
 * The Class VendorOptionInfoManager.
 *
 * @author Robert Ward (SCISYS)
 */
public class VendorOptionInfoManager {

    /** The singleton instance. */
    private static VendorOptionInfoManager instance = null;

    /** The info model. */
    private VendorOptionInfoModel infoModel = null;

    /** The panel. */
    private VendorOptionPanel panel = null;

    /** The list of classes received data from. */
    private List<Class<?>> receivedFromList = new ArrayList<Class<?>>();

    /** The vendor option factory list. */
    private List<VendorOptionFactoryInterface> voFactoryList = new ArrayList<VendorOptionFactoryInterface>();

    /**
     * Instantiates a new vendor option info manager.
     */
    private VendorOptionInfoManager() {
    }

    /**
     * Gets the single instance of VendorOptionInfoManager.
     *
     * @return single instance of VendorOptionInfoManager
     */
    public static VendorOptionInfoManager getInstance() {
        if (instance == null) {
            instance = new VendorOptionInfoManager();
        }

        return instance;
    }

    /**
     * Adds the vendor option info.
     *
     * @param obj the obj
     */
    public void addVendorOptionInfo(VendorOptionFactoryInterface obj) {
        Class<?> classReceivedFrom = obj.getClass();
        if (obj != null) {
            // Make sure data is only added once for each class sending data
            if (!receivedFromList.contains(classReceivedFrom)) {
                receivedFromList.add(classReceivedFrom);
                voFactoryList.add(obj);
            }
        }
    }

    /**
     * Gets the panel.
     *
     * @return the panel
     */
    public VendorOptionPanel getPanel() {
        if (panel == null) {

            List<VendorOptionInfo> infoList = new ArrayList<VendorOptionInfo>();

            for(VendorOptionFactoryInterface vo : voFactoryList)
            {
                List<VendorOptionInfo> vendorOptionInfoList = vo.getVendorOptionInfoList();

                infoList.addAll(vendorOptionInfoList);
            }

            infoModel = new VendorOptionInfoModel();
            infoModel.addVendorOptionInfo(infoList);
            panel = new VendorOptionPanel(infoModel);
        }
        return panel;
    }
}
