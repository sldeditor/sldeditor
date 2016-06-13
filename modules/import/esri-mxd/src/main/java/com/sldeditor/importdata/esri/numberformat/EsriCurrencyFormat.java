/*
 *    ConvertMXD - Part of SLD Editor application
 *    Exports an Esri MXD file to an intermediate json structure file
 *    for use in SLDEditor.
 *
 *    (C) 2016, SCISYS
 *
 */
package com.sldeditor.importdata.esri.numberformat;

import com.esri.arcgis.system.CurrencyFormat;
import com.esri.arcgis.system.INumberFormat;
import com.google.gson.JsonObject;
import com.sldeditor.importdata.esri.keys.numberformat.CurrencyFormatKeys;

/**
 * Class that converts an EsriCurrencyFormat into JSON.
 * 
 * @author Robert Ward (SCISYS)
 */
public class EsriCurrencyFormat implements EsriNumberFormatInterface {

    /* (non-Javadoc)
     * @see com.sldeditor.batch.esri.numberformat.EsriNumberFormatInterface#getNumberFormatClass()
     */
    @Override
    public Class<?> getNumberFormatClass() {
        return CurrencyFormat.class;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.batch.esri.numberformat.EsriNumberFormatInterface#convert(com.esri.arcgis.system.INumberFormat)
     */
    @Override
    public JsonObject convert(INumberFormat numberFormat) {
        @SuppressWarnings("unused")
        CurrencyFormat format = (CurrencyFormat) numberFormat;
        JsonObject jsonObject = new JsonObject();
        JsonObject jsonSymbolObject = new JsonObject();

        jsonSymbolObject.addProperty("no config", "");

        jsonObject.add(CurrencyFormatKeys.CURRENCY_FORMAT, jsonSymbolObject);
        return jsonObject;
    }

}
