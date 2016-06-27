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
package com.sldeditor.exportdata.esri.symbols;

import org.geotools.styling.AnchorPoint;
import org.geotools.styling.Displacement;
import org.geotools.styling.Fill;
import org.geotools.styling.Font;
import org.geotools.styling.Rule;
import org.geotools.styling.TextSymbolizer;
import org.opengis.filter.expression.Expression;
import org.opengis.style.GraphicFill;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sldeditor.exportdata.esri.keys.symbols.CommonSymbolKeys;
import com.sldeditor.exportdata.esri.keys.symbols.FontSymbolKeys;
import com.sldeditor.exportdata.esri.keys.symbols.TextSymbolKeys;
import com.sldeditor.exportdata.esri.options.MXDOptions;

/**
 * Converts an Esri TextSymbol to its SLD equivalent.
 * 
 * @author Robert Ward (SCISYS)
 */
public class TextSymbol extends BaseSymbol implements EsriSymbolInterface,
EsriTextSymbolInterface {

    /**
     * Instantiates a new text symbol.
     */
    public TextSymbol()
    {
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    @Override
    public String getName() {
        return TextSymbolKeys.TEXT_SYMBOL;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.convert.esri.symbols.EsriSymbolInterface#convert(org.geotools.styling.Rule, com.google.gson.JsonElement, java.lang.String, int)
     */
    @Override
    public void convert(Rule rule, JsonElement element, String layerName, int transparency) {
        // Do nothing
    }

    /* (non-Javadoc)
     * @see com.sldeditor.convert.esri.symbols.EsriTextSymbolInterface#convert(org.geotools.styling.TextSymbolizer, com.google.gson.JsonElement, int)
     */
    @SuppressWarnings("unused")
    @Override
    public void convert(TextSymbolizer textSymbolizer, JsonElement element,
            int transparency)
    {
        if((element != null) && (textSymbolizer != null))
        {
            JsonObject obj = element.getAsJsonObject();

            double angle = getDouble(obj, CommonSymbolKeys.ANGLE);
            int breakCharacter = getInt(obj, TextSymbolKeys.BREAK_CHARACTER);
            int textCase = getInt(obj, TextSymbolKeys.CASE);
            double characterSpacing = getDouble(obj, TextSymbolKeys.CHARACTER_SPACING);
            double characterWidth = getDouble(obj, TextSymbolKeys.CHARACTER_WIDTH);
            double size = getDouble(obj, CommonSymbolKeys.SIZE);

            size += MXDOptions.getInstance().getFontSizeFactor();

            int direction = getInt(obj, TextSymbolKeys.DIRECTION);
            double flipAngle = getDouble(obj, TextSymbolKeys.FLIP_ANGLE);
            int horizontalAlignment = getInt(obj, TextSymbolKeys.HORIZONTAL_ALIGNMENT);
            int verticalAlignment = getInt(obj, TextSymbolKeys.VERTICAL_ALIGNMENT);
            double leading = getDouble(obj, TextSymbolKeys.LEADING);
            double margin = getDouble(obj, TextSymbolKeys.MARGIN);
            double maskSize = getDouble(obj, TextSymbolKeys.MASK_SIZE);
            int maskStyle = getInt(obj, TextSymbolKeys.MASK_STYLE);
            int position = getInt(obj, TextSymbolKeys.POSITION);
            double xOffset = getDouble(obj, CommonSymbolKeys.X_OFFSET);
            double yOffset = getDouble(obj, CommonSymbolKeys.Y_OFFSET);
            double shadowXOffset = getDouble(obj, TextSymbolKeys.SHADOW_X_OFFSET);
            double shadowYOffset = getDouble(obj, TextSymbolKeys.SHADOW_Y_OFFSET);
            double wordSpacing = getDouble(obj, TextSymbolKeys.WORD_SPACING);
            boolean CJKCharactersRotation = getBoolean(obj, TextSymbolKeys.CJK_CHARACTERS_ROTATION);
            boolean clip = getBoolean(obj, TextSymbolKeys.CLIP);
            boolean kerning = getBoolean(obj, TextSymbolKeys.KERNING);
            boolean rightToLeft = getBoolean(obj, TextSymbolKeys.RIGHT_TO_LEFT);
            boolean rotateWithTransform = getBoolean(obj, TextSymbolKeys.ROTATE_WITH_TRANSFORM);
            boolean typeSetting = getBoolean(obj, TextSymbolKeys.TYPE_SETTING);

            //
            // Point placement
            //
            AnchorPoint anchorPoint = styleFactory.anchorPoint(ff.literal(0), ff.literal(0));
            Displacement displacement = styleFactory.displacement(ff.literal(xOffset), ff.literal(yOffset));
            Expression rotationExpression = ff.literal(angle);

            styleFactory.pointPlacement(anchorPoint, displacement, rotationExpression);

            //
            // Font
            //
            JsonElement fontElement = obj.get(TextSymbolKeys.FONT);

            if(fontElement != null)
            {
                JsonObject fontObj = fontElement.getAsJsonObject();

                String fontName = getString(fontObj, FontSymbolKeys.FONT_NAME);
                boolean bold = getBoolean(fontObj, FontSymbolKeys.BOLD);
                boolean italic = getBoolean(fontObj, FontSymbolKeys.ITALIC);
                boolean strikeThrough = getBoolean(fontObj, FontSymbolKeys.STRIKE_THROUGH);
                boolean underline = getBoolean(fontObj, FontSymbolKeys.UNDERLINE);
                int weight = getInt(fontObj, FontSymbolKeys.FONT_WEIGHT);
                int charset = getInt(fontObj, FontSymbolKeys.CHARSET);
                int fontSize = getInt(obj, FontSymbolKeys.FONT_SIZE);

                Expression fontFamilyExpression = ff.literal(fontName);
                Expression fontSizeExpression = ff.literal(size);

                String fontStyle = "normal";
                if(italic)
                {
                    fontStyle = "italic";
                }
                Expression fontStyleExpression = ff.literal(fontStyle);

                String fontWeight = "normal";
                if(bold)
                {
                    fontWeight = "bold";
                }

                Expression fontWeightExpression = ff.literal(fontWeight);
                Font font = styleFactory.createFont(fontFamilyExpression, fontStyleExpression, fontWeightExpression, fontSizeExpression);

                textSymbolizer.setFont(font);
            }

            GraphicFill graphicFill = null;
            Expression textColour = getColour(obj.get(CommonSymbolKeys.COLOUR));
            Expression opacity = null;

            if(transparency != 0)
            {
                opacity = ff.literal(transparency / 255);
            }

            Fill fill = styleFactory.fill(graphicFill, textColour, opacity);
            textSymbolizer.setFill(fill);

            String geometryPropertyName = null;
            textSymbolizer.setGeometryPropertyName(geometryPropertyName);
            Expression shadowColour = getColour(obj.get(TextSymbolKeys.SHADOW_COLOUR));
        }
    }
}
