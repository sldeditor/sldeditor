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

import com.esri.arcgis.display.BalloonCallout;
import com.esri.arcgis.display.ISymbol;
import com.esri.arcgis.display.ITextBackground;
import com.esri.arcgis.interop.AutomationException;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sldeditor.importdata.esri.CommonObjects;
import com.sldeditor.importdata.esri.ParseLayer;
import com.sldeditor.importdata.esri.keys.symbols.CommonSymbolKeys;
import com.sldeditor.importdata.esri.keys.textbackground.BalloonCalloutKeys;

/**
 * Class that converts an EsriBalloonCallout into JSON.
 * <p>
 * <pre>
 * {@code
 *  "BalloonCallout": {
 *        "anchorPoint" : getAnchorPoint(),
 *        "leaderTolerance" : getLeaderTolerance(),
 *        "bottomMargin" : getBottomMargin(),
 *        "leftMargin" : getLeftMargin(),
 *        "rightMargin" : getRightMargin(),
 *        "topMargin" : getTopMargin(),
 *        "style" : getStyle(),
 *        "fillSymbol" : getSymbol(),
 *        "textSymbol" : getTextSymbol(),
 *   }
 * }
 * </pre>
 * @author Robert Ward (SCISYS)
 */
public class EsriBalloonCallout implements EsriTextBackgroundInterface {

    /**
     * Gets the background class.
     *
     * @return the background class
     */
    @Override
    public Class<?> getBackgroundClass() {
        return BalloonCallout.class;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.batch.esri.textbackground.EsriTextBackgroundInterface#convert(com.esri.arcgis.display.ITextBackground)
     */
    @Override
    public JsonObject convert(ITextBackground background) {
        BalloonCallout callout = (BalloonCallout) background;
        JsonObject jsonObject = new JsonObject();
        JsonObject jsonSymbolObject = new JsonObject();

        try {
            JsonElement anchorPoint = CommonObjects.createPoint(callout.getAnchorPoint());
            if(anchorPoint != null)
            {
                jsonSymbolObject.add(BalloonCalloutKeys.ANCHOR_POINT, anchorPoint);
            }
            jsonSymbolObject.addProperty(BalloonCalloutKeys.LEADER_TOLERANCE, callout.getLeaderTolerance());
            jsonSymbolObject.addProperty(BalloonCalloutKeys.BOTTOM_MARGIN, callout.getBottomMargin());
            jsonSymbolObject.addProperty(BalloonCalloutKeys.LEFT_MARGIN, callout.getLeftMargin());
            jsonSymbolObject.addProperty(BalloonCalloutKeys.RIGHT_MARGIN, callout.getRightMargin());
            jsonSymbolObject.addProperty(BalloonCalloutKeys.TOP_MARGIN, callout.getTopMargin());
            jsonSymbolObject.addProperty(CommonSymbolKeys.STYLE, callout.getStyle());

            JsonObject symbolFillSymbol = ParseLayer.createSymbol((ISymbol) callout.getSymbol());
            if(symbolFillSymbol != null)
            {
                jsonSymbolObject.add(BalloonCalloutKeys.FILL_SYMBOL, symbolFillSymbol);
            }
            JsonObject symbolTextSymbol = ParseLayer.createSymbol((ISymbol) callout.getTextSymbol());
            if(symbolTextSymbol != null)
            {
                jsonSymbolObject.add(BalloonCalloutKeys.TEXT_SYMBOL, symbolTextSymbol);
            }
        } catch (AutomationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        jsonObject.add(BalloonCalloutKeys.BALLOON_CALLOUT, jsonSymbolObject);
        return jsonObject;
    }
}
