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

package com.sldeditor.datasource.extension.filesystem.node.file;

import java.io.File;
import java.io.Serializable;
import java.util.List;

import javax.swing.Icon;
import javax.swing.tree.DefaultTreeModel;

import com.sldeditor.common.NodeInterface;
import com.sldeditor.common.SLDDataInterface;
import com.sldeditor.common.filesystem.FileSystemInterface;
import com.sldeditor.datasource.extension.filesystem.node.file.FileTreeNode;

/**
 * The Interface FileHandlerInterface.
 * 
 * @author Robert Ward (SCISYS)
 */
public interface FileHandlerInterface extends Serializable {

    /**
     * Gets the list of supported file extensions.
     *
     * @return the file extension list
     */
    List<String> getFileExtensionList();

    /**
     * Populate tree.
     *
     * @param inputInterface the input interface
     * @param treeModel the tree model
     * @param node the node
     * @return true, if successful
     */
    boolean populate(FileSystemInterface inputInterface, DefaultTreeModel treeModel,
            FileTreeNode node);

    /**
     * Gets the SLD contents.
     *
     * @param node the node
     * @return the SLD contents
     */
    List<SLDDataInterface> getSLDContents(NodeInterface node);

    /**
     * Open file.
     *
     * @param file the file
     * @return the SLD data
     */
    List<SLDDataInterface> open(File file);

    /**
     * Save file.
     *
     * @param sldData the sld data
     * @return the SLD data
     */
    boolean save(SLDDataInterface sldData);

    /**
     * Gets the SLD name.
     *
     * @param sldData the sld data
     * @return the SLD name
     */
    String getSLDName(SLDDataInterface sldData);

    /**
     * Returns if files selected are a data source, e.g. raster or vector.
     *
     * @return true, if is data source
     */
    boolean isDataSource();

    /**
     * Gets the icon.
     *
     * @param path the path
     * @param filename the filename
     * @return the icon
     */
    Icon getIcon(String path, String filename);
}
