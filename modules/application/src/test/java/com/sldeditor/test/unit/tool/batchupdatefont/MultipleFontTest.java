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

package com.sldeditor.test.unit.tool.batchupdatefont;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.sldeditor.tool.batchupdatefont.MultipleFont;
import java.util.ArrayList;
import java.util.List;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.styling.Font;
import org.geotools.styling.StyleFactoryImpl;
import org.junit.Test;
import org.opengis.filter.FilterFactory;

/**
 * The unit test for MultipleFont.
 *
 * <p>{@link com.sldeditor.tool.batchupdatefont.MultipleFont}
 *
 * @author Robert Ward (SCISYS)
 */
public class MultipleFontTest {

    /**
     * Test method for {@link
     * com.sldeditor.tool.batchupdatefont.MultipleFont#parseList(java.util.List)}. Test method for
     * {@link com.sldeditor.tool.batchupdatefont.MultipleFont#getFont()}.
     */
    @Test
    public void testParseList() {
        StyleFactoryImpl styleFactory = (StyleFactoryImpl) CommonFactoryFinder.getStyleFactory();
        List<Font> entries = new ArrayList<Font>();

        MultipleFont testObj = new MultipleFont();

        testObj.parseList(null);

        assertNotNull(testObj.getFont());

        FilterFactory ff = CommonFactoryFinder.getFilterFactory();
        String originalFontname = "Serif";
        String originalFontStyle = "normal";
        String originalFontWeight = "normal";
        int originalFontSize = 24;
        Font font =
                styleFactory.createFont(
                        ff.literal(originalFontname),
                        ff.literal(originalFontStyle),
                        ff.literal(originalFontWeight),
                        ff.literal(originalFontSize));

        entries.add(font);

        testObj.parseList(entries);
        Font actualFont = testObj.getFont();
        assertNotNull(actualFont.getSize());
        assertNotNull(actualFont.getStyle());
        assertNotNull(actualFont.getWeight());
        assertTrue(!actualFont.getFamily().isEmpty());

        // 2nd font is completely different
        String newFontname = "Serif2";
        String newFontStyle = "italic";
        String newFontWeight = "bold";
        int newFontSize = 25;

        Font font2 =
                styleFactory.createFont(
                        ff.literal(newFontname),
                        ff.literal(newFontStyle),
                        ff.literal(newFontWeight),
                        ff.literal(newFontSize));

        entries.add(font2);
        testObj.parseList(entries);
        actualFont = testObj.getFont();
        assertNull(actualFont.getSize());
        assertNull(actualFont.getStyle());
        assertNull(actualFont.getWeight());
        assertTrue(actualFont.getFamily().isEmpty());

        // Change family, style, weight and size
        entries.clear();
        entries.add(font);
        Font font3 =
                styleFactory.createFont(
                        ff.literal(originalFontname),
                        ff.literal(originalFontStyle),
                        ff.literal(originalFontWeight),
                        ff.literal(originalFontSize));
        entries.add(font3);
        testObj.parseList(entries);
        actualFont = testObj.getFont();
        assertNotNull(actualFont.getSize());
        assertNotNull(actualFont.getStyle());
        assertNotNull(actualFont.getWeight());
        assertTrue(!actualFont.getFamily().isEmpty());
    }
}
