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

package com.sldeditor.test.unit.ui.detail.config.colourmap;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.styling.ColorMap;
import org.geotools.styling.ColorMapEntryImpl;
import org.geotools.styling.ColorMapImpl;
import org.junit.Test;
import org.opengis.filter.FilterFactory;

import com.sldeditor.common.xml.ui.XMLColourMapEntry;
import com.sldeditor.ui.detail.config.colourmap.EncodeColourMap;

/**
 * The unit test for EncodeColourMap.
 * 
 * <p>{@link com.sldeditor.ui.detail.config.colourmap.EncodeColourMap}
 *
 * @author Robert Ward (SCISYS)
 */
public class EncodeColourMapTest {

    /** The Constant QUANTITY_1. */
    private static final int QUANTITY_1 = 42;

    /** The Constant QUANTITY_2. */
    private static final int QUANTITY_2 = 12;

    /** The Constant OPACITY_1. */
    private static final double OPACITY_1 = 0.42;

    /** The Constant OPACITY_2. */
    private static final double OPACITY_2 = 0.0;

    /** The Constant COLOUR_1. */
    private static final String COLOUR_1 = "#001122";

    /** The Constant COLOUR_2. */
    private static final String COLOUR_2 = "#551122";

    /** The Constant LABEL_2. */
    private static final String LABEL_2 = "testlabel2";

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.colourmap.EncodeColourMap#encode(org.geotools.styling.ColorMap)}.
     * Test method for {@link com.sldeditor.ui.detail.config.colourmap.EncodeColourMap#encode(java.util.List)}.
     */
    @Test
    public void testEncodeColorMap() {
        FilterFactory ff = CommonFactoryFinder.getFilterFactory();

        ColorMapEntryImpl entry1 = new ColorMapEntryImpl();
        entry1.setColor(ff.literal(COLOUR_1));
        entry1.setOpacity(ff.literal(OPACITY_1));
        entry1.setQuantity(ff.literal(QUANTITY_1));
        ColorMap expectedValue = new ColorMapImpl();
        expectedValue.addColorMapEntry(entry1);
        ColorMapEntryImpl entry2 = new ColorMapEntryImpl();
        entry2.setColor(ff.literal(COLOUR_2));
        entry2.setLabel(LABEL_2);
        entry2.setOpacity(ff.literal(OPACITY_2));
        entry2.setQuantity(ff.literal(QUANTITY_2));
        expectedValue.addColorMapEntry(entry2);

        XMLColourMapEntry xml1 = new XMLColourMapEntry();
        xml1.setColour(COLOUR_1);
        xml1.setOpacity(OPACITY_1);
        xml1.setQuantity(42);
        List<XMLColourMapEntry> xmlList = new ArrayList<XMLColourMapEntry>();
        xmlList.add(xml1);

        XMLColourMapEntry xml2 = new XMLColourMapEntry();
        xml2.setColour(COLOUR_2);
        xml2.setLabel(LABEL_2);
        xml2.setOpacity(OPACITY_2);
        xml2.setQuantity(QUANTITY_2);
        xmlList.add(xml2);

        String actualValue1 = EncodeColourMap.encode(expectedValue);
        String actualValue2 = EncodeColourMap.encode(xmlList);

        assertTrue(actualValue1.compareTo(actualValue2) == 0);
    }

}
