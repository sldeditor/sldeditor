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

import com.esri.arcgis.display.IMarkerBackground;
import com.esri.arcgis.display.ISymbol;
import com.esri.arcgis.display.PieChartSymbol;
import com.esri.arcgis.interop.AutomationException;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sldeditor.importdata.esri.CommonObjects;
import com.sldeditor.importdata.esri.ParseLayer;
import com.sldeditor.importdata.esri.keys.symbols.CommonSymbolKeys;
import com.sldeditor.importdata.esri.keys.symbols.PieChartSymbolKeys;

/**
 * Class that converts an EsriPieChartSymbol into JSON.
 * <p>
 * <pre>
 * {@code
 *  "PieChartSymbol": {
 *        "rop2" : getROP2(),
 *        "angle" : getAngle(),
 *        "backgroundSymbol" : getMarkerSymbol(),
 *        "maxValue" : getMaxValue(),
 *        "size" : getSize(),
 *        "thickness" : getThickness(),
 *        "tilt" : getTilt(),
 *        "xOffset" : getXOffset(),
 *        "yOffset" : getYOffset(),
 *        "display3D" : isDisplay3D(),
 *        "clockwise" : isClockwise(),
 *        "useOutline" : isUseOutline(),
 *        "outline" : getOutline(),
 *        "colour" : getColor(),
 *        "symbols" : [
 *          {
 *            "value" : getValue(symbolIndex),
 *            "symbol" : getSymbol(symbolIndex)
 *          },
 *          ...
 *        ]
 *  }
 * }
 * </pre>
 * @author Robert Ward (SCISYS)
 */
public class EsriPieChartSymbol implements EsriSymbolInterface {

    /* (non-Javadoc)
     * @see com.sldeditor.batch.esri.symbol.EsriSymbolInterface#getSymbolClass()
     */
    @Override
    public Class<?> getSymbolClass() {
        return PieChartSymbol.class;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.batch.esri.symbol.EsriSymbolInterface#convert(com.esri.arcgis.display.ISymbol)
     */
    @Override
    public JsonObject convert(ISymbol baseSymbol) {
        PieChartSymbol symbol = (PieChartSymbol) baseSymbol;

        JsonObject jsonObject = new JsonObject();
        JsonObject jsonSymbolObject = new JsonObject();

        try {
            jsonSymbolObject.addProperty(CommonSymbolKeys.ROP2, symbol.getROP2());
            jsonSymbolObject.addProperty(CommonSymbolKeys.ANGLE, symbol.getAngle());

            IMarkerBackground markerBackground = symbol.getBackground();

            if(markerBackground != null)
            {
                JsonObject symbolBackground = ParseLayer.createSymbol((ISymbol) markerBackground.getMarkerSymbol());
                if(symbolBackground != null)
                {
                    jsonSymbolObject.add(PieChartSymbolKeys.BACKGROUND_SYMBOL, symbolBackground);
                }
            }

            jsonSymbolObject.addProperty(PieChartSymbolKeys.MAX_VALUE, symbol.getMaxValue());
            jsonSymbolObject.addProperty(CommonSymbolKeys.SIZE, symbol.getSize());

            jsonSymbolObject.addProperty(PieChartSymbolKeys.THICKNESS, symbol.getThickness());
            jsonSymbolObject.addProperty(PieChartSymbolKeys.TILT, symbol.getTilt());
            jsonSymbolObject.addProperty(CommonSymbolKeys.X_OFFSET, symbol.getXOffset());
            jsonSymbolObject.addProperty(CommonSymbolKeys.Y_OFFSET, symbol.getYOffset());
            jsonSymbolObject.addProperty(PieChartSymbolKeys.DISPLAY3D, symbol.isDisplay3D());
            jsonSymbolObject.addProperty(PieChartSymbolKeys.CLOCKWISE, symbol.isClockwise());
            jsonSymbolObject.addProperty(PieChartSymbolKeys.USE_OUTLINE, symbol.isUseOutline());

            if(symbol.isUseOutline())
            {
                JsonObject symbolOutline = ParseLayer.createSymbol((ISymbol) symbol.getOutline());
                if(symbolOutline != null)
                {
                    jsonSymbolObject.add(PieChartSymbolKeys.OUTLINE, symbolOutline);
                }
            }

            JsonObject symbolColour = CommonObjects.createColour(symbol.getColor());
            if(symbolColour != null)
            {
                jsonSymbolObject.add(CommonSymbolKeys.COLOUR, symbolColour);
            }

            //
            // Symbols
            //
            JsonArray symbolArray = new JsonArray();

            for(int index = 0; index < symbol.getSymbolCount(); index ++)
            {
                JsonObject symbolObject = new JsonObject();
                symbolObject.addProperty(PieChartSymbolKeys.VALUE, symbol.getValue(index));
                JsonObject jsonSymbol = ParseLayer.createSymbol((ISymbol) symbol.getSymbol(index));
                if(jsonSymbol != null)
                {
                    symbolObject.add(PieChartSymbolKeys.SYMBOL, jsonSymbol);
                }

                symbolArray.add(symbolObject);
            }

            jsonSymbolObject.add(PieChartSymbolKeys.SYMBOL_LIST, symbolArray);

        } catch (AutomationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        jsonObject.add(PieChartSymbolKeys.PIE_CHART_SYMBOL, jsonSymbolObject);
        return jsonObject;
    }

}
