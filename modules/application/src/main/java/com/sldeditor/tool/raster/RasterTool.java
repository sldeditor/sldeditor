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

package com.sldeditor.tool.raster;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

import com.sldeditor.common.DataSourceConnectorInterface;
import com.sldeditor.common.DataSourcePropertiesInterface;
import com.sldeditor.common.LoadSLDInterface;
import com.sldeditor.common.NodeInterface;
import com.sldeditor.common.SLDDataInterface;
import com.sldeditor.common.SLDEditorInterface;
import com.sldeditor.common.console.ConsoleManager;
import com.sldeditor.common.filesystem.SelectedFiles;
import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.datasource.DataSourceInterface;
import com.sldeditor.datasource.SLDEditorFile;
import com.sldeditor.datasource.connector.DataSourceConnectorFactory;
import com.sldeditor.datasource.connector.instance.DataSourceConnector;
import com.sldeditor.datasource.extension.filesystem.node.file.FileTreeNode;
import com.sldeditor.datasource.extension.filesystem.node.file.FileTreeNodeTypeEnum;
import com.sldeditor.datasource.impl.DataSourceFactory;
import com.sldeditor.datasource.impl.DataSourceProperties;
import com.sldeditor.tool.ToolButton;
import com.sldeditor.tool.ToolInterface;
import com.sldeditor.tool.ToolPanel;

/**
 * The Class RasterTool.
 *
 * @author Robert Ward (SCISYS)
 */
public class RasterTool implements ToolInterface {

    private static final int PANEL_WIDTH = 90;

    /** The raster panel. */
    private JPanel rasterPanel = null;

    /** The import raster button. */
    private JButton importRasterButton;

    /** The data source button. */
    private JButton dataSourceButton;

    /** The node type list. */
    private List<NodeInterface> nodeTypeList = null;

    /** The raster reader. */
    private RasterReaderInterface rasterReader = new RasterReader();

    /** The sld editor interface. */
    private SLDEditorInterface sldEditorInterface = null;

    /**
     * Instantiates a new raster tool.
     *
     * @param sldEditorInterface the sld editor interface
     */
    public RasterTool(SLDEditorInterface sldEditorInterface) {
        super();

        this.sldEditorInterface = sldEditorInterface;
        createUI();
    }

    /**
     * Creates the ui.
     */
    private void createUI() {
        rasterPanel = new JPanel();
        FlowLayout flowLayout = (FlowLayout) rasterPanel.getLayout();
        flowLayout.setVgap(0);
        flowLayout.setHgap(0);
        rasterPanel.setBorder(BorderFactory
                .createTitledBorder(Localisation.getString(RasterTool.class, "RasterTool.title")));

        //
        // Import raster
        //
        importRasterButton = new ToolButton(
                Localisation.getString(RasterTool.class, "RasterTool.import"),
                "tool/importraster.png");
        rasterPanel.add(importRasterButton);
        importRasterButton.setEnabled(false);

        importRasterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ((nodeTypeList != null) && (nodeTypeList.size() == 1)) {
                    if (sldEditorInterface != null) {
                        FileTreeNode fileTreeNode = (FileTreeNode) nodeTypeList.get(0);

                        File rasterFile = fileTreeNode.getFile();
                        ConsoleManager.getInstance().information(this,
                                String.format("%s : %s",
                                        Localisation.getString(RasterTool.class,
                                                "RasterTool.createSymbol"),
                                        rasterFile.getAbsolutePath()));
                        SLDDataInterface sldData = rasterReader.createRasterSLDData(rasterFile);

                        // Raster file
                        DataSourcePropertiesInterface dsProperties = SLDEditorFile.getInstance()
                                .getDataSource();

                        DataSourceConnectorInterface dsc = DataSourceConnectorFactory
                                .getDataSource(DataSourceConnector.class);

                        dsProperties = dsc.getDataSourceProperties(
                                DataSourceProperties.encodeFilename(rasterFile.getAbsolutePath()));

                        SLDEditorFile.getInstance().setSLDData(sldData);
                        SLDEditorFile.getInstance().setDataSource(dsProperties);

                        // Clear the data change flag
                        SLDEditorFile.getInstance().fileOpenedSaved();

                        // Load sld
                        List<SLDDataInterface> sldFilesToLoad = new ArrayList<SLDDataInterface>();
                        sldFilesToLoad.add(sldData);

                        SelectedFiles selectedFiles = new SelectedFiles();
                        selectedFiles.setSldData(sldFilesToLoad);
                        selectedFiles.setFolderName(rasterFile.getParent());

                        LoadSLDInterface loadSLD = sldEditorInterface.getLoadSLDInterface();
                        loadSLD.loadSLDString(selectedFiles);
                    }
                }
            }
        });

        //
        // Set data source
        //
        dataSourceButton = new ToolButton(
                Localisation.getString(RasterTool.class, "RasterTool.dataSource"),
                "tool/setdatasource.png");
        rasterPanel.add(dataSourceButton);
        dataSourceButton.setEnabled(false);

        dataSourceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ((nodeTypeList != null) && (nodeTypeList.size() == 1)) {
                    if (sldEditorInterface != null) {
                        FileTreeNode fileTreeNode = (FileTreeNode) nodeTypeList.get(0);

                        File rasterFile = fileTreeNode.getFile();

                        ConsoleManager.getInstance().information(this,
                                String.format("%s : %s",
                                        Localisation.getString(RasterTool.class,
                                                "RasterTool.setDataSource"),
                                        rasterFile.getAbsolutePath()));

                        // Raster file
                        DataSourceConnectorInterface dsc = DataSourceConnectorFactory
                                .getDataSource(DataSourceConnector.class);

                        String rasterFilename = null;
                        try {
                            rasterFilename = rasterFile.toURI().toURL().toString();
                        } catch (MalformedURLException exceptionObj) {
                            ConsoleManager.getInstance().exception(RasterTool.class, exceptionObj);
                            return;
                        }
                        DataSourcePropertiesInterface dsProperties = dsc.getDataSourceProperties(
                                DataSourceProperties.encodeFilename(rasterFilename));

                        SLDEditorFile.getInstance().setDataSource(dsProperties);

                        DataSourceInterface dataSource = DataSourceFactory.createDataSource(null);

                        if (dataSource != null) {
                            dataSource.connect(rasterFilename, SLDEditorFile.getInstance(), null);
                        }
                    }
                }
            }
        });
        rasterPanel.setPreferredSize(new Dimension(PANEL_WIDTH, ToolPanel.TOOL_PANEL_HEIGHT));
    }

    /**
     * Gets the panel.
     *
     * @return the panel
     */
    @Override
    public JPanel getPanel() {
        return rasterPanel;
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

        if (importRasterButton != null) {
            importRasterButton.setEnabled(nodeTypeList.size() == 1);
        }

        if (dataSourceButton != null) {
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
    public boolean supports(List<Class<?>> uniqueNodeTypeList, List<NodeInterface> nodeTypeList,
            List<SLDDataInterface> sldDataList) {
        for (NodeInterface node : nodeTypeList) {
            if (node instanceof FileTreeNode) {
                FileTreeNode fileTreeNode = (FileTreeNode) node;

                if (fileTreeNode.getFileCategory() != FileTreeNodeTypeEnum.RASTER) {
                    return false;
                }
            } else {
                return false;
            }
        }
        return true;
    }
}
