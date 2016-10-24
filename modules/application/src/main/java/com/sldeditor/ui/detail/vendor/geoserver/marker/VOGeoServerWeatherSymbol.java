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
package com.sldeditor.ui.detail.vendor.geoserver.marker;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.RasterSymbolizer;
import org.geotools.styling.TextSymbolizer;

import com.sldeditor.common.vendoroption.GeoServerVendorOption;
import com.sldeditor.common.vendoroption.VendorOptionManager;
import com.sldeditor.common.vendoroption.VendorOptionVersion;
import com.sldeditor.ui.detail.StandardPanel;
import com.sldeditor.ui.detail.config.symboltype.SymbolTypeConfig;
import com.sldeditor.ui.detail.config.symboltype.SymbolTypeConfigReader;
import com.sldeditor.ui.detail.vendor.geoserver.VendorOptionInterface;
import com.sldeditor.ui.iface.UpdateSymbolInterface;
import com.sldeditor.ui.widgets.ValueComboBoxData;

/**
 * Class to handle the getting and setting of GeoServer eather symbol vendor option data.
 * 
 * @author Robert Ward (SCISYS)
 */
public class VOGeoServerWeatherSymbol implements VendorOptionInterface, VOMarkerSymbolInterface {

    /** The field enable map. */
    private Map<Class<?>, List<SymbolTypeConfig> > fieldEnableMap = new HashMap<Class<?>, List<SymbolTypeConfig> >();

    /** The parent obj. */
    private UpdateSymbolInterface parentObj = null;

    /**
     * Instantiates a new VO geo server weather symbol.
     */
    public VOGeoServerWeatherSymbol()
    {
        String fullResourceName = "geoserver/GeoServerExtShapeSymbol.xml";
        SymbolTypeConfigReader.readConfig(VOGeoServerWeatherSymbol.class, fullResourceName, fieldEnableMap);
    }

    /* (non-Javadoc)
     * @see com.sldeditor.ui.detail.vendor.geoserver.VendorOptionInterface#getVendorOption()
     */
    @Override
    public VendorOptionVersion getVendorOption() {
        return VendorOptionManager.getInstance().getVendorOptionVersion(GeoServerVendorOption.class);
    }

    /**
     * Adds the vendor option.
     *
     * @param symbolizerClass the symbolizer class
     * @param symbolList the symbol list
     * @param fieldEnableMap the field enable map
     * @param panelId the panel id
     */
    @Override
    public void addVendorOption(Class<?> symbolizerClass, List<ValueComboBoxData> symbolList, Map<Class<?>, List<SymbolTypeConfig> > fieldEnableMap, Class<?> panelId) {
        fieldEnableMap.putAll(this.fieldEnableMap);
    }

    /* (non-Javadoc)
     * @see com.sldeditor.ui.detail.vendor.geoserver.VendorOptionInterface#populate(org.geotools.styling.TextSymbolizer)
     */
    @Override
    public void populate(TextSymbolizer textSymbolizer)
    {
        // Do nothing
    }

    /* (non-Javadoc)
     * @see com.sldeditor.ui.detail.vendor.geoserver.VendorOptionInterface#updateSymbol(org.geotools.styling.TextSymbolizer)
     */
    @Override
    public void updateSymbol(TextSymbolizer textSymbolizer)
    {
        // Do nothing
    }

    /* (non-Javadoc)
     * @see com.sldeditor.ui.detail.vendor.geoserver.VendorOptionInterface#getPanel()
     */
    @Override
    public StandardPanel getPanel()
    {
        return null;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.ui.detail.vendor.geoserver.VendorOptionInterface#setParentPanel(com.sldeditor.ui.iface.UpdateSymbolInterface)
     */
    @Override
    public void setParentPanel(UpdateSymbolInterface parent)
    {
        this.parentObj = parent;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.ui.detail.vendor.geoserver.marker.VOMarkerSymbolInterface#getFieldMap()
     */
    @Override
    public Map<Class<?>, List<SymbolTypeConfig>> getFieldMap() {
        return this.fieldEnableMap;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.ui.detail.vendor.geoserver.VendorOptionInterface#updateSymbol(org.geotools.styling.PolygonSymbolizer)
     */
    @Override
    public void updateSymbol(PolygonSymbolizer polygonSymbolizer)
    {
        // Do nothing
    }

    /* (non-Javadoc)
     * @see com.sldeditor.ui.detail.vendor.geoserver.VendorOptionInterface#populate(org.geotools.styling.PolygonSymbolizer)
     */
    @Override
    public void populate(PolygonSymbolizer polygonSymbolizer)
    {
        // Do nothing
    }

    /* (non-Javadoc)
     * @see com.sldeditor.ui.detail.vendor.geoserver.VendorOptionInterface#populate(org.geotools.styling.RasterSymbolizer)
     */
    @Override
    public void populate(RasterSymbolizer rasterSymbolizer) {
        // Do nothing
    }

    /* (non-Javadoc)
     * @see com.sldeditor.ui.detail.vendor.geoserver.VendorOptionInterface#updateSymbol(org.geotools.styling.RasterSymbolizer)
     */
    @Override
    public void updateSymbol(RasterSymbolizer rasterSymbolizer) {
        // Do nothing
    }

    /* (non-Javadoc)
     * @see com.sldeditor.ui.detail.vendor.geoserver.VendorOptionInterface#getParentPanel()
     */
    @Override
    public UpdateSymbolInterface getParentPanel() {
        return parentObj;
    }
}
