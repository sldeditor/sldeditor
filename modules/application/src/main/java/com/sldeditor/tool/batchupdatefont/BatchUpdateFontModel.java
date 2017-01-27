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
package com.sldeditor.tool.batchupdatefont;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumnModel;

import org.geotools.styling.Font;

import com.sldeditor.common.SLDEditorInterface;
import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.common.output.SLDWriterInterface;
import com.sldeditor.common.output.impl.SLDWriterFactory;

/**
 * Table model to allows the viewing and editing of ScaleSLDData objects.
 */
public class BatchUpdateFontModel extends AbstractTableModel {

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

    /** The symbolizer column index. */
    private static final int COL_SYMBOLIZER = 6;

    /** The font name column index. */
    private static final int COL_FONT_NAME = 7;

    /** The font name column index. */
    private static final int COL_FONT_STYLE = 8;

    /** The font name column index. */
    private static final int COL_FONT_WEIGHT = 9;

    /** The font name column index. */
    private static final int COL_FONT_SIZE = 10;

    /** The column name list. */
    private List<String> columnNameList = new ArrayList<String>();

    /** The font list. */
    private List<BatchUpdateFontData> fontList = new ArrayList<BatchUpdateFontData>();

    /** The sld writer. */
    private SLDWriterInterface sldWriter = null;

    /**
     * Constructor.
     */
    public BatchUpdateFontModel() {
        columnNameList.add(Localisation.getString(BatchUpdateFontModel.class,
                "BatchUpdateFontModel.workspace"));
        columnNameList.add(
                Localisation.getString(BatchUpdateFontModel.class, "BatchUpdateFontModel.source"));
        columnNameList.add(Localisation.getString(BatchUpdateFontModel.class,
                "BatchUpdateFontModel.namedLayer"));
        columnNameList.add(
                Localisation.getString(BatchUpdateFontModel.class, "BatchUpdateFontModel.style"));
        columnNameList.add(Localisation.getString(BatchUpdateFontModel.class,
                "BatchUpdateFontModel.featureTypeStyle"));
        columnNameList.add(
                Localisation.getString(BatchUpdateFontModel.class, "BatchUpdateFontModel.rule"));
        columnNameList.add(Localisation.getString(BatchUpdateFontModel.class,
                "BatchUpdateFontModel.symbolizer"));
        columnNameList.add(
                Localisation.getString(BatchUpdateFontModel.class, "BatchUpdateFontModel.name"));
        columnNameList.add(
                Localisation.getString(BatchUpdateFontModel.class, "BatchUpdateFontModel.style"));
        columnNameList.add(
                Localisation.getString(BatchUpdateFontModel.class, "BatchUpdateFontModel.weight"));
        columnNameList.add(
                Localisation.getString(BatchUpdateFontModel.class, "BatchUpdateFontModel.size"));
    }

    /**
     * Gets the row count.
     *
     * @return the row count
     */
    @Override
    public int getRowCount() {
        return fontList.size();
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
        BatchUpdateFontData data = fontList.get(rowIndex);

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
        case COL_SYMBOLIZER:
            return data.getSymbolizer();
        case COL_FONT_NAME:
            if (data.isFontNameSet()) {
                return data.getFontName();
            }
            break;
        case COL_FONT_STYLE:
            if (data.isFontStyleSet()) {
                return data.getFontStyle();
            }
            break;
        case COL_FONT_WEIGHT:
            if (data.isFontWeightSet()) {
                return data.getFontWeight();
            }
            break;
        case COL_FONT_SIZE:
            if (data.isFontSizeSet()) {
                return data.getFontSize();
            }
            break;
        }
        return null;
    }

    /**
     * Load data into model.
     *
     * @param dataList the data list
     */
    public void loadData(List<BatchUpdateFontData> dataList) {
        fontList = dataList;

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
        return false;
    }

    /**
     * Revert data to original.
     */
    public void revertData() {
        for (BatchUpdateFontData data : fontList) {
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
        BatchUpdateFontData data = fontList.get(rowIndex);

        if (columnIndex == COL_FONT_NAME) {
            return data.isFontNameUpdated();
        } else if (columnIndex == COL_FONT_STYLE) {
            return data.isFontStyleUpdated();
        } else if (columnIndex == COL_FONT_WEIGHT) {
            return data.isFontWeightUpdated();
        } else if (columnIndex == COL_FONT_SIZE) {
            return data.isFontSizeUpdated();
        }
        return false;
    }

    /**
     * Sets the column renderers.
     *
     * @param columnModel the new column renderer
     */
    public void setColumnRenderer(TableColumnModel columnModel) {
        columnModel.getColumn(COL_FONT_NAME).setCellRenderer(new BatchUpdateFontRenderer());
        columnModel.getColumn(COL_FONT_STYLE).setCellRenderer(new BatchUpdateFontRenderer());
        columnModel.getColumn(COL_FONT_WEIGHT).setCellRenderer(new BatchUpdateFontRenderer());
        columnModel.getColumn(COL_FONT_SIZE).setCellRenderer(new BatchUpdateFontRenderer());
    }

    /**
     * Apply data.
     *
     * @param application the application
     */
    public boolean saveData(SLDEditorInterface application) {
        boolean refreshUI = false;
        if (sldWriter == null) {
            sldWriter = SLDWriterFactory.createWriter(null);
        }

        for (BatchUpdateFontData data : fontList) {
            if (data.isFontNameUpdated() || data.isFontStyleUpdated() || data.isFontWeightUpdated()
                    || data.isFontSizeUpdated()) {

                if(data.updateFont(sldWriter))
                {
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

    /**
     * Gets the font entries.
     *
     * @param selectedRows the selected rows
     * @return the font entries
     */
    public List<Font> getFontEntries(int[] selectedRows) {
        List<Font> selectedFontList = null;
        if (selectedRows != null) {
            selectedFontList = new ArrayList<Font>();
            for (int row : selectedRows) {
                BatchUpdateFontData entry = fontList.get(row);
                selectedFontList.add(entry.getFont());
            }
        }
        return selectedFontList;
    }

    /**
     * Apply data.
     *
     * @param selectedRows the selected rows
     * @param fontData the font data
     */
    public void applyData(int[] selectedRows, Font fontData) {
        if (fontData != null) {
            for (int row : selectedRows) {
                BatchUpdateFontData entry = fontList.get(row);

                entry.updateFont(fontData);
            }
        }
        this.fireTableDataChanged();
    }

    /**
     * Find out if any data changes have been made
     *
     * @return true, if changes exists
     */
    public boolean anyChanges() {
        for (BatchUpdateFontData data : fontList) {
            if (data.anyChanges()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Apply font size data.
     *
     * @param selectedRows the selected rows
     * @param fontSize the font size
     */
    public void applyData(int[] selectedRows, int fontSize) {
        if (fontSize != 0) {
            for (int row : selectedRows) {
                BatchUpdateFontData entry = fontList.get(row);

                entry.updateFontSize(fontSize);
            }
        }
        this.fireTableDataChanged();
    }
}
