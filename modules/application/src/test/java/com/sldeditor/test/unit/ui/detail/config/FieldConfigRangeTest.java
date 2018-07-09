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

import com.sldeditor.common.undo.UndoEvent;
import com.sldeditor.common.undo.UndoManager;
import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.ui.detail.config.FieldConfigBase;
import com.sldeditor.ui.detail.config.FieldConfigCommonData;
import com.sldeditor.ui.detail.config.FieldConfigPopulate;
import com.sldeditor.ui.detail.config.FieldConfigRange;
import org.jaitools.numeric.Range;
import org.junit.Test;
import org.opengis.filter.expression.Expression;

/**
 * The unit test for FieldConfigRange.
 *
 * <p>{@link com.sldeditor.ui.detail.config.FieldConfigRange}
 *
 * @author Robert Ward (SCISYS)
 */
public class FieldConfigRangeTest {

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.config.FieldConfigRange#internal_setEnabled(boolean)}. Test method
     * for {@link com.sldeditor.ui.detail.config.FieldConfigRange#isEnabled()}. Test method for
     * {@link com.sldeditor.ui.detail.config.FieldConfigRange#createUI(javax.swing.Box)}.
     */
    @Test
    public void testSetEnabled() {
        // Value only, no attribute/expression dropdown
        boolean valueOnly = true;
        FieldConfigRange field =
                new FieldConfigRange(
                        new FieldConfigCommonData(
                                Double.class, FieldIdEnum.NAME, "label", valueOnly),
                        org.jaitools.numeric.Range.class);

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
        FieldConfigRange field2 =
                new FieldConfigRange(
                        new FieldConfigCommonData(
                                Double.class, FieldIdEnum.NAME, "label", valueOnly),
                        org.jaitools.numeric.Range.class);

        // Text field will not have been created
        expectedValue = true;
        field2.internal_setEnabled(expectedValue);
        assertFalse(field2.isEnabled());

        // Create text field
        field2.createUI();

        assertEquals(expectedValue, field2.isEnabled());

        expectedValue = false;
        field2.internal_setEnabled(expectedValue);

        assertEquals(expectedValue, field2.isEnabled());
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigRange#setVisible(boolean)}.
     */
    @Test
    public void testSetVisible() {
        boolean valueOnly = true;
        FieldConfigRange field =
                new FieldConfigRange(
                        new FieldConfigCommonData(
                                Double.class, FieldIdEnum.NAME, "label", valueOnly),
                        org.jaitools.numeric.Range.class);

        boolean expectedValue = true;
        field.setVisible(expectedValue);
        field.createUI();
        field.setVisible(expectedValue);

        expectedValue = false;
        field.setVisible(expectedValue);
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigRange#generateExpression()}.
     * Test method for {@link
     * com.sldeditor.ui.detail.config.FieldConfigRange#populateExpression(java.lang.Object,
     * org.opengis.filter.expression.Expression)}. Test method for {@link
     * com.sldeditor.ui.detail.config.FieldConfigRange#populateField(java.lang.Double)}. Test method
     * for {@link
     * com.sldeditor.ui.detail.config.FieldConfigRange#setTestValue(com.sldeditor.ui.detail.config.FieldId,
     * double)}. Test method for {@link
     * com.sldeditor.ui.detail.config.FieldConfigRange#getDoubleValue()}.
     */
    @SuppressWarnings("rawtypes")
    @Test
    public void testGenerateExpression() {
        boolean valueOnly = true;
        FieldConfigRange field =
                new FieldConfigRange(
                        new FieldConfigCommonData(
                                Double.class, FieldIdEnum.NAME, "label", valueOnly),
                        org.jaitools.numeric.Range.class);
        field.createUI();

        Range expectedValue = Range.create(3.0, false, 11.0, false);
        field.populateField(expectedValue);
        Range actualValueJAITools = field.getRange();
        assertTrue(
                Math.abs(
                                actualValueJAITools.getMin().doubleValue()
                                        - expectedValue.getMin().doubleValue())
                        < 0.001);
        assertEquals(actualValueJAITools.isMinIncluded(), expectedValue.isMinIncluded());
        assertTrue(
                Math.abs(
                                actualValueJAITools.getMax().doubleValue()
                                        - expectedValue.getMax().doubleValue())
                        < 0.001);
        assertEquals(actualValueJAITools.isMaxIncluded(), expectedValue.isMaxIncluded());

        field.populateExpression(null);
        actualValueJAITools = field.getRange();
        Range expectedDefaultValue = Range.create(0.0, true, 1.0, true);

        assertTrue(
                Math.abs(
                                actualValueJAITools.getMin().doubleValue()
                                        - expectedDefaultValue.getMin().doubleValue())
                        < 0.001);
        assertEquals(actualValueJAITools.isMinIncluded(), expectedDefaultValue.isMinIncluded());
        assertTrue(
                Math.abs(
                                actualValueJAITools.getMax().doubleValue()
                                        - expectedDefaultValue.getMax().doubleValue())
                        < 0.001);
        assertEquals(actualValueJAITools.isMaxIncluded(), expectedDefaultValue.isMaxIncluded());

        // Try using it.geosolutions.jaiext.range.Range
        field =
                new FieldConfigRange(
                        new FieldConfigCommonData(
                                Double.class, FieldIdEnum.NAME, "label", valueOnly),
                        it.geosolutions.jaiext.range.Range.class);
        field.createUI();

        field.populateField(expectedValue);
        Range actualValueJAIExt = field.getRange();
        assertTrue(
                Math.abs(
                                actualValueJAIExt.getMin().doubleValue()
                                        - expectedValue.getMin().doubleValue())
                        < 0.001);
        assertEquals(actualValueJAIExt.isMinIncluded(), expectedValue.isMinIncluded());
        assertTrue(
                Math.abs(
                                actualValueJAIExt.getMax().doubleValue()
                                        - expectedValue.getMax().doubleValue())
                        < 0.001);
        assertEquals(actualValueJAIExt.isMaxIncluded(), expectedValue.isMaxIncluded());
        Expression rangeJAIToolsExpression = field.getExpression();

        field.populate(rangeJAIToolsExpression);
        actualValueJAITools = field.getRange();
        assertTrue(
                Math.abs(
                                actualValueJAIExt.getMin().doubleValue()
                                        - expectedValue.getMin().doubleValue())
                        < 0.001);
        assertEquals(actualValueJAIExt.isMinIncluded(), expectedValue.isMinIncluded());
        assertTrue(
                Math.abs(
                                actualValueJAIExt.getMax().doubleValue()
                                        - expectedValue.getMax().doubleValue())
                        < 0.001);
        assertEquals(actualValueJAIExt.isMaxIncluded(), expectedValue.isMaxIncluded());
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.config.FieldConfigRange#revertToDefaultValue()}. Test method for
     * {@link com.sldeditor.ui.detail.config.FieldConfigRange#setDefaultValue(double)}.
     */
    @SuppressWarnings("rawtypes")
    @Test
    public void testRevertToDefaultValue() {
        boolean valueOnly = true;
        FieldConfigRange field =
                new FieldConfigRange(
                        new FieldConfigCommonData(
                                Double.class, FieldIdEnum.NAME, "label", valueOnly),
                        org.jaitools.numeric.Range.class);

        Range expectedDefaultValue = Range.create(0.0, true, 1.0, true);
        field.revertToDefaultValue();
        Range actualValue = field.getRange();

        assertTrue(
                Math.abs(
                                actualValue.getMin().doubleValue()
                                        - expectedDefaultValue.getMin().doubleValue())
                        < 0.001);
        assertEquals(actualValue.isMinIncluded(), expectedDefaultValue.isMinIncluded());
        assertTrue(
                Math.abs(
                                actualValue.getMax().doubleValue()
                                        - expectedDefaultValue.getMax().doubleValue())
                        < 0.001);
        assertEquals(actualValue.isMaxIncluded(), expectedDefaultValue.isMaxIncluded());

        field.createUI();
        field.createUI();
        expectedDefaultValue = Range.create(5.1, true, 7.5, false);
        field.setDefaultValue(expectedDefaultValue);
        field.revertToDefaultValue();
        actualValue = field.getRange();
        assertTrue(
                Math.abs(
                                actualValue.getMin().doubleValue()
                                        - expectedDefaultValue.getMin().doubleValue())
                        < 0.001);
        assertEquals(actualValue.isMinIncluded(), expectedDefaultValue.isMinIncluded());
        assertTrue(
                Math.abs(
                                actualValue.getMax().doubleValue()
                                        - expectedDefaultValue.getMax().doubleValue())
                        < 0.001);
        assertEquals(actualValue.isMaxIncluded(), expectedDefaultValue.isMaxIncluded());
        assertTrue(String.valueOf(expectedDefaultValue).compareTo(field.getStringValue()) == 0);
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.config.FieldConfigRange#createCopy(com.sldeditor.ui.detail.config.FieldConfigBase)}.
     */
    @Test
    public void testCreateCopy() {
        boolean valueOnly = true;

        class TestFieldConfigRange extends FieldConfigRange {
            public TestFieldConfigRange(FieldConfigCommonData commonData, Class<?> rangeClass) {
                super(commonData, rangeClass);
            }

            public FieldConfigPopulate callCreateCopy(FieldConfigBase fieldConfigBase) {
                return createCopy(fieldConfigBase);
            }
        }

        TestFieldConfigRange field =
                new TestFieldConfigRange(
                        new FieldConfigCommonData(
                                Double.class, FieldIdEnum.NAME, "label", valueOnly),
                        org.jaitools.numeric.Range.class);
        FieldConfigRange copy = (FieldConfigRange) field.callCreateCopy(null);
        assertNull(copy);

        copy = (FieldConfigRange) field.callCreateCopy(field);
        assertEquals(field.getFieldId(), copy.getFieldId());
        assertTrue(field.getLabel().compareTo(copy.getLabel()) == 0);
        assertEquals(field.isValueOnly(), copy.isValueOnly());
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.config.FieldConfigRange#attributeSelection(java.lang.String)}.
     */
    @Test
    public void testAttributeSelection() {
        boolean valueOnly = true;
        FieldConfigRange field =
                new FieldConfigRange(
                        new FieldConfigCommonData(
                                Double.class, FieldIdEnum.NAME, "label", valueOnly),
                        org.jaitools.numeric.Range.class);
        field.attributeSelection(null);

        field.createUI();
        assertTrue(field.isEnabled());
        field.attributeSelection("test");
        assertFalse(field.isEnabled());
        field.attributeSelection(null);
        assertTrue(field.isEnabled());
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.config.FieldConfigRange#undoAction(com.sldeditor.common.undo.UndoInterface)}.
     * Test method for {@link
     * com.sldeditor.ui.detail.config.FieldConfigRange#redoAction(com.sldeditor.common.undo.UndoInterface)}.
     */
    @SuppressWarnings("rawtypes")
    @Test
    public void testUndoAction() {
        boolean valueOnly = true;
        FieldConfigRange field =
                new FieldConfigRange(
                        new FieldConfigCommonData(
                                Double.class, FieldIdEnum.NAME, "label", valueOnly),
                        org.jaitools.numeric.Range.class);

        field.undoAction(null);
        field.redoAction(null);

        Range expectedValue1 = Range.create(3.0, false, 11.0, false);
        field.createUI();
        field.populateField(expectedValue1);
        Range actualValue = field.getRange();
        assertTrue(
                Math.abs(actualValue.getMin().doubleValue() - expectedValue1.getMin().doubleValue())
                        < 0.001);
        assertEquals(actualValue.isMinIncluded(), expectedValue1.isMinIncluded());
        assertTrue(
                Math.abs(actualValue.getMax().doubleValue() - expectedValue1.getMax().doubleValue())
                        < 0.001);
        assertEquals(actualValue.isMaxIncluded(), expectedValue1.isMaxIncluded());

        Range expectedValue2 = Range.create(23.0, true, 51.0, false);
        field.populateField(expectedValue2);
        actualValue = field.getRange();
        assertTrue(
                Math.abs(actualValue.getMin().doubleValue() - expectedValue2.getMin().doubleValue())
                        < 0.001);
        assertEquals(actualValue.isMinIncluded(), expectedValue2.isMinIncluded());
        assertTrue(
                Math.abs(actualValue.getMax().doubleValue() - expectedValue2.getMax().doubleValue())
                        < 0.001);
        assertEquals(actualValue.isMaxIncluded(), expectedValue2.isMaxIncluded());

        UndoManager.getInstance().undo();
        actualValue = field.getRange();
        assertTrue(
                Math.abs(actualValue.getMin().doubleValue() - expectedValue1.getMin().doubleValue())
                        < 0.001);
        assertEquals(actualValue.isMinIncluded(), expectedValue1.isMinIncluded());
        assertTrue(
                Math.abs(actualValue.getMax().doubleValue() - expectedValue1.getMax().doubleValue())
                        < 0.001);
        assertEquals(actualValue.isMaxIncluded(), expectedValue1.isMaxIncluded());
        UndoManager.getInstance().redo();
        actualValue = field.getRange();
        assertTrue(
                Math.abs(actualValue.getMin().doubleValue() - expectedValue2.getMin().doubleValue())
                        < 0.001);
        assertEquals(actualValue.isMinIncluded(), expectedValue2.isMinIncluded());
        assertTrue(
                Math.abs(actualValue.getMax().doubleValue() - expectedValue2.getMax().doubleValue())
                        < 0.001);
        assertEquals(actualValue.isMaxIncluded(), expectedValue2.isMaxIncluded());

        // Increase the code coverage
        field.undoAction(null);
        field.redoAction(null);
        field.undoAction(new UndoEvent(null, FieldIdEnum.NAME, "", "new"));
        field.redoAction(new UndoEvent(null, FieldIdEnum.NAME, "", "new"));
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigRange#testSetConfig(double,
     * double, double, double)}.
     */
    @SuppressWarnings("rawtypes")
    @Test
    public void testSetConfig() {
        boolean valueOnly = true;
        FieldConfigRange field =
                new FieldConfigRange(
                        new FieldConfigCommonData(
                                Double.class, FieldIdEnum.NAME, "label", valueOnly),
                        org.jaitools.numeric.Range.class);

        field.createUI();

        double minValue = 10.0;
        double maxValue = 20.0;
        double stepSize = 1.0;
        int noOfDecimalPlaces = 2;

        field.createUI();
        field.setConfig(minValue, maxValue, stepSize, noOfDecimalPlaces);

        // Should be set to the minimum value
        Range expectedValue1 = Range.create(3.0, false, 11.0, false);
        field.populateField(expectedValue1);
        Range actualValue = field.getRange();
        assertTrue(Math.abs(actualValue.getMin().doubleValue() - minValue) < 0.001);
        assertEquals(actualValue.isMinIncluded(), expectedValue1.isMinIncluded());
        assertTrue(
                Math.abs(actualValue.getMax().doubleValue() - expectedValue1.getMax().doubleValue())
                        < 0.001);
        assertEquals(actualValue.isMaxIncluded(), expectedValue1.isMaxIncluded());

        Range expectedValue1a = Range.create(113.0, false, 121.0, false);
        field.populateField(expectedValue1a);
        actualValue = field.getRange();
        assertTrue(Math.abs(actualValue.getMin().doubleValue() - maxValue) < 0.001);
        assertTrue(Math.abs(actualValue.getMax().doubleValue() - maxValue) < 0.001);

        // Should be set to the maximum value
        Range expectedValue2 = Range.create(15.0, false, 31.0, false);
        field.populateField(expectedValue2);
        actualValue = field.getRange();
        assertTrue(
                Math.abs(actualValue.getMin().doubleValue() - expectedValue2.getMin().doubleValue())
                        < 0.001);
        assertEquals(actualValue.isMinIncluded(), expectedValue2.isMinIncluded());
        assertTrue(Math.abs(actualValue.getMax().doubleValue() - maxValue) < 0.001);
        assertEquals(actualValue.isMaxIncluded(), expectedValue2.isMaxIncluded());

        Range expectedValue2a = Range.create(-13.0, false, -11.0, false);
        field.populateField(expectedValue2a);
        actualValue = field.getRange();
        assertTrue(Math.abs(actualValue.getMin().doubleValue() - minValue) < 0.001);
        assertTrue(Math.abs(actualValue.getMax().doubleValue() - minValue) < 0.001);
    }
}
