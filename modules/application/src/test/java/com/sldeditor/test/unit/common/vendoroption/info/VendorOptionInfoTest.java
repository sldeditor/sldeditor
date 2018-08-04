/*
 *    SLDEditor - SLD Editor application
 *
 *    (C) 2016, SCISYS
 *
 */

package com.sldeditor.test.unit.common.vendoroption.info;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sldeditor.common.vendoroption.GeoServerVendorOption;
import com.sldeditor.common.vendoroption.VendorOptionVersion;
import com.sldeditor.common.vendoroption.VersionData;
import com.sldeditor.common.vendoroption.info.VendorOptionInfo;
import org.junit.jupiter.api.Test;

/**
 * Unit test for VendorOptionInfo.
 *
 * <p>{@link com.sldeditor.common.vendoroption.info.VendorOptionInfo}
 *
 * @author Robert Ward (SCISYS)
 */
public class VendorOptionInfoTest {

    /**
     * Test method for {@link
     * com.sldeditor.common.vendoroption.info.VendorOptionInfo#VendorOptionInfo(java.lang.String,
     * com.sldeditor.common.vendoroption.VendorOptionVersion, java.lang.String)}. Test method for
     * {@link com.sldeditor.common.vendoroption.info.VendorOptionInfo#getName()}. Test method for
     * {@link com.sldeditor.common.vendoroption.info.VendorOptionInfo#getDescription()}. Test method
     * for {@link com.sldeditor.common.vendoroption.info.VendorOptionInfo#getVersionData()}. Test
     * method for {@link
     * com.sldeditor.common.vendoroption.info.VendorOptionInfo#getVersionString()}.
     */
    @Test
    public void testVendorOptionInfo() {
        String name = "name";
        String description = "test description";
        VendorOptionInfo info = new VendorOptionInfo(name, null, description);

        assertEquals(name, info.getName());
        assertEquals(description, info.getDescription());
        assertNull(info.getVersionData());
        assertEquals("", info.getVersionString());

        VersionData versionDataMin = VersionData.decode(getClass(), "2.4.1");
        VersionData versionDataMax = VersionData.decode(getClass(), "2.8.3");
        VendorOptionVersion versionData =
                new VendorOptionVersion(
                        GeoServerVendorOption.class, versionDataMin, versionDataMax);
        VendorOptionInfo info2 = new VendorOptionInfo(name, versionData, description);
        assertEquals("GeoServer 2.4.1-2.8.3", info2.getVersionString());

        assertEquals(0, info.compareTo(info));
        assertEquals(1, info.compareTo(info2));
        assertEquals(-1, info2.compareTo(info));

        VendorOptionVersion versionData2 =
                new VendorOptionVersion(
                        GeoServerVendorOption.class,
                        VersionData.decode(getClass(), "2.2.1"),
                        versionDataMax);
        VendorOptionInfo info3 = new VendorOptionInfo(name, versionData2, description);
        assertEquals(-1, info3.compareTo(info2));
        assertEquals(1, info2.compareTo(info3));
        assertFalse(info2.equals(info3));
    }

    @SuppressWarnings("unlikely-arg-type")
    @Test
    void testEquals() {
        String name = "name";
        String description = "test description";

        VendorOptionVersion versionData2 =
                new VendorOptionVersion(
                        GeoServerVendorOption.class,
                        VersionData.decode(getClass(), "2.2.1"),
                        VersionData.decode(getClass(), "2.4.1"));
        VendorOptionInfo info3 = new VendorOptionInfo(name, versionData2, description);

        VendorOptionVersion versionData3 =
                new VendorOptionVersion(
                        GeoServerVendorOption.class,
                        VersionData.decode(getClass(), "2.2.1"),
                        VersionData.decode(getClass(), "2.4.1"));
        VendorOptionInfo info4 = new VendorOptionInfo(name, versionData3, description);

        assertTrue(info4.equals(info4));
        assertFalse(info4.equals(null));
        assertTrue(info4.equals(info3));

        assertEquals(info4.hashCode(), info3.hashCode());

        // Null everything out

        VendorOptionVersion versionData4 =
                new VendorOptionVersion(
                        GeoServerVendorOption.class,
                        VersionData.decode(getClass(), "2.2.1"),
                        VersionData.decode(getClass(), "2.4.1"));

        String test = "invalid";
        assertFalse(info4.equals(test));
        assertFalse(info4.equals(new VendorOptionInfo(name, versionData4, null)));
        assertFalse(info4.equals(new VendorOptionInfo(null, versionData4, description)));
        assertFalse(info4.equals(new VendorOptionInfo(name, null, description)));

        assertFalse(new VendorOptionInfo(name, versionData4, null).equals(info4));
        assertFalse(new VendorOptionInfo(null, versionData4, description).equals(info4));
        assertFalse(new VendorOptionInfo(name, null, description).equals(info4));
    }
}
