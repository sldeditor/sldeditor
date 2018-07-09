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

import com.sldeditor.common.SLDDataInterface;
import com.sldeditor.common.connection.DatabaseConnectionManager;
import com.sldeditor.common.console.ConsoleManager;
import com.sldeditor.common.data.DatabaseConnection;
import com.sldeditor.common.data.SLDData;
import com.sldeditor.common.data.StyleWrapper;
import com.sldeditor.common.defaultsymbol.DefaultSymbols;
import com.sldeditor.common.output.SLDWriterInterface;
import com.sldeditor.common.output.impl.SLDWriterFactory;
import com.sldeditor.common.utils.ExternalFilenames;
import com.sldeditor.datasource.impl.DataSourceProperties;
import com.sldeditor.datasource.impl.GeometryTypeEnum;
import com.sldeditor.datasource.impl.GeometryTypeMapping;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Map;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.styling.StyledLayerDescriptor;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.GeometryType;

/**
 * The Class VectorReader.
 *
 * @author Robert Ward (SCISYS)
 */
public class VectorReader implements VectorReaderInterface {

    /** The sld writer. */
    private SLDWriterInterface sldWriter = SLDWriterFactory.createWriter(null);

    /**
     * Creates the vector sld.
     *
     * @param vectorFile the vector file
     * @return the styled layer descriptor
     */
    @Override
    public SLDDataInterface createVectorSLDData(File vectorFile) {
        if (vectorFile == null) {
            return null;
        }

        Map<String, Object> map = null;
        try {
            map = DataSourceProperties.encodeFilename(vectorFile.toURI().toURL().toString());
        } catch (MalformedURLException e) {
            ConsoleManager.getInstance().exception(this, e);
            return null;
        }

        StyledLayerDescriptor sld = createSLDData(map, null);
        SLDData sldData = null;
        if (sld != null) {
            File sldFilename = ExternalFilenames.createSLDFilename(vectorFile);

            StyleWrapper styleWrapper = new StyleWrapper(sldFilename.getName());
            String sldContents = sldWriter.encodeSLD(null, sld);
            sldData = new SLDData(styleWrapper, sldContents);
            sldData.setSLDFile(sldFilename);
            sldData.setReadOnly(false);
        }

        return sldData;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.tool.vector.VectorReaderInterface#createVectorSLDData(com.sldeditor.common.data.DatabaseConnection, java.lang.String)
     */
    @Override
    public SLDDataInterface createVectorSLDData(
            DatabaseConnection databaseConnection, String featureClass) {
        if ((databaseConnection == null) || (featureClass == null)) {
            return null;
        }

        Map<String, Object> map =
                DatabaseConnectionManager.getInstance().getDBConnectionParams(databaseConnection);

        StyledLayerDescriptor sld = createSLDData(map, featureClass);
        SLDData sldData = null;
        if (sld != null) {
            File sldFilename = null;

            StyleWrapper styleWrapper = new StyleWrapper(featureClass);
            String sldContents = sldWriter.encodeSLD(null, sld);
            sldData = new SLDData(styleWrapper, sldContents);
            sldData.setSLDFile(sldFilename);
            sldData.setReadOnly(false);
        }

        return sldData;
    }

    /**
     * Creates the SLD data.
     *
     * @param map the map
     * @return the styled layer descriptor
     */
    private StyledLayerDescriptor createSLDData(Map<String, Object> map, String featureClass) {
        StyledLayerDescriptor sld = null;

        DataStore dataStore = null;
        try {
            dataStore = DataStoreFinder.getDataStore(map);
        } catch (IOException e) {
            ConsoleManager.getInstance().exception(this, e);
            return null;
        }

        if (dataStore != null) {
            // Try connecting to a vector data source
            String typeName;
            GeometryTypeEnum geometryTypeEnum = GeometryTypeEnum.UNKNOWN;

            try {
                if (featureClass == null) {
                    typeName = dataStore.getTypeNames()[0];
                } else {
                    typeName = featureClass;
                }
                SimpleFeatureSource source = dataStore.getFeatureSource(typeName);
                SimpleFeatureType schema = source.getSchema();

                GeometryType geometryType = schema.getGeometryDescriptor().getType();
                Class<?> bindingType = geometryType.getBinding();
                geometryTypeEnum = GeometryTypeMapping.getGeometryType(bindingType);
            } catch (IOException e) {
                ConsoleManager.getInstance().exception(this, e);
                return null;
            } finally {
                dataStore.dispose();
            }

            switch (geometryTypeEnum) {
                case POINT:
                    sld = DefaultSymbols.createNewPoint();
                    break;
                case LINE:
                    sld = DefaultSymbols.createNewLine();
                    break;
                case POLYGON:
                    sld = DefaultSymbols.createNewPolygon();
                    break;
                default:
                    break;
            }
        }
        return sld;
    }
}
