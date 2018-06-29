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

package com.sldeditor.ui.detail.vendor.geoserver.featuretypestyle;

import java.util.List;
import java.util.Map;

import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.RasterSymbolizer;
import org.geotools.styling.SelectedChannelType;
import org.geotools.styling.TextSymbolizer;

import com.sldeditor.common.data.SelectedSymbol;
import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.common.vendoroption.VendorOptionVersion;
import com.sldeditor.common.vendoroption.info.VendorOptionInfo;
import com.sldeditor.common.vendoroption.minversion.VendorOptionPresent;
import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.common.xml.ui.GroupIdEnum;
import com.sldeditor.ui.detail.GraphicPanelFieldManager;
import com.sldeditor.ui.detail.StandardPanel;
import com.sldeditor.ui.detail.config.base.GroupConfigInterface;
import com.sldeditor.ui.detail.vendor.geoserver.VendorOptionInterface;
import com.sldeditor.ui.iface.PopulateDetailsInterface;
import com.sldeditor.ui.iface.UpdateSymbolInterface;

/**
 * Class to handle the getting and setting of GeoServer composite base vendor option data.
 * 
 * @author Robert Ward (SCISYS)
 */
public class VOGeoServerFTSCompositeBase extends StandardPanel
        implements VendorOptionInterface, PopulateDetailsInterface, UpdateSymbolInterface {

    /** The Constant DEFAULT_COMPOSITE_BASE. */
    private static final boolean DEFAULT_COMPOSITE_BASE = false;

    /** The Constant PANEL_CONFIG. */
    private static final String PANEL_CONFIG = "featuretypestyle/PanelConfig_CompositeBase.xml";

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The parent obj. */
    private UpdateSymbolInterface parentObj = null;

    /** The vendor option info. */
    private VendorOptionInfo vendorOptionInfo = null;

    /**
     * Constructor.
     *
     * @param panelId the panel id
     */
    public VOGeoServerFTSCompositeBase(Class<?> panelId) {
        super(panelId);

        createUI();
    }

    /**
     * Creates the ui.
     */
    private void createUI() {
        readConfigFileNoScrollPane(null, getPanelId(), this, PANEL_CONFIG);
    }

    /**
     * Gets the vendor option.
     *
     * @return the vendor option
     */
    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.ui.detail.vendor.geoserver.VendorOptionInterface#getVendorOption()
     */
    @Override
    public VendorOptionVersion getVendorOption() {
        return getVendorOptionVersion();
    }

    /**
     * Data changed.
     *
     * @param changedField the changed field
     */
    /*
     * (non-Javadoc)
     * 
     * @see
     * com.sldeditor.ui.iface.UpdateSymbolInterface#dataChanged(com.sldeditor.ui.detail.config.xml.
     * FieldId)
     */
    @Override
    public void dataChanged(FieldIdEnum changedField) {
        if (parentObj != null) {
            parentObj.dataChanged(changedField);
        }
    }

    /**
     * Populate.
     *
     * @param selectedSymbol the selected symbol
     */
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

    /**
     * Populate.
     *
     * @param textSymbolizer the text symbolizer
     */
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
     * @see
     * com.sldeditor.ui.detail.vendor.geoserver.VendorOptionInterface#populate(org.geotools.styling.
     * FeatureTypeStyle)
     */
    @Override
    public void populate(FeatureTypeStyle featureTypeStyle) {
        Map<String, String> options = featureTypeStyle.getOptions();

        String compositeBaseString = options.get(FeatureTypeStyle.COMPOSITE_BASE);

        boolean value = DEFAULT_COMPOSITE_BASE;
        try {
            value = Boolean.valueOf(compositeBaseString);
        } catch (Exception e) {
            // Do nothing and revert to default
        }

        fieldConfigVisitor.populateBooleanField(FieldIdEnum.VO_FTS_COMPOSITE_BASE_BOOL, value);

        GroupConfigInterface groupPanel = getGroup(GroupIdEnum.VO_FTS_COMPOSITE_BASE);
        groupPanel.enable(compositeBaseString != null);
    }

    /**
     * Populate.
     *
     * @param polygonSymbolizer the polygon symbolizer
     */
    /*
     * (non-Javadoc)
     * 
     * @see
     * com.sldeditor.ui.detail.vendor.geoserver.VendorOptionInterface#populate(org.geotools.styling.
     * PolygonSymbolizer)
     */
    @Override
    public void populate(PolygonSymbolizer polygonSymbolizer) {
        // Do nothing
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
        Map<String, String> options = featureTypeStyle.getOptions();

        GroupConfigInterface groupPanel = getGroup(GroupIdEnum.VO_FTS_COMPOSITE_BASE);
        if (groupPanel.isPanelEnabled()) {
            boolean value = fieldConfigVisitor.getBoolean(FieldIdEnum.VO_FTS_COMPOSITE_BASE_BOOL);

            options.put(FeatureTypeStyle.COMPOSITE_BASE, String.valueOf(value));
        } else {
            options.remove(FeatureTypeStyle.COMPOSITE_BASE);
        }
    }

    /**
     * Update symbol.
     *
     * @param polygonSymbolizer the polygon symbolizer
     */
    /*
     * (non-Javadoc)
     * 
     * @see
     * com.sldeditor.ui.detail.vendor.geoserver.VendorOptionInterface#updateSymbol(org.geotools.
     * styling.PolygonSymbolizer)
     */
    @Override
    public void updateSymbol(PolygonSymbolizer polygonSymbolizer) {
        // Do nothing
    }

    /**
     * Update symbol.
     *
     * @param textSymbolizer the text symbolizer
     */
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

    /**
     * Gets the panel.
     *
     * @return the panel
     */
    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.ui.detail.vendor.geoserver.VendorOptionInterface#getPanel()
     */
    @Override
    public StandardPanel getPanel() {
        return this;
    }

    /**
     * Sets the parent panel.
     *
     * @param parent the new parent panel
     */
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

    /**
     * Checks if is data present.
     *
     * @return true, if is data present
     */
    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.ui.iface.PopulateDetailsInterface#isDataPresent()
     */
    @Override
    public boolean isDataPresent() {
        return true;
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
            vendorOptionInfo = new VendorOptionInfo(
                    Localisation.getString(VOGeoServerFTSCompositeBase.class,
                            "geoserver.compositebase.title"),
                    this.getVendorOption(),
                    Localisation.getString(VOGeoServerFTSCompositeBase.class,
                            "geoserver.compositebase.description"));
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
        if (sldObj instanceof FeatureTypeStyle) {
            FeatureTypeStyle fts = (FeatureTypeStyle) sldObj;
            Map<String, String> options = fts.getOptions();

            if (options.containsKey(FeatureTypeStyle.COMPOSITE_BASE)) {
                VendorOptionPresent voPresent = new VendorOptionPresent(sldObj,
                        getVendorOptionInfo());

                vendorOptionsPresentList.add(voPresent);
            }
        }
    }

    /* (non-Javadoc)
     * @see com.sldeditor.ui.detail.vendor.geoserver.VendorOptionInterface#populate(org.geotools.styling.SelectedChannelType)
     */
    @Override
    public void populate(SelectedChannelType channelType) {
        // Do nothing
    }

    /* (non-Javadoc)
     * @see com.sldeditor.ui.detail.vendor.geoserver.VendorOptionInterface#updateSymbol(org.geotools.styling.SelectedChannelType)
     */
    @Override
    public void updateSymbol(SelectedChannelType channelType) {
        // Do nothing
    }
}
