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

import com.sldeditor.common.defaultsymbol.DefaultSymbols;
import com.sldeditor.ui.tree.SLDTreeItemWrapper;
import org.geotools.styling.HaloImpl;
import org.geotools.styling.TextSymbolizer;
import org.junit.Test;

/**
 * The unit test for SLDTreeItemWrapper.
 *
 * <p>{@link com.sldeditor.ui.tree.SLDTreeItemWrapper}
 *
 * @author Robert Ward (SCISYS)
 */
public class SLDTreeItemWrapperTest {

    /**
     * Test method for {@link
     * com.sldeditor.ui.tree.SLDTreeItemWrapper#SLDTreeItemWrapper(java.lang.Object)}.
     */
    @Test
    public void testSLDTreeItemWrapper() {
        // Try with null objects first
        String testObj1 = SLDTreeItemWrapper.generateKey(null);
        String testObj2 = SLDTreeItemWrapper.generateKey(null);

        assertTrue(testObj1.equals(testObj1));
        assertTrue(testObj1.equals(testObj2));

        // Try with 2 different text symbolizers instances but the same contents, hash codes should
        // be different
        String testObj3 =
                SLDTreeItemWrapper.generateKey(DefaultSymbols.createDefaultTextSymbolizer());
        String testObj4 =
                SLDTreeItemWrapper.generateKey(DefaultSymbols.createDefaultTextSymbolizer());

        assertTrue(testObj3.equals(testObj3));
        assertFalse(testObj3.equals(testObj4));

        // Try with one text symbolizer instances the same contents, hash codes should be the same
        TextSymbolizer textSymbolizer = DefaultSymbols.createDefaultTextSymbolizer();
        textSymbolizer.setHalo(new HaloImpl());
        String testObj5 = SLDTreeItemWrapper.generateKey(textSymbolizer);
        String testObj6 = SLDTreeItemWrapper.generateKey(textSymbolizer);

        assertTrue(testObj5.equals(testObj5));
        assertTrue(testObj5.equals(testObj6));
    }
}
