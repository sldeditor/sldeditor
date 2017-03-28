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

package com.sldeditor.datasource.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.swing.dialog.JCRSChooser;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.GeometryType;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.sldeditor.common.Controller;
import com.sldeditor.common.DataSourceConnectorInterface;
import com.sldeditor.common.DataSourcePropertiesInterface;
import com.sldeditor.common.SLDDataInterface;
import com.sldeditor.common.console.ConsoleManager;
import com.sldeditor.common.coordinate.CoordManager;
import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.common.utils.ExternalFilenames;
import com.sldeditor.datasource.SLDEditorFileInterface;
import com.sldeditor.datasource.chooseraster.ChooseRasterFormatInterface;
import com.sldeditor.datasource.chooseraster.ChooseRasterFormatPanel;
import com.sldeditor.datasource.chooseraster.DetermineRasterFormat;

/**
 * The Class CreateExternalDataSource.
 *
 * @author Robert Ward (SCISYS)
 */
public class CreateExternalDataSource implements CreateDataSourceInterface {

    /** The data source info. */
    private DataSourceInfo dsInfo = new DataSourceInfo();

    /** The default crs. */
    private CoordinateReferenceSystem defaultCRS;

    /** The logger. */
    private static Logger logger = Logger.getLogger(CreateExternalDataSource.class);

    /**
     * Default constructor.
     */
    public CreateExternalDataSource() {
        defaultCRS = CoordManager.getInstance().getWGS84();
    }

    /**
     * Connect.
     *
     * @param typeName the type name
     * @param geometryFieldName the geometry field name
     * @param editorFile the editor file
     * @return the list of datastores
     */
    @Override
    public List<DataSourceInfo> connect(String typeName, String geometryFieldName,
            SLDEditorFileInterface editorFile) {
        List<DataSourceInfo> dataSourceInfoList = new ArrayList<DataSourceInfo>();
        dataSourceInfoList.add(dsInfo);

        dsInfo.reset();

        if (editorFile != null) {
            SLDDataInterface sldData = editorFile.getSLDData();

            DataSourcePropertiesInterface dataSourceProperties = sldData.getDataSourceProperties();

            Map<String, Object> map = dataSourceProperties.getConnectionProperties();

            if (dataSourceProperties.hasPassword()) {
                String password = dataSourceProperties.getPassword();

                if (password == null) {
                    password = "dummy password";

                    dataSourceProperties.setPassword(password);

                    map = dataSourceProperties.getConnectionProperties();
                }
            }

            DataStore dataStore = null;
            try {
                dataStore = DataStoreFinder.getDataStore(map);

                if (dataStore != null) {
                    // Try connecting to a vector data source
                    dsInfo.setTypeName(typeName);

                    SimpleFeatureSource source = dataStore.getFeatureSource(typeName);
                    SimpleFeatureType schema = source.getSchema();

                    if (schema.getCoordinateReferenceSystem() == null) {
                        // No crs found to set a default and reload
                        if (dataStore instanceof ShapefileDataStore) {
                            ShapefileDataStore shapeFileDatastore = (ShapefileDataStore) dataStore;

                            CoordinateReferenceSystem crs = JCRSChooser.showDialog(
                                    Localisation.getString(CreateExternalDataSource.class,
                                            "CRSPanel.title"),
                                    defaultCRS.getIdentifiers().iterator().next().toString());
                            if (crs != null) {
                                shapeFileDatastore.forceSchemaCRS(crs);
                            }

                            source = dataStore.getFeatureSource(typeName);
                            schema = source.getSchema();
                        }
                    }
                    dsInfo.setSchema(schema);

                    determineGeometryType(schema.getGeometryDescriptor().getType());
                } else {
                    // Try connecting to a raster data source
                    Object rasterFilename = map.get(DataSourceConnectorInterface.FILE_MAP_KEY);
                    if (rasterFilename != null) {
                        File rasterFile = new File(
                                ExternalFilenames.convertURLToFile((String) rasterFilename));

                        ChooseRasterFormatInterface panel = new ChooseRasterFormatPanel(
                                Controller.getInstance().getFrame());

                        AbstractGridFormat format = DetermineRasterFormat.choose(rasterFile, panel);
                        AbstractGridCoverage2DReader reader = format.getReader(rasterFile);

                        dsInfo.setGridCoverageReader(reader);
                    } else {
                        logger.error("No matching datastore");
                    }
                }
            } catch (IOException e) {
                ConsoleManager.getInstance().exception(this, e);
            }

            dsInfo.setDataStore(dataStore);

            if (!dsInfo.hasData()) {
                ConsoleManager.getInstance().error(this,
                        Localisation.getField(CreateExternalDataSource.class,
                                "CreateExternalDataSource.failedToConnect")
                                + dataSourceProperties.getDebugConnectionString());
            }
        }
        return dataSourceInfoList;
    }

    /**
     * Determine geometry type.
     *
     * @param type the type
     */
    private void determineGeometryType(GeometryType type) {
        Class<?> bindingType = type.getBinding();

        dsInfo.setGeometryType(GeometryTypeMapping.getGeometryType(bindingType));
    }

}
