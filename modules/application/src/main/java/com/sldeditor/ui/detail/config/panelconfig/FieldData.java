/*
 * SLD Editor - The Open Source Java SLD Editor
 *
 * Copyright (C) 2018, SCISYS UK Limited
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

package com.sldeditor.ui.detail.config.panelconfig;

import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.common.xml.ui.XMLFieldConfigData;
import com.sldeditor.ui.detail.config.FieldConfigCommonData;

/**
 * The Class FieldData.
 *
 * @author Robert Ward (SCISYS)
 */
public class FieldData {

    /** The localisation class. */
    private Class<?> localisationClass;

    /** The panel id. */
    private Class<?> panelId;

    /** The xml field config. */
    private XMLFieldConfigData xmlFieldConfig;

    /** The common data. */
    private FieldConfigCommonData commonData;

    /**
     * Instantiates a new field data.
     *
     * @param localisationClass the localisation class
     * @param panelId the panel id
     * @param groupConfig the group config
     * @param xmlFieldConfig the xml field config
     * @param commonData the common data
     */
    public FieldData(
            Class<?> localisationClass,
            Class<?> panelId,
            XMLFieldConfigData xmlFieldConfig,
            FieldConfigCommonData commonData) {
        super();
        this.localisationClass = localisationClass;
        this.panelId = panelId;
        this.xmlFieldConfig = xmlFieldConfig;
        this.commonData = commonData;
    }

    /**
     * Gets the localisation class.
     *
     * @return the localisationClass
     */
    public Class<?> getLocalisationClass() {
        return localisationClass;
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
     * Gets the xml field config.
     *
     * @return the xmlFieldConfig
     */
    public XMLFieldConfigData getXmlFieldConfig() {
        return xmlFieldConfig;
    }

    /**
     * Gets the common data.
     *
     * @return the commonData
     */
    public FieldConfigCommonData getCommonData() {
        return commonData;
    }

    /**
     * Gets the default value.
     *
     * @return the default value
     */
    public String getDefaultValue() {
        return xmlFieldConfig.getDefault();
    }

    /**
     * Gets the id.
     *
     * @return the id
     */
    public FieldIdEnum getId() {
        return xmlFieldConfig.getId();
    }
}
