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
import com.esri.arcgis.display.PictureLineSymbol;
import com.esri.arcgis.interop.AutomationException;
import com.google.gson.JsonObject;
import com.sldeditor.importdata.esri.CommonObjects;
import com.sldeditor.importdata.esri.keys.symbols.CommonSymbolKeys;
import com.sldeditor.importdata.esri.keys.symbols.PictureLineSymbolKeys;

/**
 * Class that converts an EsriPictureLineSymbol into JSON.
 * <p>
 * <pre>
 * {@code
 *  "PictureLineSymbol": {
 *        "mapLevel : getMapLevel(),
 *        "rop2" : getROP2(),
 *        "isRotated" : isRotate(),
 *        "swapForeGroundBackGroundColour" : isSwapForeGroundBackGroundColor(),
 *        "width" : getWidth(),
 *        "xScale" : getXScale(),
 *        "yScale" : getYScale(),
 *        "picture" : getPicture(),
 *        "backgroundColour" : getBackgroundColor(),
 *        "bitmapTransparencyColour" : getBitmapTransparencyColor(),
 *        "colour" : getColor()
 *  }
 * }
 * </pre>
 * @author Robert Ward (SCISYS)
 */
public class EsriPictureLineSymbol implements EsriSymbolInterface {

    /* (non-Javadoc)
     * @see com.sldeditor.batch.esri.symbol.EsriSymbolInterface#getSymbolClass()
     */
    @Override
    public Class<?> getSymbolClass() {
        return PictureLineSymbol.class;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.batch.esri.symbol.EsriSymbolInterface#convert(com.esri.arcgis.display.ISymbol)
     */
    @Override
    public JsonObject convert(ISymbol baseSymbol) {
        PictureLineSymbol symbol = (PictureLineSymbol) baseSymbol;

        JsonObject jsonObject = new JsonObject();
        JsonObject jsonSymbolObject = new JsonObject();

        try {
            jsonSymbolObject.addProperty(CommonSymbolKeys.MAP_LEVEL, symbol.getMapLevel());
            jsonSymbolObject.addProperty(CommonSymbolKeys.ROP2, symbol.getROP2());
            jsonSymbolObject.addProperty(PictureLineSymbolKeys.IS_ROTATED, symbol.isRotate());
            jsonSymbolObject.addProperty(PictureLineSymbolKeys.SWAP_FORE_GROUND_BACK_GROUND_COLOUR, symbol.isSwapForeGroundBackGroundColor());
            jsonSymbolObject.addProperty(CommonSymbolKeys.WIDTH, symbol.getWidth());
            jsonSymbolObject.addProperty(PictureLineSymbolKeys.X_SCALE, symbol.getXScale());
            jsonSymbolObject.addProperty(PictureLineSymbolKeys.Y_SCALE, symbol.getYScale());

            JsonObject symbolPicture = CommonObjects.createPicture(symbol.getPicture());
            if(symbolPicture != null)
            {
                jsonSymbolObject.add(PictureLineSymbolKeys.PICTURE, symbolPicture);
            }
            JsonObject symbolBackgroundColour = CommonObjects.createColour(symbol.getBackgroundColor());
            if(symbolBackgroundColour != null)
            {
                jsonSymbolObject.add(PictureLineSymbolKeys.BACKGROUND_COLOUR, symbolBackgroundColour);
            }
            JsonObject symbolBitmapTransparencyColour = CommonObjects.createColour(symbol.getBitmapTransparencyColor());
            if(symbolBitmapTransparencyColour != null)
            {
                jsonSymbolObject.add(PictureLineSymbolKeys.BITMAP_TRANSPARENCY_COLOUR, symbolBitmapTransparencyColour);
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
        jsonObject.add(PictureLineSymbolKeys.PICTURE_LINE_SYMBOL, jsonSymbolObject);
        return jsonObject;
    }

}
