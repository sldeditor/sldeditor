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

package com.sldeditor.test.unit.filter.v2.function;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.sldeditor.filter.v2.expression.ExpressionTypeEnum;
import com.sldeditor.filter.v2.function.FilterName;
import com.sldeditor.filter.v2.function.FilterNameParameter;

/**
 * Unit test for FilterName class.
 * <p>{@link com.sldeditor.filter.v2.function.FilterName}
 * 
 * @author Robert Ward (SCISYS)
 *
 */
public class FilterNameTest {

    /**
     * Test method for {@link com.sldeditor.filter.v2.function.FilterName#FilterName(java.lang.String, java.lang.Class)}.
     * Test method for {@link com.sldeditor.filter.v2.function.FilterName#getFilterName()}.
     * Test method for {@link com.sldeditor.filter.v2.function.FilterName#getReturnType()}.
     * Test method for {@link com.sldeditor.filter.v2.function.FilterName#getParameterList()}.
     * Test method for {@link com.sldeditor.filter.v2.function.FilterName#getParameter(int)}.
     * Test method for {@link com.sldeditor.filter.v2.function.FilterName#addParameter(com.sldeditor.filter.v2.function.FilterNameParameter)}.
     */
    @Test
    public void testGetFilterName() {
        String filterName = "test filter";
        Class<?> returnType = String.class;
        FilterName f = new FilterName(filterName, returnType);

        assertTrue(filterName.compareTo(f.getFilterName()) == 0);
        assertEquals(returnType, f.getReturnType());

        FilterNameParameter p1 = new FilterNameParameter("parameter 1", ExpressionTypeEnum.LITERAL, Integer.class);
        f.addParameter(p1);
        assertEquals(1, f.getParameterList().size());
        assertEquals(null, f.getParameter(-1));
        assertEquals(null, f.getParameter(99));
        assertEquals(p1, f.getParameter(0));
    }
}
