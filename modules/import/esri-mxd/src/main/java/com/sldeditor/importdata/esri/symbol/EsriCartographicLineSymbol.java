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

import com.esri.arcgis.display.CartographicLineSymbol;
import com.esri.arcgis.display.ILineDecoration;
import com.esri.arcgis.display.ILineDecorationElement;
import com.esri.arcgis.display.ISymbol;
import com.esri.arcgis.display.ITemplate;
import com.esri.arcgis.interop.AutomationException;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sldeditor.importdata.esri.CommonObjects;
import com.sldeditor.importdata.esri.keys.symbols.CartographicLineSymbolKeys;
import com.sldeditor.importdata.esri.keys.symbols.CommonSymbolKeys;

/**
 * Class that converts an EsriCartographicLineSymbol into JSON.
 * <p>
 * <pre>
 * {@code
 *  "CartographicLineSymbol": {
 *        "colour" : getColor(),
 *        "width : getWidth(),
 *        "mapLevel : getMapLevel(),
 *        "rop2" : getROP2(),
 *        "cap" : getCap(),
 *        "join" : getJoin(),
 *        "mitreLimit" : getMiterLimit(),
 *        "lineDecoration" : [
 *          {
 *            "position" : getLineDecoration().getPosition(index)
 *          },
 *          ...
 *        ],
 *        "lineStartOffset" : getLineStartOffset(),
 *        "interval" : getTemplate().getInterval(),
 *        "template" : [
 *          {
 *            "mark" : getTemplate().getPatternElement(index)
 *            "gap" : getTemplate().getPatternElement(index),
 *          },
 *          ...
 *        ]
 *   }
 * }
 * </pre>
 * @author Robert Ward (SCISYS)
 */
public class EsriCartographicLineSymbol implements EsriSymbolInterface {

    /* (non-Javadoc)
     * @see com.sldeditor.batch.esri.symbol.EsriSymbolInterface#getSymbolClass()
     */
    @Override
    public Class<?> getSymbolClass() {
        return CartographicLineSymbol.class;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.batch.esri.symbol.EsriSymbolInterface#convert(com.esri.arcgis.display.ISymbol)
     */
    @Override
    public JsonObject convert(ISymbol baseSymbol) {
        CartographicLineSymbol symbol = (CartographicLineSymbol) baseSymbol;

        JsonObject jsonObject = new JsonObject();
        JsonObject jsonSymbolObject = new JsonObject();

        try {

            double width = symbol.getWidth();

            if(width <= 0.0) return null;

            JsonObject symbolColour = CommonObjects.createColour(symbol.getColor());
            if(symbolColour != null)
            {
                jsonSymbolObject.add(CommonSymbolKeys.COLOUR, symbolColour);
            }

            jsonSymbolObject.addProperty(CommonSymbolKeys.WIDTH, width);

            jsonSymbolObject.addProperty(CommonSymbolKeys.MAP_LEVEL, symbol.getMapLevel());
            jsonSymbolObject.addProperty(CommonSymbolKeys.ROP2, symbol.getROP2());
            jsonSymbolObject.addProperty(CartographicLineSymbolKeys.CAP, symbol.getCap());
            jsonSymbolObject.addProperty(CartographicLineSymbolKeys.JOIN, symbol.getJoin());
            jsonSymbolObject.addProperty(CartographicLineSymbolKeys.MITRE_LIMIT, symbol.getMiterLimit());
            ILineDecoration lineDecoration = symbol.getLineDecoration();

            if(lineDecoration != null)
            {
                JsonArray lineDecorationArray = new JsonArray();

                for(int index = 0; index < lineDecoration.getElementCount(); index ++)
                {
                    JsonObject jsonLineDecorationElement = new JsonObject();

                    ILineDecorationElement lineDecorationElement = lineDecoration.getElement(index);

                    JsonArray positionArray = new JsonArray();

                    for(int position = 0; position < lineDecorationElement.getPositionCount(); position ++)
                    {
                        JsonObject positionElement = new JsonObject();

                        jsonSymbolObject.addProperty(CartographicLineSymbolKeys.POSITION, lineDecorationElement.getPosition(position));

                        positionArray.add(positionElement);
                    }
                    lineDecorationArray.add(jsonLineDecorationElement);
                }
                jsonSymbolObject.add(CartographicLineSymbolKeys.LINE_DECORATION, lineDecorationArray);
            }
            jsonSymbolObject.addProperty(CartographicLineSymbolKeys.LINE_START_OFFSET, symbol.getLineStartOffset());
            ITemplate template = symbol.getTemplate();

            if(template != null)
            {
                JsonArray templateElementArray = new JsonArray();

                jsonSymbolObject.addProperty(CartographicLineSymbolKeys.INTERVAL, template.getInterval());

                for(int index = 0; index < template.getPatternElementCount(); index ++)
                {
                    JsonObject patternElement = new JsonObject();

                    double[] mark = new double[1];
                    double[] gap = new double[1];
                    template.getPatternElement(index, mark, gap);
                    patternElement.addProperty(CartographicLineSymbolKeys.MARK, mark[0]);
                    patternElement.addProperty(CartographicLineSymbolKeys.GAP, gap[0]);

                    templateElementArray.add(patternElement);
                }
                jsonSymbolObject.add(CartographicLineSymbolKeys.TEMPLATE, templateElementArray);
            }

        } catch (AutomationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        jsonObject.add(CartographicLineSymbolKeys.CARTOGRAPHIC_LINE_SYMBOL, jsonSymbolObject);
        return jsonObject;
    }

}
