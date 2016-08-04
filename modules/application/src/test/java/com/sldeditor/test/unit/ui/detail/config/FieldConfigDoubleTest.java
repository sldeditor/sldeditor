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
import com.sldeditor.ui.detail.config.FieldConfigDouble;
import com.sldeditor.ui.detail.config.FieldId;

/**
 * The unit test for FieldConfigDouble.
 * <p>{@link com.sldeditor.ui.detail.config.FieldConfigDouble}
 *
 * @author Robert Ward (SCISYS)
 */
public class FieldConfigDoubleTest {

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigDouble#setEnabled(boolean)}.
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigDouble#isEnabled()}.
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigDouble#createUI(javax.swing.Box)}.
     */
    @Test
    public void testSetEnabled() {
        // Value only, no attribute/expression dropdown
        boolean valueOnly = true;
        FieldConfigDouble field = new FieldConfigDouble(Double.class, new FieldId(FieldIdEnum.NAME), "label", valueOnly);

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
        FieldConfigDouble field2 = new FieldConfigDouble(Double.class, new FieldId(FieldIdEnum.NAME), "label", valueOnly);

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
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigDouble#setVisible(boolean)}.
     */
    @Test
    public void testSetVisible() {
        boolean valueOnly = true;
        FieldConfigDouble field = new FieldConfigDouble(Double.class, new FieldId(FieldIdEnum.NAME), "label", valueOnly);

        boolean expectedValue = true;
        field.setVisible(expectedValue);
        field.createUI();
        field.setVisible(expectedValue);

        expectedValue = false;
        field.setVisible(expectedValue);
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigDouble#generateExpression()}.
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigDouble#populateExpression(java.lang.Object, org.opengis.filter.expression.Expression)}.
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigDouble#populateField(java.lang.Double)}.
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigDouble#setTestValue(com.sldeditor.ui.detail.config.FieldId, double)}.
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigDouble#getDoubleValue()}.
     */
    @Test
    public void testGenerateExpression() {
        boolean valueOnly = true;
        FieldConfigDouble field = new FieldConfigDouble(Double.class, new FieldId(FieldIdEnum.NAME), "label", valueOnly);

        double expectedValue = 1.0;
        field.populateField(expectedValue);
        double actualValue = field.getDoubleValue();
        assertTrue(Math.abs(actualValue - 0.0) < 0.001);

        field.setTestValue(null, expectedValue);
        actualValue = field.getDoubleValue();
        assertTrue(Math.abs(actualValue - 0.0) < 0.001);

        field.populateExpression(null);
        actualValue = field.getDoubleValue();
        assertTrue(Math.abs(actualValue - 0.0) < 0.001);

        field.createUI();
        field.populateField(expectedValue);
        actualValue = field.getDoubleValue();
        assertTrue(Math.abs(actualValue - expectedValue) < 0.001);

        double expectedValue2 = 0.134;
        field.setTestValue(null, expectedValue2);
        actualValue = field.getDoubleValue();
        assertTrue(Math.abs(actualValue - expectedValue2) < 0.001);

        Integer expectedValue3a = Integer.valueOf(42);
        field.populateExpression(expectedValue3a);
        actualValue = field.getDoubleValue();
        assertTrue(Math.abs(actualValue - expectedValue3a) < 0.001);

        Long expectedValue3b = Long.valueOf(4567);
        field.populateExpression(expectedValue3b);
        actualValue = field.getDoubleValue();
        assertTrue(Math.abs(actualValue - expectedValue3b) < 0.001);

        Double expectedValue3c = Double.valueOf(42.12);
        field.populateExpression(expectedValue3c);
        actualValue = field.getDoubleValue();
        assertTrue(Math.abs(actualValue - expectedValue3c) < 0.001);

        double expectedValue3d = 698.7;
        String expectedValue3dString = String.valueOf(expectedValue3d);
        field.populateExpression(expectedValue3dString);
        actualValue = field.getDoubleValue();
        assertTrue(Math.abs(actualValue - expectedValue3d) < 0.001);
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigDouble#revertToDefaultValue()}.
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigDouble#setDefaultValue(double)}.
     */
    @Test
    public void testRevertToDefaultValue() {
        boolean valueOnly = true;
        FieldConfigDouble field = new FieldConfigDouble(Double.class, new FieldId(FieldIdEnum.NAME), "label", valueOnly);

        Double expectedDefaultValue = 42.19;
        field.revertToDefaultValue();
        assertTrue(Math.abs(field.getDoubleValue() - 0.0) < 0.001);

        field.createUI();
        field.setDefaultValue(expectedDefaultValue);
        field.revertToDefaultValue();
        assertTrue(Math.abs(field.getDoubleValue() - expectedDefaultValue) < 0.001);
        assertTrue(String.valueOf(expectedDefaultValue).compareTo(field.getStringValue()) == 0);
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigDouble#createCopy(com.sldeditor.ui.detail.config.FieldConfigBase)}.
     */
    @Test
    public void testCreateCopy() {
        boolean valueOnly = true;

        class TestFieldConfigDouble extends FieldConfigDouble
        {
            public TestFieldConfigDouble(Class<?> panelId, FieldId id, String label, boolean valueOnly) {
                super(panelId, id, label, valueOnly);
            }

            public FieldConfigBase callCreateCopy(FieldConfigBase fieldConfigBase)
            {
                return createCopy(fieldConfigBase);
            }
        }

        TestFieldConfigDouble field = new TestFieldConfigDouble(Double.class, new FieldId(FieldIdEnum.NAME), "label", valueOnly);
        FieldConfigDouble copy = (FieldConfigDouble) field.callCreateCopy(null);
        assertNull(copy);

        copy = (FieldConfigDouble) field.callCreateCopy(field);
        assertEquals(field.getFieldId().getFieldId(), copy.getFieldId().getFieldId());
        assertTrue(field.getLabel().compareTo(copy.getLabel()) == 0);
        assertEquals(field.isValueOnly(), copy.isValueOnly());
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigDouble#attributeSelection(java.lang.String)}.
     */
    @Test
    public void testAttributeSelection() {
        boolean valueOnly = true;
        FieldConfigDouble field = new FieldConfigDouble(Double.class, new FieldId(FieldIdEnum.NAME), "label", valueOnly);
        field.attributeSelection(null);

        field.createUI();
        assertTrue(field.isEnabled());
        field.attributeSelection("test");
        assertFalse(field.isEnabled());
        field.attributeSelection(null);
        assertTrue(field.isEnabled());
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigDouble#undoAction(com.sldeditor.common.undo.UndoInterface)}.
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigDouble#redoAction(com.sldeditor.common.undo.UndoInterface)}.
     */
    @Test
    public void testUndoAction() {
        boolean valueOnly = true;
        FieldConfigDouble field = new FieldConfigDouble(Double.class, new FieldId(FieldIdEnum.NAME), "label", valueOnly);

        field.undoAction(null);
        field.redoAction(null);

        double expectedValue1 = 13.4;
        field.createUI();
        field.populateField(expectedValue1);
        assertTrue(Math.abs(field.getDoubleValue() - expectedValue1) < 0.001);

        double expectedValue2 = 987.6;
        field.setTestValue(null, expectedValue2);
        assertTrue(Math.abs(field.getDoubleValue() - expectedValue2) < 0.001);

        UndoManager.getInstance().undo();
        assertTrue(Math.abs(field.getDoubleValue() - expectedValue1) < 0.001);
        UndoManager.getInstance().redo();
        assertTrue(Math.abs(field.getDoubleValue() - expectedValue2) < 0.001);

        // Increase the code coverage
        field.undoAction(null);
        field.redoAction(null);
        field.undoAction(new UndoEvent(null, new FieldId(FieldIdEnum.NAME), "", "new"));
        field.redoAction(new UndoEvent(null, new FieldId(FieldIdEnum.NAME), "", "new"));
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigDouble#testSetConfig(double, double, double, double)}.
     */
    @Test
    public void testSetConfig() {
        boolean valueOnly = true;
        FieldConfigDouble field = new FieldConfigDouble(Double.class, new FieldId(FieldIdEnum.NAME), "label", valueOnly);

        field.createUI();
        double minValue = 10.0;
        double maxValue = 20.0;
        double stepSize = 1.0;
        int noOfDecimalPlaces = 2;

        field.setConfig(minValue, maxValue, stepSize, noOfDecimalPlaces);

        // Should be set to the minimum value
        double expectedValue1 = 1.4;
        field.populateField(expectedValue1);
        assertTrue(Math.abs(field.getDoubleValue() - minValue) < 0.001);

        // Should be set to the maximum value
        double expectedValue2 = 41.4;
        field.populateField(expectedValue2);
        assertTrue(Math.abs(field.getDoubleValue() - maxValue) < 0.001);
    }
}
