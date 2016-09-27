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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.sldeditor.common.Controller;
import com.sldeditor.common.undo.UndoEvent;
import com.sldeditor.common.undo.UndoManager;
import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.ui.detail.config.FieldConfigBase;
import com.sldeditor.ui.detail.config.FieldConfigCommonData;
import com.sldeditor.ui.detail.config.FieldConfigSymbolType;
import com.sldeditor.ui.detail.config.FieldId;
import com.sldeditor.ui.detail.config.symboltype.FieldConfigMarker;
import com.sldeditor.ui.widgets.ValueComboBoxData;
import com.sldeditor.ui.widgets.ValueComboBoxDataGroup;

/**
 * The unit test for FieldConfigBase.
 * <p>{@link com.sldeditor.ui.detail.config.FieldConfigBase}
 *
 * @author Robert Ward (SCISYS)
 */
public class FieldConfigSymbolTypeTest {

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigSymbolType#setEnabled(boolean)}.
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigSymbolType#isEnabled()}.
     */
    @Test
    public void testSetEnabled() {
        // Value only, no attribute/expression dropdown
        boolean valueOnly = true;
        FieldConfigSymbolType field = new FieldConfigSymbolType(new FieldConfigCommonData(Integer.class, new FieldId(FieldIdEnum.NAME), "label", valueOnly));

        // Text field will not have been created
        boolean expectedValue = true;
        field.setEnabled(expectedValue);

        assertFalse(field.isEnabled());

        // Create text field
        field.createUI();
        assertEquals(expectedValue, field.isEnabled());

        expectedValue = false;
        field.setEnabled(expectedValue);

        assertEquals(expectedValue, field.isEnabled());

        // Has attribute/expression dropdown
        valueOnly = false;
        FieldConfigSymbolType field2 = new FieldConfigSymbolType(new FieldConfigCommonData(Integer.class, new FieldId(FieldIdEnum.NAME), "label", valueOnly));

        // Text field will not have been created
        expectedValue = true;
        field2.setEnabled(expectedValue);
        assertFalse(field2.isEnabled());

        // Create text field
        field2.createUI();

        assertEquals(expectedValue, field2.isEnabled());

        expectedValue = false;
        field2.setEnabled(expectedValue);

        // Actual value is coming from the attribute panel, not the text field
        assertEquals(!expectedValue, field2.isEnabled());
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigSymbolType#setVisible(boolean)}.
     */
    @Test
    public void testSetVisible() {
        boolean valueOnly = true;
        FieldConfigSymbolType field = new FieldConfigSymbolType(new FieldConfigCommonData(Integer.class, new FieldId(FieldIdEnum.NAME), "label", valueOnly));

        boolean expectedValue = true;
        field.setVisible(expectedValue);
        field.createUI();
        field.setVisible(expectedValue);

        expectedValue = false;
        field.setVisible(expectedValue);
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigSymbolType#generateExpression()}.
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigSymbolType#populateExpression(java.lang.Object, org.opengis.filter.expression.Expression)}.
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigSymbolType#populateField(java.lang.String)}.
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigSymbolType#setTestValue(com.sldeditor.ui.detail.config.FieldId, java.lang.String)}.
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigSymbolType#getEnumValue()}.
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigSymbolType#FieldConfigSymbolType(java.lang.Class, com.sldeditor.ui.detail.config.FieldId, java.lang.String, boolean)}.
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigSymbolType#getStringValue()}.
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigSymbolType#addField(com.sldeditor.ui.detail.config.symboltype.SymbolTypeInterface)}.
     */
    @Test
    public void testGenerateExpression() {
        boolean valueOnly = true;
        FieldConfigSymbolType field = new FieldConfigSymbolType(new FieldConfigCommonData(Integer.class, new FieldId(FieldIdEnum.NAME), "label", valueOnly));

        field.populateExpression(null);
        field.setTestValue(null, (String)null);
        assertNull(field.getEnumValue());
        assertNull(field.getSelectedValueObj());
        assertNull(field.getSelectedValue());

        field.createUI();

        String expectedValue1 = "circle";
        field.populateExpression(expectedValue1);

        String actualValue1 = field.getStringValue();
        assertNull(actualValue1);

        FieldConfigMarker marker = new FieldConfigMarker(new FieldConfigCommonData(String.class, new FieldId(FieldIdEnum.ANGLE), "label", valueOnly), null, null, null);
        marker.createUI();

        List<ValueComboBoxDataGroup> combinedSymbolList = new ArrayList<ValueComboBoxDataGroup>();

        List<ValueComboBoxData> dataList = new ArrayList<ValueComboBoxData>();
        dataList.add(new ValueComboBoxData("key 1", "Value 1", String.class));
        dataList.add(new ValueComboBoxData("key 2", "Value 2", Integer.class));
        dataList.add(new ValueComboBoxData("key 3", "Value 3", Boolean.class));

        combinedSymbolList.add(new ValueComboBoxDataGroup(dataList));

        field.createUI();
        field.addField(marker);
        field.populate(null, combinedSymbolList);
        field.populateExpression(expectedValue1);
        actualValue1 = field.getStringValue();
        assertNull(actualValue1);

        String expectedValue2 = "key 2";
        field.populateExpression(expectedValue2);
        String actualValue2 = field.getStringValue();
        assertTrue(actualValue2.compareTo(field.getStringValue()) == 0);

        ValueComboBoxData actualValueObj2 = field.getEnumValue();
        assertNotNull(actualValueObj2.getKey());
        assertTrue(actualValueObj2.getKey().compareTo(expectedValue2) == 0);

        String expectedValue3 = "key 1";
        field.setTestValue(null, expectedValue3);
        String actualValue3 = field.getStringValue();
        assertTrue(expectedValue3.compareTo(actualValue3) == 0);

        String expectedValue4 = "key 3";
        field.populateField(expectedValue4);
        String actualValue4 = field.getStringValue();
        assertTrue(expectedValue4.compareTo(actualValue4) == 0);

        String expectedValue5 = "key 2";
        field.setSelectedItem(expectedValue5);
        String actualValue5 = field.getStringValue();
        assertTrue(expectedValue5.compareTo(actualValue5) == 0);

        ValueComboBoxData actualValueObj5 = field.getSelectedValueObj();
        assertNotNull(actualValueObj5.getKey());
        assertTrue(actualValueObj5.getKey().compareTo(expectedValue5) == 0);

        Class<?> actualValue6 = field.getSelectedValue();
        assertEquals(Integer.class, actualValue6);
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigSymbolType#revertToDefaultValue()}.
     */
    @Test
    public void testRevertToDefaultValue() {
        boolean valueOnly = true;
        FieldConfigSymbolType field = new FieldConfigSymbolType(new FieldConfigCommonData(Integer.class, new FieldId(FieldIdEnum.NAME), "label", valueOnly));

        field.revertToDefaultValue();
        // Does nothing
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigSymbolType#createCopy(com.sldeditor.ui.detail.config.FieldConfigBase)}.
     */
    @Test
    public void testCreateCopy() {
        boolean valueOnly = true;

        class TestFieldConfigSymbolType extends FieldConfigSymbolType
        {
            public TestFieldConfigSymbolType(FieldConfigCommonData commonData) {
                super(commonData);
            }

            public FieldConfigBase callCreateCopy(FieldConfigBase fieldConfigBase)
            {
                return createCopy(fieldConfigBase);
            }
        }

        TestFieldConfigSymbolType field = new TestFieldConfigSymbolType(new FieldConfigCommonData(Integer.class, new FieldId(FieldIdEnum.NAME), "label", valueOnly));
        FieldConfigSymbolType copy = (FieldConfigSymbolType) field.callCreateCopy(null);
        assertNull(copy);

        copy = (FieldConfigSymbolType) field.callCreateCopy(field);
        assertEquals(field.getFieldId().getFieldId(), copy.getFieldId().getFieldId());
        assertTrue(field.getLabel().compareTo(copy.getLabel()) == 0);
        assertEquals(field.isValueOnly(), copy.isValueOnly());
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigSymbolType#attributeSelection(java.lang.String)}.
     */
    @Test
    public void testAttributeSelection() {
        boolean valueOnly = true;
        FieldConfigSymbolType field = new FieldConfigSymbolType(new FieldConfigCommonData(Integer.class, new FieldId(FieldIdEnum.NAME), "label", valueOnly));
        field.attributeSelection(null);

        // Do nothing
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigSymbolType#undoAction(com.sldeditor.common.undo.UndoInterface)}.
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigSymbolType#redoAction(com.sldeditor.common.undo.UndoInterface)}.
     */
    @Test
    public void testUndoAction() {
        UndoManager.getInstance().setPopulationCheck(Controller.getInstance());
        boolean valueOnly = true;
        FieldConfigSymbolType field = new FieldConfigSymbolType(new FieldConfigCommonData(Integer.class, new FieldId(FieldIdEnum.NAME), "label", valueOnly));
        field.undoAction(null);
        field.redoAction(null);

        field.createUI();

        FieldConfigMarker marker = new FieldConfigMarker(new FieldConfigCommonData(String.class, new FieldId(FieldIdEnum.ANGLE), "label", valueOnly), null, null, null);
        marker.createUI();

        List<ValueComboBoxDataGroup> combinedSymbolList = new ArrayList<ValueComboBoxDataGroup>();

        List<ValueComboBoxData> dataList = new ArrayList<ValueComboBoxData>();
        dataList.add(new ValueComboBoxData("key 1", "Value 1", FieldConfigMarker.class));
        dataList.add(new ValueComboBoxData("key 2", "Value 2", FieldConfigMarker.class));
        dataList.add(new ValueComboBoxData("key 3", "Value 3", FieldConfigMarker.class));

        combinedSymbolList.add(new ValueComboBoxDataGroup(dataList));

        field.createUI();
        field.addField(marker);
        field.populate(null, combinedSymbolList);

        String expectedValue2 = "key 2";
        field.populateExpression(expectedValue2);
        String actualValue2 = field.getStringValue();
        assertTrue(actualValue2.compareTo(field.getStringValue()) == 0);

        ValueComboBoxData actualValueObj2 = field.getEnumValue();
        assertNotNull(actualValueObj2.getKey());
        assertTrue(actualValueObj2.getKey().compareTo(expectedValue2) == 0);

        String expectedValue3 = "key 1";
        field.populateField(expectedValue3);
        String actualValue3 = field.getStringValue();
        assertTrue(expectedValue3.compareTo(actualValue3) == 0);

        String expectedValue4 = "key 3";
        field.populateField(expectedValue4);
        String actualValue4 = field.getStringValue();
        assertTrue(expectedValue4.compareTo(actualValue4) == 0);

        UndoManager.getInstance().undo();
        String actualValue = field.getStringValue();
        assertTrue(expectedValue3.compareTo(actualValue) == 0);

        UndoManager.getInstance().redo();
        actualValue = field.getStringValue();
        assertTrue(expectedValue4.compareTo(actualValue) == 0);

        // Increase the code coverage
        field.undoAction(null);
        field.undoAction(new UndoEvent(null, new FieldId(FieldIdEnum.NAME), Double.valueOf(0),  Double.valueOf(23)));
        field.redoAction(null);
        field.redoAction(new UndoEvent(null, new FieldId(FieldIdEnum.NAME),  Double.valueOf(0),  Double.valueOf(54)));
        UndoManager.getInstance().setPopulationCheck(null);
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigSymbolType#optionSelected(com.sldeditor.ui.widgets.ValueComboBoxData)}.
     */
    @Test
    public void testOptionSelected() {
        boolean valueOnly = true;
        FieldConfigSymbolType field = new FieldConfigSymbolType(new FieldConfigCommonData(Integer.class, new FieldId(FieldIdEnum.NAME), "label", valueOnly));

        field.optionSelected(null);
    }

}
