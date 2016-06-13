/*
 *    ConvertMXD - Part of SLD Editor application
 *    Exports an Esri MXD file to an intermediate json structure file
 *    for use in SLDEditor.
 *
 *    (C) 2016, SCISYS
 *
 */
package com.sldeditor.importdata.esri.renderer;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.esri.arcgis.carto.BiUniqueValueRenderer;
import com.esri.arcgis.carto.IFeatureRenderer;
import com.esri.arcgis.interop.AutomationException;
import com.google.gson.JsonObject;
import com.sldeditor.importdata.esri.ParseLayer;
import com.sldeditor.importdata.esri.keys.renderer.BiUniqueValueRendererKeys;

/**
 * Class that converts an EsriBiUniqueValueRenderer into JSON.
 * <p>
 * <pre>
 * {@code
 *  "BiUniqueValueRenderer": {
 *        "mainRenderer": getMainRenderer(),
 *        "variationRenderer": getVariationRenderer(),
 *        "graduatedSymbols": isSymbolsAreGraduated()
 *   }
 * }
 * </pre>
 * @author Robert Ward (SCISYS)
 */
public class EsriBiUniqueValueRenderer implements EsriRendererInterface {

    /** The logger. */
    private static Logger logger = Logger.getLogger(EsriChartRenderer.class);

    /* (non-Javadoc)
     * @see com.sldeditor.batch.esri.renderer.EsriRendererInterface#convert(com.esri.arcgis.carto.IFeatureRenderer)
     */
    @Override
    public JsonObject convert(IFeatureRenderer renderer) {

        logger.info(BiUniqueValueRendererKeys.BI_UNIQUE_VALUE_RENDERER);

        BiUniqueValueRenderer biUnqueValueRenderer = (BiUniqueValueRenderer) renderer;
        try {
            JsonObject jsonObject = new JsonObject();
            JsonObject rendererObject = new JsonObject();

            rendererObject.add(BiUniqueValueRendererKeys.MAIN_RENDERER, ParseLayer.createRenderer(biUnqueValueRenderer.getMainRenderer()));
            rendererObject.add(BiUniqueValueRendererKeys.VARIATION_RENDERER, ParseLayer.createRenderer(biUnqueValueRenderer.getVariationRenderer()));

            rendererObject.addProperty(BiUniqueValueRendererKeys.GRADUATED_SYMBOLS, biUnqueValueRenderer.isSymbolsAreGraduated());

            jsonObject.add(BiUniqueValueRendererKeys.BI_UNIQUE_VALUE_RENDERER, rendererObject);

            return jsonObject;

        } catch (AutomationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.batch.esri.renderer.EsriRendererInterface#getRendererClass()
     */
    @Override
    public Class<?> getRendererClass() {
        return BiUniqueValueRenderer.class;
    }
}
