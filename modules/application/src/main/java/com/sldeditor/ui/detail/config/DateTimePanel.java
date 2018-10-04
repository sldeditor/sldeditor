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

import com.github.lgooddatepicker.components.DatePickerSettings;
import com.github.lgooddatepicker.components.DateTimePicker;
import com.github.lgooddatepicker.components.TimePickerSettings;
import com.github.lgooddatepicker.optionalusertools.DateTimeChangeListener;
import com.github.lgooddatepicker.optionalusertools.PickerUtilities;
import com.github.lgooddatepicker.zinternaltools.DateTimeChangeEvent;
import com.sldeditor.filter.v2.function.temporal.DateUtils;
import com.sldeditor.ui.detail.BasePanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * The Class DateTimePanel wraps a date picker GUI component.
 *
 * @author Robert Ward (SCISYS)
 */
public class DateTimePanel {

    /** */
    private static final int DATEPICKER_WIDTH = 300;

    /** The Constant OFFSET_SIGN_WIDTH. */
    private static final int OFFSET_SIGN_WIDTH = 60;

    /** The Constant OFFSET_HOURS_WIDTH. */
    private static final int OFFSET_HOURS_WIDTH = 50;

    /** The Constant OFFSET_MINUTES_WIDTH. */
    private static final int OFFSET_MINUTES_WIDTH = 60;

    /** The date/time picker. */
    private DateTimePicker dateTimePicker;

    /** The date model. */
    private DatePickerSettings dateModel = new DatePickerSettings();

    /** The time model. */
    private TimePickerSettings timeModel = new TimePickerSettings();

    /** The offset sign. */
    private JComboBox<String> offsetSign;

    /** The hour spinner. */
    private JSpinner hourSpinner;

    /** The minute spinner. */
    private JSpinner minuteSpinner;

    /** The callback. */
    private DateTimePanelUpdateInterface callback;

    /** The offset strings. */
    private String[] offsetStrings = {"+", "-"};

    /**
     * Instantiates a new date time panel.
     *
     * @param parent the parent
     */
    public DateTimePanel(DateTimePanelUpdateInterface parent) {
        callback = parent;
    }

    /**
     * Creates the UI.
     *
     * @param xPos the x pos
     * @param indentWidth the indent width
     * @param yOffset the y offset
     * @param fieldPanel the field panel
     */
    public void createUI(int xPos, int indentWidth, int yOffset, JPanel fieldPanel) {
        if (dateTimePicker == null) {

            dateModel = new DatePickerSettings();
            dateModel.setAllowEmptyDates(false);
            dateModel.setFormatForDatesCommonEra(DateTimeFormatter.ofPattern("dd MMM yyyy"));
            timeModel = new TimePickerSettings();
            timeModel.setAllowEmptyTimes(false);
            timeModel.setDisplaySpinnerButtons(true);
            timeModel.setDisplayToggleTimeMenuButton(false);
            timeModel.setFormatForDisplayTime(
                    PickerUtilities.createFormatterFromPatternString(
                            "HH:mm:ss.SSS", timeModel.getLocale()));

            dateTimePicker = new DateTimePicker(dateModel, timeModel);

            int x = xPos + indentWidth;
            dateTimePicker.setBounds(x, yOffset, DATEPICKER_WIDTH, BasePanel.WIDGET_HEIGHT);
            fieldPanel.add(dateTimePicker);

            offsetSign = new JComboBox<String>(offsetStrings);
            int y = BasePanel.WIDGET_HEIGHT + yOffset;
            offsetSign.setBounds(x, y, OFFSET_SIGN_WIDTH, BasePanel.WIDGET_HEIGHT);
            offsetSign.addActionListener(
                    new ActionListener() {

                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if (callback != null) {
                                callback.dateTimeValueUpdated();
                            }
                        }
                    });
            fieldPanel.add(offsetSign);

            SpinnerNumberModel hourModel = new SpinnerNumberModel();
            hourModel.setStepSize(1);
            hourModel.setMinimum(0);
            hourModel.setMaximum(23);
            hourSpinner = new JSpinner(hourModel);
            hourSpinner.addChangeListener(
                    new ChangeListener() {

                        @Override
                        public void stateChanged(ChangeEvent e) {
                            if (callback != null) {
                                callback.dateTimeValueUpdated();
                            }
                        }
                    });

            fieldPanel.add(hourSpinner);

            x += OFFSET_SIGN_WIDTH + 2;
            hourSpinner.setBounds(x, y, OFFSET_HOURS_WIDTH, BasePanel.WIDGET_HEIGHT);

            SpinnerNumberModel secondModel = new SpinnerNumberModel();
            secondModel.setStepSize(1);
            secondModel.setMinimum(0);
            secondModel.setMaximum(59);
            minuteSpinner = new JSpinner(secondModel);
            fieldPanel.add(minuteSpinner);
            minuteSpinner.addChangeListener(
                    new ChangeListener() {

                        @Override
                        public void stateChanged(ChangeEvent e) {
                            if (callback != null) {
                                callback.dateTimeValueUpdated();
                            }
                        }
                    });

            x += OFFSET_HOURS_WIDTH + 2;
            minuteSpinner.setBounds(x, y, OFFSET_MINUTES_WIDTH, BasePanel.WIDGET_HEIGHT);

            dateTimePicker.addDateTimeChangeListener(
                    new DateTimeChangeListener() {
                        @Override
                        public void dateOrTimeChanged(DateTimeChangeEvent event) {
                            if (callback != null) {
                                callback.dateTimeValueUpdated();
                            }
                        }
                    });
        }
    }

    /**
     * Sets the enabled.
     *
     * @param enabled the new enabled
     */
    public void setEnabled(boolean enabled) {
        if (dateTimePicker != null) {
            dateTimePicker.setEnabled(enabled);
            hourSpinner.setEnabled(enabled);
            minuteSpinner.setEnabled(enabled);
            offsetSign.setEnabled(enabled);
        }
    }

    /**
     * Checks if is enabled.
     *
     * @return true, if is enabled
     */
    public boolean isEnabled() {
        if (dateTimePicker != null) {
            return dateTimePicker.isEnabled();
        }
        return false;
    }

    /**
     * Gets the string value.
     *
     * @return the string value
     */
    public String getStringValue() {
        ZonedDateTime date = getDate();
        if (date == null) {
            return null;
        }

        return DateUtils.getString(date);
    }

    /**
     * Gets the date.
     *
     * @return the date
     */
    public ZonedDateTime getDate() {
        if (dateTimePicker == null) {
            return null;
        }
        LocalDateTime localDateTime = dateTimePicker.getDateTimeStrict();

        if (localDateTime == null) {
            return null;
        }

        // Populate zone offset
        String offsetId =
                String.format(
                        "%s%02d:%02d",
                        (offsetSign.getSelectedIndex() == 0 ? offsetStrings[0] : offsetStrings[1]),
                        hourSpinner.getValue(),
                        minuteSpinner.getValue());

        ZonedDateTime newDateTime = localDateTime.atZone(ZoneOffset.of(offsetId));

        return newDateTime;
    }

    /**
     * Populate UI.
     *
     * @param value the value
     */
    public void populateUI(ZonedDateTime value) {

        dateTimePicker.setDateTimeStrict(value.toLocalDateTime());

        // Populate zone offset
        String zone = value.getOffset().getId();

        int index = 0;
        Integer hour = 0;
        Integer minute = 0;

        if (zone.length() > 0) {
            index = (zone.charAt(0) == '-') ? 1 : 0;

            String[] components = zone.substring(1).split(":");

            if (components.length == 2) {
                hour = Integer.valueOf(components[0]);
                minute = Integer.valueOf(components[1]);
            }
        }
        offsetSign.setSelectedIndex(index);
        hourSpinner.setValue(hour);
        minuteSpinner.setValue(minute);
    }

    /**
     * Sets the field visible.
     *
     * @param visible the new visible state
     */
    public void setVisible(boolean visible) {
        if (dateTimePicker != null) {
            dateTimePicker.setVisible(visible);
        }
    }
}
