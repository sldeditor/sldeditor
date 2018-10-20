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

package com.sldeditor.datasource.extension.filesystem.node;

import com.sldeditor.common.NodeInterface;
import com.sldeditor.datasource.extension.filesystem.dataflavour.DataFlavourManager;
import com.sldeditor.datasource.extension.filesystem.dataflavour.TransferredData;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

/**
 * Class that allows nodes to be dragged and dropped within the file system tree.
 *
 * @author Robert Ward (SCISYS)
 */
public class TransferableDataItem extends DefaultMutableTreeNode implements Transferable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 8898392127421632735L;

    /** The data. */
    private TransferredData data;

    /** Default constructor. */
    public TransferableDataItem() {
        // Default constructor
    }

    /**
     * Instantiates a new transferable data item.
     *
     * @param selectedData the selected data
     */
    public TransferableDataItem(Map<NodeInterface, TreePath> selectedData) {
        internalPopulate(selectedData);
    }

    /**
     * Internal populate.
     *
     * @param selectedData the selected data
     */
    private void internalPopulate(Map<NodeInterface, TreePath> selectedData) {
        if (selectedData != null) {
            this.data = new TransferredData();

            for (Entry<NodeInterface, TreePath> entry : selectedData.entrySet()) {
                NodeInterface selection = entry.getKey();
                TreePath path = selectedData.get(selection);

                DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
                this.data.addData(path, node.getUserObject(), selection.getDataFlavour());
            }
        }
    }

    /**
     * Instantiates a new transferable data item.
     *
     * @param destinationTreeNode the destination tree node
     * @param path the path
     */
    public TransferableDataItem(NodeInterface destinationTreeNode, TreePath path) {
        if ((destinationTreeNode != null) && (path != null)) {
            Map<NodeInterface, TreePath> selectedData = new LinkedHashMap<>();

            selectedData.put(destinationTreeNode, path);

            internalPopulate(selectedData);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see java.awt.datatransfer.Transferable#getTransferDataFlavors()
     */
    public DataFlavor[] getTransferDataFlavors() {
        return DataFlavourManager.getDataFlavourArray();
    }

    /*
     * (non-Javadoc)
     *
     * @see java.awt.datatransfer.Transferable#getTransferData(java.awt.datatransfer.DataFlavor)
     */
    public Object getTransferData(DataFlavor flavor)
            throws UnsupportedFlavorException, IOException {
        return data;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.awt.datatransfer.Transferable#isDataFlavorSupported(java.awt.datatransfer.DataFlavor)
     */
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return DataFlavourManager.isSupported(this.getDataFlavour(), flavor);
    }

    /**
     * Gets the data flavour for the first data item.
     *
     * @return the data flavour
     */
    public DataFlavor getDataFlavour() {
        return data.getDataFlavour(0);
    }
}
