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

import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.ui.detail.config.FieldConfigBase;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.styling.Extent;
import org.geotools.styling.FeatureTypeConstraint;
import org.geotools.styling.StyleFactoryImpl;
import org.opengis.filter.Filter;

/**
 * The Class FeatureTypeConstraintModel.
 *
 * @author Robert Ward (SCISYS)
 */
public class FeatureTypeConstraintModel extends AbstractTableModel {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The Constant DEFAULT_NAME. */
    private static final String DEFAULT_NAME = "Feature";

    /** The Constant COL_NUMBER. */
    private static final int COL_NAME = 0;

    /** The Constant COL_LABEL. */
    private static final int COL_FILTER = 1;

    /** The feature type constraint list. */
    private transient List<FeatureTypeConstraint> ftcList = new ArrayList<>();

    /** The column list. */
    private List<String> columnList = new ArrayList<>();

    /** The parent object to inform of any changes. */
    private transient FeatureTypeConstraintModelUpdateInterface parentObj = null;

    /** The style factory. */
    private transient StyleFactoryImpl styleFactory =
            (StyleFactoryImpl) CommonFactoryFinder.getStyleFactory();

    /**
     * Instantiates a new feature type constraint model.
     *
     * @param parent the parent
     */
    public FeatureTypeConstraintModel(FeatureTypeConstraintModelUpdateInterface parent) {
        this.parentObj = parent;

        columnList.add(
                Localisation.getString(FieldConfigBase.class, "FeatureTypeConstraintModel.name"));
        columnList.add(
                Localisation.getString(FieldConfigBase.class, "FeatureTypeConstraintModel.filter"));
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.swing.table.TableModel#getRowCount()
     */
    @Override
    public int getRowCount() {
        return ftcList.size();
    }

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
     * Gets the column name.
     *
     * @param column the column
     * @return the column name
     */
    @Override
    public String getColumnName(int column) {
        return columnList.get(column);
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if ((rowIndex < 0) || (rowIndex >= getRowCount())) {
            return null;
        }

        if ((columnIndex < 0) || (columnIndex >= getColumnCount())) {
            return null;
        }

        FeatureTypeConstraint ftc = ftcList.get(rowIndex);

        switch (columnIndex) {
            case COL_NAME:
                return ftc.getFeatureTypeName();
            case COL_FILTER:
                if (ftc.getFilter() != null) {
                    return ftc.getFilter().toString();
                }
                break;
            default:
                break;
        }
        return null;
    }

    /** Adds the new entry. */
    public void addNewEntry() {

        FeatureTypeConstraint ftc =
                styleFactory.createFeatureTypeConstraint(
                        DEFAULT_NAME, Filter.INCLUDE, new Extent[0]);
        ftcList.add(ftc);

        this.fireTableDataChanged();

        if (parentObj != null) {
            parentObj.featureTypeConstraintUpdated();
        }
    }

    /**
     * Removes the entries.
     *
     * @param minSelectionIndex the min selection index
     * @param maxSelectionIndex the max selection index
     */
    public void removeEntries(int minSelectionIndex, int maxSelectionIndex) {
        if ((minSelectionIndex < 0)
                || (maxSelectionIndex < minSelectionIndex)
                || (maxSelectionIndex >= ftcList.size())) {
            return;
        }

        int index = maxSelectionIndex;

        while (index >= minSelectionIndex) {
            ftcList.remove(index);
            index--;
        }
        this.fireTableDataChanged();

        if (parentObj != null) {
            parentObj.featureTypeConstraintUpdated();
        }
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
        if ((rowIndex < 0) || (rowIndex >= getRowCount())) {
            return;
        }

        if ((columnIndex < 0) || (columnIndex >= getColumnCount())) {
            return;
        }

        FeatureTypeConstraint ftc = ftcList.get(rowIndex);

        switch (columnIndex) {
            case COL_NAME:
                setName(aValue, ftc);
                break;
            case COL_FILTER:
                break;
            default:
                break;
        }

        this.fireTableDataChanged();

        if (parentObj != null) {
            parentObj.featureTypeConstraintUpdated();
        }
    }

    /**
     * Sets the name.
     *
     * @param aValue the a value
     * @param ftc the ftc
     */
    private void setName(Object aValue, FeatureTypeConstraint ftc) {
        String name = (String) aValue;
        ftc.setFeatureTypeName(name);
    }

    /**
     * Populate.
     *
     * @param ftcList the ftc list
     */
    public void populate(List<FeatureTypeConstraint> ftcList) {
        this.ftcList.clear();

        if (ftcList != null) {
            for (FeatureTypeConstraint ftc : ftcList) {
                FeatureTypeConstraint newFTC =
                        styleFactory.createFeatureTypeConstraint(
                                ftc.getFeatureTypeName(), ftc.getFilter(), new Extent[0]);

                this.ftcList.add(newFTC);
            }
        }
        this.fireTableDataChanged();
    }

    /**
     * Gets the feature type constraint.
     *
     * @return the feature type constraint
     */
    public List<FeatureTypeConstraint> getFeatureTypeConstraint() {
        return ftcList;
    }

    /**
     * Gets the feature type constraint.
     *
     * @param row the row
     * @return the feature type constraint
     */
    public FeatureTypeConstraint getFeatureTypeConstraint(int row) {
        if ((row >= 0) && (row < ftcList.size())) {
            return ftcList.get(row);
        }
        return null;
    }

    /**
     * Checks if the selected column is filter column.
     *
     * @param selectedColumns the selected columns
     * @return true, if is filter column
     */
    public boolean isFilterColumn(int[] selectedColumns) {
        if (selectedColumns != null) {
            for (int column : selectedColumns) {
                if (column == COL_FILTER) {
                    return true;
                }
            }
        }
        return false;
    }
}
