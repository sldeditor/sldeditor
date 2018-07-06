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
import org.locationtech.jts.geom.Geometry;

import com.sldeditor.common.undo.UndoEvent;
import com.sldeditor.common.undo.UndoManager;
import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.ui.detail.config.FieldConfigBase;
import com.sldeditor.ui.detail.config.FieldConfigBoolean;
import com.sldeditor.ui.detail.config.FieldConfigCommonData;
import com.sldeditor.ui.detail.config.FieldConfigPopulate;

/**
 * The unit test for FieldConfigBoolean.
 * 
 * <p>{@link com.sldeditor.ui.detail.config.FieldConfigBoolean}
 *
 * @author Robert Ward (SCISYS)
 */
public class FieldConfigBooleanTest {

    /**
     * Test method for
     * {@link com.sldeditor.ui.detail.config.FieldConfigBoolean#internal_setEnabled(boolean)}. Test
     * method for {@link com.sldeditor.ui.detail.config.FieldConfigBoolean#isEnabled()}.
     */
    @Test
    public void testSetEnabled() {
        // Value only, no attribute/expression dropdown
        boolean valueOnly = true;
        FieldConfigBoolean field = new FieldConfigBoolean(
                new FieldConfigCommonData(Geometry.class, FieldIdEnum.NAME, "label", valueOnly));

        // Text field will not have been created
        boolean expectedValue = true;
        field.internal_setEnabled(expectedValue);

        assertFalse(field.isEnabled());

        // Create text field
        field.createUI();
        assertEquals(expectedValue, field.isEnabled());

        expectedValue = false;
        field.internal_setEnabled(expectedValue);

        assertEquals(expectedValue, field.isEnabled());

        // Has attribute/expression dropdown
        valueOnly = false;
        FieldConfigBoolean field2 = new FieldConfigBoolean(
                new FieldConfigCommonData(Geometry.class, FieldIdEnum.NAME, "label", valueOnly));

        // Text field will not have been created
        expectedValue = true;
        field2.internal_setEnabled(expectedValue);
        assertFalse(field2.isEnabled());

        // Create text field
        field2.createUI();

        assertEquals(expectedValue, field2.isEnabled());

        expectedValue = false;
        field2.internal_setEnabled(expectedValue);

        // Actual value is coming from the attribute panel, not the text field
        assertEquals(!expectedValue, field2.isEnabled());
    }

    /**
     * Test method for
     * {@link com.sldeditor.ui.detail.config.FieldConfigBoolean#setVisible(boolean)}.
     */
    @Test
    public void testSetVisible() {
        boolean valueOnly = true;
        FieldConfigBoolean field = new FieldConfigBoolean(
                new FieldConfigCommonData(Geometry.class, FieldIdEnum.NAME, "label", valueOnly));

        boolean expectedValue = true;
        field.setVisible(expectedValue);
        field.createUI();
        field.setVisible(expectedValue);

        expectedValue = false;
        field.setVisible(expectedValue);
    }

    /**
     * Test method for
     * {@link com.sldeditor.ui.detail.config.FieldConfigBoolean#generateExpression()}.
     */
    @Test
    public void testGenerateExpression() {
        boolean valueOnly = true;
        FieldConfigBoolean field = new FieldConfigBoolean(
                new FieldConfigCommonData(Geometry.class, FieldIdEnum.NAME, "label", valueOnly));

        field.setTestValue(null, true);
        field.populateField((Boolean) null);
        field.populateField(Boolean.TRUE);
        field.populateExpression(null);

        field.createUI();
        field.createUI();
        field.populateField(Boolean.TRUE);
        assertTrue(field.getBooleanValue());

        field.populateExpression(Boolean.FALSE);
        assertFalse(field.getBooleanValue());

        field.setTestValue(null, true);
        assertTrue(field.getBooleanValue());
        assertTrue(field.getStringValue().toLowerCase().compareTo("true") == 0);
    }

    /**
     * Test method for
     * {@link com.sldeditor.ui.detail.config.FieldConfigBoolean#revertToDefaultValue()}. Test method
     * for {@link com.sldeditor.ui.detail.config.FieldConfigBoolean#setDefaultValue(boolean)}.
     */
    @Test
    public void testRevertToDefaultValue() {
        boolean valueOnly = true;
        FieldConfigBoolean field = new FieldConfigBoolean(
                new FieldConfigCommonData(Geometry.class, FieldIdEnum.NAME, "label", valueOnly));

        field.revertToDefaultValue();
        assertFalse(field.getBooleanValue());

        field.createUI();
        field.revertToDefaultValue();
        assertFalse(field.getBooleanValue());

        boolean expectedDefaultValue = true;
        field.setDefaultValue(expectedDefaultValue);
        field.revertToDefaultValue();
        assertEquals(expectedDefaultValue, field.getBooleanValue());
    }

    /**
     * Test method for
     * {@link com.sldeditor.ui.detail.config.FieldConfigBoolean#populateExpression(java.lang.Object, org.opengis.filter.expression.Expression)}.
     * Test method for
     * {@link com.sldeditor.ui.detail.config.FieldConfigBoolean#populateField(java.lang.Boolean)}.
     * Test method for
     * {@link com.sldeditor.ui.detail.config.FieldConfigBoolean#setTestValue(com.sldeditor.ui.detail.config.FieldId, boolean)}.
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigBoolean#getBooleanValue()}.
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigBoolean#getStringValue()}.
     */
    @Test
    public void testPopulateExpression() {
    }

    /**
     * Test method for
     * {@link com.sldeditor.ui.detail.config.FieldConfigBoolean#createCopy(com.sldeditor.ui.detail.config.FieldConfigBase)}.
     */
    @Test
    public void testCreateCopy() {
        boolean valueOnly = true;

        class TestFieldConfigBoolean extends FieldConfigBoolean {
            public TestFieldConfigBoolean(FieldConfigCommonData commonData) {
                super(commonData);
            }

            public FieldConfigPopulate callCreateCopy(FieldConfigBase fieldConfigBase) {
                return createCopy(fieldConfigBase);
            }
        }

        TestFieldConfigBoolean field = new TestFieldConfigBoolean(
                new FieldConfigCommonData(Geometry.class, FieldIdEnum.NAME, "label", valueOnly));
        FieldConfigBoolean copy = (FieldConfigBoolean) field.callCreateCopy(null);
        assertNull(copy);

        copy = (FieldConfigBoolean) field.callCreateCopy(field);
        assertEquals(field.getFieldId(), copy.getFieldId());
        assertTrue(field.getLabel().compareTo(copy.getLabel()) == 0);
        assertEquals(field.isValueOnly(), copy.isValueOnly());
    }

    /**
     * Test method for
     * {@link com.sldeditor.ui.detail.config.FieldConfigBoolean#attributeSelection(java.lang.String)}.
     */
    @Test
    public void testAttributeSelection() {
        boolean valueOnly = true;
        FieldConfigBoolean field = new FieldConfigBoolean(
                new FieldConfigCommonData(Geometry.class, FieldIdEnum.NAME, "label", valueOnly));
        field.attributeSelection(null);

        // Does nothing
    }

    /**
     * Test method for
     * {@link com.sldeditor.ui.detail.config.FieldConfigBoolean#undoAction(com.sldeditor.common.undo.UndoInterface)}.
     * Test method for
     * {@link com.sldeditor.ui.detail.config.FieldConfigBoolean#redoAction(com.sldeditor.common.undo.UndoInterface)}.
     */
    @Test
    public void testUndoAction() {
        boolean valueOnly = true;
        FieldConfigBoolean field = new FieldConfigBoolean(
                new FieldConfigCommonData(Geometry.class, FieldIdEnum.NAME, "label", valueOnly));
        field.undoAction(null);
        field.redoAction(null);

        field.createUI();
        field.populateField(Boolean.TRUE);
        field.populateField(Boolean.FALSE);
        assertFalse(field.getBooleanValue());

        UndoManager.getInstance().undo();
        assertTrue(field.getBooleanValue());
        UndoManager.getInstance().redo();
        assertFalse(field.getBooleanValue());

        field.setTestValue(null, true);
        assertTrue(field.getBooleanValue());
        assertTrue(field.getStringValue().toLowerCase().compareTo("true") == 0);

        // Increase the code coverage
        field.undoAction(null);
        field.undoAction(new UndoEvent(null, FieldIdEnum.NAME, "", "new"));
        field.redoAction(null);
        field.redoAction(new UndoEvent(null, FieldIdEnum.NAME, "", "new"));
    }

}
