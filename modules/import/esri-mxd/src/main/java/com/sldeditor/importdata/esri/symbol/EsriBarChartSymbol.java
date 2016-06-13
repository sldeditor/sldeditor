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

import com.esri.arcgis.display.BarChartSymbol;
import com.esri.arcgis.display.IMarkerBackground;
import com.esri.arcgis.display.ISymbol;
import com.esri.arcgis.interop.AutomationException;
import com.google.gson.JsonObject;
import com.sldeditor.importdata.esri.CommonObjects;
import com.sldeditor.importdata.esri.ParseLayer;
import com.sldeditor.importdata.esri.keys.symbols.BarChartSymbolKeys;
import com.sldeditor.importdata.esri.keys.symbols.CommonSymbolKeys;

/**
 * Class that converts an EsriBarChartSymbol into JSON.
 * <p>
 * <pre>
 * {@code
 *  "BarChartSymbol": {
 *        "rop2" : getROP2(),
 *        "angle" : getAngle(),
 *        "axesSymbol" : getAxes(),
 *        "backgroundSymbol" : getBackground().getMarkerSymbol(),
 *        "maxValue" : getMaxValue(),
 *        "size" : getSize(),
 *        "spacing : getSpacing(),
 *        "thickness : getThickness(),
 *        "tilt : getTilt(),
 *        "width : getWidth(),
 *        "xOffset : getXOffset(),
 *        "yOffset : getYOffset(),
 *        "display3D : isDisplay3D(),
 *        "showAxes : isShowAxes(),
 *        "isVerticalBars : isVerticalBars(),
 *        "colour" : getColor()
 *   }
 * }
 * </pre>
 * @author Robert Ward (SCISYS)
 */
public class EsriBarChartSymbol implements EsriSymbolInterface {

    /* (non-Javadoc)
     * @see com.sldeditor.batch.esri.symbol.EsriSymbolInterface#getSymbolClass()
     */
    @Override
    public Class<?> getSymbolClass() {
        return BarChartSymbol.class;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.batch.esri.symbol.EsriSymbolInterface#convert(com.esri.arcgis.display.ISymbol)
     */
    @Override
    public JsonObject convert(ISymbol baseSymbol) {
        BarChartSymbol symbol = (BarChartSymbol) baseSymbol;

        JsonObject jsonObject = new JsonObject();
        JsonObject jsonSymbolObject = new JsonObject();

        try {
            jsonSymbolObject.addProperty(CommonSymbolKeys.ROP2, symbol.getROP2());
            jsonSymbolObject.addProperty(CommonSymbolKeys.ANGLE, symbol.getAngle());

            JsonObject symbolAxes = ParseLayer.createSymbol((ISymbol) symbol.getAxes());
            if(symbolAxes != null)
            {
                jsonSymbolObject.add(BarChartSymbolKeys.AXES_SYMBOL, symbolAxes);
            }

            IMarkerBackground markerBackground = symbol.getBackground();

            if(markerBackground != null)
            {
                JsonObject symbolBackground = ParseLayer.createSymbol((ISymbol) markerBackground.getMarkerSymbol());
                if(symbolBackground != null)
                {
                    jsonSymbolObject.add(BarChartSymbolKeys.BACKGROUND_SYMBOL, symbolBackground);
                }
            }

            jsonSymbolObject.addProperty(BarChartSymbolKeys.MAX_VALUE, symbol.getMaxValue());
            jsonSymbolObject.addProperty(CommonSymbolKeys.SIZE, symbol.getSize());

            jsonSymbolObject.addProperty(BarChartSymbolKeys.SPACING, symbol.getSpacing());
            jsonSymbolObject.addProperty(BarChartSymbolKeys.THICKNESS, symbol.getThickness());
            jsonSymbolObject.addProperty(BarChartSymbolKeys.TILT, symbol.getTilt());
            jsonSymbolObject.addProperty(CommonSymbolKeys.WIDTH, symbol.getWidth());
            jsonSymbolObject.addProperty(CommonSymbolKeys.X_OFFSET, symbol.getXOffset());
            jsonSymbolObject.addProperty(CommonSymbolKeys.Y_OFFSET, symbol.getYOffset());
            jsonSymbolObject.addProperty(BarChartSymbolKeys.DISPLAY3D, symbol.isDisplay3D());
            jsonSymbolObject.addProperty(BarChartSymbolKeys.SHOW_AXES, symbol.isShowAxes());
            jsonSymbolObject.addProperty(BarChartSymbolKeys.IS_VERTICAL_BARS, symbol.isVerticalBars());

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
        jsonObject.add(BarChartSymbolKeys.BAR_CHART_SYMBOL, jsonSymbolObject);
        return jsonObject;
    }

}
