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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sldeditor.common.vendoroption.VersionData;
import org.junit.jupiter.api.Test;

/**
 * Unit test for VersionData.
 *
 * <p>{@link com.sldeditor.common.vendoroption.VersionData}
 *
 * @author Robert Ward (SCISYS)
 */
public class VersionDataTest {

    /** Test method for {@link com.sldeditor.common.vendoroption.VersionData#clone()}. */
    @Test
    public void testClone() {
        VersionData versionData1 = VersionData.decode(getClass(), "1.2.3");
        VersionData versionData2 = versionData1.clone();
        assertEquals(versionData1, versionData2);
    }

    /**
     * Test method for {@link com.sldeditor.common.vendoroption.VersionData#decode(java.lang.Class,
     * java.lang.String)}. Test method for {@link
     * com.sldeditor.common.vendoroption.VersionData#getVersionString()}.
     */
    @Test
    public void testDecode() {
        assertNull(VersionData.decode(null, null));

        String expectedVersionString1 = "1.2.3";
        VersionData versionData1 = VersionData.decode(getClass(), expectedVersionString1);
        assertEquals(getClass(), versionData1.getVendorOptionType());
        assertEquals(expectedVersionString1, versionData1.getVersionString());

        String expectedVersionString2 = "1.2";
        VersionData versionData2 = VersionData.decode(getClass(), expectedVersionString2);
        assertEquals(expectedVersionString2, versionData2.getVersionString());

        String expectedVersionString3 = "1";
        VersionData versionData3 = VersionData.decode(getClass(), expectedVersionString3);
        assertEquals(expectedVersionString3, versionData3.getVersionString());

        String expectedVersionString4 = "";
        VersionData versionData4 = VersionData.decode(getClass(), expectedVersionString4);
        assertNull(versionData4);

        String expectedVersionString5 = "1,2,3";
        VersionData versionData5 = VersionData.decode(getClass(), expectedVersionString5);
        assertNull(versionData5);

        String expectedVersionString6 = "1.2.3a";
        VersionData versionData6 = VersionData.decode(getClass(), expectedVersionString6);
        assertNull(versionData6);

        String expectedVersionString7 = "Latest";
        VersionData versionData7 = VersionData.decode(getClass(), expectedVersionString7);
        assertEquals(expectedVersionString7, versionData7.getVersionString());

        String expectedVersionString8 = "first";
        VersionData versionData8 = VersionData.decode(getClass(), expectedVersionString8);
        assertEquals(expectedVersionString8, versionData8.getVersionString());

        String expectedVersionString9 = "1.2.3-SNAPSHOT";
        VersionData versionData9 = VersionData.decode(getClass(), expectedVersionString9);
        assertEquals(expectedVersionString9, versionData9.getVersionString());

        String expectedVersionString10 = "NotSet";
        VersionData versionData10 = VersionData.decode(getClass(), expectedVersionString10);
        assertEquals(expectedVersionString10, versionData10.getVersionString());
    }

    /**
     * Test method for {@link
     * com.sldeditor.common.vendoroption.VersionData#compareTo(com.sldeditor.common.vendoroption.VersionData)}.
     * Test method for {@link
     * com.sldeditor.common.vendoroption.VersionData#equals(java.lang.Object)}.
     */
    @SuppressWarnings("unlikely-arg-type")
    @Test
    public void testCompareTo() {
        VersionData versionData1 = VersionData.decode(getClass(), "1.2.3");
        VersionData versionData2 = VersionData.decode(getClass(), "1.2.3");
        VersionData versionData3 = VersionData.decode(getClass(), "1.2");
        // CHECKSTYLE:OFF
        VersionData versionData4 = VersionData.decode(getClass(), "8.2.3");
        VersionData versionData5 = VersionData.decode(getClass(), "8.2.2");
        VersionData versionData6 = VersionData.decode(getClass(), "8.2.4");
        VersionData versionData7 = VersionData.decode(getClass(), "8.1.2");
        VersionData versionData8 = VersionData.decode(getClass(), "8.3.4");
        VersionData versionData9 = VersionData.decode(getClass(), "8.3.4-SNAPSHOT");
        VersionData versionData10 = VersionData.decode(getClass(), "8.3.4.1");
        VersionData versionData11 = VersionData.decode(getClass(), "8.3.4.2");
        VersionData versionData12 = VersionData.decode(null, "8.3.4.2");
        // CHECKSTYLE:ON

        assertTrue(versionData1.compareTo(versionData2) == 0);
        assertTrue(versionData1.equals(versionData2));
        assertTrue(versionData1.compareTo(versionData3) != 0);
        assertFalse(versionData1.equals(versionData3));
        assertTrue(versionData1.compareTo(versionData4) != 0);
        assertFalse(versionData1.equals(versionData4));
        assertTrue(versionData3.compareTo(versionData4) != 0);
        assertFalse(versionData3.equals(versionData4));
        assertTrue(versionData4.compareTo(versionData5) != 0);
        assertFalse(versionData4.equals(versionData5));
        assertTrue(versionData4.compareTo(versionData6) != 0);
        assertFalse(versionData4.equals(versionData6));
        assertTrue(versionData4.compareTo(versionData7) != 0);
        assertFalse(versionData4.equals(versionData7));
        assertTrue(versionData4.compareTo(versionData8) != 0);
        assertFalse(versionData4.equals(versionData8));
        assertTrue(versionData10.compareTo(versionData11) != 0);
        assertFalse(versionData10.equals(versionData11));
        assertFalse(versionData12.equals(versionData11));

        assertFalse(versionData4.equals(null));
        assertFalse(versionData4.equals(new String()));

        assertTrue(versionData1.hashCode() == versionData2.hashCode());
        assertFalse(versionData1.hashCode() == versionData4.hashCode());

        // SNAPSHOT versions come before non-SNAPSHOT versions if the numbers are the same
        assertTrue(versionData8.compareTo(versionData9) == 1);
        assertFalse(versionData8.equals(versionData9));
    }

    /**
     * Test method for {@link
     * com.sldeditor.common.vendoroption.VersionData#getEarliestVersion(java.lang.Class)}.
     */
    @Test
    public void testGetEarliestVersion() {
        VersionData earliest = VersionData.getEarliestVersion(getClass());
        assertEquals("First", earliest.getVersionString());
    }

    /**
     * Test method for {@link
     * com.sldeditor.common.vendoroption.VersionData#getLatestVersion(java.lang.Class)}.
     */
    @Test
    public void testGetLatestVersion() {
        VersionData latest = VersionData.getLatestVersion(getClass());
        assertEquals("Latest", latest.getVersionString());
    }

    /**
     * Test method for {@link
     * com.sldeditor.common.vendoroption.VersionData#getNotSetVersion(java.lang.Class)}. Test method
     * for {@link com.sldeditor.common.vendoroption.VersionData#isNotSet()}.
     */
    @Test
    public void testGetNotSetVersion() {
        VersionData latest = VersionData.getLatestVersion(getClass());
        assertFalse(latest.isNotSet());

        VersionData notSet = VersionData.getNotSetVersion(getClass());
        assertTrue(notSet.isNotSet());
    }

    /** Test method for {@link com.sldeditor.common.vendoroption.VersionData#toString()}. */
    @Test
    public void testToString() {
        String expectedVersionString = "1.2.3";
        VersionData versionData1 = VersionData.decode(getClass(), expectedVersionString);
        assertEquals(expectedVersionString, versionData1.toString());
    }

    /**
     * Test method for {@link
     * com.sldeditor.common.vendoroption.VersionData#inRange(com.sldeditor.common.vendoroption.VersionData,
     * com.sldeditor.common.vendoroption.VersionData)}.
     */
    @Test
    public void testInRange() {
        VersionData versionDataMin = VersionData.decode(getClass(), "2.4.1");
        VersionData versionDataMax = VersionData.decode(getClass(), "2.8.3");

        VersionData versionData1 = VersionData.decode(getClass(), "1.2");
        VersionData versionData2 = VersionData.decode(getClass(), "2.4.0");
        VersionData versionData3 = VersionData.decode(getClass(), "2.4.1");
        // CHECKSTYLE:OFF
        VersionData versionData4 = VersionData.decode(getClass(), "2.5.1");
        VersionData versionData5 = VersionData.decode(getClass(), "2.8.3");
        VersionData versionData6 = VersionData.decode(getClass(), "2.8.4");
        VersionData versionData7 = VersionData.getNotSetVersion(getClass());
        // CHECKSTYLE:ON

        assertFalse(versionData1.inRange(versionDataMin, versionDataMax));
        assertFalse(versionData2.inRange(versionDataMin, versionDataMax));
        assertTrue(versionData3.inRange(versionDataMin, versionDataMax));
        assertTrue(versionData4.inRange(versionDataMin, versionDataMax));
        assertTrue(versionData5.inRange(versionDataMin, versionDataMax));
        assertFalse(versionData6.inRange(versionDataMin, versionDataMax));
        assertFalse(versionData7.inRange(versionDataMin, versionDataMax));

        assertFalse(versionData1.inRange(null, versionDataMax));
        assertFalse(versionData1.inRange(versionDataMin, null));
        assertFalse(versionData1.inRange(null, null));
    }

    /**
     * Test method for {@link com.sldeditor.common.vendoroption.VersionData#getEncodedString()}.
     * Test method for {@link
     * com.sldeditor.common.vendoroption.VersionData#getDecodedString(java.lang.String)}.
     */
    @Test
    public void testEncodedString() {
        VersionData versionData1 = VersionData.decode(getClass(), "2.4.1");
        String encodeString1 = versionData1.getEncodedString();
        VersionData versionData1Decoded = VersionData.getDecodedString(encodeString1);
        assertEquals(versionData1, versionData1Decoded);

        VersionData versionData2 = VersionData.getEarliestVersion(getClass());
        String encodeString2 = versionData2.getEncodedString();
        VersionData versionData2Decoded = VersionData.getDecodedString(encodeString2);
        assertEquals(versionData2, versionData2Decoded);

        VersionData versionData3 = VersionData.getLatestVersion(getClass());
        String encodeString3 = versionData3.getEncodedString();
        VersionData versionData3Decoded = VersionData.getDecodedString(encodeString3);
        assertEquals(versionData3, versionData3Decoded);
    }
}
