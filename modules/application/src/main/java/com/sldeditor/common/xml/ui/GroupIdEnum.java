//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-833 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: [TEXT REMOVED by maven-replacer-plugin]
//


package com.sldeditor.common.xml.ui;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for GroupIdEnum.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="GroupIdEnum">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="UNKNOWN"/>
 *     &lt;enumeration value="STANDARD"/>
 *     &lt;enumeration value="GENERAL"/>
 *     &lt;enumeration value="ANCHORPOINT"/>
 *     &lt;enumeration value="DISPLACEMENT"/>
 *     &lt;enumeration value="ROTATION"/>
 *     &lt;enumeration value="SCALE"/>
 *     &lt;enumeration value="FILL"/>
 *     &lt;enumeration value="FILLSYMBOL"/>
 *     &lt;enumeration value="STROKE"/>
 *     &lt;enumeration value="STROKESYMBOL"/>
 *     &lt;enumeration value="STROKEANCHORPOINT"/>
 *     &lt;enumeration value="STROKEDISPLACEMENT"/>
 *     &lt;enumeration value="FILLCOLOUR"/>
 *     &lt;enumeration value="STROKECOLOUR"/>
 *     &lt;enumeration value="FONT"/>
 *     &lt;enumeration value="HALO"/>
 *     &lt;enumeration value="PLACEMENT"/>
 *     &lt;enumeration value="POINTPLACEMENT"/>
 *     &lt;enumeration value="LINEPLACEMENT"/>
 *     &lt;enumeration value="HALO"/>
 *     &lt;enumeration value="VO_LABELLING"/>
 *     &lt;enumeration value="VO_LABELLING_UNDERLINE"/>
 *     &lt;enumeration value="VO_RANDOMFILL"/>
 *     &lt;enumeration value="VO_POINT_RANDOMFILL"/>
 *     &lt;enumeration value="VO_POLYGON_RANDOMFILL"/>
 *     &lt;enumeration value="VO_RASTER_NORMALIZE_RED"/>
 *     &lt;enumeration value="VO_RASTER_NORMALIZE_GREEN"/>
 *     &lt;enumeration value="VO_RASTER_NORMALIZE_BLUE"/>
 *     &lt;enumeration value="VO_RASTER_NORMALIZE_GREY"/>
 *     &lt;enumeration value="VO_RASTER_NORMALIZE_OVERALL"/>
 *     &lt;enumeration value="RASTER"/>
 *     &lt;enumeration value="RASTER_CONTRAST"/>
 *     &lt;enumeration value="RASTER_OVERALL_CONTRAST_METHOD"/>
 *     &lt;enumeration value="RASTER_OVERALL_CONTRAST_METHOD_NONE"/>
 *     &lt;enumeration value="RASTER_OVERALL_CONTRAST_METHOD_EXPONENTIAL"/>
 *     &lt;enumeration value="RASTER_OVERALL_CONTRAST_METHOD_HISTOGRAM"/>
 *     &lt;enumeration value="RASTER_OVERALL_CONTRAST_METHOD_LOGARITHMIC"/>
 *     &lt;enumeration value="RASTER_OVERALL_CONTRAST_METHOD_NORMALIZE"/>
 *     &lt;enumeration value="RASTER_OVERALL_CONTRAST_METHOD_NORMALIZE_GROUP"/>
 *     &lt;enumeration value="RASTER_CHANNELSELECTION"/>
 *     &lt;enumeration value="RASTER_SHADEDRELIEF"/>
 *     &lt;enumeration value="RASTER_OVERLAP"/>
 *     &lt;enumeration value="RASTER_COLOURMAP"/>
 *     &lt;enumeration value="RASTER_CHANNELSELECTION"/>
 *     &lt;enumeration value="RASTER_RGB_CHANNEL_OPTION"/>
 *     &lt;enumeration value="RASTER_RGB_CHANNEL_RED"/>
 *     &lt;enumeration value="RASTER_RGB_CHANNEL_RED_CONTRAST"/>
 *     &lt;enumeration value="RASTER_RGB_CHANNEL_RED_CONTRAST_METHOD"/>
 *     &lt;enumeration value="RASTER_RGB_CHANNEL_RED_CONTRAST_METHOD_NONE"/>
 *     &lt;enumeration value="RASTER_RGB_CHANNEL_RED_CONTRAST_METHOD_EXPONENTIAL"/>
 *     &lt;enumeration value="RASTER_RGB_CHANNEL_RED_CONTRAST_METHOD_HISTOGRAM"/>
 *     &lt;enumeration value="RASTER_RGB_CHANNEL_RED_CONTRAST_METHOD_LOGARITHMIC"/>
 *     &lt;enumeration value="RASTER_RGB_CHANNEL_RED_CONTRAST_METHOD_NORMALIZE"/>
 *     &lt;enumeration value="RASTER_RGB_CHANNEL_RED_CONTRAST_METHOD_NORMALIZE_GROUP"/>
 *     &lt;enumeration value="RASTER_RGB_CHANNEL_GREEN"/>
 *     &lt;enumeration value="RASTER_RGB_CHANNEL_GREEN_CONTRAST"/>
 *     &lt;enumeration value="RASTER_RGB_CHANNEL_GREEN_CONTRAST_METHOD"/>
 *     &lt;enumeration value="RASTER_RGB_CHANNEL_GREEN_CONTRAST_METHOD_NONE"/>
 *     &lt;enumeration value="RASTER_RGB_CHANNEL_GREEN_CONTRAST_METHOD_EXPONENTIAL"/>
 *     &lt;enumeration value="RASTER_RGB_CHANNEL_GREEN_CONTRAST_METHOD_HISTOGRAM"/>
 *     &lt;enumeration value="RASTER_RGB_CHANNEL_GREEN_CONTRAST_METHOD_LOGARITHMIC"/>
 *     &lt;enumeration value="RASTER_RGB_CHANNEL_GREEN_CONTRAST_METHOD_NORMALIZE"/>
 *     &lt;enumeration value="RASTER_RGB_CHANNEL_GREEN_CONTRAST_METHOD_NORMALIZE_GROUP"/>
 *     &lt;enumeration value="RASTER_RGB_CHANNEL_BLUE"/>
 *     &lt;enumeration value="RASTER_RGB_CHANNEL_BLUE_CONTRAST"/>
 *     &lt;enumeration value="RASTER_RGB_CHANNEL_BLUE_CONTRAST_METHOD"/>
 *     &lt;enumeration value="RASTER_RGB_CHANNEL_BLUE_CONTRAST_METHOD_NONE"/>
 *     &lt;enumeration value="RASTER_RGB_CHANNEL_BLUE_CONTRAST_METHOD_EXPONENTIAL"/>
 *     &lt;enumeration value="RASTER_RGB_CHANNEL_BLUE_CONTRAST_METHOD_HISTOGRAM"/>
 *     &lt;enumeration value="RASTER_RGB_CHANNEL_BLUE_CONTRAST_METHOD_LOGARITHMIC"/>
 *     &lt;enumeration value="RASTER_RGB_CHANNEL_BLUE_CONTRAST_METHOD_NORMALIZE"/>
 *     &lt;enumeration value="RASTER_RGB_CHANNEL_BLUE_CONTRAST_METHOD_NORMALIZE_GROUP"/>
 *     &lt;enumeration value="RASTER_GREY_CHANNEL_OPTION"/>
 *     &lt;enumeration value="RASTER_GREY_CHANNEL"/>
 *     &lt;enumeration value="RASTER_RGB_CHANNEL_GREY_CONTRAST"/>
 *     &lt;enumeration value="RASTER_RGB_CHANNEL_GREY_CONTRAST_METHOD"/>
 *     &lt;enumeration value="RASTER_RGB_CHANNEL_GREY_CONTRAST_METHOD_NONE"/>
 *     &lt;enumeration value="RASTER_RGB_CHANNEL_GREY_CONTRAST_METHOD_EXPONENTIAL"/>
 *     &lt;enumeration value="RASTER_RGB_CHANNEL_GREY_CONTRAST_METHOD_HISTOGRAM"/>
 *     &lt;enumeration value="RASTER_RGB_CHANNEL_GREY_CONTRAST_METHOD_LOGARITHMIC"/>
 *     &lt;enumeration value="RASTER_RGB_CHANNEL_GREY_CONTRAST_METHOD_NORMALIZE"/>
 *     &lt;enumeration value="RASTER_RGB_CHANNEL_GREY_CONTRAST_METHOD_NORMALIZE_GROUP"/>
 *     &lt;enumeration value="REMOTE_OWS"/>
 *     &lt;enumeration value="REMOTE_OWS_OPTION"/>
 *     &lt;enumeration value="FEATURE_TYPE_CONSTRAINTS"/>
 *     &lt;enumeration value="INLINE_FEATURE"/>
 *     &lt;enumeration value="INLINE_FEATURE_OPTION"/>
 *     &lt;enumeration value="USER_LAYER_SOURCE"/>
 *     &lt;enumeration value="VO_TEXT_LABEL"/>
 *     &lt;enumeration value="VO_TEXT_LABEL_UNDERLINE"/>
 *     &lt;enumeration value="VO_TEXTSYMBOLIZER_2"/>
 *     &lt;enumeration value="VO_TEXTSYMBOLIZER_2_GRAPHIC"/>
 *     &lt;enumeration value="VO_TEXTSYMBOLIZER_2_FILL"/>
 *     &lt;enumeration value="VO_TEXTSYMBOLIZER_2_STROKE"/>
 *     &lt;enumeration value="VO_TEXTSYMBOLIZER_2_OTHERTEXT"/>
 *     &lt;enumeration value="VO_TEXT_SPACING"/>
 *     &lt;enumeration value="VO_FTS"/>
 *     &lt;enumeration value="VO_FTS_COMPOSITE"/>
 *     &lt;enumeration value="VO_FTS_COMPOSITE_BASE"/>
 *     &lt;enumeration value="VO_FTS_RULE_EVALUATION"/>
 *     &lt;enumeration value="VO_FTS_SORTBY_MULTIOPTION"/>
 *     &lt;enumeration value="VO_FTS_SORTBY_MULTIOPTION_SORTBY_OPTION"/>
 *     &lt;enumeration value="VO_FTS_SORTBY_MULTIOPTION_SORTBY"/>
 *     &lt;enumeration value="VO_FTS_SORTBY_MULTIOPTION_SORTBY_GROUP"/>
 *     &lt;enumeration value="VO_FTS_SORTBY_MULTIOPTION_SORTBY_GROUP_OPTION"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "GroupIdEnum")
@XmlEnum
public enum GroupIdEnum {

    UNKNOWN,
    STANDARD,
    GENERAL,
    ANCHORPOINT,
    DISPLACEMENT,
    ROTATION,
    SCALE,
    FILL,
    FILLSYMBOL,
    STROKE,
    STROKESYMBOL,
    STROKEANCHORPOINT,
    STROKEDISPLACEMENT,
    FILLCOLOUR,
    STROKECOLOUR,
    FONT,
    HALO,
    PLACEMENT,
    POINTPLACEMENT,
    LINEPLACEMENT,
    VO_LABELLING,
    VO_LABELLING_UNDERLINE,
    VO_RANDOMFILL,
    VO_POINT_RANDOMFILL,
    VO_POLYGON_RANDOMFILL,
    VO_RASTER_NORMALIZE_RED,
    VO_RASTER_NORMALIZE_GREEN,
    VO_RASTER_NORMALIZE_BLUE,
    VO_RASTER_NORMALIZE_GREY,
    VO_RASTER_NORMALIZE_OVERALL,
    RASTER,
    RASTER_CONTRAST,
    RASTER_OVERALL_CONTRAST_METHOD,
    RASTER_OVERALL_CONTRAST_METHOD_NONE,
    RASTER_OVERALL_CONTRAST_METHOD_EXPONENTIAL,
    RASTER_OVERALL_CONTRAST_METHOD_HISTOGRAM,
    RASTER_OVERALL_CONTRAST_METHOD_LOGARITHMIC,
    RASTER_OVERALL_CONTRAST_METHOD_NORMALIZE,
    RASTER_OVERALL_CONTRAST_METHOD_NORMALIZE_GROUP,
    RASTER_CHANNELSELECTION,
    RASTER_SHADEDRELIEF,
    RASTER_OVERLAP,
    RASTER_COLOURMAP,
    RASTER_RGB_CHANNEL_OPTION,
    RASTER_RGB_CHANNEL_RED,
    RASTER_RGB_CHANNEL_RED_CONTRAST,
    RASTER_RGB_CHANNEL_RED_CONTRAST_METHOD,
    RASTER_RGB_CHANNEL_RED_CONTRAST_METHOD_NONE,
    RASTER_RGB_CHANNEL_RED_CONTRAST_METHOD_EXPONENTIAL,
    RASTER_RGB_CHANNEL_RED_CONTRAST_METHOD_HISTOGRAM,
    RASTER_RGB_CHANNEL_RED_CONTRAST_METHOD_LOGARITHMIC,
    RASTER_RGB_CHANNEL_RED_CONTRAST_METHOD_NORMALIZE,
    RASTER_RGB_CHANNEL_RED_CONTRAST_METHOD_NORMALIZE_GROUP,
    RASTER_RGB_CHANNEL_GREEN,
    RASTER_RGB_CHANNEL_GREEN_CONTRAST,
    RASTER_RGB_CHANNEL_GREEN_CONTRAST_METHOD,
    RASTER_RGB_CHANNEL_GREEN_CONTRAST_METHOD_NONE,
    RASTER_RGB_CHANNEL_GREEN_CONTRAST_METHOD_EXPONENTIAL,
    RASTER_RGB_CHANNEL_GREEN_CONTRAST_METHOD_HISTOGRAM,
    RASTER_RGB_CHANNEL_GREEN_CONTRAST_METHOD_LOGARITHMIC,
    RASTER_RGB_CHANNEL_GREEN_CONTRAST_METHOD_NORMALIZE,
    RASTER_RGB_CHANNEL_GREEN_CONTRAST_METHOD_NORMALIZE_GROUP,
    RASTER_RGB_CHANNEL_BLUE,
    RASTER_RGB_CHANNEL_BLUE_CONTRAST,
    RASTER_RGB_CHANNEL_BLUE_CONTRAST_METHOD,
    RASTER_RGB_CHANNEL_BLUE_CONTRAST_METHOD_NONE,
    RASTER_RGB_CHANNEL_BLUE_CONTRAST_METHOD_EXPONENTIAL,
    RASTER_RGB_CHANNEL_BLUE_CONTRAST_METHOD_HISTOGRAM,
    RASTER_RGB_CHANNEL_BLUE_CONTRAST_METHOD_LOGARITHMIC,
    RASTER_RGB_CHANNEL_BLUE_CONTRAST_METHOD_NORMALIZE,
    RASTER_RGB_CHANNEL_BLUE_CONTRAST_METHOD_NORMALIZE_GROUP,
    RASTER_GREY_CHANNEL_OPTION,
    RASTER_GREY_CHANNEL,
    RASTER_RGB_CHANNEL_GREY_CONTRAST,
    RASTER_RGB_CHANNEL_GREY_CONTRAST_METHOD,
    RASTER_RGB_CHANNEL_GREY_CONTRAST_METHOD_NONE,
    RASTER_RGB_CHANNEL_GREY_CONTRAST_METHOD_EXPONENTIAL,
    RASTER_RGB_CHANNEL_GREY_CONTRAST_METHOD_HISTOGRAM,
    RASTER_RGB_CHANNEL_GREY_CONTRAST_METHOD_LOGARITHMIC,
    RASTER_RGB_CHANNEL_GREY_CONTRAST_METHOD_NORMALIZE,
    RASTER_RGB_CHANNEL_GREY_CONTRAST_METHOD_NORMALIZE_GROUP,
    REMOTE_OWS,
    REMOTE_OWS_OPTION,
    FEATURE_TYPE_CONSTRAINTS,
    INLINE_FEATURE,
    INLINE_FEATURE_OPTION,
    USER_LAYER_SOURCE,
    VO_TEXT_LABEL,
    VO_TEXT_LABEL_UNDERLINE,
    VO_TEXTSYMBOLIZER_2,
    VO_TEXTSYMBOLIZER_2_GRAPHIC,
    VO_TEXTSYMBOLIZER_2_FILL,
    VO_TEXTSYMBOLIZER_2_STROKE,
    VO_TEXTSYMBOLIZER_2_OTHERTEXT,
    VO_TEXT_SPACING,
    VO_FTS,
    VO_FTS_COMPOSITE,
    VO_FTS_COMPOSITE_BASE,
    VO_FTS_RULE_EVALUATION,
    VO_FTS_SORTBY_MULTIOPTION,
    VO_FTS_SORTBY_MULTIOPTION_SORTBY_OPTION,
    VO_FTS_SORTBY_MULTIOPTION_SORTBY,
    VO_FTS_SORTBY_MULTIOPTION_SORTBY_GROUP,
    VO_FTS_SORTBY_MULTIOPTION_SORTBY_GROUP_OPTION;

    public String value() {
        return name();
    }

    public static GroupIdEnum fromValue(String v) {
        return valueOf(v);
    }

}
