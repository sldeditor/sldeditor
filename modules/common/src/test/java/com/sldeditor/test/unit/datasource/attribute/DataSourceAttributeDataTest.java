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

import org.geotools.feature.NameImpl;
import org.junit.Test;
import org.opengis.feature.type.Name;

import com.sldeditor.datasource.attribute.DataSourceAttributeData;

/**
 * Unit test for DataSourceAttributeData class.
 * 
 * <p>{@link com.sldeditor.datasource.attribute.DataSourceAttributeData}
 * @author Robert Ward (SCISYS)
 *
 */
public class DataSourceAttributeDataTest {

    /**
     * Test method for {@link com.sldeditor.datasource.attribute.DataSourceAttributeData#DataSourceAttributeData(org.opengis.feature.type.Name, java.lang.Class, java.lang.Object)}.
     */
    @Test
    public void testDataSourceAttributeDataNameClassOfQObject() {
        Name expectedName = new NameImpl("test name");
        Class<?> expectedType = Integer.class;
        Object expectedValue = Integer.valueOf(42);

        DataSourceAttributeData dsa = new DataSourceAttributeData(expectedName, expectedType, expectedValue);

        assertEquals(expectedName, dsa.getName());
        assertEquals(expectedType, dsa.getType());
        assertEquals(expectedValue, dsa.getValue());
    }

    /**
     * Test method for {@link com.sldeditor.datasource.attribute.DataSourceAttributeData#clone()}.
     */
    @Test
    public void testClone() {
        Name expectedName = new NameImpl("test name");
        Class<?> expectedType = Integer.class;
        Object expectedValue = Integer.valueOf(42);

        DataSourceAttributeData dsa = new DataSourceAttributeData(expectedName, expectedType, expectedValue);

        DataSourceAttributeData cloneDsa = dsa.clone();

        assertEquals(expectedName, cloneDsa.getName());
        assertEquals(expectedType, cloneDsa.getType());
        assertEquals(expectedValue, cloneDsa.getValue());
    }

    /**
     * Test method for {@link com.sldeditor.datasource.attribute.DataSourceAttributeData#DataSourceAttributeData(com.sldeditor.datasource.attribute.DataSourceAttributeData)}.
     */
    @Test
    public void testDataSourceAttributeDataDataSourceAttributeData() {
        Name expectedName = new NameImpl("test name");
        Class<?> expectedType = Integer.class;
        Object expectedValue = Integer.valueOf(42);

        DataSourceAttributeData dsa = new DataSourceAttributeData(expectedName, expectedType, expectedValue);

        DataSourceAttributeData copyDsa = new DataSourceAttributeData(dsa);

        assertEquals(expectedName, copyDsa.getName());
        assertEquals(expectedType, copyDsa.getType());
        assertEquals(expectedValue, copyDsa.getValue());
    }

    /**
     * Test method for {@link com.sldeditor.datasource.attribute.DataSourceAttributeData#setName(org.opengis.feature.type.Name)}.
     */
    @Test
    public void testSetName() {
        Name expectedName1 = new NameImpl("test name");
        Class<?> expectedType = Integer.class;
        Object expectedValue = Integer.valueOf(42);

        DataSourceAttributeData dsa = new DataSourceAttributeData(expectedName1, expectedType, expectedValue);

        Name expectedName2 = new NameImpl("updated test name");
        dsa.setName(expectedName2);
        assertEquals(expectedName2, dsa.getName());
    }

    /**
     * Test method for {@link com.sldeditor.datasource.attribute.DataSourceAttributeData#setType(java.lang.Class)}.
     */
    @Test
    public void testSetType() {
        Name expectedName = new NameImpl("test name");
        Class<?> expectedType1 = Integer.class;
        Object expectedValue = Integer.valueOf(42);

        DataSourceAttributeData dsa = new DataSourceAttributeData(expectedName, expectedType1, expectedValue);

        Class<?> expectedType2 = Float.class;
        dsa.setType(expectedType2);

        assertEquals(expectedType2, dsa.getType());
    }

    /**
     * Test method for {@link com.sldeditor.datasource.attribute.DataSourceAttributeData#setValue(java.lang.Object)}.
     */
    @Test
    public void testSetValue() {
        Name expectedName = new NameImpl("test name");
        Class<?> expectedType = Integer.class;
        Object expectedValue1 = Integer.valueOf(42);

        DataSourceAttributeData dsa = new DataSourceAttributeData(expectedName, expectedType, expectedValue1);

        Object expectedValue2 = Integer.valueOf(69);
        dsa.setValue(expectedValue2);
        assertEquals(expectedValue2, dsa.getValue());
    }

}
