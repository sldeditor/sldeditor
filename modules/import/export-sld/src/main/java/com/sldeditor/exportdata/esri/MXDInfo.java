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
package com.sldeditor.exportdata.esri;

import java.io.File;
import java.io.Serializable;
import java.util.List;

/**
 * Class that encapsulates Esri MXD data.
 * 
 * @author Robert Ward (SCISYS)
 */
public class MXDInfo implements Serializable
{
    
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -1448600430760851924L;

    /** The mxd filename. */
    private String mxdFilename;
    
    /** The mxd name. */
    private String mxdName;
    
    /** The layer list. */
    private List<String> layerList;

    /** The intermediate file. */
    private File intermediateFile = null;
    
    /**
     * Gets the mxd filename.
     *
     * @return the mxd filename
     */
    public String getMxdFilename()
    {
        return mxdFilename;
    }

    /**
     * Sets the mxd filename.
     *
     * @param mxdFilename the new mxd filename
     */
    public void setMxdFilename(String mxdFilename)
    {
        this.mxdFilename = mxdFilename;
    }

    /**
     * Gets the mxd name.
     *
     * @return the mxd name
     */
    public String getMxdName()
    {
        return mxdName;
    }

    /**
     * Sets the mxd name.
     *
     * @param mxdName the new mxd name
     */
    public void setMxdName(String mxdName)
    {
        this.mxdName = mxdName;
    }

    /**
     * Gets the layer list.
     *
     * @return the layer list
     */
    public List<String> getLayerList()
    {
        return layerList;
    }

    /**
     * Sets the layer list.
     *
     * @param layerList the new layer list
     */
    public void setLayerList(List<String> layerList)
    {
        this.layerList = layerList;
    }

    /**
     * Gets the intermediate file.
     *
     * @return the intermediate file
     */
    public File getIntermediateFile()
    {
        return intermediateFile;
    }

    /**
     * Sets the intermediate file.
     *
     * @param intermediateFile the new intermediate file
     */
    public void setIntermediateFile(File intermediateFile)
    {
        this.intermediateFile = intermediateFile;
    }
}
