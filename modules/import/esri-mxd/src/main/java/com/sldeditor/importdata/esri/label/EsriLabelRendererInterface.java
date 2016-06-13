/*
 *    ConvertMXD - Part of SLD Editor application
 *    Exports an Esri MXD file to an intermediate json structure file
 *    for use in SLDEditor.
 *
 *    (C) 2016, SCISYS
 *
 */
package com.sldeditor.importdata.esri.label;

import com.esri.arcgis.carto.IAnnotateLayerProperties;
import com.esri.arcgis.carto.Map;
import com.google.gson.JsonObject;

/**
 * The Interface EsriLabelRendererInterface.
 * 
 * @author Robert Ward (SCISYS)
 */
public interface EsriLabelRendererInterface {

    /**
     * Gets the renderer class.
     *
     * @return the renderer class
     */
    public Class<?> getRendererClass();

    /**
     * Convert simple renderer.
     *
     * @param layerProperties the layer properties
     * @param map the map
     * @return the json object
     */
    public JsonObject convert(IAnnotateLayerProperties layerProperties, Map map);

}