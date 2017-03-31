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

package com.sldeditor.test.unit.ui.detail.config.symboltype.externalgraphic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import org.geotools.styling.ExternalGraphicImpl;
import org.geotools.styling.Fill;
import org.geotools.styling.Mark;
import org.geotools.styling.StyleBuilder;
import org.junit.Test;
import org.opengis.style.GraphicFill;
import org.opengis.style.GraphicalSymbol;

import com.sldeditor.common.vendoroption.VendorOptionManager;
import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.ui.detail.GraphicPanelFieldManager;
import com.sldeditor.ui.detail.PointFillDetails;
import com.sldeditor.ui.detail.config.FieldConfigBase;
import com.sldeditor.ui.detail.config.FieldConfigColour;
import com.sldeditor.ui.detail.config.FieldConfigCommonData;
import com.sldeditor.ui.detail.config.FieldConfigSlider;
import com.sldeditor.ui.detail.config.FieldConfigSymbolType;
import com.sldeditor.ui.detail.config.FieldConfigPopulate;
import com.sldeditor.ui.detail.config.symboltype.externalgraphic.FieldConfigFilename;

/**
 * The unit test for FieldConfigFilename.
 * 
 * <p>{@link com.sldeditor.ui.detail.config.symboltype.externalgraphic.FieldConfigFilename}
 *
 * @author Robert Ward (SCISYS)
 */
public class FieldConfigFilenameTest {

    /**
     * Test method for
     * {@link com.sldeditor.ui.detail.config.symboltype.externalgraphic.FieldConfigFilename#internal_setEnabled(boolean)}.
     * Test method for
     * {@link com.sldeditor.ui.detail.config.symboltype.externalgraphic.FieldConfigFilename#isEnabled()}.
     */
    @Test
    public void testSetEnabled() {
        // Value only, no attribute/expression dropdown
        boolean valueOnly = true;
        FieldConfigFilename field = new FieldConfigFilename(
                new FieldConfigCommonData(String.class, FieldIdEnum.NAME, "test label", valueOnly),
                null, null, null);

        // Text field will not have been created
        boolean expectedValue = true;
        field.internal_setEnabled(expectedValue);

        assertTrue(field.isEnabled());

        // Create text field
        field.createUI();
        field.createUI();
        assertTrue(field.isEnabled());

        expectedValue = false;
        field.internal_setEnabled(expectedValue);

        assertTrue(field.isEnabled());

        // Has attribute/expression dropdown
        valueOnly = false;
        FieldConfigFilename field2 = new FieldConfigFilename(
                new FieldConfigCommonData(String.class, FieldIdEnum.NAME, "test label", valueOnly),
                null, null, null);

        // Text field will not have been created
        expectedValue = true;
        field2.internal_setEnabled(expectedValue);
        assertTrue(field2.isEnabled());

        // Create text field
        field2.createUI();

        assertEquals(expectedValue, field2.isEnabled());

        expectedValue = false;
        field2.internal_setEnabled(expectedValue);

        assertTrue(field2.isEnabled());
    }

    /**
     * Test method for
     * {@link com.sldeditor.ui.detail.config.symboltype.externalgraphic.FieldConfigFilename#setVisible(boolean)}.
     */
    @Test
    public void testSetVisible() {
        boolean valueOnly = true;
        FieldConfigFilename field = new FieldConfigFilename(
                new FieldConfigCommonData(String.class, FieldIdEnum.NAME, "test label", valueOnly),
                null, null, null);

        boolean expectedValue = true;
        field.setVisible(expectedValue);

        field.createUI();
        expectedValue = false;
        field.setVisible(expectedValue);
    }

    /**
     * Test method for
     * {@link com.sldeditor.ui.detail.config.symboltype.externalgraphic.FieldConfigFilename#generateExpression()}.
     * Test method for
     * {@link com.sldeditor.ui.detail.config.symboltype.externalgraphic.FieldConfigFilename#populateExpression(java.lang.Object, org.opengis.filter.expression.Expression)}.
     * Test method for
     * {@link com.sldeditor.ui.detail.config.symboltype.externalgraphic.FieldConfigFilename#populateField(java.lang.String)}.
     * Test method for
     * {@link com.sldeditor.ui.detail.config.symboltype.externalgraphic.FieldConfigFilename#setTestValue(com.sldeditor.ui.detail.config.FieldId, java.lang.String)}.
     */
    @Test
    public void testGenerateExpression() {
        boolean valueOnly = true;
        FieldConfigFilename field = new FieldConfigFilename(
                new FieldConfigCommonData(String.class, FieldIdEnum.NAME, "test label", valueOnly),
                null, null, null);

        field.populateExpression((Double) null);
        field.populateField((String) null);
        assertNull(field.getStringValue());

        // Create ui
        field.createUI();
        field.populateExpression((Double) null);
        String expectedValue = "string value";
        field.populateExpression(expectedValue);

        assertEquals(expectedValue, field.getStringValue());

        File f = null;
        try {
            f = File.createTempFile(getClass().getSimpleName(), ".tmp");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if ((f != null) && (f.toURI() != null)) {
                if (f.toURI().toURL() != null) {
                    expectedValue = f.toURI().toURL().toString();
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        field.setTestValue(null, expectedValue);

        field.populateExpression(expectedValue);

        assertTrue(expectedValue.compareTo(field.getStringValue()) == 0);

        f.delete();
    }

    /**
     * Test method for
     * {@link com.sldeditor.ui.detail.config.symboltype.externalgraphic.FieldConfigFilename#revertToDefaultValue()}.
     */
    @Test
    public void testRevertToDefaultValue() {
        boolean valueOnly = true;
        FieldConfigFilename field = new FieldConfigFilename(
                new FieldConfigCommonData(String.class, FieldIdEnum.NAME, "test label", valueOnly),
                null, null, null);

        field.revertToDefaultValue();

        field.createUI();
        field.revertToDefaultValue();
    }

    /**
     * Test method for
     * {@link com.sldeditor.ui.detail.config.symboltype.externalgraphic.FieldConfigFilename#justSelected()}.
     */
    @Test
    public void testJustSelected() {
        boolean valueOnly = true;
        FieldConfigFilename field = new FieldConfigFilename(
                new FieldConfigCommonData(String.class, FieldIdEnum.NAME, "test label", valueOnly),
                null, null, null);

        field.justSelected();
    }

    /**
     * Test method for
     * {@link com.sldeditor.ui.detail.config.symboltype.externalgraphic.FieldConfigFilename#createCopy(com.sldeditor.ui.detail.config.FieldConfigBase)}.
     */
    @Test
    public void testCreateCopy() {
        boolean valueOnly = true;

        class TestFieldConfigFilename extends FieldConfigFilename {

            public TestFieldConfigFilename(FieldConfigCommonData commonData) {
                super(commonData, null, null, null);
            }

            public FieldConfigPopulate callCreateCopy(FieldConfigBase fieldConfigBase) {
                return createCopy(fieldConfigBase);
            }
        }

        TestFieldConfigFilename field = new TestFieldConfigFilename(
                new FieldConfigCommonData(String.class, FieldIdEnum.NAME, "test label", valueOnly));
        FieldConfigFilename copy = (FieldConfigFilename) field.callCreateCopy(null);
        assertNull(copy);

        copy = (FieldConfigFilename) field.callCreateCopy(field);
        assertEquals(field.getFieldId(), copy.getFieldId());
        assertTrue(field.getLabel().compareTo(copy.getLabel()) == 0);
        assertEquals(field.isValueOnly(), copy.isValueOnly());
    }

    /**
     * Test method for
     * {@link com.sldeditor.ui.detail.config.symboltype.externalgraphic.FieldConfigFilename#attributeSelection(java.lang.String)}.
     */
    @Test
    public void testAttributeSelection() {
        boolean valueOnly = true;
        FieldConfigFilename field = new FieldConfigFilename(
                new FieldConfigCommonData(String.class, FieldIdEnum.NAME, "test label", valueOnly),
                null, null, null);

        field.attributeSelection("field");
        // Does nothing
    }

    /**
     * Test method for
     * {@link com.sldeditor.ui.detail.config.symboltype.externalgraphic.FieldConfigFilename#getVendorOption()}.
     */
    @Test
    public void testGetVendorOption() {
        boolean valueOnly = true;
        FieldConfigFilename field = new FieldConfigFilename(
                new FieldConfigCommonData(String.class, FieldIdEnum.NAME, "test label", valueOnly),
                null, null, null);

        assertEquals(VendorOptionManager.getInstance().getDefaultVendorOptionVersion(),
                field.getVendorOption());
    }

    /**
     * Test method for
     * {@link com.sldeditor.ui.detail.config.symboltype.externalgraphic.FieldConfigFilename#getSymbolClass()}.
     */
    @Test
    public void testGetSymbolClass() {
        boolean valueOnly = true;
        FieldConfigFilename field = new FieldConfigFilename(
                new FieldConfigCommonData(String.class, FieldIdEnum.NAME, "test label", valueOnly),
                null, null, null);

        assertEquals(ExternalGraphicImpl.class, field.getSymbolClass());
    }

    /**
     * Test method for
     * {@link com.sldeditor.ui.detail.config.symboltype.externalgraphic.FieldConfigFilename#setValue(com.sldeditor.ui.detail.GraphicPanelFieldManager, com.sldeditor.ui.detail.config.FieldConfigSymbolType, org.opengis.style.GraphicalSymbol)}.
     */
    @Test
    public void testSetValue() {
        boolean valueOnly = true;

        GraphicPanelFieldManager fieldConfigManager = null;

        Class<?> panelId = PointFillDetails.class;
        fieldConfigManager = new GraphicPanelFieldManager(panelId);

        FieldConfigFilename field = new FieldConfigFilename(
                new FieldConfigCommonData(String.class, FieldIdEnum.NAME, "test label", valueOnly),
                null, null, null);

        field.setValue(null, null, null, null, null);
        field.setValue(null, fieldConfigManager, null, null, null);

        field.createUI();
        StyleBuilder styleBuilder = new StyleBuilder();
        Mark marker = styleBuilder.createMark("star");
        field.setValue(null, null, null, null, marker);
        field.setValue(null, fieldConfigManager, null, null, marker);

        File f = null;
        String filename = null;
        try {
            f = File.createTempFile(getClass().getSimpleName(), ".png");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if ((f != null) && (f.toURI() != null)) {
                if (f.toURI().toURL() != null) {
                    filename = f.toURI().toURL().toString();
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        ExternalGraphicImpl externalGraphic = (ExternalGraphicImpl) styleBuilder
                .createExternalGraphic(filename, "png");
        field.setValue(null, fieldConfigManager, null, null, externalGraphic);

        if (f != null) {
            f.delete();
        }
    }

    /**
     * Test method for
     * {@link com.sldeditor.ui.detail.config.symboltype.externalgraphic.FieldConfigFilename#getValue(com.sldeditor.ui.detail.GraphicPanelFieldManager, org.opengis.filter.expression.Expression, boolean, boolean)}.
     */
    @Test
    public void testGetValue() {
        boolean valueOnly = true;
        FieldConfigFilename field = new FieldConfigFilename(
                new FieldConfigCommonData(String.class, FieldIdEnum.NAME, "test label", valueOnly),
                null, null, null);

        assertNull(field.getValue(null, null, false, false));

        field.createUI();
        List<GraphicalSymbol> actualValue = field.getValue(null, null, false, false);
        assertFalse(actualValue.isEmpty());
    }

    /**
     * Test method for
     * {@link com.sldeditor.ui.detail.config.symboltype.externalgraphic.FieldConfigFilename#getFill(org.opengis.style.GraphicFill, com.sldeditor.ui.detail.GraphicPanelFieldManager)}.
     */
    @Test
    public void testGetFill() {
        boolean valueOnly = true;
        FieldConfigFilename field = new FieldConfigFilename(
                new FieldConfigCommonData(String.class, FieldIdEnum.NAME, "test label", valueOnly),
                null, null, null);

        GraphicFill graphicFill = null;
        GraphicPanelFieldManager fieldConfigManager = null;
        assertNull(field.getFill(graphicFill, fieldConfigManager));

        Class<?> panelId = PointFillDetails.class;
        FieldIdEnum colourFieldId = FieldIdEnum.FILL_COLOUR;
        FieldConfigColour colourField = new FieldConfigColour(
                new FieldConfigCommonData(panelId, colourFieldId, "", false));
        colourField.createUI();
        String expectedColourValue = "#012345";
        colourField.setTestValue(FieldIdEnum.UNKNOWN, expectedColourValue);
        double expectedOpacityValue = 0.72;
        FieldConfigSlider opacityField = new FieldConfigSlider(
                new FieldConfigCommonData(panelId, colourFieldId, "", false));
        opacityField.createUI();
        opacityField.populateField(expectedOpacityValue);
        FieldConfigBase symbolSelectionField = new FieldConfigSymbolType(
                new FieldConfigCommonData(panelId, colourFieldId, "", false));
        symbolSelectionField.createUI();

        fieldConfigManager = new GraphicPanelFieldManager(panelId);
        fieldConfigManager.add(colourFieldId, colourField);
        FieldIdEnum opacityFieldId = FieldIdEnum.OVERALL_OPACITY;
        fieldConfigManager.add(opacityFieldId, opacityField);
        FieldIdEnum symbolSelectionFieldId = FieldIdEnum.SYMBOL_TYPE;
        fieldConfigManager.add(symbolSelectionFieldId, symbolSelectionField);

        field.createUI();
        StyleBuilder styleBuilder = new StyleBuilder();
        graphicFill = styleBuilder.createGraphic();

        Fill actualValue = field.getFill(graphicFill, fieldConfigManager);

        assertTrue(actualValue.getOpacity().toString()
                .compareTo(String.valueOf(expectedOpacityValue)) == 0);
    }

    /**
     * Test method for
     * {@link com.sldeditor.ui.detail.config.symboltype.externalgraphic.FieldConfigFilename#getBasePanel()}.
     */
    @Test
    public void testGetBasePanel() {
        boolean valueOnly = true;
        FieldConfigFilename field = new FieldConfigFilename(
                new FieldConfigCommonData(String.class, FieldIdEnum.NAME, "test label", valueOnly),
                null, null, null);

        assertNull(field.getBasePanel());
    }

    /**
     * Test method for
     * {@link com.sldeditor.ui.detail.config.symboltype.externalgraphic.FieldConfigFilename#getFieldList(com.sldeditor.ui.detail.GraphicPanelFieldManager)}.
     */
    @Test
    public void testGetFieldList() {
        boolean valueOnly = true;
        FieldConfigFilename field = new FieldConfigFilename(
                new FieldConfigCommonData(String.class, FieldIdEnum.NAME, "test label", valueOnly),
                null, null, null);

        assertEquals(1, field.getFieldList(null).size());
    }

    /**
     * Test method for
     * {@link com.sldeditor.ui.detail.config.symboltype.externalgraphic.FieldConfigFilename#accept(org.opengis.style.GraphicalSymbol)}.
     */
    @Test
    public void testAccept() {
        boolean valueOnly = true;
        FieldConfigFilename field = new FieldConfigFilename(
                new FieldConfigCommonData(String.class, FieldIdEnum.NAME, "test label", valueOnly),
                null, null, null);

        assertFalse(field.accept(null));

        StyleBuilder styleBuilder = new StyleBuilder();
        ExternalGraphicImpl externalGraphic = (ExternalGraphicImpl) styleBuilder
                .createExternalGraphic("test.tmp", "png");
        assertTrue(field.accept(externalGraphic));

        Mark marker = styleBuilder.createMark("triangle");
        assertFalse(field.accept(marker));
    }

    /**
     * Test method for
     * {@link com.sldeditor.ui.detail.config.symboltype.externalgraphic.FieldConfigFilename#setUpdateSymbolListener(com.sldeditor.ui.iface.UpdateSymbolInterface)}.
     */
    @Test
    public void testSetUpdateSymbolListener() {
        boolean valueOnly = true;
        FieldConfigFilename field = new FieldConfigFilename(
                new FieldConfigCommonData(String.class, FieldIdEnum.NAME, "test label", valueOnly),
                null, null, null);

        field.setUpdateSymbolListener(null);
    }
}
