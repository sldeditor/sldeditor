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
package com.sldeditor.tool.scale;

import org.geotools.styling.Rule;
import org.geotools.styling.StyledLayerDescriptor;
import org.geotools.styling.Symbolizer;
import org.geotools.styling.TextSymbolizer;

import com.sldeditor.common.SLDDataInterface;
import com.sldeditor.common.data.SLDUtils;
import com.sldeditor.common.data.SelectedSymbol;
import com.sldeditor.common.output.SLDWriterInterface;
import com.sldeditor.common.utils.ScaleUtil;
import com.sldeditor.datasource.SLDEditorFile;

/**
 * Class that encapsulates information about the scales at which a rule is displayed.
 */
public class ScaleSLDData {

    /** The Constant NOT_SET_VALUE. */
    private static final double NOT_SET_VALUE = Double.POSITIVE_INFINITY;

    /** The workspace. */
    private String workspace;

    /** The name. */
    private String name;

    /** The named layer. */
    private String namedLayer;

    /** The style. */
    private String style;

    /** The feature type style. */
    private String featureTypeStyle;

    /** The rule. */
    private Rule rule;

    /** The minimum scale. */
    private double minScale = NOT_SET_VALUE;

    /** The maximum scale. */
    private double maxScale = NOT_SET_VALUE;

    /** The original minimum scale. */
    private double originalMinScale = NOT_SET_VALUE;

    /** The original maximum scale. */
    private double originalMaxScale = NOT_SET_VALUE;

    /** The minimum scale updated flag. */
    private boolean minimumScaleUpdated = false;

    /** The maximum scale updated flag. */
    private boolean maximumScaleUpdated = false;

    /** The sld data. */
    private SLDDataInterface sldData = null;

    /** The sld. */
    private StyledLayerDescriptor sld = null;

    /**
     * Constructor.
     *
     * @param sld the sld
     * @param sldData the sld data
     */
    public ScaleSLDData(StyledLayerDescriptor sld, SLDDataInterface sldData) {
        super();
        this.sld = sld;
        this.sldData = sldData;
        this.workspace = this.sldData.getStyle().getWorkspace();
        this.name = this.sldData.getLayerName();
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the named layer.
     *
     * @return the namedLayer
     */
    public String getNamedLayer() {
        return namedLayer;
    }

    /**
     * Gets the workspace.
     *
     * @return the workspace
     */
    public String getWorkspace() {
        return workspace;
    }

    /**
     * Gets the style.
     *
     * @return the style
     */
    public String getStyle() {
        return style;
    }

    /**
     * Gets the feature type style.
     *
     * @return the featureTypeStyle
     */
    public String getFeatureTypeStyle() {
        return featureTypeStyle;
    }

    /**
     * Gets the rule name.
     *
     * @return the rule name
     */
    public String getRuleName() {
        return rule.getName();
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
     * Gets the minimum scale.
     *
     * @return the minScale
     */
    public double getMinScale() {
        return minScale;
    }

    /**
     * Gets the minimum scale as a string.
     *
     * @return the minimum scale a string
     */
    public String getMinScaleString() {
        return ScaleUtil.getValue(minScale);
    }

    /**
     * Gets the maximum scale as a string.
     *
     * @return the maximum scale a string
     */
    public String getMaxScaleString() {
        return ScaleUtil.getValue(maxScale);
    }

    /**
     * Gets the max scale.
     *
     * @return the maxScale
     */
    public double getMaxScale() {
        return maxScale;
    }

    /**
     * Gets the sld data.
     *
     * @return the sldData
     */
    public SLDDataInterface getSldData() {
        return sldData;
    }

    /**
     * Checks if is minimum scale set.
     *
     * @return true, if minimum scale is set
     */
    public boolean isMinScaleSet() {
        return !ScaleUtil.isNotSet(minScale);
    }

    /**
     * Checks if is maximum scale set.
     *
     * @return true, if maximum scale is set
     */
    public boolean isMaxScaleSet() {
        return !ScaleUtil.isNotSet(maxScale);
    }

    /**
     * Sets the named layer.
     *
     * @param namedLayer the namedLayer to set
     */
    public void setNamedLayer(String namedLayer) {
        this.namedLayer = namedLayer;
    }

    /**
     * Sets the style.
     *
     * @param style the style to set
     */
    public void setStyle(String style) {
        this.style = style;
    }

    /**
     * Sets the feature type style.
     *
     * @param featureTypeStyle the featureTypeStyle to set
     */
    public void setFeatureTypeStyle(String featureTypeStyle) {
        this.featureTypeStyle = featureTypeStyle;
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
     * Sets the minimum scale.
     *
     * @param minScale the minimum to set
     */
    public void setMinScale(double minScale) {
        this.minScale = minScale;
        this.originalMinScale = minScale;
        this.minimumScaleUpdated = false;
    }

    /**
     * Sets the maximum scale.
     *
     * @param maxScale the maximum to set
     */
    public void setMaxScale(double maxScale) {
        this.maxScale = maxScale;
        this.originalMaxScale = maxScale;
        this.maximumScaleUpdated = false;
    }

    /**
     * Revert to original.
     */
    public void revertToOriginal() {
        this.minScale = this.originalMinScale;
        this.maxScale = this.originalMaxScale;
        this.minimumScaleUpdated = false;
        this.maximumScaleUpdated = false;
    }

    /**
     * Sets the minimum scale string.
     *
     * @param stringValue the new min scale string
     */
    public void setMinScaleString(Object stringValue) {
        this.minScale = ScaleUtil.extractValue((String) stringValue);
        this.minimumScaleUpdated = true;
    }

    /**
     * Sets the maximum scale string.
     *
     * @param stringValue the new max scale string
     */
    public void setMaxScaleString(Object stringValue) {
        this.maxScale = ScaleUtil.extractValue((String) stringValue);
        this.maximumScaleUpdated = true;
    }

    /**
     * Checks if is minimum scale updated.
     *
     * @return the minimumScaleUpdated
     */
    public boolean isMinimumScaleUpdated() {
        return minimumScaleUpdated;
    }

    /**
     * Checks if is maximum scale updated.
     *
     * @return the maximumScaleUpdated
     */
    public boolean isMaximumScaleUpdated() {
        return maximumScaleUpdated;
    }

    /**
     * Update scales.
     *
     * @param sldWriter the sld writer
     */
    public boolean updateScales(SLDWriterInterface sldWriter) {
        boolean refreshUI = false;
        if (rule != null) {
            if (isMinimumScaleUpdated()) {
                rule.setMinScaleDenominator(minScale);
                minimumScaleUpdated = false;
            }

            if (isMaximumScaleUpdated()) {
                rule.setMaxScaleDenominator(maxScale);
                maximumScaleUpdated = false;
            }

            String sldContents = sldWriter.encodeSLD(null, this.sld);

            SLDDataInterface current = SLDEditorFile.getInstance().getSLDData();

            if (current.getSLDFile().equals(sldData.getSLDFile())
                    || current.getSLDURL().equals(sldData.getSLDURL())) {
                Rule currentFule = SLDUtils.findRule(sld, rule,
                        SelectedSymbol.getInstance().getSld());
                if (currentFule != null) {
                    if (isMinimumScaleUpdated()) {
                        currentFule.setMinScaleDenominator(minScale);
                    }
                    if (isMaximumScaleUpdated()) {
                        currentFule.setMaxScaleDenominator(maxScale);
                    }
                    refreshUI = true;
                }
            }

            sldData.updateSLDContents(sldContents);
        }

        return refreshUI;
    }
}
