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

import com.esri.arcgis.display.ArrowMarkerSymbol;
import com.esri.arcgis.display.ISymbol;
import com.esri.arcgis.interop.AutomationException;
import com.google.gson.JsonObject;
import com.sldeditor.importdata.esri.CommonObjects;
import com.sldeditor.importdata.esri.keys.symbols.ArrowMarkerSymbolKeys;
import com.sldeditor.importdata.esri.keys.symbols.CommonSymbolKeys;

/**
 * Class that converts an EsriArrowMarkerSymbol into JSON.
 * <p>
 * <pre>
 * {@code
 *  "ArrowMarkerSymbol": {
 *        "mapLevel" : getMapLevel(),
 *        "rop2" : getROP2(),
 *        "angle" : getAngle(),
 *        "length" : getLength(),
 *        "size" : getSize(),
 *        "style" : getStyle(),
 *        "width" : getWidth(),
 *        "xOffset" : getXOffset(),
 *        "yOffset" : getYOffset(),
 *        "rotateWithinTransform" : isRotateWithTransform(),
 *        "colour" : getColor()
 *   }
 * }
 * </pre>
 * @author Robert Ward (SCISYS)
 */
public class EsriArrowMarkerSymbol implements EsriSymbolInterface {

    /* (non-Javadoc)
     * @see com.sldeditor.batch.esri.symbol.EsriSymbolInterface#getSymbolClass()
     */
    @Override
    public Class<?> getSymbolClass() {
        return ArrowMarkerSymbol.class;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.batch.esri.symbol.EsriSymbolInterface#convert(com.esri.arcgis.display.ISymbol)
     */
    @Override
    public JsonObject convert(ISymbol baseSymbol) {
        ArrowMarkerSymbol symbol = (ArrowMarkerSymbol) baseSymbol;

        JsonObject jsonObject = new JsonObject();
        JsonObject jsonSymbolObject = new JsonObject();

        try {
            jsonSymbolObject.addProperty(CommonSymbolKeys.MAP_LEVEL, symbol.getMapLevel());
            jsonSymbolObject.addProperty(CommonSymbolKeys.ROP2, symbol.getROP2());
            jsonSymbolObject.addProperty(CommonSymbolKeys.ANGLE, symbol.getAngle());
            jsonSymbolObject.addProperty(CommonSymbolKeys.LENGTH, symbol.getLength());
            jsonSymbolObject.addProperty(CommonSymbolKeys.SIZE, symbol.getSize());
            jsonSymbolObject.addProperty(CommonSymbolKeys.STYLE, symbol.getStyle());
            jsonSymbolObject.addProperty(CommonSymbolKeys.WIDTH, symbol.getWidth());
            jsonSymbolObject.addProperty(CommonSymbolKeys.X_OFFSET, symbol.getXOffset());
            jsonSymbolObject.addProperty(CommonSymbolKeys.Y_OFFSET, symbol.getYOffset());
            jsonSymbolObject.addProperty(ArrowMarkerSymbolKeys.ROTATE_WITHIN_TRANSFORM, symbol.isRotateWithTransform());

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
        jsonObject.add(ArrowMarkerSymbolKeys.ARROW_MARKER_SYMBOL, jsonSymbolObject);
        return jsonObject;
    }

}
