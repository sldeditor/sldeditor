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

package com.sldeditor.test.unit.ui.detail.vendor.geoserver.marker.arrow;

import static org.junit.Assert.assertEquals;

import com.sldeditor.ui.detail.vendor.geoserver.marker.arrow.ArrowUtils;
import org.geotools.factory.CommonFactoryFinder;
import org.junit.Test;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;

/**
 * The unit test for ArrowUtils.
 *
 * <p>{@link com.sldeditor.ui.detail.vendor.geoserver.marker.arrow.ArrowUtils}
 *
 * @author Robert Ward (SCISYS)
 */
public class ArrowUtilsTest {

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.marker.arrow.ArrowUtils#getArrowPrefix()}.
     */
    @Test
    public void testGetArrowPrefix() {
        assertEquals("extshape://arrow", ArrowUtils.getArrowPrefix());
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.marker.arrow.ArrowUtils#encode(org.opengis.filter.expression.Expression,
     * org.opengis.filter.expression.Expression, org.opengis.filter.expression.Expression)}.
     */
    @Test
    public void testEncode() {
        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);

        assertEquals(
                "extshape://arrow?hr=1.2&t=0.34&ab=0.56",
                ArrowUtils.encode(ff.literal(1.2), ff.literal(0.34), ff.literal(0.56)));
        assertEquals(
                "extshape://arrow?hr=2.0&t=0.34&ab=0.56",
                ArrowUtils.encode(null, ff.literal(0.34), ff.literal(0.56)));
        assertEquals(
                "extshape://arrow?hr=1.2&t=0.2&ab=0.56",
                ArrowUtils.encode(ff.literal(1.2), null, ff.literal(0.56)));
        assertEquals(
                "extshape://arrow?hr=1.2&t=0.34&ab=0.5",
                ArrowUtils.encode(ff.literal(1.2), ff.literal(0.34), null));
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.marker.arrow.ArrowUtils#decodeArrowThickness(java.lang.String)}.
     */
    @Test
    public void testDecodeArrowThickness() {
        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);

        Expression hr = ff.literal(1.2);
        Expression t = ff.literal(0.34);
        Expression ab = ff.literal(0.56);
        String expectedString = ArrowUtils.encode(hr, t, ab);

        Expression actual = ArrowUtils.decodeArrowThickness(expectedString);
        assertEquals(t.toString(), actual.toString());

        actual = ArrowUtils.decodeArrowThickness("abcdefg");
        assertEquals("0.2", actual.toString());

        actual = ArrowUtils.decodeHeightOverWidth(expectedString);
        assertEquals(hr.toString(), actual.toString());

        actual = ArrowUtils.decodeHeightOverWidth("abcdefg");
        assertEquals("2.0", actual.toString());

        actual = ArrowUtils.decodeHeadBaseRatio(expectedString);
        assertEquals(ab.toString(), actual.toString());

        actual = ArrowUtils.decodeHeadBaseRatio("abcdefg");
        assertEquals("0.5", actual.toString());
    }
}
