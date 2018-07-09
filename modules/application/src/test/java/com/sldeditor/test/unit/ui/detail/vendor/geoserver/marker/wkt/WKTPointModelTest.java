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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.sldeditor.ui.detail.vendor.geoserver.marker.wkt.WKTPoint;
import com.sldeditor.ui.detail.vendor.geoserver.marker.wkt.WKTPointModel;
import com.sldeditor.ui.detail.vendor.geoserver.marker.wkt.WKTSegmentList;
import com.sldeditor.ui.detail.vendor.geoserver.marker.wkt.WKTType;
import org.geotools.geometry.DirectPosition2D;
import org.junit.Test;
import org.opengis.geometry.DirectPosition;

/**
 * The unit test for WKTPointModel.
 *
 * <p>{@link com.sldeditor.ui.detail.vendor.geoserver.marker.wkt.WKTPointModel}
 *
 * @author Robert Ward (SCISYS)
 */
public class WKTPointModelTest {

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.marker.wkt.WKTPointModel#WKTPointModel()}. Test
     * method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.marker.wkt.WKTPointModel#getColumnName(int)}. Test
     * method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.marker.wkt.WKTPointModel#getColumnCount()}.
     */
    @Test
    public void testWKTPointModel() {
        WKTPointModel model = new WKTPointModel();

        assertEquals(2, model.getColumnCount());
        assertTrue(model.getColumnName(0).compareTo("X") == 0);
        assertTrue(model.getColumnName(1).compareTo("Y") == 0);
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.marker.wkt.WKTPointModel#getValueAt(int, int)}. Test
     * method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.marker.wkt.WKTPointModel#isCellEditable(int, int)}.
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.marker.wkt.WKTPointModel#setValueAt(java.lang.Object,
     * int, int)}. Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.marker.wkt.WKTPointModel#getRowCount()}. Test method
     * for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.marker.wkt.WKTPointModel#removePoint(int)}. Test
     * method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.marker.wkt.WKTPointModel#addNewPoint()}. Test method
     * for {@link com.sldeditor.ui.detail.vendor.geoserver.marker.wkt.WKTPointModel#clear()}.
     */
    @Test
    public void testGetValueAt() {
        WKTPointModel model = new WKTPointModel();

        assertEquals(0, model.getRowCount());
        assertTrue(model.isCellEditable(0, 0));
        assertTrue(model.isCellEditable(0, 1));

        model.addNewPoint();
        assertEquals(1, model.getRowCount());

        assertNull(model.getValueAt(-1, 0));
        assertNull(model.getValueAt(5, 0));
        assertNull(model.getValueAt(0, -1));
        assertNull(model.getValueAt(0, 5));

        assertTrue(Math.abs(((Double) model.getValueAt(0, 0)).doubleValue() - 0.0) < 0.001);
        assertTrue(Math.abs(((Double) model.getValueAt(0, 1)).doubleValue() - 0.0) < 0.001);

        model.setValueAt(null, -1, 0);
        model.setValueAt(null, 5, 0);
        model.setValueAt(null, 0, -1);
        model.setValueAt(null, 0, 5);

        Double expectedValue = 42.0;
        // Expecting a double as a string, so should be ignored
        model.setValueAt(expectedValue, 0, 0);
        assertTrue(Math.abs(((Double) model.getValueAt(0, 0)).doubleValue() - 0.0) < 0.001);

        // Double as a string, should be valid
        model.setValueAt(String.valueOf(expectedValue), 0, 0);
        assertTrue(
                Math.abs(
                                ((Double) model.getValueAt(0, 0)).doubleValue()
                                        - expectedValue.doubleValue())
                        < 0.001);
        model.setValueAt(String.valueOf(expectedValue), 0, 1);
        assertTrue(
                Math.abs(
                                ((Double) model.getValueAt(0, 1)).doubleValue()
                                        - expectedValue.doubleValue())
                        < 0.001);

        model.removePoint(-1);
        assertEquals(1, model.getRowCount());
        model.removePoint(1);
        assertEquals(1, model.getRowCount());
        model.removePoint(0);
        assertEquals(0, model.getRowCount());

        model.addNewPoint();
        model.addNewPoint();
        model.addNewPoint();
        assertEquals(3, model.getRowCount());
        model.clear();
        assertEquals(0, model.getRowCount());
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.marker.wkt.WKTPointModel#setWKTType(com.sldeditor.ui.detail.vendor.geoserver.marker.wkt.WKTType)}.
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.marker.wkt.WKTPointModel#populate(com.sldeditor.ui.detail.vendor.geoserver.marker.wkt.WKTSegmentList)}.
     */
    @Test
    public void testPopulateWKTSegmentList() {
        WKTSegmentList segmentList = new WKTSegmentList();

        DirectPosition pos1 = new DirectPosition2D(1.0, 1.0);
        DirectPosition pos2 = new DirectPosition2D(2.0, 2.0);
        DirectPosition pos3 = new DirectPosition2D(3.0, 3.0);
        DirectPosition pos4 = new DirectPosition2D(4.0, 4.0);
        segmentList.addPoint(new WKTPoint(pos1));
        segmentList.addPoint(new WKTPoint(pos2));
        segmentList.addPoint(new WKTPoint(pos3));
        segmentList.addPoint(new WKTPoint(pos4));

        WKTPointModel model = new WKTPointModel();

        WKTType wktType = new WKTType("name", false, 5, "", false, false);
        model.setWKTType(wktType);
        model.populate(null);
        assertTrue(model.getRowCount() == 0);
        model.populate(segmentList);
        assertTrue(model.getRowCount() == 4);

        model.populate(segmentList);
        assertTrue(model.getRowCount() == 4);

        segmentList.addPoint(new WKTPoint(pos1));
        assertTrue(model.getRowCount() == 5);

        // Set WKTType to null is the same as first and last points flag = false
        model.setWKTType(null);
        model.populate(segmentList);
        assertTrue(model.getRowCount() == 5);
    }
}
