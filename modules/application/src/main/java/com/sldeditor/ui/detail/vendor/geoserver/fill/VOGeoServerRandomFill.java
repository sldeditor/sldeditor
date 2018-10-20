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

import com.sldeditor.common.data.SelectedSymbol;
import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.common.vendoroption.VendorOptionVersion;
import com.sldeditor.common.vendoroption.info.VendorOptionInfo;
import com.sldeditor.common.vendoroption.minversion.VendorOptionPresent;
import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.ui.detail.GraphicPanelFieldManager;
import com.sldeditor.ui.detail.StandardPanel;
import com.sldeditor.ui.detail.vendor.VOPopulation;
import com.sldeditor.ui.detail.vendor.geoserver.VendorOptionInterface;
import com.sldeditor.ui.iface.PopulateDetailsInterface;
import com.sldeditor.ui.iface.UpdateSymbolInterface;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.RasterSymbolizer;
import org.geotools.styling.SelectedChannelType;
import org.geotools.styling.TextSymbolizer;

/**
 * Class to handle the getting and setting of GeoServer random fill vendor option data.
 *
 * @author Robert Ward (SCISYS)
 */
public class VOGeoServerRandomFill extends VOPopulation
        implements VendorOptionInterface, PopulateDetailsInterface, UpdateSymbolInterface {

    /** The Constant PANEL_CONFIG. */
    private static final String PANEL_CONFIG = "symbol/fill/PanelConfig_GeoServerRandomFill.xml";

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The field map. */
    private Map<FieldIdEnum, String> fieldMap = new EnumMap<>(FieldIdEnum.class);

    /** The parent obj. */
    private transient UpdateSymbolInterface parentObj = null;

    /** The vendor option info. */
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

    /** Creates the ui. */
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
     * (non-Javadoc)
     *
     * @see
     *     com.sldeditor.ui.detail.vendor.geoserver.VendorOptionInterface#populate(org.geotools.styling.FeatureTypeStyle)
     */
    @Override
    public void populate(FeatureTypeStyle featureTypeStyle) {
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

        for (Entry<FieldIdEnum, String> entry : fieldMap.entrySet()) {
            internalPopulate(options, entry.getKey(), entry.getValue());
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
        if (polygonSymbolizer != null) {
            Map<String, String> options = polygonSymbolizer.getOptions();

            options.clear();

            for (Entry<FieldIdEnum, String> entry : fieldMap.entrySet()) {
                internalUpdateSymbol(options, entry.getKey(), entry.getValue());
            }
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
     * @see
     * com.sldeditor.ui.detail.vendor.geoserver.VendorOptionInterface#updateSymbol(org.geotools.
     * styling.FeatureTypeStyle)
     */
    @Override
    public void updateSymbol(FeatureTypeStyle featureTypeStyle) {
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
            vendorOptionInfo =
                    new VendorOptionInfo(
                            "Polygon Random Fill",
                            getVendorOptionVersion(),
                            Localisation.getString(
                                    VOGeoServerRandomFill.class,
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
    public void getMinimumVersion(
            Object parentObj, Object sldObj, List<VendorOptionPresent> vendorOptionsPresentList) {

        if (parentObj instanceof PolygonSymbolizer) {
            PolygonSymbolizer polygon = (PolygonSymbolizer) parentObj;
            Map<String, String> options = polygon.getOptions();

            for (Entry<FieldIdEnum, String> entry : fieldMap.entrySet()) {
                String vendorOptionAttributeKey = entry.getValue();

                if (options.containsKey(vendorOptionAttributeKey)) {
                    VendorOptionPresent voPresent =
                            new VendorOptionPresent(sldObj, getVendorOptionInfo());

                    if (vendorOptionsPresentList != null) {
                        vendorOptionsPresentList.add(voPresent);
                    }
                }
            }
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.sldeditor.ui.detail.vendor.geoserver.VendorOptionInterface#populate(org.geotools.styling.
     * SelectedChannelType)
     */
    @Override
    public void populate(SelectedChannelType channelType) {
        // Do nothing
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.sldeditor.ui.detail.vendor.geoserver.VendorOptionInterface#updateSymbol(org.geotools.
     * styling.SelectedChannelType)
     */
    @Override
    public void updateSymbol(SelectedChannelType channelType) {
        // Do nothing
    }
}
