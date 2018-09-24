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

package com.sldeditor.test.unit.rendertransformation.types;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.rendertransformation.types.GeometryValues;
import com.sldeditor.ui.detail.config.FieldConfigBase;
import com.sldeditor.ui.detail.config.FieldConfigCommonData;
import com.sldeditor.ui.detail.config.FieldConfigGeometry;
import com.sldeditor.ui.detail.config.symboltype.SymbolTypeConfig;
import java.util.Arrays;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;

/**
 * Unit test for GeometryValues class.
 *
 * <p>{@link com.sldeditor.rendertransformation.test.GeometryValues}
 *
 * @author Robert Ward (SCISYS)
 */
class GeometryValuesTest {

    private FilterFactory ff = CommonFactoryFinder.getFilterFactory();

    class TestGeometryValues extends GeometryValues {

        /* (non-Javadoc)
         * @see com.sldeditor.rendertransformation.types.GeometryValues#populateSymbolType(com.sldeditor.ui.detail.config.symboltype.SymbolTypeConfig)
         */
        @Override
        protected void populateSymbolType(SymbolTypeConfig symbolTypeConfig) {
            super.populateSymbolType(symbolTypeConfig);
        }
    }

    /**
     * Test method for {@link
     * com.sldeditor.rendertransformation.types.GeometryValues#GeometryValues()}.
     */
    @Test
    void testGeometryValues() {
        GeometryValues testObj = new GeometryValues();
        testObj.createInstance();

        assertEquals(
                Arrays.asList(
                        Geometry.class,
                        LineString.class,
                        Point.class,
                        Polygon.class,
                        GridCoverage2D.class),
                testObj.getType());

        GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();

        Geometry geometry = null;
        WKTReader reader = new WKTReader(geometryFactory);
        try {
            Point point = (Point) reader.read("POINT (1 1)");
            geometry = point;
        } catch (ParseException e) {
            e.printStackTrace();
            fail("");
        }

        testObj.setDefaultValue(geometry);
        String p = geometry.toString();
        assertEquals(testObj.getExpression().toString(), p);

        // Geometry value
        testObj.setValue(geometry);
        assertEquals(testObj.getExpression().toString(), p);

        // Literal expression
        Expression expectedExpression = ff.literal(p);
        testObj.setValue(expectedExpression);
        assertEquals(testObj.getExpression().toString(), p);

        // Attribute expression
        expectedExpression = ff.property("test");
        testObj.setValue(expectedExpression);
        assertEquals(expectedExpression, testObj.getExpression());

        // Not set
        testObj.setValue("");
        assertNull(testObj.getExpression());

        FieldConfigBase field =
                testObj.getField(
                        new FieldConfigCommonData(
                                GeometryValues.class,
                                FieldIdEnum.INITIAL_GAP,
                                "label",
                                true,
                                false,
                                false));
        assertEquals(FieldConfigGeometry.class, field.getClass());

        // Increase code coverage
        TestGeometryValues testObj2 = new TestGeometryValues();
        testObj2.populateSymbolType(null);
    }
}
