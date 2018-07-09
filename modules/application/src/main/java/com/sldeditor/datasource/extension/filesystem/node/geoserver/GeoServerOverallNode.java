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
import com.sldeditor.common.filesystem.FileSystemInterface;
import com.sldeditor.datasource.extension.filesystem.dataflavour.BuiltInDataFlavour;
import java.awt.datatransfer.DataFlavor;
import java.net.URL;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * File system tree node representing a GeoServer server.
 *
 * @author Robert Ward (SCISYS)
 */
public class GeoServerOverallNode extends DefaultMutableTreeNode implements NodeInterface {

    /** The Constant GEOSERVER_NODE. */
    public static final String GEOSERVER_NODE = "GeoServer";

    /** The handler. */
    private FileSystemInterface handler = null;

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The Constant RESOURCE_ICON. */
    private static final String RESOURCE_ICON = "ui/filesystemicons/geoserver.png";

    /** The tree icon. */
    private Icon treeIcon = null;

    /**
     * Instantiates a new geo server overall node.
     *
     * @param handler the handler
     */
    public GeoServerOverallNode(FileSystemInterface handler) {
        super(GEOSERVER_NODE);
        this.handler = handler;
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
        return BuiltInDataFlavour.GEOSERVER_OVERALL_DATAITEM_FLAVOUR;
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

    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.common.NodeInterface#getIcon()
     */
    @Override
    public Icon getIcon() {
        if (treeIcon == null) {
            URL url = GeoServerOverallNode.class.getClassLoader().getResource(RESOURCE_ICON);

            treeIcon = new ImageIcon(url);
        }
        return treeIcon;
    }
}
