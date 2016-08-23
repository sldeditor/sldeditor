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

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.geotools.filter.LiteralExpressionImpl;
import org.geotools.styling.ColorMap;
import org.geotools.styling.ColorMapEntry;
import org.geotools.styling.ColorMapEntryImpl;
import org.geotools.styling.ColorMapImpl;
import org.geotools.styling.FeatureTypeConstraint;
import org.geotools.styling.Font;
import org.geotools.styling.StyleBuilder;
import org.geotools.styling.UserLayer;
import org.junit.Test;
import org.opengis.filter.expression.Expression;

import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.ui.detail.GraphicPanelFieldManager;
import com.sldeditor.ui.detail.config.FieldConfigBoolean;
import com.sldeditor.ui.detail.config.FieldConfigColour;
import com.sldeditor.ui.detail.config.FieldConfigDouble;
import com.sldeditor.ui.detail.config.FieldConfigEnum;
import com.sldeditor.ui.detail.config.FieldConfigInteger;
import com.sldeditor.ui.detail.config.FieldConfigPopulation;
import com.sldeditor.ui.detail.config.FieldConfigString;
import com.sldeditor.ui.detail.config.FieldId;
import com.sldeditor.ui.detail.config.colourmap.FieldConfigColourMap;
import com.sldeditor.ui.detail.config.font.FieldConfigFont;
import com.sldeditor.ui.detail.config.symboltype.SymbolTypeConfig;
import com.vividsolutions.jts.geom.Geometry;

/**
 * The unit test for FieldConfigPopulation.
 * <p>{@link com.sldeditor.ui.detail.config.FieldConfigPopulation}
 *
 * @author Robert Ward (SCISYS)
 */
public class FieldConfigPopulationTest {

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigPopulation#FieldConfigPopulation(com.sldeditor.ui.detail.GraphicPanelFieldManager)}.
     */
    @Test
    public void testFieldConfigPopulation() {
        FieldId fieldId = new FieldId();
        FieldConfigPopulation obj = new FieldConfigPopulation(null);
        obj.populateBooleanField(fieldId, Boolean.TRUE);
        obj.populateComboBoxField(fieldId, "");
        obj.populateColourField(fieldId, null);
        obj.populateColourMapField(FieldIdEnum.ANCHOR_POINT_V, (ColorMap)null);
        obj.populateFontField(FieldIdEnum.ANCHOR_POINT_V, (Font)null);
        obj.populateTextField(fieldId, (String)null);
        obj.populateDoubleField(fieldId, (Double)null);
        obj.populateIntegerField(fieldId, (Integer)null);
        obj.populateField(fieldId, (Expression)null);
        obj.populateUserLayer(fieldId, (UserLayer)null);
        obj.populateFieldTypeConstraint(fieldId.getFieldId(), (List<FeatureTypeConstraint>)null);
        
        assertNull(obj.getExpression(fieldId));
        assertFalse(obj.getBoolean(fieldId));
        assertEquals(0, obj.getInteger(fieldId));
        assertTrue(Math.abs(obj.getDouble(fieldId) - 0.0) < 0.001);
        assertTrue(obj.getText(fieldId).compareTo("") == 0);
        assertNull(obj.getComboBox(fieldId));
        assertNull(obj.getColourMap(fieldId));
        assertNull(obj.getFieldConfig(fieldId));
        assertNull(obj.getFeatureTypeConstraint(fieldId));
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigPopulation#populateBooleanField(com.sldeditor.ui.detail.config.FieldId, java.lang.Boolean)}.
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigPopulation#populateBooleanField(com.sldeditor.common.xml.ui.FieldIdEnum, java.lang.Boolean)}.
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigPopulation#getBoolean(com.sldeditor.ui.detail.config.FieldId)}.
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigPopulation#getBoolean(com.sldeditor.common.xml.ui.FieldIdEnum)}.
     */
    @Test
    public void testBoolean() {
        FieldIdEnum wrongFieldEnum = FieldIdEnum.ELSE_FILTER;

        FieldIdEnum fieldEnum = FieldIdEnum.DESCRIPTION;
        FieldId fieldId = new FieldId(fieldEnum);

        GraphicPanelFieldManager fieldConfigManager = new GraphicPanelFieldManager(String.class);

        FieldConfigBoolean booleanField = new FieldConfigBoolean(Geometry.class, fieldId, "label", true);
        booleanField.createUI();
        fieldConfigManager.add(fieldId, booleanField);

        FieldConfigPopulation obj = new FieldConfigPopulation(fieldConfigManager);

        Boolean expectedValue = Boolean.TRUE;
        obj.populateBooleanField(fieldId, expectedValue);
        assertEquals(expectedValue.booleanValue(), obj.getBoolean(fieldId));
        assertEquals(expectedValue.booleanValue(), obj.getBoolean(fieldEnum));

        // This shouldn't work as it does not know about the field
        assertFalse(obj.getBoolean(wrongFieldEnum));

        // Try with null - should revert to default value (false)
        obj.populateBooleanField(fieldId, null);
        assertFalse(obj.getBoolean(fieldId));
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigPopulation#populateComboBoxField(com.sldeditor.ui.detail.config.FieldId, java.lang.String)}.
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigPopulation#populateComboBoxField(com.sldeditor.common.xml.ui.FieldIdEnum, java.lang.String)}.
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigPopulation#getComboBox(com.sldeditor.common.xml.ui.FieldIdEnum)}.
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigPopulation#getComboBox(com.sldeditor.ui.detail.config.FieldId)}.
     */
    @Test
    public void testComboBox() {
        FieldIdEnum wrongFieldEnum = FieldIdEnum.ELSE_FILTER;

        FieldIdEnum fieldEnum = FieldIdEnum.DESCRIPTION;
        FieldId fieldId = new FieldId(fieldEnum);

        GraphicPanelFieldManager fieldConfigManager = new GraphicPanelFieldManager(String.class);

        List<SymbolTypeConfig> configList = new ArrayList<SymbolTypeConfig>();
        SymbolTypeConfig s1 = new SymbolTypeConfig(null);
        s1.addOption("key1", "Value 1");
        s1.addOption("key2", "Value 2");
        s1.addOption("key3", "Value 3");
        s1.addField(new FieldId(FieldIdEnum.ANCHOR_POINT_H), true);
        s1.addField(new FieldId(FieldIdEnum.ANCHOR_POINT_V), false);
        configList.add(s1);

        FieldConfigEnum enumField = new FieldConfigEnum(Geometry.class, fieldId, "label", true);
        enumField.addConfig(configList);
        enumField.createUI();
        fieldConfigManager.add(fieldId, enumField);

        FieldConfigPopulation obj = new FieldConfigPopulation(fieldConfigManager);

        String expectedValue = "key2";
        obj.populateComboBoxField(fieldId, expectedValue);
        assertTrue(expectedValue.compareTo(obj.getComboBox(fieldId).getKey()) == 0);
        assertTrue(expectedValue.compareTo(obj.getComboBox(fieldEnum).getKey()) == 0);

        // This shouldn't work as it does not know about the field
        assertNull(obj.getComboBox(wrongFieldEnum));

        // Try with null - should revert to default value (first enum item)
        obj.populateComboBoxField(fieldId, null);
        expectedValue = "key1";
        assertTrue(expectedValue.compareTo(obj.getComboBox(fieldId).getKey()) == 0);
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigPopulation#populateColourField(com.sldeditor.ui.detail.config.FieldId, org.opengis.filter.expression.Expression, org.opengis.filter.expression.Expression)}.
     */
    @Test
    public void testColour() {
        FieldIdEnum wrongFieldEnum = FieldIdEnum.ELSE_FILTER;

        FieldIdEnum fieldEnum = FieldIdEnum.DESCRIPTION;
        FieldId fieldId = new FieldId(fieldEnum);

        GraphicPanelFieldManager fieldConfigManager = new GraphicPanelFieldManager(String.class);

        FieldConfigColour colourField = new FieldConfigColour(Geometry.class, fieldId, "label", true);
        colourField.createUI();
        fieldConfigManager.add(fieldId, colourField);

        FieldConfigPopulation obj = new FieldConfigPopulation(fieldConfigManager);

        StyleBuilder styleBuilder = new StyleBuilder();
        Expression colour = styleBuilder.colorExpression(Color.red);

        obj.populateColourField(fieldId, colour);
        obj.populateColourField(new FieldId(wrongFieldEnum), colour);
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigPopulation#populateColourMapField(com.sldeditor.common.xml.ui.FieldIdEnum, org.geotools.styling.ColorMap)}.
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigPopulation#getColourMap(com.sldeditor.common.xml.ui.FieldIdEnum)}.
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigPopulation#getColourMap(com.sldeditor.ui.detail.config.FieldId)}.
     */
    @Test
    public void testColourMap() {
        FieldIdEnum wrongFieldEnum = FieldIdEnum.ELSE_FILTER;

        FieldIdEnum fieldEnum = FieldIdEnum.DESCRIPTION;
        FieldId fieldId = new FieldId(fieldEnum);

        GraphicPanelFieldManager fieldConfigManager = new GraphicPanelFieldManager(String.class);

        FieldConfigColourMap colourMapField = new FieldConfigColourMap(Geometry.class, fieldId, "label");
        colourMapField.createUI();
        fieldConfigManager.add(fieldId, colourMapField);

        FieldConfigPopulation obj = new FieldConfigPopulation(fieldConfigManager);

        ColorMap expectedValue = new ColorMapImpl();
        ColorMapEntry entry = new ColorMapEntryImpl();
        StyleBuilder styleBuilder = new StyleBuilder();

        entry.setColor(styleBuilder.colorExpression(Color.PINK));
        entry.setQuantity(styleBuilder.literalExpression(2.3));
        expectedValue.addColorMapEntry(entry);
        obj.populateColourMapField(fieldEnum, expectedValue);
        assertEquals(expectedValue.getColorMapEntries().length, obj.getColourMap(fieldId).getColorMapEntries().length);
        assertEquals(expectedValue.getColorMapEntries().length, obj.getColourMap(fieldEnum).getColorMapEntries().length);

        // This shouldn't work as it does not know about the field
        obj.populateColourMapField(wrongFieldEnum, expectedValue);
        assertNull(obj.getColourMap(wrongFieldEnum));
    }
    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigPopulation#populateFontField(com.sldeditor.common.xml.ui.FieldIdEnum, org.geotools.styling.Font)}.
     */
    @Test
    public void testFont() {
        FieldIdEnum wrongFieldEnum = FieldIdEnum.ELSE_FILTER;

        FieldIdEnum fieldEnum = FieldIdEnum.DESCRIPTION;
        FieldId fieldId = new FieldId(fieldEnum);

        GraphicPanelFieldManager fieldConfigManager = new GraphicPanelFieldManager(String.class);

        FieldConfigFont fontField = new FieldConfigFont(Geometry.class, fieldId, "label", true);
        fontField.createUI();
        fieldConfigManager.add(fieldId, fontField);

        FieldConfigPopulation obj = new FieldConfigPopulation(fieldConfigManager);

        StyleBuilder styleBuilder = new StyleBuilder();

        Font expectedValue = styleBuilder.createFont(java.awt.Font.decode(null));
        obj.populateFontField(fieldEnum, expectedValue);

        // This shouldn't work as it does not know about the field
        obj.populateFontField(wrongFieldEnum, expectedValue);
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigPopulation#populateTextField(com.sldeditor.ui.detail.config.FieldId, java.lang.String)}.
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigPopulation#populateTextField(com.sldeditor.common.xml.ui.FieldIdEnum, java.lang.String)}.
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigPopulation#getText(com.sldeditor.ui.detail.config.FieldId)}.
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigPopulation#getText(com.sldeditor.common.xml.ui.FieldIdEnum)}.
     */
    @Test
    public void testString() {
        FieldIdEnum wrongFieldEnum = FieldIdEnum.ELSE_FILTER;

        FieldIdEnum fieldEnum = FieldIdEnum.DESCRIPTION;
        FieldId fieldId = new FieldId(fieldEnum);

        GraphicPanelFieldManager fieldConfigManager = new GraphicPanelFieldManager(String.class);

        FieldConfigString stringField = new FieldConfigString(Geometry.class, fieldId, "label", true, "button text");
        stringField.createUI();
        fieldConfigManager.add(fieldId, stringField);

        FieldConfigPopulation obj = new FieldConfigPopulation(fieldConfigManager);

        String expectedValue = "test string";
        obj.populateTextField(fieldId, expectedValue);
        assertTrue(expectedValue.compareTo(obj.getText(fieldId)) == 0);
        assertTrue(expectedValue.compareTo(obj.getText(fieldEnum)) == 0);

        // This shouldn't work as it does not know about the field
        assertTrue("".compareTo(obj.getText(wrongFieldEnum)) == 0);

        // Try with null
        obj.populateTextField(fieldId, null);
        obj.populateTextField(wrongFieldEnum, expectedValue);
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigPopulation#populateDoubleField(com.sldeditor.ui.detail.config.FieldId, java.lang.Double)}.
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigPopulation#getDouble(com.sldeditor.ui.detail.config.FieldId)}.
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigPopulation#getDouble(com.sldeditor.common.xml.ui.FieldIdEnum)}.
     */
    @Test
    public void testDouble() {
        FieldIdEnum wrongFieldEnum = FieldIdEnum.ELSE_FILTER;

        FieldIdEnum fieldEnum = FieldIdEnum.DESCRIPTION;
        FieldId fieldId = new FieldId(fieldEnum);

        GraphicPanelFieldManager fieldConfigManager = new GraphicPanelFieldManager(String.class);

        FieldConfigDouble doubleField = new FieldConfigDouble(Geometry.class, fieldId, "label", true);
        doubleField.createUI();
        fieldConfigManager.add(fieldId, doubleField);

        FieldConfigPopulation obj = new FieldConfigPopulation(fieldConfigManager);

        double expectedValue = 1.256;
        obj.populateDoubleField(fieldId, expectedValue);
        assertTrue(Math.abs(expectedValue - obj.getDouble(fieldId)) < 0.001);
        assertTrue(Math.abs(expectedValue - obj.getDouble(fieldEnum)) < 0.001);

        // This shouldn't work as it does not know about the field
        assertTrue(Math.abs(obj.getDouble(wrongFieldEnum) - 0.0) < 0.001);

        // Try with null - should revert to default value (0.0)
        obj.populateDoubleField(fieldId, null);
        assertTrue(Math.abs(obj.getDouble(fieldEnum) - 0.0) < 0.001);
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigPopulation#populateIntegerField(com.sldeditor.ui.detail.config.FieldId, java.lang.Integer)}.
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigPopulation#getInteger(com.sldeditor.ui.detail.config.FieldId)}.
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigPopulation#getInteger(com.sldeditor.common.xml.ui.FieldIdEnum)}.
     */
    @Test
    public void testInteger() {
        FieldIdEnum wrongFieldEnum = FieldIdEnum.ELSE_FILTER;

        FieldIdEnum fieldEnum = FieldIdEnum.DESCRIPTION;
        FieldId fieldId = new FieldId(fieldEnum);

        GraphicPanelFieldManager fieldConfigManager = new GraphicPanelFieldManager(String.class);

        FieldConfigInteger intField = new FieldConfigInteger(Geometry.class, fieldId, "label", true);
        intField.createUI();
        fieldConfigManager.add(fieldId, intField);

        FieldConfigPopulation obj = new FieldConfigPopulation(fieldConfigManager);

        int expectedValue = 1256;
        obj.populateIntegerField(fieldId, expectedValue);
        assertEquals(expectedValue, obj.getInteger(fieldId));
        assertEquals(expectedValue, obj.getInteger(fieldEnum));

        // This shouldn't work as it does not know about the field
        assertEquals(0, obj.getInteger(wrongFieldEnum));

        // Try with null - should revert to default value (0.0)
        obj.populateIntegerField(fieldId, null);
        assertEquals(0, obj.getInteger(fieldEnum));
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigPopulation#populateField(com.sldeditor.ui.detail.config.FieldId, org.opengis.filter.expression.Expression)}.
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigPopulation#populateField(com.sldeditor.common.xml.ui.FieldIdEnum, org.opengis.filter.expression.Expression)}.
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigPopulation#getExpression(com.sldeditor.ui.detail.config.FieldId)}.
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigPopulation#getExpression(com.sldeditor.common.xml.ui.FieldIdEnum)}.
     */
    @Test
    public void testExpression() {
        FieldIdEnum wrongFieldEnum = FieldIdEnum.ELSE_FILTER;

        FieldIdEnum fieldEnum = FieldIdEnum.DESCRIPTION;
        FieldId fieldId = new FieldId(fieldEnum);

        GraphicPanelFieldManager fieldConfigManager = new GraphicPanelFieldManager(String.class);

        FieldConfigInteger intField = new FieldConfigInteger(Geometry.class, fieldId, "label", true);
        intField.createUI();
        fieldConfigManager.add(fieldId, intField);

        FieldConfigPopulation obj = new FieldConfigPopulation(fieldConfigManager);

        int expectedValue = 1256;
        StyleBuilder styleBuilder = new StyleBuilder();

        Expression expression = styleBuilder.literalExpression(expectedValue);
        obj.populateField(fieldId, expression);

        LiteralExpressionImpl actualValue = (LiteralExpressionImpl) obj.getExpression(fieldId);

        assertEquals(expectedValue, ((Integer)actualValue.getValue()).intValue());

        actualValue = (LiteralExpressionImpl) obj.getExpression(fieldEnum);
        assertEquals(expectedValue, ((Integer)actualValue.getValue()).intValue());

        // This shouldn't work as it does not know about the field
        obj.populateField(wrongFieldEnum, expression);
        assertNull(obj.getExpression(wrongFieldEnum));

        // Try with null
        obj.populateField(fieldId, null);
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigPopulation#isTreeDataUpdated()}.
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigPopulation#resetTreeDataUpdated()}.
     */
    @Test
    public void testIsTreeDataUpdated() {
        FieldIdEnum fieldEnum = FieldIdEnum.DESCRIPTION;
        FieldId fieldId = new FieldId(fieldEnum);

        GraphicPanelFieldManager fieldConfigManager = new GraphicPanelFieldManager(String.class);

        FieldConfigString stringField = new FieldConfigString(Geometry.class, fieldId, "label", true, "button text");
        stringField.createUI();
        fieldConfigManager.add(fieldId, stringField);

        FieldConfigPopulation obj = new FieldConfigPopulation(fieldConfigManager);
        assertFalse(obj.isTreeDataUpdated());

        // Set data
        String expectedValue = "test string";
        obj.populateTextField(fieldId, expectedValue);
        assertTrue(expectedValue.compareTo(obj.getText(fieldId)) == 0);
        assertFalse(obj.isTreeDataUpdated());

        // Simulate a key being pressed
        expectedValue = "test string1";
        stringField.populateField(expectedValue);

        // Data is different so flag should have changed
        assertTrue(expectedValue.compareTo(obj.getText(fieldEnum)) == 0);
        assertTrue(obj.isTreeDataUpdated());
        obj.resetTreeDataUpdated();
        assertFalse(obj.isTreeDataUpdated());

        // Populate the same data again and the flag should not have changed
        obj.populateTextField(fieldId, expectedValue);
        assertTrue(expectedValue.compareTo(obj.getText(fieldId)) == 0);
        assertFalse(obj.isTreeDataUpdated());
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigPopulation#getFieldConfig(com.sldeditor.ui.detail.config.FieldId)}.
     */
    @Test
    public void testGetFieldConfig() {
        FieldIdEnum wrongFieldEnum = FieldIdEnum.ELSE_FILTER;

        FieldIdEnum fieldEnum = FieldIdEnum.DESCRIPTION;
        FieldId fieldId = new FieldId(fieldEnum);

        GraphicPanelFieldManager fieldConfigManager = new GraphicPanelFieldManager(String.class);

        FieldConfigInteger intField = new FieldConfigInteger(Geometry.class, fieldId, "label", true);
        intField.createUI();
        fieldConfigManager.add(fieldId, intField);

        FieldConfigPopulation obj = new FieldConfigPopulation(fieldConfigManager);

        assertNotNull(obj.getFieldConfig(fieldId));
        assertNull(obj.getFieldConfig(new FieldId(wrongFieldEnum)));
    }

}
