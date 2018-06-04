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

package com.sldeditor.filter.v2.function;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import org.geotools.filter.FunctionExpression;
import org.geotools.filter.FunctionExpressionImpl;
import org.geotools.filter.function.string.ConcatenateFunction;
import org.opengis.filter.capability.FunctionName;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Function;
import org.opengis.parameter.Parameter;

import com.sldeditor.common.console.ConsoleManager;
import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.common.undo.UndoActionInterface;
import com.sldeditor.common.undo.UndoEvent;
import com.sldeditor.common.undo.UndoInterface;
import com.sldeditor.common.undo.UndoManager;
import com.sldeditor.filter.v2.expression.ExpressionNode;
import com.sldeditor.filter.v2.expression.ExpressionPanelv2;
import com.sldeditor.filter.v2.expression.FunctionInterfaceUtils;
import com.sldeditor.filter.v2.function.namefilter.FunctionNameFilterAll;
import com.sldeditor.filter.v2.function.namefilter.FunctionNameFilterInterface;
import com.sldeditor.filter.v2.function.namefilter.FunctionNameFilterRaster;
import com.sldeditor.ui.attribute.DataSourceAttributePanel;
import com.sldeditor.ui.attribute.SubPanelUpdatedInterface;

/**
 * Panel to be able to edit FunctionField objects.
 * 
 * @author Robert Ward (SCISYS)
 */
public class FunctionField extends JPanel implements UndoActionInterface {

    /** The Constant FUNCTION_PANEL. */
    private static final String FUNCTIONFIELD_PANEL = "FunctionField";

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The function combo box. */
    private JComboBox<String> functionComboBox;

    /** The function name map. */
    private Map<String, FunctionName> functionNameMap = new LinkedHashMap<String, FunctionName>();

    /** The old value obj. */
    private Object oldValueObj = null;

    /** The function name mgr. */
    private FunctionNameInterface functionNameMgr = null;

    /** Is edited symbol a raster flag. */
    private boolean isRasterSymbol = false;

    /** The current expression. */
    private Expression currentExpression = null;

    /** The current expression node. */
    private ExpressionNode currentExpressionNode = null;

    /** The add parameter button. */
    private JButton btnAddParameter;

    /** The pre selected function. */
    private boolean preSelectedFunction = false;

    /**
     * Gets the panel name.
     *
     * @return the panel name
     */
    public static String getPanelName() {
        return FUNCTIONFIELD_PANEL;
    }

    /**
     * Instantiates a new data source attribute panel.
     *
     * @param parentObj the parent obj
     * @param functionNameMgr the function name mgr
     */
    public FunctionField(SubPanelUpdatedInterface parentObj,
            FunctionNameInterface functionNameMgr) {
        final UndoActionInterface thisObj = this;
        this.functionNameMgr = functionNameMgr;

        setLayout(new BorderLayout(5, 0));

        functionComboBox = new JComboBox<String>();
        add(functionComboBox, BorderLayout.CENTER);

        JPanel panel = new JPanel();
        add(panel, BorderLayout.SOUTH);

        btnAddParameter = new JButton(
                Localisation.getString(ExpressionPanelv2.class, "FunctionField.addParameter"));
        btnAddParameter.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addParameter();
                if (parentObj != null) {
                    parentObj.parameterAdded();
                }
            }
        });
        panel.add(btnAddParameter);

        functionComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                String newValueObj = (String) functionComboBox.getSelectedItem();

                UndoManager.getInstance()
                        .addUndoEvent(new UndoEvent(thisObj, "Function", oldValueObj, newValueObj));

                FunctionName functionName = functionNameMap.get(newValueObj);
                boolean variableNoOfParameters = false;
                if (functionName != null) {
                    variableNoOfParameters = (functionName.getArgumentCount() < 0);
                }

                btnAddParameter.setVisible(preSelectedFunction && variableNoOfParameters);

                if (parentObj != null) {
                    parentObj.updateSymbol();
                }
            }
        });
    }

    /**
     * Adds a variable parameter.
     */
    private void addParameter() {
        if (currentExpression instanceof FunctionExpression) {
            FunctionExpression functionExpression = (FunctionExpression) currentExpression;
            FunctionName functionName = functionExpression.getFunctionName();

            int argCount = functionName.getArgumentCount();

            if (functionName.getArgumentCount() < 0) {
                argCount *= -1;

                ExpressionNode childNode = new ExpressionNode();
                Parameter<?> parameter = functionName.getArguments().get(argCount - 1);
                childNode.setParameter(parameter);

                Expression newExpression = functionExpression.getParameters().get(argCount - 1);
                childNode.setExpression(newExpression);
                functionExpression.getParameters().add(newExpression);
                currentExpressionNode.insert(childNode, currentExpressionNode.getChildCount());
                currentExpressionNode.setDisplayString();
            }
        } else if (currentExpression instanceof ConcatenateFunction) {
            ConcatenateFunction functionExpression = (ConcatenateFunction) currentExpression;
            FunctionName functionName = functionExpression.getFunctionName();

            int argCount = functionName.getArgumentCount();

            if (functionName.getArgumentCount() < 0) {
                argCount *= -1;
            }
            ExpressionNode childNode = new ExpressionNode();
            Parameter<?> parameter = functionName.getArguments()
                    .get(Math.min(functionName.getArguments().size() - 1, argCount - 1));
            childNode.setType(parameter.getType());
            childNode.setName(parameter.getName());

            Expression newExpression = null;
            childNode.setExpression(newExpression);
            List<Expression> parameters = functionExpression.getParameters();
            parameters.add(newExpression);
            functionExpression.setParameters(parameters);
            currentExpressionNode.insert(childNode, currentExpressionNode.getChildCount());
            currentExpressionNode.setDisplayString();
        }
    }

    /**
     * Sets the field data types.
     *
     * @param fieldType the new data type
     */
    public void setDataType(Class<?> fieldType) {
        functionNameMap.clear();

        List<FunctionNameFilterInterface> functionNameFilterList = new ArrayList<FunctionNameFilterInterface>();

        if (isRasterSymbol) {
            functionNameFilterList.add(new FunctionNameFilterRaster());
        } else {
            functionNameFilterList.add(new FunctionNameFilterAll());
        }

        List<FunctionName> functionNameList = functionNameMgr.getFunctionNameList(fieldType,
                functionNameFilterList);

        for (FunctionName functionName : functionNameList) {
            String functionNameString = functionName.getName();

            functionNameMap.put(functionNameString, functionName);
        }

        populateFunctionComboBox();
    }

    /**
     * Populate function combo box.
     */
    private void populateFunctionComboBox() {
        if (functionComboBox != null) {
            DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String>();

            model.addElement("");

            // Sort function names alphabetically
            List<String> functionNameList = new ArrayList<String>(functionNameMap.keySet());
            java.util.Collections.sort(functionNameList);

            for (String name : functionNameList) {
                model.addElement(name);
            }
            functionComboBox.setModel(model);
        }
    }

    /**
     * Gets the selected item.
     *
     * @return the selected item
     */
    public String getSelectedItem() {
        return (String) functionComboBox.getSelectedItem();
    }

    /**
     * Sets the panel enabled.
     *
     * @param enabled the new panel enabled
     */
    public void setPanelEnabled(boolean enabled) {
        functionComboBox.setEnabled(enabled);
    }

    /**
     * Sets the function.
     *
     * @param expression the new attribute
     * @param node the node
     */
    public void setFunction(Expression expression, ExpressionNode node) {

        currentExpression = expression;
        currentExpressionNode = node;
        preSelectedFunction = true;

        if (expression == null) {
            functionComboBox.setSelectedIndex(-1);
        } else {
            if (expression instanceof FunctionExpressionImpl) {
                FunctionExpressionImpl functionExpression = (FunctionExpressionImpl) expression;
                FunctionName function = functionExpression.getFunctionName();

                String functionName = function.getName();
                oldValueObj = functionName;

                functionComboBox.setSelectedItem(functionName);
            } else if (expression instanceof ConcatenateFunction) {
                ConcatenateFunction concatenateExpression = (ConcatenateFunction) expression;
                FunctionName function = concatenateExpression.getFunctionName();

                String functionName = function.getName();
                oldValueObj = functionName;

                functionComboBox.setSelectedItem(functionName);
            } else if (expression instanceof Function) {
                Function functionExpression = (Function) expression;
                FunctionName function = functionExpression.getFunctionName();

                String functionName = function.getName();
                oldValueObj = functionName;

                functionComboBox.setSelectedItem(functionName);
            } else {
                ConsoleManager.getInstance().error(this, Localisation.getString(
                        DataSourceAttributePanel.class, "DataSourceAttributePanel.error1"));
            }
        }

        preSelectedFunction = false;
    }

    /**
     * Gets the expression.
     *
     * @return the expression
     */
    public Expression getExpression() {
        String functionNameString = (String) functionComboBox.getSelectedItem();

        FunctionName functionName = functionNameMap.get(functionNameString);
        Expression newExpression = functionNameMgr.createExpression(functionName);

        int argCount = functionName.getArgumentCount();
        if (argCount < 0) {
            argCount *= -1;
        }
        if (newExpression instanceof FunctionExpression) {
            FunctionExpression expression = (FunctionExpression) newExpression;

            if (expression != null) {
                List<Expression> params = new ArrayList<Expression>();

                boolean validSymbolFlag = (params.size() == argCount);
                if (validSymbolFlag) {
                    expression.setParameters(params);
                }
            }
        } else if (newExpression instanceof ConcatenateFunction) {
            ConcatenateFunction expression = (ConcatenateFunction) newExpression;

            if (expression != null) {
                List<Expression> params = new ArrayList<Expression>();

                for (int i = 0; i < argCount; i++) {
                    params.add(null);
                }
                boolean validSymbolFlag = (params.size() == argCount);
                if (validSymbolFlag) {
                    expression.setParameters(params);
                }
            }
        } else if (newExpression instanceof Function) {
            Function function = (Function) newExpression;

            List<Expression> params = new ArrayList<Expression>();

            for (Parameter<?> param : functionName.getArguments()) {
                for (int index = 0; index < param.getMinOccurs(); index++) {
                    params.add(null);
                }
            }
            boolean validSymbolFlag = (params.size() == argCount);
            if (validSymbolFlag) {
                Expression retValue = FunctionInterfaceUtils.createNewFunction(newExpression,
                        params);

                if (retValue != null) {
                    return retValue;
                } else {
                    ((FunctionExpression) function).setParameters(params);
                }
            }
        }

        return newExpression;
    }

    /**
     * Undo action.
     *
     * @param undoRedoObject the undo redo object
     */
    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.undo.UndoActionInterface#undoAction(com.sldeditor.undo.UndoInterface)
     */
    @Override
    public void undoAction(UndoInterface undoRedoObject) {
        String oldValueObj = (String) undoRedoObject.getOldValue();

        functionComboBox.setSelectedItem(oldValueObj);
    }

    /**
     * Redo action.
     *
     * @param undoRedoObject the undo redo object
     */
    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.undo.UndoActionInterface#redoAction(com.sldeditor.undo.UndoInterface)
     */
    @Override
    public void redoAction(UndoInterface undoRedoObject) {
        String newValueObj = (String) undoRedoObject.getNewValue();

        functionComboBox.setSelectedItem(newValueObj);
    }

    /**
     * Sets the is edited symbol a raster flag.
     *
     * @param isRasterSymbol the isRasterSymbol to set
     */
    public void setIsRasterSymbol(boolean isRasterSymbol) {
        this.isRasterSymbol = isRasterSymbol;
    }
}
