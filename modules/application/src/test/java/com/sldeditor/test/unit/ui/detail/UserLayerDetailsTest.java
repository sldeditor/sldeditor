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

package com.sldeditor.test.unit.ui.detail;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.geotools.styling.StyledLayerDescriptor;
import org.geotools.styling.UserLayer;
import org.junit.Test;

import com.sldeditor.common.data.SelectedSymbol;
import com.sldeditor.common.defaultsymbol.DefaultSymbols;
import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.ui.detail.GraphicPanelFieldManager;
import com.sldeditor.ui.detail.UserLayerDetails;
import com.sldeditor.ui.detail.config.FieldConfigString;

/**
 * The unit test for UserLayerDetails.
 * <p>{@link com.sldeditor.ui.detail.UserLayerDetails}
 *
 * @author Robert Ward (SCISYS)
 */
public class UserLayerDetailsTest {

    /**
     * Test method for {@link com.sldeditor.ui.detail.UserLayerDetails#UserLayerDetails(com.sldeditor.filter.v2.function.FunctionNameInterface)}.
     * Test method for {@link com.sldeditor.ui.detail.UserLayerDetails#populate(com.sldeditor.common.data.SelectedSymbol)}.
     * Test method for {@link com.sldeditor.ui.detail.UserLayerDetails#dataChanged(com.sldeditor.ui.detail.config.FieldId)}.
     * Test method for {@link com.sldeditor.ui.detail.UserLayerDetails#getFieldDataManager()}.
     * Test method for {@link com.sldeditor.ui.detail.UserLayerDetails#isDataPresent()}.
     * Test method for {@link com.sldeditor.ui.detail.UserLayerDetails#preLoadSymbol()}.
     */
    @Test
    public void testUserLayerDetails() {
        UserLayerDetails panel = new UserLayerDetails(null);
        panel.populate(null);

        // Set up test data
        StyledLayerDescriptor sld = DefaultSymbols.createNewSLD();
        SelectedSymbol.getInstance().createNewSLD(sld);
        UserLayer userLayer = DefaultSymbols.createNewUserLayer();
        String expectedUserValue = "user layer test value";
        userLayer.setName(expectedUserValue);
        userLayer.addUserStyle(DefaultSymbols.createNewStyle());
        sld.layers().add(userLayer);
        SelectedSymbol.getInstance().addNewStyledLayer(userLayer);
        SelectedSymbol.getInstance().setStyledLayer(userLayer);

        panel.populate(SelectedSymbol.getInstance());
        panel.dataChanged(null);
        GraphicPanelFieldManager fieldDataManager = panel.getFieldDataManager();
        assertNotNull(fieldDataManager);

        panel.dataChanged(FieldIdEnum.INLINE_FEATURE);

        FieldConfigString nameField = (FieldConfigString) fieldDataManager.get(FieldIdEnum.NAME);
        assertTrue(expectedUserValue.compareTo(nameField.getStringValue()) == 0);
        assertTrue(panel.isDataPresent());

        // Reset to default value
        panel.preLoadSymbol();
        assertTrue("".compareTo(nameField.getStringValue()) == 0);
    }
}
