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

package com.sldeditor.datasource.extension.filesystem.dataflavour;

import com.sldeditor.common.NodeInterface;
import com.sldeditor.common.SLDDataInterface;
import com.sldeditor.common.console.ConsoleManager;
import com.sldeditor.common.filesystem.SelectedFiles;
import com.sldeditor.datasource.extension.filesystem.node.file.FileTreeNode;
import com.sldeditor.datasource.extension.filesystem.node.geoserver.GeoServerLayerHeadingNode;
import com.sldeditor.datasource.extension.filesystem.node.geoserver.GeoServerStyleHeadingNode;
import com.sldeditor.datasource.extension.filesystem.node.geoserver.GeoServerWorkspaceNode;
import java.awt.datatransfer.DataFlavor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.TransferHandler;
import javax.swing.tree.DefaultTreeModel;

/**
 * Class that contains the supported data flavours, which allow the dragging and dropping of nodes
 * within the file system tree.
 *
 * @author Robert Ward (SCISYS)
 */
public class DataFlavourManager {

    /** The Constant GEOSERVER_WORKSPACE_DATAITEM_FLAVOUR. */
    public static final SLDDataFlavour GEOSERVER_WORKSPACE_DATAITEM_FLAVOUR =
            new SLDDataFlavour(
                    GeoServerWorkspaceNode.class, GeoServerWorkspaceNode.class.getName());

    /** The Constant GEOSERVER_HEADING_STYLE_FLAVOUR. */
    public static final SLDDataFlavour GEOSERVER_HEADING_STYLE_FLAVOUR =
            new SLDDataFlavour(
                    GeoServerStyleHeadingNode.class, GeoServerStyleHeadingNode.class.getName());

    /** The Constant GEOSERVER_HEADING_LAYER_FLAVOUR. */
    public static final SLDDataFlavour GEOSERVER_HEADING_LAYER_FLAVOUR =
            new SLDDataFlavour(
                    GeoServerLayerHeadingNode.class, GeoServerLayerHeadingNode.class.getName());

    /** The Constant FOLDER_DATAITEM_FLAVOR. */
    public static final SLDDataFlavour FOLDER_DATAITEM_FLAVOR =
            new SLDDataFlavour(FileTreeNode.class, "Folder");

    /** The data flavour array. */
    private static DataFlavor[] dataFlavourArray = null;

    /** The supported map. */
    private static Map<DataFlavor, List<DataFlavor>> supportedMap =
            new HashMap<DataFlavor, List<DataFlavor>>();

    /** Private default constructor. */
    private DataFlavourManager() {
        // Default constructor
    }

    /**
     * Gets the data flavour array.
     *
     * @return the data flavour array
     */
    public static DataFlavor[] getDataFlavourArray() {
        if (supportedMap.isEmpty()) {
            populateSupportedMap();
        }

        return dataFlavourArray;
    }

    /**
     * Checks if is supported.
     *
     * @param source the source
     * @param destination the destination
     * @return true, if is supported
     */
    public static boolean isSupported(DataFlavor source, DataFlavor destination) {
        if (supportedMap.isEmpty()) {
            populateSupportedMap();
        }

        List<DataFlavor> list = supportedMap.get(destination);

        if (list != null) {
            return list.contains(source);
        }

        return false;
    }

    /** Populate supported map. */
    private static void populateSupportedMap() {
        List<String> classNameList = new ArrayList<String>();
        classNameList.add(BuiltInDataFlavour.class.getName());

        List<DataFlavor> dataFlavourList = new ArrayList<DataFlavor>();
        List<DataFlavor> destinationFolderList = new ArrayList<DataFlavor>();
        List<DataFlavor> destinationGeoServerList = new ArrayList<DataFlavor>();

        for (String className : classNameList) {
            try {
                DataFlavourInterface obj =
                        (DataFlavourInterface) Class.forName(className).newInstance();
                obj.populate(dataFlavourList, destinationFolderList, destinationGeoServerList);
            } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
                ConsoleManager.getInstance().exception(DataFlavourManager.class, e);
            }
        }

        dataFlavourArray = convertToArray(dataFlavourList);
        populate(FOLDER_DATAITEM_FLAVOR, convertToArray(destinationFolderList));
        populate(GEOSERVER_WORKSPACE_DATAITEM_FLAVOUR, convertToArray(destinationGeoServerList));
    }

    /**
     * Convert DataFlavor list to array.
     *
     * @param dataFlavourList the data flavour list
     * @return the data flavor[]
     */
    private static DataFlavor[] convertToArray(List<DataFlavor> dataFlavourList) {
        DataFlavor[] array = new DataFlavor[dataFlavourList.size()];

        dataFlavourList.toArray(array);

        return array;
    }

    /**
     * Populate.
     *
     * @param destination the destination
     * @param supportedList the supported list
     */
    private static void populate(DataFlavor destination, DataFlavor[] supportedList) {
        List<DataFlavor> list = new ArrayList<DataFlavor>();

        for (DataFlavor flavour : supportedList) {
            list.add(flavour);
        }
        supportedMap.put(destination, list);
    }

    /**
     * Copy.
     *
     * @param destinationTreeNode the destination tree node
     * @param transferredData the transferred data
     * @return true, if successful
     */
    public static boolean copy(NodeInterface destinationTreeNode, TransferredData transferredData) {
        if ((destinationTreeNode == null) || (transferredData == null)) {
            return false;
        }

        Map<NodeInterface, List<SLDDataInterface>> map =
                new LinkedHashMap<NodeInterface, List<SLDDataInterface>>();

        for (int index = 0; index < transferredData.getDataListSize(); index++) {
            NodeInterface nodeToTransfer =
                    (NodeInterface) transferredData.getTreePath(index).getLastPathComponent();

            SelectedFiles selectedFiles =
                    nodeToTransfer.getHandler().getSLDContents(nodeToTransfer);

            map.put(nodeToTransfer, selectedFiles.getSldData());
        }

        return destinationTreeNode.getHandler().copyNodes(destinationTreeNode, map);
    }

    /**
     * Delete nodes.
     *
     * @param model the model
     * @param transferredData the transferred data
     */
    public static void deleteNodes(DefaultTreeModel model, TransferredData transferredData) {
        if ((model == null) || (transferredData == null)) {
            return;
        }

        for (int index = 0; index < transferredData.getDataListSize(); index++) {
            NodeInterface nodeToTransfer =
                    (NodeInterface) transferredData.getTreePath(index).getLastPathComponent();
            SelectedFiles selectedFiles =
                    nodeToTransfer.getHandler().getSLDContents(nodeToTransfer);

            nodeToTransfer.getHandler().deleteNodes(nodeToTransfer, selectedFiles.getSldData());
        }
    }

    /**
     * Display messages.
     *
     * @param destinationTreeNode the destination tree node
     * @param transferredData the transferred data
     * @param action the action
     */
    public static void displayMessages(
            NodeInterface destinationTreeNode, TransferredData transferredData, int action) {
        if ((destinationTreeNode == null) || (transferredData == null)) {
            return;
        }

        String actionString = "???";
        if (action == TransferHandler.MOVE) {
            actionString = "Moved";
        } else if (action == TransferHandler.COPY) {
            actionString = "Copied";
        }

        String destinationString =
                destinationTreeNode.getHandler().getDestinationText(destinationTreeNode);
        for (int index = 0; index < transferredData.getDataListSize(); index++) {
            NodeInterface nodeToTransfer =
                    (NodeInterface) transferredData.getTreePath(index).getLastPathComponent();
            SelectedFiles selectedFiles =
                    nodeToTransfer.getHandler().getSLDContents(nodeToTransfer);

            for (SLDDataInterface sldData : selectedFiles.getSldData()) {
                ConsoleManager.getInstance()
                        .information(
                                DataFlavourManager.class,
                                String.format(
                                        "%s %s -> %s",
                                        actionString, sldData.getLayerName(), destinationString));
            }
        }
    }
}
