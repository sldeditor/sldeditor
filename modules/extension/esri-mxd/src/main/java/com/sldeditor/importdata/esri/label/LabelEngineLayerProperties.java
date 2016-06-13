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
package com.sldeditor.importdata.esri.label;

import java.util.List;

import javax.measure.unit.NonSI;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.styling.Rule;
import org.geotools.styling.StyleFactoryImpl;
import org.geotools.styling.TextSymbolizer;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sldeditor.importdata.esri.keys.label.LabelEngineLayerPropertiesKey;
import com.sldeditor.importdata.esri.symbols.SymbolManager;

/**
 * Converts an Esri LabelEngineLayerProperties to its SLD equivalent.
 * 
 * @author Robert Ward (SCISYS)
 */
public class LabelEngineLayerProperties implements EsriLabelRendererInterface {

    /** The style factory. */
    private static StyleFactoryImpl styleFactory = (StyleFactoryImpl) CommonFactoryFinder.getStyleFactory();

    /** The filter factory. */
    protected static FilterFactory ff = CommonFactoryFinder.getFilterFactory();

    /* (non-Javadoc)
     * @see com.sldeditor.convert.esri.EsriRendererInterface#getName()
     */
    @Override
    public String getName() {
        return LabelEngineLayerPropertiesKey.NAME;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.extension.convert.esri.label.EsriLabelRendererInterface#convert(java.util.List, org.geotools.styling.Rule, com.google.gson.JsonElement, int)
     */
    @Override
    public void convert(List<Rule> labelRuleList, Rule rule, JsonElement json, int transparency)
    {       
        if((json != null) && (rule != null) && (labelRuleList != null))
        {
            JsonObject jsonObj = json.getAsJsonObject();

            TextSymbolizer textSymbolizer = styleFactory.createTextSymbolizer();

            textSymbolizer.setUnitOfMeasure(NonSI.PIXEL);

            textSymbolizer.setLabel(extractExpression(jsonObj));
            JsonElement jsonElement = jsonObj.get(LabelEngineLayerPropertiesKey.SYMBOL);
            SymbolManager.getInstance().convertTextSymbols(textSymbolizer, transparency, jsonElement);

            // Yes, this really is round the wrong way
            double maxScale = extractDouble(jsonObj, LabelEngineLayerPropertiesKey.ANNOTATION_MINIMUM_SCALE);
            double minScale = extractDouble(jsonObj, LabelEngineLayerPropertiesKey.ANNOTATION_MAXIMUM_SCALE);
            
            if((minScale > 0.0) || (maxScale > 0.0))
            {
                Rule labelRule = styleFactory.createRule();

                labelRule.setName(extractString(jsonObj, LabelEngineLayerPropertiesKey.CLASS));
                if(minScale > 0.0)
                {
                    labelRule.setMinScaleDenominator(minScale);
                }

                if(maxScale > 0.0)
                {
                    labelRule.setMaxScaleDenominator(maxScale);
                }
                labelRule.symbolizers().add(textSymbolizer);
                
                labelRuleList.add(labelRule);
            }
            else
            {
                rule.symbolizers().add(textSymbolizer);
            }
        }
    }

    /**
     * Extract double.
     *
     * @param jsonObj the json obj
     * @param field the field
     * @return the double
     */
    private double extractDouble(JsonObject jsonObj, String field)
    {
        double value = 0.0;
        
        if(jsonObj != null)
        {
            JsonElement element = jsonObj.get(field);
            if(element != null)
            {
                value = element.getAsDouble();
            }
        }
        return value;
    }

    /**
     * Extract string.
     *
     * @param jsonObj the json obj
     * @param field the field
     * @return the string
     */
    private String extractString(JsonObject jsonObj, String field)
    {
        String value = "";
        
        if(jsonObj != null)
        {
            JsonElement element = jsonObj.get(field);
            if(element != null)
            {
                value = element.getAsString();
            }
        }
        return value;
    }

    /**
     * Extract expression.
     *
     * @param jsonObj the json obj
     * @return the string
     */
    private Expression extractExpression(JsonObject jsonObj)
    {
        Expression expression = null;
        String expressionString = "";

        if(jsonObj != null)
        {
            expressionString = jsonObj.get(LabelEngineLayerPropertiesKey.EXPRESSION).getAsString();
            if(expressionString.startsWith("["))
            {
                expressionString = expressionString.substring(1);
                if(expressionString.endsWith("]"))
                {
                    expressionString = expressionString.substring(0, expressionString.length() - 1);
                    expression = ff.property(expressionString);
                }
            }
            else
            {
                expression = ff.literal(expressionString);
            }
        }
        return expression;
    }
}
