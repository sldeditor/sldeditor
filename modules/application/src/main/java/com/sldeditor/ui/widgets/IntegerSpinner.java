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
import java.util.ArrayList;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.DefaultFormatter;

/**
 * Component that extends a JSpinner, allows only integers and is configured from an xml file.
 *
 * @author Robert Ward (SCISYS)
 */
public class IntegerSpinner extends JSpinner {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The observers. */
    private ArrayList<SpinnerNotifyInterface> observers = new ArrayList<>();

    /** The minimum value is zero flag. */
    private boolean minIsZero = false;

    /** Instantiates a new value spinner. */
    public IntegerSpinner(int min, int max, int stepSize) {
        SpinnerNumberModel model = new SpinnerNumberModel(min, min, max, stepSize);
        setModel(model);

        JComponent comp = getEditor();
        final JFormattedTextField field = (JFormattedTextField) comp.getComponent(0);
        DefaultFormatter formatter = (DefaultFormatter) field.getFormatter();
        formatter.setCommitsOnValidEdit(true);
        addChangeListener(
                new ChangeListener() {
                    private double oldValue = Double.MAX_VALUE;

                    @Override
                    public void stateChanged(ChangeEvent e) {

                        Double doubleValue = IntegerSpinner.this.getDoubleValue();

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
    public IntegerSpinner(SpinnerModel model) {
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
     * Checks if is min is zero.
     *
     * @return true, if is min is zero
     */
    public boolean isMinIsZero() {
        return minIsZero;
    }

    /**
     * Sets the min is zero.
     *
     * @param minIsZero the new min is zero
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
