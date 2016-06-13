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

import com.esri.arcgis.display.HashLineSymbol;
import com.esri.arcgis.display.ISymbol;
import com.esri.arcgis.interop.AutomationException;
import com.google.gson.JsonObject;
import com.sldeditor.importdata.esri.CommonObjects;
import com.sldeditor.importdata.esri.ParseLayer;
import com.sldeditor.importdata.esri.keys.symbols.CommonSymbolKeys;
import com.sldeditor.importdata.esri.keys.symbols.HashLineSymbolKeys;

/**
 * Class that converts an EsriHashLineSymbol into JSON.
 * <p>
 * <pre>
 * {@code
 *  "HashLineSymbol": {
 *        "mapLevel : getMapLevel(),
 *        "rop2" : getROP2(),
 *        "angle" : getAngle(),
 *        "width" : getWidth(),
 *        "hashSymbol" : getHashSymbol(),
 *        "colour" : getColor()
 *   }
 * }
 * </pre>
 * @author Robert Ward (SCISYS)
 */
public class EsriHashLineSymbol implements EsriSymbolInterface {

    /* (non-Javadoc)
     * @see com.sldeditor.batch.esri.symbol.EsriSymbolInterface#getSymbolClass()
     */
    @Override
    public Class<?> getSymbolClass() {
        return HashLineSymbol.class;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.batch.esri.symbol.EsriSymbolInterface#convert(com.esri.arcgis.display.ISymbol)
     */
    @Override
    public JsonObject convert(ISymbol baseSymbol) {
        HashLineSymbol symbol = (HashLineSymbol) baseSymbol;
        JsonObject jsonObject = new JsonObject();
        JsonObject jsonSymbolObject = new JsonObject();

        try {
            jsonSymbolObject.addProperty(CommonSymbolKeys.MAP_LEVEL, symbol.getMapLevel());
            jsonSymbolObject.addProperty(CommonSymbolKeys.ROP2, symbol.getROP2());
            jsonSymbolObject.addProperty(CommonSymbolKeys.ANGLE, symbol.getAngle());
            jsonSymbolObject.addProperty(CommonSymbolKeys.WIDTH, symbol.getWidth());
            JsonObject symbolHash = ParseLayer.createSymbol((ISymbol) symbol.getHashSymbol());
            if(symbolHash != null)
            {
                jsonSymbolObject.add(HashLineSymbolKeys.HASH_SYMBOL, symbolHash);
            }
            JsonObject symbolColour = CommonObjects.createColour(symbol.getColor());
            if(symbolColour != null)
            {
                jsonSymbolObject.add(CommonSymbolKeys.COLOUR, symbolColour);
            }
        } catch (AutomationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        jsonObject.add(HashLineSymbolKeys.HASH_LINE_SYMBOL, jsonSymbolObject);
        return jsonObject;
    }

}
