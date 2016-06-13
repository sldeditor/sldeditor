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
package com.sldeditor.ui.detail.config.colourmap;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.util.EventObject;

import javax.swing.DefaultCellEditor;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;

/**
 * The Class SpinnerEditor.
 *
 * @author Robert Ward (SCISYS)
 */
public class SpinnerEditor extends DefaultCellEditor
{
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The spinner. */
    JSpinner spinner;

    /** The model. */
    SpinnerNumberModel model;

    /** The editor. */
    JSpinner.NumberEditor editor;

    /** The text field. */
    JTextField textField;

    /** The value set. */
    boolean valueSet;

    /**
     * Instantiates a new spinner editor.
     */
    public SpinnerEditor()
    {
        super(new JTextField());
        spinner = new JSpinner();
        editor = new JSpinner.NumberEditor(spinner);

        configure();
    }

    /**
     * Instantiates a new spinner editor.
     *
     * @param minValue the min value
     * @param maxValue the max value
     * @param stepSize the step size
     */
    public SpinnerEditor(double minValue, double maxValue, double stepSize) {
        super(new JTextField());
        model = new SpinnerNumberModel(0.0, minValue, maxValue, stepSize);
        spinner = new JSpinner(model);
        editor = new JSpinner.NumberEditor(spinner);
        
        configure();
    }

    /**
     * 
     */
    private void configure() {
        textField = editor.getTextField();
        textField.addFocusListener( new FocusListener() {
            public void focusGained( FocusEvent fe ) {
                SwingUtilities.invokeLater( new Runnable() {
                    public void run() {
                        if ( valueSet ) {
                            textField.setCaretPosition(1);
                        }
                    }
                });
            }
            public void focusLost( FocusEvent fe ) {
            }
        });
        textField.addActionListener( new ActionListener() {
            public void actionPerformed( ActionEvent ae ) {
                stopCellEditing();
            }
        });
    }

    /**
     * Gets the table cell editor component.
     *
     * @param table the table
     * @param value the value
     * @param isSelected the is selected
     * @param row the row
     * @param column the column
     * @return the table cell editor component
     */
    // Prepares the spinner component and returns it.
    public Component getTableCellEditorComponent(
            JTable table, Object value, boolean isSelected, int row, int column
            ) {
        if ( !valueSet ) {
            spinner.setValue(value);
        }
        SwingUtilities.invokeLater( new Runnable() {
            public void run() {
                textField.requestFocus();
            }
        });
        return spinner;
    }

    /**
     * Checks if is cell editable.
     *
     * @param eo the eo
     * @return true, if is cell editable
     */
    public boolean isCellEditable( EventObject eo ) {
        if ( eo instanceof KeyEvent ) {
            KeyEvent ke = (KeyEvent)eo;
            textField.setText(String.valueOf(ke.getKeyChar()));
            valueSet = true;
        } else {
            valueSet = false;
        }
        return true;
    }

    /**
     * Gets the cell editor value.
     *
     * @return the cell editor value
     */
    // Returns the spinners current value.
    public Object getCellEditorValue() {
        return spinner.getValue();
    }

    /**
     * Stop cell editing.
     *
     * @return true, if successful
     */
    public boolean stopCellEditing() {
        try {
            editor.commitEdit();
            spinner.commitEdit();
        } catch ( java.text.ParseException e ) {
        }
        return super.stopCellEditing();
    }
}