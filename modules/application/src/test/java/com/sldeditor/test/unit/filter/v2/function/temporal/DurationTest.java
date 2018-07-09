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

package com.sldeditor.test.unit.filter.v2.function.temporal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.sldeditor.filter.v2.function.temporal.Duration;
import org.junit.Test;

/**
 * Unit test for Duration class.
 *
 * <p>{@link com.sldeditor.filter.v2.function.temporal.Duration}
 *
 * @author Robert Ward (SCISYS)
 */
public class DurationTest {

    /** Test method for {@link com.sldeditor.filter.v2.function.temporal.Duration#Duration()}. */
    @Test
    public void testDuration() {
        Duration duration = new Duration();
        assertEquals(0, duration.getDurationDays());
        assertTrue(duration.isDate());
    }

    /**
     * Test method for {@link
     * com.sldeditor.filter.v2.function.temporal.Duration#Duration(java.lang.String)}.
     */
    @Test
    public void testDurationString() {
        test("P10D", 0, 0, 10, 0, 0, 0, null);
        test("P1Y2M3D", 1, 2, 3, 0, 0, 0, null);
        test("P1Y2M", 1, 2, 0, 0, 0, 0, null);
        test("P1Y2M3D4H5M6S", 1, 2, 3, 4, 5, 6, null);
        test("P1Y6S", 1, 0, 0, 0, 0, 6, null);
        test("P1Y0H6M", 1, 0, 0, 0, 6, 0, null);
        test("P1Y6H", 1, 0, 0, 6, 0, 0, null);
        test("T1H6M", 0, 0, 0, 1, 6, 0, null);
        test("P1M6M", 0, 1, 0, 0, 0, 0, "P1M"); // This is not valid
        test("T10H", 0, 0, 0, 10, 0, 0, null);

        String dateString = "20-09-2016T17:42:27Z";
        Duration duration = new Duration(dateString);
        assertTrue(duration.isDate());
        String actual = duration.getString();

        assertTrue(dateString, dateString.compareTo(actual) == 0);
    }

    private void test(
            String durationString,
            int year,
            int month,
            int days,
            int hours,
            int mins,
            int sec,
            String alternative) {
        Duration duration = new Duration(durationString);
        assertEquals(durationString, year, duration.getDurationYears());
        assertEquals(durationString, month, duration.getDurationMonths());
        assertEquals(durationString, days, duration.getDurationDays());
        assertEquals(durationString, hours, duration.getDurationHours());
        assertEquals(durationString, mins, duration.getDurationMinutes());
        assertEquals(durationString, sec, duration.getDurationSeconds());
        assertFalse(durationString, duration.isDate());

        String actual = duration.getString();

        String expected = (alternative != null) ? alternative : durationString;
        assertTrue(durationString, expected.compareTo(actual) == 0);
    }

    /**
     * Test method for {@link
     * com.sldeditor.filter.v2.function.temporal.Duration#setDate(java.util.Date)}.
     */
    @Test
    public void testSetDate() {}

    /**
     * Test method for {@link com.sldeditor.filter.v2.function.temporal.Duration#setDuration(int,
     * int, int, int, int, int)}.
     */
    @Test
    public void testSetDuration() {}

    /** Test method for {@link com.sldeditor.filter.v2.function.temporal.Duration#getString()}. */
    @Test
    public void testGetString() {}

    /** Test method for {@link com.sldeditor.filter.v2.function.temporal.Duration#getDate()}. */
    @Test
    public void testGetDate() {}

    /**
     * Test method for {@link
     * com.sldeditor.filter.v2.function.temporal.Duration#getDurationYears()}.
     */
    @Test
    public void testGetDurationYears() {}

    /**
     * Test method for {@link
     * com.sldeditor.filter.v2.function.temporal.Duration#getDurationMonths()}.
     */
    @Test
    public void testGetDurationMonths() {}

    /**
     * Test method for {@link com.sldeditor.filter.v2.function.temporal.Duration#getDurationDays()}.
     */
    @Test
    public void testGetDurationDays() {}

    /**
     * Test method for {@link
     * com.sldeditor.filter.v2.function.temporal.Duration#getDurationHours()}.
     */
    @Test
    public void testGetDurationHours() {}

    /**
     * Test method for {@link
     * com.sldeditor.filter.v2.function.temporal.Duration#getDurationMinutes()}.
     */
    @Test
    public void testGetDurationMinutes() {}

    /**
     * Test method for {@link
     * com.sldeditor.filter.v2.function.temporal.Duration#getDurationSeconds()}.
     */
    @Test
    public void testGetDurationSeconds() {}

    /** Test method for {@link com.sldeditor.filter.v2.function.temporal.Duration#isDate()}. */
    @Test
    public void testIsDate() {}
}
