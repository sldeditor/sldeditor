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
package com.sldeditor.common.vendoroption.selection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.common.vendoroption.VendorOptionManager;
import com.sldeditor.common.vendoroption.VendorOptionTypeInterface;
import com.sldeditor.common.vendoroption.VersionData;
import com.sldeditor.common.vendoroption.info.VendorOptionInfoManager;

/**
 * Table model that allows the viewing and editing of VendorOption objects.
 * 
 * @author Robert Ward (SCISYS)
 */
public class VendorOptionTableModel extends AbstractTableModel
{

    /** The column names. */
    private String[] columnNames = new String[2];

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The selected version list. */
    private List<VersionData> selectedVersionList = new ArrayList<VersionData>();

    /** The name map. */
    private Map<VendorOptionTypeInterface, String> nameMap = new HashMap<VendorOptionTypeInterface, String>();

    /** The name order. */
    private List<VendorOptionTypeInterface> nameOrder = null;

    /**
     * Instantiates a new vendor option model.
     *
     * @param options the options
     */
    public VendorOptionTableModel(Map<VendorOptionTypeInterface, String> options)
    {
        columnNames[0] = Localisation.getString(VendorOptionTableModel.class, "VendorOptionTableModel.vendor");
        columnNames[1] = Localisation.getString(VendorOptionTableModel.class, "VendorOptionTableModel.selected");

        List<VendorOptionTypeInterface> orderList = new ArrayList<VendorOptionTypeInterface>();

        if(options != null)
        {
            nameMap = options;

            for(VendorOptionTypeInterface key : options.keySet())
            {
                orderList.add(key);
                selectedVersionList.add(VersionData.getLatestVersion(key.getClass()));
            }
        }
        nameOrder = orderList;
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getRowCount()
     */
    @Override
    public int getRowCount() {
        return nameMap.size();
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getColumnCount()
     */
    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if((rowIndex >= 0) && (rowIndex < nameOrder.size()))
        {
            VendorOptionTypeInterface index = nameOrder.get(rowIndex);

            if(columnIndex == 0)
            {
                return nameMap.get(index);
            }
            else if(columnIndex == 1)
            {
                return selectedVersionList.get(rowIndex);
            }
        }
        return null;
    }

    /* (non-Javadoc)
     * @see javax.swing.table.AbstractTableModel#getColumnName(int)
     */
    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    /* (non-Javadoc)
     * @see javax.swing.table.AbstractTableModel#isCellEditable(int, int)
     */
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return (columnIndex == 1);
    }

    /* (non-Javadoc)
     * @see javax.swing.table.AbstractTableModel#setValueAt(java.lang.Object, int, int)
     */
    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

        if((columnIndex == 1) && (rowIndex >= 0) && (rowIndex < selectedVersionList.size()))
        {
            VersionData value = (VersionData) aValue;

            selectedVersionList.remove(rowIndex);
            selectedVersionList.add(value);

            fireTableCellUpdated(rowIndex, columnIndex);
        }
    }

    /* (non-Javadoc)
     * @see javax.swing.table.AbstractTableModel#getColumnClass(int)
     */
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if(columnIndex == 1)
        {
            return String.class;
        }
        return super.getColumnClass(columnIndex);
    }

    /**
     * Gets the vendor option version list.
     *
     * @return the vendor option version list
     */
    public List<VersionData> getVendorOptionVersionList() {

        List<VersionData> localVendorOptionList = new ArrayList<VersionData>();

        localVendorOptionList.add(VendorOptionManager.getInstance().getDefaultVendorOptionVersionData());

        // Add all non-Not Set values
        for(VersionData versionData : selectedVersionList)
        {
            if(!versionData.isNotSet())
            {
                localVendorOptionList.add(versionData);
            }
        }

        return localVendorOptionList;
    }

    /**
     * Sets the selected vendor option versions.
     *
     * @param selectedVersionList the new selected vendor option versions
     */
    public void setSelectedVendorOptionVersions(List<VersionData> selectedVersionList) {
        if(selectedVersionList != null)
        {
            this.selectedVersionList = new ArrayList<VersionData>();

            for(VendorOptionTypeInterface key : nameOrder)
            {
                boolean found = false;
                for(VersionData versionData : selectedVersionList)
                {
                    if(versionData.getVendorOptionType() == key.getClass())
                    {
                        found = true;
                        this.selectedVersionList.clear();
                        this.selectedVersionList.add(versionData);
                        VendorOptionInfoManager.getInstance().setSelectedVersion(versionData);
                    }
                }

                if(!found)
                {
                    this.selectedVersionList.add(VersionData.getNotSetVersion(key.getClass()));
                }
            }

            VersionData defaultVendorOption = VendorOptionManager.getInstance().getDefaultVendorOptionVersionData();

            this.selectedVersionList.remove(defaultVendorOption);

            this.fireTableDataChanged();
        }
    }

    /**
     * Gets the vendor option version data list.
     *
     * @param row the row
     * @return the vendor option
     */
    public List<VersionData> getVendorOption(int row)
    {
        if((row >= 0) && (row < nameOrder.size()))
        {
            VendorOptionTypeInterface vendorOption = nameOrder.get(row);

            return vendorOption.getVersionList();
        }

        return null;
    }

    /**
     * Sets the selected version.
     *
     * @param versionData the version data
     * @param selectedRowIndex the selected row index
     */
    public void setSelectedVersion(VersionData versionData, int selectedRowIndex)
    {
        setValueAt(versionData, selectedRowIndex, 1);
        VendorOptionInfoManager.getInstance().setSelectedVersion(versionData);
    }
}
