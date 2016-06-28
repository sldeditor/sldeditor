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
package com.sldeditor.common.defaultsymbol;

import java.util.ArrayList;
import java.util.List;

import javax.measure.quantity.Length;
import javax.measure.unit.Unit;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.styling.AnchorPoint;
import org.geotools.styling.Description;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Fill;
import org.geotools.styling.Font;
import org.geotools.styling.Graphic;
import org.geotools.styling.Halo;
import org.geotools.styling.LineSymbolizer;
import org.geotools.styling.NamedLayer;
import org.geotools.styling.PointPlacement;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.RasterSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactoryImpl;
import org.geotools.styling.StyledLayerDescriptor;
import org.geotools.styling.Symbolizer;
import org.geotools.styling.TextSymbolizer;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;
import org.opengis.style.Displacement;
import org.opengis.style.GraphicalSymbol;
import org.opengis.style.Stroke;

/**
 * Class of static methods to provide default sld values to the application.
 * 
 * @author Robert Ward (SCISYS)
 */
public class DefaultSymbols {

    /** The Constant DEFAULT_COLOUR_OPACITY. */
    private static final float DEFAULT_COLOUR_OPACITY = 1.0f;

    /** The Constant DEFAULT_COLOUR. */
    private static final String DEFAULT_COLOUR = "#000000";

    /** The Constant DEFAULT_MARKER_COLOUR. */
    private static final String DEFAULT_MARKER_COLOUR = "#FF0000";

    /** The Constant DEFAULT_LINE_COLOUR. */
    private static final String DEFAULT_LINE_COLOUR = "#FF0000";

    /** The Constant DEFAULT_FILL_COLOUR. */
    private static final String DEFAULT_FILL_COLOUR = "#0000FF";

    /** The style factory. */
    private static StyleFactoryImpl styleFactory = (StyleFactoryImpl) CommonFactoryFinder.getStyleFactory();

    /** The filter factory. */
    private static FilterFactory ff = CommonFactoryFinder.getFilterFactory();

    /**
     * Creates the default point symbolizer.
     *
     * @return the point symbolizer
     */
    public static PointSymbolizer createDefaultPointSymbolizer() {
        String geometryFieldName = null;
        Expression geometryField = ff.property(geometryFieldName);

        List<GraphicalSymbol> symbolList = new ArrayList<GraphicalSymbol>();

        Stroke stroke = null;
        AnchorPoint anchorPoint = styleFactory.anchorPoint(ff.literal(0.0), ff.literal(0.0));
        Displacement displacement = styleFactory.createDisplacement(ff.literal(0.0), ff.literal(0.0));
        Fill fill = styleFactory.createFill(ff.literal(DEFAULT_MARKER_COLOUR));
        GraphicalSymbol symbol = styleFactory.mark(ff.literal("circle"), fill, stroke);

        symbolList.add(symbol);
        Graphic graphic = styleFactory.graphic(symbolList,
                ff.literal(1.0),
                ff.literal(10.0), 
                ff.literal(0.0),
                anchorPoint,
                displacement);
        PointSymbolizer newPointSymbolizer = (PointSymbolizer) styleFactory.pointSymbolizer("New Marker",
                geometryField,
                null,
                null,
                graphic);

        return newPointSymbolizer;
    }

    /**
     * Creates the default text symbolizer.
     *
     * @return the text symbolizer
     */
    public static TextSymbolizer createDefaultTextSymbolizer() {
        Expression fontFamily = ff.literal("Serif");
        Expression fontSize = ff.literal(10.0);
        Expression fontStyle = ff.literal("normal");
        Expression fontWeight = ff.literal("normal");
        Expression rotation = ff.literal(0.0);
        Expression label = ff.literal("Test");

        String geometryFieldName = null;
        Expression geometryField = ff.property(geometryFieldName);

        String name = "TextSymbol";
        AnchorPoint anchor = null;
        Displacement displacement = null;

        PointPlacement pointPlacement = (PointPlacement) styleFactory.pointPlacement(anchor, displacement, rotation);

        Expression fillColour = ff.literal(DEFAULT_COLOUR);
        Expression fillColourOpacity = ff.literal(1.0);

        Fill fill = styleFactory.fill(null, fillColour, fillColourOpacity);

        Halo halo = null;

        List<Expression> fontFamilyList = new ArrayList<Expression>();
        fontFamilyList.add(fontFamily);
        Font font = (Font) styleFactory.font(fontFamilyList, fontStyle, fontWeight, fontSize);

        Description description = null;
        Unit<Length> unit = null;

        TextSymbolizer newTextSymbolizer = (TextSymbolizer) styleFactory.textSymbolizer(name,
                geometryField,
                description, 
                unit, 
                label,
                font,
                pointPlacement,
                halo,
                fill);

        return newTextSymbolizer;
    }

    /**
     * Default colour.
     *
     * @return the string
     */
    public static String defaultColour() {
        return DEFAULT_COLOUR;
    }

    /**
     * Default colour opacity.
     *
     * @return the float
     */
    public static float defaultColourOpacity() {
        return DEFAULT_COLOUR_OPACITY;
    }

    /**
     * Creates the default line symbolizer.
     *
     * @return the line symbolizer
     */
    public static LineSymbolizer createDefaultLineSymbolizer() {

        Stroke stroke = styleFactory.createStroke(
                ff.literal(DEFAULT_LINE_COLOUR),
                ff.literal(2));

        LineSymbolizer lineSymbolizer = styleFactory.createLineSymbolizer();
        lineSymbolizer.setStroke( stroke );

        return lineSymbolizer;
    }

    /**
     * Creates the default polygon symbolizer.
     *
     * @return the polygon symbolizer
     */
    public static PolygonSymbolizer createDefaultPolygonSymbolizer() {
        Stroke stroke = styleFactory.createStroke(
                ff.literal(DEFAULT_LINE_COLOUR),
                ff.literal(2));

        Fill fill = styleFactory.getDefaultFill();
        PolygonSymbolizer polygonSymbolizer = styleFactory.createPolygonSymbolizer();
        polygonSymbolizer.setStroke(stroke);
        polygonSymbolizer.setFill(fill);
        return polygonSymbolizer;
    }

    /**
     * Creates a default graphic fill.
     *
     * @return the fill
     */
    public static Fill createDefaultGraphicFill() {

        Graphic graphicFill = styleFactory.createDefaultGraphic();

        Expression colour = ff.literal(DEFAULT_FILL_COLOUR);
        Expression backgroundColour = null;
        Expression opacity = ff.literal(1.0);

        Fill fill = styleFactory.createFill(colour, backgroundColour, opacity, graphicFill);

        return fill;
    }

    /**
     * Creates the new rule.
     *
     * @return the rule
     */
    public static Rule createNewRule() {
        Rule rule = styleFactory.createRule();
        rule.setName("New Rule");
        return rule;
    }

    /**
     * Creates the new style.
     *
     * @return the style
     */
    public static Style createNewStyle() {
        Style style = styleFactory.createStyle();
        style.setName("New Style");
        return style;
    }

    /**
     * Creates the new feature type style.
     *
     * @return the feature type style
     */
    public static FeatureTypeStyle createNewFeatureTypeStyle() {
        FeatureTypeStyle featureTypeStyle = styleFactory.createFeatureTypeStyle();

        return featureTypeStyle;
    }

    /**
     * Creates the new named layer.
     *
     * @return the style
     */
    public static NamedLayer createNewNamedLayer() {
        NamedLayer namedLayer = styleFactory.createNamedLayer();

        return namedLayer;
    }

    /**
     * Creates the new sld.
     *
     * @return the styled layer descriptor
     */
    public static StyledLayerDescriptor createNewSLD() {
        StyledLayerDescriptor sld = styleFactory.createStyledLayerDescriptor();

        return sld;
    }

    /**
     * Creates the default raster symbolizer.
     *
     * @return the raster symbolizer
     */
    public static RasterSymbolizer createDefaultRasterSymbolizer() {
        RasterSymbolizer rasterSymbolizer = styleFactory.createRasterSymbolizer();

        return rasterSymbolizer;
    }

    /**
     * Creates a new polygon symbol.
     *
     * @return the styled layer descriptor
     */
    public static StyledLayerDescriptor createNewPolygon() {
        StyledLayerDescriptor sld = styleFactory.createStyledLayerDescriptor();

        NamedLayer namedLayer = styleFactory.createNamedLayer();

        sld.addStyledLayer(namedLayer);

        Style style = styleFactory.createStyle();
        namedLayer.addStyle(style);

        List<FeatureTypeStyle> ftsList = style.featureTypeStyles();

        FeatureTypeStyle fts = styleFactory.createFeatureTypeStyle();

        ftsList.add(fts);

        Rule rule = styleFactory.createRule();

        fts.rules().add(rule);

        PolygonSymbolizer polygon = createDefaultPolygonSymbolizer();

        rule.symbolizers().add(polygon);

        return sld;
    }

    /**
     * Creates a new point symbol.
     *
     * @return the styled layer descriptor
     */
    public static StyledLayerDescriptor createNewPoint() {
        StyledLayerDescriptor sld = styleFactory.createStyledLayerDescriptor();

        NamedLayer namedLayer = styleFactory.createNamedLayer();

        sld.addStyledLayer(namedLayer);

        Style style = styleFactory.createStyle();
        namedLayer.addStyle(style);

        List<FeatureTypeStyle> ftsList = style.featureTypeStyles();

        FeatureTypeStyle fts = styleFactory.createFeatureTypeStyle();

        ftsList.add(fts);

        Rule rule = styleFactory.createRule();

        fts.rules().add(rule);

        PointSymbolizer point = createDefaultPointSymbolizer();

        rule.symbolizers().add(point);

        return sld;
    }

    /**
     * Creates a new line symbol.
     *
     * @return the styled layer descriptor
     */
    public static StyledLayerDescriptor createNewLine() {
        StyledLayerDescriptor sld = styleFactory.createStyledLayerDescriptor();

        NamedLayer namedLayer = styleFactory.createNamedLayer();

        sld.addStyledLayer(namedLayer);

        Style style = styleFactory.createStyle();
        namedLayer.addStyle(style);

        List<FeatureTypeStyle> ftsList = style.featureTypeStyles();

        FeatureTypeStyle fts = styleFactory.createFeatureTypeStyle();

        ftsList.add(fts);

        Rule rule = styleFactory.createRule();

        fts.rules().add(rule);

        LineSymbolizer line = createDefaultLineSymbolizer();

        rule.symbolizers().add(line);

        return sld;
    }
}
