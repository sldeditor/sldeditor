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
    
    /** The image size expressed as a percentage. 100% = original image size*/
    private int imageSize = 100;

    /** The split symbolizers, if true then legend entries are saved to individual files. */
    private boolean splitSymbolizers = false;
    
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
}
