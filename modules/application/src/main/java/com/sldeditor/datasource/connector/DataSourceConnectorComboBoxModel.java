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
package com.sldeditor.datasource.connector;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

import com.sldeditor.common.DataSourceConnectorInterface;

/**
 * Combo box model to support DataSourceConnectorInterface objects.
 * 
 * @author Robert Ward (SCISYS)
 */
public class DataSourceConnectorComboBoxModel extends AbstractListModel<String> implements ComboBoxModel<String>
{
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The data source connector list. */
    private Map<Class<?>, DataSourceConnectorInterface> dscMap = null;

    /** The dsc display name list. */
    private List<String> dscDisplayNameList = new ArrayList<String>();

    /** The selected item. */
    private DataSourceConnectorInterface selectedItem = null;

    /** The selected name. */
    private String selectedName = null;

    /**
     * Instantiates a new data source connector combo box model.
     *
     * @param dscMap the map of available data source connectors
     */
    public DataSourceConnectorComboBoxModel(Map<Class<?>, DataSourceConnectorInterface> dscMap)
    {
        this.dscMap = dscMap;

        for(Class<?> key : dscMap.keySet())
        {
            String displayName = dscMap.get(key).getDisplayName();
            dscDisplayNameList.add(displayName);
        }
    }

    /* (non-Javadoc)
     * @see javax.swing.ListModel#getSize()
     */
    @Override
    public int getSize()
    {
        return dscMap.size();
    }

    /* (non-Javadoc)
     * @see javax.swing.ListModel#getElementAt(int)
     */
    @Override
    public String getElementAt(int index)
    {
        return dscDisplayNameList.get(index);
    }

    /* (non-Javadoc)
     * @see javax.swing.ComboBoxModel#setSelectedItem(java.lang.Object)
     */
    @Override
    public void setSelectedItem(Object anItem)
    {
        selectedName = (String)anItem;
        selectedItem = null;

        if(selectedName != null)
        {
            for(Class<?> key : dscMap.keySet())
            {
                DataSourceConnectorInterface dsc = dscMap.get(key);
                if(dsc.getDisplayName().compareTo(selectedName) == 0)
                {
                    selectedItem = dsc;
                    break;
                }
            }
        }

        fireContentsChanged(this, -1, -1);
    }

    /* (non-Javadoc)
     * @see javax.swing.ComboBoxModel#getSelectedItem()
     */
    @Override
    public Object getSelectedItem()
    {
        return selectedName;
    }

    /**
     * Gets the selected dsc item.
     *
     * @return the selected dsc item
     */
    public DataSourceConnectorInterface getSelectedDSCItem()
    {
        return selectedItem;
    }

    /**
     * Reset.
     */
    public void reset() {
        for(Class<?> key : dscMap.keySet())
        {
            dscMap.get(key).reset();
        }
    }
}
