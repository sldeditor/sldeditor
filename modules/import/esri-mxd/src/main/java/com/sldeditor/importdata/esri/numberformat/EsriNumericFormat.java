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
import com.esri.arcgis.system.NumericFormat;
import com.google.gson.JsonObject;
import com.sldeditor.importdata.esri.keys.numberformat.NumericFormatKeys;

/**
 * Class that converts an EsriNumericFormat into JSON.
 * <p>
 * <pre>
 * {@code
 *  "NumericFormat": {
 *        "alignmentOption" : getAlignmentOption(),
 *        "alignmentWidth" : getAlignmentWidth(),
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
public class EsriNumericFormat implements EsriNumberFormatInterface {

    /* (non-Javadoc)
     * @see com.sldeditor.batch.esri.numberformat.EsriNumberFormatInterface#getNumberFormatClass()
     */
    @Override
    public Class<?> getNumberFormatClass() {
        return NumericFormat.class;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.batch.esri.numberformat.EsriNumberFormatInterface#convert(com.esri.arcgis.system.INumberFormat)
     */
    @Override
    public JsonObject convert(INumberFormat numberFormat) {
        NumericFormat format = (NumericFormat) numberFormat;
        JsonObject jsonObject = new JsonObject();
        JsonObject jsonSymbolObject = new JsonObject();

        try {
            jsonSymbolObject.addProperty(NumericFormatKeys.ALIGNMENT_OPTION, format.getAlignmentOption());
            jsonSymbolObject.addProperty(NumericFormatKeys.ALIGNMENT_WIDTH, format.getAlignmentWidth());
            jsonSymbolObject.addProperty(NumericFormatKeys.ROUNDING_OPTION, format.getRoundingOption());
            jsonSymbolObject.addProperty(NumericFormatKeys.ROUNDING_VALUE, format.getRoundingValue());
            jsonSymbolObject.addProperty(NumericFormatKeys.SHOW_PLUS_SIGN, format.isShowPlusSign());
            jsonSymbolObject.addProperty(NumericFormatKeys.USE_SEPARATOR, format.isUseSeparator());
            jsonSymbolObject.addProperty(NumericFormatKeys.ZERO_PAD, format.isZeroPad());

        } catch (AutomationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        jsonObject.add(NumericFormatKeys.NUMERIC_FORMAT, jsonSymbolObject);
        return jsonObject;
    }

}
