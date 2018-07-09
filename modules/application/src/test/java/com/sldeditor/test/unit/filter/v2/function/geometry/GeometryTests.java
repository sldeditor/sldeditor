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

package com.sldeditor.test.unit.filter.v2.function.geometry;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.sldeditor.filter.v2.function.FilterConfigInterface;
import com.sldeditor.filter.v2.function.geometry.BBox;
import com.sldeditor.filter.v2.function.geometry.Contains;
import com.sldeditor.filter.v2.function.geometry.Crosses;
import com.sldeditor.filter.v2.function.geometry.DWithin;
import com.sldeditor.filter.v2.function.geometry.Disjoint;
import com.sldeditor.filter.v2.function.geometry.Equals;
import com.sldeditor.filter.v2.function.geometry.Intersects;
import com.sldeditor.filter.v2.function.geometry.Overlaps;
import com.sldeditor.filter.v2.function.geometry.Touches;
import com.sldeditor.filter.v2.function.geometry.Within;
import java.util.ArrayList;
import java.util.List;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.GeometryFilterImpl;
import org.junit.Test;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;

/**
 * Unit test for the following classes:
 *
 * <p>{@link com.sldeditor.filter.v2.function.geometry.BBox}.
 *
 * <p>{@link com.sldeditor.filter.v2.function.geometry.Contains}.
 *
 * <p>{@link com.sldeditor.filter.v2.function.geometry.Crosses}.
 *
 * <p>{@link com.sldeditor.filter.v2.function.geometry.Disjoint}.
 *
 * <p>{@link com.sldeditor.filter.v2.function.geometry.DWithin}.
 *
 * <p>{@link com.sldeditor.filter.v2.function.geometry.Equals}.
 *
 * <p>{@link com.sldeditor.filter.v2.function.geometry.Intersects}.
 *
 * <p>{@link com.sldeditor.filter.v2.function.geometry.Overlaps}.
 *
 * <p>{@link com.sldeditor.filter.v2.function.geometry.Touches}.
 *
 * <p>{@link com.sldeditor.filter.v2.function.geometry.Within}.
 *
 * @author Robert Ward (SCISYS)
 */
public class GeometryTests {

    private FilterFactory ff = CommonFactoryFinder.getFilterFactory();
    private String category = "Test category";

    /** {@link com.sldeditor.filter.v2.function.geometry.BBox}. */
    @Test
    public void testBBox() {
        testClass(new BBox(category));
    }

    /** {@link com.sldeditor.filter.v2.function.geometry.Contains}. */
    @Test
    public void testContains() {
        testClass(new Contains(category));
    }

    /** {@link com.sldeditor.filter.v2.function.geometry.Crosses}. */
    @Test
    public void testCrosses() {
        testClass(new Crosses(category));
    }

    /** {@link com.sldeditor.filter.v2.function.geometry.Disjoint}. */
    @Test
    public void testDisjoint() {
        testClass(new Disjoint(category));
    }

    /** {@link com.sldeditor.filter.v2.function.geometry.DWithin}. */
    @Test
    public void testDWithin() {
        testClass(new DWithin(category));
    }

    /** {@link com.sldeditor.filter.v2.function.geometry.Equals}. */
    @Test
    public void testEquals() {
        testClass(new Equals(category));
    }

    /** {@link com.sldeditor.filter.v2.function.geometry.Intersects}. */
    @Test
    public void testIntersects() {
        testClass(new Intersects(category));
    }

    /** {@link com.sldeditor.filter.v2.function.geometry.Overlaps}. */
    @Test
    public void testOverlaps() {
        testClass(new Overlaps(category));
    }

    /** {@link com.sldeditor.filter.v2.function.geometry.Touches}. */
    @Test
    public void testTouches() {
        testClass(new Touches(category));
    }

    /** {@link com.sldeditor.filter.v2.function.geometry.Within}. */
    @Test
    public void testWithin() {
        testClass(new Within(category));
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

        GeometryFilterImpl filter = (GeometryFilterImpl) objUnderTest.createFilter(null);
        assertNull(filter.getExpression1());
        assertNull(filter.getExpression2());

        List<Expression> parameterList = new ArrayList<Expression>();
        parameterList.add(ff.literal("expr1"));

        filter = (GeometryFilterImpl) objUnderTest.createFilter(parameterList);
        assertNull(filter.getExpression1());
        assertNull(filter.getExpression2());

        parameterList.add(ff.literal("expr2"));
        filter = (GeometryFilterImpl) objUnderTest.createFilter(parameterList);
        assertNotNull(filter.getExpression1());
        assertNotNull(filter.getExpression2());
        assertTrue(objUnderTest.category().compareTo(category) == 0);

        System.out.println(filter.toString());
    }
}
