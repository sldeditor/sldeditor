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

package com.sldeditor.test.unit.ui.detail.config.sortby;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sldeditor.common.undo.UndoEvent;
import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.ui.detail.config.FieldConfigBase;
import com.sldeditor.ui.detail.config.FieldConfigCommonData;
import com.sldeditor.ui.detail.config.FieldConfigPopulate;
import com.sldeditor.ui.detail.config.sortby.FieldConfigSortBy;
import org.junit.jupiter.api.Test;
import org.opengis.filter.expression.Expression;

/**
 * The unit test for FieldConfigSortBy.
 *
 * <p>{@link com.sldeditor.ui.detail.config.FieldConfigSortBy}
 *
 * @author Robert Ward (SCISYS)
 */
public class FieldConfigSortByTest {

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.config.FieldConfigSortBy#internal_setEnabled(boolean)}. Test method
     * for {@link com.sldeditor.ui.detail.config.FieldConfigSortBy#isEnabled()}. Test method for
     * {@link com.sldeditor.ui.detail.config.FieldConfigSortBy#createUI(javax.swing.Box)}.
     */
    @Test
    public void testSetEnabled() {
        // Value only, no attribute/expression dropdown
        boolean valueOnly = true;
        FieldConfigSortBy field =
                new FieldConfigSortBy(
                        new FieldConfigCommonData(
                                String.class, FieldIdEnum.NAME, "test label", valueOnly, false));

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
        FieldConfigSortBy field2 =
                new FieldConfigSortBy(
                        new FieldConfigCommonData(
                                String.class, FieldIdEnum.NAME, "test label", valueOnly, false));

        // Text field will not have been created
        expectedValue = true;
        field2.internal_setEnabled(expectedValue);
        assertFalse(field2.isEnabled());

        // Create text field
        field2.createUI();

        assertEquals(expectedValue, field2.isEnabled());

        expectedValue = true;
        field2.internal_setEnabled(expectedValue);

        // Actual value is coming from the attribute panel, not the text field
        assertEquals(expectedValue, field2.isEnabled());
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigSortBy#setVisible(boolean)}.
     */
    @Test
    public void testSetVisible() {
        boolean valueOnly = true;
        FieldConfigSortBy field =
                new FieldConfigSortBy(
                        new FieldConfigCommonData(
                                String.class, FieldIdEnum.NAME, "test label", valueOnly, false));

        boolean expectedValue = true;
        field.setVisible(expectedValue);

        expectedValue = false;
        field.setVisible(expectedValue);
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.config.FieldConfigSortBy#generateExpression()}. Test method for
     * {@link com.sldeditor.ui.detail.config.FieldConfigSortBy#populateExpression(java.lang.Object,
     * org.opengis.filter.expression.Expression)}.
     */
    @Test
    public void testGenerateExpression() {
        boolean valueOnly = true;

        class TestFieldConfigSortBy extends FieldConfigSortBy {

            public TestFieldConfigSortBy(FieldConfigCommonData commonData) {
                super(commonData);
            }

            public Expression callGenerateExpression() {
                return generateExpression();
            }
        }

        TestFieldConfigSortBy field =
                new TestFieldConfigSortBy(
                        new FieldConfigCommonData(
                                String.class, FieldIdEnum.NAME, "test label", valueOnly, false));
        Expression actualExpression = field.callGenerateExpression();
        assertNull(actualExpression);

        field.createUI();
        String expectedValue = "test A, test2 D";
        field.setTestValue(null, expectedValue);
        actualExpression = field.callGenerateExpression();
        assertTrue(expectedValue.compareTo(actualExpression.toString()) == 0);

        expectedValue = "test A, test3 A";
        field.populateExpression("test, test3 A");
        actualExpression = field.callGenerateExpression();
        assertTrue(expectedValue.compareTo(actualExpression.toString()) == 0);

        expectedValue = "test A, test2 D, test3 D";
        field.populateField(expectedValue);
        field.sortByUpdated(expectedValue);
        actualExpression = field.callGenerateExpression();
        assertTrue(expectedValue.compareTo(actualExpression.toString()) == 0);
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.config.FieldConfigSortBy#revertToDefaultValue()}. Test method for
     * {@link com.sldeditor.ui.detail.config.FieldConfigSortBy#setDefaultValue(java.lang.String)}.
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigSortBy#getStringValue()}.
     */
    @Test
    public void testRevertToDefaultValue() {
        boolean valueOnly = true;
        FieldConfigSortBy field =
                new FieldConfigSortBy(
                        new FieldConfigCommonData(
                                String.class, FieldIdEnum.NAME, "test label", valueOnly, false));

        String expectedDefaultValue = "default value";
        field.setDefaultValue(expectedDefaultValue);
        field.revertToDefaultValue();
        assertNull(field.getStringValue());

        field.createUI();
        field.revertToDefaultValue();
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.config.FieldConfigSortBy#createCopy(com.sldeditor.ui.detail.config.FieldConfigBase)}.
     */
    @Test
    public void testCreateCopy() {
        boolean valueOnly = true;

        class TestFieldConfigSortBy extends FieldConfigSortBy {

            public TestFieldConfigSortBy(FieldConfigCommonData commonData) {
                super(commonData);
            }

            public FieldConfigPopulate callCreateCopy(FieldConfigBase fieldConfigBase) {
                return createCopy(fieldConfigBase);
            }
        }

        TestFieldConfigSortBy field =
                new TestFieldConfigSortBy(
                        new FieldConfigCommonData(
                                String.class, FieldIdEnum.NAME, "test label", valueOnly, false));
        FieldConfigSortBy copy = (FieldConfigSortBy) field.callCreateCopy(null);
        assertNull(copy);

        copy = (FieldConfigSortBy) field.callCreateCopy(field);
        assertEquals(field.getFieldId(), copy.getFieldId());
        assertTrue(field.getLabel().compareTo(copy.getLabel()) == 0);
        assertEquals(field.isValueOnly(), copy.isValueOnly());
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.config.FieldConfigSortBy#attributeSelection(java.lang.String)}.
     */
    @Test
    public void testAttributeSelection() {
        boolean valueOnly = true;
        FieldConfigSortBy field =
                new FieldConfigSortBy(
                        new FieldConfigCommonData(
                                String.class, FieldIdEnum.NAME, "test label", valueOnly, false));

        field.attributeSelection("field");
        // Does nothing
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.config.FieldConfigSortBy#undoAction(com.sldeditor.common.undo.UndoInterface)}.
     * Test method for {@link
     * com.sldeditor.ui.detail.config.FieldConfigSortBy#redoAction(com.sldeditor.common.undo.UndoInterface)}.
     */
    @Test
    public void testUndoAction() {
        boolean valueOnly = true;
        FieldConfigSortBy field =
                new FieldConfigSortBy(
                        new FieldConfigCommonData(
                                String.class, FieldIdEnum.NAME, "test label", valueOnly, false));

        field.undoAction(null);
        field.redoAction(null);

        field.createUI();
        field.createUI();
        field.undoAction(null);
        field.redoAction(null);

        String expectedTestValue = "test A, test2 D";
        field.setTestValue(null, expectedTestValue);
        assertTrue(expectedTestValue.compareTo(field.getStringValue()) == 0);

        String expectedUndoTestValue = "test2 D";
        String expectedRedoTestValue = "test A, test2 D";

        UndoEvent undoEvent =
                new UndoEvent(
                        null, FieldIdEnum.UNKNOWN, expectedUndoTestValue, expectedRedoTestValue);
        field.undoAction(undoEvent);
        assertTrue(expectedUndoTestValue.compareTo(field.getStringValue()) == 0);

        field.redoAction(undoEvent);
        assertTrue(expectedRedoTestValue.compareTo(field.getStringValue()) == 0);
    }
}
