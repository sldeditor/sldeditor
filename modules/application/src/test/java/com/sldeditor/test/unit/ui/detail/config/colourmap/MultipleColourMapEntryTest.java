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

package com.sldeditor.test.unit.ui.detail.config.colourmap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.sldeditor.ui.detail.config.colourmap.MultipleColourMapEntry;
import java.util.ArrayList;
import java.util.List;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.styling.ColorMapEntry;
import org.geotools.styling.ColorMapEntryImpl;
import org.junit.jupiter.api.Test;
import org.opengis.filter.FilterFactory;

/**
 * The unit test for MultipleColourMapEntry.
 *
 * <p>{@link com.sldeditor.ui.detail.config.colourmap.MultipleColourMapEntry}
 *
 * @author Robert Ward (SCISYS)
 */
public class MultipleColourMapEntryTest {

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.config.colourmap.MultipleColourMapEntry#parseList(java.util.List)}.
     * Test method for {@link
     * com.sldeditor.ui.detail.config.colourmap.MultipleColourMapEntry#getColourMapEntry()}.
     */
    @Test
    public void testParseList() {
        FilterFactory ff = CommonFactoryFinder.getFilterFactory();

        MultipleColourMapEntry testObj = new MultipleColourMapEntry();

        assertNotNull(testObj.getColourMapEntry());

        ColorMapEntry c1 = new ColorMapEntryImpl();
        String expectedLabel = "abc";
        String expectedColour = "#123456";
        double expectedOpacity = 0.5;
        int expectedQuantity = 42;

        c1.setLabel(expectedLabel);
        c1.setColor(ff.literal(expectedColour));
        c1.setOpacity(ff.literal(expectedOpacity));
        c1.setQuantity(ff.literal(expectedQuantity));
        List<ColorMapEntry> expectedList = new ArrayList<ColorMapEntry>();
        expectedList.add(c1);

        ColorMapEntry c2 = new ColorMapEntryImpl();
        c2.setLabel(expectedLabel);
        c2.setColor(ff.literal(expectedColour));
        c2.setOpacity(ff.literal(expectedOpacity));
        c2.setQuantity(ff.literal(expectedQuantity));
        expectedList.add(c2);

        ColorMapEntry c3 = new ColorMapEntryImpl();
        c3.setLabel(expectedLabel);
        c3.setColor(ff.literal(expectedColour));
        c3.setOpacity(ff.literal(expectedOpacity));
        c3.setQuantity(ff.literal(expectedQuantity));
        expectedList.add(c3);

        testObj.parseList(expectedList);

        ColorMapEntry actual = testObj.getColourMapEntry();

        // All the same
        assertNotNull(testObj.getColourMapEntry());
        assertEquals(actual.getLabel(), expectedLabel);
        assertEquals(actual.getColor().toString(), expectedColour);
        assertEquals(actual.getOpacity().toString(), String.valueOf(expectedOpacity));
        assertEquals(actual.getQuantity().toString(), String.valueOf(expectedQuantity));

        // Change label
        c2.setLabel("different");
        testObj.parseList(expectedList);

        actual = testObj.getColourMapEntry();

        assertNotNull(testObj.getColourMapEntry());
        assertNull(actual.getLabel());
        assertEquals(actual.getColor().toString(), expectedColour);
        assertEquals(actual.getOpacity().toString(), String.valueOf(expectedOpacity));
        assertEquals(actual.getQuantity().toString(), String.valueOf(expectedQuantity));

        // Change colour
        c1.setColor(ff.literal("#987654"));
        testObj.parseList(expectedList);

        actual = testObj.getColourMapEntry();

        assertNotNull(testObj.getColourMapEntry());
        assertNull(actual.getLabel());
        assertNull(actual.getColor());
        assertEquals(actual.getOpacity().toString(), String.valueOf(expectedOpacity));
        assertEquals(actual.getQuantity().toString(), String.valueOf(expectedQuantity));

        // Change opacity
        c3.setOpacity(ff.literal(1.0));
        testObj.parseList(expectedList);

        actual = testObj.getColourMapEntry();

        assertNotNull(testObj.getColourMapEntry());
        assertNull(actual.getLabel());
        assertNull(actual.getColor());
        assertNull(actual.getOpacity());
        assertEquals(actual.getQuantity().toString(), String.valueOf(expectedQuantity));

        // Change quantity
        c2.setQuantity(ff.literal(39.0));
        testObj.parseList(expectedList);

        actual = testObj.getColourMapEntry();

        assertNotNull(testObj.getColourMapEntry());
        assertNull(actual.getLabel());
        assertNull(actual.getColor());
        assertNull(actual.getOpacity());
        assertNull(actual.getQuantity());
    }
}
