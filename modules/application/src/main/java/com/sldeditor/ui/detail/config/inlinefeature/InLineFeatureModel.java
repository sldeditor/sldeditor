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

package com.sldeditor.ui.detail.config.inlinefeature;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;

import org.geotools.data.DataStore;
import org.geotools.data.DataUtilities;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.feature.type.GeometryDescriptorImpl;
import org.geotools.styling.UserLayer;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.sldeditor.common.console.ConsoleManager;
import com.sldeditor.common.coordinate.CoordManager;
import com.sldeditor.ui.detail.vendor.geoserver.marker.wkt.WKTConversion;
import com.sldeditor.ui.widgets.ValueComboBox;
import com.sldeditor.ui.widgets.ValueComboBoxData;
import com.vividsolutions.jts.geom.Geometry;

/**
 * The Class InLineFeatureModel.
 *
 * @author Robert Ward (SCISYS)
 */
public class InLineFeatureModel extends AbstractTableModel {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The column list. */
    private List<String> columnList = new ArrayList<String>();

    /** The parent obj. */
    private InlineFeatureUpdateInterface parentObj = null;

    /** The feature collection. */
    private SimpleFeatureCollection featureCollection = null;

    /** The geometry field index. */
    private int geometryFieldIndex = -1;

    /** The user layer. */
    private UserLayer userLayer = null;

    /** The last row. */
    private int lastRow = -1;

    /** The cached feature. */
    private SimpleFeature cachedFeature = null;

    /** The feature table. */
    private JTable featureTable = null;

    /** The crs combo box. */
    private ValueComboBox crsComboBox;

    /**
     * Instantiates a new in line feature model.
     *
     * @param parent the parent
     */
    public InLineFeatureModel(InlineFeatureUpdateInterface parent)
    {
        this.parentObj = parent;
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getColumnCount()
     */
    @Override
    public int getColumnCount() {
        return columnList.size();
    }

    /**
     * Gets the column name.
     *
     * @param column the column
     * @return the column name
     */
    @Override
    public String getColumnName(int column) {
        if((column < 0) || (column >= columnList.size()))
        {
            return null;
        }
        return columnList.get(column);
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getRowCount()
     */
    @Override
    public int getRowCount() {
        if(featureCollection != null)
        {
            return featureCollection.size();
        }
        return 0;
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    @Override
    public Object getValueAt(int row, int column) {
        if((row < 0) || (row >= getRowCount()))
        {
            return null;
        }

        if((column < 0) || (column >= getColumnCount()))
        {
            return null;
        }

        SimpleFeature feature = getFeature(row);

        if(feature != null)
        {
            if(column == geometryFieldIndex)
            {
                Object defaultGeometry = feature.getDefaultGeometry();
                return defaultGeometry;
            }
            else
            {
                Object attributeData = feature.getAttribute(column);
                return attributeData;
            }
        }
        return null;
    }

    /**
     * Gets the feature.
     *
     * @param row the row
     * @return the feature
     */
    private SimpleFeature getFeature(int row) {
        if(featureCollection != null)
        {
            if(row != lastRow)
            {
                SimpleFeatureIterator iterator = featureCollection.features();

                SimpleFeature feature = iterator.next();
                int index = 0;
                while(iterator.hasNext() && (index < row))
                {
                    feature = iterator.next();
                    index ++;
                }

                lastRow = row;
                cachedFeature = feature;
            }
            return cachedFeature;
        }
        return null;
    }

    /**
     * Populate.
     *
     * @param userLayer the user layer
     */
    public void populate(UserLayer userLayer) {
        this.userLayer = userLayer;
        featureCollection = null;
        geometryFieldIndex = -1;

        columnList.clear();

        if(userLayer != null)
        {
            if(userLayer.getInlineFeatureType() != null)
            {
                String typeName = userLayer.getInlineFeatureType().getTypeName();
                try {
                    SimpleFeatureSource featureSource = userLayer.getInlineFeatureDatastore().getFeatureSource(typeName);
                    if(featureSource != null)
                    {
                        featureCollection = featureSource.getFeatures();
                    }
                } catch (IOException e) {
                    ConsoleManager.getInstance().exception(this, e);
                }

                if(featureCollection != null)
                {
                    // Populate field names
                    List<AttributeDescriptor> descriptorList = featureCollection.getSchema().getAttributeDescriptors();
                    int index = 0;
                    for(AttributeDescriptor descriptor : descriptorList)
                    {
                        if(descriptor instanceof GeometryDescriptorImpl)
                        {
                            geometryFieldIndex = index;
                        }
                        columnList.add(descriptor.getLocalName());
                        index ++;
                    }
                }
            }
        }

        this.fireTableStructureChanged();
        this.fireTableDataChanged();

        // Set up the editor to handle editing the table cells
        InlineCellEditor editor = new InlineCellEditor(this);

        if(featureTable != null)
        {
            if(featureTable.getColumnModel().getColumnCount() > 0)
            {
                TableColumn column = featureTable.getColumnModel().getColumn(0);
                column.setCellEditor(editor);
                featureTable.setCellEditor(editor);
            }
        }
    }

    /**
     * Gets the geometry field index.
     *
     * @return the geometry field index
     */
    public int getGeometryFieldIndex() {
        return geometryFieldIndex;
    }

    /**
     * Checks if cell editable.
     *
     * @param rowIndex the row index
     * @param columnIndex the column index
     * @return true, if is cell editable
     */
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }

    /**
     * Sets the value at.
     *
     * @param aValue the a value
     * @param rowIndex the row index
     * @param columnIndex the column index
     */
    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if((rowIndex < 0) || (rowIndex >= getRowCount()))
        {
            return;
        }

        if((columnIndex < 0) || (columnIndex >= getColumnCount()))
        {
            return;
        }

        SimpleFeature feature = getFeature(rowIndex);

        if(feature != null)
        {
            if(columnIndex == getGeometryFieldIndex())
            {
                feature.setAttribute(columnIndex, aValue);
            }
            else
            {
                feature.setAttribute(columnIndex, aValue);
            }
        }

        if(parentObj != null)
        {
            parentObj.inlineFeatureUpdated();
        }
    }

    /**
     * Gets the feature collection.
     *
     * @return the featureCollection
     */
    public SimpleFeatureCollection getFeatureCollection() {
        return featureCollection;
    }

    /**
     * Gets the inline features.
     *
     * @return the inline features
     */
    public String getInlineFeatures() {
        return InlineFeatureUtils.getInlineFeaturesText(userLayer);
    }

    /**
     * Adds the new column.
     */
    public void addNewColumn() {
        if(featureCollection != null)
        {
            String attributeName = getUniqueAttributeName();

            columnList.add(attributeName);

            // Populate field names
            SimpleFeatureTypeBuilder featureTypeBuilder = new SimpleFeatureTypeBuilder();
            featureTypeBuilder.init(featureCollection.getSchema());
            featureTypeBuilder.add(attributeName, String.class);

            SimpleFeatureType newFeatureType = featureTypeBuilder.buildFeatureType();

            String typeName = userLayer.getInlineFeatureType().getTypeName();
            try {
                SimpleFeatureSource featureSource = userLayer.getInlineFeatureDatastore().getFeatureSource(typeName);

                SimpleFeatureBuilder sfb = new SimpleFeatureBuilder(newFeatureType); 

                ArrayList<SimpleFeature> featureList = new ArrayList<SimpleFeature>();

                SimpleFeatureIterator it = featureSource.getFeatures().features();
                try { 
                    while (it.hasNext()) { 
                        SimpleFeature sf = it.next(); 
                        sfb.addAll(sf.getAttributes()); 
                        sfb.add(new String("")); 
                        featureList.add(sfb.buildFeature(null)); 
                    } 
                } finally { 
                    it.close(); 
                } 

                SimpleFeatureCollection collection = new ListFeatureCollection(newFeatureType, featureList);
                DataStore dataStore = DataUtilities.dataStore( collection );

                featureCollection = collection;
                cachedFeature = null;
                lastRow = -1;
                userLayer.setInlineFeatureDatastore(dataStore);
                userLayer.setInlineFeatureType(newFeatureType);

            } catch (IOException e) {
                ConsoleManager.getInstance().exception(this, e);
            } 

            this.fireTableStructureChanged();
            this.fireTableDataChanged();

            if(parentObj != null)
            {
                parentObj.inlineFeatureUpdated();
            }
        }
    }

    /**
     * Gets the unique attribute name.
     *
     * @return the unique attribute name
     */
    private String getUniqueAttributeName() {
        String newColumnName = "";
        List<String> columnNameList = new ArrayList<String>();

        List<AttributeDescriptor> descriptorList = featureCollection.getSchema().getAttributeDescriptors();

        for(AttributeDescriptor attribute : descriptorList)
        {
            columnNameList.add(attribute.getLocalName());
        }

        int colIndex = descriptorList.size() + 1;
        boolean found = false;
        while(!found)
        {
            newColumnName = String.format("attr%02d", colIndex);

            if(columnNameList.contains(newColumnName))
            {
                colIndex ++;
            }
            else
            {
                return newColumnName;
            }
        }
        return newColumnName;
    }

    /**
     * Gets the column names, excluding the geometry.
     *
     * @return the column names
     */
    public List<String> getColumnNames() {
        List<String> columnNames = new ArrayList<String>();
        int index = 0;
        for(String columnName : columnList)
        {
            if(index != geometryFieldIndex)
            {
                columnNames.add(columnName);
            }
            index ++;
        }
        return columnNames;
    }

    /**
     * Removes the column.
     *
     * @param columnName the column name
     */
    public void removeColumn(String columnName) {
        if(featureCollection != null)
        {
            if(columnList.contains(columnName))
            {
                columnList.remove(columnName);

                // Find field name to remote
                SimpleFeatureTypeBuilder featureTypeBuilder = new SimpleFeatureTypeBuilder();
                featureTypeBuilder.init(featureCollection.getSchema());
                featureTypeBuilder.remove(columnName);

                SimpleFeatureType newFeatureType = featureTypeBuilder.buildFeatureType();

                int attributeToRemoveIndex = 0;
                for(AttributeDescriptor descriptor : newFeatureType.getAttributeDescriptors())
                {
                    if(descriptor.getLocalName().compareTo(columnName) == 0)
                    {
                        break;
                    }
                    attributeToRemoveIndex ++;
                }

                String typeName = userLayer.getInlineFeatureType().getTypeName();
                try {
                    SimpleFeatureSource featureSource = userLayer.getInlineFeatureDatastore().getFeatureSource(typeName);

                    SimpleFeatureBuilder sfb = new SimpleFeatureBuilder(newFeatureType); 

                    ArrayList<SimpleFeature> featureList = new ArrayList<SimpleFeature>();

                    SimpleFeatureIterator it = featureSource.getFeatures().features();
                    try {
                        while (it.hasNext()) { 
                            SimpleFeature sf = it.next();
                            List<Object> attributes = sf.getAttributes();
                            attributes.remove(attributeToRemoveIndex);

                            sfb.addAll(attributes); 
                            featureList.add(sfb.buildFeature(null)); 
                        } 
                    } finally { 
                        it.close(); 
                    } 

                    SimpleFeatureCollection collection = new ListFeatureCollection(newFeatureType, featureList);
                    DataStore dataStore = DataUtilities.dataStore( collection );

                    featureCollection = collection;
                    cachedFeature = null;
                    lastRow = -1;
                    userLayer.setInlineFeatureDatastore(dataStore);
                    userLayer.setInlineFeatureType(newFeatureType);

                } catch (IOException e) {
                    ConsoleManager.getInstance().exception(this, e);
                } 

                this.fireTableStructureChanged();
                this.fireTableDataChanged();

                if(parentObj != null)
                {
                    parentObj.inlineFeatureUpdated();
                }
            }
        }
    }

    /**
     * Update CRS.
     *
     * @param selectedValue the selected value
     */
    public void updateCRS(ValueComboBoxData selectedValue) {
        if(selectedValue != null)
        {
            String crsCode = selectedValue.getKey();

            CoordinateReferenceSystem newCRS = CoordManager.getInstance().getCRS(crsCode);

            SimpleFeatureType newFeatureType = SimpleFeatureTypeBuilder.retype(featureCollection.getSchema(), newCRS);

            String typeName = userLayer.getInlineFeatureType().getTypeName();
            try {
                SimpleFeatureSource featureSource = userLayer.getInlineFeatureDatastore().getFeatureSource(typeName);

                SimpleFeatureBuilder sfb = new SimpleFeatureBuilder(newFeatureType); 

                ArrayList<SimpleFeature> featureList = new ArrayList<SimpleFeature>();

                SimpleFeatureIterator it = featureSource.getFeatures().features();
                try { 
                    while (it.hasNext()) { 
                        SimpleFeature sf = it.next(); 
                        List<Object> attributeValueList = sf.getAttributes();
                        sfb.addAll(attributeValueList);
                        featureList.add(sfb.buildFeature(null)); 
                    } 
                } finally { 
                    it.close(); 
                } 

                SimpleFeatureCollection collection = new ListFeatureCollection(newFeatureType, featureList);
                DataStore dataStore = DataUtilities.dataStore( collection );

                featureCollection = collection;
                cachedFeature = null;
                lastRow = -1;
                userLayer.setInlineFeatureDatastore(dataStore);
                userLayer.setInlineFeatureType(newFeatureType);

            } catch (IOException e) {
                ConsoleManager.getInstance().exception(this, e);
            } 

            this.fireTableStructureChanged();
            this.fireTableDataChanged();

            if(parentObj != null)
            {
                parentObj.inlineFeatureUpdated();
            }
        }
    }

    /**
     * Update geometry for a given row.
     *
     * @param row the row to update
     * @param geometry the new geometry
     */
    public void updateGeometry(int row, Geometry geometry) {
        setValueAt(geometry, row, getGeometryFieldIndex());
    }

    /**
     * Adds the new feature.
     */
    public void addNewFeature() {
        SimpleFeatureType featureType = userLayer.getInlineFeatureType();

        String typeName = userLayer.getInlineFeatureType().getTypeName();
        try {
            SimpleFeatureSource featureSource = userLayer.getInlineFeatureDatastore().getFeatureSource(typeName);

            SimpleFeatureBuilder sfb = new SimpleFeatureBuilder(featureType); 

            ArrayList<SimpleFeature> featureList = new ArrayList<SimpleFeature>();

            SimpleFeatureIterator it = featureSource.getFeatures().features();
            try { 
                while (it.hasNext()) { 
                    SimpleFeature sf = it.next(); 
                    List<Object> attributeValueList = sf.getAttributes();
                    sfb.addAll(attributeValueList);
                    featureList.add(sfb.buildFeature(null)); 
                }
                // Add new feature
                String wktString = "wkt://POINT(0 0)";
                Geometry geometry = WKTConversion.convertToGeometry(wktString, getSelectedCRSCode());
                sfb.add(geometry);
                featureList.add(sfb.buildFeature(null)); 
            } finally { 
                it.close(); 
            } 

            SimpleFeatureCollection collection = new ListFeatureCollection(featureType, featureList);
            DataStore dataStore = DataUtilities.dataStore( collection );

            featureCollection = collection;
            cachedFeature = null;
            lastRow = -1;
            userLayer.setInlineFeatureDatastore(dataStore);

        } catch (IOException e) {
            ConsoleManager.getInstance().exception(this, e);
        } 

        this.fireTableStructureChanged();
        this.fireTableDataChanged();

        if(parentObj != null)
        {
            parentObj.inlineFeatureUpdated();
        }
    }

    /**
     * Removes the feature.
     *
     * @param selectedRow the selected row
     */
    public void removeFeature(int selectedRow) {
        if((selectedRow < 0) || (selectedRow >= getRowCount()))
        {
            return;
        }

        SimpleFeatureType featureType = userLayer.getInlineFeatureType();

        String typeName = userLayer.getInlineFeatureType().getTypeName();
        try {
            SimpleFeatureSource featureSource = userLayer.getInlineFeatureDatastore().getFeatureSource(typeName);

            SimpleFeatureBuilder sfb = new SimpleFeatureBuilder(featureType); 

            ArrayList<SimpleFeature> featureList = new ArrayList<SimpleFeature>();

            SimpleFeatureIterator it = featureSource.getFeatures().features();
            try {
                int index = 0;
                while (it.hasNext()) { 
                    SimpleFeature sf = it.next();

                    if(index != selectedRow)
                    {
                        List<Object> attributeValueList = sf.getAttributes();
                        sfb.addAll(attributeValueList);
                        featureList.add(sfb.buildFeature(null));
                    }
                    index ++;
                }
            } finally { 
                it.close(); 
            } 

            SimpleFeatureCollection collection = new ListFeatureCollection(featureType, featureList);
            DataStore dataStore = DataUtilities.dataStore( collection );

            featureCollection = collection;
            cachedFeature = null;
            lastRow = -1;
            userLayer.setInlineFeatureDatastore(dataStore);

        } catch (IOException e) {
            ConsoleManager.getInstance().exception(this, e);
        } 

        this.fireTableStructureChanged();
        this.fireTableDataChanged();

        if(parentObj != null)
        {
            parentObj.inlineFeatureUpdated();
        }
    }

    /**
     * Sets the table.
     *
     * @param featureTable the new table
     * @param crsComboBox the crs combo box
     */
    public void setTable(JTable featureTable, ValueComboBox crsComboBox) {
        this.featureTable = featureTable;
        this.crsComboBox = crsComboBox;
    }

    /**
     * Gets the selected CRS code.
     *
     * @return the selected CRS code
     */
    public String getSelectedCRSCode() {
        String crsCode = null;
        if(crsComboBox != null)
        {
            if(crsComboBox.getSelectedValue() != null)
            {
                crsCode = crsComboBox.getSelectedValue().getKey();
            }
        }
        return crsCode;
    }
}
