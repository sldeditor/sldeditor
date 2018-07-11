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

package com.sldeditor.test.unit.filter.v2.function.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import com.sldeditor.filter.v2.function.FilterConfigInterface;
import com.sldeditor.filter.v2.function.logic.And;
import com.sldeditor.filter.v2.function.logic.Not;
import com.sldeditor.filter.v2.function.logic.Or;
import java.util.ArrayList;
import java.util.List;
import org.geotools.filter.LogicFilterImpl;
import org.geotools.filter.text.cql2.CQL;
import org.geotools.filter.text.cql2.CQLException;
import org.junit.jupiter.api.Test;
import org.opengis.filter.Filter;

/**
 * Unit test for the following classes: {@link com.sldeditor.filter.v2.function.logic.And} {@link
 * com.sldeditor.filter.v2.function.logic.Or} {@link com.sldeditor.filter.v2.function.logic.Not}
 *
 * @author Robert Ward (SCISYS)
 */
public class LogicTests {

    private String category = "Test category";

    /** {@link com.sldeditor.filter.v2.function.logic.And}. */
    @Test
    public void testAnd() {
        testClass(new And(category), 2);
    }

    /** {@link com.sldeditor.filter.v2.function.logic.Or}. */
    @Test
    public void testOr() {
        testClass(new Or(category), 2);
    }

    /** {@link com.sldeditor.filter.v2.function.logic.Not}. */
    @Test
    public void testNot() {
        testClass(new Not(category), 1);
    }

    /**
     * Test class.
     *
     * @param objUnderTest the obj under test
     * @param noOFExpectedFilters the no OF expected filters
     */
    private void testClass(FilterConfigInterface objUnderTest, int noOFExpectedFilters) {
        assertNotNull(objUnderTest.getFilterConfiguration());
        assertNotNull(objUnderTest.createFilter());
        assertNull(objUnderTest.createFilter(null));

        LogicFilterImpl filter = (LogicFilterImpl) objUnderTest.createLogicFilter(null);
        assertEquals(0, filter.getChildren().size());

        List<Filter> filterList = new ArrayList<Filter>();
        try {
            filterList.add(CQL.toFilter("filter1 >= 5"));
        } catch (CQLException e) {
            fail(e.getStackTrace().toString());
        }

        filter = (LogicFilterImpl) objUnderTest.createLogicFilter(filterList);

        if (noOFExpectedFilters > 1) {
            assertEquals(0, filter.getChildren().size());
            try {
                filterList.add(CQL.toFilter("filter2 >= 5"));
            } catch (CQLException e) {
                fail(e.getStackTrace().toString());
            }
            filter = (LogicFilterImpl) objUnderTest.createLogicFilter(filterList);
        }
        assertEquals(noOFExpectedFilters, filter.getChildren().size());
        assertTrue(objUnderTest.category().compareTo(category) == 0);

        System.out.println(filter.toString());
    }
}
