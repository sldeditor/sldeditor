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
import org.geotools.filter.LiteralExpressionImpl;
import org.geotools.styling.ColorMap;
import org.geotools.styling.ColorMapEntry;
import org.geotools.styling.ColorMapEntryImpl;
import org.geotools.styling.ColorMapImpl;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;

import com.sldeditor.colourramp.ramp.ColourRampData;
import com.sldeditor.common.console.ConsoleManager;
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

    /** The Constant COL_RGB. */
    private static final int COL_RGB = 3;

    /** The Constant COL_OPACITY. */
    private static final int COL_OPACITY = 4;

    /** The Constant COL_QUANTITY. */
    private static final int COL_QUANTITY = 5;

    /** The colour map list. */
    private List<ColourMapData> colourMapList = new ArrayList<ColourMapData>();

    /** The column list. */
    private List<String> columnList = new ArrayList<String>();

    /** The filter factory. */
    private static FilterFactory ff = CommonFactoryFinder.getFilterFactory( null );

    /** The parent object to inform of any changes. */
    private ColourMapModelUpdateInterface parentObj = null;

    /**
     * Instantiates a new colour map model.
     *
     * @param parent the parent
     */
    public ColourMapModel(ColourMapModelUpdateInterface parent)
    {
        this.parentObj = parent;

        columnList.add(Localisation.getString(FieldConfigBase.class, "ColourMapModel.number"));
        columnList.add(Localisation.getString(FieldConfigBase.class, "ColourMapModel.label"));
        columnList.add(Localisation.getString(FieldConfigBase.class, "ColourMapModel.colour"));
        columnList.add(Localisation.getString(FieldConfigBase.class, "ColourMapModel.rgb"));
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

        switch(columnIndex)
        {
        case COL_NUMBER:
            return (rowIndex + 1);
        case COL_LABEL:
            return colourMapData.getLabel();
        case COL_COLOUR:
            break;
        case COL_RGB:
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
        return (columnIndex != COL_NUMBER);
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
        ColourMapData colourMapData = colourMapList.get(rowIndex);

        switch(columnIndex)
        {
        case COL_COLOUR:
        {
            Color colour = (Color) aValue;
            colourMapData.setColour(colour);
        }
        break;
        case COL_LABEL:
            colourMapData.setLabel((String) aValue);
            break;
        case COL_RGB:
        {
            Color colour = ColourUtils.toColour((String) aValue);
            colourMapData.setColour(colour);
        }
        break;
        case COL_OPACITY:
            colourMapData.setOpacity((double) aValue);
            break;
        case COL_QUANTITY:
            colourMapData.setQuantity((int) aValue);
            break;
        case COL_NUMBER:
        default:
            break;
        }

        this.fireTableDataChanged();

        if(parentObj != null)
        {
            parentObj.colourMapUpdated();
        }
    }

    /**
     * Populate.
     *
     * @param value the value
     */
    public void populate(ColorMap value) {
        colourMapList.clear();

        for(ColorMapEntry colourMapEntry : value.getColorMapEntries())
        {
            Expression colourExpression = colourMapEntry.getColor();
            Expression opacityExpression = colourMapEntry.getOpacity();
            Expression quantityExpression = colourMapEntry.getQuantity();
            String label = colourMapEntry.getLabel();

            ColourMapData data = new ColourMapData();

            if(colourExpression != null)
            {
                String colourString = (String) ((LiteralExpressionImpl)colourExpression).getValue();
                double opacity = 1.0;
                if(opacityExpression != null)
                {
                    Object opacityValue = ((LiteralExpressionImpl)opacityExpression).getValue();
                    if(opacityValue instanceof Double)
                    {
                        opacity = (double) opacityValue;
                    }
                    else if(opacityValue instanceof String)
                    {
                        opacity = Double.valueOf((String)opacityValue);
                    }
                    else
                    {
                        ConsoleManager.getInstance().error(this, "Unknown opacity type" + opacityValue.getClass().getName());
                    }
                }
                int quantity = 1;
                if(quantityExpression != null)
                {
                    Object quantityValue = ((LiteralExpressionImpl)quantityExpression).getValue();
                    if(quantityValue instanceof Integer)
                    {
                        quantity = ((Integer) quantityValue).intValue();
                    }
                    else if(quantityValue instanceof Double)
                    {
                        quantity = ((Double) quantityValue).intValue();
                    }
                    else if(quantityValue instanceof String)
                    {
                        quantity = Integer.valueOf((String) quantityValue);
                    }
                }

                data.setColour(ColourUtils.toColour(colourString));
                data.setOpacity(opacity);
                data.setQuantity(quantity);
                data.setLabel(label);
                colourMapList.add(data);
            }
        }
        this.fireTableDataChanged();
    }

    /**
     * Sets the cell renderer.
     *
     * @param table the new cell renderer
     */
    public void setCellRenderer(JTable table) {
        table.getColumnModel().getColumn(COL_COLOUR).setCellRenderer(new ColourTableCellRenderer(this));
        table.getColumnModel().getColumn(COL_COLOUR).setCellEditor(new ColourEditor(this));
        table.getColumnModel().getColumn(COL_QUANTITY).setCellEditor(new SpinnerEditor());
        table.getColumnModel().getColumn(COL_OPACITY).setCellEditor(new SpinnerEditor(0.0, 1.0, 0.01));
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

        double opacity = 1.0;
        int quantity = 0;

        data.setColour(Color.black);
        data.setOpacity(opacity);
        data.setQuantity(quantity);

        colourMapList.add(data);

        this.fireTableDataChanged();
    }

    /**
     * Removes the selected colour map entries.
     */
    public void removeEntries(int minSelectionIndex, int maxSelectionIndex) {
        if((maxSelectionIndex < minSelectionIndex) || (maxSelectionIndex >= colourMapList.size()))
        {
            return;
        }

        int index = maxSelectionIndex;

        while(index >= minSelectionIndex)
        {
            colourMapList.remove(index);
            index --;
        }
        this.fireTableDataChanged();
    }

    /**
     * Gets the colour map.
     *
     * @return the colour map
     */
    public ColorMap getColourMap() {
        ColorMap colourMap = new ColorMapImpl();

        for(ColourMapData data : colourMapList)
        {
            ColorMapEntry entry = new ColorMapEntryImpl();
            entry.setColor(ff.literal(data.getColourString()));
            entry.setOpacity(ff.literal(data.getOpacity()));
            entry.setQuantity(ff.literal(Integer.valueOf(data.getQuantity()).toString()));
            entry.setLabel(data.getLabel());

            colourMap.addColorMapEntry(entry);
        }
        return colourMap;
    }

    /**
     * Update colour ramp.
     *
     * @param data the data
     */
    public void updateColourRamp(ColourRampData data) {
        if(data != null)
        {
            for(ColourMapData row : colourMapList)
            {
                Color colour = data.getColourRamp().getColour(data, row.getQuantity());

                row.setColour(colour);
            }

            this.fireTableDataChanged();
        }
    }

}
