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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sldeditor.common.coordinate.CoordManager;
import com.sldeditor.common.undo.UndoEvent;
import com.sldeditor.common.undo.UndoManager;
import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.ui.detail.config.FieldConfigBase;
import com.sldeditor.ui.detail.config.FieldConfigBoundingBox;
import com.sldeditor.ui.detail.config.FieldConfigCommonData;
import com.sldeditor.ui.detail.config.FieldConfigPopulate;
import com.sldeditor.ui.iface.UpdateSymbolInterface;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Geometry;
import org.opengis.filter.expression.Expression;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * The unit test for FieldConfigBoundingBox.
 *
 * <p>{@link com.sldeditor.ui.detail.config.FieldConfigBoundingBox}
 *
 * @author Robert Ward (SCISYS)
 */
public class FieldConfigBoundingBoxTest {

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.config.FieldConfigBoundingBox#internal_setEnabled(boolean)}. Test
     * method for {@link com.sldeditor.ui.detail.config.FieldConfigBoundingBox#isEnabled()}. Test
     * method for {@link
     * com.sldeditor.ui.detail.config.FieldConfigBoundingBox#createUI(javax.swing.Box)}.
     */
    @Test
    public void testSetEnabled() {
        // Value only, no attribute/expression dropdown
        boolean valueOnly = true;
        FieldConfigBoundingBox field =
                new FieldConfigBoundingBox(
                        new FieldConfigCommonData(
                                Geometry.class, FieldIdEnum.NAME, "label", valueOnly, false));

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
        FieldConfigBoundingBox field2 =
                new FieldConfigBoundingBox(
                        new FieldConfigCommonData(
                                Geometry.class, FieldIdEnum.NAME, "label", valueOnly, false));

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
     * com.sldeditor.ui.detail.config.FieldConfigBoundingBox#setVisible(boolean)}.
     */
    @Test
    public void testSetVisible() {
        boolean valueOnly = true;
        FieldConfigBoundingBox field =
                new FieldConfigBoundingBox(
                        new FieldConfigCommonData(
                                Geometry.class, FieldIdEnum.NAME, "label", valueOnly, false));

        boolean expectedValue = true;
        field.setVisible(expectedValue);
        field.createUI();
        field.setVisible(expectedValue);

        expectedValue = false;
        field.setVisible(expectedValue);
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.config.FieldConfigBoundingBox#generateExpression()}. Test method for
     * {@link
     * com.sldeditor.ui.detail.config.FieldConfigBoundingBox#populateExpression(java.lang.Object,
     * org.opengis.filter.expression.Expression)}. Test method for {@link
     * com.sldeditor.ui.detail.config.FieldConfigBoundingBox#populateField(org.geotools.geometry.jts.ReferencedEnvelope)}.
     * Test method for {@link
     * com.sldeditor.ui.detail.config.FieldConfigBoundingBox#setTestValue(com.sldeditor.ui.detail.config.FieldId,
     * boolean)}.
     */
    @Test
    public void testGenerateExpression() {
        boolean valueOnly = true;

        class TestFieldConfigBoundingBox extends FieldConfigBoundingBox {
            public TestFieldConfigBoundingBox(FieldConfigCommonData commonData) {
                super(commonData);
            }

            public Expression callGenerateExpression() {
                return generateExpression();
            }
        }

        TestFieldConfigBoundingBox field =
                new TestFieldConfigBoundingBox(
                        new FieldConfigCommonData(
                                Geometry.class, FieldIdEnum.NAME, "label", valueOnly, false));
        Expression actualExpression = field.callGenerateExpression();
        assertNotNull(actualExpression);

        // Try string values - erroneous
        field.createUI();
        field.populateExpression("");

        CoordinateReferenceSystem crs = CoordManager.getInstance().getWGS84();
        ReferencedEnvelope envelope1 = new ReferencedEnvelope(0.0, 1.0, 51.0, 51.1, crs);
        field.populateExpression(envelope1);
        actualExpression = field.callGenerateExpression();
        assertTrue(envelope1.toString().compareTo(actualExpression.toString()) == 0);

        ReferencedEnvelope envelope2 = new ReferencedEnvelope(-10.0, -4.0, 31.0, 45.11, crs);
        field.setTestValue(null, envelope2);
        actualExpression = field.callGenerateExpression();
        assertTrue(envelope2.toString().compareTo(actualExpression.toString()) == 0);
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.config.FieldConfigBoundingBox#revertToDefaultValue()}. Test method
     * for {@link com.sldeditor.ui.detail.config.FieldConfigBoundingBox#getStringValue()}.
     */
    @Test
    public void testRevertToDefaultValue() {
        boolean valueOnly = true;
        FieldConfigBoundingBox field =
                new FieldConfigBoundingBox(
                        new FieldConfigCommonData(
                                Geometry.class, FieldIdEnum.NAME, "label", valueOnly, false));

        field.revertToDefaultValue();
        assertNotNull(field.getStringValue());

        field.createUI();
        field.revertToDefaultValue();
        String expectedDefaultValue = "default value";
        assertTrue(expectedDefaultValue.compareTo(field.getStringValue()) != 0);
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.config.FieldConfigBoundingBox#createCopy(com.sldeditor.ui.detail.config.FieldConfigBase)}.
     */
    @Test
    public void testCreateCopy() {
        boolean valueOnly = true;

        class TestFieldConfigBoundingBox extends FieldConfigBoundingBox {
            public TestFieldConfigBoundingBox(FieldConfigCommonData commonData) {
                super(commonData);
            }

            public FieldConfigPopulate callCreateCopy(FieldConfigBase fieldConfigBase) {
                return createCopy(fieldConfigBase);
            }
        }

        TestFieldConfigBoundingBox field =
                new TestFieldConfigBoundingBox(
                        new FieldConfigCommonData(
                                Geometry.class, FieldIdEnum.NAME, "label", valueOnly, false));
        FieldConfigBoundingBox copy = (FieldConfigBoundingBox) field.callCreateCopy(null);
        assertNull(copy);

        copy = (FieldConfigBoundingBox) field.callCreateCopy(field);
        assertEquals(field.getFieldId(), copy.getFieldId());
        assertTrue(field.getLabel().compareTo(copy.getLabel()) == 0);
        assertEquals(field.isValueOnly(), copy.isValueOnly());
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.config.FieldConfigBoundingBox#attributeSelection(java.lang.String)}.
     */
    @Test
    public void testAttributeSelection() {
        boolean valueOnly = true;
        FieldConfigBoundingBox field =
                new FieldConfigBoundingBox(
                        new FieldConfigCommonData(
                                Geometry.class, FieldIdEnum.NAME, "label", valueOnly, false));
        field.attributeSelection(null);

        // Does nothing
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.config.FieldConfigBoundingBox#undoAction(com.sldeditor.common.undo.UndoInterface)}.
     * Test method for {@link
     * com.sldeditor.ui.detail.config.FieldConfigBoundingBox#redoAction(com.sldeditor.common.undo.UndoInterface)}.
     */
    @Test
    public void testUndoAction() {
        FieldConfigBoundingBox field =
                new FieldConfigBoundingBox(
                        new FieldConfigCommonData(
                                Geometry.class, FieldIdEnum.NAME, "label", false, false));
        field.undoAction(null);
        field.redoAction(null);
        field.createUI();
        field.createUI();

        int undoListSize = UndoManager.getInstance().getUndoListSize();

        CoordinateReferenceSystem crs = CoordManager.getInstance().getWGS84();
        ReferencedEnvelope envelope1 = new ReferencedEnvelope(0.0, 1.0, 51.0, 51.1, crs);
        field.populateField(envelope1);
        assertTrue(envelope1.toString().compareTo(field.getStringValue()) == 0);

        ReferencedEnvelope envelope2 = new ReferencedEnvelope(-10.0, -4.0, 31.0, 45.11, crs);
        field.populateField(envelope2);
        assertTrue(envelope2.toString().compareTo(field.getStringValue()) == 0);

        UndoManager.getInstance().undo();
        String actualValue = field.getStringValue();
        assertTrue(actualValue.compareTo(envelope1.toString()) == 0);

        UndoManager.getInstance().redo();
        actualValue = field.getStringValue();
        assertTrue(actualValue.compareTo(envelope2.toString()) == 0);

        assertEquals(undoListSize + 2, UndoManager.getInstance().getUndoListSize());

        // Suppress undo events
        field.setSuppressUndoEvents(true);
        field.populateField(envelope2);
        assertEquals(undoListSize + 2, UndoManager.getInstance().getUndoListSize());

        field.undoAction(null);
        field.undoAction(new UndoEvent(null, FieldIdEnum.NAME, "", "new"));
        field.redoAction(null);
        field.redoAction(new UndoEvent(null, FieldIdEnum.NAME, "", "new"));
    }

    @Test
    public void testValueStored() {
        boolean valueOnly = true;

        class TestFieldConfigBoundingBox extends FieldConfigBoundingBox {
            public TestFieldConfigBoundingBox(FieldConfigCommonData commonData) {
                super(commonData);
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

        TestFieldConfigBoundingBox field =
                new TestFieldConfigBoundingBox(
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
                new TestFieldConfigBoundingBox(
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
