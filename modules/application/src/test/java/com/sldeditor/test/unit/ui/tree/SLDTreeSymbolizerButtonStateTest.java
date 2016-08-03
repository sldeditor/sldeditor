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

package com.sldeditor.test.unit.ui.tree;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.geotools.styling.RasterSymbolizer;
import org.geotools.styling.Rule;
import org.junit.Test;

import com.sldeditor.common.defaultsymbol.DefaultSymbols;
import com.sldeditor.datasource.impl.GeometryTypeEnum;
import com.sldeditor.ui.tree.SLDTreeSymbolizerButtonState;

/**
 * The unit test for SLDTreeSymbolizerButtonState.
 * <p>{@link com.sldeditor.ui.tree.SLDTreeSymbolizerButtonState}
 *
 * @author Robert Ward (SCISYS)
 */
public class SLDTreeSymbolizerButtonStateTest {

    /**
     * Test method for {@link com.sldeditor.ui.tree.SLDTreeSymbolizerButtonState#setGeometryType(com.sldeditor.datasource.impl.GeometryTypeEnum)}.
     * Test method for {@link com.sldeditor.ui.tree.SLDTreeSymbolizerButtonState#isMarkerVisible(java.lang.Object, java.lang.Object)}.
     * Test method for {@link com.sldeditor.ui.tree.SLDTreeSymbolizerButtonState#isLineVisible(java.lang.Object, java.lang.Object)}.
     * Test method for {@link com.sldeditor.ui.tree.SLDTreeSymbolizerButtonState#isPolygonVisible(java.lang.Object, java.lang.Object)}.
     * Test method for {@link com.sldeditor.ui.tree.SLDTreeSymbolizerButtonState#isRasterVisible(java.lang.Object, java.lang.Object)}.
     * Test method for {@link com.sldeditor.ui.tree.SLDTreeSymbolizerButtonState#isTextVisible(java.lang.Object, java.lang.Object)}.
     * Test method for {@link com.sldeditor.ui.tree.SLDTreeSymbolizerButtonState#isImageOutlineLineVisible(java.lang.Object, java.lang.Object)}.
     * Test method for {@link com.sldeditor.ui.tree.SLDTreeSymbolizerButtonState#isImageOutlinePolygonVisible(java.lang.Object, java.lang.Object)}.
     */
    @Test
    public void testMarker() {
        SLDTreeSymbolizerButtonState obj = new SLDTreeSymbolizerButtonState();

        obj.setGeometryType(GeometryTypeEnum.POINT);
        obj.showSymbolizerButtons();
        assertFalse(obj.isLineVisible(null, null));
        assertFalse(obj.isMarkerVisible(null, null));
        assertFalse(obj.isPolygonVisible(null, null));
        assertFalse(obj.isRasterVisible(null, null));
        assertFalse(obj.isTextVisible(null, null));
        assertFalse(obj.isImageOutlineLineVisible(null, null));
        assertFalse(obj.isImageOutlinePolygonVisible(null, null));

        Rule rule = DefaultSymbols.createNewRule();
        assertTrue(obj.isMarkerVisible(null, rule));
        assertTrue(obj.isTextVisible(null, rule));
    }

    /**
     * Test method for {@link com.sldeditor.ui.tree.SLDTreeSymbolizerButtonState#setGeometryType(com.sldeditor.datasource.impl.GeometryTypeEnum)}.
     * Test method for {@link com.sldeditor.ui.tree.SLDTreeSymbolizerButtonState#isMarkerVisible(java.lang.Object, java.lang.Object)}.
     * Test method for {@link com.sldeditor.ui.tree.SLDTreeSymbolizerButtonState#isLineVisible(java.lang.Object, java.lang.Object)}.
     * Test method for {@link com.sldeditor.ui.tree.SLDTreeSymbolizerButtonState#isPolygonVisible(java.lang.Object, java.lang.Object)}.
     * Test method for {@link com.sldeditor.ui.tree.SLDTreeSymbolizerButtonState#isRasterVisible(java.lang.Object, java.lang.Object)}.
     * Test method for {@link com.sldeditor.ui.tree.SLDTreeSymbolizerButtonState#isTextVisible(java.lang.Object, java.lang.Object)}.
     * Test method for {@link com.sldeditor.ui.tree.SLDTreeSymbolizerButtonState#isImageOutlineLineVisible(java.lang.Object, java.lang.Object)}.
     * Test method for {@link com.sldeditor.ui.tree.SLDTreeSymbolizerButtonState#isImageOutlinePolygonVisible(java.lang.Object, java.lang.Object)}.
     */
    @Test
    public void testLine() {
        SLDTreeSymbolizerButtonState obj = new SLDTreeSymbolizerButtonState();

        obj.setGeometryType(GeometryTypeEnum.LINE);
        obj.showSymbolizerButtons();
        assertFalse(obj.isLineVisible(null, null));
        assertFalse(obj.isMarkerVisible(null, null));
        assertFalse(obj.isPolygonVisible(null, null));
        assertFalse(obj.isRasterVisible(null, null));
        assertFalse(obj.isTextVisible(null, null));
        assertFalse(obj.isImageOutlineLineVisible(null, null));
        assertFalse(obj.isImageOutlinePolygonVisible(null, null));

        Rule rule = DefaultSymbols.createNewRule();
        assertTrue(obj.isLineVisible(null, rule));
        assertTrue(obj.isTextVisible(null, rule));
    }

    /**
     * Test method for {@link com.sldeditor.ui.tree.SLDTreeSymbolizerButtonState#setGeometryType(com.sldeditor.datasource.impl.GeometryTypeEnum)}.
     * Test method for {@link com.sldeditor.ui.tree.SLDTreeSymbolizerButtonState#isMarkerVisible(java.lang.Object, java.lang.Object)}.
     * Test method for {@link com.sldeditor.ui.tree.SLDTreeSymbolizerButtonState#isLineVisible(java.lang.Object, java.lang.Object)}.
     * Test method for {@link com.sldeditor.ui.tree.SLDTreeSymbolizerButtonState#isPolygonVisible(java.lang.Object, java.lang.Object)}.
     * Test method for {@link com.sldeditor.ui.tree.SLDTreeSymbolizerButtonState#isRasterVisible(java.lang.Object, java.lang.Object)}.
     * Test method for {@link com.sldeditor.ui.tree.SLDTreeSymbolizerButtonState#isTextVisible(java.lang.Object, java.lang.Object)}.
     * Test method for {@link com.sldeditor.ui.tree.SLDTreeSymbolizerButtonState#isImageOutlineLineVisible(java.lang.Object, java.lang.Object)}.
     * Test method for {@link com.sldeditor.ui.tree.SLDTreeSymbolizerButtonState#isImageOutlinePolygonVisible(java.lang.Object, java.lang.Object)}.
     */
    @Test
    public void testPolygon() {
        SLDTreeSymbolizerButtonState obj = new SLDTreeSymbolizerButtonState();

        obj.setGeometryType(GeometryTypeEnum.POLYGON);
        obj.showSymbolizerButtons();
        assertFalse(obj.isLineVisible(null, null));
        assertFalse(obj.isMarkerVisible(null, null));
        assertFalse(obj.isPolygonVisible(null, null));
        assertFalse(obj.isRasterVisible(null, null));
        assertFalse(obj.isTextVisible(null, null));
        assertFalse(obj.isImageOutlineLineVisible(null, null));
        assertFalse(obj.isImageOutlinePolygonVisible(null, null));

        Rule rule = DefaultSymbols.createNewRule();
        assertTrue(obj.isPolygonVisible(null, rule));
        assertTrue(obj.isTextVisible(null, rule));
    }

    /**
     * Test method for {@link com.sldeditor.ui.tree.SLDTreeSymbolizerButtonState#setGeometryType(com.sldeditor.datasource.impl.GeometryTypeEnum)}.
     * Test method for {@link com.sldeditor.ui.tree.SLDTreeSymbolizerButtonState#isMarkerVisible(java.lang.Object, java.lang.Object)}.
     * Test method for {@link com.sldeditor.ui.tree.SLDTreeSymbolizerButtonState#isLineVisible(java.lang.Object, java.lang.Object)}.
     * Test method for {@link com.sldeditor.ui.tree.SLDTreeSymbolizerButtonState#isPolygonVisible(java.lang.Object, java.lang.Object)}.
     * Test method for {@link com.sldeditor.ui.tree.SLDTreeSymbolizerButtonState#isRasterVisible(java.lang.Object, java.lang.Object)}.
     * Test method for {@link com.sldeditor.ui.tree.SLDTreeSymbolizerButtonState#isTextVisible(java.lang.Object, java.lang.Object)}.
     * Test method for {@link com.sldeditor.ui.tree.SLDTreeSymbolizerButtonState#isImageOutlineLineVisible(java.lang.Object, java.lang.Object)}.
     * Test method for {@link com.sldeditor.ui.tree.SLDTreeSymbolizerButtonState#isImageOutlinePolygonVisible(java.lang.Object, java.lang.Object)}.
     */
    @Test
    public void testRaster() {
        SLDTreeSymbolizerButtonState obj = new SLDTreeSymbolizerButtonState();

        obj.setGeometryType(GeometryTypeEnum.RASTER);
        obj.showSymbolizerButtons();
        assertFalse(obj.isLineVisible(null, null));
        assertFalse(obj.isMarkerVisible(null, null));
        assertFalse(obj.isPolygonVisible(null, null));
        assertFalse(obj.isRasterVisible(null, null));
        assertFalse(obj.isTextVisible(null, null));
        assertFalse(obj.isImageOutlineLineVisible(null, null));
        assertFalse(obj.isImageOutlinePolygonVisible(null, null));

        Rule rule = DefaultSymbols.createNewRule();
        assertTrue(obj.isRasterVisible(null, rule));
        assertTrue(obj.isTextVisible(null, rule));

        RasterSymbolizer raster = DefaultSymbols.createDefaultRasterSymbolizer();
        assertTrue(obj.isImageOutlineLineVisible(null, raster));
        assertTrue(obj.isImageOutlinePolygonVisible(null, raster));

        raster.setImageOutline(DefaultSymbols.createDefaultLineSymbolizer());
        assertFalse(obj.isImageOutlineLineVisible(null, raster));
        assertFalse(obj.isImageOutlinePolygonVisible(null, raster));

        raster.setImageOutline(DefaultSymbols.createDefaultPolygonSymbolizer());
        assertFalse(obj.isImageOutlineLineVisible(null, raster));
        assertFalse(obj.isImageOutlinePolygonVisible(null, raster));
    }

    /**
     * Test method for {@link com.sldeditor.ui.tree.SLDTreeSymbolizerButtonState#showSymbolizerButtons()}.
     * Test method for {@link com.sldeditor.ui.tree.SLDTreeSymbolizerButtonState#isMarkerVisible(java.lang.Object, java.lang.Object)}.
     * Test method for {@link com.sldeditor.ui.tree.SLDTreeSymbolizerButtonState#isLineVisible(java.lang.Object, java.lang.Object)}.
     * Test method for {@link com.sldeditor.ui.tree.SLDTreeSymbolizerButtonState#isPolygonVisible(java.lang.Object, java.lang.Object)}.
     * Test method for {@link com.sldeditor.ui.tree.SLDTreeSymbolizerButtonState#isRasterVisible(java.lang.Object, java.lang.Object)}.
     * Test method for {@link com.sldeditor.ui.tree.SLDTreeSymbolizerButtonState#isTextVisible(java.lang.Object, java.lang.Object)}.
     * Test method for {@link com.sldeditor.ui.tree.SLDTreeSymbolizerButtonState#isImageOutlineLineVisible(java.lang.Object, java.lang.Object)}.
     * Test method for {@link com.sldeditor.ui.tree.SLDTreeSymbolizerButtonState#isImageOutlinePolygonVisible(java.lang.Object, java.lang.Object)}.
     */
    @Test
    public void testDefaultState() {
        SLDTreeSymbolizerButtonState obj = new SLDTreeSymbolizerButtonState();

        // Symbolizers override flag is false so nothing is displayed
        assertFalse(obj.isLineVisible(null, null));
        assertFalse(obj.isMarkerVisible(null, null));
        assertFalse(obj.isPolygonVisible(null, null));
        assertFalse(obj.isRasterVisible(null, null));
        assertFalse(obj.isTextVisible(null, null));
        assertFalse(obj.isImageOutlineLineVisible(null, null));
        assertFalse(obj.isImageOutlinePolygonVisible(null, null));

        // Symbolizers override flag is true so nothing is displayed as no geometry set
        obj.showSymbolizerButtons();
        assertFalse(obj.isLineVisible(null, null));
        assertFalse(obj.isMarkerVisible(null, null));
        assertFalse(obj.isPolygonVisible(null, null));
        assertFalse(obj.isRasterVisible(null, null));
        assertFalse(obj.isTextVisible(null, null));
        assertFalse(obj.isImageOutlineLineVisible(null, null));
        assertFalse(obj.isImageOutlinePolygonVisible(null, null));
    }
}
