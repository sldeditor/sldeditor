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
package com.sldeditor.exportdata.esri.options;

import com.sldeditor.common.property.PropertyManagerFactory;

/**
 * Class that encapsulates the MXD conversion options.
 * 
 * @author Robert Ward (SCISYS)
 */
public class MXDOptions
{
    
    private static final String MXD_FONT_SIZE_FACTOR_FIELD = "MXD.fontSizeFactor";

    /** The font size factor. */
    private double fontSizeFactor = 2.0;
    
    /** The instance. */
    private static MXDOptions instance = null;
    
    /**
     * Instantiates a new MXD options.
     */
    public MXDOptions()
    {
        fontSizeFactor = PropertyManagerFactory.getInstance().getDoubleValue(MXD_FONT_SIZE_FACTOR_FIELD, fontSizeFactor);
    }
    
    /**
     * Gets the single instance of MXDOptions.
     *
     * @return single instance of MXDOptions
     */
    public static MXDOptions getInstance()
    {
        if(instance == null)
        {
            instance = new MXDOptions();
        }
        
        return instance;
    }

    /**
     * Gets the font size factor.
     *
     * @return the font size factor
     */
    public double getFontSizeFactor()
    {
        return fontSizeFactor;
    }

    /**
     * Sets the font size factor.
     *
     * @param fontSizeFactor the new font size factor
     */
    public void setFontSizeFactor(double fontSizeFactor)
    {
        this.fontSizeFactor = fontSizeFactor;
        
        PropertyManagerFactory.getInstance().updateValue(MXD_FONT_SIZE_FACTOR_FIELD, String.valueOf(fontSizeFactor));
    }
}
