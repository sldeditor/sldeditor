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

import com.esri.arcgis.display.CharacterMarkerSymbol;
import com.esri.arcgis.display.ISymbol;
import com.esri.arcgis.interop.AutomationException;
import com.esri.arcgis.support.ms.stdole.Font;
import com.google.gson.JsonObject;
import com.sldeditor.importdata.esri.CommonObjects;
import com.sldeditor.importdata.esri.keys.symbols.CharacterMarkerSymbolKeys;
import com.sldeditor.importdata.esri.keys.symbols.CommonSymbolKeys;

/**
 * Class that converts an EsriCharacterMarkerSymbol into JSON.
 * <p>
 * <pre>
 * {@code
 *  "CharacterMarkerSymbol": {
 *        "mapLevel : getMapLevel(),
 *        "rop2" : getROP2(),
 *        "angle" : getAngle(),
 *        "characterIndex" : getCharacterIndex(),
 *        "size" : getSize(),
 *        "xOffset" : getXOffset(),
 *        "yOffset" : getYOffset(),
 *        "fonty" : getFont(),
 *        "colour" : getColor()
 *   }
 * }
 * </pre>
 * @author Robert Ward (SCISYS)
 */
public class EsriCharacterMarkerSymbol implements EsriSymbolInterface {

    /* (non-Javadoc)
     * @see com.sldeditor.batch.esri.symbol.EsriSymbolInterface#getSymbolClass()
     */
    @Override
    public Class<?> getSymbolClass() {
        return CharacterMarkerSymbol.class;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.batch.esri.symbol.EsriSymbolInterface#convert(com.esri.arcgis.display.ISymbol)
     */
    @Override
    public JsonObject convert(ISymbol baseSymbol) {
        CharacterMarkerSymbol symbol = (CharacterMarkerSymbol) baseSymbol;

        JsonObject jsonObject = new JsonObject();
        JsonObject jsonSymbolObject = new JsonObject();

        try {
            jsonSymbolObject.addProperty(CommonSymbolKeys.MAP_LEVEL, symbol.getMapLevel());
            jsonSymbolObject.addProperty(CommonSymbolKeys.ROP2, symbol.getROP2());
            jsonSymbolObject.addProperty(CommonSymbolKeys.ANGLE, symbol.getAngle());
            jsonSymbolObject.addProperty(CharacterMarkerSymbolKeys.CHARACTER_INDEX, symbol.getCharacterIndex());
            jsonSymbolObject.addProperty(CommonSymbolKeys.SIZE, symbol.getSize());
            jsonSymbolObject.addProperty(CommonSymbolKeys.X_OFFSET, symbol.getXOffset());
            jsonSymbolObject.addProperty(CommonSymbolKeys.Y_OFFSET, symbol.getYOffset());

            Font font = symbol.getFont();
            if(font != null)
            {
                jsonSymbolObject.add(CharacterMarkerSymbolKeys.FONT, CommonObjects.createFont(font));

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
        jsonObject.add(CharacterMarkerSymbolKeys.CHARACTER_MARKER_SYMBOL, jsonSymbolObject);
        return jsonObject;
    }

}
