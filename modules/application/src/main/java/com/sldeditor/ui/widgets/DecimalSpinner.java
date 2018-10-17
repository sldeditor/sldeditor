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

package com.sldeditor.ui.widgets;

import com.sldeditor.ui.iface.SpinnerNotifyInterface;
import java.text.DecimalFormat;
import java.util.ArrayList;
import javax.swing.JFormattedTextField;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.DefaultFormatter;

/**
 * Component that extends a JSpinner allowing the entry of floating point numbers and is configured
 * from an xml file.
 *
 * @author Robert Ward (SCISYS)
 */
public class DecimalSpinner extends JSpinner {

    /** The Constant DEFAULT_NO_OF_DECIMAL_PLACES. */
    private static final int DEFAULT_NO_OF_DECIMAL_PLACES = 3;

    /** The Constant DEFAULT_STEPSIZE. */
    private static final double DEFAULT_STEPSIZE = 0.001;

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The observers. */
    private ArrayList<SpinnerNotifyInterface> observers = new ArrayList<>();

    /** The minimum number is zero. */
    private boolean minIsZero = false;

    /** Instantiates a new decimal spinner with default values. */
    public DecimalSpinner() {
        createUI(0.0, null, null, DEFAULT_STEPSIZE, DEFAULT_NO_OF_DECIMAL_PLACES);
    }

    /**
     * Instantiates a new decimal spinner.
     *
     * @param min the minimum value
     * @param max the maximum value
     * @param stepSize the step size
     * @param noOfDecimalPlaces the number of decimal places
     */
    public DecimalSpinner(double min, double max, double stepSize, double noOfDecimalPlaces) {
        createUI(min, min, max, stepSize, noOfDecimalPlaces);
    }

    /**
     * Creates the ui.
     *
     * @param initialValue the initial value
     * @param min the minimum value
     * @param max the maximum value
     * @param stepSize the step size
     * @param noOfDecimalPlaces the number of decimal places
     */
    private void createUI(
            Double initialValue,
            Double min,
            Double max,
            Double stepSize,
            double noOfDecimalPlaces) {
        SpinnerNumberModel model = new SpinnerNumberModel(initialValue, min, max, stepSize);
        setModel(model);

        JSpinner.NumberEditor editor = (JSpinner.NumberEditor) getEditor();
        DecimalFormat format = editor.getFormat();
        format.setMinimumFractionDigits((int) noOfDecimalPlaces);

        final JFormattedTextField field = editor.getTextField();
        DefaultFormatter formatter = (DefaultFormatter) field.getFormatter();
        formatter.setCommitsOnValidEdit(true);
        addChangeListener(
                new ChangeListener() {
                    private double oldValue = Double.MAX_VALUE;

                    @Override
                    public void stateChanged(ChangeEvent e) {

                        Double doubleValue = DecimalSpinner.this.getDoubleValue();

                        if (doubleValue != oldValue) {
                            double oldValueCopy = oldValue;

                            oldValue = doubleValue;
                            if (minIsZero && (doubleValue < 0.0)) {
                                doubleValue = 0.0;
                                field.setValue(doubleValue);
                            }

                            notifyListeners(oldValueCopy, doubleValue);
                        }
                    }
                });
    }

    /**
     * Instantiates a new value spinner.
     *
     * @param model the model
     */
    public DecimalSpinner(SpinnerModel model) {
        super(model);
    }

    /**
     * Register observer.
     *
     * @param observer the observer
     */
    public void registerObserver(SpinnerNotifyInterface observer) {
        observers.add(observer);
    }

    /**
     * Notify listeners.
     *
     * @param oldValue the old value
     * @param newValue the new value
     */
    public void notifyListeners(double oldValue, double newValue) {

        for (SpinnerNotifyInterface observer : observers) {
            observer.notify(oldValue, newValue);
        }
    }

    /**
     * Checks if is minimum number is zero.
     *
     * @return true, if is minimum number is zero
     */
    public boolean isMinIsZero() {
        return minIsZero;
    }

    /**
     * Sets the minimum number is zero.
     *
     * @param minIsZero the minimum number is zero flag
     */
    public void setMinIsZero(boolean minIsZero) {
        this.minIsZero = minIsZero;
    }

    /**
     * Gets the double value.
     *
     * @return the double value
     */
    public Double getDoubleValue() {
        Double doubleValue = 0.0;
        Object obj = getValue();

        if (obj instanceof Integer) {
            Integer intValue = (Integer) obj;
            doubleValue = (double) intValue;
        }

        if (obj instanceof Double) {
            doubleValue = (Double) obj;
        }
        return doubleValue;
    }
}
