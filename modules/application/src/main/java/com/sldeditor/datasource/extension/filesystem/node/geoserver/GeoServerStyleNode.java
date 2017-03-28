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

import java.awt.datatransfer.DataFlavor;

import javax.swing.Icon;
import javax.swing.tree.DefaultMutableTreeNode;

import com.sldeditor.common.NodeInterface;
import com.sldeditor.common.data.GeoServerConnection;
import com.sldeditor.common.data.StyleWrapper;
import com.sldeditor.common.filesystem.FileSystemInterface;
import com.sldeditor.datasource.extension.filesystem.dataflavour.BuiltInDataFlavour;

/**
 * File system tree node representing a GeoServer style.
 * 
 * @author Robert Ward (SCISYS)
 */
public class GeoServerStyleNode extends DefaultMutableTreeNode implements NodeInterface {

    /** The connection data. */
    private GeoServerConnection connectData = null;

    /** The style. */
    private StyleWrapper style = null;

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The handler. */
    private FileSystemInterface handler = null;

    /**
     * Instantiates a new GeoServerStyleNode.
     *
     * @param handler the handler
     * @param connectData the connect data
     * @param style the style
     */
    public GeoServerStyleNode(FileSystemInterface handler, GeoServerConnection connectData,
            StyleWrapper style) {
        super(style.getStyle());
        this.handler = handler;
        this.style = style;
        this.connectData = connectData;
    }

    /**
     * Gets the style.
     *
     * @return the style
     */
    public StyleWrapper getStyle() {
        return style;
    }

    /**
     * Gets the connect data.
     *
     * @return the connect data
     */
    public GeoServerConnection getConnectionData() {
        return connectData;
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
        return BuiltInDataFlavour.GEOSERVER_STYLE_DATAITEM_FLAVOUR;
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
