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
import com.esri.arcgis.system.PercentageFormat;
import com.google.gson.JsonObject;
import com.sldeditor.importdata.esri.keys.numberformat.PercentageFormatKeys;

/**
 * Class that converts an EsriPercentageFormat into JSON.
 * <p>
 * <pre>
 * {@code
 *  "PercentageFormat": {
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
public class EsriPercentageFormat implements EsriNumberFormatInterface {

    /* (non-Javadoc)
     * @see com.sldeditor.batch.esri.numberformat.EsriNumberFormatInterface#getNumberFormatClass()
     */
    @Override
    public Class<?> getNumberFormatClass() {
        return PercentageFormat.class;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.batch.esri.numberformat.EsriNumberFormatInterface#convert(com.esri.arcgis.system.INumberFormat)
     */
    @Override
    public JsonObject convert(INumberFormat numberFormat) {
        PercentageFormat format = (PercentageFormat) numberFormat;
        JsonObject jsonObject = new JsonObject();
        JsonObject jsonSymbolObject = new JsonObject();

        try {
            jsonSymbolObject.addProperty(PercentageFormatKeys.ALIGNMENT_OPTION, format.getAlignmentOption());
            jsonSymbolObject.addProperty(PercentageFormatKeys.ALIGNMENT_WIDTH, format.getAlignmentWidth());
            jsonSymbolObject.addProperty(PercentageFormatKeys.ROUNDING_OPTION, format.getRoundingOption());
            jsonSymbolObject.addProperty(PercentageFormatKeys.ROUNDING_VALUE, format.getRoundingValue());
            jsonSymbolObject.addProperty(PercentageFormatKeys.SHOW_PLUS_SIGN, format.isShowPlusSign());
            jsonSymbolObject.addProperty(PercentageFormatKeys.USE_SEPARATOR, format.isUseSeparator());
            jsonSymbolObject.addProperty(PercentageFormatKeys.ZERO_PAD, format.isZeroPad());

        } catch (AutomationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        jsonObject.add(PercentageFormatKeys.PERCENTAGE_FORMAT, jsonSymbolObject);
        return jsonObject;
    }

}
