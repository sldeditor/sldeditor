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
package com.sldeditor.test.unit.datasource;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.sldeditor.datasource.DataSourceField;

/**
 * Unit test for DataSourceField.
 * <p>{@link com.sldeditor.datasource.DataSourceField}
 * 
 * @author Robert Ward (SCISYS)
 */
public class DataSourceFieldTest {

    /**
     * Test method for {@link com.sldeditor.datasource.DataSourceField#DataSourceField(java.lang.String, java.lang.Class)}.
     * Test method for {@link com.sldeditor.datasource.DataSourceField#getName()}.
     * Test method for {@link com.sldeditor.datasource.DataSourceField#setName(java.lang.String)}.
     * Test method for {@link com.sldeditor.datasource.DataSourceField#getFieldType()}.
     * Test method for {@link com.sldeditor.datasource.DataSourceField#setFieldType(java.lang.Class)}.
     */
    @Test
    public void testDataSourceField() {
        String expectedName = "Test field name";
        Class<?> expectedFieldType = Double.class;
        
        DataSourceField dsField = new DataSourceField(expectedName, expectedFieldType);
        
        assertEquals(expectedName, dsField.getName());
        assertEquals(expectedFieldType, dsField.getFieldType());

        expectedName = "Updated test field name";
        dsField.setName(expectedName);
        expectedFieldType = String.class;
        dsField.setFieldType(expectedFieldType);
        assertEquals(expectedName, dsField.getName());
        assertEquals(expectedFieldType, dsField.getFieldType());
    }

}
