//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: [TEXT REMOVED by maven-replacer-plugin]
//


package com.sldeditor.common.xml.ui;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for FieldIdEnum.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="FieldIdEnum"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="UNKNOWN"/&gt;
 *     &lt;enumeration value="GEOMETRY"/&gt;
 *     &lt;enumeration value="TITLE"/&gt;
 *     &lt;enumeration value="SYMBOL_TYPE"/&gt;
 *     &lt;enumeration value="SIZE"/&gt;
 *     &lt;enumeration value="ANGLE"/&gt;
 *     &lt;enumeration value="DISPLACEMENT_X"/&gt;
 *     &lt;enumeration value="DISPLACEMENT_Y"/&gt;
 *     &lt;enumeration value="ANCHOR_POINT_V"/&gt;
 *     &lt;enumeration value="ANCHOR_POINT_H"/&gt;
 *     &lt;enumeration value="GAP"/&gt;
 *     &lt;enumeration value="INITIAL_GAP"/&gt;
 *     &lt;enumeration value="OPACITY"/&gt;
 *     &lt;enumeration value="FILTER"/&gt;
 *     &lt;enumeration value="FILL_COLOUR"/&gt;
 *     &lt;enumeration value="DEFAULT_STYLE"/&gt;
 *     &lt;enumeration value="ELSE_FILTER"/&gt;
 *     &lt;enumeration value="EXTERNAL_GRAPHIC"/&gt;
 *     &lt;enumeration value="LABEL"/&gt;
 *     &lt;enumeration value="DESCRIPTION"/&gt;
 *     &lt;enumeration value="MINIMUM_SCALE"/&gt;
 *     &lt;enumeration value="MAXIMUM_SCALE"/&gt;
 *     &lt;enumeration value="UOM"/&gt;
 *     &lt;enumeration value="NAME"/&gt;
 *     &lt;enumeration value="STROKE_STYLE"/&gt;
 *     &lt;enumeration value="STROKE_FILL_COLOUR"/&gt;
 *     &lt;enumeration value="STROKE_FILL_OPACITY"/&gt;
 *     &lt;enumeration value="STROKE_FILL_WIDTH"/&gt;
 *     &lt;enumeration value="STROKE_WIDTH"/&gt;
 *     &lt;enumeration value="STROKE_LINE_JOIN"/&gt;
 *     &lt;enumeration value="STROKE_LINE_CAP"/&gt;
 *     &lt;enumeration value="STROKE_DASH_ARRAY"/&gt;
 *     &lt;enumeration value="STROKE_OFFSET"/&gt;
 *     &lt;enumeration value="STROKE_STROKE_COLOUR"/&gt;
 *     &lt;enumeration value="STROKE_STROKE_OPACITY"/&gt;
 *     &lt;enumeration value="STROKE_SYMBOL_SIZE"/&gt;
 *     &lt;enumeration value="STROKE_SYMBOL_ANGLE"/&gt;
 *     &lt;enumeration value="STROKE_SYMBOL_DISPLACEMENT_X"/&gt;
 *     &lt;enumeration value="STROKE_SYMBOL_DISPLACEMENT_Y"/&gt;
 *     &lt;enumeration value="STROKE_SYMBOL_ANCHOR_POINT_V"/&gt;
 *     &lt;enumeration value="STROKE_SYMBOL_ANCHOR_POINT_H"/&gt;
 *     &lt;enumeration value="STROKE_SYMBOL_GAP"/&gt;
 *     &lt;enumeration value="STROKE_SYMBOL_INITIAL_GAP"/&gt;
 *     &lt;enumeration value="FONT_FAMILY"/&gt;
 *     &lt;enumeration value="FONT_STYLE"/&gt;
 *     &lt;enumeration value="FONT_WEIGHT"/&gt;
 *     &lt;enumeration value="FONT_SIZE"/&gt;
 *     &lt;enumeration value="FONT_PREVIEW"/&gt;
 *     &lt;enumeration value="HALO_COLOUR"/&gt;
 *     &lt;enumeration value="HALO_RADIUS"/&gt;
 *     &lt;enumeration value="LABEL_ALLOW_OVERRUNS"/&gt;
 *     &lt;enumeration value="LABEL_AUTO_WRAP"/&gt;
 *     &lt;enumeration value="LABEL_CONFLICT_RESOLUTION"/&gt;
 *     &lt;enumeration value="LABEL_FOLLOW_LINE"/&gt;
 *     &lt;enumeration value="LABEL_FORCE_LEFT_TO_RIGHT"/&gt;
 *     &lt;enumeration value="LABEL_GOODNESS_OF_FIT"/&gt;
 *     &lt;enumeration value="LABEL_GRAPHIC_MARGIN"/&gt;
 *     &lt;enumeration value="LABEL_GRAPHIC_RESIZE"/&gt;
 *     &lt;enumeration value="LABEL_GROUP"/&gt;
 *     &lt;enumeration value="LABEL_LABEL_ALL_GROUP"/&gt;
 *     &lt;enumeration value="LABEL_LABEL_REPEAT"/&gt;
 *     &lt;enumeration value="LABEL_MAX_ANGLE_DELTA"/&gt;
 *     &lt;enumeration value="LABEL_MAX_DISPLACEMENT"/&gt;
 *     &lt;enumeration value="LABEL_MIN_GROUP_DISTANCE"/&gt;
 *     &lt;enumeration value="LABEL_PARTIALS"/&gt;
 *     &lt;enumeration value="LABEL_POLYGONALIGN"/&gt;
 *     &lt;enumeration value="LABEL_SPACE_AROUND"/&gt;
 *     &lt;enumeration value="MAX_DISPLACEMENT"/&gt;
 *     &lt;enumeration value="PERPENDICULAR_OFFSET"/&gt;
 *     &lt;enumeration value="LABEL_PLACEMENT"/&gt;
 *     &lt;enumeration value="ALIGN"/&gt;
 *     &lt;enumeration value="REPEATED"/&gt;
 *     &lt;enumeration value="GENERALISED_LINE"/&gt;
 *     &lt;enumeration value="TRANSFORMATION"/&gt;
 *     &lt;enumeration value="TTF_SYMBOL"/&gt;
 *     &lt;enumeration value="RANDOM_FILL_ACTIVATE"/&gt;
 *     &lt;enumeration value="RANDOM_FILL_TILE_SIZE"/&gt;
 *     &lt;enumeration value="RANDOM_FILL_ROTATION"/&gt;
 *     &lt;enumeration value="RANDOM_FILL_SYMBOL_COUNT"/&gt;
 *     &lt;enumeration value="RANDOM_FILL_RANDOM_SEED"/&gt;
 *     &lt;enumeration value="WINDBARBS"/&gt;
 *     &lt;enumeration value="WINDBARB_WINDSPEED"/&gt;
 *     &lt;enumeration value="WINDBARB_WINDSPEED_UNITS"/&gt;
 *     &lt;enumeration value="WINDBARB_NORTHERN_HEMISPHERE"/&gt;
 *     &lt;enumeration value="WKT"/&gt;
 *     &lt;enumeration value="FUNCTION"/&gt;
 *     &lt;enumeration value="RASTER_OPACITY"/&gt;
 *     &lt;enumeration value="RASTER_CONTRAST_GAMMAVALUE"/&gt;
 *     &lt;enumeration value="RASTER_OVERLAP_BEHAVIOUR"/&gt;
 *     &lt;enumeration value="RASTER_SHADEDRELIEF_BRIGHTNESS"/&gt;
 *     &lt;enumeration value="RASTER_SHADEDRELIEF_FACTOR"/&gt;
 *     &lt;enumeration value="RASTER_COLOURMAP_TYPE"/&gt;
 *     &lt;enumeration value="RASTER_COLOURMAP"/&gt;
 *     &lt;enumeration value="RASTER_RGB_RED_NAME"/&gt;
 *     &lt;enumeration value="RASTER_RGB_CHANNEL_RED_CONTRAST_GAMMA"/&gt;
 *     &lt;enumeration value="RASTER_RGB_GREEN_NAME"/&gt;
 *     &lt;enumeration value="RASTER_RGB_CHANNEL_GREEN_CONTRAST_GAMMA"/&gt;
 *     &lt;enumeration value="RASTER_RGB_BLUE_NAME"/&gt;
 *     &lt;enumeration value="RASTER_RGB_CHANNEL_BLUE_CONTRAST_GAMMA"/&gt;
 *     &lt;enumeration value="RASTER_RGB_GREY_NAME"/&gt;
 *     &lt;enumeration value="RASTER_RGB_CHANNEL_GREY_CONTRAST_GAMMA"/&gt;
 *     &lt;enumeration value="RASTER_COLOURMAP_ENTRY_LABEL"/&gt;
 *     &lt;enumeration value="RASTER_COLOURMAP_ENTRY_COLOUR"/&gt;
 *     &lt;enumeration value="RASTER_COLOURMAP_ENTRY_OPACITY"/&gt;
 *     &lt;enumeration value="RASTER_COLOURMAP_ENTRY_QUANTITY"/&gt;
 *     &lt;enumeration value="VO_RASTER_NORMALIZE_ALGORITHM_RED"/&gt;
 *     &lt;enumeration value="VO_RASTER_NORMALIZE_MIN_VALUE_RED"/&gt;
 *     &lt;enumeration value="VO_RASTER_NORMALIZE_MAX_VALUE_RED"/&gt;
 *     &lt;enumeration value="VO_RASTER_NORMALIZE_ALGORITHM_GREEN"/&gt;
 *     &lt;enumeration value="VO_RASTER_NORMALIZE_MIN_VALUE_GREEN"/&gt;
 *     &lt;enumeration value="VO_RASTER_NORMALIZE_MAX_VALUE_GREEN"/&gt;
 *     &lt;enumeration value="VO_RASTER_NORMALIZE_ALGORITHM_BLUE"/&gt;
 *     &lt;enumeration value="VO_RASTER_NORMALIZE_MIN_VALUE_BLUE"/&gt;
 *     &lt;enumeration value="VO_RASTER_NORMALIZE_MAX_VALUE_BLUE"/&gt;
 *     &lt;enumeration value="VO_RASTER_NORMALIZE_ALGORITHM_GREY"/&gt;
 *     &lt;enumeration value="VO_RASTER_NORMALIZE_MIN_VALUE_GREY"/&gt;
 *     &lt;enumeration value="VO_RASTER_NORMALIZE_MAX_VALUE_GREY"/&gt;
 *     &lt;enumeration value="VO_RASTER_NORMALIZE_ALGORITHM_OVERALL"/&gt;
 *     &lt;enumeration value="VO_RASTER_NORMALIZE_MIN_VALUE_OVERALL"/&gt;
 *     &lt;enumeration value="VO_RASTER_NORMALIZE_MAX_VALUE_OVERALL"/&gt;
 *     &lt;enumeration value="REMOTE_OWS_SERVICE"/&gt;
 *     &lt;enumeration value="REMOTE_OWS_ONLINERESOURCE"/&gt;
 *     &lt;enumeration value="INLINE_FEATURE"/&gt;
 *     &lt;enumeration value="LAYER_FEATURE_CONSTRAINTS"/&gt;
 *     &lt;enumeration value="COLOUR_RAMP_TYPE"/&gt;
 *     &lt;enumeration value="COLOUR_RAMP_COLOUR"/&gt;
 *     &lt;enumeration value="COLOUR_RAMP_REVERSE"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "FieldIdEnum")
@XmlEnum
public enum FieldIdEnum {

    UNKNOWN,
    GEOMETRY,
    TITLE,
    SYMBOL_TYPE,
    SIZE,
    ANGLE,
    DISPLACEMENT_X,
    DISPLACEMENT_Y,
    ANCHOR_POINT_V,
    ANCHOR_POINT_H,
    GAP,
    INITIAL_GAP,
    OPACITY,
    FILTER,
    FILL_COLOUR,
    DEFAULT_STYLE,
    ELSE_FILTER,
    EXTERNAL_GRAPHIC,
    LABEL,
    DESCRIPTION,
    MINIMUM_SCALE,
    MAXIMUM_SCALE,
    UOM,
    NAME,
    STROKE_STYLE,
    STROKE_FILL_COLOUR,
    STROKE_FILL_OPACITY,
    STROKE_FILL_WIDTH,
    STROKE_WIDTH,
    STROKE_LINE_JOIN,
    STROKE_LINE_CAP,
    STROKE_DASH_ARRAY,
    STROKE_OFFSET,
    STROKE_STROKE_COLOUR,
    STROKE_STROKE_OPACITY,
    STROKE_SYMBOL_SIZE,
    STROKE_SYMBOL_ANGLE,
    STROKE_SYMBOL_DISPLACEMENT_X,
    STROKE_SYMBOL_DISPLACEMENT_Y,
    STROKE_SYMBOL_ANCHOR_POINT_V,
    STROKE_SYMBOL_ANCHOR_POINT_H,
    STROKE_SYMBOL_GAP,
    STROKE_SYMBOL_INITIAL_GAP,
    FONT_FAMILY,
    FONT_STYLE,
    FONT_WEIGHT,
    FONT_SIZE,
    FONT_PREVIEW,
    HALO_COLOUR,
    HALO_RADIUS,
    LABEL_ALLOW_OVERRUNS,
    LABEL_AUTO_WRAP,
    LABEL_CONFLICT_RESOLUTION,
    LABEL_FOLLOW_LINE,
    LABEL_FORCE_LEFT_TO_RIGHT,
    LABEL_GOODNESS_OF_FIT,
    LABEL_GRAPHIC_MARGIN,
    LABEL_GRAPHIC_RESIZE,
    LABEL_GROUP,
    LABEL_LABEL_ALL_GROUP,
    LABEL_LABEL_REPEAT,
    LABEL_MAX_ANGLE_DELTA,
    LABEL_MAX_DISPLACEMENT,
    LABEL_MIN_GROUP_DISTANCE,
    LABEL_PARTIALS,
    LABEL_POLYGONALIGN,
    LABEL_SPACE_AROUND,
    MAX_DISPLACEMENT,
    PERPENDICULAR_OFFSET,
    LABEL_PLACEMENT,
    ALIGN,
    REPEATED,
    GENERALISED_LINE,
    TRANSFORMATION,
    TTF_SYMBOL,
    RANDOM_FILL_ACTIVATE,
    RANDOM_FILL_TILE_SIZE,
    RANDOM_FILL_ROTATION,
    RANDOM_FILL_SYMBOL_COUNT,
    RANDOM_FILL_RANDOM_SEED,
    WINDBARBS,
    WINDBARB_WINDSPEED,
    WINDBARB_WINDSPEED_UNITS,
    WINDBARB_NORTHERN_HEMISPHERE,
    WKT,
    FUNCTION,
    RASTER_OPACITY,
    RASTER_CONTRAST_GAMMAVALUE,
    RASTER_OVERLAP_BEHAVIOUR,
    RASTER_SHADEDRELIEF_BRIGHTNESS,
    RASTER_SHADEDRELIEF_FACTOR,
    RASTER_COLOURMAP_TYPE,
    RASTER_COLOURMAP,
    RASTER_RGB_RED_NAME,
    RASTER_RGB_CHANNEL_RED_CONTRAST_GAMMA,
    RASTER_RGB_GREEN_NAME,
    RASTER_RGB_CHANNEL_GREEN_CONTRAST_GAMMA,
    RASTER_RGB_BLUE_NAME,
    RASTER_RGB_CHANNEL_BLUE_CONTRAST_GAMMA,
    RASTER_RGB_GREY_NAME,
    RASTER_RGB_CHANNEL_GREY_CONTRAST_GAMMA,
    RASTER_COLOURMAP_ENTRY_LABEL,
    RASTER_COLOURMAP_ENTRY_COLOUR,
    RASTER_COLOURMAP_ENTRY_OPACITY,
    RASTER_COLOURMAP_ENTRY_QUANTITY,
    VO_RASTER_NORMALIZE_ALGORITHM_RED,
    VO_RASTER_NORMALIZE_MIN_VALUE_RED,
    VO_RASTER_NORMALIZE_MAX_VALUE_RED,
    VO_RASTER_NORMALIZE_ALGORITHM_GREEN,
    VO_RASTER_NORMALIZE_MIN_VALUE_GREEN,
    VO_RASTER_NORMALIZE_MAX_VALUE_GREEN,
    VO_RASTER_NORMALIZE_ALGORITHM_BLUE,
    VO_RASTER_NORMALIZE_MIN_VALUE_BLUE,
    VO_RASTER_NORMALIZE_MAX_VALUE_BLUE,
    VO_RASTER_NORMALIZE_ALGORITHM_GREY,
    VO_RASTER_NORMALIZE_MIN_VALUE_GREY,
    VO_RASTER_NORMALIZE_MAX_VALUE_GREY,
    VO_RASTER_NORMALIZE_ALGORITHM_OVERALL,
    VO_RASTER_NORMALIZE_MIN_VALUE_OVERALL,
    VO_RASTER_NORMALIZE_MAX_VALUE_OVERALL,
    REMOTE_OWS_SERVICE,
    REMOTE_OWS_ONLINERESOURCE,
    INLINE_FEATURE,
    LAYER_FEATURE_CONSTRAINTS,
    COLOUR_RAMP_TYPE,
    COLOUR_RAMP_COLOUR,
    COLOUR_RAMP_REVERSE;

    public String value() {
        return name();
    }

    public static FieldIdEnum fromValue(String v) {
        return valueOf(v);
    }

}
