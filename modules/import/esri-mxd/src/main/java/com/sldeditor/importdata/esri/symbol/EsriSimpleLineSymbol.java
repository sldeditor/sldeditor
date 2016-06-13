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
import com.esri.arcgis.display.SimpleLineSymbol;
import com.esri.arcgis.interop.AutomationException;
import com.google.gson.JsonObject;
import com.sldeditor.importdata.esri.CommonObjects;
import com.sldeditor.importdata.esri.keys.symbols.CommonSymbolKeys;
import com.sldeditor.importdata.esri.keys.symbols.SimpleLineSymbolKeys;

/**
 * Class that converts an EsriSimpleLineSymbol into JSON.
 * <p>
 * <pre>
 * {@code
 *  "SimpleFillSymbol": {
 *        "mapLevel" : getMapLevel(),
 *        "rop2" : getROP2(),
 *        "style" : getStyle(),
 *        "colour" : getColor(),
 *        "width" : getWidth()
 *  }
 * }
 * </pre>
 * @author Robert Ward (SCISYS)
 */
public class EsriSimpleLineSymbol implements EsriSymbolInterface {

    /* (non-Javadoc)
     * @see com.sldeditor.batch.esri.symbol.EsriSymbolInterface#getSymbolClass()
     */
    @Override
    public Class<?> getSymbolClass() {
        return SimpleLineSymbol.class;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.batch.esri.symbol.EsriSymbolInterface#convert(com.esri.arcgis.display.ISymbol)
     */
    @Override
    public JsonObject convert(ISymbol baseSymbol) {
        SimpleLineSymbol symbol = (SimpleLineSymbol) baseSymbol;

        double width = 0.0;
        try {
            width = symbol.getWidth();
        } catch (AutomationException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        if (width <= 0.0) return null;

        JsonObject jsonObject = new JsonObject();
        JsonObject jsonSymbolObject = new JsonObject();

        try {
            JsonObject symbolColour = CommonObjects.createColour(symbol.getColor());
            if(symbolColour != null)
            {
                jsonSymbolObject.add(CommonSymbolKeys.COLOUR, symbolColour);
            }

            jsonSymbolObject.addProperty(CommonSymbolKeys.WIDTH, width);

            jsonSymbolObject.addProperty(CommonSymbolKeys.MAP_LEVEL, symbol.getMapLevel());
            jsonSymbolObject.addProperty(CommonSymbolKeys.ROP2, symbol.getROP2());
            jsonSymbolObject.addProperty(CommonSymbolKeys.STYLE, symbol.getStyle());
        } catch (AutomationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        jsonObject.add(SimpleLineSymbolKeys.SIMPLE_LINE_SYMBOL, jsonSymbolObject);
        return jsonObject;
    }

}
