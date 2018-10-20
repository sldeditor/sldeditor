/*
 * SLD Editor - The Open Source Java SLD Editor
 *
 * Copyright (C) 2018, SCISYS UK Limited
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
import com.sldeditor.datasource.extension.filesystem.node.file.FileTreeNode;
import com.sldeditor.datasource.impl.DataSourceFactory;
import com.sldeditor.datasource.impl.DataSourceProperties;
import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

/**
 * The Class RasterImport, does all the work of the Raster Tool.
 *
 * @author Robert Ward (SCISYS)
 */
public class RasterImport {

    /** The raster reader. */
    private RasterReaderInterface rasterReader = new RasterReader();

    /**
     * Import raster.
     *
     * @param nodeTypeList the node type list
     * @param sldEditorInterface the sld editor interface
     */
    public void importRaster(
            List<NodeInterface> nodeTypeList, SLDEditorInterface sldEditorInterface) {
        if ((nodeTypeList != null) && (nodeTypeList.size() == 1)) {
            if (sldEditorInterface != null) {
                FileTreeNode fileTreeNode = (FileTreeNode) nodeTypeList.get(0);

                File rasterFile = fileTreeNode.getFile();
                ConsoleManager.getInstance()
                        .information(
                                this,
                                String.format(
                                        "%s : %s",
                                        Localisation.getString(
                                                RasterTool.class, "RasterTool.createSymbol"),
                                        rasterFile.getAbsolutePath()));
                SLDDataInterface sldData = rasterReader.createRasterSLDData(rasterFile);

                // Raster file
                DataSourceConnectorInterface dsc = DataSourceConnectorFactory.getDataSource();

                DataSourcePropertiesInterface dsProperties =
                        dsc.getDataSourceProperties(
                                DataSourceProperties.encodeFilename(rasterFile.getAbsolutePath()));

                SLDEditorFile.getInstance().setSLDData(sldData);
                SLDEditorFile.getInstance().setDataSource(dsProperties);

                // Clear the data change flag
                SLDEditorFile.getInstance().fileOpenedSaved();

                // Load sld
                List<SLDDataInterface> sldFilesToLoad = new ArrayList<>();
                sldFilesToLoad.add(sldData);

                SelectedFiles selectedFiles = new SelectedFiles();
                selectedFiles.setSldData(sldFilesToLoad);
                selectedFiles.setFolderName(rasterFile.getParent());

                LoadSLDInterface loadSLD = sldEditorInterface.getLoadSLDInterface();
                loadSLD.loadSLDString(selectedFiles);
            }
        }
    }

    /**
     * Import raster data source.
     *
     * @param nodeTypeList the node type list
     */
    public void importDataSource(List<NodeInterface> nodeTypeList) {
        if ((nodeTypeList != null) && (nodeTypeList.size() == 1)) {
            FileTreeNode fileTreeNode = (FileTreeNode) nodeTypeList.get(0);

            File rasterFile = fileTreeNode.getFile();

            ConsoleManager.getInstance()
                    .information(
                            this,
                            String.format(
                                    "%s : %s",
                                    Localisation.getString(
                                            RasterTool.class, "RasterTool.setDataSource"),
                                    rasterFile.getAbsolutePath()));

            // Raster file
            DataSourceConnectorInterface dsc = DataSourceConnectorFactory.getDataSource();

            String rasterFilename = null;
            try {
                rasterFilename = rasterFile.toURI().toURL().toString();
            } catch (MalformedURLException exceptionObj) {
                ConsoleManager.getInstance().exception(RasterTool.class, exceptionObj);
                return;
            }
            DataSourcePropertiesInterface dsProperties =
                    dsc.getDataSourceProperties(
                            DataSourceProperties.encodeFilename(rasterFilename));

            SLDEditorFile.getInstance().setDataSource(dsProperties);

            DataSourceInterface dataSource = DataSourceFactory.createDataSource(null);

            if (dataSource != null) {
                dataSource.connect(rasterFilename, SLDEditorFile.getInstance(), null);
            }
        }
    }
}
