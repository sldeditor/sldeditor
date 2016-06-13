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
import com.esri.arcgis.system.RateFormat;
import com.google.gson.JsonObject;
import com.sldeditor.importdata.esri.keys.numberformat.RateFormatKeys;

/**
 * Class that converts an EsriRateFormat into JSON.
 * <p>
 * <pre>
 * {@code
 *  "RateFormat": {
 *        "alignmentOption" : getAlignmentOption(),
 *        "alignmentWidth" : getAlignmentWidth(),
 *        "rateFactor" : getRateFactor(),
 *        "rateString" : getRateString(),
 *        "roundingOption" : getRoundingOption(),
 *        "roundingValue" : getRoundingValue(),
 *        "showPlusSign" : isShowPlusSign(),
 *        "useSeparator" : isUseSeparator(),
 *        "zeroPad" : isZeroPad()
 *   }
 * }
 * </pre>
 * @author Robert Ward (SCISYS)
 */
public class EsriRateFormat implements EsriNumberFormatInterface {

    /* (non-Javadoc)
     * @see com.sldeditor.batch.esri.numberformat.EsriNumberFormatInterface#getNumberFormatClass()
     */
    @Override
    public Class<?> getNumberFormatClass() {
        return RateFormat.class;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.batch.esri.numberformat.EsriNumberFormatInterface#convert(com.esri.arcgis.system.INumberFormat)
     */
    @Override
    public JsonObject convert(INumberFormat numberFormat) {
        RateFormat format = (RateFormat) numberFormat;
        JsonObject jsonObject = new JsonObject();
        JsonObject jsonSymbolObject = new JsonObject();

        try {
            jsonSymbolObject.addProperty(RateFormatKeys.ALIGNMENT_OPTION, format.getAlignmentOption());
            jsonSymbolObject.addProperty(RateFormatKeys.ALIGNMENT_WIDTH, format.getAlignmentWidth());
            jsonSymbolObject.addProperty(RateFormatKeys.RATE_FACTOR, format.getRateFactor());
            jsonSymbolObject.addProperty(RateFormatKeys.RATE_STRING, format.getRateString());
            jsonSymbolObject.addProperty(RateFormatKeys.ROUNDING_OPTION, format.getRoundingOption());
            jsonSymbolObject.addProperty(RateFormatKeys.ROUNDING_VALUE, format.getRoundingValue());
            jsonSymbolObject.addProperty(RateFormatKeys.SHOW_PLUS_SIGN, format.isShowPlusSign());
            jsonSymbolObject.addProperty(RateFormatKeys.USE_SEPARATOR, format.isUseSeparator());
            jsonSymbolObject.addProperty(RateFormatKeys.ZERO_PAD, format.isZeroPad());
        } catch (AutomationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        jsonObject.add(RateFormatKeys.RATE_FORMAT, jsonSymbolObject);
        return jsonObject;
    }

}
