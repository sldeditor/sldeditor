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
import java.util.List;
import java.util.Map;

import org.geotools.styling.ColorMap;
import org.geotools.styling.FeatureTypeConstraint;
import org.geotools.styling.Font;
import org.geotools.styling.UserLayer;
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
    public void populateBooleanField(FieldIdEnum fieldId, Boolean value) {
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
     * @param fieldId the field id
     * @param value the value
     */
    public void populateComboBoxField(FieldIdEnum fieldId, String value) {
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
     * Populate colour field.
     *
     * @param fieldId the field id
     * @param colour the colour
     */
    public void populateColourField(FieldIdEnum fieldId, Expression colour)
    {
        if(fieldConfigManager == null)
        {
            return;
        }
        FieldConfigBase fieldConfig = fieldConfigManager.get(fieldId);
        if(fieldConfig != null)
        {
            fieldConfig.populateExpression(colour);
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
     * Populate feature type constraint field.
     *
     * @param fieldId the field id
     * @param ftcList the ftc list
     */
    public void populateFieldTypeConstraint(FieldIdEnum fieldId, List<FeatureTypeConstraint> ftcList) {
        if(fieldConfigManager == null)
        {
            return;
        }
        FieldConfigBase fieldConfig = fieldConfigManager.get(fieldId);
        if(fieldConfig != null)
        {
            fieldConfig.populateField(ftcList);
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
    public void populateTextField(FieldIdEnum fieldId, String value)
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
     * @param fieldId the field id
     * @param value the user layer
     */
    public void populateUserLayer(FieldIdEnum fieldId, UserLayer value)
    {
        if(fieldConfigManager == null)
        {
            return;
        }
        FieldConfigBase fieldConfig = fieldConfigManager.get(fieldId);
        if(fieldConfig != null)
        {
            ((FieldConfigValuePopulateInterface)fieldConfig).populateField(value);
        }
        else
        {
            ConsoleManager.getInstance().error(this, String.format("populateUserLayer - %s : %s", Localisation.getString(StandardPanel.class, "StandardPanel.unknownField") , fieldId));
        }
    }

    /**
     * Populate double field.
     *
     * @param fieldId the field id
     * @param value the value
     */
    public void populateDoubleField(FieldIdEnum fieldId, Double value) {
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
    public void populateIntegerField(FieldIdEnum fieldId, Integer value) {
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
    public void populateField(FieldIdEnum fieldId, Expression value)
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
     * Gets the expression from a field.
     *
     * @param fieldId the field id
     * @return the expression
     */
    public Expression getExpression(FieldIdEnum fieldId) {
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
     * Gets the boolean field value.
     *
     * @param fieldId the field id
     * @return the boolean
     */
    public boolean getBoolean(FieldIdEnum fieldId) {
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
     * Gets the integer field value.
     *
     * @param fieldId the field id
     * @return the integer
     */
    public int getInteger(FieldIdEnum fieldId) {
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
     * Gets the double field value.
     *
     * @param fieldId the field id
     * @return the double
     */
    public double getDouble(FieldIdEnum fieldId) {
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
     * Gets the text field value.
     *
     * @param fieldId the field id
     * @return the text
     */
    public String getText(FieldIdEnum fieldId) {
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
     * Gets the list of feature type constraints
     *
     * @param fieldId the field id
     * @return the list of feature type constraints
     */
    public List<FeatureTypeConstraint> getFeatureTypeConstraint(FieldIdEnum fieldId) {
        if(fieldConfigManager != null)
        {
            FieldConfigValuePopulateInterface fieldConfig = fieldConfigManager.get(fieldId);

            if(fieldConfig != null)
            {
                return fieldConfig.getFeatureTypeConstraint();
            }
        }
        return null;
    }

    /**
     * Gets the combo box field value.
     *
     * @param fieldId the field id
     * @return the combo box
     */
    public ValueComboBoxData getComboBox(FieldIdEnum fieldId) {
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
     * @param fieldId the field id
     * @return the colour map
     */
    public ColorMap getColourMap(FieldIdEnum fieldId) {
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
    public FieldConfigBase getFieldConfig(FieldIdEnum fieldId) {
        FieldConfigBase fieldConfig = null;
        if(fieldConfigManager != null)
        {
            fieldConfig = fieldConfigManager.get(fieldId);
        }
        return (FieldConfigBase) fieldConfig;
    }
}
