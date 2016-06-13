/*
 *    ConvertMXD - Part of SLD Editor application
 *    Exports an Esri MXD file to an intermediate json structure file
 *    for use in SLDEditor.
 *
 *    (C) 2016, SCISYS
 *
 */
package com.sldeditor.importdata.esri.numberformat;

import java.io.IOException;

import com.esri.arcgis.interop.AutomationException;
import com.esri.arcgis.system.INumberFormat;
import com.esri.arcgis.system.LatLonFormat;
import com.google.gson.JsonObject;
import com.sldeditor.importdata.esri.keys.numberformat.LatLonFormatKeys;

/**
 * Class that converts an EsriLatLonFormat into JSON.
 * <p>
 * <pre>
 * {@code
 *  "LatLonFormat": {
 *        "alignmentOption" : getAlignmentOption(),
 *        "alignmentWidth" : getAlignmentWidth(),
 *        "roundingOption" : getRoundingOption(),
 *        "roundingValue" : getRoundingValue(),
 *        "isLatitude" : isLatitude(),
 *        "showDirections" : isShowDirections(),
 *        "showPlusSign" : isShowPlusSign(),
 *        "showZeroMinutes" : isShowZeroMinutes(),
 *        "showZeroSeconds" : isShowZeroSeconds(),
 *        "useSeparator" : isUseSeparator(),
 *        "zeroPad" : isZeroPad()
 *   }
 * }
 * </pre>
 * @author Robert Ward (SCISYS)
 */
public class EsriLatLonFormat implements EsriNumberFormatInterface {

    /* (non-Javadoc)
     * @see com.sldeditor.batch.esri.numberformat.EsriNumberFormatInterface#getNumberFormatClass()
     */
    @Override
    public Class<?> getNumberFormatClass() {
        return LatLonFormat.class;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.batch.esri.numberformat.EsriNumberFormatInterface#convert(com.esri.arcgis.system.INumberFormat)
     */
    @Override
    public JsonObject convert(INumberFormat numberFormat) {
        LatLonFormat format = (LatLonFormat) numberFormat;
        JsonObject jsonObject = new JsonObject();
        JsonObject jsonSymbolObject = new JsonObject();

        try {
            jsonSymbolObject.addProperty(LatLonFormatKeys.ALIGNMENT_OPTION, format.getAlignmentOption());
            jsonSymbolObject.addProperty(LatLonFormatKeys.ALIGNMENT_WIDTH, format.getAlignmentWidth());
            jsonSymbolObject.addProperty(LatLonFormatKeys.ROUNDING_OPTION, format.getRoundingOption());
            jsonSymbolObject.addProperty(LatLonFormatKeys.ROUNDING_VALUE, format.getRoundingValue());
            jsonSymbolObject.addProperty(LatLonFormatKeys.IS_LATITUDE, format.isLatitude());
            jsonSymbolObject.addProperty(LatLonFormatKeys.SHOW_DIRECTIONS, format.isShowDirections());
            jsonSymbolObject.addProperty(LatLonFormatKeys.SHOW_PLUS_SIGN, format.isShowPlusSign());
            jsonSymbolObject.addProperty(LatLonFormatKeys.SHOW_ZERO_MINUTES, format.isShowZeroMinutes());
            jsonSymbolObject.addProperty(LatLonFormatKeys.SHOW_ZERO_SECONDS, format.isShowZeroSeconds());
            jsonSymbolObject.addProperty(LatLonFormatKeys.USE_SEPARATOR, format.isUseSeparator());
            jsonSymbolObject.addProperty(LatLonFormatKeys.ZERO_PAD, format.isZeroPad());

        } catch (AutomationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        jsonObject.add(LatLonFormatKeys.LAT_LON_FORMAT, jsonSymbolObject);
        return jsonObject;
    }

}
