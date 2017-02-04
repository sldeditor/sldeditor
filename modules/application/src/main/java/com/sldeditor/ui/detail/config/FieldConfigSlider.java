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

import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.opengis.filter.expression.Expression;

import com.sldeditor.common.undo.UndoActionInterface;
import com.sldeditor.common.undo.UndoEvent;
import com.sldeditor.common.undo.UndoInterface;
import com.sldeditor.common.undo.UndoManager;
import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.ui.detail.BasePanel;
import com.sldeditor.ui.widgets.FieldPanel;

/**
 * The Class FieldConfigSlider wraps a slider GUI component and an optional value/attribute/expression drop down,
 * ({@link com.sldeditor.ui.attribute.AttributeSelection})
 * <p>
 * Values between 0.0 and 1.0.
 * <p>
 * Supports undo/redo functionality.
 * <p>
 * Instantiated by {@link com.sldeditor.ui.detail.config.ReadPanelConfig}
 * 
 * @author Robert Ward (SCISYS)
 */
public class FieldConfigSlider extends FieldConfigBase implements UndoActionInterface {

    /** The slider. */
    private JSlider slider;

    /** The default value. */
    private double defaultValue = 0.5;

    /** The maximum value. */
    private double maxValue = 1.0;

    /** The old value obj. */
    private Object oldValueObj = null;

    /**
     * Instantiates a new field config slider.
     *
     * @param commonData the common data
     */
    public FieldConfigSlider(FieldConfigCommonData commonData) {
        super(commonData);
    }

    /**
     * Creates the ui.
     */
    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.ui.detail.config.FieldConfigBase#createUI()
     */
    @Override
    public void createUI() {
        if (slider == null) {
            final UndoActionInterface parentObj = this;

            int xPos = getXPos();

            FieldPanel fieldPanel = createFieldPanel(xPos, getLabel());

            slider = new JSlider();
            slider.setBounds(xPos + BasePanel.WIDGET_X_START, 0, BasePanel.WIDGET_STANDARD_WIDTH,
                    BasePanel.WIDGET_HEIGHT);
            fieldPanel.add(slider);

            slider.addChangeListener(new ChangeListener() {

                @Override
                public void stateChanged(ChangeEvent e) {
                    JSlider source = (JSlider) e.getSource();
                    Integer newValueObj = (int) source.getValue();

                    UndoManager.getInstance().addUndoEvent(
                            new UndoEvent(parentObj, getFieldId(), oldValueObj, newValueObj));

                    oldValueObj = newValueObj;
                    valueUpdated();
                }
            });

            if (!isValueOnly()) {
                setAttributeSelectionPanel(
                        fieldPanel.internalCreateAttrButton(Double.class, this, isRasterSymbol()));
            }
        }
    }

    /**
     * Attribute selection.
     *
     * @param field the field
     */
    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.ui.iface.AttributeButtonSelectionInterface#attributeSelection(java.lang.String)
     */
    @Override
    public void attributeSelection(String field) {
        // Not used
    }

    /**
     * Sets the enabled.
     *
     * @param enabled the new enabled
     */
    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.ui.detail.config.FieldConfigBase#setEnabled(boolean)
     */
    @Override
    public void internal_setEnabled(boolean enabled) {
        if (slider != null) {
            slider.setEnabled(enabled);
        }
    }

    /**
     * Generate expression.
     *
     * @return the expression
     */
    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.ui.detail.config.FieldConfigBase#generateExpression()
     */
    @Override
    protected Expression generateExpression() {
        Expression expression = null;

        if (slider != null) {
            expression = getFilterFactory().literal(getDoubleValue());
        }
        return expression;
    }

    /**
     * Checks if is enabled.
     *
     * @return true, if is enabled
     */
    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.ui.detail.config.FieldConfigBase#isEnabled()
     */
    @Override
    public boolean isEnabled() {
        if ((attributeSelectionPanel != null) && !isValueOnly()) {
            return attributeSelectionPanel.isEnabled();
        } else {
            if (slider != null) {
                return slider.isEnabled();
            }
        }
        return false;
    }

    /**
     * Revert to default value.
     */
    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.ui.detail.config.FieldConfigBase#revertToDefaultValue()
     */
    @Override
    public void revertToDefaultValue() {
        populateField(this.defaultValue);
    }

    /**
     * Populate expression.
     *
     * @param objValue the obj value
     */
    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.ui.detail.config.FieldConfigBase#populateExpression(java.lang.Object)
     */
    @Override
    public void populateExpression(Object objValue) {
        Double value = defaultValue;

        if (objValue instanceof Integer) {
            int i = ((Integer) objValue).intValue();
            value = Double.valueOf((double) i);
        } else if (objValue instanceof Double) {
            value = (Double) objValue;
        } else if (objValue instanceof String) {
            value = Double.valueOf((String) objValue);
        }

        populateField(value);
    }

    /**
     * Gets the slider value.
     *
     * @return the slider value
     */
    @Override
    public double getDoubleValue() {
        double dValue = defaultValue;
        if (slider != null) {
            int iValue = slider.getValue();

            double ratio = (double) iValue / slider.getMaximum();
            dValue = ratio * this.maxValue;
        }

        return dValue;
    }

    /**
     * Gets the string value.
     *
     * @return the string value
     */
    @Override
    public String getStringValue() {
        return String.valueOf(getDoubleValue());
    }

    /**
     * Undo action.
     *
     * @param undoRedoObject the undo/redo object
     */
    @Override
    public void undoAction(UndoInterface undoRedoObject) {
        if ((slider != null) && (undoRedoObject != null)) {
            if (undoRedoObject.getOldValue() instanceof Integer) {
                Integer oldValue = (Integer) undoRedoObject.getOldValue();

                slider.setValue(oldValue.intValue());
            }
        }
    }

    /**
     * Redo action.
     *
     * @param undoRedoObject the undo/redo object
     */
    @Override
    public void redoAction(UndoInterface undoRedoObject) {
        if ((slider != null) && (undoRedoObject != null)) {
            if (undoRedoObject.getNewValue() instanceof Integer) {
                Integer newValue = (Integer) undoRedoObject.getNewValue();

                slider.setValue(newValue.intValue());
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

        valueUpdated();
    }

    /**
     * Populate field.
     *
     * @param value the value
     */
    @Override
    public void populateField(Double value) {
        if (slider != null) {
            double ratio = value / this.maxValue;
            int sliderRatio = (int) (ratio * slider.getMaximum());
            slider.setValue(sliderRatio);

            oldValueObj = Integer.valueOf(sliderRatio);
        }
    }

    /**
     * Sets the default value.
     *
     * @param defaultValue the new default value
     */
    public void setDefaultValue(double defaultValue) {
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
        FieldConfigSlider copy = null;

        if (fieldConfigBase != null) {
            copy = new FieldConfigSlider(fieldConfigBase.getCommonData());
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
        if (slider != null) {
            slider.setVisible(visible);
        }
    }
}
