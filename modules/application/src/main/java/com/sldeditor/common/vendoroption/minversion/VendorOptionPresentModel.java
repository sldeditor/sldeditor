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

package com.sldeditor.common.vendoroption.minversion;

import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.common.vendoroption.VersionData;
import com.sldeditor.common.vendoroption.info.VendorOptionInfo;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 * The Class VendorOptionPresentModel.
 *
 * @author Robert Ward (SCISYS)
 */
public class VendorOptionPresentModel extends AbstractTableModel {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    private List<VendorOptionPresent> vendorOptionsPresentList = null;

    /** The column names. */
    private String[] columnNames = new String[2];

    /** Instantiates a new vendor option present model. */
    public VendorOptionPresentModel() {
        columnNames[0] =
                Localisation.getString(
                        VendorOptionPresentModel.class, "VendorOptionPresentModel.name");
        columnNames[1] =
                Localisation.getString(
                        VendorOptionPresentModel.class, "VendorOptionPresentModel.version");
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.swing.table.TableModel#getRowCount()
     */
    @Override
    public int getRowCount() {
        if (vendorOptionsPresentList == null) {
            return 0;
        }
        return vendorOptionsPresentList.size();
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
     * @see javax.swing.table.AbstractTableModel#getColumnName(int)
     */
    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if ((rowIndex >= 0) && (rowIndex < vendorOptionsPresentList.size())) {
            VendorOptionPresent voPresent = vendorOptionsPresentList.get(rowIndex);
            VendorOptionInfo info = voPresent.getVendorOptionInfo();

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

    /**
     * Populate.
     *
     * @param vendorOptionsPresentList the vendor options present list
     */
    public void populate(List<VendorOptionPresent> vendorOptionsPresentList) {
        this.vendorOptionsPresentList = vendorOptionsPresentList;

        this.fireTableDataChanged();
    }

    /**
     * Gets the minimum vendor option version.
     *
     * @return the minimum vendor option version
     */
    public List<VersionData> getMinimum() {
        List<VersionData> minimum = new ArrayList<VersionData>();
        VendorOptionPresent voPresent =
                vendorOptionsPresentList.get(vendorOptionsPresentList.size() - 1);

        VersionData data = voPresent.getVendorOptionInfo().getVersionData().getEarliest();
        minimum.add(data);

        return minimum;
    }
}
