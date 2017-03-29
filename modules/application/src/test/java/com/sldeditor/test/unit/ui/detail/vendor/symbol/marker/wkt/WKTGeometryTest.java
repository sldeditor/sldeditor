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

package com.sldeditor.test.unit.ui.detail.vendor.symbol.marker.wkt;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.ui.detail.vendor.geoserver.marker.wkt.WKTDialog;
import com.sldeditor.ui.detail.vendor.geoserver.marker.wkt.WKTGeometry;
import com.sldeditor.ui.detail.vendor.geoserver.marker.wkt.WKTType;

/**
 * The unit test for WKTGeometry.
 * 
 * <p>{@link com.sldeditor.ui.detail.vendor.geoserver.marker.wkt.WKTGeometry}
 *
 * @author Robert Ward (SCISYS)
 */
public class WKTGeometryTest {

    /**
     * Test method for
     * {@link com.sldeditor.ui.detail.vendor.geoserver.marker.wkt.WKTGeometry#isValid()}. Test
     * method for
     * {@link com.sldeditor.ui.detail.vendor.geoserver.marker.wkt.WKTGeometry#setValid(boolean)}.
     */
    @Test
    public void testIsValid() {
        WKTGeometry g = new WKTGeometry();
        assertTrue(g.isValid());
        assertTrue(g.isEmpty());

        g.setValid(false);
        assertFalse(g.isValid());
        g.setValid(true);
        assertTrue(g.isValid());
    }

    /**
     * Test method for
     * {@link com.sldeditor.ui.detail.vendor.geoserver.marker.wkt.WKTGeometry#getGeometryType()}.
     * Test method for
     * {@link com.sldeditor.ui.detail.vendor.geoserver.marker.wkt.WKTGeometry#setGeometryType(com.sldeditor.ui.detail.vendor.geoserver.marker.wkt.WKTType)}.
     */
    @Test
    public void testGetGeometryType() {
        WKTGeometry g = new WKTGeometry();

        WKTType wktType = new WKTType("name", false, 5, "", false, true);

        g.setGeometryType(wktType);

        assertEquals(wktType, g.getGeometryType());
    }

    /**
     * Test method for
     * {@link com.sldeditor.ui.detail.vendor.geoserver.marker.wkt.WKTGeometry#addNewSegment()}. Test
     * method for
     * {@link com.sldeditor.ui.detail.vendor.geoserver.marker.wkt.WKTGeometry#addNewSegment(int)}.
     * Test method for
     * {@link com.sldeditor.ui.detail.vendor.geoserver.marker.wkt.WKTGeometry#isEmpty()}. Test
     * method for
     * {@link com.sldeditor.ui.detail.vendor.geoserver.marker.wkt.WKTGeometry#getSegmentList(int)}.
     */
    @Test
    public void testAddNewSegment() {
        WKTGeometry g = new WKTGeometry();

        WKTType wktType = new WKTType("name", false, 5, "", false, true);

        g.setGeometryType(wktType);

        g.addNewSegment();
        assertFalse(g.isEmpty());
        assertEquals(1, g.getNoOfSegments());

        assertEquals(-1, g.addNewSegment(-3));
        assertFalse(g.isEmpty());
        assertEquals(1, g.getNoOfSegments());

        assertEquals(0, g.addNewSegment(3));
        assertFalse(g.isEmpty());
        assertEquals(2, g.getNoOfSegments());
    }

    /**
     * Test method for
     * {@link com.sldeditor.ui.detail.vendor.geoserver.marker.wkt.WKTGeometry#addNewShape()}. Test
     * method for
     * {@link com.sldeditor.ui.detail.vendor.geoserver.marker.wkt.WKTGeometry#removeShape(int)}.
     * Test method for
     * {@link com.sldeditor.ui.detail.vendor.geoserver.marker.wkt.WKTGeometry#removeSegment(int, int)}.
     */
    @Test
    public void testAddNewShape() {
        WKTGeometry g = new WKTGeometry();

        String expectedListItem = "listitem";
        WKTType wktType = new WKTType("name", false, 5, expectedListItem, false, true);

        g.setGeometryType(wktType);

        g.addNewShape();

        int expectedIndex = 0;
        String actualValue = g.getMultiShapeName(expectedIndex);
        assertTrue(actualValue
                .compareTo(String.format("%s %d", expectedListItem, expectedIndex + 1)) == 0);

        g.addNewShape();

        g.removeShape(-1);
        g.removeShape(2);
        g.removeShape(0);
        assertTrue(g.getNoOfSegments() == 1);

        assertEquals(2, g.addNewShape());

        assertEquals(1, g.addNewSegment(1));
        assertEquals(2, g.addNewSegment(1));

        g.removeSegment(0, -1);
        assertEquals(3, g.addNewSegment(1));
        g.removeSegment(0, 5);
        assertEquals(4, g.addNewSegment(1));

        g.removeSegment(1, 2);
        int actualSize = g.getSegmentList(1).size();
        assertEquals(4, actualSize);

        actualValue = g.getSegmentName(0);
        assertTrue(actualValue
                .compareTo(String.format("%s %d", expectedListItem, expectedIndex + 1)) == 0);

        wktType = new WKTType("name", false, 5, expectedListItem, true, true);

        g.setGeometryType(wktType);
        actualValue = g.getSegmentName(0);
        assertTrue(actualValue.compareTo(String.format("%s %d",
                Localisation.getString(WKTDialog.class, "WKTDialog.partShape"),
                expectedIndex + 1)) == 0);

        assertNull(g.getSegmentList(-4));
        assertNull(g.getSegmentList(43));
    }

    /**
     * Test method for
     * {@link com.sldeditor.ui.detail.vendor.geoserver.marker.wkt.WKTGeometry#hashCode()}.
     */
    @Test
    public void testHashCode() {
        WKTGeometry g = new WKTGeometry();
        String expectedListItem = "listitem";
        WKTType wktType = new WKTType("name", false, 5, expectedListItem, false, true);
        g.setGeometryType(wktType);
        g.addNewShape();
        g.addNewShape();
        g.addNewShape();

        WKTGeometry g2 = new WKTGeometry();
        g2.setGeometryType(wktType);
        g2.addNewShape();
        g2.addNewShape();

        assertTrue(g.hashCode() != g2.hashCode());

        assertTrue(g.equals(g));
        assertFalse(g.equals(null));
        assertFalse(g.equals(expectedListItem));

        WKTGeometry g3 = new WKTGeometry();
        WKTType wktType2 = new WKTType("name", false, 5, expectedListItem, true, true);
        g3.setGeometryType(wktType2);
        g3.addNewShape();
        g3.addNewShape();
        g3.addNewShape();
        assertFalse(g.equals(g3));
    }

}
