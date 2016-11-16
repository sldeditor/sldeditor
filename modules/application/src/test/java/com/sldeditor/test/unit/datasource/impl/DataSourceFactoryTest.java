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
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.data.FeatureSource;
import org.geotools.styling.UserLayer;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.PropertyDescriptor;

import com.sldeditor.common.DataSourcePropertiesInterface;
import com.sldeditor.datasource.DataSourceInterface;
import com.sldeditor.datasource.DataSourceUpdatedInterface;
import com.sldeditor.datasource.SLDEditorFileInterface;
import com.sldeditor.datasource.attribute.DataSourceAttributeData;
import com.sldeditor.datasource.attribute.DataSourceAttributeListInterface;
import com.sldeditor.datasource.example.ExampleLineInterface;
import com.sldeditor.datasource.example.ExamplePointInterface;
import com.sldeditor.datasource.example.ExamplePolygonInterface;
import com.sldeditor.datasource.example.impl.ExampleLineImpl;
import com.sldeditor.datasource.example.impl.ExamplePointImpl;
import com.sldeditor.datasource.example.impl.ExamplePolygonImplIOM;
import com.sldeditor.datasource.impl.CreateDataSourceInterface;
import com.sldeditor.datasource.impl.DataSourceFactory;
import com.sldeditor.datasource.impl.GeometryTypeEnum;

/**
 * Unit test for DataSourceFactory.
 * <p>{@link com.sldeditor.datasource.impl.DataSourceFactory}
 * 
 * @author Robert Ward (SCISYS)
 *
 */
public class DataSourceFactoryTest {

    class DummyDataSource implements DataSourceInterface
    {
        public GeometryTypeEnum geometryType = GeometryTypeEnum.UNKNOWN;

        @Override
        public void addListener(DataSourceUpdatedInterface listener) {
        }

        @Override
        public void connect(SLDEditorFileInterface editorFile) {
        }

        @Override
        public void reset() {
        }

        @Override
        public FeatureSource<SimpleFeatureType, SimpleFeature> getFeatureSource() {
            return null;
        }

        @Override
        public List<String> getAttributes(Class<?> expectedDataType) {
            return null;
        }

        @Override
        public GeometryTypeEnum getGeometryType() {
            return geometryType;
        }

        @Override
        public void readAttributes(DataSourceAttributeListInterface attributeData) {
        }

        @Override
        public DataSourcePropertiesInterface getDataConnectorProperties() {
            return null;
        }

        @Override
        public List<String> getAvailableDataStoreList() {
            return null;
        }

        @Override
        public void updateFields(DataSourceAttributeListInterface attributeData) {
        }

        @Override
        public void addField(DataSourceAttributeData dataSourceField) {
        }

        @Override
        public void setDataSourceCreation(CreateDataSourceInterface internalDataSource,
                CreateDataSourceInterface externalDataSource,
                CreateDataSourceInterface inlineDataSource) {
        }

        @Override
        public Collection<PropertyDescriptor> getPropertyDescriptorList() {
            return null;
        }

        @Override
        public void removeListener(DataSourceUpdatedInterface listener) {
        }

        @Override
        public AbstractGridCoverage2DReader getGridCoverageReader() {
            return null;
        }

        @Override
        public FeatureSource<SimpleFeatureType, SimpleFeature> getExampleFeatureSource() {
            return null;
        }

        @Override
        public Map<UserLayer, FeatureSource<SimpleFeatureType, SimpleFeature>> getUserLayerFeatureSource() {
            return null;
        }

        @Override
        public void updateUserLayers() {
        }

        /* (non-Javadoc)
         * @see com.sldeditor.datasource.DataSourceInterface#updateFieldType(java.lang.String, java.lang.Class)
         */
        @Override
        public void updateFieldType(String fieldName, Class<?> dataType) {
        }

        /* (non-Javadoc)
         * @see com.sldeditor.datasource.DataSourceInterface#getGeometryFieldName()
         */
        @Override
        public String getGeometryFieldName() {
            return null;
        }
    }

    /**
     * Test method for {@link com.sldeditor.datasource.impl.DataSourceFactory#createDataSource(java.lang.String)}.
     * Test method for {@link com.sldeditor.datasource.impl.DataSourceFactory#getDataSource()}.
     */
    @Test
    public void testCreateDataSource() {
        DataSourceInterface dataSource = DataSourceFactory.createDataSource(null);

        assertTrue(dataSource != null);
        assertTrue(DataSourceFactory.getDataSource() != null);
    }

    /**
     * Test method for {@link com.sldeditor.datasource.impl.DataSourceFactory#createExamplePolygon(java.lang.String)}.
     */
    @Test
    public void testCreateExamplePolygon() {
        ExamplePolygonInterface examplePolygon = DataSourceFactory.createExamplePolygon(null);

        assertEquals(ExamplePolygonImplIOM.class, examplePolygon.getClass());
    }

    /**
     * Test method for {@link com.sldeditor.datasource.impl.DataSourceFactory#createExampleLine(java.lang.Object)}.
     */
    @Test
    public void testCreateExampleLine() {
        ExampleLineInterface exampleLine = DataSourceFactory.createExampleLine(null);

        assertEquals(ExampleLineImpl.class, exampleLine.getClass());
    }

    /**
     * Test method for {@link com.sldeditor.datasource.impl.DataSourceFactory#createExamplePoint(java.lang.Object)}.
     */
    @Test
    public void testCreateExamplePoint() {
        ExamplePointInterface examplePoint = DataSourceFactory.createExamplePoint(null);

        assertEquals(ExamplePointImpl.class, examplePoint.getClass());
    }
}
