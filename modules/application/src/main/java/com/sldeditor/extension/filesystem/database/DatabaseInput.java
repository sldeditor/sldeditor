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
package com.sldeditor.extension.filesystem.database;

import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JMenu;
import javax.swing.JPopupMenu;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.apache.log4j.Logger;

import com.sldeditor.common.NodeInterface;
import com.sldeditor.common.SLDDataInterface;
import com.sldeditor.common.ToolSelectionInterface;
import com.sldeditor.common.connection.DatabaseConnectionManager;
import com.sldeditor.common.connection.GeoServerConnectionManager;
import com.sldeditor.common.console.ConsoleManager;
import com.sldeditor.common.data.DatabaseConnection;
import com.sldeditor.common.filesystem.FileSystemInterface;
import com.sldeditor.common.filesystem.SelectedFiles;
import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.datasource.extension.filesystem.DatabaseConnectUpdateInterface;
import com.sldeditor.datasource.extension.filesystem.node.FSTree;
import com.sldeditor.datasource.extension.filesystem.node.database.DatabaseNode;
import com.sldeditor.datasource.extension.filesystem.node.database.DatabaseOverallNode;
import com.sldeditor.extension.filesystem.database.client.DatabaseClientInterface;
import com.sldeditor.extension.filesystem.geoserver.client.GeoServerClientInterface;
import com.sldeditor.tool.ToolManager;
import com.sldeditor.tool.databaseconnection.DatabaseConnectStateInterface;
import com.sldeditor.tool.databaseconnection.DatabaseConnectionTool;
import com.sldeditor.tool.dbconnectionlist.DatabaseConnectionListTool;
import com.sldeditor.tool.geoserverconnection.GeoServerConnectionTool;

/**
 * Class that makes databases appear as a file system.
 * 
 * @author Robert Ward (SCISYS)
 */
public class DatabaseInput implements FileSystemInterface, DatabaseConnectUpdateInterface, DatabaseParseCompleteInterface, DatabaseConnectStateInterface {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -2014826409816444581L;

    /** The Database connection tool. */
    private transient DatabaseConnectionTool databaseConnectionTool = null;

    /** The progress. */
    private transient DatabaseReadProgress progress = new DatabaseReadProgress(this, this);

    /** The feature class map. */
    private Map<DatabaseConnection, List<String>> featureClassMap = new HashMap<DatabaseConnection, List<String>>();

    /** The logger. */
    private transient static Logger logger = Logger.getLogger(DatabaseInput.class.getName());

    /** The database root node. */
    private transient List<DatabaseOverallNode> databaseRootNodeList = null;

    /** The tool mgr. */
    private transient ToolSelectionInterface toolMgr = null;

    /**
     * Instantiates a new database input.
     *
     * @param toolMgr the tool mgr
     */
    public DatabaseInput(ToolSelectionInterface toolMgr) {
        logger.debug("Adding database extension");

        this.toolMgr = toolMgr;

        if (toolMgr != null) {
            databaseConnectionTool = new DatabaseConnectionTool(this);
            ToolManager.getInstance().registerTool(DatabaseNode.class, databaseConnectionTool);
            DatabaseConnectionListTool connectionListTool = new DatabaseConnectionListTool(this);
            ToolManager.getInstance().registerTool(DatabaseOverallNode.class, connectionListTool);
            ToolManager.getInstance().registerTool(DatabaseNode.class, connectionListTool);
        }
    }

    /**
     * Read property file.
     */
    public void readPropertyFile() {
        DatabaseConnectionManager.getInstance().readPropertyFile(progress);
    }

    /**
     * Update property file.
     */
    private void updatePropertyFile() {
        DatabaseConnectionManager.getInstance().updateList();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.common.filesystem.FileSystemInterface#populate(com.sldeditor.datasource.extension.filesystem.node.FSTree,
     * javax.swing.tree.DefaultTreeModel, javax.swing.tree.DefaultMutableTreeNode)
     */
    @Override
    public void populate(FSTree tree, DefaultTreeModel model, DefaultMutableTreeNode rootNode) {
        progress.setTreeModel(tree, model);

        if (rootNode != null) {
            List<DatabaseOverallNode> nodeList = getRootDatabaseNodes();

            for (DatabaseOverallNode node : nodeList) {
                rootNode.add(node);
            }
        }

        for (DatabaseConnection connection : DatabaseConnectionManager.getInstance()
                .getConnectionMap().keySet()) {
            addConnectionNode(connection);
        }
    }

    /**
     * Gets the root database nodes.
     *
     * @return the list of root database nodes
     */
    private List<DatabaseOverallNode> getRootDatabaseNodes() {
        if (databaseRootNodeList == null) {
            databaseRootNodeList = new ArrayList<DatabaseOverallNode>();
            databaseRootNodeList.add(
                    new DatabaseOverallNode(this, "Postgres", "ui/filesystemicons/postgresql.png"));
        }

        return databaseRootNodeList;
    }

    /**
     * Adds the connection node.
     *
     * @param connection the connection
     */
    private void addConnectionNode(DatabaseConnection connection) {
        DatabaseNode node = new DatabaseNode(this, connection);
        getRootDatabaseNodes().get(0).add(node);

        progress.addNewConnectionNode(connection, node);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.extension.input.FileSystemInterface#treeExpanded(java.lang.Object)
     */
    @Override
    public boolean treeExpanded(Object selectedItem) {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.common.filesystem.FileSystemInterface#rightMouseButton(javax.swing.JPopupMenu, java.lang.Object, java.awt.event.MouseEvent)
     */
    @Override
    public void rightMouseButton(JPopupMenu popupMenu, Object selectedItem, MouseEvent e) {
//        if (selectedItem instanceof GeoServerNode) {
//            GeoServerNode geoServerNode = (GeoServerNode) selectedItem;
//
//            GeoServerConnection connection = geoServerNode.getConnection();
//
//            GeoServerClientInterface client = GeoServerConnectionManager.getInstance()
//                    .getConnectionMap().get(connection);
//            if (client != null) {
//                if (client.isConnected()) {
//                    JMenuItem connectMenuItem = new JMenuItem(Localisation
//                            .getString(DatabaseInput.class, "GeoServerInput.disconnect"));
//                    connectMenuItem.addActionListener(new ActionListener() {
//                        public void actionPerformed(ActionEvent event) {
//                            disconnectFromGeoServer(client);
//                        }
//                    });
//                    popupMenu.add(connectMenuItem);
//                } else {
//                    JMenuItem connectMenuItem = new JMenuItem(
//                            Localisation.getString(DatabaseInput.class, "GeoServerInput.connect"));
//                    connectMenuItem.addActionListener(new ActionListener() {
//                        public void actionPerformed(ActionEvent event) {
//                            GeoServerNode geoserver = (GeoServerNode) selectedItem;
//                            GeoServerConnection connection = geoserver.getConnection();
//                            connectToGeoServer(connection);
//                        }
//                    });
//                    popupMenu.add(connectMenuItem);
//                }
//            }
//        } else if (selectedItem instanceof FileTreeNode) {
//            FileTreeNode fileNode = (FileTreeNode) selectedItem;
//            if (ExternalFilenames.getFileExtension(fileNode.getFile().getAbsolutePath())
//                    .compareToIgnoreCase(SLD_FILE_EXTENSION) == 0) {
//                JMenu uploadToGeoServerMenu = new JMenu(Localisation.getString(DatabaseInput.class,
//                        "GeoServerInput.uploadToGeoServer"));
//                populateGeoServerConnections(uploadToGeoServerMenu);
//                popupMenu.add(uploadToGeoServerMenu);
//            }
//        }
    }

    /**
     * Populate geo server connections.
     *
     * @param uploadToGeoServerMenu the upload to geo server menu
     */
    private void populateGeoServerConnections(JMenu uploadToGeoServerMenu) {
//        if (uploadToGeoServerMenu != null) {
//            Map<GeoServerConnection, GeoServerClientInterface> connectionMap = GeoServerConnectionManager
//                    .getInstance().getConnectionMap();
//
//            if (connectionMap.isEmpty()) {
//                JMenuItem noGeoServerMenuItem = new JMenuItem(Localisation
//                        .getString(DatabaseInput.class, "GeoServerInput.noGeoServerConnections"));
//
//                uploadToGeoServerMenu.add(noGeoServerMenuItem);
//            } else {
//                for (GeoServerConnection connection : connectionMap.keySet()) {
//                    JMenu geoServer = new JMenu(connection.getConnectionName());
//
//                    uploadToGeoServerMenu.add(geoServer);
//
//                    DatabaseClientInterface client = connectionMap.get(connection);
//                    if (client.isConnected()) {
//                        populateWorkspaceList(client, geoServer);
//                    } else {
//                        JMenuItem connectMenuItem = new JMenuItem(Localisation
//                                .getString(DatabaseInput.class, "GeoServerInput.connect"));
//                        connectMenuItem.addActionListener(new ActionListener() {
//                            public void actionPerformed(ActionEvent event) {
//                                connectMenuItem.setEnabled(false);
//                                connectToDatabase(connection);
//                                connectMenuItem.setEnabled(true);
//                            }
//                        });
//
//                        geoServer.add(connectMenuItem);
//                    }
//                }
//            }
//        }
    }

    /**
     * Disconnect from database.
     *
     * @param client the connection
     */
    private void disconnectFromDatabase(DatabaseClientInterface client) {
        if (client != null) {
            client.disconnect();

            DatabaseConnection connection = client.getConnection();

            if (connection != null) {
                featureClassMap.remove(connection);
                progress.disconnect(connection);
            }
        }
    }

    /**
     * Connect to database.
     *
     * @param connection the connection
     * @return true, if successful
     */
    private boolean connectToDatabase(DatabaseConnection connection) {
        boolean isConnected = false;

        DatabaseClientInterface client = DatabaseConnectionManager.getInstance()
                .getConnectionMap().get(connection);

        if (client != null) {
            client.connect();

            if (client.isConnected()) {
                String message = String.format("%s : %s",
                        Localisation.getString(GeoServerConnectionTool.class,
                                "GeoServerConnectionTool.connected"),
                        connection.getConnectionName());
                ConsoleManager.getInstance().information(GeoServerConnectionTool.class, message);
                client.retrieveData();
                isConnected = true;
            } else {
                String errorMessage = String.format("%s : %s",
                        Localisation.getString(GeoServerConnectionTool.class,
                                "GeoServerConnectionTool.failedToConnect"),
                        connection.getConnectionName());
                ConsoleManager.getInstance().error(GeoServerConnectionTool.class, errorMessage);
            }
        }

        return isConnected;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.extension.input.FileSystemInterface#getSLDContents(com.sldeditor.extension.input.NodeInterface)
     */
    @Override
    public SelectedFiles getSLDContents(NodeInterface node) {
//        if (node instanceof GeoServerStyleNode) {
//            GeoServerStyleNode styleNode = (GeoServerStyleNode) node;
//
//            GeoServerConnection connectionData = styleNode.getConnectionData();
//            GeoServerClientInterface client = GeoServerConnectionManager.getInstance()
//                    .getConnectionMap().get(connectionData);
//
//            if (client != null) {
//                String sldContent = client.getStyle(styleNode.getStyle());
//
//                SLDDataInterface sldData = new SLDData(styleNode.getStyle(), sldContent);
//                sldData.setConnectionData(connectionData);
//                sldData.setReadOnly(false);
//
//                List<SLDDataInterface> sldDataList = new ArrayList<SLDDataInterface>();
//
//                sldDataList.add(sldData);
//
//                SelectedFiles selectedFiles = new SelectedFiles();
//                selectedFiles.setSldData(sldDataList);
//                selectedFiles.setDataSource(false);
//                selectedFiles.setConnectionData(connectionData);
//
//                return selectedFiles;
//            }
//        } else if (node instanceof GeoServerWorkspaceNode) {
//            GeoServerWorkspaceNode workspaceNode = (GeoServerWorkspaceNode) node;
//
//            GeoServerConnection connectionData = workspaceNode.getConnection();
//            GeoServerClientInterface client = GeoServerConnectionManager.getInstance()
//                    .getConnectionMap().get(connectionData);
//
//            List<SLDDataInterface> sldDataList = new ArrayList<SLDDataInterface>();
//
//            if (workspaceNode.isStyle()) {
//                Map<String, List<StyleWrapper>> styleMap = getStyleMap(connectionData);
//
//                if ((client != null) && (styleMap != null)) {
//                    for (StyleWrapper style : styleMap.get(workspaceNode.getWorkspaceName())) {
//                        String sldContent = client.getStyle(style);
//
//                        SLDDataInterface sldData = new SLDData(style, sldContent);
//                        sldData.setConnectionData(connectionData);
//                        sldData.setReadOnly(false);
//
//                        sldDataList.add(sldData);
//                    }
//                }
//            }
//
//            SelectedFiles selectedFiles = new SelectedFiles();
//            selectedFiles.setSldData(sldDataList);
//            selectedFiles.setDataSource(false);
//            selectedFiles.setConnectionData(connectionData);
//
//            return selectedFiles;
//        } else if (node instanceof GeoServerStyleHeadingNode) {
//            GeoServerStyleHeadingNode styleHeadingNode = (GeoServerStyleHeadingNode) node;
//
//            GeoServerConnection connectionData = styleHeadingNode.getConnection();
//            GeoServerClientInterface client = GeoServerConnectionManager.getInstance()
//                    .getConnectionMap().get(connectionData);
//
//            List<SLDDataInterface> sldDataList = new ArrayList<SLDDataInterface>();
//
//            Map<String, List<StyleWrapper>> styleMap = getStyleMap(connectionData);
//
//            if ((client != null) && (styleMap != null)) {
//                for (String workspaceName : styleMap.keySet()) {
//                    for (StyleWrapper style : styleMap.get(workspaceName)) {
//                        String sldContent = client.getStyle(style);
//
//                        SLDDataInterface sldData = new SLDData(style, sldContent);
//                        sldData.setConnectionData(connectionData);
//                        sldData.setReadOnly(false);
//
//                        sldDataList.add(sldData);
//                    }
//                }
//            }
//
//            SelectedFiles selectedFiles = new SelectedFiles();
//            selectedFiles.setSldData(sldDataList);
//            selectedFiles.setDataSource(false);
//            selectedFiles.setConnectionData(connectionData);
//
//            return selectedFiles;
//        } else if (node instanceof GeoServerNode) {
//            GeoServerNode geoServerNode = (GeoServerNode) node;
//
//            GeoServerConnection connectionData = geoServerNode.getConnection();
//
//            SelectedFiles selectedFiles = new SelectedFiles();
//            selectedFiles.setDataSource(false);
//            selectedFiles.setConnectionData(connectionData);
//
//            return selectedFiles;
//        }

        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.extension.input.FileSystemInterface#open(java.net.URL)
     */
    @Override
    public List<SLDDataInterface> open(URL url) {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.extension.input.FileSystemInterface#save(java.net.URL, com.sldeditor.ui.iface.SLDDataInterface)
     */
    @Override
    public boolean save(SLDDataInterface sldData) {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.extension.input.geoserver.GeoServerConnectUpdateInterface#getConnectionDetails()
     */
    @Override
    public List<DatabaseConnection> getConnectionDetails() {
        List<DatabaseConnection> list = new ArrayList<DatabaseConnection>();
        for (DatabaseConnection key : DatabaseConnectionManager.getInstance().getConnectionMap()
                .keySet()) {
            list.add(key);
        }
        return list;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.extension.input.FileSystemInterface#getNodeTypes()
     */
    @Override
    public List<NodeInterface> getNodeTypes() {
        List<NodeInterface> nodeTypeList = new ArrayList<NodeInterface>();

        return nodeTypeList;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.tool.GeoServerConnectStateInterface#isConnected(com.sldeditor.extension.input.geoserver.GeoServerConnection)
     */
    @Override
    public boolean isConnected(DatabaseConnection connection) {
        GeoServerClientInterface client = GeoServerConnectionManager.getInstance()
                .getConnectionMap().get(connection);
        if (client != null) {
            return client.isConnected();
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.tool.GeoServerConnectStateInterface#connect(java.util.List)
     */
    @Override
    public void connect(List<DatabaseConnection> connectionList) {
        if (connectionList != null) {
            for (DatabaseConnection connection : connectionList) {
                connectToDatabase(connection);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.tool.DatabaseConnectStateInterface#disconnect(java.util.List)
     */
    @Override
    public void disconnect(List<DatabaseConnection> connectionList) {
        if (connectionList != null) {
            for (DatabaseConnection connection : connectionList) {
                DatabaseClientInterface client = DatabaseConnectionManager.getInstance()
                        .getConnectionMap().get(connection);

                if (client != null) {
                    disconnectFromDatabase(client);

                    if (databaseConnectionTool != null) {
                        databaseConnectionTool.populateComplete(connection);
                    }
                }
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.extension.input.geoserver.GeoServerParseCompleteInterface#populateComplete(com.sldeditor.extension.input.geoserver.
     * GeoServerConnection, java.util.Map, java.util.Map)
     */
    @Override
    public void populateComplete(DatabaseConnection connection, List<String> featureClassList) {
        featureClassMap.put(connection, featureClassList);

        if (databaseConnectionTool != null) {
            databaseConnectionTool.populateComplete(connection);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.extension.input.geoserver.GeoServerConnectUpdateInterface#addNewConnection(com.sldeditor.extension.input.geoserver.
     * GeoServerConnection)
     */
    @Override
    public void addNewConnection(DatabaseConnection newConnectionDetails) {
        if (newConnectionDetails != null) {
            logger.debug("Add new connection : " + newConnectionDetails.getConnectionName());

            DatabaseConnectionManager.getInstance().addNewConnection(progress,
                    newConnectionDetails);

            addConnectionNode(newConnectionDetails);

            progress.refreshNode(getRootDatabaseNodes().get(0));
            progress.setFolder(newConnectionDetails, false);

            updatePropertyFile();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.extension.input.geoserver.GeoServerConnectUpdateInterface#updateConnectionDetails(com.sldeditor.extension.input.geoserver.
     * GeoServerConnection, com.sldeditor.extension.input.geoserver.GeoServerConnection)
     */
    @Override
    public void updateConnectionDetails(DatabaseConnection originalConnectionDetails,
            DatabaseConnection newConnectionDetails) {
        if ((originalConnectionDetails == null) || (newConnectionDetails == null)) {
            return;
        }

        logger.debug("Updating connection : " + newConnectionDetails.getConnectionName());

        DatabaseClientInterface client = DatabaseConnectionManager.getInstance()
                .getConnectionMap().get(originalConnectionDetails);
        if (client != null) {
            disconnectFromDatabase(client);
        }

        progress.updateConnection(originalConnectionDetails, newConnectionDetails);

        if (toolMgr != null) {
            toolMgr.refreshSelection();
        }

        updatePropertyFile();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.extension.input.geoserver.GeoServerConnectUpdateInterface#deleteConnections(java.util.List)
     */
    @Override
    public void deleteConnections(List<DatabaseConnection> connectionList) {
        if (connectionList == null) {
            return;
        }

        for (DatabaseConnection connection : connectionList) {
            logger.debug("Deleting connection : " + connection.getConnectionName());

            DatabaseClientInterface client = DatabaseConnectionManager.getInstance()
                    .getConnectionMap().get(connection);
            if (client != null) {
                disconnectFromDatabase(client);

                featureClassMap.remove(connection);
                DatabaseConnectionManager.getInstance().removeConnection(connection);

                progress.deleteConnection(connection);
            }
        }
        updatePropertyFile();
        progress.refreshNode(getRootDatabaseNodes().get(0));
        progress.setFolder(null, false);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.extension.input.FileSystemInterface#drop(com.sldeditor.extension.input.NodeInterface, java.util.Map)
     */
    @Override
    public boolean copyNodes(NodeInterface destinationTreeNode,
            Map<NodeInterface, List<SLDDataInterface>> droppedDataMap) {

        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.extension.input.FileSystemInterface#deleteNodes(com.sldeditor.extension.input.NodeInterface, java.util.List)
     */
    @Override
    public void deleteNodes(NodeInterface nodeToTransfer, List<SLDDataInterface> sldDataList) {
        // No deletion allowed
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.extension.input.FileSystemInterface#getDestinationText(com.sldeditor.extension.input.NodeInterface)
     */
    @Override
    public String getDestinationText(NodeInterface destinationTreeNode) {

        return "Unknown";
    }

    /**
     * Sets the folder.
     *
     * @param connectionData the connection data
     * @param disableTreeSelection the disable tree selection
     */
    public void setFolder(DatabaseConnection connectionData, boolean disableTreeSelection) {
        progress.setFolder(connectionData, disableTreeSelection);
    }

}
