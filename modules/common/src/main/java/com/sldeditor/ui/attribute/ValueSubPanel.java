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
package com.sldeditor.ui.attribute;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JTextField;

import org.geotools.filter.LiteralExpressionImpl;
import org.opengis.filter.expression.Expression;

/**
 * Panel that allows the user to configure values for an SLD attribute.
 * 
 * @author Robert Ward (SCISYS)
 */
public class ValueSubPanel extends JPanel {

    /** The Constant VALUE_PANEL. */
    private static final String VALUE_PANEL = "Value";

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The value text field. */
    private JTextField valueTextField;

    /**
     * Gets the panel name.
     *
     * @return the panel name
     */
    public static String getPanelName()
    {
        return VALUE_PANEL;
    }

    /**
     * Instantiates a new value panel.
     *
     * @param addValueField the add value field
     */
    public ValueSubPanel(boolean addValueField)
    {
        setLayout(new BorderLayout(0, 0));

        if(addValueField)
        {
            valueTextField = new JTextField();
            add(valueTextField, BorderLayout.CENTER);
        }
    }

    /**
     * Populate expression.
     *
     * @param expression the expression
     */
    public void populateExpression(Expression expression) {
        if(valueTextField != null)
        {
            valueTextField.setText(expression.toString());
        }
    }

    /**
     * Sets the panel enabled.
     *
     * @param enabled the new panel enabled
     */
    public void setPanelEnabled(boolean enabled) {
        if(valueTextField != null)
        {
            valueTextField.setEnabled(enabled);
        }
    }

    /**
     * Gets the expression.
     *
     * @return the expression
     */
    public Expression getExpression() {
        Expression expression = null;

        if(valueTextField != null)
        {
            expression = new LiteralExpressionImpl(valueTextField.getText());
        }

        return expression;
    }
}
