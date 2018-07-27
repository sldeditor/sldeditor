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

package com.sldeditor.ui.detail.vendor;

import com.sldeditor.common.console.ConsoleManager;
import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.ui.detail.StandardPanel;
import com.sldeditor.ui.detail.config.FieldConfigBase;
import com.sldeditor.ui.detail.config.FieldConfigBoolean;
import com.sldeditor.ui.detail.config.FieldConfigDouble;
import com.sldeditor.ui.detail.config.FieldConfigEnum;
import com.sldeditor.ui.detail.config.FieldConfigInteger;
import com.sldeditor.ui.widgets.ValueComboBoxData;
import java.util.Map;
import org.geotools.styling.TextSymbolizer;
import org.geotools.styling.TextSymbolizer.PolygonAlignOptions;

/**
 * The Class VOPopulation, populates extracts data from vendor option panels.
 *
 * @author Robert Ward (SCISYS)
 */
public class VOPopulation extends StandardPanel {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /**
     * Instantiates a new VO population.
     *
     * @param panelId the panel id
     */
    protected VOPopulation(Class<?> panelId) {
        super(panelId);
    }

    /**
     * Internal populate.
     *
     * @param options the options
     * @param field the field
     * @param key the key
     */
    protected void internalPopulate(Map<String, String> options, FieldIdEnum field, String key) {
        FieldConfigBase fieldConfig = fieldConfigManager.get(field);

        if (fieldConfig != null) {
            if (fieldConfig instanceof FieldConfigBoolean) {
                internal_populateBooleanField(options, field, key);
            } else if (fieldConfig instanceof FieldConfigInteger) {
                internal_populateIntegerField(options, field, key);
            } else if (fieldConfig instanceof FieldConfigDouble) {
                internal_populateDoubleField(options, field, key);
            } else if (fieldConfig instanceof FieldConfigEnum) {
                internal_populateEnumField(options, field, key);
            } else {
                ConsoleManager.getInstance()
                        .error(
                                this,
                                "Unsupported field type : "
                                        + field
                                        + " "
                                        + fieldConfig.getClass().getName());
            }
        }
    }

    /**
     * Internal populate double field.
     *
     * @param options the options
     * @param fieldId the field id
     * @param key the key
     */
    protected void internal_populateDoubleField(
            Map<String, String> options, FieldIdEnum fieldId, String key) {
        if ((options != null) && options.containsKey(key)) {
            String storedValue = options.get(key);
            Double value = Double.valueOf(storedValue);
            fieldConfigVisitor.populateDoubleField(fieldId, value);
        } else {
            setDefaultValue(fieldId);
        }
    }

    /**
     * Internal_populate boolean field.
     *
     * @param options the options
     * @param fieldId the field id
     * @param key the key
     */
    protected void internal_populateBooleanField(
            Map<String, String> options, FieldIdEnum fieldId, String key) {
        if ((options != null) && options.containsKey(key)) {
            String storedValue = options.get(key);
            Boolean value = Boolean.valueOf(storedValue);
            fieldConfigVisitor.populateBooleanField(fieldId, value);
        } else {
            setDefaultValue(fieldId);
        }
    }

    /**
     * Internal_populate integer field.
     *
     * @param options the options
     * @param fieldId the field id
     * @param key the key
     */
    protected void internal_populateIntegerField(
            Map<String, String> options, FieldIdEnum fieldId, String key) {
        if ((options != null) && options.containsKey(key)) {
            String storedValue = options.get(key);
            Integer value = Double.valueOf(storedValue).intValue();
            fieldConfigVisitor.populateIntegerField(fieldId, value);
        } else {
            setDefaultValue(fieldId);
        }
    }

    /**
     * Internal_populate enum field.
     *
     * @param options the options
     * @param fieldId the field id
     * @param key the key
     */
    protected void internal_populateEnumField(
            Map<String, String> options, FieldIdEnum fieldId, String key) {
        if ((options != null) && options.containsKey(key)) {
            String value = options.get(key);
            fieldConfigVisitor.populateComboBoxField(fieldId, value);
        } else {
            setDefaultValue(fieldId);
        }
    }

    /**
     * Internal update symbol.
     *
     * @param options the options
     * @param field the field
     * @param key the key
     */
    protected void internalUpdateSymbol(
            Map<String, String> options, FieldIdEnum field, String key) {
        FieldConfigBase fieldConfig = fieldConfigManager.get(field);

        if (fieldConfig instanceof FieldConfigBoolean) {
            internal_updateSymbolBooleanField(options, field, key);
        } else if (fieldConfig instanceof FieldConfigInteger) {
            internal_updateSymbolIntegerField(options, field, key);
        } else if (fieldConfig instanceof FieldConfigDouble) {
            internal_updateSymbolDoubleField(options, field, key);
        } else if (fieldConfig instanceof FieldConfigEnum) {
            internal_updateSymbolEnumField(options, field, key);
        } else {
            ConsoleManager.getInstance()
                    .error(
                            this,
                            "Unsupported field type : "
                                    + field
                                    + " "
                                    + fieldConfig.getClass().getName());
        }
    }

    /**
     * Internal_update symbol enum field.
     *
     * @param options the options
     * @param field the field
     * @param key the key
     */
    protected void internal_updateSymbolEnumField(
            Map<String, String> options, FieldIdEnum field, String key) {
        ValueComboBoxData value = fieldConfigVisitor.getComboBox(field);

        Object object = getDefaultFieldValue(field);
        String defaultValue = null;

        if (object instanceof TextSymbolizer.PolygonAlignOptions) {
            TextSymbolizer.PolygonAlignOptions option = (PolygonAlignOptions) object;
            defaultValue = option.toString();
        } else {
            defaultValue = (String) object;
        }

        if (defaultValue == null) {
            ConsoleManager.getInstance().error(this, "Failed to find default for field : " + field);
        } else if ((value.getKey().compareToIgnoreCase(defaultValue) != 0) || includeValue(field)) {
            options.put(key, value.getKey());
        }
    }

    /**
     * Internal_update symbol double field.
     *
     * @param options the options
     * @param field the field
     * @param key the key
     */
    protected void internal_updateSymbolDoubleField(
            Map<String, String> options, FieldIdEnum field, String key) {
        double value = fieldConfigVisitor.getDouble(field);

        Double defaultValue = (Double) getDefaultFieldValue(field);

        if (defaultValue == null) {
            ConsoleManager.getInstance().error(this, "Failed to find default for field : " + field);
        } else if ((value != defaultValue) || (includeValue(field))) {
            options.put(key, String.valueOf(value));
        }
    }

    /**
     * Internal_update symbol integer field.
     *
     * @param options the options
     * @param field the field
     * @param key the key
     */
    protected void internal_updateSymbolIntegerField(
            Map<String, String> options, FieldIdEnum field, String key) {
        int value = fieldConfigVisitor.getInteger(field);

        Integer defaultValue = (Integer) getDefaultFieldValue(field);

        if (defaultValue == null) {
            ConsoleManager.getInstance().error(this, "Failed to find default for field : " + field);
        } else if ((value != defaultValue) || (includeValue(field))) {
            options.put(key, String.valueOf(value));
        }
    }

    /**
     * Internal_update symbol boolean field.
     *
     * @param options the options
     * @param field the field
     * @param key the key
     */
    protected void internal_updateSymbolBooleanField(
            Map<String, String> options, FieldIdEnum field, String key) {
        boolean value = fieldConfigVisitor.getBoolean(field);

        Boolean defaultValue = (Boolean) getDefaultFieldValue(field);

        if (defaultValue == null) {
            ConsoleManager.getInstance().error(this, "Failed to find default for field : " + field);
        } else if ((value != defaultValue) || (includeValue(field))) {
            options.put(key, String.valueOf(value));
        }
    }

    /**
     * Include value.
     *
     * @param field the field
     * @return true, if successful
     */
    protected boolean includeValue(FieldIdEnum field) {
        return true;
    }
}
