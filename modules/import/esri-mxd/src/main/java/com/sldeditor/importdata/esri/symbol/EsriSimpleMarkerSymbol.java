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
import com.esri.arcgis.display.SimpleMarkerSymbol;
import com.esri.arcgis.interop.AutomationException;
import com.google.gson.JsonObject;
import com.sldeditor.importdata.esri.CommonObjects;
import com.sldeditor.importdata.esri.keys.symbols.CommonSymbolKeys;
import com.sldeditor.importdata.esri.keys.symbols.SimpleMarkerSymbolKeys;

/**
 * Class that converts an EsriSimpleMarkerSymbol into JSON.
 * <p>
 * <pre>
 * {@code
 *  "SimpleMarkerSymbol": {
 *        "angle" : getAngle(),
 *        "mapLevel" : getMapLevel(),
 *        "rop2" : getROP2(),
 *        "outlineSize" : getOutlineSize(),
 *        "size" : getSize(),
 *        "style" : getStyle(),
 *        "xOffset" : getXOffset(),
 *        "yOffset" : getYOffset(),
 *        "colour" : getColor(),
 *        "outlineColour" : getOutlineColor()
 *  }
 * }
 * </pre>
 * @author Robert Ward (SCISYS)
 */
public class EsriSimpleMarkerSymbol implements EsriSymbolInterface {

    /* (non-Javadoc)
     * @see com.sldeditor.batch.esri.symbol.EsriSymbolInterface#getSymbolClass()
     */
    @Override
    public Class<?> getSymbolClass() {
        return SimpleMarkerSymbol.class;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.batch.esri.symbol.EsriSymbolInterface#convert(com.esri.arcgis.display.ISymbol)
     */
    @Override
    public JsonObject convert(ISymbol baseSymbol) {
        SimpleMarkerSymbol symbol = (SimpleMarkerSymbol) baseSymbol;
        JsonObject jsonObject = new JsonObject();
        JsonObject jsonSymbolObject = new JsonObject();

        try {
            jsonSymbolObject.addProperty(CommonSymbolKeys.ANGLE, symbol.getAngle());
            jsonSymbolObject.addProperty(CommonSymbolKeys.MAP_LEVEL, symbol.getMapLevel());
            jsonSymbolObject.addProperty(SimpleMarkerSymbolKeys.OUTLINE_SIZE, symbol.getOutlineSize());
            jsonSymbolObject.addProperty(CommonSymbolKeys.ROP2, symbol.getROP2());
            jsonSymbolObject.addProperty(CommonSymbolKeys.SIZE, symbol.getSize());
            jsonSymbolObject.addProperty(CommonSymbolKeys.STYLE, symbol.getStyle());
            jsonSymbolObject.addProperty(CommonSymbolKeys.X_OFFSET, symbol.getXOffset());
            jsonSymbolObject.addProperty(CommonSymbolKeys.Y_OFFSET, symbol.getYOffset());
            JsonObject symbolColour = CommonObjects.createColour(symbol.getColor());
            if(symbolColour != null)
            {
                jsonSymbolObject.add(CommonSymbolKeys.COLOUR, symbolColour);
            }
            JsonObject symbolOutlineColour = CommonObjects.createColour(symbol.getOutlineColor());
            if(symbolOutlineColour != null)
            {
                jsonSymbolObject.add(SimpleMarkerSymbolKeys.OUTLINE_COLOUR, symbolOutlineColour);
            }
        } catch (AutomationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        jsonObject.add(SimpleMarkerSymbolKeys.SIMPLE_MARKER_SYMBOL, jsonSymbolObject);
        return jsonObject;
    }

}
