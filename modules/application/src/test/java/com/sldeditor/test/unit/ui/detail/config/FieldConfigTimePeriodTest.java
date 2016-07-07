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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;
import org.opengis.filter.expression.Expression;
import org.opengis.temporal.Period;

import com.sldeditor.common.undo.UndoEvent;
import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.filter.v2.function.temporal.Duration;
import com.sldeditor.filter.v2.function.temporal.TimePeriod;
import com.sldeditor.ui.detail.config.FieldConfigBase;
import com.sldeditor.ui.detail.config.FieldConfigTimePeriod;
import com.sldeditor.ui.detail.config.FieldId;

/**
 * The unit test for FieldConfigTimePeriod.
 * <p>{@link com.sldeditor.ui.detail.config.FieldConfigTimePerio}
 *
 * @author Robert Ward (SCISYS)
 */
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
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigTimePeriod#populateExpression(java.lang.Object, org.opengis.filter.expression.Expression)}.
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigTimePeriod#populateField(com.sldeditor.filter.v2.function.temporal.TimePeriod)}.
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigTimePeriod#setTestValue(com.sldeditor.ui.detail.config.FieldId, java.lang.String)}.
     */
    @Test
    public void testGenerateExpression() {
        boolean valueOnly = true;

        class TestFieldConfigTimePeriod extends FieldConfigTimePeriod
        {
            public TestFieldConfigTimePeriod(Class<?> panelId, FieldId id, boolean valueOnly) {
                super(panelId, id, valueOnly);
            }

            public Expression callGenerateExpression()
            {
                return generateExpression();
            }
        }

        TestFieldConfigTimePeriod field = new TestFieldConfigTimePeriod(String.class, new FieldId(FieldIdEnum.NAME), valueOnly);
        Expression actualExpression = field.callGenerateExpression();
        assertNotNull(actualExpression);

        // Try string values - erroneous
        field.createUI(null);
        String expectedValue = "test string value";
        field.setTestValue(null, expectedValue);
        actualExpression = field.callGenerateExpression();
        assertNotNull(actualExpression);

        expectedValue = "test string value as expression";
        field.populateExpression(expectedValue, null);
        actualExpression = field.callGenerateExpression();
        assertNotNull(actualExpression);

        // Time period values
        String timePeriod = "07-07-2016T17:42:27Z / 07-07-2016T17:42:27Z";

        field.setTestValue(null, (String)null);
        field.setTestValue(null, timePeriod);
        actualExpression = field.callGenerateExpression();
        assertTrue(timePeriod.compareTo(actualExpression.toString()) == 0);

        TimePeriod period = new TimePeriod();
        Duration start = new Duration();
        start.setDuration(0, 0, 1, 0, 32, 9);
        period.setStart(start);

        field.populateField((TimePeriod)null);
        field.populateField(period);
        actualExpression = field.callGenerateExpression();
        assertTrue(period.getString().compareTo(actualExpression.toString()) == 0);
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigTimePeriod#revertToDefaultValue()}.
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigTimePeriod#getStringValue()}.
     */
    @Test
    public void testRevertToDefaultValue() {
        boolean valueOnly = true;
        FieldConfigTimePeriod field = new FieldConfigTimePeriod(String.class, new FieldId(FieldIdEnum.NAME), valueOnly);

        String expectedDefaultValue = "default value";
        field.revertToDefaultValue();
        assertNotNull(field.getStringValue());

        field.createUI(null);
        field.revertToDefaultValue();
        assertTrue(expectedDefaultValue.compareTo(field.getStringValue()) != 0);
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigTimePeriod#getClassType()}.
     */
    @Test
    public void testGetClassType() {
        boolean valueOnly = true;
        FieldConfigTimePeriod field = new FieldConfigTimePeriod(String.class, new FieldId(FieldIdEnum.NAME), valueOnly);

        assertEquals(Period.class, field.getClassType());
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigTimePeriod#createCopy(com.sldeditor.ui.detail.config.FieldConfigBase)}.
     */
    @Test
    public void testCreateCopy() {        
        boolean valueOnly = true;

        class TestFieldConfigTimePeriod extends FieldConfigTimePeriod
        {

            public TestFieldConfigTimePeriod(Class<?> panelId, FieldId id, boolean valueOnly) {
                super(panelId, id, valueOnly);
            }

            public FieldConfigBase callCreateCopy(FieldConfigBase fieldConfigBase)
            {
                return createCopy(fieldConfigBase);
            }
        }

        TestFieldConfigTimePeriod field = new TestFieldConfigTimePeriod(String.class, new FieldId(FieldIdEnum.NAME), valueOnly);
        FieldConfigTimePeriod copy = (FieldConfigTimePeriod) field.callCreateCopy(null);
        assertNull(copy);

        copy = (FieldConfigTimePeriod) field.callCreateCopy(field);
        assertEquals(field.getFieldId().getFieldId(), copy.getFieldId().getFieldId());
        assertNull(copy.getLabel());
        assertEquals(field.isValueOnly(), copy.isValueOnly());
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigTimePeriod#attributeSelection(java.lang.String)}.
     */
    @Test
    public void testAttributeSelection() {
        boolean valueOnly = true;
        FieldConfigTimePeriod field = new FieldConfigTimePeriod(String.class, new FieldId(FieldIdEnum.NAME), valueOnly);
        field.attributeSelection(null);

        // Does nothing
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigTimePeriod#undoAction(com.sldeditor.common.undo.UndoInterface)}.
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigTimePeriod#redoAction(com.sldeditor.common.undo.UndoInterface)}.
     */
    @Test
    public void testUndoAction() {
        FieldConfigTimePeriod field = new FieldConfigTimePeriod(String.class, new FieldId(FieldIdEnum.NAME), false);
        field.createUI(null);

        // Dates
        SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MMM-yyyy");
        Date date1 = null;
        String dateInString1 = "7-Jun-2013";

        SimpleDateFormat formatter2 = new SimpleDateFormat("EEEE, MMM dd, yyyy HH:mm:ss a");
        String dateInString2 = "Friday, Jun 7, 2013 12:10:56 PM";                
        Date date2 = null;

        try {
            date1 = formatter1.parse(dateInString1);
            date2 = formatter2.parse(dateInString2);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        UndoEvent undoEventDate = new UndoEvent(null, new FieldId(), date1, date2);

        field.undoAction(null);
        field.undoAction(undoEventDate);
        String actualValue = field.getStringValue();
        String expectedDate1 = "";
 //       assertTrue(actualValue.compareTo(expectedDate1) == 0);

        field.redoAction(null);
        field.redoAction(undoEventDate);
        String expectedDate2 = "";
 //       assertTrue(actualValue.compareTo(expectedDate2) == 0);

        // Time period values
        String timePeriod = "07-07-2016T17:42:27Z / 07-07-2016T17:42:27Z";
        TimePeriod period1 = new TimePeriod();
        period1.decode(timePeriod);
        String expectedPeriod1 = period1.getString();

        TimePeriod period2 = new TimePeriod();        
        Duration start = new Duration();
        start.setDuration(0, 0, 1, 0, 32, 9);
        period2.setStart(start);

        UndoEvent undoTimePeriodEvent = new UndoEvent(null, new FieldId(), period1, period2);

        field.undoAction(null);
        field.undoAction(undoTimePeriodEvent);
        actualValue = field.getStringValue();
        assertTrue(actualValue.compareTo(expectedPeriod1) == 0);

        field.redoAction(null);
        field.redoAction(undoTimePeriodEvent);
        assertTrue(actualValue.compareTo(expectedPeriod1) == 0);
    }
}
