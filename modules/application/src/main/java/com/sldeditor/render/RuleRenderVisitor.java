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

package com.sldeditor.render;

import com.sldeditor.ui.render.RuleRenderOptions;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.FeatureTypeStyleImpl;
import org.geotools.styling.Graphic;
import org.geotools.styling.LineSymbolizer;
import org.geotools.styling.OtherText;
import org.geotools.styling.OtherTextImpl;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.RasterSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.Style;
import org.geotools.styling.Symbolizer;
import org.geotools.styling.TextSymbolizer;
import org.geotools.styling.TextSymbolizer2;
import org.geotools.styling.visitor.DuplicatingStyleVisitor;
import org.opengis.filter.Filter;
import org.opengis.style.Description;

/**
 * Visitor class to decide whether to draw part of the SLD symbol.
 *
 * @author Robert Ward (SCISYS)
 */
public class RuleRenderVisitor extends DuplicatingStyleVisitor {

    /** The rule to render. */
    private Rule ruleToRender = null;

    /** The display overall flag. */
    private boolean displayOverall = false;

    /** The symbolizer index. */
    private int symbolizerIndex = -1;

    /** The feature type style to render. */
    private FeatureTypeStyle ftsToRender = null;

    /** The rule render options. */
    private RuleRenderOptions options = new RuleRenderOptions();

    /**
     * Instantiates a new rule render visitor.
     *
     * @param featureTypeStyle the feature type style
     * @param rule the rule
     * @param symbolizerIndex the symbolizer index
     * @param options the options
     */
    public RuleRenderVisitor(
            FeatureTypeStyle featureTypeStyle,
            Rule rule,
            int symbolizerIndex,
            RuleRenderOptions options) {
        this.ftsToRender = featureTypeStyle;
        this.ruleToRender = rule;
        this.symbolizerIndex = symbolizerIndex;
        this.options = options;
    }

    /**
     * Visit.
     *
     * @param rule the rule
     */
    /*
     * (non-Javadoc)
     *
     * @see org.geotools.styling.visitor.DuplicatingStyleVisitor#visit(org.geotools.styling.Rule)
     */
    @SuppressWarnings("deprecation")
    @Override
    public void visit(Rule rule) {
        Rule copy = null;

        List<Symbolizer> symsCopy = null;

        if (!displayOverall
                && ((symbolizerIndex >= 0) && (symbolizerIndex < rule.getSymbolizers().length))) {
            symsCopy = new ArrayList<Symbolizer>();
            symsCopy.add(copy(rule.getSymbolizers()[symbolizerIndex]));
        }

        Filter filterCopy = null;

        if (symsCopy == null) {
            symsCopy = rule.symbolizers().stream().map(s -> copy(s)).collect(Collectors.toList());
        }

        Graphic legendCopy = copy((Graphic) rule.getLegend());

        Description descCopy = rule.getDescription();
        descCopy = copy(descCopy);

        copy = sf.createRule();
        copy.symbolizers().addAll(symsCopy);
        copy.setDescription(descCopy);
        copy.setLegend(legendCopy);
        copy.setName(rule.getName());
        copy.setFilter(filterCopy);
        copy.setElseFilter(rule.isElseFilter());
        // Do not copy the min and max scales

        if (STRICT && !copy.equals(rule)) {
            throw new IllegalStateException("Was unable to duplicate provided Rule:" + rule);
        }
        pages.push(copy);
    }

    /**
     * Visit.
     *
     * @param fts the fts
     */
    /*
     * (non-Javadoc)
     *
     * @see org.geotools.styling.visitor.DuplicatingStyleVisitor#visit(org.geotools.styling. FeatureTypeStyle)
     */
    @SuppressWarnings("deprecation")
    @Override
    public void visit(FeatureTypeStyle fts) {

        FeatureTypeStyle copy = new FeatureTypeStyleImpl(fts);

        List<Rule> rulesCopy = null;
        if (this.ruleToRender == null) {
            rulesCopy =
                    fts.rules()
                            .stream()
                            .filter(r -> r != null)
                            .map(
                                    r -> {
                                        r.accept(this);
                                        return (Rule) pages.pop();
                                    })
                            .collect(Collectors.toList());
        } else {
            rulesCopy = new ArrayList<Rule>();
            for (Rule rule : fts.rules()) {
                if ((rule != null) && (renderRule(rule))) {
                    rule.accept(this);
                    rulesCopy.add((Rule) pages.pop());
                }
            }
        }

        copy.rules().clear();
        copy.rules().addAll(rulesCopy);

        if ((fts.getTransformation() != null) && !options.isTransformationApplied()) {
            copy.setTransformation(copy(fts.getTransformation()));
        }
        if (fts.getOnlineResource() != null) {
            copy.setOnlineResource(fts.getOnlineResource());
        }
        copy.getOptions().clear();
        copy.getOptions().putAll(fts.getOptions());

        if (STRICT && !copy.equals(fts)) {
            throw new IllegalStateException(
                    "Was unable to duplicate provided FeatureTypeStyle:" + fts);
        }

        pages.push(copy);
    }

    /**
     * Render rule.
     *
     * @param rule the rule
     * @return true, if successful
     */
    private boolean renderRule(Rule rule) {
        boolean render = false;

        if ((rule != null) && (ruleToRender != null)) {
            render = (rule == ruleToRender);
        }
        return render;
    }

    /**
     * Visit.
     *
     * @param style the style
     */
    // CHECKSTYLE:OFF
    @SuppressWarnings("deprecation")
    // CHECKSTYLE:ON
    @Override
    public void visit(Style style) {
        Style copy = null;

        List<FeatureTypeStyle> ftsCopy = new ArrayList<>();
        if (this.ftsToRender != null) {
            for (FeatureTypeStyle fts : style.featureTypeStyles()) {
                if (fts != null) {
                    if (fts == this.ftsToRender) {
                        fts.accept(this);
                        ftsCopy.add((FeatureTypeStyle) pages.pop());
                    }
                }
            }
        } else {
            for (FeatureTypeStyle fts : style.featureTypeStyles()) {
                if (fts != null) {
                    fts.accept(this);
                    ftsCopy.add((FeatureTypeStyle) pages.pop());
                }
            }
        }
        copy = sf.createStyle();
        copy.getDescription().setAbstract(style.getDescription().getAbstract());
        copy.setName(style.getName());
        copy.getDescription().setTitle(style.getDescription().getTitle());
        copy.featureTypeStyles().addAll(ftsCopy);

        if (STRICT && !copy.equals(style)) {
            throw new IllegalStateException("Was unable to duplicate provided Style:" + style);
        }
        pages.push(copy);
    }

    /**
     * (non-Javadoc)
     *
     * @see
     *     org.geotools.styling.visitor.DuplicatingStyleVisitor#visit(org.geotools.styling.PointSymbolizer)
     */
    public void visit(PointSymbolizer ps) {
        PointSymbolizer copy = sf.getDefaultPointSymbolizer();

        copy.setGeometry(copy(ps.getGeometry()));

        copy.setUnitOfMeasure(ps.getUnitOfMeasure());
        copy.setGraphic(copy(ps.getGraphic()));
        copy.getOptions().putAll(ps.getOptions());

        if (STRICT) {
            if (!copy.equals(ps)) {
                throw new IllegalStateException("Was unable to duplicate provided Graphic:" + ps);
            }
        }
        pages.push(copy);
    }

    /**
     * (non-Javadoc)
     *
     * @see
     *     org.geotools.styling.visitor.DuplicatingStyleVisitor#visit(org.geotools.styling.LineSymbolizer)
     */
    public void visit(LineSymbolizer line) {
        LineSymbolizer copy = sf.getDefaultLineSymbolizer();

        copy.setGeometry(copy(line.getGeometry()));

        copy.setUnitOfMeasure(line.getUnitOfMeasure());
        copy.setStroke(copy(line.getStroke()));
        copy.getOptions().putAll(line.getOptions());
        copy.setPerpendicularOffset(line.getPerpendicularOffset());

        if (STRICT && !copy.equals(line)) {
            throw new IllegalStateException(
                    "Was unable to duplicate provided LineSymbolizer:" + line);
        }
        pages.push(copy);
    }

    /**
     * (non-Javadoc)
     *
     * @see
     *     org.geotools.styling.visitor.DuplicatingStyleVisitor#visit(org.geotools.styling.PolygonSymbolizer)
     */
    public void visit(PolygonSymbolizer poly) {
        PolygonSymbolizer copy = sf.createPolygonSymbolizer();
        copy.setFill(copy(poly.getFill()));

        copy.setGeometry(copy(poly.getGeometry()));

        copy.setUnitOfMeasure(poly.getUnitOfMeasure());
        copy.setStroke(copy(poly.getStroke()));
        copy.getOptions().putAll(poly.getOptions());

        if (STRICT && !copy.equals(poly)) {
            throw new IllegalStateException(
                    "Was unable to duplicate provided PolygonSymbolizer:" + poly);
        }
        pages.push(copy);
    }

    /**
     * (non-Javadoc)
     *
     * @see
     *     org.geotools.styling.visitor.DuplicatingStyleVisitor#visit(org.geotools.styling.TextSymbolizer)
     */
    public void visit(TextSymbolizer text) {
        TextSymbolizer copy = sf.createTextSymbolizer();

        copy.setFill(copy(text.getFill()));
        copy.fonts().clear();
        copy.fonts().addAll(copyFonts(text.fonts()));

        // Ignore geometry field so that symbol is rendered
        copy.setGeometry(copy(text.getGeometry()));

        copy.setUnitOfMeasure(text.getUnitOfMeasure());
        copy.setHalo(copy(text.getHalo()));
        copy.setLabel(copy(text.getLabel()));
        copy.setLabelPlacement(copy(text.getLabelPlacement()));
        copy.setPriority(copy(text.getPriority()));
        copy.getOptions().putAll(text.getOptions());

        if (text instanceof TextSymbolizer2) {
            TextSymbolizer2 text2 = (TextSymbolizer2) text;
            TextSymbolizer2 copy2 = (TextSymbolizer2) copy;

            copy2.setGraphic(copy(text2.getGraphic()));
            copy2.setSnippet(copy(text2.getSnippet()));
            copy2.setFeatureDescription(copy(text2.getFeatureDescription()));
            copy2.setOtherText(copyOtherText(text2.getOtherText()));
        }

        if (STRICT && !copy.equals(text)) {
            throw new IllegalStateException(
                    "Was unable to duplicate provided TextSymbolizer:" + text);
        }
        pages.push(copy);
    }

    /**
     * Method exists in DuplicatingStyleVisitor but is marked private so has to be recreated here.
     *
     * @param otherText the other text
     * @return the other text
     */
    private OtherText copyOtherText(OtherText otherText) {
        if (otherText == null) {
            return null;
        }

        OtherTextImpl copy = new OtherTextImpl();
        copy.setTarget(otherText.getTarget());
        copy.setText(copy(otherText.getText()));
        return copy;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.styling.visitor.DuplicatingStyleVisitor#visit(org.geotools.styling.RasterSymbolizer)
     */
    // CHECKSTYLE:OFF
    public void visit(RasterSymbolizer raster) {
        // CHECKSTYLE:ON
        RasterSymbolizer copy = sf.createRasterSymbolizer();
        copy.setChannelSelection(copy(raster.getChannelSelection()));
        copy.setColorMap(copy(raster.getColorMap()));
        copy.setContrastEnhancement(copy(raster.getContrastEnhancement()));

        copy.setGeometry(copy(raster.getGeometry()));

        copy.setUnitOfMeasure(raster.getUnitOfMeasure());
        copy.setImageOutline(copy(raster.getImageOutline()));
        copy.setOpacity(copy(raster.getOpacity()));
        copy.setOverlap(copy(raster.getOverlap()));
        copy.setShadedRelief(copy(raster.getShadedRelief()));

        if (STRICT && !copy.equals(raster)) {
            throw new IllegalStateException("Was unable to duplicate provided raster:" + raster);
        }
        pages.push(copy);
    }
}
