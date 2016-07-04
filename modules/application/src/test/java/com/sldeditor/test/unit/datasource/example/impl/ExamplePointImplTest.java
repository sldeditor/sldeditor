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

package com.sldeditor.test.unit.datasource.example.impl;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.sldeditor.datasource.example.impl.ExamplePointImpl;

/**
 * Unit test for ExamplePointImpl class.
 * <p>{@link com.sldeditor.datasource.example.impl.ExamplePointImpl}
 * 
 * @author Robert Ward (SCISYS)
 *
 */
public class ExamplePointImplTest {

    /**
     * Test method for {@link com.sldeditor.datasource.example.impl.ExamplePointImpl#getLine()}.
     */
    @Test
    public void testExample() {
        ExamplePointImpl example = new ExamplePointImpl();

        assertTrue(example.getPoint() != null);
        assertTrue(example.getPoint() != null);
    }
}
