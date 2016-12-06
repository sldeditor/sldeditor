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
package com.sldeditor.common.preferences;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sldeditor.common.data.GeoServerConnection;
import com.sldeditor.common.filesystem.SelectedFiles;
import com.sldeditor.common.preferences.iface.PrefUpdateInterface;
import com.sldeditor.common.property.PropertyManagerInterface;
import com.sldeditor.common.undo.UndoActionInterface;
import com.sldeditor.common.undo.UndoEvent;
import com.sldeditor.common.undo.UndoInterface;
import com.sldeditor.common.undo.UndoManager;
import com.sldeditor.common.vendoroption.VendorOptionManager;
import com.sldeditor.common.vendoroption.VersionData;

/**
 * Class that manages user preference data.
 * <p>
 * Implemented as a singleton.
 * 
 * @author Robert Ward (SCISYS)
 */
public class PrefManager implements UndoActionInterface {

    /** The Constant USE_ANTI_ALIAS_FIELD. */
    private static final String USE_ANTI_ALIAS_FIELD = "SldEditor.useAntiAlias";

    /** The Constant VENDOROPTIONS_FIELD. */
    private static final String VENDOROPTIONS_FIELD = "SldEditor.vendorOptions";

    /** The Constant UILAYOUT_FIELD. */
    private static final String UILAYOUT_FIELD = "SldEditor.uilayout";

    /** The Constant BACKGROUND_COLOUR_FIELD. */
    private static final String BACKGROUND_COLOUR_FIELD = "SldEditor.backgroundColour";

    /** The Constant SAVE_LAST_FOLDER_VIEWED_FIELD. */
    private static final String SAVE_LAST_FOLDER_VIEWED_FIELD = "SldEditor.saveLastFolderViewed";

    /** The Constant LAST_FOLDER_VIEWED_FIELD. */
    private static final String LAST_FOLDER_VIEWED_FIELD = "SldEditor.lastFolderViewed";

    /** The Constant LAST_GEOSERVER_VIEWED_FIELD. */
    private static final String LAST_GEOSERVER_VIEWED_FIELD = "SldEditor.lastGeoServerViewed";

    /** The Constant CHECK_APP_VERSION_ON_STARTUP_FIELD. */
    private static final String CHECK_APP_VERSION_ON_STARTUP_FIELD = "SldEditor.checkAppVersionOnStartUp";

    /** The singleton instance. */
    private static PrefManager instance = null;

    /** The data. */
    private PrefData prefData = new PrefData();

    /** The listener list. */
    private List<PrefUpdateInterface> listenerList = new ArrayList<PrefUpdateInterface>();

    /** The old value obj. */
    private Object oldValueObj = null;

    /** The property manager. */
    private static PropertyManagerInterface propertyManagerInstance = null;

    /** The last viewed map. */
    private Map<PrefDataLastViewedEnum, String> lastViewedMap = null;

    /**
     * Gets the single instance of PrefManager.
     *
     * @return single instance of PrefManager
     */
    public static PrefManager getInstance() {
        if (instance == null) {
            instance = new PrefManager();
        }

        return instance;
    }

    /**
     * Destroy instance.
     */
    public static void destroyInstance() {
        instance = null;
    }

    /**
     * Private default constructor.
     */
    private PrefManager() {
        lastViewedMap = new HashMap<PrefDataLastViewedEnum, String>();
        lastViewedMap.put(PrefDataLastViewedEnum.FOLDER, LAST_FOLDER_VIEWED_FIELD);
        lastViewedMap.put(PrefDataLastViewedEnum.GEOSERVER, LAST_GEOSERVER_VIEWED_FIELD);
    }

    /**
     * Adds the listener.
     *
     * @param listener the listener
     */
    public void addListener(PrefUpdateInterface listener) {
        if (!listenerList.contains(listener)) {
            listenerList.add(listener);

            listener.useAntiAliasUpdated(this.prefData.isUseAntiAlias());
        }
    }

    /**
     * Sets the use anti alias.
     *
     * @param useAntiAlias the new use anti alias
     */
    private void setUseAntiAlias(boolean useAntiAlias) {
        if (this.prefData.isUseAntiAlias() != useAntiAlias) {
            this.prefData.setUseAntiAlias(useAntiAlias);

            if (propertyManagerInstance != null) {
                propertyManagerInstance.updateValue(USE_ANTI_ALIAS_FIELD, useAntiAlias);
            }

            for (PrefUpdateInterface listener : listenerList) {
                listener.useAntiAliasUpdated(useAntiAlias);
            }
        }
    }

    /**
     * Initialise.
     *
     * @param propertyManager the property manager
     */
    public static void initialise(PropertyManagerInterface propertyManager) {
        propertyManagerInstance = propertyManager;
        PrefManager.destroyInstance();

        if (propertyManager != null) {
            String uiLayoutClass = propertyManager.getStringValue(UILAYOUT_FIELD, null);
            PrefManager.getInstance().setUiLayoutClass(uiLayoutClass);
        }
    }

    /**
     * Finish.
     */
    public void finish() {
        if (propertyManagerInstance != null) {
            PrefData newPrefData = new PrefData();
            newPrefData.setCheckAppVersionOnStartUp(propertyManagerInstance
                    .getBooleanValue(CHECK_APP_VERSION_ON_STARTUP_FIELD, true));
            newPrefData.setUseAntiAlias(
                    propertyManagerInstance.getBooleanValue(USE_ANTI_ALIAS_FIELD, true));

            List<String> stringList = propertyManagerInstance
                    .getStringListValue(VENDOROPTIONS_FIELD);
            List<VersionData> vendorOptionVersionList = new ArrayList<VersionData>();

            if (stringList != null) {
                for (String string : stringList) {
                    VersionData versionData = VersionData.getDecodedString(string);

                    if (versionData != null) {
                        vendorOptionVersionList.add(versionData);
                    }
                }
            }

            VersionData defaultVendorOption = VendorOptionManager.getInstance()
                    .getDefaultVendorOptionVersionData();
            if (!vendorOptionVersionList.contains(defaultVendorOption)) {
                vendorOptionVersionList.add(defaultVendorOption);
            }
            newPrefData.setBackgroundColour(
                    propertyManagerInstance.getColourValue(BACKGROUND_COLOUR_FIELD, Color.WHITE));
            newPrefData.setSaveLastFolderView(
                    propertyManagerInstance.getBooleanValue(SAVE_LAST_FOLDER_VIEWED_FIELD, false));

            String folderName = propertyManagerInstance.getStringValue(LAST_FOLDER_VIEWED_FIELD,
                    null);
            if (folderName != null) {
                newPrefData.setLastViewedKey(PrefDataLastViewedEnum.FOLDER);
                newPrefData.setLastFolderViewed(folderName);
            }
            String geoServerConnection = propertyManagerInstance
                    .getStringValue(LAST_GEOSERVER_VIEWED_FIELD, null);
            if (geoServerConnection != null) {
                newPrefData.setLastViewedKey(PrefDataLastViewedEnum.GEOSERVER);
                newPrefData.setLastFolderViewed(geoServerConnection);
            }

            newPrefData
                    .setUiLayoutClass(propertyManagerInstance.getStringValue(UILAYOUT_FIELD, null));

            setPrefData(newPrefData);
        }
    }

    /**
     * Compares lists of objects.
     *
     * @param l1 the l1
     * @param l2 the l2
     * @return true, if successful
     */
    public static boolean cmpList(List<?> l1, List<?> l2) {
        if ((l1 == null) && (l2 == null)) {
            return true;
        }

        if ((l1 == null) || (l2 == null)) {
            return false;
        }

        // make a copy of the list so the original list is not changed, and remove() is supported
        ArrayList<?> cp = new ArrayList<>(l1);
        for (Object o : l2) {
            if (!cp.remove(o)) {
                return false;
            }
        }
        return cp.isEmpty();
    }

    /**
     * Sets the pref data.
     *
     * @param newPrefData the new pref data
     */
    public void setPrefData(PrefData newPrefData) {
        oldValueObj = prefData.clone();

        setUseAntiAlias(newPrefData.isUseAntiAlias());
        setUiLayoutClass(newPrefData.getUiLayoutClass());
        setBackgroundColour(newPrefData.getBackgroundColour());
        setLastFolderViewed(newPrefData.isSaveLastFolderView(), newPrefData.getLastViewedKey(),
                newPrefData.getLastFolderViewed());
        setCheckAppVersionOnStartUp(newPrefData.isCheckAppVersionOnStartUp());

        UndoManager.getInstance()
                .addUndoEvent(new UndoEvent(this, "Preferences", oldValueObj, newPrefData));
    }

    /**
     * Sets the check app version on start up.
     *
     * @param checkAppVersionOnStartUp the new check app version on start up
     */
    private void setCheckAppVersionOnStartUp(boolean checkAppVersionOnStartUp) {
        if (this.prefData.isCheckAppVersionOnStartUp() != checkAppVersionOnStartUp) {
            this.prefData.setCheckAppVersionOnStartUp(checkAppVersionOnStartUp);

            if (propertyManagerInstance != null) {
                propertyManagerInstance.updateValue(CHECK_APP_VERSION_ON_STARTUP_FIELD,
                        checkAppVersionOnStartUp);
            }
        }
    }

    /**
     * Sets the last folder viewed.
     *
     * @param selectedFiles the new last folder viewed
     */
    public void setLastFolderViewed(SelectedFiles selectedFiles) {
        String lastViewed = null;
        PrefDataLastViewedEnum key = PrefDataLastViewedEnum.FOLDER;

        if (selectedFiles != null) {
            if (selectedFiles.getFolderName() != null) {
                lastViewed = selectedFiles.getFolderName();
                key = PrefDataLastViewedEnum.FOLDER;
            } else if (selectedFiles.getConnectionData() != null) {
                GeoServerConnection connectData = selectedFiles.getConnectionData();

                lastViewed = connectData.getConnectionName();
                key = PrefDataLastViewedEnum.GEOSERVER;
            }
        }

        setLastFolderViewed(this.prefData.isSaveLastFolderView(), key, lastViewed);
    }

    /**
     * Sets the last folder viewed.
     *
     * @param saveLastFolderViewed the save last folder viewed
     * @param key the key
     * @param lastFolderViewed the new last folder viewed
     */
    private void setLastFolderViewed(boolean saveLastFolderViewed, PrefDataLastViewedEnum key,
            String lastFolderViewed) {

        boolean different = (this.prefData.isSaveLastFolderView() != saveLastFolderViewed)
                || ((this.prefData.getLastViewedKey() != key));

        if (!different) {
            if ((this.prefData.getLastFolderViewed() == null) || (lastFolderViewed == null)) {
                different = !((this.prefData.getLastFolderViewed() == null)
                        && (lastFolderViewed == null));
            } else if ((this.prefData.getLastFolderViewed() != null)
                    && (lastFolderViewed != null)) {
                different = (this.prefData.getLastFolderViewed().compareTo(lastFolderViewed) != 0);
            }
        }

        this.prefData.setSaveLastFolderView(saveLastFolderViewed);
        if ((key != null) && (lastFolderViewed != null)) {
            this.prefData.setLastViewedKey(key);
            this.prefData.setLastFolderViewed(lastFolderViewed);
        }

        if (different) {
            updateLastFolderViewed();
        }
    }

    /**
     * Update last folder viewed.
     */
    private void updateLastFolderViewed() {
        propertyManagerInstance.clearValue(LAST_FOLDER_VIEWED_FIELD, false);
        propertyManagerInstance.clearValue(LAST_GEOSERVER_VIEWED_FIELD, false);

        propertyManagerInstance.updateValue(SAVE_LAST_FOLDER_VIEWED_FIELD,
                this.prefData.isSaveLastFolderView());
        if (this.prefData.isSaveLastFolderView()) {
            String key = lastViewedMap.get(this.prefData.getLastViewedKey());
            propertyManagerInstance.updateValue(key, this.prefData.getLastFolderViewed());
        }
    }

    /**
     * Sets the background colour.
     *
     * @param backgroundColour the new background colour
     */
    private void setBackgroundColour(Color backgroundColour) {
        boolean different = false;
        if (this.prefData.getBackgroundColour() == null) {
            different = (backgroundColour != null);
        } else {
            if (backgroundColour == null) {
                different = true;
            } else {
                different = (this.prefData.getBackgroundColour().getRGB() != backgroundColour
                        .getRGB());
            }
        }

        if (different) {
            this.prefData.setBackgroundColour(backgroundColour);

            if (propertyManagerInstance != null) {
                propertyManagerInstance.updateValue(BACKGROUND_COLOUR_FIELD, backgroundColour);
            }

            for (PrefUpdateInterface listener : listenerList) {
                listener.backgroundColourUpdate(backgroundColour);
            }
        }
    }

    /**
     * Sets the ui layout class.
     *
     * @param uiLayoutClassName the new ui layout class
     */
    private void setUiLayoutClass(String uiLayoutClassName) {
        boolean different = false;
        if (this.prefData.getUiLayoutClass() == null) {
            different = (uiLayoutClassName != null);
        } else {
            if (uiLayoutClassName == null) {
                different = true;
            } else {
                different = (this.prefData.getUiLayoutClass().compareTo(uiLayoutClassName) != 0);
            }
        }

        if (different) {
            this.prefData.setUiLayoutClass(uiLayoutClassName);

            if (propertyManagerInstance != null) {
                propertyManagerInstance.updateValue(UILAYOUT_FIELD, uiLayoutClassName);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.undo.UndoActionInterface#undoAction(com.sldeditor.undo.UndoInterface)
     */
    @Override
    public void undoAction(UndoInterface undoRedoObject) {
        if (undoRedoObject != null) {
            if (undoRedoObject.getOldValue() instanceof PrefData) {
                PrefData prefData = (PrefData) undoRedoObject.getOldValue();

                setPrefData(prefData);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.undo.UndoActionInterface#redoAction(com.sldeditor.undo.UndoInterface)
     */
    @Override
    public void redoAction(UndoInterface undoRedoObject) {
        if (undoRedoObject != null) {
            if (undoRedoObject.getNewValue() instanceof PrefData) {
                PrefData prefData = (PrefData) undoRedoObject.getNewValue();

                setPrefData(prefData);
            }
        }
    }

    /**
     * Gets the pref data.
     *
     * @return the pref data
     */
    public PrefData getPrefData() {
        return prefData.clone();
    }
}
