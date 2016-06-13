/*
 *    ConvertMXD - Part of SLD Editor application
 *    Exports an Esri MXD file to an intermediate json structure file
 *    for use in SLDEditor.
 *
 *    (C) 2016, SCISYS
 *
 */
package com.sldeditor.importdata.esri.renderer;

import com.esri.arcgis.carto.IFeatureRenderer;
import com.google.gson.JsonObject;

/**
 * The Interface EsriRendererInterface.
 * 
 * @author Robert Ward (SCISYS)
 */
public interface EsriRendererInterface {

    /**
     * Gets the renderer class.
     *
     * @return the renderer class
     */
    public Class<?> getRendererClass();

    /**
     * Convert simple renderer.
     *
     * @param renderer the renderer
     * @return the json object
     */
    public JsonObject convert(IFeatureRenderer renderer);

}