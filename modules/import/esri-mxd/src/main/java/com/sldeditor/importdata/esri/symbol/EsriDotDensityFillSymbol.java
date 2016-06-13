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

import com.esri.arcgis.display.DotDensityFillSymbol;
import com.esri.arcgis.display.ISymbol;
import com.esri.arcgis.interop.AutomationException;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sldeditor.importdata.esri.CommonObjects;
import com.sldeditor.importdata.esri.ParseLayer;
import com.sldeditor.importdata.esri.keys.symbols.CommonSymbolKeys;
import com.sldeditor.importdata.esri.keys.symbols.DotDensityFillSymbolKeys;

/**
 * Class that converts an EsriDotDensityFillSymbol into JSON.
 * <p>
 * <pre>
 * {@code
 *  "DotDensityFillSymbol": {
 *        "mapLevel : getMapLevel(),
 *        "rop2" : getROP2(),
 *        "dotSize" : getDotSize(),
 *        "dotSpacing" : getDotSpacing(),
 *        "maskGeometry" : getMaskGeometry(),
 *        "backgroundColour" : getBackgroundColor(),
 *        "fillColour" : getColor(),
 *        "outline" : getOutline(),
 *        "symbolList" : [
 *          {
 *            "dotCount" : getDotCount(index),
 *            "symbol" : getSymbol(index)
 *          },
 *          ...
 *        ],
 *        "useMasking" : isUseMasking(),
 *        "fixedPlacement" : isFixedPlacement()
 *   }
 * }
 * </pre>
 * @author Robert Ward (SCISYS)
 */
public class EsriDotDensityFillSymbol implements EsriSymbolInterface {

    /* (non-Javadoc)
     * @see com.sldeditor.batch.esri.symbol.EsriSymbolInterface#getSymbolClass()
     */
    @Override
    public Class<?> getSymbolClass() {
        return DotDensityFillSymbol.class;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.batch.esri.symbol.EsriSymbolInterface#convert(com.esri.arcgis.display.ISymbol)
     */
    @Override
    public JsonObject convert(ISymbol baseSymbol) {
        DotDensityFillSymbol symbol = (DotDensityFillSymbol) baseSymbol;

        JsonObject jsonObject = new JsonObject();
        JsonObject jsonSymbolObject = new JsonObject();

        try {
            jsonSymbolObject.addProperty(CommonSymbolKeys.MAP_LEVEL, symbol.getMapLevel());
            jsonSymbolObject.addProperty(CommonSymbolKeys.ROP2, symbol.getROP2());
            jsonSymbolObject.addProperty(DotDensityFillSymbolKeys.DOT_SIZE, symbol.getDotSize());
            jsonSymbolObject.addProperty(DotDensityFillSymbolKeys.DOT_SPACING, symbol.getDotSpacing());

            JsonElement geometryElement = CommonObjects.createGeometry(symbol.getMaskGeometry());
            if(geometryElement != null)
            {
                jsonSymbolObject.add(DotDensityFillSymbolKeys.MASK_GEOMETRY, geometryElement);
            }

            JsonObject backgroundColour = CommonObjects.createColour(symbol.getBackgroundColor());
            if(backgroundColour != null)
            {
                jsonSymbolObject.add(DotDensityFillSymbolKeys.BACKGROUND_COLOUR, backgroundColour);
            }

            JsonObject symbolColour = CommonObjects.createColour(symbol.getColor());
            if(symbolColour != null)
            {
                jsonSymbolObject.add(DotDensityFillSymbolKeys.FILL_COLOUR, symbolColour);
            }
            JsonObject symbolOutline = ParseLayer.createSymbol((ISymbol) symbol.getOutline());
            if(symbolOutline != null)
            {
                jsonSymbolObject.add(DotDensityFillSymbolKeys.OUTLINE, symbolOutline);
            }

            JsonArray symbolArray = new JsonArray();
            for(int index = 0; index < symbol.getSymbolCount(); index ++)
            {
                JsonObject jsonSubSymbolObject = new JsonObject();

                jsonSubSymbolObject.addProperty("dotCount", symbol.getDotCount(index));
                JsonObject symbolObject = ParseLayer.createSymbol((ISymbol) symbol.getSymbol(index));
                if(symbolObject != null)
                {
                    jsonSubSymbolObject.add(DotDensityFillSymbolKeys.SYMBOL, symbolObject);
                }

                symbolArray.add(jsonSubSymbolObject);
            }

            jsonSymbolObject.add(DotDensityFillSymbolKeys.SYMBOL_LIST, symbolArray);


            jsonSymbolObject.addProperty(DotDensityFillSymbolKeys.USE_MASKING, symbol.isUseMasking());
            jsonSymbolObject.addProperty(DotDensityFillSymbolKeys.FIXED_PLACEMENT, symbol.isFixedPlacement());

        } catch (AutomationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        jsonObject.add(DotDensityFillSymbolKeys.DOT_DENSITY_FILL_SYMBOL, jsonSymbolObject);
        return jsonObject;
    }

}
