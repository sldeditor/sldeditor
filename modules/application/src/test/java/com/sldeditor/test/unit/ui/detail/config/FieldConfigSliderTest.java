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
import com.sldeditor.ui.detail.config.FieldConfigCommonData;
import com.sldeditor.ui.detail.config.FieldConfigSlider;

/**
 * The unit test for FieldConfigSlider.
 * <p>{@link com.sldeditor.ui.detail.config.FieldConfigSlider}
 *
 * @author Robert Ward (SCISYS)
 */
public class FieldConfigSliderTest {

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigSlider#setEnabled(boolean)}.
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigSlider#isEnabled()}.
     */
    @Test
    public void testSetEnabled() {
        // Value only, no attribute/expression dropdown
        boolean valueOnly = true;
        FieldConfigSlider field = new FieldConfigSlider(new FieldConfigCommonData(Double.class, FieldIdEnum.NAME, "label", valueOnly));

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
        FieldConfigSlider field2 = new FieldConfigSlider(new FieldConfigCommonData(Double.class, FieldIdEnum.NAME, "label", valueOnly));

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
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigSlider#setVisible(boolean)}.
     */
    @Test
    public void testSetVisible() {
        boolean valueOnly = true;
        FieldConfigSlider field = new FieldConfigSlider(new FieldConfigCommonData(Double.class, FieldIdEnum.NAME, "label", valueOnly));

        boolean expectedValue = true;
        field.setVisible(expectedValue);
        field.createUI();
        field.setVisible(expectedValue);

        expectedValue = false;
        field.setVisible(expectedValue);
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigSlider#generateExpression()}.
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigSlider#populateExpression(java.lang.Object, org.opengis.filter.expression.Expression)}.
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigSlider#populateField(java.lang.Double)}.
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigSlider#setTestValue(com.sldeditor.ui.detail.config.FieldId, double)}.
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigSlider#getDoubleValue()}.
     */
    @Test
    public void testGenerateExpression() {
        boolean valueOnly = true;
        FieldConfigSlider field = new FieldConfigSlider(new FieldConfigCommonData(Double.class, FieldIdEnum.NAME, "label", valueOnly));

        double defaultDefaultValue = 0.5;
        double expectedValue = 1.0;
        field.populateField(expectedValue);
        double actualValue = field.getDoubleValue();
        assertTrue(Math.abs(actualValue - defaultDefaultValue) < 0.001);

        field.setTestValue(null, expectedValue);
        actualValue = field.getDoubleValue();
        assertTrue(Math.abs(actualValue - defaultDefaultValue) < 0.001);

        field.populateExpression(null);
        actualValue = field.getDoubleValue();
        assertTrue(Math.abs(actualValue - defaultDefaultValue) < 0.001);

        field.createUI();
        field.populateField(expectedValue);
        actualValue = field.getDoubleValue();
        assertTrue(Math.abs(actualValue - expectedValue) < 0.001);

        double expectedValue2 = 0.14;
        field.setTestValue(null, expectedValue2);
        actualValue = field.getDoubleValue();
        assertTrue(Math.abs(actualValue - expectedValue2) < 0.001);

        Integer expectedValue3a = Integer.valueOf(1);
        field.populateExpression(expectedValue3a);
        actualValue = field.getDoubleValue();
        assertTrue(Math.abs(actualValue - expectedValue3a) < 0.001);

        Double expectedValue3c = Double.valueOf(0.4);
        field.populateExpression(expectedValue3c);
        actualValue = field.getDoubleValue();
        assertTrue(Math.abs(actualValue - expectedValue3c) < 0.001);

        double expectedValue3d = 0.87;
        String expectedValue3dString = String.valueOf(expectedValue3d);
        field.populateExpression(expectedValue3dString);
        actualValue = field.getDoubleValue();
        assertTrue(Math.abs(actualValue - expectedValue3d) < 0.001);
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigSlider#revertToDefaultValue()}.
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigSlider#setDefaultValue(double)}.
     */
    @Test
    public void testRevertToDefaultValue() {
        boolean valueOnly = true;
        FieldConfigSlider field = new FieldConfigSlider(new FieldConfigCommonData(Double.class, FieldIdEnum.NAME, "label", valueOnly));

        double defaultDefaultValue = 0.5; 
        Double expectedDefaultValue = 0.42;
        field.revertToDefaultValue();
        assertTrue(Math.abs(field.getDoubleValue() - defaultDefaultValue) < 0.001);

        field.createUI();
        field.setDefaultValue(expectedDefaultValue);
        field.revertToDefaultValue();
        assertTrue(Math.abs(field.getDoubleValue() - expectedDefaultValue) < 0.001);
        assertTrue(String.valueOf(expectedDefaultValue).compareTo(field.getStringValue()) == 0);
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigSlider#createCopy(com.sldeditor.ui.detail.config.FieldConfigBase)}.
     */
    @Test
    public void testCreateCopy() {
        boolean valueOnly = true;

        class TestFieldConfigSlider extends FieldConfigSlider
        {
            public TestFieldConfigSlider(FieldConfigCommonData commonData) {
                super(commonData);
            }

            public FieldConfigBase callCreateCopy(FieldConfigBase fieldConfigBase)
            {
                return createCopy(fieldConfigBase);
            }
        }

        TestFieldConfigSlider field = new TestFieldConfigSlider(new FieldConfigCommonData(Double.class, FieldIdEnum.NAME, "label", valueOnly));
        FieldConfigSlider copy = (FieldConfigSlider) field.callCreateCopy(null);
        assertNull(copy);

        copy = (FieldConfigSlider) field.callCreateCopy(field);
        assertEquals(field.getFieldId(), copy.getFieldId());
        assertTrue(field.getLabel().compareTo(copy.getLabel()) == 0);
        assertEquals(field.isValueOnly(), copy.isValueOnly());
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigSlider#attributeSelection(java.lang.String)}.
     */
    @Test
    public void testAttributeSelection() {
        boolean valueOnly = true;
        FieldConfigSlider field = new FieldConfigSlider(new FieldConfigCommonData(Double.class, FieldIdEnum.NAME, "label", valueOnly));
        field.attributeSelection(null);

        // Do nothing
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigSlider#undoAction(com.sldeditor.common.undo.UndoInterface)}.
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigSlider#redoAction(com.sldeditor.common.undo.UndoInterface)}.
     */
    @Test
    public void testUndoAction() {
        boolean valueOnly = true;
        FieldConfigSlider field = new FieldConfigSlider(new FieldConfigCommonData(Double.class, FieldIdEnum.NAME, "label", valueOnly));
        field.undoAction(null);
        field.redoAction(null);

        double expectedValue1 = 0.13;
        field.createUI();
        field.populateField(expectedValue1);
        assertTrue(Math.abs(field.getDoubleValue() - expectedValue1) < 0.001);

        double expectedValue2 = 0.98;
        field.setTestValue(null, expectedValue2);
        assertTrue(Math.abs(field.getDoubleValue() - expectedValue2) < 0.001);

        UndoManager.getInstance().undo();
        assertTrue(Math.abs(field.getDoubleValue() - expectedValue1) < 0.001);
        UndoManager.getInstance().redo();
        assertTrue(Math.abs(field.getDoubleValue() - expectedValue2) < 0.001);

        // Increase the code coverage
        field.undoAction(null);
        field.undoAction(new UndoEvent(null, FieldIdEnum.NAME, "", "new"));
        field.redoAction(null);
        field.redoAction(new UndoEvent(null, FieldIdEnum.NAME, "", "new"));
    }

}
