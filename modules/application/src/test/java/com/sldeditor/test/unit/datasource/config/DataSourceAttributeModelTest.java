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

package com.sldeditor.test.unit.datasource.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.sldeditor.datasource.attribute.DataSourceAttributeData;
import com.sldeditor.datasource.attribute.DataSourceAttributeListInterface;
import com.sldeditor.datasource.config.DataSourceAttributeModel;

/**
 * Unit test for DataSourceAttributeModel class.
 * <p>{@link com.sldeditor.datasource.config.DataSourceAttributeModel}
 * 
 * @author Robert Ward (SCISYS)
 *
 */
public class DataSourceAttributeModelTest {

    /**
     * Test method for {@link com.sldeditor.datasource.config.DataSourceAttributeModel#isCellEditable(int, int)}.
     * Test method for {@link com.sldeditor.datasource.config.DataSourceAttributeModel#setConnectedToDataSource(boolean)}.
     */
    @Test
    public void testIsCellEditable() {
        DataSourceAttributeModel model = new DataSourceAttributeModel();

        // Not connected to data source
        assertTrue(model.isCellEditable(0, 0));
        assertTrue(model.isCellEditable(0, 1));

        // Connected to data source
        model.setConnectedToDataSource(true);
        assertFalse(model.isCellEditable(0, 0));
        assertTrue(model.isCellEditable(0, 2));
    }

    /**
     * Test method for {@link com.sldeditor.datasource.config.DataSourceAttributeModel#populate(java.util.List)}.
     * Test method for {@link com.sldeditor.datasource.config.DataSourceAttributeModel#getValueAt(int, int)}.
     * Test method for {@link com.sldeditor.datasource.config.DataSourceAttributeModel#setValueAt(java.lang.Object, int, int)}.
     * Test method for {@link com.sldeditor.datasource.config.DataSourceAttributeModel#retrieveData()}.
     * Test method for {@link com.sldeditor.datasource.config.DataSourceAttributeModel#getAttributeData()}.
     */
    @Test
    public void testPopulate() {
        DataSourceAttributeModel model = new DataSourceAttributeModel();
        assertEquals(0, model.getRowCount());

        String expectedName1 = "test name1";
        Class<?> expectedType1 = Integer.class;
        Object expectedValue1 = Integer.valueOf(42);

        DataSourceAttributeData dsa1 = new DataSourceAttributeData(expectedName1, expectedType1, expectedValue1);

        String expectedName2 = "test name2";
        Class<?> expectedType2 = Integer.class;
        Object expectedValue2 = Integer.valueOf(53);
        DataSourceAttributeData dsa2 = new DataSourceAttributeData(expectedName2, expectedType2, expectedValue2);

        List<DataSourceAttributeData> attributeList = new ArrayList<DataSourceAttributeData>();
        attributeList.add(dsa1);
        attributeList.add(dsa2);

        model.populate(null);
        model.populate(attributeList);
        assertEquals(attributeList.size(), model.getRowCount());

        // Check get value at
        assertNull(model.getValueAt(0, 9));
        assertNull(model.getValueAt(0, -1));
        assertNull(model.getValueAt(-1, 0));
        assertNull(model.getValueAt(attributeList.size(), 0));

        assertTrue(expectedName1.compareTo(((String)model.getValueAt(0, 0))) == 0);
        String actualType = (String)model.getValueAt(0, 1);
        assertTrue(expectedType1.getSimpleName().compareTo(actualType) == 0);
        assertEquals(expectedValue1, model.getValueAt(0, 2));

        assertTrue(expectedName2.compareTo(((String)model.getValueAt(1, 0))) == 0);
        actualType = (String)model.getValueAt(1, 1);
        assertTrue(expectedType2.getSimpleName().compareTo(actualType) == 0);
        assertEquals(expectedValue2, model.getValueAt(1, 2));

        // Check set value at
        String expectedName3 = "test name3";
        Class<?> expectedType3 = String.class;
        Object expectedValue3 = String.valueOf(42);

        // Set Value At - illegal values
        model.setValueAt(null, 0, 9);
        model.setValueAt(null, 0, -1);
        model.setValueAt(null, -1, 0);
        model.setValueAt(null, attributeList.size(), 0);

        // Set Value At - legal values
        model.setValueAt(expectedName3, 0, 0);
        assertTrue(expectedName3.compareTo(((String)model.getValueAt(0, 0))) == 0);
        model.setValueAt(expectedType3.getSimpleName(), 1, 1);
        actualType = (String)model.getValueAt(1, 1);
        assertTrue(expectedType3.getSimpleName().compareTo(actualType) == 0);
        model.setValueAt(expectedValue3, 1, 2);
        assertEquals(expectedValue3, model.getValueAt(1, 2));

        // Retrieve data
        List<DataSourceAttributeData> actualAttributeList = model.retrieveData();
        assertEquals(attributeList.size(), actualAttributeList.size());

        assertTrue(expectedName3.compareTo(actualAttributeList.get(0).getName()) == 0);
        assertEquals(expectedType3, actualAttributeList.get(1).getType());
        assertEquals(expectedValue3, actualAttributeList.get(1).getValue());

        // Get attribute data
        DataSourceAttributeListInterface attributeDataImpl = model.getAttributeData();
        actualAttributeList = attributeDataImpl.getData();
        assertEquals(attributeList.size(), actualAttributeList.size());

        assertTrue(expectedName3.compareTo(actualAttributeList.get(0).getName()) == 0);
        assertEquals(expectedType3, actualAttributeList.get(1).getType());
        assertEquals(expectedValue3, actualAttributeList.get(1).getValue());
        
        // Try and set an unknown type
        model.setValueAt(DataSourceAttributeModel.class.getSimpleName(), 0, 1);
        assertNull(model.getValueAt(0, 1));
    }

    /**
     * Test method for {@link com.sldeditor.datasource.config.DataSourceAttributeModel#getColumnCount()}.
     * Test method for {@link com.sldeditor.datasource.config.DataSourceAttributeModel#getColumnName(int)}.
     */
    @Test
    public void testGetColumnCount() {
        DataSourceAttributeModel model = new DataSourceAttributeModel();

        assertEquals(3, model.getColumnCount());

        String col1 = model.getColumnName(0);
        String col2 = model.getColumnName(1);
        String col3 = model.getColumnName(2);

        assertFalse(col1.isEmpty());
        assertFalse(col2.isEmpty());
        assertFalse(col3.isEmpty());
    }

    /**
     * Test method for {@link com.sldeditor.datasource.config.DataSourceAttributeModel#getTypeData()}.
     */
    @Test
    public void testGetTypeData() {
        DataSourceAttributeModel model = new DataSourceAttributeModel();
        assertTrue(model.getTypeData().length > 0);
    }

    /**
     * Test method for {@link com.sldeditor.datasource.config.DataSourceAttributeModel#addNewField()}.
     * Test method for {@link com.sldeditor.datasource.config.DataSourceAttributeModel#removeFields(int[])}.
     */
    @Test
    public void testAddNewField() {
        DataSourceAttributeModel model = new DataSourceAttributeModel();
        assertEquals(0, model.getRowCount());

        model.addNewField();
        assertEquals(1, model.getRowCount());

        model.addNewField();
        model.addNewField();
        assertEquals(3, model.getRowCount());

        model.removeFields(null);
        assertEquals(3, model.getRowCount());

        int[] selectedRowIndexes = new int[3];
        selectedRowIndexes[0] = 0;
        selectedRowIndexes[1] = -1;
        selectedRowIndexes[2] = 5;
        model.removeFields(selectedRowIndexes);
        assertEquals(2, model.getRowCount());
    }

}
