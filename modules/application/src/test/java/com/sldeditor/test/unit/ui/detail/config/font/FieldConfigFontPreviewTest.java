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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.ui.detail.config.FieldConfigBase;
import com.sldeditor.ui.detail.config.FieldConfigCommonData;
import com.sldeditor.ui.detail.config.FieldConfigPopulate;
import com.sldeditor.ui.detail.config.font.FieldConfigFontPreview;
import java.awt.GraphicsEnvironment;
import org.geotools.styling.Font;
import org.geotools.styling.StyleBuilder;
import org.junit.Test;

/**
 * The unit test for FieldConfigFontPreview.
 *
 * <p>{@link com.sldeditor.ui.detail.config.font.FieldConfigFontPreview}
 *
 * @author Robert Ward (SCISYS)
 */
public class FieldConfigFontPreviewTest {

    /** The font families. */
    private String[] fontFamilies =
            GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.config.font.FieldConfigFontPreview#internal_setEnabled(boolean)}.
     * Test method for {@link
     * com.sldeditor.ui.detail.config.font.FieldConfigFontPreview#isEnabled()}.
     */
    @Test
    public void testSetEnabled() {
        // Value only, no attribute/expression dropdown
        boolean valueOnly = true;
        FieldConfigFontPreview field =
                new FieldConfigFontPreview(
                        new FieldConfigCommonData(
                                String.class, FieldIdEnum.NAME, "test label", valueOnly));

        // Text field will not have been created
        boolean expectedValue = true;
        field.internal_setEnabled(expectedValue);

        assertTrue(field.isEnabled());

        // Create text field
        field.createUI();
        field.createUI();
        assertEquals(expectedValue, field.isEnabled());

        expectedValue = false;
        field.internal_setEnabled(expectedValue);

        assertTrue(field.isEnabled());

        // Has attribute/expression dropdown
        valueOnly = false;
        FieldConfigFontPreview field2 =
                new FieldConfigFontPreview(
                        new FieldConfigCommonData(
                                String.class, FieldIdEnum.NAME, "test label", valueOnly));

        // Text field will not have been created
        expectedValue = true;
        field.internal_setEnabled(expectedValue);
        assertTrue(field2.isEnabled());

        // Create text field
        field2.createUI();

        assertEquals(expectedValue, field2.isEnabled());

        expectedValue = false;
        field.internal_setEnabled(expectedValue);

        // Actual value is coming from the attribute panel, not the text field
        assertTrue(field2.isEnabled());
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.config.font.FieldConfigFontPreview#setVisible(boolean)}.
     */
    @Test
    public void testSetVisible() {
        boolean valueOnly = true;
        FieldConfigFontPreview field =
                new FieldConfigFontPreview(
                        new FieldConfigCommonData(
                                String.class, FieldIdEnum.NAME, "test label", valueOnly));

        boolean expectedValue = true;
        field.setVisible(expectedValue);

        field.createUI();
        expectedValue = false;
        field.setVisible(expectedValue);
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.config.font.FieldConfigFontPreview#generateExpression()}. Test method
     * for {@link
     * com.sldeditor.ui.detail.config.font.FieldConfigFontPreview#populateExpression(java.lang.Object,
     * org.opengis.filter.expression.Expression)}. Test method for {@link
     * com.sldeditor.ui.detail.config.font.FieldConfigFontPreview#populateField(org.geotools.styling.Font)}.
     * Test method for {@link
     * com.sldeditor.ui.detail.config.font.FieldConfigFontPreview#setTestValue(com.sldeditor.ui.detail.config.FieldId,
     * java.lang.String)}. Test method for {@link
     * com.sldeditor.ui.detail.config.font.FieldConfigFontPreview#getStringValue()}.
     */
    @Test
    public void testGenerateExpression() {
        boolean valueOnly = true;
        FieldConfigFontPreview field =
                new FieldConfigFontPreview(
                        new FieldConfigCommonData(
                                String.class, FieldIdEnum.NAME, "test label", valueOnly));

        field.setTestValue(FieldIdEnum.UNKNOWN, (String) null);
        field.populateField((String) null);
        field.populateField((Font) null);
        field.populateExpression((Font) null);

        String expectedValue = fontFamilies[0];
        field.createUI();
        field.populateField(expectedValue);
        String actualValue = field.getStringValue();
        assertNotNull(actualValue);

        field.setTestValue(FieldIdEnum.UNKNOWN, expectedValue);
        actualValue = field.getStringValue();
        assertNotNull(actualValue);

        StyleBuilder styleBuilder = new StyleBuilder();

        Font f1 = styleBuilder.createFont(expectedValue, false, true, 24.0);
        field.populateField(f1);
        assertNotNull(field.getStringValue());

        Font f2 = styleBuilder.createFont(expectedValue, true, true, 24.0);
        field.populateField(f2);
        assertNotNull(field.getStringValue());

        Font f3 = styleBuilder.createFont(expectedValue, true, false, 24.0);
        field.populateField(f3);
        assertNotNull(field.getStringValue());

        Font f4 = styleBuilder.createFont(expectedValue, false, false, 24.0);
        field.populateField(f4);
        assertNotNull(field.getStringValue());
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.config.font.FieldConfigFontPreview#revertToDefaultValue()}. Test
     * method for {@link
     * com.sldeditor.ui.detail.config.font.FieldConfigFontPreview#setDefaultValue(java.lang.String)}.
     */
    @Test
    public void testRevertToDefaultValue() {
        boolean valueOnly = true;
        FieldConfigFontPreview field =
                new FieldConfigFontPreview(
                        new FieldConfigCommonData(
                                String.class, FieldIdEnum.NAME, "test label", valueOnly));

        field.revertToDefaultValue();
        String actualValue = field.getStringValue();
        assertNull(actualValue);

        field.createUI();
        String expectedDefaultValue = fontFamilies[2];
        field.setDefaultValue(expectedDefaultValue);
        field.revertToDefaultValue();
        actualValue = field.getStringValue();
        // It returns the sample text
        assertNotNull(actualValue);
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.config.font.FieldConfigFontPreview#createCopy(com.sldeditor.ui.detail.config.FieldConfigBase)}.
     */
    @Test
    public void testCreateCopy() {
        boolean valueOnly = true;

        class TestFieldConfigFontPreview extends FieldConfigFontPreview {

            public TestFieldConfigFontPreview(FieldConfigCommonData commonData) {
                super(commonData);
            }

            public FieldConfigPopulate callCreateCopy(FieldConfigBase fieldConfigBase) {
                return createCopy(fieldConfigBase);
            }
        }

        TestFieldConfigFontPreview field =
                new TestFieldConfigFontPreview(
                        new FieldConfigCommonData(
                                String.class, FieldIdEnum.NAME, "test label", valueOnly));
        FieldConfigFontPreview copy = (FieldConfigFontPreview) field.callCreateCopy(null);
        assertNull(copy);

        copy = (FieldConfigFontPreview) field.callCreateCopy(field);
        assertEquals(field.getFieldId(), copy.getFieldId());
        assertTrue(field.getLabel().compareTo(copy.getLabel()) == 0);
        assertEquals(field.isValueOnly(), copy.isValueOnly());
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.config.font.FieldConfigFontPreview#attributeSelection(java.lang.String)}.
     */
    @Test
    public void testAttributeSelection() {
        boolean valueOnly = true;
        FieldConfigFontPreview field =
                new FieldConfigFontPreview(
                        new FieldConfigCommonData(
                                String.class, FieldIdEnum.NAME, "test label", valueOnly));

        field.attributeSelection("field");
        // Does nothing
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.config.font.FieldConfigFontPreview#undoAction(com.sldeditor.common.undo.UndoInterface)}.
     * Test method for {@link
     * com.sldeditor.ui.detail.config.font.FieldConfigFontPreview#redoAction(com.sldeditor.common.undo.UndoInterface)}.
     */
    @Test
    public void testUndoAction() {
        FieldConfigFontPreview field =
                new FieldConfigFontPreview(
                        new FieldConfigCommonData(
                                String.class, FieldIdEnum.NAME, "test label", false));
        field.undoAction(null);
        field.redoAction(null);

        // Does nothing
    }
}
