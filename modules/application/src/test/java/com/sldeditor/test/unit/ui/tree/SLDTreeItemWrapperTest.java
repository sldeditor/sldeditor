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

package com.sldeditor.test.unit.ui.tree;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.geotools.styling.HaloImpl;
import org.geotools.styling.TextSymbolizer;
import org.junit.Test;

import com.sldeditor.common.defaultsymbol.DefaultSymbols;
import com.sldeditor.ui.tree.SLDTreeItemWrapper;

/**
 * The unit test for SLDTreeItemWrapper.
 * <p>{@link com.sldeditor.ui.tree.SLDTreeItemWrapper}
 *
 * @author Robert Ward (SCISYS)
 */
public class SLDTreeItemWrapperTest {

    /**
     * Test method for {@link com.sldeditor.ui.tree.SLDTreeItemWrapper#SLDTreeItemWrapper(java.lang.Object)}.
     */
    @Test
    public void testSLDTreeItemWrapper() {
        // Try with null objects first
        SLDTreeItemWrapper testObj1 = new SLDTreeItemWrapper(null);
        SLDTreeItemWrapper testObj2 = new SLDTreeItemWrapper(null);

        assertTrue(testObj1.equals(testObj1));
        assertTrue(testObj1.equals(testObj2));

        assertTrue(testObj1.hashCode() == testObj2.hashCode());

        // Try with 2 different text symbolizers instances but the same contents, hash codes should be different
        SLDTreeItemWrapper testObj3 = new SLDTreeItemWrapper(DefaultSymbols.createDefaultTextSymbolizer());
        SLDTreeItemWrapper testObj4 = new SLDTreeItemWrapper(DefaultSymbols.createDefaultTextSymbolizer());

        assertTrue(testObj3.equals(testObj3));
        assertTrue(testObj3.equals(testObj4));

        assertFalse(testObj3.hashCode() == testObj4.hashCode());

        // Try with one text symbolizer instances the same contents, hash codes should be the same
        TextSymbolizer textSymbolizer = DefaultSymbols.createDefaultTextSymbolizer();
        textSymbolizer.setHalo(new HaloImpl());
        SLDTreeItemWrapper testObj5 = new SLDTreeItemWrapper(textSymbolizer);
        SLDTreeItemWrapper testObj6 = new SLDTreeItemWrapper(textSymbolizer);

        assertTrue(testObj5.equals(testObj5));
        assertTrue(testObj5.equals(testObj6));

        assertTrue(testObj5.hashCode() == testObj6.hashCode());

        // Complete code coverage
        assertFalse(testObj1.equals(testObj6));
        assertFalse(testObj3.equals(null));
        assertFalse(testObj3.equals(new String()));
        assertFalse(testObj3.equals(testObj2));
        assertFalse(testObj3.equals(testObj6));
    }

}
