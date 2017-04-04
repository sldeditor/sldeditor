/*
 * SLD Editor - The Open Source Java SLD Editor
 *
 * Copyright (C) 2017, SCISYS UK Limited
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

package com.sldeditor.ui.detail.config.sortby;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.SortByImpl;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.sort.SortBy;
import org.opengis.filter.sort.SortOrder;

/**
 * The Class SortByTableModel.
 *
 * @author Robert Ward (SCISYS)
 */
public class SortByTableModel extends DefaultTableModel {

    /** The Constant COL_SORT_ORDER. */
    private static final int COL_SORT_ORDER = 1;

    /** The Constant DEFAULT_SORT_ORDER. */
    private static final SortOrder DEFAULT_SORT_ORDER = SortOrder.ASCENDING;

    /** The filter factory. */
    private FilterFactory ff = CommonFactoryFinder.getFilterFactory();

    /** The data list. */
    private List<SortBy> dataList = new ArrayList<SortBy>();

    /** The column name list. */
    private List<String> columnNameList = new ArrayList<String>();

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    public SortByTableModel() {
        columnNameList.add("Property");
        columnNameList.add("Ascending");
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.DefaultTableModel#getRowCount()
     */
    @Override
    public int getRowCount() {
        if (dataList == null) {
            return 0;
        }
        return dataList.size();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.DefaultTableModel#getColumnCount()
     */
    @Override
    public int getColumnCount() {
        return columnNameList.size();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.DefaultTableModel#getColumnName(int)
     */
    @Override
    public String getColumnName(int column) {
        return columnNameList.get(column);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.DefaultTableModel#isCellEditable(int, int)
     */
    @Override
    public boolean isCellEditable(int row, int column) {
        return (column == 1);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.DefaultTableModel#getValueAt(int, int)
     */
    @Override
    public Object getValueAt(int row, int column) {
        SortBy obj = dataList.get(row);

        switch (column) {
        case 0:
            return obj.getPropertyName().getPropertyName();
        case COL_SORT_ORDER:
            return isAscendingSortOrder(obj);
        default:
            break;
        }
        return null;
    }

    /**
     * Checks if is ascending sort order.
     *
     * @param obj the obj
     * @return the boolean
     */
    private static Boolean isAscendingSortOrder(SortBy obj) {
        return (obj.getSortOrder().name().compareTo(SortOrder.ASCENDING.name()) == 0);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.DefaultTableModel#setValueAt(java.lang.Object, int, int)
     */
    @Override
    public void setValueAt(Object aValue, int row, int column) {
        SortBy obj = dataList.get(row);

        switch (column) {
        case 0:
            break;
        case COL_SORT_ORDER: {
            Boolean b = (Boolean) aValue;

            SortOrder sortOrder = b.booleanValue() ? SortOrder.ASCENDING : SortOrder.DESCENDING;
            ((SortByImpl) obj).setSortOrder(sortOrder);
            this.fireTableCellUpdated(row, column);
            break;
        }
        default:
            break;
        }
    }

    /**
     * Removes the selected properties from the destination table and returns a list so it can be
     * added to the source list.
     *
     * @param table the table
     * @return the list of removed property names
     */
    public List<String> removeSelected(JTable table) {
        List<String> itemsRemovedList = new ArrayList<String>();

        int[] selectedRows = table.getSelectedRows();
        if (selectedRows.length > 0) {
            for (int i = selectedRows.length - 1; i >= 0; i--) {
                SortBy obj = dataList.get(selectedRows[i]);

                itemsRemovedList.add(0, obj.getPropertyName().getPropertyName());
                dataList.remove(selectedRows[i]);
            }
        }

        this.fireTableDataChanged();

        return itemsRemovedList;
    }

    /**
     * Adds the property.
     *
     * @param propertyName the property name
     */
    public void addProperty(String propertyName) {
        dataList.add((SortByImpl) ff.sort(propertyName, DEFAULT_SORT_ORDER));

        this.fireTableDataChanged();
    }

    /**
     * Populate.
     *
     * @param sortArray the sort array
     */
    public void populate(SortBy[] sortArray) {
        dataList.clear();
        for (SortBy sortBy : sortArray) {
            dataList.add(sortBy);
        }

        this.fireTableDataChanged();
    }

    /**
     * Gets the encoded string.
     *
     * @return the encoded string
     */
    public String getEncodedString() {
        List<String> list = new ArrayList<String>();

        for (SortBy sortBy : dataList) {
            String string = String.format("%s %s", sortBy.getPropertyName().getPropertyName(),
                    sortBy.getSortOrder().identifier().substring(0, 1));
            list.add(string);
        }

        return String.join(", ", list);
    }

    /**
     * Check to see if list contains property name.
     *
     * @param item the item
     * @return true, if successful
     */
    public boolean containsProperty(String item) {
        for (SortBy sortBy : dataList) {
            if (sortBy.getPropertyName().getPropertyName().equals(item)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the sort order column.
     *
     * @return the sort order column
     */
    public static int getSortOrderColumn() {
        return COL_SORT_ORDER;
    }

    /**
     * Move row down.
     *
     * @param index the index
     */
    public void moveRowDown(int index) {
        SortBy sortBy = dataList.remove(index);
        dataList.add(index + 1, sortBy);

        this.fireTableDataChanged();
    }

    /**
     * Move row up.
     *
     * @param index the index
     */
    public void moveRowUp(int index) {
        SortBy sortBy = dataList.remove(index);
        dataList.add(index - 1, sortBy);

        this.fireTableDataChanged();
    }
}
