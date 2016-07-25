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

import org.geotools.filter.text.cql2.CQL;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.styling.FeatureTypeConstraint;
import org.geotools.styling.FeatureTypeConstraintImpl;
import org.opengis.filter.Filter;

import com.sldeditor.common.console.ConsoleManager;
import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.ui.detail.config.FieldConfigBase;

/**
 * The Class FeatureTypeConstraintModel.
 *
 * @author Robert Ward (SCISYS)
 */
public class FeatureTypeConstraintModel extends AbstractTableModel {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The Constant COL_NUMBER. */
    private static final int COL_NAME = 0;

    /** The Constant COL_LABEL. */
    private static final int COL_FILTER = 1;

    /** The feature type constraint list. */
    private List<FeatureTypeConstraint> ftcList = new ArrayList<FeatureTypeConstraint>();

    /** The column list. */
    private List<String> columnList = new ArrayList<String>();

    /** The parent object to inform of any changes. */
    private FeatureTypeConstraintModelUpdateInterface parentObj = null;

    /**
     * Instantiates a new feature type constraint model.
     *
     * @param parent the parent
     */
    public FeatureTypeConstraintModel(FeatureTypeConstraintModelUpdateInterface parent) {
        this.parentObj = parent;

        columnList.add(Localisation.getString(FieldConfigBase.class, "FeatureTypeConstraintModel.name"));
        columnList.add(Localisation.getString(FieldConfigBase.class, "FeatureTypeConstraintModel.filter"));
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getRowCount()
     */
    @Override
    public int getRowCount() {
        return ftcList.size();
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
        FeatureTypeConstraint ftc = ftcList.get(rowIndex);

        switch(columnIndex)
        {
        case COL_NAME:
            return ftc.getFeatureTypeName();
        case COL_FILTER:
            if(ftc.getFilter() != null)
            {
                return ftc.getFilter().toString();
            }
        default:
            break;
        }
        return null;
    }

    /**
     * Adds the new entry.
     */
    public void addNewEntry() {
        FeatureTypeConstraint ftc = new FeatureTypeConstraintImpl();
        ftc.setFeatureTypeName("New type");
        ftcList.add(ftc);

        this.fireTableDataChanged();
    }

    /**
     * Removes the entries.
     *
     * @param minSelectionIndex the min selection index
     * @param maxSelectionIndex the max selection index
     */
    public void removeEntries(int minSelectionIndex, int maxSelectionIndex) {
        if((maxSelectionIndex < minSelectionIndex) || (maxSelectionIndex >= ftcList.size()))
        {
            return;
        }

        int index = maxSelectionIndex;

        while(index >= minSelectionIndex)
        {
            ftcList.remove(index);
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
        FeatureTypeConstraint ftc = ftcList.get(rowIndex);

        switch(columnIndex)
        {
        case COL_NAME:
        {
            String name = (String) aValue;
            ftc.setFeatureTypeName(name);
        }
        break;
        case COL_FILTER:
            break;
        default:
            break;
        }

        this.fireTableDataChanged();

        if(parentObj != null)
        {
            parentObj.featureTypeConstraintUpdated();
        }
    }

    /**
     * Populate.
     *
     * @param ftcList the ftc list
     */
    public void populate(List<FeatureTypeConstraint> ftcList) {
        this.ftcList.clear();

        for(FeatureTypeConstraint ftc : ftcList)
        {
            FeatureTypeConstraint newFTC = new FeatureTypeConstraintImpl();
            newFTC.setFeatureTypeName(ftc.getFeatureTypeName());

            Filter newFilter = null;
            try {
                newFilter = CQL.toFilter(CQL.toCQL(ftc.getFilter()));
            } catch (CQLException e) {
                ConsoleManager.getInstance().exception(this, e);
            }

            newFTC.setFilter(newFilter);

            this.ftcList.add(newFTC);
        }
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
        if((row >= 0) && (row < ftcList.size()))
        {
            FeatureTypeConstraint ftc = ftcList.get(row);

            return ftc;
        }
        return null;
    }

}
