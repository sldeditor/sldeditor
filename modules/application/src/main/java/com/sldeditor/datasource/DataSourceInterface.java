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
package com.sldeditor.datasource;

import java.util.Collection;
import java.util.List;

import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.data.FeatureSource;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.PropertyDescriptor;

import com.sldeditor.common.DataSourceFieldInterface;
import com.sldeditor.common.DataSourcePropertiesInterface;
import com.sldeditor.datasource.attribute.DataSourceAttributeListInterface;
import com.sldeditor.datasource.impl.CreateDataSourceInterface;
import com.sldeditor.datasource.impl.GeometryTypeEnum;

/**
 * The Interface DataSourceInterface.
 * 
 * @author Robert Ward (SCISYS)
 */
public interface DataSourceInterface {

    /**
     * Adds the listener.
     *
     * @param listener the listener
     */
    void addListener(DataSourceUpdatedInterface listener);

    /**
     * Removes the listener.
     *
     * @param listener the listener
     */
    void removeListener(DataSourceUpdatedInterface listener);

    /**
     * Connect to data source.
     *
     * @param editorFile the editor file
     */
    void connect(SLDEditorFileInterface editorFile);

    /**
     * Reset.
     */
    void reset();

    /**
     * Gets the feature source.
     *
     * @return the feature source
     */
    FeatureSource<SimpleFeatureType, SimpleFeature> getFeatureSource();

    /**
     * Gets the example feature source.
     *
     * @return the example feature source
     */
    FeatureSource<SimpleFeatureType, SimpleFeature> getExampleFeatureSource();

    /**
     * Gets the attributes.
     *
     * @param expectedDataType the expected data type
     * @return the attributes
     */
    List<String> getAttributes(Class<?> expectedDataType);

    /**
     * Gets the geometry type.
     *
     * @return the geometry type
     */
    GeometryTypeEnum getGeometryType();

    /**
     * Read attributes from feature.
     *
     * @param attributeData the attribute data
     */
    void readAttributes(DataSourceAttributeListInterface attributeData);

    /**
     * Gets the data connector properties.
     *
     * @return the data connector properties
     */
    DataSourcePropertiesInterface getDataConnectorProperties();

    /**
     * Gets the available data store list.
     *
     * @return the available data store list
     */
    List<String> getAvailableDataStoreList();

    /**
     * Update fields.
     *
     * @param attributeData the attribute data
     */
    void updateFields(DataSourceAttributeListInterface attributeData);

    /**
     * Adds the field to the data source.
     *
     * @param dataSourceField the data source field
     */
    void addField(DataSourceFieldInterface dataSourceField);

    /**
     * Sets the data source creation lcasses.
     *
     * @param internalDataSource the internal data source
     * @param externalDataSource the external data source
     */
    void setDataSourceCreation(CreateDataSourceInterface internalDataSource,
            CreateDataSourceInterface externalDataSource);

    /**
     * Gets the property descriptor list.
     *
     * @return the property descriptor list
     */
    Collection<PropertyDescriptor> getPropertyDescriptorList();

    /**
     * Gets the grid coverage reader.
     *
     * @return the grid coverage reader
     */
    AbstractGridCoverage2DReader getGridCoverageReader();

}
