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

package com.sldeditor.ui.detail.vendor.geoserver.marker.wkt;

import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

/**
 * ComboBoxModel used to view and set the WKT type.
 *
 * @author Robert Ward (SCISYS)
 */
@SuppressWarnings("rawtypes")
public class WKTTypeComboBoxModel extends AbstractListModel implements ComboBoxModel {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The wkt type list. */
    private List<WKTType> wktTypeList = new ArrayList<WKTType>();

    /** The selection. */
    private WKTType selection = null;

    /**
     * Instantiates a new WKT type combo box model.
     *
     * @param wktTypeDataList the wkt type data list
     */
    public WKTTypeComboBoxModel(List<WKTType> wktTypeDataList) {
        this.wktTypeList = wktTypeDataList;
    }

    /* (non-Javadoc)
     * @see javax.swing.ListModel#getElementAt(int)
     */
    public Object getElementAt(int index) {
        return wktTypeList.get(index);
    }

    /* (non-Javadoc)
     * @see javax.swing.ListModel#getSize()
     */
    public int getSize() {
        return wktTypeList.size();
    }

    /* (non-Javadoc)
     * @see javax.swing.ComboBoxModel#setSelectedItem(java.lang.Object)
     */
    public void setSelectedItem(Object anItem) {
        selection = (WKTType) anItem; // to select and register an
    } // item from the pull-down list

    /* (non-Javadoc)
     * @see javax.swing.ComboBoxModel#getSelectedItem()
     */
    // Methods implemented from the interface ComboBoxModel
    public Object getSelectedItem() {
        return selection; // to add the selection to the combo box
    }
}
