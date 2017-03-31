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

package com.sldeditor.test.unit.common.console;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.sldeditor.common.console.ConsoleData;
import com.sldeditor.common.console.ConsoleDataEnum;

/**
 * The unit test for ConsoleData.
 * 
 * <p>{@link com.sldeditor.common.console.ConsoleData}
 *
 * @author Robert Ward (SCISYS)
 */
public class ConsoleDataTest {

    /**
     * Test method for {@link com.sldeditor.common.console.ConsoleData#ConsoleData(java.lang.String, com.sldeditor.common.console.ConsoleDataEnum)}.
     */
    @Test
    public void testConsoleData() {
        String expectedMessage = "test message";
        ConsoleDataEnum expectedType = ConsoleDataEnum.EXCEPTION;
        ConsoleData data = new ConsoleData(expectedMessage, expectedType);
        assertEquals(expectedMessage, data.getMessage());
        assertEquals(expectedType, data.getType());
        assertEquals(expectedMessage, data.toString());
    }

}
