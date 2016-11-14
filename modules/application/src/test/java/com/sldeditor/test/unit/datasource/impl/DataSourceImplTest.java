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
package com.sldeditor.test.unit.datasource.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.geotools.data.DataStore;
import org.geotools.data.FeatureSource;
import org.geotools.feature.type.GeometryDescriptorImpl;
import org.geotools.styling.UserLayer;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.PropertyDescriptor;

import com.sldeditor.common.DataSourcePropertiesInterface;
import com.sldeditor.datasource.DataSourceUpdatedInterface;
import com.sldeditor.datasource.attribute.DataSourceAttributeData;
import com.sldeditor.datasource.attribute.DataSourceAttributeList;
import com.sldeditor.datasource.impl.CreateDataSourceInterface;
import com.sldeditor.datasource.impl.CreateExternalDataSource;
import com.sldeditor.datasource.impl.CreateInlineDataSource;
import com.sldeditor.datasource.impl.CreateInternalDataSource;
import com.sldeditor.datasource.impl.DataSourceImpl;
import com.sldeditor.datasource.impl.GeometryTypeEnum;

/**
 * Unit test for DataSourceImpl.
 * <p>{@link com.sldeditor.datasource.impl.DataSourceImpl()}
 * 
 * @author Robert Ward (SCISYS)
 *
 */
public class DataSourceImplTest {

    class DummyDataSourceUpdate implements DataSourceUpdatedInterface
    {
        public GeometryTypeEnum geometryType = GeometryTypeEnum.UNKNOWN;
        public boolean isConnectedToDataSourceFlag = false;

        private boolean hasBeenCalled = false;

        @Override
        public void dataSourceLoaded(GeometryTypeEnum geometryType,
                boolean isConnectedToDataSourceFlag) {
            this.geometryType = geometryType;
            this.isConnectedToDataSourceFlag = isConnectedToDataSourceFlag;
            this.hasBeenCalled = true;
        }

        public boolean hasBeenCalled()
        {
            boolean tmp = hasBeenCalled;
            hasBeenCalled = false;

            return tmp;
        }

        /* (non-Javadoc)
         * @see com.sldeditor.datasource.DataSourceUpdatedInterface#dataSourceAboutToUnloaded(org.geotools.data.DataStore)
         */
        @Override
        public void dataSourceAboutToUnloaded(DataStore dataStore) {
            // Does nothing
        }
    }

    /**
     * Test method for {@link com.sldeditor.datasource.impl.DataSourceImpl#connect()}.
     * Test method for {@link com.sldeditor.datasource.impl.DataSourceImpl#addListener(com.sldeditor.datasource.DataSourceUpdatedInterface)}.
     */
    @Test
    public void testConnectToInternalDataSource() {
        DataSourceImpl ds = new DataSourceImpl();

        DummyInternalSLDFile editorFile = new DummyInternalSLDFile();
        DummyDataSourceUpdate dataSourceUpdateListener = new DummyDataSourceUpdate();
        ds.addListener(dataSourceUpdateListener);
        ds.addListener(dataSourceUpdateListener);

        CreateDataSourceInterface internalDataSource = new CreateInternalDataSource();
        CreateDataSourceInterface externalDataSource = new DummyCreateDataSource();
        CreateDataSourceInterface inlineDataSource = new DummyCreateDataSource();

        ds.setDataSourceCreation(internalDataSource, externalDataSource, inlineDataSource);
        ds.connect(editorFile);

        assertEquals(GeometryTypeEnum.POINT, dataSourceUpdateListener.geometryType);
        assertFalse(dataSourceUpdateListener.isConnectedToDataSourceFlag);

        Collection<PropertyDescriptor> fieldList = ds.getPropertyDescriptorList();
        assertTrue(fieldList != null);

        List<String> actualFieldnameList = new ArrayList<String>();
        for(PropertyDescriptor field : fieldList)
        {
            if(!(field instanceof GeometryDescriptorImpl))
            {
                actualFieldnameList.add(field.getName().getLocalPart());
            }
        }

        // Check fields extracted ok
        List<String> expectedFieldList = editorFile.getExpectedFieldList();
        assertTrue(expectedFieldList.size() == actualFieldnameList.size());

        // Not assuming fields are in the same order
        int count = 0;
        for(String fieldName : actualFieldnameList)
        {
            if(expectedFieldList.contains(fieldName))
            {
                count ++;
            }
        }
        assertTrue(expectedFieldList.size() == count);

        // Check for fields of certain types
        assertFalse(ds.getAttributes(Integer.class).isEmpty());
        assertFalse(ds.getAttributes(Double.class).isEmpty());

        assertEquals(2, ds.getAttributes(String.class).size());

        // Add new field
        DataSourceAttributeData dataSourceField = new DataSourceAttributeData("bearing", Double.class, null);
        ds.addField(dataSourceField);
        assertTrue(ds.getAttributes(Double.class).size() == 2);

        // Update field
        DataSourceAttributeList attributeData = new DataSourceAttributeList();
        ds.readAttributes(null);
        ds.readAttributes(attributeData);

        assertTrue(ds.getPropertyDescriptorList().size() == attributeData.getData().size());

        List<DataSourceAttributeData> attributeDataList = attributeData.getData();

        DataSourceAttributeData data = attributeDataList.remove(2);
        data.setType(Integer.class);
        attributeDataList.add(2, data);

        ds.updateFields(null);
        ds.updateFields(attributeData);
        List<String> actualAttributes = ds.getAttributes(Integer.class);
        assertTrue(actualAttributes.size() == 2);

        FeatureSource<SimpleFeatureType, SimpleFeature> features = ds.getFeatureSource();
        try {
            assertEquals(1, features.getFeatures().size());
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
        assertFalse(dataSourceUpdateListener.isConnectedToDataSourceFlag);

        ds.removeListener(dataSourceUpdateListener);
        assertFalse(dataSourceUpdateListener.isConnectedToDataSourceFlag);
    }

    /**
     * Test method for {@link com.sldeditor.datasource.impl.DataSourceImpl#getAvailableDataStoreList()}.
     */
    @Test
    public void testGetAvailableDataStoreList() {
        DataSourceImpl ds = new DataSourceImpl();
        assertTrue(ds.getAvailableDataStoreList().size() != 0);

        System.out.println(ds.getAvailableDataStoreList());
    }

    /**
     * Test method for {@link com.sldeditor.datasource.impl.DataSourceImpl#connect()}.
     */
    @Test
    public void testConnectToExternalDataSource() {
        DataSourceImpl ds = new DataSourceImpl();

        DummyExternalSLDFile editorFile = new DummyExternalSLDFile();
        DummyDataSourceUpdate dataSourceUpdateListener = new DummyDataSourceUpdate();
        ds.addListener(dataSourceUpdateListener);

        CreateDataSourceInterface internalDataSource = new DummyCreateDataSource();
        CreateDataSourceInterface externalDataSource = new CreateExternalDataSource();
        CreateDataSourceInterface inlineDataSource = new DummyCreateDataSource();

        ds.setDataSourceCreation(internalDataSource, externalDataSource, inlineDataSource);
        ds.connect(editorFile);

        assertEquals(GeometryTypeEnum.POINT, dataSourceUpdateListener.geometryType);
        assertTrue(dataSourceUpdateListener.isConnectedToDataSourceFlag);

        Collection<PropertyDescriptor> fieldList = ds.getPropertyDescriptorList();
        assertTrue(fieldList != null);

        List<String> actualFieldnameList = new ArrayList<String>();
        for(PropertyDescriptor field : fieldList)
        {
            actualFieldnameList.add(field.getName().getLocalPart());
        }

        // Check fields extracted ok
        List<String> expectedFieldList = editorFile.getExpectedFieldList();
        assertTrue(expectedFieldList.size() == actualFieldnameList.size());

        // Not assuming fields are in the same order
        int count = 0;
        for(String fieldName : actualFieldnameList)
        {
            if(expectedFieldList.contains(fieldName))
            {
                count ++;
            }
        }
        assertTrue(expectedFieldList.size() == count);

        // Check for fields of certain types
        assertEquals(1, ds.getAttributes(Integer.class).size());
        assertEquals(1, ds.getAttributes(Long.class).size());
        assertEquals(1, ds.getAttributes(Double.class).size());
        assertEquals(1, ds.getAttributes(String.class).size());

        // Add new field - shouldn't work because connections to external data sources are fixed
        DataSourceAttributeData dataSourceField = new DataSourceAttributeData("bearing", Double.class, null);
        ds.addField(dataSourceField);
        assertTrue(ds.getAttributes(Double.class).size() == 1);

        // Update field
        DataSourceAttributeList attributeData = new DataSourceAttributeList();
        ds.readAttributes(attributeData);

        assertTrue(ds.getPropertyDescriptorList().size() == attributeData.getData().size());

        List<DataSourceAttributeData> attributeDataList = attributeData.getData();

        DataSourceAttributeData data = attributeDataList.remove(2);
        data.setType(Integer.class);
        attributeDataList.add(2, data);

        // Update fields - shouldn't work because connections to external data sources are fixed
        ds.updateFields(attributeData);
        assertTrue(ds.getAttributes(Integer.class).size() == 1);

        FeatureSource<SimpleFeatureType, SimpleFeature> features = ds.getFeatureSource();
        try {
            assertTrue(features.getFeatures().size() > 1);
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
        assertTrue(dataSourceUpdateListener.isConnectedToDataSourceFlag);
    }

    /**
     * Test method for {@link com.sldeditor.datasource.impl.DataSourceImpl#connect()}.
     */
    @Test
    public void testConnectToInlineDataSource() {
        DataSourceImpl ds = new DataSourceImpl();

        DummyInlineSLDFile editorFile = new DummyInlineSLDFile();
        DummyDataSourceUpdate dataSourceUpdateListener = new DummyDataSourceUpdate();
        ds.addListener(dataSourceUpdateListener);

        CreateDataSourceInterface internalDataSource = new DummyCreateDataSource();
        CreateDataSourceInterface externalDataSource = new DummyCreateDataSource();
        CreateDataSourceInterface inlineDataSource = new CreateInlineDataSource();

        ds.setDataSourceCreation(internalDataSource, externalDataSource, inlineDataSource);
        ds.connect(editorFile);

        assertEquals(GeometryTypeEnum.UNKNOWN, dataSourceUpdateListener.geometryType);
        assertFalse(dataSourceUpdateListener.isConnectedToDataSourceFlag);

        Collection<PropertyDescriptor> fieldList = ds.getPropertyDescriptorList();
        assertNull(fieldList);

        FeatureSource<SimpleFeatureType, SimpleFeature> exampleLayer = ds.getExampleFeatureSource();
        assertNull(exampleLayer);

        Map<UserLayer, FeatureSource<SimpleFeatureType, SimpleFeature>> userLayerMap = ds.getUserLayerFeatureSource();
        assertEquals(1, userLayerMap.size());

        assertFalse(dataSourceUpdateListener.hasBeenCalled());

        ds.updateUserLayers();
        assertTrue(dataSourceUpdateListener.hasBeenCalled());

        DataSourcePropertiesInterface dsi = ds.getDataConnectorProperties();
        assertNotNull(dsi);
    }
}
