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

import static org.junit.Assert.assertEquals;

import com.sldeditor.common.vendoroption.GeoServerVendorOption;
import com.sldeditor.common.vendoroption.VendorOptionManager;
import com.sldeditor.common.vendoroption.VendorOptionStatus;
import com.sldeditor.common.vendoroption.VendorOptionVersion;
import com.sldeditor.common.vendoroption.VersionData;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

/**
 * Unit test for VendorOptionStatus.
 *
 * <p>{@link com.sldeditor.common.vendoroption.VendorOptionStatus}
 *
 * @author Robert Ward (SCISYS)
 */
public class VendorOptionStatusTest {

    /**
     * Test method for {@link
     * com.sldeditor.common.vendoroption.VendorOptionStatus#getVersionString(java.util.List)}.
     */
    @Test
    public void getVersionString() {
        // The expected string returned is the last vendor option in the list
        List<VersionData> expectedList = null;
        assertEquals("", VendorOptionStatus.getVersionString(expectedList));

        expectedList = new ArrayList<VersionData>();
        assertEquals("", VendorOptionStatus.getVersionString(expectedList));

        expectedList.add(null);
        assertEquals("", VendorOptionStatus.getVersionString(expectedList));

        // Try no vendor option
        expectedList.add(VendorOptionManager.getInstance().getDefaultVendorOptionVersionData());
        assertEquals("Strict SLD", VendorOptionStatus.getVersionString(expectedList));

        // Try a valid GeoServer option
        expectedList.add(VersionData.decode(GeoServerVendorOption.class, "1.2.4"));
        assertEquals("GeoServer 1.2.4", VendorOptionStatus.getVersionString(expectedList));

        // Try an invalid class
        expectedList.add(VersionData.decode(String.class, "1.2.4"));
        assertEquals("", VendorOptionStatus.getVersionString(expectedList));
    }

    /**
     * Test method for {@link
     * com.sldeditor.common.vendoroption.VendorOptionStatus#getVendorOptionVersionString(VersionData)}.
     */
    @Test
    public void testGetVendorOptionVersionString() {
        VersionData versionDataMin = VersionData.decode(getClass(), "2.4.1");
        VersionData versionDataMax = VersionData.decode(getClass(), "2.8.3");

        VendorOptionVersion versionData =
                new VendorOptionVersion(
                        GeoServerVendorOption.class, versionDataMin, versionDataMax);
        assertEquals("", VendorOptionStatus.getVendorOptionVersionString(null));
        assertEquals(
                "GeoServer 2.4.1-2.8.3",
                VendorOptionStatus.getVendorOptionVersionString(versionData));

        versionData =
                new VendorOptionVersion(
                        GeoServerVendorOption.class,
                        versionDataMin,
                        VersionData.decode(getClass(), "Latest"));
        assertEquals(
                "GeoServer 2.4.1-", VendorOptionStatus.getVendorOptionVersionString(versionData));

        versionData =
                new VendorOptionVersion(
                        GeoServerVendorOption.class,
                        VersionData.decode(getClass(), "Earliest"),
                        versionDataMax);
        assertEquals(
                "GeoServer -2.8.3", VendorOptionStatus.getVendorOptionVersionString(versionData));
    }
}
