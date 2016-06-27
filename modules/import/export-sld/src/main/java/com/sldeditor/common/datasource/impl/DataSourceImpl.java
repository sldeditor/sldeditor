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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.geotools.data.DataAccessFactory;
import org.geotools.data.DataStoreFactorySpi;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.FeatureSource;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.Name;
import org.opengis.feature.type.PropertyDescriptor;

import com.sldeditor.common.datasource.DataSourceFieldInterface;
import com.sldeditor.common.datasource.DataSourceInterface;
import com.sldeditor.common.datasource.DataSourcePropertiesInterface;
import com.sldeditor.common.datasource.DataSourceUpdatedInterface;
import com.sldeditor.common.datasource.attribute.AllowedAttributeTypes;
import com.sldeditor.common.datasource.attribute.DataSourceAttributeData;
import com.sldeditor.common.datasource.attribute.DataSourceAttributeListInterface;

/**
 * Class that represents an SLD Editor data source. Provides functionality to read and update its schema.
 * 
 * @author Robert Ward (SCISYS)
 */
public class DataSourceImpl implements DataSourceInterface {

    /** The logger. */
    private static Logger logger = Logger.getLogger(DataSourceImpl.class);

    /** The listener list. */
    private List<DataSourceUpdatedInterface> listenerList = new ArrayList<DataSourceUpdatedInterface>();

    /** The data source info. */
    private DataSourceInfo dataSourceInfo = new DataSourceInfo();

    /** The example data source info, used to draw the single rendered symbol. */
    private DataSourceInfo exampleDataSourceInfo = new DataSourceInfo();

    /** The field name map. */
    private Map<Integer, Name> fieldNameMap = new HashMap<Integer, Name>();

    /** The field type map. */
    private Map<Integer, Class<?> > fieldTypeMap = new HashMap<Integer, Class<?> >();

    /** The data source properties. */
    private DataSourcePropertiesInterface dataSourceProperties = null;

    /** The connected to data source flag. */
    private boolean connectedToDataSourceFlag = false;

    /** The available data stores. */
    private List<String> availableDataStoreList = new ArrayList<String>();

    /** The geometry field name. */
    @SuppressWarnings("unused")
    private String geometryFieldName = null;

    /**
     * Default constructor.
     */
    public DataSourceImpl()
    {
        populateAvailableDataStores();
    }

    /**
     * Adds the data source updated listener.
     *
     * @param listener the listener
     */
    @Override
    public void addListener(DataSourceUpdatedInterface listener)
    {
        if(!listenerList.contains(listener))
        {
            listenerList.add(listener);

            if(getGeometryType() != GeometryTypeEnum.UNKNOWN)
            {
                notifyDataSourceLoaded();
            }
        }
    }

    /**
     * Removes the data source updated listener.
     *
     * @param listener the listener
     */
    @Override
    public void removeListener(DataSourceUpdatedInterface listener)
    {
        listenerList.remove(listener);
    }

    /**
     * Populates the list of available data stores that can be connected to.
     */
    private void populateAvailableDataStores()
    {
        DataAccessFactory fac;

        logger.debug("Available data store factories:");

        Iterator<DataStoreFactorySpi> iterator = DataStoreFinder.getAvailableDataStores();
        while (iterator.hasNext()) {
            fac = (DataAccessFactory) iterator.next();

            logger.debug("\t" + fac.getDisplayName());

            availableDataStoreList.add(fac.getDisplayName());
        }
    }

    /**
     * Populate field map for the data source.
     */
    private void populateFieldMap() {
        if(dataSourceInfo != null)
        {
            geometryFieldName = dataSourceInfo.getGeometryFieldName();

            fieldNameMap.clear();
            fieldTypeMap.clear();

            logger.debug("Datasource fields:");
            int index = 0;
            Collection<PropertyDescriptor> descriptorList = dataSourceInfo.getPropertyDescriptorList();
            if(descriptorList != null)
            {
                for(PropertyDescriptor property : descriptorList)
                {
                    logger.debug(String.format("    %-20s %s", property.getName(), property.getType().getBinding().getName()));
                    fieldNameMap.put(index, property.getName());
                    fieldTypeMap.put(index, property.getType().getBinding());
                    index ++;
                }
            }
        }
    }

    /**
     * Unload data store.
     */
    private void unloadDataStore()
    {
        if(dataSourceInfo != null)
        {
            dataSourceInfo.unloadDataStore();
        }

        if(exampleDataSourceInfo != null)
        {
            exampleDataSourceInfo.unloadDataStore();
        }
    }

    /**
     * Gets the feature source.
     *
     * @return the feature source
     */
    /* (non-Javadoc)
     * @see com.sldeditor.datasource.impl.DataSourceInterface#getFeatureSource()
     */
    @Override
    public FeatureSource<SimpleFeatureType, SimpleFeature> getFeatureSource() {
        FeatureSource<SimpleFeatureType, SimpleFeature> features = dataSourceInfo.getFeatures();

        return features;
    }

    /**
     * Gets the example feature source.
     *
     * @return the example feature source
     */
    /* (non-Javadoc)
     * @see com.sldeditor.datasource.impl.DataSourceInterface#getExampleFeatureSource()
     */
    @Override
    public FeatureSource<SimpleFeatureType, SimpleFeature> getExampleFeatureSource() {
        FeatureSource<SimpleFeatureType, SimpleFeature> features = exampleDataSourceInfo.getFeatures();

        return features;
    }

    /**
     * Gets the attributes.
     *
     * @param expectedDataType the expected data type
     * @return the attributes
     */
    /* (non-Javadoc)
     * @see com.sldeditor.datasource.impl.DataSourceInterface#getAttributes(java.lang.Class)
     */
    @Override
    public List<String> getAttributes(Class<?> expectedDataType) {
        List<String> attributeNameList = new ArrayList<String>();

        Collection<PropertyDescriptor> descriptorList = getPropertyDescriptorList();

        if(descriptorList != null)
        {
            for(PropertyDescriptor property : descriptorList)
            {
                Class<?> bindingType = property.getType().getBinding();
                if(AllowedAttributeTypes.isAllowed(bindingType, expectedDataType))
                {
                    attributeNameList.add(property.getName().toString());
                }
            }
        }
        return attributeNameList;
    }

    /**
     * Gets the geometry type.
     *
     * @return the geometry type
     */
    /* (non-Javadoc)
     * @see com.sldeditor.datasource.DataSourceInterface#getGeometryType()
     */
    @Override
    public GeometryTypeEnum getGeometryType() {
        return (dataSourceInfo != null) ? dataSourceInfo.getGeometryType() : GeometryTypeEnum.UNKNOWN;
    }

    /**
     * Read attributes.
     *
     * @param attributeData the attribute data
     */
    /* (non-Javadoc)
     * @see com.sldeditor.datasource.DataSourceInterface#updateAttributes(com.sldeditor.render.iface.RenderAttributeDataInterface)
     */
    @Override
    public void readAttributes(DataSourceAttributeListInterface attributeData) {
        if(attributeData == null)
        {
            return;
        }

        List<DataSourceAttributeData> valueMap = new ArrayList<DataSourceAttributeData>();

        SimpleFeatureCollection featureCollection = dataSourceInfo.getFeatureCollection();
        if(featureCollection != null)
        {
            SimpleFeatureIterator iterator = featureCollection.features();
            if(iterator.hasNext())
            {
                SimpleFeature feature = iterator.next();

                List<Object> attributes = feature.getAttributes();
                for (int i = 0; i < attributes.size(); i++)
                {
                    Name fieldName = fieldNameMap.get(i);

                    DataSourceAttributeData data = new DataSourceAttributeData(fieldName,
                            fieldTypeMap.get(i),
                            attributes.get(i));

                    valueMap.add(data);
                }
            }
        }

        attributeData.setData(valueMap);
    }

    /**
     * Reset.
     */
    /* (non-Javadoc)
     * @see com.sldeditor.datasource.DataSourceInterface#reset()
     */
    @Override
    public void reset() {
        unloadDataStore();

        dataSourceInfo.reset();
        dataSourceProperties = null;

        if(exampleDataSourceInfo != null)
        {
            exampleDataSourceInfo.reset();
            exampleDataSourceInfo = null;
        }
    }

    /**
     * Notify data source loaded.
     */
    private void notifyDataSourceLoaded()
    {
        List<DataSourceUpdatedInterface> copyListenerList = new ArrayList<DataSourceUpdatedInterface>(listenerList);
        for(DataSourceUpdatedInterface listener : copyListenerList)
        {
            listener.dataSourceLoaded(getGeometryType(), this.connectedToDataSourceFlag);
        }
    }

    /**
     * Gets the data connector properties.
     *
     * @return the data connector properties
     */
    /* (non-Javadoc)
     * @see com.sldeditor.datasource.DataSourceInterface#getDataConnectorProperties()
     */
    @Override
    public DataSourcePropertiesInterface getDataConnectorProperties()
    {
        return dataSourceProperties;
    }

    /**
     * Gets the available data store list.
     *
     * @return the available data store list
     */
    /* (non-Javadoc)
     * @see com.sldeditor.datasource.DataSourceInterface#getAvailableDataStoreList()
     */
    @Override
    public List<String> getAvailableDataStoreList()
    {
        return availableDataStoreList;
    }

    /**
     * Update fields.
     *
     * @param attributeData the attribute data
     */
    /* (non-Javadoc)
     * @see com.sldeditor.datasource.DataSourceInterface#updateFields(com.sldeditor.render.iface.RenderAttributeDataInterface)
     */
    @Override
    public void updateFields(DataSourceAttributeListInterface attributeData) {
    }

    /**
     * Adds the field.
     *
     * @param dataSourceField the data source field
     */
    @Override
    public void addField(DataSourceFieldInterface dataSourceField) {
    }

    /**
     * Gets the property descriptor list.
     *
     * @return the property descriptor list
     */
    @Override
    public Collection<PropertyDescriptor> getPropertyDescriptorList() {
        if(dataSourceInfo != null)
        {
            return dataSourceInfo.getPropertyDescriptorList();
        }
        return null;
    }

    @Override
    public void connect() {
        // TODO Auto-generated method stub
        
    }

}
