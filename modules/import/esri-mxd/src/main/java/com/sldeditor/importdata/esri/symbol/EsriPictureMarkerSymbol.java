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
import com.esri.arcgis.display.PictureMarkerSymbol;
import com.esri.arcgis.interop.AutomationException;
import com.google.gson.JsonObject;
import com.sldeditor.importdata.esri.CommonObjects;
import com.sldeditor.importdata.esri.keys.symbols.CommonSymbolKeys;
import com.sldeditor.importdata.esri.keys.symbols.PictureMarkerSymbolKeys;

/**
 * Class that converts an EsriPictureMarkerSymbol into JSON.
 * <p>
 * <pre>
 * {@code
 *  "PictureMarkerSymbol": {
 *        "mapLevel : getMapLevel(),
 *        "rop2" : getROP2(),
 *        "angle" : getAngle(),
 *        "swapForeGroundBackGroundColour" : isSwapForeGroundBackGroundColor(),
 *        "size" : getSize(),
 *        "xOffset" : getXOffset(),
 *        "yOffset" : getYOffset(),
 *        "xScale" : getXScale(),
 *        "yScale" : getYScale(),
 *        "rotateWithTransform" : isRotateWithTransform(),
 *        "picture" : getPicture(),
 *        "backgroundColour" : getBackgroundColor(),
 *        "bitmapTransparencyColour" : getBitmapTransparencyColor(),
 *        "colour" : getColor()
 *  }
 * }
 * </pre>
 * @author Robert Ward (SCISYS)
 */
public class EsriPictureMarkerSymbol implements EsriSymbolInterface {

    /* (non-Javadoc)
     * @see com.sldeditor.batch.esri.symbol.EsriSymbolInterface#getSymbolClass()
     */
    @Override
    public Class<?> getSymbolClass() {
        return PictureMarkerSymbol.class;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.batch.esri.symbol.EsriSymbolInterface#convert(com.esri.arcgis.display.ISymbol)
     */
    @Override
    public JsonObject convert(ISymbol baseSymbol) {
        PictureMarkerSymbol symbol = (PictureMarkerSymbol) baseSymbol;

        JsonObject jsonObject = new JsonObject();
        JsonObject jsonSymbolObject = new JsonObject();

        try {
            jsonSymbolObject.addProperty(CommonSymbolKeys.MAP_LEVEL, symbol.getMapLevel());
            jsonSymbolObject.addProperty(CommonSymbolKeys.ROP2, symbol.getROP2());
            jsonSymbolObject.addProperty(CommonSymbolKeys.ANGLE, symbol.getAngle());
            jsonSymbolObject.addProperty(PictureMarkerSymbolKeys.SWAP_FORE_GROUND_BACK_GROUND_COLOUR, symbol.isSwapForeGroundBackGroundColor());
            jsonSymbolObject.addProperty(CommonSymbolKeys.SIZE, symbol.getSize());
            jsonSymbolObject.addProperty(CommonSymbolKeys.X_OFFSET, symbol.getXOffset());
            jsonSymbolObject.addProperty(CommonSymbolKeys.Y_OFFSET, symbol.getYOffset());
            jsonSymbolObject.addProperty(PictureMarkerSymbolKeys.X_SCALE, symbol.getXScale());
            jsonSymbolObject.addProperty(PictureMarkerSymbolKeys.Y_SCALE, symbol.getYScale());
            jsonSymbolObject.addProperty(PictureMarkerSymbolKeys.ROTATE_WITH_TRANSFORM, symbol.isRotateWithTransform());

            JsonObject symbolPicture = CommonObjects.createPicture(symbol.getPicture());
            if(symbolPicture != null)
            {
                jsonSymbolObject.add(PictureMarkerSymbolKeys.PICTURE, symbolPicture);
            }
            JsonObject symbolBackgroundColour = CommonObjects.createColour(symbol.getBackgroundColor());
            if(symbolBackgroundColour != null)
            {
                jsonSymbolObject.add(PictureMarkerSymbolKeys.BACKGROUND_COLOUR, symbolBackgroundColour);
            }
            JsonObject symbolBitmapTransparencyColour = CommonObjects.createColour(symbol.getBitmapTransparencyColor());
            if(symbolBitmapTransparencyColour != null)
            {
                jsonSymbolObject.add(PictureMarkerSymbolKeys.BITMAP_TRANSPARENCY_COLOUR, symbolBitmapTransparencyColour);
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
        jsonObject.add(PictureMarkerSymbolKeys.PICTURE_MARKER_SYMBOL, jsonSymbolObject);
        return jsonObject;
    }

}
