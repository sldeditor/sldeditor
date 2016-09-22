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
import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.tree.DefaultMutableTreeNode;

import com.sldeditor.common.NodeInterface;
import com.sldeditor.common.data.GeoServerConnection;
import com.sldeditor.common.filesystem.FileSystemInterface;
import com.sldeditor.datasource.extension.filesystem.dataflavour.DataFlavourManager;

/**
 * File system tree node representing a GeoServer workspace.
 * 
 * @author Robert Ward (SCISYS)
 */
public class GeoServerWorkspaceNode extends DefaultMutableTreeNode implements NodeInterface
{
    
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -4880478655273655975L;
    
    /** The handler. */
    private FileSystemInterface handler = null;
    
    /** The connection. */
    private GeoServerConnection connection = null;
    
    /** The workspace name. */
    private String workspaceName;
    
    /** The is style flag (true = style, false = layer). */
    private boolean isStyle = false;

    /** The Constant RESOURCE_ICON. */
    private static final String RESOURCE_ICON = "ui/filesystemicons/geoserverworkspace.png";

    /** The tree icon. */
    private Icon treeIcon = null;

    /**
     * Instantiates a new geo server workspace node.
     *
     * @param handler the handler
     * @param connection the connection
     * @param workspaceName the workspace name
     * @param isStyle the is style
     */
    public GeoServerWorkspaceNode(FileSystemInterface handler,
        GeoServerConnection connection, String workspaceName, boolean isStyle)
    {
        super(workspaceName);
        
        this.handler = handler;
        this.connection = connection;
        this.workspaceName = workspaceName;
        this.isStyle = isStyle;
    }

    /**
     * Gets the handler.
     *
     * @return the handler
     */
    /* (non-Javadoc)
     * @see com.sldeditor.extension.input.NodeInterface#getHandler()
     */
    @Override
    public FileSystemInterface getHandler()
    {
        return handler;
    }

    /**
     * Gets the data flavour.
     *
     * @return the data flavour
     */
    /* (non-Javadoc)
     * @see com.sldeditor.extension.input.NodeInterface#getDataFlavour()
     */
    @Override
    public DataFlavor getDataFlavour()
    {
        return DataFlavourManager.GEOSERVER_WORKSPACE_DATAITEM_FLAVOUR;
    }

    /**
     * Gets the connection.
     *
     * @return the connection
     */
    public GeoServerConnection getConnection()
    {
        return connection;
    }

    /**
     * Gets the workspace name.
     *
     * @return the workspace name
     */
    public String getWorkspaceName()
    {
        return workspaceName;
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

    /**
     * Checks workspace contains styles.
     *
     * @return true, if is style. False for a layer
     */
    public boolean isStyle() {
        return isStyle;
    }

    @Override
    public Icon getIcon() {
        if(treeIcon == null)
        {
            URL url = GeoServerOverallNode.class.getClassLoader().getResource(RESOURCE_ICON);

            treeIcon = new ImageIcon(url);
        }
        return treeIcon;
    }
}
