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

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.geotools.geometry.DirectPosition2D;
import org.junit.Test;
import org.opengis.geometry.DirectPosition;

import com.sldeditor.ui.detail.vendor.geoserver.marker.wkt.WKTPoint;
import com.sldeditor.ui.detail.vendor.geoserver.marker.wkt.WKTSegmentList;

/**
 * The unit test for WKTSegmentList.
 * <p>{@link com.sldeditor.ui.detail.vendor.geoserver.marker.wkt.WKTSegmentList}
 *
 * @author Robert Ward (SCISYS)
 */
public class WKTSegmentListTest {

    /**
     * Test method for {@link com.sldeditor.ui.detail.vendor.geoserver.marker.wkt.WKTSegmentList#getWktPointList(boolean)}.
     * Test method for {@link com.sldeditor.ui.detail.vendor.geoserver.marker.wkt.WKTSegmentList#setWktPointList(java.util.List)}.
     * Test method for {@link com.sldeditor.ui.detail.vendor.geoserver.marker.wkt.WKTSegmentList#addPoint(com.sldeditor.ui.detail.vendor.geoserver.marker.wkt.WKTPoint)}.
     * Test method for {@link com.sldeditor.ui.detail.vendor.geoserver.marker.wkt.WKTSegmentList#getWKTString()}.
     * Test method for {@link com.sldeditor.ui.detail.vendor.geoserver.marker.wkt.WKTSegmentList#getWKTString(boolean)}.
     */
    @Test
    public void testGetWktPointList() {
        WKTSegmentList segmentList = new WKTSegmentList();
        
        DirectPosition pos1 = new DirectPosition2D(1.0, 1.0);
        DirectPosition pos2 = new DirectPosition2D(2.0, 2.0);
        DirectPosition pos3 = new DirectPosition2D(3.0, 3.0);
        DirectPosition pos4 = new DirectPosition2D(4.0, 4.0);
        segmentList.addPoint(new WKTPoint(pos1));
        
        String actualValue = segmentList.getWKTString();
        assertTrue(actualValue.compareTo("(1.0 1.0)") == 0);

        List<WKTPoint> ptList = new ArrayList<WKTPoint>();
        ptList.add(new WKTPoint(pos1));
        ptList.add(new WKTPoint(pos2));
        ptList.add(new WKTPoint(pos3));
        ptList.add(new WKTPoint(pos4));
        segmentList.setWktPointList(ptList);
        actualValue = segmentList.getWKTString();
        assertTrue(actualValue.compareTo("(1.0 1.0, 2.0 2.0, 3.0 3.0, 4.0 4.0)") == 0);

        actualValue = segmentList.getWKTString(false);
        assertTrue(actualValue.compareTo("(1.0 1.0, 2.0 2.0, 3.0 3.0, 4.0 4.0)") == 0);

        actualValue = segmentList.getWKTString(true);
        assertTrue(actualValue.compareTo("(1.0 1.0, 2.0 2.0, 3.0 3.0, 4.0 4.0, 1.0 1.0)") == 0);

        ptList.add(new WKTPoint(pos1));
        segmentList.setWktPointList(ptList);

        assertTrue(segmentList.getWktPointList(false).size() == ptList.size());
        assertTrue((segmentList.getWktPointList(true).size() + 1) == ptList.size());

        ptList.clear();
        ptList.add(new WKTPoint(pos1));
        assertTrue(segmentList.getWktPointList(false).size() == ptList.size());
        assertTrue(segmentList.getWktPointList(true).size() == ptList.size());

        segmentList.addPoint(new WKTPoint(pos2));
        assertTrue(segmentList.getWktPointList(false).size() == ptList.size());
        assertTrue(segmentList.getWktPointList(true).size() == ptList.size());
    }
}
