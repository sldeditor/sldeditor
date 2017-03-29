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

package com.sldeditor.test.unit.ui.detail.config.inlinefeature;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.geotools.styling.UserLayer;
import org.junit.Test;

import com.sldeditor.common.Controller;
import com.sldeditor.common.defaultsymbol.DefaultSymbols;
import com.sldeditor.common.undo.UndoEvent;
import com.sldeditor.common.undo.UndoManager;
import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.test.unit.datasource.impl.DummyInlineSLDFile;
import com.sldeditor.test.unit.datasource.impl.DummyInlineSLDFile2;
import com.sldeditor.ui.detail.config.FieldConfigBase;
import com.sldeditor.ui.detail.config.FieldConfigCommonData;
import com.sldeditor.ui.detail.config.FieldConfigPopulate;
import com.sldeditor.ui.detail.config.inlinefeature.FieldConfigInlineFeature;
import com.sldeditor.ui.detail.config.inlinefeature.InlineFeatureUtils;
import com.vividsolutions.jts.geom.Geometry;

/**
 * The unit test for FieldConfigInlineFeature.
 * 
 * <p>{@link com.sldeditor.ui.detail.config.inlinefeature.FieldConfigInlineFeature}
 *
 * @author Robert Ward (SCISYS)
 */
public class FieldConfigInlineFeatureTest {

    /**
     * Test method for
     * {@link com.sldeditor.ui.detail.config.inlinefeature.FieldConfigInlineFeature#internal_setEnabled(boolean)}.
     * Test method for
     * {@link com.sldeditor.ui.detail.config.inlinefeature.FieldConfigInlineFeature#isEnabled()}.
     */
    @Test
    public void testSetEnabled() {
        FieldConfigInlineFeature field = new FieldConfigInlineFeature(
                new FieldConfigCommonData(Geometry.class, FieldIdEnum.NAME, null, true));

        // Field will not have been created
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
    }

    /**
     * Test method for
     * {@link com.sldeditor.ui.detail.config.inlinefeature.FieldConfigInlineFeature#setVisible(boolean)}.
     */
    @Test
    public void testSetVisible() {
        FieldConfigInlineFeature field = new FieldConfigInlineFeature(
                new FieldConfigCommonData(Geometry.class, FieldIdEnum.NAME, null, true));

        boolean expectedValue = true;
        field.setVisible(expectedValue);
        field.createUI();
        field.setVisible(expectedValue);

        expectedValue = false;
        field.setVisible(expectedValue);
    }

    /**
     * Test method for
     * {@link com.sldeditor.ui.detail.config.inlinefeature.FieldConfigInlineFeature#generateExpression()}.
     * Test method for
     * {@link com.sldeditor.ui.detail.config.inlinefeature.FieldConfigInlineFeature#populateExpression(java.lang.Object)}.
     * Test method for
     * {@link com.sldeditor.ui.detail.config.inlinefeature.FieldConfigInlineFeature#populateField(java.lang.String)}.
     * Test method for
     * {@link com.sldeditor.ui.detail.config.inlinefeature.FieldConfigInlineFeature#setTestValue(com.sldeditor.ui.detail.config.FieldId, java.lang.String)}.
     * Test method for
     * {@link com.sldeditor.ui.detail.config.inlinefeature.FieldConfigInlineFeature#getStringValue()}.
     */
    @Test
    public void testGenerateExpression() {
        FieldConfigInlineFeature field = new FieldConfigInlineFeature(
                new FieldConfigCommonData(Geometry.class, FieldIdEnum.NAME, null, true));
        String testValue = null;
        field.populate(null);
        field.setTestValue(FieldIdEnum.UNKNOWN, testValue);
        field.populateField(testValue);

        field.createUI();

        UserLayer userLayer1 = DefaultSymbols.createNewUserLayer();
        field.populateField(userLayer1);
        String actualValue = field.getStringValue();
        assertTrue(actualValue.compareTo("") == 0);

        DummyInlineSLDFile testData1 = new DummyInlineSLDFile();

        UserLayer userLayer2 = (UserLayer) testData1.getSLD().layers().get(0);
        Controller.getInstance().setPopulating(true);
        field.populateField(userLayer2);
        String expectedValue2 = InlineFeatureUtils.getInlineFeaturesText(userLayer2);
        actualValue = field.getStringValue();
        assertTrue(actualValue.compareTo(expectedValue2) == 0);

        field.setTestValue(FieldIdEnum.UNKNOWN, expectedValue2);
        actualValue = field.getStringValue();
        // The fids are different
        assertTrue(expectedValue2.compareTo(actualValue) != 0);

        field.populateExpression((String) null);
        field.populateExpression((Integer) null);
        String expectedValue3 = "test inline value3";
        field.populateExpression(expectedValue3);
        Controller.getInstance().setPopulating(false);
    }

    /**
     * Test method for
     * {@link com.sldeditor.ui.detail.config.inlinefeature.FieldConfigInlineFeature#revertToDefaultValue()}.
     * Test method for
     * {@link com.sldeditor.ui.detail.config.inlinefeature.FieldConfigInlineFeature#setDefaultValue(java.lang.String)}.
     */
    @Test
    public void testRevertToDefaultValue() {
        FieldConfigInlineFeature field = new FieldConfigInlineFeature(
                new FieldConfigCommonData(Geometry.class, FieldIdEnum.NAME, null, true));

        field.revertToDefaultValue();
        assertNull(field.getStringValue());

        field.createUI();
        field.revertToDefaultValue();
        assertNotNull(field.getStringValue());
        assertTrue(field.getStringValue().isEmpty());

        String expectedValue = "test inline data";
        field.setDefaultValue(expectedValue);
        field.revertToDefaultValue();
        assertNotNull(field.getStringValue());
    }

    /**
     * Test method for
     * {@link com.sldeditor.ui.detail.config.inlinefeature.FieldConfigInlineFeature#createCopy(com.sldeditor.ui.detail.config.FieldConfigBase)}.
     */
    @Test
    public void testCreateCopy() {
        class TestFieldConfigInlineFeature extends FieldConfigInlineFeature {
            public TestFieldConfigInlineFeature(FieldConfigCommonData commonData) {
                super(commonData);
            }

            public FieldConfigPopulate callCreateCopy(FieldConfigBase fieldConfigBase) {
                return createCopy(fieldConfigBase);
            }
        }

        TestFieldConfigInlineFeature field = new TestFieldConfigInlineFeature(
                new FieldConfigCommonData(Geometry.class, FieldIdEnum.NAME, "", true));
        FieldConfigInlineFeature copy = (FieldConfigInlineFeature) field.callCreateCopy(null);
        assertNull(copy);

        copy = (FieldConfigInlineFeature) field.callCreateCopy(field);
        assertEquals(field.getFieldId(), copy.getFieldId());
        assertTrue(field.getLabel().compareTo(copy.getLabel()) == 0);
        assertEquals(field.isValueOnly(), copy.isValueOnly());
    }

    /**
     * Test method for
     * {@link com.sldeditor.ui.detail.config.inlinefeature.FieldConfigInlineFeature#attributeSelection(java.lang.String)}.
     */
    @Test
    public void testAttributeSelection() {
        FieldConfigInlineFeature field = new FieldConfigInlineFeature(
                new FieldConfigCommonData(Geometry.class, FieldIdEnum.NAME, null, true));
        field.attributeSelection(null);

        // Does nothing
    }

    /**
     * Test method for
     * {@link com.sldeditor.ui.detail.config.inlinefeature.FieldConfigInlineFeature#undoAction(com.sldeditor.common.undo.UndoInterface)}.
     * Test method for
     * {@link com.sldeditor.ui.detail.config.inlinefeature.FieldConfigInlineFeature#redoAction(com.sldeditor.common.undo.UndoInterface)}.
     */
    @Test
    public void testUndoAction() {
        FieldConfigInlineFeature field = new FieldConfigInlineFeature(
                new FieldConfigCommonData(Geometry.class, FieldIdEnum.NAME, null, true));
        field.undoAction(null);
        field.redoAction(null);
        field.createUI();

        DummyInlineSLDFile testData1 = new DummyInlineSLDFile();

        UserLayer userLayer1 = (UserLayer) testData1.getSLD().layers().get(0);
        field.populateField(userLayer1);
        String expectedValue1 = InlineFeatureUtils.getInlineFeaturesText(userLayer1);
        String actualValue = field.getStringValue();
        assertTrue(actualValue.compareTo(expectedValue1) == 0);

        DummyInlineSLDFile2 testData2 = new DummyInlineSLDFile2();

        UserLayer userLayer2 = (UserLayer) testData2.getSLD().layers().get(0);
        field.populateField(userLayer2);
        String expectedValue2 = InlineFeatureUtils.getInlineFeaturesText(userLayer2);
        actualValue = field.getStringValue();
        assertTrue(actualValue.compareTo(expectedValue2) == 0);
        UndoManager.getInstance().undo();
        actualValue = field.getStringValue();
        assertTrue(actualValue.compareTo(expectedValue1) == 0);
        UndoManager.getInstance().redo();
        actualValue = field.getStringValue();
        assertTrue(actualValue.compareTo(expectedValue2) == 0);

        // Increase the code coverage
        field.undoAction(null);
        field.undoAction(
                new UndoEvent(null, FieldIdEnum.NAME, Double.valueOf(42.0), Integer.valueOf(11)));
        field.redoAction(null);
        field.redoAction(
                new UndoEvent(null, FieldIdEnum.NAME, Double.valueOf(454.0), Integer.valueOf(69)));
    }

}
