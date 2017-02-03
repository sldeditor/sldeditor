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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.Test;

import com.sldeditor.rendertransformation.ProcessFunctionParameterValue;

/**
 * Unit test for ProcessFunctionParameterValue class.
 * <p>{@link com.sldeditor.rendertransformation.ProcessFunctionParameterValue}
 * 
 * @author Robert Ward (SCISYS)
 *
 */
public class ProcessFunctionParameterValueTest {

    /**
     * Test method for {@link com.sldeditor.rendertransformation.ProcessFunctionParameterValue#ProcessFunctionParameterValue(com.sldeditor.rendertransformation.ProcessFunctionParameterValue)}.
     */
    @Test
    public void testProcessFunctionParameterValueProcessFunctionParameterValue() {
        ProcessFunctionParameterValue obj1 = new ProcessFunctionParameterValue();
        obj1.name = "test";
        obj1.dataType = "datatype";
        obj1.type = Date.class;
        obj1.minOccurences = 10;
        obj1.maxOccurences = 78;
        obj1.included = true;
        obj1.optional = true;

        ProcessFunctionParameterValue obj2 = new ProcessFunctionParameterValue(obj1);
        assertTrue(obj1.name.compareTo(obj2.name) == 0);
        assertTrue(obj1.dataType.compareTo(obj2.dataType) == 0);
        assertEquals(obj1.included, obj2.included);
        assertEquals(obj1.type, obj2.type);
        assertEquals(obj1.minOccurences, obj2.minOccurences);
        assertEquals(obj1.maxOccurences, obj2.maxOccurences);
        assertEquals(obj1.optional, obj2.optional);
    }

}
