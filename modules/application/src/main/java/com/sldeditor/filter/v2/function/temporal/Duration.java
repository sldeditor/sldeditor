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

package com.sldeditor.filter.v2.function.temporal;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sldeditor.common.console.ConsoleManager;

/**
 * The Class Duration.
 *
 * @author Robert Ward (SCISYS)
 */
public class Duration {

    /** The Constant BEFORE_PREFIX. */
    private static final String DURATION_TIME_PREFIX = "T";

    /** The Constant AFTER_PREFIX. */
    private static final String DURATION_DATE_PREFIX = "P";

    /** The Constant SECOND_SUFFIX. */
    private static final String SECOND_SUFFIX = "S";

    /** The Constant MINUTE_SUFFIX. */
    private static final String MINUTE_SUFFIX = "M";

    /** The Constant HOUR_SUFFIX. */
    private static final String HOUR_SUFFIX = "H";

    /** The Constant DAY_SUFFIX. */
    private static final String DAY_SUFFIX = "D";

    /** The Constant MONTH_SUFFIX. */
    private static final String MONTH_SUFFIX = "M";

    /** The Constant YEAR_SUFFIX. */
    private static final String YEAR_SUFFIX = "Y";

    /** The date format. */
    private DateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);

    /** The time format. */
    private DateFormat tf = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);

    /** The date. */
    private Date date = new Date();

    /** The duration years. */
    private int durationYears = 0;

    /** The duration months. */
    private int durationMonths = 0;

    /** The duration days. */
    private int durationDays = 0;

    /** The duration hours. */
    private int durationHours = 0;

    /** The duration minutes. */
    private int durationMinutes = 0;

    /** The duration seconds. */
    private int durationSeconds = 0;

    /** The is date flag. */
    private boolean isDate = true;

    /** The after flag. */
    private boolean isDurationDate = false;

    /**
     * Instantiates a new duration.
     */
    public Duration() {
    }

    /**
     * Instantiates and decodes a duration.
     *
     * @param string the string
     */
    public Duration(String string) {
        if (string.startsWith(DURATION_DATE_PREFIX)) {
            int year = 0;
            int month = 0;
            int day = 0;
            int hour = 0;
            int minute = 0;
            int second = 0;

            // Assume duration
            List<String> outputList = extractDurationValues(string.substring(1));

            boolean hasTime = (outputList.size() >= 2) && (outputList.get(0).endsWith(YEAR_SUFFIX)
                    && (outputList.get(1).endsWith(HOUR_SUFFIX)
                            || outputList.get(1).endsWith(MINUTE_SUFFIX)
                            || outputList.get(1).endsWith(SECOND_SUFFIX)));

            year = extractValue(outputList, YEAR_SUFFIX);
            month = extractValue(outputList, MONTH_SUFFIX);
            day = extractValue(outputList, DAY_SUFFIX);

            if (hasTime) {
                hour = extractValue(outputList, HOUR_SUFFIX);
                minute = extractValue(outputList, MINUTE_SUFFIX);
                second = extractValue(outputList, SECOND_SUFFIX);
            }

            setDuration(year, month, day, hour, minute, second);
        } else if (string.startsWith(DURATION_TIME_PREFIX)) {
            int year = 0;
            int month = 0;
            int day = 0;
            int hour = 0;
            int minute = 0;
            int second = 0;

            // Assume duration
            List<String> outputList = extractDurationValues(string);

            hour = extractValue(outputList, HOUR_SUFFIX);
            minute = extractValue(outputList, MINUTE_SUFFIX);
            second = extractValue(outputList, SECOND_SUFFIX);

            setDuration(year, month, day, hour, minute, second);
        } else {
            String[] components = string.split(DURATION_TIME_PREFIX);

            try {
                Date date = df.parse((String) components[0]);
                Date time = tf.parse((String) components[1]);
                Calendar cal = Calendar.getInstance();
                cal.setTime(time);

                int hour = cal.get(Calendar.HOUR_OF_DAY);
                int minute = cal.get(Calendar.MINUTE);
                int seconds = cal.get(Calendar.SECOND);

                cal.setTime(date);
                cal.set(Calendar.HOUR, hour);
                cal.set(Calendar.MINUTE, minute);
                cal.set(Calendar.SECOND, seconds);

                setDate(cal.getTime());
            } catch (ParseException e) {
                ConsoleManager.getInstance().exception(this, e);
            }
        }
    }

    /**
     * Extract duration values.
     *
     * @param string the string
     * @return the list
     */
    private List<String> extractDurationValues(String string) {
        List<String> outputList = new ArrayList<String>();
        Matcher match = Pattern.compile("[0-9]+[A-Z]").matcher(string.replace(" ", ""));
        while (match.find()) {
            outputList.add(match.group());
        }
        return outputList;
    }

    /**
     * Extract value.
     *
     * @param outputList the output list
     * @param suffix the suffix
     * @return the int
     */
    private int extractValue(List<String> outputList, String suffix) {
        if (!outputList.isEmpty()) {
            String value = outputList.get(0);
            if (value.endsWith(suffix)) {
                outputList.remove(0);
                return Integer.valueOf(value.substring(0, value.length() - 1)).intValue();
            }
        }
        return 0;
    }

    /**
     * Sets the date.
     *
     * @param date the date to set
     */
    public void setDate(Date date) {
        this.date = date;
        isDate = true;
    }

    /**
     * Sets the duration.
     *
     * @param years the years
     * @param months the months
     * @param days the days
     * @param hours the hours
     * @param minutes the minutes
     * @param seconds the seconds
     */
    public void setDuration(int years, int months, int days, int hours, int minutes, int seconds) {
        isDate = false;
        durationYears = years;
        durationMonths = months;
        durationDays = days;
        durationHours = hours;
        durationMinutes = minutes;
        durationSeconds = seconds;
        isDurationDate = (durationYears > 0) || (durationMonths > 0) || (durationDays > 0);
    }

    /**
     * Gets the string.
     *
     * @return the string
     */
    public String getString() {
        if (isDate) {
            return String.format("%sT%sZ", df.format(date), tf.format(date));
        } else {
            List<String> list = new ArrayList<String>();

            if (durationDays > 0) {
                if (durationYears > 0) {
                    list.add(buildDurationString(durationYears, YEAR_SUFFIX));
                }

                if (durationMonths > 0) {
                    list.add(buildDurationString(durationMonths, MONTH_SUFFIX));
                }

                list.add(buildDurationString(durationDays, DAY_SUFFIX));
            } else if (durationMonths > 0) {
                if (durationYears > 0) {
                    list.add(buildDurationString(durationYears, YEAR_SUFFIX));
                }
                list.add(buildDurationString(durationMonths, MONTH_SUFFIX));
            } else if (durationYears > 0) {
                list.add(buildDurationString(durationYears, YEAR_SUFFIX));
            }

            if (durationSeconds > 0) {
                if (durationHours > 0) {
                    list.add(buildDurationString(durationHours, HOUR_SUFFIX));
                }
                if (durationMinutes > 0) {
                    list.add(buildDurationString(durationMinutes, MINUTE_SUFFIX));
                }
                list.add(buildDurationString(durationSeconds, SECOND_SUFFIX));
            } else if (durationMinutes > 0) {
                if (isDurationDate && (durationYears == 0)) {
                    list.add(buildDurationString(durationYears, YEAR_SUFFIX));
                }
                list.add(buildDurationString(durationHours, HOUR_SUFFIX));
                list.add(buildDurationString(durationMinutes, MINUTE_SUFFIX));
            } else if (durationHours > 0) {
                if (isDurationDate && (durationYears == 0)) {
                    list.add(buildDurationString(durationYears, YEAR_SUFFIX));
                }
                list.add(buildDurationString(durationHours, HOUR_SUFFIX));
            }

            StringBuilder sb = new StringBuilder();

            sb.append(isDurationDate ? DURATION_DATE_PREFIX : DURATION_TIME_PREFIX);
            for (String item : list) {
                sb.append(item);
            }

            return sb.toString();
        }
    }

    /**
     * Builds the duration string.
     *
     * @param value the value
     * @param suffix the suffix
     * @return the string
     */
    private String buildDurationString(int value, String suffix) {
        return String.format("%d%s", value, suffix);
    }

    /**
     * Gets the date.
     *
     * @return the date
     */
    public Date getDate() {
        return date;
    }

    /**
     * Gets the duration years.
     *
     * @return the durationYears
     */
    public int getDurationYears() {
        return durationYears;
    }

    /**
     * Gets the duration months.
     *
     * @return the durationMonths
     */
    public int getDurationMonths() {
        return durationMonths;
    }

    /**
     * Gets the duration days.
     *
     * @return the durationDays
     */
    public int getDurationDays() {
        return durationDays;
    }

    /**
     * Gets the duration hours.
     *
     * @return the durationHours
     */
    public int getDurationHours() {
        return durationHours;
    }

    /**
     * Gets the duration minutes.
     *
     * @return the durationMinutes
     */
    public int getDurationMinutes() {
        return durationMinutes;
    }

    /**
     * Gets the duration seconds.
     *
     * @return the durationSeconds
     */
    public int getDurationSeconds() {
        return durationSeconds;
    }

    /**
     * Checks if is date.
     *
     * @return the isDate
     */
    public boolean isDate() {
        return isDate;
    }

    /**
     * Checks if is duration date.
     *
     * @return true, if is duration date
     */
    public boolean isDurationDate() {
        return isDurationDate;
    }
}
