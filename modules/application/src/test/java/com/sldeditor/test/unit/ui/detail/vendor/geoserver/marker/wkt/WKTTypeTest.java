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

package com.sldeditor.test.unit.ui.detail.vendor.geoserver.marker.wkt;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.sldeditor.ui.detail.vendor.geoserver.marker.wkt.WKTType;

/**
 * The unit test for WKTType.
 * <p>{@link com.sldeditor.ui.detail.vendor.geoserver.marker.wkt.WKTType}
 *
 * @author Robert Ward (SCISYS)
 */
public class WKTTypeTest {

    /**
     * Test method for {@link com.sldeditor.ui.detail.vendor.geoserver.marker.wkt.WKTType#WKTType(java.lang.String, boolean, int, java.lang.String, boolean, boolean)}.
     * Test method for {@link com.sldeditor.ui.detail.vendor.geoserver.marker.wkt.WKTType#WKTType(java.lang.String, boolean, int, java.lang.String, boolean)}.
     * Test method for {@link com.sldeditor.ui.detail.vendor.geoserver.marker.wkt.WKTType#getName()}.
     * Test method for {@link com.sldeditor.ui.detail.vendor.geoserver.marker.wkt.WKTType#isMultipleCoordinates()}.
     * Test method for {@link com.sldeditor.ui.detail.vendor.geoserver.marker.wkt.WKTType#toString()}.
     * Test method for {@link com.sldeditor.ui.detail.vendor.geoserver.marker.wkt.WKTType#getNumOfPoints()}.
     * Test method for {@link com.sldeditor.ui.detail.vendor.geoserver.marker.wkt.WKTType#getListItem()}.
     * Test method for {@link com.sldeditor.ui.detail.vendor.geoserver.marker.wkt.WKTType#canHaveMultipleShapes()}.
     * Test method for {@link com.sldeditor.ui.detail.vendor.geoserver.marker.wkt.WKTType#doFirstLastHaveToBeSame()}.
     */
    @Test
    public void testDoFirstLastHaveToBeSame() {
        String name = "test"; 
        boolean multipleCoordinates = true;
        int numOfPoints = 5;
        String listItem = "qwerty";
        boolean canHaveMultipleShapes = true;
        boolean doFirstLastHaveToBeSame = false;

        WKTType wktType = new WKTType(name, 
                multipleCoordinates,
                numOfPoints,
                listItem, 
                canHaveMultipleShapes,
                doFirstLastHaveToBeSame);

        assertTrue(wktType.canHaveMultipleShapes());
        assertTrue(wktType.isMultipleCoordinates());
        assertFalse(wktType.doFirstLastHaveToBeSame());
        assertEquals(numOfPoints, wktType.getNumOfPoints());
        assertTrue(wktType.getListItem().compareTo(listItem) == 0);
        assertTrue(wktType.getName().compareTo(name) == 0);
        assertTrue(wktType.toString().compareTo(name) == 0);

        WKTType wktType2 = new WKTType(name, 
                multipleCoordinates,
                numOfPoints,
                listItem,
                false,
                doFirstLastHaveToBeSame);
        assertFalse(wktType2.canHaveMultipleShapes());

        assertFalse(wktType.equals(wktType2));

        WKTType wktTypeCopy = new WKTType(name, 
                multipleCoordinates,
                numOfPoints,
                listItem, 
                canHaveMultipleShapes,
                doFirstLastHaveToBeSame);
        assertEquals(wktType, wktTypeCopy);

        assertTrue(wktType.hashCode() != wktType2.hashCode());
    }

}
