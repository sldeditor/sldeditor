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

import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.rendertransformation.types.RenderTransformValueFactory;
import com.sldeditor.rendertransformation.types.RenderTransformValueInterface;
import com.sldeditor.ui.detail.config.FieldConfigBase;
import com.sldeditor.ui.detail.config.FieldConfigCommonData;
import java.util.List;

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
     * @param valueTextLocalisation the value text localisation
     * @param nodeType the node type
     * @param enumList the enumerated list values
     * @return the field
     */
    public static FieldConfigBase getField(
            Class<?> classType,
            String valueTextLocalisation,
            Class<?> nodeType,
            List<String> enumList) {
        FieldConfigBase fieldConfig = null;

        RenderTransformValueInterface value = null;
        if (enumList != null) {
            value = RenderTransformValueFactory.getInstance().getEnum(String.class, enumList);
        } else {
            value = RenderTransformValueFactory.getInstance().getValue(nodeType);
        }

        if (value != null) {
            String valueText = Localisation.getString(classType, valueTextLocalisation);
            FieldIdEnum fieldId = FieldIdEnum.FUNCTION;

            // Suppress undo events
            FieldConfigCommonData commonData =
                    new FieldConfigCommonData(null, fieldId, valueText, true, true);

            fieldConfig = value.getField(commonData);
        } else {
            System.err.println("Unknown field type : " + nodeType);
        }

        return fieldConfig;
    }
}
