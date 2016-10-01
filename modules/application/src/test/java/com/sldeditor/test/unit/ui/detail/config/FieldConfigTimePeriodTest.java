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

import org.geotools.temporal.object.DefaultInstant;
import org.geotools.temporal.object.DefaultPeriod;
import org.geotools.temporal.object.DefaultPosition;
import org.junit.Test;
import org.opengis.filter.expression.Expression;

import com.sldeditor.common.undo.UndoManager;
import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.filter.v2.function.temporal.Duration;
import com.sldeditor.filter.v2.function.temporal.TimePeriod;
import com.sldeditor.ui.detail.config.FieldConfigBase;
import com.sldeditor.ui.detail.config.FieldConfigCommonData;
import com.sldeditor.ui.detail.config.FieldConfigTimePeriod;
import com.sldeditor.ui.detail.config.FieldConfigPopulate;

/**
 * The unit test for FieldConfigTimePeriod.
 * <p>{@link com.sldeditor.ui.detail.config.FieldConfigTimePeriod}
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
        FieldConfigTimePeriod field = new FieldConfigTimePeriod(new FieldConfigCommonData(String.class, FieldIdEnum.NAME, null, valueOnly));

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
        FieldConfigTimePeriod field2 = new FieldConfigTimePeriod(new FieldConfigCommonData(String.class, FieldIdEnum.NAME, null, valueOnly));

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
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigTimePeriod#setVisible(boolean)}.
     */
    @Test
    public void testSetVisible() {
        boolean valueOnly = true;
        FieldConfigTimePeriod field = new FieldConfigTimePeriod(new FieldConfigCommonData(String.class, FieldIdEnum.NAME, null, valueOnly));

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
            public TestFieldConfigTimePeriod(FieldConfigCommonData commonData) {
                super(commonData);
            }

            public Expression callGenerateExpression()
            {
                return generateExpression();
            }
        }

        TestFieldConfigTimePeriod field = new TestFieldConfigTimePeriod(new FieldConfigCommonData(String.class, FieldIdEnum.NAME, null, valueOnly));
        Expression actualExpression = field.callGenerateExpression();
        assertNotNull(actualExpression);

        // Try string values - erroneous
        field.createUI();
        String expectedValue = "test string value";
        field.setTestValue(FieldIdEnum.UNKNOWN, expectedValue);
        actualExpression = field.callGenerateExpression();
        assertNotNull(actualExpression);

        expectedValue = "test string value as expression";
        field.populateExpression(expectedValue);
        actualExpression = field.callGenerateExpression();
        assertNotNull(actualExpression);

        // Time period values
        String timePeriod = "07-07-2016T17:42:27Z / 08-07-2016T17:42:27Z";

        field.setTestValue(FieldIdEnum.UNKNOWN, (String)null);
        field.setTestValue(FieldIdEnum.UNKNOWN, timePeriod);
        actualExpression = field.callGenerateExpression();
        assertTrue(timePeriod.compareTo(actualExpression.toString()) == 0);

        TimePeriod period = new TimePeriod();
        Duration start = new Duration();
        start.setDuration(0, 0, 1, 0, 32, 9);
        period.setStart(start);

        field.populateField((TimePeriod)null);
        field.populateField(period);
        actualExpression = field.callGenerateExpression();
        String expectedPeriod = period.getString();
        assertTrue(expectedPeriod.compareTo(actualExpression.toString()) == 0);

        TimePeriod timePeriodObj = new TimePeriod();
        timePeriodObj.decode(timePeriod);
        DefaultPeriod defaultPeriod = new DefaultPeriod(
                new DefaultInstant(new DefaultPosition(timePeriodObj.getStart().getDate())),
                new DefaultInstant(new DefaultPosition(timePeriodObj.getEnd().getDate())));

        field.populateExpression(defaultPeriod);
        actualExpression = field.callGenerateExpression();
        assertTrue(timePeriod.compareTo(actualExpression.toString()) == 0);
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigTimePeriod#revertToDefaultValue()}.
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigTimePeriod#getStringValue()}.
     */
    @Test
    public void testRevertToDefaultValue() {
        boolean valueOnly = true;
        FieldConfigTimePeriod field = new FieldConfigTimePeriod(new FieldConfigCommonData(String.class, FieldIdEnum.NAME, null, valueOnly));

        String expectedDefaultValue = "default value";
        field.revertToDefaultValue();
        assertNotNull(field.getStringValue());

        field.createUI();
        field.revertToDefaultValue();
        assertTrue(expectedDefaultValue.compareTo(field.getStringValue()) != 0);
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigTimePeriod#createCopy(com.sldeditor.ui.detail.config.FieldConfigBase)}.
     */
    @Test
    public void testCreateCopy() {
        boolean valueOnly = true;

        class TestFieldConfigTimePeriod extends FieldConfigTimePeriod
        {

            public TestFieldConfigTimePeriod(FieldConfigCommonData commonData) {
                super(commonData);
            }

            public FieldConfigPopulate callCreateCopy(FieldConfigBase fieldConfigBase)
            {
                return createCopy(fieldConfigBase);
            }
        }

        TestFieldConfigTimePeriod field = new TestFieldConfigTimePeriod(new FieldConfigCommonData(String.class, FieldIdEnum.NAME, null, valueOnly));
        FieldConfigTimePeriod copy = (FieldConfigTimePeriod) field.callCreateCopy(null);
        assertNull(copy);

        copy = (FieldConfigTimePeriod) field.callCreateCopy(field);
        assertEquals(field.getFieldId(), copy.getFieldId());
        assertNull(copy.getLabel());
        assertEquals(field.isValueOnly(), copy.isValueOnly());
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigTimePeriod#attributeSelection(java.lang.String)}.
     */
    @Test
    public void testAttributeSelection() {
        boolean valueOnly = true;
        FieldConfigTimePeriod field = new FieldConfigTimePeriod(new FieldConfigCommonData(String.class, FieldIdEnum.NAME, null, valueOnly));
        field.attributeSelection(null);

        // Does nothing
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigTimePeriod#undoAction(com.sldeditor.common.undo.UndoInterface)}.
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigTimePeriod#redoAction(com.sldeditor.common.undo.UndoInterface)}.
     */
    @Test
    public void testUndoAction() {
        FieldConfigTimePeriod field = new FieldConfigTimePeriod(new FieldConfigCommonData(String.class, FieldIdEnum.NAME, null, true));
        field.undoAction(null);
        field.redoAction(null);
        field.createUI();

        // Time period values
        String timePeriod1 = "07-07-2016T17:42:27Z / 07-07-2016T17:42:27Z";
        TimePeriod period1 = new TimePeriod();
        period1.decode(timePeriod1);
        String expectedPeriod1 = period1.getString();

        field.populateField(period1);

        String timePeriod2 = "P 1 D 32 M 9 S / 08-07-2016T09:42:06Z";
        TimePeriod period2 = new TimePeriod();
        period2.decode(timePeriod2);
        String expectedPeriod2 = period2.getString();

        field.populateField(period2);

        UndoManager.getInstance().undo();
        String actualValue = field.getStringValue();
        assertTrue(actualValue.compareTo(expectedPeriod1) == 0);

        UndoManager.getInstance().redo();
        actualValue = field.getStringValue();
        assertTrue(actualValue.replace(" ",  "").compareTo(expectedPeriod2.replace(" ",  "")) == 0);
    }
}
