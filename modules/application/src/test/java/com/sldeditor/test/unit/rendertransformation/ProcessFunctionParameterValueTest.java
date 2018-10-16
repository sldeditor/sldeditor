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

package com.sldeditor.test.unit.rendertransformation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sldeditor.rendertransformation.ProcessFunctionParameterValue;
import java.util.Date;
import org.junit.jupiter.api.Test;

/**
 * Unit test for ProcessFunctionParameterValue class.
 *
 * <p>{@link com.sldeditor.rendertransformation.ProcessFunctionParameterValue}
 *
 * @author Robert Ward (SCISYS)
 */
public class ProcessFunctionParameterValueTest {

    /**
     * Test method for {@link
     * com.sldeditor.rendertransformation.ProcessFunctionParameterValue#ProcessFunctionParameterValue(com.sldeditor.rendertransformation.ProcessFunctionParameterValue)}.
     */
    @Test
    public void testProcessFunctionParameterValueProcessFunctionParameterValue() {
        ProcessFunctionParameterValue obj1 = new ProcessFunctionParameterValue();
        obj1.setName("test");
        obj1.setDataType("datatype");
        obj1.setType(Date.class);
        obj1.setMinOccurences(10);
        obj1.setMaxOccurences(78);
        obj1.setIncluded(true);
        obj1.setOptional(true);

        ProcessFunctionParameterValue obj2 = new ProcessFunctionParameterValue(obj1);
        assertTrue(obj1.getName().compareTo(obj2.getName()) == 0);
        assertTrue(obj1.getDataType().compareTo(obj2.getDataType()) == 0);
        assertEquals(obj1.isIncluded(), obj2.isIncluded());
        assertEquals(obj1.getType(), obj2.getType());
        assertEquals(obj1.getMinOccurences(), obj2.getMinOccurences());
        assertEquals(obj1.getMaxOccurences(), obj2.getMaxOccurences());
        assertEquals(obj1.isOptional(), obj2.isOptional());
    }
}
