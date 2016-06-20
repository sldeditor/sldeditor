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
package com.sldeditor.extension.filesystem.file.esri;

import java.awt.image.Raster;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.NamedLayer;
import org.geotools.styling.Rule;
import org.geotools.styling.Style;
import org.geotools.styling.StyledLayer;
import org.geotools.styling.StyledLayerDescriptor;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sldeditor.common.DataSourceFieldInterface;
import com.sldeditor.common.DataSourcePropertiesInterface;
import com.sldeditor.common.SLDDataInterface;
import com.sldeditor.common.console.ConsoleManager;
import com.sldeditor.common.data.SLDData;
import com.sldeditor.common.data.StyleWrapper;
import com.sldeditor.common.output.SLDWriterInterface;
import com.sldeditor.common.output.impl.SLDWriterFactory;
import com.sldeditor.datasource.DataSourceField;
import com.sldeditor.importdata.esri.datasource.DataSourceManager;
import com.sldeditor.importdata.esri.keys.DatasourceKeys;
import com.sldeditor.importdata.esri.label.EsriLabelRendererInterface;
import com.sldeditor.importdata.esri.label.LabelEngineLayerProperties;
import com.sldeditor.importdata.esri.renderer.EsriRendererInterface;
import com.sldeditor.importdata.esri.renderer.SimpleRenderer;
import com.sldeditor.importdata.esri.renderer.UniqueValueRenderer;
import com.sldeditor.importdata.esri.symbols.SymbolUtils;
import com.vividsolutions.jts.geom.Geometry;

/**
 * Class that reads the intermediate Json file containing Esri MXD data and converts it to SLD data.
 * 
 * @author Robert Ward (SCISYS)
 */
public class MXDParser
{
    /** The renderer map. */
    private Map<String, EsriRendererInterface> rendererMap = new HashMap<String, EsriRendererInterface>();

    /** The label renderer map. */
    private Map<String, EsriLabelRendererInterface> labelRendererMap = new HashMap<String, EsriLabelRendererInterface>();

    /** The field type map. */
    private static Map<String, Class<?> > fieldTypeMap = new HashMap<String, Class<?> >();

    /** The logger. */
    private static Logger logger = Logger.getLogger(MXDParser.class);

    /** The mxd layer cache map. */
    private Map<String, SLDDataInterface> mxdLayerCacheMap = new HashMap<String, SLDDataInterface>();

    /** The map of class instances. */
    private static Map<String, MXDParser> instanceMap = new HashMap<String, MXDParser>();

    /** The sld writer. */
    private SLDWriterInterface sldWriter = null;

    /**
     * Default constructor
     */
    private MXDParser()
    {
        initialise();
    }

    /**
     * Initialise.
     */
    private void initialise()
    {
        registerRenderers();
        registerLabelRenderers();

        if(fieldTypeMap.isEmpty())
        {
            fieldTypeMap.put("esriFieldTypeSmallInteger", Short.class);
            fieldTypeMap.put("esriFieldTypeInteger", Integer.class);
            fieldTypeMap.put("esriFieldTypeSingle", Float.class);
            fieldTypeMap.put("esriFieldTypeDouble", Double.class);
            fieldTypeMap.put("esriFieldTypeString", String.class);
            fieldTypeMap.put("esriFieldTypeDate", Date.class);
            fieldTypeMap.put("esriFieldTypeOID", Integer.class);
            fieldTypeMap.put("esriFieldTypeGeometry", Geometry.class);
            fieldTypeMap.put("esriFieldTypeBlob", Blob.class);
            fieldTypeMap.put("esriFieldTypeRaster", Raster.class);
            fieldTypeMap.put("esriFieldTypeGUID", String.class);
            fieldTypeMap.put("esriFieldTypeGlobalID", String.class);
        }
    }

    /**
     * Read layers from intermediate mxd files.
     *
     * @param filename the filename
     * @return the MXD info
     */
    public static MXDInfo readLayers(String filename)
    {
        if(filename == null)
        {
            return null;
        }

        File intermediateFile = new File(filename);
        if(!intermediateFile.exists())
        {
            return null;
        }

        MXDParser parser = getInstance(filename);

        MXDInfo mxdInfo = new MXDInfo();
        mxdInfo.setIntermediateFile(intermediateFile);
        JsonObject mxdJSON = convertFileToJSON(filename);

        // Read mxd filename
        String mxdFilename = "";
        JsonElement mxdFilenameJson = mxdJSON.get("mxd");
        if(mxdFilenameJson != null)
        {
            mxdFilename = mxdFilenameJson.getAsString();
            mxdInfo.setMxdFilename(mxdFilename);
        }

        File f = new File(mxdFilename);

        String mxdName = f.getName();
        int pos = mxdName.lastIndexOf("\\");
        if (pos > 0) {
            mxdName = mxdName.substring(pos + 1);
        }
        mxdInfo.setMxdName(mxdName);

        // Read layers
        JsonArray layerList = mxdJSON.getAsJsonArray("layers");
        List<String> mxdLayerList = new ArrayList<String>();

        for(int index = 0; index < layerList.size(); index ++)
        {
            JsonObject layer = (JsonObject) layerList.get(index);

            JsonElement layerNameElement = layer.get("name");
            if(layerNameElement != null)
            {
                String layerName = layerNameElement.getAsString();

                SLDDataInterface sldData = parser.importLayer(layer);
                sldData.setSLDFile(intermediateFile);

                parser.addLayer(layerName, sldData);
                mxdLayerList.add(layerName);
            }
        }

        mxdInfo.setLayerList(mxdLayerList);

        return mxdInfo;
    }

    /**
     * Adds the layer.
     *
     * @param layerName the layer name
     * @param sldData the sld data
     */
    private void addLayer(String layerName, SLDDataInterface sldData)
    {
        mxdLayerCacheMap.put(layerName, sldData);
    }

    /**
     * Convert file to json.
     *
     * @param fileName the file name
     * @return the json object
     */
    private static JsonObject convertFileToJSON(String fileName)
    {
        // Read from File to String
        JsonObject jsonObject = new JsonObject();

        try {
            JsonParser parser = new JsonParser();
            JsonElement jsonElement = parser.parse(new FileReader(fileName));
            jsonObject = jsonElement.getAsJsonObject();
        } catch (FileNotFoundException e) {
            ConsoleManager.getInstance().exception(MXDParser.class, e);
        }

        return jsonObject;
    }

    /**
     * Read layer.
     *
     * @param filename the filename
     * @param layerName the layer name
     * @return the SLD data
     */
    public static SLDDataInterface readLayer(String filename, String layerName) {

        MXDParser parser = getInstance(filename);

        return parser.getLayer(layerName);
    }

    /**
     * Gets the layer.
     *
     * @param layerName the layer name
     * @return the layer
     */
    private SLDDataInterface getLayer(String layerName)
    {
        return mxdLayerCacheMap.get(layerName);
    }

    /**
     * Gets the instance of MXDParser, given the filename.
     *
     * @return Instance of MXDParser
     */
    private static MXDParser getInstance(String filename)
    {
        MXDParser instance = instanceMap.get(filename);
        
        if(instance == null)
        {
            instance = new MXDParser();
            instanceMap.put(filename, instance);
        }

        return instance;
    }

    /**
     * Import layer.
     *
     * @param layer the layer
     * @return the styled layer descriptor
     */
    private SLDDataInterface importLayer(JsonObject layer) {

        StyledLayerDescriptor sld = null;
        String layerName = layer.get("name").getAsString();

        double minScale = layer.get("minScale").getAsDouble();
        double maxScale = layer.get("maxScale").getAsDouble();

        int transparency = layer.get("transparency").getAsInt();

        JsonElement renderElement = layer.get("renderer");
        sld = getRenderer(layerName, minScale, maxScale, transparency, renderElement);

        JsonElement labelRenderArrayElement = layer.get("labelRenderers");
        if(labelRenderArrayElement != null)
        {
            processLabelRenderer(sld, labelRenderArrayElement.getAsJsonArray(), transparency);
        }

        JsonElement fieldArray = layer.get("fields");
        List<DataSourceFieldInterface> fieldList = processFields(layerName, fieldArray);

        JsonElement dataSourcePropertiesElement = layer.get("dataSource");
        DataSourcePropertiesInterface dataSourceProperties = processDataSource(layerName, dataSourcePropertiesElement);

        if(sldWriter  == null)
        {
            sldWriter = SLDWriterFactory.createWriter(null);
        }

        String sldContents = sldWriter.encodeSLD(sld);

        StyleWrapper styleWrapper = new StyleWrapper(layerName, layerName);
        SLDDataInterface sldData = new SLDData(styleWrapper, sldContents);
        sldData.setDataSourceProperties(dataSourceProperties);
        sldData.setFieldList(fieldList);
        sldData.setReadOnly(true);

        return sldData;
    }

    /**
     * Process fields.
     *
     * @param layerName the layer name
     * @param fieldArrayElement the field array element
     * @return the list
     */
    private List<DataSourceFieldInterface> processFields(String layerName, JsonElement fieldArrayElement)
    {
        List<DataSourceFieldInterface> fieldList = new ArrayList<DataSourceFieldInterface>();

        if(fieldArrayElement != null)
        {
            JsonArray fieldArray = fieldArrayElement.getAsJsonArray();

            for(int index = 0; index < fieldArray.size(); index ++)
            {
                JsonObject fieldObject = null;

                try
                {
                    fieldObject = fieldArray.get(index).getAsJsonObject();
                }
                catch(IllegalStateException e)
                {
                    ConsoleManager.getInstance().error(this, "Layer : " + layerName);
                    ConsoleManager.getInstance().exception(this, e);
                }

                if(fieldObject != null)
                {
                    Class<?> fieldType = convertFieldType(fieldObject.get("type").getAsString());

                    DataSourceField esriField = new DataSourceField(fieldObject.get("field").getAsString(),
                            fieldType);

                    fieldList.add(esriField);
                }
            }
        }

        return fieldList;
    }

    /**
     * Process fields.
     *
     * @param layerName the layer name
     * @param dataSourcePropertiesElement the data source properties element
     * @return the data source properties
     */
    private DataSourcePropertiesInterface processDataSource(String layerName, JsonElement dataSourcePropertiesElement)
    {
        Map<String, String> propertyMap = new LinkedHashMap<String, String>();

        if(dataSourcePropertiesElement != null)
        {
            JsonObject dsObj = dataSourcePropertiesElement.getAsJsonObject();

            JsonElement typeElement = dsObj.get(DatasourceKeys.TYPE);
            propertyMap.put(DatasourceKeys.TYPE, typeElement.getAsString());

            JsonElement pathElement = dsObj.get(DatasourceKeys.PATH);
            propertyMap.put(DatasourceKeys.PATH, pathElement.getAsString());

            JsonObject properties = dsObj.getAsJsonObject(DatasourceKeys.PROPERTIES);

            for(Map.Entry<String, JsonElement> field : properties.entrySet())
            {
                propertyMap.put(field.getKey(), field.getValue().getAsString());
            }
        }

        DataSourcePropertiesInterface dataSourceProperties = DataSourceManager.getInstance().convert(propertyMap);

        return dataSourceProperties;
    }

    /**
     * Convert field type from String to a Class type.
     *
     * @param fieldType the field type
     * @return the class type
     */
    private static Class<?> convertFieldType(String fieldType)
    {
        if(fieldTypeMap.containsKey(fieldType))
        {
            return fieldTypeMap.get(fieldType);
        }
        return null;
    }

    /**
     * Gets the renderer.
     *
     * @param layerName the layer name
     * @param minScale the min scale
     * @param maxScale the max scale
     * @param transparency the transparency
     * @param renderElement the render element
     * @return the renderer
     */
    private StyledLayerDescriptor getRenderer(
            String layerName, double minScale, double maxScale, int transparency,
            JsonElement renderElement)
    {
        StyledLayerDescriptor sld = null;
        if(renderElement != null)
        {
            JsonObject renderer = renderElement.getAsJsonObject();

            boolean found = false;
            for(String rendererType : rendererMap.keySet())
            {
                JsonElement obj = renderer.get(rendererType);

                if(obj != null)
                {
                    EsriRendererInterface esriRenderer = rendererMap.get(rendererType);
                    sld = esriRenderer.convert(obj.getAsJsonObject(), layerName, minScale, maxScale, transparency);
                    found = true;
                    break;
                }
            }

            if(!found)
            {
                ConsoleManager.getInstance().error(this, "Unsupported renderer : " + SymbolUtils.extractName(renderElement));
            }
        }
        return sld;
    }

    /**
     * Gets the label renderer.
     *
     * @param sld the sld
     * @param labelRenderArrayElement the label render array element
     * @param transparency the transparency
     */
    private void processLabelRenderer(StyledLayerDescriptor sld, JsonArray labelRenderArrayElement, int transparency)
    {
        if(sld != null)
        {
            if(labelRenderArrayElement != null)
            {
                List<StyledLayer> styledLayers = sld.layers();

                if(!styledLayers.isEmpty())
                {
                    if(styledLayers.get(0) instanceof NamedLayer)
                    {
                        NamedLayer namedLayer = (NamedLayer)styledLayers.get(0);

                        List<Style> styleList = namedLayer.styles();

                        if(!styleList.isEmpty())
                        {
                            List<FeatureTypeStyle> ftsList = styleList.get(0).featureTypeStyles();
                            if(!ftsList.isEmpty())
                            {
                                List<Rule> ruleList = ftsList.get(0).rules();
                                List<Rule> newLabelRuleList = new ArrayList<Rule>();

                                for(int index = 0; index < labelRenderArrayElement.size(); index ++)
                                {
                                    JsonElement labelRenderElement = labelRenderArrayElement.get(index);

                                    JsonObject renderer = labelRenderElement.getAsJsonObject();

                                    boolean found = false;
                                    for(String labelRendererType : labelRendererMap.keySet())
                                    {
                                        JsonElement obj = renderer.get(labelRendererType);

                                        if(obj != null)
                                        {
                                            EsriLabelRendererInterface esriLabelRenderer = labelRendererMap.get(labelRendererType);
                                            if(esriLabelRenderer != null)
                                            {
                                                for(Rule rule : ruleList)
                                                {
                                                    esriLabelRenderer.convert(newLabelRuleList, rule, obj, transparency);
                                                }
                                            }

                                            found = true;
                                            break;
                                        }
                                    }

                                    if(!found)
                                    {
                                        ConsoleManager.getInstance().error(this, "Unsupported label renderer : " + SymbolUtils.extractName(labelRenderElement));
                                    }
                                }

                                ruleList.addAll(newLabelRuleList);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Register renderer converters.
     */
    private void registerRenderers() {

        logger.info("Renderers supported:");

        addRenderer(new SimpleRenderer());
        addRenderer(new UniqueValueRenderer());
    }

    /**
     * Adds the renderer.
     *
     * @param rendererObj the renderer obj
     */
    private void addRenderer(EsriRendererInterface rendererObj)
    {
        logger.info("\t" + rendererObj.getName());
        rendererMap.put(rendererObj.getName(), rendererObj);
    }

    /**
     * Adds the label renderer.
     *
     * @param labelRendererObj the label renderer obj
     */
    private void addLabelRenderer(EsriLabelRendererInterface labelRendererObj)
    {
        logger.info("\t" + labelRendererObj.getName());
        labelRendererMap.put(labelRendererObj.getName(), labelRendererObj);
    }

    /**
     * Register label renderer converters.
     */
    private void registerLabelRenderers() {
        logger.info("Label Renderers supported:");
        addLabelRenderer(new LabelEngineLayerProperties());
    }

}

