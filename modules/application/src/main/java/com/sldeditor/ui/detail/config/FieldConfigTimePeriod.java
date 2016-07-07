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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.DateFormatter;

import org.geotools.temporal.object.DefaultPeriod;
import org.opengis.filter.expression.Expression;
import org.opengis.temporal.Period;

import com.sldeditor.common.console.ConsoleManager;
import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.common.undo.UndoActionInterface;
import com.sldeditor.common.undo.UndoEvent;
import com.sldeditor.common.undo.UndoInterface;
import com.sldeditor.common.undo.UndoManager;
import com.sldeditor.filter.v2.function.temporal.Duration;
import com.sldeditor.filter.v2.function.temporal.TimePeriod;
import com.sldeditor.ui.detail.BasePanel;
import com.sldeditor.ui.widgets.FieldPanel;

import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;

/**
 * The Class FieldConfigTimePeriod wraps a time period picker GUI component and an optional
 * value/attribute/expression drop down, ({@link com.sldeditor.ui.attribute.AttributeSelection})
 * <p>
 * Supports undo/redo functionality. 
 * <p>
 * Instantiated by {@link com.sldeditor.ui.detail.config.ReadPanelConfig} 
 * 
 * @author Robert Ward (SCISYS)
 */
public class FieldConfigTimePeriod extends FieldConfigBase implements UndoActionInterface {

    /** The old value obj. */
    private Object oldValueObj = null;

    /** The date format. */
    private DateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);

    /**
     * The Class TimePeriodPanel.
     */
    private static class TimePeriodPanel
    {
        /** The date model. */
        private UtilDateModel dateModel = new UtilDateModel();

        /** The date picker. */
        private JDatePickerImpl datePicker;

        /** The time picker. */
        private JSpinner timePicker;

        /** The time editor. */
        private JSpinner.DateEditor timeEditor;

        /** The radio button group. */
        private ButtonGroup buttonGroup = new ButtonGroup();

        /** The year spinner. */
        private JSpinner yearSpinner;

        /** The month spinner. */
        private JSpinner monthSpinner;

        /** The day spinner. */
        private JSpinner daySpinner;

        /** The date checkbox. */
        private JCheckBox dateCheckbox;

        /** The time checkbox. */
        private JCheckBox timeCheckbox;

        /** The hour spinner. */
        private JSpinner hourSpinner;

        /** The minute spinner. */
        private JSpinner minuteSpinner;

        /** The second spinner. */
        private JSpinner secondSpinner;

        /** The duration radio button. */
        private JRadioButton durationRadioButton;

        /** The date radio button. */
        private JRadioButton dateRadioButton;

        /** The panel. */
        private JPanel panel;
    }

    /** The start. */
    private TimePeriodPanel start = new TimePeriodPanel();

    /** The end. */
    private TimePeriodPanel end = new TimePeriodPanel();

    /**
     * Instantiates a new field config time period.
     *
     * @param panelId the panel id
     * @param id the id
     * @param valueOnly the value only
     */
    public FieldConfigTimePeriod(Class<?> panelId, FieldId id, boolean valueOnly) {
        super(panelId, id, null, valueOnly);
    }

    /**
     * Creates the ui.
     *
     * @param parentBox the parent box
     */
    @Override
    public void createUI(Box parentBox) {

        FieldPanel fieldPanel = createFieldPanel(getXPos(), getLabel(), parentBox);

        createUIPanel(fieldPanel, start, 0, Localisation.getString(FieldConfigBase.class, "FieldConfigTimePeriod.from"));
        createUIPanel(fieldPanel, end, 1, Localisation.getString(FieldConfigBase.class, "FieldConfigTimePeriod.to"));

        Dimension preferredSize = new Dimension((int)fieldPanel.getPreferredSize().getWidth(), end.panel.getY() + end.panel.getHeight());

        fieldPanel.setPreferredSize(preferredSize);
        if(!isValueOnly())
        {
            setAttributeSelectionPanel(fieldPanel.internalCreateAttrButton(Double.class, this));
        }
    }

    /**
     * Gets the row y.
     *
     * @param row the row
     * @return the row y
     */
    private int getRowY(int row)
    {
        return (row * BasePanel.WIDGET_HEIGHT) + 13;
    }

    /**
     * Creates the ui panel.
     *
     * @param fieldPanel the field panel
     * @param panelData the panel
     * @param index the index
     * @param title the title
     * @return the int
     */
    private void createUIPanel(FieldPanel fieldPanel, TimePeriodPanel panelData, int index, String title)
    {
        final UndoActionInterface parentObj = this;

        int row = 0;
        panelData.panel = new JPanel();
        panelData.panel.setLayout(null);

        int xPos = getXPos();

        panelData.dateRadioButton = new JRadioButton();
        panelData.dateRadioButton.setBounds(2, getRowY(row), 20, BasePanel.WIDGET_HEIGHT);
        panelData.dateRadioButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                updateFields(panelData);
            }});
        panelData.panel.add(panelData.dateRadioButton);
        panelData.buttonGroup.add(panelData.dateRadioButton);

        JDatePanelImpl datePanel = new JDatePanelImpl(panelData.dateModel);
        panelData.datePicker = new JDatePickerImpl(datePanel);
        panelData.datePicker.setBounds(xPos + BasePanel.WIDGET_X_START, getRowY(row), BasePanel.WIDGET_STANDARD_WIDTH, BasePanel.WIDGET_HEIGHT);
        panelData.panel.add(panelData.datePicker);

        panelData.dateModel.addChangeListener(new ChangeListener() {

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
        panelData.timePicker = new JSpinner(model);
        panelData.timePicker.setBounds(panelData.datePicker.getX() + panelData.datePicker.getWidth() + 10, getRowY(row), (int)(BasePanel.WIDGET_STANDARD_WIDTH * 0.75), BasePanel.WIDGET_HEIGHT);
        panelData.timePicker.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                valueUpdated();
            }});
        panelData.panel.add(panelData.timePicker);
        panelData.timeEditor = new JSpinner.DateEditor(panelData.timePicker, "HH:mm:ss");
        panelData.timePicker.setEditor(panelData.timeEditor);
        DateFormatter formatter = (DateFormatter)panelData.timeEditor.getTextField().getFormatter();
        formatter.setAllowsInvalid(false); 
        formatter.setOverwriteMode(true);

        //
        // Date
        //
        row ++;
        int spinnerWidth = (int)(BasePanel.WIDGET_STANDARD_WIDTH * 0.4);

        panelData.durationRadioButton = new JRadioButton();
        panelData.durationRadioButton.setBounds(2, getRowY(row), 20, BasePanel.WIDGET_HEIGHT);
        panelData.durationRadioButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                updateFields(panelData);
            }});

        panelData.panel.add(panelData.durationRadioButton);
        panelData.buttonGroup.add(panelData.durationRadioButton);

        panelData.dateCheckbox = new JCheckBox(Localisation.getString(FieldConfigBase.class, "FieldConfigTimePeriod.date"));
        panelData.dateCheckbox.setBounds(20, getRowY(row), 55, BasePanel.WIDGET_HEIGHT);
        panelData.dateCheckbox.setSelected(true);
        panelData.dateCheckbox.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                boolean enabled = panelData.dateCheckbox.isSelected();
                panelData.yearSpinner.setEnabled(enabled);
                panelData.monthSpinner.setEnabled(enabled);
                panelData.daySpinner.setEnabled(enabled);
            }});
        panelData.panel.add(panelData.dateCheckbox);

        int x = xPos + BasePanel.WIDGET_X_START - 10;

        // Year
        JLabel yearLbl = new JLabel(Localisation.getField(FieldConfigBase.class, "FieldConfigTimePeriod.year"));
        yearLbl.setHorizontalAlignment(SwingConstants.LEADING);
        yearLbl.setBounds(x, getRowY(row), 40, BasePanel.WIDGET_HEIGHT);
        panelData.panel.add(yearLbl);

        panelData.yearSpinner = new JSpinner();
        x = x + 40;
        panelData.yearSpinner.setBounds(x, getRowY(row), spinnerWidth, BasePanel.WIDGET_HEIGHT);
        panelData.panel.add(panelData.yearSpinner);
        panelData.yearSpinner.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                valueUpdated();
            }});

        // Month
        x = x + spinnerWidth + 2;
        JLabel monthLbl = new JLabel(Localisation.getField(FieldConfigBase.class, "FieldConfigTimePeriod.month"));
        monthLbl.setHorizontalAlignment(SwingConstants.LEADING);
        monthLbl.setBounds(x, getRowY(row), 40, BasePanel.WIDGET_HEIGHT);
        panelData.panel.add(monthLbl);

        x = x + 40;
        panelData.monthSpinner = new JSpinner();
        panelData.monthSpinner.setBounds(x, getRowY(row), spinnerWidth, BasePanel.WIDGET_HEIGHT);
        panelData.panel.add(panelData.monthSpinner);
        panelData.monthSpinner.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                valueUpdated();
            }});

        // Day
        x = x + spinnerWidth + 2;
        JLabel dayLbl = new JLabel(Localisation.getField(FieldConfigBase.class, "FieldConfigTimePeriod.day"));
        dayLbl.setHorizontalAlignment(SwingConstants.LEADING);
        dayLbl.setBounds(x, getRowY(row), 40, BasePanel.WIDGET_HEIGHT);
        panelData.panel.add(dayLbl);

        x = x + 40;
        panelData.daySpinner = new JSpinner();
        panelData.daySpinner.setBounds(x, getRowY(row), spinnerWidth, BasePanel.WIDGET_HEIGHT);
        panelData.panel.add(panelData.daySpinner);
        panelData.daySpinner.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                valueUpdated();
            }});

        //
        // Time
        //
        row ++;
        panelData.timeCheckbox = new JCheckBox(Localisation.getString(FieldConfigBase.class, "FieldConfigTimePeriod.time"));
        panelData.timeCheckbox.setBounds(20, getRowY(row), 55, BasePanel.WIDGET_HEIGHT);
        panelData.timeCheckbox.setSelected(true);
        panelData.timeCheckbox.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                boolean enabled = panelData.timeCheckbox.isSelected();
                panelData.hourSpinner.setEnabled(enabled);
                panelData.minuteSpinner.setEnabled(enabled);
                panelData.secondSpinner.setEnabled(enabled);
            }});
        panelData.panel.add(panelData.timeCheckbox);

        x = xPos + BasePanel.WIDGET_X_START - 10;

        // Hours
        JLabel hoursLbl = new JLabel(Localisation.getField(FieldConfigBase.class, "FieldConfigTimePeriod.hours"));
        hoursLbl.setHorizontalAlignment(SwingConstants.LEADING);
        hoursLbl.setBounds(x, getRowY(row), 40, BasePanel.WIDGET_HEIGHT);
        panelData.panel.add(hoursLbl);

        panelData.hourSpinner = new JSpinner();
        x = x + 40;
        panelData.hourSpinner.setBounds(x, getRowY(row), spinnerWidth, BasePanel.WIDGET_HEIGHT);
        panelData.hourSpinner.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                valueUpdated();
            }});
        panelData.panel.add(panelData.hourSpinner);

        // Minutes
        x = x + spinnerWidth + 2;
        JLabel minuteLbl = new JLabel(Localisation.getField(FieldConfigBase.class, "FieldConfigTimePeriod.minutes"));
        minuteLbl.setHorizontalAlignment(SwingConstants.LEADING);
        minuteLbl.setBounds(x, getRowY(row), 40, BasePanel.WIDGET_HEIGHT);
        panelData.panel.add(minuteLbl);

        x = x + 40;
        panelData.minuteSpinner = new JSpinner();
        panelData.minuteSpinner.setBounds(x, getRowY(row), spinnerWidth, BasePanel.WIDGET_HEIGHT);
        panelData.minuteSpinner.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                valueUpdated();
            }});
        panelData.panel.add(panelData.minuteSpinner);

        // Seconds
        x = x + spinnerWidth + 2;
        JLabel secondLabel = new JLabel(Localisation.getField(FieldConfigBase.class, "FieldConfigTimePeriod.seconds"));
        secondLabel.setHorizontalAlignment(SwingConstants.LEADING);
        secondLabel.setBounds(x, getRowY(row), 40, BasePanel.WIDGET_HEIGHT);
        panelData.panel.add(secondLabel);

        x = x + 40;
        panelData.secondSpinner = new JSpinner();
        panelData.secondSpinner.setBounds(x, getRowY(row), spinnerWidth, BasePanel.WIDGET_HEIGHT);
        panelData.secondSpinner.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                valueUpdated();
            }});
        panelData.panel.add(panelData.secondSpinner);

        fieldPanel.add(panelData.panel);

        panelData.panel.setBounds(0, index * ((row * BasePanel.WIDGET_HEIGHT) + 50), BasePanel.FIELD_PANEL_WIDTH, (row + 2) * BasePanel.WIDGET_HEIGHT);

        Dimension preferredSize = new Dimension((int)panelData.panel.getPreferredSize().getWidth(), (row + 2) * BasePanel.WIDGET_HEIGHT);

        panelData.panel.setPreferredSize(preferredSize);

        panelData.panel.setBorder(BorderFactory.createTitledBorder(title));
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
        if(start.datePicker != null)
        {
            start.datePicker.setEnabled(enabled);
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
        Expression expression = getFilterFactory().literal(getStringValue());

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
            if(start.datePicker != null)
            {
                return start.datePicker.isEnabled();
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
        TimePeriod timePeriod = new TimePeriod();

        populateField(timePeriod);
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
        TimePeriod timePeriod = new TimePeriod();

        if(objValue instanceof String)
        {
            timePeriod.decode((String) objValue);
        }
        else if(objValue instanceof DefaultPeriod)
        {
            timePeriod.decode((DefaultPeriod) objValue);
        }

        populateField(timePeriod);
    }

    /**
     * Gets the string value.
     *
     * @return the string value
     */
    @Override
    public String getStringValue()
    {
        TimePeriod timePeriod = new TimePeriod();
        timePeriod.setStart(getDuration(start));
        timePeriod.setEnd(getDuration(end));

        return timePeriod.getString();
    }

    /**
     * Gets the duration.
     *
     * @param panel the panel
     * @return the duration
     */
    private Duration getDuration(TimePeriodPanel panel) {
        Duration duration = new Duration();

        if(panel.dateRadioButton.isSelected())
        {
            duration.setDate(getDate(panel));
        }
        else
        {
            int years = -1;
            int months = -1;
            int days = -1;
            int hours = -1;
            int minutes = -1;
            int seconds = -1;

            if(panel.dateCheckbox.isSelected())
            {
                years = (int) panel.yearSpinner.getValue();
                months = (int) panel.monthSpinner.getValue();
                days = (int) panel.daySpinner.getValue();
            }

            if(panel.timeCheckbox.isSelected())
            {
                hours = (int) panel.hourSpinner.getValue();
                minutes = (int) panel.minuteSpinner.getValue();
                seconds = (int) panel.secondSpinner.getValue();
            }
            duration.setDuration(years, months, days, hours, minutes, seconds);
        }
        return duration;
    }

    /**
     * Gets the date.
     *
     * @param panel the panel
     * @return the date
     */
    private Date getDate(TimePeriodPanel panel)
    {
        Date selectedDate = (Date) panel.datePicker.getModel().getValue();

        Date time = (Date) panel.timePicker.getValue();

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
        if(start.datePicker != null)
        {
            Date oldValue = (Date)undoRedoObject.getOldValue();

            start.dateModel.setValue(oldValue);
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
        if(start.datePicker != null)
        {
            Date newValue = (Date)undoRedoObject.getNewValue();

            start.dateModel.setValue(newValue);
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
    public void populateField(TimePeriod value) {
        populateDuration(start, value.getStart());
        populateDuration(end, value.getEnd());

        UndoManager.getInstance().addUndoEvent(new UndoEvent(this, getFieldId(), oldValueObj, value));
        oldValueObj = value;
        valueUpdated();
    }

    /**
     * Populate duration.
     *
     * @param timePeriodPanel the time period panel
     * @param duration the duration
     */
    private void populateDuration(TimePeriodPanel timePeriodPanel, Duration duration) {
        if(timePeriodPanel.dateModel != null)
        {
            if(duration.isDate())
            {
                if(timePeriodPanel.dateRadioButton != null)
                {
                    timePeriodPanel.dateRadioButton.setSelected(true);
                }
                if(timePeriodPanel.dateModel != null)
                {
                    timePeriodPanel.dateModel.setValue(duration.getDate());
                }
                if(timePeriodPanel.timePicker != null)
                {
                    timePeriodPanel.timePicker.setValue(duration.getDate());
                }
            }
            else
            {
                if(timePeriodPanel.durationRadioButton != null)
                {
                    timePeriodPanel.durationRadioButton.setSelected(true);
                }
                if(timePeriodPanel.yearSpinner != null)
                {
                    timePeriodPanel.yearSpinner.setValue(duration.getDurationYears());
                }
                if(timePeriodPanel.monthSpinner != null)
                {
                    timePeriodPanel.monthSpinner.setValue(duration.getDurationMonths());
                }
                if(timePeriodPanel.daySpinner != null)
                {
                    timePeriodPanel.daySpinner.setValue(duration.getDurationDays());
                }
                if(timePeriodPanel.hourSpinner != null)
                {
                    timePeriodPanel.hourSpinner.setValue(duration.getDurationHours());
                }
                if(timePeriodPanel.minuteSpinner != null)
                {
                    timePeriodPanel.minuteSpinner.setValue(duration.getDurationMinutes());
                }
                if(timePeriodPanel.secondSpinner != null)
                {
                    timePeriodPanel.secondSpinner.setValue(duration.getDurationSeconds());
                }
            }

            updateFields(timePeriodPanel);
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
        FieldConfigTimePeriod copy = new FieldConfigTimePeriod(fieldConfigBase.getPanelId(),
                fieldConfigBase.getFieldId(),
                fieldConfigBase.isValueOnly());
        return copy;
    }

    /**
     * Gets the class type supported.
     *
     * @return the class type
     */
    @Override
    public Class<?> getClassType() {
        return Period.class;
    }

    /**
     * Sets the field visible.
     *
     * @param visible the new visible state
     */
    @Override
    public void setVisible(boolean visible) {
        if(start.datePicker != null)
        {
            start.datePicker.setVisible(visible);
        }
        if(end.datePicker != null)
        {
            end.datePicker.setVisible(visible);
        }
    }

    /**
     * Update fields.
     *
     * @param panel the panel
     */
    public void updateFields(TimePeriodPanel panel) {
        if(panel.dateRadioButton == null)
        {
            return;
        }
        boolean dateEnabled = panel.dateRadioButton.isSelected();

        panel.datePicker.setEnabled(dateEnabled);
        panel.timePicker.setEnabled(dateEnabled);

        if(panel.durationRadioButton.isSelected())
        {
            panel.dateCheckbox.setEnabled(true);

            boolean durationDateEnabled = panel.dateCheckbox.isSelected();
            panel.yearSpinner.setEnabled(durationDateEnabled);
            panel.monthSpinner.setEnabled(durationDateEnabled);
            panel.daySpinner.setEnabled(durationDateEnabled);

            panel.timeCheckbox.setEnabled(true);

            boolean durationTimeEnabled = panel.timeCheckbox.isSelected();
            panel.hourSpinner.setEnabled(durationTimeEnabled);
            panel.minuteSpinner.setEnabled(durationTimeEnabled);
            panel.secondSpinner.setEnabled(durationTimeEnabled);
        }
        else
        {
            panel.dateCheckbox.setEnabled(false);
            panel.yearSpinner.setEnabled(false);
            panel.monthSpinner.setEnabled(false);
            panel.daySpinner.setEnabled(false);

            panel.timeCheckbox.setEnabled(false);
            panel.hourSpinner.setEnabled(false);
            panel.minuteSpinner.setEnabled(false);
            panel.secondSpinner.setEnabled(false);
        }
    }
}
