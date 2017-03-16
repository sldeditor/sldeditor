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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import com.sldeditor.common.data.DatabaseConnection;
import com.sldeditor.common.filesystem.FileSystemInterface;
import com.sldeditor.datasource.extension.filesystem.node.FSTree;
import com.sldeditor.datasource.extension.filesystem.node.FileSystemNodeManager;
import com.sldeditor.datasource.extension.filesystem.node.database.DatabaseFeatureClassNode;
import com.sldeditor.datasource.extension.filesystem.node.database.DatabaseNode;

/**
 * Class that handles the progress of reading databases for data sources.
 * 
 * @author Robert Ward (SCISYS)
 */
public class DatabaseReadProgress implements DatabaseReadProgressInterface
{
    /**
     * Internal class to handle the state of the operation.
     */
    class PopulateState
    {

        /** The feature class complete flag. */
        private boolean featureClassComplete = false;

        /**
         * Instantiates a new populate state.
         */
        PopulateState()
        {
            startFeatureClasses();
        }

        /**
         * Sets the styles complete.
         */
        public void setFeatureClassesComplete()
        {
            featureClassComplete = true;
        }

        /**
         * Checks if is complete.
         *
         * @return true, if is complete
         */
        public boolean isComplete()
        {
            return featureClassComplete;
        }

        /**
         * Start feature classes.
         */
        public void startFeatureClasses()
        {
            featureClassComplete = false;
        }
    }

    /** The Constant PROGRESS_NODE_TITLE. */
    private static final String PROGRESS_NODE_TITLE = "Progress";

    /** The tree model. */
    private DefaultTreeModel treeModel;

    /** The tree. */
    private FSTree tree = null;

    /** The node map. */
    private Map<DatabaseConnection, DatabaseNode> nodeMap = new HashMap<DatabaseConnection, DatabaseNode>();

    /** The populate state map. */
    private Map<DatabaseConnection, PopulateState> populateStateMap = new HashMap<DatabaseConnection, PopulateState>();

    /** The feature class map. */
    private Map<DatabaseConnection, List<String>> databaseFeatureClassMap = new HashMap<DatabaseConnection, List<String>>();

    /** The handler. */
    private FileSystemInterface handler = null;

    /** The parse complete. */
    private DatabaseParseCompleteInterface parseComplete = null;

    /**
     * Instantiates a new geo server read progress.
     *
     * @param handler the handler
     * @param parseComplete the parse complete
     */
    public DatabaseReadProgress(FileSystemInterface handler, DatabaseParseCompleteInterface parseComplete)
    {
        this.handler = handler;
        this.parseComplete = parseComplete;
    }

    /**
     * Read feature classes complete.
     *
     * @param connection the connection
     * @param featureClassList the feature class list
     */
    public void readFeatureClassesComplete(DatabaseConnection connection, List<String> featureClassList)
    {
        if(featureClassList == null)
        {
            return;
        }

        this.databaseFeatureClassMap.put(connection, featureClassList);

        // Update state
        PopulateState state = populateStateMap.get(connection);

        if(state != null)
        {
            state.setFeatureClassesComplete();
        }

        checkPopulateComplete(connection);
    }

    /**
     * Check populate complete.
     *
     * @param connection the connection
     */
    private void checkPopulateComplete(DatabaseConnection connection)
    {
        PopulateState state = populateStateMap.get(connection);

        if(state != null)
        {
            if(state.isComplete())
            {
                DatabaseNode databaseNode = nodeMap.get(connection);

                if(databaseNode != null)
                {
                    removeNode(databaseNode, PROGRESS_NODE_TITLE);

                    populateFeatureClasses(connection, databaseNode);

                    if(treeModel != null)
                    {
                        treeModel.reload(databaseNode); //this notifies the listeners and changes the GUI
                    }
                }

                parseComplete.populateComplete(connection, databaseFeatureClassMap.get(connection));
            }
        }
    }

    /**
     * Populate feature classes.
     *
     * @param connection the connection
     * @param databaseNode the database node
     */
    private void populateFeatureClasses(DatabaseConnection connection, DatabaseNode databaseNode)
    {
        List<String> featureClassList = databaseFeatureClassMap.get(connection);

        for(String featureClass : featureClassList)
        {
            DatabaseFeatureClassNode fcNode = new DatabaseFeatureClassNode(this.handler, connection, featureClass);

            // It is key to invoke this on the TreeModel, and NOT DefaultMutableTreeNode
            treeModel.insertNodeInto(fcNode, databaseNode, databaseNode.getChildCount());
        }
    }

    /**
     * Removes the node.
     *
     * @param databaseNode the database node
     * @param nodeTitleToRemove the node title to remove
     */
    public static void removeNode(DatabaseNode databaseNode, String nodeTitleToRemove)
    {
        if((databaseNode != null) && (nodeTitleToRemove != null))
        {
            for(int index = 0; index < databaseNode.getChildCount(); index ++)
            {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode)databaseNode.getChildAt(index);
                String nodeName = (String)node.getUserObject();
                if(nodeName != null)
                {
                    if(nodeName.startsWith(nodeTitleToRemove))
                    {
                        databaseNode.remove(index);
                        break;
                    }
                }
            }
        }
    }

    /* (non-Javadoc)
     * @see com.sldeditor.extension.filesystem.database.DatabaseReadProgressInterface#startPopulating(com.sldeditor.common.data.DatabaseConnection)
     */
    @Override
    public void startPopulating(DatabaseConnection connection)
    {
        PopulateState state = populateStateMap.get(connection);

        if(state != null)
        {
            state.startFeatureClasses();
        }
    }

    /**
     * Disconnect.
     *
     * @param connection the node
     */
    public void disconnect(DatabaseConnection connection)
    {
        DatabaseNode node = nodeMap.get(connection);

        node.removeAllChildren();

        if(treeModel != null)
        {
            treeModel.reload(node);
        }
    }

    /**
     * Sets the tree model.
     *
     * @param tree the tree
     * @param model the model
     */
    public void setTreeModel(FSTree tree, DefaultTreeModel model)
    {
        this.tree = tree;
        this.treeModel = model;
    }

    /**
     * Adds the new connection node.
     *
     * @param connection the connection
     * @param node the node
     */
    public void addNewConnectionNode(DatabaseConnection connection, DatabaseNode node)
    {
        nodeMap.put(connection, node);
        populateStateMap.put(connection, new PopulateState());
    }

    /**
     * Refresh node.
     *
     * @param nodeToRefresh the node to refresh
     */
    public void refreshNode(DefaultMutableTreeNode nodeToRefresh)
    {
        if(treeModel != null)
        {
            treeModel.reload(nodeToRefresh);
        }
    }

    /**
     * Delete connection.
     *
     * @param connection the connection
     */
    public void deleteConnection(DatabaseConnection connection)
    {
        DatabaseNode node = nodeMap.get(connection);

        if(treeModel != null)
        {
            treeModel.removeNodeFromParent(node);
        }

        nodeMap.remove(connection);
    }

    /**
     * Update connection.
     *
     * @param originalConnectionDetails the original connection details
     * @param newConnectionDetails the new connection details
     */
    public void updateConnection(DatabaseConnection originalConnectionDetails, DatabaseConnection newConnectionDetails)
    {
        if(newConnectionDetails != null)
        {
            DatabaseNode databaseNode = nodeMap.get(originalConnectionDetails);

            originalConnectionDetails.update(newConnectionDetails);

            if(databaseNode != null)
            {
                databaseNode.setUserObject(newConnectionDetails.getConnectionName());

                refreshNode(databaseNode);

                setFolder(newConnectionDetails.getDatabaseTypeLabel(), newConnectionDetails, false);
            }
        }
    }

    /**
     * Sets the folder.
     *
     * @param overallNodeName the overall node name
     * @param connectionData the connection data
     * @param disableTreeSelection the disable tree selection
     */
    public void setFolder(String overallNodeName, DatabaseConnection connectionData, boolean disableTreeSelection) {
        if(tree != null)
        {
            if(disableTreeSelection)
            {
                // Disable the tree selection
                tree.setIgnoreSelection(true);
            }
            tree.clearSelection();

            FileSystemNodeManager.showNodeInTree(overallNodeName, connectionData);
            if(disableTreeSelection)
            {
                // Enable the tree selection
                tree.setIgnoreSelection(false);
            }
        }
    }
}
