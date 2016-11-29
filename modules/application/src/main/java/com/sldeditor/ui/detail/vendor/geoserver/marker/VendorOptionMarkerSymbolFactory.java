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

import com.sldeditor.ui.detail.config.symboltype.SymbolTypeConfig;
import com.sldeditor.ui.detail.vendor.VendorOptionFactoryInterface;
import com.sldeditor.ui.detail.vendor.geoserver.VendorOptionInterface;
import com.sldeditor.ui.widgets.ValueComboBoxData;

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
    public VendorOptionMarkerSymbolFactory()
    {
        list.add(vendorOptionGeoServerShape);
        list.add(vendorOptionGeoServerWeather);
        list.add(vendorOptionGeoServerArrow);
    }

    /**
     * Adds the vendor option.
     *
     * @param symbolizerClass the symbolizer class
     * @param symbolList the symbol list
     * @param fieldEnableMap the field enable map
     * @param panelId the panel id
     */
    public void addVendorOption(Class<?> symbolizerClass, List<ValueComboBoxData> symbolList, Map<Class<?>, List<SymbolTypeConfig> > fieldEnableMap, Class<?> panelId) {

        for(VOMarkerSymbolInterface obj : list)
        {
            obj.addVendorOption(symbolizerClass, symbolList, fieldEnableMap, panelId);
        }
    }

    /**
     * Gets the field map.
     *
     * @param fieldEnableMap the field enable map
     */
    public void getFieldMap(Map<Class<?>, List<SymbolTypeConfig>> fieldEnableMap) {
        for(VOMarkerSymbolInterface obj : list)
        {
            Map<Class<?>, List<SymbolTypeConfig>> map = obj.getFieldMap();

            if(map != null)
            {
                for(Class<?> symbolizer : map.keySet())
                {
                    List<SymbolTypeConfig> existing = fieldEnableMap.get(symbolizer);

                    if(existing == null)
                    {
                        existing = new ArrayList<SymbolTypeConfig>();
                        fieldEnableMap.put(symbolizer, existing);
                    }

                    List<SymbolTypeConfig> newData = map.get(symbolizer);
                    existing.addAll(newData);
                }
            }
        }
    }

    /* (non-Javadoc)
     * @see com.sldeditor.ui.detail.vendor.VendorOptionFactoryInterface#getVendorOptionList()
     */
    @Override
    public List<VendorOptionInterface> getVendorOptionList() {
        return null;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.ui.detail.vendor.VendorOptionFactoryInterface#getVendorOptionList(java.lang.String)
     */
    @Override
    public List<VendorOptionInterface> getVendorOptionList(String className) {
        return null;
    }
}
