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
package com.sldeditor.tool.legendpanel.option;

import java.awt.Color;
import java.awt.Font;

import org.geoserver.wms.legendgraphic.LegendUtils;

/**
 * Class encapsulating legend configuration data.
 * 
 * @author Robert Ward (SCISYS)
 */
public class LegendOptionData
{

    /** The image width. */
    private int imageWidth = 20;

    /** The image height. */
    private int imageHeight = 20;

    /** The maintain aspect ratio. */
    private boolean maintainAspectRatio = true;

    /** The anti alias. */
    private boolean antiAlias = true;

    /** The background colour. */
    private Color backgroundColour = Color.WHITE;

    /** The dpi. */
    private int dpi = 96;

    /** The show labels flag. */
    private boolean showLabels = true;

    /** The show title flag. */
    private boolean showTitle = true;

    /** The image size expressed as a percentage. 100% = original image size */
    private int imageSize = 100;

    /** The split symbolizers, if true then legend entries are saved to individual files. */
    private boolean splitSymbolizers = false;

    /** The label font. */
    private Font labelFont = LegendUtils.DEFAULT_FONT;

    /** The label font colour. */
    private Color labelFontColour = LegendUtils.DEFAULT_FONT_COLOR;

    /** The border colour. */
    private Color borderColour = LegendUtils.DEFAULT_BORDER_COLOR;

    /** The border rule. */
    private boolean borderRule = false;

    /** The gray channel name. */
    private String grayChannelName = "1";

    /** The font anti aliasing. */
    private boolean fontAntiAliasing = true;

    /** The border flag. */
    private boolean border = false;

    /** The band information flag. */
    private boolean bandInformation = false;

    /** The transparent flag. */
    private boolean transparent = false;

    /**
     * Gets the image width.
     *
     * @return the image width
     */
    public int getImageWidth()
    {
        return imageWidth;
    }

    /**
     * Sets the image width.
     *
     * @param imageWidth the new image width
     */
    public void setImageWidth(int imageWidth)
    {
        this.imageWidth = imageWidth;

        if(maintainAspectRatio)
        {
            this.imageHeight = imageWidth;
        }
    }

    /**
     * Gets the image height.
     *
     * @return the image height
     */
    public int getImageHeight()
    {
        return imageHeight;
    }

    /**
     * Sets the image height.
     *
     * @param imageHeight the new image height
     */
    public void setImageHeight(int imageHeight)
    {
        this.imageHeight = imageHeight;

        if(maintainAspectRatio)
        {
            this.imageWidth = imageHeight;
        }
    }

    /**
     * Checks if is maintain aspect ratio.
     *
     * @return true, if is maintain aspect ratio
     */
    public boolean isMaintainAspectRatio()
    {
        return maintainAspectRatio;
    }

    /**
     * Sets the maintain aspect ratio.
     *
     * @param maintainAspectRatio the new maintain aspect ratio
     */
    public void setMaintainAspectRatio(boolean maintainAspectRatio)
    {
        this.maintainAspectRatio = maintainAspectRatio;
    }

    /**
     * Gets the dpi.
     *
     * @return the dpi
     */
    public int getDpi()
    {
        return dpi;
    }

    /**
     * Sets the dpi.
     *
     * @param dpi the new dpi
     */
    public void setDpi(int dpi)
    {
        this.dpi = dpi;
    }

    /**
     * Checks if is anti alias.
     *
     * @return true, if is anti alias
     */
    public boolean isAntiAlias()
    {
        return antiAlias;
    }

    /**
     * Sets the anti alias.
     *
     * @param antiAlias the new anti alias
     */
    public void setAntiAlias(boolean antiAlias)
    {
        this.antiAlias = antiAlias;
    }

    /**
     * Gets the background colour.
     *
     * @return the background colour
     */
    public Color getBackgroundColour()
    {
        return backgroundColour;
    }

    /**
     * Sets the background colour.
     *
     * @param backgroundColour the new background colour
     */
    public void setBackgroundColour(Color backgroundColour)
    {
        this.backgroundColour = backgroundColour;
    }

    /**
     * Checks if is hide labels.
     *
     * @return true, if is hide labels
     */
    public boolean showLabels()
    {
        return showLabels;
    }

    /**
     * Sets the show labels flag.
     *
     * @param showLabels the new show labels
     */
    public void setShowLabels(boolean showLabels)
    {
        this.showLabels = showLabels;
    }

    /**
     * Gets the image size.
     *
     * @return the image size
     */
    public int getImageSize()
    {
        return imageSize;
    }

    /**
     * Sets the image size.
     *
     * @param imageSize the new image size
     */
    public void setImageSize(int imageSize)
    {
        this.imageSize = imageSize;
    }

    /**
     * Return split symbolizers flag.
     *
     * @return true, if successful
     */
    public boolean splitSymbolizers()
    {
        return splitSymbolizers;
    }

    /**
     * Sets the split symbolizers.
     *
     * @param splitSymbolizers the new split symbolizers
     */
    public void setSplitSymbolizers(boolean splitSymbolizers)
    {
        this.splitSymbolizers = splitSymbolizers;
    }

    /**
     * Checks if is show title.
     *
     * @return the showTitle
     */
    public boolean isShowTitle() {
        return showTitle;
    }

    /**
     * Sets the show title.
     *
     * @param showTitle the showTitle to set
     */
    public void setShowTitle(boolean showTitle) {
        this.showTitle = showTitle;
    }

    /**
     * Gets the label font.
     *
     * @return the labelFont
     */
    public Font getLabelFont() {
        return labelFont;
    }

    /**
     * Sets the label font.
     *
     * @param labelFont the labelFont to set
     */
    public void setLabelFont(Font labelFont) {
        this.labelFont = labelFont;
    }

    /**
     * Gets the label font colour.
     *
     * @return the labelFontColour
     */
    public Color getLabelFontColour() {
        return labelFontColour;
    }

    /**
     * Sets the label font colour.
     *
     * @param labelFontColour the labelFontColour to set
     */
    public void setLabelFontColour(Color labelFontColour) {
        this.labelFontColour = labelFontColour;
    }

    /**
     * Gets the border colour.
     *
     * @return the borderColour
     */
    public Color getBorderColour() {
        return borderColour;
    }

    /**
     * Sets the border colour.
     *
     * @param borderColour the borderColour to set
     */
    public void setBorderColour(Color borderColour) {
        this.borderColour = borderColour;
    }

    /**
     * Checks if is border rule.
     *
     * @return the borderRule
     */
    public boolean isBorderRule() {
        return borderRule;
    }

    /**
     * Sets the border rule.
     *
     * @param borderRule the borderRule to set
     */
    public void setBorderRule(boolean borderRule) {
        this.borderRule = borderRule;
    }

    /**
     * Gets the gray channel name.
     *
     * @return the grayChannelName
     */
    public String getGrayChannelName() {
        return grayChannelName;
    }

    /**
     * Sets the gray channel name.
     *
     * @param grayChannelName the grayChannelName to set
     */
    public void setGrayChannelName(String grayChannelName) {
        this.grayChannelName = grayChannelName;
    }

    /**
     * Checks if is font anti aliasing.
     *
     * @return the fontAntiAliasing
     */
    public boolean isFontAntiAliasing() {
        return fontAntiAliasing;
    }

    /**
     * Sets the font anti aliasing.
     *
     * @param fontAntiAliasing the fontAntiAliasing to set
     */
    public void setFontAntiAliasing(boolean fontAntiAliasing) {
        this.fontAntiAliasing = fontAntiAliasing;
    }

    /**
     * Checks if is border.
     *
     * @return the border
     */
    public boolean isBorder() {
        return border;
    }

    /**
     * Sets the border.
     *
     * @param border the border to set
     */
    public void setBorder(boolean border) {
        this.border = border;
    }

    /**
     * Checks if is band information.
     *
     * @return the bandInformation
     */
    public boolean isBandInformation() {
        return bandInformation;
    }

    /**
     * Sets the band information.
     *
     * @param bandInformation the bandInformation to set
     */
    public void setBandInformation(boolean bandInformation) {
        this.bandInformation = bandInformation;
    }

    /**
     * Checks if is show labels.
     *
     * @return the showLabels
     */
    public boolean isShowLabels() {
        return showLabels;
    }

    /**
     * Checks if flag is transparent.
     *
     * @return the transparent
     */
    public boolean isTransparent() {
        return transparent;
    }

    /**
     * Sets the transparent flag.
     *
     * @param transparent the transparent to set
     */
    public void setTransparent(boolean transparent) {
        this.transparent = transparent;
    }  
}
