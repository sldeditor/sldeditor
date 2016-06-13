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

import com.esri.arcgis.display.ILineSymbol;
import com.esri.arcgis.display.ISymbol;
import com.esri.arcgis.display.MultiLayerLineSymbol;
import com.esri.arcgis.interop.AutomationException;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sldeditor.importdata.esri.ParseLayer;
import com.sldeditor.importdata.esri.keys.symbols.MultiLayerLineSymbolKeys;

/**
 * Class that converts an EsriMultiLayerLineSymbol into JSON.
 * <p>
 * <pre>
 * {@code
 *  "MultiLayerLineSymbol": [
 *      {
 *        "line" : getLayer(layerIndex)
 *      },
 *      ...
 *  ]
 * }
 * </pre>
 * @author Robert Ward (SCISYS)
 */
public class EsriMultiLayerLineSymbol implements EsriSymbolInterface {

    /* (non-Javadoc)
     * @see com.sldeditor.batch.esri.symbol.EsriSymbolInterface#getSymbolClass()
     */
    @Override
    public Class<?> getSymbolClass() {
        return MultiLayerLineSymbol.class;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.batch.esri.symbol.EsriSymbolInterface#convert(com.esri.arcgis.display.ISymbol)
     */
    @Override
    public JsonObject convert(ISymbol baseSymbol) {
        MultiLayerLineSymbol symbol = (MultiLayerLineSymbol) baseSymbol;
        JsonObject jsonObject = new JsonObject();
        JsonArray jsonLayerList = new JsonArray();

        try {
            for(int layerIndex = 0; layerIndex < symbol.getLayerCount(); layerIndex ++)
            {
                JsonObject jsonSymbolObject = new JsonObject();

                ILineSymbol lineSymbol = symbol.getLayer(layerIndex);
                jsonSymbolObject.add(MultiLayerLineSymbolKeys.LINE, ParseLayer.createSymbol((ISymbol) lineSymbol));

                jsonLayerList.add(jsonSymbolObject);
            }
        } catch (AutomationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        jsonObject.add(MultiLayerLineSymbolKeys.MULTI_LAYER_LINE_SYMBOL, jsonLayerList);
        return jsonObject;
    }

}
