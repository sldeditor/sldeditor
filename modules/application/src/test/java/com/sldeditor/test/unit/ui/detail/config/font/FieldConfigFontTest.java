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

package com.sldeditor.test.unit.ui.detail.config.font;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.awt.GraphicsEnvironment;

import org.geotools.styling.Font;
import org.geotools.styling.StyleBuilder;
import org.junit.Test;

import com.sldeditor.common.undo.UndoEvent;
import com.sldeditor.common.undo.UndoManager;
import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.ui.detail.config.FieldConfigBase;
import com.sldeditor.ui.detail.config.FieldId;
import com.sldeditor.ui.detail.config.font.FieldConfigFont;

/**
 * The unit test for FieldConfigFont.
 * <p>{@link com.sldeditor.ui.detail.config.font.FieldConfigFont}
 *
 * @author Robert Ward (SCISYS)
 */
public class FieldConfigFontTest {

    /** The font families. */
    private String[] fontFamilies = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.font.FieldConfigFont#setEnabled(boolean)}.
     * Test method for {@link com.sldeditor.ui.detail.config.font.FieldConfigFont#isEnabled()}.
     */
    @Test
    public void testSetEnabled() {
        // Value only, no attribute/expression dropdown
        boolean valueOnly = true;
        FieldConfigFont field = new FieldConfigFont(String.class, new FieldId(FieldIdEnum.NAME), "test label", valueOnly);

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
        FieldConfigFont field2 = new FieldConfigFont(String.class, new FieldId(FieldIdEnum.NAME), "test label", valueOnly);

        // Text field will not have been created
        expectedValue = true;
        field.setEnabled(expectedValue);
        assertFalse(field2.isEnabled());

        // Create text field
        field2.createUI();

        assertEquals(expectedValue, field2.isEnabled());

        expectedValue = false;
        field.setEnabled(expectedValue);

        // Actual value is coming from the attribute panel, not the text field
        assertEquals(!expectedValue, field2.isEnabled());
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.font.FieldConfigFont#setVisible(boolean)}.
     */
    @Test
    public void testSetVisible() {
        boolean valueOnly = true;
        FieldConfigFont field = new FieldConfigFont(String.class, new FieldId(FieldIdEnum.NAME), "test label", valueOnly);

        boolean expectedValue = true;
        field.setVisible(expectedValue);

        field.createUI();
        expectedValue = false;
        field.setVisible(expectedValue);
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.font.FieldConfigFont#generateExpression()}.
     * Test method for {@link com.sldeditor.ui.detail.config.font.FieldConfigFont#populateField(org.geotools.styling.Font)}.
     * Test method for {@link com.sldeditor.ui.detail.config.font.FieldConfigFont#setTestValue(com.sldeditor.ui.detail.config.FieldId, java.lang.String)}.
     * Test method for {@link com.sldeditor.ui.detail.config.font.FieldConfigFont#getFont()}.
     * Test method for {@link com.sldeditor.ui.detail.config.font.FieldConfigFont#populateExpression(java.lang.Object, org.opengis.filter.expression.Expression)}.
     * Test method for {@link com.sldeditor.ui.detail.config.font.FieldConfigFont#getStringValue()}.
     */
    @Test
    public void testGenerateExpression() {
        boolean valueOnly = true;
        FieldConfigFont field = new FieldConfigFont(String.class, new FieldId(FieldIdEnum.NAME), "test label", valueOnly);

        field.setTestValue(null, (String)null);
        field.populateField((String)null);
        field.populateField((Font)null);
        field.populateExpression((Font)null);

        String expectedValue = fontFamilies[0];
        field.createUI();
        field.populateField(expectedValue);
        String actualValue = field.getStringValue();
        assertTrue(expectedValue.compareTo(actualValue) == 0);

        field.setTestValue(null, expectedValue);
        actualValue = field.getStringValue();
        assertTrue(expectedValue.compareTo(actualValue) == 0);

        StyleBuilder styleBuilder = new StyleBuilder();

        Font f1 = styleBuilder.createFont(expectedValue, false, true, 24.0);
        field.populateField(f1);
        actualValue = field.getStringValue();
        assertTrue(expectedValue.compareTo(actualValue) == 0);
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.font.FieldConfigFont#revertToDefaultValue()}.
     * Test method for {@link com.sldeditor.ui.detail.config.font.FieldConfigFont#setDefaultValue(java.lang.String)}.
     */
    @Test
    public void testRevertToDefaultValue() {
        boolean valueOnly = true;
        FieldConfigFont field = new FieldConfigFont(String.class, new FieldId(FieldIdEnum.NAME), "test label", valueOnly);

        String expectedDefaultValue = fontFamilies[2];
        field.revertToDefaultValue();
        String actualValue = field.getStringValue();
        assertNull(actualValue);

        field.createUI();
        field.setDefaultValue(expectedDefaultValue);
        field.revertToDefaultValue();
        actualValue = field.getStringValue();
        assertTrue(expectedDefaultValue.compareTo(actualValue) == 0);
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.font.FieldConfigFont#getClassType()}.
     */
    @Test
    public void testGetClassType() {
        boolean valueOnly = true;
        FieldConfigFont field = new FieldConfigFont(String.class, new FieldId(FieldIdEnum.NAME), "test label", valueOnly);

        assertEquals(Font.class, field.getClassType());
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.font.FieldConfigFont#createCopy(com.sldeditor.ui.detail.config.FieldConfigBase)}.
     */
    @Test
    public void testCreateCopy() {
        boolean valueOnly = true;

        class TestFieldConfigFont extends FieldConfigFont
        {

            public TestFieldConfigFont(Class<?> panelId, FieldId id, String label,
                    boolean valueOnly) {
                super(panelId, id, label, valueOnly);
            }

            public FieldConfigBase callCreateCopy(FieldConfigBase fieldConfigBase)
            {
                return createCopy(fieldConfigBase);
            }
        }

        TestFieldConfigFont field = new TestFieldConfigFont(String.class, new FieldId(FieldIdEnum.NAME), "test label", valueOnly);
        FieldConfigFont copy = (FieldConfigFont) field.callCreateCopy(null);
        assertNull(copy);

        copy = (FieldConfigFont) field.callCreateCopy(field);
        assertEquals(field.getFieldId().getFieldId(), copy.getFieldId().getFieldId());
        assertTrue(field.getLabel().compareTo(copy.getLabel()) == 0);
        assertEquals(field.isValueOnly(), copy.isValueOnly());
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.font.FieldConfigFont#attributeSelection(java.lang.String)}.
     */
    @Test
    public void testAttributeSelection() {
        boolean valueOnly = true;
        FieldConfigFont field = new FieldConfigFont(String.class, new FieldId(FieldIdEnum.NAME), "test label", valueOnly);

        field.attributeSelection("field");
        // Does nothing
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.font.FieldConfigFont#undoAction(com.sldeditor.common.undo.UndoInterface)}.
     * Test method for {@link com.sldeditor.ui.detail.config.font.FieldConfigFont#redoAction(com.sldeditor.common.undo.UndoInterface)}.
     */
    @Test
    public void testUndoAction() {
        boolean valueOnly = true;
        FieldConfigFont field = new FieldConfigFont(String.class, new FieldId(FieldIdEnum.NAME), "test label", valueOnly);

        String expectedDefaultValue1 = fontFamilies[0];
        field.createUI();
        field.populateField(expectedDefaultValue1);
        String actualValue1 = field.getStringValue();
        assertTrue(expectedDefaultValue1.compareTo(actualValue1) == 0);

        String expectedDefaultValue2 = fontFamilies[0];
        field.populateField(expectedDefaultValue2);
        String actualValue2 = field.getStringValue();
        assertTrue(expectedDefaultValue2.compareTo(actualValue2) == 0);

        UndoManager.getInstance().undo();
        String actualValue = field.getStringValue();
        assertTrue(expectedDefaultValue1.compareTo(actualValue) == 0);

        UndoManager.getInstance().redo();
        actualValue = field.getStringValue();
        assertTrue(expectedDefaultValue2.compareTo(actualValue) == 0);

        // Increase the code coverage
        field.undoAction(null);
        field.undoAction(new UndoEvent(null, new FieldId(FieldIdEnum.NAME), Double.valueOf(1.0), Double.valueOf(2.0)));
        field.redoAction(null);
        field.redoAction(new UndoEvent(null, new FieldId(FieldIdEnum.NAME), Double.valueOf(1.0), Double.valueOf(2.0)));
    }

}
