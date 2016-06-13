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
import com.esri.arcgis.system.ScientificFormat;
import com.google.gson.JsonObject;
import com.sldeditor.importdata.esri.keys.numberformat.ScientificFormatKeys;

/**
 * Class that converts an EsriScientificFormat into JSON.
 * <p>
 * <pre>
 * {@code
 *  "ScientificFormat": {
 *        "decimalPlaces" : getDecimalPlaces
 *   }
 * }
 * </pre>
 * @author Robert Ward (SCISYS)
 */
public class EsriScientificFormat implements EsriNumberFormatInterface {

    /* (non-Javadoc)
     * @see com.sldeditor.batch.esri.numberformat.EsriNumberFormatInterface#getNumberFormatClass()
     */
    @Override
    public Class<?> getNumberFormatClass() {
        return ScientificFormat.class;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.batch.esri.numberformat.EsriNumberFormatInterface#convert(com.esri.arcgis.system.INumberFormat)
     */
    @Override
    public JsonObject convert(INumberFormat numberFormat) {
        ScientificFormat format = (ScientificFormat) numberFormat;
        JsonObject jsonObject = new JsonObject();
        JsonObject jsonSymbolObject = new JsonObject();

        try {
            jsonSymbolObject.addProperty(ScientificFormatKeys.DECIMAL_PLACES, format.getDecimalPlaces());
        } catch (AutomationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        jsonObject.add(ScientificFormatKeys.SCIENTIFIC_FORMAT, jsonSymbolObject);
        return jsonObject;
    }

}
