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
package com.sldeditor.importdata.esri;

import java.io.IOException;
import java.util.HashMap;

import com.esri.arcgis.carto.FeatureLayer;
import com.esri.arcgis.carto.GroupLayer;
import com.esri.arcgis.carto.IAnnotateLayerProperties;
import com.esri.arcgis.carto.IAnnotateLayerPropertiesCollection;
import com.esri.arcgis.carto.IFeatureRenderer;
import com.esri.arcgis.carto.IGeoFeatureLayer;
import com.esri.arcgis.carto.ILayer;
import com.esri.arcgis.carto.Map;
import com.esri.arcgis.display.ISymbol;
import com.esri.arcgis.display.ITextBackground;
import com.esri.arcgis.geodatabase.IField;
import com.esri.arcgis.geodatabase.IFieldInfo;
import com.esri.arcgis.geodatabase.IWorkspace;
import com.esri.arcgis.interop.AutomationException;
import com.esri.arcgis.system.INumberFormat;
import com.esri.arcgis.system.IPropertySet;
import com.esri.arcgis.system.NumericFormat;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sldeditor.importdata.esri.keys.DatasourceKeys;
import com.sldeditor.importdata.esri.label.EsriLabelRendererInterface;
import com.sldeditor.importdata.esri.numberformat.EsriNumberFormatInterface;
import com.sldeditor.importdata.esri.renderer.EsriRendererInterface;
import com.sldeditor.importdata.esri.symbol.EsriSymbolInterface;
import com.sldeditor.importdata.esri.textbackground.EsriTextBackgroundInterface;

/**
 * The Class ParseLayer.
 *
 * @author Robert Ward (SCISYS)
 */
public class ParseLayer {

    /** The instance. */
    private static ParseLayer instance = null;

    /** The field type map. */
    private static java.util.Map<Integer, String> fieldTypeMap = new HashMap<Integer,String>();

    private ConversionData data = null;

    /**
     * Instantiates a new parses the layer.
     * @param data 
     */
    public ParseLayer(ConversionData data)
    {
        this.data = data;
        instance = this;
    }

    /**
     * Convert layer.
     *
     * @param count the count
     * @param total the total
     * @param jsonLayerlist the json layerlist
     * @param layer the layer
     * @param mxdMap the mxd map
     */
    public void convertLayer(int count, int total, JsonArray jsonLayerlist, ILayer layer, Map mxdMap) {
        if(layer == null)
        {
            Progress.error(getClass(), "convertLayer() : layer == null");
        }

        try {
            Progress.setProgress(count);
            Progress.info(getClass(), String.format("Reading layer (%d/%d) : %s", count, total, layer.getName()));

            if(layer instanceof GroupLayer)
            {
                Progress.info(getClass(), "Group Layer");
            }
            else if(layer instanceof FeatureLayer)
            {
                JsonObject jsonLayerObject = new JsonObject();

                jsonLayerObject.addProperty("name", layer.getName());
                jsonLayerObject.addProperty("maxScale", layer.getMinimumScale());
                jsonLayerObject.addProperty("minScale", layer.getMaximumScale());

                FeatureLayer featureLayer = (FeatureLayer) layer;

                jsonLayerObject.addProperty("definitionExpression", featureLayer.getDefinitionExpression());
                jsonLayerObject.addProperty("transparency", featureLayer.getTransparency());

                IFeatureRenderer renderer = featureLayer.getRenderer();

                JsonObject jsonRenderer = internal_createRenderer(renderer);

                if(jsonRenderer != null)
                {
                    jsonLayerObject.add("renderer", jsonRenderer);
                }

                // Labels
                JsonArray jsonLabelRendererArray = new JsonArray();

                IGeoFeatureLayer geoFeatureLayer = (IGeoFeatureLayer) layer;

                if(geoFeatureLayer.isDisplayAnnotation())
                {
                    IAnnotateLayerPropertiesCollection annotationProperties = geoFeatureLayer.getAnnotationProperties();

                    for(int index = 0; index < annotationProperties.getCount(); index ++)
                    {
                        IAnnotateLayerProperties[] item = new IAnnotateLayerProperties[1];
                        annotationProperties.queryItem(index, item, null, null);

                        if(item[0] != null)
                        {
                            EsriLabelRendererInterface labelConverter = data.getLabel(item[0].getClass());

                            if(labelConverter != null)
                            {
                                JsonObject jsonLabelRenderer = labelConverter.convert(item[0], mxdMap);
                                if(jsonLabelRenderer != null)
                                {
                                    jsonLabelRendererArray.add(jsonLabelRenderer);
                                }
                            }
                        }
                    }
                    jsonLayerObject.add("labelRenderers", jsonLabelRendererArray);
                }

                JsonArray fieldArray = readFields(featureLayer);
                jsonLayerObject.add("fields", fieldArray);
                jsonLayerlist.add(jsonLayerObject);

                JsonObject dataSourceObject = readDataSource(featureLayer);
                jsonLayerObject.add("dataSource", dataSourceObject);
            }
            else
            {
                Progress.info(getClass(), "Unsupported layer : " + layer.getClass().getName());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Internal_create background.
     *
     * @param background the background
     * @return the json element
     */
    public JsonObject internal_createBackground(ITextBackground background) {
        JsonObject jsonSymbol = null;

        if(background != null)
        {
            EsriTextBackgroundInterface backgroundConverter = data.getTextBackgroundMap(background.getClass());

            if(backgroundConverter != null)
            {
                jsonSymbol = backgroundConverter.convert(background);
            }
            else
            {
                System.err.println("Unsupported background : " + background.getClass().getName());
            }
        }
        return jsonSymbol;
    }

    /**
     * Creates the background.
     *
     * @param background the background
     * @return the json object
     */
    public static JsonObject createBackground(ITextBackground background) {
        if(instance != null)
        {
            return instance.internal_createBackground(background);
        }

        return null;
    }

    /**
     * Creates the number format.
     *
     * @param numberFormat the number format
     * @return the json element
     */
    public static JsonElement createNumberFormat(INumberFormat numberFormat) {
        if(instance != null)
        {
            return instance.internal_createNumberFormat(numberFormat);
        }

        return null;
    }

    /**
     * Internal_create number format.
     *
     * @param numberFormat the number format
     * @return the json element
     */
    private JsonElement internal_createNumberFormat(INumberFormat numberFormat) {
        JsonObject jsonSymbol = null;

        if(numberFormat != null)
        {
            EsriNumberFormatInterface numberFormatConverter = data.getNumberFormat(numberFormat.getClass());

            if(numberFormatConverter != null)
            {
                jsonSymbol = numberFormatConverter.convert(numberFormat);
            }
            else
            {
                System.err.println("Unsupported number format : " + numberFormat.getClass().getName());
            }
        }
        return jsonSymbol;
    }

    /**
     * Creates the renderer.
     *
     * @param renderer the renderer
     * @return the json element
     */
    public static JsonElement createRenderer(IFeatureRenderer renderer) {
        if(instance != null)
        {
            return instance.internal_createRenderer(renderer);
        }

        return null;
    }

    /**
     * Read data source.
     *
     * @param featureLayer the feature layer
     * @return the json object
     */
    private JsonObject readDataSource(FeatureLayer featureLayer) {
        JsonObject jsonDataSourceObject = new JsonObject();

        try {
            IWorkspace workspace = featureLayer.getWorkspace();
            jsonDataSourceObject.addProperty(DatasourceKeys.TYPE, workspace.getType());
            jsonDataSourceObject.addProperty(DatasourceKeys.PATH, workspace.getPathName());
            IPropertySet properties = workspace.getConnectionProperties();
            Object[] keys = new Object[1];
            Object[] values = new Object[1];

            properties.getAllProperties(keys, values);

            JsonObject jsonDataSourcePropertiesObject = new JsonObject();

            for(int index = 0; index < properties.getCount(); index ++)
            {
                String key = ((Object[])keys[0])[index].toString();
                String value = ((Object[])values[0])[index].toString();

                if(key.compareToIgnoreCase("PASSWORD") != 0)
                {
                    jsonDataSourcePropertiesObject.addProperty(key, value);
                }
            }
            jsonDataSourceObject.add(DatasourceKeys.PROPERTIES, jsonDataSourcePropertiesObject);

        } catch (AutomationException e) {
            Progress.warn(getClass(), "Failed to connect to layer's data source so fields were not read");
        } catch (IOException e) {
            Progress.warn(getClass(), "Failed to connect to layer's data source so fields were not read");
        }

        return jsonDataSourceObject;
    }

    /**
     * Read fields.
     *
     * @param layer the layer
     * @return the json array
     */
    private JsonArray readFields(FeatureLayer layer) {
        JsonArray fieldArrayJson = new JsonArray();

        if(layer != null)
        {
            try
            {
                for(int index = 0; index < layer.getFieldCount(); index ++)
                {
                    IField field = layer.getField(index);

                    IFieldInfo fieldInfo = layer.getFieldInfo(index);
                    if(fieldInfo != null)
                    {
                        JsonObject fieldJson = new JsonObject();
                        fieldJson.addProperty(DatasourceKeys.FIELD_NAME, field.getName());

                        if(fieldTypeMap.isEmpty())
                        {
                            fieldTypeMap.put(com.esri.arcgis.geodatabase.esriFieldType.esriFieldTypeSmallInteger, "esriFieldTypeSmallInteger");
                            fieldTypeMap.put(com.esri.arcgis.geodatabase.esriFieldType.esriFieldTypeInteger, "esriFieldTypeInteger");
                            fieldTypeMap.put(com.esri.arcgis.geodatabase.esriFieldType.esriFieldTypeSingle, "esriFieldTypeSingle");
                            fieldTypeMap.put(com.esri.arcgis.geodatabase.esriFieldType.esriFieldTypeDouble, "esriFieldTypeDouble");
                            fieldTypeMap.put(com.esri.arcgis.geodatabase.esriFieldType.esriFieldTypeString, "esriFieldTypeString");
                            fieldTypeMap.put(com.esri.arcgis.geodatabase.esriFieldType.esriFieldTypeDate, "esriFieldTypeDate");
                            fieldTypeMap.put(com.esri.arcgis.geodatabase.esriFieldType.esriFieldTypeOID, "esriFieldTypeOID");
                            fieldTypeMap.put(com.esri.arcgis.geodatabase.esriFieldType.esriFieldTypeGeometry, "esriFieldTypeGeometry");
                            fieldTypeMap.put(com.esri.arcgis.geodatabase.esriFieldType.esriFieldTypeBlob, "esriFieldTypeBlob");
                            fieldTypeMap.put(com.esri.arcgis.geodatabase.esriFieldType.esriFieldTypeRaster, "esriFieldTypeRaster");
                            fieldTypeMap.put(com.esri.arcgis.geodatabase.esriFieldType.esriFieldTypeGUID, "esriFieldTypeGUID");
                            fieldTypeMap.put(com.esri.arcgis.geodatabase.esriFieldType.esriFieldTypeGlobalID, "esriFieldTypeGlobalID");
                        }

                        fieldJson.addProperty(DatasourceKeys.FIELD_TYPE, fieldTypeMap.get(field.getType()));

                        INumberFormat numberFormat = fieldInfo.getNumberFormat();
                        if(numberFormat != null)
                        {
                            if(numberFormat instanceof NumericFormat)
                            {
                                NumericFormat numeric = (NumericFormat) numberFormat;

                                if(numeric.getRoundingOption() == com.esri.arcgis.system.esriRoundingOptionEnum.esriRoundNumberOfDecimals)
                                {
                                    fieldJson.addProperty(DatasourceKeys.FIELD_DECIMAL_PLACES, numeric.getRoundingValue());
                                }
                                else
                                {
                                    fieldJson.addProperty(DatasourceKeys.FIELD_SIG_FIGS, numeric.getRoundingValue());
                                }
                            }
                        }
                        fieldArrayJson.add(fieldJson);
                    }
                }
            } catch (AutomationException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return fieldArrayJson;
    }

    /**
     * Internal_create renderer.
     *
     * @param renderer the renderer
     * @return the json object
     */
    private JsonObject internal_createRenderer(IFeatureRenderer renderer) {
        EsriRendererInterface rendererConverter = data.getRenderer(renderer.getClass());

        JsonObject jsonRenderer = null;

        if(rendererConverter != null)
        {
            jsonRenderer = rendererConverter.convert(renderer);
        }
        else
        {
            System.err.println("Unsupported renderer : " + renderer.getClass().getName());
        }
        return jsonRenderer;
    }

    /**
     * Creates the symbol.
     *
     * @param symbol the symbol
     * @return the json object
     */
    public static JsonObject createSymbol(ISymbol symbol) {
        if(instance != null)
        {
            return instance.internal_createSymbol(symbol);
        }

        return null;
    }

    /**
     * Creates the symbol.
     *
     * @param symbol the symbol
     * @return the json object
     */
    private JsonObject internal_createSymbol(ISymbol symbol) {

        JsonObject jsonSymbol = null;

        if(symbol != null)
        {
            EsriSymbolInterface symbolConverter = data.getSymbol(symbol.getClass());

            if(symbolConverter != null)
            {
                jsonSymbol = symbolConverter.convert(symbol);
            }
            else
            {
                System.err.println("Unsupported symbol : " + symbol.getClass().getName());
            }
        }
        return jsonSymbol;
    }
}
