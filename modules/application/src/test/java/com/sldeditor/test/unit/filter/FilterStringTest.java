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

package com.sldeditor.test.unit.filter;

import static org.junit.Assert.*;

import org.geotools.factory.CommonFactoryFinder;
import org.junit.Test;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;

import com.sldeditor.filter.v2.FilterString;

/**
 * Unit test for FilterString class.
 * <p>{@link com.sldeditor.filter.v2.FilterString}
 * 
 * @author Robert Ward (SCISYS)
 *
 */
public class FilterStringTest {

    private static FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();

    /**
     * Test method for {@link com.sldeditor.filter.v2.FilterString#getValidFilterString(org.opengis.filter.Filter)}.
     */
    @Test
    public void testGetValidFilterString() {
        String expression1 = "expression1";
        String expression2 = "s1 34";

        Filter originalfilter = ff.equal(ff.literal(expression1), ff.literal(expression2));
        String expectedFilterString = String.format("[ '%s' equals '%s' ]", expression1, expression2);

        String actualString = FilterString.getValidFilterString(originalfilter);

        assertEquals(actualString, expectedFilterString);
    }

}
