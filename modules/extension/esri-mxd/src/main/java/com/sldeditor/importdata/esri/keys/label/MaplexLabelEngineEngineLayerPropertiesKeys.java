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

package com.sldeditor.importdata.esri.keys.label;

/**
 * The Class MaplexLabelEngineEngineLayerPropertiesKeys, contains all the keys used within
 * the intermediate json file to represent an Esri MXD maplex label renderer that the
 * SLD Editor can understand.
 *
 * @author Robert Ward (SCISYS)
 */
public class MaplexLabelEngineEngineLayerPropertiesKeys {

    public static final String DICTIONARY_ENTRY = "entry";
    public static final String DICTIONARY_TYPE = "type";
    public static final String DICTIONARY_TEXT = "text";
    public static final String DICTIONARY_ABBREVIATION = "abbreviation";
    public static final String DICTIONARY_NAME = "name";
    public static final String MAPLEX_OVERPOSTER_PROPERTIES_DICTIONARIES = "dictionaries";
    public static final String MAPLEX_LABEL_ENGINE_LAYER_PROPERTIES = "MaplexLabelEngineLayerProperties";
    public static final String MAPLEX_OVERPOSTER_PROPERTIES = "maplexOverposterProperties";
    public static final String CONNECTION_TYPE = "connectionType";
    public static final String PLACEMENT_QUALITY = "placementQuality";
    public static final String ALLOW_BORDER_OVERLAP = "allowBorderOverlap";
    public static final String MAPLEX_ROTATION_PRIORITIES = "maplexRotationPriorities";
    public static final String ROTATION_TYPE = "rotationType";
    public static final String ROTATION_FIELD = "rotationField";
    public static final String POINT_PLACEMENT_PRIORITIES = "pointPlacementPriorities";
    public static final String CENTER_RIGHT = "centerRight";
    public static final String CENTER_LEFT = "centerLeft";
    public static final String BELOW_RIGHT = "belowRight";
    public static final String BELOW_LEFT = "belowLeft";
    public static final String BELOW_CENTER = "belowCenter";
    public static final String ABOVE_RIGHT = "aboveRight";
    public static final String ABOVE_LEFT = "aboveLeft";
    public static final String ABOVE_CENTER = "aboveCenter";
    public static final String MAPLEX_OFFSET_ALONG_LINE_PROPERTIES = "maplexOffsetAlongLineProperties";
    public static final String IS_USE_LINE_DIRECTION = "isUseLineDirection";
    public static final String TOLERANCE = "tolerance";
    public static final String DISTANCE = "distance";
    public static final String PLACEMENT_METHOD = "placementMethod";
    public static final String LABEL_ANCHOR_POINT = "labelAnchorPoint";
    public static final String DISTANCE_UNIT = "distanceUnit";
    public static final String MAPLEX_LABEL_STACKING_PROPERTIES = "maplexLabelStackingProperties";
    public static final String STACK_JUSTIFICATION = "stackJustification";
    public static final String SEPARATOR_COUNT = "separatorCount";
    public static final String MINIMUM_NUMBER_OF_CHARS_PER_LINE = "minimumNumberOfCharsPerLine";
    public static final String MAXIMUM_NUMBER_OF_LINES = "maximumNumberOfLines";
    public static final String MAXIMUM_NUMBER_OF_CHARS_PER_LINE = "maximumNumberOfCharsPerLine";
    public static final String OVERPOSTER_LAYER_PROPERTIES = "overposterLayerProperties";
    public static final String OFFSET_FROM_FEATURE_GEOMETRY = "offsetFromFeatureGeometry";
    public static final String MINIMUM_SIZE_BASED_ON_AREA = "minimumSizeBasedOnArea";
    public static final String LABEL_BUFFER_HARD_CONSTRAINT = "labelBufferHardConstraint";
    public static final String ENABLE_SECONDARY_OFFSET = "enableSecondaryOffset";
    public static final String ENABLE_POLYGON_FIXED_POSITION = "enablePolygonFixedPosition";
    public static final String CAN_REDUCE_LEADING = "canReduceLeading";
    public static final String CAN_PLACE_LABEL_ON_TOP_OF_FEATURE = "canPlaceLabelOnTopOfFeature";
    public static final String CAN_FLIP_STACKED_STREET_LABEL = "canFlipStackedStreetLabel";
    public static final String SECONDARY_OFFSET_MINIMUM = "secondaryOffsetMinimum";
    public static final String SECONDARY_OFFSET_MAXIMUM = "secondaryOffsetMaximum";
    public static final String THINNING_DISTANCE_UNIT = "thinningDistanceUnit";
    public static final String REPETITION_INTERVAL_UNIT = "repetitionIntervalUnit";
    public static final String POLYGON_FEATURE_TYPE = "polygonFeatureType";
    public static final String POLYGON_ANCHOR_POINT_TYPE = "polygonAnchorPointType";
    public static final String MINIMUM_FEATURE_SIZE_UNIT = "minimumFeatureSizeUnit";
    public static final String MAXIMUM_LABEL_OVERRUN_UNIT = "maximumLabelOverrunUnit";
    public static final String LINE_FEATURE_TYPE = "lineFeatureType";
    public static final String GRATICULE_ALIGNMENT_TYPE = "graticuleAlignmentType";
    public static final String CONTOUR_MAXIMUM_ANGLE = "contourMaximumAngle";
    public static final String CONTOUR_LADDER_TYPE = "contourLadderType";
    public static final String CONTOUR_ALIGNMENT_TYPE = "contourAlignmentType";
    public static final String OVERPOSTER3 = "overposter";
    public static final String THIN_DUPLICATE_LABELS = "thinDuplicateLabels";
    public static final String STREETPLACEMENT = "streetplacement";
    public static final String SPREAD_WORDS = "spreadWords";
    public static final String SPREAD_CHARACTERS = "spreadCharacters";
    public static final String REPEAT_LABEL = "repeatLabel";
    public static final String PREFER_HORIZONTAL_PLACEMENT = "preferHorizontalPlacement";
    public static final String NEVER_REMOVE_LABEL = "neverRemoveLabel";
    public static final String LAND_PARCEL_PLACEMENT = "landParcelPlacement";
    public static final String GRATICULE_ALIGNMENT = "graticuleAlignment";
    public static final String ENABLE_POINT_PLACEMENT_PRIORITIES = "enablePointPlacementPriorities";
    public static final String CAN_TRUNCATE_LABEL = "canTruncateLabel";
    public static final String CAN_STACK_LABEL = "canStackLabel";
    public static final String CAN_SHIFT_POINT_LABEL = "canShiftPointLabel";
    public static final String CAN_REMOVE_OVERLAPPING_LABEL = "canRemoveOverlappingLabel";
    public static final String CAN_REDUCE_FONT_SIZE = "canReduceFontSize";
    public static final String CAN_PLACE_LABEL_OUTSIDE_POLYGON = "canPlaceLabelOutsidePolygon";
    public static final String CAN_OVERRUN_FEATURE = "canOverrunFeature";
    public static final String CAN_ABBREVIATE_LABEL = "canAbbreviateLabel";
    public static final String BACKGROUND_LABEL = "backgroundLabel";
    public static final String ALLOW_ASYMMETRIC_OVERRUN = "allowAsymmetricOverrun";
    public static final String ALIGN_LABEL_TO_LINE_DIRECTION = "alignLabelToLineDirection";
    public static final String THINNING_DISTANCE = "thinningDistance";
    public static final String SECONDARY_OFFSET = "secondaryOffset";
    public static final String PRIMARY_OFFSET = "primaryOffset";
    public static final String PREFERRED_END_OF_STREET_CLEARANCE = "preferredEndOfStreetClearance";
    public static final String MINIMUM_SIZE_FOR_LABELING = "minimumSizeForLabeling";
    public static final String MINIMUM_REPETITION_INTERVAL = "minimumRepetitionInterval";
    public static final String MINIMUM_END_OF_STREET_CLEARANCE = "minimumEndOfStreetClearance";
    public static final String MAXIMUM_WORD_SPACING = "maximumWordSpacing";
    public static final String MAXIMUM_LABEL_OVERRUN = "maximumLabelOverrun";
    public static final String MAXIMUM_CHARACTER_SPACING = "maximumCharacterSpacing";
    public static final String FONT_WIDTH_REDUCTION_STEP = "fontWidthReductionStep";
    public static final String GET_FONT_WIDTH_REDUCTION_LIMIT = "getFontWidthReductionLimit";
    public static final String FONT_HEIGHT_REDUCTION_STEP = "fontHeightReductionStep";
    public static final String FONT_HEIGHT_REDUCTION_LIMIT = "fontHeightReductionLimit";
    public static final String PRIMARY_OFFSET_UNIT = "primaryOffsetUnit";
    public static final String POLYGON_PLACEMENT_METHOD = "polygonPlacementMethod";
    public static final String POLYGON_BOUNDARY_WEIGHT = "polygonBoundaryWeight";
    public static final String POINT_PLACEMENT_METHOD = "pointPlacementMethod";
    public static final String LINE_PLACEMENT_METHOD = "linePlacementMethod";
    public static final String LABEL_PRIORITY = "labelPriority";
    public static final String LABEL_BUFFER = "labelBuffer";
    public static final String FEATURE_WEIGHT = "featureWeight";
    public static final String FEATURE_TYPE = "featureType";
    public static final String FEATURE_BUFFER = "featureBuffer";
    public static final String CONSTRAIN_OFFSET = "constrainOffset";
    public static final String SYMBOL = "symbol";
    public static final String ADD_UNPLACED_TO_GRAPHICS_CONTAINER = "addUnplacedToGraphicsContainer";
    public static final String UNITS = "units";
    public static final String SCALE_RATIO = "scaleRatio";
    public static final String REFERENCE_SCALE = "referenceScale";
    public static final String PRIORITY = "priority";
    public static final String OFFSET = "offset";
    public static final String LABEL_WHICH_FEATURES = "labelWhichFeatures";
    public static final String WHERE = "where";
    public static final String EXPRESSION = "expression";
}
