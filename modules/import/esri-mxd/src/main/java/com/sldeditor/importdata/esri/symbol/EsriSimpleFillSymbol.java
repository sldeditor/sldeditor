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
import com.esri.arcgis.display.SimpleFillSymbol;
import com.esri.arcgis.interop.AutomationException;
import com.google.gson.JsonObject;
import com.sldeditor.importdata.esri.CommonObjects;
import com.sldeditor.importdata.esri.ParseLayer;
import com.sldeditor.importdata.esri.keys.symbols.CommonSymbolKeys;
import com.sldeditor.importdata.esri.keys.symbols.SimpleFillSymbolKeys;

/**
 * Class that converts an EsriSimpleFillSymbol into JSON.
 * <p>
 * <pre>
 * {@code
 *  "SimpleFillSymbol": {
 *        "mapLevel" : getMapLevel(),
 *        "rop2" : getROP2(),
 *        "style" : getStyle(),
 *        "fillColour" : getColor(),
 *        "outline" : getOutline()
 *  }
 * }
 * </pre>
 * @author Robert Ward (SCISYS)
 */
public class EsriSimpleFillSymbol implements EsriSymbolInterface {

    /* (non-Javadoc)
     * @see com.sldeditor.batch.esri.symbol.EsriSymbolInterface#getSymbolClass()
     */
    @Override
    public Class<?> getSymbolClass() {
        return SimpleFillSymbol.class;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.batch.esri.symbol.EsriSymbolInterface#convert(com.esri.arcgis.display.ISymbol)
     */
    @Override
    public JsonObject convert(ISymbol baseSymbol) {
        SimpleFillSymbol symbol = (SimpleFillSymbol) baseSymbol;

        JsonObject jsonObject = new JsonObject();
        JsonObject jsonSymbolObject = new JsonObject();

        try {
            jsonSymbolObject.addProperty(CommonSymbolKeys.MAP_LEVEL, symbol.getMapLevel());
            jsonSymbolObject.addProperty(CommonSymbolKeys.ROP2, symbol.getROP2());
            jsonSymbolObject.addProperty(CommonSymbolKeys.STYLE, symbol.getStyle());

            JsonObject symbolColour = CommonObjects.createColour(symbol.getColor());
            if(symbolColour != null)
            {
                jsonSymbolObject.add(SimpleFillSymbolKeys.FILL_COLOUR, symbolColour);
            }
            JsonObject symbolOutline = ParseLayer.createSymbol((ISymbol) symbol.getOutline());
            if(symbolOutline != null)
            {
                jsonSymbolObject.add(SimpleFillSymbolKeys.OUTLINE, symbolOutline);
            }
        } catch (AutomationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        jsonObject.add(SimpleFillSymbolKeys.SIMPLE_FILL_SYMBOL, jsonSymbolObject);
        return jsonObject;
    }

}
