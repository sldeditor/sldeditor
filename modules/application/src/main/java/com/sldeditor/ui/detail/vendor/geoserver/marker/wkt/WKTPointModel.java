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
package com.sldeditor.ui.detail.vendor.geoserver.marker.wkt;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

/**
 * Table model that allows the getting and setting of WKTPoint objects.
 * 
 * @author Robert Ward (SCISYS)
 */
public class WKTPointModel extends AbstractTableModel {

    /** The point list. */
    private List<WKTPoint> pointList = new ArrayList<WKTPoint>();

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The Constant X_COLUMN_ID. */
    private static final int X_COLUMN_ID = 0;

    /** The Constant Y_COLUMN_ID. */
    private static final int Y_COLUMN_ID = 1;

    /** The column list. */
    private List<String> columnList = new ArrayList<String>();

    /** The ensure first and last points are the same. */
    private boolean ensureFirstAndLastPointsAreTheSame = false;

    /**
     * Constructor.
     */
    public WKTPointModel() {
        columnList.add("X");
        columnList.add("Y");
    }

    /**
     * Gets the column name.
     *
     * @param col the column index
     * @return the column name
     */
    /* (non-Javadoc)
     * @see javax.swing.table.AbstractTableModel#getColumnName(int)
     */
    public String getColumnName(int col) {
        return columnList.get(col);
    }

    /**
     * Checks if is cell editable.
     *
     * @param row the row
     * @param col the col
     * @return true, if is cell editable
     */
    /* (non-Javadoc)
     * @see javax.swing.table.AbstractTableModel#isCellEditable(int, int)
     */
    public boolean isCellEditable(int row, int col) {
        return true;
    }

    /**
     * Gets the row count.
     *
     * @return the row count
     */
    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getRowCount()
     */
    @Override
    public int getRowCount() {
        return pointList.size();
    }

    /**
     * Gets the column count.
     *
     * @return the column count
     */
    /* (non-Javadoc)
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
    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if((rowIndex < 0) || (rowIndex >= pointList.size()))
        {
            return null;
        }

        WKTPoint data = pointList.get(rowIndex);

        switch(columnIndex)
        {
        case X_COLUMN_ID:
            return data.getX();
        case Y_COLUMN_ID:
            return data.getY();
        default:
            return null;
        }
    }

    /**
     * Sets the value at.
     *
     * @param value the value
     * @param row the row
     * @param col the col
     */
    /* (non-Javadoc)
     * @see javax.swing.table.AbstractTableModel#setValueAt(java.lang.Object, int, int)
     */
    public void setValueAt(Object value, int row, int col) {
        if((row < 0) || (row >= pointList.size()))
        {
            return;
        }

        if(value instanceof String)
        {
            WKTPoint data = pointList.get(row);
            switch(col)
            {
            case X_COLUMN_ID:
                data.setX(Double.valueOf((String) value));
                break;
            case Y_COLUMN_ID:
                data.setY(Double.valueOf((String) value));
                break;
            default:
                break;
            }
            fireTableCellUpdated(row, col);
        }
    }

    /**
     * Adds the new field.
     */
    public void addNewPoint()
    {
        pointList.add(new WKTPoint());

        this.fireTableDataChanged();
    }

    /**
     * Populate.
     *
     * @param wktPointList the wkt point list
     */
    public void populate(WKTSegmentList wktPointList) {

        if(wktPointList == null)
        {
            pointList.clear();
        }
        else
        {
            pointList = wktPointList.getWktPointList(ensureFirstAndLastPointsAreTheSame);
        }

        this.fireTableDataChanged();
    }

    /**
     * Removes the point.
     *
     * @param rowIndex the row index
     */
    public void removePoint(int rowIndex) {
        if((rowIndex < 0) || (rowIndex >= pointList.size()))
        {
            return;
        }
        pointList.remove(rowIndex);

        this.fireTableDataChanged();
    }

    /**
     * Clear all the data from the model.
     */
    public void clear() {
        pointList.clear();
        this.fireTableDataChanged();
    }

    /**
     * Sets the WKT type.
     *
     * @param wktType the new WKT type
     */
    public void setWKTType(WKTType wktType) {
        if(wktType != null)
        {
            ensureFirstAndLastPointsAreTheSame = wktType.doFirstLastHaveToBeSame();
        }
        else
        {
            ensureFirstAndLastPointsAreTheSame = false;
        }
    }
}
