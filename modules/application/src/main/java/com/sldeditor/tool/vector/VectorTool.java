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
package com.sldeditor.tool.vector;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

import com.sldeditor.common.DataSourcePropertiesInterface;
import com.sldeditor.common.LoadSLDInterface;
import com.sldeditor.common.NodeInterface;
import com.sldeditor.common.SLDDataInterface;
import com.sldeditor.common.SLDEditorInterface;
import com.sldeditor.common.console.ConsoleManager;
import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.datasource.DataSourceInterface;
import com.sldeditor.datasource.SLDEditorFile;
import com.sldeditor.datasource.connector.instance.DataSourceConnectorShapeFile;
import com.sldeditor.datasource.extension.filesystem.node.file.FileTreeNode;
import com.sldeditor.datasource.extension.filesystem.node.file.FileTreeNodeTypeEnum;
import com.sldeditor.datasource.impl.DataSourceFactory;
import com.sldeditor.datasource.impl.DataSourceProperties;
import com.sldeditor.tool.ToolButton;
import com.sldeditor.tool.ToolInterface;

/**
 * The Class VectorTool.
 *
 * @author Robert Ward (SCISYS)
 */
public class VectorTool implements ToolInterface {

    /** The vector panel. */
    private JPanel vectorPanel = null;

    /** The import vector button. */
    private JButton importVectorButton;

    /** The data source button. */
    private JButton dataSourceButton;

    /** The node type list. */
    private List<NodeInterface> nodeTypeList = null;

    /** The vector reader. */
    private VectorReaderInterface vectorReader = new VectorReader();

    /** The sld editor interface. */
    private SLDEditorInterface sldEditorInterface = null;

    /**
     * Instantiates a new vector tool.
     *
     * @param sldEditorInterface the sld editor interface
     */
    public VectorTool(SLDEditorInterface sldEditorInterface)
    {
        super();

        this.sldEditorInterface = sldEditorInterface;
        createUI();
    }

    /**
     * Creates the ui.
     */
    private void createUI() {
        vectorPanel = new JPanel();
        vectorPanel.setBorder(BorderFactory.createTitledBorder(Localisation.getString(VectorTool.class, "VectorTool.title")));

        //
        // Import raster
        //
        importVectorButton = new ToolButton(Localisation.getString(VectorTool.class, "VectorTool.import"),
                "tool/importvector.png");
        vectorPanel.add(importVectorButton);
        importVectorButton.setEnabled(false);

        importVectorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if((nodeTypeList != null) && (nodeTypeList.size() == 1))
                {
                    if(sldEditorInterface != null)
                    {
                        FileTreeNode fileTreeNode = (FileTreeNode) nodeTypeList.get(0);

                        File vectorFile = fileTreeNode.getFile();
                        SLDDataInterface sldData = vectorReader.createVectorSLDData(vectorFile);
                        LoadSLDInterface loadSLD = sldEditorInterface.getLoadSLDInterface();

                        // Vector file
                        DataSourcePropertiesInterface dsProperties = SLDEditorFile.getInstance().getDataSource();

                        DataSourceConnectorShapeFile dsc = new DataSourceConnectorShapeFile();

                        try {
                            dsProperties = dsc.getDataSourceProperties(DataSourceProperties.encodeFilename(vectorFile.toURI().toURL().toString()));
                        }
                        catch (MalformedURLException exceptionObj) {
                            ConsoleManager.getInstance().exception(VectorTool.class, exceptionObj);
                            return;
                        }

                        SLDEditorFile.getInstance().setSLDData(sldData);
                        SLDEditorFile.getInstance().setDataSource(dsProperties);

                        DataSourceInterface dataSource = DataSourceFactory.createDataSource(null);

                        if(dataSource != null)
                        {
                            dataSource.connect(SLDEditorFile.getInstance());
                        }

                        // Load sld
                        List<SLDDataInterface> sldFilesToLoad = new ArrayList<SLDDataInterface>();
                        sldFilesToLoad.add(sldData);
                        loadSLD.loadSLDString(false, false, sldFilesToLoad);
                    }
                }
            }
        });

        //
        // Set data source
        //
        dataSourceButton = new ToolButton(Localisation.getString(VectorTool.class, "VectorTool.dataSource"),
                "tool/setdatasource.png");
        vectorPanel.add(dataSourceButton);
        dataSourceButton.setEnabled(false);

        dataSourceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if((nodeTypeList != null) && (nodeTypeList.size() == 1))
                {
                    if(sldEditorInterface != null)
                    {
                        FileTreeNode fileTreeNode = (FileTreeNode) nodeTypeList.get(0);

                        File vectorFile = fileTreeNode.getFile();

                        // Vector file
                        DataSourceConnectorShapeFile dsc = new DataSourceConnectorShapeFile();

                        DataSourcePropertiesInterface dsProperties = null;
                        try {
                            dsProperties = dsc.getDataSourceProperties(DataSourceProperties.encodeFilename(vectorFile.toURI().toURL().toString()));
                        }
                        catch (MalformedURLException exceptionObj) {
                            ConsoleManager.getInstance().exception(VectorTool.class, exceptionObj);
                            return;
                        }

                        SLDEditorFile.getInstance().setDataSource(dsProperties);

                        DataSourceInterface dataSource = DataSourceFactory.createDataSource(null);

                        if(dataSource != null)
                        {
                            dataSource.connect(SLDEditorFile.getInstance());
                        }
                    }
                }
            }
        });
    }

    /**
     * Gets the panel.
     *
     * @return the panel
     */
    @Override
    public JPanel getPanel() {
        return vectorPanel;
    }

    /**
     * Sets the selected items.
     *
     * @param nodeTypeList the node type list
     * @param sldDataList the sld data list
     */
    @Override
    public void setSelectedItems(List<NodeInterface> nodeTypeList,
            List<SLDDataInterface> sldDataList) {
        this.nodeTypeList = nodeTypeList;

        if(importVectorButton != null)
        {
            importVectorButton.setEnabled(nodeTypeList.size() == 1);
        }

        if(dataSourceButton != null)
        {
            dataSourceButton.setEnabled(nodeTypeList.size() == 1);
        }
    }

    /**
     * Gets the tool name.
     *
     * @return the tool name
     */
    @Override
    public String getToolName() {
        return getClass().getName();
    }

    /**
     * Supports.
     *
     * @param uniqueNodeTypeList the unique node type list
     * @param nodeTypeList the node type list
     * @param sldDataList the sld data list
     * @return true, if successful
     */
    @Override
    public boolean supports(List<Class<?>> uniqueNodeTypeList, List<NodeInterface> nodeTypeList, List<SLDDataInterface> sldDataList) {
        for(NodeInterface node : nodeTypeList)
        {
            if(node instanceof FileTreeNode)
            {
                FileTreeNode fileTreeNode = (FileTreeNode) node;

                if(fileTreeNode.getFileCategory() != FileTreeNodeTypeEnum.VECTOR)
                {
                    return false;
                }
            }
            else
            {
                return false;
            }
        }
        return true;
    }
}
