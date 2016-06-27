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

import com.sldeditor.ui.detail.config.FieldConfigBase;
import com.sldeditor.ui.detail.config.FieldId;

/**
 * The Class MultipleFieldHandler.
 *
 * @author Robert Ward (SCISYS)
 */
public class MultipleFieldHandler {

    /** The map. */
    private Map<FieldId, List<FieldConfigBase>> map = new HashMap<FieldId, List<FieldConfigBase>>();

    /**
     * Adds the field.
     *
     * @param field the field
     */
    public void addField(FieldConfigBase field) {
        List<FieldConfigBase> fieldList = map.get(field.getFieldId());

        if(fieldList == null)
        {
            fieldList = new ArrayList<FieldConfigBase>();
            map.put(field.getFieldId(), fieldList);
        }

        fieldList.add(field);
    }

    /**
     * Removes the field.
     *
     * @param field the field
     */
    public void removeField(FieldConfigBase field) {
        List<FieldConfigBase> fieldList = map.get(field.getFieldId());

        if(fieldList != null)
        {
            fieldList.remove(field);
        }
    }

    /**
     * Update button states.
     *
     * @param functionFieldList the function field list
     */
    public void updateButtonStates(List<FieldConfigBase> functionFieldList) {

        if((functionFieldList != null) && !functionFieldList.isEmpty())
        {
            FieldConfigBase config = functionFieldList.get(0);

            if(config.hasMultipleValues())
            {
                boolean enableAddButton = false;
                boolean enableRemoveButton = false;

                for(int index = 0; index < functionFieldList.size(); index ++)
                {
                    FieldConfigBase field = functionFieldList.get(index);

                    if(index == 0)
                    {
                        enableAddButton = true;
                        enableRemoveButton = false;
                    }
                    else if(index == functionFieldList.size() - 1)
                    {
                        enableAddButton = true;
                        enableRemoveButton = true;
                    }
                    else
                    {
                        enableAddButton = true;
                        enableRemoveButton = true;
                    }

                    field.setMultipleValueButtonState(enableAddButton, enableRemoveButton);
                }
            }
        }
    }
}
