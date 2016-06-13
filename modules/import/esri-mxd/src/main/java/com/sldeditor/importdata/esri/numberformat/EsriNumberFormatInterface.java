/*
 *    ConvertMXD - Part of SLD Editor application
 *    Exports an Esri MXD file to an intermediate json structure file
 *    for use in SLDEditor.
 *
 *    (C) 2016, SCISYS
 *
 */
package com.sldeditor.importdata.esri.numberformat;

import com.esri.arcgis.system.INumberFormat;
import com.google.gson.JsonObject;

/**
 * The Interface EsriNumberFormatInterface.
 * 
 * @author Robert Ward (SCISYS)
 */
public interface EsriNumberFormatInterface {

    /**
     * Gets the number format class.
     *
     * @return the number format class
     */
    public Class<?> getNumberFormatClass();

    /**
     * Convert number format renderer.
     *
     * @param numberFormat the number format
     * @return the json object
     */
    public JsonObject convert(INumberFormat numberFormat);

}