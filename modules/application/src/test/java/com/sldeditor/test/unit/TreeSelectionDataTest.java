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

package com.sldeditor.test.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.sldeditor.TreeSelectionData;
import com.sldeditor.common.xml.ui.SelectedTreeItemEnum;
import com.sldeditor.ui.detail.LineSymbolizerDetails;
import com.sldeditor.ui.detail.PointFillDetails;
import com.sldeditor.ui.detail.PointSymbolizerDetails;
import com.sldeditor.ui.detail.PolygonFillDetails;
import com.sldeditor.ui.detail.PolygonSymbolizerDetails;
import com.sldeditor.ui.detail.RasterSymbolizerDetails;
import com.sldeditor.ui.detail.StrokeDetails;
import com.sldeditor.ui.detail.TextSymbolizerDetails;
import org.junit.Test;

/**
 * Unit test for TreeSelectionData class.
 *
 * <p>{@link com.sldeditor.TreeSelectionData}
 *
 * @author Robert Ward (SCISYS)
 */
public class TreeSelectionDataTest {

    /**
     * Test method for {@link com.sldeditor.TreeSelectionData#getLayerIndex()}. Test method for
     * {@link com.sldeditor.TreeSelectionData#setLayerIndex(int)}.
     */
    @Test
    public void testGetLayerIndex() {
        TreeSelectionData data = new TreeSelectionData();

        assertEquals(-1, data.getLayerIndex());
        int actual = 42;
        data.setLayerIndex(actual);
        assertEquals(actual, data.getLayerIndex());
    }

    /**
     * Test method for {@link com.sldeditor.TreeSelectionData#getStyleIndex()}. Test method for
     * {@link com.sldeditor.TreeSelectionData#setStyleIndex(int)}.
     */
    @Test
    public void testGetStyleIndex() {
        TreeSelectionData data = new TreeSelectionData();

        assertEquals(-1, data.getStyleIndex());
        int actual = 23;
        data.setStyleIndex(actual);
        assertEquals(actual, data.getStyleIndex());
    }

    /**
     * Test method for {@link com.sldeditor.TreeSelectionData#getFeatureTypeStyleIndex()}. Test
     * method for {@link com.sldeditor.TreeSelectionData#setFeatureTypeStyleIndex(int)}.
     */
    @Test
    public void testGetFeatureTypeStyleIndex() {
        TreeSelectionData data = new TreeSelectionData();

        assertEquals(-1, data.getFeatureTypeStyleIndex());
        int actual = 12;
        data.setFeatureTypeStyleIndex(actual);
        assertEquals(actual, data.getFeatureTypeStyleIndex());
    }

    /**
     * Test method for {@link com.sldeditor.TreeSelectionData#getRuleIndex()}. Test method for
     * {@link com.sldeditor.TreeSelectionData#setRuleIndex(int)}.
     */
    @Test
    public void testGetRuleIndex() {
        TreeSelectionData data = new TreeSelectionData();

        assertEquals(-1, data.getRuleIndex());
        int actual = 98;
        data.setRuleIndex(actual);
        assertEquals(actual, data.getRuleIndex());
    }

    /**
     * Test method for {@link com.sldeditor.TreeSelectionData#getSymbolizerIndex()}. Test method for
     * {@link com.sldeditor.TreeSelectionData#setSymbolizerIndex(int)}.
     */
    @Test
    public void testGetSymbolizerIndex() {
        TreeSelectionData data = new TreeSelectionData();

        assertEquals(-1, data.getSymbolizerIndex());
        int actual = 34;
        data.setSymbolizerIndex(actual);
        assertEquals(actual, data.getSymbolizerIndex());
    }

    /**
     * Test method for {@link com.sldeditor.TreeSelectionData#getSymbolizerDetailIndex()}. Test
     * method for {@link com.sldeditor.TreeSelectionData#setSymbolizerDetailIndex(int)}.
     */
    @Test
    public void testGetSymbolizerDetailIndex() {
        TreeSelectionData data = new TreeSelectionData();

        assertEquals(-1, data.getSymbolizerDetailIndex());
        int actual = 65;
        data.setSymbolizerDetailIndex(actual);
        assertEquals(actual, data.getSymbolizerDetailIndex());
    }

    /** Test method for {@link com.sldeditor.TreeSelectionData#getSelection()}. */
    @Test
    public void testGetSelection() {
        TreeSelectionData data = new TreeSelectionData();

        assertEquals(SelectedTreeItemEnum.UNKNOWN, data.getSelection());

        data.setLayerIndex(5);
        assertEquals(SelectedTreeItemEnum.LAYER, data.getSelection());

        data.setStyleIndex(3);
        assertEquals(SelectedTreeItemEnum.STYLE, data.getSelection());

        data.setFeatureTypeStyleIndex(2);
        assertEquals(SelectedTreeItemEnum.FEATURETYPESTYLE, data.getSelection());

        data.setRuleIndex(0);
        assertEquals(SelectedTreeItemEnum.RULE, data.getSelection());

        data.setSymbolizerIndex(0);
        data.setSelectedPanel(PointSymbolizerDetails.class);
        assertEquals(SelectedTreeItemEnum.POINT_SYMBOLIZER, data.getSelection());
        data.setSelectedPanel(LineSymbolizerDetails.class);
        assertEquals(SelectedTreeItemEnum.LINE_SYMBOLIZER, data.getSelection());
        data.setSelectedPanel(PolygonSymbolizerDetails.class);
        assertEquals(SelectedTreeItemEnum.POLYGON_SYMBOLIZER, data.getSelection());
        data.setSelectedPanel(TextSymbolizerDetails.class);
        assertEquals(SelectedTreeItemEnum.TEXT_SYMBOLIZER, data.getSelection());
        data.setSelectedPanel(RasterSymbolizerDetails.class);
        assertEquals(SelectedTreeItemEnum.RASTER_SYMBOLIZER, data.getSelection());
        data.setSelectedPanel(Integer.class);
        assertEquals(SelectedTreeItemEnum.UNKNOWN, data.getSelection());

        data.setSymbolizerDetailIndex(0);
        data.setSelectedPanel(PointFillDetails.class);
        assertEquals(SelectedTreeItemEnum.POINT_FILL, data.getSelection());

        data.setSelectedPanel(PolygonFillDetails.class);
        assertEquals(SelectedTreeItemEnum.POLYGON_FILL, data.getSelection());

        data.setSelectedPanel(StrokeDetails.class);
        assertEquals(SelectedTreeItemEnum.STROKE, data.getSelection());

        data.setSelectedPanel(String.class);
        assertEquals(SelectedTreeItemEnum.UNKNOWN, data.getSelection());
    }

    /**
     * Test method for {@link com.sldeditor.TreeSelectionData#getSelectedPanel()}. Test method for
     * {@link com.sldeditor.TreeSelectionData#setSelectedPanel(java.lang.Class)}.
     */
    @Test
    public void testGetSelectedPanel() {
        TreeSelectionData data = new TreeSelectionData();

        assertNull(data.getSelectedPanel());
        Class<?> actual = String.class;
        data.setSelectedPanel(actual);
        assertEquals(actual, data.getSelectedPanel());
    }
}
