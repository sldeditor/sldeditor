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

package com.sldeditor.test.unit.common.vendoroption;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.sldeditor.common.vendoroption.GeoServerVendorOption;
import com.sldeditor.common.vendoroption.VersionData;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * Unit test for GeoServerVendorOption.
 *
 * <p>{@link com.sldeditor.common.vendoroption.GeoServerVendorOption}
 *
 * @author Robert Ward (SCISYS)
 */
public class GeoServerVendorOptionTest {

    /**
     * Test method for {@link com.sldeditor.common.vendoroption.GeoServerVendorOption#getName()}.
     */
    @Test
    public void testGetName() {
        GeoServerVendorOption vendorOption = new GeoServerVendorOption();

        assertEquals("GeoServer", vendorOption.getName());
    }

    /**
     * Test method for {@link
     * com.sldeditor.common.vendoroption.GeoServerVendorOption#getVersionStringList()}. Test method
     * for {@link
     * com.sldeditor.common.vendoroption.GeoServerVendorOption#addVersion(com.sldeditor.common.vendoroption.VersionData)}.
     * Test method for {@link
     * com.sldeditor.common.vendoroption.GeoServerVendorOption#getVersion(java.lang.String)}. Test
     * method for {@link com.sldeditor.common.vendoroption.GeoServerVendorOption#getVersionList()}.
     */
    @Test
    public void testGetVersionStringList() {
        GeoServerVendorOption vendorOption = new GeoServerVendorOption();

        assertEquals(0, vendorOption.getVersionStringList().size());
        assertEquals(0, vendorOption.getVersionList().size());

        // This should be ignored
        vendorOption.addVersion(null);
        assertEquals(0, vendorOption.getVersionStringList().size());
        assertEquals(0, vendorOption.getVersionList().size());

        VersionData versionData1 = new VersionData();
        vendorOption.addVersion(versionData1);
        assertEquals(1, vendorOption.getVersionStringList().size());
        assertEquals(1, vendorOption.getVersionList().size());

        VersionData versionData2 = new VersionData();
        vendorOption.addVersion(versionData2);
        assertEquals(2, vendorOption.getVersionStringList().size());
        assertEquals(2, vendorOption.getVersionList().size());

        List<VersionData> voList = vendorOption.getVersionList();
        assertEquals(versionData1, voList.get(0));
        assertEquals(versionData2, voList.get(1));

        List<String> voStringList = vendorOption.getVersionStringList();
        assertEquals(versionData1.getVersionString(), voStringList.get(0));
        assertEquals(versionData2.getVersionString(), voStringList.get(1));
    }
}
