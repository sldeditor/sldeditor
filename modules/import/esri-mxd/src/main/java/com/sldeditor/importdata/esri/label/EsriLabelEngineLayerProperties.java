/*
 *    ConvertMXD - Part of SLD Editor application
 *    Exports an Esri MXD file to an intermediate json structure file
 *    for use in SLDEditor.
 *
 *    (C) 2016, SCISYS
 *
 */
package com.sldeditor.importdata.esri.label;

import java.io.IOException;

import com.esri.arcgis.carto.IAnnotateLayerProperties;
import com.esri.arcgis.carto.IBasicOverposterLayerProperties;
import com.esri.arcgis.carto.ILineLabelPlacementPriorities;
import com.esri.arcgis.carto.ILineLabelPosition;
import com.esri.arcgis.carto.IOverposterLayerProperties;
import com.esri.arcgis.carto.LabelEngineLayerProperties;
import com.esri.arcgis.carto.Map;
import com.esri.arcgis.display.ISymbol;
import com.esri.arcgis.interop.AutomationException;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sldeditor.importdata.esri.ParseLayer;
import com.sldeditor.importdata.esri.keys.label.LabelEngineLayerPropertiesKey;

/**
 * Class that converts an EsriLabelEngineLayerProperties into JSON.
 * <p>
 * <pre>
 * {@code
 *  "LabelEngineLayerProperties": {
 *        "class" : "esri_getClass()",
 *        "expression" : "getExpression()",
 *        "where" : "getWhereClause()",
 *        "labelWhichFeatures" : "getLabelWhichFeatures()",
 *        "offset" : getOffset(),
 *        "priority" : getPriority(),
 *        "referenceScale" : getReferenceScale(),
 *        "scaleRatio" : getScaleRatio(),
 *        "units" : getUnits(),
 *        "annotationMinimumScale" : getAnnotationMinimumScale(),
 *        "annotationMaximumScale" : getAnnotationMaximumScale(),
 *        "addUnplacedToGraphicsContainer" : isAddUnplacedToGraphicsContainer(),
 *        "symbol" : getSymbol(),
 *        "overposterLayerProperties" : {
 *          "isBarrier" : isBarrier(),
 *          "isPlaceLabels" : isPlaceLabels(),
 *          "isPlaceSymbols" : isPlaceSymbols()
 *        },
 *        "basicOverposterLayerProperties" : {
 *          "bufferRatio" : getBufferRatio(),
 *          "featureType" : getFeatureType(),
 *          "featureWeight" : getFeatureWeight(),
 *          "labelWeight" : getLabelWeight(),
 *          "lineOffset" : getLineOffset(),
 *          "numLabelsOption" : getNumLabelsOption(),
 *          "pointPlacementMethod" : getPointPlacementMethod(),
 *          "lineLabelPlacementPriorities" : {
 *            "aboveAfter" : getLineLabelPlacementPriorities().getAboveAfter(),
 *            "aboveAlong" : getLineLabelPlacementPriorities().getAboveAlong(),
 *            "aboveBefore" : getLineLabelPlacementPriorities().getAboveBefore(),
 *            "aboveEnd" : getLineLabelPlacementPriorities().getAboveEnd(),
 *            "aboveStart" : getLineLabelPlacementPriorities().getAboveStart(),
 *            "belowAfter" : getLineLabelPlacementPriorities().getBelowAfter(),
 *            "belowAlong" : getLineLabelPlacementPriorities().getBelowAlong(),
 *            "belowBefore" : getLineLabelPlacementPriorities().getBelowBefore(),
 *            "belowEnd" : getLineLabelPlacementPriorities().getBelowEnd(),
 *            "belowStart" : getLineLabelPlacementPriorities().getBelowStart(),
 *            "centreAfter" : getLineLabelPlacementPriorities().getCenterAfter(),
 *            "centreAlong" : getLineLabelPlacementPriorities().getCenterAlong(),
 *            "centreBefore" : getLineLabelPlacementPriorities().getCenterBefore(),
 *            "centreEnd" : getLineLabelPlacementPriorities().getCenterEnd(),
 *            "centreStart" : getLineLabelPlacementPriorities().getCenterStart(),
 *          },
 *          "lineLabelPosition" : {
 *            "offset" : getLineLabelPosition().getOffset(),
 *            "isAbove" : getLineLabelPosition().isAbove(),
 *            "isAtEnd" : getLineLabelPosition().isAtEnd(),
 *            "isAtStart" : getLineLabelPosition().isAtStart(),
 *            "isBelow" : getLineLabelPosition().isBelow(),
 *            "isHorizontal" : getLineLabelPosition().isHorizontal(),
 *            "isInLine" : getLineLabelPosition().isInLine(),
 *            "isLeft" : getLineLabelPosition().isLeft(),
 *            "isOnTop" : getLineLabelPosition().isOnTop(),
 *            "isParallel" : getLineLabelPosition().isParallel(),
 *            "isPerpendicular" : getLineLabelPosition().isPerpendicular(),
 *            "isProduceCurvedLabels" : getLineLabelPosition().isProduceCurvedLabels(),
 *            "isRight" : getLineLabelPosition().isRight()
 *          }
 *        }
 *   }
 * }
 * </pre>
  * @author Robert Ward (SCISYS)
 */
public class EsriLabelEngineLayerProperties implements EsriLabelRendererInterface {

    /* (non-Javadoc)
     * @see com.sldeditor.batch.esri.renderer.EsriLabelRendererInterface#getRendererClass()
     */
    @Override
    public Class<?> getRendererClass() {
        return LabelEngineLayerProperties.class;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.batch.esri.label.EsriLabelRendererInterface#convert(com.esri.arcgis.carto.IAnnotateLayerProperties, com.esri.arcgis.carto.Map)
     */
    @Override
    public JsonObject convert(IAnnotateLayerProperties layerProperties, Map map) {
        LabelEngineLayerProperties labelEngineProperties = (LabelEngineLayerProperties) layerProperties;

        JsonObject jsonObject = new JsonObject();
        JsonObject rendererObject = new JsonObject();

        try {
            rendererObject.addProperty(LabelEngineLayerPropertiesKey.CLASS, labelEngineProperties.esri_getClass());
            rendererObject.addProperty(LabelEngineLayerPropertiesKey.EXPRESSION, labelEngineProperties.getExpression());
            rendererObject.addProperty(LabelEngineLayerPropertiesKey.WHERE, labelEngineProperties.getWhereClause());
            rendererObject.addProperty(LabelEngineLayerPropertiesKey.LABEL_WHICH_FEATURES, labelEngineProperties.getLabelWhichFeatures());
            rendererObject.addProperty(LabelEngineLayerPropertiesKey.OFFSET, labelEngineProperties.getOffset());
            rendererObject.addProperty(LabelEngineLayerPropertiesKey.PRIORITY, labelEngineProperties.getPriority());
            rendererObject.addProperty(LabelEngineLayerPropertiesKey.REFERENCE_SCALE, labelEngineProperties.getReferenceScale());
            rendererObject.addProperty(LabelEngineLayerPropertiesKey.SCALE_RATIO, labelEngineProperties.getScaleRatio());
            rendererObject.addProperty(LabelEngineLayerPropertiesKey.UNITS, labelEngineProperties.getUnits());
            rendererObject.addProperty(LabelEngineLayerPropertiesKey.ANNOTATION_MINIMUM_SCALE, labelEngineProperties.getAnnotationMinimumScale());
            rendererObject.addProperty(LabelEngineLayerPropertiesKey.ANNOTATION_MAXIMUM_SCALE, labelEngineProperties.getAnnotationMaximumScale());
            rendererObject.addProperty(LabelEngineLayerPropertiesKey.ADD_UNPLACED_TO_GRAPHICS_CONTAINER, labelEngineProperties.isAddUnplacedToGraphicsContainer());
            rendererObject.add(LabelEngineLayerPropertiesKey.SYMBOL, ParseLayer.createSymbol((ISymbol) labelEngineProperties.getSymbol()));

            IOverposterLayerProperties overposterLayerProperties = labelEngineProperties.getOverposterLayerProperties();
            if(overposterLayerProperties != null)
            {
                JsonObject jsonOverposterLayerObject = new JsonObject();

                jsonOverposterLayerObject.addProperty(LabelEngineLayerPropertiesKey.OVERPOSTER_IS_BARRIER, overposterLayerProperties.isBarrier());
                jsonOverposterLayerObject.addProperty(LabelEngineLayerPropertiesKey.OVERPOSTER_IS_PLACE_LABELS, overposterLayerProperties.isPlaceLabels());
                jsonOverposterLayerObject.addProperty(LabelEngineLayerPropertiesKey.OVERPOSTER_IS_PLACE_SYMBOLS, overposterLayerProperties.isPlaceSymbols());

                jsonObject.add(LabelEngineLayerPropertiesKey.OVERPOSTER_LAYER_PROPERTIES, jsonOverposterLayerObject);
            }

            if(labelEngineProperties.getBasicOverposterLayerProperties() != null)
            {
                jsonObject.add(LabelEngineLayerPropertiesKey.BASIC_OVERPOSTER_LAYER_PROPERTIES, convert(labelEngineProperties.getBasicOverposterLayerProperties()));
            }
            jsonObject.add(LabelEngineLayerPropertiesKey.NAME, rendererObject);

        } catch (AutomationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    /**
     * Convert IBasicOverposterLayerProperties
     *
     * @param basicOverposterLayerProperties the basic overposter layer properties
     * @return the json element
     */
    private JsonElement convert(IBasicOverposterLayerProperties basicOverposterLayerProperties) {
        JsonObject jsonObject = null;

        if(basicOverposterLayerProperties != null)
        {
            jsonObject = new JsonObject();

            try {
                jsonObject.addProperty(LabelEngineLayerPropertiesKey.BASIC_OVERPOSTER_LAYER_BUFFER_RATIO, basicOverposterLayerProperties.getBufferRatio());
                jsonObject.addProperty(LabelEngineLayerPropertiesKey.BASIC_OVERPOSTER_LAYER_FEATURE_TYPE, basicOverposterLayerProperties.getFeatureType());
                jsonObject.addProperty(LabelEngineLayerPropertiesKey.BASIC_OVERPOSTER_LAYER_FEATURE_WEIGHT, basicOverposterLayerProperties.getFeatureWeight());
                jsonObject.addProperty(LabelEngineLayerPropertiesKey.BASIC_OVERPOSTER_LAYER_LABEL_WEIGHT, basicOverposterLayerProperties.getLabelWeight());
                jsonObject.addProperty(LabelEngineLayerPropertiesKey.BASIC_OVERPOSTER_LAYER_LINE_OFFSET, basicOverposterLayerProperties.getLineOffset());
                jsonObject.addProperty(LabelEngineLayerPropertiesKey.BASIC_OVERPOSTER_LAYER_NUM_LABELS_OPTION, basicOverposterLayerProperties.getNumLabelsOption());
                jsonObject.addProperty(LabelEngineLayerPropertiesKey.BASIC_OVERPOSTER_LAYER_POINT_PLACEMENT_METHOD, basicOverposterLayerProperties.getPointPlacementMethod());

                ILineLabelPlacementPriorities lineLabelProperties = basicOverposterLayerProperties.getLineLabelPlacementPriorities();
                if(lineLabelProperties != null)
                {
                    JsonObject jsonLineLabelProperties = new JsonObject();

                    jsonLineLabelProperties.addProperty(LabelEngineLayerPropertiesKey.BASIC_OVERPOSTER_LAYER_LINE_ABOVE_AFTER, lineLabelProperties.getAboveAfter());
                    jsonLineLabelProperties.addProperty(LabelEngineLayerPropertiesKey.BASIC_OVERPOSTER_LAYER_LINE_ABOVE_ALONG, lineLabelProperties.getAboveAlong());
                    jsonLineLabelProperties.addProperty(LabelEngineLayerPropertiesKey.BASIC_OVERPOSTER_LAYER_LINE_ABOVE_BEFORE, lineLabelProperties.getAboveBefore());
                    jsonLineLabelProperties.addProperty(LabelEngineLayerPropertiesKey.BASIC_OVERPOSTER_LAYER_LINE_ABOVE_END, lineLabelProperties.getAboveEnd());
                    jsonLineLabelProperties.addProperty(LabelEngineLayerPropertiesKey.BASIC_OVERPOSTER_LAYER_LINE_ABOVE_START, lineLabelProperties.getAboveStart());
                    jsonLineLabelProperties.addProperty(LabelEngineLayerPropertiesKey.BASIC_OVERPOSTER_LAYER_LINE_BELOW_AFTER, lineLabelProperties.getBelowAfter());
                    jsonLineLabelProperties.addProperty(LabelEngineLayerPropertiesKey.BASIC_OVERPOSTER_LAYER_LINE_BELOW_ALONG, lineLabelProperties.getBelowAlong());
                    jsonLineLabelProperties.addProperty(LabelEngineLayerPropertiesKey.BASIC_OVERPOSTER_LAYER_LINE_BELOW_BEFORE, lineLabelProperties.getBelowBefore());
                    jsonLineLabelProperties.addProperty(LabelEngineLayerPropertiesKey.BASIC_OVERPOSTER_LAYER_LINE_BELOW_END, lineLabelProperties.getBelowEnd());
                    jsonLineLabelProperties.addProperty(LabelEngineLayerPropertiesKey.BASIC_OVERPOSTER_LAYER_LINE_BELOW_START, lineLabelProperties.getBelowStart());
                    jsonLineLabelProperties.addProperty(LabelEngineLayerPropertiesKey.BASIC_OVERPOSTER_LAYER_LINE_CENTRE_AFTER, lineLabelProperties.getCenterAfter());
                    jsonLineLabelProperties.addProperty(LabelEngineLayerPropertiesKey.BASIC_OVERPOSTER_LAYER_LINE_CENTRE_ALONG, lineLabelProperties.getCenterAlong());
                    jsonLineLabelProperties.addProperty(LabelEngineLayerPropertiesKey.BASIC_OVERPOSTER_LAYER_LINE_CENTRE_BEFORE, lineLabelProperties.getCenterBefore());
                    jsonLineLabelProperties.addProperty(LabelEngineLayerPropertiesKey.BASIC_OVERPOSTER_LAYER_LINE_CENTRE_END, lineLabelProperties.getCenterEnd());
                    jsonLineLabelProperties.addProperty(LabelEngineLayerPropertiesKey.BASIC_OVERPOSTER_LAYER_LINE_CENTRE_START, lineLabelProperties.getCenterStart());

                    jsonObject.add(LabelEngineLayerPropertiesKey.BASIC_OVERPOSTER_LAYER_LINE_LABEL_PLACEMENT_PRIORITIES, jsonLineLabelProperties);
                }

                ILineLabelPosition lineLabelPosition = basicOverposterLayerProperties.getLineLabelPosition();
                if(lineLabelPosition != null)
                {
                    JsonObject jsonLineLabelPosition = new JsonObject();

                    jsonLineLabelPosition.addProperty(LabelEngineLayerPropertiesKey.LINE_LABEL_POSITION_OFFSET, lineLabelPosition.getOffset());
                    jsonLineLabelPosition.addProperty(LabelEngineLayerPropertiesKey.LINE_LABEL_POSITION_IS_ABOVE, lineLabelPosition.isAbove());
                    jsonLineLabelPosition.addProperty(LabelEngineLayerPropertiesKey.LINE_LABEL_POSITION_IS_AT_END, lineLabelPosition.isAtEnd());
                    jsonLineLabelPosition.addProperty(LabelEngineLayerPropertiesKey.LINE_LABEL_POSITION_IS_AT_START, lineLabelPosition.isAtStart());
                    jsonLineLabelPosition.addProperty(LabelEngineLayerPropertiesKey.LINE_LABEL_POSITION_IS_BELOW, lineLabelPosition.isBelow());
                    jsonLineLabelPosition.addProperty(LabelEngineLayerPropertiesKey.LINE_LABEL_POSITION_IS_HORIZONTAL, lineLabelPosition.isHorizontal());
                    jsonLineLabelPosition.addProperty(LabelEngineLayerPropertiesKey.LINE_LABEL_POSITION_IS_IN_LINE, lineLabelPosition.isInLine());
                    jsonLineLabelPosition.addProperty(LabelEngineLayerPropertiesKey.LINE_LABEL_POSITION_IS_LEFT, lineLabelPosition.isLeft());
                    jsonLineLabelPosition.addProperty(LabelEngineLayerPropertiesKey.LINE_LABEL_POSITION_IS_ON_TOP, lineLabelPosition.isOnTop());
                    jsonLineLabelPosition.addProperty(LabelEngineLayerPropertiesKey.LINE_LABEL_POSITION_IS_PARALLEL, lineLabelPosition.isParallel());
                    jsonLineLabelPosition.addProperty(LabelEngineLayerPropertiesKey.LINE_LABEL_POSITION_IS_PERPENDICULAR, lineLabelPosition.isPerpendicular());
                    jsonLineLabelPosition.addProperty(LabelEngineLayerPropertiesKey.LINE_LABEL_POSITION_IS_PRODUCE_CURVED_LABELS, lineLabelPosition.isProduceCurvedLabels());
                    jsonLineLabelPosition.addProperty(LabelEngineLayerPropertiesKey.LINE_LABEL_POSITION_IS_RIGHT, lineLabelPosition.isRight());

                    jsonObject.add(LabelEngineLayerPropertiesKey.LINE_LABEL_POSITION, jsonLineLabelPosition);
                }
            } catch (AutomationException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return jsonObject;
    }

}
