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

package com.sldeditor.ui.detail.config.colourmap;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.styling.ColorMap;
import org.geotools.styling.ColorMapEntry;
import org.geotools.styling.ColorMapEntryImpl;
import org.geotools.styling.ColorMapImpl;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;

import com.sldeditor.colourramp.ramp.ColourRampData;
import com.sldeditor.common.defaultsymbol.DefaultSymbols;
import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.common.utils.ColourUtils;
import com.sldeditor.ui.detail.config.FieldConfigBase;

/**
 * The Class ColourMapModel.
 *
 * @author Robert Ward (SCISYS)
 */
public class ColourMapModel extends AbstractTableModel {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The Constant COL_NUMBER. */
    private static final int COL_NUMBER = 0;

    /** The Constant COL_LABEL. */
    private static final int COL_LABEL = 1;

    /** The Constant COL_COLOUR. */
    private static final int COL_COLOUR = 2;

    /** The Constant COL_OPACITY. */
    private static final int COL_OPACITY = 3;

    /** The Constant COL_QUANTITY. */
    private static final int COL_QUANTITY = 4;

    /** The colour map list. */
    private List<ColourMapData> colourMapList = new ArrayList<ColourMapData>();

    /** The column list. */
    private List<String> columnList = new ArrayList<String>();

    /** The filter factory. */
    private static FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);

    /** The parent object to inform of any changes. */
    private ColourMapModelUpdateInterface parentObj = null;

    /**
     * Instantiates a new colour map model.
     *
     * @param parent the parent
     */
    public ColourMapModel(ColourMapModelUpdateInterface parent) {
        this.parentObj = parent;

        columnList.add(Localisation.getString(FieldConfigBase.class, "ColourMapModel.number"));
        columnList.add(Localisation.getString(FieldConfigBase.class, "ColourMapModel.label"));
        columnList.add(Localisation.getString(FieldConfigBase.class, "ColourMapModel.colour"));
        columnList.add(Localisation.getString(FieldConfigBase.class, "ColourMapModel.opacity"));
        columnList.add(Localisation.getString(FieldConfigBase.class, "ColourMapModel.value"));
    }

    /**
     * Gets the row count.
     *
     * @return the row count
     */
    @Override
    public int getRowCount() {
        return colourMapList.size();
    }

    /**
     * Gets the column count.
     *
     * @return the column count
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
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        ColourMapData colourMapData = colourMapList.get(rowIndex);

        switch (columnIndex) {
        case COL_NUMBER:
            return (rowIndex + 1);
        case COL_LABEL:
            return colourMapData.getLabel();
        case COL_COLOUR:
            return colourMapData.getColourString();
        case COL_OPACITY:
            return colourMapData.getOpacity();
        case COL_QUANTITY:
            return colourMapData.getQuantity();
        default:
            break;
        }
        return null;
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
     * Populate.
     *
     * @param value the value
     */
    public void populate(ColorMap value) {
        colourMapList.clear();

        for (ColorMapEntry colourMapEntry : value.getColorMapEntries()) {
            Expression colourExpression = colourMapEntry.getColor();
            Expression opacityExpression = colourMapEntry.getOpacity();
            Expression quantityExpression = colourMapEntry.getQuantity();
            String label = colourMapEntry.getLabel();

            ColourMapData data = new ColourMapData();

            data.setColour(colourExpression);
            data.setOpacity(opacityExpression);
            data.setQuantity(quantityExpression);
            data.setLabel(label);
            colourMapList.add(data);
        }
        this.fireTableDataChanged();
    }

    /**
     * Sets the cell renderer.
     *
     * @param table the new cell renderer
     */
    public void setCellRenderer(JTable table) {
        table.getColumnModel().getColumn(COL_COLOUR)
                .setCellRenderer(new ColourTableCellRenderer(this));
    }

    /**
     * Gets the colour.
     *
     * @param row the row
     * @return the colour
     */
    public Color getColour(int row) {
        ColourMapData colourMapData = colourMapList.get(row);

        return colourMapData.getColour();
    }

    /**
     * Adds a new colour map entry.
     */
    public void addNewEntry() {
        ColourMapData data = new ColourMapData();

        Expression quantity = ff.literal(0);

        // Get last entry
        if (!colourMapList.isEmpty()) {
            ColourMapData lastEntry = colourMapList.get(colourMapList.size() - 1);
            if (lastEntry != null) {
                quantity = lastEntry.getNextQuantity();
            }
        }

        data.setColour(ff.literal(ColourUtils.fromColour(ColourUtils.createRandomColour())));
        data.setOpacity(ff.literal(DefaultSymbols.defaultColourOpacity()));
        data.setQuantity(quantity);

        colourMapList.add(data);

        this.fireTableDataChanged();

        if (parentObj != null) {
            parentObj.colourMapUpdated();
        }
    }

    /**
     * Removes the selected colour map entries.
     *
     * @param minSelectionIndex the min selection index
     * @param maxSelectionIndex the max selection index
     */
    public void removeEntries(int minSelectionIndex, int maxSelectionIndex) {
        if ((maxSelectionIndex < minSelectionIndex)
                || (maxSelectionIndex >= colourMapList.size())) {
            return;
        }

        int index = maxSelectionIndex;

        while (index >= minSelectionIndex) {
            colourMapList.remove(index);
            index--;
        }
        this.fireTableDataChanged();

        if (parentObj != null) {
            parentObj.colourMapUpdated();
        }
    }

    /**
     * Gets the colour map.
     *
     * @return the colour map
     */
    public ColorMap getColourMap() {
        ColorMap colourMap = new ColorMapImpl();

        for (ColourMapData data : colourMapList) {
            ColorMapEntry entry = createColourMapEntry(data);

            colourMap.addColorMapEntry(entry);
        }
        return colourMap;
    }

    /**
     * Creates the colour map entry.
     *
     * @param data the data
     * @return the color map entry
     */
    private ColorMapEntry createColourMapEntry(ColourMapData data) {
        ColorMapEntry entry = new ColorMapEntryImpl();
        entry.setColor(data.getColourExpression());
        entry.setOpacity(data.getOpacity());
        entry.setQuantity(data.getQuantity());
        entry.setLabel(data.getLabel());
        return entry;
    }

    /**
     * Update colour ramp.
     *
     * @param data the data
     */
    public void updateColourRamp(ColourRampData data) {
        if (data != null) {
            for (ColourMapData row : colourMapList) {
                Expression colour = data.getColourRamp().getColour(data, row.getQuantity(),
                        data.reverseColours());

                row.setColour(colour);
            }

            this.fireTableDataChanged();
        }
    }

    /**
     * Gets the colour map entry.
     *
     * @param selectedRow the selected row
     * @return the colour map entry
     */
    public ColorMapEntry getColourMapEntry(int selectedRow) {
        if ((selectedRow < 0) || (selectedRow >= colourMapList.size())) {
            return null;
        }
        ColourMapData colourMapData = colourMapList.get(selectedRow);

        return createColourMapEntry(colourMapData);
    }

    /**
     * Gets the colour map entries.
     *
     * @param selectedRows the selected rows
     * @return the colour map entries
     */
    public List<ColorMapEntry> getColourMapEntries(int[] selectedRows) {
        if (selectedRows == null) {
            return null;
        }
        List<ColorMapEntry> entries = new ArrayList<ColorMapEntry>();

        for (int selectedRow : selectedRows) {
            ColourMapData colourMapData = colourMapList.get(selectedRow);

            entries.add(createColourMapEntry(colourMapData));
        }

        return entries;
    }

    /**
     * Update colour map entry.
     *
     * @param rows the rows
     * @param newData the new data
     */
    public void updateColourMapEntry(int[] rows, ColourMapData newData) {
        for (int selectedRow : rows) {
            if ((selectedRow >= 0) && (selectedRow < colourMapList.size())) {
                ColourMapData existingColourMapData = colourMapList.get(selectedRow);
                if (existingColourMapData != null) {
                    existingColourMapData.update(newData);
                }
            }
        }
        this.fireTableDataChanged();

        if (parentObj != null) {
            parentObj.colourMapUpdated();
        }
    }
}
