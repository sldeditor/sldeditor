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

package com.sldeditor.test.unit.ui.attribute;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.sldeditor.ui.attribute.AttributeUtils;

/**
 * The unit test for AttributeUtils.
 * 
 * <p>{@link com.sldeditor.ui.attribute.AttributeUtils}
 *
 * @author Robert Ward (SCISYS)
 */
public class AttributeUtilsTest {

    /**
     * Test method for {@link com.sldeditor.ui.attribute.AttributeUtils#isAttribute(java.lang.Object)}.
     */
    @Test
    public void testIsAttribute() {
        assertFalse(AttributeUtils.isAttribute(null));
        assertFalse(AttributeUtils.isAttribute(Integer.valueOf(42)));
        assertFalse(AttributeUtils.isAttribute("abc"));
        assertFalse(AttributeUtils.isAttribute("<ogc:PropertyName>xyz"));
        assertFalse(AttributeUtils.isAttribute("xyz</ogc:PropertyName>"));
        assertTrue(AttributeUtils.isAttribute("<ogc:PropertyName>xyz</ogc:PropertyName>"));
        assertTrue(AttributeUtils.isAttribute("<ogc:PropertyName></ogc:PropertyName>"));
    }

    /**
     * Test method for {@link com.sldeditor.ui.attribute.AttributeUtils#extract(java.lang.String)}.
     */
    @Test
    public void testExtract() {
        assertNull(AttributeUtils.extract(null));
        String objValue = "abc";
        assertTrue(objValue.compareTo(AttributeUtils.extract(objValue)) == 0);
        objValue = "<ogc:PropertyName>xyz";
        assertTrue(objValue.compareTo(AttributeUtils.extract(objValue)) == 0);

        objValue = "xyz</ogc:PropertyName>";
        assertTrue(objValue.compareTo(AttributeUtils.extract(objValue)) == 0);

        objValue = "<ogc:PropertyName>xyz</ogc:PropertyName>";
        assertTrue("xyz".compareTo(AttributeUtils.extract(objValue)) == 0);

        objValue = "<ogc:PropertyName></ogc:PropertyName>";
        assertTrue("".compareTo(AttributeUtils.extract(objValue)) == 0);
    }

}
