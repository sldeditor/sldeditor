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

import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.datasource.DataSourceInterface;
import com.sldeditor.datasource.impl.DataSourceFactory;
import com.sldeditor.filter.v2.function.FunctionManager;
import com.sldeditor.ui.tree.SLDTreeTools;
import java.util.ArrayList;
import java.util.List;
import javax.measure.Unit;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.function.FilterFunction_endAngle;
import org.geotools.filter.function.FilterFunction_endPoint;
import org.geotools.filter.function.FilterFunction_startAngle;
import org.geotools.filter.function.FilterFunction_startPoint;
import org.geotools.styling.AnchorPoint;
import org.geotools.styling.Description;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Fill;
import org.geotools.styling.Font;
import org.geotools.styling.Graphic;
import org.geotools.styling.Halo;
import org.geotools.styling.LineSymbolizer;
import org.geotools.styling.MarkImpl;
import org.geotools.styling.NamedLayer;
import org.geotools.styling.PointPlacement;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.RasterSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.Stroke;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactoryImpl;
import org.geotools.styling.StyledLayerDescriptor;
import org.geotools.styling.TextSymbolizer;
import org.geotools.styling.UserLayer;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.capability.FunctionName;
import org.opengis.filter.expression.Expression;
import org.opengis.style.Displacement;
import org.opengis.style.GraphicalSymbol;

/**
 * Class of static methods to provide default sld values to the application.
 *
 * @author Robert Ward (SCISYS)
 */
public class DefaultSymbols {

    /** The Constant DEGREES_180. */
    private static final double DEGREES_180 = 180.0;

    /** The Constant DEFAULT_ARROW_SIZE. */
    private static final double DEFAULT_ARROW_SIZE = 30.0;

    /** The Constant DEFAULT_ARROW_SYMBOL. */
    private static final String DEFAULT_ARROW_SYMBOL = "shape://oarrow";

    /** The Constant DEFAULT_MARKER_SYMBOL_SIZE. */
    private static final double DEFAULT_MARKER_SYMBOL_SIZE = 10.0;

    /** The Constant DEFAULT_MARKER_SYMBOL. */
    private static final String DEFAULT_MARKER_SYMBOL = "circle";

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
    private static StyleFactoryImpl styleFactory =
            (StyleFactoryImpl) CommonFactoryFinder.getStyleFactory();

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
        AnchorPoint anchorPoint = null;
        Displacement displacement = null;
        Fill fill = styleFactory.createFill(ff.literal(DEFAULT_MARKER_COLOUR));
        GraphicalSymbol symbol = styleFactory.mark(ff.literal(DEFAULT_MARKER_SYMBOL), fill, stroke);

        symbolList.add(symbol);
        Graphic graphic =
                styleFactory.graphic(
                        symbolList,
                        ff.literal(DEFAULT_COLOUR_OPACITY),
                        ff.literal(DEFAULT_MARKER_SYMBOL_SIZE),
                        ff.literal(0.0),
                        anchorPoint,
                        displacement);
        PointSymbolizer newPointSymbolizer =
                (PointSymbolizer)
                        styleFactory.pointSymbolizer(
                                Localisation.getString(SLDTreeTools.class, "TreeItem.newMarker"),
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

        String name = Localisation.getString(SLDTreeTools.class, "TreeItem.newText");
        AnchorPoint anchor = null;
        Displacement displacement = null;

        PointPlacement pointPlacement =
                (PointPlacement) styleFactory.pointPlacement(anchor, displacement, rotation);

        Expression fillColour = ff.literal(DEFAULT_COLOUR);
        Expression fillColourOpacity = ff.literal(1.0);

        Fill fill = styleFactory.fill(null, fillColour, fillColourOpacity);

        Halo halo = null;

        List<Expression> fontFamilyList = new ArrayList<Expression>();
        fontFamilyList.add(fontFamily);
        Font font = (Font) styleFactory.font(fontFamilyList, fontStyle, fontWeight, fontSize);

        Description description = null;
        Unit<?> unit = null;

        TextSymbolizer newTextSymbolizer =
                (TextSymbolizer)
                        styleFactory.textSymbolizer(
                                name,
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

        Stroke stroke = styleFactory.createStroke(ff.literal(DEFAULT_LINE_COLOUR), ff.literal(2));

        LineSymbolizer lineSymbolizer = styleFactory.createLineSymbolizer();
        lineSymbolizer.setStroke(stroke);

        return lineSymbolizer;
    }

    /**
     * Creates the default polygon symbolizer.
     *
     * @return the polygon symbolizer
     */
    public static PolygonSymbolizer createDefaultPolygonSymbolizer() {
        Stroke stroke = styleFactory.createStroke(ff.literal(DEFAULT_LINE_COLOUR), ff.literal(2));

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
        rule.setName(Localisation.getString(SLDTreeTools.class, "TreeItem.newRule"));
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
     * @return the named layer
     */
    public static NamedLayer createNewNamedLayer() {
        NamedLayer namedLayer = styleFactory.createNamedLayer();

        return namedLayer;
    }

    /**
     * Creates the new user layer.
     *
     * @return the user layer
     */
    public static UserLayer createNewUserLayer() {
        UserLayer userLayer = styleFactory.createUserLayer();

        return userLayer;
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

    /**
     * Creates the default stroke.
     *
     * @return the stroke
     */
    public static Stroke createDefaultStroke() {
        Stroke stroke = styleFactory.getDefaultStroke();
        return stroke;
    }

    /**
     * Creates the new raster symbol.
     *
     * @return the styled layer descriptor
     */
    public static StyledLayerDescriptor createNewRaster() {
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

        RasterSymbolizer raster = createDefaultRasterSymbolizer();

        rule.symbolizers().add(raster);

        return sld;
    }

    /**
     * Creates the arrow.
     *
     * @return the point symbolizer containing the arrow
     */
    public static PointSymbolizer createArrow(boolean isSourceArrow) {
        FunctionName angleFunction = null;
        FunctionName locationFunction = null;
        if (isSourceArrow) {
            angleFunction = FilterFunction_startAngle.NAME;
            locationFunction = FilterFunction_startPoint.NAME;
        } else {
            angleFunction = FilterFunction_endAngle.NAME;
            locationFunction = FilterFunction_endPoint.NAME;
        }
        return createArrow(angleFunction, locationFunction, DEFAULT_ARROW_SYMBOL, isSourceArrow);
    }

    /**
     * Creates the arrow.
     *
     * @param angleFunction the angle function
     * @param locationFunction the location function
     * @param markerSymbol the marker symbol
     * @param isSourceArrow the is source arrow
     * @return the point symbolizer
     */
    private static PointSymbolizer createArrow(
            FunctionName angleFunction,
            FunctionName locationFunction,
            String markerSymbol,
            boolean isSourceArrow) {
        String name =
                isSourceArrow
                        ? Localisation.getString(SLDTreeTools.class, "TreeItem.sourceArrow")
                        : Localisation.getString(SLDTreeTools.class, "TreeItem.destArrow");

        PointSymbolizer pointSymbolizer = createDefaultPointSymbolizer();

        pointSymbolizer.setName(name);
        Graphic graphic = pointSymbolizer.getGraphic();
        graphic.setSize(ff.literal(DEFAULT_ARROW_SIZE));
        List<GraphicalSymbol> graphicalSymbolList = graphic.graphicalSymbols();
        MarkImpl mark = (MarkImpl) graphicalSymbolList.get(0);

        Expression wellKnownName = ff.literal(markerSymbol);
        mark.setWellKnownName(wellKnownName);

        mark.getFill().setColor(ff.literal(DEFAULT_COLOUR));

        // Arrow rotation
        List<Expression> rotationArgumentList = new ArrayList<Expression>();

        String geometryFieldName = "geom";
        DataSourceInterface dsInfo = DataSourceFactory.getDataSource();
        if (dsInfo != null) {
            geometryFieldName = dsInfo.getGeometryFieldName();
        }
        rotationArgumentList.add(ff.property(geometryFieldName));

        Expression rotation =
                FunctionManager.getInstance().createExpression(angleFunction, rotationArgumentList);
        if (isSourceArrow) {
            graphic.setRotation(ff.add(ff.literal(DEGREES_180), rotation));
        } else {
            graphic.setRotation(rotation);
        }

        AnchorPoint anchorPoint = styleFactory.anchorPoint(ff.literal(0.5), ff.literal(0.5));
        graphic.setAnchorPoint(anchorPoint);

        // Set location of the arrow head
        List<Expression> endPointArgumentList = new ArrayList<Expression>();
        endPointArgumentList.add(ff.property(geometryFieldName));

        Expression geometry =
                FunctionManager.getInstance()
                        .createExpression(locationFunction, endPointArgumentList);
        pointSymbolizer.setGeometry(geometry);

        return pointSymbolizer;
    }
}
