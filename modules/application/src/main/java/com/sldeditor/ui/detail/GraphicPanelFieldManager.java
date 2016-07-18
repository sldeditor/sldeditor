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
package com.sldeditor.ui.detail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.common.xml.ui.GroupIdEnum;
import com.sldeditor.ui.detail.config.FieldConfigBase;
import com.sldeditor.ui.detail.config.FieldId;
import com.sldeditor.ui.detail.config.base.GroupConfig;
import com.sldeditor.ui.detail.config.base.GroupConfigInterface;
import com.sldeditor.ui.detail.config.base.MultiOptionGroup;

/**
 * Class that manages all the field information so that if other panels or the test class require
 * access to specific field data they can get it.
 * 
 * @author Robert Ward (SCISYS)
 */
public class GraphicPanelFieldManager {

    /** The field configuration map. */
    private Map<Class<?>, Map<FieldId, FieldConfigBase> > fieldConfigMap = new HashMap<Class<?>, Map<FieldId, FieldConfigBase> >();

    /** The map of groups. */
    private Map<Class<?>, Map<GroupIdEnum, GroupConfigInterface> > groupMap = new HashMap<Class<?>, Map<GroupIdEnum, GroupConfigInterface>>();

    /** The panel id. */
    private Class<?> panelId = null;

    /**
     * Instantiates a new graphic panel field manager.
     *
     * @param panelId the panel id
     */
    public GraphicPanelFieldManager(Class<?> panelId)
    {
        this.panelId = panelId;

        Map<FieldId, FieldConfigBase> fieldMap = new HashMap<FieldId, FieldConfigBase>();

        fieldConfigMap.put(this.panelId, fieldMap);
    }

    /**
     * Adds the field configuration to the internal structure.
     *
     * @param fieldConfig the field configuration
     */
    public void addField(FieldConfigBase fieldConfig) {
        if(fieldConfig != null)
        {
            add(fieldConfig.getFieldId(), fieldConfig);
        }
    }

    /**
     * Adds the field data manager to the internal structure.
     *
     * @param fieldId the field
     * @param fieldConfig the field configuration
     */
    public void add(FieldId fieldId, FieldConfigBase fieldConfig)
    {
        Map<FieldId, FieldConfigBase> panelMap = fieldConfigMap.get(panelId);

        if(panelMap == null)
        {
            panelMap = new HashMap<FieldId, FieldConfigBase>();
        }
        panelMap.put(fieldId, fieldConfig);
    }

    /**
     * Gets the field by specifying field.
     *
     * @param field the field
     * @return the field data manager
     */
    public FieldConfigBase get(FieldId field)
    {
        Map<FieldId, FieldConfigBase> panelMap = fieldConfigMap.get(panelId);

        if(panelMap != null)
        {
            return panelMap.get(field);
        }
        return null;
    }


    /**
     * Gets the.
     *
     * @param field the field
     * @return the field config base
     */
    public FieldConfigBase get(FieldIdEnum field)
    {
        return get(new FieldId(field));
    }

    /**
     * Gets the fields for the given class type, if class type is null then all are returned
     *
     * @param fieldType the field type
     * @return the fields
     */
    public List<FieldConfigBase> getFields(Class<?> fieldType)
    {
        List<FieldConfigBase> fieldList = new ArrayList<FieldConfigBase>();
        Map<FieldId, FieldConfigBase> panelMap = fieldConfigMap.get(panelId);

        if(panelMap != null)
        {
            for(FieldConfigBase field : panelMap.values())
            {
                if((fieldType == null) || field.getClass() == fieldType)
                {
                    fieldList.add(field);
                }
            }
        }

        return fieldList;
    }

    /**
     * Gets the field enum given sub panel, panel id and field.
     *
     * @param panelId the panel id
     * @param fieldConfigToFind the field configuration to find
     * @return the field enum
     */
    public FieldId getFieldEnum(Class<?> panelId, FieldConfigBase fieldConfigToFind)
    {
        Map<FieldId, FieldConfigBase> panelMap = fieldConfigMap.get(panelId);

        if(panelMap != null)
        {
            for(FieldId key : panelMap.keySet())
            {
                FieldConfigBase fieldConfig = panelMap.get(key);

                if(fieldConfig == fieldConfigToFind)
                {
                    return key;
                }
            }
        }
        return FieldId.getUnknownValue();
    }

    /**
     * Adds an existing GraphicPanelFieldManager to this one.
     *
     * @param fieldConfigManager the field config manager
     */
    public void add(GraphicPanelFieldManager fieldConfigManager)
    {
        if(fieldConfigManager != null)
        {
            Map<FieldId, FieldConfigBase> componentMapToAdd = fieldConfigManager.fieldConfigMap.get(fieldConfigManager.panelId);
            Map<FieldId, FieldConfigBase> componentMap = this.fieldConfigMap.get(fieldConfigManager.panelId);

            if(componentMap == null)
            {
                Map<FieldId, FieldConfigBase> value = fieldConfigManager.fieldConfigMap.get(fieldConfigManager.panelId);
                this.fieldConfigMap.put(fieldConfigManager.panelId, value);
            }
            else
            {
                for(FieldId fieldId : componentMapToAdd.keySet())
                {
                    FieldConfigBase dataToAdd = componentMapToAdd.get(fieldId);

                    componentMap.put(fieldId, dataToAdd);
                }
            }

            // Add groups
            Map<GroupIdEnum, GroupConfigInterface> groupMapToAdd = fieldConfigManager.groupMap.get(fieldConfigManager.panelId);
            Map<GroupIdEnum, GroupConfigInterface> thisGroupMap = this.groupMap.get(fieldConfigManager.panelId);

            if(thisGroupMap == null)
            {
                Map<GroupIdEnum, GroupConfigInterface> value = fieldConfigManager.groupMap.get(fieldConfigManager.panelId);
                this.groupMap.put(fieldConfigManager.panelId, value);
            }
            else
            {
                for(GroupIdEnum groupId : groupMapToAdd.keySet())
                {
                    GroupConfigInterface dataToAdd = groupMapToAdd.get(groupId);

                    thisGroupMap.put(groupId, dataToAdd);
                }
            }
        }
    }

    /**
     * Gets the component id.
     *
     * @return the component id
     */
    public Class<?> getComponentId()
    {
        return panelId;
    }

    /**
     * Gets the data.
     *
     * @param requestedPanelId the requested panel id
     * @param requestedField the requested field
     * @return the data
     */
    public FieldConfigBase getData(Class<?> requestedPanelId, 
            FieldId requestedField)
    {
        FieldConfigBase retVal = null;

        Map<FieldId, FieldConfigBase> panelMap = fieldConfigMap.get(requestedPanelId);

        if(panelMap != null)
        {
            retVal = panelMap.get(requestedField);
        }

        return retVal;
    }

    /**
     * Adds the group.
     *
     * @param group the group
     */
    public void addGroup(GroupConfig group) {
        Map<GroupIdEnum, GroupConfigInterface> panelMap = groupMap.get(panelId);

        if(panelMap == null)
        {
            panelMap = new HashMap<GroupIdEnum, GroupConfigInterface>();
            groupMap.put(panelId, panelMap);
        }

        if(group != null)
        {
            panelMap.put(group.getId(), group);
        }
    }

    /**
     * Adds the multi option group.
     *
     * @param multiOption the multi option
     */
    public void addMultiOptionGroup(MultiOptionGroup multiOption) {
        Map<GroupIdEnum, GroupConfigInterface> panelMap = groupMap.get(panelId);

        if(panelMap == null)
        {
            panelMap = new HashMap<GroupIdEnum, GroupConfigInterface>();
            groupMap.put(panelId, panelMap);
        }

        if(multiOption != null)
        {
            panelMap.put(multiOption.getId(), multiOption);
        }
    }

    /**
     * Gets the multi option group for the given group id.
     *
     * @param requestedPanelId the requested panel id
     * @param groupId the group id
     * @return the multi option group
     */
    public MultiOptionGroup getMultiOptionGroup(Class<?> requestedPanelId, GroupIdEnum groupId)
    {
        Map<GroupIdEnum, GroupConfigInterface> panelMap = groupMap.get(requestedPanelId);

        if(panelMap != null)
        {
            return (MultiOptionGroup) panelMap.get(groupId);
        }
        return null;
    }

    /**
     * Gets the group for the given group id.
     *
     * @param requestedPanelId the requested panel id
     * @param groupId the group id
     * @return the option group
     */
    public GroupConfig getGroup(Class<?> requestedPanelId, GroupIdEnum groupId)
    {
        Map<GroupIdEnum, GroupConfigInterface> panelMap = groupMap.get(requestedPanelId);

        if(panelMap != null)
        {
            return (GroupConfig) panelMap.get(groupId);
        }

        return null;
    }

    /**
     * Removes the field.
     *
     * @param fieldConfig the field config
     */
    public void removeField(FieldConfigBase fieldConfig) {
        if(fieldConfig != null)
        {
            Map<FieldId, FieldConfigBase> panelMap = fieldConfigMap.get(panelId);

            if(panelMap != null)
            {
                panelMap.remove(fieldConfig.getFieldId());
            }
        }
    }
}
