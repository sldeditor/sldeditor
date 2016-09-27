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

/**
 * The Class FieldConfigCommonData.
 *
 * @author Robert Ward (SCISYS)
 */
public class FieldConfigCommonData {

    /** The panel id. */
    private Class<?> panelId;

    /** The id. */
    private FieldId id;

    /** The label. */
    private String label;

    /** The value only flag: <ul> <li>true - do not display a value/attribute/expression drop down list</li> <li>false- display a value/attribute/expression drop down list</li> </ul>. */
    private boolean valueOnly = false;;

    /** The raster symbol. */
    private boolean rasterSymbol = false;

    /**
     * Instantiates a new field config common data.
     *
     * @param panelId the panel id
     * @param id the id
     * @param label the label
     * @param valueOnly the value only
     * @param rasterSymbol the raster symbol
     */
    public FieldConfigCommonData(Class<?> panelId, FieldId id, String label, boolean valueOnly,
            boolean rasterSymbol) {
        super();
        this.panelId = panelId;
        this.id = id;
        this.label = label;
        this.valueOnly = valueOnly;
        this.rasterSymbol = rasterSymbol;
    }

    /**
     * Instantiates a new field config common data.
     *
     * @param panelId the panel id
     * @param id the id
     * @param label the label
     * @param valueOnly the value only
     */
    public FieldConfigCommonData(Class<?> panelId, FieldId id, String label, boolean valueOnly) {
        super();
        this.panelId = panelId;
        this.id = id;
        this.label = label;
        this.valueOnly = valueOnly;
    }

    /**
     * Instantiates a new field config common data.
     *
     * @param commonData the common data
     */
    public FieldConfigCommonData(FieldConfigCommonData commonData) {
        this.panelId = commonData.panelId;
        this.id = commonData.id;
        this.label = commonData.label;
        this.valueOnly = commonData.valueOnly;
        this.rasterSymbol = commonData.rasterSymbol;
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
    public FieldId getFieldId() {
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
}
