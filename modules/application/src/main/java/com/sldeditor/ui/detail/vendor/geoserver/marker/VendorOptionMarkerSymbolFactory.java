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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.ui.detail.ColourFieldConfig;
import com.sldeditor.ui.detail.config.symboltype.FieldState;
import com.sldeditor.ui.detail.config.symboltype.SymbolTypeConfig;
import com.sldeditor.ui.detail.vendor.VendorOptionFactoryInterface;
import com.sldeditor.ui.detail.vendor.geoserver.VendorOptionInterface;

/**
 * A factory for creating VendorOptionMarkerSymbolFactory objects.
 * 
 * @author Robert Ward (SCISYS)
 */
public class VendorOptionMarkerSymbolFactory implements VendorOptionFactoryInterface {

    /** The GeoServer vendor option for shapes:// */
    private VOMarkerSymbolInterface vendorOptionGeoServerShape = new VOGeoServerShapeSymbol();

    /** The GeoServer vendor option for extshapes:// */
    private VOMarkerSymbolInterface vendorOptionGeoServerWeather = new VOGeoServerWeatherSymbol();

    /** The GeoServer vendor option for extshape://arrow */
    private VOMarkerSymbolInterface vendorOptionGeoServerArrow = new VOGeoServerArrowSymbol();

    /** The list of all the extensions. */
    private List<VOMarkerSymbolInterface> list = new ArrayList<VOMarkerSymbolInterface>();

    /**
     * Instantiates a new vendor option marker symbol factory.
     */ 
    public VendorOptionMarkerSymbolFactory() {
        list.add(vendorOptionGeoServerShape);
        list.add(vendorOptionGeoServerWeather);
        list.add(vendorOptionGeoServerArrow);
    }

    /**
     * Gets the field map.
     *
     * @param fieldEnableMap the field enable map
     */
    public void getFieldMap(Map<Class<?>, List<SymbolTypeConfig>> fieldEnableMap) {
        for (VOMarkerSymbolInterface obj : list) {
            Map<Class<?>, List<SymbolTypeConfig>> map = obj.getFieldMap();

            if (map != null) {
                for (Class<?> symbolizer : map.keySet()) {
                    List<SymbolTypeConfig> existing = fieldEnableMap.get(symbolizer);

                    if (existing == null) {
                        existing = new ArrayList<SymbolTypeConfig>();
                        fieldEnableMap.put(symbolizer, existing);
                    }

                    List<SymbolTypeConfig> newData = map.get(symbolizer);
                    existing.addAll(newData);
                }
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.ui.detail.vendor.VendorOptionFactoryInterface#getVendorOptionList()
     */
    @Override
    public List<VendorOptionInterface> getVendorOptionList() {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.ui.detail.vendor.VendorOptionFactoryInterface#getVendorOptionList(java.lang.String)
     */
    @Override
    public List<VendorOptionInterface> getVendorOptionList(String className) {
        return null;
    }

    /**
     * Gets the vendor option marker symbols.
     *
     * @param panelId the panel id
     * @param fillFieldConfig the fill field config
     * @param strokeFieldConfig the stroke field config
     * @param symbolSelectionField the symbol selection field
     * @return the vendor option marker symbols
     */
    public List<FieldState> getVendorOptionMarkerSymbols(Class<?> panelId,
            ColourFieldConfig fillFieldConfig,
            ColourFieldConfig strokeFieldConfig, FieldIdEnum symbolSelectionField) {
        List<FieldState> fieldStateList = new ArrayList<FieldState>();

        for (VOMarkerSymbolInterface obj : list) {
            List<FieldState> markerFieldStateList = obj.getMarkerSymbols(panelId, fillFieldConfig, strokeFieldConfig, symbolSelectionField);
            if((markerFieldStateList != null) && !markerFieldStateList.isEmpty())
            {
                fieldStateList.addAll(markerFieldStateList);
            }
        }

        return fieldStateList;
    }
}
