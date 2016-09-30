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
package com.sldeditor.ui.detail.config.symboltype;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.ui.detail.FieldEnableState;

/**
 * The Class SymbolTypeConfig contains all the configuration for a symbol type.
 * <p>
 * A symbol type config consists of one or more option names which all have the same field enable state.
 * <p>
 * For example:
 * <ul>
 * <li>One SymbolTypeConfig could have an option name of None which if selected could disable all fields.</li>
 * <li>Another SymbolTypeConfigcould have several option names e.g. Solid, Cross, which if selected could enable all fields.<l/i>
 * </ul>
 * @author Robert Ward (SCISYS)
 */
public class SymbolTypeConfig
{

    /** The group name. */
    private String groupName = "Not set";

    /** Flag indicating whether this set of symbol types appears as a separate menu group in the symbol type drop down. */
    private boolean isSeparateGroup = false;

    /** The key order list. */
    private List<String> keyOrderList = new ArrayList<String>();

    /** The option map. */
    private Map<String, String> optionMap = new LinkedHashMap<String, String>();

    /** The field map. */
    private Map<FieldIdEnum, Boolean> fieldMap = new HashMap<FieldIdEnum, Boolean>();

    /** The logger. */
    private static Logger logger = Logger.getLogger(SymbolTypeConfig.class);

    /** The panel id. */
    private Class<?> panelId = null;

    /**
     * Constructor
     */
    public SymbolTypeConfig(Class<?> panelId)
    {
        this.panelId = panelId;
    }

    /**
     * Adds the option.
     *
     * @param key the key
     * @param title the title
     */
    public void addOption(String key, String title)
    {
        keyOrderList.add(key);
        optionMap.put(key, title);
    }

    /**
     * Gets the optimised field map, contains only the fields that should be enabled when the option is selected.
     *
     * @param fieldEnableState the field enable state
     * @param panelName the panel name
     */
    public void updateFieldState(FieldEnableState fieldEnableState, String panelName)
    {
        for(String menuOption : keyOrderList)
        {
            List<FieldIdEnum> fieldList = new ArrayList<FieldIdEnum>();

            for(FieldIdEnum fieldKey : fieldMap.keySet())
            {
                boolean value = fieldMap.get(fieldKey);

                if(value)
                {
                    fieldList.add(fieldKey);
                }
            }

            if(fieldEnableState != null)
            {
                fieldEnableState.add(panelName, menuOption, fieldList);
            }
        }
    }

    /**
     * Gets the field map, regardless of whether the field is enabled or disabled when the option is selected.
     *
     * @return the field map
     */
    public Map<Class<?>, Map<FieldIdEnum, Boolean> > getFieldMap()
    {
        Map<Class<?>, Map<FieldIdEnum, Boolean> > map = new HashMap<Class<?>, Map<FieldIdEnum, Boolean> >();

        Map<FieldIdEnum, Boolean> fieldList = new HashMap<FieldIdEnum, Boolean>();

        for(FieldIdEnum fieldKey : fieldMap.keySet())
        {
            boolean value = fieldMap.get(fieldKey);

            fieldList.put(fieldKey, value);
        }

        map.put(panelId, fieldList);

        return map;
    }

    /**
     * Gets the key order list.
     *
     * @return the key order list
     */
    public List<String> getKeyOrderList()
    {
        return keyOrderList;
    }

    /**
     * Gets the title.
     *
     * @param key the key
     * @return the title
     */
    public String getTitle(String key)
    {
        return optionMap.get(key);
    }

    /**
     * Adds the field.
     *
     * @param fieldId the field id
     * @param enabled the enabled flag
     */
    public void addField(FieldIdEnum fieldId, boolean enabled)
    {
        logger.debug(String.format("AddField %s %s", fieldId.toString(), enabled));
        fieldMap.put(fieldId, enabled);
    }

    /**
     * Gets the option map.
     *
     * @return the option map
     */
    public Map<String, String> getOptionMap() {
        return optionMap;
    }

    /**
     * Checks if is separate group.
     *
     * @return true, if is separate group
     */
    public boolean isSeparateGroup() {
        return isSeparateGroup;
    }

    /**
     * Sets the group name.
     *
     * @param groupName the new group name
     */
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    /**
     * Sets the separate group.
     *
     * @param isSeparateGroup the new separate group
     */
    public void setSeparateGroup(boolean isSeparateGroup) {
        this.isSeparateGroup = isSeparateGroup;
    }

    /**
     * Gets the group name.
     *
     * @return the group name
     */
    public String getGroupName() {
        return groupName;
    }
}
