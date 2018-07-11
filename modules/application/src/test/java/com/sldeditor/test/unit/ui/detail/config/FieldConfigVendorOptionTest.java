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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sldeditor.common.vendoroption.GeoServerVendorOption;
import com.sldeditor.common.vendoroption.VersionData;
import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.ui.detail.RasterSymbolizerDetails;
import com.sldeditor.ui.detail.config.FieldConfigBase;
import com.sldeditor.ui.detail.config.FieldConfigCommonData;
import com.sldeditor.ui.detail.config.FieldConfigPopulate;
import com.sldeditor.ui.detail.config.FieldConfigVendorOption;
import com.sldeditor.ui.detail.vendor.geoserver.VendorOptionInterface;
import com.sldeditor.ui.detail.vendor.geoserver.raster.VendorOptionRasterFactory;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * The unit test for FieldConfigVendorOption.
 *
 * <p>{@link com.sldeditor.ui.detail.config.FieldConfigVendorOption}
 *
 * @author Robert Ward (SCISYS)
 */
public class FieldConfigVendorOptionTest {

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.config.FieldConfigVendorOption#internal_setEnabled(boolean)}. Test
     * method for {@link com.sldeditor.ui.detail.config.FieldConfigVendorOption#isEnabled()}. Test
     * method for {@link com.sldeditor.ui.detail.config.FieldConfigVendorOption#createUI()}.
     */
    @Test
    public void testSetEnabled() {
        // Value only, no attribute/expression dropdown
        List<VendorOptionInterface> veList = null;
        boolean valueOnly = true;
        FieldConfigVendorOption field =
                new FieldConfigVendorOption(
                        new FieldConfigCommonData(
                                Double.class, FieldIdEnum.NAME, "label", valueOnly),
                        veList);

        // Text field will not have been created
        boolean expectedValue = true;
        field.internal_setEnabled(expectedValue);

        assertTrue(field.isEnabled());

        // Create text field
        field.createUI();
        assertEquals(expectedValue, field.isEnabled());

        expectedValue = false;
        field.internal_setEnabled(expectedValue);

        assertTrue(field.isEnabled());

        // Has attribute/expression dropdown
        valueOnly = false;
        FieldConfigVendorOption field2 =
                new FieldConfigVendorOption(
                        new FieldConfigCommonData(
                                Double.class, FieldIdEnum.NAME, "label", valueOnly),
                        veList);

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
     * Test method for {@link
     * com.sldeditor.ui.detail.config.FieldConfigVendorOption#setVisible(boolean)}.
     */
    @Test
    public void testSetVisible() {
        List<VendorOptionInterface> veList = null;
        boolean valueOnly = true;
        FieldConfigVendorOption field =
                new FieldConfigVendorOption(
                        new FieldConfigCommonData(
                                Double.class, FieldIdEnum.NAME, "label", valueOnly),
                        veList);

        boolean expectedValue = true;
        field.setVisible(expectedValue);
        field.createUI();
        field.setVisible(expectedValue);

        expectedValue = false;
        field.setVisible(expectedValue);
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.config.FieldConfigVendorOption#generateExpression()}. Test method for
     * {@link com.sldeditor.ui.detail.config.FieldConfigVendorOption#getStringValue()}. Test method
     * for {@link
     * com.sldeditor.ui.detail.config.FieldConfigVendorOption#populateExpression(java.lang.Object)}.
     */
    @Test
    public void testGenerateExpression() {
        boolean valueOnly = true;
        List<VendorOptionInterface> veList = null;
        FieldConfigVendorOption field =
                new FieldConfigVendorOption(
                        new FieldConfigCommonData(
                                Double.class, FieldIdEnum.NAME, "label", valueOnly),
                        veList);

        assertNull(field.getStringValue());
        field.populateExpression(null);
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.config.FieldConfigVendorOption#revertToDefaultValue()}.
     */
    @Test
    public void testRevertToDefaultValue() {
        List<VendorOptionInterface> veList = null;
        boolean valueOnly = true;
        FieldConfigVendorOption field =
                new FieldConfigVendorOption(
                        new FieldConfigCommonData(
                                Double.class, FieldIdEnum.NAME, "label", valueOnly),
                        veList);

        field.revertToDefaultValue();

        field.createUI();
        field.revertToDefaultValue();
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.config.FieldConfigVendorOption#createCopy(com.sldeditor.ui.detail.config.FieldConfigBase)}.
     */
    @Test
    public void testCreateCopy() {
        boolean valueOnly = true;

        class TestFieldConfigVendorOption extends FieldConfigVendorOption {
            public TestFieldConfigVendorOption(
                    FieldConfigCommonData commonData, List<VendorOptionInterface> veList) {
                super(commonData, veList);
            }

            public FieldConfigPopulate callCreateCopy(FieldConfigBase fieldConfigBase) {
                return createCopy(fieldConfigBase);
            }
        }

        TestFieldConfigVendorOption field =
                new TestFieldConfigVendorOption(
                        new FieldConfigCommonData(
                                Double.class, FieldIdEnum.NAME, "label", valueOnly),
                        null);
        FieldConfigVendorOption copy = (FieldConfigVendorOption) field.callCreateCopy(null);
        assertNull(copy);

        copy = (FieldConfigVendorOption) field.callCreateCopy(field);
        assertEquals(field.getFieldId(), copy.getFieldId());
        assertTrue(field.getLabel().compareTo(copy.getLabel()) == 0);
        assertEquals(field.isValueOnly(), copy.isValueOnly());
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.config.FieldConfigVendorOption#addToOptionBox(javax.swing.Box)}.
     */
    @Test
    public void testAddToOptionBox() {
        FieldConfigVendorOption field =
                new FieldConfigVendorOption(
                        new FieldConfigCommonData(Double.class, FieldIdEnum.NAME, "label", false),
                        null);
        field.addToOptionBox(null);
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.config.FieldConfigVendorOption#vendorOptionsUpdated(java.util.List)}.
     */
    @Test
    public void testVendorOptionsUpdated() {
        RasterSymbolizerDetails panel = new RasterSymbolizerDetails();
        VendorOptionRasterFactory vendorOptionRasterFactory =
                new VendorOptionRasterFactory(getClass(), panel);

        // CHECKSTYLE:OFF
        List<VendorOptionInterface> veList =
                vendorOptionRasterFactory.getVendorOptionList(
                        "com.sldeditor.ui.detail.vendor.geoserver.raster.VOGeoServerContrastEnhancementNormalizeOverall");
        // CHECKSTYLE:ON

        for (VendorOptionInterface extension : veList) {
            extension.setParentPanel(panel);
        }
        FieldConfigVendorOption field =
                new FieldConfigVendorOption(
                        new FieldConfigCommonData(Double.class, FieldIdEnum.NAME, "label", false),
                        veList);
        field.vendorOptionsUpdated(null);
        field.createUI();

        List<VersionData> vendorOptionVersionsList = new ArrayList<VersionData>();
        vendorOptionVersionsList.add(VersionData.getLatestVersion(GeoServerVendorOption.class));
        field.vendorOptionsUpdated(vendorOptionVersionsList);
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.config.FieldConfigVendorOption#attributeSelection(java.lang.String)}.
     */
    @Test
    public void testAttributeSelection() {
        boolean valueOnly = true;
        FieldConfigVendorOption field =
                new FieldConfigVendorOption(
                        new FieldConfigCommonData(
                                Double.class, FieldIdEnum.NAME, "label", valueOnly),
                        null);
        field.attributeSelection(null);

        field.createUI();
        field.createUI();
        assertTrue(field.isEnabled());
        field.attributeSelection("test");
        assertTrue(field.isEnabled());
        field.attributeSelection(null);
        assertTrue(field.isEnabled());
    }
}
