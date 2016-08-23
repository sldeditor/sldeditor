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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.sldeditor.common.undo.UndoEvent;
import com.sldeditor.common.undo.UndoManager;
import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.ui.detail.config.FieldConfigBase;
import com.sldeditor.ui.detail.config.FieldConfigInteger;
import com.sldeditor.ui.detail.config.FieldId;

/**
 * The unit test for FieldConfigInteger.
 * <p>{@link com.sldeditor.ui.detail.config.FieldConfigInteger}
 *
 * @author Robert Ward (SCISYS)
 */
public class FieldConfigIntegerTest {

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigInteger#setEnabled(boolean)}.
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigInteger#isEnabled()}.
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigInteger#createUI(javax.swing.Box)}.
     */
    @Test
    public void testSetEnabled() {
        // Value only, no attribute/expression dropdown
        boolean valueOnly = true;
        FieldConfigInteger field = new FieldConfigInteger(Integer.class, new FieldId(FieldIdEnum.NAME), "label", valueOnly);

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
        FieldConfigInteger field2 = new FieldConfigInteger(Integer.class, new FieldId(FieldIdEnum.NAME), "label", valueOnly);

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
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigInteger#setVisible(boolean)}.
     */
    @Test
    public void testSetVisible() {
        boolean valueOnly = true;
        FieldConfigInteger field = new FieldConfigInteger(Integer.class, new FieldId(FieldIdEnum.NAME), "label", valueOnly);

        boolean expectedValue = true;
        field.setVisible(expectedValue);
        field.createUI();
        field.setVisible(expectedValue);

        expectedValue = false;
        field.setVisible(expectedValue);
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigInteger#generateExpression()}.
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigInteger#populateExpression(java.lang.Object, org.opengis.filter.expression.Expression)}.
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigInteger#setTestValue(com.sldeditor.ui.detail.config.FieldId, int)}.
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigInteger#populateField(java.lang.Integer)}.
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigInteger#getIntValue()}.
     */
    @Test
    public void testGenerateExpression() {
        boolean valueOnly = true;
        FieldConfigInteger field = new FieldConfigInteger(Integer.class, new FieldId(FieldIdEnum.NAME), "label", valueOnly);

        int expectedValue = 1;
        field.populateField(expectedValue);
        assertEquals(0, field.getIntValue());

        field.setTestValue(null, expectedValue);
        assertEquals(0, field.getIntValue());

        field.populateExpression(null);
        assertEquals(0, field.getIntValue());

        field.createUI();
        field.populateField(expectedValue);
        assertEquals(expectedValue, field.getIntValue());

        int expectedValue2 = 134;
        field.setTestValue(null, expectedValue2);
        assertEquals(expectedValue2, field.getIntValue());

        Integer expectedValue3a = Integer.valueOf(42);
        field.populateExpression(expectedValue3a);
        assertEquals(expectedValue3a.intValue(), field.getIntValue());

        Long expectedValue3b = Long.valueOf(4567);
        field.populateExpression(expectedValue3b);
        assertEquals(expectedValue3b.intValue(), field.getIntValue());

        Double expectedValue3c = Double.valueOf(42.12);
        field.populateExpression(expectedValue3c);
        assertEquals(expectedValue3c.intValue(), field.getIntValue());

        String expectedValue3d = String.valueOf(6987);
        field.populateExpression(expectedValue3d);
        assertEquals(Integer.valueOf(expectedValue3d).intValue(), field.getIntValue());
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigInteger#revertToDefaultValue()}.
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigInteger#setDefaultValue(int)}.
     */
    @Test
    public void testRevertToDefaultValue() {
        boolean valueOnly = true;
        FieldConfigInteger field = new FieldConfigInteger(Integer.class, new FieldId(FieldIdEnum.NAME), "label", valueOnly);

        Integer expectedDefaultValue = 42;
        field.revertToDefaultValue();
        assertEquals(0, field.getIntValue());

        field.createUI();
        field.setDefaultValue(expectedDefaultValue);
        field.revertToDefaultValue();
        assertEquals(expectedDefaultValue.intValue(), field.getIntValue());
        assertTrue(String.valueOf(expectedDefaultValue).compareTo(field.getStringValue()) == 0);
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigInteger#createCopy(com.sldeditor.ui.detail.config.FieldConfigBase)}.
     */
    @Test
    public void testCreateCopy() {
        boolean valueOnly = true;

        class TestFieldConfigInteger extends FieldConfigInteger
        {
            public TestFieldConfigInteger(Class<?> panelId, FieldId id, String label, boolean valueOnly) {
                super(panelId, id, label, valueOnly);
            }

            public FieldConfigBase callCreateCopy(FieldConfigBase fieldConfigBase)
            {
                return createCopy(fieldConfigBase);
            }
        }

        TestFieldConfigInteger field = new TestFieldConfigInteger(Integer.class, new FieldId(FieldIdEnum.NAME), "label", valueOnly);
        FieldConfigInteger copy = (FieldConfigInteger) field.callCreateCopy(null);
        assertNull(copy);

        copy = (FieldConfigInteger) field.callCreateCopy(field);
        assertEquals(field.getFieldId().getFieldId(), copy.getFieldId().getFieldId());
        assertTrue(field.getLabel().compareTo(copy.getLabel()) == 0);
        assertEquals(field.isValueOnly(), copy.isValueOnly());
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigInteger#attributeSelection(java.lang.String)}.
     */
    @Test
    public void testAttributeSelection() {
        boolean valueOnly = true;
        FieldConfigInteger field = new FieldConfigInteger(Integer.class, new FieldId(FieldIdEnum.NAME), "label", valueOnly);
        field.attributeSelection(null);

        field.createUI();
        assertTrue(field.isEnabled());
        field.attributeSelection("test");
        assertFalse(field.isEnabled());
        field.attributeSelection(null);
        assertTrue(field.isEnabled());
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigInteger#undoAction(com.sldeditor.common.undo.UndoInterface)}.
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigInteger#redoAction(com.sldeditor.common.undo.UndoInterface)}.
     */
    @Test
    public void testUndoAction() {
        boolean valueOnly = true;
        FieldConfigInteger field = new FieldConfigInteger(Integer.class, new FieldId(FieldIdEnum.NAME), "label", valueOnly);
        field.undoAction(null);
        field.redoAction(null);

        int expectedValue1 = 134;
        field.createUI();
        field.populateField(expectedValue1);
        assertEquals(expectedValue1, field.getIntValue());

        int expectedValue2 = 9876;
        field.setTestValue(null, expectedValue2);
        assertEquals(expectedValue2, field.getIntValue());

        UndoManager.getInstance().undo();
        assertEquals(expectedValue1, field.getIntValue());
        UndoManager.getInstance().redo();
        assertEquals(expectedValue2, field.getIntValue());

        // Increase the code coverage
        field.undoAction(null);
        field.undoAction(new UndoEvent(null, new FieldId(FieldIdEnum.NAME), "", "new"));
        field.redoAction(null);
        field.redoAction(new UndoEvent(null, new FieldId(FieldIdEnum.NAME), "", "new"));
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigInteger#testSetConfig(double, double, double)}.
     */
    @Test
    public void testSetConfig() {
        boolean valueOnly = true;
        FieldConfigInteger field = new FieldConfigInteger(Integer.class, new FieldId(FieldIdEnum.NAME), "label", valueOnly);

        field.createUI();
        int minValue = 10;
        int maxValue = 20;
        int stepSize = 1;

        field.setConfig(minValue, maxValue, stepSize);

        // Should be set to the minimum value
        int expectedValue1 = 1;
        field.populateField(expectedValue1);
        assertEquals(minValue, field.getIntValue());

        // Should be set to the maximum value
        int expectedValue2 = 41;
        field.populateField(expectedValue2);
        assertEquals(maxValue, field.getIntValue());
    }
}
