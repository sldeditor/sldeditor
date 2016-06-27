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
package com.sldeditor.common.datasource.impl;

import java.io.IOException;
import java.util.Collection;

import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.data.DataStore;
import org.geotools.data.FeatureSource;
import org.geotools.data.FeatureStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.PropertyDescriptor;

import com.sldeditor.common.console.ConsoleManager;

/**
 * The Class DataSourceInfo.
 *
 * @author Robert Ward (SCISYS)
 */
public class DataSourceInfo {

    /** The schema. */
    private FeatureType schema = null;

    /** The geometry type. */
    private GeometryTypeEnum geometryType = GeometryTypeEnum.UNKNOWN;

    /** The type name. */
    private String typeName = null;

    /** The data store. */
    private DataStore dataStore = null;

    /** The grid coverage. */
    private AbstractGridCoverage2DReader gridCoverageReader = null;

    /**
     * Default constructor.
     */
    public DataSourceInfo()
    {
    }

    /**
     * Reset the member data.
     */
    public void reset()
    {
        schema = null;

        geometryType = GeometryTypeEnum.UNKNOWN;

        typeName = null;

        dataStore = null;

        gridCoverageReader = null;
    }

    /**
     * Sets the schema.
     *
     * @param schema the schema to set
     */
    public void setSchema(FeatureType schema) {
        this.schema = schema;
    }

    /**
     * Gets the geometry type.
     *
     * @return the geometryType
     */
    public GeometryTypeEnum getGeometryType() {
        return geometryType;
    }

    /**
     * Sets the geometry type.
     *
     * @param geometryType the geometryType to set
     */
    public void setGeometryType(GeometryTypeEnum geometryType) {
        this.geometryType = geometryType;
    }

    /**
     * Gets the type name.
     *
     * @return the typeName
     */
    public String getTypeName() {
        return typeName;
    }

    /**
     * Sets the type name.
     *
     * @param typeName the typeName to set
     */
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    /**
     * Gets the data store.
     *
     * @return the dataStore
     */
    public DataStore getDataStore() {
        return dataStore;
    }

    /**
     * Sets the data store.
     *
     * @param dataStore the dataStore to set
     */
    public void setDataStore(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    /**
     * Unload data store.
     */
    public void unloadDataStore() {
        if(dataStore != null)
        {
            dataStore.dispose();
        }
    }

    /**
     * Gets the features.
     *
     * @return the features
     */
    public FeatureSource<SimpleFeatureType, SimpleFeature> getFeatures() {
        FeatureSource<SimpleFeatureType, SimpleFeature> features = null;

        try
        {
            if((schema != null) && (dataStore != null))
            {
                features = dataStore.getFeatureSource(schema.getName());
            }
        }catch (IOException e) {
            ConsoleManager.getInstance().exception(this, e);
        }

        return features;
    }

    /**
     * Gets the feature collection.
     *
     * @return the feature collection
     */
    public SimpleFeatureCollection getFeatureCollection() {
        SimpleFeatureCollection featureCollection = null;
        try {
            if(dataStore != null)
            {
                SimpleFeatureSource source = dataStore.getFeatureSource(typeName);
                featureCollection = source.getFeatures();
            }
        } catch (IOException e) {
            ConsoleManager.getInstance().exception(this, e);
        }
        return featureCollection;
    }

    /**
     * Gets the feature store.
     *
     * @return the feature store
     */
    public FeatureStore<SimpleFeatureType, SimpleFeature> getFeatureStore() {
        FeatureStore<SimpleFeatureType, SimpleFeature> featureStore = null;

        if(dataStore != null)
        {
            try
            {
                FeatureSource<SimpleFeatureType, SimpleFeature> featureSource = dataStore.getFeatureSource(typeName);

                featureStore = (FeatureStore<SimpleFeatureType, SimpleFeature>) featureSource;
            } catch (IOException e) {
                ConsoleManager.getInstance().exception(this, e);
            }
        }
        return featureStore;
    }

    /**
     * Gets the property descriptor list.
     *
     * @return the property descriptor list
     */
    public Collection<PropertyDescriptor> getPropertyDescriptorList() {
        if(schema != null)
        {
            return schema.getDescriptors();
        }
        return null;
    }

    /**
     * Gets the geometry field name.
     *
     * @return the geometry field name
     */
    public String getGeometryFieldName() {
        if(schema != null)
        {
            return schema.getGeometryDescriptor().getLocalName();
        }

        return null;
    }

    /**
     * Gets the grid coverage reader.
     *
     * @return the gridCoverage reader
     */
    public AbstractGridCoverage2DReader getGridCoverageReader() {
        return gridCoverageReader;
    }

    /**
     * Sets the grid coverage reader.
     *
     * @param gridCoverage the gridCoverage to set
     */
    public void setGridCoverageReader(AbstractGridCoverage2DReader gridCoverage) {
        this.gridCoverageReader = gridCoverage;
        geometryType = GeometryTypeEnum.RASTER;
    }

    /**
     * Checks for data.
     *
     * @return true, if successful
     */
    public boolean hasData() {
        return (dataStore != null) || (gridCoverageReader != null);
    }

}
