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
import com.esri.arcgis.carto.IMapOverposter;
import com.esri.arcgis.carto.IMaplexDictionaries;
import com.esri.arcgis.carto.IMaplexDictionary;
import com.esri.arcgis.carto.IMaplexDictionaryEntry;
import com.esri.arcgis.carto.IMaplexLabelStackingProperties;
import com.esri.arcgis.carto.IMaplexOffsetAlongLineProperties;
import com.esri.arcgis.carto.IMaplexOverposterLayerProperties;
import com.esri.arcgis.carto.IMaplexOverposterLayerProperties2;
import com.esri.arcgis.carto.IMaplexOverposterProperties;
import com.esri.arcgis.carto.IMaplexRotationProperties;
import com.esri.arcgis.carto.IOverposterProperties;
import com.esri.arcgis.carto.IPointPlacementPriorities;
import com.esri.arcgis.carto.Map;
import com.esri.arcgis.carto.MaplexLabelEngineLayerProperties;
import com.esri.arcgis.display.ISymbol;
import com.esri.arcgis.interop.AutomationException;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sldeditor.importdata.esri.ParseLayer;
import com.sldeditor.importdata.esri.keys.label.MaplexLabelEngineEngineLayerPropertiesKeys;

/**
 * Class that converts an EsriMaplexLabelEngineLayerProperties into JSON.
 * <p>
 * <pre>
 * {@code
 *  "MaplexLabelEngineLayerProperties": {
 *        "expression" : "getExpression()",
 *        "where" : "getWhereClause()",
 *        "labelWhichFeatures" : getLabelWhichFeatures(),
 *        "offset" : getOffset(),
 *        "priority" : getPriority(),
 *        "referenceScale" : getReferenceScale(),
 *        "scaleRatio" : getScaleRatio(),
 *        "units" : getUnits(),
 *        "addUnplacedToGraphicsContainer" : isAddUnplacedToGraphicsContainer(),
 *        "symbol", getSymbol(),
 *        "overposter" : {
 *          "constrainOffset" : getOverposterLayerProperties().getConstrainOffset(),
 *          "featureBuffer" : getOverposterLayerProperties().getFeatureBuffer(),
 *          "featureType" : getOverposterLayerProperties().getFeatureType(),
 *          "featureWeight" : getOverposterLayerProperties().getFeatureWeight(),
 *          "labelBuffer" : getOverposterLayerProperties().getLabelBuffer(),
 *          "labelPriority" : getOverposterLayerProperties().getLabelPriority(),
 *          "linePlacementMethod" : getOverposterLayerProperties().getLinePlacementMethod(),
 *          "pointPlacementMethod" : getOverposterLayerProperties().getPointPlacementMethod(),
 *          "polygonBoundaryWeight" : getOverposterLayerProperties().getPolygonBoundaryWeight(),
 *          "polygonPlacementMethod" : getOverposterLayerProperties().getPolygonPlacementMethod(),
 *          "primaryOffsetUnit" : getOverposterLayerProperties().getPrimaryOffsetUnit(),
 *          "fontHeightReductionLimit" : getOverposterLayerProperties().getFontHeightReductionLimit(),
 *          "fontHeightReductionStep" : getOverposterLayerProperties().getFontHeightReductionStep(),
 *          "getFontWidthReductionLimit" : getOverposterLayerProperties().getFontWidthReductionLimit(),
 *          "fontWidthReductionStep" : getOverposterLayerProperties().getFontWidthReductionStep(),
 *          "maximumCharacterSpacing" : getOverposterLayerProperties().getMaximumCharacterSpacing(),
 *          "maximumWordSpacing" : getOverposterLayerProperties().getMaximumLabelOverrun(),
 *          "maximumWordSpacing" : getOverposterLayerProperties().getMaximumWordSpacing(),
 *          "minimumEndOfStreetClearance" : getOverposterLayerProperties().getMinimumEndOfStreetClearance(),
 *          "minimumRepetitionInterval" : getOverposterLayerProperties().getMinimumRepetitionInterval(),
 *          "minimumSizeForLabeling" : getOverposterLayerProperties().getMinimumSizeForLabeling(),
 *          "preferredEndOfStreetClearance" : getOverposterLayerProperties().getPreferredEndOfStreetClearance(),
 *          "primaryOffset" : getOverposterLayerProperties().getPrimaryOffset(),
 *          "secondaryOffset" : getOverposterLayerProperties().getSecondaryOffset(),
 *          "thinningDistance" : getOverposterLayerProperties().getThinningDistance(),
 *          "alignLabelToLineDirection" : getOverposterLayerProperties().isAlignLabelToLineDirection(),
 *          "allowAsymmetricOverrun" : getOverposterLayerProperties().isAllowAsymmetricOverrun(),
 *          "backgroundLabel" : getOverposterLayerProperties().isBackgroundLabel(),
 *          "canAbbreviateLabel" : getOverposterLayerProperties().isCanAbbreviateLabel(),
 *          "canOverrunFeature" : getOverposterLayerProperties().isCanOverrunFeature(),
 *          "canPlaceLabelOutsidePolygon" : getOverposterLayerProperties().isCanPlaceLabelOutsidePolygon(),
 *          "canReduceFontSize" : getOverposterLayerProperties().isCanReduceFontSize(),
 *          "canRemoveOverlappingLabel" : getOverposterLayerProperties().isCanRemoveOverlappingLabel(),
 *          "canShiftPointLabel" : getOverposterLayerProperties().isCanShiftPointLabel(),
 *          "canStackLabel" : getOverposterLayerProperties().isCanStackLabel(),
 *          "canTruncateLabel" : getOverposterLayerProperties().isCanTruncateLabel(),
 *          "enablePointPlacementPriorities" : getOverposterLayerProperties().isEnablePointPlacementPriorities(),
 *          "graticuleAlignment" : getOverposterLayerProperties().isGraticuleAlignment(),
 *          "landParcelPlacement" : getOverposterLayerProperties().isLandParcelPlacement(),
 *          "neverRemoveLabel" : getOverposterLayerProperties().isNeverRemoveLabel(),
 *          "preferHorizontalPlacement" : getOverposterLayerProperties().isPreferHorizontalPlacement(),
 *          "repeatLabel" : getOverposterLayerProperties().isRepeatLabel(),
 *          "spreadCharacters" : getOverposterLayerProperties().isSpreadCharacters(),
 *          "spreadWords" : getOverposterLayerProperties().isSpreadWords(),
 *          "streetplacement" : getOverposterLayerProperties().isStreetPlacement(),
 *          "thinDuplicateLabels" : getOverposterLayerProperties().isThinDuplicateLabels()
 *        },
 *        "overposterLayerProperties" : {
 *          "contourAlignmentType" : getOverposterLayerProperties().getContourAlignmentType(),
 *          "contourLadderType" : getOverposterLayerProperties().getContourLadderType(),
 *          "contourMaximumAngle" : getOverposterLayerProperties().getContourMaximumAngle(),
 *          "graticuleAlignmentType" : getOverposterLayerProperties().getGraticuleAlignmentType(),
 *          "lineFeatureType" : getOverposterLayerProperties().getLineFeatureType(),
 *          "maximumLabelOverrunUnit" : getOverposterLayerProperties().getMaximumLabelOverrunUnit(),
 *          "minimumFeatureSizeUnit" : getOverposterLayerProperties().getMinimumFeatureSizeUnit(),
 *          "polygonAnchorPointType" : getOverposterLayerProperties().getPolygonAnchorPointType(),
 *          "polygonFeatureType" : getOverposterLayerProperties().getPolygonFeatureType(),
 *          "repetitionIntervalUnit" : getOverposterLayerProperties().getRepetitionIntervalUnit(),
 *          "thinningDistanceUnit" : getOverposterLayerProperties().getThinningDistanceUnit(),
 *          "secondaryOffsetMaximum" : getOverposterLayerProperties().getSecondaryOffsetMaximum(),
 *          "secondaryOffsetMinimum" : getOverposterLayerProperties().getSecondaryOffsetMinimum(),
 *          "canFlipStackedStreetLabel" : getOverposterLayerProperties().isCanFlipStackedStreetLabel(),
 *          "canPlaceLabelOnTopOfFeature" : getOverposterLayerProperties().isCanPlaceLabelOnTopOfFeature(),
 *          "canReduceLeading" : getOverposterLayerProperties().isCanReduceLeading(),
 *          "enablePolygonFixedPosition" : getOverposterLayerProperties().isEnablePolygonFixedPosition(),
 *          "enableSecondaryOffset" : getOverposterLayerProperties().isEnableSecondaryOffset(),
 *          "labelBufferHardConstraint" : getOverposterLayerProperties().isLabelBufferHardConstraint(),
 *          "minimumSizeBasedOnArea" : getOverposterLayerProperties().isMinimumSizeBasedOnArea(),
 *          "offsetFromFeatureGeometry" : getOverposterLayerProperties().isOffsetFromFeatureGeometry()
 *        },
 *        "maplexLabelStackingProperties" : {
 *          "maximumNumberOfCharsPerLine" : getLabelStackingProperties().getMaximumNumberOfCharsPerLine(),
 *          "maximumNumberOfLines" : getLabelStackingProperties().getMaximumNumberOfLines(),
 *          "minimumNumberOfCharsPerLine" : getLabelStackingProperties().getMinimumNumberOfCharsPerLine(),
 *          "separatorCount" : getLabelStackingProperties().getSeparatorCount(),
 *          "stackJustification" : getLabelStackingProperties().getStackJustification()
 *        },
 *        "maplexOffsetAlongLineProperties" : {
 *          "distanceUnit" : getOffsetAlongLineProperties().getDistanceUnit(),
 *          "labelAnchorPoint" : getOffsetAlongLineProperties().getLabelAnchorPoint(),
 *          "placementMethod" : getOffsetAlongLineProperties().getPlacementMethod(),
 *          "distance" : getOffsetAlongLineProperties().getDistance(),
 *          "tolerance" : getOffsetAlongLineProperties().getTolerance(),
 *          "isUseLineDirection" : getOffsetAlongLineProperties().isUseLineDirection()
 *        },
 *        "pointPlacementPriorities" : {
 *          "aboveCenter" : getPointPlacementPriorities().getAboveCenter(),
 *          "aboveLeft" : getPointPlacementPriorities().getAboveLeft(),
 *          "aboveRight" : getPointPlacementPriorities().getAboveRight(),
 *          "belowCenter" : getPointPlacementPriorities().getBelowCenter(),
 *          "belowLeft" : getPointPlacementPriorities().getBelowLeft(),
 *          "belowRight" : getPointPlacementPriorities().getBelowRight(),
 *          "centerLeft" : getPointPlacementPriorities().getCenterLeft(),
 *          "centerRight" : getPointPlacementPriorities().getCenterRight()
 *        },
 *        "maplexRotationPriorities" : {
 *          "rotationField" : getRotationProperties().getRotationField(),
 *          "rotationType" :getRotationProperties(). getRotationType()
 *        },
 *        "maplexOverposterProperties" : {
 *          "allowBorderOverlap" : getOverposterProperties().isAllowBorderOverlap(),
 *          "placementQuality" : getOverposterProperties().getPlacementQuality(),
 *          "connectionType" : getOverposterProperties().getConnectionType(),
 *          "dictionaries" : [
 *            "name" : getDictionary(dictionaryIndex).getName(),
 *            "entry" : {
 *              "abbreviation" : getDictionary(dictionaryIndex).getEntry(entryIndex).getAbbreviation(),
 *              "text" : getDictionary(dictionaryIndex).getEntry(entryIndex).getText(),
 *              "type" : getDictionary(dictionaryIndex).getEntry(entryIndex).getType()
 *            }
 *          ]
 *        }
 *   }
 * }
 * </pre>
 * @author Robert Ward (SCISYS)
 */
public class EsriMaplexLabelEngineLayerProperties implements EsriLabelRendererInterface {


    /* (non-Javadoc)
     * @see com.sldeditor.batch.esri.renderer.EsriLabelRendererInterface#getRendererClass()
     */
    @Override
    public Class<?> getRendererClass() {
        return MaplexLabelEngineLayerProperties.class;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.batch.esri.label.EsriLabelRendererInterface#convert(com.esri.arcgis.carto.IAnnotateLayerProperties, com.esri.arcgis.carto.Map)
     */
    @Override
    public JsonObject convert(IAnnotateLayerProperties layerProperties, Map map) {
        MaplexLabelEngineLayerProperties labelEngineProperties = (MaplexLabelEngineLayerProperties) layerProperties;


        JsonObject jsonObject = new JsonObject();
        JsonObject rendererObject = new JsonObject();

        try {
            rendererObject.addProperty(MaplexLabelEngineEngineLayerPropertiesKeys.EXPRESSION, labelEngineProperties.getExpression());
            rendererObject.addProperty(MaplexLabelEngineEngineLayerPropertiesKeys.WHERE, labelEngineProperties.getWhereClause());
            rendererObject.addProperty(MaplexLabelEngineEngineLayerPropertiesKeys.LABEL_WHICH_FEATURES, labelEngineProperties.getLabelWhichFeatures());
            rendererObject.addProperty(MaplexLabelEngineEngineLayerPropertiesKeys.OFFSET, labelEngineProperties.getOffset());
            rendererObject.addProperty(MaplexLabelEngineEngineLayerPropertiesKeys.PRIORITY, labelEngineProperties.getPriority());
            rendererObject.addProperty(MaplexLabelEngineEngineLayerPropertiesKeys.REFERENCE_SCALE, labelEngineProperties.getReferenceScale());
            rendererObject.addProperty(MaplexLabelEngineEngineLayerPropertiesKeys.SCALE_RATIO, labelEngineProperties.getScaleRatio());
            rendererObject.addProperty(MaplexLabelEngineEngineLayerPropertiesKeys.UNITS, labelEngineProperties.getUnits());
            rendererObject.addProperty(MaplexLabelEngineEngineLayerPropertiesKeys.ADD_UNPLACED_TO_GRAPHICS_CONTAINER, labelEngineProperties.isAddUnplacedToGraphicsContainer());
            rendererObject.add(MaplexLabelEngineEngineLayerPropertiesKeys.SYMBOL, ParseLayer.createSymbol((ISymbol) labelEngineProperties.getSymbol()));

            // Overposter
            JsonObject overposterObject = new JsonObject();

            IMaplexOverposterLayerProperties overposter = (IMaplexOverposterLayerProperties) labelEngineProperties.getOverposterLayerProperties();

            overposterObject.addProperty(MaplexLabelEngineEngineLayerPropertiesKeys.CONSTRAIN_OFFSET, overposter.getConstrainOffset());
            overposterObject.addProperty(MaplexLabelEngineEngineLayerPropertiesKeys.FEATURE_BUFFER, overposter.getFeatureBuffer());
            overposterObject.addProperty(MaplexLabelEngineEngineLayerPropertiesKeys.FEATURE_TYPE, overposter.getFeatureType());
            overposterObject.addProperty(MaplexLabelEngineEngineLayerPropertiesKeys.FEATURE_WEIGHT, overposter.getFeatureWeight());
            overposterObject.addProperty(MaplexLabelEngineEngineLayerPropertiesKeys.LABEL_BUFFER, overposter.getLabelBuffer());
            overposterObject.addProperty(MaplexLabelEngineEngineLayerPropertiesKeys.LABEL_PRIORITY, overposter.getLabelPriority());
            overposterObject.addProperty(MaplexLabelEngineEngineLayerPropertiesKeys.LINE_PLACEMENT_METHOD, overposter.getLinePlacementMethod());
            overposterObject.addProperty(MaplexLabelEngineEngineLayerPropertiesKeys.POINT_PLACEMENT_METHOD, overposter.getPointPlacementMethod());
            overposterObject.addProperty(MaplexLabelEngineEngineLayerPropertiesKeys.POLYGON_BOUNDARY_WEIGHT, overposter.getPolygonBoundaryWeight());
            overposterObject.addProperty(MaplexLabelEngineEngineLayerPropertiesKeys.POLYGON_PLACEMENT_METHOD, overposter.getPolygonPlacementMethod());
            overposterObject.addProperty(MaplexLabelEngineEngineLayerPropertiesKeys.PRIMARY_OFFSET_UNIT, overposter.getPrimaryOffsetUnit());
            overposterObject.addProperty(MaplexLabelEngineEngineLayerPropertiesKeys.FONT_HEIGHT_REDUCTION_LIMIT, overposter.getFontHeightReductionLimit());
            overposterObject.addProperty(MaplexLabelEngineEngineLayerPropertiesKeys.FONT_HEIGHT_REDUCTION_STEP, overposter.getFontHeightReductionStep());
            overposterObject.addProperty(MaplexLabelEngineEngineLayerPropertiesKeys.GET_FONT_WIDTH_REDUCTION_LIMIT, overposter.getFontWidthReductionLimit());
            overposterObject.addProperty(MaplexLabelEngineEngineLayerPropertiesKeys.FONT_WIDTH_REDUCTION_STEP, overposter.getFontWidthReductionStep());
            overposterObject.addProperty(MaplexLabelEngineEngineLayerPropertiesKeys.MAXIMUM_CHARACTER_SPACING, overposter.getMaximumCharacterSpacing());
            overposterObject.addProperty(MaplexLabelEngineEngineLayerPropertiesKeys.MAXIMUM_LABEL_OVERRUN, overposter.getMaximumLabelOverrun());
            overposterObject.addProperty(MaplexLabelEngineEngineLayerPropertiesKeys.MAXIMUM_WORD_SPACING, overposter.getMaximumWordSpacing());
            overposterObject.addProperty(MaplexLabelEngineEngineLayerPropertiesKeys.MINIMUM_END_OF_STREET_CLEARANCE, overposter.getMinimumEndOfStreetClearance());
            overposterObject.addProperty(MaplexLabelEngineEngineLayerPropertiesKeys.MINIMUM_REPETITION_INTERVAL, overposter.getMinimumRepetitionInterval());
            overposterObject.addProperty(MaplexLabelEngineEngineLayerPropertiesKeys.MINIMUM_SIZE_FOR_LABELING, overposter.getMinimumSizeForLabeling());
            overposterObject.addProperty(MaplexLabelEngineEngineLayerPropertiesKeys.PREFERRED_END_OF_STREET_CLEARANCE, overposter.getPreferredEndOfStreetClearance());
            overposterObject.addProperty(MaplexLabelEngineEngineLayerPropertiesKeys.PRIMARY_OFFSET, overposter.getPrimaryOffset());
            overposterObject.addProperty(MaplexLabelEngineEngineLayerPropertiesKeys.SECONDARY_OFFSET, overposter.getSecondaryOffset());
            overposterObject.addProperty(MaplexLabelEngineEngineLayerPropertiesKeys.THINNING_DISTANCE, overposter.getThinningDistance());
            overposterObject.addProperty(MaplexLabelEngineEngineLayerPropertiesKeys.ALIGN_LABEL_TO_LINE_DIRECTION, overposter.isAlignLabelToLineDirection());
            overposterObject.addProperty(MaplexLabelEngineEngineLayerPropertiesKeys.ALLOW_ASYMMETRIC_OVERRUN, overposter.isAllowAsymmetricOverrun());
            overposterObject.addProperty(MaplexLabelEngineEngineLayerPropertiesKeys.BACKGROUND_LABEL, overposter.isBackgroundLabel());
            overposterObject.addProperty(MaplexLabelEngineEngineLayerPropertiesKeys.CAN_ABBREVIATE_LABEL, overposter.isCanAbbreviateLabel());
            overposterObject.addProperty(MaplexLabelEngineEngineLayerPropertiesKeys.CAN_OVERRUN_FEATURE, overposter.isCanOverrunFeature());
            overposterObject.addProperty(MaplexLabelEngineEngineLayerPropertiesKeys.CAN_PLACE_LABEL_OUTSIDE_POLYGON, overposter.isCanPlaceLabelOutsidePolygon());
            overposterObject.addProperty(MaplexLabelEngineEngineLayerPropertiesKeys.CAN_REDUCE_FONT_SIZE, overposter.isCanReduceFontSize());
            overposterObject.addProperty(MaplexLabelEngineEngineLayerPropertiesKeys.CAN_REMOVE_OVERLAPPING_LABEL, overposter.isCanRemoveOverlappingLabel());
            overposterObject.addProperty(MaplexLabelEngineEngineLayerPropertiesKeys.CAN_SHIFT_POINT_LABEL, overposter.isCanShiftPointLabel());
            overposterObject.addProperty(MaplexLabelEngineEngineLayerPropertiesKeys.CAN_STACK_LABEL, overposter.isCanStackLabel());
            overposterObject.addProperty(MaplexLabelEngineEngineLayerPropertiesKeys.CAN_TRUNCATE_LABEL, overposter.isCanTruncateLabel());
            overposterObject.addProperty(MaplexLabelEngineEngineLayerPropertiesKeys.ENABLE_POINT_PLACEMENT_PRIORITIES, overposter.isEnablePointPlacementPriorities());
            overposterObject.addProperty(MaplexLabelEngineEngineLayerPropertiesKeys.GRATICULE_ALIGNMENT, overposter.isGraticuleAlignment());
            overposterObject.addProperty(MaplexLabelEngineEngineLayerPropertiesKeys.LAND_PARCEL_PLACEMENT, overposter.isLandParcelPlacement());
            overposterObject.addProperty(MaplexLabelEngineEngineLayerPropertiesKeys.NEVER_REMOVE_LABEL, overposter.isNeverRemoveLabel());
            overposterObject.addProperty(MaplexLabelEngineEngineLayerPropertiesKeys.PREFER_HORIZONTAL_PLACEMENT, overposter.isPreferHorizontalPlacement());
            overposterObject.addProperty(MaplexLabelEngineEngineLayerPropertiesKeys.REPEAT_LABEL, overposter.isRepeatLabel());
            overposterObject.addProperty(MaplexLabelEngineEngineLayerPropertiesKeys.SPREAD_CHARACTERS, overposter.isSpreadCharacters());
            overposterObject.addProperty(MaplexLabelEngineEngineLayerPropertiesKeys.SPREAD_WORDS, overposter.isSpreadWords());
            overposterObject.addProperty(MaplexLabelEngineEngineLayerPropertiesKeys.STREETPLACEMENT, overposter.isStreetPlacement());
            overposterObject.addProperty(MaplexLabelEngineEngineLayerPropertiesKeys.THIN_DUPLICATE_LABELS, overposter.isThinDuplicateLabels());

            rendererObject.add(MaplexLabelEngineEngineLayerPropertiesKeys.OVERPOSTER3, overposterObject);

            // IMaplexOverposterLayerProperties2
            JsonObject overposterLayerPropertiesObject = new JsonObject();

            IMaplexOverposterLayerProperties2 overposter2 = (IMaplexOverposterLayerProperties2) labelEngineProperties.getOverposterLayerProperties();

            overposterLayerPropertiesObject.addProperty(MaplexLabelEngineEngineLayerPropertiesKeys.CONTOUR_ALIGNMENT_TYPE, overposter2.getContourAlignmentType());
            overposterLayerPropertiesObject.addProperty(MaplexLabelEngineEngineLayerPropertiesKeys.CONTOUR_LADDER_TYPE, overposter2.getContourLadderType());
            overposterLayerPropertiesObject.addProperty(MaplexLabelEngineEngineLayerPropertiesKeys.CONTOUR_MAXIMUM_ANGLE, overposter2.getContourMaximumAngle());
            overposterLayerPropertiesObject.addProperty(MaplexLabelEngineEngineLayerPropertiesKeys.GRATICULE_ALIGNMENT_TYPE, overposter2.getGraticuleAlignmentType());
            overposterLayerPropertiesObject.addProperty(MaplexLabelEngineEngineLayerPropertiesKeys.LINE_FEATURE_TYPE, overposter2.getLineFeatureType());
            overposterLayerPropertiesObject.addProperty(MaplexLabelEngineEngineLayerPropertiesKeys.MAXIMUM_LABEL_OVERRUN_UNIT, overposter2.getMaximumLabelOverrunUnit());
            overposterLayerPropertiesObject.addProperty(MaplexLabelEngineEngineLayerPropertiesKeys.MINIMUM_FEATURE_SIZE_UNIT, overposter2.getMinimumFeatureSizeUnit());
            overposterLayerPropertiesObject.addProperty(MaplexLabelEngineEngineLayerPropertiesKeys.POLYGON_ANCHOR_POINT_TYPE, overposter2.getPolygonAnchorPointType());
            overposterLayerPropertiesObject.addProperty(MaplexLabelEngineEngineLayerPropertiesKeys.POLYGON_FEATURE_TYPE, overposter2.getPolygonFeatureType());
            overposterLayerPropertiesObject.addProperty(MaplexLabelEngineEngineLayerPropertiesKeys.REPETITION_INTERVAL_UNIT, overposter2.getRepetitionIntervalUnit());
            overposterLayerPropertiesObject.addProperty(MaplexLabelEngineEngineLayerPropertiesKeys.THINNING_DISTANCE_UNIT, overposter2.getThinningDistanceUnit());
            overposterLayerPropertiesObject.addProperty(MaplexLabelEngineEngineLayerPropertiesKeys.SECONDARY_OFFSET_MAXIMUM, overposter2.getSecondaryOffsetMaximum());
            overposterLayerPropertiesObject.addProperty(MaplexLabelEngineEngineLayerPropertiesKeys.SECONDARY_OFFSET_MINIMUM, overposter2.getSecondaryOffsetMinimum());
            overposterLayerPropertiesObject.addProperty(MaplexLabelEngineEngineLayerPropertiesKeys.CAN_FLIP_STACKED_STREET_LABEL, overposter2.isCanFlipStackedStreetLabel());
            overposterLayerPropertiesObject.addProperty(MaplexLabelEngineEngineLayerPropertiesKeys.CAN_PLACE_LABEL_ON_TOP_OF_FEATURE, overposter2.isCanPlaceLabelOnTopOfFeature());
            overposterLayerPropertiesObject.addProperty(MaplexLabelEngineEngineLayerPropertiesKeys.CAN_REDUCE_LEADING, overposter2.isCanReduceLeading());
            overposterLayerPropertiesObject.addProperty(MaplexLabelEngineEngineLayerPropertiesKeys.ENABLE_POLYGON_FIXED_POSITION, overposter2.isEnablePolygonFixedPosition());
            overposterLayerPropertiesObject.addProperty(MaplexLabelEngineEngineLayerPropertiesKeys.ENABLE_SECONDARY_OFFSET, overposter2.isEnableSecondaryOffset());
            overposterLayerPropertiesObject.addProperty(MaplexLabelEngineEngineLayerPropertiesKeys.LABEL_BUFFER_HARD_CONSTRAINT, overposter2.isLabelBufferHardConstraint());
            overposterLayerPropertiesObject.addProperty(MaplexLabelEngineEngineLayerPropertiesKeys.MINIMUM_SIZE_BASED_ON_AREA, overposter2.isMinimumSizeBasedOnArea());
            overposterLayerPropertiesObject.addProperty(MaplexLabelEngineEngineLayerPropertiesKeys.OFFSET_FROM_FEATURE_GEOMETRY, overposter2.isOffsetFromFeatureGeometry());
            rendererObject.add(MaplexLabelEngineEngineLayerPropertiesKeys.OVERPOSTER_LAYER_PROPERTIES, overposterLayerPropertiesObject);

            // IMaplexLabelStackingProperties
            JsonObject maplexLabelStackingPropertiesObject = new JsonObject();

            IMaplexLabelStackingProperties stacking = overposter.getLabelStackingProperties();
            maplexLabelStackingPropertiesObject.addProperty(MaplexLabelEngineEngineLayerPropertiesKeys.MAXIMUM_NUMBER_OF_CHARS_PER_LINE, stacking.getMaximumNumberOfCharsPerLine());
            maplexLabelStackingPropertiesObject.addProperty(MaplexLabelEngineEngineLayerPropertiesKeys.MAXIMUM_NUMBER_OF_LINES, stacking.getMaximumNumberOfLines());
            maplexLabelStackingPropertiesObject.addProperty(MaplexLabelEngineEngineLayerPropertiesKeys.MINIMUM_NUMBER_OF_CHARS_PER_LINE, stacking.getMinimumNumberOfCharsPerLine());
            maplexLabelStackingPropertiesObject.addProperty(MaplexLabelEngineEngineLayerPropertiesKeys.SEPARATOR_COUNT, stacking.getSeparatorCount());
            maplexLabelStackingPropertiesObject.addProperty(MaplexLabelEngineEngineLayerPropertiesKeys.STACK_JUSTIFICATION, stacking.getStackJustification());

            rendererObject.add(MaplexLabelEngineEngineLayerPropertiesKeys.MAPLEX_LABEL_STACKING_PROPERTIES, maplexLabelStackingPropertiesObject);

            // IMaplexOffsetAlongLineProperties
            JsonObject maplexOffsetAlongLinePropertiesObject = new JsonObject();

            IMaplexOffsetAlongLineProperties alongLine = overposter.getOffsetAlongLineProperties();
            maplexOffsetAlongLinePropertiesObject.addProperty(MaplexLabelEngineEngineLayerPropertiesKeys.DISTANCE_UNIT, alongLine.getDistanceUnit());
            maplexOffsetAlongLinePropertiesObject.addProperty(MaplexLabelEngineEngineLayerPropertiesKeys.LABEL_ANCHOR_POINT, alongLine.getLabelAnchorPoint());
            maplexOffsetAlongLinePropertiesObject.addProperty(MaplexLabelEngineEngineLayerPropertiesKeys.PLACEMENT_METHOD, alongLine.getPlacementMethod());
            maplexOffsetAlongLinePropertiesObject.addProperty(MaplexLabelEngineEngineLayerPropertiesKeys.DISTANCE, alongLine.getDistance());
            maplexOffsetAlongLinePropertiesObject.addProperty(MaplexLabelEngineEngineLayerPropertiesKeys.TOLERANCE, alongLine.getTolerance());
            maplexOffsetAlongLinePropertiesObject.addProperty(MaplexLabelEngineEngineLayerPropertiesKeys.IS_USE_LINE_DIRECTION, alongLine.isUseLineDirection());

            rendererObject.add(MaplexLabelEngineEngineLayerPropertiesKeys.MAPLEX_OFFSET_ALONG_LINE_PROPERTIES, maplexLabelStackingPropertiesObject);

            // IPointPlacementPriorities
            JsonObject pointPlacementPrioritiesObject = new JsonObject();

            IPointPlacementPriorities pointPlacement = overposter.getPointPlacementPriorities();
            pointPlacementPrioritiesObject.addProperty(MaplexLabelEngineEngineLayerPropertiesKeys.ABOVE_CENTER, pointPlacement.getAboveCenter());
            pointPlacementPrioritiesObject.addProperty(MaplexLabelEngineEngineLayerPropertiesKeys.ABOVE_LEFT, pointPlacement.getAboveLeft());
            pointPlacementPrioritiesObject.addProperty(MaplexLabelEngineEngineLayerPropertiesKeys.ABOVE_RIGHT, pointPlacement.getAboveRight());
            pointPlacementPrioritiesObject.addProperty(MaplexLabelEngineEngineLayerPropertiesKeys.BELOW_CENTER, pointPlacement.getBelowCenter());
            pointPlacementPrioritiesObject.addProperty(MaplexLabelEngineEngineLayerPropertiesKeys.BELOW_LEFT, pointPlacement.getBelowLeft());
            pointPlacementPrioritiesObject.addProperty(MaplexLabelEngineEngineLayerPropertiesKeys.BELOW_RIGHT, pointPlacement.getBelowRight());
            pointPlacementPrioritiesObject.addProperty(MaplexLabelEngineEngineLayerPropertiesKeys.CENTER_LEFT, pointPlacement.getCenterLeft());
            pointPlacementPrioritiesObject.addProperty(MaplexLabelEngineEngineLayerPropertiesKeys.CENTER_RIGHT, pointPlacement.getCenterRight());
            rendererObject.add(MaplexLabelEngineEngineLayerPropertiesKeys.POINT_PLACEMENT_PRIORITIES, pointPlacementPrioritiesObject);

            // IMaplexRotationProperties
            JsonObject maplexRotationPrioritiesObject = new JsonObject();

            IMaplexRotationProperties rotationProperties = overposter.getRotationProperties();
            maplexRotationPrioritiesObject.addProperty(MaplexLabelEngineEngineLayerPropertiesKeys.ROTATION_FIELD, rotationProperties.getRotationField());
            maplexRotationPrioritiesObject.addProperty(MaplexLabelEngineEngineLayerPropertiesKeys.ROTATION_TYPE, rotationProperties.getRotationType());
            rendererObject.add(MaplexLabelEngineEngineLayerPropertiesKeys.MAPLEX_ROTATION_PRIORITIES, maplexRotationPrioritiesObject);

            // MaplexOverposterProperties
            JsonObject maplexOverposterPropertiesObject = new JsonObject();

            IMapOverposter mapOverposter = (IMapOverposter) map;
            IOverposterProperties overpostProperties = mapOverposter.getOverposterProperties();
            IMaplexOverposterProperties maplexOverposterProperties = (IMaplexOverposterProperties) overpostProperties;

            maplexOverposterPropertiesObject.addProperty(MaplexLabelEngineEngineLayerPropertiesKeys.ALLOW_BORDER_OVERLAP, maplexOverposterProperties.isAllowBorderOverlap());
            maplexOverposterPropertiesObject.addProperty(MaplexLabelEngineEngineLayerPropertiesKeys.PLACEMENT_QUALITY, maplexOverposterProperties.getPlacementQuality());
            maplexOverposterPropertiesObject.addProperty(MaplexLabelEngineEngineLayerPropertiesKeys.CONNECTION_TYPE, maplexOverposterProperties.getConnectionType());

            // Dictionaries
            JsonArray maplexDictionariesArrayObject = new JsonArray();
            IMaplexDictionaries dictionaries = maplexOverposterProperties.getDictionaries();

            for(int index = 0; index < dictionaries.getDictionaryCount(); index ++)
            {
                IMaplexDictionary dict = dictionaries.getDictionary(index);
                JsonObject maplexDictionaryObject = new JsonObject();

                maplexDictionaryObject.addProperty(MaplexLabelEngineEngineLayerPropertiesKeys.DICTIONARY_NAME, dict.getName());

                for(int entry = 0; entry < dict.getEntryCount(); entry ++)
                {
                    JsonObject maplexDictionaryEntryObject = new JsonObject();
                    IMaplexDictionaryEntry dictEntry = dict.getEntry(entry);

                    maplexDictionaryEntryObject.addProperty(MaplexLabelEngineEngineLayerPropertiesKeys.DICTIONARY_ABBREVIATION, dictEntry.getAbbreviation());
                    maplexDictionaryEntryObject.addProperty(MaplexLabelEngineEngineLayerPropertiesKeys.DICTIONARY_TEXT, dictEntry.getText());
                    maplexDictionaryEntryObject.addProperty(MaplexLabelEngineEngineLayerPropertiesKeys.DICTIONARY_TYPE, dictEntry.getType());

                    maplexDictionaryObject.add(MaplexLabelEngineEngineLayerPropertiesKeys.DICTIONARY_ENTRY, maplexDictionaryEntryObject);
                }
                maplexDictionariesArrayObject.add(maplexDictionaryObject);
            }
            maplexOverposterPropertiesObject.add(MaplexLabelEngineEngineLayerPropertiesKeys.MAPLEX_OVERPOSTER_PROPERTIES_DICTIONARIES, maplexDictionariesArrayObject);
            rendererObject.add(MaplexLabelEngineEngineLayerPropertiesKeys.MAPLEX_OVERPOSTER_PROPERTIES, maplexOverposterPropertiesObject);

            jsonObject.add(MaplexLabelEngineEngineLayerPropertiesKeys.MAPLEX_LABEL_ENGINE_LAYER_PROPERTIES, rendererObject);

        } catch (AutomationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

}
