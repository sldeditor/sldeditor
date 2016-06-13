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
package com.sldeditor.extension.filesystem.file.esri;

import java.awt.datatransfer.DataFlavor;

import javax.swing.tree.DefaultMutableTreeNode;

import com.sldeditor.common.NodeInterface;
import com.sldeditor.common.filesystem.FileSystemInterface;

/**
 * Class that represents an Esri MXD layer in the file system tree.
 * 
 * @author Robert Ward (SCISYS)
 */
public class MXDLayerNode extends DefaultMutableTreeNode implements NodeInterface
{
    /** The handler. */
    private FileSystemInterface handler;
    
    /** The mxd info. */
    private MXDInfo mxdInfo = null;
    
    /** The layer name. */
    private String layerName;
    
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /**
     * Instantiates a new geo server node.
     *
     * @param handler the handler
     * @param mxdInfo the mxd info
     * @param layerName the layer name
     */
    public MXDLayerNode(FileSystemInterface handler, MXDInfo mxdInfo, String layerName)
    {
        super(layerName);
        this.layerName = layerName;
        this.mxdInfo = mxdInfo;
        this.handler = handler;
    }

    /**
     * Gets the MXD info.
     *
     * @return the MXD info
     */
    public MXDInfo getMXDInfo()
    {
        return mxdInfo;
    }

    /**
     * Gets the layer name.
     *
     * @return the layer name
     */
    public String getLayerName()
    {
        return layerName;
    }

    /**
     * Gets the handler.
     *
     * @return the handler
     */
    @Override
    public FileSystemInterface getHandler()
    {
        return handler;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.extension.input.NodeInterface#getDataFlavour()
     */
    @Override
    public DataFlavor getDataFlavour()
    {
        return EsriDataFlavour.MXDLAYER_NODE_DATAITEM_FLAVOUR;
    }

    /**
     * Gets the destination text.
     *
     * @return the destination text
     */
    @Override
    public String getDestinationText() {
        return getLayerName();
    }

}
