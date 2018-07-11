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

package com.sldeditor.test.unit.common.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.sldeditor.common.utils.ColourUtils;
import java.awt.Color;
import org.geotools.factory.CommonFactoryFinder;
import org.junit.jupiter.api.Test;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;

/**
 * Unit test for ColourUtils.
 *
 * <p>{@link com.sldeditor.common.utils.ColourUtils}
 *
 * @author Robert Ward (SCISYS)
 */
public class ColourUtilsTest {

    /**
     * Test method for {@link com.sldeditor.common.utils.ColourUtils#fromColour(java.awt.Color)}.
     */
    @Test
    public void testFromColour() {
        String actualString = ColourUtils.fromColour(Color.BLUE);
        assertEquals("#0000FF", actualString);

        actualString = ColourUtils.fromColour(null);
        assertNull(actualString);
    }

    /**
     * Test method for {@link com.sldeditor.common.utils.ColourUtils#toColour(java.lang.String)}.
     */
    @Test
    public void testToColour() {
        Color actualColour = ColourUtils.toColour("#123456");

        int red = actualColour.getRed();
        assertEquals(18, red);
        int green = actualColour.getGreen();
        assertEquals(52, green);
        int blue = actualColour.getBlue();
        assertEquals(86, blue);

        // These are invalid
        actualColour = ColourUtils.toColour("#12345678");
        assertNull(actualColour);

        actualColour = ColourUtils.toColour("Not a colour");
        assertNull(actualColour);

        actualColour = ColourUtils.toColour("123456");
        assertNull(actualColour);

        actualColour = ColourUtils.toColour("1234567");
        assertNull(actualColour);
    }

    /**
     * Test method for {@link
     * com.sldeditor.common.utils.ColourUtils#getIntColour(org.opengis.filter.expression.Expression)}.
     */
    @Test
    public void testGetIntColour() {

        FilterFactory ff = CommonFactoryFinder.getFilterFactory();

        Expression colourExpression = ff.literal("#FF00FF");
        int colourValue = ColourUtils.getIntColour(colourExpression);
        assertEquals(0xff00ff, colourValue);

        colourValue = ColourUtils.getIntColour(null);
    }

    /** Test method for {@link com.sldeditor.common.utils.ColourUtils#createRandomColour()}. */
    @Test
    public void testCreateRandomColour() {

        Color colour = ColourUtils.createRandomColour();

        assertNotNull(colour);
    }

    /**
     * Test method for {@link com.sldeditor.common.utils.ColourUtils#getTextColour(java.awt.Color)}.
     */
    @Test
    public void testGetTextColour() {
        assertEquals(Color.black, ColourUtils.getTextColour(Color.white));
        assertEquals(Color.white, ColourUtils.getTextColour(Color.black));
    }
}
