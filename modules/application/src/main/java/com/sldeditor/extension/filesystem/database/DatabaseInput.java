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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPopupMenu;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.apache.log4j.Logger;

import com.sldeditor.common.NodeInterface;
import com.sldeditor.common.SLDDataInterface;
import com.sldeditor.common.ToolSelectionInterface;
import com.sldeditor.common.connection.DatabaseConnectionManager;
import com.sldeditor.common.console.ConsoleManager;
import com.sldeditor.common.data.DatabaseConnection;
import com.sldeditor.common.filesystem.FileSystemInterface;
import com.sldeditor.common.filesystem.SelectedFiles;
import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.datasource.extension.filesystem.DatabaseConnectUpdateInterface;
import com.sldeditor.datasource.extension.filesystem.node.FSTree;
import com.sldeditor.datasource.extension.filesystem.node.database.DatabaseFeatureClassNode;
import com.sldeditor.datasource.extension.filesystem.node.database.DatabaseNode;
import com.sldeditor.datasource.extension.filesystem.node.database.DatabaseOverallNode;
import com.sldeditor.datasource.extension.filesystem.node.file.FileTreeNode;
import com.sldeditor.extension.filesystem.database.client.DatabaseClientInterface;
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
public class DatabaseInput implements FileSystemInterface, DatabaseConnectUpdateInterface,
        DatabaseParseCompleteInterface, DatabaseConnectStateInterface {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -2014826409816444581L;

    /** The Database connection tool. */
    private transient DatabaseConnectionTool databaseConnectionTool = null;

    /** The progress. */
    private transient DatabaseReadProgress progress = new DatabaseReadProgress(this, this);

    /** The feature class map. */
    private Map<DatabaseConnection, List<String>> featureClassMap =
            new HashMap<DatabaseConnection, List<String>>();

    /** The logger. */
    private static transient Logger logger = Logger.getLogger(DatabaseInput.class.getName());

    /** The database root node map. */
    private transient Map<String, DatabaseOverallNode> databaseRootNodeMap = null;

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
            ToolManager.getInstance().registerTool(FileTreeNode.class, databaseConnectionTool);
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

            for (String key : getRootDatabaseNodes().keySet()) {
                DatabaseOverallNode node = getRootDatabaseNodes().get(key);

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
    private Map<String, DatabaseOverallNode> getRootDatabaseNodes() {
        if (databaseRootNodeMap == null) {
            databaseRootNodeMap = new LinkedHashMap<String, DatabaseOverallNode>();

            List<DatabaseOverallNode> list = new ArrayList<DatabaseOverallNode>();

            list.add(new DatabaseOverallNode(this, "PostGIS", "ui/filesystemicons/postgresql.png"));

            list.add(new DatabaseOverallNode(this, "GeoPackage",
                    "ui/filesystemicons/geopackage.png"));

            list.add(new DatabaseOverallNode(this, "SpatiaLite",
                    "ui/filesystemicons/spatialite.png"));

            list.add(new DatabaseOverallNode(this, "DB2", "ui/filesystemicons/db2.png"));

            list.add(new DatabaseOverallNode(this, "H2", "ui/filesystemicons/h2.png"));

            list.add(new DatabaseOverallNode(this, "MySQL", "ui/filesystemicons/mysql.png"));

            list.add(new DatabaseOverallNode(this, "Oracle", "ui/filesystemicons/oracle.png"));

            list.add(new DatabaseOverallNode(this, "Microsoft SQL Server (JTDS Driver) (JNDI)",
                    "ui/filesystemicons/sqlserver.png"));

            list.add(new DatabaseOverallNode(this, "Teradata", "ui/filesystemicons/teradata.png"));

            for (DatabaseOverallNode node : list) {
                databaseRootNodeMap.put(node.toString(), node);
            }
        }
        return databaseRootNodeMap;
    }

    /**
     * Adds the connection node.
     *
     * @param connection the connection
     */
    private void addConnectionNode(DatabaseConnection connection) {
        DatabaseNode node = new DatabaseNode(this, connection);
        getRootDatabaseNode(connection).add(node);

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
        // Do nothings
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

        DatabaseConnection dbConnection = DatabaseConnectionManager.getInstance()
                .getMatchingConnection(connection);
        if (dbConnection == null) {
            addNewConnection(connection);
            dbConnection = connection;
        }

        DatabaseClientInterface client = DatabaseConnectionManager.getInstance().getConnectionMap()
                .get(dbConnection);

        if (client != null) {
            client.connect();

            if (client.isConnected()) {
                String message = String.format("%s : %s",
                        Localisation.getString(GeoServerConnectionTool.class,
                                "GeoServerConnectionTool.connected"),
                        dbConnection.getConnectionName());
                ConsoleManager.getInstance().information(GeoServerConnectionTool.class, message);
                client.retrieveData();
                isConnected = true;
            } else {
                String errorMessage = String.format("%s : %s",
                        Localisation.getString(GeoServerConnectionTool.class,
                                "GeoServerConnectionTool.failedToConnect"),
                        dbConnection.getConnectionName());
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

        SelectedFiles selectedFiles = new SelectedFiles();
        if (node instanceof DatabaseFeatureClassNode) {
            List<SLDDataInterface> sldContentList = new ArrayList<SLDDataInterface>();

            selectedFiles.setSldData(sldContentList);
            selectedFiles.setDataSource(true);
        }

        return selectedFiles;
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
     * @see com.sldeditor.datasource.extension.filesystem.DatabaseConnectUpdateInterface#getConnectionDetails()
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
     * @see com.sldeditor.tool.databaseconnection.DatabaseConnectStateInterface#isConnected(com.sldeditor.common.data.DatabaseConnection)
     */
    @Override
    public boolean isConnected(DatabaseConnection connection) {
        DatabaseClientInterface client = DatabaseConnectionManager.getInstance().getConnectionMap()
                .get(connection);
        if (client != null) {
            return client.isConnected();
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.tool.databaseconnection.DatabaseConnectStateInterface#connect(java.util.List)
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
     * @see com.sldeditor.extension.filesystem.database.DatabaseParseCompleteInterface#populateComplete(com.sldeditor.common.data.DatabaseConnection,
     * java.util.List)
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
     * @see com.sldeditor.datasource.extension.filesystem.DatabaseConnectUpdateInterface#addNewConnection(com.sldeditor.common.data.DatabaseConnection)
     */
    @Override
    public void addNewConnection(DatabaseConnection newConnectionDetails) {
        if (newConnectionDetails != null) {
            logger.debug("Add new connection : " + newConnectionDetails.getConnectionName());

            DatabaseConnectionManager.getInstance().addNewConnection(progress,
                    newConnectionDetails);

            addConnectionNode(newConnectionDetails);

            progress.refreshNode(getRootDatabaseNode(newConnectionDetails));
            progress.setFolder(newConnectionDetails.getDatabaseTypeLabel(), newConnectionDetails,
                    false);

            updatePropertyFile();
        }
    }

    /**
     * Gets the root database node.
     *
     * @param newConnectionDetails the new connection details
     * @return the root database node
     */
    private DefaultMutableTreeNode getRootDatabaseNode(DatabaseConnection newConnectionDetails) {
        return getRootDatabaseNodes().get(newConnectionDetails.getDatabaseTypeLabel());
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.datasource.extension.filesystem.DatabaseConnectUpdateInterface#updateConnectionDetails(com.sldeditor.common.data.
     * DatabaseConnection, com.sldeditor.common.data.DatabaseConnection)
     */
    @Override
    public void updateConnectionDetails(DatabaseConnection originalConnectionDetails,
            DatabaseConnection newConnectionDetails) {
        if ((originalConnectionDetails == null) || (newConnectionDetails == null)) {
            return;
        }

        logger.debug("Updating connection : " + newConnectionDetails.getConnectionName());

        DatabaseClientInterface client = DatabaseConnectionManager.getInstance().getConnectionMap()
                .remove(originalConnectionDetails);
        if (client != null) {
            disconnectFromDatabase(client);
        }

        progress.updateConnection(originalConnectionDetails, newConnectionDetails);

        DatabaseConnectionManager.getInstance().addNewConnection(progress, newConnectionDetails);
        if (toolMgr != null) {
            toolMgr.refreshSelection();
        }

        updatePropertyFile();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.datasource.extension.filesystem.DatabaseConnectUpdateInterface#deleteConnections(java.util.List)
     */
    @Override
    public void deleteConnections(List<DatabaseConnection> connectionList) {
        if (connectionList == null) {
            return;
        }

        List<DefaultMutableTreeNode> overallNodesToRefresh =
                new ArrayList<DefaultMutableTreeNode>();
        String overallNodeName = null;
        for (DatabaseConnection connection : connectionList) {
            logger.debug("Deleting connection : " + connection.getConnectionName());

            DatabaseClientInterface client = DatabaseConnectionManager.getInstance()
                    .getConnectionMap().get(connection);
            if (client != null) {
                disconnectFromDatabase(client);

                featureClassMap.remove(connection);
                DatabaseConnectionManager.getInstance().removeConnection(connection);

                progress.deleteConnection(connection);
                DefaultMutableTreeNode overallNode = getRootDatabaseNode(connection);
                if (!overallNodesToRefresh.contains(overallNode)) {
                    overallNodesToRefresh.add(overallNode);
                }

                overallNodeName = connection.getDatabaseTypeLabel();
            }
        }
        updatePropertyFile();
        for (DefaultMutableTreeNode overallNode : overallNodesToRefresh) {
            progress.refreshNode(overallNode);
        }
        progress.setFolder(overallNodeName, null, false);
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
        String overallNodeName = getRootDatabaseNodes().keySet().iterator().next();

        if (connectionData != null) {
            overallNodeName = connectionData.getDatabaseTypeLabel();
        }
        progress.setFolder(overallNodeName, connectionData, disableTreeSelection);
    }

}
