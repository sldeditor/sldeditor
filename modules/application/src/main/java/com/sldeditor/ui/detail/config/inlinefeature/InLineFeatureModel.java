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

import javax.swing.table.AbstractTableModel;

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

import com.sldeditor.common.console.ConsoleManager;

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
        this.fireTableStructureChanged();
        this.fireTableDataChanged();
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
     * Checks if is cell editable.
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
}
