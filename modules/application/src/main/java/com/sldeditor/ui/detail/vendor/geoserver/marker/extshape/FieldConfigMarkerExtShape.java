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

package com.sldeditor.ui.detail.vendor.geoserver.marker.extshape;

import java.util.List;

import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.common.vendoroption.info.VendorOptionInfo;
import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.minversion.VendorOptionPresent;
import com.sldeditor.ui.detail.ColourFieldConfig;
import com.sldeditor.ui.detail.config.FieldConfigCommonData;
import com.sldeditor.ui.detail.config.symboltype.FieldConfigMarker;

/**
 * The Class FieldConfigMarkerExtShape.
 *
 * @author Robert Ward (SCISYS)
 */
public class FieldConfigMarkerExtShape extends FieldConfigMarker {

    /** The Constant SYMBOLTYPE_FIELD_STATE_RESOURCE. */
    private static final String SYMBOLTYPE_FIELD_STATE_RESOURCE = "symbol/marker/extshape/SymbolTypeFieldState_ExtShape.xml";

    /** The vendor option info. */
    private VendorOptionInfo vendorOptionInfo = null;

    /**
     * Instantiates a new field config marker ext shape.
     *
     * @param commonData the common data
     * @param fillFieldConfig the fill field config
     * @param strokeFieldConfig the stroke field config
     * @param symbolSelectionField the symbol selection field
     */
    public FieldConfigMarkerExtShape(FieldConfigCommonData commonData,
            ColourFieldConfig fillFieldConfig, ColourFieldConfig strokeFieldConfig,
            FieldIdEnum symbolSelectionField) {
        super(SYMBOLTYPE_FIELD_STATE_RESOURCE, commonData, fillFieldConfig, strokeFieldConfig,
                symbolSelectionField);
    }

    /* (non-Javadoc)
     * @see com.sldeditor.ui.detail.config.symboltype.FieldState#getMinimumVersion(java.lang.Object, java.util.List)
     */
    @Override
    public void getMinimumVersion(Object parentObj, Object sldObj,
            List<VendorOptionPresent> vendorOptionsPresentList) {
        VendorOptionPresent voPresent = new VendorOptionPresent(sldObj,
                getVendorOptionInfo());

        vendorOptionsPresentList.add(voPresent);
    }

    /* (non-Javadoc)
     * @see com.sldeditor.ui.detail.config.symboltype.FieldState#getVendorOptionInfo()
     */
    @Override
    public VendorOptionInfo getVendorOptionInfo() {
        if(vendorOptionInfo == null)
        {
            vendorOptionInfo = new VendorOptionInfo("extshape://",
                    getVendorOption(),
                    Localisation.getString(VOGeoServerExtShapeSymbol.class, "VOGeoServerExtShapeSymbol.description"));
        }
        return vendorOptionInfo;
    }

}
