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
package com.sldeditor.extension.filesystem.file.esri;

import java.awt.datatransfer.DataFlavor;
import java.util.List;

import com.sldeditor.datasource.extension.filesystem.dataflavour.DataFlavourInterface;
import com.sldeditor.datasource.extension.filesystem.dataflavour.SLDDataFlavour;

/**
 * Class that registers Esri MXD data flavours to allow dragging and dropping within the file system tree.
 * 
 * @author Robert Ward (SCISYS)
 */
public class EsriDataFlavour implements DataFlavourInterface {

    /** The Constant MXD_NODE_DATAITEM_FLAVOUR. */
    final public static SLDDataFlavour MXD_NODE_DATAITEM_FLAVOUR = new SLDDataFlavour(MXDNode.class, MXDNode.class.getName());

    /** The Constant MXDLAYER_NODE_DATAITEM_FLAVOUR. */
    final public static SLDDataFlavour MXDLAYER_NODE_DATAITEM_FLAVOUR = new SLDDataFlavour(MXDLayerNode.class, MXDLayerNode.class.getName());

    /**
     * Default constructor
     */
    public EsriDataFlavour()
    {
    }

    /**
     * Populate data flavours supported.
     *
     * @param dataFlavourList the data flavour list
     * @param destinationFolderList the destination folder list
     * @param destinationGeoServerList the destination geo server list
     */
    @Override
    public void populate(List<DataFlavor> dataFlavourList, List<DataFlavor> destinationFolderList,
            List<DataFlavor> destinationGeoServerList) {

        dataFlavourList.add(MXD_NODE_DATAITEM_FLAVOUR);
        dataFlavourList.add(MXDLAYER_NODE_DATAITEM_FLAVOUR);

        destinationFolderList.add(MXDLAYER_NODE_DATAITEM_FLAVOUR);

        destinationGeoServerList.add(MXDLAYER_NODE_DATAITEM_FLAVOUR);
    }

}
