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
package com.sldeditor.extension.filesystem.file.vector;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.tree.DefaultTreeModel;

import com.sldeditor.common.NodeInterface;
import com.sldeditor.common.SLDDataInterface;
import com.sldeditor.common.filesystem.FileSystemInterface;
import com.sldeditor.common.utils.ExternalFilenames;
import com.sldeditor.datasource.SLDEditorFile;
import com.sldeditor.datasource.extension.filesystem.node.file.FileHandlerInterface;
import com.sldeditor.datasource.extension.filesystem.node.file.FileTreeNode;
import com.sldeditor.datasource.extension.filesystem.node.file.FileTreeNodeTypeEnum;

/**
 * Class that handles reading vector files to the file system.
 * 
 * @author Robert Ward (SCISYS)
 */
public class VectorFileHandler implements FileHandlerInterface
{
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 7418498226923733628L;

    /**
     * Default constructor
     */
    public VectorFileHandler()
    {
    }

    /**
     * Gets the file extension.
     *
     * @return the file extension
     */
    @Override
    public List<String> getFileExtensionList()
    {
        return Arrays.asList("shp");
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
    public boolean populate(FileSystemInterface inputInterface,  DefaultTreeModel treeModel, FileTreeNode node)
    {
        if(node != null)
        {
            node.setFileCategory(FileTreeNodeTypeEnum.VECTOR);
        }

        return false;
    }

    /**
     * Gets the SLD contents.
     *
     * @param node the node
     * @return the SLD contents
     */
    /* (non-Javadoc)
     * @see com.sldeditor.extension.input.FileHandlerInterface#getSLDContents(com.sldeditor.extension.input.NodeInterface)
     */
    @Override
    public List<SLDDataInterface> getSLDContents(NodeInterface node)
    {
        if(node instanceof FileTreeNode)
        {
            FileTreeNode fileTreeNode = (FileTreeNode)node;

            File f = fileTreeNode.getFile();

            String fileExtension = ExternalFilenames.getFileExtension(f.getAbsolutePath());
            if(getFileExtensionList().contains(fileExtension))
            {
                return open(f);
            }
        }
        return null;
    }

    /**
     * Open.
     *
     * @param f the f
     * @return the list
     */
    @Override
    public List<SLDDataInterface> open(File f)
    {
        return new ArrayList<SLDDataInterface>();
    }

    /**
     * Save.
     *
     * @param sldData the sld data
     * @return true, if successful
     */
    /* (non-Javadoc)
     * @see com.sldeditor.extension.input.file.FileHandlerInterface#save(com.sldeditor.ui.iface.SLDDataInterface)
     */
    @Override
    public boolean save(SLDDataInterface sldData)
    {
        return false;
    }

    /**
     * Gets the SLD name.
     *
     * @param sldData the sld data
     * @return the SLD name
     */
    /* (non-Javadoc)
     * @see com.sldeditor.extension.input.file.FileHandlerInterface#getSLDName(com.sldeditor.ui.iface.SLDDataInterface)
     */
    @Override
    public String getSLDName(SLDDataInterface sldData)
    {
        if(sldData != null)
        {
            return sldData.getLayerNameWithOutSuffix() + ExternalFilenames.addFileExtensionSeparator(SLDEditorFile.getSLDFileExtension());
        }

        return "";
    }

    /**
     * Returns if files selected are a data source, e.g. raster or vector.
     *
     * @return true, if is data source
     */
    @Override
    public boolean isDataSource() {
        return true;
    }
}
