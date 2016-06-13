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

import static org.junit.Assert.assertEquals;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.temporal.AfterImpl;
import org.geotools.filter.temporal.AnyInteractsImpl;
import org.geotools.filter.temporal.BeforeImpl;
import org.geotools.filter.temporal.BeginsImpl;
import org.geotools.filter.temporal.BegunByImpl;
import org.geotools.filter.temporal.DuringImpl;
import org.geotools.filter.temporal.EndedByImpl;
import org.geotools.filter.temporal.EndsImpl;
import org.geotools.filter.temporal.MeetsImpl;
import org.geotools.filter.temporal.MetByImpl;
import org.geotools.filter.temporal.OverlappedByImpl;
import org.geotools.filter.temporal.TContainsImpl;
import org.geotools.filter.temporal.TEqualsImpl;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;

import com.sldeditor.filter.v2.ValidFilterVisitor;

/**
 * The Class TestValidFilterVisitor.
 * 
 * @author Robert Ward (SCISYS)
 */
public class ValidFilterVisitorTest {

    private static ValidFilterVisitor target = null;
    private static FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();

    @BeforeClass
    public static void setUp()
    {
        target = new ValidFilterVisitor(ff);
    }
    

    @Test
    public void visit_A$PropertyIsBetween$Object() throws Exception {
        String expression1 = "expression1";
        String expression2 = "s1 34";
        
        Filter originalfilter = ff.between(ff.literal(expression1), ff.literal(expression2), ff.literal(expression2));
        String expectedFilterString = String.format("[ '%s' BETWEEN '%s' AND '%s' ]", expression1, expression2, expression2);

        Filter actualFilter = (Filter) originalfilter.accept(target, null);
        String actualString = actualFilter.toString();
        
        assertEquals(actualString, expectedFilterString);
    }
    
    @Test
    public void visit_A$PropertyIsEqualTo$Object() throws Exception {
        String expression1 = "expression1";
        String expression2 = "s1 34";
        
        Filter originalfilter = ff.equal(ff.literal(expression1), ff.literal(expression2));
        String expectedFilterString = String.format("[ '%s' equals '%s' ]", expression1, expression2);

        Filter actualFilter = (Filter) originalfilter.accept(target, null);
        String actualString = actualFilter.toString();
        
        assertEquals(actualString, expectedFilterString);
    }

    @Test
    public void visit_A$PropertyIsNotEqualTo$Object() throws Exception {
        String expression1 = "expression1";
        String expression2 = "s1 34";
        
        Filter originalfilter = ff.notEqual(ff.literal(expression1), ff.literal(expression2));
        String expectedFilterString = String.format("[ '%s' != '%s' ]", expression1, expression2);

        Filter actualFilter = (Filter) originalfilter.accept(target, null);
        String actualString = actualFilter.toString();
        
        assertEquals(actualString, expectedFilterString);
    }

    @Test
    public void visit_A$PropertyIsGreaterThan$Object() throws Exception {
        String expression1 = "expression1";
        String expression2 = "s1 34";
        
        Filter originalfilter = ff.greater(ff.literal(expression1), ff.literal(expression2));
        String expectedFilterString = String.format("[ '%s' > '%s' ]", expression1, expression2);

        Filter actualFilter = (Filter) originalfilter.accept(target, null);
        String actualString = actualFilter.toString();
        
        assertEquals(actualString, expectedFilterString);
    }

    @Test
    public void visit_A$PropertyIsGreaterThanOrEqualTo$Object() throws Exception {
        String expression1 = "expression1";
        String expression2 = "s1 34";
        
        Filter originalfilter = ff.greaterOrEqual(ff.literal(expression1), ff.literal(expression2));
        String expectedFilterString = String.format("[ '%s' >= '%s' ]", expression1, expression2);

        Filter actualFilter = (Filter) originalfilter.accept(target, null);
        String actualString = actualFilter.toString();
        
        assertEquals(actualString, expectedFilterString);
    }

    @Test
    public void visit_A$PropertyIsLessThan$Object() throws Exception {
        String expression1 = "expression1";
        String expression2 = "s1 34";
        
        Filter originalfilter = ff.less(ff.literal(expression1), ff.literal(expression2));
        String expectedFilterString = String.format("[ '%s' < '%s' ]", expression1, expression2);

        Filter actualFilter = (Filter) originalfilter.accept(target, null);
        String actualString = actualFilter.toString();
        
        assertEquals(actualString, expectedFilterString);
    }

    @Test
    public void visit_A$PropertyIsLessThanOrEqualTo$Object() throws Exception {
        String expression1 = "expression1";
        String expression2 = "s1 34";
        
        Filter originalfilter = ff.lessOrEqual(ff.literal(expression1), ff.literal(expression2));
        String expectedFilterString = String.format("[ '%s' <= '%s' ]", expression1, expression2);

        Filter actualFilter = (Filter) originalfilter.accept(target, null);
        String actualString = actualFilter.toString();
        
        assertEquals(actualString, expectedFilterString);
    }

    @Test
    public void visit_A$PropertyIsLike$Object() throws Exception {
        String expression1 = "expression1";
        String expression2 = "s1 34";
        
        Filter originalfilter = ff.like(ff.literal(expression1), expression2);
        String expectedFilterString = String.format("[ '%s' is like '%s' ]", expression1, expression2);

        Filter actualFilter = (Filter) originalfilter.accept(target, null);
        String actualString = actualFilter.toString();
        
        assertEquals(actualString, expectedFilterString);
    }

    @Test
    public void visit_A$Equals$Object() throws Exception {
        String expression1 = "expression1";
        String expression2 = "s1 34";
        
        Filter originalfilter = ff.equals(ff.literal(expression1), ff.literal(expression2));
        String expectedFilterString = String.format("[ '%s' = '%s' ]", expression1, expression2);

        Filter actualFilter = (Filter) originalfilter.accept(target, null);
        String actualString = actualFilter.toString();
        
        assertEquals(actualString, expectedFilterString);
    }

    @Test
    public void visit_A$After$Object() throws Exception {
        String expression1 = "expression1";
        String expression2 = "s1 34";
        
        Filter originalfilter = ff.after(ff.literal(expression1), ff.literal(expression2));
        String expectedFilterString = String.format("'%s'", expression2);

        AfterImpl actualFilter = (AfterImpl) originalfilter.accept(target, null);
        String actualString = actualFilter.getExpression2().toString();
        
        assertEquals(actualString, expectedFilterString);
    }

    @Test
    public void visit_A$AnyInteracts$Object() throws Exception {
        String expression1 = "expression1";
        String expression2 = "s1 34";
        
        Filter originalfilter = ff.anyInteracts(ff.literal(expression1), ff.literal(expression2));
        String expectedFilterString = String.format("'%s'", expression2);

        AnyInteractsImpl actualFilter = (AnyInteractsImpl) originalfilter.accept(target, null);
        String actualString = actualFilter.getExpression2().toString();
        
        assertEquals(actualString, expectedFilterString);
    }

    @Test
    public void visit_A$Before$Object() throws Exception {
        String expression1 = "expression1";
        String expression2 = "s1 34";
        
        Filter originalfilter = ff.before(ff.literal(expression1), ff.literal(expression2));
        String expectedFilterString = String.format("'%s'", expression2);

        BeforeImpl actualFilter = (BeforeImpl) originalfilter.accept(target, null);
        String actualString = actualFilter.getExpression2().toString();
        
        assertEquals(actualString, expectedFilterString);
    }

    @Test
    public void visit_A$Begins$Object() throws Exception {
        String expression1 = "expression1";
        String expression2 = "s1 34";
        
        Filter originalfilter = ff.begins(ff.literal(expression1), ff.literal(expression2));
        String expectedFilterString = String.format("'%s'", expression2);

        BeginsImpl actualFilter = (BeginsImpl) originalfilter.accept(target, null);
        String actualString = actualFilter.getExpression2().toString();
        
        assertEquals(actualString, expectedFilterString);
    }

    @Test
    public void visit_A$BegunBy$Object() throws Exception {
        String expression1 = "expression1";
        String expression2 = "s1 34";
        
        Filter originalfilter = ff.begunBy(ff.literal(expression1), ff.literal(expression2));
        String expectedFilterString = String.format("'%s'", expression2);

        BegunByImpl actualFilter = (BegunByImpl) originalfilter.accept(target, null);
        String actualString = actualFilter.getExpression2().toString();
        
        assertEquals(actualString, expectedFilterString);
    }

    @Test
    public void visit_A$During$Object() throws Exception {
        String expression1 = "expression1";
        String expression2 = "s1 34";
        
        Filter originalfilter = ff.during(ff.literal(expression1), ff.literal(expression2));
        String expectedFilterString = String.format("'%s'", expression2);

        DuringImpl actualFilter = (DuringImpl) originalfilter.accept(target, null);
        
        String actualString = actualFilter.getExpression2().toString();
        
        assertEquals(actualString, expectedFilterString);
    }

    @Test
    public void visit_A$EndedBy$Object() throws Exception {
        String expression1 = "expression1";
        String expression2 = "s1 34";
        
        Filter originalfilter = ff.endedBy(ff.literal(expression1), ff.literal(expression2));
        String expectedFilterString = String.format("'%s'", expression2);

        EndedByImpl actualFilter = (EndedByImpl) originalfilter.accept(target, null);
        String actualString = actualFilter.getExpression2().toString();
        
        assertEquals(actualString, expectedFilterString);
    }

    @Test
    public void visit_A$Ends$Object() throws Exception {
        String expression1 = "expression1";
        String expression2 = "s1 34";
        
        Filter originalfilter = ff.ends(ff.literal(expression1), ff.literal(expression2));
        String expectedFilterString = String.format("'%s'", expression2);

        EndsImpl actualFilter = (EndsImpl) originalfilter.accept(target, null);
        String actualString = actualFilter.getExpression2().toString();
        
        assertEquals(actualString, expectedFilterString);
    }

    @Test
    public void visit_A$Meets$Object() throws Exception {
        String expression1 = "expression1";
        String expression2 = "s1 34";
        
        Filter originalfilter = ff.meets(ff.literal(expression1), ff.literal(expression2));
        String expectedFilterString = String.format("'%s'", expression2);

        MeetsImpl actualFilter = (MeetsImpl) originalfilter.accept(target, null);
        String actualString = actualFilter.getExpression2().toString();
        
        assertEquals(actualString, expectedFilterString);
    }

    @Test
    public void visit_A$MetBy$Object() throws Exception {
        String expression1 = "expression1";
        String expression2 = "s1 34";
        
        Filter originalfilter = ff.metBy(ff.literal(expression1), ff.literal(expression2));
        String expectedFilterString = String.format("'%s'", expression2);

        MetByImpl actualFilter = (MetByImpl) originalfilter.accept(target, null);
        String actualString = actualFilter.getExpression2().toString();
        
        assertEquals(actualString, expectedFilterString);
    }

    @Test
    public void visit_A$OverlappedBy$Object() throws Exception {
        String expression1 = "expression1";
        String expression2 = "s1 34";
        
        Filter originalfilter = ff.overlappedBy(ff.literal(expression1), ff.literal(expression2));
        String expectedFilterString = String.format("'%s'", expression2);

        OverlappedByImpl actualFilter = (OverlappedByImpl) originalfilter.accept(target, null);
        String actualString = actualFilter.getExpression2().toString();
        
        assertEquals(actualString, expectedFilterString);
    }

    @Test
    public void visit_A$TContains$Object() throws Exception {
        String expression1 = "expression1";
        String expression2 = "s1 34";
        
        Filter originalfilter = ff.tcontains(ff.literal(expression1), ff.literal(expression2));
        String expectedFilterString = String.format("'%s'", expression2);

        TContainsImpl actualFilter = (TContainsImpl) originalfilter.accept(target, null);
        String actualString = actualFilter.getExpression2().toString();
        
        assertEquals(actualString, expectedFilterString);
    }

    @Test
    public void visit_A$TEquals$Object() throws Exception {
        String expression1 = "expression1";
        String expression2 = "s1 34";
        
        Filter originalfilter = ff.tequals(ff.literal(expression1), ff.literal(expression2));
        String expectedFilterString = String.format("'%s'", expression2);

        TEqualsImpl actualFilter = (TEqualsImpl) originalfilter.accept(target, null);
        String actualString = actualFilter.getExpression2().toString();
        
        assertEquals(actualString, expectedFilterString);
    }

}
