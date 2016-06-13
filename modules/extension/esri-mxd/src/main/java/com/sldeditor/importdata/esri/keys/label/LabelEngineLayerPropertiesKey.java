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
 * The Class LabelEngineLayerPropertiesKey, contains all the keys used within
 * the intermediate json file to represent an Esri MXD simple renderer that the
 * SLD Editor can understand.
 *
 * @author Robert Ward (SCISYS)
 */
public class LabelEngineLayerPropertiesKey {

    public static final String NAME = "LabelEngineLayerProperties";

    public static final String CLASS = "class";
    public static final String SYMBOL = "symbol";
    public static final String ADD_UNPLACED_TO_GRAPHICS_CONTAINER = "addUnplacedToGraphicsContainer";
    public static final String ANNOTATION_MAXIMUM_SCALE = "annotationMaximumScale";
    public static final String ANNOTATION_MINIMUM_SCALE = "annotationMinimumScale";
    public static final String UNITS = "units";
    public static final String SCALE_RATIO = "scaleRatio";
    public static final String REFERENCE_SCALE = "referenceScale";
    public static final String PRIORITY = "priority";
    public static final String OFFSET = "offset";
    public static final String LABEL_WHICH_FEATURES = "labelWhichFeatures";
    public static final String WHERE = "where";
    public static final String EXPRESSION = "expression";
    
    public static final String OVERPOSTER_LAYER_PROPERTIES = "overposterLayerProperties";
    public static final String OVERPOSTER_IS_PLACE_SYMBOLS = "isPlaceSymbols";
    public static final String OVERPOSTER_IS_PLACE_LABELS = "isPlaceLabels";
    public static final String OVERPOSTER_IS_BARRIER = "isBarrier";

    public static final String BASIC_OVERPOSTER_LAYER_PROPERTIES = "basicOverposterLayerProperties";
    public static final String BASIC_OVERPOSTER_LAYER_LINE_LABEL_PLACEMENT_PRIORITIES = "lineLabelPlacementPriorities";
    public static final String BASIC_OVERPOSTER_LAYER_LINE_CENTRE_START = "centreStart";
    public static final String BASIC_OVERPOSTER_LAYER_LINE_CENTRE_END = "centreEnd";
    public static final String BASIC_OVERPOSTER_LAYER_LINE_CENTRE_BEFORE = "centreBefore";
    public static final String BASIC_OVERPOSTER_LAYER_LINE_CENTRE_ALONG = "centreAlong";
    public static final String BASIC_OVERPOSTER_LAYER_LINE_CENTRE_AFTER = "centreAfter";
    public static final String BASIC_OVERPOSTER_LAYER_LINE_BELOW_START = "belowStart";
    public static final String BASIC_OVERPOSTER_LAYER_LINE_BELOW_END = "belowEnd";
    public static final String BASIC_OVERPOSTER_LAYER_LINE_BELOW_BEFORE = "belowBefore";
    public static final String BASIC_OVERPOSTER_LAYER_LINE_BELOW_ALONG = "belowAlong";
    public static final String BASIC_OVERPOSTER_LAYER_LINE_BELOW_AFTER = "belowAfter";
    public static final String BASIC_OVERPOSTER_LAYER_LINE_ABOVE_START = "aboveStart";
    public static final String BASIC_OVERPOSTER_LAYER_LINE_ABOVE_END = "aboveEnd";
    public static final String BASIC_OVERPOSTER_LAYER_LINE_ABOVE_BEFORE = "aboveBefore";
    public static final String BASIC_OVERPOSTER_LAYER_LINE_ABOVE_ALONG = "aboveAlong";
    public static final String BASIC_OVERPOSTER_LAYER_LINE_ABOVE_AFTER = "aboveAfter";
    public static final String BASIC_OVERPOSTER_LAYER_POINT_PLACEMENT_METHOD = "pointPlacementMethod";
    public static final String BASIC_OVERPOSTER_LAYER_NUM_LABELS_OPTION = "numLabelsOption";
    public static final String BASIC_OVERPOSTER_LAYER_LINE_OFFSET = "lineOffset";
    public static final String BASIC_OVERPOSTER_LAYER_LABEL_WEIGHT = "labelWeight";
    public static final String BASIC_OVERPOSTER_LAYER_FEATURE_WEIGHT = "featureWeight";
    public static final String BASIC_OVERPOSTER_LAYER_FEATURE_TYPE = "featureType";
    public static final String BASIC_OVERPOSTER_LAYER_BUFFER_RATIO = "bufferRatio";

    public static final String LINE_LABEL_POSITION_IS_RIGHT = "isRight";
    public static final String LINE_LABEL_POSITION_IS_PRODUCE_CURVED_LABELS = "isProduceCurvedLabels";
    public static final String LINE_LABEL_POSITION_IS_PERPENDICULAR = "isPerpendicular";
    public static final String LINE_LABEL_POSITION_IS_PARALLEL = "isParallel";
    public static final String LINE_LABEL_POSITION_IS_ON_TOP = "isOnTop";
    public static final String LINE_LABEL_POSITION_IS_LEFT = "isLeft";
    public static final String LINE_LABEL_POSITION_IS_IN_LINE = "isInLine";
    public static final String LINE_LABEL_POSITION_IS_HORIZONTAL = "isHorizontal";
    public static final String LINE_LABEL_POSITION_IS_BELOW = "isBelow";
    public static final String LINE_LABEL_POSITION_IS_AT_START = "isAtStart";
    public static final String LINE_LABEL_POSITION_IS_AT_END = "isAtEnd";
    public static final String LINE_LABEL_POSITION_IS_ABOVE = "isAbove";
    public static final String LINE_LABEL_POSITION_OFFSET = "offset";
    public static final String LINE_LABEL_POSITION = "lineLabelPosition";
}
