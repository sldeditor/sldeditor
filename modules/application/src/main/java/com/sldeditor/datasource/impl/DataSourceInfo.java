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
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.data.DataStore;
import org.geotools.data.FeatureSource;
import org.geotools.data.FeatureStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.NameImpl;
import org.geotools.feature.type.FeatureTypeFactoryImpl;
import org.geotools.styling.UserLayer;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.FeatureTypeFactory;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.feature.type.GeometryType;
import org.opengis.feature.type.Name;
import org.opengis.feature.type.PropertyDescriptor;
import org.opengis.filter.Filter;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.util.InternationalString;

import com.sldeditor.common.console.ConsoleManager;

/**
 * The Class DataSourceInfo.
 *
 * @author Robert Ward (SCISYS)
 */
public class DataSourceInfo {

    /** The logger. */
    private static Logger logger = Logger.getLogger(DataSourceInfo.class);

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

    /** The field name map. */
    private Map<Integer, Name> fieldNameMap = new HashMap<Integer, Name>();

    /** The field type map. */
    private Map<Integer, Class<?>> fieldTypeMap = new HashMap<Integer, Class<?>>();

    /** The user layer. */
    private UserLayer userLayer = null;

    /** The Constant rasterGeometryField. */
    private static final String rasterGeometryField = "grid";

    /** The raster geometry descriptor. */
    private Collection<PropertyDescriptor> rasterPropertyDescriptorList = null;

    /** The feature type factory. */
    private static FeatureTypeFactory featureTypeFactory = new FeatureTypeFactoryImpl();

    /**
     * Default constructor.
     */
    public DataSourceInfo() {
    }

    /**
     * Reset the member data.
     */
    public void reset() {
        schema = null;

        geometryType = GeometryTypeEnum.UNKNOWN;

        typeName = null;

        dataStore = null;

        gridCoverageReader = null;

        fieldNameMap.clear();

        fieldTypeMap.clear();
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
        if (dataStore != null) {
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

        try {
            if ((schema != null) && (dataStore != null)) {
                features = dataStore.getFeatureSource(schema.getName());
            }
        } catch (IOException e) {
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
            if (dataStore != null) {
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

        if (dataStore != null) {
            try {
                FeatureSource<SimpleFeatureType, SimpleFeature> featureSource = dataStore
                        .getFeatureSource(typeName);

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
        if (schema != null) {
            return schema.getDescriptors();
        } else {
            if (geometryType == GeometryTypeEnum.RASTER) {
                if (rasterPropertyDescriptorList == null) {
                    rasterPropertyDescriptorList = new ArrayList<PropertyDescriptor>();

                    CoordinateReferenceSystem crs = null;
                    boolean isIdentifiable = false;
                    boolean isAbstract = false;
                    List<Filter> restrictions = null;
                    AttributeType superType = null;
                    InternationalString description = null;
                    GeometryType type = featureTypeFactory.createGeometryType(
                            new NameImpl(rasterGeometryField), GridCoverage2D.class, crs,
                            isIdentifiable, isAbstract, restrictions, superType, description);
                    GeometryDescriptor descriptor = featureTypeFactory.createGeometryDescriptor(
                            type, new NameImpl(rasterGeometryField), 0, 1, false, null);

                    rasterPropertyDescriptorList.add(descriptor);
                }

                return rasterPropertyDescriptorList;
            }
        }
        return null;
    }

    /**
     * Gets the geometry field name.
     *
     * @return the geometry field name
     */
    public String getGeometryFieldName() {
        if (schema != null) {
            return schema.getGeometryDescriptor().getLocalName();
        } else {
            if (geometryType == GeometryTypeEnum.RASTER) {
                return rasterGeometryField;
            }
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

    /**
     * Gets the field name map.
     *
     * @return the fieldNameMap
     */
    public Map<Integer, Name> getFieldNameMap() {
        return fieldNameMap;
    }

    /**
     * Gets the field type map.
     *
     * @return the fieldTypeMap
     */
    public Map<Integer, Class<?>> getFieldTypeMap() {
        return fieldTypeMap;
    }

    /**
     * Populate field map for the data source.
     */
    public void populateFieldMap() {
        fieldNameMap.clear();
        fieldTypeMap.clear();

        logger.debug("Datasource fields:");
        int index = 0;
        Collection<PropertyDescriptor> descriptorList = getPropertyDescriptorList();
        if (descriptorList != null) {
            for (PropertyDescriptor property : descriptorList) {
                if (property != null) {
                    logger.debug(String.format("    %-20s %s", property.getName(),
                            property.getType().getBinding().getName()));
                    fieldNameMap.put(index, property.getName());
                    fieldTypeMap.put(index, property.getType().getBinding());
                }
                index++;
            }
        }
    }

    /**
     * Gets the user layer.
     *
     * @return the userLayer
     */
    public UserLayer getUserLayer() {
        return userLayer;
    }

    /**
     * Sets the user layer.
     *
     * @param userLayer the userLayer to set
     */
    public void setUserLayer(UserLayer userLayer) {
        this.userLayer = userLayer;
    }
}
