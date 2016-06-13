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
import com.esri.arcgis.system.DirectionFormat;
import com.esri.arcgis.system.INumberFormat;
import com.google.gson.JsonObject;
import com.sldeditor.importdata.esri.keys.numberformat.DirectionFormatKeys;

/**
 * Class that converts an EsriDirectionFormat into JSON.
 * <p>
 * <pre>
 * {@code
 *  "DirectionFormat": {
 *        "decimalPlaces" : getDecimalPlaces(),
 *        "directionType" : getDirectionType(),
 *        "directionUnits" : getDirectionUnits(),
 *        "displayFormat" : getDisplayFormat()
 *   }
 * }
 * </pre>
 * @author Robert Ward (SCISYS)
 */
public class EsriDirectionFormat implements EsriNumberFormatInterface {

    /* (non-Javadoc)
     * @see com.sldeditor.batch.esri.numberformat.EsriNumberFormatInterface#getNumberFormatClass()
     */
    @Override
    public Class<?> getNumberFormatClass() {
        return DirectionFormat.class;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.batch.esri.numberformat.EsriNumberFormatInterface#convert(com.esri.arcgis.system.INumberFormat)
     */
    @Override
    public JsonObject convert(INumberFormat numberFormat) {
        DirectionFormat format = (DirectionFormat) numberFormat;
        JsonObject jsonObject = new JsonObject();
        JsonObject jsonSymbolObject = new JsonObject();

        try {
            jsonSymbolObject.addProperty(DirectionFormatKeys.DECIMAL_PLACES, format.getDecimalPlaces());
            jsonSymbolObject.addProperty(DirectionFormatKeys.DIRECTION_TYPE, format.getDirectionType());
            jsonSymbolObject.addProperty(DirectionFormatKeys.DIRECTION_UNITS, format.getDirectionUnits());
            jsonSymbolObject.addProperty(DirectionFormatKeys.DISPLAY_FORMAT, format.getDisplayFormat());

        } catch (AutomationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        jsonObject.add(DirectionFormatKeys.DIRECTION_FORMAT, jsonSymbolObject);
        return jsonObject;
    }

}
