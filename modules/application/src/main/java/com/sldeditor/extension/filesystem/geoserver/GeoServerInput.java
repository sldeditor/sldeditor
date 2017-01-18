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
package com.sldeditor.extension.filesystem.geoserver;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.apache.log4j.Logger;

import com.sldeditor.common.NodeInterface;
import com.sldeditor.common.SLDDataInterface;
import com.sldeditor.common.ToolSelectionInterface;
import com.sldeditor.common.connection.GeoServerConnectionManager;
import com.sldeditor.common.data.GeoServerConnection;
import com.sldeditor.common.data.GeoServerLayer;
import com.sldeditor.common.data.SLDData;
import com.sldeditor.common.data.StyleWrapper;
import com.sldeditor.common.filesystem.FileSystemInterface;
import com.sldeditor.common.filesystem.SelectedFiles;
import com.sldeditor.datasource.extension.filesystem.GeoServerConnectUpdateInterface;
import com.sldeditor.datasource.extension.filesystem.node.FSTree;
import com.sldeditor.datasource.extension.filesystem.node.geoserver.GeoServerLayerHeadingNode;
import com.sldeditor.datasource.extension.filesystem.node.geoserver.GeoServerLayerNode;
import com.sldeditor.datasource.extension.filesystem.node.geoserver.GeoServerNode;
import com.sldeditor.datasource.extension.filesystem.node.geoserver.GeoServerOverallNode;
import com.sldeditor.datasource.extension.filesystem.node.geoserver.GeoServerStyleHeadingNode;
import com.sldeditor.datasource.extension.filesystem.node.geoserver.GeoServerStyleNode;
import com.sldeditor.datasource.extension.filesystem.node.geoserver.GeoServerWorkspaceNode;
import com.sldeditor.extension.filesystem.geoserver.client.GeoServerClientInterface;
import com.sldeditor.tool.ToolManager;
import com.sldeditor.tool.connectionlist.GeoServerConnectionListTool;
import com.sldeditor.tool.geoserverconnection.GeoServerConnectStateInterface;
import com.sldeditor.tool.geoserverconnection.GeoServerConnectionTool;
import com.sldeditor.tool.layerstyle.GeoServerLayerUpdateInterface;
import com.sldeditor.tool.layerstyle.GeoServerLayerUpdateTool;
import com.sldeditor.tool.legend.LegendTool;
import com.sldeditor.tool.savesld.SaveSLDTool;
import com.sldeditor.tool.scale.ScaleTool;

/**
 * Class that makes GeoServer appear as a file system.
 * 
 * @author Robert Ward (SCISYS)
 */
public class GeoServerInput implements FileSystemInterface, GeoServerConnectUpdateInterface, GeoServerConnectStateInterface, GeoServerLayerUpdateInterface, GeoServerParseCompleteInterface
{
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 6659749130067227L;

    /** The GeoServer connection tool. */
    private transient GeoServerConnectionTool geoServerConnectionTool = null;

    /** The style map. */
    private Map<GeoServerConnection, Map<String, List<StyleWrapper>>> geoServerStyleMap = new HashMap<GeoServerConnection, Map<String, List<StyleWrapper>>>();

    /** The GeoServer layer map. */
    private Map<GeoServerConnection, Map<String, List<GeoServerLayer>>> geoServerLayerMap = new HashMap<GeoServerConnection, Map<String, List<GeoServerLayer>>>();

    /** The progress. */
    private transient GeoServerReadProgress progress = new GeoServerReadProgress(this, this);

    /** The logger. */
    private transient static Logger logger = Logger.getLogger(GeoServerInput.class.getName());

    /** The GeoServer root node. */
    private transient GeoServerOverallNode geoServerRootNode;

    /** The tool mgr. */
    private transient ToolSelectionInterface toolMgr = null;

    /**
     * Instantiates a new GeoServer input.
     *
     * @param toolMgr the tool mgr
     */
    public GeoServerInput(ToolSelectionInterface toolMgr)
    {
        logger.debug("Adding GeoServerInput extension");

        this.toolMgr = toolMgr;

        if(toolMgr != null)
        {
            geoServerConnectionTool = new GeoServerConnectionTool(this);
            ToolManager.getInstance().registerTool(GeoServerNode.class, geoServerConnectionTool);
            GeoServerConnectionListTool connectionListTool = new GeoServerConnectionListTool(this);
            ToolManager.getInstance().registerTool(GeoServerOverallNode.class, connectionListTool);
            ToolManager.getInstance().registerTool(GeoServerNode.class, connectionListTool);
            ToolManager.getInstance().registerTool(GeoServerStyleNode.class, new SaveSLDTool());
            ToolManager.getInstance().registerTool(GeoServerStyleNode.class, new LegendTool());

            // Scale tool
            ToolManager.getInstance().registerTool(GeoServerStyleHeadingNode.class, new ScaleTool(toolMgr.getApplication()));
            ToolManager.getInstance().registerTool(GeoServerWorkspaceNode.class, new ScaleTool(toolMgr.getApplication()));
            ToolManager.getInstance().registerTool(GeoServerStyleNode.class, new ScaleTool(toolMgr.getApplication()));

            // Layer update tool
            ToolManager.getInstance().registerTool(GeoServerLayerNode.class, new GeoServerLayerUpdateTool(this));
            ToolManager.getInstance().registerTool(GeoServerOverallNode.class, new GeoServerLayerUpdateTool(this));
            ToolManager.getInstance().registerTool(GeoServerWorkspaceNode.class, new GeoServerLayerUpdateTool(this));
            ToolManager.getInstance().registerTool(GeoServerLayerHeadingNode.class, new GeoServerLayerUpdateTool(this));
        }
    }

    /**
     * Read property file.
     */
    public void readPropertyFile()
    {
        GeoServerConnectionManager.getInstance().readPropertyFile(progress);
    }

    /**
     * Overrides the GeoServer client class.
     *
     * @param geoServerClientClass the geoServerClientClass to set
     */
    public static void overrideGeoServerClientClass(Class<?> geoServerClientClass) {
        GeoServerConnectionManager.geoServerClientClass = geoServerClientClass;
    }

    /**
     * Update property file.
     */
    private void updatePropertyFile()
    {
        GeoServerConnectionManager.getInstance().updateList();
    }

    /* (non-Javadoc)
     * @see com.sldeditor.common.filesystem.FileSystemInterface#populate(com.sldeditor.datasource.extension.filesystem.node.FSTree, javax.swing.tree.DefaultTreeModel, javax.swing.tree.DefaultMutableTreeNode)
     */
    @Override
    public void populate(FSTree tree, DefaultTreeModel model, DefaultMutableTreeNode rootNode)
    {
        progress.setTreeModel(tree, model);

        if(rootNode != null)
        {
            rootNode.add(getRootGeoServerNode());
        }

        for(GeoServerConnection connection : GeoServerConnectionManager.getInstance().getConnectionMap().keySet())
        {
            addConnectionNode(connection);
        }
    }

    /**
     * Gets the root GeoServer node.
     *
     * @return the root GeoServer node
     */
    private DefaultMutableTreeNode getRootGeoServerNode()
    {
        if(geoServerRootNode == null)
        {
            geoServerRootNode = new GeoServerOverallNode(this);
        }

        return geoServerRootNode;
    }

    /**
     * Adds the connection node.
     *
     * @param connection the connection
     */
    private void addConnectionNode(GeoServerConnection connection)
    {
        GeoServerNode node = new GeoServerNode(this, connection);
        getRootGeoServerNode().add(node);

        progress.addNewConnectionNode(connection, node);
    }

    /* (non-Javadoc)
     * @see com.sldeditor.extension.input.FileSystemInterface#treeExpanded(java.lang.Object)
     */
    @Override
    public boolean treeExpanded(Object selectedItem)
    {
        return false;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.extension.input.FileSystemInterface#rightMouseButton(java.lang.Object, java.awt.event.MouseEvent)
     */
    @Override
    public void rightMouseButton(Object selectedItem, MouseEvent e)
    {
        if(selectedItem instanceof GeoServerNode)
        {
            GeoServerNode geoServerNode = (GeoServerNode)selectedItem;

            GeoServerConnection connection = geoServerNode.getConnection();

            JPopupMenu popupMenu = new JPopupMenu();

            GeoServerClientInterface client = GeoServerConnectionManager.getInstance().getConnectionMap().get(connection);
            if(client != null)
            {
                if(client.isConnected())
                {
                    JMenuItem connectMenuItem = new JMenuItem("Disconnect");
                    connectMenuItem.addActionListener(new ActionListener()
                    {
                        public void actionPerformed(ActionEvent event)
                        {
                            disconnectFromGeoServer(client);
                        }
                    });
                    popupMenu.add(connectMenuItem);
                }
                else
                {
                    JMenuItem connectMenuItem = new JMenuItem("Connect");
                    connectMenuItem.addActionListener(new ActionListener()
                    {
                        public void actionPerformed(ActionEvent event)
                        {
                            GeoServerNode geoserver = (GeoServerNode)selectedItem;
                            GeoServerConnection connection = geoserver.getConnection();
                            connectToGeoServer(connection);
                        }
                    });
                    popupMenu.add(connectMenuItem);
                }

                if(e != null)
                {
                    popupMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        }
    }

    /**
     * Disconnect from GeoServer.
     *
     * @param client the connection
     */
    private void disconnectFromGeoServer(GeoServerClientInterface client)
    {
        if(client != null)
        {
            client.disconnect();

            GeoServerConnection connection = client.getConnection();

            if(connection != null)
            {
                geoServerStyleMap.remove(connection);
                geoServerLayerMap.remove(connection);
                progress.disconnect(connection);
            }
        }
    }

    /**
     * Connect to GeoServer.
     *
     * @param connection the connection
     */
    private void connectToGeoServer(GeoServerConnection connection)
    {
        GeoServerClientInterface client = GeoServerConnectionManager.getInstance().getConnectionMap().get(connection);

        if(client != null)
        {
            client.connect();
            if(client.isConnected())
            {
                client.retrieveData();
            }
        }
    }

    /* (non-Javadoc)
     * @see com.sldeditor.extension.input.FileSystemInterface#getSLDContents(com.sldeditor.extension.input.NodeInterface)
     */
    @Override
    public SelectedFiles getSLDContents(NodeInterface node)
    {
        if(node instanceof GeoServerStyleNode)
        {
            GeoServerStyleNode styleNode = (GeoServerStyleNode) node;

            GeoServerConnection connectionData = styleNode.getConnectionData();
            GeoServerClientInterface client = GeoServerConnectionManager.getInstance().getConnectionMap().get(connectionData);

            if(client != null)
            {
                String sldContent = client.getStyle(styleNode.getStyle());

                SLDDataInterface sldData = new SLDData(styleNode.getStyle(), sldContent);
                sldData.setConnectionData(connectionData);
                sldData.setReadOnly(false);

                List<SLDDataInterface> sldDataList = new ArrayList<SLDDataInterface>();

                sldDataList.add(sldData);

                SelectedFiles selectedFiles = new SelectedFiles();
                selectedFiles.setSldData(sldDataList);
                selectedFiles.setDataSource(false);
                selectedFiles.setConnectionData(connectionData);

                return selectedFiles;
            }
        }
        else if(node instanceof GeoServerWorkspaceNode)
        {
            GeoServerWorkspaceNode workspaceNode = (GeoServerWorkspaceNode) node;

            GeoServerConnection connectionData = workspaceNode.getConnection();
            GeoServerClientInterface client = GeoServerConnectionManager.getInstance().getConnectionMap().get(connectionData);

            List<SLDDataInterface> sldDataList = new ArrayList<SLDDataInterface>();

            if(workspaceNode.isStyle())
            {
                Map<String, List<StyleWrapper>> styleMap = getStyleMap(connectionData);

                if((client != null) && (styleMap != null))
                {
                    for(StyleWrapper style : styleMap.get(workspaceNode.getWorkspaceName()))
                    {
                        String sldContent = client.getStyle(style);

                        SLDDataInterface sldData = new SLDData(style, sldContent);
                        sldData.setConnectionData(connectionData);
                        sldData.setReadOnly(false);

                        sldDataList.add(sldData);
                    }
                }
            }

            SelectedFiles selectedFiles = new SelectedFiles();
            selectedFiles.setSldData(sldDataList);
            selectedFiles.setDataSource(false);
            selectedFiles.setConnectionData(connectionData);

            return selectedFiles;
        }
        else if(node instanceof GeoServerStyleHeadingNode)
        {
            GeoServerStyleHeadingNode styleHeadingNode = (GeoServerStyleHeadingNode) node;

            GeoServerConnection connectionData = styleHeadingNode.getConnection();
            GeoServerClientInterface client = GeoServerConnectionManager.getInstance().getConnectionMap().get(connectionData);

            List<SLDDataInterface> sldDataList = new ArrayList<SLDDataInterface>();

            Map<String, List<StyleWrapper>> styleMap = getStyleMap(connectionData);

            if((client != null) && (styleMap != null))
            {
                for(String workspaceName : styleMap.keySet())
                {
                    for(StyleWrapper style : styleMap.get(workspaceName))
                    {
                        String sldContent = client.getStyle(style);

                        SLDDataInterface sldData = new SLDData(style, sldContent);
                        sldData.setConnectionData(connectionData);
                        sldData.setReadOnly(false);

                        sldDataList.add(sldData);
                    }
                }
            }

            SelectedFiles selectedFiles = new SelectedFiles();
            selectedFiles.setSldData(sldDataList);
            selectedFiles.setDataSource(false);
            selectedFiles.setConnectionData(connectionData);

            return selectedFiles;
        }
        else if(node instanceof GeoServerNode)
        {
            GeoServerNode geoServerNode = (GeoServerNode) node;

            GeoServerConnection connectionData = geoServerNode.getConnection();

            SelectedFiles selectedFiles = new SelectedFiles();
            selectedFiles.setDataSource(false);
            selectedFiles.setConnectionData(connectionData);

            return selectedFiles;
        }

        return null;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.extension.input.FileSystemInterface#open(java.net.URL)
     */
    @Override
    public List<SLDDataInterface> open(URL url)
    {
        return null;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.extension.input.FileSystemInterface#save(java.net.URL, com.sldeditor.ui.iface.SLDDataInterface)
     */
    @Override
    public boolean save(SLDDataInterface sldData)
    {
        if(sldData != null)
        {
            GeoServerClientInterface client = GeoServerConnectionManager.getInstance().getConnectionMap().get(sldData.getConnectionData());
            if(client != null)
            {
                return client.uploadSLD(sldData.getStyle(), sldData.getSld());
            }
        }

        return false;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.extension.input.geoserver.GeoServerConnectUpdateInterface#getConnectionDetails()
     */
    @Override
    public List<GeoServerConnection> getConnectionDetails()
    {
        List<GeoServerConnection> list = new ArrayList<GeoServerConnection>();
        for(GeoServerConnection key : GeoServerConnectionManager.getInstance().getConnectionMap().keySet())
        {
            list.add(key);
        }
        return list;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.extension.input.FileSystemInterface#getNodeTypes()
     */
    @Override
    public List<NodeInterface> getNodeTypes()
    {
        List<NodeInterface> nodeTypeList = new ArrayList<NodeInterface>();

        return nodeTypeList;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.tool.GeoServerConnectStateInterface#isConnected(com.sldeditor.extension.input.geoserver.GeoServerConnection)
     */
    @Override
    public boolean isConnected(GeoServerConnection connection)
    {
        GeoServerClientInterface client = GeoServerConnectionManager.getInstance().getConnectionMap().get(connection);
        if(client != null)
        {
            return client.isConnected();
        }
        return false;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.tool.GeoServerConnectStateInterface#connect(java.util.List)
     */
    @Override
    public void connect(List<GeoServerConnection> connectionList)
    {
        if(connectionList != null)
        {
            for(GeoServerConnection connection : connectionList)
            {
                connectToGeoServer(connection);
            }
        }
    }

    /* (non-Javadoc)
     * @see com.sldeditor.tool.GeoServerConnectStateInterface#disconnect(java.util.List)
     */
    @Override
    public void disconnect(List<GeoServerConnection> connectionList)
    {
        if(connectionList != null)
        {
            for(GeoServerConnection connection : connectionList)
            {
                GeoServerClientInterface client = GeoServerConnectionManager.getInstance().getConnectionMap().get(connection);

                if(client != null)
                {
                    disconnectFromGeoServer(client);

                    if(geoServerConnectionTool != null)
                    {
                        geoServerConnectionTool.populateComplete(connection);
                    }
                }
            }
        }
    }

    /**
     * Gets the style map.
     *
     * @param connection the connection
     * @return the style map
     */
    @Override
    public Map<String, List<StyleWrapper>> getStyleMap(GeoServerConnection connection)
    {
        return this.geoServerStyleMap.get(connection);
    }

    /**
     * Update layer style.
     *
     * @param layerList the layer list
     */
    @Override
    public void updateLayerStyle(List<GeoServerLayer> layerList)
    {
        if(layerList != null)
        {
            for(GeoServerLayer layer : layerList)
            {
                GeoServerClientInterface client = GeoServerConnectionManager.getInstance().getConnectionMap().get(layer.getConnection());

                if(client != null)
                {
                    client.updateLayerStyles(layer);
                }
            }
        }
    }

    /* (non-Javadoc)
     * @see com.sldeditor.extension.input.geoserver.GeoServerParseCompleteInterface#populateComplete(com.sldeditor.extension.input.geoserver.GeoServerConnection, java.util.Map, java.util.Map)
     */
    @Override
    public void populateComplete(GeoServerConnection connection,
            Map<String, List<StyleWrapper>> styleMap,
            Map<String, List<GeoServerLayer>> layerMap)
    {
        geoServerStyleMap.put(connection, styleMap);
        geoServerLayerMap.put(connection, layerMap);

        if(geoServerConnectionTool != null)
        {
            geoServerConnectionTool.populateComplete(connection);
        }
    }

    /* (non-Javadoc)
     * @see com.sldeditor.extension.input.geoserver.GeoServerConnectUpdateInterface#addNewConnection(com.sldeditor.extension.input.geoserver.GeoServerConnection)
     */
    @Override
    public void addNewConnection(GeoServerConnection newConnectionDetails)
    {
        if(newConnectionDetails != null)
        {
            logger.debug("Add new connection : " + newConnectionDetails.getConnectionName());
            
            GeoServerConnectionManager.getInstance().addNewConnection(progress, newConnectionDetails);

            addConnectionNode(newConnectionDetails);

            progress.refreshNode(getRootGeoServerNode());
            progress.setFolder(newConnectionDetails, false);

            updatePropertyFile();
        }
    }

    /* (non-Javadoc)
     * @see com.sldeditor.extension.input.geoserver.GeoServerConnectUpdateInterface#updateConnectionDetails(com.sldeditor.extension.input.geoserver.GeoServerConnection, com.sldeditor.extension.input.geoserver.GeoServerConnection)
     */
    @Override
    public void updateConnectionDetails(GeoServerConnection originalConnectionDetails, GeoServerConnection newConnectionDetails)
    {
        if((originalConnectionDetails == null) || (newConnectionDetails == null))
        {
            return;
        }

        logger.debug("Updating connection : " + newConnectionDetails.getConnectionName());

        GeoServerClientInterface client = GeoServerConnectionManager.getInstance().getConnectionMap().get(originalConnectionDetails);
        if(client != null)
        {
            disconnectFromGeoServer(client);
        }

        progress.updateConnection(originalConnectionDetails, newConnectionDetails);

        if(toolMgr != null)
        {
            toolMgr.refreshSelection();
        }

        updatePropertyFile();
    }

    /* (non-Javadoc)
     * @see com.sldeditor.extension.input.geoserver.GeoServerConnectUpdateInterface#deleteConnections(java.util.List)
     */
    @Override
    public void deleteConnections(List<GeoServerConnection> connectionList)
    {
        if(connectionList == null)
        {
            return;
        }

        for(GeoServerConnection connection : connectionList)
        {
            logger.debug("Deleting connection : " + connection.getConnectionName());

            GeoServerClientInterface client = GeoServerConnectionManager.getInstance().getConnectionMap().get(connection);
            if(client != null)
            {
                disconnectFromGeoServer(client);

                geoServerLayerMap.remove(connection);
                geoServerStyleMap.remove(connection);
                GeoServerConnectionManager.getInstance().removeConnection(connection);

                progress.deleteConnection(connection);
            }
        }
        updatePropertyFile();
        progress.refreshNode(getRootGeoServerNode());
        progress.setFolder(null, false);
    }

    /* (non-Javadoc)
     * @see com.sldeditor.extension.input.FileSystemInterface#drop(com.sldeditor.extension.input.NodeInterface, java.util.Map)
     */
    @Override
    public boolean copyNodes(NodeInterface destinationTreeNode, Map<NodeInterface, List<SLDDataInterface>> droppedDataMap)
    {
        if(droppedDataMap == null)
        {
            return false;
        }

        if(destinationTreeNode instanceof GeoServerWorkspaceNode)
        {
            GeoServerWorkspaceNode workspaceNode = (GeoServerWorkspaceNode)destinationTreeNode;

            GeoServerClientInterface client = GeoServerConnectionManager.getInstance().getConnectionMap().get(workspaceNode.getConnection());

            if(client == null)
            {
                return false;
            }
            else
            {
                for(NodeInterface key : droppedDataMap.keySet())
                {
                    for(SLDDataInterface sldData : droppedDataMap.get(key))
                    {
                        StyleWrapper styleWrapper = sldData.getStyle();

                        removeStyleFileExtension(styleWrapper);

                        styleWrapper.setWorkspace(workspaceNode.getWorkspaceName());
                        client.uploadSLD(styleWrapper, sldData.getSld());
                    }
                }

                client.refreshWorkspace(workspaceNode.getWorkspaceName());
            }

            return true;
        }
        return false;
    }

    /**
     * Removes the style file extension.
     *
     * @param styleWrapper the style wrapper
     */
    protected void removeStyleFileExtension(StyleWrapper styleWrapper) {
        if(styleWrapper != null)
        {
            String original = styleWrapper.getStyle();
            if(original != null)
            {
                int pos = original.lastIndexOf('.');
                if(pos > 0)
                {
                    styleWrapper.setStyle(original.substring(0, pos));
                }
            }
        }
    }

    /* (non-Javadoc)
     * @see com.sldeditor.extension.input.FileSystemInterface#deleteNodes(com.sldeditor.extension.input.NodeInterface, java.util.List)
     */
    @Override
    public void deleteNodes(NodeInterface nodeToTransfer, List<SLDDataInterface> sldDataList)
    {
        if((nodeToTransfer == null) || (sldDataList == null))
        {
            return;
        }

        if(nodeToTransfer instanceof GeoServerWorkspaceNode)
        {
            Map<GeoServerClientInterface, String> connectionsToRefreshMap = new HashMap<GeoServerClientInterface, String>();

            for(SLDDataInterface sldData : sldDataList)
            {
                GeoServerClientInterface client = GeoServerConnectionManager.getInstance().getConnectionMap().get(sldData.getConnectionData());

                if(client != null)
                {
                    connectionsToRefreshMap.put(client, sldData.getStyle().getWorkspace());
                    client.deleteStyle(sldData.getStyle());
                }
            }

            // Refreshing the workspace re-populates the user interface
            for(GeoServerClientInterface client : connectionsToRefreshMap.keySet())
            {
                client.refreshWorkspace(connectionsToRefreshMap.get(client));
            }
        }
    }

    /* (non-Javadoc)
     * @see com.sldeditor.extension.input.FileSystemInterface#getDestinationText(com.sldeditor.extension.input.NodeInterface)
     */
    @Override
    public String getDestinationText(NodeInterface destinationTreeNode)
    {
        if(destinationTreeNode instanceof GeoServerWorkspaceNode)
        {
            GeoServerWorkspaceNode destinationNode = (GeoServerWorkspaceNode)destinationTreeNode;

            GeoServerConnection connection = destinationNode.getConnection();

            return String.format("GeoServer %s / Workspace %s", connection.getConnectionName(), destinationNode.getWorkspaceName());
        }

        return "Unknown";
    }

    /**
     * Sets the folder.
     *
     * @param connectionData the connection data
     * @param disableTreeSelection the disable tree selection
     */
    public void setFolder(GeoServerConnection connectionData, boolean disableTreeSelection) {
        progress.setFolder(connectionData, disableTreeSelection);
    }

}
