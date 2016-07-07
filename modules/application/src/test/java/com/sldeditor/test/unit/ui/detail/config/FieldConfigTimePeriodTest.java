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
import static org.junit.Assert.fail;

import org.junit.Ignore;
import org.junit.Test;

import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.ui.detail.config.FieldConfigTimePeriod;
import com.sldeditor.ui.detail.config.FieldId;

/**
 * The unit test for FieldConfigTimePeriod.
 * <p>{@link com.sldeditor.ui.detail.config.FieldConfigTimePerio}
 *
 * @author Robert Ward (SCISYS)
 */
@Ignore
public class FieldConfigTimePeriodTest {

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigTimePeriod#setEnabled(boolean)}.
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigTimePeriod#isEnabled()}.
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigTimePeriod#createUI(javax.swing.Box)}.
     */
    @Test
    public void testSetEnabled() {
        // Value only, no attribute/expression dropdown
        boolean valueOnly = true;
        FieldConfigTimePeriod field = new FieldConfigTimePeriod(String.class, new FieldId(FieldIdEnum.NAME), valueOnly);

        // Text field will not have been created
        boolean expectedValue = true;
        field.setEnabled(expectedValue);

        assertFalse(field.isEnabled());

        // Create text field
        field.createUI(null);
        assertEquals(expectedValue, field.isEnabled());

        expectedValue = false;
        field.setEnabled(expectedValue);

        assertEquals(expectedValue, field.isEnabled());

        // Has attribute/expression dropdown
        valueOnly = false;
        FieldConfigTimePeriod field2 = new FieldConfigTimePeriod(String.class, new FieldId(FieldIdEnum.NAME), valueOnly);

        // Text field will not have been created
        expectedValue = true;
        field.setEnabled(expectedValue);
        assertFalse(field2.isEnabled());

        // Create text field
        field2.createUI(null);

        assertEquals(expectedValue, field2.isEnabled());

        expectedValue = false;
        field.setEnabled(expectedValue);

        // Actual value is coming from the attribute panel, not the text field
        assertEquals(!expectedValue, field2.isEnabled());    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigTimePeriod#setVisible(boolean)}.
     */
    @Test
    public void testSetVisible() {
        boolean valueOnly = true;
        FieldConfigTimePeriod field = new FieldConfigTimePeriod(String.class, new FieldId(FieldIdEnum.NAME), valueOnly);

        boolean expectedValue = true;
        field.setVisible(expectedValue);

        expectedValue = false;
        field.setVisible(expectedValue);
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigTimePeriod#generateExpression()}.
     */
    @Test
    public void testGenerateExpression() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigTimePeriod#revertToDefaultValue()}.
     */
    @Test
    public void testRevertToDefaultValue() {
        boolean valueOnly = true;
        FieldConfigTimePeriod field = new FieldConfigTimePeriod(String.class, new FieldId(FieldIdEnum.NAME), valueOnly);

        String expectedDefaultValue = "default value";
        field.revertToDefaultValue();
        assertNull(field.getStringValue());

        field.createUI(null);
        field.revertToDefaultValue();
        assertTrue(expectedDefaultValue.compareTo(field.getStringValue()) == 0);
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigTimePeriod#populateExpression(java.lang.Object, org.opengis.filter.expression.Expression)}.
     */
    @Test
    public void testPopulateExpression() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigTimePeriod#getClassType()}.
     */
    @Test
    public void testGetClassType() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigTimePeriod#populateField(com.sldeditor.filter.v2.function.temporal.TimePeriod)}.
     */
    @Test
    public void testPopulateFieldTimePeriod() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigTimePeriod#setTestValue(com.sldeditor.ui.detail.config.FieldId, java.lang.String)}.
     */
    @Test
    public void testSetTestValueFieldIdString() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigTimePeriod#createCopy(com.sldeditor.ui.detail.config.FieldConfigBase)}.
     */
    @Test
    public void testCreateCopy() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigTimePeriod#FieldConfigTimePeriod(java.lang.Class, com.sldeditor.ui.detail.config.FieldId, boolean)}.
     */
    @Test
    public void testFieldConfigTimePeriod() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigTimePeriod#attributeSelection(java.lang.String)}.
     */
    @Test
    public void testAttributeSelection() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigTimePeriod#getStringValue()}.
     */
    @Test
    public void testGetStringValue() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigTimePeriod#undoAction(com.sldeditor.common.undo.UndoInterface)}.
     */
    @Test
    public void testUndoAction() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigTimePeriod#redoAction(com.sldeditor.common.undo.UndoInterface)}.
     */
    @Test
    public void testRedoAction() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigTimePeriod#updateFields(com.sldeditor.ui.detail.config.FieldConfigTimePeriod.TimePeriodPanel)}.
     */
    @Test
    public void testUpdateFields() {
        fail("Not yet implemented");
    }

}
