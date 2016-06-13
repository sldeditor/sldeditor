/*
 *    ConvertMXD - Part of SLD Editor application
 *    Exports an Esri MXD file to an intermediate json structure file
 *    for use in SLDEditor.
 *
 *    (C) 2016, SCISYS
 *
 */
package com.sldeditor.importdata.esri.textbackground;

import java.io.IOException;

import com.esri.arcgis.display.ISymbol;
import com.esri.arcgis.display.ITextBackground;
import com.esri.arcgis.display.MarkerTextBackground;
import com.esri.arcgis.interop.AutomationException;
import com.google.gson.JsonObject;
import com.sldeditor.importdata.esri.ParseLayer;
import com.sldeditor.importdata.esri.keys.textbackground.MarkerTextBackgroundKeys;

/**
 * Class that converts an EsriMarkerTextBackground into JSON.
 * <p>
 * <pre>
 * {@code
 *  "MarkerTextBackground": {
 *        "scaleToFit" : isScaleToFit(),
 *        "marker" : getSymbol(),
 *        "textSymbol" : getTextSymbol()
 *   }
 * }
 * </pre>
 * @author Robert Ward (SCISYS)
 */
public class EsriMarkerTextBackground implements EsriTextBackgroundInterface {

    /**
     * Gets the background class.
     *
     * @return the background class
     */
    @Override
    public Class<?> getBackgroundClass() {
        return MarkerTextBackground.class;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.batch.esri.textbackground.EsriTextBackgroundInterface#convert(com.esri.arcgis.display.ITextBackground)
     */
    @Override
    public JsonObject convert(ITextBackground background) {
        MarkerTextBackground callout = (MarkerTextBackground) background;
        JsonObject jsonObject = new JsonObject();
        JsonObject jsonSymbolObject = new JsonObject();

        try {
            jsonSymbolObject.addProperty(MarkerTextBackgroundKeys.SCALE_TO_FIT, callout.isScaleToFit());

            JsonObject symbolMarkerSymbol = ParseLayer.createSymbol((ISymbol) callout.getSymbol());
            if(symbolMarkerSymbol != null)
            {
                jsonSymbolObject.add(MarkerTextBackgroundKeys.MARKER, symbolMarkerSymbol);
            }
            JsonObject symbolTextSymbol = ParseLayer.createSymbol((ISymbol) callout.getTextSymbol());
            if(symbolTextSymbol != null)
            {
                jsonSymbolObject.add(MarkerTextBackgroundKeys.TEXT_SYMBOL, symbolTextSymbol);
            }
        } catch (AutomationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        jsonObject.add(MarkerTextBackgroundKeys.MARKER_TEXT_BACKGROUND, jsonSymbolObject);
        return jsonObject;
    }
}
