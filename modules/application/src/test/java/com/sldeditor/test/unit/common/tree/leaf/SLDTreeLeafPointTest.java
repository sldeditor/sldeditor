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
import static org.junit.Assert.assertTrue;

import com.sldeditor.common.defaultsymbol.DefaultSymbols;
import com.sldeditor.common.tree.leaf.SLDTreeLeafPoint;
import java.util.List;
import org.geotools.styling.Fill;
import org.geotools.styling.Graphic;
import org.geotools.styling.MarkImpl;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.PointSymbolizerImpl;
import org.junit.Test;
import org.opengis.style.GraphicalSymbol;

/**
 * The unit test for SLDTreeLeafPoint
 *
 * <p>{@link com.sldeditor.common.tree.leaf.SLDTreeLeafPoint}
 *
 * @author Robert Ward (SCISYS)
 */
public class SLDTreeLeafPointTest {

    /** Test method for {@link com.sldeditor.common.tree.leaf.SLDTreeLeafPoint#getSymbolizer()}. */
    @Test
    public void testGetSymbolizer() {
        SLDTreeLeafPoint leaf = new SLDTreeLeafPoint();

        assertEquals(PointSymbolizerImpl.class, leaf.getSymbolizer());
    }

    /**
     * Test method for {@link
     * com.sldeditor.common.tree.leaf.SLDTreeLeafPoint#hasFill(org.opengis.style.Symbolizer)}.
     */
    @Test
    public void testHasFill() {
        SLDTreeLeafPoint leaf = new SLDTreeLeafPoint();

        assertFalse(leaf.hasFill(null));
        assertFalse(leaf.hasFill(DefaultSymbols.createDefaultPolygonSymbolizer()));

        PointSymbolizer pointSymbolizer = DefaultSymbols.createDefaultPointSymbolizer();
        assertTrue(leaf.hasFill(pointSymbolizer));

        pointSymbolizer.setGraphic(null);
        assertFalse(leaf.hasFill(pointSymbolizer));
    }

    /**
     * Test method for {@link
     * com.sldeditor.common.tree.leaf.SLDTreeLeafPoint#hasStroke(org.opengis.style.Symbolizer)}.
     */
    @Test
    public void testHasStroke() {
        SLDTreeLeafPoint leaf = new SLDTreeLeafPoint();

        assertFalse(leaf.hasStroke(null));
        assertFalse(leaf.hasStroke(DefaultSymbols.createDefaultPointSymbolizer()));

        PointSymbolizer pointSymbolizer = DefaultSymbols.createDefaultPointSymbolizer();
        assertFalse(leaf.hasStroke(pointSymbolizer));
    }

    /**
     * Test method for {@link
     * com.sldeditor.common.tree.leaf.SLDTreeLeafPoint#getFill(org.opengis.style.Symbolizer)}.
     */
    @Test
    public void testGetFill() {
        SLDTreeLeafPoint leaf = new SLDTreeLeafPoint();
        assertNull(leaf.getFill(null));
        assertNull(leaf.getFill(DefaultSymbols.createDefaultPolygonSymbolizer()));

        PointSymbolizer pointSymbolizer = DefaultSymbols.createDefaultPointSymbolizer();

        Fill expectedFill = null;
        Graphic graphic = pointSymbolizer.getGraphic();

        if (graphic != null) {
            List<GraphicalSymbol> symbolList = graphic.graphicalSymbols();

            if ((symbolList != null) && !symbolList.isEmpty()) {
                GraphicalSymbol obj = symbolList.get(0);

                if (obj != null) {
                    if (obj instanceof MarkImpl) {
                        MarkImpl mark = (MarkImpl) obj;

                        expectedFill = mark.getFill();
                    }
                }
            }
        }

        assertEquals(expectedFill, leaf.getFill(pointSymbolizer));
    }

    /**
     * Test method for {@link
     * com.sldeditor.common.tree.leaf.SLDTreeLeafPoint#getStroke(org.opengis.style.Symbolizer)}.
     */
    @Test
    public void testGetStroke() {
        SLDTreeLeafPoint leaf = new SLDTreeLeafPoint();
        assertNull(leaf.getStroke(null));
        assertNull(leaf.getStroke(DefaultSymbols.createDefaultPointSymbolizer()));

        PointSymbolizer pointSymbolizer = DefaultSymbols.createDefaultPointSymbolizer();
        assertNull(leaf.getStroke(pointSymbolizer));
    }

    /**
     * Test method for {@link
     * com.sldeditor.common.tree.leaf.SLDTreeLeafPoint#removeStroke(org.opengis.style.Symbolizer)}.
     */
    @Test
    public void testRemoveStroke() {
        SLDTreeLeafPoint leaf = new SLDTreeLeafPoint();

        PointSymbolizer pointSymbolizer = DefaultSymbols.createDefaultPointSymbolizer();
        leaf.removeStroke(pointSymbolizer);

        // Does nothing
    }

    /**
     * Test method for {@link
     * com.sldeditor.common.tree.leaf.SLDTreeLeafPoint#createStroke(org.opengis.style.Symbolizer)}.
     */
    @Test
    public void testCreateStroke() {
        SLDTreeLeafPoint leaf = new SLDTreeLeafPoint();

        PointSymbolizer pointSymbolizer = DefaultSymbols.createDefaultPointSymbolizer();
        leaf.removeStroke(pointSymbolizer);

        // Does nothing
    }

    /**
     * Test method for {@link
     * com.sldeditor.common.tree.leaf.SLDTreeLeafPoint#createFill(org.opengis.style.Symbolizer)}.
     */
    @Test
    public void testCreateFill() {
        SLDTreeLeafPoint leaf = new SLDTreeLeafPoint();
        PointSymbolizer pointSymbolizer = DefaultSymbols.createDefaultPointSymbolizer();
        pointSymbolizer.setGraphic(null);

        leaf.createFill(pointSymbolizer);

        assertTrue(pointSymbolizer.getGraphic() != null);
    }

    /**
     * Test method for {@link
     * com.sldeditor.common.tree.leaf.SLDTreeLeafPoint#removeFill(org.opengis.style.Symbolizer)}.
     */
    @Test
    public void testRemoveFill() {
        SLDTreeLeafPoint leaf = new SLDTreeLeafPoint();
        PointSymbolizer pointSymbolizer = DefaultSymbols.createDefaultPointSymbolizer();
        leaf.removeFill(pointSymbolizer);

        assertNull(pointSymbolizer.getGraphic());
    }
}
