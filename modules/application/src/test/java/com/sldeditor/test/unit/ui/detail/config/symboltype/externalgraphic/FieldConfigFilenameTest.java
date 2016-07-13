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
import com.sldeditor.ui.detail.FillDetails;
import com.sldeditor.ui.detail.GraphicPanelFieldManager;
import com.sldeditor.ui.detail.config.FieldConfigBase;
import com.sldeditor.ui.detail.config.FieldConfigColour;
import com.sldeditor.ui.detail.config.FieldConfigSlider;
import com.sldeditor.ui.detail.config.FieldConfigSymbolType;
import com.sldeditor.ui.detail.config.FieldId;
import com.sldeditor.ui.detail.config.symboltype.externalgraphic.FieldConfigFilename;

/**
 * The unit test for FieldConfigFilename.
 * <p>{@link com.sldeditor.ui.detail.config.symboltype.externalgraphic.FieldConfigFilename}
 *
 * @author Robert Ward (SCISYS)
 */
public class FieldConfigFilenameTest {

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.symboltype.externalgraphic.FieldConfigFilename#setEnabled(boolean)}.
     * Test method for {@link com.sldeditor.ui.detail.config.symboltype.externalgraphic.FieldConfigFilename#isEnabled()}.
     */
    @Test
    public void testSetEnabled() {
        // Value only, no attribute/expression dropdown
        boolean valueOnly = true;
        FieldConfigFilename field = new FieldConfigFilename(String.class, new FieldId(FieldIdEnum.NAME), "test label", valueOnly);

        // Text field will not have been created
        boolean expectedValue = true;
        field.setEnabled(expectedValue);

        assertTrue(field.isEnabled());

        // Create text field
        field.createUI(null);
        assertTrue(field.isEnabled());

        expectedValue = false;
        field.setEnabled(expectedValue);

        assertTrue(field.isEnabled());

        // Has attribute/expression dropdown
        valueOnly = false;
        FieldConfigFilename field2 = new FieldConfigFilename(String.class, new FieldId(FieldIdEnum.NAME), "test label", valueOnly);

        // Text field will not have been created
        expectedValue = true;
        field2.setEnabled(expectedValue);
        assertTrue(field2.isEnabled());

        // Create text field
        field2.createUI(null);

        assertEquals(expectedValue, field2.isEnabled());

        expectedValue = false;
        field2.setEnabled(expectedValue);

        assertTrue(field2.isEnabled());
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.symboltype.externalgraphic.FieldConfigFilename#setVisible(boolean)}.
     */
    @Test
    public void testSetVisible() {
        boolean valueOnly = true;
        FieldConfigFilename field = new FieldConfigFilename(String.class, new FieldId(FieldIdEnum.NAME), "test label", valueOnly);

        boolean expectedValue = true;
        field.setVisible(expectedValue);

        field.createUI(null);
        expectedValue = false;
        field.setVisible(expectedValue);
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.symboltype.externalgraphic.FieldConfigFilename#generateExpression()}.
     * Test method for {@link com.sldeditor.ui.detail.config.symboltype.externalgraphic.FieldConfigFilename#populateExpression(java.lang.Object, org.opengis.filter.expression.Expression)}.
     * Test method for {@link com.sldeditor.ui.detail.config.symboltype.externalgraphic.FieldConfigFilename#populateField(java.lang.String)}.
     * Test method for {@link com.sldeditor.ui.detail.config.symboltype.externalgraphic.FieldConfigFilename#setTestValue(com.sldeditor.ui.detail.config.FieldId, java.lang.String)}.
     */
    @Test
    public void testGenerateExpression() {
        boolean valueOnly = true;
        FieldConfigFilename field = new FieldConfigFilename(String.class, new FieldId(FieldIdEnum.NAME), "test label", valueOnly);

        field.populateExpression((Double)null, null);
        field.populateField((String)null);
        assertNull(field.getStringValue());

        // Create ui
        field.createUI(null);
        field.populateExpression((Double)null, null);
        String expectedValue = "string value";
        field.populateExpression(expectedValue, null);
        assertTrue(expectedValue.compareTo(field.getStringValue()) == 0);

        File f = null;
        try {
            f = File.createTempFile("test", ".tmp");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            expectedValue = f.toURI().toURL().toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        field.setTestValue(null, expectedValue);

        field.populateExpression(expectedValue, null);
        assertTrue(expectedValue.compareTo(field.getStringValue()) == 0);
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.symboltype.externalgraphic.FieldConfigFilename#revertToDefaultValue()}.
     */
    @Test
    public void testRevertToDefaultValue() {
        boolean valueOnly = true;
        FieldConfigFilename field = new FieldConfigFilename(String.class, new FieldId(FieldIdEnum.NAME), "test label", valueOnly);

        field.revertToDefaultValue();

        field.createUI(null);
        field.revertToDefaultValue();
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.symboltype.externalgraphic.FieldConfigFilename#getClassType()}.
     */
    @Test
    public void testGetClassType() {
        boolean valueOnly = true;
        FieldConfigFilename field = new FieldConfigFilename(String.class, new FieldId(FieldIdEnum.NAME), "test label", valueOnly);

        assertEquals(String.class, field.getClassType());
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.symboltype.externalgraphic.FieldConfigFilename#justSelected()}.
     */
    @Test
    public void testJustSelected() {
        boolean valueOnly = true;
        FieldConfigFilename field = new FieldConfigFilename(String.class, new FieldId(FieldIdEnum.NAME), "test label", valueOnly);

        field.justSelected();
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.symboltype.externalgraphic.FieldConfigFilename#createCopy(com.sldeditor.ui.detail.config.FieldConfigBase)}.
     */
    @Test
    public void testCreateCopy() {
        boolean valueOnly = true;

        class TestFieldConfigFilename extends FieldConfigFilename
        {

            public TestFieldConfigFilename(Class<?> panelId, FieldId id, String label,
                    boolean valueOnly) {
                super(panelId, id, label, valueOnly);
            }

            public FieldConfigBase callCreateCopy(FieldConfigBase fieldConfigBase)
            {
                return createCopy(fieldConfigBase);
            }
        }

        TestFieldConfigFilename field = new TestFieldConfigFilename(String.class, new FieldId(FieldIdEnum.NAME), "test label", valueOnly);
        FieldConfigFilename copy = (FieldConfigFilename) field.callCreateCopy(null);
        assertNull(copy);

        copy = (FieldConfigFilename) field.callCreateCopy(field);
        assertEquals(field.getFieldId().getFieldId(), copy.getFieldId().getFieldId());
        assertTrue(field.getLabel().compareTo(copy.getLabel()) == 0);
        assertEquals(field.isValueOnly(), copy.isValueOnly());
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.symboltype.externalgraphic.FieldConfigFilename#attributeSelection(java.lang.String)}.
     */
    @Test
    public void testAttributeSelection() {
        boolean valueOnly = true;
        FieldConfigFilename field = new FieldConfigFilename(String.class, new FieldId(FieldIdEnum.NAME), "test label", valueOnly);

        field.attributeSelection("field");
        // Does nothing
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.symboltype.externalgraphic.FieldConfigFilename#getVendorOption()}.
     */
    @Test
    public void testGetVendorOption() {
        boolean valueOnly = true;
        FieldConfigFilename field = new FieldConfigFilename(String.class, new FieldId(FieldIdEnum.NAME), "test label", valueOnly);

        assertEquals(VendorOptionManager.getInstance().getDefaultVendorOptionVersion(), field.getVendorOption());
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.symboltype.externalgraphic.FieldConfigFilename#getSymbolClass()}.
     */
    @Test
    public void testGetSymbolClass() {
        boolean valueOnly = true;
        FieldConfigFilename field = new FieldConfigFilename(String.class, new FieldId(FieldIdEnum.NAME), "test label", valueOnly);

        assertEquals(ExternalGraphicImpl.class, field.getSymbolClass());
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.symboltype.externalgraphic.FieldConfigFilename#setValue(com.sldeditor.ui.detail.GraphicPanelFieldManager, com.sldeditor.ui.detail.config.FieldConfigSymbolType, org.opengis.style.GraphicalSymbol)}.
     */
    @Test
    public void testSetValue() {
        boolean valueOnly = true;

        GraphicPanelFieldManager fieldConfigManager = null;

        Class<?> panelId = FillDetails.class;
        fieldConfigManager = new GraphicPanelFieldManager(panelId);

        FieldConfigFilename field = new FieldConfigFilename(String.class, new FieldId(FieldIdEnum.NAME), "test label", valueOnly);

        field.setValue(null, null, null);
        field.setValue(fieldConfigManager, null, null);

        field.createUI(null);
        StyleBuilder styleBuilder = new StyleBuilder();
        Mark marker = styleBuilder.createMark("star");
        field.setValue(null, null, marker);
        field.setValue(fieldConfigManager, null, marker);

        File f = null;
        String filename = null;
        try {
            f = File.createTempFile("test", ".png");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            filename = f.toURI().toURL().toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        ExternalGraphicImpl externalGraphic = (ExternalGraphicImpl) styleBuilder.createExternalGraphic(filename, "png");
        field.setValue(fieldConfigManager, null, externalGraphic);
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.symboltype.externalgraphic.FieldConfigFilename#getValue(com.sldeditor.ui.detail.GraphicPanelFieldManager, org.opengis.filter.expression.Expression, boolean, boolean)}.
     */
    @Test
    public void testGetValue() {
        boolean valueOnly = true;
        FieldConfigFilename field = new FieldConfigFilename(String.class, new FieldId(FieldIdEnum.NAME), "test label", valueOnly);

        assertNull(field.getValue(null, null, false, false));

        field.createUI(null);
        List<GraphicalSymbol> actualValue = field.getValue(null, null, false, false);
        assertFalse(actualValue.isEmpty());
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.symboltype.externalgraphic.FieldConfigFilename#getFill(org.opengis.style.GraphicFill, com.sldeditor.ui.detail.GraphicPanelFieldManager)}.
     */
    @Test
    public void testGetFill() {
        boolean valueOnly = true;
        FieldConfigFilename field = new FieldConfigFilename(String.class, new FieldId(FieldIdEnum.NAME), "test label", valueOnly);

        GraphicFill graphicFill = null;
        GraphicPanelFieldManager fieldConfigManager = null;
        assertNull(field.getFill(graphicFill, fieldConfigManager));

        Class<?> panelId = FillDetails.class;
        FieldId colourFieldId = new FieldId(FieldIdEnum.FILL_COLOUR);
        FieldConfigColour colourField = new FieldConfigColour(panelId, colourFieldId, "", false);
        colourField.createUI(null);
        String expectedColourValue = "#012345";
        colourField.setTestValue(null, expectedColourValue);
        FieldId opacityFieldId = new FieldId(FieldIdEnum.OPACITY);
        double expectedOpacityValue = 0.72;
        FieldConfigSlider opacityField = new FieldConfigSlider(panelId, colourFieldId, "", false);
        opacityField.createUI(null);
        opacityField.populateField(expectedOpacityValue);
        FieldId symbolSelectionFieldId = new FieldId(FieldIdEnum.SYMBOL_TYPE);
        FieldConfigBase symbolSelectionField = new FieldConfigSymbolType(panelId, colourFieldId, "", false);
        symbolSelectionField.createUI(null);

        fieldConfigManager = new GraphicPanelFieldManager(panelId);
        fieldConfigManager.add(colourFieldId, colourField);
        fieldConfigManager.add(opacityFieldId, opacityField);
        fieldConfigManager.add(symbolSelectionFieldId, symbolSelectionField);

        field.createUI(null);
        StyleBuilder styleBuilder = new StyleBuilder();
        graphicFill = styleBuilder.createGraphic();

        Fill actualValue = field.getFill(graphicFill, fieldConfigManager);

        assertTrue(actualValue.getOpacity().toString().compareTo(String.valueOf(expectedOpacityValue)) == 0);
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.symboltype.externalgraphic.FieldConfigFilename#getBasePanel()}.
     */
    @Test
    public void testGetBasePanel() {
        boolean valueOnly = true;
        FieldConfigFilename field = new FieldConfigFilename(String.class, new FieldId(FieldIdEnum.NAME), "test label", valueOnly);

        assertNull(field.getBasePanel());
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.symboltype.externalgraphic.FieldConfigFilename#getFieldList(com.sldeditor.ui.detail.GraphicPanelFieldManager)}.
     */
    @Test
    public void testGetFieldList() {
        boolean valueOnly = true;
        FieldConfigFilename field = new FieldConfigFilename(String.class, new FieldId(FieldIdEnum.NAME), "test label", valueOnly);
        
        assertEquals(1, field.getFieldList(null).size());
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.symboltype.externalgraphic.FieldConfigFilename#accept(org.opengis.style.GraphicalSymbol)}.
     */
    @Test
    public void testAccept() {
        boolean valueOnly = true;
        FieldConfigFilename field = new FieldConfigFilename(String.class, new FieldId(FieldIdEnum.NAME), "test label", valueOnly);

        assertFalse(field.accept(null));

        StyleBuilder styleBuilder = new StyleBuilder();
        ExternalGraphicImpl externalGraphic = (ExternalGraphicImpl) styleBuilder.createExternalGraphic("test.tmp", "png");
        assertTrue(field.accept(externalGraphic));

        Mark marker = styleBuilder.createMark("triangle");
        assertFalse(field.accept(marker));
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.symboltype.externalgraphic.FieldConfigFilename#setUpdateSymbolListener(com.sldeditor.ui.iface.UpdateSymbolInterface)}.
     */
    @Test
    public void testSetUpdateSymbolListener() {
        boolean valueOnly = true;
        FieldConfigFilename field = new FieldConfigFilename(String.class, new FieldId(FieldIdEnum.NAME), "test label", valueOnly);
        
        field.setUpdateSymbolListener(null);
    }
}
