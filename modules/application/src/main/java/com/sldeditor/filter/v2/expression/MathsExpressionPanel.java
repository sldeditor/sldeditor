/*
 * SLD Editor - The Open Source Java SLD Editor
 *
 * Copyright (C) 2018, SCISYS UK Limited
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

package com.sldeditor.filter.v2.expression;

import com.sldeditor.ui.attribute.SubPanelUpdatedInterface;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.geotools.filter.expression.AddImpl;
import org.geotools.filter.expression.DivideImpl;
import org.geotools.filter.expression.MultiplyImpl;
import org.geotools.filter.expression.SubtractImpl;
import org.opengis.filter.expression.Add;
import org.opengis.filter.expression.Divide;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Multiply;
import org.opengis.filter.expression.Subtract;

/**
 * Panel to be able to select maths expressions.
 *
 * @author Robert Ward (SCISYS)
 */
public class MathsExpressionPanel extends JPanel {

    /** The Constant DIVIDE. */
    private static final String DIVIDE = "Divide";

    /** The Constant MULTIPLY. */
    private static final String MULTIPLY = "Multiply";

    /** The Constant SUBTRACT. */
    private static final String SUBTRACT = "Subtract";

    /** The Constant ADD. */
    private static final String ADD = "Add";

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The attribute combo box. */
    private JComboBox<String> comboBox = new JComboBox<>();

    /** The data model. */
    private DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();

    /** The label : data type. */
    private JLabel lblDataType;

    /**
     * Instantiates a new maths expression panel.
     *
     * @param parentObj the parent obj
     */
    public MathsExpressionPanel(SubPanelUpdatedInterface parentObj) {

        setLayout(new BorderLayout(5, 0));

        lblDataType = new JLabel();
        add(lblDataType, BorderLayout.WEST);

        add(comboBox, BorderLayout.CENTER);

        populateComboBox();
        comboBox.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {

                        if (parentObj != null) {
                            parentObj.updateSymbol();
                        }
                    }
                });
    }

    /** Populate combo box. */
    private void populateComboBox() {
        if (comboBox != null) {
            // CHECKSTYLE:OFF
            Object selectedItem = model.getSelectedItem();
            // CHECKSTYLE:ON
            model.removeAllElements();
            model.addElement("");
            model.addElement(ADD);
            model.addElement(SUBTRACT);
            model.addElement(MULTIPLY);
            model.addElement(DIVIDE);

            comboBox.setModel(model);
            model.setSelectedItem(selectedItem);
        }
    }

    /**
     * Gets the selected item.
     *
     * @return the selected item
     */
    public String getSelectedItem() {
        return (String) comboBox.getSelectedItem();
    }

    /**
     * Sets the panel enabled.
     *
     * @param enabled the new panel enabled
     */
    public void setPanelEnabled(boolean enabled) {
        comboBox.setEnabled(enabled);
    }

    /**
     * Sets the maths expression.
     *
     * @param expression the new maths expression
     */
    public void setExpression(Expression expression) {
        String selectedItem = null;

        if (expression instanceof Add) {
            selectedItem = ADD;
        } else if (expression instanceof Subtract) {
            selectedItem = SUBTRACT;
        } else if (expression instanceof Multiply) {
            selectedItem = MULTIPLY;
        } else if (expression instanceof Divide) {
            selectedItem = DIVIDE;
        }

        if (selectedItem != null) {
            comboBox.setSelectedItem(selectedItem);
        } else {
            comboBox.setSelectedIndex(-1);
        }
    }

    /**
     * Gets the expression.
     *
     * @return the expression
     */
    public Expression getExpression() {
        Expression expression = null;
        String selectedItem = (String) comboBox.getSelectedItem();

        if (selectedItem != null) {
            if (selectedItem.equals(ADD)) {
                expression = new AddImpl(null, null);
            } else if (selectedItem.equals(SUBTRACT)) {
                expression = new SubtractImpl(null, null);
            } else if (selectedItem.equals(MULTIPLY)) {
                expression = new MultiplyImpl(null, null);
            } else if (selectedItem.equals(DIVIDE)) {
                expression = new DivideImpl(null, null);
            }
        }

        return expression;
    }
}
