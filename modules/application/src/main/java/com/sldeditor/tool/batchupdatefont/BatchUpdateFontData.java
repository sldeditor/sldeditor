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

import java.util.ArrayList;
import java.util.List;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.AttributeExpressionImpl;
import org.geotools.filter.ConstantExpression;
import org.geotools.filter.FunctionExpression;
import org.geotools.filter.LiteralExpressionImpl;
import org.geotools.filter.MathExpressionImpl;
import org.geotools.styling.Font;
import org.geotools.styling.Rule;
import org.geotools.styling.StyleFactoryImpl;
import org.geotools.styling.StyledLayerDescriptor;
import org.geotools.styling.Symbolizer;
import org.geotools.styling.TextSymbolizer;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;

import com.sldeditor.common.SLDDataInterface;
import com.sldeditor.common.data.SLDUtils;
import com.sldeditor.common.data.SelectedSymbol;
import com.sldeditor.common.output.SLDWriterInterface;
import com.sldeditor.datasource.SLDEditorFile;

/**
 * Class that encapsulates information about the scales at which a rule is displayed.
 */
public class BatchUpdateFontData {

    /** The filter factory. */
    private static FilterFactory ff = CommonFactoryFinder.getFilterFactory();

    /** The style factory. */
    private static StyleFactoryImpl styleFactory = (StyleFactoryImpl) CommonFactoryFinder
            .getStyleFactory();

    /** The Constant NOT_SET_STRING. */
    private static final String NOT_SET_STRING = "";

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

    /** The original font name. */
    private List<Expression> originalFontName = new ArrayList<Expression>();

    /** The original font style. */
    private Expression originalFontStyle = null;

    /** The original font weight. */
    private Expression originalFontWeight = null;

    /** The original font size. */
    private Expression originalFontSize = null;

    /** The sld data. */
    private SLDDataInterface sldData = null;

    /** The sld. */
    private StyledLayerDescriptor sld = null;

    /** The font. */
    private Font font = null;

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
        if (sldData != null) {
            this.workspace = this.sldData.getStyle().getWorkspace();
            this.name = this.sldData.getLayerName();
        }
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
        if (rule != null) {
            return rule.getName();
        }

        return NOT_SET_STRING;
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
        return ((font != null) && !font.getFamily().isEmpty());
    }

    /**
     * Checks if font style set.
     *
     * @return true, if is font name set
     */
    public boolean isFontStyleSet() {
        return ((font != null) && (font.getStyle() != null));
    }

    /**
     * Checks if font weight set.
     *
     * @return true, if is font name set
     */
    public boolean isFontWeightSet() {
        return ((font != null) && (font.getWeight() != null));
    }

    /**
     * Checks if font size set.
     *
     * @return true, if is font name set
     */
    public boolean isFontSizeSet() {
        return ((font != null) && (font.getSize() != null));
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
     * Revert to original.
     */
    public void revertToOriginal() {

        if (this.font != null) {
            this.font.getFamily().clear();
            this.font.getFamily().addAll(originalFontName);
            this.font.setWeight(originalFontWeight);
            this.font.setStyle(originalFontStyle);
            this.font.setSize(originalFontSize);
        }
    }

    /**
     * Update scales.
     *
     * @param sldWriter the sld writer
     * @return true, if successful
     */
    public boolean updateFont(SLDWriterInterface sldWriter) {
        boolean refreshUI = false;
        if ((rule != null) && (sldWriter != null)) {
            List<Font> fontList = symbolizer.fonts();
            Font font = fontList.get(0);
            if (isFontNameUpdated()) {
                font.getFamily().clear();
                font.getFamily().addAll(this.font.getFamily());
            }

            if (isFontStyleUpdated()) {
                font.setStyle(ff.literal(this.font.getStyle()));
            }

            if (isFontWeightUpdated()) {
                font.setWeight(ff.literal(this.font.getWeight()));
            }

            if (isFontSizeUpdated()) {
                font.setSize(ff.literal(this.font.getSize()));
            }

            String sldContents = sldWriter.encodeSLD(null, this.sld);

            SLDDataInterface current = SLDEditorFile.getInstance().getSLDData();

            if ((current != null) && (sldData != null)) {
                if (((current.getSLDFile() == null) && (sldData.getSLDFile() == null)) ||
                       ((current.getSLDFile() != null) && current.getSLDFile().equals(sldData.getSLDFile()))
                        || ((current.getSLDURL() != null) && current.getSLDURL().equals(sldData.getSLDURL()))) {
                    Symbolizer currentSymbolizer = SLDUtils.findSymbolizer(sld, symbolizer,
                            SelectedSymbol.getInstance().getSld());
                    if (currentSymbolizer != null) {
                        if (currentSymbolizer instanceof TextSymbolizer) {
                            TextSymbolizer textSymbolizer = (TextSymbolizer) currentSymbolizer;
                            textSymbolizer.fonts().clear();
                            textSymbolizer.fonts().add(font);
                            refreshUI = true;
                        }
                    }
                }
            }
            sldData.updateSLDContents(sldContents);

            setOriginalData(font);
        }
        return refreshUI;
    }

    /**
     * Gets the font name.
     *
     * @return the fontName
     */
    public String getFontName() {
        if (isFontNameSet()) {
            return this.font.getFamily().get(0).toString();
        }
        return NOT_SET_STRING;
    }

    /**
     * Gets the font style.
     *
     * @return the fontStyle
     */
    public String getFontStyle() {
        if (isFontStyleSet()) {
            return this.font.getStyle().toString();
        }
        return NOT_SET_STRING;
    }

    /**
     * Gets the font weight.
     *
     * @return the fontWeight
     */
    public String getFontWeight() {
        if (isFontWeightSet()) {
            return this.font.getWeight().toString();
        }
        return NOT_SET_STRING;
    }

    /**
     * Gets the font size.
     *
     * @return the fontSize
     */
    public String getFontSize() {
        if (isFontSizeSet()) {
            return (this.font.getSize().toString());
        }
        return NOT_SET_STRING;
    }

    /**
     * Checks if is font name updated.
     *
     * @return the fontNameUpdated
     */
    public boolean isFontNameUpdated() {
        return ((font != null) && !(originalFontName.equals(font.getFamily())));
    }

    /**
     * Checks if is font style updated.
     *
     * @return the fontStyleUpdated
     */
    public boolean isFontStyleUpdated() {
        return (font != null) && isSame(originalFontStyle, font.getStyle());
    }

    /**
     * Checks if is font weight updated.
     *
     * @return the fontWeightUpdated
     */
    public boolean isFontWeightUpdated() {
        return (font != null) && isSame(originalFontWeight, font.getWeight());
    }

    /**
     * Checks if expression has changed from the original
     *
     * @param original the original
     * @param exp the exp
     * @return true, if is same
     */
    private boolean isSame(Expression original, Expression exp) {
        if ((original == null) && (exp == null)) {
            return false;
        } else if ((original != null) && (exp != null)) {
            return (!(original.equals(exp)));
        }
        return true;
    }

    /**
     * Checks if is font size updated.
     *
     * @return the fontSizeUpdated
     */
    public boolean isFontSizeUpdated() {
        return (font != null) && isSame(originalFontSize, font.getSize());
    }

    /**
     * Gets the symbolizer.
     *
     * @return the symbolizer
     */
    public String getSymbolizer() {
        if (symbolizer != null) {
            return symbolizer.getName();
        }
        return NOT_SET_STRING;
    }

    /**
     * Sets the symbolizer.
     *
     * @param symbolizer the symbolizer to set
     */
    public void setSymbolizer(TextSymbolizer symbolizer) {
        this.symbolizer = symbolizer;
    }

    /**
     * Gets the font.
     *
     * @return the font
     */
    public Font getFont() {
        return font;
    }

    /**
     * Sets the font.
     *
     * @param newFont the new font
     */
    public void setFont(Font newFont) {
        if (newFont != null) {
            this.font = styleFactory.getDefaultFont();

            font.getFamily().clear();
            font.getFamily().addAll(newFont.getFamily());
            font.setStyle(newFont.getStyle());
            font.setWeight(newFont.getWeight());
            font.setSize(newFont.getSize());

            setOriginalData(newFont);
        }
    }

    /**
     * Sets the original data.
     *
     * @param newFont the new original data
     */
    private void setOriginalData(Font newFont) {
        this.originalFontName = newFont.getFamily();
        this.originalFontStyle = newFont.getStyle();
        this.originalFontWeight = newFont.getWeight();
        this.originalFontSize = newFont.getSize();
    }

    /**
     * Update font.
     *
     * @param fontData the font data
     */
    public void updateFont(Font fontData) {

        if ((fontData != null) && (font != null)) {
            if (!fontData.getFamily().isEmpty()) {
                if (!(fontData.getFamily().equals(font.getFamily()))) {
                    font.getFamily().clear();
                    font.getFamily().addAll(fontData.getFamily());
                }
            }

            if (fontData.getWeight() != null) {
                if (!(fontData.getWeight().equals(font.getWeight()))) {
                    font.setWeight(fontData.getWeight());
                }
            }

            if (fontData.getStyle() != null) {
                if (!(fontData.getStyle().equals(font.getStyle()))) {
                    font.setStyle(fontData.getStyle());
                }
            }

            if (fontData.getSize() != null) {
                if (!(fontData.getSize().equals(font.getSize()))) {
                    font.setSize(fontData.getSize());
                }
            }
        }
    }

    /**
     * Find out whether any data has changed
     *
     * @return true, if any data has been changed
     */
    public boolean anyChanges() {
        return isFontNameUpdated() || isFontStyleUpdated() || isFontWeightUpdated()
                || isFontSizeUpdated();
    }

    /**
     * Update font size.
     *
     * @param fontSize the font size
     */
    public void updateFontSize(int fontSize) {
        if (font != null) {
            if (!(String.valueOf(fontSize).equals(font.getSize().toString()))) {
                if (font.getSize() instanceof LiteralExpressionImpl) {
                    int updatedSize = Double.valueOf(font.getSize().toString()).intValue()
                            + fontSize;
                    // Make sure we don't get negative sized fonts!
                    if (updatedSize < 1) {
                        updatedSize = 1;
                    }
                    Expression exp = ff.literal(updatedSize);

                    font.setSize(exp);
                } else if ((font.getSize() instanceof FunctionExpression)
                        || (font.getSize() instanceof MathExpressionImpl)
                        || (font.getSize() instanceof ConstantExpression)
                        || (font.getSize() instanceof AttributeExpressionImpl)) {
                    Expression updatedSize = ff.add(font.getSize(), ff.literal(fontSize));
                    font.setSize(updatedSize);
                }
            }
        }
    }

}
