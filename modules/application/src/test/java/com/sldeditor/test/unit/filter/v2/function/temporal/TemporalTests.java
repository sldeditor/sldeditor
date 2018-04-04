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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.temporal.BinaryTemporalOperatorImpl;
import org.junit.Test;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;

import com.sldeditor.filter.v2.function.FilterConfigInterface;
import com.sldeditor.filter.v2.function.temporal.After;
import com.sldeditor.filter.v2.function.temporal.AnyInteracts;
import com.sldeditor.filter.v2.function.temporal.Before;
import com.sldeditor.filter.v2.function.temporal.BegunBy;
import com.sldeditor.filter.v2.function.temporal.During;
import com.sldeditor.filter.v2.function.temporal.EndedBy;
import com.sldeditor.filter.v2.function.temporal.Meets;
import com.sldeditor.filter.v2.function.temporal.MetBy;
import com.sldeditor.filter.v2.function.temporal.TContains;
import com.sldeditor.filter.v2.function.temporal.TEquals;
import com.sldeditor.filter.v2.function.temporal.TOverlaps;

/**
 * Unit tests for the following temporal filter classes.
 * 
 * @author Robert Ward (SCISYS)
 *
 */
public class TemporalTests {

    private FilterFactory ff = CommonFactoryFinder.getFilterFactory();
    private String category = "Test category";

    /**
     * Unit test for the following class:
     * {@link com.sldeditor.filter.v2.function.temporal.After}.
     */
    @Test
    public void testAfter() {
        testClass(new After(category));
    }

    /**
     * Unit test for the following class:
     * {@link com.sldeditor.filter.v2.function.temporal.Before}.
     */
    @Test
    public void testBefore() {
        testClass(new Before(category));
    }

    /**
     * Unit test for the following class:
     * {@link com.sldeditor.filter.v2.function.temporal.During}.
     */
    @Test
    public void testDuring() {
        testClass(new During(category));
    }

    /**
     * Unit test for the following class:
     * {@link com.sldeditor.filter.v2.function.temporal.TContains}.
     */
    @Test
    public void testTContains() {
        testClass(new TContains(category));
    }

    /**
     * Unit test for the following class:
     * {@link com.sldeditor.filter.v2.function.temporal.TEquals}.
     */
    @Test
    public void testTEquals() {
        testClass(new TEquals(category));
    }

    /**
     * Unit test for the following class:
     * {@link com.sldeditor.filter.v2.function.temporal.TOverlaps}.
     */
    @Test
    public void testTOverlaps() {
        testClass(new TOverlaps(category));
    }

    /**
     * Unit test for the following class:
     * {@link com.sldeditor.filter.v2.function.temporal.testBegunBy}.
     */
    @Test
    public void testBegunBy() {
        testClass(new BegunBy(category));
    }

    /**
     * Unit test for the following class:
     * {@link com.sldeditor.filter.v2.function.temporal.EndedBy}.
     */
    @Test
    public void testEndedBy() {
        testClass(new EndedBy(category));
    }

    /**
     * Unit test for the following class:
     * {@link com.sldeditor.filter.v2.function.temporal.AnyInteracts}.
     */
    @Test
    public void testAnyInteracts() {
        testClass(new AnyInteracts(category));
    }

    /**
     * Unit test for the following class:
     * {@link com.sldeditor.filter.v2.function.temporal.Meets}.
     */
    @Test
    public void testMeets() {
        testClass(new Meets(category));
    }

    /**
     * Unit test for the following class:
     * {@link com.sldeditor.filter.v2.function.temporal.MetBy}.
     */
    @Test
    public void testMetBy() {
        testClass(new MetBy(category));
    }

    /**
     * Test class.
     *
     * @param objUnderTest the obj under test
     */
    private void testClass(FilterConfigInterface objUnderTest) {
        assertNotNull(objUnderTest.getFilterConfiguration());
        assertNotNull(objUnderTest.createFilter());
        assertNull(objUnderTest.createLogicFilter(null));

        BinaryTemporalOperatorImpl filter =
                (BinaryTemporalOperatorImpl)objUnderTest.createFilter(null);
        assertNull(filter.getExpression1());
        assertNull(filter.getExpression2());

        List<Expression> parameterList = new ArrayList<Expression>();
        parameterList.add(ff.literal("expr1"));

        filter = (BinaryTemporalOperatorImpl) objUnderTest.createFilter(parameterList);
        assertNull(filter.getExpression1());
        assertNull(filter.getExpression2());

        parameterList.add(ff.literal("expr2"));
        filter = (BinaryTemporalOperatorImpl) objUnderTest.createFilter(parameterList);
        assertNotNull(filter.getExpression1());
        assertNotNull(filter.getExpression2());
        assertTrue(objUnderTest.category().compareTo(category) == 0);

        System.out.println(filter.toString());
    }
}
