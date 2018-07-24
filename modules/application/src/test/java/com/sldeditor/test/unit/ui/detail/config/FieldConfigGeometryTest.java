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
import com.sldeditor.ui.detail.config.FieldConfigGeometry;
import com.sldeditor.ui.detail.config.FieldConfigPopulate;
import com.sldeditor.ui.detail.config.FieldConfigStringButtonInterface;
import com.sldeditor.ui.iface.UpdateSymbolInterface;
import java.awt.Component;
import javax.swing.JButton;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Geometry;

/**
 * The unit test for FieldConfigGeometry.
 *
 * <p>{@link com.sldeditor.ui.detail.config.FieldConfigGeometry}
 *
 * @author Robert Ward (SCISYS)
 */
public class FieldConfigGeometryTest {

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.config.FieldConfigGeometry#internal_setEnabled(boolean)}. Test method
     * for {@link com.sldeditor.ui.detail.config.FieldConfigGeometry#isEnabled()}.
     */
    @Test
    public void testSetEnabled() {
        // Value only, no attribute/expression dropdown
        boolean valueOnly = true;
        FieldConfigGeometry field =
                new FieldConfigGeometry(
                        new FieldConfigCommonData(
                                Geometry.class, FieldIdEnum.NAME, "label", valueOnly, false),
                        "button text");

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
        FieldConfigGeometry field2 =
                new FieldConfigGeometry(
                        new FieldConfigCommonData(
                                Geometry.class, FieldIdEnum.NAME, "label", valueOnly, false),
                        "button text");

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
     * Test method for {@link
     * com.sldeditor.ui.detail.config.FieldConfigGeometry#setVisible(boolean)}.
     */
    @Test
    public void testSetVisible() {
        boolean valueOnly = true;
        FieldConfigGeometry field =
                new FieldConfigGeometry(
                        new FieldConfigCommonData(
                                Geometry.class, FieldIdEnum.NAME, "label", valueOnly, false),
                        "button text");

        boolean expectedValue = true;
        field.setVisible(expectedValue);
        field.createUI();
        field.setVisible(expectedValue);

        expectedValue = false;
        field.setVisible(expectedValue);
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.config.FieldConfigGeometry#generateExpression()}. Test method for
     * {@link
     * com.sldeditor.ui.detail.config.FieldConfigGeometry#populateExpression(java.lang.Object,
     * org.opengis.filter.expression.Expression)}. Test method for {@link
     * com.sldeditor.ui.detail.config.FieldConfigGeometry#populateField(java.lang.String)}. Test
     * method for {@link
     * com.sldeditor.ui.detail.config.FieldConfigGeometry#setTestValue(com.sldeditor.ui.detail.config.FieldId,
     * java.lang.String)}. Test method for {@link
     * com.sldeditor.ui.detail.config.FieldConfigGeometry#getStringValue()}.
     */
    @Test
    public void testGenerateExpression() {
        FieldConfigGeometry field =
                new FieldConfigGeometry(
                        new FieldConfigCommonData(
                                Geometry.class, FieldIdEnum.NAME, "label", true, false),
                        "button text");

        field.createUI();
        field.setTestValue(FieldIdEnum.UNKNOWN, (String) null);
        field.populateField((String) null);
        field.populateExpression((String) null);

        String wktPoint = "POINT( 48.44 -123.37)";

        field.populateField(wktPoint);
        String actualValue = field.getStringValue();
        assertTrue(wktPoint.compareTo(actualValue) == 0);

        String wktPolygon = "POLYGON((20 10, 30 0, 40 10, 30 20, 20 10))";

        field.setTestValue(FieldIdEnum.UNKNOWN, wktPolygon);
        actualValue = field.getStringValue();
        assertTrue(wktPolygon.compareTo(actualValue) == 0);

        String wktLine = "LINESTRING(0 2, 2 0, 8 6)";

        field.populateExpression(wktLine);
        actualValue = field.getStringValue();
        assertTrue(wktLine.compareTo(actualValue) == 0);
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.config.FieldConfigGeometry#revertToDefaultValue()}. Test method for
     * {@link com.sldeditor.ui.detail.config.FieldConfigGeometry#setDefaultValue(java.lang.String)}.
     */
    @Test
    public void testRevertToDefaultValue() {
        boolean valueOnly = true;
        FieldConfigGeometry field =
                new FieldConfigGeometry(
                        new FieldConfigCommonData(
                                Geometry.class, FieldIdEnum.NAME, "label", valueOnly, false),
                        "button text");

        field.revertToDefaultValue();
        assertNull(field.getStringValue());

        field.createUI();
        field.createUI();
        field.revertToDefaultValue();
        String expectedDefaultValue = "default value";
        assertTrue(expectedDefaultValue.compareTo(field.getStringValue()) != 0);
        field.setDefaultValue(expectedDefaultValue);
        field.revertToDefaultValue();
        assertTrue(expectedDefaultValue.compareTo(field.getStringValue()) == 0);
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.config.FieldConfigGeometry#createCopy(com.sldeditor.ui.detail.config.FieldConfigBase)}.
     */
    @Test
    public void testCreateCopy() {
        boolean valueOnly = true;

        class TestFieldConfigGeometry extends FieldConfigGeometry {
            public TestFieldConfigGeometry(FieldConfigCommonData commonData, String buttonText) {
                super(commonData, buttonText);
            }

            public FieldConfigPopulate callCreateCopy(FieldConfigBase fieldConfigBase) {
                return createCopy(fieldConfigBase);
            }
        }

        TestFieldConfigGeometry field =
                new TestFieldConfigGeometry(
                        new FieldConfigCommonData(
                                Geometry.class, FieldIdEnum.NAME, "label", valueOnly, false),
                        "button text");
        FieldConfigGeometry copy = (TestFieldConfigGeometry) field.callCreateCopy(null);
        assertNull(copy);

        copy = (FieldConfigGeometry) field.callCreateCopy(field);
        assertEquals(field.getFieldId(), copy.getFieldId());
        assertTrue(field.getLabel().compareTo(copy.getLabel()) == 0);
        assertEquals(field.isValueOnly(), copy.isValueOnly());
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.config.FieldConfigGeometry#attributeSelection(java.lang.String)}.
     */
    @Test
    public void testAttributeSelection() {
        boolean valueOnly = true;
        FieldConfigGeometry field =
                new FieldConfigGeometry(
                        new FieldConfigCommonData(
                                Geometry.class, FieldIdEnum.NAME, "label", valueOnly, false),
                        "button text");
        field.attributeSelection(null);

        // Does nothing
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.config.FieldConfigGeometry#undoAction(com.sldeditor.common.undo.UndoInterface)}.
     * Test method for {@link
     * com.sldeditor.ui.detail.config.FieldConfigGeometry#redoAction(com.sldeditor.common.undo.UndoInterface)}.
     */
    @Test
    public void testUndoAction() {
        FieldConfigGeometry field =
                new FieldConfigGeometry(
                        new FieldConfigCommonData(
                                Geometry.class, FieldIdEnum.NAME, "label", false, false),
                        "button text");
        field.undoAction(null);
        field.redoAction(null);

        field.createUI();
        field.setTestValue(FieldIdEnum.UNKNOWN, (String) null);
        field.populateField((String) null);
        field.populateExpression((String) null);

        String wktPoint = "POINT( 48.44 -123.37)";

        field.populateField(wktPoint);
        String actualValue = field.getStringValue();
        assertTrue(wktPoint.compareTo(actualValue) == 0);

        String wktPolygon = "POLYGON((20 10, 30 0, 40 10, 30 20, 20 10))";

        field.populateField(wktPolygon);
        actualValue = field.getStringValue();
        assertTrue(wktPolygon.compareTo(actualValue) == 0);

        String wktLine = "LINESTRING(0 2, 2 0, 8 6)";

        field.populateExpression(wktLine);
        actualValue = field.getStringValue();
        assertTrue(wktLine.compareTo(actualValue) == 0);

        UndoManager.getInstance().undo();
        actualValue = field.getStringValue();
        assertTrue(wktPolygon.compareTo(actualValue) == 0);

        UndoManager.getInstance().redo();
        actualValue = field.getStringValue();
        assertTrue(wktLine.compareTo(actualValue) == 0);

        // Increase the code coverage
        field.undoAction(null);
        field.undoAction(new UndoEvent(null, FieldIdEnum.NAME, "", "new"));
        field.undoAction(
                new UndoEvent(null, FieldIdEnum.NAME, Integer.valueOf(1), Integer.valueOf(2)));
        field.redoAction(null);
        field.redoAction(new UndoEvent(null, FieldIdEnum.NAME, "", "new"));
        field.redoAction(
                new UndoEvent(null, FieldIdEnum.NAME, Integer.valueOf(8), Integer.valueOf(2)));
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.config.FieldConfigGeometry#addButtonPressedListener(com.sldeditor.ui.detail.config.FieldConfigStringButtonInterface)}.
     */
    @Test
    public void testAddButtonPressedListener() {
        class TestFieldConfigGeometry extends FieldConfigGeometry {
            /**
             * Instantiates a new test field config geometry.
             *
             * @param commonData the common data
             * @param buttonText the button text
             */
            public TestFieldConfigGeometry(FieldConfigCommonData commonData, String buttonText) {
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
             * @see com.sldeditor.ui.detail.config.FieldConfigGeometry#valueStored(java.lang.String)
             */
            @Override
            protected void valueStored(String text) {
                super.valueStored(text);
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
        TestFieldConfigGeometry field =
                new TestFieldConfigGeometry(
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

        class TestFieldConfigGeometry extends FieldConfigGeometry {
            public TestFieldConfigGeometry(FieldConfigCommonData commonData) {
                super(commonData, "Button");
            }

            /*
             * (non-Javadoc)
             *
             * @see com.sldeditor.ui.detail.config.FieldConfigBoundingBox#valueStored()
             */
            @Override
            protected void valueStored(String textValue) {
                super.valueStored(textValue);
            }
        }

        TestFieldConfigGeometry field =
                new TestFieldConfigGeometry(
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
        field.valueStored("test value");
        assertTrue(update.dataChanged);

        assertEquals(undoListSize + 1, UndoManager.getInstance().getUndoListSize());
        update.dataChanged = false;

        // now suppress undo events
        field =
                new TestFieldConfigGeometry(
                        new FieldConfigCommonData(
                                Geometry.class, FieldIdEnum.NAME, "label", valueOnly, true));

        undoListSize = UndoManager.getInstance().getUndoListSize();
        field.addDataChangedListener(update);
        assertFalse(update.dataChanged);
        field.valueStored("test value again");
        assertTrue(update.dataChanged);

        assertEquals(undoListSize, UndoManager.getInstance().getUndoListSize());
    }
}
