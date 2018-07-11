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

package com.sldeditor.test.unit.filter.v2.expression;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.filter.v2.expression.ExpressionPanelv2;
import com.sldeditor.filter.v2.expression.FilterNode;
import com.sldeditor.filter.v2.function.FilterConfigInterface;
import com.sldeditor.filter.v2.function.geometry.Overlaps;
import com.sldeditor.filter.v2.function.logic.And;
import com.sldeditor.filter.v2.function.misc.IsLike;
import com.sldeditor.filter.v2.function.misc.IsNull;
import com.sldeditor.filter.v2.function.property.IsBetween;
import com.sldeditor.filter.v2.function.property.IsGreaterThan;
import com.sldeditor.filter.v2.function.temporal.After;
import java.text.ParseException;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.Hints;
import org.geotools.geometry.GeometryFactoryFinder;
import org.geotools.geometry.text.WKTParser;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.junit.jupiter.api.Test;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.geometry.Geometry;
import org.opengis.geometry.PositionFactory;
import org.opengis.geometry.aggregate.AggregateFactory;
import org.opengis.geometry.coordinate.GeometryFactory;
import org.opengis.geometry.primitive.PrimitiveFactory;

/**
 * Unit test for FilterNode class.
 *
 * <p>{@link com.sldeditor.filter.v2.expression.FilterNode}
 *
 * @author Robert Ward (SCISYS)
 */
public class FilterNodeTest {

    /**
     * Test method for {@link com.sldeditor.filter.v2.expression.FilterNode#FilterNode()}. Test
     * method for {@link com.sldeditor.filter.v2.expression.FilterNode#toString()}. Test method for
     * {@link com.sldeditor.filter.v2.expression.FilterNode#getFilter()}. Test method for {@link
     * com.sldeditor.filter.v2.expression.FilterNode#getType()}. Test method for {@link
     * com.sldeditor.filter.v2.expression.FilterNode#setType(java.lang.Class)}.
     */
    @Test
    public void testFilterNode() {
        FilterNode node = new FilterNode();
        assertNull(node.getFilter());

        assertTrue(
                node.toString()
                                .compareTo(
                                        Localisation.getString(
                                                ExpressionPanelv2.class, "FilterNode.filterNotSet"))
                        == 0);

        Class<?> type = Double.class;
        node.setType(type);
        assertEquals(type, node.getType());
    }

    /**
     * Test method for {@link
     * com.sldeditor.filter.v2.expression.FilterNode#setFilter(org.opengis.filter.Filter,
     * com.sldeditor.filter.v2.function.FilterConfigInterface)}.
     */
    @Test
    public void testSetFilter() {
        String category = "Test category";
        FilterFactory ff = CommonFactoryFinder.getFilterFactory();

        FilterNode node = new FilterNode();

        // BinaryComparisonAbstract
        Filter filter = ff.greaterOrEqual(ff.literal(45), ff.literal(23));
        node.setFilter(filter, null);
        node.addFilter();

        String actual = node.toString();
        String expected =
                "Filter : "
                        + Localisation.getString(
                                ExpressionPanelv2.class, "FilterNode.filterNotSet");
        assertTrue(actual.compareTo(expected) == 0);
        assertEquals(filter, node.getFilter());

        FilterConfigInterface filterConfig = new IsGreaterThan(category);
        node.setFilter(filter, filterConfig);

        assertEquals(filterConfig, node.getFilterConfig());
        expected = "Filter : PropertyIsGreaterThan";
        actual = node.toString();
        assertTrue(actual.compareTo(expected) == 0);
        assertTrue(filterConfig.category().compareTo(category) == 0);

        // PropertyIsLike
        filter = ff.like(ff.literal("abc def ghi"), "abc");
        filterConfig = new IsLike(category);
        node.setFilter(filter, filterConfig);
        expected = "Filter : Like";
        actual = node.toString();
        assertTrue(actual.compareTo(expected) == 0);
        assertTrue(filterConfig.category().compareTo(category) == 0);

        // BinarySpatialOperator
        Hints hints = new Hints(Hints.CRS, DefaultGeographicCRS.WGS84);

        PositionFactory positionFactory = GeometryFactoryFinder.getPositionFactory(hints);
        GeometryFactory geometryFactory = GeometryFactoryFinder.getGeometryFactory(hints);
        PrimitiveFactory primitiveFactory = GeometryFactoryFinder.getPrimitiveFactory(hints);
        AggregateFactory aggregateFactory = GeometryFactoryFinder.getAggregateFactory(hints);

        WKTParser wktParser =
                new WKTParser(geometryFactory, primitiveFactory, positionFactory, aggregateFactory);
        Geometry geometry = null;
        try {
            geometry = wktParser.parse("POINT( 48.44 -123.37)");
        } catch (ParseException e) {
            fail(e.getStackTrace().toString());
        }
        filter = ff.overlaps("property", geometry);
        filterConfig = new Overlaps(category);
        node.setFilter(filter, filterConfig);
        expected = "Filter : Overlaps";
        actual = node.toString();
        assertTrue(actual.compareTo(expected) == 0);
        assertTrue(filterConfig.category().compareTo(category) == 0);

        // Is Between
        filter = ff.between(ff.literal(25), ff.literal(5), ff.literal(50));
        filterConfig = new IsBetween(category);
        node.setFilter(filter, filterConfig);
        expected = "Filter : PropertyIsBetween";
        actual = node.toString();
        assertTrue(actual.compareTo(expected) == 0);
        assertTrue(filterConfig.category().compareTo(category) == 0);

        // Is Null
        filter = ff.isNull(ff.literal(12));
        filterConfig = new IsNull(category);
        node.setFilter(filter, filterConfig);
        expected = "Filter : IsNull";
        actual = node.toString();
        assertTrue(actual.compareTo(expected) == 0);
        assertTrue(filterConfig.category().compareTo(category) == 0);

        // BinaryTemporalOperator
        filter = ff.after(ff.literal(12), ff.literal(312));
        filterConfig = new After(category);
        node.setFilter(filter, filterConfig);
        expected = "Filter : After";
        actual = node.toString();
        assertTrue(actual.compareTo(expected) == 0);
        assertTrue(filterConfig.category().compareTo(category) == 0);

        // Logic filter
        filter =
                ff.and(
                        ff.after(ff.literal(12), ff.literal(312)),
                        ff.between(ff.literal(25), ff.literal(5), ff.literal(50)));
        filterConfig = new And(category);
        node.setFilter(filter, filterConfig);
        expected = "Filter : And";
        actual = node.toString();
        assertTrue(actual.compareTo(expected) == 0);
        assertTrue(filterConfig.category().compareTo(category) == 0);

        node.addFilter();
    }

    /**
     * Test method for {@link
     * com.sldeditor.filter.v2.expression.FilterNode#setName(java.lang.String)}.
     */
    @Test
    public void testSetName() {
        FilterNode node = new FilterNode();
        assertNull(node.getFilter());

        String name = "filtername";
        node.setName(name);
        String expected =
                name
                        + " : "
                        + Localisation.getString(
                                ExpressionPanelv2.class, "FilterNode.filterNotSet");
        assertTrue(node.toString().compareTo(expected) == 0);
    }
}
