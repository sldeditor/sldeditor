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

package com.sldeditor.test.unit.ui.detail.config.symboltype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.geotools.filter.LiteralExpressionImpl;
import org.geotools.styling.ExternalGraphicImpl;
import org.geotools.styling.Fill;
import org.geotools.styling.Mark;
import org.geotools.styling.MarkImpl;
import org.geotools.styling.StyleBuilder;
import org.junit.Test;
import org.opengis.filter.expression.Expression;
import org.opengis.style.GraphicFill;
import org.opengis.style.GraphicalSymbol;

import com.sldeditor.common.vendoroption.VendorOptionManager;
import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.common.xml.ui.GroupIdEnum;
import com.sldeditor.ui.detail.ColourFieldConfig;
import com.sldeditor.ui.detail.GraphicPanelFieldManager;
import com.sldeditor.ui.detail.PointFillDetails;
import com.sldeditor.ui.detail.PointSymbolizerDetails;
import com.sldeditor.ui.detail.config.FieldConfigBase;
import com.sldeditor.ui.detail.config.FieldConfigColour;
import com.sldeditor.ui.detail.config.FieldConfigCommonData;
import com.sldeditor.ui.detail.config.FieldConfigPopulate;
import com.sldeditor.ui.detail.config.FieldConfigSlider;
import com.sldeditor.ui.detail.config.FieldConfigSymbolType;
import com.sldeditor.ui.detail.config.symboltype.FieldConfigMarker;
import com.sldeditor.ui.widgets.ValueComboBoxData;
import com.sldeditor.ui.widgets.ValueComboBoxDataGroup;

/**
 * The unit test for FieldConfigMarker.
 * <p>{@link com.sldeditor.ui.detail.config.symboltype.FieldConfigMarker}
 *
 * @author Robert Ward (SCISYS)
 */
public class FieldConfigMarkerTest {

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.symboltype.FieldConfigMarker#setEnabled(boolean)}.
     * Test method for {@link com.sldeditor.ui.detail.config.symboltype.FieldConfigMarker#isEnabled()}.
     */
    @Test
    public void testSetEnabled() {
        // Value only, no attribute/expression dropdown
        boolean valueOnly = true;
        FieldConfigMarker field = new FieldConfigMarker(new FieldConfigCommonData(String.class, FieldIdEnum.NAME, "test label", valueOnly), null, null, null);

        // Text field will not have been created
        boolean expectedValue = true;
        field.setEnabled(expectedValue);

        assertFalse(field.isEnabled());

        // Create text field
        field.createUI();
        assertFalse(field.isEnabled());

        expectedValue = false;
        field.setEnabled(expectedValue);

        assertFalse(field.isEnabled());

        // Has attribute/expression dropdown
        valueOnly = false;
        FieldConfigMarker field2 = new FieldConfigMarker(new FieldConfigCommonData(String.class, FieldIdEnum.NAME, "test label", valueOnly), null, null, null);

        // Text field will not have been created
        expectedValue = true;
        field2.setEnabled(expectedValue);
        assertFalse(field2.isEnabled());

        // Create text field
        field2.createUI();

        assertFalse(field2.isEnabled());

        expectedValue = false;
        field2.setEnabled(expectedValue);

        assertFalse(field2.isEnabled());
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.symboltype.FieldConfigMarker#setVisible(boolean)}.
     */
    @Test
    public void testSetVisible() {
        boolean valueOnly = true;
        FieldConfigMarker field = new FieldConfigMarker(new FieldConfigCommonData(String.class, FieldIdEnum.NAME, "test label", valueOnly), null, null, null);

        boolean expectedValue = true;
        field.setVisible(expectedValue);

        field.createUI();
        expectedValue = false;
        field.setVisible(expectedValue);
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.symboltype.FieldConfigMarker#populateExpression(java.lang.Object, org.opengis.filter.expression.Expression)}.
     * Test method for {@link com.sldeditor.ui.detail.config.symboltype.FieldConfigMarker#generateExpression()}.
     */
    @Test
    public void testGenerateExpression() {
        boolean valueOnly = true;
        FieldConfigMarker field = new FieldConfigMarker(new FieldConfigCommonData(String.class, FieldIdEnum.NAME, "test label", valueOnly), null, null, null);

        assertNull(field.getStringValue());

        field.populateExpression((String)null);
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.symboltype.FieldConfigMarker#revertToDefaultValue()}.
     */
    @Test
    public void testRevertToDefaultValue() {
        boolean valueOnly = true;
        FieldConfigMarker field = new FieldConfigMarker(new FieldConfigCommonData(String.class, FieldIdEnum.NAME, "test label", valueOnly), null, null, null);

        field.revertToDefaultValue();

        // Does nothing
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.symboltype.FieldConfigMarker#createCopy(com.sldeditor.ui.detail.config.FieldConfigBase)}.
     */
    @Test
    public void testCreateCopy() {
        boolean valueOnly = true;

        class TestFieldConfigMarker extends FieldConfigMarker
        {

            public TestFieldConfigMarker(FieldConfigCommonData commonData,
                    ColourFieldConfig fillFieldConfig,
                    ColourFieldConfig strokeFieldConfig,
                    FieldIdEnum symbolSelectionField) {
                super(commonData,
                        fillFieldConfig, 
                        strokeFieldConfig,
                        symbolSelectionField);
            }

            public FieldConfigPopulate callCreateCopy(FieldConfigBase fieldConfigBase)
            {
                return createCopy(fieldConfigBase);
            }
        }

        TestFieldConfigMarker field = new TestFieldConfigMarker(new FieldConfigCommonData(String.class, FieldIdEnum.NAME, "test label", valueOnly), null, null, null);
        FieldConfigMarker copy = (FieldConfigMarker) field.callCreateCopy(null);
        assertNull(copy);

        copy = (FieldConfigMarker) field.callCreateCopy(field);
        assertEquals(field.getFieldId(), copy.getFieldId());
        assertTrue(field.getLabel().compareTo(copy.getLabel()) == 0);
        assertEquals(field.isValueOnly(), copy.isValueOnly());
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.symboltype.FieldConfigMarker#attributeSelection(java.lang.String)}.
     */
    @Test
    public void testAttributeSelection() {
        boolean valueOnly = true;
        FieldConfigMarker field = new FieldConfigMarker(new FieldConfigCommonData(String.class, FieldIdEnum.NAME, "test label", valueOnly), null, null, null);

        field.attributeSelection("field");
        // Does nothing
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.symboltype.FieldConfigMarker#getVendorOption()}.
     */
    @Test
    public void testGetVendorOption() {
        boolean valueOnly = true;
        FieldConfigMarker field = new FieldConfigMarker(new FieldConfigCommonData(String.class, FieldIdEnum.NAME, "test label", valueOnly), null, null, null);

        assertEquals(VendorOptionManager.getInstance().getDefaultVendorOptionVersion(), field.getVendorOption());
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.symboltype.FieldConfigMarker#getSymbolClass()}.
     */
    @Test
    public void testGetSymbolClass() {
        boolean valueOnly = true;
        FieldConfigMarker field = new FieldConfigMarker(new FieldConfigCommonData(String.class, FieldIdEnum.NAME, "test label", valueOnly), null, null, null);

        assertEquals(MarkImpl.class, field.getSymbolClass());
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.symboltype.FieldConfigMarker#populateSymbolList(java.lang.Class, java.util.List)}.
     */
    @Test
    public void testPopulateSymbolList() {
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.symboltype.FieldConfigMarker#getFill(org.opengis.style.GraphicFill, com.sldeditor.ui.detail.GraphicPanelFieldManager)}.
     */
    @Test
    public void testGetFill() {
        // Test it with null values
        boolean valueOnly = true;
        ColourFieldConfig fillConfig = new ColourFieldConfig(GroupIdEnum.FILL, FieldIdEnum.FILL_COLOUR, FieldIdEnum.OVERALL_OPACITY, FieldIdEnum.STROKE_WIDTH);
        ColourFieldConfig strokeConfig = new ColourFieldConfig(GroupIdEnum.STROKE, FieldIdEnum.STROKE_STROKE_COLOUR, FieldIdEnum.OVERALL_OPACITY, FieldIdEnum.STROKE_FILL_WIDTH);
        FieldConfigMarker field = new FieldConfigMarker(new FieldConfigCommonData(String.class, FieldIdEnum.NAME, "test label", valueOnly), fillConfig, strokeConfig, null);

        assertNull(field.getStringValue());

        GraphicFill graphicFill = null;
        GraphicPanelFieldManager fieldConfigManager = null;
        Fill actualValue = field.getFill(graphicFill, fieldConfigManager);

        assertNull(actualValue);

        Class<?> panelId = PointFillDetails.class;
        fieldConfigManager = new GraphicPanelFieldManager(panelId);
        actualValue = field.getFill(graphicFill, fieldConfigManager);
        assertNotNull(actualValue);
        assertNull(actualValue.getColor());
        assertNull(actualValue.getGraphicFill());
        assertNull(actualValue.getOpacity());

        // Test it with non null values
        FieldIdEnum colourFieldId = FieldIdEnum.FILL_COLOUR;

        FieldConfigColour colourField = new FieldConfigColour(new FieldConfigCommonData(panelId, colourFieldId, "", false));
        colourField.createUI();
        String expectedColourValue = "#012345";
        colourField.setTestValue(null, expectedColourValue);
        FieldIdEnum opacityFieldId = FieldIdEnum.OVERALL_OPACITY;
        double expectedOpacityValue = 0.72;
        FieldConfigSlider opacityField = new FieldConfigSlider(new FieldConfigCommonData(panelId, colourFieldId, "", false));
        opacityField.createUI();
        opacityField.populateField(expectedOpacityValue);
        FieldIdEnum symbolSelectionFieldId = FieldIdEnum.SYMBOL_TYPE;
        FieldConfigBase symbolSelectionField = new FieldConfigSymbolType(new FieldConfigCommonData(panelId, colourFieldId, "", false));
        symbolSelectionField.createUI();

        fieldConfigManager.add(colourFieldId, colourField);
        fieldConfigManager.add(opacityFieldId, opacityField);
        fieldConfigManager.add(symbolSelectionFieldId, symbolSelectionField);

        FieldConfigMarker field2 = new FieldConfigMarker(new FieldConfigCommonData(String.class, FieldIdEnum.NAME, "test label", valueOnly), fillConfig, strokeConfig, symbolSelectionFieldId);
        actualValue = field2.getFill(graphicFill, fieldConfigManager);
        assertNotNull(actualValue);
        LiteralExpressionImpl literalExpressionImpl = (LiteralExpressionImpl)actualValue.getColor();
        String actualColourString = literalExpressionImpl.toString();
        assertTrue(actualColourString.compareTo(expectedColourValue) == 0);

        StyleBuilder styleBuilder = new StyleBuilder();

        graphicFill = styleBuilder.createGraphic();
        actualValue = field2.getFill(graphicFill, fieldConfigManager);
        assertNull(actualValue.getColor());
        assertNull(actualValue.getOpacity());
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.symboltype.FieldConfigMarker#getBasePanel()}.
     */
    @Test
    public void testGetBasePanel() {
        boolean valueOnly = true;
        FieldConfigMarker field = new FieldConfigMarker(new FieldConfigCommonData(String.class, FieldIdEnum.NAME, "test label", valueOnly), null, null, null);

        assertNull(field.getBasePanel());
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.symboltype.FieldConfigMarker#setSolidFill(com.sldeditor.ui.detail.GraphicPanelFieldManager, org.opengis.filter.expression.Expression, org.opengis.filter.expression.Expression)}.
     */
    @Test
    public void testSetSolidFill() {
        boolean valueOnly = true;

        Class<?> panelId = PointFillDetails.class;
        GraphicPanelFieldManager fieldConfigManager = new GraphicPanelFieldManager(panelId);

        // Test it with non null values
        ColourFieldConfig fillConfig = new ColourFieldConfig(GroupIdEnum.FILL, FieldIdEnum.FILL_COLOUR, FieldIdEnum.OVERALL_OPACITY, FieldIdEnum.STROKE_WIDTH);
        ColourFieldConfig strokeConfig = new ColourFieldConfig(GroupIdEnum.STROKE, FieldIdEnum.STROKE_STROKE_COLOUR, FieldIdEnum.OVERALL_OPACITY, FieldIdEnum.STROKE_FILL_WIDTH);
        FieldIdEnum colourFieldId = FieldIdEnum.FILL_COLOUR;
        FieldConfigColour colourField = new FieldConfigColour(new FieldConfigCommonData(panelId, colourFieldId, "", false));
        colourField.createUI();
        String expectedColourValue = "#012345";
        colourField.setTestValue(null, expectedColourValue);
        FieldIdEnum opacityFieldId = FieldIdEnum.OVERALL_OPACITY;
        double expectedOpacityValue = 0.72;
        FieldConfigSlider opacityField = new FieldConfigSlider(new FieldConfigCommonData(panelId, colourFieldId, "", false));
        opacityField.createUI();
        opacityField.populateField(expectedOpacityValue);
        FieldIdEnum symbolSelectionFieldId = FieldIdEnum.SYMBOL_TYPE;
        FieldConfigBase symbolSelectionField = new FieldConfigSymbolType(new FieldConfigCommonData(panelId, colourFieldId, "", false));
        symbolSelectionField.createUI();

        fieldConfigManager.add(colourFieldId, colourField);
        fieldConfigManager.add(opacityFieldId, opacityField);
        fieldConfigManager.add(symbolSelectionFieldId, symbolSelectionField);

        FieldConfigMarker field = new FieldConfigMarker(new FieldConfigCommonData(String.class, FieldIdEnum.NAME, "test label", valueOnly), fillConfig, strokeConfig, null);

        field.setSolidFill(null, null, null);
        field.setSolidFill(fieldConfigManager, null, null);
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.symboltype.FieldConfigMarker#populateFieldOverrideMap(java.lang.Class, com.sldeditor.ui.detail.FieldEnableState)}.
     */
    @Test
    public void testPopulateFieldOverrideMap() {
        boolean valueOnly = true;

        Class<?> panelId = PointFillDetails.class;
        GraphicPanelFieldManager fieldConfigManager = new GraphicPanelFieldManager(panelId);

        // Test it with non null values
        ColourFieldConfig fillConfig = new ColourFieldConfig(GroupIdEnum.FILL, FieldIdEnum.FILL_COLOUR, FieldIdEnum.OVERALL_OPACITY, FieldIdEnum.STROKE_WIDTH);
        ColourFieldConfig strokeConfig = new ColourFieldConfig(GroupIdEnum.STROKE, FieldIdEnum.STROKE_STROKE_COLOUR, FieldIdEnum.OVERALL_OPACITY, FieldIdEnum.STROKE_FILL_WIDTH);
        FieldIdEnum colourFieldId = FieldIdEnum.FILL_COLOUR;
        FieldConfigColour colourField = new FieldConfigColour(new FieldConfigCommonData(panelId, colourFieldId, "", false));
        colourField.createUI();
        String expectedColourValue = "#012345";
        colourField.setTestValue(null, expectedColourValue);
        FieldIdEnum opacityFieldId = FieldIdEnum.OVERALL_OPACITY;
        double expectedOpacityValue = 0.72;
        FieldConfigSlider opacityField = new FieldConfigSlider(new FieldConfigCommonData(panelId, colourFieldId, "", false));
        opacityField.createUI();
        opacityField.populateField(expectedOpacityValue);
        FieldIdEnum symbolSelectionFieldId = FieldIdEnum.SYMBOL_TYPE;
        FieldConfigBase symbolSelectionField = new FieldConfigSymbolType(new FieldConfigCommonData(panelId, colourFieldId, "", false));
        symbolSelectionField.createUI();

        fieldConfigManager.add(colourFieldId, colourField);
        fieldConfigManager.add(opacityFieldId, opacityField);
        fieldConfigManager.add(symbolSelectionFieldId, symbolSelectionField);

        FieldConfigMarker field = new FieldConfigMarker(new FieldConfigCommonData(String.class, FieldIdEnum.NAME, "test label", valueOnly), fillConfig, strokeConfig, null);

        field.populateFieldOverrideMap(String.class, null);
        field.populateFieldOverrideMap(PointSymbolizerDetails.class, null);
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.symboltype.FieldConfigMarker#getFieldList(com.sldeditor.ui.detail.GraphicPanelFieldManager)}.
     */
    @Test
    public void testGetFieldList() {
        boolean valueOnly = true;

        Class<?> panelId = PointFillDetails.class;
        GraphicPanelFieldManager fieldConfigManager = new GraphicPanelFieldManager(panelId);

        // Test it with non null values
        ColourFieldConfig fillConfig = new ColourFieldConfig(GroupIdEnum.FILL, FieldIdEnum.FILL_COLOUR, FieldIdEnum.OVERALL_OPACITY, FieldIdEnum.STROKE_WIDTH);
        ColourFieldConfig strokeConfig = new ColourFieldConfig(GroupIdEnum.STROKE, FieldIdEnum.STROKE_STROKE_COLOUR, FieldIdEnum.OVERALL_OPACITY, FieldIdEnum.STROKE_FILL_WIDTH);
        FieldIdEnum colourFieldId = FieldIdEnum.FILL_COLOUR;
        FieldConfigColour colourField = new FieldConfigColour(new FieldConfigCommonData(panelId, colourFieldId, "", false));
        colourField.createUI();
        String expectedColourValue = "#012345";
        colourField.setTestValue(null, expectedColourValue);
        FieldIdEnum opacityFieldId = FieldIdEnum.OVERALL_OPACITY;
        double expectedOpacityValue = 0.72;
        FieldConfigSlider opacityField = new FieldConfigSlider(new FieldConfigCommonData(panelId, colourFieldId, "", false));
        opacityField.createUI();
        opacityField.populateField(expectedOpacityValue);
        FieldIdEnum symbolSelectionFieldId = FieldIdEnum.SYMBOL_TYPE;
        FieldConfigBase symbolSelectionField = new FieldConfigSymbolType(new FieldConfigCommonData(panelId, colourFieldId, "", false));
        symbolSelectionField.createUI();

        fieldConfigManager.add(colourFieldId, colourField);
        fieldConfigManager.add(opacityFieldId, opacityField);
        fieldConfigManager.add(symbolSelectionFieldId, symbolSelectionField);

        FieldConfigMarker field = new FieldConfigMarker(new FieldConfigCommonData(String.class, FieldIdEnum.NAME, "test label", valueOnly), fillConfig, strokeConfig, null);

        assertTrue(field.getFieldList(null).isEmpty());
        Map<FieldIdEnum, FieldConfigBase> actualFieldList = field.getFieldList(fieldConfigManager);
        assertFalse(actualFieldList.isEmpty());
        assertEquals(5, actualFieldList.size());
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.symboltype.FieldConfigMarker#accept(org.opengis.style.GraphicalSymbol)}.
     */
    @Test
    public void testAccept() {
        boolean valueOnly = true;
        FieldConfigMarker field = new FieldConfigMarker(new FieldConfigCommonData(String.class, FieldIdEnum.NAME, "test label", valueOnly), null, null, null);

        assertFalse(field.accept(null));

        StyleBuilder styleBuilder = new StyleBuilder();
        ExternalGraphicImpl externalGraphic = (ExternalGraphicImpl) styleBuilder.createExternalGraphic("test.tmp", "png");
        assertFalse(field.accept(externalGraphic));

        Mark marker = styleBuilder.createMark("triangle");
        assertFalse(field.accept(marker));

        List<ValueComboBoxData> dataList = new ArrayList<ValueComboBoxData>();

        dataList.add(new ValueComboBoxData("star", "Star", this.getClass()));
        dataList.add(new ValueComboBoxData("square", "Square", this.getClass()));
        dataList.add(new ValueComboBoxData("triangle", "Triangle", this.getClass()));

        List<ValueComboBoxDataGroup> groupList = new ArrayList<ValueComboBoxDataGroup>();
        groupList.add(new ValueComboBoxDataGroup(dataList));

        field.populateSymbolList(String.class, groupList);
        field.populateSymbolList(PointFillDetails.class, groupList);
        assertTrue(field.accept(marker));
        field.populateSymbolList(PointFillDetails.class, groupList);
        assertTrue(field.accept(marker));
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.symboltype.FieldConfigMarker#setUpdateSymbolListener(com.sldeditor.ui.iface.UpdateSymbolInterface)}.
     */
    @Test
    public void testSetUpdateSymbolListener() {
        boolean valueOnly = true;
        FieldConfigMarker field = new FieldConfigMarker(new FieldConfigCommonData(String.class, FieldIdEnum.NAME, "test label", valueOnly), null, null, null);

        field.setUpdateSymbolListener(null);
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.symboltype.FieldConfigMarker#getConfigField()}.
     */
    @Test
    public void testGetConfigField() {
        boolean valueOnly = true;
        FieldConfigMarker field = new FieldConfigMarker(new FieldConfigCommonData(String.class, FieldIdEnum.NAME, "test label", valueOnly), null, null, null);

        assertEquals(field, field.getConfigField());
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.symboltype.FieldConfigMarker#getColourExpression()}.
     */
    @Test
    public void testGetColourExpression() {
        boolean valueOnly = true;
        FieldConfigMarker field = new FieldConfigMarker(new FieldConfigCommonData(String.class, FieldIdEnum.NAME, "test label", valueOnly), null, null, null);

        assertNull(field.getColourExpression());
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.symboltype.FieldConfigMarker#getFillColourOpacity()}.
     */
    @Test
    public void testGetFillColourOpacity() {
        boolean valueOnly = true;
        FieldConfigMarker field = new FieldConfigMarker(new FieldConfigCommonData(String.class, FieldIdEnum.NAME, "test label", valueOnly), null, null, null);

        assertNull(field.getFillColourOpacity());
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.symboltype.FieldConfigMarker#getSolidFillValue()}.
     */
    @Test
    public void testGetSolidFillValue() {
        assertTrue(FieldConfigMarker.getSolidFillValue().compareTo("solid") == 0);
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.symboltype.FieldConfigMarker#getNoFillValue()}.
     */
    @Test
    public void testGetNoFillValue() {
        assertTrue(FieldConfigMarker.getNoFillValue().compareTo("none") == 0);
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.symboltype.FieldConfigMarker#setValue(com.sldeditor.ui.detail.GraphicPanelFieldManager, com.sldeditor.ui.detail.config.FieldConfigSymbolType, org.opengis.style.GraphicalSymbol)}.
     */
    @Test
    public void testSetValue() {
        boolean valueOnly = true;

        GraphicPanelFieldManager fieldConfigManager = null;

        Class<?> panelId = PointFillDetails.class;
        fieldConfigManager = new GraphicPanelFieldManager(panelId);

        // Test it with non null values
        ColourFieldConfig fillConfig = new ColourFieldConfig(GroupIdEnum.FILL, FieldIdEnum.FILL_COLOUR, FieldIdEnum.OVERALL_OPACITY, FieldIdEnum.STROKE_WIDTH);
        ColourFieldConfig strokeConfig = new ColourFieldConfig(GroupIdEnum.STROKE, FieldIdEnum.STROKE_STROKE_COLOUR, FieldIdEnum.OVERALL_OPACITY, FieldIdEnum.STROKE_FILL_WIDTH);
        FieldIdEnum colourFieldId = FieldIdEnum.FILL_COLOUR;
        FieldConfigColour colourField = new FieldConfigColour(new FieldConfigCommonData(panelId, colourFieldId, "", false));
        colourField.createUI();
        String expectedColourValue = "#012345";
        colourField.setTestValue(null, expectedColourValue);
        FieldIdEnum opacityFieldId = FieldIdEnum.OVERALL_OPACITY;
        double expectedOpacityValue = 0.72;
        FieldConfigSlider opacityField = new FieldConfigSlider(new FieldConfigCommonData(panelId, colourFieldId, "", false));
        opacityField.createUI();
        opacityField.populateField(expectedOpacityValue);
        FieldIdEnum symbolSelectionFieldId = FieldIdEnum.SYMBOL_TYPE;
        FieldConfigBase symbolSelectionField = new FieldConfigSymbolType(new FieldConfigCommonData(panelId, colourFieldId, "", false));
        symbolSelectionField.createUI();

        fieldConfigManager.add(colourFieldId, colourField);
        fieldConfigManager.add(opacityFieldId, opacityField);
        fieldConfigManager.add(symbolSelectionFieldId, symbolSelectionField);

        FieldConfigMarker field2 = new FieldConfigMarker(new FieldConfigCommonData(String.class, FieldIdEnum.NAME, "test label", valueOnly), fillConfig, strokeConfig, null);

        field2.setValue(null, null, null, null,null);
        field2.setValue(null, fieldConfigManager, null, null, null);

        StyleBuilder styleBuilder = new StyleBuilder();
        Mark marker = styleBuilder.createMark("star");
        field2.setValue(null, null, null, null,marker);
        field2.setValue(null, fieldConfigManager, null, null,marker);
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.symboltype.FieldConfigMarker#getValue(com.sldeditor.ui.detail.GraphicPanelFieldManager, org.opengis.filter.expression.Expression, boolean, boolean)}.
     */
    @Test
    public void testGetValue() {
        StyleBuilder styleBuilder = new StyleBuilder();
        // Test it with null values
        boolean valueOnly = true;
        ColourFieldConfig fillConfig = new ColourFieldConfig(GroupIdEnum.FILL, FieldIdEnum.FILL_COLOUR, FieldIdEnum.OVERALL_OPACITY, FieldIdEnum.STROKE_WIDTH);
        ColourFieldConfig strokeConfig = new ColourFieldConfig(GroupIdEnum.STROKE, FieldIdEnum.STROKE_STROKE_COLOUR, FieldIdEnum.OVERALL_OPACITY, FieldIdEnum.STROKE_FILL_WIDTH);

        FieldConfigMarker field = new FieldConfigMarker(new FieldConfigCommonData(String.class, FieldIdEnum.NAME, "test label", valueOnly), fillConfig, strokeConfig, null);

        assertNull(field.getStringValue());

        GraphicPanelFieldManager fieldConfigManager = null;
        Expression symbolType = null;
        List<GraphicalSymbol> actualValue = field.getValue(fieldConfigManager, symbolType, false, false);

        assertNull(actualValue);

        Class<?> panelId = PointFillDetails.class;
        fieldConfigManager = new GraphicPanelFieldManager(panelId);
        String actualMarkerSymbol = "solid";
        symbolType = styleBuilder.literalExpression(actualMarkerSymbol);

        FieldIdEnum colourFieldId = FieldIdEnum.FILL_COLOUR;
        FieldConfigColour colourField = new FieldConfigColour(new FieldConfigCommonData(panelId, colourFieldId, "", false));
        colourField.createUI();
        String expectedColourValue = "#012345";
        colourField.setTestValue(null, expectedColourValue);
        FieldIdEnum opacityFieldId = FieldIdEnum.OVERALL_OPACITY;
        double expectedOpacityValue = 0.72;
        FieldConfigSlider opacityField = new FieldConfigSlider(new FieldConfigCommonData(panelId, colourFieldId, "", false));
        opacityField.createUI();
        opacityField.populateField(expectedOpacityValue);
        FieldIdEnum symbolSelectionFieldId = FieldIdEnum.SYMBOL_TYPE;
        FieldConfigBase symbolSelectionField = new FieldConfigSymbolType(new FieldConfigCommonData(panelId, colourFieldId, "", false));
        symbolSelectionField.createUI();

        fieldConfigManager.add(colourFieldId, colourField);
        fieldConfigManager.add(opacityFieldId, opacityField);
        fieldConfigManager.add(symbolSelectionFieldId, symbolSelectionField);

        // Try without setting any fields
        actualValue = field.getValue(fieldConfigManager, symbolType, false, false);
        assertNotNull(actualValue);
        assertEquals(1, actualValue.size());
        Mark actualSymbol = (Mark) actualValue.get(0);
        assertNull(actualSymbol.getWellKnownName());

        // Try with symbol type of solid
        FieldConfigMarker field2 = new FieldConfigMarker(new FieldConfigCommonData(String.class, FieldIdEnum.NAME, "test label", valueOnly), fillConfig, strokeConfig, symbolSelectionFieldId);

        actualValue = field2.getValue(fieldConfigManager, symbolType, false, false);
        assertNotNull(actualValue);
        assertEquals(1, actualValue.size());
        actualSymbol = (Mark) actualValue.get(0);
        assertNull(actualSymbol.getWellKnownName());

        // Try with symbol type of circle
        actualMarkerSymbol = "circle";
        symbolType = styleBuilder.literalExpression(actualMarkerSymbol);
        actualValue = field2.getValue(fieldConfigManager, symbolType, false, false);
        assertNotNull(actualValue);
        assertEquals(1, actualValue.size());
        actualSymbol = (Mark) actualValue.get(0);
        assertTrue(actualSymbol.getWellKnownName().toString().compareTo(actualMarkerSymbol) == 0);
        assertNull(actualSymbol.getFill());
        assertNull(actualSymbol.getStroke());

        // Enable stroke and fill flags
        actualValue = field2.getValue(fieldConfigManager, symbolType, true, true);
        assertNotNull(actualValue);
        assertEquals(1, actualValue.size());
        actualSymbol = (Mark) actualValue.get(0);
        assertTrue(actualSymbol.getWellKnownName().toString().compareTo(actualMarkerSymbol) == 0);
        assertNotNull(actualSymbol.getFill());
        assertNotNull(actualSymbol.getStroke());
    }
}
