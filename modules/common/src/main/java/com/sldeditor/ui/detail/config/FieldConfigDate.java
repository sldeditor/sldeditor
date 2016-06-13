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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.swing.Box;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.DateFormatter;

import org.opengis.filter.expression.Expression;

import com.sldeditor.common.console.ConsoleManager;
import com.sldeditor.common.undo.UndoActionInterface;
import com.sldeditor.common.undo.UndoEvent;
import com.sldeditor.common.undo.UndoInterface;
import com.sldeditor.common.undo.UndoManager;
import com.sldeditor.ui.detail.BasePanel;
import com.sldeditor.ui.detail.MultipleFieldInterface;
import com.sldeditor.ui.widgets.FieldPanel;

import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;

/**
 * The Class FieldConfigDate wraps a date picker GUI component.
 * <p>
 * Supports undo/redo functionality. 
 * <p>
 * Instantiated by {@link com.sldeditor.ui.detail.config.ReadPanelConfig} 
 * 
 * @author Robert Ward (SCISYS)
 */
public class FieldConfigDate extends FieldConfigBase implements UndoActionInterface {

    /** The old value obj. */
    private Object oldValueObj = null;

    /** The date picker. */
    private JDatePickerImpl datePicker;

    /** The time picker. */
    private JSpinner timePicker;

    /** The date format. */
    private DateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);

    /** The time format. */
    private DateFormat tf = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);

    /** The date model. */
    private UtilDateModel dateModel = new UtilDateModel();

    /** The time editor. */
    private JSpinner.DateEditor timeEditor;

    /**
     * Instantiates a new field config slider.
     *
     * @param panelId the panel id
     * @param id the id
     * @param label the label
     * @param valueOnly the value only
     * @param multipleValues the multiple values
     */
    public FieldConfigDate(Class<?> panelId, FieldId id, String label, boolean valueOnly, boolean multipleValues) {
        super(panelId, id, label, valueOnly, multipleValues);
    }

    /**
     * Creates the ui.
     *
     * @param parentPanel the parent panel
     * @param parentBox the parent box
     */
    /* (non-Javadoc)
     * @see com.sldeditor.ui.detail.config.FieldConfigBase#createUI()
     */
    @Override
    public void createUI(MultipleFieldInterface parentPanel, Box parentBox) {
        final UndoActionInterface parentObj = this;

        int xPos = getXPos();

        FieldPanel fieldPanel = createFieldPanel(xPos, getLabel(), parentPanel, parentBox);

        JDatePanelImpl datePanel = new JDatePanelImpl(dateModel);
        datePicker = new JDatePickerImpl(datePanel);
        datePicker.setBounds(xPos + BasePanel.WIDGET_X_START, 0, BasePanel.WIDGET_STANDARD_WIDTH, BasePanel.WIDGET_HEIGHT);
        fieldPanel.add(datePicker);

        dateModel.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e)
            {
                UtilDateModel utilDateModel = (UtilDateModel) e.getSource();

                int day = utilDateModel.getDay();
                int month = utilDateModel.getMonth();
                int year = utilDateModel.getYear();

                Calendar.getInstance().set(year, month, day);

                Object newValueObj = Calendar.getInstance().getTime();

                UndoManager.getInstance().addUndoEvent(new UndoEvent(parentObj, getFieldId(), oldValueObj, newValueObj));

                oldValueObj = newValueObj;
                valueUpdated();
            }
        });

        // Time picker
        SpinnerDateModel model = new SpinnerDateModel();
        Calendar calendar = Calendar.getInstance();
        model.setValue(calendar.getTime());
        timePicker = new JSpinner(model);
        timePicker.setBounds(datePicker.getX() + datePicker.getWidth() + 10, 0, BasePanel.WIDGET_STANDARD_WIDTH, BasePanel.WIDGET_HEIGHT);
        timePicker.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                valueUpdated();
            }});
        fieldPanel.add(timePicker);
        timeEditor = new JSpinner.DateEditor(timePicker, "HH:mm:ss");
        timePicker.setEditor(timeEditor);
        DateFormatter formatter = (DateFormatter)timeEditor.getTextField().getFormatter();
        formatter.setAllowsInvalid(false); 
        formatter.setOverwriteMode(true);

        if(!isValueOnly())
        {
            setAttributeSelectionPanel(fieldPanel.internalCreateAttrButton(Double.class, this));
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
        // Not used
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
        if(datePicker != null)
        {
            datePicker.setEnabled(enabled);
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

        if(datePicker != null)
        {
            expression = getFilterFactory().literal(getStringValue());
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
            if(datePicker != null)
            {
                return datePicker.isEnabled();
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
        Date currentDate = new Date();

        populateField(currentDate);
    }

    /**
     * Populate expression.
     *
     * @param objValue the obj value
     * @param opacity the opacity
     */
    /* (non-Javadoc)
     * @see com.sldeditor.ui.detail.config.FieldConfigBase#populateExpression(java.lang.Object, org.opengis.filter.expression.Expression)
     */
    @Override
    public void populateExpression(Object objValue, Expression opacity)
    {
        Date value = null;

        if(objValue instanceof Date)
        {
            value = (Date) objValue;
        }
        else if(objValue instanceof String)
        {
            try {
                value = df.parse((String) objValue);
            }
            catch (ParseException e) {
                ConsoleManager.getInstance().exception(this, e);
            }
        }

        populateField(value);
    }

    /**
     * Gets the string value.
     *
     * @return the string value
     */
    @Override
    public String getStringValue()
    {
        return String.format("%sT%sZ", df.format(getDate()), tf.format(getDate()));
    }

    /**
     * Gets the date.
     *
     * @return the date
     */
    private Date getDate()
    {
        Date selectedDate = (Date) datePicker.getModel().getValue();

        Date time = (Date) timePicker.getValue();

        Calendar cal = Calendar.getInstance();
        cal.setTime(time);

        int hour = cal.get(Calendar.HOUR);
        int minute = cal.get(Calendar.MINUTE);
        int seconds = cal.get(Calendar.SECOND);

        cal.setTime(selectedDate);
        cal.set(Calendar.HOUR, hour);
        cal.set(Calendar.MINUTE, minute);
        cal.set(Calendar.SECOND, seconds);

        return cal.getTime();
    }

    /**
     * Undo action.
     *
     * @param undoRedoObject the undo/redo object
     */
    @Override
    public void undoAction(UndoInterface undoRedoObject)
    {
        if(datePicker != null)
        {
            Date oldValue = (Date)undoRedoObject.getOldValue();

            dateModel.setValue(oldValue);
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
        if(datePicker != null)
        {
            Date newValue = (Date)undoRedoObject.getNewValue();

            dateModel.setValue(newValue);
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
        try {
            Date date = df.parse(testValue);
            populateField(date);

            valueUpdated();
        }
        catch (ParseException e) {
            ConsoleManager.getInstance().exception(this, e);
        }
    }

    /**
     * Populate field.
     *
     * @param value the value
     */
    @Override
    public void populateField(Date value) {
        if(dateModel != null)
        {
            dateModel.setValue(value);
            UndoManager.getInstance().addUndoEvent(new UndoEvent(this, getFieldId(), oldValueObj, value));
            oldValueObj = value;
            valueUpdated();
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
        FieldConfigDate copy = new FieldConfigDate(fieldConfigBase.getPanelId(),
                fieldConfigBase.getFieldId(),
                fieldConfigBase.getLabel(),
                fieldConfigBase.isValueOnly(),
                fieldConfigBase.hasMultipleValues());
        return copy;
    }

    /**
     * Gets the class type supported.
     *
     * @return the class type
     */
    @Override
    public Class<?> getClassType() {
        return Date.class;
    }

    /**
     * Sets the field visible.
     *
     * @param visible the new visible state
     */
    @Override
    public void setVisible(boolean visible) {
        if(datePicker != null)
        {
            datePicker.setVisible(visible);
        }
    }
}
