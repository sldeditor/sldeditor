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

package com.sldeditor.test.unit.datasource.attribute;

import static org.junit.Assert.*;

import org.junit.Test;

import com.sldeditor.datasource.attribute.AllowedAttributeTypes;

/**
 * Unit test for AllowedAttributeTypes class.
 * <p>{@link com.sldeditor.datasource.attribute.AllowedAttributeTypes}
 * 
 * @author Robert Ward (SCISYS)
 */
public class AllowedAttributeTypesTest {

    /**
     * Test method for {@link com.sldeditor.datasource.attribute.AllowedAttributeTypes#isAllowed(org.opengis.feature.type.PropertyDescriptor, java.lang.Class)}.
     */
    @Test
    public void testIsAllowed() {

        assertFalse(AllowedAttributeTypes.isAllowed(Double.class, AllowedAttributeTypes.class));

        assertTrue(AllowedAttributeTypes.isAllowed(Integer.class, Double.class));
        assertTrue(AllowedAttributeTypes.isAllowed(String.class, String.class));
        assertFalse(AllowedAttributeTypes.isAllowed(String.class, Double.class));

        assertFalse(AllowedAttributeTypes.isAllowed(Double.class, String.class));
        assertTrue(AllowedAttributeTypes.isAllowed(Integer.class, Double.class));
        assertTrue(AllowedAttributeTypes.isAllowed(Long.class, Double.class));
        assertTrue(AllowedAttributeTypes.isAllowed(Double.class, Double.class));
        assertTrue(AllowedAttributeTypes.isAllowed(Float.class, Double.class));
    }

}
