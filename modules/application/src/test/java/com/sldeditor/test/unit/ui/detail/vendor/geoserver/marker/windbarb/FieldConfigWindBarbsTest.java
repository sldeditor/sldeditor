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

package com.sldeditor.test.unit.ui.detail.vendor.geoserver.marker.windbarb;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sldeditor.common.vendoroption.VendorOptionManager;
import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.ui.detail.GraphicPanelFieldManager;
import com.sldeditor.ui.detail.PointFillDetails;
import com.sldeditor.ui.detail.config.FieldConfigBase;
import com.sldeditor.ui.detail.config.FieldConfigColour;
import com.sldeditor.ui.detail.config.FieldConfigCommonData;
import com.sldeditor.ui.detail.config.FieldConfigPopulate;
import com.sldeditor.ui.detail.config.FieldConfigSlider;
import com.sldeditor.ui.detail.config.FieldConfigSymbolType;
import com.sldeditor.ui.detail.vendor.geoserver.marker.windbarb.FieldConfigWindBarbs;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import org.geotools.styling.ExternalGraphicImpl;
import org.geotools.styling.Mark;
import org.geotools.styling.StyleBuilder;
import org.junit.jupiter.api.Test;
import org.opengis.filter.expression.Expression;
import org.opengis.style.GraphicalSymbol;

/**
 * The unit test for FieldConfigWindBarbs.
 *
 * <p>{@link com.sldeditor.ui.detail.vendor.geoserver.marker.windbarb.FieldConfigWindBarbs}
 *
 * @author Robert Ward (SCISYS)
 */
public class FieldConfigWindBarbsTest {

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.marker.windbarb.FieldConfigWindBarbs#internalSetEnabled(boolean)}.
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.marker.windbarb.FieldConfigWindBarbs#isEnabled()}.
     */
    @Test
    public void testSetEnabled() {
        // Value only, no attribute/expression dropdown
        boolean valueOnly = true;
        FieldConfigWindBarbs field =
                new FieldConfigWindBarbs(
                        new FieldConfigCommonData(
                                String.class, FieldIdEnum.NAME, "test label", valueOnly, false),
                        null,
                        null,
                        null);

        // Text field will not have been created
        boolean expectedValue = true;
        field.internalSetEnabled(expectedValue);

        assertTrue(field.isEnabled());

        // Create text field
        field.createUI();
        assertTrue(field.isEnabled());

        expectedValue = false;
        field.internalSetEnabled(expectedValue);

        assertTrue(field.isEnabled());

        // Has attribute/expression dropdown
        valueOnly = false;
        FieldConfigWindBarbs field2 =
                new FieldConfigWindBarbs(
                        new FieldConfigCommonData(
                                String.class, FieldIdEnum.NAME, "test label", valueOnly, false),
                        null,
                        null,
                        null);

        // Text field will not have been created
        expectedValue = true;
        field2.internalSetEnabled(expectedValue);
        assertTrue(field2.isEnabled());

        // Create text field
        field2.createUI();

        assertEquals(expectedValue, field2.isEnabled());

        expectedValue = false;
        field2.internalSetEnabled(expectedValue);

        assertTrue(field2.isEnabled());
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.marker.windbarb.FieldConfigWindBarbs#setVisible(boolean)}.
     */
    @Test
    public void testSetVisible() {
        boolean valueOnly = true;
        FieldConfigWindBarbs field =
                new FieldConfigWindBarbs(
                        new FieldConfigCommonData(
                                String.class, FieldIdEnum.NAME, "test label", valueOnly, false),
                        null,
                        null,
                        null);

        boolean expectedValue = true;
        field.setVisible(expectedValue);

        field.createUI();
        expectedValue = false;
        field.setVisible(expectedValue);
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.marker.windbarb.FieldConfigWindBarbs#generateExpression()}.
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.marker.windbarb.FieldConfigWindBarbs#populateExpression(java.lang.Object,
     * org.opengis.filter.expression.Expression)}. Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.marker.windbarb.FieldConfigWindBarbs#setTestValue(com.sldeditor.ui.detail.config.FieldId,
     * java.lang.String)}.
     */
    @Test
    public void testGenerateExpression() {
        boolean valueOnly = true;
        FieldConfigWindBarbs field =
                new FieldConfigWindBarbs(
                        new FieldConfigCommonData(
                                String.class, FieldIdEnum.NAME, "test label", valueOnly, false),
                        null,
                        null,
                        null);

        field.setTestValue(null, (String) null);
        field.populateExpression((Double) null);
        assertNull(field.getStringValue());

        // Create ui
        field.createUI();
        field.populateExpression((Double) null);
        String expectedValue = "string value";
        field.populateExpression(expectedValue);

        expectedValue = "windbarbs://default(9)[m/s]?hemisphere=s";
        field.setTestValue(null, expectedValue);

        field.populateExpression(expectedValue);
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.marker.windbarb.FieldConfigWindBarbs#revertToDefaultValue()}.
     */
    @Test
    public void testRevertToDefaultValue() {
        boolean valueOnly = true;
        FieldConfigWindBarbs field =
                new FieldConfigWindBarbs(
                        new FieldConfigCommonData(
                                String.class, FieldIdEnum.NAME, "test label", valueOnly, false),
                        null,
                        null,
                        null);

        field.revertToDefaultValue();

        field.createUI();
        field.revertToDefaultValue();
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.marker.windbarb.FieldConfigWindBarbs#isASingleValue()}.
     */
    @Test
    public void testIsASingleValue() {
        boolean valueOnly = true;
        FieldConfigWindBarbs field =
                new FieldConfigWindBarbs(
                        new FieldConfigCommonData(
                                String.class, FieldIdEnum.NAME, "test label", valueOnly, false),
                        null,
                        null,
                        null);

        assertFalse(field.isASingleValue());
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.marker.windbarb.FieldConfigWindBarbs#justSelected()}.
     */
    @Test
    public void testJustSelected() {
        boolean valueOnly = true;
        FieldConfigWindBarbs field =
                new FieldConfigWindBarbs(
                        new FieldConfigCommonData(
                                String.class, FieldIdEnum.NAME, "test label", valueOnly, false),
                        null,
                        null,
                        null);

        field.justSelected();
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.marker.windbarb.FieldConfigWindBarbs#createCopy(com.sldeditor.ui.detail.config.FieldConfigBase)}.
     */
    @Test
    public void testCreateCopy() {
        boolean valueOnly = true;

        class TestFieldConfigWindBarbs extends FieldConfigWindBarbs {
            public TestFieldConfigWindBarbs(FieldConfigCommonData commonData) {
                super(commonData, null, null, null);
            }

            public FieldConfigPopulate callCreateCopy(FieldConfigBase fieldConfigBase) {
                return createCopy(fieldConfigBase);
            }
        }

        TestFieldConfigWindBarbs field =
                new TestFieldConfigWindBarbs(
                        new FieldConfigCommonData(
                                String.class, FieldIdEnum.NAME, "test label", valueOnly, false));
        FieldConfigWindBarbs copy = (FieldConfigWindBarbs) field.callCreateCopy(null);
        assertNull(copy);

        copy = (FieldConfigWindBarbs) field.callCreateCopy(field);
        assertEquals(field.getFieldId(), copy.getFieldId());
        assertTrue(field.getLabel().compareTo(copy.getLabel()) == 0);
        assertEquals(field.isValueOnly(), copy.isValueOnly());
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.marker.windbarb.FieldConfigWindBarbs#attributeSelection(java.lang.String)}.
     */
    @Test
    public void testAttributeSelection() {
        boolean valueOnly = true;
        FieldConfigWindBarbs field =
                new FieldConfigWindBarbs(
                        new FieldConfigCommonData(
                                String.class, FieldIdEnum.NAME, "test label", valueOnly, false),
                        null,
                        null,
                        null);

        field.attributeSelection("field");
        // Does nothing
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.marker.windbarb.FieldConfigWindBarbs#getVendorOption()}.
     */
    @Test
    public void testGetVendorOption() {
        boolean valueOnly = true;
        FieldConfigWindBarbs field =
                new FieldConfigWindBarbs(
                        new FieldConfigCommonData(
                                String.class, FieldIdEnum.NAME, "test label", valueOnly, false),
                        null,
                        null,
                        null);

        assertEquals(
                VendorOptionManager.getInstance().getDefaultVendorOptionVersion(),
                field.getVendorOption());
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.marker.windbarb.FieldConfigWindBarbs#getSymbolClass()}.
     */
    @Test
    public void testGetSymbolClass() {
        boolean valueOnly = true;
        FieldConfigWindBarbs field =
                new FieldConfigWindBarbs(
                        new FieldConfigCommonData(
                                String.class, FieldIdEnum.NAME, "test label", valueOnly, false),
                        null,
                        null,
                        null);

        assertEquals(ExternalGraphicImpl.class, field.getSymbolClass());
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.marker.windbarb.FieldConfigWindBarbs#setValue(com.sldeditor.ui.detail.GraphicPanelFieldManager,
     * com.sldeditor.ui.detail.config.FieldConfigSymbolType, org.opengis.style.GraphicalSymbol)}.
     */
    @Test
    public void testSetValue() {
        boolean valueOnly = true;

        GraphicPanelFieldManager fieldConfigManager = null;

        FieldConfigWindBarbs field =
                new FieldConfigWindBarbs(
                        new FieldConfigCommonData(
                                String.class, FieldIdEnum.NAME, "test label", valueOnly, false),
                        null,
                        null,
                        null);

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

        ExternalGraphicImpl externalGraphic =
                (ExternalGraphicImpl) styleBuilder.createExternalGraphic(filename, "png");
        field.setValue(null, fieldConfigManager, null, null, externalGraphic);

        if (f != null) {
            f.delete();
        }
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.marker.windbarb.FieldConfigWindBarbs#getValue(com.sldeditor.ui.detail.GraphicPanelFieldManager,
     * org.opengis.filter.expression.Expression, boolean, boolean)}.
     */
    @Test
    public void testGetValue() {
        // Test it with null values
        boolean valueOnly = true;
        FieldConfigWindBarbs field =
                new FieldConfigWindBarbs(
                        new FieldConfigCommonData(
                                String.class, FieldIdEnum.NAME, "test label", valueOnly, false),
                        null,
                        null,
                        null);
        field.createUI();
        assertNull(field.getStringValue());

        GraphicPanelFieldManager fieldConfigManager = null;
        Expression symbolType = null;
        List<GraphicalSymbol> actualValue =
                field.getValue(fieldConfigManager, symbolType, false, false);

        assertTrue(actualValue.isEmpty());

        Class<?> panelId = PointFillDetails.class;
        fieldConfigManager = new GraphicPanelFieldManager(panelId);
        String actualMarkerSymbol = "solid";
        StyleBuilder styleBuilder = new StyleBuilder();
        symbolType = styleBuilder.literalExpression(actualMarkerSymbol);

        FieldIdEnum colourFieldId = FieldIdEnum.FILL_COLOUR;
        FieldConfigColour colourField =
                new FieldConfigColour(
                        new FieldConfigCommonData(panelId, colourFieldId, "", false, false));
        colourField.createUI();
        String expectedColourValue = "#012345";
        colourField.setTestValue(null, expectedColourValue);
        double expectedOpacityValue = 0.72;
        FieldConfigSlider opacityField =
                new FieldConfigSlider(
                        new FieldConfigCommonData(panelId, colourFieldId, "", false, false));
        opacityField.createUI();
        opacityField.populateField(expectedOpacityValue);
        FieldConfigBase symbolSelectionField =
                new FieldConfigSymbolType(
                        new FieldConfigCommonData(panelId, colourFieldId, "", false, false));
        symbolSelectionField.createUI();

        fieldConfigManager.add(colourFieldId, colourField);
        FieldIdEnum opacityFieldId = FieldIdEnum.OVERALL_OPACITY;
        fieldConfigManager.add(opacityFieldId, opacityField);
        FieldIdEnum symbolSelectionFieldId = FieldIdEnum.SYMBOL_TYPE;
        fieldConfigManager.add(symbolSelectionFieldId, symbolSelectionField);

        // Try without setting any fields
        actualValue = field.getValue(fieldConfigManager, symbolType, false, false);
        assertNotNull(actualValue);
        assertEquals(1, actualValue.size());
        Mark actualSymbol = (Mark) actualValue.get(0);
        assertTrue(
                actualSymbol.getWellKnownName().toString().compareTo("windbarbs://default(0)[m/s]")
                        == 0);
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.marker.windbarb.FieldConfigWindBarbs#getFill(org.opengis.style.GraphicFill,
     * com.sldeditor.ui.detail.GraphicPanelFieldManager)}.
     */
    @Test
    public void testGetFill() {
        boolean valueOnly = true;
        FieldConfigWindBarbs field =
                new FieldConfigWindBarbs(
                        new FieldConfigCommonData(
                                String.class, FieldIdEnum.NAME, "test label", valueOnly, false),
                        null,
                        null,
                        null);

        assertNull(field.getFill(null, null));
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.marker.windbarb.FieldConfigWindBarbs#getBasePanel()}.
     */
    @Test
    public void testGetBasePanel() {
        boolean valueOnly = true;
        FieldConfigWindBarbs field =
                new FieldConfigWindBarbs(
                        new FieldConfigCommonData(
                                String.class, FieldIdEnum.NAME, "test label", valueOnly, false),
                        null,
                        null,
                        null);

        assertNull(field.getBasePanel());
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.marker.windbarb.FieldConfigWindBarbs#getFieldList(com.sldeditor.ui.detail.GraphicPanelFieldManager)}.
     */
    @Test
    public void testGetFieldList() {
        boolean valueOnly = true;
        FieldConfigWindBarbs field =
                new FieldConfigWindBarbs(
                        new FieldConfigCommonData(
                                String.class, FieldIdEnum.NAME, "test label", valueOnly, false),
                        null,
                        null,
                        null);

        assertEquals(4, field.getFieldList(null).size());
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.marker.windbarb.FieldConfigWindBarbs#accept(org.opengis.style.GraphicalSymbol)}.
     */
    @Test
    public void testAccept() {
        boolean valueOnly = true;
        FieldConfigWindBarbs field =
                new FieldConfigWindBarbs(
                        new FieldConfigCommonData(
                                String.class, FieldIdEnum.NAME, "test label", valueOnly, false),
                        null,
                        null,
                        null);

        assertFalse(field.accept(null));

        StyleBuilder styleBuilder = new StyleBuilder();
        ExternalGraphicImpl externalGraphic =
                (ExternalGraphicImpl) styleBuilder.createExternalGraphic("test.tmp", "png");
        assertFalse(field.accept(externalGraphic));

        Mark marker1 = styleBuilder.createMark("triangle");
        assertFalse(field.accept(marker1));

        Mark marker2 = styleBuilder.createMark("windbarbs://default(15)[kts]");
        assertTrue(field.accept(marker2));
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.marker.windbarb.FieldConfigWindBarbs#setUpdateSymbolListener(com.sldeditor.ui.iface.UpdateSymbolInterface)}.
     */
    @Test
    public void testSetUpdateSymbolListener() {
        boolean valueOnly = true;
        FieldConfigWindBarbs field =
                new FieldConfigWindBarbs(
                        new FieldConfigCommonData(
                                String.class, FieldIdEnum.NAME, "test label", valueOnly, false),
                        null,
                        null,
                        null);

        field.setUpdateSymbolListener(null);
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.marker.windbarb.FieldConfigWindBarbs#getConfigField()}.
     */
    @Test
    public void testGetConfigField() {
        boolean valueOnly = true;
        FieldConfigWindBarbs field =
                new FieldConfigWindBarbs(
                        new FieldConfigCommonData(
                                String.class, FieldIdEnum.NAME, "test label", valueOnly, false),
                        null,
                        null,
                        null);

        assertEquals(field, field.getConfigField());
    }
}
