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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.sldeditor.ui.detail.vendor.geoserver.marker.wkt.WKTConversion;
import com.sldeditor.ui.detail.vendor.geoserver.marker.wkt.WKTGeometry;
import com.sldeditor.ui.detail.vendor.geoserver.marker.wkt.WKTType;
import java.util.List;
import org.junit.Test;
import org.locationtech.jts.geom.Geometry;

/**
 * The unit test for WKTConversion.
 *
 * <p>{@link com.sldeditor.ui.detail.vendor.geoserver.marker.wkt.WKTConversion}
 *
 * @author Robert Ward (SCISYS)
 */
public class WKTConversionTest {

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.marker.wkt.WKTConversion#getWKTTypeData()}. Test
     * method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.marker.wkt.WKTConversion#getWKTType(java.lang.String)}.
     */
    @Test
    public void testGetWKTTypeData() {
        String geometryType = "POLYGON";
        WKTType wktType = WKTConversion.getWKTType(geometryType);
        assertNotNull(wktType);

        List<WKTType> data = WKTConversion.getWKTTypeData();

        assertFalse(data.isEmpty());

        data = WKTConversion.getWKTTypeData();

        assertFalse(data.isEmpty());

        wktType = WKTConversion.getWKTType(geometryType);
        assertNotNull(wktType);
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.marker.wkt.WKTConversion#parseWKTString(java.lang.String)}.
     */
    @Test
    public void testParseWKTString() {
        WKTGeometry geometry = WKTConversion.parseWKTString(null);
        assertNull(geometry);

        // CHECKSTYLE:OFF
        String wktString =
                "wkt://MULTILINESTRING((-0.25 -0.25, -0.125 -0.25), (0.125 -0.25, 0.25 -0.25), (-0.25 0.25, -0.125 0.25), (0.125 0.25, 0.25 0.25))";
        // CHECKSTYLE:ON
        geometry = WKTConversion.parseWKTString(wktString);
        assertNotNull(geometry);
        assertTrue(geometry.isValid());

        wktString = "LINESTRING(0.0 0.25, 0.25 0.25, 0.5 0.75, 0.75 0.25, 1.00 0.25)";
        geometry = WKTConversion.parseWKTString(wktString);
        assertNotNull(geometry);
        assertTrue(geometry.isValid());

        wktString = "POLYGON((0 0, 1 0, 1 1, 0 1, 0 0))";
        geometry = WKTConversion.parseWKTString(wktString);
        assertNotNull(geometry);
        assertTrue(geometry.isValid());

        wktString = "wkt://POINT(40 0)";
        geometry = WKTConversion.parseWKTString(wktString);
        assertNotNull(geometry);
        assertTrue(geometry.isValid());

        wktString = "wkt://MULTIPOINT (10 40, 40 30, 20 20, 30 10)";
        geometry = WKTConversion.parseWKTString(wktString);
        assertNotNull(geometry);
        assertTrue(geometry.isValid());

        // CHECKSTYLE:OFF
        wktString =
                "MULTIPOLYGON (((30 20, 45 40, 10 40, 30 20)),((15 5, 40 10, 10 20, 5 10, 15 5)))";
        // CHECKSTYLE:ON
        geometry = WKTConversion.parseWKTString(wktString);
        assertNotNull(geometry);
        assertTrue(geometry.isValid());

        wktString = "MULTILINESTRING ((10 10, 20 20, 10 40),(40 40, 30 30, 40 20, 30 10))";
        geometry = WKTConversion.parseWKTString(wktString);
        assertNotNull(geometry);
        assertTrue(geometry.isValid());

        wktString = "invalid wkt string";
        geometry = WKTConversion.parseWKTString(wktString);
        assertNotNull(geometry);
        assertFalse(geometry.isValid());
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.marker.wkt.WKTConversion#generateWKTString(com.sldeditor.ui.detail.vendor.geoserver.marker.wkt.WKTGeometry,
     * boolean)}.
     */
    @Test
    public void testGenerateWKTString() {
        String actualString = WKTConversion.generateWKTString(null, false);
        assertNull(actualString);

        // CHECKSTYLE:OFF
        String wktString =
                "wkt://MULTILINESTRING((10 10, 20 20, 10 40),(40 40, 30 30, 40 20, 30 10))";
        // CHECKSTYLE:ON
        WKTGeometry geometry = WKTConversion.parseWKTString(wktString);
        assertNotNull(geometry);
        assertTrue(geometry.isValid());
        actualString = WKTConversion.generateWKTString(geometry, false);
        WKTGeometry geometry2 = WKTConversion.parseWKTString(actualString);
        @SuppressWarnings("unused")
        // CHECKSTYLE:OFF
        String actualString2 = WKTConversion.generateWKTString(geometry2, true);
        // CHECKSTYLE:ON

        // Can't compare because the WKT parser keeps swapping the 2 line strings around
        // assertEquals(geometry, geometry2);

        wktString = "LINESTRING(0.0 0.25, 0.25 0.25, 0.5 0.75, 0.75 0.25, 1.00 0.25)";
        geometry = WKTConversion.parseWKTString(wktString);
        assertNotNull(geometry);
        assertTrue(geometry.isValid());
        actualString = WKTConversion.generateWKTString(geometry, false);
        geometry2 = WKTConversion.parseWKTString(actualString);
        actualString2 = WKTConversion.generateWKTString(geometry2, false);
        assertEquals(geometry, geometry2);

        wktString = "POLYGON((0 0, 1 0, 1 1, 0 1, 0 0))";
        geometry = WKTConversion.parseWKTString(wktString);
        assertNotNull(geometry);
        assertTrue(geometry.isValid());
        actualString = WKTConversion.generateWKTString(geometry, false);
        geometry2 = WKTConversion.parseWKTString(actualString);
        actualString2 = WKTConversion.generateWKTString(geometry2, false);
        assertEquals(geometry, geometry2);

        wktString = "wkt://POINT(40 0)";
        geometry = WKTConversion.parseWKTString(wktString);
        assertNotNull(geometry);
        assertTrue(geometry.isValid());
        actualString = WKTConversion.generateWKTString(geometry, false);
        geometry2 = WKTConversion.parseWKTString(actualString);
        actualString2 = WKTConversion.generateWKTString(geometry2, false);
        assertEquals(geometry, geometry2);

        wktString = "wkt://MULTIPOINT (10 40, 40 30, 20 20, 30 10)";
        geometry = WKTConversion.parseWKTString(wktString);
        assertNotNull(geometry);
        assertTrue(geometry.isValid());
        actualString = WKTConversion.generateWKTString(geometry, false);
        geometry2 = WKTConversion.parseWKTString(actualString);
        actualString2 = WKTConversion.generateWKTString(geometry2, false);
        // Can't compare because the points are added in a random order
        // assertEquals(geometry, geometry2);

        // CHECKSTYLE:OFF
        wktString =
                "wkt://MULTIPOLYGON(((30 20, 45 40, 10 40, 30 20)),((15 5, 40 10, 10 20, 5 10, 15 5)))";
        // CHECKSTYLE:ON
        geometry = WKTConversion.parseWKTString(wktString);
        assertNotNull(geometry);
        assertTrue(geometry.isValid());
        actualString = WKTConversion.generateWKTString(geometry, true);
        geometry2 = WKTConversion.parseWKTString(actualString);
        actualString2 = WKTConversion.generateWKTString(geometry2, true);
        // Can't compare because the WKT parser keeps swapping the 2 polygons around
        // assertEquals(geometry, geometry2);

        wktString = "MULTILINESTRING ((10 10, 20 20, 10 40),(40 40, 30 30, 40 20, 30 10))";
        geometry = WKTConversion.parseWKTString(wktString);
        assertNotNull(geometry);
        assertTrue(geometry.isValid());
        actualString = WKTConversion.generateWKTString(geometry, false);
        geometry2 = WKTConversion.parseWKTString(actualString);
        actualString2 = WKTConversion.generateWKTString(geometry2, false);
        // Can't compare because the WKT parser keeps swapping the 2 line around
        // assertEquals(geometry, geometry2);

        wktString = "invalid wkt string";
        geometry = WKTConversion.parseWKTString(wktString);
        assertNotNull(geometry);
        assertFalse(geometry.isValid());
        actualString = WKTConversion.generateWKTString(geometry, false);
        geometry2 = WKTConversion.parseWKTString(actualString);
        actualString2 = WKTConversion.generateWKTString(geometry2, false);
        assertEquals(geometry, geometry2);
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.marker.wkt.WKTConversion#createEmpty(com.sldeditor.ui.detail.vendor.geoserver.marker.wkt.WKTType)}.
     */
    @Test
    public void testCreateEmpty() {
        WKTType wktType = null;

        WKTGeometry geometry = WKTConversion.createEmpty(wktType);
        assertNull(geometry.getGeometryType());

        wktType = new WKTType("name", false, 5, "", false, true);

        geometry = WKTConversion.createEmpty(wktType);
        assertEquals(wktType, geometry.getGeometryType());
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.marker.wkt.WKTConversion#convertToGeometry(java.lang.String,
     * java.lang.String)}.
     */
    @Test
    public void testConvertToGeometry() {

        // CHECKSTYLE:OFF
        String wktString =
                "wkt://MULTIPOLYGON(((30 20, 45 40, 10 40, 30 20)),((15 5, 40 10, 10 20, 5 10, 15 5)))";
        // CHECKSTYLE:ON
        String crsCode = null;

        Geometry geometry = WKTConversion.convertToGeometry(wktString, crsCode);

        assertNotNull(geometry);
        assertTrue(geometry.getSRID() == 0);

        crsCode = "EPSG:4326";
        geometry = WKTConversion.convertToGeometry(wktString, crsCode);

        assertNotNull(geometry);
        assertTrue(geometry.getSRID() == 4326);
    }
}
