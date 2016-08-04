/**
 * 
 */
package com.sldeditor.test.unit.ui.detail.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
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
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;

import com.sldeditor.common.DataSourceFieldInterface;
import com.sldeditor.common.DataSourcePropertiesInterface;
import com.sldeditor.common.undo.UndoEvent;
import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.datasource.DataSourceInterface;
import com.sldeditor.datasource.DataSourceUpdatedInterface;
import com.sldeditor.datasource.SLDEditorFileInterface;
import com.sldeditor.datasource.attribute.DataSourceAttributeListInterface;
import com.sldeditor.datasource.impl.CreateDataSourceInterface;
import com.sldeditor.datasource.impl.DataSourceFactory;
import com.sldeditor.datasource.impl.GeometryTypeEnum;
import com.sldeditor.ui.detail.config.FieldConfigBase;
import com.sldeditor.ui.detail.config.FieldConfigGeometryField;
import com.sldeditor.ui.detail.config.FieldId;
import com.vividsolutions.jts.geom.Geometry;

/**
 * The unit test for FieldConfigGeometryField.
 * <p>{@link com.sldeditor.ui.detail.config.FieldConfigGeometryField}
 *
 * @author Robert Ward (SCISYS)
 */
public class FieldConfigGeometryFieldTest {

    /**
     * The Class TestDataSourceImpl.
     */
    public class TestDataSource implements DataSourceInterface {

        private static final String GEOMETRY_FIELD = "Geometry_1";
        /** The listener list. */
        private List<DataSourceUpdatedInterface> listenerList = new ArrayList<DataSourceUpdatedInterface>();

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
         * @param editorFile the editor file
         */
        @Override
        public void connect(SLDEditorFileInterface editorFile) {
        }

        /**
         * Reset.
         */
        @Override
        public void reset() {
        }

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
            List<String> attributeList = new ArrayList<String>();

            if(expectedDataType == Date.class)
            {
                attributeList.add("Date_1");
                attributeList.add("Date_2");
            }
            else if(expectedDataType == Number.class)
            {
                attributeList.add("Integer_1");
                attributeList.add("Double_2");
            }
            else if(expectedDataType == Geometry.class)
            {
                attributeList.add(GEOMETRY_FIELD);
            }
            else if(expectedDataType == String.class)
            {
                attributeList.add("String_1");
                attributeList.add("String_2");
                attributeList.add("String_3");
            }
            else
            {
                attributeList.add("Date_1");
                attributeList.add("Date_2");
                attributeList.add("Integer_1");
                attributeList.add("Double_2");
                attributeList.add(GEOMETRY_FIELD);
                attributeList.add("String_1");
                attributeList.add("String_2");
                attributeList.add("String_3");
            }

            return attributeList;
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
        public void readAttributes(DataSourceAttributeListInterface attributeData) {
        }

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
         * Sets the data source creation.
         *
         * @param internalDataSource the internal data source
         * @param externalDataSource the external data source
         */
        @Override
        public void setDataSourceCreation(CreateDataSourceInterface internalDataSource,
                CreateDataSourceInterface externalDataSource,
                CreateDataSourceInterface inlineDataSource) {
        }

        /**
         * Gets the property descriptor list.
         *
         * @return the property descriptor list
         */
        @Override
        public Collection<PropertyDescriptor> getPropertyDescriptorList() {
            return null;
        }

        /**
         * Notify data source loaded.
         */
        public void notifyDataSourceLoaded()
        {
            for(DataSourceUpdatedInterface listener : listenerList)
            {
                listener.dataSourceLoaded(getGeometryType(), false);
            }
        }

        /**
         * Removes the listener.
         *
         * @param listener the listener
         */
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

        public String getDefaultGeometryField() {
            return GEOMETRY_FIELD;
        }

        @Override
        public Map<UserLayer, FeatureSource<SimpleFeatureType, SimpleFeature>> getUserLayerFeatureSource() {
            return null;
        }

        @Override
        public void updateUserLayers() {
        }
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigGeometryField#setEnabled(boolean)}.
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigGeometryField#isEnabled()}.
     */
    @Test
    public void testSetEnabled() {
        FieldConfigGeometryField field = new FieldConfigGeometryField(String.class, new FieldId(FieldIdEnum.NAME), "test label");

        // Text field will not have been created
        boolean expectedValue = true;
        field.setEnabled(expectedValue);

        assertTrue(field.isEnabled());

        // Create text field
        field.createUI();
        assertEquals(expectedValue, field.isEnabled());

        expectedValue = false;
        field.setEnabled(expectedValue);

        assertEquals(expectedValue, field.isEnabled());
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigGeometryField#setVisible(boolean)}.
     */
    @Test
    public void testSetVisible() {
        FieldConfigGeometryField field = new FieldConfigGeometryField(String.class, new FieldId(FieldIdEnum.NAME), "test label");

        boolean expectedValue = true;
        field.setVisible(expectedValue);

        expectedValue = false;
        field.setVisible(expectedValue);
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigGeometryField#generateExpression()}.
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigGeometryField#populateExpression(java.lang.Object)}.
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigGeometryField#populateField(java.lang.String)}.
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigGeometryField#setTestValue(com.sldeditor.ui.detail.config.FieldId, java.lang.String)}.
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigGeometryField#getStringValue()}.
     */
    @Test
    public void testGenerateExpression() {
        class TestFieldConfigGeometryField extends FieldConfigGeometryField
        {

            public TestFieldConfigGeometryField(Class<?> panelId, FieldId id, String label) {
                super(panelId, id, label);
            }

            public Expression callGenerateExpression()
            {
                return generateExpression();
            }
        }

        TestFieldConfigGeometryField field = new TestFieldConfigGeometryField(String.class, new FieldId(FieldIdEnum.NAME), "test label");
        Expression actualExpression = field.callGenerateExpression();
        assertNull(actualExpression);

        field.createUI();
        TestDataSource testDataSource = new TestDataSource();
        @SuppressWarnings("unused")
        DataSourceInterface dataSource = DataSourceFactory.createDataSource(testDataSource);

        field.dataSourceLoaded(GeometryTypeEnum.POLYGON, false);

        String expectedValue = "";
        field.setTestValue(null, expectedValue);
        actualExpression = field.callGenerateExpression();
        assertTrue(expectedValue.compareTo(actualExpression.toString()) == 0);

        // Attribute expression
        FilterFactory ff = CommonFactoryFinder.getFilterFactory();
        Expression expectedExpression = ff.property(testDataSource.getDefaultGeometryField());
        field.populateExpression(expectedExpression);
        actualExpression = field.callGenerateExpression();
        assertTrue(expectedExpression.toString().compareTo(actualExpression.toString()) == 0);

        // Literal expression
        expectedExpression = ff.literal(testDataSource.getDefaultGeometryField());
        field.populateExpression(expectedExpression);
        actualExpression = field.callGenerateExpression();
        assertTrue(expectedExpression.toString().compareTo(actualExpression.toString()) == 0);

        
        field.populateField((String)null);
        String stringValue = field.getStringValue();
        assertNull(stringValue);
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigGeometryField#revertToDefaultValue()}.
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigGeometryField#setDefaultValue(java.lang.String)}.
     */
    @Test
    public void testRevertToDefaultValue() {
        FieldConfigGeometryField field = new FieldConfigGeometryField(String.class, new FieldId(FieldIdEnum.NAME), "test label");

        TestDataSource testDataSource = new TestDataSource();
        String expectedDefaultValue = testDataSource.getDefaultGeometryField();
        field.setDefaultValue(expectedDefaultValue);
        field.revertToDefaultValue();
        assertNull(field.getStringValue());

        @SuppressWarnings("unused")
        DataSourceInterface dataSource = DataSourceFactory.createDataSource(testDataSource);

        field.createUI();
        field.dataSourceLoaded(GeometryTypeEnum.POLYGON, false);
        field.revertToDefaultValue();
        assertTrue(expectedDefaultValue.compareTo(field.getStringValue()) == 0);
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigGeometryField#getClassType()}.
     */
    @Test
    public void testGetClassType() {
        FieldConfigGeometryField field = new FieldConfigGeometryField(String.class, new FieldId(FieldIdEnum.NAME), "test label");

        assertEquals(String.class, field.getClassType());
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigGeometryField#createCopy(com.sldeditor.ui.detail.config.FieldConfigBase)}.
     */
    @Test
    public void testCreateCopy() {

        class TestFieldConfigGeometryField extends FieldConfigGeometryField
        {

            public TestFieldConfigGeometryField(Class<?> panelId, FieldId id, String label) {
                super(panelId, id, label);
            }

            public FieldConfigBase callCreateCopy(FieldConfigBase fieldConfigBase)
            {
                return createCopy(fieldConfigBase);
            }
        }

        TestFieldConfigGeometryField field = new TestFieldConfigGeometryField(String.class, new FieldId(FieldIdEnum.NAME), "test label");

        FieldConfigGeometryField copy = (FieldConfigGeometryField) field.callCreateCopy(null);
        assertNull(copy);

        copy = (FieldConfigGeometryField) field.callCreateCopy(field);
        assertEquals(field.getFieldId().getFieldId(), copy.getFieldId().getFieldId());
        assertTrue(field.getLabel().compareTo(copy.getLabel()) == 0);
        assertEquals(field.isValueOnly(), copy.isValueOnly());
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigGeometryField#undoAction(com.sldeditor.common.undo.UndoInterface)}.
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigGeometryField#redoAction(com.sldeditor.common.undo.UndoInterface)}.
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigGeometryField#dataSourceLoaded(com.sldeditor.datasource.impl.GeometryTypeEnum, boolean)}.
     */
    @Test
    public void testUndoAction() {
        FieldConfigGeometryField field = new FieldConfigGeometryField(String.class, new FieldId(FieldIdEnum.NAME), "test label");

        field.undoAction(null);
        field.redoAction(null);

        field.createUI();
        field.undoAction(null);
        field.redoAction(null);

        TestDataSource testDataSource = new TestDataSource();
        @SuppressWarnings("unused")
        DataSourceInterface dataSource = DataSourceFactory.createDataSource(testDataSource);

        field.dataSourceLoaded(GeometryTypeEnum.POLYGON, false);

        String expectedTestValue = testDataSource.getDefaultGeometryField();
        field.setTestValue(null, expectedTestValue);
        assertTrue(expectedTestValue.compareTo(field.getStringValue()) == 0);

        String expectedUndoTestValue = "";
        String expectedRedoTestValue = testDataSource.getDefaultGeometryField();

        UndoEvent undoEvent = new UndoEvent(null, new FieldId(), expectedUndoTestValue, expectedRedoTestValue);
        field.undoAction(undoEvent);
        assertTrue(expectedUndoTestValue.compareTo(field.getStringValue()) == 0);

        field.redoAction(undoEvent);
        assertTrue(expectedRedoTestValue.compareTo(field.getStringValue()) == 0);
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigGeometryField#attributeSelection(java.lang.String)}.
     */
    @Test
    public void testAttributeSelection() {
        FieldConfigGeometryField field = new FieldConfigGeometryField(String.class, new FieldId(FieldIdEnum.NAME), "test label");

        field.attributeSelection("field");
        // Does nothing
    }

}
