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

import com.sldeditor.common.console.ConsoleManager;
import com.sldeditor.datasource.SLDEditorFileInterface;
import com.sldeditor.ui.detail.config.inlinefeature.InlineFeatureUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.geotools.data.DataStore;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.styling.StyledLayerDescriptor;
import org.geotools.styling.UserLayer;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.Name;

/**
 * The Class CreateInlineDataSource.
 *
 * @author Robert Ward (SCISYS)
 */
public class CreateInlineDataSource implements CreateDataSourceInterface {

    /** The data source info. */
    private List<DataSourceInfo> dataSourceInfoList = new ArrayList<>();

    /**
     * Creates the data source.
     *
     * @param typeName the type name
     * @param geometryFieldName the geometry field name
     * @param editorFile the editor file
     * @return the list of data stores
     */
    @Override
    public List<DataSourceInfo> connect(
            String typeName, String geometryFieldName, SLDEditorFileInterface editorFile) {
        for (DataSourceInfo dsInfo : dataSourceInfoList) {
            dsInfo.reset();
        }
        dataSourceInfoList.clear();

        if (editorFile != null) {
            StyledLayerDescriptor sld = editorFile.getSLD();

            List<UserLayer> userLayerList = InlineFeatureUtils.extractUserLayers(sld);

            for (UserLayer userLayer : userLayerList) {
                DataSourceInfo dsInfo = new DataSourceInfo();
                dsInfo.setUserLayer(userLayer);
                dataSourceInfoList.add(dsInfo);
                DataStore dataStore = userLayer.getInlineFeatureDatastore();
                if (dataStore == null) {
                    continue;
                }

                try {
                    List<Name> nameList = dataStore.getNames();
                    if (!nameList.isEmpty()) {
                        typeName = nameList.get(0).getLocalPart();
                    }

                    // Set the type name
                    dsInfo.setTypeName(typeName);

                    SimpleFeatureSource source = dataStore.getFeatureSource(typeName);
                    SimpleFeatureType schema = source.getSchema();
                    dsInfo.setSchema(schema);

                    dsInfo.setDataStore(dataStore);

                    GeometryTypeEnum geometryType =
                            InlineFeatureUtils.determineGeometryType(
                                    schema.getGeometryDescriptor(), source.getFeatures());

                    dsInfo.setGeometryType(geometryType);
                } catch (IOException e) {
                    ConsoleManager.getInstance().exception(this, e);
                    dsInfo.reset();
                }
            }
        }
        return dataSourceInfoList;
    }
}
