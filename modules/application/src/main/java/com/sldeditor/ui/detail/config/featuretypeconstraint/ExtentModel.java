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

package com.sldeditor.ui.detail.config.featuretypeconstraint;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.styling.Extent;
import org.geotools.styling.StyleFactoryImpl;

import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.ui.detail.config.FieldConfigBase;

/**
 * The Class ExtentModel.
 *
 * @author Robert Ward (SCISYS)
 */
public class ExtentModel extends AbstractTableModel {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The Constant COL_MINX. */
    private static final int COL_MINX = 0;

    /** The Constant COL_MINY. */
    private static final int COL_MINY = 1;

    /** The Constant COL_MAXX. */
    private static final int COL_MAXX = 2;

    /** The Constant COL_MAXY. */
    private static final int COL_MAXY = 3;

    /** The extent list. */
    private List<Extent> extentList = new ArrayList<Extent>();

    /** The column list. */
    private List<String> columnList = new ArrayList<String>();

    /** The parent object to inform of any changes. */
    private FeatureTypeConstraintModelUpdateInterface parentObj = null;

    /** The style factory. */
    private static StyleFactoryImpl styleFactory = (StyleFactoryImpl) CommonFactoryFinder.getStyleFactory();

    /**
     * Instantiates a new feature type constraint model.
     *
     * @param parent the parent
     */
    public ExtentModel(FeatureTypeConstraintModelUpdateInterface parent) {
        this.parentObj = parent;

        columnList.add(Localisation.getString(FieldConfigBase.class, "FeatureTypeConstraintModel.minX"));
        columnList.add(Localisation.getString(FieldConfigBase.class, "FeatureTypeConstraintModel.minY"));
        columnList.add(Localisation.getString(FieldConfigBase.class, "FeatureTypeConstraintModel.maxX"));
        columnList.add(Localisation.getString(FieldConfigBase.class, "FeatureTypeConstraintModel.maxY"));
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getRowCount()
     */
    @Override
    public int getRowCount() {
        return extentList.size();
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
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Extent extent = extentList.get(rowIndex);

        String[] components = extent.getValue().split(" ");

        if(components != null)
        {
            switch(columnIndex)
            {
            case COL_MINX:
                if(components.length > 0)
                {
                    return components[0];
                }
                break;
            case COL_MINY:
                if(components.length > 1)
                {
                    return components[1];
                }
                break;
            case COL_MAXX:
                if(components.length > 2)
                {
                    return components[2];
                }
                break;
            case COL_MAXY:
                if(components.length > 3)
                {
                    return components[3];
                }
                break;
            default:
                break;
            }
        }
        return null;
    }

    /**
     * Adds the new entry.
     */
    public void addNewEntry() {

        Extent extent = styleFactory.createExtent("", "0 0 0 0");

        extentList.add(extent);

        this.fireTableDataChanged();
    }

    /**
     * Removes the entries.
     *
     * @param minSelectionIndex the min selection index
     * @param maxSelectionIndex the max selection index
     */
    public void removeEntries(int minSelectionIndex, int maxSelectionIndex) {
        if((maxSelectionIndex < minSelectionIndex) || (maxSelectionIndex >= extentList.size()))
        {
            return;
        }

        int index = maxSelectionIndex;

        while(index >= minSelectionIndex)
        {
            extentList.remove(index);
            index --;
        }
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
        Extent extent = extentList.get(rowIndex);

        String[] components = extent.getValue().split(" ");

        if(components.length != 4)
        {
            components = new String[4];
            components[0] = "0";
            components[1] = "0";
            components[2] = "0";
            components[3] = "0";
        }

        Double value = 0.0;

        try
        {
            value = Double.valueOf((String)aValue);
            switch(columnIndex)
            {
            case COL_MINX:
                components[0] = value.toString();
                break;
            case COL_MINY:
                components[1] = value.toString();
                break;
            case COL_MAXX:
                components[2] = value.toString();
                break;
            case COL_MAXY:
                components[3] = value.toString();
                break;
            default:
                break;
            }
        }
        catch(NumberFormatException e)
        {
            // Ignore
        }

        String encodedExtent = String.format("%s %s %s %s", components[0], components[1], components[2], components[3]);

        extent.setValue(encodedExtent);
        this.fireTableDataChanged();

        if(parentObj != null)
        {
            parentObj.featureTypeConstraintUpdated();
        }
    }

    /**
     * Populate.
     *
     * @param extentArray the extent array
     */
    public void populate(Extent[] extentArray) {
        this.extentList.clear();

        if(extentArray != null)
        {
            for(Extent extent : extentArray)
            {
                Extent newExtent = styleFactory.createExtent(extent.getName(), extent.getValue());

                this.extentList.add(newExtent);
            }
        }
        this.fireTableDataChanged();
    }

    /**
     * Gets the extent list.
     *
     * @return the extent list
     */
    public List<Extent> getExtentList() {
        return extentList;
    }

}
