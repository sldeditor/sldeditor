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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.geotools.factory.CommonFactoryFinder;
import org.junit.Test;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Divide;
import org.opengis.filter.expression.Expression;

import com.sldeditor.common.utils.ColourUtils;
import com.sldeditor.ui.detail.config.colourmap.ColourMapData;

/**
 * The unit test for ColourMapData.
 * 
 * <p>{@link com.sldeditor.ui.detail.config.colourmap.ColourMapData}
 *
 * @author Robert Ward (SCISYS)
 */
public class ColourMapDataTest {

    /**
     * Test method for
     * {@link com.sldeditor.ui.detail.config.colourmap.ColourMapData#ColourMapData()}.
     */
    @Test
    public void testColourMapData() {
        FilterFactory ff = CommonFactoryFinder.getFilterFactory();

        ColourMapData actual = new ColourMapData();
        String expectedLabel = "abc";
        String expectedColour = "#123456";
        double expectedOpacity = 0.5;
        int expectedQuantity = 42;

        actual.setLabel(expectedLabel);
        actual.setColour(ff.literal(expectedColour));
        actual.setOpacity(ff.literal(expectedOpacity));
        actual.setQuantity(ff.literal(expectedQuantity));

        assertEquals(actual.getLabel(), expectedLabel);
        assertEquals(ColourUtils.fromColour(actual.getColour()), expectedColour);
        assertEquals(actual.getOpacity().toString(), String.valueOf(expectedOpacity));
        assertEquals(actual.getQuantity().toString(), String.valueOf(expectedQuantity));

        // Test getNextQuantity with a value
        Expression nextQuantity = actual.getNextQuantity();
        int iValue = Double.valueOf(nextQuantity.toString()).intValue();
        assertTrue(iValue == (expectedQuantity + 1));

        // Test getNextQuantity with a function expression set
        Divide divide = ff.divide(ff.literal(10.0), ff.literal(2.0));

        actual.setQuantity(divide);
        nextQuantity = actual.getNextQuantity();
        assertEquals(nextQuantity.toString(), actual.getQuantity().toString());
        actual.update(null);

        // Label
        ColourMapData update = new ColourMapData();
        String updatedLabel = "new label";
        update.setLabel(updatedLabel);
        actual.update(update);
        assertEquals(actual.getLabel(), updatedLabel);

        // Colour
        ColourMapData updateColour = new ColourMapData();
        String updatedColour = "#234567";
        updateColour.setColour(ff.literal(updatedColour));
        actual.update(updateColour);
        assertEquals(actual.getColourString(), updatedColour);

        // Opacity
        ColourMapData updateOpacity = new ColourMapData();
        double updatedOpacity = 0.25;
        updateOpacity.setOpacity(ff.literal(updatedOpacity));
        actual.update(updateOpacity);
        assertEquals(actual.getOpacity().toString(), String.valueOf(updatedOpacity));

        // Quantity
        ColourMapData updateQuantity = new ColourMapData();
        int updatedQuantity = 78;
        updateQuantity.setQuantity(ff.literal(updatedQuantity));
        actual.update(updateQuantity);
        assertEquals(actual.getQuantity().toString(), String.valueOf(updatedQuantity));
    }

}
