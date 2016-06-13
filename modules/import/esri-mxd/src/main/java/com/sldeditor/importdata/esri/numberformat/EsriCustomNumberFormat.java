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
import com.esri.arcgis.system.CustomNumberFormat;
import com.esri.arcgis.system.INumberFormat;
import com.google.gson.JsonObject;
import com.sldeditor.importdata.esri.keys.numberformat.CustomNumberFormatKeys;

/**
 * Class that converts an EsriCustomNumberFormat into JSON.
 * <p>
 * <pre>
 * {@code
 *  "CustomNumberFormat": {
 *        "formatString" : "getFormatString()"
 *   }
 * }
 * </pre>
 * @author Robert Ward (SCISYS)
 */
public class EsriCustomNumberFormat implements EsriNumberFormatInterface {

    /* (non-Javadoc)
     * @see com.sldeditor.batch.esri.numberformat.EsriNumberFormatInterface#getNumberFormatClass()
     */
    @Override
    public Class<?> getNumberFormatClass() {
        return CustomNumberFormat.class;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.batch.esri.numberformat.EsriNumberFormatInterface#convert(com.esri.arcgis.system.INumberFormat)
     */
    @Override
    public JsonObject convert(INumberFormat numberFormat) {
        CustomNumberFormat format = (CustomNumberFormat) numberFormat;
        JsonObject jsonObject = new JsonObject();
        JsonObject jsonSymbolObject = new JsonObject();

        try {
            jsonSymbolObject.addProperty(CustomNumberFormatKeys.FORMAT_STRING, format.getFormatString());
        } catch (AutomationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        jsonObject.add(CustomNumberFormatKeys.CUSTOM_NUMBER_FORMAT, jsonSymbolObject);
        return jsonObject;
    }

}
