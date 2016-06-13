/*
 *    ConvertMXD - Part of SLD Editor application
 *    Exports an Esri MXD file to an intermediate json structure file
 *    for use in SLDEditor.
 *
 *    (C) 2016, SCISYS
 *
 */
package com.sldeditor.importdata.esri.renderer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.esri.arcgis.carto.IFeatureRenderer;
import com.esri.arcgis.carto.RepresentationRenderer;
import com.esri.arcgis.display.GraphicAttributeDashType;
import com.esri.arcgis.display.GraphicAttributeEnumType;
import com.esri.arcgis.display.IGraphicAttributes;
import com.esri.arcgis.display.esriGraphicAttributeType;
import com.esri.arcgis.geodatabase.IFeatureClass;
import com.esri.arcgis.geodatabase.IRepresentationClass;
import com.esri.arcgis.geodatabase.ITable;
import com.esri.arcgis.interop.AutomationException;
import com.esri.arcgis.system.IName;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.sldeditor.importdata.esri.CommonObjects;
import com.sldeditor.importdata.esri.keys.renderer.CommonRendererKeys;
import com.sldeditor.importdata.esri.keys.renderer.RepresentationRendererKeys;

/**
 * Class that converts an EsriRepresentationRenderer into JSON.
 * <p>
 * <pre>
 * {@code
 *  "RepresentationRenderer": {
 *        "dataSourceName": "getDataSourceName().getNameString()",
 *        "invalidRuleColour": getInvalidRuleColor(),
 *        "invisibleRuleColour": getInvisibleColor(),
 *        "relativeBase": "getRelativeBase()",
 *        "representationClass": {
 *           "overrideFieldIndex": "getOverrideFieldIndex()",
 *           "ruleIDFieldIndex": "getRuleIDFieldIndex()",
 *           "featureClass": "getFeatureClass().getFeatureDataset().getBrowseName()",
 *           "graphicAttributes": {
 *             "id": "getID(attributeIndex)",
 *             "name": "getName(attributeIndex)",
 *             "type": "getType(attributeIndex).getType()",
 *             "dashType": "getType(attributeIndex).getDashType()",
 *             "enumValues": [getType(attributeIndex) venum alues],
 *           },
 *        }
 *   }
 * }
 * </pre>
 * @author Robert Ward (SCISYS)
 */
public class EsriRepresentationRenderer implements EsriRendererInterface {

    /** The logger. */
    private static Logger logger = Logger.getLogger(RepresentationRendererKeys.class);

    /** The esri graphic attribute type map. */
    private Map<Integer, String> esriGraphicAttributeTypeMap = null;

    /**
     * Convert unique value renderer.
     *
     * @param renderer the renderer
     * @return the json object
     */
    @Override
    public JsonObject convert(IFeatureRenderer renderer) {
        RepresentationRenderer representationRenderer = (RepresentationRenderer) renderer;
        logger.info(RepresentationRendererKeys.REPRESENTATION_RENDERER);

        String value = null;
        String referenceValue = null;
        int index = -1;

        try {
            JsonObject jsonObject = new JsonObject();
            JsonObject rendererObject = new JsonObject();

            JsonArray fieldArray = new JsonArray();

            IName dataSourceName = representationRenderer.getDataSourceName();
            if(dataSourceName != null)
            {
                rendererObject.addProperty(RepresentationRendererKeys.DATA_SOURCE_NAME, dataSourceName.getNameString());
            }

            // Invalid rule colour
            JsonObject invalidRuleColour = CommonObjects.createColour(representationRenderer.getInvalidRuleColor());
            if(invalidRuleColour != null)
            {
                rendererObject.add(RepresentationRendererKeys.INVALID_RULE_COLOUR, invalidRuleColour);
            }

            // Invisible rule colour
            JsonObject invisibleRuleColour = CommonObjects.createColour(representationRenderer.getInvisibleColor());
            if(invisibleRuleColour != null)
            {
                rendererObject.add(RepresentationRendererKeys.INVISIBLE_RULE_COLOUR, invisibleRuleColour);
            }

            rendererObject.addProperty(RepresentationRendererKeys.RELATIVE_BASE, representationRenderer.getRelativeBase());

            JsonObject representationClassObject = getRepresentationClass(representationRenderer.getRepresentationClass());
            if(representationClassObject != null)
            {
                rendererObject.add(RepresentationRendererKeys.REPRESENTATION_CLASS, representationClassObject);
            }

            // Fields
            for(int fieldIndex = 0; fieldIndex < representationRenderer.getFieldCount(); fieldIndex ++)
            {
                JsonObject jsonValueObject = new JsonObject();
                jsonValueObject.addProperty(RepresentationRendererKeys.FIELD_NAME, representationRenderer.getField(fieldIndex));
                ITable table = null;
                jsonValueObject.addProperty(RepresentationRendererKeys.WHERE_CLAUSE, representationRenderer.getWhereClause(fieldIndex, table));

                fieldArray.add(jsonValueObject);
            }

            rendererObject.addProperty(RepresentationRendererKeys.DRAW_INVALID_RULE, representationRenderer.isDrawInvalidRule());
            rendererObject.addProperty(RepresentationRendererKeys.DRAW_INVISIBLE, representationRenderer.isDrawInvisible());
            rendererObject.addProperty(CommonRendererKeys.GRADUATED_SYMBOLS, representationRenderer.isSymbolsAreGraduated());

            rendererObject.add(RepresentationRendererKeys.FIELDS, fieldArray);

            jsonObject.add(RepresentationRendererKeys.REPRESENTATION_RENDERER, rendererObject);

            return jsonObject;

        } catch (AutomationException e) {
            logger.error(String.format("%s %s %d", value, referenceValue, index));
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }

        return null;
    }

    /**
     * Gets the representation class.
     *
     * @param representationClass the representation class
     * @return the representation class
     */
    private JsonObject getRepresentationClass(IRepresentationClass representationClass) {
        JsonObject representationClassObject = null;

        if(representationClass != null)
        {
            if(esriGraphicAttributeTypeMap == null)
            {
                populate();
            }

            representationClassObject = new JsonObject();
            try {
                representationClassObject.addProperty("overrideFieldIndex", representationClass.getOverrideFieldIndex());
                representationClassObject.addProperty("ruleIDFieldIndex", representationClass.getRuleIDFieldIndex());

                // Feature class
                IFeatureClass featureClass = representationClass.getFeatureClass();
                if(featureClass != null)
                {
                    representationClassObject.addProperty("featureClass", featureClass.getFeatureDataset().getBrowseName());
                }

                // Graphic attributes
                IGraphicAttributes graphicAttributes = representationClass.getGraphicAttributes();
                if(graphicAttributes != null)
                {
                    JsonArray graphicAttributeArray = new JsonArray();

                    for(int index = 0; index < graphicAttributes.getGraphicAttributeCount(); index ++)
                    {
                        JsonObject graphicAttributesObject = new JsonObject();
                        representationClassObject.addProperty("id", graphicAttributes.getID(index));
                        representationClassObject.addProperty(RepresentationRendererKeys.FIELD_NAME, graphicAttributes.getName(index));
                        int type = graphicAttributes.getType(index).getType();
                        representationClassObject.addProperty("type", esriGraphicAttributeTypeMap.get(type));

                        switch(type)
                        {
                        case esriGraphicAttributeType.esriAttributeTypeDash:
                        {
                            GraphicAttributeDashType dashType = (GraphicAttributeDashType) graphicAttributes.getType(index);
                            representationClassObject.addProperty("dashType", dashType.getDashType());
                        }
                        break;
                        case esriGraphicAttributeType.esriAttributeTypeEnum:
                        {
                            GraphicAttributeEnumType enumType = (GraphicAttributeEnumType) graphicAttributes.getType(index);
                            enumType.reset();
                            JsonArray enumValueArray = new JsonArray();

                            int enumValue = enumType.nextValue();
                            while(enumValue > -1)
                            {
                                enumValueArray.add(new JsonPrimitive(enumValue));
                                enumValue = enumType.nextValue();
                            }
                            representationClassObject.add("enumValues", enumValueArray);
                        }
                        break;
                        default:
                            break;
                        }

                        graphicAttributeArray.add(graphicAttributesObject);
                    }
                    representationClassObject.add("graphicAttributes", graphicAttributeArray);
                }
            } catch (AutomationException e) {
                e.printStackTrace();
                logger.error(e.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
                logger.error(e.getMessage());
            }
        }
        return representationClassObject;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.batch.esri.renderer.EsriRendererInterface#getRendererClass()
     */
    @Override
    public Class<?> getRendererClass() {
        return RepresentationRenderer.class;
    }

    /**
     * Populate.
     */
    private void populate()
    {
        esriGraphicAttributeTypeMap = new HashMap<Integer, String>();
        esriGraphicAttributeTypeMap.put(esriGraphicAttributeType.esriAttributeTypeBoolean, "esriAttributeTypeBoolean");
        esriGraphicAttributeTypeMap.put(esriGraphicAttributeType.esriAttributeTypeInteger, "esriAttributeTypeInteger");
        esriGraphicAttributeTypeMap.put(esriGraphicAttributeType.esriAttributeTypeEnum, "esriAttributeTypeEnum");
        esriGraphicAttributeTypeMap.put(esriGraphicAttributeType.esriAttributeTypeDouble, "esriAttributeTypeDouble");
        esriGraphicAttributeTypeMap.put(esriGraphicAttributeType.esriAttributeTypeSize, "esriAttributeTypeSize");
        esriGraphicAttributeTypeMap.put(esriGraphicAttributeType.esriAttributeTypeText, "esriAttributeTypeText");
        esriGraphicAttributeTypeMap.put(esriGraphicAttributeType.esriAttributeTypeColor, "esriAttributeTypeColor");
        esriGraphicAttributeTypeMap.put(esriGraphicAttributeType.esriAttributeTypeMarker, "esriAttributeTypeMarker");
        esriGraphicAttributeTypeMap.put(esriGraphicAttributeType.esriAttributeTypeDash, "esriAttributeTypeDash");
        esriGraphicAttributeTypeMap.put(esriGraphicAttributeType.esriAttributeTypeAngle, "esriAttributeTypeAngle");
    }
}
