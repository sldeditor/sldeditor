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

import com.esri.arcgis.display.IMarkerSymbol;
import com.esri.arcgis.display.ISymbol;
import com.esri.arcgis.display.MultiLayerMarkerSymbol;
import com.esri.arcgis.interop.AutomationException;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sldeditor.importdata.esri.CommonObjects;
import com.sldeditor.importdata.esri.ParseLayer;
import com.sldeditor.importdata.esri.keys.symbols.CommonSymbolKeys;
import com.sldeditor.importdata.esri.keys.symbols.MultiLayerMarkerSymbolKeys;

/**
 * Class that converts an EsriMultiLayerMarkerSymbol into JSON.
 * <p>
 * <pre>
 * {@code
 *  "MultiLayerMarkerSymbol": [
 *      {
 *        "mapLevel : getMapLevel(),
 *        "rop2" : getROP2(),
 *        "marker" : getLayer(layerIndex),
 *        "angle" : getAngle(),
 *        "maskSize" : getMaskSize(),
 *        "maskStyle" : getMaskStyle(),
 *        "size" : getSize(),
 *        "xOffset" : getXOffset(),
 *        "yOffset" : getYOffset(),
 *        "colour" : getColor()
 *      },
 *      ...
 *  ]
 * }
 * </pre>
 * @author Robert Ward (SCISYS)
 */
public class EsriMultiLayerMarkerSymbol implements EsriSymbolInterface {

    /* (non-Javadoc)
     * @see com.sldeditor.batch.esri.symbol.EsriSymbolInterface#getSymbolClass()
     */
    @Override
    public Class<?> getSymbolClass() {
        return MultiLayerMarkerSymbol.class;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.batch.esri.symbol.EsriSymbolInterface#convert(com.esri.arcgis.display.ISymbol)
     */
    @Override
    public JsonObject convert(ISymbol baseSymbol) {
        MultiLayerMarkerSymbol symbol = (MultiLayerMarkerSymbol) baseSymbol;

        JsonObject jsonObject = new JsonObject();
        JsonArray jsonLayerList = new JsonArray();

        try {
            for(int layerIndex = 0; layerIndex < symbol.getLayerCount(); layerIndex ++)
            {
                JsonObject jsonSymbolObject = new JsonObject();

                IMarkerSymbol markerSymbol = symbol.getLayer(layerIndex);
                jsonSymbolObject.add(MultiLayerMarkerSymbolKeys.MARKER, ParseLayer.createSymbol((ISymbol) markerSymbol));
                jsonSymbolObject.addProperty(CommonSymbolKeys.MAP_LEVEL, symbol.getMapLevel());
                jsonSymbolObject.addProperty(CommonSymbolKeys.ROP2, symbol.getROP2());
                jsonSymbolObject.addProperty(CommonSymbolKeys.ANGLE, symbol.getAngle());
                jsonSymbolObject.addProperty(MultiLayerMarkerSymbolKeys.MASK_SIZE, symbol.getMaskSize());
                jsonSymbolObject.addProperty(MultiLayerMarkerSymbolKeys.MASK_STYLE, symbol.getMaskStyle());
                jsonSymbolObject.addProperty(CommonSymbolKeys.SIZE, symbol.getSize());
                jsonSymbolObject.addProperty(CommonSymbolKeys.X_OFFSET, symbol.getXOffset());
                jsonSymbolObject.addProperty(CommonSymbolKeys.Y_OFFSET, symbol.getYOffset());
                JsonObject symbolColour = CommonObjects.createColour(symbol.getColor());
                if(symbolColour != null)
                {
                    jsonSymbolObject.add(CommonSymbolKeys.COLOUR, symbolColour);
                }
                jsonLayerList.add(jsonSymbolObject);
            }
        } catch (AutomationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        jsonObject.add(MultiLayerMarkerSymbolKeys.MULTI_LAYER_MARKER_SYMBOL, jsonLayerList);
        return jsonObject;
    }

}
