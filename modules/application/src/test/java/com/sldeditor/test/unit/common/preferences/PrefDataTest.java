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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sldeditor.common.preferences.PrefData;
import com.sldeditor.common.preferences.PrefDataLastViewedEnum;
import com.sldeditor.common.vendoroption.VendorOptionManager;
import com.sldeditor.common.vendoroption.VersionData;
import java.awt.Color;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * Unit test for PrefData.
 *
 * <p>{@link com.sldeditor.common.preferences.PrefData}
 *
 * @author Robert Ward (SCISYS)
 */
public class PrefDataTest {

    /** Test method for {@link com.sldeditor.common.preferences.PrefData#clone()}. */
    @Test
    public void testClone() {
        PrefData prefData = new PrefData();
        prefData.setUseAntiAlias(true);

        List<VersionData> vendorOptionList = new ArrayList<VersionData>();
        vendorOptionList.add(VendorOptionManager.getInstance().getDefaultVendorOptionVersionData());

        prefData.setVendorOptionVersionList(vendorOptionList);
        String uiLayoutClass = "perfect curve";

        prefData.setUiLayoutClass(uiLayoutClass);
        prefData.setLastFolderViewed("last folder");

        PrefData newObj = new PrefData(prefData);

        assertEquals(newObj.getBackgroundColour(), prefData.getBackgroundColour());
        assertEquals(newObj.getUiLayoutClass(), prefData.getUiLayoutClass());
        assertEquals(newObj.getVendorOptionVersionList(), prefData.getVendorOptionVersionList());
        assertEquals(newObj.isUseAntiAlias(), prefData.isUseAntiAlias());
        assertEquals(newObj.isSaveLastFolderView(), prefData.isSaveLastFolderView());
        assertEquals(newObj.getLastViewedKey(), prefData.getLastViewedKey());
        assertEquals(newObj.getLastFolderViewed(), prefData.getLastFolderViewed());
        assertEquals(newObj.isCheckAppVersionOnStartUp(), prefData.isCheckAppVersionOnStartUp());
        assertEquals(newObj.getFileEncoding(), prefData.getFileEncoding());
    }

    /** Test method for {@link com.sldeditor.common.preferences.PrefData#isUseAntiAlias()}. */
    @Test
    public void testUseAntiAlias() {
        PrefData prefData = new PrefData();

        prefData.setUseAntiAlias(true);
        assertTrue(prefData.isUseAntiAlias());

        prefData.setUseAntiAlias(false);
        assertFalse(prefData.isUseAntiAlias());
    }

    /**
     * Test method for {@link
     * com.sldeditor.common.preferences.PrefData#setCheckAppVersionOnStartUp(boolean)}. Test method
     * for {@link com.sldeditor.common.preferences.PrefData#isCheckAppVersionOnStartUp()}.
     */
    @Test
    public void testCheckAppVersionOnStartUp() {
        PrefData prefData = new PrefData();

        prefData.setCheckAppVersionOnStartUp(false);
        assertFalse(prefData.isCheckAppVersionOnStartUp());

        prefData.setCheckAppVersionOnStartUp(true);
        assertTrue(prefData.isCheckAppVersionOnStartUp());
    }

    /**
     * Test method for {@link
     * com.sldeditor.common.preferences.PrefData#getVendorOptionVersionList()}. Test method for
     * {@link com.sldeditor.common.preferences.PrefData#setVendorOptionVersionList()}.
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
     * Test method for {@link com.sldeditor.common.preferences.PrefData#getUiLayoutClass()}. Test
     * method for {@link
     * com.sldeditor.common.preferences.PrefData#setUiLayoutClass(java.lang.String)}.
     */
    @Test
    public void testUiLayoutClass() {
        PrefData prefData = new PrefData();

        String uiLayoutClass = "perfect curve";

        prefData.setUiLayoutClass(uiLayoutClass);
        assertEquals(uiLayoutClass, prefData.getUiLayoutClass());
    }

    /**
     * Test method for {@link com.sldeditor.common.preferences.PrefData#getBackgroundColour()}. Test
     * method for {@link
     * com.sldeditor.common.preferences.PrefData#setBackgroundColour(java.awt.Color)}.
     */
    @Test
    public void testBackgroundColour() {
        PrefData prefData = new PrefData();
        Color testColour = Color.blue;
        prefData.setBackgroundColour(testColour);
        assertEquals(testColour, prefData.getBackgroundColour());
    }

    /**
     * Test method for {@link com.sldeditor.common.preferences.PrefData#getSaveLastFolderView()}.
     * Test method for {@link
     * com.sldeditor.common.preferences.PrefData#setSaveLastFolderView(boolean)}.
     */
    @Test
    public void testSaveLastFolderView() {
        PrefData prefData = new PrefData();
        boolean expectedValue = true;
        prefData.setSaveLastFolderView(expectedValue);
        assertEquals(expectedValue, prefData.isSaveLastFolderView());

        expectedValue = false;
        prefData.setSaveLastFolderView(expectedValue);
        assertEquals(expectedValue, prefData.isSaveLastFolderView());
    }

    /**
     * Test method for {@link com.sldeditor.common.preferences.PrefData#getLastViewedKey()}. Test
     * method for {@link
     * com.sldeditor.common.preferences.PrefData#setLastViewedKey(PrefDataLastViewedEnum)}.
     */
    @Test
    public void testLastViewedKey() {
        PrefData prefData = new PrefData();
        PrefDataLastViewedEnum expectedValue = PrefDataLastViewedEnum.FOLDER;
        prefData.setLastViewedKey(expectedValue);
        assertEquals(expectedValue, prefData.getLastViewedKey());

        expectedValue = PrefDataLastViewedEnum.GEOSERVER;
        prefData.setLastViewedKey(expectedValue);
        assertEquals(expectedValue, prefData.getLastViewedKey());
    }

    /**
     * Test method for {@link com.sldeditor.common.preferences.PrefData#geLastFolderViewed()}. Test
     * method for {@link com.sldeditor.common.preferences.PrefData#setLastFolderViewed(String)}.
     */
    @Test
    public void testLastFolderViewed() {
        PrefData prefData = new PrefData();
        String expectedValue = "last viewed folder";
        prefData.setLastFolderViewed(expectedValue);
        assertEquals(expectedValue, prefData.getLastFolderViewed());
    }

    /**
     * Test method for {@link com.sldeditor.common.preferences.PrefData#charsetName(string)}. Test
     * method for {@link com.sldeditor.common.preferences.PrefData#getFileEncoding()}.
     */
    @Test
    public void testFileEncoding() {
        PrefData prefData = new PrefData();

        String charsetName = "windows-1253";
        prefData.setFileEncoding(Charset.forName(charsetName));
        assertEquals(charsetName, prefData.getFileEncoding().name());
    }
}
