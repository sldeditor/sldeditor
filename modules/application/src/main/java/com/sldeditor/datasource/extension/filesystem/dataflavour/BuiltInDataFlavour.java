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

import com.sldeditor.datasource.extension.filesystem.node.database.DatabaseNode;
import com.sldeditor.datasource.extension.filesystem.node.database.DatabaseOverallNode;
import com.sldeditor.datasource.extension.filesystem.node.file.FileTreeNode;
import com.sldeditor.datasource.extension.filesystem.node.geoserver.GeoServerLayerNode;
import com.sldeditor.datasource.extension.filesystem.node.geoserver.GeoServerNode;
import com.sldeditor.datasource.extension.filesystem.node.geoserver.GeoServerOverallNode;
import com.sldeditor.datasource.extension.filesystem.node.geoserver.GeoServerStyleNode;
import java.awt.datatransfer.DataFlavor;
import java.util.List;

/**
 * Adds the built-in (file, folder, GeoServer) data flavours to the application.
 *
 * @author Robert Ward (SCISYS)
 */
public class BuiltInDataFlavour implements DataFlavourInterface {

    /** The Constant GEOSERVER_OVERALL_DATAITEM_FLAVOR. */
    public static final SLDDataFlavour GEOSERVER_OVERALL_DATAITEM_FLAVOUR =
            new SLDDataFlavour(GeoServerOverallNode.class, GeoServerOverallNode.class.getName());

    /** The Constant GEOSERVER_DATAITEM_FLAVOUR. */
    public static final SLDDataFlavour GEOSERVER_DATAITEM_FLAVOUR =
            new SLDDataFlavour(GeoServerNode.class, GeoServerNode.class.getName());

    /** The Constant GEOSERVER_LAYER_DATAITEM_FLAVOUR. */
    public static final SLDDataFlavour GEOSERVER_LAYER_DATAITEM_FLAVOUR =
            new SLDDataFlavour(GeoServerLayerNode.class, GeoServerLayerNode.class.getName());

    /** The Constant GEOSERVER_STYLE_DATAITEM_FLAVOR. */
    public static final SLDDataFlavour GEOSERVER_STYLE_DATAITEM_FLAVOUR =
            new SLDDataFlavour(GeoServerStyleNode.class, GeoServerStyleNode.class.getName());

    /** The Constant DEFAULT_MUTABLE_DATAITEM_FLAVOR. */
    public static final SLDDataFlavour FILE_DATAITEM_FLAVOR =
            new SLDDataFlavour(FileTreeNode.class, "File");

    /** The Constant DATABASE_OVERALL_DATAITEM_FLAVOUR. */
    public static final SLDDataFlavour DATABASE_OVERALL_DATAITEM_FLAVOUR =
            new SLDDataFlavour(DatabaseOverallNode.class, DatabaseOverallNode.class.getName());

    /** The Constant DATABASE_DATAITEM_FLAVOUR. */
    public static final SLDDataFlavour DATABASE_DATAITEM_FLAVOUR =
            new SLDDataFlavour(DatabaseNode.class, DatabaseNode.class.getName());

    /** Instantiates a new built in data flavour. */
    public BuiltInDataFlavour() {}

    /**
     * Populate data flavours supported.
     *
     * @param dataFlavourList the data flavour list
     * @param destinationFolderList the destination folder list
     * @param destinationGeoServerList the destination geo server list
     */
    @Override
    public void populate(
            List<DataFlavor> dataFlavourList,
            List<DataFlavor> destinationFolderList,
            List<DataFlavor> destinationGeoServerList) {

        dataFlavourList.add(FILE_DATAITEM_FLAVOR);
        dataFlavourList.add(DataFlavourManager.FOLDER_DATAITEM_FLAVOR);
        dataFlavourList.add(GEOSERVER_STYLE_DATAITEM_FLAVOUR);
        dataFlavourList.add(GEOSERVER_LAYER_DATAITEM_FLAVOUR);
        dataFlavourList.add(GEOSERVER_DATAITEM_FLAVOUR);
        dataFlavourList.add(GEOSERVER_OVERALL_DATAITEM_FLAVOUR);

        destinationFolderList.add(FILE_DATAITEM_FLAVOR);
        destinationFolderList.add(GEOSERVER_STYLE_DATAITEM_FLAVOUR);

        destinationGeoServerList.add(FILE_DATAITEM_FLAVOR);
        destinationGeoServerList.add(GEOSERVER_STYLE_DATAITEM_FLAVOUR);
    }
}
