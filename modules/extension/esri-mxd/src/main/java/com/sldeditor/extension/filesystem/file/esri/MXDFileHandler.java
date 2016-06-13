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
import com.sldeditor.datasource.extension.filesystem.FileSystemUtils;
import com.sldeditor.datasource.extension.filesystem.node.file.FileHandlerInterface;
import com.sldeditor.datasource.extension.filesystem.node.file.FileTreeNode;

/**
 * Class that allows converted MXD json file to be presented to the user as separate SLD files.
 * 
 * @author Robert Ward (SCISYS)
 */
public class MXDFileHandler implements FileHandlerInterface
{

    /** The Constant FILE_EXTENSION. */
    private static final String FILE_EXTENSION = "json";

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 2008243601724746324L;

    /* (non-Javadoc)
     * @see com.sldeditor.extension.input.FileHandlerInterface#getFileExtension()
     */
    @Override
    public List<String> getFileExtensionList()
    {
        return Arrays.asList(FILE_EXTENSION);
    }

    /* (non-Javadoc)
     * @see com.sldeditor.extension.input.FileHandlerInterface#populate(com.sldeditor.extension.input.NodeInterface, javax.swing.tree.DefaultTreeModel, com.sldeditor.extension.folder.FileTreeNode)
     */
    @Override
    public boolean populate(FileSystemInterface inputInferface, DefaultTreeModel treeModel, FileTreeNode node)
    {
        if(inputInferface == null)
        {
            return false;
        }

        if(node == null)
        {
            return false;
        }

        boolean parsedOk = true;
        File file = node.getFile();
        if(file != null)
        {
            String filename = file.getAbsolutePath();

            MXDInfo mxdInfo = MXDParser.readLayers(filename);

            MXDNode mxdNode = new MXDNode(inputInferface, mxdInfo);
            if(treeModel != null)
            {
                treeModel.insertNodeInto(mxdNode, node, node.getChildCount());

                for(String layerName : mxdInfo.getLayerList())
                {
                    MXDLayerNode mxdLayerNode = new MXDLayerNode(inputInferface, mxdInfo, layerName);

                    treeModel.insertNodeInto(mxdLayerNode, mxdNode, mxdNode.getChildCount());
                }

                treeModel.reload(node);
            }
        }

        return parsedOk;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.extension.input.FileHandlerInterface#getSLDContents(com.sldeditor.extension.input.NodeInterface)
     */
    @Override
    public List<SLDDataInterface> getSLDContents(NodeInterface node)
    {
        if(node instanceof FileTreeNode)
        {
            FileTreeNode fileTreeNode = (FileTreeNode) node;
            File f = fileTreeNode.getFile();

            String fileExtension = ExternalFilenames.getFileExtension(f.getAbsolutePath());
            if(getFileExtensionList().contains(fileExtension))
            {
                if(fileTreeNode.getFirstChild() instanceof MXDNode)
                {
                    return getMXDNodeData((NodeInterface) fileTreeNode.getFirstChild());
                }
            }
        }
        if(node instanceof MXDLayerNode)
        {
            MXDLayerNode mxdLayerNode = (MXDLayerNode)node;

            MXDInfo mxdInfo = mxdLayerNode.getMXDInfo();

            File f = mxdInfo.getIntermediateFile();

            if(f != null)
            {
                if(FileSystemUtils.isFileExtensionSupported(f, getFileExtensionList()))
                {
                    List<SLDDataInterface> list = new ArrayList<SLDDataInterface>();

                    SLDDataInterface sldData = MXDParser.readLayer(f.getAbsolutePath(), mxdLayerNode.getLayerName());
                    list.add(sldData);

                    return list;
                }
            }
        }
        else if(node instanceof MXDNode)
        {
            return getMXDNodeData(node);
        }
        return null;
    }

    /**
     * Gets the MXD node data.
     *
     * @param node the node
     * @return the MXD node data
     */
    private List<SLDDataInterface> getMXDNodeData(NodeInterface node) {
        MXDNode mxdNode = (MXDNode)node;

        MXDInfo mxdInfo = mxdNode.getMXDInfo();

        File f = mxdInfo.getIntermediateFile();

        if(f != null)
        {
            if(FileSystemUtils.isFileExtensionSupported(f, getFileExtensionList()))
            {
                List<SLDDataInterface> list = new ArrayList<SLDDataInterface>();

                for(String layerName : mxdInfo.getLayerList())
                {
                    SLDDataInterface sldData = MXDParser.readLayer(f.getAbsolutePath(), layerName);
                    list.add(sldData);
                }
                return list;
            }
        }
        return null;
    }

    /**
     * Open file.
     *
     * @param f the f
     * @return the list
     */
    @Override
    public List<SLDDataInterface> open(File f)
    {
        return null;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.extension.input.file.FileHandlerInterface#save(com.sldeditor.ui.iface.SLDDataInterface)
     */
    @Override
    public boolean save(SLDDataInterface sldData)
    {
        return false;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.extension.input.file.FileHandlerInterface#getSLDName(com.sldeditor.ui.iface.SLDDataInterface)
     */
    @Override
    public String getSLDName(SLDDataInterface sldData)
    {
        if(sldData == null)
        {
            return null;
        }
        return sldData.getLayerNameWithOutSuffix() + SLDEditorFile.getSLDFileExtension();
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
}
