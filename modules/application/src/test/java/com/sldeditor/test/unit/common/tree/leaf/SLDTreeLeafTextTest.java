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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import org.geotools.styling.TextSymbolizer;
import org.geotools.styling.TextSymbolizerImpl;
import org.junit.Test;

import com.sldeditor.common.defaultsymbol.DefaultSymbols;
import com.sldeditor.common.tree.leaf.SLDTreeLeafText;

/**
 * The unit test for SLDTreeLeafTextTest
 * 
 * <p>{@link com.sldeditor.common.tree.leaf.SLDTreeLeafTextTest}
 * 
 * @author Robert Ward (SCISYS)
 */
public class SLDTreeLeafTextTest {

    /**
     * Test method for {@link com.sldeditor.common.tree.leaf.SLDTreeLeafText#getSymbolizer()}.
     */
    @Test
    public void testGetSymbolizer() {
        SLDTreeLeafText leaf = new SLDTreeLeafText();

        assertEquals(TextSymbolizerImpl.class, leaf.getSymbolizer());
    }

    /**
     * Test method for {@link com.sldeditor.common.tree.leaf.SLDTreeLeafText#hasFill(org.opengis.style.Symbolizer)}.
     */
    @Test
    public void testHasFill() {
        SLDTreeLeafText leaf = new SLDTreeLeafText();

        assertFalse(leaf.hasFill(null));
        assertFalse(leaf.hasFill(DefaultSymbols.createDefaultPointSymbolizer()));
        assertFalse(leaf.hasFill(DefaultSymbols.createDefaultTextSymbolizer()));
    }

    /**
     * Test method for {@link com.sldeditor.common.tree.leaf.SLDTreeLeafText#hasStroke(org.opengis.style.Symbolizer)}.
     */
    @Test
    public void testHasStroke() {
        SLDTreeLeafText leaf = new SLDTreeLeafText();

        assertFalse(leaf.hasStroke(null));
        assertFalse(leaf.hasStroke(DefaultSymbols.createDefaultPointSymbolizer()));
        assertFalse(leaf.hasStroke(DefaultSymbols.createDefaultTextSymbolizer()));
    }

    /**
     * Test method for {@link com.sldeditor.common.tree.leaf.SLDTreeLeafText#getFill(org.opengis.style.Symbolizer)}.
     */
    @Test
    public void testGetFill() {
        SLDTreeLeafText leaf = new SLDTreeLeafText();
        assertNull(leaf.getFill(null));
        assertNull(leaf.getFill(DefaultSymbols.createDefaultPointSymbolizer()));
        assertNull(leaf.getFill(DefaultSymbols.createDefaultTextSymbolizer()));
    }

    /**
     * Test method for {@link com.sldeditor.common.tree.leaf.SLDTreeLeafText#getStroke(org.opengis.style.Symbolizer)}.
     */
    @Test
    public void testGetStroke() {
        SLDTreeLeafText leaf = new SLDTreeLeafText();
        assertNull(leaf.getStroke(null));
        assertNull(leaf.getStroke(DefaultSymbols.createDefaultPointSymbolizer()));
        assertNull(leaf.getStroke(DefaultSymbols.createDefaultTextSymbolizer()));
    }

    /**
     * Test method for {@link com.sldeditor.common.tree.leaf.SLDTreeLeafText#removeStroke(org.opengis.style.Symbolizer)}.
     */
    @Test
    public void testRemoveStroke() {
        SLDTreeLeafText leaf = new SLDTreeLeafText();

        TextSymbolizer textSymbolizer = DefaultSymbols.createDefaultTextSymbolizer();
        leaf.removeStroke(textSymbolizer);

        // Does nothing
    }

    /**
     * Test method for {@link com.sldeditor.common.tree.leaf.SLDTreeLeafText#createStroke(org.opengis.style.Symbolizer)}.
     */
    @Test
    public void testCreateStroke() {
        SLDTreeLeafText leaf = new SLDTreeLeafText();

        TextSymbolizer textSymbolizer = DefaultSymbols.createDefaultTextSymbolizer();
        leaf.createStroke(textSymbolizer);

        // Does nothing
    }

    /**
     * Test method for {@link com.sldeditor.common.tree.leaf.SLDTreeLeafText#createFill(org.opengis.style.Symbolizer)}.
     */
    @Test
    public void testCreateFill() {
        SLDTreeLeafText leaf = new SLDTreeLeafText();
        TextSymbolizer textSymbolizer = DefaultSymbols.createDefaultTextSymbolizer();
        leaf.createFill(textSymbolizer);

        // Does nothing
    }

    /**
     * Test method for {@link com.sldeditor.common.tree.leaf.SLDTreeLeafText#removeFill(org.opengis.style.Symbolizer)}.
     */
    @Test
    public void testRemoveFill() {
        SLDTreeLeafText leaf = new SLDTreeLeafText();
        TextSymbolizer textSymbolizer = DefaultSymbols.createDefaultTextSymbolizer();
        leaf.removeFill(textSymbolizer);

        // Does nothing
    }

}
