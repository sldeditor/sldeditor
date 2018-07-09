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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.sldeditor.datasource.attribute.DataSourceAttributeData;
import org.junit.Test;

/**
 * Unit test for DataSourceAttributeData class.
 *
 * <p>{@link com.sldeditor.datasource.attribute.DataSourceAttributeData}
 *
 * @author Robert Ward (SCISYS)
 */
public class DataSourceAttributeDataTest {

    /**
     * Test method for {@link
     * com.sldeditor.datasource.attribute.DataSourceAttributeData#DataSourceAttributeData(org.opengis.feature.type.Name,
     * java.lang.Class, java.lang.Object)}.
     */
    @Test
    public void testDataSourceAttributeDataNameClassOfQObject() {
        String expectedName = "test name";
        Class<?> expectedType = Integer.class;
        Object expectedValue = Integer.valueOf(42);

        DataSourceAttributeData dsa =
                new DataSourceAttributeData(expectedName, expectedType, expectedValue);

        assertTrue(expectedName.compareTo(dsa.getName()) == 0);
        assertEquals(expectedType, dsa.getType());
        assertEquals(expectedValue, dsa.getValue());
    }

    /**
     * Test method for {@link com.sldeditor.datasource.attribute.DataSourceAttributeData#clone()}.
     */
    @Test
    public void testClone() {
        String expectedName = "test name";
        Class<?> expectedType = Integer.class;
        Object expectedValue = Integer.valueOf(42);

        DataSourceAttributeData dsa =
                new DataSourceAttributeData(expectedName, expectedType, expectedValue);

        DataSourceAttributeData cloneDsa = dsa.clone();

        assertTrue(expectedName.compareTo(cloneDsa.getName()) == 0);
        assertEquals(expectedType, cloneDsa.getType());
        assertEquals(expectedValue, cloneDsa.getValue());
    }

    /**
     * Test method for {@link
     * com.sldeditor.datasource.attribute.DataSourceAttributeData#DataSourceAttributeData(com.sldeditor.datasource.attribute.DataSourceAttributeData)}.
     */
    @Test
    public void testDataSourceAttributeDataDataSourceAttributeData() {
        String expectedName = "test name";
        Class<?> expectedType = Integer.class;
        Object expectedValue = Integer.valueOf(42);

        DataSourceAttributeData dsa =
                new DataSourceAttributeData(expectedName, expectedType, expectedValue);

        DataSourceAttributeData copyDsa = new DataSourceAttributeData(dsa);

        assertTrue(expectedName.compareTo(copyDsa.getName()) == 0);
        assertEquals(expectedType, copyDsa.getType());
        assertEquals(expectedValue, copyDsa.getValue());
    }

    /**
     * Test method for {@link
     * com.sldeditor.datasource.attribute.DataSourceAttributeData#setName(org.opengis.feature.type.Name)}.
     */
    @Test
    public void testSetName() {
        String expectedName1 = "test name";
        Class<?> expectedType = Integer.class;
        Object expectedValue = Integer.valueOf(42);

        DataSourceAttributeData dsa =
                new DataSourceAttributeData(expectedName1, expectedType, expectedValue);

        String expectedName2 = "updated test name";
        dsa.setName(expectedName2);
        assertTrue(expectedName2.compareTo(dsa.getName()) == 0);
    }

    /**
     * Test method for {@link
     * com.sldeditor.datasource.attribute.DataSourceAttributeData#setType(java.lang.Class)}.
     */
    @Test
    public void testSetType() {
        String expectedName = "test name";
        Class<?> expectedType1 = Integer.class;
        Object expectedValue = Integer.valueOf(42);

        DataSourceAttributeData dsa =
                new DataSourceAttributeData(expectedName, expectedType1, expectedValue);

        Class<?> expectedType2 = Float.class;
        dsa.setType(expectedType2);

        assertEquals(expectedType2, dsa.getType());
    }

    /**
     * Test method for {@link
     * com.sldeditor.datasource.attribute.DataSourceAttributeData#setValue(java.lang.Object)}.
     */
    @Test
    public void testSetValue() {
        String expectedName = "test name";
        Class<?> expectedType = Integer.class;
        Object expectedValue1 = Integer.valueOf(42);

        DataSourceAttributeData dsa =
                new DataSourceAttributeData(expectedName, expectedType, expectedValue1);

        Object expectedValue2 = Integer.valueOf(69);
        dsa.setValue(expectedValue2);
        assertEquals(expectedValue2, dsa.getValue());
    }

    @Test
    public void testEquals() {
        String expectedName1 = "test name";
        Class<?> expectedType1 = Integer.class;
        Object expectedValue1 = Integer.valueOf(42);

        DataSourceAttributeData dsa1 =
                new DataSourceAttributeData(expectedName1, expectedType1, expectedValue1);
        DataSourceAttributeData dsa3 =
                new DataSourceAttributeData(expectedName1, expectedType1, expectedValue1);

        assertEquals(dsa1, dsa1);
        assertEquals(dsa1, dsa3);
        assertEquals(dsa1.hashCode(), dsa1.hashCode());
        assertFalse(dsa1.equals(null));

        String expectedName2 = "test name2";
        Class<?> expectedType2 = Double.class;
        Object expectedValue2 = Integer.valueOf(24);

        DataSourceAttributeData dsa2 =
                new DataSourceAttributeData(expectedName2, expectedType2, expectedValue2);
        assertFalse(dsa1.equals(dsa2));
        DataSourceAttributeData obj1 =
                new DataSourceAttributeData(expectedName1, expectedType2, expectedValue1);
        assertFalse(dsa1.equals(obj1));
        assertFalse(dsa1.equals(new DataSourceAttributeData(expectedName1, null, expectedValue1)));
        assertFalse(dsa1.equals(new DataSourceAttributeData(null, expectedType1, expectedValue1)));
        DataSourceAttributeData obj2 =
                new DataSourceAttributeData(expectedName1, expectedType1, expectedValue2);
        assertTrue(dsa1.equals(obj2));
        assertFalse(new DataSourceAttributeData(null, expectedType1, expectedValue1).equals(dsa1));
        assertFalse(new DataSourceAttributeData(expectedName1, null, expectedValue1).equals(dsa1));
        assertTrue(new DataSourceAttributeData(expectedName1, expectedType1, null).equals(dsa1));
        DataSourceAttributeData obj3 =
                new DataSourceAttributeData(null, expectedType1, expectedValue1);
        assertFalse(obj3.equals("wrong class"));
    }
}
