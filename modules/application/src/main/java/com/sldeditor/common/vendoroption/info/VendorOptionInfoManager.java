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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sldeditor.common.vendoroption.VendorOptionManager;
import com.sldeditor.common.vendoroption.VersionData;
import com.sldeditor.ui.detail.vendor.VendorOptionFactoryInterface;

/**
 * The Class VendorOptionInfoManager.
 *
 * @author Robert Ward (SCISYS)
 */
public class VendorOptionInfoManager {

    /** The singleton instance. */
    private static VendorOptionInfoManager instance = null;

    /**
     * The Class InstanceData.
     */
    class InstanceData {
        /** The info model. */
        private VendorOptionInfoModel infoModel = null;

        /** The panel. */
        private VendorOptionInfoPanel panel = null;
    }

    /** The instance map. */
    private Map<Class<?>, InstanceData> instanceMap = new HashMap<Class<?>, InstanceData>();

    /** The list of classes received data from. */
    private List<Class<?>> receivedFromList = new ArrayList<Class<?>>();

    /** The vendor option factory list. */
    private List<VendorOptionFactoryInterface> voFactoryList =
            new ArrayList<VendorOptionFactoryInterface>();

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
        if (obj != null) {
            Class<?> classReceivedFrom = obj.getClass();
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
    public VendorOptionInfoPanel getPanel(Class<?> instance) {

        InstanceData data = instanceMap.get(instance);

        if (data == null) {
            data = new InstanceData();

            List<VendorOptionInfo> infoList = new ArrayList<VendorOptionInfo>();

            for (VendorOptionFactoryInterface vo : voFactoryList) {
                List<VendorOptionInfo> vendorOptionInfoList = vo.getVendorOptionInfoList();
                if (vendorOptionInfoList != null) {
                    infoList.addAll(vendorOptionInfoList);
                }
            }

            data.infoModel = new VendorOptionInfoModel();
            data.infoModel.addVendorOptionInfo(infoList);
            data.panel = new VendorOptionInfoPanel(data.infoModel);
            instanceMap.put(instance, data);
        }
        return data.panel;
    }

    /**
     * Sets the selected version.
     *
     * @param versionData the new selected version
     */
    public void setSelectedVersion(Class<?> instance, VersionData versionData) {
        InstanceData data = instanceMap.get(instance);

        if (data != null) {
            if (data.infoModel != null) {
                data.infoModel.setSelectedVersion(versionData);
            }

            List<VersionData> vendorOptionList = new ArrayList<VersionData>();
            vendorOptionList.add(versionData);

            VendorOptionManager.getInstance().setSelectedVendorOptions(vendorOptionList);
        }
    }
}
