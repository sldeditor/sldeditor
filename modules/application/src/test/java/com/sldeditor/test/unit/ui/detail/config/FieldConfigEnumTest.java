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

import com.sldeditor.common.Controller;
import com.sldeditor.common.undo.UndoEvent;
import com.sldeditor.common.undo.UndoManager;
import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.ui.detail.config.FieldConfigBase;
import com.sldeditor.ui.detail.config.FieldConfigCommonData;
import com.sldeditor.ui.detail.config.FieldConfigEnum;
import com.sldeditor.ui.detail.config.FieldConfigPopulate;
import com.sldeditor.ui.detail.config.symboltype.SymbolTypeConfig;
import com.sldeditor.ui.iface.UpdateSymbolInterface;
import com.sldeditor.ui.widgets.ValueComboBoxData;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.geotools.factory.CommonFactoryFinder;
import org.junit.jupiter.api.Test;
import org.opengis.filter.FilterFactory;

/**
 * The unit test for FieldConfigEnum.
 *
 * <p>{@link com.sldeditor.ui.detail.config.FieldConfigEnum}
 *
 * @author Robert Ward (SCISYS)
 */
public class FieldConfigEnumTest {

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.config.FieldConfigEnum#internalSetEnabled(boolean)}. Test method for
     * {@link com.sldeditor.ui.detail.config.FieldConfigEnum#isEnabled()}.
     */
    @Test
    public void testSetEnabled() {
        // Value only, no attribute/expression dropdown
        boolean valueOnly = true;
        FieldConfigEnum field =
                new FieldConfigEnum(
                        new FieldConfigCommonData(
                                Integer.class, FieldIdEnum.NAME, "label", valueOnly, false));

        // Text field will not have been created
        boolean expectedValue = true;
        field.internalSetEnabled(expectedValue);

        assertFalse(field.isEnabled());

        // Create text field
        field.createUI();
        assertEquals(expectedValue, field.isEnabled());

        expectedValue = false;
        field.internalSetEnabled(expectedValue);

        assertEquals(expectedValue, field.isEnabled());

        // Has attribute/expression dropdown
        valueOnly = false;
        FieldConfigEnum field2 =
                new FieldConfigEnum(
                        new FieldConfigCommonData(
                                Integer.class, FieldIdEnum.NAME, "label", valueOnly, false));

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
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigEnum#setVisible(boolean)}.
     */
    @Test
    public void testSetVisible() {
        boolean valueOnly = true;
        FieldConfigEnum field =
                new FieldConfigEnum(
                        new FieldConfigCommonData(
                                Integer.class, FieldIdEnum.NAME, "label", valueOnly, false));

        boolean expectedValue = true;
        field.setVisible(expectedValue);
        field.createUI();
        field.setVisible(expectedValue);

        expectedValue = false;
        field.setVisible(expectedValue);
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigEnum#generateExpression()}.
     * Test method for {@link
     * com.sldeditor.ui.detail.config.FieldConfigEnum#populateExpression(java.lang.Object,
     * org.opengis.filter.expression.Expression)}. Test method for {@link
     * com.sldeditor.ui.detail.config.FieldConfigEnum#populateField(java.lang.String)}. Test method
     * for {@link
     * com.sldeditor.ui.detail.config.FieldConfigEnum#setTestValue(com.sldeditor.ui.detail.config.FieldId,
     * java.lang.String)}. Test method for {@link
     * com.sldeditor.ui.detail.config.FieldConfigEnum#getEnumValue()}. Test method for {@link
     * com.sldeditor.ui.detail.config.FieldConfigEnum#getStringValue()}.
     */
    @Test
    public void testGenerateExpression() {

        SymbolTypeConfig s1 = new SymbolTypeConfig(null);
        s1.addOption("key1", "Value 1");
        s1.addOption("key2", "Value 2");
        s1.addOption("key3", "Value 3");
        s1.addField(FieldIdEnum.ANCHOR_POINT_H, true);
        s1.addField(FieldIdEnum.ANCHOR_POINT_V, false);
        List<SymbolTypeConfig> configList = new ArrayList<SymbolTypeConfig>();
        configList.add(s1);

        SymbolTypeConfig s2 = new SymbolTypeConfig(null);
        s2.addOption("key4", "Value 4");
        s2.addOption("key5", "Value 5");
        s2.addOption("key6", "Value 6");
        s2.addField(FieldIdEnum.ANGLE, true);
        s2.addField(FieldIdEnum.DESCRIPTION, false);
        configList.add(s2);

        boolean valueOnly = true;
        FieldConfigEnum field =
                new FieldConfigEnum(
                        new FieldConfigCommonData(
                                Integer.class, FieldIdEnum.NAME, "label", valueOnly, false));
        field.undoAction(null);
        field.redoAction(null);
        field.addConfig(null);
        assertNull(field.getStringValue());

        field.addConfig(configList);

        // Try without creating the ui
        field.populateExpression(null);
        field.populateField((String) null);
        field.setTestValue(null, (String) null);

        // Now create the ui
        field.createUI();
        field.createUI();
        String expectedValue1 = "key2";
        field.populateField(expectedValue1);
        String actualValueString = field.getStringValue();
        assertTrue(expectedValue1.compareTo(actualValueString) == 0);
        ValueComboBoxData actualValue = field.getEnumValue();
        assertTrue(expectedValue1.compareTo(actualValue.getKey()) == 0);

        String expectedValue2 = "key6";
        FilterFactory ff = CommonFactoryFinder.getFilterFactory();
        field.populateExpression(ff.literal(expectedValue2));
        String actualValueString2 = field.getStringValue();
        assertTrue(expectedValue2.compareTo(actualValueString2) == 0);

        // Try valid/invalid option values
        field.populateExpression(Double.valueOf(1.3));

        assertTrue(field.isValidOption("key1"));
        assertTrue(field.isValidOption("key4"));
        assertFalse(field.isValidOption("key24"));
        assertFalse(field.isValidOption(null));
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.config.FieldConfigEnum#revertToDefaultValue()}. Test method for
     * {@link com.sldeditor.ui.detail.config.FieldConfigEnum#addConfig(java.util.List)}. Test method
     * for {@link com.sldeditor.ui.detail.config.FieldConfigEnum#setDefaultValue(java.lang.String)}.
     */
    @Test
    public void testRevertToDefaultValue() {
        boolean valueOnly = true;
        FieldConfigEnum field =
                new FieldConfigEnum(
                        new FieldConfigCommonData(
                                Integer.class, FieldIdEnum.NAME, "label", valueOnly, false));

        field.revertToDefaultValue();
        assertEquals(0, field.getIntValue());

        SymbolTypeConfig s1 = new SymbolTypeConfig(null);
        s1.addOption("key1", "Value 1");
        s1.addOption("key2", "Value 2");
        s1.addOption("key3", "Value 3");
        s1.addField(FieldIdEnum.ANCHOR_POINT_H, true);
        s1.addField(FieldIdEnum.ANCHOR_POINT_V, false);
        List<SymbolTypeConfig> configList = new ArrayList<SymbolTypeConfig>();
        configList.add(s1);

        SymbolTypeConfig s2 = new SymbolTypeConfig(null);
        s2.addOption("key4", "Value 4");
        s2.addOption("key5", "Value 5");
        s2.addOption("key6", "Value 6");
        s2.addField(FieldIdEnum.ANGLE, true);
        s2.addField(FieldIdEnum.DESCRIPTION, false);
        configList.add(s2);

        field.addConfig(configList);
        field.createUI();
        String expectedDefaultValue = "key6";
        field.setDefaultValue(expectedDefaultValue);
        field.revertToDefaultValue();
        assertTrue(expectedDefaultValue.compareTo(field.getStringValue()) == 0);
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.config.FieldConfigEnum#createCopy(com.sldeditor.ui.detail.config.FieldConfigBase)}.
     */
    @Test
    public void testCreateCopy() {
        boolean valueOnly = true;

        class TestFieldConfigEnum extends FieldConfigEnum {
            public TestFieldConfigEnum(FieldConfigCommonData commonData) {
                super(commonData);
            }

            public FieldConfigPopulate callCreateCopy(FieldConfigBase fieldConfigBase) {
                return createCopy(fieldConfigBase);
            }
        }

        TestFieldConfigEnum field =
                new TestFieldConfigEnum(
                        new FieldConfigCommonData(
                                Integer.class, FieldIdEnum.NAME, "label", valueOnly, false));
        FieldConfigEnum copy = (FieldConfigEnum) field.callCreateCopy(null);
        assertNull(copy);

        copy = (FieldConfigEnum) field.callCreateCopy(field);
        assertEquals(field.getFieldId(), copy.getFieldId());
        assertTrue(field.getLabel().compareTo(copy.getLabel()) == 0);
        assertEquals(field.isValueOnly(), copy.isValueOnly());
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.config.FieldConfigEnum#attributeSelection(java.lang.String)}.
     */
    @Test
    public void testAttributeSelection() {
        boolean valueOnly = true;
        FieldConfigEnum field =
                new FieldConfigEnum(
                        new FieldConfigCommonData(
                                Integer.class, FieldIdEnum.NAME, "label", valueOnly, false));
        field.attributeSelection(null);

        field.createUI();
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
        SymbolTypeConfig s1 = new SymbolTypeConfig(Integer.class);
        s1.addOption("key1", "Value 1");
        s1.addOption("key2", "Value 2");
        s1.addOption("key3", "Value 3");
        s1.addField(FieldIdEnum.ANCHOR_POINT_H, true);
        s1.addField(FieldIdEnum.ANCHOR_POINT_V, false);
        List<SymbolTypeConfig> configList = new ArrayList<SymbolTypeConfig>();
        configList.add(s1);

        boolean valueOnly = true;
        FieldConfigEnum field =
                new FieldConfigEnum(
                        new FieldConfigCommonData(
                                Integer.class, FieldIdEnum.NAME, "label", valueOnly, false));

        field.addConfig(configList);

        // Now create the ui
        field.createUI();

        field.populateField("key3");
        Map<FieldIdEnum, Boolean> actualMap = field.getFieldEnableState();

        assertEquals(actualMap.get(FieldIdEnum.ANCHOR_POINT_H), Boolean.TRUE);
        assertEquals(actualMap.get(FieldIdEnum.ANCHOR_POINT_V), Boolean.FALSE);
        UndoManager.getInstance().setPopulationCheck(null);
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.config.FieldConfigEnum#undoAction(com.sldeditor.common.undo.UndoInterface)}.
     * Test method for {@link
     * com.sldeditor.ui.detail.config.FieldConfigEnum#redoAction(com.sldeditor.common.undo.UndoInterface)}.
     */
    @Test
    public void testUndoAction() {
        SymbolTypeConfig s1 = new SymbolTypeConfig(null);
        s1.addOption("key1", "Value 1");
        s1.addOption("key2", "Value 2");
        s1.addOption("key3", "Value 3");
        s1.addField(FieldIdEnum.ANCHOR_POINT_H, true);
        s1.addField(FieldIdEnum.ANCHOR_POINT_V, false);
        List<SymbolTypeConfig> configList = new ArrayList<SymbolTypeConfig>();
        configList.add(s1);

        SymbolTypeConfig s2 = new SymbolTypeConfig(null);
        s2.addOption("key4", "Value 4");
        s2.addOption("key5", "Value 5");
        s2.addOption("key6", "Value 6");
        s2.addField(FieldIdEnum.ANGLE, true);
        s2.addField(FieldIdEnum.DESCRIPTION, false);
        configList.add(s2);
        boolean valueOnly = true;
        FieldConfigEnum field =
                new FieldConfigEnum(
                        new FieldConfigCommonData(
                                Integer.class, FieldIdEnum.NAME, "label", valueOnly, false));
        field.addConfig(null);
        assertNull(field.getStringValue());

        field.addConfig(configList);

        // Now create the ui
        field.createUI();
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
        field.undoAction(
                new UndoEvent(null, FieldIdEnum.NAME, Double.valueOf(0), Double.valueOf(23)));
        field.redoAction(null);
        field.redoAction(
                new UndoEvent(null, FieldIdEnum.NAME, Double.valueOf(0), Double.valueOf(54)));
    }

    @Test
    public void testValueStored() {
        boolean valueOnly = true;

        SymbolTypeConfig s1 = new SymbolTypeConfig(null);
        s1.addOption("key1", "Value 1");
        s1.addOption("key2", "Value 2");
        s1.addOption("key3", "Value 3");
        s1.addField(FieldIdEnum.ANCHOR_POINT_H, true);
        s1.addField(FieldIdEnum.ANCHOR_POINT_V, false);
        List<SymbolTypeConfig> configList = new ArrayList<SymbolTypeConfig>();
        configList.add(s1);

        SymbolTypeConfig s2 = new SymbolTypeConfig(null);
        s2.addOption("key4", "Value 4");
        s2.addOption("key5", "Value 5");
        s2.addOption("key6", "Value 6");
        s2.addField(FieldIdEnum.ANGLE, true);
        s2.addField(FieldIdEnum.DESCRIPTION, false);
        configList.add(s2);

        class TestFieldConfigEnum extends FieldConfigEnum {
            public TestFieldConfigEnum(FieldConfigCommonData commonData) {
                super(commonData);
            }
        }

        TestFieldConfigEnum field =
                new TestFieldConfigEnum(
                        new FieldConfigCommonData(
                                String.class, FieldIdEnum.NAME, "label", valueOnly, false));

        class TestUpdateSymbol implements UpdateSymbolInterface {
            public boolean dataChanged = false;

            @Override
            public void dataChanged(FieldIdEnum changedField) {
                dataChanged = true;
            }
        };
        TestUpdateSymbol update = new TestUpdateSymbol();

        int undoListSize = UndoManager.getInstance().getUndoListSize();
        field.addConfig(configList);
        field.createUI();
        field.addDataChangedListener(update);
        assertFalse(update.dataChanged);
        field.populateField("key2");
        assertTrue(update.dataChanged);

        assertEquals(undoListSize + 1, UndoManager.getInstance().getUndoListSize());
        update.dataChanged = false;

        // now suppress undo events
        field =
                new TestFieldConfigEnum(
                        new FieldConfigCommonData(
                                String.class, FieldIdEnum.NAME, "label", valueOnly, true));

        undoListSize = UndoManager.getInstance().getUndoListSize();
        field.addConfig(configList);
        field.createUI();
        field.addDataChangedListener(update);
        assertFalse(update.dataChanged);
        field.populateField("key3");
        assertTrue(update.dataChanged);

        assertEquals(undoListSize, UndoManager.getInstance().getUndoListSize());
    }
}
