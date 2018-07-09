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

package com.sldeditor.test.unit.colourramp.ramp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.sldeditor.colourramp.ColourRamp;
import com.sldeditor.colourramp.ramp.ColourRampData;
import org.junit.Test;

/**
 * Unit test for ColourRampData class.
 *
 * <p>{@link com.sldeditor.colourramp.ramp.ColourRampData}
 *
 * @author Robert Ward (SCISYS)
 */
public class ColourRampDataTest {

    /** Test method for {@link com.sldeditor.colourramp.ramp.ColourRampData#ColourRampData()}. */
    @Test
    public void testColourRampData() {
        ColourRampData obj = new ColourRampData();
        assertNull(obj.getColourRamp());
        assertEquals(0, obj.getMaxValue());
        assertEquals(0, obj.getMinValue());
        assertFalse(obj.reverseColours());

        int expectedMinValue = 42;
        obj.setMinValue(expectedMinValue);
        assertEquals(expectedMinValue, obj.getMinValue());
        int expectedMaxValue = 69;
        obj.setMaxValue(expectedMaxValue);
        assertEquals(expectedMaxValue, obj.getMaxValue());

        ColourRamp colourRamp = new ColourRamp();
        obj.setColourRamp(colourRamp);
        assertEquals(colourRamp, obj.getColourRamp());

        obj.setReverseColours(true);
        assertTrue(obj.reverseColours());
    }
}
