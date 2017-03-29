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

package com.sldeditor.ui.detail.vendor.geoserver.fill;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.RasterSymbolizer;
import org.geotools.styling.TextSymbolizer;
import org.geotools.styling.TextSymbolizer.PolygonAlignOptions;

import com.sldeditor.common.console.ConsoleManager;
import com.sldeditor.common.data.SelectedSymbol;
import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.common.vendoroption.VendorOptionVersion;
import com.sldeditor.common.vendoroption.info.VendorOptionInfo;
import com.sldeditor.common.vendoroption.minversion.VendorOptionPresent;
import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.ui.detail.GraphicPanelFieldManager;
import com.sldeditor.ui.detail.StandardPanel;
import com.sldeditor.ui.detail.config.FieldConfigBase;
import com.sldeditor.ui.detail.config.FieldConfigBoolean;
import com.sldeditor.ui.detail.config.FieldConfigDouble;
import com.sldeditor.ui.detail.config.FieldConfigEnum;
import com.sldeditor.ui.detail.config.FieldConfigInteger;
import com.sldeditor.ui.detail.vendor.geoserver.VendorOptionInterface;
import com.sldeditor.ui.iface.PopulateDetailsInterface;
import com.sldeditor.ui.iface.UpdateSymbolInterface;
import com.sldeditor.ui.widgets.ValueComboBoxData;

/**
 * Class to handle the getting and setting of GeoServer random fill vendor option data.
 * 
 * @author Robert Ward (SCISYS)
 */
public class VOGeoServerRandomFill extends StandardPanel
        implements VendorOptionInterface, PopulateDetailsInterface, UpdateSymbolInterface {

    /** The Constant PANEL_CONFIG. */
    private static final String PANEL_CONFIG = "symbol/fill/PanelConfig_GeoServerRandomFill.xml";

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The field map. */
    private Map<FieldIdEnum, String> fieldMap = new HashMap<FieldIdEnum, String>();

    /** The parent obj. */
    private UpdateSymbolInterface parentObj = null;

    private VendorOptionInfo vendorOptionInfo;

    /**
     * Instantiates a new VOGeoServerRandomFill class.
     *
     * @param panelId the panel id
     */
    public VOGeoServerRandomFill(Class<?> panelId) {
        super(panelId);

        fieldMap.put(FieldIdEnum.RANDOM_FILL_ACTIVATE, "random");
        fieldMap.put(FieldIdEnum.RANDOM_FILL_TILE_SIZE, "random-tile-size");
        fieldMap.put(FieldIdEnum.RANDOM_FILL_ROTATION, "random-rotation");
        fieldMap.put(FieldIdEnum.RANDOM_FILL_SYMBOL_COUNT, "random-symbol-count");
        fieldMap.put(FieldIdEnum.RANDOM_FILL_RANDOM_SEED, "random-seed");

        createUI();
    }

    /**
     * Creates the ui.
     */
    private void createUI() {
        readConfigFileNoScrollPane(null, getPanelId(), this, PANEL_CONFIG);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.ui.detail.vendor.geoserver.VendorOptionInterface#getVendorOption()
     */
    @Override
    public VendorOptionVersion getVendorOption() {
        return getVendorOptionVersion();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.sldeditor.ui.iface.UpdateSymbolInterface#dataChanged(com.sldeditor.ui.detail.config.xml.
     * FieldId)
     */
    @Override
    public void dataChanged(FieldIdEnum changedField) {
        handleFieldState(changedField);

        if (parentObj != null) {
            parentObj.dataChanged(changedField);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.sldeditor.ui.detail.vendor.geoserver.VendorOptionInterface#populate(org.geotools.styling.
     * RasterSymbolizer)
     */
    @Override
    public void populate(RasterSymbolizer rasterSymbolizer) {
        // Do nothing
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.ui.iface.PopulateDetailsInterface#populate(com.sldeditor.ui.detail.
     * selectedsymbol.SelectedSymbol)
     */
    @Override
    public void populate(SelectedSymbol selectedSymbol) {
        // Do nothing
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.sldeditor.ui.detail.vendor.geoserver.VendorOptionInterface#populate(org.geotools.styling.
     * TextSymbolizer)
     */
    @Override
    public void populate(TextSymbolizer textSymbolizer) {
        // Do nothing
    }

    /**
     * Populate.
     *
     * @param polygonSymbolizer the polygon symbolizer
     */
    @Override
    public void populate(PolygonSymbolizer polygonSymbolizer) {
        Map<String, String> options = null;

        if (polygonSymbolizer != null) {
            options = polygonSymbolizer.getOptions();
        }

        for (FieldIdEnum key : fieldMap.keySet()) {
            internalPopulate(options, key, fieldMap.get(key));
        }

        handleFieldState();
    }

    /**
     * Gets the field data manager.
     *
     * @return the field data manager
     */
    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.ui.iface.PopulateDetailsInterface#getFieldDataManager()
     */
    @Override
    public GraphicPanelFieldManager getFieldDataManager() {
        return this.fieldConfigManager;
    }

    /**
     * Internal populate.
     *
     * @param options the options
     * @param field the field
     * @param key the key
     */
    protected void internalPopulate(Map<String, String> options, FieldIdEnum field, String key) {
        FieldConfigBase fieldConfig = fieldConfigManager.get(field);

        if (fieldConfig instanceof FieldConfigBoolean) {
            internal_populateBooleanField(options, field, key);
        } else if (fieldConfig instanceof FieldConfigInteger) {
            internal_populateIntegerField(options, field, key);
        } else if (fieldConfig instanceof FieldConfigDouble) {
            internal_populateDoubleField(options, field, key);
        } else if (fieldConfig instanceof FieldConfigEnum) {
            internal_populateEnumField(options, field, key);
        } else {
            ConsoleManager.getInstance().error(this,
                    "Unsupported field type : " + field + " " + fieldConfig.getClass().getName());
        }
    }

    /**
     * Internal populate double field.
     *
     * @param options the options
     * @param fieldId the field id
     * @param key the key
     */
    private void internal_populateDoubleField(Map<String, String> options, FieldIdEnum fieldId,
            String key) {
        if ((options != null) && options.containsKey(key)) {
            String storedValue = options.get(key);
            Double value = Double.valueOf(storedValue);
            fieldConfigVisitor.populateDoubleField(fieldId, value);
        } else {
            setDefaultValue(fieldId);
        }
    }

    /**
     * Internal_populate boolean field.
     *
     * @param options the options
     * @param fieldId the field id
     * @param key the key
     */
    private void internal_populateBooleanField(Map<String, String> options, FieldIdEnum fieldId,
            String key) {
        if ((options != null) && options.containsKey(key)) {
            String storedValue = options.get(key);
            Boolean value = Boolean.valueOf(storedValue);
            fieldConfigVisitor.populateBooleanField(fieldId, value);
        } else {
            setDefaultValue(fieldId);
        }
    }

    /**
     * Internal_populate integer field.
     *
     * @param options the options
     * @param fieldId the field id
     * @param key the key
     */
    private void internal_populateIntegerField(Map<String, String> options, FieldIdEnum fieldId,
            String key) {
        if ((options != null) && options.containsKey(key)) {
            String storedValue = options.get(key);
            Integer value = Double.valueOf(storedValue).intValue();
            fieldConfigVisitor.populateIntegerField(fieldId, value);
        } else {
            setDefaultValue(fieldId);
        }
    }

    /**
     * Internal_populate enum field.
     *
     * @param options the options
     * @param fieldId the field id
     * @param key the key
     */
    private void internal_populateEnumField(Map<String, String> options, FieldIdEnum fieldId,
            String key) {
        if ((options != null) && options.containsKey(key)) {
            String value = options.get(key);
            fieldConfigVisitor.populateComboBoxField(fieldId, value);
        } else {
            setDefaultValue(fieldId);
        }
    }

    /**
     * Internal update symbol.
     *
     * @param options the options
     * @param field the field
     * @param key the key
     */
    private void internalUpdateSymbol(Map<String, String> options, FieldIdEnum field, String key) {
        FieldConfigBase fieldConfig = fieldConfigManager.get(field);

        if (fieldConfig instanceof FieldConfigBoolean) {
            internal_updateSymbolBooleanField(options, field, key);
        } else if (fieldConfig instanceof FieldConfigInteger) {
            internal_updateSymbolIntegerField(options, field, key);
        } else if (fieldConfig instanceof FieldConfigDouble) {
            internal_updateSymbolDoubleField(options, field, key);
        } else if (fieldConfig instanceof FieldConfigEnum) {
            internal_updateSymbolEnumField(options, field, key);
        } else {
            ConsoleManager.getInstance().error(this,
                    "Unsupported field type : " + field + " " + fieldConfig.getClass().getName());
        }
    }

    /**
     * Internal_update symbol enum field.
     *
     * @param options the options
     * @param field the field
     * @param key the key
     */
    private void internal_updateSymbolEnumField(Map<String, String> options, FieldIdEnum field,
            String key) {
        ValueComboBoxData value = fieldConfigVisitor.getComboBox(field);

        Object object = getDefaultFieldValue(field);
        String defaultValue = null;

        if (object instanceof TextSymbolizer.PolygonAlignOptions) {
            TextSymbolizer.PolygonAlignOptions option = (PolygonAlignOptions) object;
            defaultValue = option.toString();
        } else {
            defaultValue = (String) object;
        }

        if (defaultValue == null) {
            ConsoleManager.getInstance().error(this, "Failed to find default for field : " + field);
        } else if (value.getKey().compareToIgnoreCase(defaultValue) != 0) {
            options.put(key, value.getKey());
        }
    }

    /**
     * Internal_update symbol double field.
     *
     * @param options the options
     * @param field the field
     * @param key the key
     */
    private void internal_updateSymbolDoubleField(Map<String, String> options, FieldIdEnum field,
            String key) {
        double value = fieldConfigVisitor.getDouble(field);

        Double defaultValue = (Double) getDefaultFieldValue(field);

        if (defaultValue == null) {
            ConsoleManager.getInstance().error(this, "Failed to find default for field : " + field);
        } else if (value != defaultValue) {
            options.put(key, String.valueOf(value));
        }
    }

    /**
     * Internal_update symbol integer field.
     *
     * @param options the options
     * @param field the field
     * @param key the key
     */
    private void internal_updateSymbolIntegerField(Map<String, String> options, FieldIdEnum field,
            String key) {
        int value = fieldConfigVisitor.getInteger(field);

        Integer defaultValue = (Integer) getDefaultFieldValue(field);

        if (defaultValue == null) {
            ConsoleManager.getInstance().error(this, "Failed to find default for field : " + field);
        } else if (value != defaultValue) {
            options.put(key, String.valueOf(value));
        }
    }

    /**
     * Internal_update symbol boolean field.
     *
     * @param options the options
     * @param field the field
     * @param key the key
     */
    private void internal_updateSymbolBooleanField(Map<String, String> options, FieldIdEnum field,
            String key) {
        boolean value = fieldConfigVisitor.getBoolean(field);

        Boolean defaultValue = (Boolean) getDefaultFieldValue(field);

        if (defaultValue == null) {
            ConsoleManager.getInstance().error(this, "Failed to find default for field : " + field);
        } else if (value != defaultValue) {
            options.put(key, String.valueOf(value));
        }
    }

    /**
     * Gets the panel.
     *
     * @return the panel
     */
    @Override
    public StandardPanel getPanel() {
        return this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.sldeditor.ui.detail.vendor.geoserver.VendorOptionInterface#setParentPanel(com.sldeditor.
     * ui.iface.UpdateSymbolInterface)
     */
    @Override
    public void setParentPanel(UpdateSymbolInterface parent) {
        this.parentObj = parent;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.ui.iface.PopulateDetailsInterface#isDataPresent()
     */
    @Override
    public boolean isDataPresent() {
        return true;
    }

    /**
     * Update symbol.
     *
     * @param polygonSymbolizer the polygon symbolizer
     */
    @Override
    public void updateSymbol(PolygonSymbolizer polygonSymbolizer) {
        Map<String, String> options = polygonSymbolizer.getOptions();

        options.clear();

        for (FieldIdEnum key : fieldMap.keySet()) {
            internalUpdateSymbol(options, key, fieldMap.get(key));
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.sldeditor.ui.detail.vendor.geoserver.VendorOptionInterface#updateSymbol(org.geotools.
     * styling.TextSymbolizer)
     */
    @Override
    public void updateSymbol(TextSymbolizer textSymbolizer) {
        // Do nothing
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.sldeditor.ui.detail.vendor.geoserver.VendorOptionInterface#updateSymbol(org.geotools.
     * styling.RasterSymbolizer)
     */
    @Override
    public void updateSymbol(RasterSymbolizer rasterSymbolizer) {
        // Do nothing
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.ui.iface.PopulateDetailsInterface#initialseFields()
     */
    @Override
    public void preLoadSymbol() {
        setAllDefaultValues();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.ui.detail.vendor.geoserver.VendorOptionInterface#getParentPanel()
     */
    @Override
    public UpdateSymbolInterface getParentPanel() {
        return parentObj;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.ui.detail.vendor.geoserver.VendorOptionInterface#getVendorOptionInfo()
     */
    @Override
    public VendorOptionInfo getVendorOptionInfo() {
        if (vendorOptionInfo == null) {
            vendorOptionInfo = new VendorOptionInfo("Polygon Random Fill", getVendorOptionVersion(),
                    Localisation.getString(VOGeoServerRandomFill.class,
                            "VOGeoServerRandomFill.description"));
        }
        return vendorOptionInfo;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.ui.iface.PopulateDetailsInterface#getMinimumVersion(java.lang.Object,
     * java.util.List)
     */
    @Override
    public void getMinimumVersion(Object parentObj, Object sldObj,
            List<VendorOptionPresent> vendorOptionsPresentList) {

        if (parentObj instanceof PolygonSymbolizer) {
            PolygonSymbolizer polygon = (PolygonSymbolizer) parentObj;
            Map<String, String> options = polygon.getOptions();

            for (FieldIdEnum key : fieldMap.keySet()) {
                String vendorOptionAttributeKey = fieldMap.get(key);

                if (options.containsKey(vendorOptionAttributeKey)) {
                    VendorOptionPresent voPresent = new VendorOptionPresent(sldObj,
                            getVendorOptionInfo());

                    vendorOptionsPresentList.add(voPresent);
                }
            }
        }
    }
}
