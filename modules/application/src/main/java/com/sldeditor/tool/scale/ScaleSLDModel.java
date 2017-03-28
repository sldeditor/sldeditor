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

package com.sldeditor.tool.scale;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumnModel;

import com.sldeditor.common.SLDEditorInterface;
import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.common.output.SLDWriterInterface;
import com.sldeditor.common.output.impl.SLDWriterFactory;

/**
 * Table model to allows the viewing and editing of ScaleSLDData objects.
 */
public class ScaleSLDModel extends AbstractTableModel {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The workspace column index. */
    private static final int COL_WORKSPACE = 0;

    /** The source column index. */
    private static final int COL_SOURCE = 1;

    /** The named layer column index. */
    private static final int COL_NAMED_LAYER = 2;

    /** The style column index. */
    private static final int COL_STYLE = 3;

    /** The feature type style column index. */
    private static final int COL_FEATURE_TYPE_STYLE = 4;

    /** The rule column index. */
    private static final int COL_RULE = 5;

    /** The minimum scale column index. */
    private static final int COL_MIN_SCALE = 6;

    /** The maximum scale column index. */
    private static final int COL_MAX_SCALE = 7;

    /** The column name list. */
    private List<String> columnNameList = new ArrayList<String>();

    /** The scale list. */
    private List<ScaleSLDData> scaleList = new ArrayList<ScaleSLDData>();

    /** The sld writer. */
    private SLDWriterInterface sldWriter = null;

    /** The listener. */
    private ScaleToolUpdate listener = null;

    /**
     * Constructor.
     */
    public ScaleSLDModel(ScaleToolUpdate listener) {
        this.listener = listener;

        columnNameList.add(Localisation.getString(ScaleSLDModel.class, "ScaleSLDModel.workspace"));
        columnNameList.add(Localisation.getString(ScaleSLDModel.class, "ScaleSLDModel.source"));
        columnNameList.add(Localisation.getString(ScaleSLDModel.class, "ScaleSLDModel.namedLayer"));
        columnNameList.add(Localisation.getString(ScaleSLDModel.class, "ScaleSLDModel.style"));
        columnNameList
                .add(Localisation.getString(ScaleSLDModel.class, "ScaleSLDModel.featureTypeStyle"));
        columnNameList.add(Localisation.getString(ScaleSLDModel.class, "ScaleSLDModel.rule"));
        columnNameList.add(Localisation.getString(ScaleSLDModel.class, "ScaleSLDModel.minScale"));
        columnNameList.add(Localisation.getString(ScaleSLDModel.class, "ScaleSLDModel.maxScale"));
    }

    /**
     * Gets the row count.
     *
     * @return the row count
     */
    @Override
    public int getRowCount() {
        return scaleList.size();
    }

    /**
     * Gets the column count.
     *
     * @return the column count
     */
    @Override
    public int getColumnCount() {
        return columnNameList.size();
    }

    /**
     * Gets the column name.
     *
     * @param column the column
     * @return the column name
     */
    @Override
    public String getColumnName(int column) {
        return columnNameList.get(column);
    }

    /**
     * Gets the value at.
     *
     * @param rowIndex the row index
     * @param columnIndex the column index
     * @return the value at
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        ScaleSLDData data = scaleList.get(rowIndex);

        switch (columnIndex) {
        case COL_WORKSPACE:
            return data.getWorkspace();
        case COL_SOURCE:
            return data.getName();
        case COL_NAMED_LAYER:
            return data.getNamedLayer();
        case COL_STYLE:
            return data.getStyle();
        case COL_FEATURE_TYPE_STYLE:
            return data.getFeatureTypeStyle();
        case COL_RULE:
            return data.getRuleName();
        case COL_MIN_SCALE:
            if (data.isMinScaleSet()) {
                return data.getMinScaleString();
            }
            break;
        case COL_MAX_SCALE:
            if (data.isMaxScaleSet()) {
                return data.getMaxScaleString();
            }
            break;
        default:
            break;
        }
        return null;
    }

    /**
     * Load data into model.
     *
     * @param dataList the data list
     */
    public void loadData(List<ScaleSLDData> dataList) {
        scaleList = dataList;

        this.fireTableDataChanged();
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
        return ((columnIndex == COL_MIN_SCALE) || (columnIndex == COL_MAX_SCALE));
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
        ScaleSLDData data = scaleList.get(rowIndex);

        if (columnIndex == COL_MIN_SCALE) {
            data.setMinScaleString(aValue);
        }

        if (columnIndex == COL_MAX_SCALE) {
            data.setMaxScaleString(aValue);
        }

        if (listener != null) {
            listener.dataUpdated();
        }
    }

    /**
     * Revert data to original.
     */
    public void revertData() {
        for (ScaleSLDData data : scaleList) {
            data.revertToOriginal();
        }

        this.fireTableDataChanged();
    }

    /**
     * Checks for value been updated.
     *
     * @param rowIndex the row index
     * @param columnIndex the column index
     * @return true, if successful
     */
    public boolean hasValueBeenUpdated(int rowIndex, int columnIndex) {
        ScaleSLDData data = scaleList.get(rowIndex);

        if (columnIndex == COL_MIN_SCALE) {
            return data.isMinimumScaleUpdated();
        }

        if (columnIndex == COL_MAX_SCALE) {
            return data.isMaximumScaleUpdated();
        }
        return false;
    }

    /**
     * Sets the column renderers.
     *
     * @param columnModel the new column renderer
     */
    public void setColumnRenderer(TableColumnModel columnModel) {
        columnModel.getColumn(COL_MIN_SCALE).setCellRenderer(new ScaleTableRenderer());
        columnModel.getColumn(COL_MAX_SCALE).setCellRenderer(new ScaleTableRenderer());
    }

    /**
     * Apply data.
     *
     * @param application the application
     */
    public boolean applyData(SLDEditorInterface application) {
        boolean refreshUI = false;
        if (sldWriter == null) {
            sldWriter = SLDWriterFactory.createWriter(null);
        }

        for (ScaleSLDData data : scaleList) {
            if (data.isMinimumScaleUpdated() || data.isMaximumScaleUpdated()) {

                if (data.updateScales(sldWriter)) {
                    refreshUI = true;
                }

                if (application != null) {
                    application.saveSLDData(data.getSldData());
                }
            }
        }

        this.fireTableDataChanged();

        return refreshUI;
    }
}
