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
import com.sldeditor.common.connection.DatabaseConnectionManager;
import com.sldeditor.common.console.ConsoleManager;
import com.sldeditor.common.filesystem.SelectedFiles;
import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.common.utils.ExternalFilenames;
import com.sldeditor.datasource.DataSourceInterface;
import com.sldeditor.datasource.SLDEditorFile;
import com.sldeditor.datasource.connector.DataSourceConnectorFactory;
import com.sldeditor.datasource.connector.instance.DataSourceConnector;
import com.sldeditor.datasource.extension.filesystem.node.database.DatabaseFeatureClassNode;
import com.sldeditor.datasource.extension.filesystem.node.file.FileTreeNode;
import com.sldeditor.datasource.extension.filesystem.node.file.FileTreeNodeTypeEnum;
import com.sldeditor.datasource.impl.DataSourceFactory;
import com.sldeditor.datasource.impl.DataSourceProperties;
import com.sldeditor.extension.filesystem.database.client.DatabaseClientInterface;
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
    public VectorTool(SLDEditorInterface sldEditorInterface) {
        super();

        this.sldEditorInterface = sldEditorInterface;
        createUI();
    }

    /**
     * Creates the ui.
     */
    private void createUI() {
        vectorPanel = new JPanel();
        FlowLayout flowLayout = (FlowLayout) vectorPanel.getLayout();
        flowLayout.setVgap(0);
        flowLayout.setHgap(0);
        vectorPanel.setBorder(BorderFactory
                .createTitledBorder(Localisation.getString(VectorTool.class, "VectorTool.title")));

        //
        // Import vector
        //
        importVectorButton = new ToolButton(
                Localisation.getString(VectorTool.class, "VectorTool.import"),
                "tool/importvector.png");
        vectorPanel.add(importVectorButton);
        importVectorButton.setEnabled(false);

        importVectorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ((nodeTypeList != null) && (nodeTypeList.size() == 1)) {
                    if (sldEditorInterface != null) {
                        NodeInterface nodeInterface = nodeTypeList.get(0);
                        if (nodeInterface instanceof FileTreeNode) {
                            FileTreeNode fileTreeNode = (FileTreeNode) nodeInterface;

                            if (!importFile(fileTreeNode)) {
                                return;
                            }
                        } else if (nodeInterface instanceof DatabaseFeatureClassNode) {
                            DatabaseFeatureClassNode featureClassNode = (DatabaseFeatureClassNode) nodeInterface;

                            if (!importFeatureClass(featureClassNode)) {
                                return;
                            }
                        }
                    }
                }
            }
        });

        //
        // Set data source
        //
        dataSourceButton = new ToolButton(
                Localisation.getString(VectorTool.class, "VectorTool.dataSource"),
                "tool/setdatasource.png");
        vectorPanel.add(dataSourceButton);
        dataSourceButton.setEnabled(false);

        dataSourceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ((nodeTypeList != null) && (nodeTypeList.size() == 1)) {
                    if (sldEditorInterface != null) {
                        NodeInterface nodeInterface = nodeTypeList.get(0);
                        if (nodeInterface instanceof FileTreeNode) {
                            FileTreeNode fileTreeNode = (FileTreeNode) nodeInterface;

                            setDataSource(fileTreeNode);
                        } else if (nodeInterface instanceof DatabaseFeatureClassNode) {
                            DatabaseFeatureClassNode featureClassNode = (DatabaseFeatureClassNode) nodeInterface;

                            setDataSource(featureClassNode);
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

        if (importVectorButton != null) {
            importVectorButton.setEnabled(nodeTypeList.size() == 1);
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

                if (fileTreeNode.getFileCategory() != FileTreeNodeTypeEnum.VECTOR) {
                    return false;
                }
            } else if (node instanceof DatabaseFeatureClassNode) {
                return true;
            } else {
                return false;
            }
        }
        return true;
    }

    /**
     * Import file.
     *
     * @param fileTreeNode the file tree node
     */
    protected boolean importFile(FileTreeNode fileTreeNode) {
        if (fileTreeNode != null) {
            File vectorFile = fileTreeNode.getFile();
            ConsoleManager.getInstance().information(this,
                    String.format("%s : %s",
                            Localisation.getString(VectorTool.class, "VectorTool.createSymbol"),
                            vectorFile.getAbsolutePath()));
            SLDDataInterface sldData = vectorReader.createVectorSLDData(vectorFile);

            DataSourcePropertiesInterface dsProperties = SLDEditorFile.getInstance()
                    .getDataSource();

            DataSourceConnectorInterface dsc = DataSourceConnectorFactory
                    .getDataSource(DataSourceConnector.class);

            try {
                String vectorFilename = vectorFile.toURI().toURL().toString();
                dsProperties = dsc.getDataSourceProperties(
                        DataSourceProperties.encodeFilename(vectorFilename));
            } catch (MalformedURLException exceptionObj) {
                ConsoleManager.getInstance().exception(VectorTool.class, exceptionObj);
                return false;
            }

            loadSymbol(dsProperties, sldData, vectorFile);
        }
        return true;
    }

    /**
     * Load symbol.
     *
     * @param dsProperties the ds properties
     * @param sldData the sld data
     * @param folderName the folder name
     */
    private void loadSymbol(DataSourcePropertiesInterface dsProperties, SLDDataInterface sldData,
            File vectorFile) {
        loadSymbol(dsProperties, sldData, ExternalFilenames.removeSuffix(vectorFile.getName()),
                vectorFile.getParent());
    }

    /**
     * Load symbol.
     *
     * @param dsProperties the ds properties
     * @param sldData the sld data
     * @param vectorFilename the vector filename
     * @param folder the folder
     */
    private void loadSymbol(DataSourcePropertiesInterface dsProperties, SLDDataInterface sldData,
            String vectorFilename, String folder) {
        LoadSLDInterface loadSLD = sldEditorInterface.getLoadSLDInterface();

        // Vector file
        SLDEditorFile.getInstance().setSLDData(sldData);
        SLDEditorFile.getInstance().setDataSource(dsProperties);

        // Clear the data change flag
        SLDEditorFile.getInstance().fileOpenedSaved();

        // Load sld
        List<SLDDataInterface> sldFilesToLoad = new ArrayList<SLDDataInterface>();
        sldFilesToLoad.add(sldData);

        SelectedFiles selectedFiles = new SelectedFiles();
        selectedFiles.setSldData(sldFilesToLoad);
        selectedFiles.setFolderName(folder);

        loadSLD.loadSLDString(selectedFiles);
    }

    /**
     * Import feature class.
     *
     * @param featureClassNode the feature class node
     * @return true, if successful
     */
    protected boolean importFeatureClass(DatabaseFeatureClassNode featureClassNode) {
        if (featureClassNode != null) {
            DatabaseClientInterface dbClient = DatabaseConnectionManager.getInstance()
                    .getConnectionMap().get(featureClassNode.getConnectionData());
            if (dbClient != null) {

                ConsoleManager.getInstance().information(this,
                        String.format("%s : %s",
                                Localisation.getString(VectorTool.class, "VectorTool.createSymbol"),
                                featureClassNode.toString()));

                SLDDataInterface sldData = vectorReader.createVectorSLDData(
                        featureClassNode.getConnectionData(), featureClassNode.toString());

                DataSourcePropertiesInterface dsProperties = SLDEditorFile.getInstance()
                        .getDataSource();

                DataSourceConnectorInterface dsc = DataSourceConnectorFactory
                        .getDataSource(dbClient.getClass());

                dsProperties = dsc.getDataSourceProperties(dbClient.getDBConnectionParams());

                dsProperties.setFilename(featureClassNode.toString());

                loadSymbol(dsProperties, sldData, featureClassNode.toString(),
                        featureClassNode.getConnectionData().getConnectionName());
            }
        }
        return false;
    }

    /**
     * Sets the data source.
     *
     * @param fileTreeNode the new data source
     */
    protected void setDataSource(FileTreeNode fileTreeNode) {
        File vectorFile = fileTreeNode.getFile();

        ConsoleManager.getInstance().information(this,
                String.format("%s : %s",
                        Localisation.getString(VectorTool.class, "VectorTool.setDataSource"),
                        vectorFile.getAbsolutePath()));

        // Vector file
        DataSourceConnectorInterface dsc = DataSourceConnectorFactory
                .getDataSource(DataSourceConnector.class);

        DataSourcePropertiesInterface dsProperties = null;
        try {
            dsProperties = dsc.getDataSourceProperties(
                    DataSourceProperties.encodeFilename(vectorFile.toURI().toURL().toString()));
        } catch (MalformedURLException exceptionObj) {
            ConsoleManager.getInstance().exception(VectorTool.class, exceptionObj);
            return;
        }

        SLDEditorFile sldEditorFile = SLDEditorFile.getInstance();
        sldEditorFile.setDataSource(dsProperties);

        DataSourceInterface dataSource = DataSourceFactory.createDataSource(null);

        if (dataSource != null) {
            String dataSourceName = ExternalFilenames.removeSuffix(vectorFile.getName());
            dataSource.connect(dataSourceName, sldEditorFile);
        }
    }

    /**
     * Sets the data source.
     *
     * @param featureClassNode the new data source
     */
    protected void setDataSource(DatabaseFeatureClassNode featureClassNode) {
        DatabaseClientInterface dbClient = DatabaseConnectionManager.getInstance()
                .getConnectionMap().get(featureClassNode.getConnectionData());
        if (dbClient != null) {

            ConsoleManager.getInstance().information(this,
                    String.format("%s : %s",
                            Localisation.getString(VectorTool.class, "VectorTool.setDataSource"),
                            featureClassNode.toString()));

            // Vector file
            DataSourcePropertiesInterface dsProperties = SLDEditorFile.getInstance()
                    .getDataSource();

            DataSourceConnectorInterface dsc = DataSourceConnectorFactory
                    .getDataSource(dbClient.getClass());

            dsProperties = dsc.getDataSourceProperties(dbClient.getDBConnectionParams());

            dsProperties.setFilename(featureClassNode.toString());

            SLDEditorFile.getInstance().setDataSource(dsProperties);

            DataSourceInterface dataSource = DataSourceFactory.createDataSource(null);

            if (dataSource != null) {
                dataSource.connect(featureClassNode.toString(), SLDEditorFile.getInstance());
            }
        }
    }
}
