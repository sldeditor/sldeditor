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

package com.sldeditor.common.vendoroption.info;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.common.vendoroption.NoVendorOption;
import com.sldeditor.common.vendoroption.VendorOptionVersion;
import com.sldeditor.common.vendoroption.VersionData;

/**
 * The Class VendorOptionInfoModel.
 *
 * @author Robert Ward (SCISYS)
 */
public class VendorOptionInfoModel extends AbstractTableModel {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The column names. */
    private String[] columnNames = new String[2];

    /** The info list. */
    private List<VendorOptionInfo> infoList = new ArrayList<VendorOptionInfo>();

    /** The selected version. */
    private VersionData selectedVersion = null;

    /**
     * Instantiates a new vendor option info model.
     */
    public VendorOptionInfoModel() {
        columnNames[0] = Localisation.getString(VendorOptionInfoModel.class,
                "VendorOptionInfoModel.name");
        columnNames[1] = Localisation.getString(VendorOptionInfoModel.class,
                "VendorOptionInfoModel.version");
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.TableModel#getRowCount()
     */
    @Override
    public int getRowCount() {
        return infoList.size();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.TableModel#getColumnCount()
     */
    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if ((rowIndex >= 0) && (rowIndex < infoList.size())) {
            VendorOptionInfo info = infoList.get(rowIndex);

            switch (columnIndex) {
            case 0:
                return info.getName();
            case 1:
                return info.getVersionString();
            default:
                break;
            }
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.AbstractTableModel#getColumnName(int)
     */
    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.AbstractTableModel#isCellEditable(int, int)
     */
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    /**
     * Adds the vendor option info.
     *
     * @param vendorOptionInfoList the vendor option info list
     */
    public void addVendorOptionInfo(List<VendorOptionInfo> vendorOptionInfoList) {
        if (vendorOptionInfoList != null) {
            infoList.addAll(vendorOptionInfoList);

            Collections.sort(infoList);
        }
    }

    /**
     * Sets the selected version.
     *
     * @param versionData the new selected version
     */
    public void setSelectedVersion(VersionData versionData) {
        this.selectedVersion = versionData;

        this.fireTableDataChanged();
    }

    /**
     * Checks if is vendor option available.
     *
     * @param row the row
     * @return true, if is vendor option available
     */
    public boolean isVendorOptionAvailable(int row) {
        if ((row >= 0) && (row < infoList.size())) {
            VendorOptionInfo info = infoList.get(row);
            VendorOptionVersion vendorOptionVersion = info.getVersionData();

            // Check to see if it is strict SLD, always allowed
            if (vendorOptionVersion.getClassType() == NoVendorOption.class) {
                return true;
            }

            if (vendorOptionVersion.getClassType() != selectedVersion.getVendorOptionType()) {
                return false;
            }

            return selectedVersion.inRange(vendorOptionVersion.getEarliest(),
                    vendorOptionVersion.getLatest());
        }
        return false;
    }

    /**
     * Gets the description.
     *
     * @param rowIndex the row index
     * @return the description
     */
    public String getDescription(int rowIndex) {
        if ((rowIndex >= 0) && (rowIndex < infoList.size())) {
            VendorOptionInfo info = infoList.get(rowIndex);

            if (info != null) {
                return info.getDescription();
            }
        }
        return null;
    }
}
