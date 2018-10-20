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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sldeditor.common.undo.UndoEvent;
import com.sldeditor.common.undo.UndoManager;
import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.ui.detail.config.FieldConfigBase;
import com.sldeditor.ui.detail.config.FieldConfigCommonData;
import com.sldeditor.ui.detail.config.FieldConfigPopulate;
import com.sldeditor.ui.detail.config.FieldConfigString;
import com.sldeditor.ui.detail.config.FieldConfigStringButtonInterface;
import com.sldeditor.ui.iface.UpdateSymbolInterface;
import java.awt.Component;
import javax.swing.JButton;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Geometry;
import org.opengis.filter.expression.Expression;

/**
 * The unit test for FieldConfigString.
 *
 * <p>{@link com.sldeditor.ui.detail.config.FieldConfigString}
 *
 * @author Robert Ward (SCISYS)
 */
public class FieldConfigStringTest {

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.config.FieldConfigString#internalSetEnabled(boolean)}. Test method
     * for {@link com.sldeditor.ui.detail.config.FieldConfigString#isEnabled()}. Test method for
     * {@link com.sldeditor.ui.detail.config.FieldConfigString#createUI(javax.swing.Box)}.
     */
    @Test
    public void testSetEnabled() {
        // Value only, no attribute/expression dropdown
        boolean valueOnly = true;
        FieldConfigString field =
                new FieldConfigString(
                        new FieldConfigCommonData(
                                String.class, FieldIdEnum.NAME, "test label", valueOnly, false),
                        "button text");

        // Text field will not have been created
        boolean expectedValue = true;
        field.internalSetEnabled(expectedValue);

        assertFalse(field.isEnabled());

        // Create text field
        field.createUI();
        assertEquals(expectedValue, field.isEnabled());

        expectedValue = false;
        field.internalSetEnabled(expectedValue);

        assertEquals(expectedValue, field.isEnabled());

        // Has attribute/expression dropdown
        valueOnly = false;
        FieldConfigString field2 =
                new FieldConfigString(
                        new FieldConfigCommonData(
                                String.class, FieldIdEnum.NAME, "test label", valueOnly, false),
                        "button text");

        // Text field will not have been created
        expectedValue = true;
        field2.internalSetEnabled(expectedValue);
        assertFalse(field2.isEnabled());

        // Create text field
        field2.createUI();

        assertEquals(expectedValue, field2.isEnabled());

        expectedValue = false;
        field2.internalSetEnabled(expectedValue);

        // Actual value is coming from the attribute panel, not the text field
        assertEquals(!expectedValue, field2.isEnabled());
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigString#setVisible(boolean)}.
     */
    @Test
    public void testSetVisible() {
        boolean valueOnly = true;
        FieldConfigString field =
                new FieldConfigString(
                        new FieldConfigCommonData(
                                String.class, FieldIdEnum.NAME, "test label", valueOnly, false),
                        "button text");

        boolean expectedValue = true;
        field.setVisible(expectedValue);

        expectedValue = false;
        field.setVisible(expectedValue);
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.config.FieldConfigString#generateExpression()}. Test method for
     * {@link com.sldeditor.ui.detail.config.FieldConfigString#populateExpression(java.lang.Object,
     * org.opengis.filter.expression.Expression)}.
     */
    @Test
    public void testGenerateExpression() {
        boolean valueOnly = true;

        class TestFieldConfigString extends FieldConfigString {

            public TestFieldConfigString(FieldConfigCommonData commonData, String buttonText) {
                super(commonData, buttonText);
            }

            public Expression callGenerateExpression() {
                return generateExpression();
            }
        }

        TestFieldConfigString field =
                new TestFieldConfigString(
                        new FieldConfigCommonData(
                                String.class, FieldIdEnum.NAME, "test label", valueOnly, false),
                        "button text");
        Expression actualExpression = field.callGenerateExpression();
        assertNull(actualExpression);

        field.createUI();
        String expectedValue = "test string value";
        field.setTestValue(null, expectedValue);
        actualExpression = field.callGenerateExpression();
        assertTrue(expectedValue.compareTo(actualExpression.toString()) == 0);

        expectedValue = "test string value as expression";
        field.populateExpression(expectedValue);
        actualExpression = field.callGenerateExpression();
        assertTrue(expectedValue.compareTo(actualExpression.toString()) == 0);

        // Try with unsupported type
        field.populateExpression(Integer.valueOf(0));
        actualExpression = field.callGenerateExpression();
        assertTrue(expectedValue.compareTo(actualExpression.toString()) == 0);
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.config.FieldConfigString#revertToDefaultValue()}. Test method for
     * {@link com.sldeditor.ui.detail.config.FieldConfigString#setDefaultValue(java.lang.String)}.
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigString#getStringValue()}.
     */
    @Test
    public void testRevertToDefaultValue() {
        boolean valueOnly = true;
        FieldConfigString field =
                new FieldConfigString(
                        new FieldConfigCommonData(
                                String.class, FieldIdEnum.NAME, "test label", valueOnly, false),
                        "button text");

        String expectedDefaultValue = "default value";
        field.setDefaultValue(expectedDefaultValue);
        field.revertToDefaultValue();
        assertNull(field.getStringValue());

        field.createUI();
        field.revertToDefaultValue();
        assertTrue(expectedDefaultValue.compareTo(field.getStringValue()) == 0);
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.config.FieldConfigString#setTestValue(com.sldeditor.ui.detail.config.FieldId,
     * java.lang.String)}.
     */
    @Test
    public void testSetTestValueFieldIdString() {
        boolean valueOnly = true;
        FieldConfigString field =
                new FieldConfigString(
                        new FieldConfigCommonData(
                                String.class, FieldIdEnum.NAME, "test label", valueOnly, false),
                        "button text");

        String expectedTestValue = "test value";
        field.setTestValue(FieldIdEnum.ANCHOR_POINT_V, expectedTestValue);
        assertNull(field.getStringValue());

        field.createUI();
        field.setTestValue(FieldIdEnum.ANCHOR_POINT_V, expectedTestValue);
        assertTrue(expectedTestValue.compareTo(field.getStringValue()) == 0);
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.config.FieldConfigString#createCopy(com.sldeditor.ui.detail.config.FieldConfigBase)}.
     */
    @Test
    public void testCreateCopy() {
        boolean valueOnly = true;

        class TestFieldConfigString extends FieldConfigString {

            public TestFieldConfigString(FieldConfigCommonData commonData, String buttonText) {
                super(commonData, buttonText);
            }

            public FieldConfigPopulate callCreateCopy(FieldConfigBase fieldConfigBase) {
                return createCopy(fieldConfigBase);
            }
        }

        TestFieldConfigString field =
                new TestFieldConfigString(
                        new FieldConfigCommonData(
                                String.class, FieldIdEnum.NAME, "test label", valueOnly, false),
                        "button text");
        FieldConfigString copy = (FieldConfigString) field.callCreateCopy(null);
        assertNull(copy);

        copy = (FieldConfigString) field.callCreateCopy(field);
        assertEquals(field.getFieldId(), copy.getFieldId());
        assertTrue(field.getLabel().compareTo(copy.getLabel()) == 0);
        assertEquals(field.isValueOnly(), copy.isValueOnly());
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.config.FieldConfigString#attributeSelection(java.lang.String)}.
     */
    @Test
    public void testAttributeSelection() {
        boolean valueOnly = true;
        FieldConfigString field =
                new FieldConfigString(
                        new FieldConfigCommonData(
                                String.class, FieldIdEnum.NAME, "test label", valueOnly, false),
                        "button text");

        field.attributeSelection("field");
        // Does nothing
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.config.FieldConfigString#undoAction(com.sldeditor.common.undo.UndoInterface)}.
     * Test method for {@link
     * com.sldeditor.ui.detail.config.FieldConfigString#redoAction(com.sldeditor.common.undo.UndoInterface)}.
     */
    @Test
    public void testUndoAction() {
        boolean valueOnly = true;
        FieldConfigString field =
                new FieldConfigString(
                        new FieldConfigCommonData(
                                String.class, FieldIdEnum.NAME, "test label", valueOnly, false),
                        "button text");

        field.undoAction(null);
        field.redoAction(null);

        field.createUI();
        field.createUI();
        field.undoAction(null);
        field.redoAction(null);

        String expectedTestValue = "test value";
        field.setTestValue(null, expectedTestValue);
        assertTrue(expectedTestValue.compareTo(field.getStringValue()) == 0);

        String expectedUndoTestValue = "undo value";
        String expectedRedoTestValue = "redo value";

        UndoEvent undoEvent =
                new UndoEvent(
                        null, FieldIdEnum.UNKNOWN, expectedUndoTestValue, expectedRedoTestValue);
        field.undoAction(undoEvent);
        assertTrue(expectedUndoTestValue.compareTo(field.getStringValue()) == 0);

        field.redoAction(undoEvent);
        assertTrue(expectedRedoTestValue.compareTo(field.getStringValue()) == 0);
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.config.FieldConfigString#addButtonPressedListener(com.sldeditor.ui.detail.config.FieldConfigStringButtonInterface)}.
     */
    @Test
    public void testAddButtonPressedListener() {
        class TestFieldConfigString extends FieldConfigString {
            /**
             * Instantiates a new test field config string.
             *
             * @param commonData the common data
             * @param buttonText the button text
             */
            public TestFieldConfigString(FieldConfigCommonData commonData, String buttonText) {
                super(commonData, buttonText);
            }

            /* (non-Javadoc)
             * @see com.sldeditor.ui.detail.config.FieldConfigGeometry#externalButtonPressed(javax.swing.JButton)
             */
            @Override
            protected void externalButtonPressed(JButton buttonExternal) {
                super.externalButtonPressed(buttonExternal);
            }

            /* (non-Javadoc)
             * @see com.sldeditor.ui.detail.config.FieldConfigString#valueStored(java.lang.String, java.lang.String)
             */
            @Override
            protected void valueStored(String prev, String text) {
                super.valueStored(prev, text);
            }
        }

        class TestFieldConfigStringButtonInterface implements FieldConfigStringButtonInterface {

            public boolean buttonPressed = false;

            @Override
            public void buttonPressed(Component buttonExternal) {
                buttonPressed = true;
            }
        };

        TestFieldConfigStringButtonInterface buttonPressedInterface =
                new TestFieldConfigStringButtonInterface();

        boolean valueOnly = true;
        TestFieldConfigString field =
                new TestFieldConfigString(
                        new FieldConfigCommonData(
                                Geometry.class, FieldIdEnum.NAME, "label", valueOnly, false),
                        "button text");

        field.addButtonPressedListener(null);

        field.addButtonPressedListener(buttonPressedInterface);
        assertFalse(buttonPressedInterface.buttonPressed);
        field.externalButtonPressed(null);
        assertTrue(buttonPressedInterface.buttonPressed);
    }

    @Test
    public void testValueStored() {
        boolean valueOnly = true;

        class TestFieldConfigString extends FieldConfigString {
            public TestFieldConfigString(FieldConfigCommonData commonData) {
                super(commonData, "Button");
            }

            /* (non-Javadoc)
             * @see com.sldeditor.ui.detail.config.FieldConfigString#valueStored(java.lang.String, java.lang.String)
             */
            @Override
            protected void valueStored(String prev, String text) {
                super.valueStored(prev, text);
            }
        }

        TestFieldConfigString field =
                new TestFieldConfigString(
                        new FieldConfigCommonData(
                                Geometry.class, FieldIdEnum.NAME, "label", valueOnly, false));

        class TestUpdateSymbol implements UpdateSymbolInterface {
            public boolean dataChanged = false;

            @Override
            public void dataChanged(FieldIdEnum changedField) {
                dataChanged = true;
            }
        };
        TestUpdateSymbol update = new TestUpdateSymbol();

        int undoListSize = UndoManager.getInstance().getUndoListSize();
        field.createUI();
        field.addDataChangedListener(update);
        assertFalse(update.dataChanged);
        field.valueStored("prev value", "test value");
        assertTrue(update.dataChanged);
        update.dataChanged = false;
        field.valueStored("same value", "same value");
        assertFalse(update.dataChanged);

        assertEquals(undoListSize + 1, UndoManager.getInstance().getUndoListSize());
        update.dataChanged = false;

        // now suppress undo events
        field =
                new TestFieldConfigString(
                        new FieldConfigCommonData(
                                Geometry.class, FieldIdEnum.NAME, "label", valueOnly, true));

        undoListSize = UndoManager.getInstance().getUndoListSize();
        field.addDataChangedListener(update);
        assertFalse(update.dataChanged);
        field.valueStored("prev value", "test value again");
        assertTrue(update.dataChanged);

        assertEquals(undoListSize, UndoManager.getInstance().getUndoListSize());
    }

    @Test
    public void testValueStoredRegExp() {
        boolean valueOnly = true;

        class TestFieldConfigString extends FieldConfigString {
            public TestFieldConfigString(FieldConfigCommonData commonData) {
                super(commonData, "Button");
            }

            /* (non-Javadoc)
             * @see com.sldeditor.ui.detail.config.FieldConfigString#valueStored(java.lang.String, java.lang.String)
             */
            @Override
            protected void valueStored(String prev, String text) {
                super.valueStored(prev, text);
            }
        }

        TestFieldConfigString field =
                new TestFieldConfigString(
                        new FieldConfigCommonData(
                                Geometry.class, FieldIdEnum.NAME, "label", valueOnly, false));

        field.setMaximumStringSize(1);
        field.setRegExpString(true);

        field.createUI();
        String testValue = ".a";
        field.setTestValue(FieldIdEnum.NAME, testValue);
        assertEquals(testValue, field.getStringValue());

        field.setTestValue(FieldIdEnum.NAME, "");
        assertEquals(".", field.getStringValue());
        field.setTestValue(FieldIdEnum.NAME, ".^");
        assertEquals(".^", field.getStringValue());
    }
}
