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

package com.sldeditor.test.unit.colourramp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.awt.Color;

import javax.swing.ImageIcon;

import org.geotools.factory.CommonFactoryFinder;
import org.junit.Test;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;

import com.sldeditor.colourramp.ColourRamp;
import com.sldeditor.colourramp.ramp.ColourRampData;
import com.sldeditor.common.utils.ColourUtils;

/**
 * Unit test for ColourRamp class.
 * <p>{@link com.sldeditor.colourramp.ColourRamp}
 * 
 * @author Robert Ward (SCISYS)
 *
 */
public class ColourRampTest {

    /**
     * Test method for {@link com.sldeditor.colourramp.ColourRamp#ColourRamp()}.
     */
    @Test
    public void testColourRamp() {
        ColourRamp ramp = new ColourRamp();

        assertEquals(Color.BLACK, ramp.getStartColour());
        assertEquals(Color.WHITE, ramp.getEndColour());

        Color expectedStart = Color.red;
        Color expectedEnd = Color.pink;

        ramp.setColourRamp(expectedStart, expectedEnd);
        assertEquals(expectedStart, ramp.getStartColour());
        assertEquals(expectedEnd, ramp.getEndColour());

        Color expectedEnd2 = Color.cyan;
        ramp.addColour(expectedEnd2);
        assertEquals(expectedStart, ramp.getStartColour());
        assertEquals(expectedEnd2, ramp.getEndColour());
        assertEquals(3, ramp.getColourList().size());

        ImageIcon icon1 = ramp.getImageIcon(false);
        assertNotNull(icon1);
        ImageIcon icon2 = ramp.getImageIcon(true);
        assertNotNull(icon2);

        FilterFactory ff = CommonFactoryFinder.getFilterFactory( null );
        ColourRampData data = new ColourRampData();
        Expression expectedMinValue = ff.literal(1);
        Expression expectedMaxValue = ff.literal(10);
        data.setColourRamp(ramp);
        data.setMinValue(1);
        data.setMaxValue(10);

        Expression actualStart = ramp.getColour(data, expectedMinValue, false);
        @SuppressWarnings("unused")
        Expression actualEnd = ramp.getColour(data, expectedMaxValue, false);

        assertEquals(ColourUtils.toColour(actualStart.toString()), expectedStart);

        // Can't test end value
        // assertEquals(ColourUtils.toColour(actualEnd.toString()), expectedEnd2);

        // Reverse colours
        actualStart = ramp.getColour(data, expectedMinValue, true);
        actualEnd = ramp.getColour(data, expectedMaxValue, true);

        assertEquals(ColourUtils.toColour(actualStart.toString()), expectedEnd2);
        // Can't test end value
        //        assertEquals(ColourUtils.toColour(actualEnd.toString()), expectedStart);
    }

}
