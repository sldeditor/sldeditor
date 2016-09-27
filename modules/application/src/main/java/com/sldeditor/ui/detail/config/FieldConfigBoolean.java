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

import javax.swing.JCheckBox;

import org.opengis.filter.expression.Expression;

import com.sldeditor.common.undo.UndoActionInterface;
import com.sldeditor.common.undo.UndoEvent;
import com.sldeditor.common.undo.UndoInterface;
import com.sldeditor.common.undo.UndoManager;
import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.ui.detail.BasePanel;
import com.sldeditor.ui.widgets.FieldPanel;

/**
 * The Class FieldConfigBoolean wraps a check box GUI component and an optional
 * value/attribute/expression drop down, ({@link com.sldeditor.ui.attribute.AttributeSelection})
 * <p>
 * Supports undo/redo functionality. 
 * <p>
 * Instantiated by {@link com.sldeditor.ui.detail.config.ReadPanelConfig} 
 * 
 * @author Robert Ward (SCISYS)
 */
public class FieldConfigBoolean extends FieldConfigBase implements UndoActionInterface {

    /** The check box. */
    private JCheckBox checkBox;

    /** The default value. */
    private boolean defaultValue = false;

    /** The old value obj. */
    private Boolean oldValueObj = null;

    /**
     * Instantiates a new field config boolean.
     *
     * @param commonData the common data
     */
    public FieldConfigBoolean(FieldConfigCommonData commonData) {
        super(commonData);
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

        checkBox = new JCheckBox("");
        checkBox.setBounds(xPos + BasePanel.WIDGET_X_START, 0, BasePanel.WIDGET_STANDARD_WIDTH, BasePanel.WIDGET_HEIGHT);
        fieldPanel.add(checkBox);
        checkBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                boolean isSelected = checkBox.isSelected();
                Boolean oldValueObj = Boolean.valueOf(!isSelected);
                Boolean newValueObj = Boolean.valueOf(isSelected);

                UndoManager.getInstance().addUndoEvent(new UndoEvent(parentObj, getFieldId(), oldValueObj, newValueObj));

                valueUpdated();
            }
        });

        if(!isValueOnly())
        {
            setAttributeSelectionPanel(fieldPanel.internalCreateAttrButton(Boolean.class, this, isRasterSymbol()));
        }
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
        // Does nothing
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
        if(this.checkBox != null)
        {
            checkBox.setEnabled(enabled);
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
    @Override
    protected Expression generateExpression()
    {
        return getFilterFactory().literal(getBooleanValue());
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
            if(this.checkBox != null)
            {
                return checkBox.isEnabled();
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
        if(this.checkBox != null)
        {
            checkBox.setSelected(defaultValue);
        }
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
        if(objValue != null)
        {
            if(objValue instanceof Boolean)
            {
                populateField((Boolean) objValue);

                valueUpdated();
            }
        }
    }

    /**
     * Gets the value.
     *
     * @return the value
     */
    @Override
    public boolean getBooleanValue()
    {
        if(this.checkBox != null)
        {
            return checkBox.isSelected();
        }
        return false;
    }

    /**
     * Sets the default value.
     *
     * @param defaultValue the new default value
     */
    public void setDefaultValue(boolean defaultValue)
    {
        this.defaultValue = defaultValue;
    }

    /**
     * Gets the string value.
     *
     * @return the string value
     */
    @Override
    public String getStringValue()
    {
        return String.valueOf(getBooleanValue());
    }

    /**
     * Undo action.
     *
     * @param undoRedoObject the undo/redo object
     */
    @Override
    public void undoAction(UndoInterface undoRedoObject)
    {
        if((checkBox != null) && (undoRedoObject != null))
        {
            if(undoRedoObject.getOldValue() instanceof Boolean)
            {
                Boolean oldValue = (Boolean)undoRedoObject.getOldValue();

                checkBox.setSelected(oldValue.booleanValue());
            }
        }
    }

    /**
     * Redo action.
     *
     * @param undoRedoObject the undo/redo object
     */
    @Override
    public void redoAction(UndoInterface undoRedoObject)
    {
        if((checkBox != null) && (undoRedoObject != null))
        {
            if(undoRedoObject.getNewValue() instanceof Boolean)
            {
                Boolean newValue = (Boolean)undoRedoObject.getNewValue();

                checkBox.setSelected(newValue.booleanValue());
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
    public void setTestValue(FieldIdEnum fieldId, boolean testValue) {
        populateField(testValue);

        valueUpdated();
    }

    /**
     * Populate field.
     *
     * @param value the value
     */
    @Override
    public void populateField(Boolean value) {
        if((value != null) && (this.checkBox != null))
        {
            checkBox.setSelected(value);

            UndoManager.getInstance().addUndoEvent(new UndoEvent(this, getFieldId(), oldValueObj, value));

            oldValueObj = value;
        }
    }

    /**
     * Creates a copy of the field.
     *
     * @param fieldConfigBase the field config base
     * @return the field config base
     */
    @Override
    protected FieldConfigBase createCopy(FieldConfigBase fieldConfigBase) {
        FieldConfigBoolean copy = null;

        if(fieldConfigBase != null)
        {
            copy = new FieldConfigBoolean(fieldConfigBase.getCommonData());
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
        if(checkBox != null)
        {
            checkBox.setVisible(visible);
        }
    }
}
