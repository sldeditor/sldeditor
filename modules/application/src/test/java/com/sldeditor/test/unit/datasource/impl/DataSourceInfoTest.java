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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import com.sldeditor.datasource.SLDEditorFile;
import com.sldeditor.datasource.impl.DataSourceInfo;
import com.sldeditor.datasource.impl.GeometryTypeEnum;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.FeatureStore;
import org.geotools.data.simple.SimpleFeatureSource;
import org.junit.jupiter.api.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.PropertyDescriptor;

/**
 * Unit test for DataSourceInfo.
 *
 * <p>{@link com.sldeditor.datasource.impl.DataSourceInfo}
 *
 * @author Robert Ward (SCISYS)
 */
public class DataSourceInfoTest {

    /**
     * Test method for {@link
     * com.sldeditor.datasource.impl.DataSourceInfo#setGeometryType(com.sldeditor.datasource.impl.GeometryTypeEnum)}.
     * Test method for {@link com.sldeditor.datasource.impl.DataSourceInfo#getGeometryType()}.
     */
    @Test
    public void testSetGeometryType() {
        DataSourceInfo ds = new DataSourceInfo();

        GeometryTypeEnum expectedGeometry = GeometryTypeEnum.POLYGON;
        ds.setGeometryType(expectedGeometry);

        assertEquals(expectedGeometry, ds.getGeometryType());

        ds.reset();

        assertEquals(GeometryTypeEnum.UNKNOWN, ds.getGeometryType());
    }

    /**
     * Test method for {@link
     * com.sldeditor.datasource.impl.DataSourceInfo#setTypeName(java.lang.String)}. Test method for
     * {@link com.sldeditor.datasource.impl.DataSourceInfo#getTypeName()}.
     */
    @Test
    public void testSetTypeName() {
        DataSourceInfo ds = new DataSourceInfo();
        String expectedTypeName = "test type name";
        ds.setTypeName(expectedTypeName);
        assertEquals(expectedTypeName, ds.getTypeName());

        ds.reset();

        assertNull(ds.getTypeName());
    }

    /**
     * Test method for {@link com.sldeditor.datasource.impl.DataSourceInfo#getDataStore()}. Test
     * method for {@link
     * com.sldeditor.datasource.impl.DataSourceInfo#setDataStore(org.geotools.data.DataStore)}. Test
     * method for {@link com.sldeditor.datasource.impl.DataSourceInfo#unloadDataStore()}.
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @Test
    public void testDataStore() {
        File file = new File("example.shp");
        Map map = new HashMap();
        try {
            map.put("url", file.toURI().toURL());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
        try {
            DataStore dataStore = DataStoreFinder.getDataStore(map);

            DataSourceInfo ds = new DataSourceInfo();

            ds.setDataStore(dataStore);

            assertEquals(dataStore, ds.getDataStore());

            ds.unloadDataStore();

            ds.reset();

            assertNull(ds.getDataStore());
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    /** Test method for {@link com.sldeditor.datasource.impl.DataSourceInfo#getFeatures()}. */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @Test
    public void testGetFeatures() {
        URL url =
                SLDEditorFile.class
                        .getClassLoader()
                        .getResource("point/sld/shp/sld_cookbook_point.shp");

        Map map = new HashMap();
        map.put("url", url);
        DataStore dataStore;
        try {
            dataStore = DataStoreFinder.getDataStore(map);

            DataSourceInfo dsInfo = new DataSourceInfo();

            String typeName = dataStore.getTypeNames()[0];
            dsInfo.setTypeName(typeName);
            SimpleFeatureSource source = dataStore.getFeatureSource(typeName);
            SimpleFeatureType schema = source.getSchema();

            assertNull(dsInfo.getFeatures());
            dsInfo.setSchema(schema);

            assertNull(dsInfo.getFeatures());
            dsInfo.setDataStore(dataStore);

            assertTrue(dsInfo.getFeatures() != null);
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    /**
     * Test method for {@link com.sldeditor.datasource.impl.DataSourceInfo#getFeatureCollection()}.
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @Test
    public void testGetFeatureCollection() {
        URL url =
                SLDEditorFile.class
                        .getClassLoader()
                        .getResource("point/sld/shp/sld_cookbook_point.shp");

        Map map = new HashMap();
        map.put("url", url);
        DataStore dataStore;
        try {
            dataStore = DataStoreFinder.getDataStore(map);

            DataSourceInfo dsInfo = new DataSourceInfo();

            String typeName = dataStore.getTypeNames()[0];
            dsInfo.setTypeName(typeName);
            SimpleFeatureSource source = dataStore.getFeatureSource(typeName);
            SimpleFeatureType schema = source.getSchema();

            assertNull(dsInfo.getGeometryFieldName());
            dsInfo.setSchema(schema);

            assertEquals("the_geom", dsInfo.getGeometryFieldName());
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    /**
     * Test method for {@link com.sldeditor.datasource.impl.DataSourceInfo#getFeatureStore()}. Test
     * method for {@link
     * com.sldeditor.datasource.impl.DataSourceInfo#setSchema(org.opengis.feature.type.FeatureType)}.
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @Test
    public void testGetFeatureStore() {
        URL url =
                SLDEditorFile.class
                        .getClassLoader()
                        .getResource("point/sld/shp/sld_cookbook_point.shp");

        Map map = new HashMap();
        map.put("url", url);
        DataStore dataStore;
        try {
            dataStore = DataStoreFinder.getDataStore(map);

            DataSourceInfo dsInfo = new DataSourceInfo();

            String typeName = dataStore.getTypeNames()[0];
            dsInfo.setTypeName(typeName);
            SimpleFeatureSource source = dataStore.getFeatureSource(typeName);
            SimpleFeatureType schema = source.getSchema();
            dsInfo.setSchema(schema);

            assertNull(dsInfo.getFeatureStore());
            dsInfo.setDataStore(dataStore);

            FeatureStore<SimpleFeatureType, SimpleFeature> featureStore = dsInfo.getFeatureStore();

            assertTrue(featureStore != null);
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    /**
     * Test method for {@link
     * com.sldeditor.datasource.impl.DataSourceInfo#getPropertyDescriptorList()}.
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @Test
    public void testGetPropertyDescriptorList() {
        URL url =
                SLDEditorFile.class
                        .getClassLoader()
                        .getResource("point/sld/shp/sld_cookbook_point.shp");

        Map map = new HashMap();
        map.put("url", url);
        DataStore dataStore;
        try {
            dataStore = DataStoreFinder.getDataStore(map);

            DataSourceInfo dsInfo = new DataSourceInfo();

            String typeName = dataStore.getTypeNames()[0];
            dsInfo.setTypeName(typeName);
            SimpleFeatureSource source = dataStore.getFeatureSource(typeName);
            SimpleFeatureType schema = source.getSchema();

            assertNull(dsInfo.getPropertyDescriptorList());
            dsInfo.setSchema(schema);

            Collection<PropertyDescriptor> fieldList = dsInfo.getPropertyDescriptorList();

            assertTrue(fieldList.size() == 3);
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    /**
     * Test method for {@link com.sldeditor.datasource.impl.DataSourceInfo#getGeometryFieldName()}.
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @Test
    public void testGetGeometryFieldName() {
        URL url =
                SLDEditorFile.class
                        .getClassLoader()
                        .getResource("point/sld/shp/sld_cookbook_point.shp");

        Map map = new HashMap();
        map.put("url", url);
        DataStore dataStore;
        try {
            dataStore = DataStoreFinder.getDataStore(map);

            DataSourceInfo dsInfo = new DataSourceInfo();

            String typeName = dataStore.getTypeNames()[0];
            dsInfo.setTypeName(typeName);
            SimpleFeatureSource source = dataStore.getFeatureSource(typeName);
            SimpleFeatureType schema = source.getSchema();

            assertNull(dsInfo.getGeometryFieldName());
            dsInfo.setSchema(schema);

            assertEquals("the_geom", dsInfo.getGeometryFieldName());
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }
}
