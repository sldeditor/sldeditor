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

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.AbstractCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import com.sldeditor.common.vendoroption.VersionData;
import com.sldeditor.ui.iface.ValueComboBoxDataSelectedInterface;
import com.sldeditor.ui.menucombobox.MenuComboBox;
import com.sldeditor.ui.widgets.ValueComboBoxData;
import com.sldeditor.ui.widgets.ValueComboBoxDataGroup;

/**
 * Table cell editor that allows the editing of vendor option data.
 * 
 * @author Robert Ward (SCISYS)
 */
public class VersionCellEditor extends AbstractCellEditor
        implements TableCellEditor, ActionListener, ValueComboBoxDataSelectedInterface {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The version data. */
    private VersionData versionData;

    /** The list version data. */
    private List<VersionData> listVersionData;

    /** The model. */
    private VendorOptionTableModel model = null;

    /** The selected row index. */
    private int selectedRowIndex = -1;

    /**
     * Version data cell editor.
     *
     * @param model the model
     */
    public VersionCellEditor(VendorOptionTableModel model) {
        this.model = model;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.CellEditor#getCellEditorValue()
     */
    @Override
    public Object getCellEditorValue() {
        return this.versionData;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.TableCellEditor#getTableCellEditorComponent(javax.swing.JTable,
     * java.lang.Object, boolean, int, int)
     */
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected,
            int row, int column) {

        selectedRowIndex = row;

        if (value instanceof VersionData) {
            this.versionData = (VersionData) value;
        }

        listVersionData = model.getVendorOption(row);
        MenuComboBox comboVersionData = new MenuComboBox(this);

        List<ValueComboBoxDataGroup> dataSelectionList = VendorOptionMenuUtils
                .createMenu(listVersionData);

        comboVersionData.vendorOptionsUpdated(listVersionData);
        comboVersionData.initialiseMenu(dataSelectionList);

        // for (VersionData aVersionData : listVersionData) {
        // comboVersionData.addItem(aVersionData);
        // }
        //
        // comboVersionData.setSelectedItem(versionData);
        // comboVersionData.addActionListener(this);

        if (isSelected) {
            comboVersionData.setBackground(table.getSelectionBackground());
        } else {
            comboVersionData.setBackground(table.getSelectionForeground());
        }

        return comboVersionData;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        @SuppressWarnings("unchecked")
        JComboBox<VersionData> comboVersionData = (JComboBox<VersionData>) event.getSource();
        this.versionData = (VersionData) comboVersionData.getSelectedItem();

        model.setSelectedVersion(this.versionData, selectedRowIndex);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.sldeditor.ui.iface.ValueComboBoxDataSelectedInterface#optionSelected(com.sldeditor.ui.
     * widgets.ValueComboBoxData)
     */
    @Override
    public void optionSelected(ValueComboBoxData selectedData) {
        // TODO Auto-generated method stub

    }

}