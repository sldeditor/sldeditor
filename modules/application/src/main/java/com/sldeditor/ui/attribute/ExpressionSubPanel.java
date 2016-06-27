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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.geotools.filter.text.cql2.CQL;
import org.geotools.filter.text.cql2.CQLException;
import org.opengis.filter.expression.Expression;

import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.common.undo.UndoActionInterface;
import com.sldeditor.common.undo.UndoInterface;
import com.sldeditor.filter.ExpressionPanelFactory;
import com.sldeditor.filter.ExpressionPanelInterface;

/**
 * Panel that allows a user to specify an expression as an SLD attribute.
 * 
 * @author Robert Ward (SCISYS)
 */
public class ExpressionSubPanel extends JPanel implements UndoActionInterface {

    /** The Constant EXPRESSION_PANEL. */
    private static final String EXPRESSION_PANEL = "Expression";

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The btn edit expression. */
    private JButton btnEditExpression;

    /** The expression text field. */
    private JTextField expressionTextField;

    /** The expression being configured. */
    private Expression storedExpression = null;

    /**
     * Gets the panel name.
     *
     * @return the panel name
     */
    public static String getPanelName()
    {
        return EXPRESSION_PANEL;
    }

    /**
     * Instantiates a new expression panel.
     *
     * @param parentObj the parent obj
     * @param expectedDataType the expected data type
     */
    public ExpressionSubPanel(SubPanelUpdatedInterface parentObj, Class<?> expectedDataType)
    {
        setLayout(new BorderLayout(0, 0));

        btnEditExpression = new JButton(Localisation.getString(ExpressionSubPanel.class, "ExpressionSubPanel.edit"));
        expressionTextField = new JTextField();

        btnEditExpression.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                ExpressionPanelInterface expressionPanel = ExpressionPanelFactory.getExpressionPanel(null);

                expressionPanel.configure(Localisation.getString(ExpressionSubPanel.class, "ExpressionSubPanel.dialogTitle"), expectedDataType);

                expressionPanel.populate(storedExpression, null);

                if(expressionPanel.showDialog())
                {
                    Expression expression = expressionPanel.getExpression();
                    
                    populateExpression(expression);

                    if(parentObj != null)
                    {
                        parentObj.updateSymbol();
                    }

//                    String expressionString = expressionPanel.getExpressionString();
//
//                    if(expressionString != null)
//                    {
//                        createExpression(expressionString);
//                    }
                }
            }
        });
        add(btnEditExpression, BorderLayout.WEST);

        add(expressionTextField, BorderLayout.CENTER);
        expressionTextField.setColumns(10);
    }

    /**
     * Gets the expression.
     *
     * @return the expression
     */
    public Expression getExpression() {
        if(storedExpression == null)
        {
            Expression expression = null;
            try {
                expression = CQL.toExpression(expressionTextField.getText());
            } catch (CQLException e) {
           //     ConsoleManager.getInstance().exception(this, e);
            }
            return expression;
        }

        return storedExpression;
    }

    /**
     * Populate expression.
     *
     * @param expression the expression
     */
    public void populateExpression(Expression expression) {
        this.storedExpression = expression;
        String expressionString = "";

        if(expression != null)
        {
            expressionString = expression.toString();
        }
        expressionTextField.setText(expressionString);
    }

    /**
     * Sets the panel enabled.
     *
     * @param enabled the new panel enabled
     */
    public void setPanelEnabled(boolean enabled) {
        btnEditExpression.setEnabled(enabled);
        expressionTextField.setEnabled(enabled);
    }

    /**
     * Undo action.
     *
     * @param undoRedoObject the undo redo object
     */
    @Override
    public void undoAction(UndoInterface undoRedoObject) {
        // Do nothing
    }

    /**
     * Redo action.
     *
     * @param undoRedoObject the undo redo object
     */
    @Override
    public void redoAction(UndoInterface undoRedoObject) {
        // Do nothing
    }
}
