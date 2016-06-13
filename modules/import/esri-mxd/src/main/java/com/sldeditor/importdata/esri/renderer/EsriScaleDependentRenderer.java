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

import com.esri.arcgis.carto.IFeatureRenderer;
import com.esri.arcgis.carto.ScaleDependentRenderer;
import com.esri.arcgis.interop.AutomationException;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sldeditor.importdata.esri.ParseLayer;
import com.sldeditor.importdata.esri.keys.renderer.ScaleDependentRendererKeys;

/**
 * Class that converts an EsriScaleDependentRenderer into JSON.
 * <p>
 * <pre>
 * {@code
 *  "ScaleDependentRenderer": {
 *        "graduatedSymbols": "isSymbolsAreGraduated()",
 *        "renderers": [
 *          {
 *            "break": "getBreak(rendererIndex)",
 *            "renderer": "getRenderer(rendererIndex)"
 *          },
 *          ...
 *         ]
 *   }
 * }
 * </pre>
 * @author Robert Ward (SCISYS)
 */
public class EsriScaleDependentRenderer implements EsriRendererInterface {

    /** The logger. */
    private static Logger logger = Logger.getLogger(EsriScaleDependentRenderer.class);

    /**
     * Convert class breaks renderer.
     *
     * @param renderer the renderer
     * @return the json object
     */
    @Override
    public JsonObject convert(IFeatureRenderer renderer) {
        ScaleDependentRenderer scaleDependentRenderer = (ScaleDependentRenderer) renderer;
        logger.info(ScaleDependentRendererKeys.SCALE_DEPENDENT_RENDERER);

        int index = -1;

        try {
            JsonObject jsonObject = new JsonObject();
            JsonObject rendererObject = new JsonObject();

            JsonArray rendererArray = new JsonArray();

            rendererObject.addProperty(ScaleDependentRendererKeys.GRADUATED_SYMBOLS, scaleDependentRenderer.isSymbolsAreGraduated());

            // Renderer count
            for(index = 0; index < scaleDependentRenderer.getRendererCount(); index ++)
            {
                JsonObject jsonValueObject = new JsonObject();

                try
                {
                    double breakValue = scaleDependentRenderer.getBreak(index);
                    jsonValueObject.addProperty(ScaleDependentRendererKeys.BREAK, breakValue);
                }
                catch(AutomationException e)
                {

                }
                try
                {
                    JsonElement rendererElement = ParseLayer.createRenderer((IFeatureRenderer) scaleDependentRenderer.getRenderer(index));

                    if(rendererElement != null)
                    {
                        jsonValueObject.add(ScaleDependentRendererKeys.RENDERER, rendererElement);
                    }
                }
                catch(AutomationException e)
                {

                }

                rendererArray.add(jsonValueObject);
            }

            rendererObject.add(ScaleDependentRendererKeys.RENDERER_LISTS, rendererArray);

            jsonObject.add(ScaleDependentRendererKeys.SCALE_DEPENDENT_RENDERER, rendererObject);

            return jsonObject;

        } catch (AutomationException e) {
            logger.error(String.format("ClassBreaksRender index : %d", index));
            e.printStackTrace();
            logger.error(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }

        return null;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.batch.esri.renderer.EsriRendererInterface#getRendererClass()
     */
    @Override
    public Class<?> getRendererClass() {
        return ScaleDependentRenderer.class;
    }
}
