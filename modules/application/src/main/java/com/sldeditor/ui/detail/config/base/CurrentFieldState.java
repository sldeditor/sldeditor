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

/**
 * The Class CurrentFieldState.
 *
 * @author Robert Ward (SCISYS)
 */
public class CurrentFieldState {

    /** The group enabled flag. */
    private boolean groupEnabledFlag = true;

    /** The group selected flag. */
    private boolean groupSelectedFlag = true;

    /** The field enabled flag. */
    private boolean fieldEnabledFlag = true;

    /**
     * Checks if is group enabled flag.
     *
     * @return the groupEnabledFlag
     */
    public boolean isGroupEnabled() {
        return groupEnabledFlag;
    }

    /**
     * Sets the group enabled flag.
     *
     * @param groupEnabledFlag the groupEnabledFlag to set
     */
    public void setGroupEnabled(boolean groupEnabledFlag) {
        this.groupEnabledFlag = groupEnabledFlag;
    }

    /**
     * Checks if is group selected flag.
     *
     * @return the groupSelectedFlag
     */
    public boolean isGroupSelected() {
        return groupSelectedFlag;
    }

    /**
     * Sets the group selected flag.
     *
     * @param groupSelectedFlag the groupSelectedFlag to set
     */
    public void setGroupSelected(boolean groupSelectedFlag) {
        this.groupSelectedFlag = groupSelectedFlag;
    }

    /**
     * Checks if is field enabled flag.
     *
     * @return the fieldEnabledFlag
     */
    public boolean isFieldEnabled() {
        return fieldEnabledFlag;
    }

    /**
     * Sets the field enabled flag.
     *
     * @param fieldEnabledFlag the fieldEnabledFlag to set
     */
    public void setFieldEnabled(boolean fieldEnabledFlag) {
        this.fieldEnabledFlag = fieldEnabledFlag;
    }

    /**
     * Gets the field enabled state.
     *
     * @return the field enabled state
     */
    public boolean getFieldEnabledState() {
        boolean enabled = false;

        if (groupEnabledFlag) {
            enabled = groupSelectedFlag && fieldEnabledFlag;
        }
        return enabled;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "CurrentFieldState [groupEnabledFlag="
                + groupEnabledFlag
                + ", groupSelectedFlag="
                + groupSelectedFlag
                + ", fieldEnabledFlag="
                + fieldEnabledFlag
                + "]";
    }
}
