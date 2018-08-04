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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sldeditor.common.vendoroption.VendorOptionVersion;
import com.sldeditor.common.vendoroption.VersionData;
import org.junit.jupiter.api.Test;

/**
 * Unit test for VendorOptionVersion.
 *
 * <p>{@link com.sldeditor.common.vendoroption.VendorOptionVersion}
 *
 * @author Robert Ward (SCISYS)
 */
public class VendorOptionVersionTest {

    /**
     * Test method for {@link
     * com.sldeditor.common.vendoroption.VendorOptionVersion#VendorOptionVersion(java.lang.Class,
     * com.sldeditor.common.vendoroption.VersionData,
     * com.sldeditor.common.vendoroption.VersionData)}.
     */
    @Test
    public void testVendorOptionVersionClassOfQVersionDataVersionData() {
        VersionData versionDataMin = VersionData.decode(getClass(), "2.4.1");
        VersionData versionDataMax = VersionData.decode(getClass(), "2.8.3");

        VendorOptionVersion vo =
                new VendorOptionVersion(getClass(), versionDataMin, versionDataMax);

        assertEquals(versionDataMax, vo.getLatest());
    }

    /**
     * Test method for {@link
     * com.sldeditor.common.vendoroption.VendorOptionVersion#VendorOptionVersion(java.lang.Class,
     * com.sldeditor.common.vendoroption.VersionData)}.
     */
    @Test
    public void testVendorOptionVersionClassOfQVersionData() {
        VersionData versionDataMin = VersionData.decode(getClass(), "2.4.1");

        VendorOptionVersion vo = new VendorOptionVersion(getClass(), versionDataMin);

        assertEquals(versionDataMin, vo.getLatest());
    }

    /**
     * Test method for {@link
     * com.sldeditor.common.vendoroption.VendorOptionVersion#isAllowed(com.sldeditor.common.vendoroption.VersionData)}.
     */
    @Test
    public void testIsAllowed() {
        VersionData versionDataMin = VersionData.decode(getClass(), "2.4.1");
        VersionData versionDataMax = VersionData.decode(getClass(), "2.8.3");

        VendorOptionVersion vo =
                new VendorOptionVersion(getClass(), versionDataMin, versionDataMax);

        assertFalse(vo.isAllowed(null));
        assertFalse(vo.isAllowed(VersionData.decode(getClass(), "1.8.3")));
        assertFalse(vo.isAllowed(VersionData.decode(getClass(), "2.8.4")));
        assertTrue(vo.isAllowed(VersionData.decode(getClass(), "2.5.4")));
        assertFalse(vo.isAllowed(VersionData.decode(String.class, "2.5.4")));
        assertFalse(vo.isAllowed(VersionData.decode(VersionData.class, "2.8.4")));
    }

    /**
     * Test method for {@link
     * com.sldeditor.common.vendoroption.VendorOptionVersion#fromString(java.lang.String)}. Test
     * method for {@link com.sldeditor.common.vendoroption.VendorOptionVersion#toString()}.
     */
    @Test
    public void testFromString() {
        VersionData versionDataMin = VersionData.decode(getClass(), "2.4.1");
        VersionData versionDataMax = VersionData.decode(getClass(), "2.8.3");

        VendorOptionVersion vo =
                new VendorOptionVersion(getClass(), versionDataMin, versionDataMax);
        String actualString = vo.toString();

        VendorOptionVersion decoded = VendorOptionVersion.fromString(actualString);

        assertEquals(vo.getLatest(), decoded.getLatest());
        assertEquals(vo.toString(), decoded.toString());

        // Try with invalid data -- wrong number of components
        String invalidTestData =
                "com.sldeditor.test.unit.common.vendoroption.VendorOptionVersionTest;com.sldeditor.test.unit.common.vendoroption.VendorOptionVersionTest@2.4.1";
        assertNull(VendorOptionVersion.fromString(invalidTestData));

        // Invalid class name
        invalidTestData =
                "com.sldeditor.test.unit.common.vendoroption.Invalid;com.sldeditor.test.unit.common.vendoroption.VendorOptionVersionTest@2.4.1;com.sldeditor.test.unit.common.vendoroption.VendorOptionVersionTest@2.4.1";
        assertNull(VendorOptionVersion.fromString(invalidTestData));

        VendorOptionVersion vo2 = new VendorOptionVersion(getClass(), null, null);
        assertNotNull(vo2.toString());
    }

    @SuppressWarnings("unlikely-arg-type")
    @Test
    public void testEquals() {
        VersionData versionDataMin = VersionData.decode(getClass(), "2.4.1");
        VersionData versionDataMax = VersionData.decode(getClass(), "2.8.3");

        VendorOptionVersion vo =
                new VendorOptionVersion(getClass(), versionDataMin, versionDataMax);
        String actualString = vo.toString();

        VendorOptionVersion decoded = VendorOptionVersion.fromString(actualString);

        assertFalse(decoded.equals(versionDataMax));
        String testData =
                "com.sldeditor.test.unit.common.vendoroption.VendorOptionStatusTest;com.sldeditor.test.unit.common.vendoroption.VendorOptionVersionTest@2.4.1;com.sldeditor.test.unit.common.vendoroption.VendorOptionVersionTest@2.4.1";
        assertFalse(decoded.equals(VendorOptionVersion.fromString(testData)));
        assertFalse(VendorOptionVersion.fromString(testData).equals(decoded));

        testData =
                "com.sldeditor.test.unit.common.vendoroption.VendorOptionStatusTest;;com.sldeditor.test.unit.common.vendoroption.VendorOptionVersionTest@2.8.3";
        assertFalse(decoded.equals(VendorOptionVersion.fromString(testData)));
        assertFalse(VendorOptionVersion.fromString(testData).equals(decoded));

        testData =
                "com.sldeditor.test.unit.common.vendoroption.VendorOptionStatusTest;com.sldeditor.test.unit.common.vendoroption.VendorOptionVersionTest@2.4.1;";
        assertFalse(decoded.equals(VendorOptionVersion.fromString(testData)));
        assertNull(VendorOptionVersion.fromString(testData));
        assertFalse(new VendorOptionVersion(null, versionDataMin, versionDataMax).equals(decoded));
        assertFalse(new VendorOptionVersion(getClass(), null, versionDataMax).equals(decoded));
        assertFalse(new VendorOptionVersion(getClass(), versionDataMin, null).equals(decoded));
        assertFalse(
                vo.equals(
                        new VendorOptionVersion(
                                getClass(),
                                versionDataMin,
                                VersionData.decode(getClass(), "2.8.4"))));
    }
}
