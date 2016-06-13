/*
 *    ConvertMXD - Part of SLD Editor application
 *    Exports an Esri MXD file to an intermediate json structure file
 *    for use in SLDEditor.
 *
 *    (C) 2016, SCISYS
 *
 */
package com.sldeditor.importdata.esri.textbackground;

import com.esri.arcgis.display.ITextBackground;
import com.google.gson.JsonObject;

/**
 * The Interface EsriTextBackgroundInterface.
 * 
 * @author Robert Ward (SCISYS)
 */
public interface EsriTextBackgroundInterface {

    /**
     * Gets the background class.
     *
     * @return the background class
     */
    public Class<?> getBackgroundClass();

    /**
     * Convert text background renderer.
     *
     * @param background the background
     * @return the json object
     */
    public JsonObject convert(ITextBackground background);

}