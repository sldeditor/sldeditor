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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import com.sldeditor.common.data.GeoServerConnection;
import com.sldeditor.common.data.GeoServerLayer;
import com.sldeditor.common.data.StyleWrapper;
import com.sldeditor.common.filesystem.FileSystemInterface;
import com.sldeditor.datasource.extension.filesystem.node.FSTree;
import com.sldeditor.datasource.extension.filesystem.node.FileSystemNodeManager;
import com.sldeditor.datasource.extension.filesystem.node.geoserver.GeoServerLayerHeadingNode;
import com.sldeditor.datasource.extension.filesystem.node.geoserver.GeoServerLayerNode;
import com.sldeditor.datasource.extension.filesystem.node.geoserver.GeoServerNode;
import com.sldeditor.datasource.extension.filesystem.node.geoserver.GeoServerStyleHeadingNode;
import com.sldeditor.datasource.extension.filesystem.node.geoserver.GeoServerStyleNode;
import com.sldeditor.datasource.extension.filesystem.node.geoserver.GeoServerWorkspaceNode;

/**
 * Class that handles the progress of reading GeoServer styles and layers.
 * 
 * @author Robert Ward (SCISYS)
 */
public class GeoServerReadProgress implements GeoServerReadProgressInterface {
    /**
     * Internal class to handle the state of the operation.
     */
    class PopulateState {

        /** The style complete. */
        private boolean styleComplete = false;

        /** The layers complete. */
        private boolean layersComplete = false;

        /**
         * Instantiates a new populate state.
         */
        PopulateState() {
            startStyles();
            startLayers();
        }

        /**
         * Sets the layers complete.
         */
        public void setLayersComplete() {
            layersComplete = true;
        }

        /**
         * Sets the styles complete.
         */
        public void setStylesComplete() {
            styleComplete = true;
        }

        /**
         * Checks if is complete.
         *
         * @return true, if is complete
         */
        public boolean isComplete() {
            return styleComplete && layersComplete;
        }

        /**
         * Start styles.
         */
        public void startStyles() {
            styleComplete = false;
        }

        /**
         * Start layers.
         */
        public void startLayers() {
            layersComplete = false;
        }
    }

    /** The Constant STYLES_NODE_TITLE. */
    private static final String STYLES_NODE_TITLE = "Styles";

    /** The Constant LAYERS_NODE_TITLE. */
    private static final String LAYERS_NODE_TITLE = "Layers";

    /** The Constant PROGRESS_NODE_TITLE. */
    private static final String PROGRESS_NODE_TITLE = "Progress";

    /** The tree model. */
    private DefaultTreeModel treeModel;

    /** The tree. */
    private FSTree tree = null;

    /** The node map. */
    private Map<GeoServerConnection, GeoServerNode> nodeMap = 
            new HashMap<GeoServerConnection, GeoServerNode>();

    /** The populate state map. */
    private Map<GeoServerConnection, PopulateState> populateStateMap = 
            new HashMap<GeoServerConnection, PopulateState>();

    /** The style map. */
    private Map<GeoServerConnection, Map<String, List<StyleWrapper>>> geoServerStyleMap = 
            new HashMap<GeoServerConnection, Map<String, List<StyleWrapper>>>();

    /** The geo server layer map. */
    private Map<GeoServerConnection, Map<String, List<GeoServerLayer>>> geoServerLayerMap =
            new HashMap<GeoServerConnection, Map<String, List<GeoServerLayer>>>();

    /** The style progress text. */
    private String styleProgressText;

    /** The layer progress text. */
    private String layerProgressText;

    /** The handler. */
    private FileSystemInterface handler = null;

    /** The parse complete. */
    private GeoServerParseCompleteInterface parseComplete = null;

    /**
     * Instantiates a new geo server read progress.
     *
     * @param handler the handler
     * @param parseComplete the parse complete
     */
    public GeoServerReadProgress(FileSystemInterface handler,
            GeoServerParseCompleteInterface parseComplete) {
        this.handler = handler;
        this.parseComplete = parseComplete;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.extension.input.geoserver.GeoServerReadProgressInterface#readStylesComplete(com.sldeditor.extension.input.geoserver.
     * GeoServerConnection, java.util.Map, boolean)
     */
    @Override
    public void readStylesComplete(GeoServerConnection connection,
            Map<String, List<StyleWrapper>> styleMap, boolean partialRefresh) {
        if (styleMap == null) {
            return;
        }

        if (partialRefresh) {
            Map<String, List<StyleWrapper>> extistingStyleMap = this.geoServerStyleMap
                    .get(connection);

            for (String workspace : styleMap.keySet()) {
                extistingStyleMap.put(workspace, styleMap.get(workspace));
            }

            GeoServerNode geoServerNode = nodeMap.get(connection);

            for (String workspace : styleMap.keySet()) {
                DefaultMutableTreeNode stylesNode = getNode(geoServerNode, STYLES_NODE_TITLE);
                GeoServerWorkspaceNode workspaceNode = (GeoServerWorkspaceNode) getNode(stylesNode,
                        workspace);
                refreshWorkspace(connection, workspaceNode);
            }
        } else {
            this.geoServerStyleMap.put(connection, styleMap);

            // Update state
            PopulateState state = populateStateMap.get(connection);

            if (state != null) {
                state.setStylesComplete();
            }

            checkPopulateComplete(connection);
        }
    }

    /**
     * Check populate complete.
     *
     * @param connection the connection
     */
    private void checkPopulateComplete(GeoServerConnection connection) {
        PopulateState state = populateStateMap.get(connection);

        if (state != null) {
            if (state.isComplete()) {
                GeoServerNode geoServerNode = nodeMap.get(connection);

                if (geoServerNode != null) {
                    removeNode(geoServerNode, PROGRESS_NODE_TITLE);

                    populateStyles(connection, geoServerNode);
                    populateLayers(connection, geoServerNode);

                    if (treeModel != null) {
                        // this notifies the listeners and changes the GUI
                        treeModel.reload(geoServerNode);
                    }
                }

                parseComplete.populateComplete(connection, geoServerStyleMap.get(connection),
                        geoServerLayerMap.get(connection));
            }
        }
    }

    /**
     * Populate styles.
     *
     * @param connection the connection
     * @param geoServerNode the geo server node
     */
    private void populateStyles(GeoServerConnection connection, GeoServerNode geoServerNode) {
        removeNode(geoServerNode, STYLES_NODE_TITLE);

        GeoServerStyleHeadingNode styleNode = new GeoServerStyleHeadingNode(this.handler,
                connection, STYLES_NODE_TITLE);
        geoServerNode.add(styleNode);

        Map<String, List<StyleWrapper>> styleMap = geoServerStyleMap.get(connection);

        for (String workspaceName : styleMap.keySet()) {
            List<StyleWrapper> styleList = styleMap.get(workspaceName);

            GeoServerWorkspaceNode workspaceNode = new GeoServerWorkspaceNode(this.handler,
                    connection, workspaceName, true);

            // It is key to invoke this on the TreeModel, and NOT DefaultMutableTreeNode
            treeModel.insertNodeInto(workspaceNode, styleNode, styleNode.getChildCount());

            for (StyleWrapper styleWrapper : styleList) {
                GeoServerStyleNode childNode = new GeoServerStyleNode(this.handler, connection,
                        styleWrapper);

                // It is key to invoke this on the TreeModel, and NOT DefaultMutableTreeNode
                treeModel.insertNodeInto(childNode, workspaceNode, workspaceNode.getChildCount());
            }
        }
    }

    /**
     * Refresh workspace.
     *
     * @param connection the connection
     * @param workspaceNode the workspace node
     */
    private void refreshWorkspace(GeoServerConnection connection,
            GeoServerWorkspaceNode workspaceNode) {
        workspaceNode.removeAllChildren();

        Map<String, List<StyleWrapper>> styleMap = geoServerStyleMap.get(connection);

        List<StyleWrapper> styleList = styleMap.get(workspaceNode.getWorkspaceName());

        for (StyleWrapper styleWrapper : styleList) {
            GeoServerStyleNode childNode = new GeoServerStyleNode(this.handler, connection,
                    styleWrapper);

            // It is key to invoke this on the TreeModel, and NOT DefaultMutableTreeNode
            treeModel.insertNodeInto(childNode, workspaceNode, workspaceNode.getChildCount());
        }

        treeModel.reload(workspaceNode);
    }

    /**
     * Populate layers.
     *
     * @param connection the connection
     * @param geoServerNode the geo server node
     */
    private void populateLayers(GeoServerConnection connection, GeoServerNode geoServerNode) {
        removeNode(geoServerNode, LAYERS_NODE_TITLE);

        GeoServerLayerHeadingNode layersNode = new GeoServerLayerHeadingNode(this.handler,
                connection, LAYERS_NODE_TITLE);
        geoServerNode.add(layersNode);

        Map<String, List<GeoServerLayer>> layerMap = geoServerLayerMap.get(connection);

        for (String workspaceName : layerMap.keySet()) {
            List<GeoServerLayer> layerList = layerMap.get(workspaceName);

            GeoServerWorkspaceNode workspaceNode = new GeoServerWorkspaceNode(this.handler,
                    connection, workspaceName, false);

            // It is key to invoke this on the TreeModel, and NOT DefaultMutableTreeNode
            treeModel.insertNodeInto(workspaceNode, layersNode, layersNode.getChildCount());

            for (GeoServerLayer layer : layerList) {
                GeoServerLayerNode childNode = new GeoServerLayerNode(this.handler, layer);

                // It is key to invoke this on the TreeModel, and NOT DefaultMutableTreeNode
                treeModel.insertNodeInto(childNode, workspaceNode, workspaceNode.getChildCount());
            }
        }
    }

    /**
     * Removes the node.
     *
     * @param geoServerNode the geo server node
     * @param nodeTitleToRemove the node title to remove
     */
    public static void removeNode(GeoServerNode geoServerNode, String nodeTitleToRemove) {
        if ((geoServerNode != null) && (nodeTitleToRemove != null)) {
            for (int index = 0; index < geoServerNode.getChildCount(); index++) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) geoServerNode
                        .getChildAt(index);
                String nodeName = (String) node.getUserObject();
                if (nodeName != null) {
                    if (nodeName.startsWith(nodeTitleToRemove)) {
                        geoServerNode.remove(index);
                        break;
                    }
                }
            }
        }
    }

    /**
     * Get the child node of a parent.
     *
     * @param parentNode the parent node
     * @param nodeTitlePrefix the node title prefix
     * @return the node
     */
    private DefaultMutableTreeNode getNode(DefaultMutableTreeNode parentNode,
            String nodeTitlePrefix) {
        if (parentNode != null) {
            for (int index = 0; index < parentNode.getChildCount(); index++) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) parentNode.getChildAt(index);
                String nodeName = (String) node.getUserObject();
                if (nodeName != null) {
                    if (nodeName.startsWith(nodeTitlePrefix)) {
                        return node;
                    }
                }
            }
        }

        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.extension.input.GeoServerInputInterface#readLayersProgress(com.sldeditor.extension.input.GeoServerConnection, int, int)
     */
    @Override
    public void readLayersProgress(GeoServerConnection connection, int count, int total) {
        // Calculate progress
        double value = ((double) count / (double) total) * 100.0;
        int iValue = (int) value;

        layerProgressText = String.format("%s : %d%%", LAYERS_NODE_TITLE, iValue);

        showProgress(connection);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.extension.input.GeoServerInputInterface#populateLayers(com.sldeditor.extension.input.GeoServerConnection, java.util.Map)
     */
    @Override
    public void readLayersComplete(GeoServerConnection connection,
            Map<String, List<GeoServerLayer>> layerMap) {
        geoServerLayerMap.put(connection, layerMap);

        // Update state
        PopulateState state = populateStateMap.get(connection);

        if (state != null) {
            state.setLayersComplete();
        }

        checkPopulateComplete(connection);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.extension.input.geoserver.GeoServerInputInterface#readStylesProgress(com.sldeditor.extension.input.geoserver.GeoServerConnection,
     * int, int)
     */
    @Override
    public void readStylesProgress(GeoServerConnection connection, int count, int total) {
        styleProgressText = String.format("%s : %d", STYLES_NODE_TITLE, count);

        showProgress(connection);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.extension.input.geoserver.GeoServerInputInterface#startPopulating()
     */
    @Override
    public void startPopulating(GeoServerConnection connection) {
        GeoServerNode geoServerNode = nodeMap.get(connection);

        removeNode(geoServerNode, STYLES_NODE_TITLE);
        removeNode(geoServerNode, LAYERS_NODE_TITLE);

        PopulateState state = populateStateMap.get(connection);

        if (state != null) {
            state.startStyles();
            state.startLayers();
        }
    }

    /**
     * Show progress.
     *
     * @param connection the connection
     * @return the default mutable tree node
     */
    private void showProgress(GeoServerConnection connection) {
        GeoServerNode geoServerNode = nodeMap.get(connection);

        if (geoServerNode == null) {
            return;
        }

        DefaultMutableTreeNode stylesNode = getNode(geoServerNode, PROGRESS_NODE_TITLE);

        if (stylesNode == null) {
            stylesNode = new DefaultMutableTreeNode(PROGRESS_NODE_TITLE);
            geoServerNode.add(stylesNode);

            if (treeModel != null) {
                TreeNode[] nodes = treeModel.getPathToRoot(geoServerNode);
                TreePath path = new TreePath(nodes);
                tree.expandPath(path);

                nodes = treeModel.getPathToRoot(stylesNode);
                path = new TreePath(nodes);
                tree.scrollPathToVisible(path);
            }
        }

        stylesNode.setUserObject(String.format("%s - %s %s", PROGRESS_NODE_TITLE, styleProgressText,
                layerProgressText));

        if (treeModel != null) {
            treeModel.reload(stylesNode);
        }
    }

    /**
     * Disconnect.
     *
     * @param connection the node
     */
    public void disconnect(GeoServerConnection connection) {
        GeoServerNode node = nodeMap.get(connection);

        removeNode(node, PROGRESS_NODE_TITLE);
        removeNode(node, STYLES_NODE_TITLE);
        removeNode(node, LAYERS_NODE_TITLE);

        if (treeModel != null) {
            treeModel.reload(node);
        }
    }

    /**
     * Sets the tree model.
     *
     * @param tree the tree
     * @param model the model
     */
    public void setTreeModel(FSTree tree, DefaultTreeModel model) {
        this.tree = tree;
        this.treeModel = model;
    }

    /**
     * Adds the new connection node.
     *
     * @param connection the connection
     * @param node the node
     */
    public void addNewConnectionNode(GeoServerConnection connection, GeoServerNode node) {
        nodeMap.put(connection, node);
        populateStateMap.put(connection, new PopulateState());
    }

    /**
     * Refresh node.
     *
     * @param nodeToRefresh the node to refresh
     */
    public void refreshNode(DefaultMutableTreeNode nodeToRefresh) {
        if (treeModel != null) {
            treeModel.reload(nodeToRefresh);
        }
    }

    /**
     * Delete connection.
     *
     * @param connection the connection
     */
    public void deleteConnection(GeoServerConnection connection) {
        GeoServerNode node = nodeMap.get(connection);

        if (treeModel != null) {
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
    public void updateConnection(GeoServerConnection originalConnectionDetails,
            GeoServerConnection newConnectionDetails) {
        if (newConnectionDetails != null) {
            GeoServerNode geoserverNode = nodeMap.get(originalConnectionDetails);

            originalConnectionDetails.update(newConnectionDetails);

            if (geoserverNode != null) {
                geoserverNode.setUserObject(newConnectionDetails.getConnectionName());

                refreshNode(geoserverNode);

                setFolder(newConnectionDetails, false);
            }
        }
    }

    /**
     * Sets the folder.
     *
     * @param connectionData the connection data
     * @param disableTreeSelection the disable tree selection
     */
    public void setFolder(GeoServerConnection connectionData, boolean disableTreeSelection) {
        if (tree != null) {
            if (disableTreeSelection) {
                // Disable the tree selection
                tree.setIgnoreSelection(true);
            }
            tree.clearSelection();

            FileSystemNodeManager.showNodeInTree(connectionData);
            if (disableTreeSelection) {
                // Enable the tree selection
                tree.setIgnoreSelection(false);
            }
        }
    }
}
