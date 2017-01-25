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

import org.opengis.filter.expression.Expression;

import com.sldeditor.common.undo.UndoActionInterface;
import com.sldeditor.common.undo.UndoEvent;
import com.sldeditor.common.undo.UndoInterface;
import com.sldeditor.common.undo.UndoManager;
import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.ui.detail.BasePanel;
import com.sldeditor.ui.iface.SpinnerNotifyInterface;
import com.sldeditor.ui.widgets.DecimalSpinner;
import com.sldeditor.ui.widgets.FieldPanel;

/**
 * The Class FieldConfigDouble wraps a spinner GUI component and an optional
 * value/attribute/expression drop down, ({@link com.sldeditor.ui.attribute.AttributeSelection})
 * <p>
 * Supports undo/redo functionality. 
 * <p>
 * Instantiated by {@link com.sldeditor.ui.detail.config.ReadPanelConfig} 
 * 
 * @author Robert Ward (SCISYS)
 */
public class FieldConfigDouble extends FieldConfigBase implements UndoActionInterface {

    /** The spinner. */
    private DecimalSpinner spinner;

    /** The default value. */
    private Double defaultValue = 0.0;

    /** The minimum value. */
    private Double minValue = Double.MIN_VALUE;

    /** The maximum value. */
    private Double maxValue = Double.MAX_VALUE;

    /** The step size. */
    private double stepSize = 1.0;

    /** The no of decimal places. */
    private int noOfDecimalPlaces = 3;

    /** The configuration set. */
    private boolean configurationSet = false;

    /**
     * Instantiates a new field config double.
     *
     * @param commonData the common data
     */
    public FieldConfigDouble(FieldConfigCommonData commonData) {
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

        if(configurationSet)
        {
            spinner = new DecimalSpinner(minValue, maxValue, stepSize, noOfDecimalPlaces);
        }
        else
        {
            spinner = new DecimalSpinner();
        }
        spinner.setBounds(xPos + BasePanel.WIDGET_X_START, 0, BasePanel.WIDGET_STANDARD_WIDTH, BasePanel.WIDGET_HEIGHT);
        fieldPanel.add(spinner);

        if(!isValueOnly())
        {
            setAttributeSelectionPanel(fieldPanel.internalCreateAttrButton(Double.class, this, isRasterSymbol()));
        }

        spinner.registerObserver(new SpinnerNotifyInterface() {
            @Override
            public void notify(double oldValue, double newValue) {
                Double oldValueObj = Double.valueOf(oldValue);
                Double newValueObj = Double.valueOf(newValue);

                UndoManager.getInstance().addUndoEvent(new UndoEvent(parentObj, getFieldId(), oldValueObj, newValueObj));

                valueUpdated();
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
        if(this.spinner != null)
        {
            this.spinner.setEnabled(field == null);
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
        if(spinner != null)
        {
            spinner.setEnabled(enabled);
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
        Expression expression = null;
        if(spinner != null)
        {
            expression = getFilterFactory().literal(spinner.getDoubleValue());
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
            if(spinner != null)
            {
                return spinner.isEnabled();
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
        internalSetValue(this.defaultValue);
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
        Double newValue = 0.0;

        if(objValue instanceof Integer)
        {
            int i = ((Integer) objValue).intValue();
            newValue = Double.valueOf((double)i);
        }
        else if(objValue instanceof Long)
        {
            long i = ((Long) objValue).longValue();
            newValue = Double.valueOf((double)i);
        }
        else if(objValue instanceof Double)
        {
            newValue = (Double) objValue;
        }
        else if(objValue instanceof String)
        {
            newValue = Double.valueOf((String)objValue);
        }

        populateField(newValue);
    }

    /**
     * Sets the default value.
     *
     * @param defaultValue the new default value
     */
    public void setDefaultValue(double defaultValue)
    {
        this.defaultValue = defaultValue;
    }

    /**
     * Gets the value.
     *
     * @return the value
     */
    @Override
    public double getDoubleValue()
    {
        if(spinner != null)
        {
            return spinner.getDoubleValue();
        }

        return 0.0;
    }

    /**
     * Gets the string value.
     *
     * @return the string value
     */
    /* (non-Javadoc)
     * @see com.sldeditor.ui.detail.config.FieldConfigBase#getStringValue()
     */
    @Override
    public String getStringValue()
    {
        return String.valueOf(getDoubleValue());
    }

    /**
     * Undo action.
     *
     * @param undoRedoObject the undo/redo object
     */
    @Override
    public void undoAction(UndoInterface undoRedoObject)
    {
        if((spinner != null) && (undoRedoObject != null))
        {
            if(undoRedoObject.getOldValue() instanceof Double)
            {
                Double oldValue = (Double)undoRedoObject.getOldValue();

                internalSetValue(oldValue);
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
        if((spinner != null) && (undoRedoObject != null))
        {
            if(undoRedoObject.getNewValue() instanceof Double)
            {
                Double newValue = (Double)undoRedoObject.getNewValue();

                internalSetValue(newValue);
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
    public void setTestValue(FieldIdEnum fieldId, double testValue) {
        populateField(testValue);
    }

    /**
     * Populate field.
     *
     * @param value the value
     */
    @Override
    public void populateField(Double value) {
        internalSetValue(value);
    }

    /**
     * Internal set value.
     *
     * @param value the value
     */
    private void internalSetValue(Double value) {
        if(spinner != null)
        {
            if(value.doubleValue() < minValue)
            {
                spinner.setValue(minValue);
            }
            else if(value.doubleValue() > maxValue)
            {
                spinner.setValue(maxValue);
            }
            else
            {
                spinner.setValue(value);
            }
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
        FieldConfigDouble copy = null;

        if(fieldConfigBase != null)
        {
            copy = new FieldConfigDouble(fieldConfigBase.getCommonData());

            FieldConfigDouble doubleFieldConfig = (FieldConfigDouble)fieldConfigBase;
            copy.setConfig(doubleFieldConfig.minValue, 
                    doubleFieldConfig.maxValue, 
                    doubleFieldConfig.stepSize,
                    doubleFieldConfig.noOfDecimalPlaces);
            copy.setDefaultValue(doubleFieldConfig.defaultValue);
        }
        return copy;
    }

    /**
     * Sets the configuration.
     *
     * @param minValue the minimum value
     * @param maxValue the maximum value
     * @param stepSize the step size
     * @param noOfDecimalPlaces the no of decimal places
     */
    public void setConfig(double minValue, 
            double maxValue,
            double stepSize,
            int noOfDecimalPlaces)
    {
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.stepSize = stepSize;
        this.noOfDecimalPlaces = noOfDecimalPlaces;
        this.configurationSet = true;
    }

    /**
     * Sets the field visible.
     *
     * @param visible the new visible state
     */
    @Override
    public void setVisible(boolean visible) {
        if(spinner != null)
        {
            spinner.setVisible(visible);
        }
    }
}
