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

package com.sldeditor.test.unit.filter.v2.function.misc;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import com.sldeditor.filter.v2.function.FilterConfigInterface;
import com.sldeditor.filter.v2.function.misc.IsLike;
import com.sldeditor.filter.v2.function.misc.IsLike.IsLikeExtended;
import com.sldeditor.filter.v2.function.misc.IsNull;
import java.util.ArrayList;
import java.util.List;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.CompareFilterImpl;
import org.geotools.filter.text.cql2.CQL;
import org.geotools.filter.text.cql2.CQLException;
import org.junit.jupiter.api.Test;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;

/**
 * Unit test for the following classes: {@link com.sldeditor.filter.v2.function.misc.IsLike} {@link
 * com.sldeditor.filter.v2.function.misc.IsNull}
 *
 * @author Robert Ward (SCISYS)
 */
public class MiscTests {

    private FilterFactory ff = CommonFactoryFinder.getFilterFactory();
    private String category = "Test category";

    /** {@link com.sldeditor.filter.v2.function.misc.IsLike}. */
    @Test
    public void testIsLikeClass() {
        FilterConfigInterface objUnderTest = new IsLike(category);

        assertNotNull(objUnderTest.getFilterConfiguration());
        assertNotNull(objUnderTest.createFilter());
        assertNull(objUnderTest.createLogicFilter(null));

        IsLikeExtended filter = (IsLikeExtended) objUnderTest.createFilter(null);
        assertNull(filter.getLiteral());
        assertNull(filter.getExpression());

        List<Expression> parameterList = new ArrayList<Expression>();
        try {
            parameterList.add(ff.literal(CQL.toFilter("expr1 >= 5")));
        } catch (CQLException e) {
            fail(e.getStackTrace().toString());
        }
        filter = (IsLikeExtended) objUnderTest.createFilter(parameterList);
        assertNull(filter.getLiteral());
        assertNull(filter.getExpression());

        parameterList.add(ff.literal("expr2"));
        parameterList.add(ff.literal("expr3"));
        parameterList.add(ff.literal("expr4"));
        parameterList.add(ff.literal("expr5"));
        parameterList.add(ff.literal(true));

        filter = (IsLikeExtended) objUnderTest.createFilter(parameterList);
        assertNotNull(filter.getLiteral());
        assertNotNull(filter.getExpression());

        System.out.println(filter.toString());
    }

    /** {@link com.sldeditor.filter.v2.function.misc.IsNull}. */
    @Test
    public void testIsNullClass() {
        FilterConfigInterface objUnderTest = new IsNull(category);

        assertNotNull(objUnderTest.getFilterConfiguration());
        assertNotNull(objUnderTest.createFilter());
        assertNull(objUnderTest.createLogicFilter(null));

        CompareFilterImpl filter = (CompareFilterImpl) objUnderTest.createFilter(null);
        assertNull(filter.getExpression1());

        List<Expression> parameterList = new ArrayList<Expression>();
        filter = (CompareFilterImpl) objUnderTest.createFilter(parameterList);
        assertNull(filter.getExpression1());

        parameterList.add(ff.literal("expr1"));

        filter = (CompareFilterImpl) objUnderTest.createFilter(parameterList);
        assertNotNull(filter.getExpression1());
        assertTrue(objUnderTest.category().compareTo(category) == 0);

        System.out.println(filter.toString());
    }
}
