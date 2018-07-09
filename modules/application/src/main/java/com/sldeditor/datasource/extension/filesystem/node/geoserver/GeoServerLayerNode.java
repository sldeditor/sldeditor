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

package com.sldeditor.datasource.extension.filesystem.node.geoserver;

import com.sldeditor.common.NodeInterface;
import com.sldeditor.common.data.GeoServerLayer;
import com.sldeditor.common.filesystem.FileSystemInterface;
import com.sldeditor.datasource.extension.filesystem.dataflavour.BuiltInDataFlavour;
import java.awt.datatransfer.DataFlavor;
import javax.swing.Icon;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * File system tree node representing a GeoServer layer.
 *
 * @author Robert Ward (SCISYS)
 */
public class GeoServerLayerNode extends DefaultMutableTreeNode implements NodeInterface {
    /** The handler. */
    private FileSystemInterface handler = null;

    /** The layer. */
    private GeoServerLayer layer = null;

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /**
     * Instantiates a new geo server layer node.
     *
     * @param handler the handler
     * @param layer the layer
     */
    public GeoServerLayerNode(FileSystemInterface handler, GeoServerLayer layer) {
        super(layer.getLayerName());
        this.layer = layer;
        this.handler = handler;
    }

    /**
     * Gets the layer.
     *
     * @return the layer
     */
    public GeoServerLayer getLayer() {
        return layer;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.extension.input.NodeInterface#getHandler()
     */
    @Override
    public FileSystemInterface getHandler() {
        return handler;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.extension.input.NodeInterface#getDataFlavour()
     */
    @Override
    public DataFlavor getDataFlavour() {
        return BuiltInDataFlavour.GEOSERVER_LAYER_DATAITEM_FLAVOUR;
    }

    /**
     * Gets the destination text.
     *
     * @return the destination text
     */
    @Override
    public String getDestinationText() {
        return null;
    }

    @Override
    public Icon getIcon() {
        return null;
    }
}
