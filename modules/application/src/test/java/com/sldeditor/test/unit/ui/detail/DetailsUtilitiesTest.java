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

package com.sldeditor.test.unit.ui.detail;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.styling.AnchorPoint;
import org.geotools.styling.AnchorPointImpl;
import org.geotools.styling.Displacement;
import org.geotools.styling.DisplacementImpl;
import org.junit.Test;
import org.opengis.filter.FilterFactory;

import com.sldeditor.ui.detail.DetailsUtilities;

/**
 * The unit test for DetailsUtilities.
 * 
 * <p>{@link com.sldeditor.ui.detail.DetailsUtilities}
 *
 * @author Robert Ward (SCISYS)
 */
public class DetailsUtilitiesTest {

    /**
     * Test method for {@link com.sldeditor.ui.detail.DetailsUtilities#isSame(org.geotools.styling.Displacement, org.geotools.styling.Displacement)}.
     */
    @Test
    public void testIsSameDisplacementDisplacement() {
        assertFalse(DetailsUtilities.isSame((Displacement)null, (Displacement)null));
        FilterFactory ff = CommonFactoryFinder.getFilterFactory();

        // Try values that are the same
        Displacement displacement1 = new DisplacementImpl();
        displacement1.setDisplacementX(ff.literal(42));
        displacement1.setDisplacementY(ff.literal(-2));

        Displacement displacement2 = new DisplacementImpl();
        displacement2.setDisplacementX(ff.literal("42"));
        displacement2.setDisplacementY(ff.literal(-2));

        assertTrue(DetailsUtilities.isSame(displacement1, displacement2));

        // Try values that are not the same
        Displacement displacement3 = new DisplacementImpl();
        displacement3.setDisplacementX(ff.literal(1));
        displacement3.setDisplacementY(ff.literal(-2));
        assertFalse(DetailsUtilities.isSame(displacement1, displacement3));
        assertFalse(DetailsUtilities.isSame(displacement2, displacement3));

        Displacement displacement4 = new DisplacementImpl();
        displacement4.setDisplacementX(ff.literal((Long)1L));
        displacement4.setDisplacementY(ff.literal(-2));
        assertFalse(DetailsUtilities.isSame(displacement1, displacement4));
        assertFalse(DetailsUtilities.isSame(displacement2, displacement4));
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.DetailsUtilities#isSame(org.geotools.styling.AnchorPoint, org.geotools.styling.AnchorPoint)}.
     */
    @Test
    public void testIsSameAnchorPointAnchorPoint() {
        assertFalse(DetailsUtilities.isSame((AnchorPoint)null, (AnchorPoint)null));

        FilterFactory ff = CommonFactoryFinder.getFilterFactory();

        // Try values that are the same
        AnchorPoint anchorPoint1 = new AnchorPointImpl();
        anchorPoint1.setAnchorPointX(ff.literal(0.58));
        anchorPoint1.setAnchorPointY(ff.literal(0.1));

        AnchorPoint anchorPoint2 = new AnchorPointImpl();
        anchorPoint2.setAnchorPointX(ff.literal(0.58));
        anchorPoint2.setAnchorPointY(ff.literal("0.1"));

        assertTrue(DetailsUtilities.isSame(anchorPoint1, anchorPoint2));

        // Try values that are not the same
        AnchorPoint anchorPoint3 = new AnchorPointImpl();
        anchorPoint3.setAnchorPointX(ff.literal(1.0));
        anchorPoint3.setAnchorPointY(ff.literal(0.1));
        assertFalse(DetailsUtilities.isSame(anchorPoint1, anchorPoint3));
        assertFalse(DetailsUtilities.isSame(anchorPoint2, anchorPoint3));
    }

}
