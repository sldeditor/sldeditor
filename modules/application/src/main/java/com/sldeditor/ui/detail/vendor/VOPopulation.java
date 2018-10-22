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
import java.util.EnumMap;
import java.util.Map;
import org.geotools.styling.TextSymbolizer;
import org.geotools.styling.TextSymbolizer.PolygonAlignOptions;

/**
 * The Class VOPopulation, populates extracts data from vendor option panels.
 *
 * @author Robert Ward (SCISYS)
 */
public class VOPopulation extends StandardPanel {

    /** The Constant FAILED_TO_FIND_DEFAULT_FOR_FIELD. */
    private static final String FAILED_TO_FIND_DEFAULT_FOR_FIELD =
            "Failed to find default for field : ";

    /** The Constant UNSUPPORTED_FIELD_TYPE. */
    private static final String UNSUPPORTED_FIELD_TYPE = "Unsupported field type : ";

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The override map. */
    private transient Map<FieldIdEnum, DefaultOverride> overrideMap =
            new EnumMap<>(FieldIdEnum.class);

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
                internalPopulateBooleanField(options, field, key);
            } else if (fieldConfig instanceof FieldConfigInteger) {
                internalPopulateIntegerField(options, field, key);
            } else if (fieldConfig instanceof FieldConfigDouble) {
                internalPopulateDoubleField(options, field, key);
            } else if (fieldConfig instanceof FieldConfigEnum) {
                internalPopulateEnumField(options, field, key);
            } else {
                ConsoleManager.getInstance()
                        .error(
                                this,
                                UNSUPPORTED_FIELD_TYPE
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
    protected void internalPopulateDoubleField(
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
    protected void internalPopulateBooleanField(
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
    protected void internalPopulateIntegerField(
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
    protected void internalPopulateEnumField(
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
            internalUpdateSymbolBooleanField(options, field, key);
        } else if (fieldConfig instanceof FieldConfigInteger) {
            internalUpdateSymbolIntegerField(options, field, key);
        } else if (fieldConfig instanceof FieldConfigDouble) {
            internalUpdateSymbolDoubleField(options, field, key);
        } else if (fieldConfig instanceof FieldConfigEnum) {
            internalUpdateSymbolEnumField(options, field, key);
        } else {
            ConsoleManager.getInstance()
                    .error(
                            this,
                            UNSUPPORTED_FIELD_TYPE
                                    + field
                                    + " "
                                    + fieldConfig.getClass().getName());
        }
    }

    /**
     * Internal update symbol enum field.
     *
     * @param options the options
     * @param field the field
     * @param key the key
     */
    protected void internalUpdateSymbolEnumField(
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
            ConsoleManager.getInstance().error(this, FAILED_TO_FIND_DEFAULT_FOR_FIELD + field);
        } else if ((value.getKey().compareToIgnoreCase(defaultValue) != 0) || includeValue(field)) {
            options.put(key, value.getKey());
        }
    }

    /**
     * Internal update symbol double field.
     *
     * @param options the options
     * @param field the field
     * @param key the key
     */
    protected void internalUpdateSymbolDoubleField(
            Map<String, String> options, FieldIdEnum field, String key) {
        double value = fieldConfigVisitor.getDouble(field);

        Double defaultValue = (Double) getDefaultFieldValue(field);

        if (defaultValue == null) {
            ConsoleManager.getInstance().error(this, FAILED_TO_FIND_DEFAULT_FOR_FIELD + field);
        } else if ((value != defaultValue) || (includeValue(field))) {
            options.put(key, String.valueOf(value));
        }
    }

    /**
     * Internal update symbol integer field.
     *
     * @param options the options
     * @param field the field
     * @param key the key
     */
    protected void internalUpdateSymbolIntegerField(
            Map<String, String> options, FieldIdEnum field, String key) {
        int value = fieldConfigVisitor.getInteger(field);

        Integer defaultValue = (Integer) getDefaultFieldValue(field);

        if (defaultValue == null) {
            ConsoleManager.getInstance().error(this, FAILED_TO_FIND_DEFAULT_FOR_FIELD + field);
        } else if ((value != defaultValue) || (includeValue(field))) {
            options.put(key, String.valueOf(value));
        }
    }

    /**
     * Internal update symbol boolean field.
     *
     * @param options the options
     * @param field the field
     * @param key the key
     */
    protected void internalUpdateSymbolBooleanField(
            Map<String, String> options, FieldIdEnum field, String key) {
        boolean value = fieldConfigVisitor.getBoolean(field);

        Boolean defaultValue = (Boolean) getDefaultFieldValue(field);

        if (defaultValue == null) {
            ConsoleManager.getInstance().error(this, FAILED_TO_FIND_DEFAULT_FOR_FIELD + field);
        } else if ((value != defaultValue) || (includeValue(field))) {
            options.put(key, String.valueOf(value));
        }
    }

    /**
     * Adds an override.
     *
     * @param fieldId the field id
     * @param override the override
     */
    protected void addOverride(FieldIdEnum fieldId, DefaultOverride override) {
        overrideMap.put(fieldId, override);
    }

    /**
     * Find out whether to include value based on the value of another field.
     *
     * @param field the field
     * @return true, if successful
     */
    protected boolean includeValue(FieldIdEnum field) {
        DefaultOverride override = overrideMap.get(field);

        if (override == null) {
            return false;
        }

        String value = null;
        FieldConfigBase fieldConfig = fieldConfigManager.get(override.getField());
        if (fieldConfig instanceof FieldConfigEnum) {
            value = String.valueOf(fieldConfigVisitor.getComboBox(override.getField()));
        } else {
            ConsoleManager.getInstance()
                    .error(
                            this,
                            UNSUPPORTED_FIELD_TYPE
                                    + field
                                    + " "
                                    + fieldConfig.getClass().getName());
        }

        if (value != null) {
            for (String legalValue : override.getLegalValues()) {
                if (value.compareToIgnoreCase(legalValue) == 0) {
                    return true;
                }
            }
        }

        return false;
    }
}
