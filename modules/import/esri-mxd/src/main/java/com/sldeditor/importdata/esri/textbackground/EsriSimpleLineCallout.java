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
import com.esri.arcgis.display.SimpleLineCallout;
import com.esri.arcgis.interop.AutomationException;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sldeditor.importdata.esri.CommonObjects;
import com.sldeditor.importdata.esri.ParseLayer;
import com.sldeditor.importdata.esri.keys.textbackground.SimpleLineCalloutKeys;

/**
 * Class that converts an EsriSimpleLineCallout into JSON.
 * <p>
 * <pre>
 * {@code
 *  "SimpleLineCallout": {
 *        "anchorPoint" : getAnchorPoint(),
 *        "leaderTolerance" : getLeaderTolerance(),
 *        "lineGeometry" : getLineGeometry(),
 *        "lineSymbol" : getLineSymbol(),
 *        "textSymbol" : getTextSymbol()
 *   }
 * }
 * </pre>
 * @author Robert Ward (SCISYS)
 */
public class EsriSimpleLineCallout implements EsriTextBackgroundInterface {

    /**
     * Gets the background class.
     *
     * @return the background class
     */
    @Override
    public Class<?> getBackgroundClass() {
        return SimpleLineCallout.class;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.batch.esri.textbackground.EsriTextBackgroundInterface#convert(com.esri.arcgis.display.ITextBackground)
     */
    @Override
    public JsonObject convert(ITextBackground background) {
        SimpleLineCallout callout = (SimpleLineCallout) background;
        JsonObject jsonObject = new JsonObject();
        JsonObject jsonSymbolObject = new JsonObject();

        try {
            JsonElement anchorPoint = CommonObjects.createPoint(callout.getAnchorPoint());
            if(anchorPoint != null)
            {
                jsonSymbolObject.add(SimpleLineCalloutKeys.ANCHOR_POINT, anchorPoint);
            }
            jsonSymbolObject.addProperty(SimpleLineCalloutKeys.LEADER_TOLERANCE, callout.getLeaderTolerance());

            JsonElement lineGeometry = CommonObjects.createGeometry(callout.getLineGeometry());
            if(lineGeometry != null)
            {
                jsonSymbolObject.add(SimpleLineCalloutKeys.LINE_GEOMETRY, lineGeometry);
            }

            JsonObject symbolLineSymbol = ParseLayer.createSymbol((ISymbol) callout.getLineSymbol());
            if(symbolLineSymbol != null)
            {
                jsonSymbolObject.add(SimpleLineCalloutKeys.LINE_SYMBOL, symbolLineSymbol);
            }
            JsonObject symbolTextSymbol = ParseLayer.createSymbol((ISymbol) callout.getTextSymbol());
            if(symbolTextSymbol != null)
            {
                jsonSymbolObject.add(SimpleLineCalloutKeys.TEXT_SYMBOL, symbolTextSymbol);
            }
        } catch (AutomationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        jsonObject.add(SimpleLineCalloutKeys.SIMPLE_LINE_CALLOUT, jsonSymbolObject);
        return jsonObject;
    }
}
