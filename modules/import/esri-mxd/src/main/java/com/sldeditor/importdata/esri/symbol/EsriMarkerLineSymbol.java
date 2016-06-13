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

import com.esri.arcgis.display.ILineDecoration;
import com.esri.arcgis.display.ILineDecorationElement;
import com.esri.arcgis.display.ISymbol;
import com.esri.arcgis.display.MarkerLineSymbol;
import com.esri.arcgis.interop.AutomationException;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sldeditor.importdata.esri.CommonObjects;
import com.sldeditor.importdata.esri.ParseLayer;
import com.sldeditor.importdata.esri.keys.symbols.CommonSymbolKeys;
import com.sldeditor.importdata.esri.keys.symbols.MarkerLineSymbolKeys;

/**
 * Class that converts an EsriMarkerLineSymbol into JSON.
 * <p>
 * <pre>
 * {@code
 *  "MarkerLineSymbol": {
 *        "mapLevel : getMapLevel(),
 *        "rop2" : getROP2(),
 *        "cap" : getCap(),
 *        "join" : getJoin(),
 *        "mitreLimit" : getMiterLimit(),
 *        "offset" : getOffset(),
 *        "decorationOnTop" : isDecorationOnTop(),
 *        "flipped" : isFlip(),
 *        "lineDecoration" : [
 *           {
 *             "positionAsRatio" : getLineDecoration().getElement(index).isPositionAsRatio(),
 *             "positionArray" : [
 *               {
 *                 "position" : getLineDecoration()..getElement(index).getPosition(positionIndex)
 *               },
 *               ...
 *             ],
 *           },
 *           ...
 *        ],
 *        "lineStartOffset" : getLineStartOffset(),
 *        "width" : getWidth(),
 *        "markerSymbol" : getMarkerSymbol(),
 *        "colour" : getColor()
 *   }
 * }
 * </pre>
 * @author Robert Ward (SCISYS)
 */
public class EsriMarkerLineSymbol implements EsriSymbolInterface {

    /* (non-Javadoc)
     * @see com.sldeditor.batch.esri.symbol.EsriSymbolInterface#getSymbolClass()
     */
    @Override
    public Class<?> getSymbolClass() {
        return MarkerLineSymbol.class;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.batch.esri.symbol.EsriSymbolInterface#convert(com.esri.arcgis.display.ISymbol)
     */
    @Override
    public JsonObject convert(ISymbol baseSymbol) {

        MarkerLineSymbol symbol = (MarkerLineSymbol) baseSymbol;

        JsonObject jsonObject = new JsonObject();
        JsonObject jsonSymbolObject = new JsonObject();

        try {
            jsonSymbolObject.addProperty(CommonSymbolKeys.MAP_LEVEL, symbol.getMapLevel());
            jsonSymbolObject.addProperty(CommonSymbolKeys.ROP2, symbol.getROP2());
            jsonSymbolObject.addProperty(MarkerLineSymbolKeys.CAP, symbol.getCap());
            jsonSymbolObject.addProperty(MarkerLineSymbolKeys.JOIN, symbol.getJoin());
            jsonSymbolObject.addProperty(MarkerLineSymbolKeys.MITRE_LIMIT, symbol.getMiterLimit());
            jsonSymbolObject.addProperty(MarkerLineSymbolKeys.OFFSET, symbol.getOffset());
            jsonSymbolObject.addProperty(MarkerLineSymbolKeys.DECORATION_ON_TOP, symbol.isDecorationOnTop());
            jsonSymbolObject.addProperty(MarkerLineSymbolKeys.FLIPPED, symbol.isFlip());

            // Line decoration
            ILineDecoration lineDecoration = symbol.getLineDecoration();
            if(lineDecoration != null)
            {
                JsonArray jsonLineDecorationArray = new JsonArray();

                for(int index = 0; index < lineDecoration.getElementCount(); index ++)
                {
                    ILineDecorationElement lineDecorationElement = lineDecoration.getElement(index);

                    JsonObject jsonLineDecorationElementObject = new JsonObject();

                    JsonArray jsonPositionArray = new JsonArray();

                    for(int position = 0; position < lineDecorationElement.getPositionCount(); position ++)
                    {
                        JsonObject jsonPosition = new JsonObject();

                        jsonPosition.addProperty(MarkerLineSymbolKeys.POSITION, lineDecorationElement.getPosition(position));

                        jsonPositionArray.add(jsonPosition);
                    }
                    jsonLineDecorationElementObject.addProperty(MarkerLineSymbolKeys.POSITION_AS_RATIO, lineDecorationElement.isPositionAsRatio());

                    jsonLineDecorationElementObject.add(MarkerLineSymbolKeys.POSITION_ARRAY, jsonPositionArray);

                    jsonLineDecorationArray.add(jsonLineDecorationElementObject);
                }

                jsonSymbolObject.add(MarkerLineSymbolKeys.LINE_DECORATION, jsonLineDecorationArray);

            }
            jsonSymbolObject.addProperty(MarkerLineSymbolKeys.LINE_START_OFFSET, symbol.getLineStartOffset());

            jsonSymbolObject.addProperty(CommonSymbolKeys.WIDTH, symbol.getWidth());
            JsonObject symbolMarker = ParseLayer.createSymbol((ISymbol) symbol.getMarkerSymbol());
            if(symbolMarker != null)
            {
                jsonSymbolObject.add(MarkerLineSymbolKeys.MARKER_SYMBOL, symbolMarker);
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
        jsonObject.add(MarkerLineSymbolKeys.MARKER_LINE_SYMBOL, jsonSymbolObject);
        return jsonObject;
    }

}
