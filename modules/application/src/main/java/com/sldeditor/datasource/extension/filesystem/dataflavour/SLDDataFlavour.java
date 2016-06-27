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
package com.sldeditor.datasource.extension.filesystem.dataflavour;

import java.awt.datatransfer.DataFlavor;
import java.io.Serializable;

/**
 * Class that contains data describing the type of data the node represents.
 * 
 * @author Robert Ward (SCISYS)
 */
public class SLDDataFlavour extends DataFlavor implements Serializable
{

    /**
     * Default constructor.
     */
    public SLDDataFlavour()
    {
        super();
    }

    /**
     * Instantiates a new SLD data flavour.
     *
     * @param mimeType the mime type
     * @param humanPresentableName the human presentable name
     * @param classLoader the class loader
     * @throws ClassNotFoundException the class not found exception
     */
    public SLDDataFlavour(String mimeType, String humanPresentableName,
            ClassLoader classLoader) throws ClassNotFoundException
    {
        super(mimeType, humanPresentableName, classLoader);
    }

    /**
     * Instantiates a new SLD data flavour.
     *
     * @param mimeType the mime type
     * @param humanPresentableName the human presentable name
     */
    public SLDDataFlavour(String mimeType, String humanPresentableName)
    {
        super(mimeType, humanPresentableName);
    }

    /**
     * Instantiates a new SLD data flavour.
     *
     * @param mimeType the mime type
     * @throws ClassNotFoundException the class not found exception
     */
    public SLDDataFlavour(String mimeType) throws ClassNotFoundException
    {
        super(mimeType);
    }

    /**
     * Instantiates a new SLD data flavour.
     *
     * @param representationClass the representation class
     * @param humanPresentableName the human presentable name
     */
    public SLDDataFlavour(Class<?> representationClass, String humanPresentableName)
    {
        super(representationClass, humanPresentableName);
    }

    /* (non-Javadoc)
     * @see java.awt.datatransfer.DataFlavor#hashCode()
     */
    @Override
    public int hashCode()
    {
        return super.hashCode() + this.getHumanPresentableName().hashCode();
    }

    /* (non-Javadoc)
     * @see java.awt.datatransfer.DataFlavor#equals(java.awt.datatransfer.DataFlavor)
     */
    @Override
    public boolean equals(DataFlavor that)
    {
        if((that == null) || !(that instanceof SLDDataFlavour))
        {
            return false;
        }

        boolean isEqual = super.equals(that);

        if(isEqual)
        {
            isEqual = this.getHumanPresentableName().equals(that.getHumanPresentableName());
        }
        return isEqual;
    }
}
