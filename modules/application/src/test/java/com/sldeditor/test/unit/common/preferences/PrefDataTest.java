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
package com.sldeditor.test.unit.common.preferences;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.sldeditor.common.preferences.PrefData;
import com.sldeditor.common.vendoroption.VendorOptionManager;
import com.sldeditor.common.vendoroption.VersionData;

/**
 * Unit test for PrefData.
 * <p>{@link com.sldeditor.common.preferences.PrefData}
 * 
 * @author Robert Ward (SCISYS)
 */
public class PrefDataTest {

    /**
     * Test method for {@link com.sldeditor.common.preferences.PrefData#clone()}.
     */
    @Test
    public void testClone() {
        PrefData prefData = new PrefData();
        prefData.setUseAntiAlias(true);
        
        List<VersionData> vendorOptionList = new ArrayList<VersionData>();
        vendorOptionList.add(VendorOptionManager.getInstance().getDefaultVendorOptionVersionData());

        prefData.setVendorOptionVersionList(vendorOptionList);
        String uiLayoutClass = "perfect curve";

        prefData.setUiLayoutClass(uiLayoutClass);
        
        PrefData newObj = prefData.clone();
        
        assertEquals(newObj.getBackgroundColour(), prefData.getBackgroundColour());
        assertEquals(newObj.getUiLayoutClass(), prefData.getUiLayoutClass());
        assertEquals(newObj.getVendorOptionVersionList(), prefData.getVendorOptionVersionList());
        assertEquals(newObj.isUseAntiAlias(), prefData.isUseAntiAlias());
    }

    /**
     * Test method for {@link com.sldeditor.common.preferences.PrefData#isUseAntiAlias()}.
     */
    @Test
    public void testUseAntiAlias() {
        PrefData prefData = new PrefData();

        prefData.setUseAntiAlias(true);
        assertTrue(prefData.isUseAntiAlias());

        prefData.setUseAntiAlias(false);
        assertFalse(prefData.isUseAntiAlias());
    }

    /**
     * Test method for {@link com.sldeditor.common.preferences.PrefData#getVendorOptionVersionList()}.
     * Test method for {@link com.sldeditor.common.preferences.PrefData#setVendorOptionVersionList()}.
     */
    @Test
    public void testVendorOptionVersionList() {
        PrefData prefData = new PrefData();

        List<VersionData> vendorOptionList = new ArrayList<VersionData>();
        vendorOptionList.add(VendorOptionManager.getInstance().getDefaultVendorOptionVersionData());

        prefData.setVendorOptionVersionList(vendorOptionList);
        assertEquals(vendorOptionList, prefData.getVendorOptionVersionList());
    }

    /**
     * Test method for {@link com.sldeditor.common.preferences.PrefData#getUiLayoutClass()}.
     * Test method for {@link com.sldeditor.common.preferences.PrefData#setUiLayoutClass(java.lang.String)}.
     */
    @Test
    public void testUiLayoutClass() {
        PrefData prefData = new PrefData();

        String uiLayoutClass = "perfect curve";

        prefData.setUiLayoutClass(uiLayoutClass);
        assertEquals(uiLayoutClass, prefData.getUiLayoutClass());
    }

    /**
     * Test method for {@link com.sldeditor.common.preferences.PrefData#getBackgroundColour()}.
     * Test method for {@link com.sldeditor.common.preferences.PrefData#setBackgroundColour(java.awt.Color)}.
     */
    @Test
    public void testBackgroundColour() {
        PrefData prefData = new PrefData();
        Color testColour = Color.blue;
        prefData.setBackgroundColour(testColour);
        assertEquals(testColour, prefData.getBackgroundColour());
    }
}
