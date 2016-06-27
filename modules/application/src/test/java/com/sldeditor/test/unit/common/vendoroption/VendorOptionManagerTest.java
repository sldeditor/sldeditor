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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.sldeditor.common.vendoroption.GeoServerVendorOption;
import com.sldeditor.common.vendoroption.NoVendorOption;
import com.sldeditor.common.vendoroption.VendorOptionManager;
import com.sldeditor.common.vendoroption.VendorOptionTypeInterface;
import com.sldeditor.common.vendoroption.VendorOptionVersion;
import com.sldeditor.common.vendoroption.VersionData;

/**
 * Unit test for VendorOptionManager.
 * <p>{@link com.sldeditor.common.vendoroption.VendorOptionManager}
 * 
 * @author Robert Ward (SCISYS)
 *
 */
public class VendorOptionManagerTest {

    /**
     * Test method for {@link com.sldeditor.common.vendoroption.VendorOptionManager#getClass(java.lang.Class)}.
     */
    @Test
    public void testGetClassClassOfQ() {
        assertNull(VendorOptionManager.getInstance().getClass(String.class));
        assertTrue(VendorOptionManager.getInstance().getClass(NoVendorOption.class) != null);
        assertTrue(VendorOptionManager.getInstance().getClass(GeoServerVendorOption.class) != null);
    }

    /**
     * Test method for {@link com.sldeditor.common.vendoroption.VendorOptionManager#getVendorOptionVersion(java.lang.Class, java.lang.String, java.lang.String)}.
     */
    @Test
    public void testGetVendorOptionVersionClassOfQStringString() {
        assertNull(VendorOptionManager.getInstance().getVendorOptionVersion(null, null, null));

        String exepectedEndVersion = "2.8.1";
        VendorOptionVersion voGS = VendorOptionManager.getInstance().getVendorOptionVersion(GeoServerVendorOption.class, "2.4.1", exepectedEndVersion);
        assertEquals(exepectedEndVersion, voGS.getLatest().getVersionString());

        VendorOptionVersion voGS1 = VendorOptionManager.getInstance().getVendorOptionVersion(GeoServerVendorOption.class, null, exepectedEndVersion);
        assertEquals(exepectedEndVersion, voGS1.getLatest().getVersionString());

        exepectedEndVersion = "2.8.a";
        VendorOptionVersion voGS2 = VendorOptionManager.getInstance().getVendorOptionVersion(GeoServerVendorOption.class, "2.4.1", exepectedEndVersion);
        assertEquals("Latest", voGS2.getLatest().getVersionString());
    }

    /**
     * Test method for {@link com.sldeditor.common.vendoroption.VendorOptionManager#getDefaultVendorOption()}.
     */
    @Test
    public void testGetDefaultVendorOption() {
        VendorOptionTypeInterface vo = VendorOptionManager.getInstance().getDefaultVendorOption();

        assertEquals(NoVendorOption.class, vo.getClass());
    }

    /**
     * Test method for {@link com.sldeditor.common.vendoroption.VendorOptionManager#getDefaultVendorOptionVersion()}.
     */
    @Test
    public void testGetDefaultVendorOptionVersion() {
        VendorOptionVersion vo = VendorOptionManager.getInstance().getDefaultVendorOptionVersion();

        assertEquals("Latest", vo.getLatest().getVersionString());
    }

    /**
     * Test method for {@link com.sldeditor.common.vendoroption.VendorOptionManager#getVendorOptionVersion(java.lang.Class)}.
     */
    @Test
    public void testGetVendorOptionVersionClassOfQ() {
        assertNull(VendorOptionManager.getInstance().getVendorOptionVersion(null));

        VendorOptionVersion vo = VendorOptionManager.getInstance().getVendorOptionVersion(NoVendorOption.class);
        assertEquals("Latest", vo.getLatest().getVersionString());

        VendorOptionVersion voGS = VendorOptionManager.getInstance().getVendorOptionVersion(GeoServerVendorOption.class);
        assertEquals("Latest", voGS.getLatest().getVersionString());
    }

    /**
     * Test method for {@link com.sldeditor.common.vendoroption.VendorOptionManager#isAllowed(java.util.List, com.sldeditor.common.vendoroption.VendorOptionVersion)}.
     */
    @Test
    public void testIsAllowed() {
        assertFalse(VendorOptionManager.getInstance().isAllowed(null, null));

        VersionData versionDataMin = VersionData.decode(getClass(), "2.4.1");
        VersionData versionDataMax = VersionData.decode(getClass(), "2.8.3");

        VendorOptionVersion vo = new VendorOptionVersion(GeoServerVendorOption.class, versionDataMin, versionDataMax);

        List<VersionData> versionList = new ArrayList<VersionData>();
        versionList.add(VersionData.decode(GeoServerVendorOption.class, "1.8.3"));

        assertFalse(VendorOptionManager.getInstance().isAllowed(versionList, vo));

        versionList.add(VersionData.decode(GeoServerVendorOption.class, "2.7.x"));
        assertTrue(VendorOptionManager.getInstance().isAllowed(versionList, vo));
    }

    /**
     * Test method for {@link com.sldeditor.common.vendoroption.VendorOptionManager#getDefaultVendorOptionVersionData()}.
     */
    @Test
    public void testGetDefaultVendorOptionVersionData() {
        VersionData versionData = VendorOptionManager.getInstance().getDefaultVendorOptionVersionData();
        assertEquals("Latest", versionData.getVersionString());
    }

}
