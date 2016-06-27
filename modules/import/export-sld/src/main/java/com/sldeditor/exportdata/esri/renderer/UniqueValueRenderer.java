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
package com.sldeditor.exportdata.esri.renderer;

import java.util.ArrayList;
import java.util.List;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.FilterFactoryImpl;
import org.geotools.styling.Description;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.NamedLayer;
import org.geotools.styling.Rule;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactoryImpl;
import org.geotools.styling.StyledLayerDescriptor;
import org.geotools.text.Text;
import org.opengis.filter.Filter;
import org.opengis.filter.PropertyIsEqualTo;
import org.opengis.filter.expression.Expression;
import org.opengis.util.InternationalString;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sldeditor.exportdata.esri.keys.renderer.CommonRendererKeys;
import com.sldeditor.exportdata.esri.keys.renderer.UniqueValueRendererKeys;
import com.sldeditor.exportdata.esri.symbols.SymbolManager;

/**
 * Converts an Esri UniqueValueRenderer to its SLD equivalent.
 * 
 * @author Robert Ward (SCISYS)
 */
public class UniqueValueRenderer implements EsriRendererInterface {

    /** The style factory. */
    private static StyleFactoryImpl styleFactory = (StyleFactoryImpl) CommonFactoryFinder.getStyleFactory();

    /** The filter factory. */
    private static FilterFactoryImpl filterFactory = (FilterFactoryImpl) CommonFactoryFinder.getFilterFactory();

    /* (non-Javadoc)
     * @see com.sldeditor.convert.esri.EsriRendererInterface#getName()
     */
    @Override
    public String getName() {
        return UniqueValueRendererKeys.RENDERER_UNIQUEVALUE;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.convert.esri.renderer.EsriRendererInterface#convert(com.google.gson.JsonObject, java.lang.String, double, double, int)
     */
    @Override
    public StyledLayerDescriptor convert(JsonObject json, String layerName, double minScale, double maxScale, int transparency) {
        StyledLayerDescriptor sld = styleFactory.createStyledLayerDescriptor();

        NamedLayer namedLayer = styleFactory.createNamedLayer();

        boolean useDefaultSymbol = false;
        JsonElement useDefaultSymbolElement = json.get(UniqueValueRendererKeys.USE_DEFAULTSYMBOL);
        if(useDefaultSymbolElement != null)
        {
            useDefaultSymbol = useDefaultSymbolElement.getAsBoolean();
        }

        JsonElement jsonElement = json.get(CommonRendererKeys.LABEL);
        if(jsonElement != null)
        {
            namedLayer.setName(jsonElement.getAsString());
        }
        sld.addStyledLayer(namedLayer);

        Style style = styleFactory.createStyle();
        namedLayer.addStyle(style);

        List<FeatureTypeStyle> ftsList = style.featureTypeStyles();

        // style.setAbstract(json.get(IntermediateFileKeys.DESCRIPTION).getAsString());

        FeatureTypeStyle fts = styleFactory.createFeatureTypeStyle();

        ftsList.add(fts);

        JsonElement element = json.get(UniqueValueRendererKeys.VALUES);
        if(element != null)
        {
            JsonArray valueList = element.getAsJsonArray();

            for(int index = 0; index < valueList.size(); index ++)
            {
                JsonElement valueElement = valueList.get(index);
                if(valueElement != null)
                {
                    Rule rule = styleFactory.createRule();
                    JsonObject valueObj = valueElement.getAsJsonObject();

                    String value = getString(valueObj, UniqueValueRendererKeys.VALUES_VALUE);

                    String label = getString(valueObj, UniqueValueRendererKeys.VALUES_LABEL);
                    rule.setName(label);

                    createFilter(rule, json.get(UniqueValueRendererKeys.FIELDS), json.get(UniqueValueRendererKeys.FIELD_DELIMETER), value);

                    // Heading /description
                    String heading = getString(valueObj, UniqueValueRendererKeys.VALUES_HEADING);
                    @SuppressWarnings("unused")
                    String description = getString(valueObj, UniqueValueRendererKeys.VALUES_DESCRIPTION);

                    if((heading != null) && !heading.isEmpty() || (label != null) && !label.isEmpty())
                    {
                        if(label == null)
                        {
                            label = "";
                        }
                        InternationalString titleString = Text.text(label);
                        
                        if(heading == null)
                        {
                            heading = "";
                        }

                        InternationalString descriptionString = Text.text(heading);

                        Description descriptionObj = styleFactory.description(titleString, descriptionString);

                        rule.setDescription(descriptionObj);
                    }

                    if(minScale > 0.0)
                    {
                        rule.setMinScaleDenominator(minScale);
                    }

                    if(maxScale > 0.0)
                    {
                        rule.setMaxScaleDenominator(maxScale);
                    }

                    JsonElement jsonSymbolElement = valueObj.get(UniqueValueRendererKeys.VALUES_SYMBOL);
                    SymbolManager.getInstance().convertSymbols(rule, layerName, transparency, jsonSymbolElement);

                    if(useDefaultSymbol && value == null)
                    {
                        rule.setIsElseFilter(true);
                    }
                    fts.rules().add(rule);
                }
            }
        }
        return sld;
    }

    /**
     * Gets the string.
     *
     * @param valueObj the value obj
     * @param key the key
     * @return the string
     */
    private static String getString(JsonObject valueObj, String key)
    {
        if (valueObj == null) return null;

        JsonElement element = valueObj.get(key);
        if (element == null) return null;

        return element.getAsString();
    }

    /**
     * Creates the filter.
     *
     * @param rule the rule
     * @param fieldsElement the fields element
     * @param fieldDelimiterElement the field delimiter element
     * @param value the value
     */
    private void createFilter(Rule rule, JsonElement fieldsElement,
        JsonElement fieldDelimiterElement, String value)
    {
        if(fieldsElement == null) return;

        if(value == null)
        {
            rule.setElseFilter(true);
        }
        else
        {
            List<String> fieldList = new ArrayList<String>();

            JsonArray fieldArray = fieldsElement.getAsJsonArray();

            for(int fieldIndex = 0; fieldIndex < fieldArray.size(); fieldIndex ++)
            {
                JsonElement jsonFieldElement = fieldArray.get(fieldIndex);
                if(jsonFieldElement != null)
                {
                    JsonObject fieldObj = jsonFieldElement.getAsJsonObject();
                    fieldList.add(fieldObj.get("name").getAsString());
                }
            }

            String[] values = null;

            if(fieldDelimiterElement != null)
            {
                values = value.split(fieldDelimiterElement.getAsString());
            }
            else
            {
                values = new String[1];
                values[0] = value;
            }

            List<Filter> filterList = new ArrayList<Filter>();

            int index = 0;
            while(index < values.length)
            {
                Expression fieldExpression = filterFactory.property(fieldList.get(index));
                Expression valueExpression = filterFactory.literal(values[index]);

                PropertyIsEqualTo filter = filterFactory.equals(fieldExpression, valueExpression);

                filterList.add(filter);
                index ++;
            }

            Filter completeFilter = null;

            if(filterList.size() > 1)
            {
                completeFilter = filterFactory.and(filterList);
            }
            else if(filterList.size() == 1)
            {
                completeFilter = filterList.get(0);
            }

            rule.setFilter(completeFilter);
        }
    }
}
