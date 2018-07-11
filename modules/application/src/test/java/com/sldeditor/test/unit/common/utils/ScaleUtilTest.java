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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sldeditor.common.defaultsymbol.DefaultSymbols;
import com.sldeditor.common.utils.ScaleUtil;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import org.geotools.styling.Rule;
import org.junit.jupiter.api.Test;

/**
 * Unit test for ScaleUtil class.
 *
 * <p>{@link com.sldeditor.common.utils.ScaleUtil}
 *
 * @author Robert Ward (SCISYS)
 */
public class ScaleUtilTest {

    /** Test method for {@link com.sldeditor.common.utils.ScaleUtil#getValue(double)}. */
    @Test
    public void testGetValue() {
        assertEquals("", ScaleUtil.getValue(0.0));
        assertEquals("", ScaleUtil.getValue(Double.NEGATIVE_INFINITY));
        assertEquals("", ScaleUtil.getValue(Double.POSITIVE_INFINITY));
        assertEquals("1:500", ScaleUtil.getValue(500.0));

        assertEquals(getExpectedValue("1:123,456,789"), ScaleUtil.getValue(123456789.0));
    }

    /**
     * Gets the expected value.
     *
     * @param expectedValue the expected value
     * @return the expected value
     */
    private static String getExpectedValue(String expectedValue) {
        DecimalFormat format = (DecimalFormat) DecimalFormat.getInstance();
        DecimalFormatSymbols symbols = format.getDecimalFormatSymbols();
        char sep = symbols.getGroupingSeparator();

        return expectedValue.replace(',', sep);
    }

    /** Test method for {@link com.sldeditor.common.utils.ScaleUtil#isNotSet(double)}. */
    @Test
    public void testIsNotSet() {
        assertTrue(ScaleUtil.isNotSet(0.0));
        assertTrue(ScaleUtil.isNotSet(Double.NEGATIVE_INFINITY));
        assertTrue(ScaleUtil.isNotSet(Double.POSITIVE_INFINITY));
        assertFalse(ScaleUtil.isNotSet(500.0));
        assertFalse(ScaleUtil.isNotSet(123456789.0));
    }

    /**
     * Test method for {@link
     * com.sldeditor.common.utils.ScaleUtil#isPresent(org.opengis.style.Rule)}.
     */
    @Test
    public void testIsPresent() {
        assertFalse(ScaleUtil.isPresent(null));

        Rule rule = DefaultSymbols.createNewRule();
        assertFalse(ScaleUtil.isPresent(rule));

        // Set max scale denominator
        rule.setMaxScaleDenominator(10000.0);
        assertTrue(ScaleUtil.isPresent(rule));

        // Set both min and max scale denominator
        rule.setMinScaleDenominator(100.0);
        assertTrue(ScaleUtil.isPresent(rule));

        // Set min scale denominator
        rule.setMaxScaleDenominator(0.0);
        assertTrue(ScaleUtil.isPresent(rule));
    }

    /**
     * Test method for {@link com.sldeditor.common.utils.ScaleUtil#extractValue(java.lang.String)}.
     */
    @Test
    public void testExtractValue() {
        assertTrue(Math.abs(ScaleUtil.extractValue(null)) < 0.001);
        assertTrue(Math.abs(ScaleUtil.extractValue("1:12345678") - 12345678.0) < 0.001);
        assertTrue(Math.abs(ScaleUtil.extractValue("12345678") - 12345678.0) < 0.001);
        assertTrue(Math.abs(ScaleUtil.extractValue("1:12,345,678") - 12345678.0) < 0.001);
        assertTrue(Math.abs(ScaleUtil.extractValue("12,345,678") - 12345678.0) < 0.001);
        assertTrue(Math.abs(ScaleUtil.extractValue("0") - 0.0) < 0.001);
    }
}
