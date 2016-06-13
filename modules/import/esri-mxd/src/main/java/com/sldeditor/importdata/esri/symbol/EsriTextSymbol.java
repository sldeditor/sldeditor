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
import com.esri.arcgis.display.TextSymbol;
import com.esri.arcgis.interop.AutomationException;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sldeditor.importdata.esri.CommonObjects;
import com.sldeditor.importdata.esri.ParseLayer;
import com.sldeditor.importdata.esri.keys.symbols.CommonSymbolKeys;
import com.sldeditor.importdata.esri.keys.symbols.TextSymbolKeys;

/**
 * Class that converts an EsriTextSymbol into JSON.
 * <p>
 * <pre>
 * {@code
 *  "TextSymbol": {
 *        "angle" : getAngle(),
 *        "mapLevel" : getMapLevel(),
 *        "breakCharacter" : getBreakCharacter(),
 *        "case" : getCase(),
 *        "characterSpacing" : getCharacterSpacing(),
 *        "characterWidth" : getCharacterWidth(),
 *        "rop2" : getROP2(),
 *        "size" : getSize(),
 *        "direction" : getDirection(),
 *        "flipAngle" : getFlipAngle(),
 *        "horizontalAlignment" : getHorizontalAlignment(),
 *        "verticalAlignment" : getVerticalAlignment(),
 *        "leading" : getLeading(),
 *        "margin" : getMargin(),
 *        "maskSize" : getMaskSize(),
 *        "maskStyle" : getMaskStyle(),
 *        "position" : getPosition(),
 *        "xOffset" : getXOffset(),
 *        "yOffset" : getYOffset(),
 *        "shadowXOffset" : getShadowXOffset(),
 *        "shadowYOffset" : getShadowYOffset(),
 *        "text" : getText(),
 *        "wordSpacing" : getWordSpacing(),
 *        "CJKCharactersRotation" : isCJKCharactersRotation(),
 *        "clip" : isClip(),
 *        "kerning" : isKerning(),
 *        "rightToLeft" : isRightToLeft(),
 *        "rotateWithTransform" : isRotateWithTransform(),
 *        "typeSetting" : isTypeSetting(),
 *        "background", background,
 *        "font", getFont(),
 *        "colour", getColor(),
 *        "shadowColour", getShadowColor(),
 *        "fillSymbol", getFillSymbol(),
 *        "maskSymbol", getMaskSymbol()
 *  }
 * }
 * </pre>
 * @author Robert Ward (SCISYS)
 */
public class EsriTextSymbol implements EsriSymbolInterface {

    /* (non-Javadoc)
     * @see com.sldeditor.batch.esri.symbol.EsriSymbolInterface#getSymbolClass()
     */
    @Override
    public Class<?> getSymbolClass() {
        return TextSymbol.class;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.batch.esri.symbol.EsriSymbolInterface#convert(com.esri.arcgis.display.ISymbol)
     */
    @Override
    public JsonObject convert(ISymbol baseSymbol) {
        TextSymbol symbol = (TextSymbol) baseSymbol;
        JsonObject jsonObject = new JsonObject();
        JsonObject jsonSymbolObject = new JsonObject();

        try {
            jsonSymbolObject.addProperty(CommonSymbolKeys.ANGLE, symbol.getAngle());
            jsonSymbolObject.addProperty(CommonSymbolKeys.MAP_LEVEL, symbol.getMapLevel());
            jsonSymbolObject.addProperty(TextSymbolKeys.BREAK_CHARACTER, symbol.getBreakCharacter());
            jsonSymbolObject.addProperty(TextSymbolKeys.CASE, symbol.getCase());
            jsonSymbolObject.addProperty(TextSymbolKeys.CHARACTER_SPACING, symbol.getCharacterSpacing());
            jsonSymbolObject.addProperty(TextSymbolKeys.CHARACTER_WIDTH, symbol.getCharacterWidth());
            jsonSymbolObject.addProperty(CommonSymbolKeys.ROP2, symbol.getROP2());
            jsonSymbolObject.addProperty(CommonSymbolKeys.SIZE, symbol.getSize());
            jsonSymbolObject.addProperty(TextSymbolKeys.DIRECTION, symbol.getDirection());
            jsonSymbolObject.addProperty(TextSymbolKeys.FLIP_ANGLE, symbol.getFlipAngle());
            jsonSymbolObject.addProperty(TextSymbolKeys.HORIZONTAL_ALIGNMENT, symbol.getHorizontalAlignment());
            jsonSymbolObject.addProperty(TextSymbolKeys.VERTICAL_ALIGNMENT, symbol.getVerticalAlignment());
            jsonSymbolObject.addProperty(TextSymbolKeys.LEADING, symbol.getLeading());
            jsonSymbolObject.addProperty(TextSymbolKeys.MARGIN, symbol.getMargin());
            jsonSymbolObject.addProperty(TextSymbolKeys.MASK_SIZE, symbol.getMaskSize());
            jsonSymbolObject.addProperty(TextSymbolKeys.MASK_STYLE, symbol.getMaskStyle());
            jsonSymbolObject.addProperty(TextSymbolKeys.POSITION, symbol.getPosition());
            jsonSymbolObject.addProperty(CommonSymbolKeys.X_OFFSET, symbol.getXOffset());
            jsonSymbolObject.addProperty(CommonSymbolKeys.Y_OFFSET, symbol.getYOffset());
            jsonSymbolObject.addProperty(TextSymbolKeys.SHADOW_X_OFFSET, symbol.getShadowXOffset());
            jsonSymbolObject.addProperty(TextSymbolKeys.SHADOW_Y_OFFSET, symbol.getShadowYOffset());
            jsonSymbolObject.addProperty(TextSymbolKeys.TEXT, symbol.getText());
            jsonSymbolObject.addProperty(TextSymbolKeys.WORD_SPACING, symbol.getWordSpacing());
            jsonSymbolObject.addProperty(TextSymbolKeys.CJK_CHARACTERS_ROTATION, symbol.isCJKCharactersRotation());
            jsonSymbolObject.addProperty(TextSymbolKeys.CLIP, symbol.isClip());
            jsonSymbolObject.addProperty(TextSymbolKeys.KERNING, symbol.isKerning());
            jsonSymbolObject.addProperty(TextSymbolKeys.RIGHT_TO_LEFT, symbol.isRightToLeft());
            jsonSymbolObject.addProperty(TextSymbolKeys.ROTATE_WITH_TRANSFORM, symbol.isRotateWithTransform());
            jsonSymbolObject.addProperty(TextSymbolKeys.TYPE_SETTING, symbol.isTypeSetting());

            JsonElement background = ParseLayer.createBackground(symbol.getBackground());
            if(background != null)
            {
                jsonSymbolObject.add(TextSymbolKeys.BACKGROUND, background);
            }

            JsonElement font = CommonObjects.createFont(symbol.getFont());
            if(font != null)
            {
                jsonSymbolObject.add(TextSymbolKeys.FONT, font);
            }

            JsonObject symbolColour = CommonObjects.createColour(symbol.getColor());
            if(symbolColour != null)
            {
                jsonSymbolObject.add(CommonSymbolKeys.COLOUR, symbolColour);
            }
            JsonObject symbolShadowColour = CommonObjects.createColour(symbol.getShadowColor());
            if(symbolShadowColour != null)
            {
                jsonSymbolObject.add(TextSymbolKeys.SHADOW_COLOUR, symbolShadowColour);
            }
            JsonObject symbolFillSymbol = ParseLayer.createSymbol((ISymbol) symbol.getFillSymbol());
            if(symbolFillSymbol != null)
            {
                jsonSymbolObject.add(TextSymbolKeys.FILL_SYMBOL, symbolFillSymbol);
            }
            JsonObject symbolMaskymbol = ParseLayer.createSymbol((ISymbol) symbol.getMaskSymbol());
            if(symbolMaskymbol != null)
            {
                jsonSymbolObject.add(TextSymbolKeys.MASK_SYMBOL, symbolMaskymbol);
            }
        } catch (AutomationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        jsonObject.add(TextSymbolKeys.TEXT_SYMBOL, jsonSymbolObject);
        return jsonObject;
    }

}
