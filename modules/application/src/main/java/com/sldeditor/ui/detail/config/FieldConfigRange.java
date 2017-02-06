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

import java.awt.Dimension;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import org.jaitools.numeric.Range;
import org.opengis.filter.expression.Expression;

import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.common.undo.UndoActionInterface;
import com.sldeditor.common.undo.UndoEvent;
import com.sldeditor.common.undo.UndoInterface;
import com.sldeditor.common.undo.UndoManager;
import com.sldeditor.ui.detail.BasePanel;
import com.sldeditor.ui.iface.SpinnerNotifyInterface;
import com.sldeditor.ui.widgets.DecimalSpinner;
import com.sldeditor.ui.widgets.FieldPanel;

/**
 * The Class FieldConfigRange wraps a spinner GUI component and an optional value/attribute/expression drop down,
 * ({@link com.sldeditor.ui.attribute.AttributeSelection})
 * <p>
 * Supports undo/redo functionality.
 * <p>
 * Instantiated by {@link com.sldeditor.ui.detail.config.ReadPanelConfig}
 * 
 * @author Robert Ward (SCISYS)
 */
public class FieldConfigRange extends FieldConfigBase implements UndoActionInterface {

    /**
     * The Class RangeData.
     */
    class RangeData {
        /** The spinner. */
        private DecimalSpinner spinner = null;

        /** The minimum value. */
        private Double minValue = Double.NEGATIVE_INFINITY;

        /** The maximum value. */
        private Double maxValue = Double.POSITIVE_INFINITY;

        /** The step size. */
        private double stepSize = 1.0;

        private JCheckBox includedCheckBox = null;

        /** The no of decimal places. */
        private int noOfDecimalPlaces = 3;
    }

    /** The start range. */
    private RangeData startRange = new RangeData();

    /** The end range. */
    private RangeData endRange = new RangeData();

    /** The default value. */
    @SuppressWarnings("rawtypes")
    private Range defaultValue = Range.create(0.0, true, 1.0, true);

    /** The configuration set. */
    private boolean configurationSet = false;

    /** The is populating flag. */
    private boolean isPopulating = false;

    @SuppressWarnings("rawtypes")
    private Range previousValue = defaultValue;

    /**
     * Instantiates a new field config double.
     *
     * @param commonData the common data
     */
    public FieldConfigRange(FieldConfigCommonData commonData) {
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
        if (startRange.spinner == null) {

            int xPos = getXPos();
            FieldPanel fieldPanel = createFieldPanel(xPos, "");

            createRow(fieldPanel, xPos, 0, startRange,
                    Localisation.getField(FieldConfigBase.class, "FieldConfigRange.minValue"));
            createRow(fieldPanel, xPos, BasePanel.WIDGET_HEIGHT, endRange,
                    Localisation.getField(FieldConfigBase.class, "FieldConfigRange.maxValue"));

            Dimension preferredSize = new Dimension((int) fieldPanel.getPreferredSize().getWidth(),
                    endRange.spinner.getY() + endRange.spinner.getHeight());

            fieldPanel.setPreferredSize(preferredSize);

            revertToDefaultValue();
        }
    }

    /**
     * Creates the row.
     *
     * @param fieldPanel the field panel
     * @param xPos the x pos
     * @param y the y coordinate
     * @param rangeConfig the range config
     * @param label the label
     */
    private void createRow(FieldPanel fieldPanel, int xPos, int y, RangeData rangeConfig,
            String label) {
        final UndoActionInterface parentObj = this;

        if (configurationSet) {
            rangeConfig.spinner = new DecimalSpinner(rangeConfig.minValue, rangeConfig.maxValue,
                    rangeConfig.stepSize, rangeConfig.noOfDecimalPlaces);
        } else {
            rangeConfig.spinner = new DecimalSpinner();
        }
        JLabel lbl = new JLabel(label);
        lbl.setHorizontalAlignment(SwingConstants.TRAILING);
        lbl.setBounds(xPos, y, BasePanel.LABEL_WIDTH, BasePanel.WIDGET_HEIGHT);
        fieldPanel.add(lbl);

        rangeConfig.spinner.setBounds(xPos + BasePanel.WIDGET_X_START, y,
                BasePanel.WIDGET_STANDARD_WIDTH, BasePanel.WIDGET_HEIGHT);
        fieldPanel.add(rangeConfig.spinner);

        rangeConfig.includedCheckBox = new JCheckBox(
                Localisation.getString(FieldConfigBase.class, "FieldConfigRange.included"));

        rangeConfig.includedCheckBox.setBounds(rangeConfig.spinner.getX() + rangeConfig.spinner.getWidth() + 5, y,
                BasePanel.WIDGET_STANDARD_WIDTH, BasePanel.WIDGET_HEIGHT);
        fieldPanel.add(rangeConfig.includedCheckBox);

        rangeConfig.spinner.registerObserver(new SpinnerNotifyInterface() {
            @Override
            public void notify(double oldValue, double newValue) {
                Double oldValueObj = Double.valueOf(oldValue);
                Double newValueObj = Double.valueOf(newValue);

                UndoManager.getInstance().addUndoEvent(
                        new UndoEvent(parentObj, getFieldId(), oldValueObj, newValueObj));

                valueUpdated();
            }
        });
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
        internal_setEnabled(field == null);
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
        if (startRange.spinner != null) {
            startRange.spinner.setEnabled(enabled);
        }
        if (startRange.includedCheckBox != null) {
            startRange.includedCheckBox.setEnabled(enabled);
        }
        if (endRange.spinner != null) {
            endRange.spinner.setEnabled(enabled);
        }
        if (endRange.includedCheckBox != null) {
            endRange.includedCheckBox.setEnabled(enabled);
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
        Expression expression = getFilterFactory().literal(getRange());

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
            if (startRange.spinner != null) {
                return startRange.spinner.isEnabled();
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
        internalSetValue(defaultValue);
    }

    /**
     * Populate expression.
     *
     * @param objValue the obj value
     */
    @SuppressWarnings("rawtypes")
    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.ui.detail.config.FieldConfigBase#populateExpression(java.lang.Object)
     */
    @Override
    public void populateExpression(Object objValue) {
        Range newValue = defaultValue;

        if (objValue instanceof Range) {
            newValue = (Range) objValue;
        }

        populateField(newValue);
    }

    /**
     * Sets the default value.
     *
     * @param defaultValue the new default value
     */
    @SuppressWarnings("rawtypes")
    public void setDefaultValue(Range defaultValue) {
        this.defaultValue = defaultValue;
    }

    /**
     * Gets the string value.
     *
     * @return the string value
     */
    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.ui.detail.config.FieldConfigBase#getStringValue()
     */
    @Override
    public String getStringValue() {
        return String.valueOf(getRangeValues());
    }

    /**
     * Undo action.
     *
     * @param undoRedoObject the undo/redo object
     */
    @SuppressWarnings("rawtypes")
    @Override
    public void undoAction(UndoInterface undoRedoObject) {
        if ((startRange.spinner != null) && (undoRedoObject != null)) {
            if (undoRedoObject.getOldValue() instanceof Range) {
                Range oldValue = (Range) undoRedoObject.getOldValue();

                internalSetValue(oldValue);
            }
        }
    }

    /**
     * Redo action.
     *
     * @param undoRedoObject the undo/redo object
     */
    @SuppressWarnings("rawtypes")
    @Override
    public void redoAction(UndoInterface undoRedoObject) {
        if ((startRange.spinner != null) && (undoRedoObject != null)) {
            if (undoRedoObject.getNewValue() instanceof Range) {
                Range newValue = (Range) undoRedoObject.getNewValue();

                internalSetValue(newValue);
            }
        }
    }

    /**
     * Populate field.
     *
     * @param value the value
     */
    @SuppressWarnings("rawtypes")
    @Override
    public void populateField(Range value) {
        internalSetValue(value);
    }

    /**
     * Internal set value.
     *
     * @param value the value
     */
    @SuppressWarnings("rawtypes")
    private void internalSetValue(Range value) {
        setPopulating(true);
        Number minValue = value.getMin();
        Number maxValue = value.getMax();

        if (startRange.spinner != null) {
            if (minValue.doubleValue() < startRange.minValue) {
                startRange.spinner.setValue(startRange.minValue);
            } else if (minValue.doubleValue() > startRange.maxValue) {
                startRange.spinner.setValue(startRange.maxValue);
            } else {
                startRange.spinner.setValue(minValue.doubleValue());
            }
        }

        if(startRange.includedCheckBox != null)
        {
            startRange.includedCheckBox.setSelected(value.isMinIncluded());
        }

        if (endRange.spinner != null) {
            if (maxValue.doubleValue() < endRange.minValue) {
                endRange.spinner.setValue(endRange.minValue);
            } else if (maxValue.doubleValue() > endRange.maxValue) {
                endRange.spinner.setValue(endRange.maxValue);
            } else {
                endRange.spinner.setValue(maxValue.doubleValue());
            }
        }

        if(endRange.includedCheckBox != null)
        {
            endRange.includedCheckBox.setSelected(value.isMaxIncluded());
        }
        setPopulating(false);
    }

    /**
     * Sets the populating.
     *
     * @param isPopulating the new populating flag
     */
    private void setPopulating(boolean isPopulating) {
        this.isPopulating = isPopulating;
    }

    /**
     * Creates a copy of the field.
     *
     * @param fieldConfigBase the field config base
     * @return the field config base
     */
    @Override
    protected FieldConfigBase createCopy(FieldConfigBase fieldConfigBase) {
        FieldConfigRange copy = null;

        if (fieldConfigBase != null) {
            copy = new FieldConfigRange(fieldConfigBase.getCommonData());

            FieldConfigRange doubleFieldConfig = (FieldConfigRange) fieldConfigBase;
            copy.setConfig(doubleFieldConfig.startRange.minValue,
                    doubleFieldConfig.startRange.maxValue, doubleFieldConfig.startRange.stepSize,
                    doubleFieldConfig.startRange.noOfDecimalPlaces);
            copy.setDefaultValue(this.defaultValue);
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
    public void setConfig(double minValue, double maxValue, double stepSize,
            int noOfDecimalPlaces) {
        startRange.minValue = minValue;
        startRange.maxValue = maxValue;
        startRange.stepSize = stepSize;
        startRange.noOfDecimalPlaces = noOfDecimalPlaces;

        endRange.minValue = minValue;
        endRange.maxValue = maxValue;
        endRange.stepSize = stepSize;
        endRange.noOfDecimalPlaces = noOfDecimalPlaces;

        this.configurationSet = true;
    }

    /**
     * Sets the field visible.
     *
     * @param visible the new visible state
     */
    @Override
    public void setVisible(boolean visible) {
        if (startRange.spinner != null) {
            startRange.spinner.setVisible(visible);
        }
        if (startRange.includedCheckBox != null) {
            startRange.includedCheckBox.setVisible(visible);
        }
        if (endRange.spinner != null) {
            endRange.spinner.setVisible(visible);
        }
        if (endRange.includedCheckBox != null) {
            endRange.includedCheckBox.setVisible(visible);
        }
    }

    /**
     * Gets the range.
     *
     * @return the range
     */
    @SuppressWarnings("rawtypes")
    private Range getRangeValues() {
        Range range = previousValue;
        if (!isPopulating()) {
            boolean minIncluded = startRange.includedCheckBox.isSelected();
            boolean maxIncluded = endRange.includedCheckBox.isSelected();;
            Double minValue = startRange.spinner.getDoubleValue();
            Double maxValue = endRange.spinner.getDoubleValue();
            range = Range.create(minValue, minIncluded, maxValue, maxIncluded);
            previousValue = range;
        }
        return range;
    }

    /**
     * Gets the range value.
     *
     * @return the font
     */
    @SuppressWarnings("rawtypes")
    @Override
    public Range getRange() {
        return getRangeValues();
    }

    /**
     * Checks if is populating flag is set.
     *
     * @return the is populating flag
     */
    public boolean isPopulating() {
        return isPopulating;
    }
}
