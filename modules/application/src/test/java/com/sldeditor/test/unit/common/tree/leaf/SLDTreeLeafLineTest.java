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
import com.sldeditor.common.tree.leaf.SLDTreeLeafLine;
import org.geotools.styling.LineSymbolizer;
import org.geotools.styling.LineSymbolizerImpl;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.Stroke;
import org.junit.jupiter.api.Test;

/**
 * The unit test for SLDTreeLeafLine
 *
 * <p>{@link com.sldeditor.common.tree.leaf.SLDTreeLeafLine}
 *
 * @author Robert Ward (SCISYS)
 */
public class SLDTreeLeafLineTest {

    /** Test method for {@link com.sldeditor.common.tree.leaf.SLDTreeLeafLine#getSymbolizer()}. */
    @Test
    public void testGetSymbolizer() {
        SLDTreeLeafLine leaf = new SLDTreeLeafLine();

        assertEquals(LineSymbolizerImpl.class, leaf.getSymbolizer());
    }

    /**
     * Test method for {@link
     * com.sldeditor.common.tree.leaf.SLDTreeLeafLine#hasFill(org.opengis.style.Symbolizer)}.
     */
    @Test
    public void testHasFill() {
        SLDTreeLeafLine leaf = new SLDTreeLeafLine();
        assertFalse(leaf.hasFill(null));
    }

    /**
     * Test method for {@link
     * com.sldeditor.common.tree.leaf.SLDTreeLeafLine#hasStroke(org.opengis.style.Symbolizer)}.
     */
    @Test
    public void testHasStroke() {
        SLDTreeLeafLine leaf = new SLDTreeLeafLine();
        assertTrue(leaf.hasStroke(null));
    }

    /**
     * Test method for {@link
     * com.sldeditor.common.tree.leaf.SLDTreeLeafLine#getFill(org.opengis.style.Symbolizer)}.
     */
    @Test
    public void testGetFill() {
        SLDTreeLeafLine leaf = new SLDTreeLeafLine();
        assertNull(leaf.getFill(null));
    }

    /**
     * Test method for {@link
     * com.sldeditor.common.tree.leaf.SLDTreeLeafLine#getStroke(org.opengis.style.Symbolizer)}.
     */
    @Test
    public void testGetStroke() {
        SLDTreeLeafLine leaf = new SLDTreeLeafLine();
        assertNull(leaf.getStroke(null));

        PolygonSymbolizer polygonSymbolizer = DefaultSymbols.createDefaultPolygonSymbolizer();
        assertNull(leaf.getStroke(polygonSymbolizer));

        LineSymbolizer lineSymbolizer = DefaultSymbols.createDefaultLineSymbolizer();
        Stroke stroke = leaf.getStroke(lineSymbolizer);

        assertEquals(stroke, lineSymbolizer.getStroke());
    }

    /**
     * Test method for {@link
     * com.sldeditor.common.tree.leaf.SLDTreeLeafLine#removeStroke(org.opengis.style.Symbolizer)}.
     */
    @Test
    public void testRemoveStroke() {
        SLDTreeLeafLine leaf = new SLDTreeLeafLine();

        LineSymbolizer lineSymbolizer = DefaultSymbols.createDefaultLineSymbolizer();
        leaf.removeStroke(lineSymbolizer);

        assertNull(lineSymbolizer.getStroke());
    }

    /**
     * Test method for {@link
     * com.sldeditor.common.tree.leaf.SLDTreeLeafLine#createStroke(org.opengis.style.Symbolizer)}.
     */
    @Test
    public void testCreateStroke() {
        SLDTreeLeafLine leaf = new SLDTreeLeafLine();

        LineSymbolizer lineSymbolizer = DefaultSymbols.createDefaultLineSymbolizer();
        lineSymbolizer.setStroke(null);
        leaf.createStroke(lineSymbolizer);

        assertTrue(lineSymbolizer.getStroke() != null);
    }

    /**
     * Test method for {@link
     * com.sldeditor.common.tree.leaf.SLDTreeLeafLine#createFill(org.opengis.style.Symbolizer)}.
     */
    @Test
    public void testCreateFill() {
        SLDTreeLeafLine leaf = new SLDTreeLeafLine();
        leaf.createFill(null);

        // Does nothing
    }

    /**
     * Test method for {@link
     * com.sldeditor.common.tree.leaf.SLDTreeLeafLine#removeFill(org.opengis.style.Symbolizer)}.
     */
    @Test
    public void testRemoveFill() {
        SLDTreeLeafLine leaf = new SLDTreeLeafLine();
        leaf.removeFill(null);

        // Does nothing
    }
}
