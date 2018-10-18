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

import java.awt.datatransfer.DataFlavor;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.swing.tree.TreePath;

/**
 * The data being dragged and dropped within the file system tree.
 *
 * @author Robert Ward (SCISYS)
 */
public class TransferredData implements Serializable {
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -3406201302956370401L;

    /**
     * Internal class that describes the data being dragged and dropped within the file system tree.
     */
    private class InternalTransferredData implements Serializable {

        /** The Constant serialVersionUID. */
        private static final long serialVersionUID = -5290507590609699883L;

        /** The user object. */
        private transient Object userObject = null;

        /** The tree path. */
        private TreePath treePath = null;

        /** The data flavour. */
        private DataFlavor dataFlavour = null;
    }

    /** The data list. */
    private List<InternalTransferredData> dataList = new ArrayList<>();

    /** Default constructor. */
    public TransferredData() {
        // Default constructor
    }

    /**
     * Gets the size of the data list.
     *
     * @return the data list size
     */
    public int getDataListSize() {
        return dataList.size();
    }

    /**
     * Gets the user object.
     *
     * @param index the index
     * @return the user object
     */
    public Object getUserObject(int index) {
        if ((index < 0) || (index >= dataList.size())) {
            return null;
        }

        return dataList.get(index).userObject;
    }

    /**
     * Gets the tree path.
     *
     * @param index the index
     * @return the tree path
     */
    public TreePath getTreePath(int index) {
        if ((index < 0) || (index >= dataList.size())) {
            return null;
        }
        return dataList.get(index).treePath;
    }

    /**
     * Gets the data flavour.
     *
     * @param index the index
     * @return the data flavour
     */
    public DataFlavor getDataFlavour(int index) {
        if ((index < 0) || (index >= dataList.size())) {
            return null;
        }
        return dataList.get(index).dataFlavour;
    }

    /**
     * Adds the data.
     *
     * @param treePath the tree path
     * @param userObject the user object
     * @param dataFlavour the data flavour
     */
    public void addData(TreePath treePath, Object userObject, DataFlavor dataFlavour) {
        InternalTransferredData data = new InternalTransferredData();
        data.treePath = treePath;
        data.userObject = userObject;
        data.dataFlavour = dataFlavour;

        dataList.add(data);
    }
}
