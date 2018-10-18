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

package com.sldeditor.ui.detail.vendor.geoserver.marker.wkt;

import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.common.vendoroption.info.VendorOptionInfo;
import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.ui.detail.ColourFieldConfig;
import com.sldeditor.ui.detail.config.FieldConfigCommonData;
import com.sldeditor.ui.detail.config.symboltype.FieldState;
import com.sldeditor.ui.detail.config.symboltype.SymbolTypeConfig;
import com.sldeditor.ui.detail.vendor.geoserver.marker.VOMarkerSymbolInterface;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Class to handle the getting and setting of GeoServer weather symbol vendor option data.
 *
 * <p>Sets the &gt;WellKnownName&lt; string, extra fields needed.
 *
 * @author Robert Ward (SCISYS)
 */
public class VOGeoServerWKTSymbol implements VOMarkerSymbolInterface {

    private FieldConfigWKT wktShape;

    /** Instantiates a new VOGeoServerWKTSymbol. */
    public VOGeoServerWKTSymbol() {
        // Default constructor
    }

    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.ui.detail.vendor.geoserver.marker.VOMarkerSymbolInterface#getFieldMap()
     */
    @Override
    public Map<Class<?>, List<SymbolTypeConfig>> getFieldMap() {
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.ui.detail.vendor.geoserver.marker.VOMarkerSymbolInterface#getMarkerSymbols(java.lang.Class, com.sldeditor.ui.detail.ColourFieldConfig, com.sldeditor.ui.detail.ColourFieldConfig, com.sldeditor.common.xml.ui.FieldIdEnum)
     */
    @Override
    public List<FieldState> getMarkerSymbols(
            Class<?> panelId,
            ColourFieldConfig fillFieldConfig,
            ColourFieldConfig strokeFieldConfig,
            FieldIdEnum symbolSelectionField) {
        List<FieldState> fieldStateList = new ArrayList<FieldState>();

        wktShape =
                new FieldConfigWKT(
                        new FieldConfigCommonData(panelId, FieldIdEnum.WKT, "", true, false),
                        fillFieldConfig,
                        strokeFieldConfig,
                        symbolSelectionField);

        fieldStateList.add(wktShape);
        return fieldStateList;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.ui.detail.vendor.geoserver.marker.VOMarkerSymbolInterface#getVendorOptionInfo()
     */
    @Override
    public VendorOptionInfo getVendorOptionInfo() {

        VendorOptionInfo info =
                new VendorOptionInfo(
                        "WKT",
                        wktShape.getVendorOptionVersion(),
                        Localisation.getString(WKTDetails.class, "WKTDetails.description"));

        return info;
    }
}
