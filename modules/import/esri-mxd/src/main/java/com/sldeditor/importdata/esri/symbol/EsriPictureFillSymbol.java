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
import com.esri.arcgis.display.PictureFillSymbol;
import com.esri.arcgis.interop.AutomationException;
import com.google.gson.JsonObject;
import com.sldeditor.importdata.esri.CommonObjects;
import com.sldeditor.importdata.esri.ParseLayer;
import com.sldeditor.importdata.esri.keys.symbols.CommonSymbolKeys;
import com.sldeditor.importdata.esri.keys.symbols.PictureFillSymbolKeys;

/**
 * Class that converts an EsriPictureFillSymbol into JSON.
 * <p>
 * <pre>
 * {@code
 *  "PictureFillSymbol": {
 *        "mapLevel : getMapLevel(),
 *        "rop2" : getROP2(),
 *        "angle" : getAngle(),
 *        "xOffset" : getXOffset(),
 *        "yOffset" : getYOffset(),
 *        "xScale" : getXScale(),
 *        "yScale" : getYScale(),
 *        "picture" : getPicture(),
 *        "backgroundColour" : getBackgroundColor(),
 *        "bitmapTransparencyColour" : getBitmapTransparencyColor(),
 *        "colour" : getColor(),
 *        "outline" : getOutline()
 *  }
 * }
 * </pre>
 * @author Robert Ward (SCISYS)
 */
public class EsriPictureFillSymbol implements EsriSymbolInterface {

    /* (non-Javadoc)
     * @see com.sldeditor.batch.esri.symbol.EsriSymbolInterface#getSymbolClass()
     */
    @Override
    public Class<?> getSymbolClass() {
        return PictureFillSymbol.class;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.batch.esri.symbol.EsriSymbolInterface#convert(com.esri.arcgis.display.ISymbol)
     */
    @Override
    public JsonObject convert(ISymbol baseSymbol) {
        PictureFillSymbol symbol = (PictureFillSymbol) baseSymbol;

        JsonObject jsonObject = new JsonObject();
        JsonObject jsonSymbolObject = new JsonObject();

        try {
            jsonSymbolObject.addProperty(CommonSymbolKeys.MAP_LEVEL, symbol.getMapLevel());
            jsonSymbolObject.addProperty(CommonSymbolKeys.ROP2, symbol.getROP2());
            jsonSymbolObject.addProperty(CommonSymbolKeys.ANGLE, symbol.getAngle());
            jsonSymbolObject.addProperty(CommonSymbolKeys.X_OFFSET, symbol.getXOffset());
            jsonSymbolObject.addProperty(CommonSymbolKeys.Y_OFFSET, symbol.getYOffset());
            jsonSymbolObject.addProperty(PictureFillSymbolKeys.X_SCALE, symbol.getXScale());
            jsonSymbolObject.addProperty(PictureFillSymbolKeys.Y_SCALE, symbol.getYScale());

            JsonObject symbolPicture = CommonObjects.createPicture(symbol.getPicture());
            if(symbolPicture != null)
            {
                jsonSymbolObject.add(PictureFillSymbolKeys.PICTURE, symbolPicture);
            }
            JsonObject symbolBackgroundColour = CommonObjects.createColour(symbol.getBackgroundColor());
            if(symbolBackgroundColour != null)
            {
                jsonSymbolObject.add(PictureFillSymbolKeys.BACKGROUND_COLOUR, symbolBackgroundColour);
            }
            JsonObject symbolBitmapTransparencyColour = CommonObjects.createColour(symbol.getBitmapTransparencyColor());
            if(symbolBitmapTransparencyColour != null)
            {
                jsonSymbolObject.add(PictureFillSymbolKeys.BITMAP_TRANSPARENCY_COLOUR, symbolBitmapTransparencyColour);
            }
            JsonObject symbolColour = CommonObjects.createColour(symbol.getColor());
            if(symbolColour != null)
            {
                jsonSymbolObject.add(CommonSymbolKeys.COLOUR, symbolColour);
            }

            JsonObject symbolOutline = ParseLayer.createSymbol((ISymbol) symbol.getOutline());
            if(symbolOutline != null)
            {
                jsonSymbolObject.add(PictureFillSymbolKeys.OUTLINE, symbolOutline);
            }
        } catch (AutomationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        jsonObject.add(PictureFillSymbolKeys.PICTURE_FILL_SYMBOL, jsonSymbolObject);
        return jsonObject;
    }

}
