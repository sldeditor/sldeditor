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
import com.esri.arcgis.display.LineCallout;
import com.esri.arcgis.interop.AutomationException;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sldeditor.importdata.esri.CommonObjects;
import com.sldeditor.importdata.esri.ParseLayer;
import com.sldeditor.importdata.esri.keys.symbols.CommonSymbolKeys;
import com.sldeditor.importdata.esri.keys.textbackground.LineCalloutKeys;

/**
 * Class that converts an EsriLineCallout into JSON.
 * <p>
 * <pre>
 * {@code
 *  "LineCallout": {
 *        "anchorPoint" : getAnchorPoint(),
 *        "leaderTolerance" : getLeaderTolerance(),
 *        "bottomMargin" : getBottomMargin(),
 *        "leftMargin" : getLeftMargin(),
 *        "rightMargin" : getRightMargin(),
 *        "topMargin" : getTopMargin(),
 *        "style" : getStyle(),
 *        "accentBar" : getAccentBar(),
 *        "border" : getBorder(),
 *        "marker" : getMarkerSymbol(),
 *        "textSymbol" : getTextSymbol()
 *   }
 * }
 * </pre>
 * @author Robert Ward (SCISYS)
 */
public class EsriLineCallout implements EsriTextBackgroundInterface {

    /**
     * Gets the background class.
     *
     * @return the background class
     */
    @Override
    public Class<?> getBackgroundClass() {
        return LineCallout.class;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.batch.esri.textbackground.EsriTextBackgroundInterface#convert(com.esri.arcgis.display.ITextBackground)
     */
    @Override
    public JsonObject convert(ITextBackground background) {
        LineCallout callout = (LineCallout) background;
        JsonObject jsonObject = new JsonObject();
        JsonObject jsonSymbolObject = new JsonObject();

        try {
            JsonElement anchorPoint = CommonObjects.createPoint(callout.getAnchorPoint());
            if(anchorPoint != null)
            {
                jsonSymbolObject.add(LineCalloutKeys.ANCHOR_POINT, anchorPoint);
            }
            jsonSymbolObject.addProperty(LineCalloutKeys.LEADER_TOLERANCE, callout.getLeaderTolerance());
            jsonSymbolObject.addProperty(LineCalloutKeys.BOTTOM_MARGIN, callout.getBottomMargin());
            jsonSymbolObject.addProperty(LineCalloutKeys.LEFT_MARGIN, callout.getLeftMargin());
            jsonSymbolObject.addProperty(LineCalloutKeys.RIGHT_MARGIN, callout.getRightMargin());
            jsonSymbolObject.addProperty(LineCalloutKeys.TOP_MARGIN, callout.getTopMargin());
            jsonSymbolObject.addProperty(CommonSymbolKeys.STYLE, callout.getStyle());

            JsonObject symbolAccentBarSymbol = ParseLayer.createSymbol((ISymbol) callout.getAccentBar());
            if(symbolAccentBarSymbol != null)
            {
                jsonSymbolObject.add(LineCalloutKeys.ACCENT_BAR, symbolAccentBarSymbol);
            }

            JsonObject symbolBorderSymbol = ParseLayer.createSymbol((ISymbol) callout.getBorder());
            if(symbolBorderSymbol != null)
            {
                jsonSymbolObject.add(LineCalloutKeys.BORDER, symbolBorderSymbol);
            }
            JsonObject symbolMarkerSymbol = ParseLayer.createSymbol((ISymbol) callout.getMarkerSymbol());
            if(symbolMarkerSymbol != null)
            {
                jsonSymbolObject.add(LineCalloutKeys.MARKER, symbolMarkerSymbol);
            }
            JsonObject symbolTextSymbol = ParseLayer.createSymbol((ISymbol) callout.getTextSymbol());
            if(symbolTextSymbol != null)
            {
                jsonSymbolObject.add(LineCalloutKeys.TEXT_SYMBOL, symbolTextSymbol);
            }
        } catch (AutomationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        jsonObject.add(LineCalloutKeys.LINE_CALLOUT, jsonSymbolObject);
        return jsonObject;
    }
}
