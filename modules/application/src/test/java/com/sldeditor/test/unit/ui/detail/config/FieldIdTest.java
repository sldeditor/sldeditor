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
package com.sldeditor.test.unit.ui.detail.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.ui.detail.config.FieldId;

/**
 * The unit test for FieldId.
 * <p>{@link com.sldeditor.ui.detail.config.FieldId}
 *
 * @author Robert Ward (SCISYS)
 */
public class FieldIdTest {

    @Test
    public void test() {
        FieldId fieldId1 = new FieldId();

        assertEquals(FieldIdEnum.UNKNOWN, fieldId1.getFieldId());
        assertEquals(0, fieldId1.getIndex());
        
        assertTrue(fieldId1.equals(FieldId.getUnknownValue()));
        
        fieldId1.setFieldId(FieldIdEnum.ELSE_FILTER);
        assertEquals(FieldIdEnum.ELSE_FILTER, fieldId1.getFieldId());

        fieldId1.setIndex(42);
        assertEquals(42, fieldId1.getIndex());

        FieldId fieldId2 = new FieldId(FieldIdEnum.ELSE_FILTER);
        assertEquals(FieldIdEnum.ELSE_FILTER, fieldId2.getFieldId());
        assertEquals(0, fieldId2.getIndex());

        FieldId fieldId3 = new FieldId(FieldIdEnum.ELSE_FILTER, 42);
        assertEquals(FieldIdEnum.ELSE_FILTER, fieldId3.getFieldId());
        assertEquals(42, fieldId3.getIndex());
    }

    @Test
    public void test_compareTo()
    {
        FieldId fieldId1 = new FieldId(FieldIdEnum.ELSE_FILTER, 42);
        FieldId fieldId2 = new FieldId(FieldIdEnum.ELSE_FILTER);
        FieldId fieldId3 = new FieldId(FieldIdEnum.ELSE_FILTER, 42);
        
        assertTrue(fieldId1.compareTo(fieldId3) == 0);
        assertTrue(fieldId1.compareTo(fieldId2) != 0);

        assertTrue(fieldId1.toString().compareTo("ELSE_FILTER.42") == 0);

        FieldId fieldId4 = new FieldId(FieldIdEnum.DISPLACEMENT_Y, 42);
        assertTrue(fieldId1.compareTo(fieldId4) != 0);

        FieldId fieldId5 = new FieldId(FieldIdEnum.RANDOM_FILL_TILE_SIZE, 42);
        assertTrue(fieldId1.compareTo(fieldId5) != 0);

        FieldId fieldId6 = new FieldId(FieldIdEnum.ELSE_FILTER, 41);
        assertTrue(fieldId1.compareTo(fieldId6) != 0);
        FieldId fieldId7 = new FieldId(FieldIdEnum.ELSE_FILTER, 43);
        assertTrue(fieldId1.compareTo(fieldId7) != 0);

    }
    
    @Test
    public void test_hashCode()
    {
        FieldId fieldId1 = new FieldId(FieldIdEnum.ELSE_FILTER, 42);
        FieldId fieldId2 = new FieldId(FieldIdEnum.ELSE_FILTER);
        FieldId fieldId3 = new FieldId(FieldIdEnum.ELSE_FILTER, 42);
        FieldId fieldId4 = new FieldId(null);

        assertEquals(fieldId1.hashCode(), fieldId3.hashCode());
        assertTrue(fieldId1.hashCode() != fieldId2.hashCode());
        assertTrue(fieldId1.hashCode() != fieldId4.hashCode());
    }
    
    @Test
    public void test_equals()
    {
        FieldId fieldId1 = new FieldId(FieldIdEnum.ELSE_FILTER, 42);
        FieldId fieldId2 = new FieldId(FieldIdEnum.ELSE_FILTER);
        FieldId fieldId3 = new FieldId(FieldIdEnum.ELSE_FILTER, 42);
        FieldId fieldId4 = new FieldId(FieldIdEnum.ANGLE);

        assertTrue(fieldId1.equals(fieldId1));
        assertFalse(fieldId1.equals(null));
        assertFalse(fieldId1.equals(String.valueOf(false)));
        assertTrue(fieldId1.equals(fieldId3));
        assertFalse(fieldId1.equals(fieldId2));
        assertFalse(fieldId1.equals(fieldId4));
    }
}
