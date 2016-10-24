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
package com.sldeditor.test.unit.common.preferences;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.sldeditor.common.preferences.PrefData;
import com.sldeditor.common.preferences.PrefManager;
import com.sldeditor.common.preferences.iface.PrefUpdateVendorOptionInterface;
import com.sldeditor.common.property.PropertyManagerInterface;
import com.sldeditor.common.vendoroption.GeoServerVendorOption;
import com.sldeditor.common.vendoroption.VendorOptionManager;
import com.sldeditor.common.vendoroption.VersionData;

/**
 * Unit test for PrefManager.
 * <p>{@link com.sldeditor.common.preferences.PrefManager}
 * 
 * @author Robert Ward (SCISYS)
 */
public class PrefManagerTest {

    class DummyPropertyManager implements PropertyManagerInterface
    {
        public String lastKey = null;
        public Object lastValue = null;

        @Override
        public void updateValue(String key, String value) {
            lastKey = key;
            lastValue = value;
        }

        @Override
        public void updateValue(String key, boolean value) {
            lastKey = key;
            lastValue = value;
        }

        @Override
        public void readConfig() {
        }

        @Override
        public double getDoubleValue(String field, double defaultValue) {
            return 0.0;
        }

        @Override
        public String getStringValue(String field, String defaultValue) {
            return null;
        }

        @Override
        public Color getColourValue(String field, Color defaultValue) {
            return null;
        }

        @Override
        public boolean getBooleanValue(String field, boolean defaultValue) {
            return false;
        }

        @Override
        public List<String> getStringListValue(String field) {
            return null;
        }

        @Override
        public void updateValue(String key, List<String> stringList) {
            lastKey = key;
            lastValue = stringList;
        }

        @Override
        public void updateValue(String key, int count, String value) {
            lastKey = key;
            lastValue = value;
        }

        @Override
        public List<String> getMultipleValues(String key) {
            return null;
        }

        @Override
        public void updateValue(String key, Color backgroundColour) {
            lastKey = key;
            lastValue = backgroundColour;
        }

    }

    class DummyVendorOptionChanged implements PrefUpdateVendorOptionInterface
    {
        public List<VersionData> vendorOptionVersionsList = null;

        @Override
        public void vendorOptionsUpdated(List<VersionData> vendorOptionVersionsList) {
            this.vendorOptionVersionsList = vendorOptionVersionsList;
        }

    }

    /**
     * Test method for {@link com.sldeditor.common.preferences.PrefManager#useAntiAlias()}.
     */
    @Test
    public void testPrefData() {
        PrefData prefData = new PrefData();
        prefData.setUseAntiAlias(true);
        Color backgroundColour = Color.GRAY;
        prefData.setBackgroundColour(backgroundColour);
        List<VersionData> vendorOptionList = new ArrayList<VersionData>();
        vendorOptionList.add(VendorOptionManager.getInstance().getDefaultVendorOptionVersionData());

        prefData.setVendorOptionVersionList(vendorOptionList);
        String uiLayoutClass = "perfect curve";

        prefData.setUiLayoutClass(uiLayoutClass);

        DummyVendorOptionChanged voListener = new DummyVendorOptionChanged();

        PrefManager.getInstance().addVendorOptionListener(voListener);

        PrefManager.getInstance().setPrefData(prefData);

        assertEquals(backgroundColour, PrefManager.getInstance().getPrefData().getBackgroundColour());
        assertEquals(uiLayoutClass, PrefManager.getInstance().getPrefData().getUiLayoutClass());
        assertEquals(true, PrefManager.getInstance().getPrefData().isUseAntiAlias());
        assertEquals(vendorOptionList, voListener.vendorOptionVersionsList);
    }

    /**
     * Test method for {@link com.sldeditor.common.preferences.PrefManager#initialise(com.sldeditor.common.property.PropertyManagerInterface)}.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testInitialise() {
        PrefManager.initialise(null);

        DummyPropertyManager propertyManager = new DummyPropertyManager();
        PrefManager.initialise(propertyManager);

        PrefData prefData = new PrefData();
        prefData.setUseAntiAlias(true);
        Color backgroundColour = Color.GRAY;
        prefData.setBackgroundColour(backgroundColour);
        List<VersionData> vendorOptionList = new ArrayList<VersionData>();
        vendorOptionList.add(VendorOptionManager.getInstance().getDefaultVendorOptionVersionData());

        prefData.setVendorOptionVersionList(vendorOptionList);
        String uiLayoutClass = "perfect curve";

        prefData.setUiLayoutClass(uiLayoutClass);

        PrefManager.getInstance().setPrefData(prefData);

        Color newBackgroundColour = Color.RED;
        prefData.setBackgroundColour(newBackgroundColour);
        propertyManager.lastValue = null;
        PrefManager.getInstance().setPrefData(prefData);
        assertEquals(newBackgroundColour, propertyManager.lastValue);
        propertyManager.lastValue = null;
        PrefManager.getInstance().setPrefData(prefData);
        assertNull(propertyManager.lastValue);

        boolean newAntiAlias = false;
        prefData.setUseAntiAlias(newAntiAlias);
        propertyManager.lastValue = null;
        PrefManager.getInstance().setPrefData(prefData);
        assertEquals(newAntiAlias, propertyManager.lastValue);
        propertyManager.lastValue = null;
        PrefManager.getInstance().setPrefData(prefData);
        assertNull(propertyManager.lastValue);

        String newLayoutClass = "bendy line";
        prefData.setUiLayoutClass(newLayoutClass);
        propertyManager.lastValue = null;
        PrefManager.getInstance().setPrefData(prefData);
        assertEquals(newLayoutClass, propertyManager.lastValue);
        propertyManager.lastValue = null;
        PrefManager.getInstance().setPrefData(prefData);
        assertNull(propertyManager.lastValue);

        List<VersionData> newVendorOptionList = new ArrayList<VersionData>();
        newVendorOptionList.add(VendorOptionManager.getInstance().getDefaultVendorOptionVersionData());

        prefData.setVendorOptionVersionList(newVendorOptionList);
        propertyManager.lastValue = null;
        PrefManager.getInstance().setPrefData(prefData);
        assertNull(propertyManager.lastValue);
        newVendorOptionList.add(VersionData.getLatestVersion(GeoServerVendorOption.class));
        prefData.setVendorOptionVersionList(newVendorOptionList);
        PrefManager.getInstance().setPrefData(prefData);
        
        List<String> encodelist = new ArrayList<String>();
        for(VersionData versionData : newVendorOptionList)
        {
            encodelist.add(versionData.getEncodedString());
        }
        assertTrue(PrefManager.cmpList(encodelist, (List<String>)propertyManager.lastValue));

        PrefManager.initialise(null);
    }

    /**
     * Test method for {@link com.sldeditor.common.preferences.PrefManager#finish()}.
     */
    @Test
    public void testFinish() {
    }

    /**
     * Test method for {@link com.sldeditor.common.preferences.PrefManager#undoAction(com.sldeditor.common.undo.UndoInterface)}.
     */
    @Test
    public void testUndoAction() {
    }

    /**
     * Test method for {@link com.sldeditor.common.preferences.PrefManager#redoAction(com.sldeditor.common.undo.UndoInterface)}.
     */
    @Test
    public void testRedoAction() {
    }

}
