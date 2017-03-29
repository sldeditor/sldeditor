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

package com.sldeditor.test.unit.common.vendoroption.info;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.sldeditor.common.vendoroption.GeoServerVendorOption;
import com.sldeditor.common.vendoroption.VersionData;
import com.sldeditor.common.vendoroption.info.VendorOptionInfo;
import com.sldeditor.common.vendoroption.info.VendorOptionInfoManager;
import com.sldeditor.common.vendoroption.info.VendorOptionInfoPanel;
import com.sldeditor.ui.detail.vendor.VendorOptionFactoryInterface;
import com.sldeditor.ui.detail.vendor.geoserver.VendorOptionInterface;

/**
 * Unit test for VendorOptionInfoModel.
 * 
 * <p>{@link com.sldeditor.common.vendoroption.info.VendorOptionInfoManager}
 * 
 * @author Robert Ward (SCISYS)
 *
 */
public class VendorOptionInfoManagerTest {

    /**
     * Test method for
     * {@link com.sldeditor.common.vendoroption.info.VendorOptionInfoManager#addVendorOptionInfo(com.sldeditor.ui.detail.vendor.VendorOptionFactoryInterface)}.
     * {@link com.sldeditor.common.vendoroption.info.VendorOptionInfoManager#getPanel(java.lang.Class)}.
     * {@link com.sldeditor.common.vendoroption.info.VendorOptionInfoManager#setSelectedVersion(java.lang.Class, com.sldeditor.common.vendoroption.VersionData)}.
     */
    @Test
    public void testAddVendorOptionInfo() {
        VendorOptionInfoManager.getInstance().addVendorOptionInfo(null);
        VendorOptionFactoryInterface f1 = new VendorOptionFactoryInterface() {

            @Override
            public List<VendorOptionInterface> getVendorOptionList() {
                return null;
            }

            @Override
            public List<VendorOptionInterface> getVendorOptionList(String className) {
                return null;
            }

            @Override
            public List<VendorOptionInfo> getVendorOptionInfoList() {
                List<VendorOptionInfo> list = new ArrayList<VendorOptionInfo>();
                return list;
            }
        };
        VendorOptionInfoManager.getInstance().addVendorOptionInfo(f1);
        VendorOptionInfoManager.getInstance().addVendorOptionInfo(f1);

        VendorOptionInfoPanel panel1 = VendorOptionInfoManager.getInstance().getPanel(String.class);
        VendorOptionInfoPanel panel2 = VendorOptionInfoManager.getInstance().getPanel(Double.class);
        VendorOptionInfoPanel panel3 = VendorOptionInfoManager.getInstance().getPanel(String.class);

        assertTrue(panel1 == panel3);
        assertTrue(panel1 != panel2);
        
        VendorOptionInfoManager.getInstance().setSelectedVersion(null, null);
        
        VersionData versionData = VersionData.decode(GeoServerVendorOption.class, "3.1.4");
        VendorOptionInfoManager.getInstance().setSelectedVersion(String.class, versionData);
    }

}
