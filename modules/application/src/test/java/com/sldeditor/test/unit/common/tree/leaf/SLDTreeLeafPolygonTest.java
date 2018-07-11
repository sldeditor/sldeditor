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
import com.sldeditor.common.tree.leaf.SLDTreeLeafPolygon;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.PolygonSymbolizerImpl;
import org.junit.jupiter.api.Test;

/**
 * The unit test for SLDTreeLeafPolygon
 *
 * <p>{@link com.sldeditor.common.tree.leaf.SLDTreeLeafPolygon}
 *
 * @author Robert Ward (SCISYS)
 */
public class SLDTreeLeafPolygonTest {

    /**
     * Test method for {@link com.sldeditor.common.tree.leaf.SLDTreeLeafPolygon#getSymbolizer()}.
     */
    @Test
    public void testGetSymbolizer() {
        SLDTreeLeafPolygon leaf = new SLDTreeLeafPolygon();

        assertEquals(PolygonSymbolizerImpl.class, leaf.getSymbolizer());
    }

    /**
     * Test method for {@link
     * com.sldeditor.common.tree.leaf.SLDTreeLeafPolygon#hasFill(org.opengis.style.Symbolizer)}.
     */
    @Test
    public void testHasFill() {
        SLDTreeLeafPolygon leaf = new SLDTreeLeafPolygon();

        assertFalse(leaf.hasFill(null));
        assertFalse(leaf.hasFill(DefaultSymbols.createDefaultPointSymbolizer()));

        PolygonSymbolizer polygonSymbolizer = DefaultSymbols.createDefaultPolygonSymbolizer();
        assertTrue(leaf.hasFill(polygonSymbolizer));

        polygonSymbolizer.setFill(null);
        assertFalse(leaf.hasFill(polygonSymbolizer));
    }

    /**
     * Test method for {@link
     * com.sldeditor.common.tree.leaf.SLDTreeLeafPolygon#hasStroke(org.opengis.style.Symbolizer)}.
     */
    @Test
    public void testHasStroke() {
        SLDTreeLeafPolygon leaf = new SLDTreeLeafPolygon();

        assertFalse(leaf.hasStroke(null));
        assertFalse(leaf.hasStroke(DefaultSymbols.createDefaultPointSymbolizer()));

        PolygonSymbolizer polygonSymbolizer = DefaultSymbols.createDefaultPolygonSymbolizer();
        assertTrue(leaf.hasStroke(polygonSymbolizer));

        polygonSymbolizer.setStroke(null);
        assertFalse(leaf.hasStroke(polygonSymbolizer));
    }

    /**
     * Test method for {@link
     * com.sldeditor.common.tree.leaf.SLDTreeLeafPolygon#getFill(org.opengis.style.Symbolizer)}.
     */
    @Test
    public void testGetFill() {
        SLDTreeLeafPolygon leaf = new SLDTreeLeafPolygon();
        assertNull(leaf.getFill(null));
        assertNull(leaf.getFill(DefaultSymbols.createDefaultPointSymbolizer()));

        PolygonSymbolizer polygonSymbolizer = DefaultSymbols.createDefaultPolygonSymbolizer();
        assertEquals(polygonSymbolizer.getFill(), leaf.getFill(polygonSymbolizer));
    }

    /**
     * Test method for {@link
     * com.sldeditor.common.tree.leaf.SLDTreeLeafPolygon#getStroke(org.opengis.style.Symbolizer)}.
     */
    @Test
    public void testGetStroke() {
        SLDTreeLeafPolygon leaf = new SLDTreeLeafPolygon();
        assertNull(leaf.getStroke(null));
        assertNull(leaf.getStroke(DefaultSymbols.createDefaultPointSymbolizer()));

        PolygonSymbolizer polygonSymbolizer = DefaultSymbols.createDefaultPolygonSymbolizer();
        assertEquals(polygonSymbolizer.getStroke(), leaf.getStroke(polygonSymbolizer));
    }

    /**
     * Test method for {@link
     * com.sldeditor.common.tree.leaf.SLDTreeLeafPolygon#removeStroke(org.opengis.style.Symbolizer)}.
     */
    @Test
    public void testRemoveStroke() {
        SLDTreeLeafPolygon leaf = new SLDTreeLeafPolygon();

        PolygonSymbolizer polygonSymbolizer = DefaultSymbols.createDefaultPolygonSymbolizer();
        leaf.removeStroke(polygonSymbolizer);

        assertNull(polygonSymbolizer.getStroke());
    }

    /**
     * Test method for {@link
     * com.sldeditor.common.tree.leaf.SLDTreeLeafPolygon#createStroke(org.opengis.style.Symbolizer)}.
     */
    @Test
    public void testCreateStroke() {
        SLDTreeLeafPolygon leaf = new SLDTreeLeafPolygon();

        PolygonSymbolizer polygonSymbolizer = DefaultSymbols.createDefaultPolygonSymbolizer();
        polygonSymbolizer.setStroke(null);
        leaf.createStroke(polygonSymbolizer);

        assertTrue(polygonSymbolizer.getStroke() != null);
    }

    /**
     * Test method for {@link
     * com.sldeditor.common.tree.leaf.SLDTreeLeafPolygon#createFill(org.opengis.style.Symbolizer)}.
     */
    @Test
    public void testCreateFill() {
        SLDTreeLeafPolygon leaf = new SLDTreeLeafPolygon();
        PolygonSymbolizer polygonSymbolizer = DefaultSymbols.createDefaultPolygonSymbolizer();
        polygonSymbolizer.setFill(null);
        leaf.createFill(polygonSymbolizer);

        assertTrue(polygonSymbolizer.getFill() != null);
    }

    /**
     * Test method for {@link
     * com.sldeditor.common.tree.leaf.SLDTreeLeafPolygon#removeFill(org.opengis.style.Symbolizer)}.
     */
    @Test
    public void testRemoveFill() {
        SLDTreeLeafPolygon leaf = new SLDTreeLeafPolygon();
        PolygonSymbolizer polygonSymbolizer = DefaultSymbols.createDefaultPolygonSymbolizer();
        leaf.removeFill(polygonSymbolizer);

        assertNull(polygonSymbolizer.getFill());
    }
}
