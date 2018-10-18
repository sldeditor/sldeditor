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

package com.sldeditor.ui.widgets;

import com.sldeditor.common.console.ConsoleManager;
import com.sldeditor.common.vendoroption.VendorOptionManager;
import com.sldeditor.common.vendoroption.VendorOptionUpdateInterface;
import com.sldeditor.common.vendoroption.VersionData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

/**
 * Class that presents a combo box to the user, configured from xml files.
 *
 * @author Robert Ward (SCISYS)
 */
public class ValueComboBox extends JComboBox<ValueComboBoxData>
        implements VendorOptionUpdateInterface {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The value map. */
    private transient Map<String, ValueComboBoxData> valueMap = new HashMap<>();

    /** The value list. */
    private transient List<ValueComboBoxData> valueList = new ArrayList<>();

    /** The vendor option list allowed to be used. */
    private transient List<VersionData> vendorOptionVersionsList = new ArrayList<>();

    /** Instantiates a new value combo box. */
    @SuppressWarnings("unchecked")
    public ValueComboBox() {
        setRenderer(new ComboBoxRenderer());
    }

    /**
     * Initialise single.
     *
     * @param valueList the value list
     */
    public void initialiseSingle(List<ValueComboBoxData> valueList) {
        VendorOptionManager.getInstance().addVendorOptionListener(this);

        if (valueList != null) {
            valueMap.clear();
            this.valueList.clear();

            for (ValueComboBoxData data : valueList) {
                valueMap.put(data.getKey(), data);

                this.valueList.add(data);
            }

            update();
        }
    }

    /** Update. */
    private void update() {
        DefaultComboBoxModel<ValueComboBoxData> model = new DefaultComboBoxModel<>();
        if (valueList != null) {
            for (ValueComboBoxData data : valueList) {
                if (VendorOptionManager.getInstance()
                        .isAllowed(vendorOptionVersionsList, data.getVendorOption())) {
                    model.addElement(data);
                }
            }
        }
        setModel(model);
    }

    /**
     * Gets the selected value.
     *
     * @return the selected value
     */
    public ValueComboBoxData getSelectedValue() {
        Object selectedObj = getSelectedItem();
        if (selectedObj != null) {
            if (selectedObj instanceof ValueComboBoxData) {
                return valueMap.get(((ValueComboBoxData) selectedObj).getKey());
            }

            String selectedItem = selectedObj.toString();

            return getSelectedValue(selectedItem);
        }
        return null;
    }

    /**
     * Gets the selected value.
     *
     * @param selectedItemValue the selected item value
     * @return the selected value
     */
    public ValueComboBoxData getSelectedValue(String selectedItemValue) {
        if (selectedItemValue != null) {
            for (Entry<String, ValueComboBoxData> entry : valueMap.entrySet()) {
                ValueComboBoxData valueComboBoxData = entry.getValue();
                if (valueComboBoxData.getText().compareTo(selectedItemValue) == 0) {
                    return valueComboBoxData;
                }
            }
        }

        return null;
    }

    /**
     * Sets the select value using a key.
     *
     * @param key the new select value
     */
    public void setSelectValueKey(String key) {
        ValueComboBoxData obj = valueMap.get(key);

        if (obj == null) {
            ConsoleManager.getInstance().error(this, "Failed to find value for key : " + key);
        } else {
            this.setSelectedItem(obj);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.sldeditor.preferences.iface.PrefUpdateVendorOptionInterface#vendorOptionsUpdated(java.
     * util.List)
     */
    @Override
    public void vendorOptionsUpdated(List<VersionData> vendorOptionVersionsList) {
        this.vendorOptionVersionsList = vendorOptionVersionsList;

        update();
    }

    /**
     * Gets the first item.
     *
     * @return the first item
     */
    public ValueComboBoxData getFirstItem() {
        return this.getItemAt(0);
    }
}
