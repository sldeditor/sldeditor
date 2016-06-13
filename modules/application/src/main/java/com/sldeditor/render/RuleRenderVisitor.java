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
import org.geotools.styling.Rule;
import org.geotools.styling.Style;
import org.geotools.styling.Symbolizer;
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
    public RuleRenderVisitor(FeatureTypeStyle featureTypeStyle, Rule rule, int symbolizerIndex, RuleRenderOptions options) {
        this.ftsToRender  = featureTypeStyle;
        this.ruleToRender = rule;
        this.symbolizerIndex = symbolizerIndex;
        this.options = options;
    }

    /**
     * Visit.
     *
     * @param rule the rule
     */
    /* (non-Javadoc)
     * @see org.geotools.styling.visitor.DuplicatingStyleVisitor#visit(org.geotools.styling.Rule)
     */
    @SuppressWarnings("deprecation")
    @Override
    public void visit(Rule rule) {
        Rule copy = null;

        Filter filterCopy = null;

        Symbolizer[] symsCopy = null;

        if(!displayOverall)
        {
            if((symbolizerIndex >= 0) && (symbolizerIndex < rule.getSymbolizers().length))
            {
                symsCopy = new Symbolizer[1];
                symsCopy[0] = copy(rule.getSymbolizers()[symbolizerIndex]);
            }
        }

        // As a catch all copy everything
        if(symsCopy == null)
        {
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
        copy.setFilter(filterCopy);
        copy.setElseFilter(rule.isElseFilter());
        // Do not copy the min and max scales

        if(STRICT && !copy.equals( rule )) {
            throw new IllegalStateException("Was unable to duplicate provided Rule:"+rule );
        }
        pages.push(copy);
    }

    /**
     * Visit.
     *
     * @param fts the fts
     */
    /* (non-Javadoc)
     * @see org.geotools.styling.visitor.DuplicatingStyleVisitor#visit(org.geotools.styling.FeatureTypeStyle)
     */
    @SuppressWarnings("deprecation")
    @Override
    public void visit(FeatureTypeStyle fts) {

        FeatureTypeStyle copy = new FeatureTypeStyleImpl( (FeatureTypeStyleImpl)fts);
        
        if(!options.isTransformationApplied())
        {
            copy.setTransformation(null);
        }
        
        Rule[] rules = fts.getRules();

        int length=rules.length;
        Rule[] rulesCopy = null;
        if(this.ruleToRender == null)
        {
            rulesCopy = new Rule[length];
            for (int i = 0; i < length; i++) {
                if (rules[i] != null) {
                    rules[i].accept(this);
                    rulesCopy[i] = (Rule) pages.pop();
                }
            }
        }
        else
        {
            rulesCopy = new Rule[1];
            for (int i = 0; i < length; i++) {
                if (rules[i] != null) {
                    if(renderRule(rules[i]))
                    {
                        rules[i].accept(this);
                        rulesCopy[0] = (Rule) pages.pop();
                    }
                }
            }
        }
        copy.setRules(rulesCopy);

        if( STRICT && !copy.equals( fts )){
            throw new IllegalStateException("Was unable to duplicate provided FeatureTypeStyle:"+fts );
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

        if((rule != null) && (ruleToRender != null))
        {
            // Compare filters
            Filter ruleFilter = rule.getFilter();
            Filter ruleToRenderFilter = ruleToRender.getFilter();
            boolean filtersSame = false;

            if((ruleFilter == null) && (ruleToRenderFilter == null))
            {
                filtersSame = true;
            }
            else if((ruleFilter == null) || (ruleToRenderFilter == null))
            {
                filtersSame = false;
            }
            else
            {
                filtersSame = ruleFilter.equals(ruleToRenderFilter);
            }

            // Compare rule names
            String ruleName = rule.getName();
            String ruleToRenderName = ruleToRender.getName();

            if((ruleName != null) && (ruleToRenderName != null))
            {
                render = (rule.getName().compareTo(ruleToRender.getName()) == 0) && filtersSame;
            }
            else if((ruleName == null) && (ruleToRenderName == null))
            {
                render = filtersSame;
            }
            else
            {
                render = false;
            }
        }
        return render;
    }

    /**
     * Visit.
     *
     * @param style the style
     */
    @SuppressWarnings("deprecation")
    @Override
    public void visit(Style style) {
        Style copy = null;

        FeatureTypeStyle[] fts = style.getFeatureTypeStyles();
        FeatureTypeStyle[] ftsCopy = null;
        if(this.ftsToRender != null)
        {
            ftsCopy = new FeatureTypeStyle[1];
            for (int i = 0; i < fts.length; i++) {
                if (fts[i] != null) {
                    if(fts[i] == this.ftsToRender)
                    {
                        fts[i].accept(this);
                        ftsCopy[0] = (FeatureTypeStyle) pages.pop();
                    }
                }
            }
        }
        else
        {
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

        if( STRICT && !copy.equals( style )){
            throw new IllegalStateException("Was unable to duplicate provided Style:"+style );
        }
        pages.push(copy);
    }
}

