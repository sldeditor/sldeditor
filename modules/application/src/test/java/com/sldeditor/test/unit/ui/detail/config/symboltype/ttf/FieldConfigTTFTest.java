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

package com.sldeditor.test.unit.ui.detail.config.symboltype.ttf;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import org.geotools.styling.ExternalGraphicImpl;
import org.geotools.styling.Mark;
import org.geotools.styling.StyleBuilder;
import org.junit.Test;
import org.opengis.style.GraphicalSymbol;

import com.sldeditor.common.vendoroption.VendorOptionManager;
import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.ui.detail.FillDetails;
import com.sldeditor.ui.detail.GraphicPanelFieldManager;
import com.sldeditor.ui.detail.config.FieldConfigBase;
import com.sldeditor.ui.detail.config.FieldConfigColour;
import com.sldeditor.ui.detail.config.FieldId;
import com.sldeditor.ui.detail.config.symboltype.ttf.FieldConfigTTF;

/**
 * The unit test for FieldConfigTTF.
 * <p>{@link com.sldeditor.ui.detail.config.symboltype.ttf.FieldConfigTTF}
 *
 * @author Robert Ward (SCISYS)
 */
public class FieldConfigTTFTest {

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.symboltype.ttf.FieldConfigTTF#setEnabled(boolean)}.
     * Test method for {@link com.sldeditor.ui.detail.config.symboltype.ttf.FieldConfigTTF#isEnabled()}.
     */
    @Test
    public void testSetEnabled() {
        // Value only, no attribute/expression dropdown
        boolean valueOnly = true;
        FieldConfigTTF field = new FieldConfigTTF(String.class, new FieldId(FieldIdEnum.NAME), "test label", valueOnly);

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
        FieldConfigTTF field2 = new FieldConfigTTF(String.class, new FieldId(FieldIdEnum.NAME), "test label", valueOnly);

        // Text field will not have been created
        expectedValue = true;
        field.setEnabled(expectedValue);
        assertFalse(field2.isEnabled());

        // Create text field
        field2.createUI(null);

        assertEquals(expectedValue, field2.isEnabled());

        expectedValue = false;
        field2.setEnabled(expectedValue);

        assertEquals(expectedValue, field2.isEnabled());

        expectedValue = false;
        field2.setEnabled(expectedValue);

        assertFalse(field2.isEnabled());
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.symboltype.ttf.FieldConfigTTF#setVisible(boolean)}.
     */
    @Test
    public void testSetVisible() {
        boolean valueOnly = true;
        FieldConfigTTF field = new FieldConfigTTF(String.class, new FieldId(FieldIdEnum.NAME), "test label", valueOnly);

        boolean expectedValue = true;
        field.setVisible(expectedValue);

        field.createUI(null);
        expectedValue = false;
        field.setVisible(expectedValue);
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.symboltype.ttf.FieldConfigTTF#generateExpression()}.
     * Test method for {@link com.sldeditor.ui.detail.config.symboltype.ttf.FieldConfigTTF#populateExpression(java.lang.Object, org.opengis.filter.expression.Expression)}.
     * Test method for {@link com.sldeditor.ui.detail.config.symboltype.ttf.FieldConfigTTF#populateField(java.lang.String)}.
     * Test method for {@link com.sldeditor.ui.detail.config.symboltype.ttf.FieldConfigTTF#setTestValue(com.sldeditor.ui.detail.config.FieldId, java.lang.String)}.
     * Test method for {@link com.sldeditor.ui.detail.config.symboltype.ttf.FieldConfigTTF#getStringValue()}.
     */
    @Test
    public void testGenerateExpression() {
        boolean valueOnly = true;
        FieldConfigTTF field = new FieldConfigTTF(String.class, new FieldId(FieldIdEnum.NAME), "test label", valueOnly);

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
            if((f != null) && (f.toURI() != null))
            {
                if(f.toURI().toURL() != null)
                {
                    expectedValue = f.toURI().toURL().toString();
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        field.setTestValue(null, expectedValue);

        field.populateExpression(expectedValue, null);
        assertTrue(expectedValue.compareTo(field.getStringValue()) == 0);
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.symboltype.ttf.FieldConfigTTF#revertToDefaultValue()}.
     */
    @Test
    public void testRevertToDefaultValue() {
        boolean valueOnly = true;
        FieldConfigTTF field = new FieldConfigTTF(String.class, new FieldId(FieldIdEnum.NAME), "test label", valueOnly);

        field.revertToDefaultValue();

        field.createUI(null);
        field.revertToDefaultValue();
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.symboltype.ttf.FieldConfigTTF#getClassType()}.
     */
    @Test
    public void testGetClassType() {
        boolean valueOnly = true;
        FieldConfigTTF field = new FieldConfigTTF(String.class, new FieldId(FieldIdEnum.NAME), "test label", valueOnly);

        assertEquals(String.class, field.getClassType());
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.symboltype.ttf.FieldConfigTTF#justSelected()}.
     */
    @Test
    public void testJustSelected() {
        boolean valueOnly = true;
        FieldConfigTTF field = new FieldConfigTTF(String.class, new FieldId(FieldIdEnum.NAME), "test label", valueOnly);

        field.justSelected();
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.symboltype.ttf.FieldConfigTTF#createCopy(com.sldeditor.ui.detail.config.FieldConfigBase)}.
     */
    @Test
    public void testCreateCopy() {
        boolean valueOnly = true;

        class TestFieldConfigTTF extends FieldConfigTTF
        {

            public TestFieldConfigTTF(Class<?> panelId, FieldId id, String label,
                    boolean valueOnly) {
                super(panelId, id, label, valueOnly);
            }

            public FieldConfigBase callCreateCopy(FieldConfigBase fieldConfigBase)
            {
                return createCopy(fieldConfigBase);
            }
        }

        TestFieldConfigTTF field = new TestFieldConfigTTF(String.class, new FieldId(FieldIdEnum.NAME), "test label", valueOnly);
        FieldConfigTTF copy = (FieldConfigTTF) field.callCreateCopy(null);
        assertNull(copy);

        copy = (FieldConfigTTF) field.callCreateCopy(field);
        assertEquals(field.getFieldId().getFieldId(), copy.getFieldId().getFieldId());
        assertTrue(field.getLabel().compareTo(copy.getLabel()) == 0);
        assertEquals(field.isValueOnly(), copy.isValueOnly());
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.symboltype.ttf.FieldConfigTTF#attributeSelection(java.lang.String)}.
     */
    @Test
    public void testAttributeSelection() {
        boolean valueOnly = true;
        FieldConfigTTF field = new FieldConfigTTF(String.class, new FieldId(FieldIdEnum.NAME), "test label", valueOnly);

        field.attributeSelection("field");
        // Does nothing
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.symboltype.ttf.FieldConfigTTF#getVendorOption()}.
     */
    @Test
    public void testGetVendorOption() {
        boolean valueOnly = true;
        FieldConfigTTF field = new FieldConfigTTF(String.class, new FieldId(FieldIdEnum.NAME), "test label", valueOnly);

        assertEquals(VendorOptionManager.getInstance().getDefaultVendorOptionVersion(), field.getVendorOption());
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.symboltype.ttf.FieldConfigTTF#getSymbolClass()}.
     */
    @Test
    public void testGetSymbolClass() {
        boolean valueOnly = true;
        FieldConfigTTF field = new FieldConfigTTF(String.class, new FieldId(FieldIdEnum.NAME), "test label", valueOnly);

        assertEquals(ExternalGraphicImpl.class, field.getSymbolClass());
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.symboltype.ttf.FieldConfigTTF#setValue(com.sldeditor.ui.detail.GraphicPanelFieldManager, com.sldeditor.ui.detail.config.FieldConfigSymbolType, org.opengis.style.GraphicalSymbol)}.
     */
    @Test
    public void testSetValue() {
        boolean valueOnly = true;

        GraphicPanelFieldManager fieldConfigManager = null;

        Class<?> panelId = FillDetails.class;
        fieldConfigManager = new GraphicPanelFieldManager(panelId);
        FieldId colourFieldId = new FieldId(FieldIdEnum.FILL_COLOUR);
        FieldConfigColour colourField = new FieldConfigColour(panelId, colourFieldId, "", false);
        colourField.createUI(null);
        String expectedColourValue = "#012345";
        colourField.setTestValue(null, expectedColourValue);
        fieldConfigManager.add(colourFieldId, colourField);
        
        FieldConfigTTF field = new FieldConfigTTF(String.class, new FieldId(FieldIdEnum.NAME), "test label", valueOnly);

        field.setValue(null, null, null);
        field.setValue(fieldConfigManager, null, null);

        field.createUI(null);
        StyleBuilder styleBuilder = new StyleBuilder();
        Mark marker = styleBuilder.createMark("star", Color.green, Color.black, 2.0);

        field.setValue(null, null, marker);
        field.setValue(fieldConfigManager, null, marker);
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.symboltype.ttf.FieldConfigTTF#getValue(com.sldeditor.ui.detail.GraphicPanelFieldManager, org.opengis.filter.expression.Expression, boolean, boolean)}.
     */
    @Test
    public void testGetValue() {
        boolean valueOnly = true;

        GraphicPanelFieldManager fieldConfigManager = null;

        Class<?> panelId = FillDetails.class;
        fieldConfigManager = new GraphicPanelFieldManager(panelId);
        FieldId colourFieldId = new FieldId(FieldIdEnum.FILL_COLOUR);
        FieldConfigColour colourField = new FieldConfigColour(panelId, colourFieldId, "", false);
        colourField.createUI(null);
        String expectedColourValue = "#012345";
        colourField.setTestValue(null, expectedColourValue);
        fieldConfigManager.add(colourFieldId, colourField);
        
        FieldConfigTTF field = new FieldConfigTTF(String.class, new FieldId(FieldIdEnum.NAME), "test label", valueOnly);

        List<GraphicalSymbol> actualValue = field.getValue(null, null, false, false);
        assertTrue(actualValue.isEmpty());

        field.createUI(null);
        actualValue = field.getValue(null, null, false, false);
        assertTrue(actualValue.isEmpty());

        actualValue = field.getValue(fieldConfigManager, null, false, false);
        assertTrue(actualValue.size() == 1);
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.symboltype.ttf.FieldConfigTTF#getFill(org.opengis.style.GraphicFill, com.sldeditor.ui.detail.GraphicPanelFieldManager)}.
     */
    @Test
    public void testGetFill() {
        boolean valueOnly = true;
        FieldConfigTTF field = new FieldConfigTTF(String.class, new FieldId(FieldIdEnum.NAME), "test label", valueOnly);

        assertNull(field.getFill(null, null));
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.symboltype.ttf.FieldConfigTTF#getBasePanel()}.
     */
    @Test
    public void testGetBasePanel() {
        boolean valueOnly = true;
        FieldConfigTTF field = new FieldConfigTTF(String.class, new FieldId(FieldIdEnum.NAME), "test label", valueOnly);

        assertNull(field.getBasePanel());
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.symboltype.ttf.FieldConfigTTF#populateFieldOverrideMap(java.lang.Class, com.sldeditor.ui.detail.FieldEnableState)}.
     */
    @Test
    public void testPopulateFieldOverrideMap() {
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.symboltype.ttf.FieldConfigTTF#getFieldList(com.sldeditor.ui.detail.GraphicPanelFieldManager)}.
     */
    @Test
    public void testGetFieldList() {
        boolean valueOnly = true;
        FieldConfigTTF field = new FieldConfigTTF(String.class, new FieldId(FieldIdEnum.NAME), "test label", valueOnly);
        
        assertEquals(1, field.getFieldList(null).size());
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.symboltype.ttf.FieldConfigTTF#accept(org.opengis.style.GraphicalSymbol)}.
     */
    @Test
    public void testAccept() {
        boolean valueOnly = true;
        FieldConfigTTF field = new FieldConfigTTF(String.class, new FieldId(FieldIdEnum.NAME), "test label", valueOnly);

        assertFalse(field.accept(null));

        StyleBuilder styleBuilder = new StyleBuilder();
        ExternalGraphicImpl externalGraphic = (ExternalGraphicImpl) styleBuilder.createExternalGraphic("test.tmp", "png");
        assertFalse(field.accept(externalGraphic));

        Mark marker1 = styleBuilder.createMark("triangle");
        assertFalse(field.accept(marker1));

        Mark marker2 = styleBuilder.createMark("ttf://Arial");
        assertTrue(field.accept(marker2));
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.symboltype.ttf.FieldConfigTTF#setUpdateSymbolListener(com.sldeditor.ui.iface.UpdateSymbolInterface)}.
     */
    @Test
    public void testSetUpdateSymbolListener() {
        boolean valueOnly = true;
        FieldConfigTTF field = new FieldConfigTTF(String.class, new FieldId(FieldIdEnum.NAME), "test label", valueOnly);

        field.setUpdateSymbolListener(null);
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.symboltype.ttf.FieldConfigTTF#getConfigField()}.
     */
    @Test
    public void testGetConfigField() {
        boolean valueOnly = true;
        FieldConfigTTF field = new FieldConfigTTF(String.class, new FieldId(FieldIdEnum.NAME), "test label", valueOnly);

        assertEquals(field, field.getConfigField());
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.symboltype.ttf.FieldConfigTTF#checkSymbolIsValid()}.
     */
    @Test
    public void testCheckSymbolIsValid() {
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.symboltype.ttf.FieldConfigTTF#ttfValueUpdated()}.
     */
    @Test
    public void testTtfValueUpdated() {
    }

}
