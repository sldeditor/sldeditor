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

import com.esri.arcgis.display.IFillSymbol;
import com.esri.arcgis.display.ISymbol;
import com.esri.arcgis.display.MultiLayerFillSymbol;
import com.esri.arcgis.interop.AutomationException;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sldeditor.importdata.esri.CommonObjects;
import com.sldeditor.importdata.esri.ParseLayer;
import com.sldeditor.importdata.esri.keys.symbols.CommonSymbolKeys;
import com.sldeditor.importdata.esri.keys.symbols.MultiLayerFillSymbolKeys;

/**
 * Class that converts an EsriMultiLayerFillSymbol into JSON.
 * <p>
 * <pre>
 * {@code
 *  "MultiLayerFillSymbol": [
 *      {
 *        "mapLevel : getMapLevel(),
 *        "rop2" : getROP2(),
 *        "fill" : getLayer(layerIndex),
 *        "fillColour" : getColor(),
 *        "outline" : getOutline()
 *      },
 *      ...
 *  ]
 * }
 * </pre>
 * @author Robert Ward (SCISYS)
 */
public class EsriMultiLayerFillSymbol implements EsriSymbolInterface {

    /**
     * Gets the symbol class.
     *
     * @return the symbol class
     */
    @Override
    public Class<?> getSymbolClass() {
        return MultiLayerFillSymbol.class;
    }

    /**
     * Convert.
     *
     * @param baseSymbol the base symbol
     * @return the json object
     */
    @Override
    public JsonObject convert(ISymbol baseSymbol) {
        MultiLayerFillSymbol symbol = (MultiLayerFillSymbol) baseSymbol;
        JsonObject jsonObject = new JsonObject();
        JsonArray jsonLayerList = new JsonArray();

        try {
            for(int layerIndex = 0; layerIndex < symbol.getLayerCount(); layerIndex ++)
            {
                JsonObject jsonSymbolObject = new JsonObject();

                IFillSymbol fillSymbol = symbol.getLayer(layerIndex);
                jsonSymbolObject.add(MultiLayerFillSymbolKeys.FILL, ParseLayer.createSymbol((ISymbol) fillSymbol));
                jsonSymbolObject.addProperty(CommonSymbolKeys.MAP_LEVEL, symbol.getMapLevel());
                jsonSymbolObject.addProperty(CommonSymbolKeys.ROP2, symbol.getROP2());
                JsonObject symbolColour = CommonObjects.createColour(symbol.getColor());
                if(symbolColour != null)
                {
                    jsonSymbolObject.add(MultiLayerFillSymbolKeys.FILL_COLOUR, symbolColour);
                }
                JsonObject symbolOutline = ParseLayer.createSymbol((ISymbol) symbol.getOutline());
                if(symbolOutline != null)
                {
                    jsonSymbolObject.add(MultiLayerFillSymbolKeys.OUTLINE, symbolOutline);
                }

                jsonLayerList.add(jsonSymbolObject);
            }
        } catch (AutomationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        jsonObject.add(MultiLayerFillSymbolKeys.MULTI_LAYER_FILL_SYMBOL, jsonLayerList);
        return jsonObject;
    }

}
