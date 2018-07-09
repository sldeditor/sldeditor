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

import com.sldeditor.common.data.SelectedSymbol;
import com.sldeditor.ui.render.RuleRenderOptions;
import java.util.List;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.NamedLayerImpl;
import org.geotools.styling.Rule;
import org.geotools.styling.Style;
import org.geotools.styling.StyledLayer;
import org.geotools.styling.UserLayerImpl;

/**
 * Class to render a symbol and decide which bit of the symbol to draw.
 *
 * @author Robert Ward (SCISYS)
 */
public class RenderSymbol {

    /** The render options. */
    private RuleRenderOptions renderOptions = new RuleRenderOptions();

    /** Instantiates a new render symbol. */
    public RenderSymbol() {}

    /**
     * Gets the render style.
     *
     * @param selectedSymbol the selected symbol
     * @return the render style
     */
    public Style getRenderStyle(SelectedSymbol selectedSymbol) {
        List<StyledLayer> styledLayerList = selectedSymbol.getSld().layers();

        for (StyledLayer styledLayer : styledLayerList) {
            List<Style> styleList = null;

            if (styledLayer instanceof NamedLayerImpl) {
                NamedLayerImpl namedLayer = (NamedLayerImpl) styledLayer;

                styleList = namedLayer.styles();
            } else if (styledLayer instanceof UserLayerImpl) {
                UserLayerImpl userLayer = (UserLayerImpl) styledLayer;

                styleList = userLayer.userStyles();
            }

            if (styleList != null) {
                for (Style style : styleList) {
                    FeatureTypeStyle ftsToRender = selectedSymbol.getFeatureTypeStyle();
                    Rule ruleToRender = selectedSymbol.getRule();

                    // Check to see if style contains the rule to render
                    if (shouldRenderSymbol(style, ftsToRender, ruleToRender)) {
                        return renderSymbol(style, ftsToRender, ruleToRender, renderOptions);
                    }
                }
            }
        }
        return null;
    }

    /**
     * Render symbol.
     *
     * @param style the style
     * @param ftsToRender the fts to render
     * @param ruleToRender the rule to render
     * @param options the options
     * @return the style
     */
    private Style renderSymbol(
            Style style,
            FeatureTypeStyle ftsToRender,
            Rule ruleToRender,
            RuleRenderOptions options) {

        int symbolIndex = SelectedSymbol.getInstance().getSymbolIndex();

        RuleRenderVisitor visitor =
                new RuleRenderVisitor(ftsToRender, ruleToRender, symbolIndex, options);
        style.accept(visitor);
        Style copy = (Style) visitor.getCopy();

        return copy;
    }

    /**
     * Should render symbol.
     *
     * @param style the style
     * @param ruleToRender the rule to render
     * @return true, if successful
     */
    private boolean shouldRenderSymbol(
            Style style, FeatureTypeStyle ftsToRender, Rule ruleToRender) {

        if (ruleToRender == null) {
            return true;
        }

        for (FeatureTypeStyle fts : style.featureTypeStyles()) {
            if (fts == ftsToRender) {
                for (Rule rule : fts.rules()) {
                    if (rule == ruleToRender) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * Gets the render options.
     *
     * @return the renderOptions
     */
    public RuleRenderOptions getRenderOptions() {
        return renderOptions;
    }
}
