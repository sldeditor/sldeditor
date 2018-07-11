/*
 * SLD Editor - The Open Source Java SLD Editor
 *
 * Copyright (C) 2017, SCISYS UK Limited
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

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.sldeditor.ui.detail.vendor.geoserver.marker.wkt.WKTConversion;
import com.sldeditor.ui.detail.vendor.geoserver.marker.wkt.WKTDialog;
import com.sldeditor.ui.detail.vendor.geoserver.marker.wkt.WKTType;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * The unit test for WKTDialog.
 *
 * <p>{@link com.sldeditor.ui.detail.vendor.geoserver.marker.wkt.WKTDialog}
 *
 * @author Robert Ward (SCISYS)
 */
public class WKTDialogTest {

    class TestWKTDialog extends WKTDialog {

        /** The Constant serialVersionUID. */
        private static final long serialVersionUID = 1L;

        public TestWKTDialog() {
            super();
        }

        public void testShow(String text) {
            populate(text);
        }

        /** Geometry type updated. */
        public void testGeometryTypeUpdated() {
            geometryTypeUpdated();
        }

        /** Reload. */
        public void testReload() {
            reload();
        }

        /** Adds the segment. */
        public void testAddSegment() {
            addSegment();
        }

        /** Removes the segment. */
        public void testRemoveSegment() {
            removeSegment();
        }

        /** Adds the point. */
        public void testAddPoint() {
            addPoint();
        }

        /** Removes the point. */
        public void testRemovePoint() {
            removePoint();
        }

        /** Adds the multi shape. */
        public void testAddMultiShape() {
            addMultiShape();
        }

        /** Removes the multi shape. */
        public void testRemoveMultiShape() {
            removeMultiShape();
        }

        /**
         * Test set geometry type.
         *
         * @param geometryType the geometry type
         */
        public void testSetGeometryType(WKTType geometryType) {
            setGeometryType(geometryType);
        }
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.marker.wkt.WKTDialog#WKTDialog()}.
     */
    @Test
    public void testWKTDialog() {
        TestWKTDialog dlg = new TestWKTDialog();

        dlg.testShow("wkt://LINESTRING (30 10, 10 30, 40 40)");

        String actual = dlg.getWKTString();

        assertNotNull(actual);

        List<WKTType> list = WKTConversion.getWKTTypeData();
        dlg.testSetGeometryType(list.get(0));
        dlg.testAddSegment();
        dlg.testAddPoint();
        dlg.testRemovePoint();
        dlg.testRemoveSegment();
        dlg.testReload();

        dlg.testShow("wkt://MULTILINESTRING ((10 10, 20 20, 10 40), (40 40, 30 30, 40 20, 30 10))");
        dlg.testAddMultiShape();
        dlg.testRemoveMultiShape();
    }
}
