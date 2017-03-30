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

import com.sldeditor.ui.render.RuleRenderOptions;

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
    public RuleRenderVisitor(FeatureTypeStyle featureTypeStyle, Rule rule, int symbolizerIndex,
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

        Symbolizer[] symsCopy = null;

        if (!displayOverall) {
            if ((symbolizerIndex >= 0) && (symbolizerIndex < rule.getSymbolizers().length)) {
                symsCopy = new Symbolizer[1];
                symsCopy[0] = copy(rule.getSymbolizers()[symbolizerIndex]);
            }
        }

        // As a catch all copy everything
        if (symsCopy == null) {
            symsCopy = rule.getSymbolizers();
            for (int i = 0; i < symsCopy.length; i++) {
                symsCopy[i] = copy(symsCopy[i]);
            }
        }

        Graphic[] legendCopy = rule.getLegendGraphic();
        for (int i = 0; i < legendCopy.length; i++) {
            legendCopy[i] = copy(legendCopy[i]);
        }

        Description descCopy = rule.getDescription();
        descCopy = copy(descCopy);

        copy = sf.createRule();
        copy.setSymbolizers(symsCopy);
        copy.setDescription(descCopy);
        copy.setLegendGraphic(legendCopy);
        copy.setName(rule.getName());
        Filter filterCopy = null;
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
     * @see org.geotools.styling.visitor.DuplicatingStyleVisitor#visit(org.geotools.styling.
     * FeatureTypeStyle)
     */
    @SuppressWarnings("deprecation")
    @Override
    public void visit(FeatureTypeStyle fts) {

        FeatureTypeStyle copy = new FeatureTypeStyleImpl((FeatureTypeStyleImpl) fts);

        if (!options.isTransformationApplied()) {
            copy.setTransformation(null);
        }

        Rule[] rules = fts.getRules();

        int length = rules.length;
        Rule[] rulesCopy = null;
        if (this.ruleToRender == null) {
            rulesCopy = new Rule[length];
            for (int i = 0; i < length; i++) {
                if (rules[i] != null) {
                    rules[i].accept(this);
                    rulesCopy[i] = (Rule) pages.pop();
                }
            }
        } else {
            rulesCopy = new Rule[1];
            for (int i = 0; i < length; i++) {
                if (rules[i] != null) {
                    if (renderRule(rules[i])) {
                        rules[i].accept(this);
                        rulesCopy[0] = (Rule) pages.pop();
                    }
                }
            }
        }
        copy.setRules(rulesCopy);

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
    //CHECKSTYLE:OFF
    @SuppressWarnings("deprecation")
    //CHECKSTYLE:ON
    @Override
    public void visit(Style style) {
        Style copy = null;

        FeatureTypeStyle[] fts = style.getFeatureTypeStyles();
        FeatureTypeStyle[] ftsCopy = null;
        if (this.ftsToRender != null) {
            ftsCopy = new FeatureTypeStyle[1];
            for (int i = 0; i < fts.length; i++) {
                if (fts[i] != null) {
                    if (fts[i] == this.ftsToRender) {
                        fts[i].accept(this);
                        ftsCopy[0] = (FeatureTypeStyle) pages.pop();
                    }
                }
            }
        } else {
            ftsCopy = new FeatureTypeStyle[fts.length];
            for (int i = 0; i < fts.length; i++) {
                if (fts[i] != null) {
                    fts[i].accept(this);
                    ftsCopy[i] = (FeatureTypeStyle) pages.pop();
                }
            }
        }

        copy = sf.createStyle();
        copy.setAbstract(style.getAbstract());
        copy.setName(style.getName());
        copy.setTitle(style.getTitle());
        copy.setFeatureTypeStyles(ftsCopy);

        if (STRICT && !copy.equals(style)) {
            throw new IllegalStateException("Was unable to duplicate provided Style:" + style);
        }
        pages.push(copy);
    }

    /**
     * (non-Javadoc)
     * 
     * @see org.geotools.styling.visitor.DuplicatingStyleVisitor#visit(org.geotools.styling.PointSymbolizer)
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
     * @see org.geotools.styling.visitor.DuplicatingStyleVisitor#visit(org.geotools.styling.LineSymbolizer)
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
     * @see org.geotools.styling.visitor.DuplicatingStyleVisitor#visit(org.geotools.styling.PolygonSymbolizer)
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
     * @see org.geotools.styling.visitor.DuplicatingStyleVisitor#visit(org.geotools.styling.TextSymbolizer)
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
            copy2.setOtherText(copy(text2.getOtherText()));
        }

        if (STRICT && !copy.equals(text)) {
            throw new IllegalStateException(
                    "Was unable to duplicate provided TextSymbolizer:" + text);
        }
        pages.push(copy);
    }

    /**
     * Method exists in DuplicatingStyleVisitor but is marked private.
     *
     * @param otherText the other text
     * @return the other text
     */
    private OtherText copy(OtherText otherText) {
        if (otherText == null) {
            return null;
        }

        // TODO: add methods to the factory to create OtherText instances
        // sf.createOtherText();
        OtherTextImpl copy = new OtherTextImpl();
        copy.setTarget(otherText.getTarget());
        copy.setText(copy(otherText.getText()));
        return copy;
    }

    /**
     * (non-Javadoc)
     * 
     * @see org.geotools.styling.visitor.DuplicatingStyleVisitor#visit(org.geotools.styling.
     * RasterSymbolizer)
     */
    //CHECKSTYLE:OFF
    public void visit(RasterSymbolizer raster) {
        //CHECKSTYLE:ON
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
