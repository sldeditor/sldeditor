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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.opengis.filter.expression.Expression;

import com.sldeditor.common.undo.UndoActionInterface;
import com.sldeditor.common.undo.UndoEvent;
import com.sldeditor.common.undo.UndoInterface;
import com.sldeditor.common.undo.UndoManager;
import com.sldeditor.ui.detail.BasePanel;
import com.sldeditor.ui.detail.config.symboltype.SymbolTypeConfig;
import com.sldeditor.ui.widgets.FieldPanel;
import com.sldeditor.ui.widgets.ValueComboBox;
import com.sldeditor.ui.widgets.ValueComboBoxData;

/**
 * The Class FieldConfigEnum wraps a drop down GUI component and an optional
 * value/attribute/expression drop down, ({@link com.sldeditor.ui.attribute.AttributeSelection})
 * <p>
 * Supports undo/redo functionality. 
 * <p>
 * Instantiated by {@link com.sldeditor.ui.detail.config.ReadPanelConfig} 
 * 
 * @author Robert Ward (SCISYS)
 */
public class FieldConfigEnum extends FieldConfigBase implements UndoActionInterface {

    /** The key list. */
    private List<String> keyList = new ArrayList<String>();

    /** The value map. */
    private Map<String, String> valueMap = new HashMap<String, String>();

    /** The combo data map. */
    private Map<String, ValueComboBoxData> comboDataMap = new HashMap<String, ValueComboBoxData>();

    /** The field map. */
    private Map<Class<?>, Map<FieldId, Boolean> > fieldMap = new HashMap<Class<?>, Map<FieldId, Boolean> >();

    /** The default value. */
    private String defaultValue = "";

    /** The combo box. */
    private ValueComboBox comboBox = null;

    /** The old value obj. */
    private Object oldValueObj = null;

    /** The logger. */
    private static Logger logger = Logger.getLogger(FieldConfigEnum.class);

    /**
     * Instantiates a new field config enum.
     *
     * @param panelId the panel id
     * @param id the id
     * @param label the label
     * @param valueOnly the value only
     */
    public FieldConfigEnum(Class<?> panelId, FieldId id, String label, boolean valueOnly) {
        super(panelId, id, label, valueOnly);
    }

    /**
     * Adds the value.
     *
     * @param key the key
     * @param value the value
     */
    private void addValue(String key, String value) {
        if((key == null) || key.isEmpty())
        {
            keyList.add("");
            valueMap.put("",  value);
        }
        else
        {
            keyList.add(key);
            valueMap.put(key,  value);
        }
    }

    /**
     * Creates the ui.
     */
    /* (non-Javadoc)
     * @see com.sldeditor.ui.detail.config.FieldConfigBase#createUI()
     */
    @Override
    public void createUI() {
        final UndoActionInterface parentObj = this;

        int xPos = getXPos();
        FieldPanel fieldPanel = createFieldPanel(xPos, getLabel());

        List<ValueComboBoxData> dataList = new ArrayList<ValueComboBoxData>();

        for(String key : keyList)
        {
            dataList.add(new ValueComboBoxData(key, valueMap.get(key), getPanelId()));
        }

        if(!dataList.isEmpty())
        {
            defaultValue = dataList.get(0).getKey();
        }

        comboBox = new ValueComboBox();
        comboBox.initialiseSingle(dataList);
        comboBox.setBounds(xPos + BasePanel.WIDGET_X_START, 0, BasePanel.WIDGET_STANDARD_WIDTH, BasePanel.WIDGET_HEIGHT);
        fieldPanel.add(comboBox);

        if(!isValueOnly())
        {
            setAttributeSelectionPanel(fieldPanel.internalCreateAttrButton(String.class, this));
        }

        if(dataList != null)
        {
            for(ValueComboBoxData data : dataList)
            {
                this.comboDataMap.put(data.getKey(), data);
            }
        }

        comboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ValueComboBox comboBox = (ValueComboBox) e.getSource();
                if (comboBox.getSelectedItem() != null) {

                    Object newValueObj = comboBox.getSelectedValue().getKey();

                    if((oldValueObj == null) && comboBox.getItemCount() > 0)
                    {
                        oldValueObj = comboBox.getFirstItem().getKey();
                    }

                    UndoManager.getInstance().addUndoEvent(new UndoEvent(parentObj, getFieldId(), oldValueObj, newValueObj));

                    oldValueObj = newValueObj;

                    valueUpdated();
                }
            }
        });
    }

    /**
     * Attribute selection.
     *
     * @param field the field
     */
    /* (non-Javadoc)
     * @see com.sldeditor.ui.iface.AttributeButtonSelectionInterface#attributeSelection(java.lang.String)
     */
    @Override
    public void attributeSelection(String field)
    {
        if(this.comboBox != null)
        {
            this.comboBox.setEnabled(field == null);
        }
    }

    /**
     * Sets the enabled.
     *
     * @param enabled the new enabled
     */
    /* (non-Javadoc)
     * @see com.sldeditor.ui.detail.config.FieldConfigBase#setEnabled(boolean)
     */
    @Override
    public void setEnabled(boolean enabled)
    {
        if(comboBox != null)
        {
            comboBox.setEnabled(enabled);
        }
    }

    /**
     * Generate expression.
     *
     * @return the expression
     */
    /* (non-Javadoc)
     * @see com.sldeditor.ui.detail.config.FieldConfigBase#generateExpression()
     */
    protected Expression generateExpression()
    {
        Expression expression = null;

        if(comboBox != null)
        {
            ValueComboBoxData value = comboBox.getSelectedValue();
            if(value != null)
            {
                expression = getFilterFactory().literal(value.getKey());
            }
        }

        return expression;
    }

    /**
     * Checks if is enabled.
     *
     * @return true, if is enabled
     */
    /* (non-Javadoc)
     * @see com.sldeditor.ui.detail.config.FieldConfigBase#isEnabled()
     */
    @Override
    public boolean isEnabled()
    {
        if((attributeSelectionPanel != null) && !isValueOnly())
        {
            return attributeSelectionPanel.isEnabled();
        }
        else
        {
            if(comboBox != null)
            {
                return comboBox.isEnabled();
            }
        }
        return false;
    }

    /**
     * Revert to default value.
     */
    /* (non-Javadoc)
     * @see com.sldeditor.ui.detail.config.FieldConfigBase#revertToDefaultValue()
     */
    @Override
    public void revertToDefaultValue()
    {
        populateField(defaultValue);
    }

    /**
     * Populate expression.
     *
     * @param objValue the obj value
     */
    /* (non-Javadoc)
     * @see com.sldeditor.ui.detail.config.FieldConfigBase#populateExpression(java.lang.Object)
     */
    @Override
    public void populateExpression(Object objValue)
    {
        if(comboBox != null)
        {
            if(objValue instanceof String)
            {
                String sValue = (String) objValue;

                populateField(sValue);
            }
        }
    }

    /**
     * Gets the value.
     *
     * @return the value
     */
    @Override
    public ValueComboBoxData getEnumValue()
    {
        if(comboBox == null)
        {
            return null;
        }
        ValueComboBoxData selectedItem = (ValueComboBoxData) comboBox.getSelectedItem();

        return selectedItem;
    }

    /**
     * Gets the string value.
     *
     * @return the string value
     */
    @Override
    public String getStringValue()
    {
        ValueComboBoxData enumValue = getEnumValue();
        if(enumValue != null)
        {
            return enumValue.getKey();
        }

        return null;
    }

    /**
     * Adds the config.
     *
     * @param configList the config list
     */
    public void addConfig(List<SymbolTypeConfig> configList)
    {
        if(configList != null)
        {
            for(SymbolTypeConfig config : configList)
            {
                if(config != null)
                {
                    fieldMap.putAll(config.getFieldMap());

                    Map<String, String> optionMap = config.getOptionMap();

                    for(String key : optionMap.keySet())
                    {
                        addValue(key, optionMap.get(key));
                    }
                }
            }
        }
    }

    /**
     * Gets the field enable state.
     *
     * @return the field enable state
     */
    public Map<FieldId, Boolean> getFieldEnableState()
    {
        ValueComboBoxData value = getEnumValue();
        if(value == null)
        {
            return null;
        }
        Class<?> panelId = value.getPanelId();

        return fieldMap.get(panelId);
    }

    /**
     * Undo action.
     *
     * @param undoRedoObject the undo redo object
     */
    /* (non-Javadoc)
     * @see com.sldeditor.undo.UndoActionInterface#undoAction(com.sldeditor.undo.UndoInterface)
     */
    @Override
    public void undoAction(UndoInterface undoRedoObject)
    {
        if((comboBox != null) && (undoRedoObject != null))
        {
            if(undoRedoObject.getOldValue() instanceof String)
            {
                String oldValue = (String)undoRedoObject.getOldValue();

                ValueComboBoxData valueComboBoxData = comboDataMap.get(oldValue);
                if(valueComboBoxData != null)
                {
                    comboBox.setSelectedItem(valueComboBoxData);
                }
            }
        }
    }

    /**
     * Redo action.
     *
     * @param undoRedoObject the undo redo object
     */
    /* (non-Javadoc)
     * @see com.sldeditor.undo.UndoActionInterface#redoAction(com.sldeditor.undo.UndoInterface)
     */
    @Override
    public void redoAction(UndoInterface undoRedoObject)
    {
        if((comboBox != null) && (undoRedoObject != null))
        {
            if(undoRedoObject.getNewValue() instanceof String)
            {
                String oldValue = (String)undoRedoObject.getNewValue();

                ValueComboBoxData valueComboBoxData = comboDataMap.get(oldValue);
                if(valueComboBoxData != null)
                {
                    comboBox.setSelectedItem(valueComboBoxData);
                }
            }
        }
    }

    /**
     * Sets the test value.
     *
     * @param fieldId the field id
     * @param testValue the test value
     */
    @Override
    public void setTestValue(FieldId fieldId, String testValue) {
        populateField(testValue);

        valueUpdated();
    }

    /**
     * Populate field.
     *
     * @param value the value
     */
    @Override
    public void populateField(String value) {
        if(comboBox != null)
        {
            ValueComboBoxData valueComboBoxData = comboDataMap.get(value);
            if(valueComboBoxData != null)
            {
                comboBox.setSelectedItem(valueComboBoxData);
            }
            else
            {
                logger.error("Unknown ValueComboBoxData value : " + value);
            }
        }
    }

    /**
     * Sets the default value.
     *
     * @param defaultValue the new default value
     */
    public void setDefaultValue(String defaultValue)
    {
        this.defaultValue = defaultValue;
    }

    /**
     * Creates a copy of the field.
     *
     * @param fieldConfigBase the field config base
     * @return the field config base
     */
    @Override
    protected FieldConfigBase createCopy(FieldConfigBase fieldConfigBase) {
        FieldConfigEnum copy = null;

        if(fieldConfigBase != null)
        {
            copy = new FieldConfigEnum(fieldConfigBase.getPanelId(),
                    fieldConfigBase.getFieldId(),
                    fieldConfigBase.getLabel(),
                    fieldConfigBase.isValueOnly());
        }
        return copy;
    }

    /**
     * Sets the field visible.
     *
     * @param visible the new visible state
     */
    @Override
    public void setVisible(boolean visible) {
        if(comboBox != null)
        {
            comboBox.setVisible(visible);
        }
    }
}
