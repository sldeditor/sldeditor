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
package com.sldeditor.tool.legendpanel;

/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.awt.image.RenderedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.geotools.data.DataUtilities;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.filter.ConstantExpression;
import org.geotools.filter.LiteralExpressionImpl;
import org.geotools.geometry.jts.LiteShape2;
import org.geotools.renderer.lite.StyledShapePainter;
import org.geotools.renderer.style.SLDStyleFactory;
import org.geotools.renderer.style.Style2D;
import org.geotools.styling.Description;
import org.geotools.styling.ExternalGraphic;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Graphic;
import org.geotools.styling.LineSymbolizer;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.PointSymbolizerImpl;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.RasterSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactoryImpl;
import org.geotools.styling.Symbolizer;
import org.geotools.styling.TextSymbolizer;
import org.geotools.util.NumberRange;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;
import org.opengis.style.GraphicalSymbol;
import org.opengis.util.InternationalString;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Polygon;

/**
 * Template {link org.vfny.geoserver.responses.wms.GetLegendGraphicProducer} based on
 * GeoTools' {link GeoTools' {link http
 * ://svn.geotools.org/geotools/trunk/gt/module/main/src/org/geotools
 * /renderer/lite/StyledShapePainter.java StyledShapePainter} that produces a BufferedImage with the
 * appropriate legend graphic for a given GetLegendGraphic WMS request.
 * 
 * <p>
 * It should be enough for a subclass to implement {link
 * org.vfny.geoserver.responses.wms.GetLegendGraphicProducer#writeTo(OutputStream)} and
 * <code>getContentType()</code> in order to encode the BufferedImage produced by this class to the
 * appropriate output format.
 * </p>
 * 
 * <p>
 * This class takes literally the fact that the arguments <code>WIDTH</code> and <code>HEIGHT</code>
 * are just <i>hints</i> about the desired dimensions of the produced graphic, and the need to
 * produce a legend graphic representative enough of the SLD style for which it is being generated.
 * Thus, if no <code>RULE</code> parameter was passed and the style has more than one applicable
 * Rule for the actual scale factor, there will be generated a legend graphic of the specified
 * width, but with as many stacked graphics as applicable rules were found, providing by this way a
 * representative enough legend.
 * </p>
 * 
 * @author Gabriel Roldan
 * @author Simone Giannecchini, GeoSolutions SAS
 * @version $Id$
 */
public class BufferedImageLegendGraphicBuilder {

    /**  Tolerance used to compare doubles for equality. */
    public static final double TOLERANCE = 1e-6;

    /**
     * Singleton shape painter to serve all legend requests. We can use a single shape painter
     * instance as long as it remains thread safe.
     */
    private static final StyledShapePainter shapePainter = new StyledShapePainter();

    /** used to create sample point shapes with LiteShape (not lines nor polygons). */
    private static final GeometryFactory geomFac = new GeometryFactory();

    /** Just a holder to avoid creating many polygon shapes from inside <code>getSampleShape()</code>. */
    private LiteShape2 sampleRect;

    /** Just a holder to avoid creating many line shapes from inside <code>getSampleShape()</code>. */
    private LiteShape2 sampleLine;

    /** Just a holder to avoid creating many point shapes from inside <code>getSampleShape()</code>. */
    private LiteShape2 samplePoint;

    /** The style factory. */
    private static StyleFactoryImpl styleFactoryImpl = (StyleFactoryImpl) CommonFactoryFinder.getStyleFactory();

    /** The last legend width. */
    private int lastLegendWidth = Integer.MIN_VALUE;

    /** The last legend height. */
    private int lastLegendHeight = Integer.MIN_VALUE;

    /** The filter factory. */
    private FilterFactory ff = CommonFactoryFinder.getFilterFactory();

    /**
     * Default constructor. Subclasses may provide its own with a String parameter to establish its
     * desired output format, if they support more than one (e.g. a JAI based one)
     */
    public BufferedImageLegendGraphicBuilder() {
        super();
    }

    /**
     * Takes a GetLegendGraphicRequest and produces a BufferedImage that then can be used by a
     * subclass to encode it to the appropriate output format.
     *
     * @param request            the "parsed" request, where "parsed" means that it's values are already validated
     *            so this method must not take care of verifying the requested layer exists and the
     *            like.
     * @param gt2Style the gt2 style
     * @param separateSymbolizers the separate symbolizers
     * @return the buffered image
     * @throws ServiceException             if there are problems creating a "sample" feature instance for the FeatureType
     *             <code>request</code> returns as the required layer (which should not occur).
     */
    public Map<String, BufferedImage> buildLegendGraphic(LegendRequest request, Style gt2Style, boolean separateSymbolizers)
            throws ServiceException {

        /*
            final FeatureType layer = null;
            boolean strict = request.isStrict();
            final boolean buildRasterLegend = (!strict && layer == null && LegendUtils
                    .checkRasterSymbolizer(gt2Style)) || LegendUtils.checkGridLayer(layer);
            if (buildRasterLegend) {
                final RasterLayerLegendHelper rasterLegendHelper = new RasterLayerLegendHelper(request);
                final BufferedImage image = rasterLegendHelper.getLegend();
                return image;
            }
         */

        final SimpleFeature sampleFeature = createSampleFeature();

        final FeatureTypeStyle[] ftStyles = gt2Style.featureTypeStyles().toArray(
                new FeatureTypeStyle[0]);
        final double scaleDenominator = request.getScale();

        List<Rule> additionalRuleList = new ArrayList<Rule>();

        // Filename heading
        Rule filenameHeadingRule = null;
        String filenameHeading = LegendUtils.getFilename(request);

        if(filenameHeading != null)
        {
            filenameHeadingRule = styleFactoryImpl.createRule();
            filenameHeadingRule.setTitle(filenameHeading);
            additionalRuleList.add(filenameHeadingRule);
        }

        // Style name heading
        Rule styleNameHeadingRule = null;
        String styleNameHeading = LegendUtils.getHeading(request);

        if(styleNameHeading != null)
        {
            styleNameHeadingRule = styleFactoryImpl.createRule();
            styleNameHeadingRule.setTitle(styleNameHeading);
            additionalRuleList.add(styleNameHeadingRule);
        }

        final Rule[] applicableRules = LegendUtils.getApplicableRules(additionalRuleList, ftStyles, scaleDenominator);

        final NumberRange<Double> scaleRange = NumberRange.create(scaleDenominator, scaleDenominator);
        final int ruleCount = applicableRules.length;

        /**
         * A legend graphic is produced for each applicable rule. They're being held here until the
         * process is done and then painted on a "stack" like legend.
         */
        final List<RenderedImage> legendsStack = new ArrayList<RenderedImage>(ruleCount);
        final int w = request.getWidth();
        final int h = request.getHeight();

        final SLDStyleFactory sldStyleFactory = new SLDStyleFactory();
        final Color bgColor = LegendUtils.getBackgroundColor(request);
        final boolean transparent = request.isTransparent();
        final boolean useAntiAlias = LegendUtils.useAntiAlias(request);

        double imageSizeFactor = LegendUtils.getImageSizeFactor(request);

        Map<Graphic, Expression> oldGraphicList = new LinkedHashMap<Graphic, Expression>();

        for (int i = 0; i < ruleCount; i++) {
            final Symbolizer[] symbolizers = applicableRules[i].getSymbolizers();

            final RenderedImage image = ImageUtils.createImage(w, h, (IndexColorModel) null, transparent);
            final Map<RenderingHints.Key, Object> hintsMap = new HashMap<RenderingHints.Key, Object>();
            final Graphics2D graphics = ImageUtils.prepareTransparency(transparent, bgColor, image,
                    hintsMap);

            if(useAntiAlias)
            {
                graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

                // Set the anti-alias rendering hints for the graphics context,
                // this allows svg images to be drawn nicely
                RenderingHints renderingHints = sldStyleFactory.getRenderingHints();

                renderingHints.remove(RenderingHints.KEY_ANTIALIASING);
                renderingHints.add(new RenderingHints(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON));

                sldStyleFactory.setRenderingHints(renderingHints);
            }

            if(symbolizers != null)
            {
                for (int sIdx = 0; sIdx < symbolizers.length; sIdx++) {
                    Symbolizer symbolizer = symbolizers[sIdx];

                    if (symbolizer instanceof RasterSymbolizer) {
                        //                        throw new IllegalStateException(
                        //                                "It is not legal to have a RasterSymbolizer here");
                    } else {
                        if(symbolizer instanceof PointSymbolizerImpl)
                        {
                            PointSymbolizerImpl point = (PointSymbolizerImpl)symbolizer;

                            Graphic graphic = point.getGraphic();
                            if(graphic != null)
                            {
                                List<GraphicalSymbol> graphicalSymbolList = graphic.graphicalSymbols();

                                for(GraphicalSymbol graphicalSymbol : graphicalSymbolList)
                                {
                                    if((graphicalSymbol != null) && (graphicalSymbol instanceof ExternalGraphic))
                                    {
                                        Expression oldSizeExpression = graphic.getSize();

                                        Object oldSizeObj = null;
                                        if(oldSizeExpression instanceof ConstantExpression)
                                        {
                                            oldSizeObj = ((ConstantExpression)oldSizeExpression).getValue();
                                        }
                                        else if(oldSizeExpression instanceof LiteralExpressionImpl)
                                        {
                                            oldSizeObj = ((LiteralExpressionImpl)oldSizeExpression).getValue();
                                        }

                                        if(oldSizeObj != null)
                                        {
                                            oldGraphicList.put(graphic, oldSizeExpression);

                                            double oldSize = 0.0;

                                            if(oldSizeObj instanceof Integer)
                                            {
                                                oldSize = ((Integer)oldSizeObj).doubleValue();
                                            }
                                            else if(oldSizeObj instanceof Double)
                                            {
                                                oldSize = ((Double)oldSizeObj).doubleValue();
                                            }
                                            else if(oldSizeObj instanceof Float)
                                            {
                                                oldSize = ((Float)oldSizeObj).doubleValue();
                                            }
                                            else if(oldSizeObj instanceof String)
                                            {
                                                oldSize = Double.valueOf((String)oldSizeObj);
                                            }

                                            if(oldSize >= 0.0)
                                            {
                                                double newSize = oldSize * imageSizeFactor;

                                                Graphic newGraphic = styleFactoryImpl.createGraphic(graphic.getExternalGraphics(), graphic.getMarks(), graphic.getSymbols(), graphic.getOpacity(), ff.literal(newSize), graphic.getRotation());
                                                symbolizer = styleFactoryImpl.createPointSymbolizer(newGraphic, point.getGeometryPropertyName());
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        Style2D style2d = sldStyleFactory.createStyle(sampleFeature, symbolizer, scaleRange);
                        LiteShape2 shape = getSampleShape(symbolizer, w, h);

                        if (style2d != null) {
                            shapePainter.paint(graphics, shape, style2d, scaleDenominator);
                        }
                    }
                }
            }
            legendsStack.add(image);
            graphics.dispose();
        }


        Map<String, BufferedImage> imageMap = null;

        if(separateSymbolizers)
        {
            imageMap = separateLegends(legendsStack, applicableRules, request);
        }
        else
        {
            imageMap = new LinkedHashMap<String, BufferedImage>();

            // JD: changed legend behavior, see GEOS-812
            // this.legendGraphic = scaleImage(mergeLegends(legendsStack), request);
            if(!legendsStack.isEmpty())
            {
                BufferedImage mergedImage = mergeLegends(legendsStack, applicableRules, request);
                imageMap.put(null, mergedImage);
            }
        }

        return imageMap;
    }

    /**
     * Receives a list of <code>BufferedImages</code> and produces a new one which holds all the
     * images in <code>imageStack</code> one above the other.
     *
     * @param imageStack            the list of BufferedImages, one for each applicable Rule
     * @param rules            The applicable rules, one for each image in the stack
     * @param req the req
     * @return the stack image with all the images on the argument list.
     * @throws IllegalArgumentException             if the list is empty
     */
    private static BufferedImage mergeLegends(List<RenderedImage> imageStack, Rule[] rules,
            LegendRequest req) {

        Font labelFont = LegendUtils.getLabelFont(req);
        boolean useAA = false;
        if (req.getLegendOptions().get("fontAntiAliasing") instanceof String) {
            String aaVal = (String) req.getLegendOptions().get("fontAntiAliasing");
            if (isTrueValue(aaVal)) {
                useAA = true;
            }
        }

        boolean forceLabelsOn = false;
        boolean forceLabelsOff = false;
        if (req.getLegendOptions().get("forceLabels") instanceof String) {
            String forceLabelsOpt = (String) req.getLegendOptions().get("forceLabels");
            if (isTrueValue(forceLabelsOpt)) {
                forceLabelsOn = true;
            } else if (isFalseValue(forceLabelsOpt)) {
                forceLabelsOff = true;
            }
        }

        if (imageStack.size() == 0) {
            throw new IllegalArgumentException("No legend graphics passed");
        }

        final BufferedImage finalLegend;

        if (imageStack.size() == 1 && !forceLabelsOn) {
            finalLegend = (BufferedImage) imageStack.get(0);
        } else {
            final int imgCount = imageStack.size();
            final String[] labels = new String[imgCount];

            BufferedImage img = ((BufferedImage) imageStack.get(0));

            int totalHeight = 0;
            int totalWidth = 0;
            int[] rowHeights = new int[imgCount];
            BufferedImage labelsGraphics[] = new BufferedImage[imgCount];
            for (int i = 0; i < imgCount; i++) {
                img = (BufferedImage) imageStack.get(i);

                if (forceLabelsOff) {
                    totalWidth = Math.max(img.getWidth(), totalWidth);
                    rowHeights[i] = img.getHeight();
                    totalHeight += img.getHeight();
                } else {

                    Rule rule = rules[i];

                    // What's the label on this rule? We prefer to use
                    // the 'title' if it's available, but fall-back to 'name'
                    final Description description = rule.getDescription();
                    if (description != null && description.getTitle() != null) {
                        final InternationalString title = description.getTitle();
                        labels[i] = title.toString();
                    } else if (rule.getName() == null) {
                        labels[i] = rule.getName();
                    } else {
                        labels[i] = "";
                    }

                    Graphics2D g = img.createGraphics();
                    g.setFont(labelFont);

                    if (useAA) {
                        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                    } else {
                        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                                RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
                    }

                    if (labels[i] != null && labels[i].length() > 0) {
                        final BufferedImage renderedLabel = LegendUtils.renderLabel(labels[i], g,
                                req);
                        labelsGraphics[i] = renderedLabel;
                        final Rectangle2D bounds = new Rectangle2D.Double(0, 0,
                                renderedLabel.getWidth(), renderedLabel.getHeight());

                        totalWidth = (int) Math.ceil(Math.max(img.getWidth() + bounds.getWidth(),
                                totalWidth));
                        rowHeights[i] = (int) Math.ceil(Math.max(img.getHeight(),
                                bounds.getHeight()));
                    } else {
                        totalWidth = Math.max(img.getWidth(), totalWidth);
                        rowHeights[i] = img.getHeight();
                        labelsGraphics[i] = null;
                    }

                    totalHeight += rowHeights[i];

                }
            }

            // buffer the width a bit
            totalWidth += 2;

            final boolean transparent = req.isTransparent();
            final Color backgroundColor = LegendUtils.getBackgroundColor(req);
            final Map<RenderingHints.Key, Object> hintsMap = new HashMap<RenderingHints.Key, Object>();
            // create the final image
            finalLegend = ImageUtils.createImage(totalWidth, totalHeight, (IndexColorModel) null,
                    transparent);
            Graphics2D finalGraphics = ImageUtils.prepareTransparency(transparent, backgroundColor,
                    finalLegend, hintsMap);

            int topOfRow = 0;

            for (int i = 0; i < imgCount; i++) {
                img = (BufferedImage) imageStack.get(i);

                // draw the image
                int y = topOfRow;

                if (img.getHeight() < rowHeights[i]) {
                    // move the image to the center of the row
                    y += (int) ((rowHeights[i] - img.getHeight()) / 2d);
                }

                finalGraphics.drawImage(img, 0, y, null);
                if (forceLabelsOff) {
                    topOfRow += rowHeights[i];
                    continue;
                }

                finalGraphics.setFont(labelFont);

                if (useAA) {
                    finalGraphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                            RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                } else {
                    finalGraphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                            RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
                }

                // draw the label
                if (labels[i] != null && labels[i].length() > 0) {
                    // first create the actual overall label image.
                    final BufferedImage renderedLabel = labelsGraphics[i];

                    y = topOfRow;

                    if (renderedLabel.getHeight() < rowHeights[i]) {
                        y += (int) ((rowHeights[i] - renderedLabel.getHeight()) / 2d);
                    }

                    finalGraphics.drawImage(renderedLabel, img.getWidth(), y, null);
                    // cleanup
                    renderedLabel.flush();
                    labelsGraphics[i] = null;
                }

                topOfRow += rowHeights[i];
            }

            finalGraphics.dispose();
        }
        return finalLegend;
    }

    /**
     * Returns a <code>java.awt.Shape</code> appropiate to render a legend graphic given the
     * symbolizer type and the legend dimensions.
     * 
     * @param symbolizer
     *            the Symbolizer for whose type a sample shape will be created
     * @param legendWidth
     *            the requested width, in output units, of the legend graphic
     * @param legendHeight
     *            the requested height, in output units, of the legend graphic
     * 
     * @return an appropiate Line2D, Rectangle2D or LiteShape(Point) for the symbolizer, wether it
     *         is a LineSymbolizer, a PolygonSymbolizer, or a Point ot Text Symbolizer
     * 
     * @throws IllegalArgumentException
     *             if an unknown symbolizer impl was passed in.
     */
    private LiteShape2 getSampleShape(Symbolizer symbolizer, int legendWidth, int legendHeight) {
        LiteShape2 sampleShape;

        if((lastLegendWidth != legendWidth) || (lastLegendHeight != legendHeight))
        {
            lastLegendHeight = legendHeight;
            lastLegendWidth = legendWidth;
            this.sampleLine = null;
            this.sampleRect = null;
            this.samplePoint = null;
        }

        final float hpad = (legendWidth * LegendUtils.hpaddingFactor);
        final float vpad = (legendHeight * LegendUtils.vpaddingFactor);

        if (symbolizer instanceof LineSymbolizer) {
            if (this.sampleLine == null) {
                Coordinate[] coords = { new Coordinate(hpad, legendHeight - vpad),
                        new Coordinate(legendWidth - hpad, vpad) };
                LineString geom = geomFac.createLineString(coords);

                try {
                    this.sampleLine = new LiteShape2(geom, null, null, false);
                } catch (Exception e) {
                    this.sampleLine = null;
                }
            }

            sampleShape = this.sampleLine;
        } else if ((symbolizer instanceof PolygonSymbolizer)
                || (symbolizer instanceof RasterSymbolizer)) {
            if (this.sampleRect == null) {
                final float w = legendWidth - (2 * hpad);
                final float h = legendHeight - (2 * vpad);

                Coordinate[] coords = { new Coordinate(hpad, vpad), new Coordinate(hpad, vpad + h),
                        new Coordinate(hpad + w, vpad + h), new Coordinate(hpad + w, vpad),
                        new Coordinate(hpad, vpad) };
                LinearRing shell = geomFac.createLinearRing(coords);
                Polygon geom = geomFac.createPolygon(shell, null);

                try {
                    this.sampleRect = new LiteShape2(geom, null, null, false);
                } catch (Exception e) {
                    this.sampleRect = null;
                }
            }

            sampleShape = this.sampleRect;
        } else if (symbolizer instanceof PointSymbolizer || symbolizer instanceof TextSymbolizer) {
            if (this.samplePoint == null) {
                Coordinate coord = new Coordinate(legendWidth / 2, legendHeight / 2);

                try {
                    this.samplePoint = new LiteShape2(geomFac.createPoint(coord), null, null, false);
                } catch (Exception e) {
                    this.samplePoint = null;
                }
            }

            sampleShape = this.samplePoint;
        } else {
            throw new IllegalArgumentException("Unknown symbolizer: " + symbolizer);
        }

        return sampleShape;
    }

    /**
     * Creates the sample feature.
     *
     * @return the simple feature
     */
    private SimpleFeature createSampleFeature() {
        SimpleFeatureType type;
        try {
            type = DataUtilities.createType("Sample", "the_geom:Geometry");
        } catch (SchemaException e) {
            throw new RuntimeException(e);
        }
        return SimpleFeatureBuilder.template((SimpleFeatureType) type, null);
    }

    /**
     * Receives a list of <code>BufferedImages</code> and produces a new one which holds all the
     * images in <code>imageStack</code> one above the other.
     *
     * @param imageStack            the list of BufferedImages, one for each applicable Rule
     * @param rules            The applicable rules, one for each image in the stack
     * @param req the req
     * @return the stack image with all the images on the argument list.
     * @throws IllegalArgumentException             if the list is empty
     */
    private static Map<String, BufferedImage> separateLegends(List<RenderedImage> imageStack, Rule[] rules,
            LegendRequest req) {

        Font labelFont = LegendUtils.getLabelFont(req);
        boolean useAA = false;
        if (req.getLegendOptions().get("fontAntiAliasing") instanceof String) {
            String aaVal = (String) req.getLegendOptions().get("fontAntiAliasing");
            if (isTrueValue(aaVal)) {
                useAA = true;
            }
        }

        boolean forceLabelsOn = false;
        boolean forceLabelsOff = false;
        if (req.getLegendOptions().get("forceLabels") instanceof String) {
            String forceLabelsOpt = (String) req.getLegendOptions().get("forceLabels");
            if (isTrueValue(forceLabelsOpt)) {
                forceLabelsOn = true;
            } else if (isFalseValue(forceLabelsOpt)) {
                forceLabelsOff = true;
            }
        }

        if (imageStack.size() == 0) {
            throw new IllegalArgumentException("No legend graphics passed");
        }

        Map<String, BufferedImage> legendMap = new LinkedHashMap<String, BufferedImage>();

        if (imageStack.size() == 1 && !forceLabelsOn) {
            BufferedImage finalLegend = (BufferedImage) imageStack.get(0);
            legendMap.put(null, finalLegend);
        } else {
            final int imgCount = imageStack.size();
            final String[] labels = new String[imgCount];

            BufferedImage img = ((BufferedImage) imageStack.get(0));

            int totalWidth = 0;
            int[] rowHeights = new int[imgCount];
            BufferedImage labelsGraphics[] = new BufferedImage[imgCount];
            for (int i = 0; i < imgCount; i++) {
                img = (BufferedImage) imageStack.get(i);

                if (forceLabelsOff) {
                    totalWidth = Math.max(img.getWidth(), totalWidth);
                    rowHeights[i] = img.getHeight();
                } else {

                    Rule rule = rules[i];

                    // What's the label on this rule? We prefer to use
                    // the 'title' if it's available, but fall-back to 'name'
                    final Description description = rule.getDescription();
                    if (description != null && description.getTitle() != null) {
                        final InternationalString title = description.getTitle();
                        labels[i] = title.toString();
                    } else if (rule.getName() == null) {
                        labels[i] = rule.getName();
                    } else {
                        labels[i] = "";
                    }

                    Graphics2D g = img.createGraphics();
                    g.setFont(labelFont);

                    if (useAA) {
                        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                    } else {
                        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                                RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
                    }

                    if (labels[i] != null && labels[i].length() > 0) {
                        final BufferedImage renderedLabel = LegendUtils.renderLabel(labels[i], g,
                                req);
                        labelsGraphics[i] = renderedLabel;
                        final Rectangle2D bounds = new Rectangle2D.Double(0, 0,
                                renderedLabel.getWidth(), renderedLabel.getHeight());

                        totalWidth = (int) Math.ceil(Math.max(img.getWidth() + bounds.getWidth(),
                                totalWidth));
                        rowHeights[i] = (int) Math.ceil(Math.max(img.getHeight(),
                                bounds.getHeight()));
                    } else {
                        totalWidth = Math.max(img.getWidth(), totalWidth);
                        rowHeights[i] = img.getHeight();
                        labelsGraphics[i] = null;
                    }
                }
            }

            final boolean transparent = req.isTransparent();
            final Color backgroundColor = LegendUtils.getBackgroundColor(req);
            final Map<RenderingHints.Key, Object> hintsMap = new HashMap<RenderingHints.Key, Object>();

            for (int i = 0; i < imgCount; i++) {
                img = (BufferedImage) imageStack.get(i);

                // create the final image
                BufferedImage finalLegend = ImageUtils.createImage(totalWidth, img.getHeight(), (IndexColorModel) null,
                        transparent);
                Graphics2D finalGraphics = ImageUtils.prepareTransparency(transparent, backgroundColor,
                        finalLegend, hintsMap);

                // draw the image
                int y = 0;

                if (img.getHeight() < rowHeights[i]) {
                    // move the image to the centre of the row
                    y += (int) ((rowHeights[i] - img.getHeight()) / 2d);
                }

                finalGraphics.drawImage(img, 0, y, null);
                if (!forceLabelsOff) {

                    finalGraphics.setFont(labelFont);

                    if (useAA) {
                        finalGraphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                    } else {
                        finalGraphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                                RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
                    }

                    // draw the label
                    if (labels[i] != null && labels[i].length() > 0) {
                        // first create the actual overall label image.
                        final BufferedImage renderedLabel = labelsGraphics[i];

                        y = 0;
                        if (renderedLabel.getHeight() < rowHeights[i]) {
                            y += (int) ((rowHeights[i] - renderedLabel.getHeight()) / 2d);
                        }

                        finalGraphics.drawImage(renderedLabel, img.getWidth(), y, null);
                        // cleanup
                        renderedLabel.flush();
                        labelsGraphics[i] = null;
                    }
                }
                finalGraphics.dispose();

                // Generate the image name
                String imageName = null;

                Rule rule = rules[i];

                if(rule != null)
                {
                    imageName = rule.getName();
                }

                if(imageName == null)
                {
                    imageName = String.format("%d", i);
                }
                legendMap.put(imageName, finalLegend);
            }

        }
        return legendMap;
    }

    /**
     * Checks if is false value.
     *
     * @param value the value
     * @return true, if is false value
     */
    private static boolean isFalseValue(String value)
    {
        return value.equalsIgnoreCase("off") || value.equalsIgnoreCase("false")
                || value.equalsIgnoreCase("no") || value.equalsIgnoreCase("0");
    }

    /**
     * Checks if is true value.
     *
     * @param value the value
     * @return true, if is true value
     */
    private static boolean isTrueValue(String value)
    {
        return value.equalsIgnoreCase("on") || value.equalsIgnoreCase("true")
                || value.equalsIgnoreCase("yes") || value.equalsIgnoreCase("1");
    }
}
