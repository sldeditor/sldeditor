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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.sldeditor.ui.detail.vendor.geoserver.marker.wkt.WKTPoint;
import org.geotools.geometry.DirectPosition2D;
import org.junit.Test;
import org.opengis.geometry.DirectPosition;

/**
 * The unit test for WKTPoint.
 *
 * <p>{@link com.sldeditor.ui.detail.vendor.geoserver.marker.wkt.WKTPoint}
 *
 * @author Robert Ward (SCISYS)
 */
public class WKTPointTest {

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.marker.wkt.WKTPoint#WKTPoint(org.opengis.geometry.DirectPosition)}.
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.marker.wkt.WKTPoint#WKTPoint()}. Test method for
     * {@link com.sldeditor.ui.detail.vendor.geoserver.marker.wkt.WKTPoint#getX()}. Test method for
     * {@link com.sldeditor.ui.detail.vendor.geoserver.marker.wkt.WKTPoint#getY()}. Test method for
     * {@link com.sldeditor.ui.detail.vendor.geoserver.marker.wkt.WKTPoint#setX(double)}. Test
     * method for {@link com.sldeditor.ui.detail.vendor.geoserver.marker.wkt.WKTPoint#setY(double)}.
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.marker.wkt.WKTPoint#equals(java.lang.Object)}.
     */
    @Test
    public void testWKTPointDirectPosition() {
        WKTPoint point = new WKTPoint();
        assertTrue(Math.abs(point.getX() - 0.0) < 0.001);
        assertTrue(Math.abs(point.getY() - 0.0) < 0.001);

        point = new WKTPoint(null);
        assertTrue(Math.abs(point.getX() - 0.0) < 0.001);
        assertTrue(Math.abs(point.getY() - 0.0) < 0.001);

        double x = 45.2;
        double y = -3.1;
        DirectPosition pos = new DirectPosition2D(x, y);
        point = new WKTPoint(pos);
        assertTrue(Math.abs(point.getX() - x) < 0.001);
        assertTrue(Math.abs(point.getY() - y) < 0.001);

        x = 42.0;
        point.setX(x);
        assertTrue(Math.abs(point.getX() - x) < 0.001);

        y = 42.0;
        point.setY(y);
        assertTrue(Math.abs(point.getY() - y) < 0.001);

        DirectPosition pos2 = new DirectPosition2D(x, y);
        WKTPoint point2 = new WKTPoint(pos2);

        assertTrue(point.equals(point2));

        point2.setX(3.14);
        assertFalse(point.equals(point2));
        assertFalse(point.equals(null));
        assertFalse(point.equals(pos2));
        assertTrue(point.equals(point));

        assertTrue(point.hashCode() != point2.hashCode());
    }
}
