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

import java.util.List;

import com.sldeditor.common.localisation.Localisation;

/**
 * Class that encapsulates the a list or group of ValueComboBoxData items.
 * 
 * @author Robert Ward (SCISYS)
 */
public class ValueComboBoxDataGroup {

    /** The group name. */
    private String groupName;

    /** The data list. */
    private List<ValueComboBoxData> dataList;

    /** The is sub menu. */
    boolean isSubMenu = false;

    /**
     * Instantiates a new value combo box data group.
     *
     * @param dataList the data list
     */
    public ValueComboBoxDataGroup(List<ValueComboBoxData> dataList) {
        super();
        this.groupName = Localisation.getString(ValueComboBoxDataGroup.class, "common.notSet");
        this.dataList = dataList;
        this.isSubMenu = false;
    }

    /**
     * Instantiates a new value combo box data group.
     *
     * @param groupName the group name
     * @param dataList the data list
     * @param isSubMenu the is sub menu
     */
    public ValueComboBoxDataGroup(String groupName, List<ValueComboBoxData> dataList,
            boolean isSubMenu) {
        super();
        this.groupName = groupName;
        this.dataList = dataList;
        this.isSubMenu = isSubMenu;
    }

    /**
     * Gets the group name.
     *
     * @return the group name
     */
    public String getGroupName() {
        return groupName;
    }

    /**
     * Gets the data list.
     *
     * @return the data list
     */
    public List<ValueComboBoxData> getDataList() {
        return dataList;
    }

    /**
     * Checks if is sub menu.
     *
     * @return true, if is sub menu
     */
    public boolean isSubMenu() {
        return isSubMenu;
    }

}
