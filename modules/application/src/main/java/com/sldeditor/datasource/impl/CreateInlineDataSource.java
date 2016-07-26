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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.geotools.data.DataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.styling.StyledLayerDescriptor;
import org.geotools.styling.UserLayer;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.feature.type.Name;

import com.sldeditor.common.DataSourceFieldInterface;
import com.sldeditor.common.SLDDataInterface;
import com.sldeditor.common.console.ConsoleManager;
import com.sldeditor.datasource.SLDEditorFileInterface;
import com.sldeditor.ui.detail.config.inlinefeature.InlineFeatureUtils;
import com.vividsolutions.jts.geom.Geometry;

/**
 * The Class CreateInlineDataSource.
 *
 * @author Robert Ward (SCISYS)
 */
public class CreateInlineDataSource implements CreateDataSourceInterface {

    /** The data source info. */
    private DataSourceInfo dsInfo = new DataSourceInfo();

    /**
     * Creates the data source.
     *
     * @param editorFile the editor file
     * @return the data source info
     */
    @Override
    public DataSourceInfo connect(SLDEditorFileInterface editorFile)
    {
        dsInfo.reset();

        if(editorFile != null)
        {
            StyledLayerDescriptor sld = editorFile.getSLD();
            SLDDataInterface sldData = editorFile.getSLDData();

            List<UserLayer> userLayerList = InlineFeatureUtils.extractUserLayers(sld);

            UserLayer userLayer = userLayerList.get(0);

            DataStore dataStore = userLayer.getInlineFeatureDatastore();
            try {
                // Set the type name
                String typeName = dataStore.getTypeNames()[0];
                dsInfo.setTypeName(typeName);

                List<DataSourceFieldInterface> fieldList = sldData.getFieldList();

                // Store the fields
                sldData.setFieldList(fieldList);

                SimpleFeatureSource source = dataStore.getFeatureSource(typeName);
                SimpleFeatureType schema = source.getSchema();
                dsInfo.setSchema(schema);

                dsInfo.setDataStore(dataStore);

                GeometryTypeEnum geometryType = InlineFeatureUtils.determineGeometryType(schema.getGeometryDescriptor(),
                        source.getFeatures());

                dsInfo.setGeometryType(geometryType);
            } catch (IOException e) {
                ConsoleManager.getInstance().exception(this, e);
                dsInfo.reset();
            }
        }
        return dsInfo;
    }
}
