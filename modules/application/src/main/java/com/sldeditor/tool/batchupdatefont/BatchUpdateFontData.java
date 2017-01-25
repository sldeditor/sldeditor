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
package com.sldeditor.tool.batchupdatefont;

import java.util.List;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.styling.Font;
import org.geotools.styling.Rule;
import org.geotools.styling.StyledLayerDescriptor;
import org.geotools.styling.TextSymbolizer;
import org.opengis.filter.FilterFactory;

import com.sldeditor.common.SLDDataInterface;
import com.sldeditor.common.output.SLDWriterInterface;

/**
 * Class that encapsulates information about the scales at which a rule is displayed.
 */
public class BatchUpdateFontData {

    /** The ff. */
    private static FilterFactory ff = CommonFactoryFinder.getFilterFactory();

    /** The Constant NOT_SET_STRING. */
    private static final String NOT_SET_STRING = "";

    /** The Constant NOT_SET_VALUE. */
    private static final int NOT_SET_VALUE = Integer.MIN_VALUE;

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

    /** The symbolizer. */
    private TextSymbolizer symbolizer;

    /** The font name. */
    private String fontName = NOT_SET_STRING;
    
    /** The font style. */
    private String fontStyle = NOT_SET_STRING;

    /** The font weight. */
    private String fontWeight = NOT_SET_STRING;

    /** The font size. */
    private int fontSize = NOT_SET_VALUE;

    /** The original font name. */
    private String originalFontName = NOT_SET_STRING;
    
    /** The original font style. */
    private String originalFontStyle = NOT_SET_STRING;

    /** The original font weight. */
    private String originalFontWeight = NOT_SET_STRING;

    /** The original font size. */
    private int originalFontSize = NOT_SET_VALUE;

    /** The minimum scale updated flag. */
    private boolean fontNameUpdated = false;

    /** The font style updated flag. */
    private boolean fontStyleUpdated = false;

    /** The font weight updated flag. */
    private boolean fontWeightUpdated = false;

    /** The font size updated flag. */
    private boolean fontSizeUpdated = false;

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
    public BatchUpdateFontData(StyledLayerDescriptor sld, SLDDataInterface sldData) {
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
    public Rule getRule()
    {
        return rule;
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
     * Checks if font name set.
     *
     * @return true, if is font name set
     */
    public boolean isFontNameSet() {
        return !fontName.equals(NOT_SET_STRING);
    }

    /**
     * Checks if font style set.
     *
     * @return true, if is font name set
     */
    public boolean isFontStyleSet() {
        return !fontStyle.equals(NOT_SET_STRING);
    }

    /**
     * Checks if font weight set.
     *
     * @return true, if is font name set
     */
    public boolean isFontWeightSet() {
        return !fontWeight.equals(NOT_SET_STRING);
    }

    /**
     * Checks if font size set.
     *
     * @return true, if is font name set
     */
    public boolean isFontSizeSet() {
        return !(fontSize == NOT_SET_VALUE);
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
     * Sets the font name.
     *
     * @param fontName the new font name
     */
    public void setFontName(String fontName) {
        this.fontName = fontName;
        this.originalFontName = fontName;
        this.fontNameUpdated = false;
    }

    /**
     * Sets the font style.
     *
     * @param fontStyle the fontStyle to set
     */
    public void setFontStyle(String fontStyle) {
        this.fontStyle = fontStyle;
        this.originalFontStyle = fontStyle;
        this.fontStyleUpdated = false;
    }

    /**
     * Sets the font weight.
     *
     * @param fontWeight the fontWeight to set
     */
    public void setFontWeight(String fontWeight) {
        this.fontWeight = fontWeight;
        this.originalFontWeight = fontWeight;
        this.fontWeightUpdated = false;
    }

    /**
     * Sets the font size.
     *
     * @param fontSize the fontSize to set
     */
    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
        this.originalFontSize = fontSize;
        this.fontSizeUpdated = false;
    }

    /**
     * Revert to original.
     */
    public void revertToOriginal()
    {
        this.fontName = this.originalFontName;
        this.fontStyle = this.originalFontStyle;
        this.fontWeight = this.originalFontWeight;
        this.fontSize = this.originalFontSize;
        this.fontNameUpdated = false;
        this.fontStyleUpdated = false;
        this.fontWeightUpdated = false;
        this.fontSizeUpdated = false;
    }

    /**
     * Update scales.
     *
     * @param sldWriter the sld writer
     */
    public void updateFont(SLDWriterInterface sldWriter) {
        if(rule != null)
        {
            List<Font> fontList = symbolizer.fonts();
            Font font = fontList.get(0);
            if(isFontNameUpdated())
            {
                font.getFamily().clear();
                font.getFamily().add(ff.literal(fontName));
                fontNameUpdated = false;
            }

            if(isFontStyleUpdated())
            {
                font.setStyle(ff.literal(fontStyle));
                fontStyleUpdated = false;
            }

            if(isFontWeightUpdated())
            {
                font.setWeight(ff.literal(fontWeight));
                fontStyleUpdated = false;
            }

            if(isFontSizeUpdated())
            {
                font.setSize(ff.literal(fontSize));
                fontSizeUpdated = false;
            }

            String sldContents = sldWriter.encodeSLD(null, this.sld);

            sldData.updateSLDContents(sldContents);
        }
    }

    /**
     * Gets the font name.
     *
     * @return the fontName
     */
    public String getFontName() {
        return fontName;
    }

    /**
     * Gets the font style.
     *
     * @return the fontStyle
     */
    public String getFontStyle() {
        return fontStyle;
    }

    /**
     * Gets the font weight.
     *
     * @return the fontWeight
     */
    public String getFontWeight() {
        return fontWeight;
    }

    /**
     * Gets the font size.
     *
     * @return the fontSize
     */
    public int getFontSize() {
        return fontSize;
    }

    /**
     * Checks if is font name updated.
     *
     * @return the fontNameUpdated
     */
    public boolean isFontNameUpdated() {
        return fontNameUpdated;
    }

    /**
     * Sets the font name updated.
     *
     * @param fontNameUpdated the fontNameUpdated to set
     */
    public void setFontNameUpdated(boolean fontNameUpdated) {
        this.fontNameUpdated = fontNameUpdated;
    }

    /**
     * Checks if is font style updated.
     *
     * @return the fontStyleUpdated
     */
    public boolean isFontStyleUpdated() {
        return fontStyleUpdated;
    }

    /**
     * Sets the font style updated.
     *
     * @param fontStyleUpdated the fontStyleUpdated to set
     */
    public void setFontStyleUpdated(boolean fontStyleUpdated) {
        this.fontStyleUpdated = fontStyleUpdated;
    }

    /**
     * Checks if is font weight updated.
     *
     * @return the fontWeightUpdated
     */
    public boolean isFontWeightUpdated() {
        return fontWeightUpdated;
    }

    /**
     * Sets the font weight updated.
     *
     * @param fontWeightUpdated the fontWeightUpdated to set
     */
    public void setFontWeightUpdated(boolean fontWeightUpdated) {
        this.fontWeightUpdated = fontWeightUpdated;
    }

    /**
     * Checks if is font size updated.
     *
     * @return the fontSizeUpdated
     */
    public boolean isFontSizeUpdated() {
        return fontSizeUpdated;
    }

    /**
     * Sets the font size updated.
     *
     * @param fontSizeUpdated the fontSizeUpdated to set
     */
    public void setFontSizeUpdated(boolean fontSizeUpdated) {
        this.fontSizeUpdated = fontSizeUpdated;
    }

    /**
     * Gets the symbolizer.
     *
     * @return the symbolizer
     */
    public String getSymbolizer() {
        return symbolizer.getName();
    }

    /**
     * Sets the symbolizer.
     *
     * @param symbolizer the symbolizer to set
     */
    public void setSymbolizer(TextSymbolizer symbolizer) {
        this.symbolizer = symbolizer;
    }

}
