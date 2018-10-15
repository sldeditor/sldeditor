/*
 * SLD Editor - The Open Source Java SLD Editor
 *
 * Copyright (C) 2018, SCISYS UK Limited
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

import static org.junit.jupiter.api.Assertions.*;

import com.sldeditor.common.utils.OSValidator;
import org.junit.jupiter.api.Test;

/**
 * The unit test for OSValidator.
 *
 * @author Robert Ward (SCISYS)
 */
class OSValidatorTest {

    private static String OS = System.getProperty("os.name").toLowerCase();

    /** Test method for {@link com.sldeditor.common.utils.OSValidator#isWindows()}. */
    @Test
    void testIsWindows() {
        boolean expectedResult = (OS.indexOf("win") >= 0);
        assertEquals(expectedResult, OSValidator.isWindows());
    }

    /** Test method for {@link com.sldeditor.common.utils.OSValidator#isMac()}. */
    @Test
    void testIsMac() {
        boolean expectedResult = (OS.indexOf("mac") >= 0);
        assertEquals(expectedResult, OSValidator.isMac());
    }

    /** Test method for {@link com.sldeditor.common.utils.OSValidator#isUnix()}. */
    @Test
    void testIsUnix() {
        boolean expectedResult = OS.endsWith("x");
        assertEquals(expectedResult, OSValidator.isUnix());
    }

    /** Test method for {@link com.sldeditor.common.utils.OSValidator#isSolaris()}. */
    @Test
    void testIsSolaris() {
        boolean expectedResult = (OS.indexOf("sunos") >= 0);
        assertEquals(expectedResult, OSValidator.isSolaris());
    }

    /** Test method for {@link com.sldeditor.common.utils.OSValidator#getOS()}. */
    @Test
    void testGetOS() {
        @SuppressWarnings("unused")
        OSValidator ignore = new OSValidator();
        System.out.println(OSValidator.getOS());
    }
}
