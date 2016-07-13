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

import java.util.HashMap;
import java.util.Map;

import org.geotools.styling.ColorMap;
import org.geotools.styling.Font;
import org.opengis.filter.expression.Expression;

import com.sldeditor.common.console.ConsoleManager;
import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.ui.detail.GraphicPanelFieldManager;
import com.sldeditor.ui.detail.StandardPanel;
import com.sldeditor.ui.widgets.ValueComboBoxData;

/**
 * The Class FieldConfigPopulation handles the population and extraction of data from fields</li>.
 *
 * @author Robert Ward (SCISYS)
 */
public class FieldConfigPopulation {

    /** The field config manager. */
    private GraphicPanelFieldManager fieldConfigManager = null;

    /**  The map to hold the original values of a text field. */
    private Map<FieldConfigValuePopulateInterface, String> valueMap = new HashMap<FieldConfigValuePopulateInterface, String>();

    /** The tree data updated flag. */
    private boolean treeDataUpdated = false;

    /**
     * Constructor.
     *
     * @param fieldConfigManager the field configuration manager
     */
    public FieldConfigPopulation(GraphicPanelFieldManager fieldConfigManager) {
        this.fieldConfigManager = fieldConfigManager;
    }

    /**
     * Populate boolean field.
     *
     * @param fieldId the field id
     * @param value the value
     */
    public void populateBooleanField(FieldId fieldId, Boolean value) {
        if(fieldConfigManager == null)
        {
            return;
        }

        FieldConfigBase fieldConfig = fieldConfigManager.get(fieldId);

        if(value != null)
        {
            ((FieldConfigValuePopulateInterface)fieldConfig).populateField(value);
        }
        else
        {
            fieldConfig.revertToDefaultValue();
        }
    }

    /**
     * Populate boolean field.
     *
     * @param fieldIdEnum the field id enum
     * @param value the value
     */
    public void populateBooleanField(FieldIdEnum fieldIdEnum, Boolean value) {
        populateBooleanField(new FieldId(fieldIdEnum), value);
    }

    /**
     * Populate combo box field.
     *
     * @param fieldId the field id
     * @param value the value
     */
    public void populateComboBoxField(FieldId fieldId, String value) {
        if(fieldConfigManager == null)
        {
            return;
        }
        FieldConfigBase fieldConfig = fieldConfigManager.get(fieldId);

        if(value != null)
        {
            ((FieldConfigValuePopulateInterface)fieldConfig).populateField(value);
        }
        else
        {
            fieldConfig.revertToDefaultValue();
        }
    }

    /**
     * Populate combo box field.
     *
     * @param fieldIdEnum the field id enum
     * @param value the value
     */
    public void populateComboBoxField(FieldIdEnum fieldIdEnum, String value) {
        populateComboBoxField(new FieldId(fieldIdEnum), value);
    }

    /**
     * Populate colour field.
     *
     * @param fieldId the field id
     * @param colour the colour
     * @param opacity the opacity
     */
    public void populateColourField(FieldId fieldId, Expression colour, Expression opacity)
    {
        if(fieldConfigManager == null)
        {
            return;
        }
        FieldConfigBase fieldConfig = fieldConfigManager.get(fieldId);
        if(fieldConfig != null)
        {
            fieldConfig.populateExpression(colour, opacity);
        }
    }

    /**
     * Populate colour map field.
     *
     * @param fieldId the field id
     * @param colourMap the colour map
     */
    public void populateColourMapField(FieldIdEnum fieldId, ColorMap colourMap) {
        if(fieldConfigManager == null)
        {
            return;
        }
        FieldConfigBase fieldConfig = fieldConfigManager.get(fieldId);
        if(fieldConfig != null)
        {
            fieldConfig.populateField(colourMap);
        }
    }

    /**
     * Populate font field.
     *
     * @param fieldId the field id
     * @param font the colour map
     */
    public void populateFontField(FieldIdEnum fieldId, Font font) {
        if(fieldConfigManager == null)
        {
            return;
        }
        FieldConfigBase fieldConfig = fieldConfigManager.get(fieldId);
        if(fieldConfig != null)
        {
            fieldConfig.populateField(font);
        }
    }

    /**
     * Populate text field.
     *
     * @param fieldId the field id
     * @param value the value
     */
    public void populateTextField(FieldId fieldId, String value)
    {
        if(fieldConfigManager == null)
        {
            return;
        }
        FieldConfigBase fieldConfig = fieldConfigManager.get(fieldId);
        if(fieldConfig != null)
        {
            ((FieldConfigValuePopulateInterface)fieldConfig).populateField(value);
            storeOriginalData(fieldConfig);
        }
        else
        {
            ConsoleManager.getInstance().error(this, String.format("populateTextField - %s : %s", Localisation.getString(StandardPanel.class, "StandardPanel.unknownField") , fieldId));
        }
    }

    /**
     * Populate text field.
     *
     * @param fieldIdEnum the field id enum
     * @param value the value
     */
    public void populateTextField(FieldIdEnum fieldIdEnum, String value)
    {
        FieldId fieldId = new FieldId(fieldIdEnum);
        populateTextField(fieldId, value);
    }

    /**
     * Populate double field.
     *
     * @param fieldId the field id
     * @param value the value
     */
    public void populateDoubleField(FieldId fieldId, Double value) {
        if(fieldConfigManager == null)
        {
            return;
        }
        FieldConfigBase fieldConfig = fieldConfigManager.get(fieldId);

        if(value != null)
        {
            ((FieldConfigValuePopulateInterface)fieldConfig).populateField(value);
        }
        else
        {
            fieldConfig.revertToDefaultValue();
        }
    }

    /**
     * Populate integer field.
     *
     * @param fieldId the field id
     * @param value the value
     */
    public void populateIntegerField(FieldId fieldId, Integer value) {
        if(fieldConfigManager == null)
        {
            return;
        }
        FieldConfigBase fieldConfig = fieldConfigManager.get(fieldId);

        if(value != null)
        {
            ((FieldConfigValuePopulateInterface)fieldConfig).populateField(value);
        }
        else
        {
            fieldConfig.revertToDefaultValue();
        }
    }

    /**
     * Populate field.
     *
     * @param fieldId the field
     * @param value the value
     */
    public void populateField(FieldId fieldId, Expression value)
    {
        if(fieldConfigManager == null)
        {
            return;
        }
        FieldConfigBase fieldConfig = fieldConfigManager.get(fieldId);
        if(fieldConfig != null)
        {
            fieldConfig.populate(value);
        }
        else
        {
            ConsoleManager.getInstance().error(this, String.format("populateField - %s : %s", Localisation.getString(StandardPanel.class, "StandardPanel.unknownField") , fieldId));
        }
    }

    /**
     * Populate field.
     *
     * @param fieldIdEnum the field id enum
     * @param value the value
     */
    public void populateField(FieldIdEnum fieldIdEnum, Expression value)
    {
        FieldId fieldId = new FieldId(fieldIdEnum);

        populateField(fieldId, value);
    }

    /**
     * Gets the expression from a field.
     *
     * @param fieldId the field id
     * @return the expression
     */
    public Expression getExpression(FieldId fieldId) {
        if(fieldConfigManager != null)
        {
            FieldConfigBase fieldConfig = fieldConfigManager.get(fieldId);
            if(fieldConfig != null)
            {
                return fieldConfig.getExpression();
            }
        }
        return null;
    }

    /**
     * Gets the expression.
     *
     * @param fieldIdEnum the field id enum
     * @return the expression
     */
    public Expression getExpression(FieldIdEnum fieldIdEnum) {
        FieldId fieldId = new FieldId(fieldIdEnum);

        return getExpression(fieldId);
    }

    /**
     * Gets the boolean field value.
     *
     * @param fieldId the field id
     * @return the boolean
     */
    public boolean getBoolean(FieldId fieldId) {
        if(fieldConfigManager != null)
        {
            FieldConfigValuePopulateInterface fieldConfig = fieldConfigManager.get(fieldId);

            if(fieldConfig != null)
            {
                return fieldConfig.getBooleanValue();
            }
        }
        return false;
    }

    /**
     * Gets the boolean.
     *
     * @param fieldIdEnum the field id enum
     * @return the boolean
     */
    public boolean getBoolean(FieldIdEnum fieldIdEnum) {
        return getBoolean(new FieldId(fieldIdEnum));
    }

    /**
     * Gets the integer field value.
     *
     * @param fieldId the field id
     * @return the integer
     */
    public int getInteger(FieldId fieldId) {
        if(fieldConfigManager != null)
        {
            FieldConfigValuePopulateInterface fieldConfig = fieldConfigManager.get(fieldId);

            if(fieldConfig != null)
            {
                return fieldConfig.getIntValue();
            }
        }
        return 0;
    }

    /**
     * Gets the integer.
     *
     * @param fieldIdEnum the field id enum
     * @return the integer
     */
    public int getInteger(FieldIdEnum fieldIdEnum) {
        return getInteger(new FieldId(fieldIdEnum));
    }

    /**
     * Gets the double field value.
     *
     * @param fieldId the field id
     * @return the double
     */
    public double getDouble(FieldId fieldId) {
        if(fieldConfigManager != null)
        {
            FieldConfigValuePopulateInterface fieldConfig = fieldConfigManager.get(fieldId);

            if(fieldConfig != null)
            {
                return fieldConfig.getDoubleValue();
            }
        }
        return 0.0;
    }

    /**
     * Gets the double field value.
     *
     * @param fieldIdEnum the field id enum
     * @return the double
     */
    public double getDouble(FieldIdEnum fieldIdEnum) {
        return getDouble(new FieldId(fieldIdEnum));
    }

    /**
     * Gets the text field value.
     *
     * @param fieldId the field id
     * @return the text
     */
    public String getText(FieldId fieldId) {
        if(fieldConfigManager != null)
        {
            FieldConfigValuePopulateInterface fieldConfig = fieldConfigManager.get(fieldId);

            if(fieldConfig != null)
            {
                compareOriginalData(fieldConfig);

                return fieldConfig.getStringValue();
            }
        }
        return "";
    }

    /**
     * Gets the text field value.
     *
     * @param fieldIdEnum the field id enum
     * @return the text
     */
    public String getText(FieldIdEnum fieldIdEnum) {
        return getText(new FieldId(fieldIdEnum));
    }

    /**
     * Gets the combo box field value.
     *
     * @param fieldId the field id
     * @return the combo box
     */
    public ValueComboBoxData getComboBox(FieldId fieldId) {
        if(fieldConfigManager != null)
        {
            FieldConfigValuePopulateInterface fieldConfig = fieldConfigManager.get(fieldId);

            if(fieldConfig != null)
            {
                return fieldConfig.getEnumValue();
            }
        }
        return null;
    }

    /**
     * Gets the colour map.
     *
     * @param fieldIdEnum the field id enum
     * @return the colour map
     */
    public ColorMap getColourMap(FieldIdEnum fieldIdEnum) {
        return getColourMap(new FieldId(fieldIdEnum));
    }

    /**
     * Gets the colour map.
     *
     * @param fieldId the field id
     * @return the colour map
     */
    public ColorMap getColourMap(FieldId fieldId) {
        if(fieldConfigManager != null)
        {
            FieldConfigValuePopulateInterface fieldConfig = fieldConfigManager.get(fieldId);

            if(fieldConfig != null)
            {
                return fieldConfig.getColourMap();
            }
        }
        return null;
    }

    /**
     * Gets the combo box.
     *
     * @param fieldIdEnum the field id enum
     * @return the combo box
     */
    public ValueComboBoxData getComboBox(FieldIdEnum fieldIdEnum) {
        return getComboBox(new FieldId(fieldIdEnum));
    }

    /**
     * Store original data.
     *
     * @param field the field configuration string
     */
    private void storeOriginalData(FieldConfigValuePopulateInterface field) {
        valueMap.put(field, field.getStringValue());
    }

    /**
     * Compare original data.
     *
     * @param field the field configuration string
     */
    private void compareOriginalData(FieldConfigValuePopulateInterface field) {
        if(field != null)
        {
            String nextText = field.getStringValue();

            String originalText = valueMap.get(field);

            if(originalText != null)
            {
                if(originalText.compareTo(nextText) != 0)
                {
                    treeDataUpdated = true;
                }
            }
        }
    }

    /**
     * Checks if is tree data updated.
     *
     * @return the treeDataUpdated
     */
    public boolean isTreeDataUpdated() {
        return treeDataUpdated;
    }

    /**
     * Reset tree data updated.
     */
    public void resetTreeDataUpdated() {
        this.treeDataUpdated = false;
    }

    /**
     * Gets the field config.
     *
     * @param fieldId the field id
     * @return the field config
     */
    public FieldConfigBase getFieldConfig(FieldId fieldId) {
        FieldConfigBase fieldConfig = null;
        if(fieldConfigManager != null)
        {
            fieldConfig = fieldConfigManager.get(fieldId);
        }
        return fieldConfig;
    }

}
