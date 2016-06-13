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

import com.esri.arcgis.display.GradientFillSymbol;
import com.esri.arcgis.display.IColorRamp;
import com.esri.arcgis.display.ISymbol;
import com.esri.arcgis.interop.AutomationException;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sldeditor.importdata.esri.CommonObjects;
import com.sldeditor.importdata.esri.ParseLayer;
import com.sldeditor.importdata.esri.keys.symbols.CommonSymbolKeys;
import com.sldeditor.importdata.esri.keys.symbols.GradientFillSymbolKeys;

/**
 * Class that converts an EsriGradientFillSymbol into JSON.
 * <p>
 * <pre>
 * {@code
 *  "GradientFillSymbol": {
 *        "mapLevel : getMapLevel(),
 *        "rop2" : getROP2(),
 *        "style" : getStyle(),
 *        "gradientAngle" : getGradientAngle(),
 *        "gradientPercentage" : getGradientPercentage(),
 *        "intervalCount" : getIntervalCount(),
 *        "colourRamp" : getColorRamp(),
 *        "fillColour" : getColor(),
 *        "outline" : getOutline()
 *   }
 * }
 * </pre>
 * @author Robert Ward (SCISYS)
 */
public class EsriGradientFillSymbol implements EsriSymbolInterface {

    /* (non-Javadoc)
     * @see com.sldeditor.batch.esri.symbol.EsriSymbolInterface#getSymbolClass()
     */
    @Override
    public Class<?> getSymbolClass() {
        return GradientFillSymbol.class;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.batch.esri.symbol.EsriSymbolInterface#convert(com.esri.arcgis.display.ISymbol)
     */
    @Override
    public JsonObject convert(ISymbol baseSymbol) {
        GradientFillSymbol symbol = (GradientFillSymbol) baseSymbol;

        JsonObject jsonObject = new JsonObject();
        JsonObject jsonSymbolObject = new JsonObject();

        try {
            jsonSymbolObject.addProperty(CommonSymbolKeys.MAP_LEVEL, symbol.getMapLevel());
            jsonSymbolObject.addProperty(CommonSymbolKeys.ROP2, symbol.getROP2());
            jsonSymbolObject.addProperty(CommonSymbolKeys.STYLE, symbol.getStyle());
            jsonSymbolObject.addProperty(GradientFillSymbolKeys.GRADIENT_ANGLE, symbol.getGradientAngle());
            jsonSymbolObject.addProperty(GradientFillSymbolKeys.GRADIENT_PERCENTAGE, symbol.getGradientPercentage());
            jsonSymbolObject.addProperty(GradientFillSymbolKeys.INTERVAL_COUNT, symbol.getIntervalCount());

            JsonElement jsonColourRamp = createColourRamp(symbol.getColorRamp());
            if(jsonColourRamp != null)
            {
                jsonSymbolObject.add(GradientFillSymbolKeys.COLOUR_RAMP, jsonColourRamp);
            }
            JsonObject symbolColour = CommonObjects.createColour(symbol.getColor());
            if(symbolColour != null)
            {
                jsonSymbolObject.add(GradientFillSymbolKeys.FILL_COLOUR, symbolColour);
            }
            JsonObject symbolOutline = ParseLayer.createSymbol((ISymbol) symbol.getOutline());
            if(symbolOutline != null)
            {
                jsonSymbolObject.add(GradientFillSymbolKeys.OUTLINE, symbolOutline);
            }
        } catch (AutomationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        jsonObject.add(GradientFillSymbolKeys.GRADIENT_FILL_SYMBOL, jsonSymbolObject);
        return jsonObject;
    }

    /**
     * Creates the colour ramp.
     *
     * @param colourRamp the colour ramp
     * @return the json element
     */
    private JsonElement createColourRamp(IColorRamp colourRamp) {
        JsonArray colourArray = null;

        if(colourRamp != null)
        {
            colourArray = new JsonArray();

            try {
                for(int index = 0; index < colourRamp.getSize(); index ++)
                {
                    JsonObject colour = CommonObjects.createColour(colourRamp.getColor(index));
                    if(colour != null)
                    {
                        colourArray.add(colour);
                    }
                }
            } catch (AutomationException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return colourArray;
    }

}
