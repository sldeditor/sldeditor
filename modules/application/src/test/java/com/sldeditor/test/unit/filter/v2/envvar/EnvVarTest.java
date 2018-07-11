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

package com.sldeditor.test.unit.filter.v2.envvar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sldeditor.filter.v2.envvar.EnvVar;
import org.junit.jupiter.api.Test;

/**
 * Unit test for EnvVar class.
 *
 * <p>{@link com.sldeditor.filter.v2.envvar.EnvVar}
 *
 * @author Robert Ward (SCISYS)
 */
public class EnvVarTest {

    /**
     * Test method for {@link com.sldeditor.filter.v2.envvar.EnvVar#EnvVar(java.lang.String,
     * java.lang.Class, boolean)}.
     */
    @Test
    public void testEnvVar() {
        String actualName = "string_var";
        Class<String> actualType = String.class;
        EnvVar envVar = new EnvVar(actualName, actualType, true);
        String actualValue = "testvalue";
        envVar.setValue(actualValue);
        assertTrue(actualName.compareTo(envVar.getName()) == 0);
        assertEquals(actualType, envVar.getType());
        assertEquals(actualValue, envVar.getValue());
        assertTrue(envVar.isPredefined());

        EnvVar envVar2 = new EnvVar(envVar);
        assertTrue(actualName.compareTo(envVar2.getName()) == 0);
        assertEquals(actualType, envVar2.getType());
        assertTrue(envVar2.isPredefined());
        assertEquals(actualValue, envVar2.getValue());

        EnvVar envVar3 = new EnvVar(envVar, false);
        String newActualName = "newvalue";
        envVar3.setName(newActualName);
        envVar3.setType(Integer.class);
        assertTrue(newActualName.compareTo(envVar3.getName()) == 0);
        assertEquals(Integer.class, envVar3.getType());
        assertFalse(envVar3.isPredefined());
        assertEquals(actualValue, envVar3.getValue());
    }
}
