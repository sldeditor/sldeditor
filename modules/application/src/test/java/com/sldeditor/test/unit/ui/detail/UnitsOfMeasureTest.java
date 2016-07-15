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

package com.sldeditor.test.unit.ui.detail;

import static org.junit.Assert.*;

import javax.measure.quantity.Length;
import javax.measure.unit.NonSI;
import javax.measure.unit.SI;
import javax.measure.unit.Unit;

import org.junit.Test;

import com.sldeditor.ui.detail.UnitsOfMeasure;

/**
 * The unit test for UnitsOfMeasure.
 * <p>{@link com.sldeditor.ui.detail.UnitsOfMeasure}
 *
 * @author Robert Ward (SCISYS)
 */
public class UnitsOfMeasureTest {

    /**
     * Test method for {@link com.sldeditor.ui.detail.UnitsOfMeasure#convert(javax.measure.unit.Unit)}.
     */
    @Test
    public void testConvertUnitOfLength() {
        UnitsOfMeasure uom = UnitsOfMeasure.getInstance();
        Unit<Length> unit = null;
        assertTrue("".compareTo(uom.convert(unit)) == 0);

        assertTrue("pixel".compareTo(uom.convert(NonSI.PIXEL)) == 0);
        assertTrue("metre".compareTo(uom.convert(SI.METRE)) == 0);
        assertTrue("foot".compareTo(uom.convert(NonSI.FOOT)) == 0);
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.UnitsOfMeasure#convert(java.lang.String)}.
     */
    @Test
    public void testConvertString() {
        UnitsOfMeasure uom = UnitsOfMeasure.getInstance();
        assertNull(uom.convert((String)null));
        assertEquals(NonSI.PIXEL, uom.convert("pixel"));
        assertEquals(SI.METRE, uom.convert("metre"));
        assertEquals(NonSI.FOOT, uom.convert("foot"));
    }

}
