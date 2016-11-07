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
 * The unit test for FieldConfigColourMap.
 * <p>{@link com.sldeditor.ui.detail.config.colourmap.EncodeColourMap}
 *
 * @author Robert Ward (SCISYS)
 */
public class EncodeColourMapTest {

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.colourmap.EncodeColourMap#encode(org.geotools.styling.ColorMap)}.
     * Test method for {@link com.sldeditor.ui.detail.config.colourmap.EncodeColourMap#encode(java.util.List)}.
     */
    @Test
    public void testEncodeColorMap() {
        FilterFactory ff = CommonFactoryFinder.getFilterFactory();

        ColorMap expectedValue = new ColorMapImpl();
        ColorMapEntryImpl entry1 = new ColorMapEntryImpl();
        entry1.setColor(ff.literal("#001122"));
        entry1.setOpacity(ff.literal(0.42));
        entry1.setQuantity(ff.literal(42));
        expectedValue.addColorMapEntry(entry1);
        ColorMapEntryImpl entry2 = new ColorMapEntryImpl();
        entry2.setColor(ff.literal("#551122"));
        entry2.setLabel("testlabel2");
        entry2.setOpacity(ff.literal(0.22));
        entry2.setQuantity(ff.literal(12));
        expectedValue.addColorMapEntry(entry2);

        List<XMLColourMapEntry> xmlList = new ArrayList<XMLColourMapEntry>();
        XMLColourMapEntry xml1 = new XMLColourMapEntry();
        xml1.setColour("#001122");
        xml1.setOpacity(0.42);
        xml1.setQuantity(42);
        xmlList.add(xml1);

        XMLColourMapEntry xml2 = new XMLColourMapEntry();
        xml2.setColour("#551122");
        xml2.setLabel("testlabel2");
        xml2.setOpacity(0.22);
        xml2.setQuantity(12);
        xmlList.add(xml2);

        String actualValue1 = EncodeColourMap.encode(expectedValue);
        String actualValue2 = EncodeColourMap.encode(xmlList);

        assertTrue(actualValue1.compareTo(actualValue2) == 0);
    }

}
