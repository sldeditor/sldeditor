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

package com.sldeditor.filter.v2.expression;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.measure.unit.Unit;

import org.geotools.geometry.jts.ReferencedEnvelope;

import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.ui.detail.config.FieldConfigBase;
import com.sldeditor.ui.detail.config.FieldConfigBoolean;
import com.sldeditor.ui.detail.config.FieldConfigBoundingBox;
import com.sldeditor.ui.detail.config.FieldConfigDate;
import com.sldeditor.ui.detail.config.FieldConfigDouble;
import com.sldeditor.ui.detail.config.FieldConfigEnum;
import com.sldeditor.ui.detail.config.FieldConfigGeometry;
import com.sldeditor.ui.detail.config.FieldConfigInteger;
import com.sldeditor.ui.detail.config.FieldConfigMapUnits;
import com.sldeditor.ui.detail.config.FieldConfigString;
import com.sldeditor.ui.detail.config.FieldId;
import com.sldeditor.ui.detail.config.symboltype.SymbolTypeConfig;
import com.vividsolutions.jts.geom.Geometry;

/**
 * The Class PanelField.
 *
 * @author Robert Ward (SCISYS)
 */
public class PanelField {

    /**
     * Gets the field.
     *
     * @param classType the class type
     * @param string the string
     * @param nodeType the node type
     * @param enumValueList the enum value list
     * @return the field
     */
    public static FieldConfigBase getField(Class<?> classType,
            String valueTextLocalisation, 
            Class<?> nodeType,
            List<String> enumValueList)
    {
        FieldConfigBase fieldConfig = null;

        String valueText = Localisation.getString(classType, valueTextLocalisation);
        FieldId fieldId = new FieldId(FieldIdEnum.FUNCTION);

        if(nodeType == Geometry.class)
        {
            fieldConfig = new FieldConfigGeometry(null, fieldId, valueText, true, null);
        }
        else if(nodeType == Date.class)
        {
            fieldConfig = new FieldConfigDate(null, fieldId, valueText, true);
        }
        else if(nodeType == ReferencedEnvelope.class)
        {
            fieldConfig = new FieldConfigBoundingBox(null, fieldId, "", true);
        }
        else if((nodeType == String.class) ||
                (nodeType == Object.class))
        {
            fieldConfig = new FieldConfigString(null, fieldId, valueText, true, null);
        }
        else if(nodeType == Boolean.class)
        {
            fieldConfig = new FieldConfigBoolean(null, fieldId, valueText, true);
        }
        else if(nodeType == Integer.class)
        {
            fieldConfig = new FieldConfigInteger(null, fieldId, valueText, true);
        }
        else if(nodeType == Double.class)
        {
            fieldConfig = new FieldConfigDouble(null, fieldId, valueText, true);
        }
        else if(nodeType == Number.class)
        {
            Class<?> filterType = TypeManager.getInstance().getDataType();
            if((filterType == Float.class) || (filterType == Double.class))
            {
                fieldConfig = new FieldConfigDouble(null, fieldId, valueText, true);
            }
            else
            {
                fieldConfig = new FieldConfigInteger(null, fieldId, valueText, true);
            }
        }
        else if(nodeType == Unit.class)
        {
            fieldConfig = new FieldConfigMapUnits(null, fieldId, null, true);
        }
        else if(nodeType == StringBuilder.class)
        {
            FieldConfigEnum fieldConfigEnum = new FieldConfigEnum(null, fieldId, valueText, true);

            List<SymbolTypeConfig> configList = new ArrayList<SymbolTypeConfig>();
            SymbolTypeConfig symbolTypeConfig = new SymbolTypeConfig(null);

            if(enumValueList != null)
            {
                for(String enumValue : enumValueList)
                {
                    symbolTypeConfig.addOption(enumValue, enumValue);
                }
            }
            configList.add(symbolTypeConfig);
            fieldConfigEnum.addConfig(configList);
            fieldConfig = fieldConfigEnum;
        }
        else
        {
            System.err.println("Unknown field type : " + nodeType);
        }

        return fieldConfig;
    }
}
