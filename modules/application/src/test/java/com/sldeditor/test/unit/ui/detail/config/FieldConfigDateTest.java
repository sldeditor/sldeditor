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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.junit.Test;
import org.opengis.filter.expression.Expression;

import com.sldeditor.common.undo.UndoEvent;
import com.sldeditor.common.undo.UndoManager;
import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.ui.detail.config.FieldConfigBase;
import com.sldeditor.ui.detail.config.FieldConfigCommonData;
import com.sldeditor.ui.detail.config.FieldConfigDate;
import com.sldeditor.ui.detail.config.FieldConfigPopulate;
import com.vividsolutions.jts.geom.Geometry;

/**
 * The unit test for FieldConfigDate.
 * 
 * <p>{@link com.sldeditor.ui.detail.config.FieldConfigDate}
 *
 * @author Robert Ward (SCISYS)
 */
public class FieldConfigDateTest {

    /**
     * Test method for
     * {@link com.sldeditor.ui.detail.config.FieldConfigDate#internal_setEnabled(boolean)}. Test
     * method for {@link com.sldeditor.ui.detail.config.FieldConfigDate#isEnabled()}. Test method
     * for {@link com.sldeditor.ui.detail.config.FieldConfigDate#createUI(javax.swing.Box)}.
     */
    @Test
    public void testSetEnabled() {
        // Value only, no attribute/expression dropdown
        boolean valueOnly = true;
        FieldConfigDate field = new FieldConfigDate(
                new FieldConfigCommonData(Date.class, FieldIdEnum.NAME, "label", valueOnly));

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
        FieldConfigDate field2 = new FieldConfigDate(
                new FieldConfigCommonData(Date.class, FieldIdEnum.NAME, "label", valueOnly));

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
        FieldConfigDate field = new FieldConfigDate(
                new FieldConfigCommonData(Date.class, FieldIdEnum.NAME, "label", valueOnly));

        boolean expectedValue = true;
        field.setVisible(expectedValue);
        field.createUI();
        field.setVisible(expectedValue);

        expectedValue = false;
        field.setVisible(expectedValue);
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigDate#generateExpression()}.
     * Test method for
     * {@link com.sldeditor.ui.detail.config.FieldConfigDate#populateExpression(java.lang.Object, org.opengis.filter.expression.Expression)}.
     * Test method for
     * {@link com.sldeditor.ui.detail.config.FieldConfigDate#populateField(java.util.Date)}. Test
     * method for
     * {@link com.sldeditor.ui.detail.config.FieldConfigDate#setTestValue(com.sldeditor.ui.detail.config.FieldId, java.lang.String)}.
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigDate#getStringValue()}.
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

        TestFieldConfigDate field = new TestFieldConfigDate(
                new FieldConfigCommonData(Geometry.class, FieldIdEnum.NAME, "label", valueOnly));
        Expression actualExpression = field.callGenerateExpression();
        assertNull(actualExpression);

        // Try string values - erroneous
        field.createUI();
        field.populateExpression("");

        SimpleDateFormat f = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String dateString = "10-01-2012 23:13:26";
        Date dateTime1 = null;
        try {
            dateTime1 = f.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        field.populateExpression(dateTime1);
        actualExpression = field.callGenerateExpression();

        DateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        DateFormat tf = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
        String dateFormat1String = String.format("%sT%sZ", df.format(dateTime1),
                tf.format(dateTime1));
        assertTrue(dateFormat1String.compareTo(actualExpression.toString()) == 0);

        field.populateExpression(dateString);
        actualExpression = field.callGenerateExpression();
        String dateFormat1aString = String.format("%sT%sZ", df.format(dateTime1),
                tf.format(dateTime1));
        assertTrue(dateFormat1aString.compareTo(actualExpression.toString()) == 0);

        String dateTime2 = "23-05-2015 11:56:47";
        field.setTestValue(FieldIdEnum.UNKNOWN, dateTime2);
        Date dateTime21 = null;
        try {
            dateTime21 = f.parse(dateTime2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String dateFormat2String = String.format("%sT%sZ", df.format(dateTime21),
                tf.format(dateTime21));

        actualExpression = field.callGenerateExpression();
        assertTrue(dateFormat2String.compareTo(actualExpression.toString()) == 0);
    }

    /**
     * Test method for
     * {@link com.sldeditor.ui.detail.config.FieldConfigDate#revertToDefaultValue()}.
     */
    @Test
    public void testRevertToDefaultValue() {
        boolean valueOnly = true;
        FieldConfigDate field = new FieldConfigDate(
                new FieldConfigCommonData(Date.class, FieldIdEnum.NAME, "label", valueOnly));

        field.revertToDefaultValue();
        assertNull(field.getStringValue());

        field.createUI();
        field.revertToDefaultValue();
        String expectedDefaultValue = "default value";
        assertTrue(expectedDefaultValue.compareTo(field.getStringValue()) != 0);
    }

    /**
     * Test method for
     * {@link com.sldeditor.ui.detail.config.FieldConfigDate#createCopy(com.sldeditor.ui.detail.config.FieldConfigBase)}.
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

        TestFieldConfigDate field = new TestFieldConfigDate(
                new FieldConfigCommonData(Geometry.class, FieldIdEnum.NAME, "label", valueOnly));
        FieldConfigDate copy = (FieldConfigDate) field.callCreateCopy(null);
        assertNull(copy);

        copy = (FieldConfigDate) field.callCreateCopy(field);
        assertEquals(field.getFieldId(), copy.getFieldId());
        assertTrue(field.getLabel().compareTo(copy.getLabel()) == 0);
        assertEquals(field.isValueOnly(), copy.isValueOnly());
    }

    /**
     * Test method for
     * {@link com.sldeditor.ui.detail.config.FieldConfigDate#attributeSelection(java.lang.String)}.
     */
    @Test
    public void testAttributeSelection() {
        boolean valueOnly = true;
        FieldConfigDate field = new FieldConfigDate(
                new FieldConfigCommonData(Date.class, FieldIdEnum.NAME, "label", valueOnly));
        field.attributeSelection(null);

        // Does nothing
    }

    /**
     * Test method for
     * {@link com.sldeditor.ui.detail.config.FieldConfigDate#undoAction(com.sldeditor.common.undo.UndoInterface)}.
     * Test method for
     * {@link com.sldeditor.ui.detail.config.FieldConfigDate#redoAction(com.sldeditor.common.undo.UndoInterface)}.
     */
    @Test
    public void testUndoAction() {
        FieldConfigDate field = new FieldConfigDate(
                new FieldConfigCommonData(Date.class, FieldIdEnum.NAME, "label", false));
        field.undoAction(null);
        field.redoAction(null);
        field.createUI();

        SimpleDateFormat f = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String dateString1 = "10-01-2012 23:13:26";
        String dateString2 = "05-06-2018 12:34:56";
        Date dateTime1 = null;
        Date dateTime2 = null;
        try {
            dateTime1 = f.parse(dateString1);
            dateTime2 = f.parse(dateString2);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        field.populateField(dateTime1);

        DateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        DateFormat tf = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
        String dateFormat1String = String.format("%sT%sZ", df.format(dateTime1),
                tf.format(dateTime1));
        assertTrue(dateFormat1String.compareTo(field.getStringValue()) == 0);

        field.populateField(dateTime2);
        String dateFormat2String = String.format("%sT%sZ", df.format(dateTime2),
                tf.format(dateTime2));
        assertTrue(dateFormat2String.compareTo(field.getStringValue()) == 0);

        UndoManager.getInstance().undo();
        String actualValue = field.getStringValue();
        assertTrue(actualValue.compareTo(dateFormat1String) == 0);

        UndoManager.getInstance().redo();
        actualValue = field.getStringValue();
        assertTrue(actualValue.compareTo(dateFormat2String) == 0);

        field.undoAction(null);
        field.undoAction(new UndoEvent(null, FieldIdEnum.NAME, "", "new"));
        field.redoAction(null);
        field.redoAction(new UndoEvent(null, FieldIdEnum.NAME, "", "new"));
    }

}
