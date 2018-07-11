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

package com.sldeditor.test.unit.common.tree.leaf;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sldeditor.common.defaultsymbol.DefaultSymbols;
import com.sldeditor.common.tree.leaf.SLDTreeLeafFactory;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.styling.LineSymbolizer;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.StyleFactoryImpl;
import org.geotools.styling.TextSymbolizer;
import org.junit.jupiter.api.Test;

/**
 * Unit test for SLDTreeLeafFactory
 *
 * <p>{@link com.sldeditor.common.tree.leaf.SLDTreeLeafFactory}
 *
 * @author Robert Ward (SCISYS)
 */
public class SLDTreeLeafFactoryTest {

    private static StyleFactoryImpl styleFactory =
            (StyleFactoryImpl) CommonFactoryFinder.getStyleFactory();

    /**
     * Test method for {@link
     * com.sldeditor.common.tree.leaf.SLDTreeLeafFactory#hasFill(org.opengis.style.Symbolizer)}.
     */
    @Test
    public void testHasFill() {
        PolygonSymbolizer polygonSymbolizer = DefaultSymbols.createDefaultPolygonSymbolizer();
        PointSymbolizer pointSymbolizer = DefaultSymbols.createDefaultPointSymbolizer();
        // CHECKSTYLE:OFF
        LineSymbolizer lineSymbolizer = DefaultSymbols.createDefaultLineSymbolizer();
        TextSymbolizer textSymbolizer = DefaultSymbols.createDefaultTextSymbolizer();
        // CHECKSTYLE:ON

        assertFalse(SLDTreeLeafFactory.getInstance().hasFill(null));
        assertTrue(SLDTreeLeafFactory.getInstance().hasFill(polygonSymbolizer));
        assertTrue(SLDTreeLeafFactory.getInstance().hasFill(pointSymbolizer));
        assertFalse(SLDTreeLeafFactory.getInstance().hasFill(lineSymbolizer));
        assertFalse(SLDTreeLeafFactory.getInstance().hasFill(textSymbolizer));
    }

    /**
     * Test method for {@link
     * com.sldeditor.common.tree.leaf.SLDTreeLeafFactory#hasStroke(org.opengis.style.Symbolizer)}.
     */
    @Test
    public void testHasStroke() {
        PolygonSymbolizer polygonSymbolizer = DefaultSymbols.createDefaultPolygonSymbolizer();
        PointSymbolizer pointSymbolizer = DefaultSymbols.createDefaultPointSymbolizer();
        // CHECKSTYLE:OFF
        LineSymbolizer lineSymbolizer = DefaultSymbols.createDefaultLineSymbolizer();
        TextSymbolizer textSymbolizer = DefaultSymbols.createDefaultTextSymbolizer();
        // CHECKSTYLE:ON

        assertFalse(SLDTreeLeafFactory.getInstance().hasStroke(null));
        assertTrue(SLDTreeLeafFactory.getInstance().hasStroke(polygonSymbolizer));
        assertFalse(SLDTreeLeafFactory.getInstance().hasStroke(pointSymbolizer));
        assertTrue(SLDTreeLeafFactory.getInstance().hasStroke(lineSymbolizer));
        assertFalse(SLDTreeLeafFactory.getInstance().hasStroke(textSymbolizer));
    }

    /**
     * Test method for {@link
     * com.sldeditor.common.tree.leaf.SLDTreeLeafFactory#getFill(org.opengis.style.Symbolizer)}.
     */
    @Test
    public void testGetFill() {
        PolygonSymbolizer polygonSymbolizer = DefaultSymbols.createDefaultPolygonSymbolizer();
        PointSymbolizer pointSymbolizer = DefaultSymbols.createDefaultPointSymbolizer();
        // CHECKSTYLE:OFF
        LineSymbolizer lineSymbolizer = DefaultSymbols.createDefaultLineSymbolizer();
        TextSymbolizer textSymbolizer = DefaultSymbols.createDefaultTextSymbolizer();
        // CHECKSTYLE:ON

        assertNull(SLDTreeLeafFactory.getInstance().getFill(null));
        assertTrue(SLDTreeLeafFactory.getInstance().getFill(polygonSymbolizer) != null);
        assertTrue(SLDTreeLeafFactory.getInstance().getFill(pointSymbolizer) != null);

        // Returns default fill
        assertTrue(SLDTreeLeafFactory.getInstance().getFill(lineSymbolizer) != null);
        assertTrue(SLDTreeLeafFactory.getInstance().getFill(textSymbolizer) != null);
    }

    /**
     * Test method for {@link
     * com.sldeditor.common.tree.leaf.SLDTreeLeafFactory#getStroke(org.opengis.style.Symbolizer)}.
     */
    @Test
    public void testGetStroke() {
        PolygonSymbolizer polygonSymbolizer = DefaultSymbols.createDefaultPolygonSymbolizer();
        // CHECKSTYLE:OFF
        PointSymbolizer pointSymbolizer = DefaultSymbols.createDefaultPointSymbolizer();
        LineSymbolizer lineSymbolizer = DefaultSymbols.createDefaultLineSymbolizer();
        TextSymbolizer textSymbolizer = DefaultSymbols.createDefaultTextSymbolizer();
        // CHECKSTYLE:ON

        assertNull(SLDTreeLeafFactory.getInstance().getStroke(null));
        assertTrue(SLDTreeLeafFactory.getInstance().getStroke(polygonSymbolizer) != null);
        assertTrue(SLDTreeLeafFactory.getInstance().getStroke(lineSymbolizer) != null);

        // Returns default stroke
        assertTrue(SLDTreeLeafFactory.getInstance().getStroke(pointSymbolizer) != null);
        assertTrue(SLDTreeLeafFactory.getInstance().getStroke(textSymbolizer) != null);
    }

    /**
     * Test method for {@link
     * com.sldeditor.common.tree.leaf.SLDTreeLeafFactory#updateStroke(boolean,
     * org.opengis.style.Symbolizer)}.
     */
    @Test
    public void testUpdateStroke() {
        assertNull(SLDTreeLeafFactory.getInstance().updateStroke(true, null));

        PolygonSymbolizer polygonSymbolizer = DefaultSymbols.createDefaultPolygonSymbolizer();
        assertEquals(
                SLDTreeLeafFactory.getInstance().updateStroke(false, polygonSymbolizer),
                styleFactory.getDefaultStroke());
        assertTrue(SLDTreeLeafFactory.getInstance().updateStroke(true, polygonSymbolizer) != null);

        PointSymbolizer pointSymbolizer = DefaultSymbols.createDefaultPointSymbolizer();
        assertEquals(
                SLDTreeLeafFactory.getInstance().updateStroke(false, pointSymbolizer),
                styleFactory.getDefaultStroke());
        assertTrue(SLDTreeLeafFactory.getInstance().updateStroke(true, pointSymbolizer) != null);

        LineSymbolizer lineSymbolizer = DefaultSymbols.createDefaultLineSymbolizer();
        assertEquals(
                SLDTreeLeafFactory.getInstance().updateStroke(false, lineSymbolizer),
                styleFactory.getDefaultStroke());
        assertEquals(
                SLDTreeLeafFactory.getInstance().updateStroke(true, lineSymbolizer),
                styleFactory.getDefaultStroke());

        TextSymbolizer textSymbolizer = DefaultSymbols.createDefaultTextSymbolizer();
        assertEquals(
                SLDTreeLeafFactory.getInstance().updateStroke(false, textSymbolizer),
                styleFactory.getDefaultStroke());
        assertEquals(
                SLDTreeLeafFactory.getInstance().updateStroke(true, textSymbolizer),
                styleFactory.getDefaultStroke());
    }

    /**
     * Test method for {@link com.sldeditor.common.tree.leaf.SLDTreeLeafFactory#updateFill(boolean,
     * org.opengis.style.Symbolizer)}.
     */
    @Test
    public void testUpdateFill() {
        assertNull(SLDTreeLeafFactory.getInstance().updateFill(true, null));

        PolygonSymbolizer polygonSymbolizer = DefaultSymbols.createDefaultPolygonSymbolizer();
        assertEquals(
                SLDTreeLeafFactory.getInstance().updateFill(false, polygonSymbolizer),
                styleFactory.getDefaultFill());
        assertTrue(SLDTreeLeafFactory.getInstance().updateFill(true, polygonSymbolizer) != null);

        PointSymbolizer pointSymbolizer = DefaultSymbols.createDefaultPointSymbolizer();
        assertEquals(
                SLDTreeLeafFactory.getInstance().updateFill(false, pointSymbolizer),
                styleFactory.getDefaultFill());
        assertTrue(SLDTreeLeafFactory.getInstance().updateFill(true, pointSymbolizer) != null);

        LineSymbolizer lineSymbolizer = DefaultSymbols.createDefaultLineSymbolizer();
        assertEquals(
                SLDTreeLeafFactory.getInstance().updateFill(false, lineSymbolizer),
                styleFactory.getDefaultFill());
        assertEquals(
                SLDTreeLeafFactory.getInstance().updateFill(true, lineSymbolizer),
                styleFactory.getDefaultFill());

        TextSymbolizer textSymbolizer = DefaultSymbols.createDefaultTextSymbolizer();
        assertEquals(
                SLDTreeLeafFactory.getInstance().updateFill(false, textSymbolizer),
                styleFactory.getDefaultFill());
        assertEquals(
                SLDTreeLeafFactory.getInstance().updateFill(true, textSymbolizer),
                styleFactory.getDefaultFill());
    }

    /**
     * Test method for {@link
     * com.sldeditor.common.tree.leaf.SLDTreeLeafFactory#isItemSelected(java.lang.Object,
     * org.geotools.styling.Symbolizer)}.
     */
    @Test
    public void testIsItemSelected() {
        assertFalse(SLDTreeLeafFactory.getInstance().isItemSelected(null, null));

        PolygonSymbolizer polygonSymbolizer = DefaultSymbols.createDefaultPolygonSymbolizer();
        assertTrue(
                SLDTreeLeafFactory.getInstance()
                        .isItemSelected(styleFactory.getDefaultFill(), polygonSymbolizer));
        assertTrue(
                SLDTreeLeafFactory.getInstance()
                        .isItemSelected(styleFactory.getDefaultStroke(), polygonSymbolizer));

        PointSymbolizer pointSymbolizer = DefaultSymbols.createDefaultPointSymbolizer();
        assertTrue(
                SLDTreeLeafFactory.getInstance()
                        .isItemSelected(styleFactory.getDefaultFill(), pointSymbolizer));
        assertFalse(
                SLDTreeLeafFactory.getInstance()
                        .isItemSelected(styleFactory.getDefaultStroke(), pointSymbolizer));

        LineSymbolizer lineSymbolizer = DefaultSymbols.createDefaultLineSymbolizer();
        assertFalse(
                SLDTreeLeafFactory.getInstance()
                        .isItemSelected(styleFactory.getDefaultFill(), lineSymbolizer));
        assertTrue(
                SLDTreeLeafFactory.getInstance()
                        .isItemSelected(styleFactory.getDefaultStroke(), lineSymbolizer));

        TextSymbolizer textSymbolizer = DefaultSymbols.createDefaultTextSymbolizer();
        assertFalse(
                SLDTreeLeafFactory.getInstance()
                        .isItemSelected(styleFactory.getDefaultFill(), textSymbolizer));
        assertFalse(
                SLDTreeLeafFactory.getInstance()
                        .isItemSelected(styleFactory.getDefaultStroke(), textSymbolizer));
    }
}
