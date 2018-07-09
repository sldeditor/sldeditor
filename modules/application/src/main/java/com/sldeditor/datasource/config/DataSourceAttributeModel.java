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

package com.sldeditor.datasource.config;

import com.sldeditor.common.console.ConsoleManager;
import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.datasource.attribute.DataSourceAttributeData;
import com.sldeditor.datasource.attribute.DataSourceAttributeList;
import com.sldeditor.datasource.attribute.DataSourceAttributeListInterface;
import com.sldeditor.datasource.impl.CreateSampleData;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.table.AbstractTableModel;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

/**
 * JTable model that allows the viewing and editing of DataSourceAttribute objects.
 *
 * @author Robert Ward (SCISYS)
 */
public class DataSourceAttributeModel extends AbstractTableModel {

    // CHECKSTYLE:OFF
    /** The array of geometry names. */
    private static final String[] GEOMETRY_NAME = {
        "Geometry (Point)",
        "Geometry (Multi-Point)",
        "Geometry (Line)",
        "Geometry (Multi-Line)",
        "Geometry (Polygon)",
        "Geometry (Multi-Polygon)",
        "String",
        "Integer",
        "Long",
        "Double",
        "Float",
        "Short",
        "DateTime",
        "Timestamp",
        "Object"
    };

    /** The class type array. */
    private static final Class<?>[] CLASSTYPE = {
        Point.class,
        MultiPoint.class,
        LineString.class,
        MultiLineString.class,
        Polygon.class,
        MultiPolygon.class,
        String.class,
        Integer.class,
        Long.class,
        Double.class,
        Float.class,
        Short.class,
        Date.class,
        java.sql.Timestamp.class,
        Object.class
    };
    // CHECKSTYLE:ON

    /** The type map. */
    private Map<Class<?>, String> typeMap = new HashMap<Class<?>, String>();

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The Constant FIELD_COLUMN_ID. */
    private static final int FIELD_COLUMN_ID = 0;

    /** The Constant TYPE_COLUMN_ID. */
    private static final int TYPE_COLUMN_ID = 1;

    /** The Constant VALUE_COLUMN_ID. */
    private static final int VALUE_COLUMN_ID = 2;

    /** The Constant DEFAULT_NEW_FIELD_NAME. */
    private static final String DEFAULT_NEW_FIELD_NAME = "New_field";

    /** The Constant DEFAULT_NEW_FIELD_TYPE. */
    private static final Class<?> DEFAULT_NEW_FIELD_TYPE = String.class;

    /** The column list. */
    private List<String> columnList = new ArrayList<String>();

    /** The value list. */
    private List<DataSourceAttributeData> valueList = new ArrayList<DataSourceAttributeData>();

    /** The value map. */
    private Map<String, DataSourceAttributeData> valueMap =
            new HashMap<String, DataSourceAttributeData>();

    /** The is connected to data source. */
    private boolean isConnectedToDataSource = false;

    /** Instantiates a new render attribute model. */
    public DataSourceAttributeModel() {
        columnList.add(
                Localisation.getString(
                        DataSourceAttributeModel.class, "DataSourceAttributeModel.name"));
        columnList.add(
                Localisation.getString(
                        DataSourceAttributeModel.class, "DataSourceAttributeModel.type"));
        columnList.add(
                Localisation.getString(
                        DataSourceAttributeModel.class, "DataSourceAttributeModel.value"));

        int index = 0;
        while (index < GEOMETRY_NAME.length) {
            typeMap.put(CLASSTYPE[index], GEOMETRY_NAME[index]);
            index++;
        }
    }

    /**
     * Populate model.
     *
     * @param attributeList the attribute list
     */
    public void populate(List<DataSourceAttributeData> attributeList) {

        valueList.clear();
        valueMap.clear();

        if (attributeList != null) {
            for (DataSourceAttributeData data : attributeList) {
                valueList.add(data);
                valueMap.put(data.getName(), data);
            }
        }
    }

    /**
     * Gets the column name.
     *
     * @param col the col
     * @return the column name
     */
    /*
     * (non-Javadoc)
     *
     * @see javax.swing.table.AbstractTableModel#getColumnName(int)
     */
    @Override
    public String getColumnName(int col) {
        return columnList.get(col);
    }

    /**
     * Retrieve data.
     *
     * @return the map
     */
    public List<DataSourceAttributeData> retrieveData() {
        List<DataSourceAttributeData> attributeList = new ArrayList<DataSourceAttributeData>();

        for (int row = 0; row < this.getRowCount(); row++) {
            String name = (String) this.getValueAt(row, FIELD_COLUMN_ID);
            Object objValue = this.getValueAt(row, VALUE_COLUMN_ID);

            DataSourceAttributeData existingData = valueMap.get(name);

            if (existingData != null) {
                DataSourceAttributeData data =
                        new DataSourceAttributeData(name, existingData.getType(), objValue);

                attributeList.add(data);
            }
        }

        return attributeList;
    }

    /**
     * Checks if is cell editable.
     *
     * @param row the row
     * @param col the col
     * @return true, if is cell editable
     */
    /*
     * (non-Javadoc)
     *
     * @see javax.swing.table.AbstractTableModel#isCellEditable(int, int)
     */
    @Override
    public boolean isCellEditable(int row, int col) {
        // Note that the data/cell address is constant,
        // no matter where the cell appears onscreen.
        if (isConnectedToDataSource) {
            if (col == VALUE_COLUMN_ID) {
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    /**
     * Gets the row count.
     *
     * @return the row count
     */
    /*
     * (non-Javadoc)
     *
     * @see javax.swing.table.TableModel#getRowCount()
     */
    @Override
    public int getRowCount() {
        return valueList.size();
    }

    /**
     * Gets the column count.
     *
     * @return the column count
     */
    /*
     * (non-Javadoc)
     *
     * @see javax.swing.table.TableModel#getColumnCount()
     */
    @Override
    public int getColumnCount() {
        return columnList.size();
    }

    /**
     * Gets the value at.
     *
     * @param rowIndex the row index
     * @param columnIndex the column index
     * @return the value at
     */
    /*
     * (non-Javadoc)
     *
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if ((rowIndex >= 0) && (rowIndex < valueList.size())) {
            DataSourceAttributeData data = valueList.get(rowIndex);

            switch (columnIndex) {
                case FIELD_COLUMN_ID:
                    return data.getName();
                case TYPE_COLUMN_ID:
                    return getTypeString(data.getType());
                case VALUE_COLUMN_ID:
                    return data.getValue();
                default:
                    break;
            }
        }
        return null;
    }

    /**
     * Gets the type string.
     *
     * @param fieldType the field type
     * @return the type string
     */
    private String getTypeString(Class<?> fieldType) {
        String key = typeMap.get(fieldType);

        if (key == null) {
            if (fieldType == null) {
                ConsoleManager.getInstance()
                        .error(
                                DataSourceAttributeModel.class,
                                "Data Source : Unknown field type class");
            } else {
                ConsoleManager.getInstance()
                        .error(
                                DataSourceAttributeModel.class,
                                "Data Source : Unknown field type class : " + fieldType.getName());
            }
        }

        return key;
    }

    /**
     * Sets the value at.
     *
     * @param value the value
     * @param row the row
     * @param col the col
     */
    /*
     * (non-Javadoc)
     *
     * @see javax.swing.table.AbstractTableModel#setValueAt(java.lang.Object, int, int)
     */
    @Override
    public void setValueAt(Object value, int row, int col) {
        if ((row >= 0) && (row < valueList.size())) {
            DataSourceAttributeData data = valueList.get(row);
            switch (col) {
                case FIELD_COLUMN_ID:
                    {
                        valueMap.remove(data.getName());
                        data.setName((String) value);
                        valueMap.put(data.getName(), data);
                        valueList.remove(row);
                        valueList.add(row, data);
                    }
                    break;
                case TYPE_COLUMN_ID:
                    {
                        Class<?> typeClass = getTypeClass((String) value);
                        data.setType(typeClass);

                        Object samppleValue =
                                CreateSampleData.getFieldTypeValue(
                                        row, data.getName(), typeClass, null);

                        data.setValue(samppleValue);
                    }
                    break;
                case VALUE_COLUMN_ID:
                    data.setValue(value);
                    break;
                default:
                    break;
            }
            fireTableCellUpdated(row, col);
        }
    }

    /**
     * Gets the type class.
     *
     * @param displayName the display name
     * @return the type class
     */
    private Class<?> getTypeClass(String displayName) {
        for (Class<?> key : typeMap.keySet()) {
            String value = typeMap.get(key);

            if (value.compareTo(displayName) == 0) {
                return key;
            }
        }
        return null;
    }

    /**
     * Gets the type data.
     *
     * @return the type data
     */
    public String[] getTypeData() {
        return GEOMETRY_NAME;
    }

    /** Adds the new field. */
    public void addNewField() {
        int index = valueMap.size();
        String name = String.format("%s_%d", DEFAULT_NEW_FIELD_NAME, index);
        Class<?> fieldType = DEFAULT_NEW_FIELD_TYPE;
        Object value = CreateSampleData.getFieldTypeValue(index, name, fieldType, null);

        DataSourceAttributeData newField = new DataSourceAttributeData(name, fieldType, value);

        valueList.add(newField);
        valueMap.put(newField.getName(), newField);
    }

    /**
     * Sets the connected to data source flag.
     *
     * @param isConnectedToDataSourceFlag the new connected to data source
     */
    public void setConnectedToDataSource(boolean isConnectedToDataSourceFlag) {
        this.isConnectedToDataSource = isConnectedToDataSourceFlag;
    }

    /**
     * Gets the attribute data.
     *
     * @return the attribute data
     */
    public DataSourceAttributeListInterface getAttributeData() {
        DataSourceAttributeList attributeDataImpl = new DataSourceAttributeList();

        List<DataSourceAttributeData> attributeList = new ArrayList<DataSourceAttributeData>();

        for (DataSourceAttributeData attributeData : valueList) {
            attributeList.add(attributeData.clone());
        }
        attributeDataImpl.setData(attributeList);

        return attributeDataImpl;
    }

    /**
     * Removes the fields.
     *
     * @param selectedRowIndexes the selected row indexes
     */
    public void removeFields(int[] selectedRowIndexes) {
        if (selectedRowIndexes != null) {
            int index = selectedRowIndexes.length - 1;
            while (index >= 0) {
                int rowIndex = selectedRowIndexes[index];
                if ((rowIndex >= 0) && (rowIndex < valueList.size())) {
                    DataSourceAttributeData data = valueList.remove(rowIndex);
                    if (data != null) {
                        valueMap.remove(data.getName());
                    }
                }
                index--;
            }
        }
    }
}
