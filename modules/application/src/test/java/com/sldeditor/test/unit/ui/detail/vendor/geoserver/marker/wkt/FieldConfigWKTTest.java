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

package com.sldeditor.test.unit.ui.detail.vendor.geoserver.marker.wkt;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import org.geotools.styling.ExternalGraphicImpl;
import org.geotools.styling.Mark;
import org.geotools.styling.StyleBuilder;
import org.junit.Test;
import org.opengis.filter.expression.Expression;
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
import com.sldeditor.ui.detail.vendor.geoserver.marker.wkt.FieldConfigWKT;

/**
 * The unit test for FieldConfigWKT.
 * <p>{@link com.sldeditor.ui.detail.vendor.geoserver.marker.wkt.FieldConfigWKT}
 *
 * @author Robert Ward (SCISYS)
 */
public class FieldConfigWKTTest {

    /**
     * Test method for {@link com.sldeditor.ui.detail.vendor.geoserver.marker.wkt.FieldConfigWKT#setEnabled(boolean)}.
     * Test method for {@link com.sldeditor.ui.detail.vendor.geoserver.marker.wkt.FieldConfigWKT#isEnabled()}.
     */
    @Test
    public void testSetEnabled() {
        // Value only, no attribute/expression dropdown
        boolean valueOnly = true;
        FieldConfigWKT field = new FieldConfigWKT(String.class, new FieldId(FieldIdEnum.NAME), "test label", valueOnly);

        // Text field will not have been created
        boolean expectedValue = true;
        field.setEnabled(expectedValue);

        assertFalse(field.isEnabled());

        // Create text field
        field.createUI();
        assertTrue(field.isEnabled());

        expectedValue = false;
        field.setEnabled(expectedValue);

        assertFalse(field.isEnabled());

        // Has attribute/expression dropdown
        valueOnly = false;
        FieldConfigWKT field2 = new FieldConfigWKT(String.class, new FieldId(FieldIdEnum.NAME), "test label", valueOnly);

        // Text field will not have been created
        expectedValue = true;
        field2.setEnabled(expectedValue);
        assertFalse(field2.isEnabled());

        // Create text field
        field2.createUI();

        assertTrue(field2.isEnabled());

        expectedValue = false;
        field2.setEnabled(expectedValue);

        assertFalse(field2.isEnabled());
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.vendor.geoserver.marker.wkt.FieldConfigWKT#setVisible(boolean)}.
     */
    @Test
    public void testSetVisible() {
        boolean valueOnly = true;
        FieldConfigWKT field = new FieldConfigWKT(String.class, new FieldId(FieldIdEnum.NAME), "test label", valueOnly);

        boolean expectedValue = true;
        field.setVisible(expectedValue);

        field.createUI();
        expectedValue = false;
        field.setVisible(expectedValue);
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.vendor.geoserver.marker.wkt.FieldConfigWKT#generateExpression()}.
     * Test method for {@link com.sldeditor.ui.detail.vendor.geoserver.marker.wkt.FieldConfigWKT#populateExpression(java.lang.Object, org.opengis.filter.expression.Expression)}.
     * Test method for {@link com.sldeditor.ui.detail.vendor.geoserver.marker.wkt.FieldConfigWKT#setTestValue(com.sldeditor.ui.detail.config.FieldId, java.lang.String)}.
     */
    @Test
    public void testGenerateExpression() {
        boolean valueOnly = true;
        FieldConfigWKT field = new FieldConfigWKT(String.class, new FieldId(FieldIdEnum.NAME), "test label", valueOnly);

        field.populateExpression((Double)null);
        field.populateField((String)null);
        assertNull(field.getStringValue());

        // Create ui
        field.createUI();
        field.populateExpression((Double)null);
        String expectedValue = "string value";
        field.populateExpression(expectedValue);
        assertTrue(expectedValue.compareTo(field.getStringValue()) == 0);

        expectedValue = "wkt://POLYGON((0 0, 1 0, 1 1, 0 1, 0 0))";
        field.setTestValue(null, expectedValue);

        field.populateExpression(expectedValue);
        assertTrue(expectedValue.compareTo(field.getStringValue()) == 0);
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.vendor.geoserver.marker.wkt.FieldConfigWKT#revertToDefaultValue()}.
     */
    @Test
    public void testRevertToDefaultValue() {
        boolean valueOnly = true;
        FieldConfigWKT field = new FieldConfigWKT(String.class, new FieldId(FieldIdEnum.NAME), "test label", valueOnly);

        field.revertToDefaultValue();

        field.createUI();

        field.revertToDefaultValue();
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.vendor.geoserver.marker.wkt.FieldConfigWKT#justSelected()}.
     */
    @Test
    public void testJustSelected() {
        boolean valueOnly = true;
        FieldConfigWKT field = new FieldConfigWKT(String.class, new FieldId(FieldIdEnum.NAME), "test label", valueOnly);

        field.justSelected();
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.vendor.geoserver.marker.wkt.FieldConfigWKT#createCopy(com.sldeditor.ui.detail.config.FieldConfigBase)}.
     */
    @Test
    public void testCreateCopy() {
        boolean valueOnly = true;

        class TestFieldConfigWKT extends FieldConfigWKT
        {

            public TestFieldConfigWKT(Class<?> panelId, FieldId id, String label,
                    boolean valueOnly) {
                super(panelId, id, label, valueOnly);
            }

            public FieldConfigBase callCreateCopy(FieldConfigBase fieldConfigBase)
            {
                return createCopy(fieldConfigBase);
            }
        }

        TestFieldConfigWKT field = new TestFieldConfigWKT(String.class, new FieldId(FieldIdEnum.NAME), "test label", valueOnly);
        FieldConfigWKT copy = (FieldConfigWKT) field.callCreateCopy(null);
        assertNull(copy);

        copy = (FieldConfigWKT) field.callCreateCopy(field);
        assertEquals(field.getFieldId().getFieldId(), copy.getFieldId().getFieldId());
        assertTrue(field.getLabel().compareTo(copy.getLabel()) == 0);
        assertEquals(field.isValueOnly(), copy.isValueOnly());
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.vendor.geoserver.marker.wkt.FieldConfigWKT#attributeSelection(java.lang.String)}.
     */
    @Test
    public void testAttributeSelection() {
        boolean valueOnly = true;
        FieldConfigWKT field = new FieldConfigWKT(String.class, new FieldId(FieldIdEnum.NAME), "test label", valueOnly);

        field.attributeSelection("field");
        // Does nothing
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.vendor.geoserver.marker.wkt.FieldConfigWKT#getVendorOption()}.
     */
    @Test
    public void testGetVendorOption() {
        boolean valueOnly = true;
        FieldConfigWKT field = new FieldConfigWKT(String.class, new FieldId(FieldIdEnum.NAME), "test label", valueOnly);

        assertEquals(VendorOptionManager.getInstance().getDefaultVendorOptionVersion(), field.getVendorOption());
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.vendor.geoserver.marker.wkt.FieldConfigWKT#getSymbolClass()}.
     */
    @Test
    public void testGetSymbolClass() {
        boolean valueOnly = true;
        FieldConfigWKT field = new FieldConfigWKT(String.class, new FieldId(FieldIdEnum.NAME), "test label", valueOnly);

        assertEquals(ExternalGraphicImpl.class, field.getSymbolClass());
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.vendor.geoserver.marker.wkt.FieldConfigWKT#setValue(com.sldeditor.ui.detail.GraphicPanelFieldManager, com.sldeditor.ui.detail.config.FieldConfigSymbolType, org.opengis.style.GraphicalSymbol)}.
     */
    @Test
    public void testSetValue() {
        boolean valueOnly = true;

        GraphicPanelFieldManager fieldConfigManager = null;

        Class<?> panelId = FillDetails.class;
        fieldConfigManager = new GraphicPanelFieldManager(panelId);

        FieldConfigWKT field = new FieldConfigWKT(String.class, new FieldId(FieldIdEnum.NAME), "test label", valueOnly);

        field.setValue(null, null, null);
        field.setValue(fieldConfigManager, null, null);

        field.createUI();
        StyleBuilder styleBuilder = new StyleBuilder();
        Mark marker1 = styleBuilder.createMark("star");
        field.setValue(null, null, marker1);
        field.setValue(fieldConfigManager, null, marker1);

        Mark marker2 = styleBuilder.createMark("wkt://POLYGON((0 0, 1 0, 1 1, 0 1, 0 0))", styleBuilder.createFill(), styleBuilder.createStroke());

        field.setValue(null, null, marker2);

        fieldConfigManager = new GraphicPanelFieldManager(panelId);

        FieldId colourFieldId = new FieldId(FieldIdEnum.FILL_COLOUR);
        FieldConfigColour colourField = new FieldConfigColour(panelId, colourFieldId, "", false);
        colourField.createUI();
        String expectedColourValue = "#012345";
        colourField.setTestValue(null, expectedColourValue);
        FieldId opacityFieldId = new FieldId(FieldIdEnum.OPACITY);
        double expectedOpacityValue = 0.72;
        FieldConfigSlider opacityField = new FieldConfigSlider(panelId, colourFieldId, "", false);
        opacityField.createUI();
        opacityField.populateField(expectedOpacityValue);
        FieldId symbolSelectionFieldId = new FieldId(FieldIdEnum.SYMBOL_TYPE);
        FieldConfigBase symbolSelectionField = new FieldConfigSymbolType(panelId, colourFieldId, "", false);
        symbolSelectionField.createUI();

        fieldConfigManager.add(colourFieldId, colourField);
        fieldConfigManager.add(opacityFieldId, opacityField);
        fieldConfigManager.add(symbolSelectionFieldId, symbolSelectionField);

        field.setValue(fieldConfigManager, null, marker2);

        File f = null;
        String filename = null;
        try {
            f = File.createTempFile("test", ".png");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if((f != null) && (f.toURI() != null))
            {
                if(f.toURI().toURL() != null)
                {
                    filename = f.toURI().toURL().toString();
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        // Try unsupported symbol
        ExternalGraphicImpl externalGraphic = (ExternalGraphicImpl) styleBuilder.createExternalGraphic(filename, "png");
        field.setValue(fieldConfigManager, null, externalGraphic);

        if(f != null)
        {
            f.delete();
        }
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.vendor.geoserver.marker.wkt.FieldConfigWKT#getValue(com.sldeditor.ui.detail.GraphicPanelFieldManager, org.opengis.filter.expression.Expression, boolean, boolean)}.
     */
    @Test
    public void testGetValue() {
        StyleBuilder styleBuilder = new StyleBuilder();
        // Test it with null values
        boolean valueOnly = true;
        FieldConfigWKT field = new FieldConfigWKT(String.class, new FieldId(FieldIdEnum.NAME), "test label", valueOnly);

        assertNull(field.getStringValue());

        GraphicPanelFieldManager fieldConfigManager = null;
        Expression symbolType = null;
        List<GraphicalSymbol> actualValue = field.getValue(fieldConfigManager, symbolType, false, false);

        assertTrue(actualValue.isEmpty());

        Class<?> panelId = FillDetails.class;
        fieldConfigManager = new GraphicPanelFieldManager(panelId);
        String actualMarkerSymbol = "solid";
        symbolType = styleBuilder.literalExpression(actualMarkerSymbol);

        FieldId colourFieldId = new FieldId(FieldIdEnum.FILL_COLOUR);
        FieldConfigColour colourField = new FieldConfigColour(panelId, colourFieldId, "", false);
        colourField.createUI();
        String expectedColourValue = "#012345";
        colourField.setTestValue(null, expectedColourValue);
        FieldId opacityFieldId = new FieldId(FieldIdEnum.OPACITY);
        double expectedOpacityValue = 0.72;
        FieldConfigSlider opacityField = new FieldConfigSlider(panelId, colourFieldId, "", false);
        opacityField.createUI();
        opacityField.populateField(expectedOpacityValue);
        FieldId symbolSelectionFieldId = new FieldId(FieldIdEnum.SYMBOL_TYPE);
        FieldConfigBase symbolSelectionField = new FieldConfigSymbolType(panelId, colourFieldId, "", false);
        symbolSelectionField.createUI();

        fieldConfigManager.add(colourFieldId, colourField);
        fieldConfigManager.add(opacityFieldId, opacityField);
        fieldConfigManager.add(symbolSelectionFieldId, symbolSelectionField);

        // Try without setting any fields
        actualValue = field.getValue(fieldConfigManager, symbolType, false, false);
        assertNotNull(actualValue);
        assertEquals(0, actualValue.size());

        // Try with symbol type of solid
        FieldConfigWKT field2 = new FieldConfigWKT(String.class, new FieldId(FieldIdEnum.NAME), "test label", valueOnly);

        actualValue = field2.getValue(fieldConfigManager, symbolType, false, false);
        assertNotNull(actualValue);
        assertEquals(0, actualValue.size());

        field2.createUI();

        // Try with symbol type of wkt shape
        actualMarkerSymbol = "wkt://POLYGON((0 0, 1 0, 1 1, 0 1, 0 0))";
        field2.setTestValue(null, actualMarkerSymbol);
        symbolType = styleBuilder.literalExpression(actualMarkerSymbol);
        actualValue = field2.getValue(fieldConfigManager, symbolType, false, false);
        assertNotNull(actualValue);
        assertEquals(1, actualValue.size());
        Mark actualSymbol = (Mark) actualValue.get(0);
        assertTrue(actualSymbol.getWellKnownName().toString().compareTo(actualMarkerSymbol) == 0);
        assertNotNull(actualSymbol.getFill());
        assertNull(actualSymbol.getStroke());

        // Enable stroke and fill flags
        actualValue = field2.getValue(fieldConfigManager, symbolType, true, true);
        assertNotNull(actualValue);
        assertEquals(1, actualValue.size());
        actualSymbol = (Mark) actualValue.get(0);
        assertTrue(actualSymbol.getWellKnownName().toString().compareTo(actualMarkerSymbol) == 0);
        assertNotNull(actualSymbol.getFill());
        assertNull(actualSymbol.getStroke());
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.vendor.geoserver.marker.wkt.FieldConfigWKT#populateSymbolList(java.lang.Class, java.util.List)}.
     */
    @Test
    public void testPopulateSymbolList() {
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.vendor.geoserver.marker.wkt.FieldConfigWKT#getFill(org.opengis.style.GraphicFill, com.sldeditor.ui.detail.GraphicPanelFieldManager)}.
     */
    @Test
    public void testGetFill() {
        boolean valueOnly = true;
        FieldConfigWKT field = new FieldConfigWKT(String.class, new FieldId(FieldIdEnum.NAME), "test label", valueOnly);

        assertNull(field.getFill(null, null));
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.vendor.geoserver.marker.wkt.FieldConfigWKT#getBasePanel()}.
     */
    @Test
    public void testGetBasePanel() {
        boolean valueOnly = true;
        FieldConfigWKT field = new FieldConfigWKT(String.class, new FieldId(FieldIdEnum.NAME), "test label", valueOnly);

        assertNull(field.getBasePanel());
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.vendor.geoserver.marker.wkt.FieldConfigWKT#populateFieldOverrideMap(java.lang.Class, com.sldeditor.ui.detail.FieldEnableState)}.
     */
    @Test
    public void testPopulateFieldOverrideMap() {
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.vendor.geoserver.marker.wkt.FieldConfigWKT#getFieldList(com.sldeditor.ui.detail.GraphicPanelFieldManager)}.
     */
    @Test
    public void testGetFieldList() {
        boolean valueOnly = true;
        FieldConfigWKT field = new FieldConfigWKT(String.class, new FieldId(FieldIdEnum.NAME), "test label", valueOnly);

        assertEquals(1, field.getFieldList(null).size());
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.vendor.geoserver.marker.wkt.FieldConfigWKT#accept(org.opengis.style.GraphicalSymbol)}.
     */
    @Test
    public void testAccept() {
        boolean valueOnly = true;
        FieldConfigWKT field = new FieldConfigWKT(String.class, new FieldId(FieldIdEnum.NAME), "test label", valueOnly);

        assertFalse(field.accept(null));

        StyleBuilder styleBuilder = new StyleBuilder();
        ExternalGraphicImpl externalGraphic = (ExternalGraphicImpl) styleBuilder.createExternalGraphic("test.tmp", "png");
        assertFalse(field.accept(externalGraphic));

        Mark marker1 = styleBuilder.createMark("star");
        assertFalse(field.accept(marker1));

        Mark marker2 = styleBuilder.createMark("wkt://MULTILINESTRING((-0.25 -0.25, -0.125 -0.25), (0.125 -0.25, 0.25 -0.25), (-0.25 0.25, -0.125 0.25), (0.125 0.25, 0.25 0.25))");
        assertTrue(field.accept(marker2));
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.vendor.geoserver.marker.wkt.FieldConfigWKT#setUpdateSymbolListener(com.sldeditor.ui.iface.UpdateSymbolInterface)}.
     */
    @Test
    public void testSetUpdateSymbolListener() {
        boolean valueOnly = true;
        FieldConfigWKT field = new FieldConfigWKT(String.class, new FieldId(FieldIdEnum.NAME), "test label", valueOnly);

        field.setUpdateSymbolListener(null);
    }

}
