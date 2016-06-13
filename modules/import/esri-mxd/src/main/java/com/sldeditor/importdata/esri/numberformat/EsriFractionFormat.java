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
import com.esri.arcgis.system.FractionFormat;
import com.esri.arcgis.system.INumberFormat;
import com.google.gson.JsonObject;
import com.sldeditor.importdata.esri.keys.numberformat.FractionFormatKeys;

/**
 * Class that converts an EsriFractionFormat into JSON.
 * <p>
 * <pre>
 * {@code
 *  "FractionFormat": {
 *        "fractionFactor" : getFractionFactor(),
 *        "fractionOption" : getFractionOption()
 *   }
 * }
 * </pre>
 * @author Robert Ward (SCISYS)
 */
public class EsriFractionFormat implements EsriNumberFormatInterface {

    /* (non-Javadoc)
     * @see com.sldeditor.batch.esri.numberformat.EsriNumberFormatInterface#getNumberFormatClass()
     */
    @Override
    public Class<?> getNumberFormatClass() {
        return FractionFormat.class;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.batch.esri.numberformat.EsriNumberFormatInterface#convert(com.esri.arcgis.system.INumberFormat)
     */
    @Override
    public JsonObject convert(INumberFormat numberFormat) {
        FractionFormat format = (FractionFormat) numberFormat;
        JsonObject jsonObject = new JsonObject();
        JsonObject jsonSymbolObject = new JsonObject();

        try {
            jsonSymbolObject.addProperty(FractionFormatKeys.FRACTION_FACTOR, format.getFractionFactor());
            jsonSymbolObject.addProperty(FractionFormatKeys.FRACTION_OPTION, format.getFractionOption());

        } catch (AutomationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        jsonObject.add(FractionFormatKeys.FRACTION_FORMAT, jsonSymbolObject);
        return jsonObject;
    }

}
