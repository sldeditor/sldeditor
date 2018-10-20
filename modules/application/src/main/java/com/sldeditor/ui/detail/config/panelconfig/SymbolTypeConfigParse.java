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

import com.sldeditor.common.xml.ui.XMLFieldConfigEnumValue;
import com.sldeditor.common.xml.ui.XMLFieldConfigEnumValue.FieldList;
import com.sldeditor.common.xml.ui.XMLFieldConfigEnumValueField;
import com.sldeditor.common.xml.ui.XMLFieldConfigEnumValueGroup;
import com.sldeditor.common.xml.ui.XMLFieldConfigEnumValueItem;
import com.sldeditor.ui.detail.config.symboltype.SymbolTypeConfig;

/**
 * The Class SymbolTypeConfigParse.
 *
 * @author Robert Ward (SCISYS)
 */
public class SymbolTypeConfigParse {

    /** Default private constructor */
    private SymbolTypeConfigParse() {
        // Default private constructor
    }

    /**
     * Parses the symbol type configuration.
     *
     * @param localisationClass the localisation class
     * @param panelId the panel id
     * @param valueObj the value obj
     * @return the symbol type config
     */
    public static SymbolTypeConfig parseSymbolTypeConfig(
            Class<?> localisationClass, Class<?> panelId, XMLFieldConfigEnumValue valueObj) {
        SymbolTypeConfig config = new SymbolTypeConfig(panelId);

        String groupName = valueObj.getGroupName();
        boolean isSeparateGroup = valueObj.getSeparateGroup();

        if (groupName != null) {
            config.setGroupName(groupName);
        }
        config.setSeparateGroup(isSeparateGroup);

        for (XMLFieldConfigEnumValueItem itemObj : valueObj.getItem()) {
            config.addOption(
                    itemObj.getId(),
                    InstantiateFields.getLocalisedText(localisationClass, itemObj.getLabel()));
        }

        FieldList fieldList = valueObj.getFieldList();
        if (fieldList != null) {
            for (XMLFieldConfigEnumValueField field : fieldList.getField()) {
                config.addField(field.getId(), field.getEnabled());
            }

            for (XMLFieldConfigEnumValueGroup group : fieldList.getGroup()) {
                config.addGroup(group.getId(), group.getEnabled());
            }
        }
        return config;
    }
}
