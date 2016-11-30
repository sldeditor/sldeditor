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

import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.ui.detail.ColourFieldConfig;
import com.sldeditor.ui.detail.config.symboltype.FieldConfigMarker;
import com.sldeditor.ui.detail.config.symboltype.FieldState;
import com.sldeditor.ui.detail.config.symboltype.SymbolTypeConfig;
import com.sldeditor.ui.detail.config.symboltype.SymbolTypeConfigReader;

/**
 * Class to handle the getting and setting of GeoServer marker shapes vendor option data.
 * 
 * Only sets the <WellKnownName> string, no extra fields needed.
 *
 * @author Robert Ward (SCISYS)
 */
public class VOGeoServerExtShapeSymbol implements VOMarkerSymbolInterface {

    /** The field enable map. */
    private Map<Class<?>, List<SymbolTypeConfig>> fieldEnableMap = new HashMap<Class<?>, List<SymbolTypeConfig>>();

    /**
     * Instantiates a new VOGeoServerShapeSymbol.
     */
    public VOGeoServerExtShapeSymbol() {
        String fullResourceName = "geoserver/SymbolTypeFieldState_ExtShape.xml";
        SymbolTypeConfigReader.readConfig(FieldConfigMarker.class, fullResourceName,
                fieldEnableMap);
    }

    /* (non-Javadoc)
     * @see com.sldeditor.ui.detail.vendor.geoserver.marker.VOMarkerSymbolInterface#getMarkerSymbols(java.lang.Class, com.sldeditor.ui.detail.ColourFieldConfig, com.sldeditor.ui.detail.ColourFieldConfig, com.sldeditor.common.xml.ui.FieldIdEnum)
     */
    @Override
    public List<FieldState> getMarkerSymbols(Class<?> panelId, ColourFieldConfig fillFieldConfig,
            ColourFieldConfig strokeFieldConfig, FieldIdEnum symbolSelectionField) {
        return null;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.ui.detail.vendor.geoserver.marker.VOMarkerSymbolInterface#getFieldMap()
     */
    @Override
    public Map<Class<?>, List<SymbolTypeConfig>> getFieldMap() {
        return fieldEnableMap;
    }
}
