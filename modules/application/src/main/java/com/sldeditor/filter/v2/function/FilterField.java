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
package com.sldeditor.filter.v2.function;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import org.opengis.filter.Filter;

import com.sldeditor.common.undo.UndoActionInterface;
import com.sldeditor.common.undo.UndoEvent;
import com.sldeditor.common.undo.UndoInterface;
import com.sldeditor.common.undo.UndoManager;
import com.sldeditor.ui.attribute.SubPanelUpdatedInterface;

/**
 * Panel to be able to edit FilterField objects.
 * 
 * @author Robert Ward (SCISYS)
 */
public class FilterField extends JPanel implements UndoActionInterface {

    /** The Constant FILTER_PANEL. */
    private static final String FILTER_PANEL = "Filter";

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The filter combo box. */
    private JComboBox<String> filterComboBox;

    /** The filter name map. */
    private Map<String, FilterConfigInterface> filterNameMap = new LinkedHashMap<String, FilterConfigInterface>();

    /** The old value obj. */
    private Object oldValueObj = null;

    /** The filter name manager. */
    private FilterNameInterface filterNameMgr = null;

    /**
     * Gets the panel name.
     *
     * @return the panel name
     */
    public static String getPanelName()
    {
        return FILTER_PANEL;
    }

    /**
     * Instantiates a new data source attribute panel.
     *
     * @param parentObj the parent obj
     * @param functionNameMgr the function name mgr
     */
    public FilterField(SubPanelUpdatedInterface parentObj, 
            FilterNameInterface functionNameMgr)
    {
        final UndoActionInterface thisObj = this;
        this.filterNameMgr = functionNameMgr;

        setLayout(new BorderLayout(5, 0));

        filterComboBox = new JComboBox<String>();
        add(filterComboBox, BorderLayout.CENTER);
        filterComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                String newValueObj = (String) filterComboBox.getSelectedItem();

                UndoManager.getInstance().addUndoEvent(new UndoEvent(thisObj, "Function", oldValueObj, newValueObj));

                if(parentObj != null)
                {
                    parentObj.updateSymbol();
                }
            }
        });

        List<FilterConfigInterface> filterConfigList = filterNameMgr.getFilterConfigList();
        for(FilterConfigInterface filterConfig : filterConfigList)
        {
            String filterNameString = filterConfig.getFilterConfiguration().getFilterName();

            filterNameMap.put(filterNameString, filterConfig);
        }

        populateFunctionComboBox();
    }

    /**
     * Populate function combo box.
     */
    private void populateFunctionComboBox() {
        if(filterComboBox != null)
        {
            DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String>();

            model.addElement("");

            for(String name : filterNameMap.keySet())
            {
                FilterConfigInterface filterConfig = filterNameMap.get(name);
                model.addElement(filterConfig.getFilterConfiguration().getFilterName());
            }
            filterComboBox.setModel(model);
        }
    }

    /**
     * Gets the selected item.
     *
     * @return the selected item
     */
    public String getSelectedItem()
    {
        return (String) filterComboBox.getSelectedItem();
    }

    /**
     * Sets the panel enabled.
     *
     * @param enabled the new panel enabled
     */
    public void setPanelEnabled(boolean enabled)
    {
        filterComboBox.setEnabled(enabled);
    }

    /**
     * Sets the filter.
     *
     * @param filter the new filter
     * @param filterConfig the filter config
     */
    public void setFilter(Filter filter, FilterConfigInterface filterConfig) {

        oldValueObj = filterConfig;

        if(filterConfig == null)
        {
            filterComboBox.setSelectedItem(null);
        }
        else
        {
            filterComboBox.setSelectedItem(filterConfig.getFilterConfiguration().getFilterName());
        }
    }

    /**
     * Undo action.
     *
     * @param undoRedoObject the undo redo object
     */
    /* (non-Javadoc)
     * @see com.sldeditor.undo.UndoActionInterface#undoAction(com.sldeditor.undo.UndoInterface)
     */
    @Override
    public void undoAction(UndoInterface undoRedoObject) {
        String oldValueObj = (String)undoRedoObject.getOldValue();

        filterComboBox.setSelectedItem(oldValueObj);
    }

    /**
     * Redo action.
     *
     * @param undoRedoObject the undo redo object
     */
    /* (non-Javadoc)
     * @see com.sldeditor.undo.UndoActionInterface#redoAction(com.sldeditor.undo.UndoInterface)
     */
    @Override
    public void redoAction(UndoInterface undoRedoObject) {
        String newValueObj = (String)undoRedoObject.getNewValue();

        filterComboBox.setSelectedItem(newValueObj);
    }

    /**
     * Gets the filter.
     *
     * @return the filter
     */
    public Filter getFilter() {
        FilterConfigInterface filterConfig = getFilterConfig();
        Filter newFilter = filterConfig.createFilter();

        return newFilter;
    }

    /**
     * Gets the function name.
     *
     * @return the function name
     */
    public FilterConfigInterface getFilterConfig() {
        String filterNameString = (String)filterComboBox.getSelectedItem();

        FilterConfigInterface filterConfig = filterNameMap.get(filterNameString);
        return filterConfig;
    }
}
