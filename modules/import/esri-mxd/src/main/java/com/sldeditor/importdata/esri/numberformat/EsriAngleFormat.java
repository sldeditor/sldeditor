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
import com.esri.arcgis.system.AngleFormat;
import com.esri.arcgis.system.INumberFormat;
import com.google.gson.JsonObject;
import com.sldeditor.importdata.esri.keys.numberformat.AngleFormatKeys;

/**
 * Class that converts an EsriAngleFormat into JSON.
 * <p>
 * <pre>
 * {@code
 *  "AngleFormat": {
 *        "alignmentOption" : getAlignmentOption(),
 *        "alignmentWidth" : getAlignmentWidth(),
 *        "roundingOption" : getRoundingOption(),
 *        "roundingValue" : getRoundingValue(),
 *        "angleInDegrees" : isAngleInDegrees(),
 *        "displayDegrees" : isDisplayDegrees(),
 *        "showPlusSign" : isShowPlusSign(),
 *        "useSeparator" : isUseSeparator(),
 *        "zeroPad" : isZeroPad(),
 *   }
 * }
 * </pre>
 * @author Robert Ward (SCISYS)
 */
public class EsriAngleFormat implements EsriNumberFormatInterface {

    /* (non-Javadoc)
     * @see com.sldeditor.batch.esri.numberformat.EsriNumberFormatInterface#getNumberFormatClass()
     */
    @Override
    public Class<?> getNumberFormatClass() {
        return AngleFormat.class;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.batch.esri.numberformat.EsriNumberFormatInterface#convert(com.esri.arcgis.system.INumberFormat)
     */
    @Override
    public JsonObject convert(INumberFormat numberFormat) {
        AngleFormat format = (AngleFormat) numberFormat;
        JsonObject jsonObject = new JsonObject();
        JsonObject jsonSymbolObject = new JsonObject();

        try {
            jsonSymbolObject.addProperty(AngleFormatKeys.ALIGNMENT_OPTION, format.getAlignmentOption());
            jsonSymbolObject.addProperty(AngleFormatKeys.ALIGNMENT_WIDTH, format.getAlignmentWidth());
            jsonSymbolObject.addProperty(AngleFormatKeys.ROUNDING_OPTION, format.getRoundingOption());
            jsonSymbolObject.addProperty(AngleFormatKeys.ROUNDING_VALUE, format.getRoundingValue());
            jsonSymbolObject.addProperty(AngleFormatKeys.ANGLE_IN_DEGREES, format.isAngleInDegrees());
            jsonSymbolObject.addProperty(AngleFormatKeys.DISPLAY_DEGREES, format.isDisplayDegrees());
            jsonSymbolObject.addProperty(AngleFormatKeys.SHOW_PLUS_SIGN, format.isShowPlusSign());
            jsonSymbolObject.addProperty(AngleFormatKeys.USE_SEPARATOR, format.isUseSeparator());
            jsonSymbolObject.addProperty(AngleFormatKeys.ZERO_PAD, format.isZeroPad());

        } catch (AutomationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        jsonObject.add(AngleFormatKeys.ANGLE_FORMAT, jsonSymbolObject);
        return jsonObject;
    }

}
