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

import com.sldeditor.common.undo.UndoActionInterface;
import com.sldeditor.common.undo.UndoEvent;
import com.sldeditor.common.undo.UndoInterface;
import com.sldeditor.common.undo.UndoManager;
import com.sldeditor.common.vendoroption.VendorOptionManager;
import com.sldeditor.ui.attribute.SubPanelUpdatedInterface;
import com.sldeditor.ui.iface.ValueComboBoxDataSelectedInterface;
import com.sldeditor.ui.menucombobox.MenuComboBox;
import com.sldeditor.ui.widgets.ValueComboBoxData;
import com.sldeditor.ui.widgets.ValueComboBoxDataGroup;
import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JPanel;
import org.opengis.filter.Filter;

/**
 * Panel to be able to edit FilterField objects.
 *
 * @author Robert Ward (SCISYS)
 */
public class FilterField extends JPanel
        implements UndoActionInterface, ValueComboBoxDataSelectedInterface {

    /** The Constant FILTER_PANEL. */
    private static final String FILTER_PANEL = "Filter";

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The menu combo box containing all the filter that can be selected. */
    private MenuComboBox filterComboBox = null;

    /** The filter name map. */
    private Map<String, FilterConfigInterface> filterNameMap =
            new LinkedHashMap<String, FilterConfigInterface>();

    /** The old value obj. */
    private Object oldValueObj = null;

    /** The filter name manager. */
    private FilterNameInterface filterNameMgr = null;

    /** The parent obj. */
    private SubPanelUpdatedInterface parentObj = null;

    /**
     * Gets the panel name.
     *
     * @return the panel name
     */
    public static String getPanelName() {
        return FILTER_PANEL;
    }

    /**
     * Instantiates a new data source attribute panel.
     *
     * @param parentObj the parent obj
     * @param functionNameMgr the function name mgr
     */
    public FilterField(SubPanelUpdatedInterface parentObj, FilterNameInterface functionNameMgr) {
        this.parentObj = parentObj;
        this.filterNameMgr = functionNameMgr;

        setLayout(new BorderLayout());

        if (filterComboBox == null) {
            filterComboBox = new MenuComboBox(this);

            VendorOptionManager.getInstance().addVendorOptionListener(filterComboBox);
            add(filterComboBox, BorderLayout.CENTER);
        }

        List<FilterConfigInterface> filterConfigList = filterNameMgr.getFilterConfigList();
        for (FilterConfigInterface filterConfig : filterConfigList) {
            String filterNameString = filterConfig.getFilterConfiguration().getFilterName();

            filterNameMap.put(filterNameString, filterConfig);
        }

        populateFunctionComboBox();
    }

    /** Populate function combo box. */
    private void populateFunctionComboBox() {
        if (filterComboBox != null) {

            List<ValueComboBoxDataGroup> dataSelectionList =
                    new ArrayList<ValueComboBoxDataGroup>();

            List<ValueComboBoxData> defaultDataList = new ArrayList<ValueComboBoxData>();
            defaultDataList.add(
                    new ValueComboBoxData(
                            null,
                            "",
                            VendorOptionManager.getInstance().getDefaultVendorOptionVersion()));
            dataSelectionList.add(new ValueComboBoxDataGroup(defaultDataList));

            Map<String, List<ValueComboBoxData>> map =
                    new HashMap<String, List<ValueComboBoxData>>();

            List<ValueComboBoxData> dataList = null;

            for (String name : filterNameMap.keySet()) {
                FilterConfigInterface filterConfig = filterNameMap.get(name);

                dataList = map.get(filterConfig.category());
                if (dataList == null) {
                    dataList = new ArrayList<ValueComboBoxData>();
                    map.put(filterConfig.category(), dataList);
                }
                String text = filterConfig.getFilterConfiguration().getFilterName();
                String key = text;

                dataList.add(
                        new ValueComboBoxData(
                                key,
                                text,
                                VendorOptionManager.getInstance().getDefaultVendorOptionVersion()));
            }

            for (String category : map.keySet()) {
                ValueComboBoxDataGroup value =
                        new ValueComboBoxDataGroup(category, map.get(category), true);
                dataSelectionList.add(value);
            }

            filterComboBox.initialiseMenu(dataSelectionList);
        }
    }

    /**
     * Gets the selected item.
     *
     * @return the selected item
     */
    public String getSelectedItem() {
        return (String) filterComboBox.getSelectedItem();
    }

    /**
     * Sets the panel enabled.
     *
     * @param enabled the new panel enabled
     */
    public void setPanelEnabled(boolean enabled) {
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

        if (filterConfig == null) {
            filterComboBox.setSelectedDataKey(null);
        } else {
            filterComboBox.setSelectedDataKey(
                    filterConfig.getFilterConfiguration().getFilterName());
        }
    }

    /**
     * Undo action.
     *
     * @param undoRedoObject the undo redo object
     */
    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.undo.UndoActionInterface#undoAction(com.sldeditor.undo.UndoInterface)
     */
    @Override
    public void undoAction(UndoInterface undoRedoObject) {
        String oldValueObj = (String) undoRedoObject.getOldValue();

        filterComboBox.setSelectedDataKey(oldValueObj);
    }

    /**
     * Redo action.
     *
     * @param undoRedoObject the undo redo object
     */
    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.undo.UndoActionInterface#redoAction(com.sldeditor.undo.UndoInterface)
     */
    @Override
    public void redoAction(UndoInterface undoRedoObject) {
        String newValueObj = (String) undoRedoObject.getNewValue();

        filterComboBox.setSelectedDataKey(newValueObj);
    }

    /**
     * Gets the filter.
     *
     * @return the filter
     */
    public Filter getFilter() {
        FilterConfigInterface filterConfig = getFilterConfig();
        Filter newFilter = null;

        if (filterConfig != null) {
            newFilter = filterConfig.createFilter();
        }
        return newFilter;
    }

    /**
     * Gets the function name.
     *
     * @return the function name
     */
    public FilterConfigInterface getFilterConfig() {
        String filterNameString = (String) filterComboBox.getSelectedItem();

        FilterConfigInterface filterConfig = filterNameMap.get(filterNameString);
        return filterConfig;
    }

    @Override
    public void optionSelected(ValueComboBoxData selectedData) {
        String newValueObj = (String) filterComboBox.getSelectedItem();

        UndoManager.getInstance()
                .addUndoEvent(new UndoEvent(this, "Function", oldValueObj, newValueObj));

        if (parentObj != null) {
            parentObj.updateSymbol();
        }
    }
}
