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

import com.sldeditor.common.undo.UndoEvent;
import com.sldeditor.common.undo.UndoManager;
import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.filter.v2.function.temporal.DateUtils;
import com.sldeditor.ui.detail.config.FieldConfigBase;
import com.sldeditor.ui.detail.config.FieldConfigCommonData;
import com.sldeditor.ui.detail.config.FieldConfigDate;
import com.sldeditor.ui.detail.config.FieldConfigPopulate;
import com.sldeditor.ui.iface.UpdateSymbolInterface;
import java.time.ZonedDateTime;
import java.util.Date;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Geometry;
import org.opengis.filter.expression.Expression;

/**
 * The unit test for FieldConfigDate.
 *
 * <p>{@link com.sldeditor.ui.detail.config.FieldConfigDate}
 *
 * @author Robert Ward (SCISYS)
 */
public class FieldConfigDateTest {

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.config.FieldConfigDate#internal_setEnabled(boolean)}. Test method for
     * {@link com.sldeditor.ui.detail.config.FieldConfigDate#isEnabled()}. Test method for {@link
     * com.sldeditor.ui.detail.config.FieldConfigDate#createUI(javax.swing.Box)}.
     */
    @Test
    public void testSetEnabled() {
        // Value only, no attribute/expression dropdown
        boolean valueOnly = true;
        FieldConfigDate field =
                new FieldConfigDate(
                        new FieldConfigCommonData(
                                Date.class, FieldIdEnum.NAME, "label", valueOnly, false));

        // Text field will not have been created
        boolean expectedValue = true;
        field.internal_setEnabled(expectedValue);

        assertFalse(field.isEnabled());

        // Create text field
        field.createUI();
        field.createUI();
        assertEquals(expectedValue, field.isEnabled());

        expectedValue = false;
        field.internal_setEnabled(expectedValue);

        assertEquals(expectedValue, field.isEnabled());

        // Has attribute/expression dropdown
        valueOnly = false;
        FieldConfigDate field2 =
                new FieldConfigDate(
                        new FieldConfigCommonData(
                                Date.class, FieldIdEnum.NAME, "label", valueOnly, false));

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
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigDate#setVisible(boolean)}.
     */
    @Test
    public void testSetVisible() {
        boolean valueOnly = true;
        FieldConfigDate field =
                new FieldConfigDate(
                        new FieldConfigCommonData(
                                Date.class, FieldIdEnum.NAME, "label", valueOnly, false));

        boolean expectedValue = true;
        field.setVisible(expectedValue);
        field.createUI();
        field.setVisible(expectedValue);

        expectedValue = false;
        field.setVisible(expectedValue);
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigDate#generateExpression()}.
     * Test method for {@link
     * com.sldeditor.ui.detail.config.FieldConfigDate#populateExpression(java.lang.Object,
     * org.opengis.filter.expression.Expression)}. Test method for {@link
     * com.sldeditor.ui.detail.config.FieldConfigDate#populateField(java.util.Date)}. Test method
     * for {@link
     * com.sldeditor.ui.detail.config.FieldConfigDate#setTestValue(com.sldeditor.ui.detail.config.FieldId,
     * java.lang.String)}. Test method for {@link
     * com.sldeditor.ui.detail.config.FieldConfigDate#getStringValue()}.
     */
    @Test
    public void testGenerateExpression() {
        boolean valueOnly = true;

        class TestFieldConfigDate extends FieldConfigDate {
            public TestFieldConfigDate(FieldConfigCommonData commonData) {
                super(commonData);
            }

            public Expression callGenerateExpression() {
                return generateExpression();
            }
        }

        TestFieldConfigDate field =
                new TestFieldConfigDate(
                        new FieldConfigCommonData(
                                Geometry.class, FieldIdEnum.NAME, "label", valueOnly, false));
        Expression actualExpression = field.callGenerateExpression();
        assertNull(actualExpression);

        // Try string values - erroneous
        field.createUI();
        field.populateExpression("");

        field.populateExpression("invalid date");

        // Increase code coverage
        @SuppressWarnings("unused")
        DateUtils x = new DateUtils();
        assertNull(DateUtils.getZonedDateTime(null));
        assertNull(DateUtils.getZonedDateTime("invalid date"));
        assertNull(DateUtils.getZonedDateTime("20A2-05-01T23:13:26+05:00"));

        String dateString = "2012-10-01T23:13:26+05:00";
        ZonedDateTime dateTime1 = DateUtils.getZonedDateTime(dateString);

        field.populateExpression(dateTime1);
        actualExpression = field.callGenerateExpression();

        assertTrue(dateString.compareTo(actualExpression.toString()) == 0);

        field.populateExpression(dateString);
        actualExpression = field.callGenerateExpression();
        assertTrue(dateString.compareTo(actualExpression.toString()) == 0);

        String dateTime2String = "2015-02-23T11:56:47Z";
        field.setTestValue(FieldIdEnum.UNKNOWN, dateTime2String);

        actualExpression = field.callGenerateExpression();
        assertTrue(dateTime2String.compareTo(actualExpression.toString()) == 0);
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.config.FieldConfigDate#revertToDefaultValue()}.
     */
    @Test
    public void testRevertToDefaultValue() {
        boolean valueOnly = true;
        FieldConfigDate field =
                new FieldConfigDate(
                        new FieldConfigCommonData(
                                Date.class, FieldIdEnum.NAME, "label", valueOnly, false));

        field.revertToDefaultValue();
        assertNull(field.getStringValue());

        field.createUI();
        field.revertToDefaultValue();
        assertNotNull(field.getStringValue());
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.config.FieldConfigDate#createCopy(com.sldeditor.ui.detail.config.FieldConfigBase)}.
     */
    @Test
    public void testCreateCopy() {
        boolean valueOnly = true;

        class TestFieldConfigDate extends FieldConfigDate {
            public TestFieldConfigDate(FieldConfigCommonData commonData) {
                super(commonData);
            }

            public FieldConfigPopulate callCreateCopy(FieldConfigBase fieldConfigBase) {
                return createCopy(fieldConfigBase);
            }
        }

        TestFieldConfigDate field =
                new TestFieldConfigDate(
                        new FieldConfigCommonData(
                                Geometry.class, FieldIdEnum.NAME, "label", valueOnly, false));
        FieldConfigDate copy = (FieldConfigDate) field.callCreateCopy(null);
        assertNull(copy);

        copy = (FieldConfigDate) field.callCreateCopy(field);
        assertEquals(field.getFieldId(), copy.getFieldId());
        assertTrue(field.getLabel().compareTo(copy.getLabel()) == 0);
        assertEquals(field.isValueOnly(), copy.isValueOnly());
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.config.FieldConfigDate#attributeSelection(java.lang.String)}.
     */
    @Test
    public void testAttributeSelection() {
        boolean valueOnly = true;
        FieldConfigDate field =
                new FieldConfigDate(
                        new FieldConfigCommonData(
                                Date.class, FieldIdEnum.NAME, "label", valueOnly, false));
        field.attributeSelection(null);

        // Does nothing
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.config.FieldConfigDate#undoAction(com.sldeditor.common.undo.UndoInterface)}.
     * Test method for {@link
     * com.sldeditor.ui.detail.config.FieldConfigDate#redoAction(com.sldeditor.common.undo.UndoInterface)}.
     */
    @Test
    public void testUndoAction() {
        FieldConfigDate field =
                new FieldConfigDate(
                        new FieldConfigCommonData(
                                Date.class, FieldIdEnum.NAME, "label", false, false));
        field.undoAction(null);
        field.redoAction(null);
        field.createUI();

        String dateString1 = "2012-10-01T23:13:26Z";
        String dateString2 = "2018-05-06T12:34:56Z";
        ZonedDateTime dateTime1 = DateUtils.getZonedDateTime(dateString1);
        ZonedDateTime dateTime2 = DateUtils.getZonedDateTime(dateString2);

        field.populateField(dateTime1);
        assertTrue(dateString1.compareTo(field.getStringValue()) == 0);

        field.populateField(dateTime2);
        assertTrue(dateString2.compareTo(field.getStringValue()) == 0);

        UndoManager.getInstance().undo();
        String actualValue = field.getStringValue();
        assertTrue(actualValue.compareTo(dateString1) == 0);

        UndoManager.getInstance().redo();
        actualValue = field.getStringValue();
        assertTrue(actualValue.compareTo(dateString2) == 0);

        field.undoAction(null);
        field.undoAction(new UndoEvent(null, FieldIdEnum.NAME, "", "new"));
        field.redoAction(null);
        field.redoAction(new UndoEvent(null, FieldIdEnum.NAME, "", "new"));
    }

    /** Test value stored. */
    @Test
    public void testValueStored() {
        UndoManager.destroyInstance();
        boolean valueOnly = true;

        class TestFieldConfigDate extends FieldConfigDate {
            public TestFieldConfigDate(FieldConfigCommonData commonData) {
                super(commonData);
            }

            /* (non-Javadoc)
             * @see com.sldeditor.ui.detail.config.FieldConfigColour#valueStored()
             */
            @Override
            protected void valueStored() {
                super.valueStored();
            }
        }

        TestFieldConfigDate field =
                new TestFieldConfigDate(
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

        field.createUI();
        field.addDataChangedListener(update);
        assertFalse(update.dataChanged);

        // Check what happens if we have not set a date
        field.valueStored();
        assertNotNull(field.getStringValue());

        int undoListSize = UndoManager.getInstance().getUndoListSize();

        String dateString1 = "2012-10-01T23:13:26Z";
        ZonedDateTime dateTime1 = DateUtils.getZonedDateTime(dateString1);
        field.populateField(dateTime1);

        assertTrue(update.dataChanged);

        assertEquals(undoListSize + 1, UndoManager.getInstance().getUndoListSize());
        update.dataChanged = false;

        // now suppress undo events
        field =
                new TestFieldConfigDate(
                        new FieldConfigCommonData(
                                Geometry.class, FieldIdEnum.NAME, "label", valueOnly, true));

        undoListSize = UndoManager.getInstance().getUndoListSize();
        field.createUI();
        field.addDataChangedListener(update);
        assertFalse(update.dataChanged);
        field.populateField(dateTime1);
        field.valueStored();
        assertTrue(update.dataChanged);

        assertEquals(undoListSize, UndoManager.getInstance().getUndoListSize());
    }
}
