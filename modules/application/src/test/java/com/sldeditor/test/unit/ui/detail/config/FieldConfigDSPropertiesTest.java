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

package com.sldeditor.test.unit.ui.detail.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.sldeditor.common.DataSourcePropertiesInterface;
import com.sldeditor.common.undo.UndoEvent;
import com.sldeditor.common.undo.UndoManager;
import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.datasource.DataSourceInterface;
import com.sldeditor.datasource.DataSourceUpdatedInterface;
import com.sldeditor.datasource.SLDEditorFileInterface;
import com.sldeditor.datasource.attribute.DataSourceAttributeData;
import com.sldeditor.datasource.attribute.DataSourceAttributeListInterface;
import com.sldeditor.datasource.checks.CheckAttributeInterface;
import com.sldeditor.datasource.impl.CreateDataSourceInterface;
import com.sldeditor.datasource.impl.DataSourceFactory;
import com.sldeditor.datasource.impl.GeometryTypeEnum;
import com.sldeditor.ui.detail.config.FieldConfigBase;
import com.sldeditor.ui.detail.config.FieldConfigCommonData;
import com.sldeditor.ui.detail.config.FieldConfigDSProperties;
import com.sldeditor.ui.detail.config.FieldConfigPopulate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.data.FeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.styling.UserLayer;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.PropertyDescriptor;

/**
 * The unit test for FieldConfigDSProperties.
 *
 * <p>{@link com.sldeditor.ui.detail.config.FieldConfigDSProperties}
 *
 * @author Robert Ward (SCISYS)
 */
public class FieldConfigDSPropertiesTest {

    /** The Class TestDataSourceImpl. */
    public class TestDataSource implements DataSourceInterface {

        private static final String GEOMETRY_FIELD = "Geometry_1";

        /** The listener list. */
        private List<DataSourceUpdatedInterface> listenerList =
                new ArrayList<DataSourceUpdatedInterface>();

        /**
         * Adds the listener.
         *
         * @param listener the listener
         */
        @Override
        public void addListener(DataSourceUpdatedInterface listener) {
            listenerList.add(listener);
        }

        /**
         * Connect.
         *
         * @param typeName the type name
         * @param editorFile the editor file
         */
        @Override
        public void connect(
                String typeName,
                SLDEditorFileInterface editorFile,
                List<CheckAttributeInterface> checkList) {}

        /** Reset. */
        @Override
        public void reset() {}

        /**
         * Gets the feature source.
         *
         * @return the feature source
         */
        @Override
        public FeatureSource<SimpleFeatureType, SimpleFeature> getFeatureSource() {
            return null;
        }

        /**
         * Gets the attributes.
         *
         * @param expectedDataType the expected data type
         * @return the attributes
         */
        @Override
        public List<String> getAttributes(Class<?> expectedDataType) {

            return null;
        }

        /**
         * Gets the geometry type.
         *
         * @return the geometry type
         */
        @Override
        public GeometryTypeEnum getGeometryType() {
            return null;
        }

        /**
         * Read attributes.
         *
         * @param attributeData the attribute data
         */
        @Override
        public void readAttributes(DataSourceAttributeListInterface attributeData) {}

        /**
         * Gets the data connector properties.
         *
         * @return the data connector properties
         */
        @Override
        public DataSourcePropertiesInterface getDataConnectorProperties() {
            return null;
        }

        /**
         * Gets the available data store list.
         *
         * @return the available data store list
         */
        @Override
        public List<String> getAvailableDataStoreList() {
            return null;
        }

        /**
         * Update fields.
         *
         * @param attributeData the attribute data
         */
        @Override
        public void updateFields(DataSourceAttributeListInterface attributeData) {}

        /**
         * Adds the field.
         *
         * @param dataSourceField the data source field
         */
        @Override
        public void addField(DataSourceAttributeData dataSourceField) {}

        /**
         * Sets the data source creation.
         *
         * @param internalDataSource the internal data source
         * @param externalDataSource the external data source
         */
        @Override
        public void setDataSourceCreation(
                CreateDataSourceInterface internalDataSource,
                CreateDataSourceInterface externalDataSource,
                CreateDataSourceInterface inlineDataSource) {}

        /**
         * Gets the property descriptor list.
         *
         * @return the property descriptor list
         */
        @Override
        public Collection<PropertyDescriptor> getPropertyDescriptorList() {
            return null;
        }

        /** Notify data source loaded. */
        public void notifyDataSourceLoaded() {
            for (DataSourceUpdatedInterface listener : listenerList) {
                listener.dataSourceLoaded(getGeometryType(), false);
            }
        }

        /**
         * Removes the listener.
         *
         * @param listener the listener
         */
        @Override
        public void removeListener(DataSourceUpdatedInterface listener) {}

        @Override
        public AbstractGridCoverage2DReader getGridCoverageReader() {
            return null;
        }

        @Override
        public FeatureSource<SimpleFeatureType, SimpleFeature> getExampleFeatureSource() {
            return null;
        }

        public String getDefaultGeometryField() {
            return GEOMETRY_FIELD;
        }

        @Override
        // CHECKSTYLE:OFF
        public Map<UserLayer, FeatureSource<SimpleFeatureType, SimpleFeature>>
                getUserLayerFeatureSource() {
            // CHECKSTYLE:ON
            return null;
        }

        @Override
        public void updateUserLayers() {}

        /*
         * (non-Javadoc)
         *
         * @see com.sldeditor.datasource.DataSourceInterface#updateFieldType(java.lang.String,
         * java.lang.Class)
         */
        @Override
        public void updateFieldType(String fieldName, Class<?> dataType) {}

        /*
         * (non-Javadoc)
         *
         * @see com.sldeditor.datasource.DataSourceInterface#getGeometryFieldName()
         */
        @Override
        public String getGeometryFieldName() {
            return null;
        }

        /* (non-Javadoc)
         * @see com.sldeditor.datasource.DataSourceInterface#getAllAttributes(boolean)
         */
        @Override
        public List<String> getAllAttributes(boolean includeGeometry) {
            List<String> attributeList = new ArrayList<String>();

            attributeList.add("Date_1");
            attributeList.add("Date_2");
            attributeList.add("Integer_1");
            attributeList.add("Double_2");
            attributeList.add("String_1");
            attributeList.add("String_2");
            attributeList.add("String_3");

            return attributeList;
        }
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.config.FieldConfigDSProperties#internal_setEnabled(boolean)}. Test
     * method for {@link com.sldeditor.ui.detail.config.FieldConfigDSProperties#isEnabled()}.
     */
    @Test
    public void testSetEnabled() {
        // Value only, no attribute/expression dropdown
        boolean valueOnly = true;
        FieldConfigDSProperties field =
                new FieldConfigDSProperties(
                        new FieldConfigCommonData(
                                Integer.class, FieldIdEnum.NAME, "label", valueOnly));

        // Text field will not have been created
        boolean expectedValue = true;
        field.internal_setEnabled(expectedValue);

        assertTrue(field.isEnabled());

        // Create text field
        field.createUI();
        assertEquals(expectedValue, field.isEnabled());

        expectedValue = false;
        field.internal_setEnabled(expectedValue);

        assertEquals(expectedValue, field.isEnabled());

        // Has attribute/expression dropdown
        valueOnly = false;
        FieldConfigDSProperties field2 =
                new FieldConfigDSProperties(
                        new FieldConfigCommonData(
                                Integer.class, FieldIdEnum.NAME, "label", valueOnly));

        // Text field will not have been created
        expectedValue = true;
        field2.internal_setEnabled(expectedValue);
        assertTrue(field2.isEnabled());

        // Create text field
        field2.createUI();

        assertEquals(expectedValue, field2.isEnabled());

        expectedValue = true;
        field2.internal_setEnabled(expectedValue);

        // Actual value is coming from the attribute panel, not the text field
        assertEquals(expectedValue, field2.isEnabled());
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.config.FieldConfigDSProperties#setVisible(boolean)}.
     */
    @Test
    public void testSetVisible() {
        boolean valueOnly = true;
        FieldConfigDSProperties field =
                new FieldConfigDSProperties(
                        new FieldConfigCommonData(
                                Integer.class, FieldIdEnum.NAME, "label", valueOnly));

        boolean expectedValue = true;
        field.setVisible(expectedValue);
        field.createUI();
        field.setVisible(expectedValue);

        expectedValue = false;
        field.setVisible(expectedValue);
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.config.FieldConfigDSProperties#generateExpression()}. Test method for
     * {@link
     * com.sldeditor.ui.detail.config.FieldConfigDSProperties#populateExpression(java.lang.Object,
     * org.opengis.filter.expression.Expression)}. Test method for {@link
     * com.sldeditor.ui.detail.config.FieldConfigDSProperties#populateField(java.lang.String)}. Test
     * method for {@link
     * com.sldeditor.ui.detail.config.FieldConfigDSProperties#setTestValue(com.sldeditor.ui.detail.config.FieldId,
     * java.lang.String)}. Test method for {@link
     * com.sldeditor.ui.detail.config.FieldConfigDSProperties#getEnumValue()}. Test method for
     * {@link com.sldeditor.ui.detail.config.FieldConfigDSProperties#getStringValue()}.
     */
    @Test
    public void testGenerateExpression() {

        TestDataSource testDataSource = new TestDataSource();
        @SuppressWarnings("unused")
        DataSourceInterface dataSource = DataSourceFactory.createDataSource(testDataSource);

        boolean valueOnly = true;
        FieldConfigDSProperties field =
                new FieldConfigDSProperties(
                        new FieldConfigCommonData(
                                Integer.class, FieldIdEnum.NAME, "label", valueOnly));
        field.undoAction(null);
        field.redoAction(null);

        field.dataSourceLoaded(GeometryTypeEnum.POLYGON, false);
        assertNull(field.getStringValue());

        // Try without creating the ui
        field.populateExpression(null);
        field.populateField((String) null);
        field.setTestValue(null, (String) null);

        // Now create the ui
        field.createUI();
        field.createUI();
        String expectedValue1 = "String_2";
        field.populateField(expectedValue1);
        String actualValueString = field.getStringValue();
        assertTrue(expectedValue1.compareTo(actualValueString) == 0);

        String expectedValue2 = "String_1";
        field.populateExpression(expectedValue2);
        actualValueString = field.getStringValue();
        assertTrue(expectedValue2.compareTo(actualValueString) == 0);

        String expectedValue3 = "Date_2";
        field.populateExpression(CommonFactoryFinder.getFilterFactory().property(expectedValue3));
        actualValueString = field.getStringValue();
        assertTrue(expectedValue3.compareTo(actualValueString) == 0);

        String expectedValue4 = "Date_1";
        field.populateExpression(CommonFactoryFinder.getFilterFactory().literal(expectedValue4));
        actualValueString = field.getStringValue();
        assertTrue(expectedValue4.compareTo(actualValueString) == 0);

        DataSourceFactory.reset();
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.config.FieldConfigDSProperties#revertToDefaultValue()}. Test method
     * for {@link com.sldeditor.ui.detail.config.FieldConfigDSProperties#addConfig(java.util.List)}.
     * Test method for {@link
     * com.sldeditor.ui.detail.config.FieldConfigDSProperties#setDefaultValue(java.lang.String)}.
     */
    @Test
    public void testRevertToDefaultValue() {
        boolean valueOnly = true;
        FieldConfigDSProperties field =
                new FieldConfigDSProperties(
                        new FieldConfigCommonData(
                                Integer.class, FieldIdEnum.NAME, "label", valueOnly));

        field.revertToDefaultValue();
        assertNull(field.getStringValue());
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.config.FieldConfigDSProperties#createCopy(com.sldeditor.ui.detail.config.FieldConfigBase)}.
     */
    @Test
    public void testCreateCopy() {
        boolean valueOnly = true;

        class TestFieldConfigDSProperties extends FieldConfigDSProperties {
            public TestFieldConfigDSProperties(FieldConfigCommonData commonData) {
                super(commonData);
            }

            public FieldConfigPopulate callCreateCopy(FieldConfigBase fieldConfigBase) {
                return createCopy(fieldConfigBase);
            }
        }

        TestFieldConfigDSProperties field =
                new TestFieldConfigDSProperties(
                        new FieldConfigCommonData(
                                Integer.class, FieldIdEnum.NAME, "label", valueOnly));
        FieldConfigDSProperties copy = (FieldConfigDSProperties) field.callCreateCopy(null);
        assertNull(copy);

        copy = (FieldConfigDSProperties) field.callCreateCopy(field);
        assertEquals(field.getFieldId(), copy.getFieldId());
        assertTrue(field.getLabel().compareTo(copy.getLabel()) == 0);
        assertEquals(field.isValueOnly(), copy.isValueOnly());
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.config.FieldConfigDSProperties#undoAction(com.sldeditor.common.undo.UndoInterface)}.
     * Test method for {@link
     * com.sldeditor.ui.detail.config.FieldConfigDSProperties#redoAction(com.sldeditor.common.undo.UndoInterface)}.
     */
    @Test
    public void testUndoAction() {
        boolean valueOnly = true;
        TestDataSource testDataSource = new TestDataSource();
        @SuppressWarnings("unused")
        DataSourceInterface dataSource = DataSourceFactory.createDataSource(testDataSource);

        FieldConfigDSProperties field =
                new FieldConfigDSProperties(
                        new FieldConfigCommonData(
                                Integer.class, FieldIdEnum.NAME, "label", valueOnly));
        field.dataSourceLoaded(GeometryTypeEnum.POLYGON, false);
        assertNull(field.getStringValue());

        // Now create the ui
        field.createUI();
        String expectedValue1 = "Date_2";
        field.populateField(expectedValue1);

        String expectedValue2 = "String_3";
        field.populateField(expectedValue2);

        UndoManager.getInstance().undo();
        String actualValueString = field.getStringValue();
        assertTrue(expectedValue1.compareTo(actualValueString) == 0);

        UndoManager.getInstance().redo();
        actualValueString = field.getStringValue();
        assertTrue(expectedValue2.compareTo(actualValueString) == 0);

        // Increase the code coverage
        field.undoAction(null);
        field.undoAction(
                new UndoEvent(null, FieldIdEnum.NAME, Double.valueOf(0), Double.valueOf(23)));
        field.redoAction(null);
        field.redoAction(
                new UndoEvent(null, FieldIdEnum.NAME, Double.valueOf(0), Double.valueOf(54)));

        DataSourceFactory.reset();
    }
}
