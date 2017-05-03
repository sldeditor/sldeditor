/*
 * SLD Editor - The Open Source Java SLD Editor
 *
 * Copyright (C) 2017, SCISYS UK Limited
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

import com.sldeditor.common.vendoroption.VersionData;
import com.sldeditor.ui.menucombobox.MenuComboBox;
import com.sldeditor.ui.widgets.ValueComboBoxData;
import com.sldeditor.ui.widgets.ValueComboBoxDataGroup;

/**
 * The Class VendorOptionMenuUtils.  Provides methods to populate vendor option
 * versions in a menu combo box.  Versions are grouped according to major/minor
 * version numbers.
 *
 * @author Robert Ward (SCISYS)
 */
public class VendorOptionMenuUtils {

    /** The data selection list. */
    private static List<ValueComboBoxDataGroup> dataSelectionList =
            new ArrayList<ValueComboBoxDataGroup>();

    /** The value map. */
    private static Map<String, ValueComboBoxData> valueMap = 
            new HashMap<String, ValueComboBoxData>();

    /**
     * Creates the menu.
     *
     * @param versionDataList the list version data
     * @return the list
     */
    public static List<ValueComboBoxDataGroup> createMenu(List<VersionData> versionDataList) {

        if (dataSelectionList.isEmpty()) {
            Map<String, List<ValueComboBoxData>> map = 
                    new HashMap<String, List<ValueComboBoxData>>();
            List<String> keyOrderList = new ArrayList<String>();

            if (versionDataList != null) {
                for (VersionData versionData : versionDataList) {

                    String key = getKey(versionData);
                    List<ValueComboBoxData> dataList = map.get(key);

                    if (dataList == null) {
                        dataList = new ArrayList<ValueComboBoxData>();
                        map.put(key, dataList);
                        keyOrderList.add(key);
                    }

                    ValueComboBoxData value = new ValueComboBoxData(versionData.getVersionString(),
                            versionData.getVersionString(), String.class);
                    dataList.add(value);
                    valueMap.put(versionData.getVersionString(), value);
                }

                // Add groups to menu combo
                for (String key : keyOrderList) {
                    List<ValueComboBoxData> dataList = map.get(key);
                    ValueComboBoxDataGroup group = new ValueComboBoxDataGroup(key + ".x", dataList,
                            (dataList.size() > 1));

                    dataSelectionList.add(group);
                }
            }
        }
        return dataSelectionList;
    }

    /**
     * Gets the key string of the version data.
     *
     * @param aVersionData the a version data
     * @return the key
     */
    private static String getKey(VersionData aVersionData) {
        if (aVersionData != null) {
            return String.format("%d.%d", aVersionData.getMajorNumber(),
                    aVersionData.getMinorNumber());
        }
        return "unknown";
    }

    /**
     * Sets the selected item.
     *
     * @param comboVersionData the combo version data
     * @param versionData the version data
     */
    public static void setSelected(MenuComboBox comboVersionData, VersionData versionData) {
        if (versionData != null) {
            ValueComboBoxData value = valueMap.get(versionData.getVersionString());

            if (comboVersionData != null) {
                comboVersionData.setSelectedData(value);
            }
        }
    }

}
