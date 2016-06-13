/*
 * SLD Editor - The Open Source Java SLD Editor
 *
 * Copyright (C) 2016, SCISYS UK Limited
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.sldeditor.importdata.esri.symbols;

import java.util.ArrayList;
import java.util.List;

import org.geotools.styling.Displacement;
import org.geotools.styling.ExternalGraphic;
import org.geotools.styling.Fill;
import org.geotools.styling.Graphic;
import org.geotools.styling.Mark;
import org.geotools.styling.Rule;
import org.geotools.styling.Stroke;
import org.geotools.styling.Symbol;
import org.opengis.filter.expression.Expression;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sldeditor.common.console.ConsoleManager;
import com.sldeditor.importdata.esri.keys.symbols.CharacterMarkerSymbolKeys;
import com.sldeditor.importdata.esri.keys.symbols.CommonSymbolKeys;
import com.sldeditor.importdata.esri.keys.symbols.FontSymbolKeys;

/**
 * Converts an Esri CharacterMarkerSymbol to its SLD equivalent.
 * 
 * @author Robert Ward (SCISYS)
 */
public class CharacterMarkerSymbol extends BaseSymbol implements EsriSymbolInterface,
EsriMarkSymbolInterface {

    /**
     * Gets the name.
     *
     * @return the name
     */
    @Override
    public String getName() {
        return CharacterMarkerSymbolKeys.CHARACTER_MARKER_SYMBOL;
    }

    /**
     * Convert.
     *
     * @param element the element
     * @return the marker graphic
     */
    @Override
    public List<Graphic> convert(JsonElement element) {

        if(element == null) return null;

        JsonObject obj = element.getAsJsonObject();

        List<Graphic> markerList = new ArrayList<Graphic>();

        double angle = getDouble(obj, CommonSymbolKeys.ANGLE);
        double symbolSize = getDouble(obj, CommonSymbolKeys.SIZE);
        double xOffset = getDouble(obj, CommonSymbolKeys.X_OFFSET);
        double yOffset = getDouble(obj, CommonSymbolKeys.Y_OFFSET);

        JsonElement fontElement = obj.get(CharacterMarkerSymbolKeys.FONT);

        if(fontElement != null)
        {
            JsonObject fontObj = fontElement.getAsJsonObject();

            String fontName = getString(fontObj, FontSymbolKeys.FONT_NAME);
            int code = getInt(obj, CharacterMarkerSymbolKeys.CHARACTER_INDEX);

            Expression wellKnownName = ff.literal(String.format("ttf://%s#%s", fontName, code));

            // Create colour
            Expression colour = getColour(obj.get(CommonSymbolKeys.COLOUR));

            Fill fill = styleFactory.createFill(colour);
            Stroke stroke = null;

            Mark mark = styleFactory.mark(wellKnownName, fill, stroke);

            ExternalGraphic [] externalGraphics = null;
            Mark[] marks = new Mark[1];
            marks[0] = mark;

            Symbol[] symbols = null;
            Expression opacity = null;
            Expression rotation = ff.literal(angle);
            Expression size = ff.literal(symbolSize);

            Graphic graphic = styleFactory.createGraphic(externalGraphics, marks, symbols, opacity, size, rotation);

            // Displacement (offsets)
            if((xOffset > 0.0) && (yOffset > 0.0))
            {
                Expression expressionX = ff.literal(xOffset);
                Expression expressionY = ff.literal(yOffset);
                Displacement displacement = styleFactory.createDisplacement(expressionX, expressionY);

                graphic.setDisplacement(displacement);
            }
            markerList.add(graphic);
        }
        return markerList;
    }

    /**
     * Convert.
     *
     * @param rule the rule
     * @param element the element
     * @param layerName the layer name
     * @param transparency the transparency
     */
    @Override
    public void convert(Rule rule, JsonElement element, String layerName, int transparency) {
        ConsoleManager.getInstance().error(this, this.getClass().getName() + " : Not supported yet");
    }
}
