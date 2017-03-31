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

package com.sldeditor.common.data;

import java.util.HashMap;
import java.util.Map;

import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Rule;
import org.geotools.styling.Style;
import org.geotools.styling.StyledLayer;
import org.geotools.styling.Symbolizer;

/**
 * The Class SymbolData contains the currently selected state of the the SLD symbol.
 * 
 * @author Robert Ward (SCISYS)
 */
public class SymbolData {

    /** The symbolizer. */
    private Symbolizer symbolizer = null;

    /** The rule. */
    private Rule rule = null;

    /** The style. */
    private Style style = null;

    /** The feature type style. */
    private FeatureTypeStyle featureTypeStyle = null;

    /** The styled layer. */
    private StyledLayer styledLayer;

    /** The selected symbolizer index. */
    private int selectedSymbolizerIndex = -1;

    /** The selected feature type index index. */
    private int selectedFTSIndex = -1;

    /** The selected rule within feature type index. */
    private int selectedRuleIndex = -1;

    /** The selected styled layer index. */
    private int selectedStyledLayerIndex = -1;

    /** The selected style index. */
    private int selectedStyleIndex = -1;

    /** The valid symbol flag map. */
    private Map<String, Boolean> validSymbolMap = new HashMap<String, Boolean>();

    /**
     * Constructor.
     */
    public SymbolData() {
        resetData();
    }

    /**
     * Reset data.
     */
    public void resetData() {
        this.style = null;
        this.styledLayer = null;
        this.featureTypeStyle = null;
        this.rule = null;
        this.symbolizer = null;
        this.selectedFTSIndex = -1;
        this.selectedRuleIndex = -1;
        this.selectedSymbolizerIndex = -1;
        this.selectedStyledLayerIndex = -1;
        this.selectedStyleIndex = -1;
        this.validSymbolMap.clear();
    }

    /**
     * Update.
     *
     * @param localSymbolData the local symbol data
     */
    public void update(SymbolData localSymbolData) {
        this.style = localSymbolData.style;
        this.styledLayer = localSymbolData.styledLayer;
        this.featureTypeStyle = localSymbolData.featureTypeStyle;
        this.rule = localSymbolData.rule;
        this.symbolizer = localSymbolData.symbolizer;
        this.selectedFTSIndex = localSymbolData.selectedFTSIndex;
        this.selectedRuleIndex = localSymbolData.selectedRuleIndex;
        this.selectedSymbolizerIndex = localSymbolData.selectedSymbolizerIndex;
        this.selectedStyledLayerIndex = localSymbolData.selectedStyledLayerIndex;
        this.selectedStyleIndex = localSymbolData.selectedStyleIndex;
    }

    /**
     * Gets the symbolizer.
     *
     * @return the symbolizer
     */
    public Symbolizer getSymbolizer() {
        return symbolizer;
    }

    /**
     * Sets the symbolizer.
     *
     * @param symbolizer the symbolizer to set
     */
    public void setSymbolizer(Symbolizer symbolizer) {
        this.symbolizer = symbolizer;
    }

    /**
     * Gets the rule.
     *
     * @return the rule
     */
    public Rule getRule() {
        return rule;
    }

    /**
     * Sets the rule.
     *
     * @param rule the rule to set
     */
    public void setRule(Rule rule) {
        this.rule = rule;
    }

    /**
     * Gets the style.
     *
     * @return the style
     */
    public Style getStyle() {
        return style;
    }

    /**
     * Sets the style.
     *
     * @param style the style to set
     */
    public void setStyle(Style style) {
        this.style = style;
    }

    /**
     * Gets the feature type style.
     *
     * @return the featureTypeStyle
     */
    public FeatureTypeStyle getFeatureTypeStyle() {
        return featureTypeStyle;
    }

    /**
     * Sets the feature type style.
     *
     * @param featureTypeStyle the featureTypeStyle to set
     */
    public void setFeatureTypeStyle(FeatureTypeStyle featureTypeStyle) {
        this.featureTypeStyle = featureTypeStyle;
    }

    /**
     * Gets the styled layer.
     *
     * @return the styledLayer
     */
    public StyledLayer getStyledLayer() {
        return styledLayer;
    }

    /**
     * Sets the styled layer.
     *
     * @param styledLayer the styledLayer to set
     */
    public void setStyledLayer(StyledLayer styledLayer) {
        this.styledLayer = styledLayer;
    }

    /**
     * Gets the selected symbolizer index.
     *
     * @return the selectedSymbolizerIndex
     */
    public int getSelectedSymbolizerIndex() {
        return selectedSymbolizerIndex;
    }

    /**
     * Set the selected symbolizer index to 0.
     */
    public void initialiseSelectedSymbolizerIndex() {
        this.selectedSymbolizerIndex = 0;
    }

    /**
     * Increment selected symbolizer index.
     */
    public void incrementSelectedSymbolizerIndex() {
        this.selectedSymbolizerIndex++;
    }

    /**
     * Gets the selected fts index.
     *
     * @return the selectedFTSIndex
     */
    public int getSelectedFTSIndex() {
        return selectedFTSIndex;
    }

    /**
     * Set the selected feature type style index to 0.
     */
    public void initialiseSelectedFTSIndex() {
        this.selectedFTSIndex = 0;
    }

    /**
     * Increment selected feature type style index.
     */
    public void incrementSelectedFTSIndex() {
        this.selectedFTSIndex++;
    }

    /**
     * Gets the selected rule index.
     *
     * @return the selectedRuleIndex
     */
    public int getSelectedRuleIndex() {
        return selectedRuleIndex;
    }

    /**
     * Set the selected rule index to 0.
     */
    public void initialiseSelectedRuleIndex() {
        this.selectedRuleIndex = 0;
    }

    /**
     * Increment selected rule index.
     */
    public void incrementSelectedRuleIndex() {
        this.selectedRuleIndex++;
    }

    /**
     * Gets the selected styled layer index.
     *
     * @return the selectedStyledLayerIndex
     */
    public int getSelectedStyledLayerIndex() {
        return selectedStyledLayerIndex;
    }

    /**
     * Set the selected styled layer index to 0.
     */
    public void initialiseSelectedStyledLayerIndex() {
        this.selectedStyledLayerIndex = 0;
    }

    /**
     * Increment selected styled layer index.
     */
    public void incrementSelectedStyledLayerIndex() {
        this.selectedStyledLayerIndex++;
    }

    /**
     * Gets the selected style index.
     *
     * @return the selectedStyleIndex
     */
    public int getSelectedStyleIndex() {
        return selectedStyleIndex;
    }

    /**
     * Set the selected style index to 0.
     */
    public void initialiseSelectedStyleIndex() {
        this.selectedStyleIndex = 0;
    }

    /**
     * Increment selected style index.
     */
    public void incrementSelectedStyleIndex() {
        this.selectedStyleIndex++;
    }

    /**
     * Checks if is valid symbol.
     *
     * @return the validSymbol
     */
    public boolean isValidSymbol() {
        return !validSymbolMap.values().contains(false);
    }

    /**
     * Sets the valid symbol.
     *
     * @param key the key
     * @param validSymbolFlag the valid symbol flag
     */
    public void setValidSymbol(String key, boolean validSymbolFlag) {
        this.validSymbolMap.put(key, validSymbolFlag);
    }
}
