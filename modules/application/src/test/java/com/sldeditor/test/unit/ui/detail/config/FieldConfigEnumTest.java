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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.sldeditor.common.Controller;
import com.sldeditor.common.undo.UndoEvent;
import com.sldeditor.common.undo.UndoManager;
import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.ui.detail.config.FieldConfigBase;
import com.sldeditor.ui.detail.config.FieldConfigEnum;
import com.sldeditor.ui.detail.config.FieldId;
import com.sldeditor.ui.detail.config.symboltype.SymbolTypeConfig;
import com.sldeditor.ui.widgets.ValueComboBoxData;

/**
 * The unit test for FieldConfigEnum.
 * <p>{@link com.sldeditor.ui.detail.config.FieldConfigEnum}
 *
 * @author Robert Ward (SCISYS)
 */
public class FieldConfigEnumTest {

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigEnum#setEnabled(boolean)}.
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigEnum#isEnabled()}.
     */
    @Test
    public void testSetEnabled() {
        // Value only, no attribute/expression dropdown
        boolean valueOnly = true;
        FieldConfigEnum field = new FieldConfigEnum(Integer.class, new FieldId(FieldIdEnum.NAME), "label", valueOnly);

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
        FieldConfigEnum field2 = new FieldConfigEnum(Integer.class, new FieldId(FieldIdEnum.NAME), "label", valueOnly);

        // Text field will not have been created
        expectedValue = true;
        field2.setEnabled(expectedValue);
        assertFalse(field2.isEnabled());

        // Create text field
        field2.createUI(null);

        assertEquals(expectedValue, field2.isEnabled());

        expectedValue = false;
        field2.setEnabled(expectedValue);

        // Actual value is coming from the attribute panel, not the text field
        assertEquals(!expectedValue, field2.isEnabled());
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigEnum#setVisible(boolean)}.
     */
    @Test
    public void testSetVisible() {
        boolean valueOnly = true;
        FieldConfigEnum field = new FieldConfigEnum(Integer.class, new FieldId(FieldIdEnum.NAME), "label", valueOnly);

        boolean expectedValue = true;
        field.setVisible(expectedValue);
        field.createUI(null);
        field.setVisible(expectedValue);

        expectedValue = false;
        field.setVisible(expectedValue);
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigEnum#generateExpression()}.
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigEnum#populateExpression(java.lang.Object, org.opengis.filter.expression.Expression)}.
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigEnum#populateField(java.lang.String)}.
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigEnum#setTestValue(com.sldeditor.ui.detail.config.FieldId, java.lang.String)}.
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigEnum#getEnumValue()}.
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigEnum#getStringValue()}.
     */
    @Test
    public void testGenerateExpression() {

        List<SymbolTypeConfig> configList = new ArrayList<SymbolTypeConfig>();
        SymbolTypeConfig s1 = new SymbolTypeConfig(null);
        s1.addOption("key1", "Value 1");
        s1.addOption("key2", "Value 2");
        s1.addOption("key3", "Value 3");
        s1.addField(new FieldId(FieldIdEnum.ANCHOR_POINT_H), true);
        s1.addField(new FieldId(FieldIdEnum.ANCHOR_POINT_V), false);
        configList.add(s1);

        SymbolTypeConfig s2 = new SymbolTypeConfig(null);
        s2.addOption("key4", "Value 4");
        s2.addOption("key5", "Value 5");
        s2.addOption("key6", "Value 6");
        s2.addField(new FieldId(FieldIdEnum.ANGLE), true);
        s2.addField(new FieldId(FieldIdEnum.DESCRIPTION), false);
        configList.add(s2);

        boolean valueOnly = true;
        FieldConfigEnum field = new FieldConfigEnum(Integer.class, new FieldId(FieldIdEnum.NAME), "label", valueOnly);
        field.undoAction(null);
        field.redoAction(null);
        field.addConfig(null);
        assertNull(field.getStringValue());

        field.addConfig(configList);

        // Try without creating the ui
        field.populateExpression(null);
        field.populateField((String)null);
        field.setTestValue(null, (String)null);

        // Now create the ui
        field.createUI(null);
        String expectedValue1 = "key2";
        field.populateField(expectedValue1);
        String actualValueString = field.getStringValue();
        assertTrue(expectedValue1.compareTo(actualValueString) == 0);
        ValueComboBoxData actualValue = field.getEnumValue();
        assertTrue(expectedValue1.compareTo(actualValue.getKey()) == 0);
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigEnum#revertToDefaultValue()}.
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigEnum#addConfig(java.util.List)}.
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigEnum#setDefaultValue(java.lang.String)}.
     */
    @Test
    public void testRevertToDefaultValue() {
        boolean valueOnly = true;
        FieldConfigEnum field = new FieldConfigEnum(Integer.class, new FieldId(FieldIdEnum.NAME), "label", valueOnly);

        String expectedDefaultValue = "key6";
        field.revertToDefaultValue();
        assertEquals(0, field.getIntValue());

        List<SymbolTypeConfig> configList = new ArrayList<SymbolTypeConfig>();
        SymbolTypeConfig s1 = new SymbolTypeConfig(null);
        s1.addOption("key1", "Value 1");
        s1.addOption("key2", "Value 2");
        s1.addOption("key3", "Value 3");
        s1.addField(new FieldId(FieldIdEnum.ANCHOR_POINT_H), true);
        s1.addField(new FieldId(FieldIdEnum.ANCHOR_POINT_V), false);
        configList.add(s1);

        SymbolTypeConfig s2 = new SymbolTypeConfig(null);
        s2.addOption("key4", "Value 4");
        s2.addOption("key5", "Value 5");
        s2.addOption("key6", "Value 6");
        s2.addField(new FieldId(FieldIdEnum.ANGLE), true);
        s2.addField(new FieldId(FieldIdEnum.DESCRIPTION), false);
        configList.add(s2);

        field.addConfig(configList);
        field.createUI(null);
        field.setDefaultValue(expectedDefaultValue);
        field.revertToDefaultValue();
        assertTrue(expectedDefaultValue.compareTo(field.getStringValue()) == 0);
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigEnum#getClassType()}.
     */
    @Test
    public void testGetClassType() {
        boolean valueOnly = true;
        FieldConfigEnum field = new FieldConfigEnum(Integer.class, new FieldId(FieldIdEnum.NAME), "label", valueOnly);

        assertEquals(String.class, field.getClassType());
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigEnum#createCopy(com.sldeditor.ui.detail.config.FieldConfigBase)}.
     */
    @Test
    public void testCreateCopy() {
        boolean valueOnly = true;

        class TestFieldConfigEnum extends FieldConfigEnum
        {
            public TestFieldConfigEnum(Class<?> panelId, FieldId id, String label, boolean valueOnly) {
                super(panelId, id, label, valueOnly);
            }

            public FieldConfigBase callCreateCopy(FieldConfigBase fieldConfigBase)
            {
                return createCopy(fieldConfigBase);
            }
        }

        TestFieldConfigEnum field = new TestFieldConfigEnum(Integer.class, new FieldId(FieldIdEnum.NAME), "label", valueOnly);
        FieldConfigEnum copy = (FieldConfigEnum) field.callCreateCopy(null);
        assertNull(copy);

        copy = (FieldConfigEnum) field.callCreateCopy(field);
        assertEquals(field.getFieldId().getFieldId(), copy.getFieldId().getFieldId());
        assertTrue(field.getLabel().compareTo(copy.getLabel()) == 0);
        assertEquals(field.isValueOnly(), copy.isValueOnly());
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigEnum#attributeSelection(java.lang.String)}.
     */
    @Test
    public void testAttributeSelection() {
        boolean valueOnly = true;
        FieldConfigEnum field = new FieldConfigEnum(Integer.class, new FieldId(FieldIdEnum.NAME), "label", valueOnly);
        field.attributeSelection(null);

        field.createUI(null);
        assertTrue(field.isEnabled());
        field.attributeSelection("test");
        assertFalse(field.isEnabled());
        field.attributeSelection(null);
        assertTrue(field.isEnabled());
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigEnum#getFieldEnableState()}.
     */
    @Test
    public void testGetFieldEnableState() {
        UndoManager.getInstance().setPopulationCheck(Controller.getInstance());
        List<SymbolTypeConfig> configList = new ArrayList<SymbolTypeConfig>();
        SymbolTypeConfig s1 = new SymbolTypeConfig(Integer.class);
        s1.addOption("key1", "Value 1");
        s1.addOption("key2", "Value 2");
        s1.addOption("key3", "Value 3");
        s1.addField(new FieldId(FieldIdEnum.ANCHOR_POINT_H), true);
        s1.addField(new FieldId(FieldIdEnum.ANCHOR_POINT_V), false);
        configList.add(s1);

        boolean valueOnly = true;
        FieldConfigEnum field = new FieldConfigEnum(Integer.class, new FieldId(FieldIdEnum.NAME), "label", valueOnly);

        field.addConfig(configList);

        // Now create the ui
        field.createUI(null);

        field.populateField("key3");
        Map<FieldId, Boolean> actualMap = field.getFieldEnableState();

        assertEquals(actualMap.get(new FieldId(FieldIdEnum.ANCHOR_POINT_H)), Boolean.TRUE);
        assertEquals(actualMap.get(new FieldId(FieldIdEnum.ANCHOR_POINT_V)), Boolean.FALSE);
        UndoManager.getInstance().setPopulationCheck(null);
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigEnum#undoAction(com.sldeditor.common.undo.UndoInterface)}.
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigEnum#redoAction(com.sldeditor.common.undo.UndoInterface)}.
     */
    @Test
    public void testUndoAction() {
        UndoManager.getInstance().setPopulationCheck(Controller.getInstance());
        List<SymbolTypeConfig> configList = new ArrayList<SymbolTypeConfig>();
        SymbolTypeConfig s1 = new SymbolTypeConfig(null);
        s1.addOption("key1", "Value 1");
        s1.addOption("key2", "Value 2");
        s1.addOption("key3", "Value 3");
        s1.addField(new FieldId(FieldIdEnum.ANCHOR_POINT_H), true);
        s1.addField(new FieldId(FieldIdEnum.ANCHOR_POINT_V), false);
        configList.add(s1);

        SymbolTypeConfig s2 = new SymbolTypeConfig(null);
        s2.addOption("key4", "Value 4");
        s2.addOption("key5", "Value 5");
        s2.addOption("key6", "Value 6");
        s2.addField(new FieldId(FieldIdEnum.ANGLE), true);
        s2.addField(new FieldId(FieldIdEnum.DESCRIPTION), false);
        configList.add(s2);

        boolean valueOnly = true;
        FieldConfigEnum field = new FieldConfigEnum(Integer.class, new FieldId(FieldIdEnum.NAME), "label", valueOnly);
        field.addConfig(null);
        assertNull(field.getStringValue());

        field.addConfig(configList);

        // Now create the ui
        field.createUI(null);
        String expectedValue1 = "key2";
        field.populateField(expectedValue1);

        String expectedValue2 = "key6";
        field.populateField(expectedValue2);

        UndoManager.getInstance().undo();
        String actualValueString = field.getStringValue();
        assertTrue(expectedValue1.compareTo(actualValueString) == 0);
        ValueComboBoxData actualValue = field.getEnumValue();
        assertTrue(expectedValue1.compareTo(actualValue.getKey()) == 0);

        UndoManager.getInstance().redo();
        actualValueString = field.getStringValue();
        assertTrue(expectedValue2.compareTo(actualValueString) == 0);
        actualValue = field.getEnumValue();
        assertTrue(expectedValue2.compareTo(actualValue.getKey()) == 0);

        // Increase the code coverage
        field.undoAction(null);
        field.undoAction(new UndoEvent(null, new FieldId(FieldIdEnum.NAME), Double.valueOf(0),  Double.valueOf(23)));
        field.redoAction(null);
        field.redoAction(new UndoEvent(null, new FieldId(FieldIdEnum.NAME),  Double.valueOf(0),  Double.valueOf(54)));
        UndoManager.getInstance().setPopulationCheck(null);
    }

}
