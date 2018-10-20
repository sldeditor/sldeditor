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

package com.sldeditor.ui.detail.config.base;

import com.sldeditor.common.xml.ui.GroupIdEnum;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that encapsulates data used as part of a MultiOptionGroup.
 *
 * @author Robert Ward (SCISYS)
 */
public class OptionGroup {

    /** The id. */
    private GroupIdEnum id;

    /** The label. */
    private String label;

    /** The show title. */
    private boolean showTitle = true;

    /** The group list. */
    private List<GroupConfigInterface> groupList = new ArrayList<>();

    /**
     * Gets the id.
     *
     * @return the id
     */
    public GroupIdEnum getId() {
        return id;
    }

    /**
     * Sets the id.
     *
     * @param id the new id
     */
    public void setId(GroupIdEnum id) {
        this.id = id;
    }

    /**
     * Gets the label.
     *
     * @return the label
     */
    public String getLabel() {
        return label;
    }

    /**
     * Sets the label.
     *
     * @param label the new label
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * Checks if is show label.
     *
     * @return true, if is show label
     */
    public boolean isShowLabel() {
        return showTitle;
    }

    /**
     * Sets the show title.
     *
     * @param showTitle the new show title
     */
    public void setShowLabel(boolean showTitle) {
        this.showTitle = showTitle;
    }

    /**
     * Gets the group list.
     *
     * @return the group list
     */
    public List<GroupConfigInterface> getGroupList() {
        return groupList;
    }

    /**
     * Adds the group.
     *
     * @param group the group
     */
    public void addGroup(GroupConfig group) {
        this.groupList.add(group);
    }
}
