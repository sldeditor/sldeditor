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

package com.sldeditor.ui.detail.config;

import com.sldeditor.common.xml.ui.FieldIdEnum;

/**
 * The Class FieldConfigCommonData.
 *
 * @author Robert Ward (SCISYS)
 */
public class FieldConfigCommonData {

    /** The panel id. */
    private Class<?> panelId;

    /** The field id. */
    private FieldIdEnum id;

    /** The label. */
    private String label;

    /**
     * The value only flag:
     *
     * <ul>
     *   <li>true - do not display a value/attribute/expression drop down list
     *   <li>false- display a value/attribute/expression drop down list
     * </ul>
     */
    private boolean valueOnly = false;

    /** The raster symbol. */
    private boolean rasterSymbol = false;

    /** The optional field checkbox. */
    private boolean optionalField = false;

    /** The suppress undo events flag. */
    private boolean suppressUndoEvents = false;

    /**
     * Instantiates a new field config common data.
     *
     * @param panelId the panel id
     * @param id the id
     * @param label the label
     * @param valueOnly the value only
     * @param rasterSymbol the raster symbol
     * @param suppressUndoEvents the suppress undo events
     */
    public FieldConfigCommonData(
            Class<?> panelId,
            FieldIdEnum id,
            String label,
            boolean valueOnly,
            boolean rasterSymbol,
            boolean suppressUndoEvents) {
        super();
        this.panelId = panelId;
        this.id = id;
        this.label = label;
        this.valueOnly = valueOnly;
        this.rasterSymbol = rasterSymbol;
        this.suppressUndoEvents = suppressUndoEvents;
    }

    /**
     * Instantiates a new field config common data.
     *
     * @param panelId the panel id
     * @param id the id
     * @param label the label
     * @param valueOnly the value only
     * @param rasterSymbol the raster symbol
     * @param optionalCheckbox the optional checkbox
     * @param suppressUndoEvents the suppress undo events
     */
    public FieldConfigCommonData(
            Class<?> panelId,
            FieldIdEnum id,
            String label,
            boolean valueOnly,
            boolean rasterSymbol,
            boolean optionalCheckbox,
            boolean suppressUndoEvents) {
        super();
        this.panelId = panelId;
        this.id = id;
        this.label = label;
        this.valueOnly = valueOnly;
        this.rasterSymbol = rasterSymbol;
        this.optionalField = optionalCheckbox;
        this.suppressUndoEvents = suppressUndoEvents;
    }

    /**
     * Instantiates a new field config common data.
     *
     * @param panelId the panel id
     * @param id the id
     * @param label the label
     * @param valueOnly the value only
     * @param suppressUndoEvents the suppress undo events
     */
    public FieldConfigCommonData(
            Class<?> panelId,
            FieldIdEnum id,
            String label,
            boolean valueOnly,
            boolean suppressUndoEvents) {
        super();
        this.panelId = panelId;
        this.id = id;
        this.label = label;
        this.valueOnly = valueOnly;
        this.suppressUndoEvents = suppressUndoEvents;
    }

    /**
     * Instantiates a new field config common data.
     *
     * @param commonData the common data
     */
    public FieldConfigCommonData(FieldConfigCommonData commonData) {
        if (commonData != null) {
            this.panelId = commonData.panelId;
            this.id = commonData.id;
            this.label = commonData.label;
            this.valueOnly = commonData.valueOnly;
            this.rasterSymbol = commonData.rasterSymbol;
            this.optionalField = commonData.optionalField;
            this.suppressUndoEvents = commonData.suppressUndoEvents;
        }
    }

    /**
     * Gets the panel id.
     *
     * @return the panelId
     */
    public Class<?> getPanelId() {
        return panelId;
    }

    /**
     * Gets the field id.
     *
     * @return the field id
     */
    public FieldIdEnum getFieldId() {
        return id;
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
     * Checks if is value only.
     *
     * @return the valueOnly
     */
    public boolean isValueOnly() {
        return valueOnly;
    }

    /**
     * Checks if is raster symbol.
     *
     * @return the rasterSymbol
     */
    public boolean isRasterSymbol() {
        return rasterSymbol;
    }

    /**
     * Sets the value only.
     *
     * @param valueOnly the valueOnly to set
     */
    public void setValueOnly(boolean valueOnly) {
        this.valueOnly = valueOnly;
    }

    /**
     * Checks if is optional field.
     *
     * @return true, if is optional field
     */
    public boolean isOptionalField() {
        return optionalField;
    }

    /**
     * Checks if is suppress undo events flag is set.
     *
     * @return the suppressUndoEvents
     */
    public boolean isSuppressUndoEvents() {
        return suppressUndoEvents;
    }

    /**
     * Sets the suppress undo events.
     *
     * @param suppressUndoEvents the suppressUndoEvents to set
     */
    public void setSuppressUndoEvents(boolean suppressUndoEvents) {
        this.suppressUndoEvents = suppressUndoEvents;
    }
}
