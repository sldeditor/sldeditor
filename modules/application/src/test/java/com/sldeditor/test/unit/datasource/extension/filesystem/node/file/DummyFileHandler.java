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

package com.sldeditor.test.unit.datasource.extension.filesystem.node.file;

import com.sldeditor.common.NodeInterface;
import com.sldeditor.common.SLDDataInterface;
import com.sldeditor.common.filesystem.FileSystemInterface;
import com.sldeditor.datasource.extension.filesystem.node.file.FileHandlerInterface;
import com.sldeditor.datasource.extension.filesystem.node.file.FileTreeNode;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import javax.swing.Icon;
import javax.swing.tree.DefaultTreeModel;

/**
 * The Class DummyFileHandler.
 *
 * @author Robert Ward (SCISYS)
 */
public class DummyFileHandler implements FileHandlerInterface {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /**
     * Gets the file extension.
     *
     * @return the file extension
     */
    @Override
    public List<String> getFileExtensionList() {
        return Arrays.asList("test");
    }

    /**
     * Populate.
     *
     * @param inputInterface the input interface
     * @param treeModel the tree model
     * @param node the node
     * @return true, if successful
     */
    @Override
    public boolean populate(
            FileSystemInterface inputInterface, DefaultTreeModel treeModel, FileTreeNode node) {
        return false;
    }

    /**
     * Gets the SLD contents.
     *
     * @param node the node
     * @return the SLD contents
     */
    @Override
    public List<SLDDataInterface> getSLDContents(NodeInterface node) {
        return null;
    }

    /**
     * Open.
     *
     * @param file the file
     * @return the list
     */
    @Override
    public List<SLDDataInterface> open(File file) {
        return null;
    }

    /**
     * Save.
     *
     * @param sldData the sld data
     * @return true, if successful
     */
    @Override
    public boolean save(SLDDataInterface sldData) {
        return false;
    }

    /**
     * Gets the SLD name.
     *
     * @param sldData the sld data
     * @return the SLD name
     */
    @Override
    public String getSLDName(SLDDataInterface sldData) {
        return null;
    }

    /**
     * Returns if files selected are a data source, e.g. raster or vector.
     *
     * @return true, if is data source
     */
    @Override
    public boolean isDataSource() {
        return false;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.sldeditor.datasource.extension.filesystem.node.file.FileHandlerInterface#getIcon(java.
     * lang.String, java.lang.String)
     */
    @Override
    public Icon getIcon(String path, String filename) {
        return null;
    }
}
