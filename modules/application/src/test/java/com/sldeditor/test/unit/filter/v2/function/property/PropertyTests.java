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

package com.sldeditor.test.unit.filter.v2.function.property;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.BinaryComparisonAbstract;
import org.junit.Test;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;

import com.sldeditor.filter.v2.function.FilterConfigInterface;
import com.sldeditor.filter.v2.function.property.IsBetween;
import com.sldeditor.filter.v2.function.property.IsEqualTo;
import com.sldeditor.filter.v2.function.property.IsGreaterThan;
import com.sldeditor.filter.v2.function.property.IsGreaterThanEqualTo;
import com.sldeditor.filter.v2.function.property.IsLessThan;
import com.sldeditor.filter.v2.function.property.IsLessThanEqualTo;
import com.sldeditor.filter.v2.function.property.IsNotEqualTo;

/**
 * Unit test for the following classes:
 * {@link com.sldeditor.filter.v2.function.property.IsBetween}.
 * {@link com.sldeditor.filter.v2.function.property.IsEqualTo}.
 * {@link com.sldeditor.filter.v2.function.property.IsGreaterThan}.
 * {@link com.sldeditor.filter.v2.function.property.IsGreaterThanEqualTo}.
 * {@link com.sldeditor.filter.v2.function.property.IsLessThan}.
 * {@link com.sldeditor.filter.v2.function.property.IsLessThanEqualTo}.
 * {@link com.sldeditor.filter.v2.function.property.IsNotEqualTo}.
 * 
 * @author Robert Ward (SCISYS)
 *
 */
public class PropertyTests {

    private FilterFactory ff = CommonFactoryFinder.getFilterFactory();

    /**
     * Unit test for the following class:
     * {@link com.sldeditor.filter.v2.function.property.IsEqualTo}.
     */
    @Test
    public void testIsEqualTo() {
        testClass(new IsEqualTo());
    }

    /**
     * Unit test for the following class:
     * {@link com.sldeditor.filter.v2.function.property.IsGreaterThan}.
     */
    @Test
    public void testIsGreaterThan() {
        testClass(new IsGreaterThan());
    }

    /**
     * Unit test for the following class:
     * {@link com.sldeditor.filter.v2.function.property.IsGreaterThanEqualTo}.
     */
    @Test
    public void testIsGreaterThanEqualTo() {
        testClass(new IsGreaterThanEqualTo());
    }

    /**
     * Unit test for the following class:
     * {@link com.sldeditor.filter.v2.function.property.IsLessThan}.
     */
    @Test
    public void testIsLessThan() {
        testClass(new IsLessThan());
    }

    /**
     * Unit test for the following class:
     * {@link com.sldeditor.filter.v2.function.property.IsLessThanEqualTo}.
     */
    @Test
    public void testIsLessThanEqualTo() {
        testClass(new IsLessThanEqualTo());
    }

    /**
     * Unit test for the following class:
     * {@link com.sldeditor.filter.v2.function.property.IsNotEqualTo}.
     */
    @Test
    public void testIsNotEqualTo() {
        testClass(new IsNotEqualTo());
    }

    /**
     * Unit test for the following class:
     * {@link com.sldeditor.filter.v2.function.property.IsBetween}.
     */
    @Test
    public void testIsBetween() {
        testClass(new IsBetween());
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

        BinaryComparisonAbstract filter = (BinaryComparisonAbstract)objUnderTest.createFilter(null);
        assertNull(filter.getExpression1());
        assertNull(filter.getExpression2());

        List<Expression> parameterList = new ArrayList<Expression>();
        parameterList.add(ff.literal("expr1"));

        filter = (BinaryComparisonAbstract) objUnderTest.createFilter(parameterList);
        assertNull(filter.getExpression1());
        assertNull(filter.getExpression2());

        parameterList.add(ff.literal("expr2"));
        parameterList.add(ff.literal(false));
        filter = (BinaryComparisonAbstract) objUnderTest.createFilter(parameterList);
        assertNotNull(filter.getExpression1());
        assertNotNull(filter.getExpression2());

        System.out.println(filter.toString());
    }
}
