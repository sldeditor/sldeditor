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

import com.sldeditor.common.defaultsymbol.DefaultSymbols;
import com.sldeditor.common.undo.UndoEvent;
import com.sldeditor.common.undo.UndoManager;
import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.ui.detail.config.FieldConfigBase;
import com.sldeditor.ui.detail.config.FieldConfigColour;
import com.sldeditor.ui.detail.config.FieldConfigCommonData;
import com.sldeditor.ui.detail.config.FieldConfigPopulate;
import com.sldeditor.ui.iface.UpdateSymbolInterface;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.AttributeExpressionImpl;
import org.geotools.filter.LiteralExpressionImpl;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Geometry;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;

/**
 * The unit test for FieldConfigColour.
 *
 * <p>{@link com.sldeditor.ui.detail.config.FieldConfigColour}
 *
 * @author Robert Ward (SCISYS)
 */
public class FieldConfigColourTest {

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.config.FieldConfigColour#internalSetEnabled(boolean)}. Test method
     * for {@link com.sldeditor.ui.detail.config.FieldConfigColour#isEnabled()}.
     */
    @Test
    public void testSetEnabled() {
        // Value only, no attribute/expression dropdown
        boolean valueOnly = true;
        FieldConfigColour field =
                new FieldConfigColour(
                        new FieldConfigCommonData(
                                Geometry.class, FieldIdEnum.NAME, "label", valueOnly, false));

        // Text field will not have been created
        boolean expectedValue = true;
        field.internalSetEnabled(expectedValue);

        assertFalse(field.isEnabled());

        // Create text field
        field.createUI();
        field.createUI();
        assertEquals(expectedValue, field.isEnabled());

        expectedValue = false;
        field.internalSetEnabled(expectedValue);

        assertEquals(expectedValue, field.isEnabled());

        // Has attribute/expression dropdown
        valueOnly = false;
        FieldConfigColour field2 =
                new FieldConfigColour(
                        new FieldConfigCommonData(
                                Geometry.class, FieldIdEnum.NAME, "label", valueOnly, false));

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
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigColour#setVisible(boolean)}.
     */
    @Test
    public void testSetVisible() {
        boolean valueOnly = true;
        FieldConfigColour field =
                new FieldConfigColour(
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
     * com.sldeditor.ui.detail.config.FieldConfigColour#generateExpression()}. Test method for
     * {@link com.sldeditor.ui.detail.config.FieldConfigColour#populateExpression(java.lang.Object,
     * org.opengis.filter.expression.Expression)}. Test method for {@link
     * com.sldeditor.ui.detail.config.FieldConfigColour#setTestValue(com.sldeditor.ui.detail.config.FieldId,
     * java.lang.String)}. Test method for {@link
     * com.sldeditor.ui.detail.config.FieldConfigColour#getColourExpression()}. Test method for
     * {@link com.sldeditor.ui.detail.config.FieldConfigColour#getColourOpacityExpression()}. Test
     * method for {@link com.sldeditor.ui.detail.config.FieldConfigColour#getStringValue()}.
     */
    @Test
    public void testGenerateExpression() {
        boolean valueOnly = true;

        class TestFieldConfigColour extends FieldConfigColour {
            public TestFieldConfigColour(FieldConfigCommonData commonData) {
                super(commonData);
            }

            public Expression callGenerateExpression() {
                return generateExpression();
            }
        }

        TestFieldConfigColour field =
                new TestFieldConfigColour(
                        new FieldConfigCommonData(
                                Geometry.class, FieldIdEnum.NAME, "label", valueOnly, false));
        Expression actualExpression = field.callGenerateExpression();
        assertNull(actualExpression);
        field.setTestValue(FieldIdEnum.UNKNOWN, (String) null);
        field.populateExpression(null);
        assertNull(field.getColourExpression());
        assertNull(field.getColourOpacityExpression());

        // Try string values - erroneous
        field.createUI();
        field.populateExpression("");
        String actualValue = field.getStringValue();
        assertTrue(actualValue.compareTo("#000000") == 0);

        String colour1 = "#123456";
        field.populateExpression(colour1);
        actualValue = field.getStringValue();
        assertTrue(colour1.compareTo(actualValue) == 0);
        actualExpression = field.getColourExpression();
        assertTrue(actualExpression instanceof LiteralExpressionImpl);
        assertTrue(actualExpression.toString().compareTo(colour1) == 0);

        String colour2 = "#AABBCC";
        field.setTestValue(FieldIdEnum.UNKNOWN, colour2);
        actualValue = field.getStringValue();
        assertTrue(colour2.compareTo(actualValue) == 0);
        actualExpression = field.getColourExpression();
        assertTrue(actualExpression instanceof LiteralExpressionImpl);
        assertTrue(actualExpression.toString().compareTo(colour2) == 0);
        actualExpression = field.getColourOpacityExpression();
        assertTrue(actualExpression instanceof LiteralExpressionImpl);
        LiteralExpressionImpl literal = (LiteralExpressionImpl) actualExpression;
        double opacity = (Double) literal.getValue();
        double expectedOpacity = 1.0;
        assertTrue(Math.abs(opacity - expectedOpacity) < 0.0001);

        // Try using FieldConfigBase.populate(colour expression, opacity)
        String colour3 = "#001122";
        expectedOpacity = DefaultSymbols.defaultColourOpacity();

        FilterFactory ff = CommonFactoryFinder.getFilterFactory();

        field.populate(ff.literal(colour3));
        actualValue = field.getStringValue();
        assertTrue(colour3.compareTo(actualValue) == 0);
        actualExpression = field.getColourExpression();
        assertTrue(actualExpression instanceof LiteralExpressionImpl);
        assertTrue(actualExpression.toString().compareTo(colour3) == 0);
        actualExpression = field.getColourOpacityExpression();
        assertTrue(actualExpression instanceof LiteralExpressionImpl);
        literal = (LiteralExpressionImpl) actualExpression;
        opacity = (Double) literal.getValue();
        assertTrue(Math.abs(opacity - expectedOpacity) < 0.1);

        AttributeExpressionImpl attributeExpression = new AttributeExpressionImpl("colour");
        field.populate(attributeExpression);
        actualExpression = field.getColourExpression();
        assertTrue(actualExpression instanceof AttributeExpressionImpl);
        assertTrue(actualExpression.toString().compareTo(attributeExpression.toString()) == 0);
        actualExpression = field.getColourOpacityExpression();
        assertTrue(actualExpression.toString().compareTo(attributeExpression.toString()) == 0);
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.config.FieldConfigColour#revertToDefaultValue()}.
     */
    @Test
    public void testRevertToDefaultValue() {
        boolean valueOnly = true;
        FieldConfigColour field =
                new FieldConfigColour(
                        new FieldConfigCommonData(
                                Geometry.class, FieldIdEnum.NAME, "label", valueOnly, false));

        field.revertToDefaultValue();
        assertTrue(field.getStringValue().compareTo("") == 0);

        field.createUI();
        field.revertToDefaultValue();
        String expectedDefaultValue = "default value";
        assertTrue(expectedDefaultValue.compareTo(field.getStringValue()) != 0);
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.config.FieldConfigColour#createCopy(com.sldeditor.ui.detail.config.FieldConfigBase)}.
     */
    @Test
    public void testCreateCopy() {
        boolean valueOnly = true;

        class TestFieldConfigColour extends FieldConfigColour {
            public TestFieldConfigColour(FieldConfigCommonData commonData) {
                super(commonData);
            }

            public FieldConfigPopulate callCreateCopy(FieldConfigBase fieldConfigBase) {
                return createCopy(fieldConfigBase);
            }
        }

        TestFieldConfigColour field =
                new TestFieldConfigColour(
                        new FieldConfigCommonData(
                                Geometry.class, FieldIdEnum.NAME, "label", valueOnly, false));
        FieldConfigColour copy = (FieldConfigColour) field.callCreateCopy(null);
        assertNull(copy);

        copy = (FieldConfigColour) field.callCreateCopy(field);
        assertEquals(field.getFieldId(), copy.getFieldId());
        assertTrue(field.getLabel().compareTo(copy.getLabel()) == 0);
        assertEquals(field.isValueOnly(), copy.isValueOnly());
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.config.FieldConfigColour#attributeSelection(java.lang.String)}.
     */
    @Test
    public void testAttributeSelection() {
        boolean valueOnly = true;
        FieldConfigColour field =
                new FieldConfigColour(
                        new FieldConfigCommonData(
                                Geometry.class, FieldIdEnum.NAME, "label", valueOnly, false));
        field.attributeSelection(null);

        // Does nothing
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.config.FieldConfigColour#undoAction(com.sldeditor.common.undo.UndoInterface)}.
     * Test method for {@link
     * com.sldeditor.ui.detail.config.FieldConfigColour#redoAction(com.sldeditor.common.undo.UndoInterface)}.
     */
    @Test
    public void testUndoAction() {
        FieldConfigColour field =
                new FieldConfigColour(
                        new FieldConfigCommonData(
                                Geometry.class, FieldIdEnum.NAME, "label", false, false));
        field.undoAction(null);
        field.redoAction(null);
        field.createUI();

        String colour1 = "#123456";
        field.populateExpression(colour1);

        String colour2 = "#AABBCC";
        field.populateExpression(colour2);

        UndoManager.getInstance().undo();

        String actualValue = field.getStringValue();
        assertTrue(colour1.compareTo(actualValue) == 0);

        UndoManager.getInstance().redo();
        actualValue = field.getStringValue();
        assertTrue(colour2.compareTo(actualValue) == 0);

        // Increase the code coverage
        field.undoAction(null);
        field.undoAction(new UndoEvent(null, FieldIdEnum.NAME, "", "new"));
        field.redoAction(null);
        field.redoAction(new UndoEvent(null, FieldIdEnum.NAME, "", "new"));
    }

    /** Test value stored. */
    @Test
    public void testValueStored() {
        boolean valueOnly = true;

        class TestFieldConfigColour extends FieldConfigColour {
            public TestFieldConfigColour(FieldConfigCommonData commonData) {
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

        TestFieldConfigColour field =
                new TestFieldConfigColour(
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
        field.valueStored();
        assertTrue(update.dataChanged);

        assertEquals(undoListSize + 1, UndoManager.getInstance().getUndoListSize());
        update.dataChanged = false;

        // now suppress undo events
        field =
                new TestFieldConfigColour(
                        new FieldConfigCommonData(
                                Geometry.class, FieldIdEnum.NAME, "label", valueOnly, true));

        undoListSize = UndoManager.getInstance().getUndoListSize();
        field.addDataChangedListener(update);
        assertFalse(update.dataChanged);
        field.valueStored();
        assertTrue(update.dataChanged);

        assertEquals(undoListSize, UndoManager.getInstance().getUndoListSize());
    }
}
