/*
 * SLD Editor - The Open Source Java SLD Editor
 *
 * Copyright (C) 2018, SCISYS UK Limited
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

package com.sldeditor.test.unit.filter.v2.expression;

import static org.junit.Assert.assertEquals;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.function.DefaultFunctionFactory;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.temporal.object.DefaultInstant;
import org.geotools.temporal.object.DefaultPosition;
import org.junit.Test;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Function;
import org.opengis.filter.identity.Version;
import org.opengis.temporal.Instant;

import com.sldeditor.common.vendoroption.VersionData;
import com.sldeditor.filter.v2.expression.FilterPanelv2;

/**
 * Unit test for FilterPanelv2 class.
 * 
 * <p>
 * {@link com.sldeditor.filter.v2.expression.FilterPanelv2}
 * 
 * @author Robert Ward (SCISYS)
 *
 */
public class TestFilterPanelv2 {

    private FilterFactory ff = CommonFactoryFinder.getFilterFactory();
    private FilterFactory2 ff2 = CommonFactoryFinder.getFilterFactory2();

    private DefaultFunctionFactory functionFactory = new DefaultFunctionFactory();

    class TestFilterPanelv2Dialog extends FilterPanelv2 {

        /** The Constant serialVersionUID. */
        private static final long serialVersionUID = 1L;

        public TestFilterPanelv2Dialog(List<VersionData> vendorOptionList) {
            super(vendorOptionList);
        }

        protected void filterDialog(Class<?> type, Filter filter) {
            internalFilterDialog(type, filter);
        }
    };

    @Test
    public void testInFunction() {

        TestFilterPanelv2Dialog testPanel = new TestFilterPanelv2Dialog(null);
        testPanel.configure("test", String.class, false);

        List<Expression> expList = new ArrayList<Expression>();
        expList.add(ff.literal(1));
        expList.add(ff.literal(2));

        Function func = functionFactory.function("in", expList, null);
        Filter filter = ff.equal(func, ff.literal(true), true);
        String expected = filter.toString();
        testPanel.filterDialog(String.class, filter);

        String actual = testPanel.getFilterString();

        assertEquals(expected, actual);
    }

    @Test
    public void testLogicFilterNot() {
        TestFilterPanelv2Dialog testPanel = new TestFilterPanelv2Dialog(null);
        testPanel.configure("test", String.class, false);

        Filter filter = ff.not(ff.notEqual(ff.property("code"), ff.literal("approved"), false));
        String expected = filter.toString();
        testPanel.filterDialog(String.class, filter);

        String actual = testPanel.getFilterString();

        assertEquals(expected, actual);
    }

    @Test
    public void testLogicFilterOR() {
        TestFilterPanelv2Dialog testPanel = new TestFilterPanelv2Dialog(null);
        testPanel.configure("test", String.class, false);

        Filter filter = ff.or(ff.less(ff.property("code"), ff.literal("approved"), false),
                ff.greater(ff.property("funding"), ff.literal(23000)));
        String expected = filter.toString();
        testPanel.filterDialog(String.class, filter);

        String actual = testPanel.getFilterString();

        assertEquals(expected, actual);
    }

    @Test
    public void testLogicFilterAND() {
        TestFilterPanelv2Dialog testPanel = new TestFilterPanelv2Dialog(null);
        testPanel.configure("test", String.class, false);

        Filter filter = ff.and(ff.lessOrEqual(ff.property("code"), ff.literal("approved"), false),
                ff.greaterOrEqual(ff.property("funding"), ff.literal(23000)));
        String expected = filter.toString();
        testPanel.filterDialog(String.class, filter);

        String actual = testPanel.getFilterString();

        assertEquals(expected, actual);
    }

    @Test
    public void testBinaryTemporal() {
        TestFilterPanelv2Dialog testPanel = new TestFilterPanelv2Dialog(null);
        testPanel.configure("test", String.class, false);

        DateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        Date date1 = null;
        try {
            date1 = FORMAT.parse("2001-07-05T12:08:56.235-0700");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Instant temporalInstant = new DefaultInstant(new DefaultPosition(date1));

        // Simple check if property is after provided temporal instant
        Filter filter = ff.after(ff.property("date"), ff.literal(temporalInstant));

        String expected = filter.toString();
        testPanel.filterDialog(String.class, filter);

        String actual = testPanel.getFilterString();

        assertEquals(expected, actual);
    }

    @Test
    public void testLike() {
        TestFilterPanelv2Dialog testPanel = new TestFilterPanelv2Dialog(null);
        testPanel.configure("test", String.class, false);

        Filter filter = ff.like(ff.property("code"), "2300%");
        String expected = filter.toString();
        testPanel.filterDialog(String.class, filter);

        String actual = testPanel.getFilterString();

        assertEquals(expected, actual);
    }

    /**
     * Test method for {@link com.sldeditor.filter.v2.expression.FilterPanelv2#getFilter()}.
     */
    @Test
    public void testIsNull() {
        TestFilterPanelv2Dialog testPanel = new TestFilterPanelv2Dialog(null);
        testPanel.configure("test", String.class, false);

        Filter filter = ff.isNull(ff.property("approved"));
        String expected = filter.toString();
        testPanel.filterDialog(String.class, filter);

        String actual = testPanel.getFilterString();

        assertEquals(expected, actual);
    }

    @Test
    public void testFID() {
        TestFilterPanelv2Dialog testPanel = new TestFilterPanelv2Dialog(null);
        testPanel.configure("test", String.class, false);

        Filter filter = ff2.id(ff.featureId("CITY.98734597823459687235"),
                ff.featureId("CITY.98734592345235823474"));

        String expected = filter.toString();
        testPanel.filterDialog(String.class, filter);

        String actual = testPanel.getFilterString();

        assertEquals(expected.replace(" ", ""), actual.replace(" ", ""));
    }

    @Test
    public void testResourceId() {
        TestFilterPanelv2Dialog testPanel = new TestFilterPanelv2Dialog(null);
        testPanel.configure("test", String.class, false);

        Filter filter = ff2.id( ff.resourceId("CITY.98734597823459687235","A457", new Version()) );
        String expected = filter.toString();
        testPanel.filterDialog(String.class, filter);

        String actual = testPanel.getFilterString();

        assertEquals(expected.replace(" ", ""), actual.replace(" ", ""));
    }

    @Test
    public void testBBox() {
        TestFilterPanelv2Dialog testPanel = new TestFilterPanelv2Dialog(null);
        testPanel.configure("test", String.class, false);

        ReferencedEnvelope bbox = new ReferencedEnvelope(-1.0,1.0,-1.9,1.0, DefaultGeographicCRS.WGS84 );
        Filter filter = ff2.bbox(ff.property("the_geom"), bbox);
        String expected = filter.toString();
        testPanel.filterDialog(String.class, filter);

        String actual = testPanel.getFilterString();

        assertEquals(expected, actual);
    }

    @Test
    public void testContains() {
        TestFilterPanelv2Dialog testPanel = new TestFilterPanelv2Dialog(null);
        testPanel.configure("test", String.class, false);

        ReferencedEnvelope bbox = new ReferencedEnvelope(-1.0,1.0,-1.9,1.0, DefaultGeographicCRS.WGS84 );
        Filter filter = ff2.contains(ff.property("the_geom"), ff.literal(bbox));
        String expected = filter.toString();
        testPanel.filterDialog(String.class, filter);

        String actual = testPanel.getFilterString();

        assertEquals(expected, actual);
    }

    @Test
    public void testIntersects() {
        TestFilterPanelv2Dialog testPanel = new TestFilterPanelv2Dialog(null);
        testPanel.configure("test", String.class, false);

        ReferencedEnvelope bbox = new ReferencedEnvelope(-1.0,1.0,-1.9,1.0, DefaultGeographicCRS.WGS84 );
        Filter filter = ff2.intersects(ff.property("the_geom"), ff.literal(bbox));
        String expected = filter.toString();
        testPanel.filterDialog(String.class, filter);

        String actual = testPanel.getFilterString();

        assertEquals(expected, actual);
    }

    @Test
    public void testCrosses() {
        TestFilterPanelv2Dialog testPanel = new TestFilterPanelv2Dialog(null);
        testPanel.configure("test", String.class, false);

        ReferencedEnvelope bbox = new ReferencedEnvelope(-1.0,1.0,-1.9,1.0, DefaultGeographicCRS.WGS84 );
        Filter filter = ff2.crosses(ff.property("the_geom"), ff.literal(bbox));
        String expected = filter.toString();
        testPanel.filterDialog(String.class, filter);

        String actual = testPanel.getFilterString();

        assertEquals(expected, actual);
    }

    @Test
    public void testDisjoint() {
        TestFilterPanelv2Dialog testPanel = new TestFilterPanelv2Dialog(null);
        testPanel.configure("test", String.class, false);

        ReferencedEnvelope bbox = new ReferencedEnvelope(-1.0,1.0,-1.9,1.0, DefaultGeographicCRS.WGS84 );
        Filter filter = ff2.disjoint(ff.property("the_geom"), ff.literal(bbox));
        String expected = filter.toString();
        testPanel.filterDialog(String.class, filter);

        String actual = testPanel.getFilterString();

        assertEquals(expected, actual);
    }

    @Test
    public void testDWithin() {
        TestFilterPanelv2Dialog testPanel = new TestFilterPanelv2Dialog(null);
        testPanel.configure("test", String.class, false);

        ReferencedEnvelope bbox = new ReferencedEnvelope(-1.0,1.0,-1.9,1.0, DefaultGeographicCRS.WGS84 );
        Filter filter = ff2.dwithin(ff.property("the_geom"), ff.literal(bbox), 10.0, "km");
        String expected = filter.toString();
        testPanel.filterDialog(String.class, filter);

        String actual = testPanel.getFilterString();

        assertEquals(expected, actual);
    }

    @Test
    public void testBeyond() {
        TestFilterPanelv2Dialog testPanel = new TestFilterPanelv2Dialog(null);
        testPanel.configure("test", String.class, false);

        ReferencedEnvelope bbox = new ReferencedEnvelope(-1.0,1.0,-1.9,1.0, DefaultGeographicCRS.WGS84 );
        Filter filter = ff2.beyond(ff.property("the_geom"), ff.literal(bbox), 10.0, "km");
        String expected = filter.toString();
        testPanel.filterDialog(String.class, filter);

        String actual = testPanel.getFilterString();

        assertEquals(expected, actual);
    }
    
    @Test
    public void testEquals() {
        TestFilterPanelv2Dialog testPanel = new TestFilterPanelv2Dialog(null);
        testPanel.configure("test", String.class, false);

        ReferencedEnvelope bbox = new ReferencedEnvelope(-1.0,1.0,-1.9,1.0, DefaultGeographicCRS.WGS84 );
        Filter filter = ff2.equals(ff.property("the_geom"), ff.literal(bbox));
        String expected = filter.toString();
        testPanel.filterDialog(String.class, filter);

        String actual = testPanel.getFilterString();

        assertEquals(expected, actual);
    }

    @Test
    public void testOverlaps() {
        TestFilterPanelv2Dialog testPanel = new TestFilterPanelv2Dialog(null);
        testPanel.configure("test", String.class, false);

        ReferencedEnvelope bbox = new ReferencedEnvelope(-1.0,1.0,-1.9,1.0, DefaultGeographicCRS.WGS84 );
        Filter filter = ff2.overlaps(ff.property("the_geom"), ff.literal(bbox));
        String expected = filter.toString();
        testPanel.filterDialog(String.class, filter);

        String actual = testPanel.getFilterString();

        assertEquals(expected, actual);
    }

    @Test
    public void testTouches() {
        TestFilterPanelv2Dialog testPanel = new TestFilterPanelv2Dialog(null);
        testPanel.configure("test", String.class, false);

        ReferencedEnvelope bbox = new ReferencedEnvelope(-1.0,1.0,-1.9,1.0, DefaultGeographicCRS.WGS84 );
        Filter filter = ff2.touches(ff.property("the_geom"), ff.literal(bbox));
        String expected = filter.toString();
        testPanel.filterDialog(String.class, filter);

        String actual = testPanel.getFilterString();

        assertEquals(expected, actual);
    }

    @Test
    public void testWithin() {
        TestFilterPanelv2Dialog testPanel = new TestFilterPanelv2Dialog(null);
        testPanel.configure("test", String.class, false);

        ReferencedEnvelope bbox = new ReferencedEnvelope(-1.0,1.0,-1.9,1.0, DefaultGeographicCRS.WGS84 );
        Filter filter = ff2.within(ff.property("the_geom"), ff.literal(bbox));
        String expected = filter.toString();
        testPanel.filterDialog(String.class, filter);

        String actual = testPanel.getFilterString();

        assertEquals(expected, actual);
    }
    
    // Temporal
    @Test
    public void testAfter() {
        TestFilterPanelv2Dialog testPanel = new TestFilterPanelv2Dialog(null);
        testPanel.configure("test", String.class, false);

        Filter filter = ff.after(ff.property("age"), ff.literal(1));
        String expected = filter.toString();
        testPanel.filterDialog(String.class, filter);

        String actual = testPanel.getFilterString();

        assertEquals(expected, actual);
    }

    @Test
    public void testBefore() {
        TestFilterPanelv2Dialog testPanel = new TestFilterPanelv2Dialog(null);
        testPanel.configure("test", String.class, false);

        Filter filter = ff.before(ff.property("age"), ff.literal(1));
        String expected = filter.toString();
        testPanel.filterDialog(String.class, filter);

        String actual = testPanel.getFilterString();

        assertEquals(expected, actual);
    }

    @Test
    public void testBetween() {
        TestFilterPanelv2Dialog testPanel = new TestFilterPanelv2Dialog(null);
        testPanel.configure("test", String.class, false);

        Filter filter = ff.between(ff.property("age"), ff.literal(1), ff.literal(10));
        String expected = filter.toString();
        testPanel.filterDialog(String.class, filter);

        String actual = testPanel.getFilterString();

        assertEquals(expected, actual);
    }

    @Test
    public void testBegunBy() {
        TestFilterPanelv2Dialog testPanel = new TestFilterPanelv2Dialog(null);
        testPanel.configure("test", String.class, false);

        Filter filter = ff.begunBy(ff.property("age"), ff.literal(1));
        String expected = filter.toString();
        testPanel.filterDialog(String.class, filter);

        String actual = testPanel.getFilterString();

        assertEquals(expected, actual);
    }

    @Test
    public void testEndedBy() {
        TestFilterPanelv2Dialog testPanel = new TestFilterPanelv2Dialog(null);
        testPanel.configure("test", String.class, false);

        Filter filter = ff.endedBy(ff.property("age"), ff.literal(1));
        String expected = filter.toString();
        testPanel.filterDialog(String.class, filter);

        String actual = testPanel.getFilterString();

        assertEquals(expected, actual);
    }

    @Test
    public void testMeets() {
        TestFilterPanelv2Dialog testPanel = new TestFilterPanelv2Dialog(null);
        testPanel.configure("test", String.class, false);

        Filter filter = ff.meets(ff.property("age"), ff.literal(1));
        String expected = filter.toString();
        testPanel.filterDialog(String.class, filter);

        String actual = testPanel.getFilterString();

        assertEquals(expected, actual);
    }

    @Test
    public void testMetBy() {
        TestFilterPanelv2Dialog testPanel = new TestFilterPanelv2Dialog(null);
        testPanel.configure("test", String.class, false);

        Filter filter = ff.metBy(ff.property("age"), ff.literal(1));
        String expected = filter.toString();
        testPanel.filterDialog(String.class, filter);

        String actual = testPanel.getFilterString();

        assertEquals(expected, actual);
    }

    @Test
    public void testEnds() {
        TestFilterPanelv2Dialog testPanel = new TestFilterPanelv2Dialog(null);
        testPanel.configure("test", String.class, false);

        Filter filter = ff.ends(ff.property("age"), ff.literal(1));
        String expected = filter.toString();
        testPanel.filterDialog(String.class, filter);

        String actual = testPanel.getFilterString();

        assertEquals(expected, actual);
    }

    @Test
    public void testOverlappedBy() {
        TestFilterPanelv2Dialog testPanel = new TestFilterPanelv2Dialog(null);
        testPanel.configure("test", String.class, false);

        Filter filter = ff.overlappedBy(ff.property("age"), ff.literal(1));
        String expected = filter.toString();
        testPanel.filterDialog(String.class, filter);

        String actual = testPanel.getFilterString();

        assertEquals(expected, actual);
    }


    @Test
    public void testAnyInteracts() {
        TestFilterPanelv2Dialog testPanel = new TestFilterPanelv2Dialog(null);
        testPanel.configure("test", String.class, false);

        Filter filter = ff.anyInteracts(ff.property("age"), ff.literal(1));
        String expected = filter.toString();
        testPanel.filterDialog(String.class, filter);

        String actual = testPanel.getFilterString();

        assertEquals(expected, actual);
    }
}
