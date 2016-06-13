/*
 *    ConvertMXD - Part of SLD Editor application
 *    Exports an Esri MXD file to an intermediate json structure file
 *    for use in SLDEditor.
 *
 *    (C) 2016, SCISYS
 *
 */
package com.sldeditor.importdata.esri.symbol;

import java.io.IOException;

import com.esri.arcgis.display.ISymbol;
import com.esri.arcgis.display.MarkerFillSymbol;
import com.esri.arcgis.interop.AutomationException;
import com.google.gson.JsonObject;
import com.sldeditor.importdata.esri.CommonObjects;
import com.sldeditor.importdata.esri.ParseLayer;
import com.sldeditor.importdata.esri.keys.symbols.CommonSymbolKeys;
import com.sldeditor.importdata.esri.keys.symbols.MarkerFillSymbolKeys;

/**
 * Class that converts an EsriMarkerFillSymbol into JSON.
 * <p>
 * <pre>
 * {@code
 *  "MarkerFillSymbol": {
 *        "mapLevel : getMapLevel(),
 *        "rop2" : getROP2(),
 *        "gridAngle" : getGridAngle(),
 *        "style" : getStyle(),
 *        "xOffset" : getXOffset(),
 *        "yOffset" : getYOffset(),
 *        "xSeparation" : getXSeparation(),
 *        "ySeparation" : getYSeparation(),
 *        "colour" : getColor(),
 *        "markerSymbol" : getMarkerSymbol(),
 *        "outline" : getOutline()
 *   }
 * }
 * </pre>
 * @author Robert Ward (SCISYS)
 */
public class EsriMarkerFillSymbol implements EsriSymbolInterface {

    /* (non-Javadoc)
     * @see com.sldeditor.batch.esri.symbol.EsriSymbolInterface#getSymbolClass()
     */
    @Override
    public Class<?> getSymbolClass() {
        return MarkerFillSymbol.class;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.batch.esri.symbol.EsriSymbolInterface#convert(com.esri.arcgis.display.ISymbol)
     */
    @Override
    public JsonObject convert(ISymbol baseSymbol) {
        MarkerFillSymbol symbol = (MarkerFillSymbol) baseSymbol;

        JsonObject jsonObject = new JsonObject();
        JsonObject jsonSymbolObject = new JsonObject();

        try {
            jsonSymbolObject.addProperty(CommonSymbolKeys.MAP_LEVEL, symbol.getMapLevel());
            jsonSymbolObject.addProperty(CommonSymbolKeys.ROP2, symbol.getROP2());
            jsonSymbolObject.addProperty(MarkerFillSymbolKeys.GRID_ANGLE, symbol.getGridAngle());
            jsonSymbolObject.addProperty(CommonSymbolKeys.STYLE, symbol.getStyle());
            jsonSymbolObject.addProperty(CommonSymbolKeys.X_OFFSET, symbol.getXOffset());
            jsonSymbolObject.addProperty(CommonSymbolKeys.Y_OFFSET, symbol.getYOffset());
            jsonSymbolObject.addProperty(MarkerFillSymbolKeys.X_SEPARATION, symbol.getXSeparation());
            jsonSymbolObject.addProperty(MarkerFillSymbolKeys.Y_SEPARATION, symbol.getYSeparation());

            JsonObject symbolColour = CommonObjects.createColour(symbol.getColor());
            if(symbolColour != null)
            {
                jsonSymbolObject.add(CommonSymbolKeys.COLOUR, symbolColour);
            }
            JsonObject symbolMarker = ParseLayer.createSymbol((ISymbol) symbol.getMarkerSymbol());
            if(symbolMarker != null)
            {
                jsonSymbolObject.add(MarkerFillSymbolKeys.MARKER_SYMBOL, symbolMarker);
            }

            JsonObject symbolOutline = ParseLayer.createSymbol((ISymbol) symbol.getOutline());
            if(symbolOutline != null)
            {
                jsonSymbolObject.add(MarkerFillSymbolKeys.OUTLINE, symbolOutline);
            }
        } catch (AutomationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        jsonObject.add(MarkerFillSymbolKeys.MARKER_FILL_SYMBOL, jsonSymbolObject);
        return jsonObject;
    }
}
