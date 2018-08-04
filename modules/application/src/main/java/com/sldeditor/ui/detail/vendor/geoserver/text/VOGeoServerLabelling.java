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

package com.sldeditor.ui.detail.vendor.geoserver.text;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.RasterSymbolizer;
import org.geotools.styling.SelectedChannelType;
import org.geotools.styling.TextSymbolizer;
import org.geotools.styling.TextSymbolizer2;

/**
 * Class to handle the getting and setting of GeoServer labelling vendor option data.
 *
 * @author Robert Ward (SCISYS)
 */
public class VOGeoServerLabelling extends VOPopulation
        implements VendorOptionInterface, PopulateDetailsInterface, UpdateSymbolInterface {

    /** The Constant PANEL_CONFIG. */
    private static final String PANEL_CONFIG = "symbol/text/PanelConfig_Label.xml";

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The field map. */
    private Map<FieldIdEnum, String> fieldMap = new HashMap<FieldIdEnum, String>();

    /** The parent obj. */
    private UpdateSymbolInterface parentObj = null;

    /** The vendor option info. */
    private VendorOptionInfo vendorOptionInfo = null;

    /**
     * Constructor.
     *
     * @param panelId the panel id
     */
    public VOGeoServerLabelling(Class<?> panelId) {
        super(panelId);

        fieldMap.put(FieldIdEnum.LABEL_ALLOW_OVERRUNS, TextSymbolizer2.ALLOW_OVERRUNS_KEY);
        fieldMap.put(FieldIdEnum.LABEL_AUTO_WRAP, TextSymbolizer2.AUTO_WRAP_KEY);
        fieldMap.put(
                FieldIdEnum.LABEL_CONFLICT_RESOLUTION, TextSymbolizer2.CONFLICT_RESOLUTION_KEY);
        fieldMap.put(FieldIdEnum.LABEL_FOLLOW_LINE, TextSymbolizer2.FOLLOW_LINE_KEY);
        fieldMap.put(
                FieldIdEnum.LABEL_FORCE_LEFT_TO_RIGHT, TextSymbolizer2.FORCE_LEFT_TO_RIGHT_KEY);
        fieldMap.put(FieldIdEnum.LABEL_GOODNESS_OF_FIT, TextSymbolizer2.GOODNESS_OF_FIT_KEY);
        fieldMap.put(FieldIdEnum.LABEL_GRAPHIC_MARGIN, TextSymbolizer2.GRAPHIC_MARGIN_KEY);
        fieldMap.put(FieldIdEnum.LABEL_GRAPHIC_RESIZE, TextSymbolizer2.GRAPHIC_RESIZE_KEY);
        fieldMap.put(FieldIdEnum.LABEL_GROUP, TextSymbolizer2.GROUP_KEY);
        fieldMap.put(FieldIdEnum.LABEL_LABEL_ALL_GROUP, TextSymbolizer2.LABEL_ALL_GROUP_KEY);
        fieldMap.put(FieldIdEnum.LABEL_LABEL_REPEAT, TextSymbolizer2.LABEL_REPEAT_KEY);
        fieldMap.put(FieldIdEnum.LABEL_MAX_ANGLE_DELTA, TextSymbolizer2.MAX_ANGLE_DELTA_KEY);
        fieldMap.put(FieldIdEnum.LABEL_MAX_DISPLACEMENT, TextSymbolizer2.MAX_DISPLACEMENT_KEY);
        fieldMap.put(FieldIdEnum.LABEL_MIN_GROUP_DISTANCE, TextSymbolizer2.MIN_GROUP_DISTANCE_KEY);
        fieldMap.put(FieldIdEnum.LABEL_PARTIALS, TextSymbolizer2.PARTIALS_KEY);
        fieldMap.put(FieldIdEnum.LABEL_POLYGONALIGN, TextSymbolizer2.POLYGONALIGN_KEY);
        fieldMap.put(FieldIdEnum.LABEL_SPACE_AROUND, TextSymbolizer2.SPACE_AROUND_KEY);

        String[] graphicResizeValues = {"Proportional", "Stretch"};
        addOverride(
                FieldIdEnum.LABEL_GRAPHIC_MARGIN,
                new DefaultOverride(FieldIdEnum.LABEL_GRAPHIC_RESIZE, graphicResizeValues));
        createUI();
    }

    /** Creates the ui. */
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
        if (textSymbolizer != null) {
            Map<String, String> options = textSymbolizer.getOptions();

            for (FieldIdEnum key : fieldMap.keySet()) {
                internalPopulate(options, key, fieldMap.get(key));
            }
        }
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
     * styling.PolygonSymbolizer)
     */
    @Override
    public void updateSymbol(PolygonSymbolizer polygonSymbolizer) {}

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
        if (textSymbolizer != null) {
            Map<String, String> options = textSymbolizer.getOptions();

            for (FieldIdEnum key : fieldMap.keySet()) {
                internalUpdateSymbol(options, key, fieldMap.get(key));
            }
        }
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
            vendorOptionInfo =
                    new VendorOptionInfo(
                            Localisation.getString(VOGeoServerLabelling.class, "geoserver.label"),
                            this.getVendorOption(),
                            Localisation.getString(
                                    VOGeoServerLabelling.class, "geoserver.label.description"));
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
        if (sldObj instanceof TextSymbolizer) {
            TextSymbolizer textSymbolizer = (TextSymbolizer) sldObj;
            Map<String, String> options = textSymbolizer.getOptions();

            for (FieldIdEnum key : fieldMap.keySet()) {
                String vendorOptionAttributeKey = fieldMap.get(key);

                if (options.containsKey(vendorOptionAttributeKey)) {
                    VendorOptionPresent voPresent =
                            new VendorOptionPresent(sldObj, getVendorOptionInfo());

                    if (vendorOptionsPresentList != null) {
                        if (!vendorOptionsPresentList.contains(voPresent)) {
                            vendorOptionsPresentList.add(voPresent);
                        }
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
