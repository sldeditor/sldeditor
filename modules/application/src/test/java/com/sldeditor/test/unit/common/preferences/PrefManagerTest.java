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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.sldeditor.common.data.GeoServerConnection;
import com.sldeditor.common.filesystem.SelectedFiles;
import com.sldeditor.common.preferences.PrefData;
import com.sldeditor.common.preferences.PrefDataLastViewedEnum;
import com.sldeditor.common.preferences.PrefManager;
import com.sldeditor.common.property.PropertyManagerInterface;
import com.sldeditor.common.undo.UndoEvent;
import com.sldeditor.common.undo.UndoManager;
import com.sldeditor.common.vendoroption.GeoServerVendorOption;
import com.sldeditor.common.vendoroption.VendorOptionUpdateInterface;
import com.sldeditor.common.vendoroption.VendorOptionManager;
import com.sldeditor.common.vendoroption.VersionData;
import com.sldeditor.common.xml.ui.FieldIdEnum;

/**
 * Unit test for PrefManager.
 * <p>{@link com.sldeditor.common.preferences.PrefManager}
 * 
 * @author Robert Ward (SCISYS)
 */
public class PrefManagerTest {

    class DummyPropertyManager implements PropertyManagerInterface
    {
        public Map<String, String> fieldValueMap = new HashMap<String, String>();

        @Override
        public void updateValue(String key, String value) {
            fieldValueMap.put(key, value);
        }

        @Override
        public void updateValue(String key, boolean value) {
            fieldValueMap.put(key, String.valueOf(value));
        }

        @Override
        public void readConfig() {
        }

        @Override
        public double getDoubleValue(String field, double defaultValue) {
            if(fieldValueMap.containsKey(field))
            {
                try
                {
                    return Double.valueOf(fieldValueMap.get(field));
                }
                catch(NumberFormatException e)
                {
                    return defaultValue;
                }
            }
            else
            {
                return defaultValue;
            }
        }

        @Override
        public String getStringValue(String field, String defaultValue) {
            if(fieldValueMap.containsKey(field))
            {
                String value = fieldValueMap.get(field);

                if(value != null)
                {
                    return value;
                }
            }

            return defaultValue;
        }

        @Override
        public Color getColourValue(String field, Color defaultValue) {
            if(fieldValueMap.containsKey(field))
            {
                String value = fieldValueMap.get(field);

                String[] components = value.split("\\.");

                if(components.length == 4)
                {
                    int red = Integer.valueOf(components[0]);
                    int green = Integer.valueOf(components[1]);
                    int blue = Integer.valueOf(components[2]);
                    int alpha = Integer.valueOf(components[3]);

                    return new Color(red, green, blue, alpha);
                }
            }

            return defaultValue;
        }

        @Override
        public boolean getBooleanValue(String field, boolean defaultValue) {
            if(fieldValueMap.containsKey(field))
            {
                String value = fieldValueMap.get(field);

                if(value != null)
                {
                    return (value.compareToIgnoreCase("true") == 0);
                }
            }

            return defaultValue;
        }

        @Override
        public List<String> getStringListValue(String field) {
            List<String> valueList = null;

            if(fieldValueMap.containsKey(field))
            {
                String value = fieldValueMap.get(field);

                if(value != null)
                {
                    String[] components = value.split(",");

                    valueList = Arrays.asList(components);
                }
            }

            return valueList;
        }

        @Override
        public void updateValue(String key, List<String> stringList) {
            StringBuilder sb = new StringBuilder();

            for(String string : stringList)
            {
                if(sb.length() > 0)
                {
                    sb.append(",");
                }
                sb.append(string);
            }

            updateValue(key, sb.toString());
        }

        @Override
        public void updateValue(String key, int count, String value) {
            String updatedKey = String.format("%s%s%d", key, '.', count);

            updateValue(updatedKey, value);
        }

        @Override
        public List<String> getMultipleValues(String key) {
            return null;
        }

        @Override
        public void updateValue(String key, Color backgroundColour) {
            String value = String.format("%03d%s%03d%s%03d%s%03d", 
                    backgroundColour.getRed(),
                    '.',
                    backgroundColour.getGreen(), 
                    '.',
                    backgroundColour.getBlue(),
                    '.',
                    backgroundColour.getAlpha());

            updateValue(key, value);
        }

        @Override
        public void clearValue(String key, boolean useDelimeter) {
            StringBuilder sb = new StringBuilder();
            sb.append(key);
            if(useDelimeter)
            {
                sb.append(".");
            }

            String prefix = sb.toString();

            List<String> keyToRemove = new ArrayList<String>();
            for(String existingKey : fieldValueMap.keySet())
            {
                if(existingKey.startsWith(prefix))
                {
                    keyToRemove.add(existingKey);
                }
            }

            for(String existingKey : keyToRemove)
            {
                fieldValueMap.remove(existingKey);
            }
        }

        /* (non-Javadoc)
         * @see com.sldeditor.common.property.PropertyManagerInterface#setPropertyFile(java.io.File)
         */
        @Override
        public void setPropertyFile(File configPropertiesFile) {
        }
    }

    class DummyVendorOptionChanged implements VendorOptionUpdateInterface
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

        String uiLayoutClass = "perfect curve";

        prefData.setUiLayoutClass(uiLayoutClass);

        PrefManager.getInstance().setPrefData(prefData);

        assertEquals(backgroundColour, PrefManager.getInstance().getPrefData().getBackgroundColour());
        assertEquals(uiLayoutClass, PrefManager.getInstance().getPrefData().getUiLayoutClass());
        assertEquals(true, PrefManager.getInstance().getPrefData().isUseAntiAlias());
    }

    /**
     * Test method for {@link com.sldeditor.common.preferences.PrefManager#initialise(com.sldeditor.common.property.PropertyManagerInterface)}.
     */
    @Test
    public void testInitialise() {
        PrefManager.initialise(null);

        DummyPropertyManager propertyManager = new DummyPropertyManager();
        PrefManager.initialise(propertyManager);

        PrefData prefData = new PrefData();
        prefData.setUseAntiAlias(true);
        Color backgroundColour = Color.GRAY;
        prefData.setBackgroundColour(backgroundColour);
        String uiLayoutClass = "perfect curve";

        prefData.setUiLayoutClass(uiLayoutClass);

        PrefManager.getInstance().setPrefData(prefData);

        Color newBackgroundColour = Color.RED;
        prefData.setBackgroundColour(newBackgroundColour);
        PrefManager.getInstance().setPrefData(prefData);
        assertEquals(newBackgroundColour, propertyManager.getColourValue("SldEditor.backgroundColour", null));

        boolean newAntiAlias = false;
        prefData.setUseAntiAlias(newAntiAlias);
        PrefManager.getInstance().setPrefData(prefData);
        assertEquals(newAntiAlias, propertyManager.getBooleanValue("SldEditor.useAntiAlias", true));

        String newLayoutClass = "bendy line";
        prefData.setUiLayoutClass(newLayoutClass);
        PrefManager.getInstance().setPrefData(prefData);
        assertEquals(newLayoutClass, propertyManager.getStringValue("SldEditor.uilayout", null));

        List<VersionData> newVendorOptionList = new ArrayList<VersionData>();
        newVendorOptionList.add(VendorOptionManager.getInstance().getDefaultVendorOptionVersionData());

        PrefManager.getInstance().setPrefData(prefData);

        List<String> encodelist = new ArrayList<String>();
        for(VersionData versionData : newVendorOptionList)
        {
            encodelist.add(versionData.getEncodedString());
        }
        PrefManager.initialise(null);
    }

    /**
     * Test method for {@link com.sldeditor.common.preferences.PrefManager#setLastFolderViewed()}.
     */
    @Test
    public void testSetLastFolderViewed() {
        PrefManager.initialise(null);

        DummyPropertyManager propertyManager = new DummyPropertyManager();
        PrefManager.initialise(propertyManager);

        // Try with a null argument
        PrefManager.getInstance().setLastFolderViewed(null);

        PrefData actualPrefData = PrefManager.getInstance().getPrefData();
        assertFalse(actualPrefData.isSaveLastFolderView());
        assertEquals(PrefDataLastViewedEnum.FOLDER, actualPrefData.getLastViewedKey());
        assertNull(actualPrefData.getLastFolderViewed());

        // Try with an empty selected files object
        SelectedFiles selectedFiles = new SelectedFiles();
        PrefManager.getInstance().setLastFolderViewed(selectedFiles);
        actualPrefData = PrefManager.getInstance().getPrefData();
        assertFalse(actualPrefData.isSaveLastFolderView());
        assertEquals(PrefDataLastViewedEnum.FOLDER, actualPrefData.getLastViewedKey());
        assertNull(actualPrefData.getLastFolderViewed());

        // Set folder
        String expectedFolderName = "/tmp/test/abc";
        selectedFiles.setFolderName(expectedFolderName);
        PrefManager.getInstance().setLastFolderViewed(selectedFiles);
        actualPrefData = PrefManager.getInstance().getPrefData();
        assertFalse(actualPrefData.isSaveLastFolderView());
        assertEquals(PrefDataLastViewedEnum.FOLDER, actualPrefData.getLastViewedKey());
        assertEquals(expectedFolderName, actualPrefData.getLastFolderViewed());

        expectedFolderName = "c:/tmp/test/abc";
        selectedFiles.setFolderName(expectedFolderName);
        PrefManager.getInstance().setLastFolderViewed(selectedFiles);
        actualPrefData = PrefManager.getInstance().getPrefData();
        assertFalse(actualPrefData.isSaveLastFolderView());
        assertEquals(PrefDataLastViewedEnum.FOLDER, actualPrefData.getLastViewedKey());
        assertEquals(expectedFolderName, actualPrefData.getLastFolderViewed());

        actualPrefData.setSaveLastFolderView(true);
        PrefManager.getInstance().setPrefData(actualPrefData);
        actualPrefData = PrefManager.getInstance().getPrefData();
        assertTrue(actualPrefData.isSaveLastFolderView());

        // Set geoserver
        String expectedGeoServerName = "GeoServer test";
        GeoServerConnection connectionData = new GeoServerConnection();
        connectionData.setConnectionName(expectedGeoServerName);
        selectedFiles = new SelectedFiles();
        selectedFiles.setConnectionData(connectionData);
        PrefManager.getInstance().setLastFolderViewed(selectedFiles);
        actualPrefData = PrefManager.getInstance().getPrefData();
        assertTrue(actualPrefData.isSaveLastFolderView());
        assertEquals(PrefDataLastViewedEnum.GEOSERVER, actualPrefData.getLastViewedKey());
        assertEquals(expectedGeoServerName, actualPrefData.getLastFolderViewed());
    }

    /**
     * Test method for {@link com.sldeditor.common.preferences.PrefManager#finish()}.
     */
    @Test
    public void testFinish() {
        PrefManager.initialise(null);
        PrefManager.getInstance().finish();

        DummyPropertyManager propertyManager = new DummyPropertyManager();
        PrefManager.initialise(propertyManager);

        // Set up data as if it had been read in
        PrefData prefData = new PrefData();
        prefData.setUseAntiAlias(true);
        Color backgroundColour = Color.GRAY;
        prefData.setBackgroundColour(backgroundColour);
        List<VersionData> vendorOptionList = new ArrayList<VersionData>();
        vendorOptionList.add(VendorOptionManager.getInstance().getDefaultVendorOptionVersionData());
        vendorOptionList.add(VersionData.getEarliestVersion(GeoServerVendorOption.class));

        String uiLayoutClass = "perfect curve";
        prefData.setUiLayoutClass(uiLayoutClass);
        String lastFolderViewed = "secret";
        prefData.setLastFolderViewed(lastFolderViewed);
        prefData.setLastViewedKey(PrefDataLastViewedEnum.FOLDER);
        prefData.setSaveLastFolderView(true);

        PrefManager.getInstance().setPrefData(prefData);
        PrefManager.getInstance().finish();

        // Try GeoServer as last item viewed
        prefData.setLastViewedKey(PrefDataLastViewedEnum.GEOSERVER);
        PrefManager.getInstance().setPrefData(prefData);
        PrefManager.getInstance().finish();
    }

    /**
     * Test method for {@link com.sldeditor.common.preferences.PrefManager#undoAction(com.sldeditor.common.undo.UndoInterface)}.
     * Test method for {@link com.sldeditor.common.preferences.PrefManager#redoAction(com.sldeditor.common.undo.UndoInterface)}.
     */
    @Test
    public void testUndoAction() {
        PrefManager.initialise(null);
        PrefManager.getInstance().finish();

        DummyPropertyManager propertyManager = new DummyPropertyManager();
        PrefManager.initialise(propertyManager);

        // Set up data as if it had been read in
        PrefData prefData = new PrefData();
        prefData.setUseAntiAlias(true);
        Color backgroundColour = Color.GRAY;
        prefData.setBackgroundColour(backgroundColour);
        List<VersionData> vendorOptionList = new ArrayList<VersionData>();
        vendorOptionList.add(VendorOptionManager.getInstance().getDefaultVendorOptionVersionData());
        vendorOptionList.add(VersionData.getEarliestVersion(GeoServerVendorOption.class));

        String uiLayoutClass = "perfect curve";
        prefData.setUiLayoutClass(uiLayoutClass);
        String lastFolderViewed = "secret";
        prefData.setLastFolderViewed(lastFolderViewed);
        prefData.setLastViewedKey(PrefDataLastViewedEnum.FOLDER);
        prefData.setSaveLastFolderView(true);

        PrefManager.getInstance().setPrefData(prefData);

        PrefData copy = prefData.clone();
        copy.setLastViewedKey(PrefDataLastViewedEnum.GEOSERVER);
        PrefManager.getInstance().setPrefData(copy);
        
        PrefData actual = PrefManager.getInstance().getPrefData();
        assertEquals(PrefDataLastViewedEnum.GEOSERVER, actual.getLastViewedKey());
        UndoManager.getInstance().undo();
        
        actual = PrefManager.getInstance().getPrefData();
        assertEquals(PrefDataLastViewedEnum.FOLDER, actual.getLastViewedKey());

        UndoManager.getInstance().redo();
        actual = PrefManager.getInstance().getPrefData();
        assertEquals(PrefDataLastViewedEnum.GEOSERVER, actual.getLastViewedKey());

        // Increase the code coverage
        PrefManager.getInstance().undoAction(null);
        PrefManager.getInstance().undoAction(new UndoEvent(null, FieldIdEnum.NAME, "", "new"));
        PrefManager.getInstance().redoAction(null);
        PrefManager.getInstance().redoAction(new UndoEvent(null, FieldIdEnum.NAME, "", "new"));    }


}
