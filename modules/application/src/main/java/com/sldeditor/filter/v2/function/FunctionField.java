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
import javax.swing.JComboBox;
import javax.swing.JPanel;

import org.geotools.filter.FunctionExpression;
import org.geotools.filter.FunctionExpressionImpl;
import org.geotools.filter.FunctionImpl;
import org.opengis.filter.capability.FunctionName;
import org.opengis.filter.expression.Expression;

import com.sldeditor.common.console.ConsoleManager;
import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.common.undo.UndoActionInterface;
import com.sldeditor.common.undo.UndoEvent;
import com.sldeditor.common.undo.UndoInterface;
import com.sldeditor.common.undo.UndoManager;
import com.sldeditor.filter.v2.function.namefilter.FunctionNameFilterAll;
import com.sldeditor.filter.v2.function.namefilter.FunctionNameFilterInterface;
import com.sldeditor.filter.v2.function.namefilter.FunctionNameFilterRaster;
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
        functionComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                String newValueObj = (String) functionComboBox.getSelectedItem();

                UndoManager.getInstance()
                        .addUndoEvent(new UndoEvent(thisObj, "Function", oldValueObj, newValueObj));

                if (parentObj != null) {
                    parentObj.updateSymbol();
                }
            }
        });
    }

    /**
     * Sets the field data types.
     *
     * @param fieldType the new data type
     * @param isRasterSymbol the is raster symbol
     */
    public void setDataType(Class<?> fieldType, boolean isRasterSymbol) {
        functionNameMap.clear();

        List<FunctionNameFilterInterface> functionNameFilterList =
                new ArrayList<FunctionNameFilterInterface>();

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
     */
    public void setFunction(Expression expression) {

        if (expression == null) {
            functionComboBox.setSelectedIndex(-1);
        } else {
            if (expression instanceof FunctionExpressionImpl) {
                FunctionExpressionImpl functionExpression = (FunctionExpressionImpl) expression;
                FunctionName function = functionExpression.getFunctionName();

                String functionName = function.getName();
                oldValueObj = functionName;

                functionComboBox.setSelectedItem(functionName);
            } else {
                ConsoleManager.getInstance().error(this, Localisation.getString(FunctionField.class,
                        "DataSourceAttributePanel.error1"));
            }
        }
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

        if (newExpression instanceof FunctionExpression) {
            FunctionExpression expression = (FunctionExpression) newExpression;

            if (expression != null) {
                List<Expression> params = new ArrayList<Expression>();

                boolean validSymbolFlag = (params.size() == functionName.getArgumentCount());
                if (validSymbolFlag) {
                    expression.setParameters(params);
                }
            }
        } else if (newExpression instanceof FunctionImpl) {
            FunctionImpl expression = (FunctionImpl) newExpression;

            if (expression != null) {
                // List<Expression> params = new ArrayList<Expression>();
                // List<FieldConfigBase> functionFields = field.getFunctionFields();
                // if(functionFields != null)
                // {
                // for(FieldConfigBase functionField : functionFields)
                // {
                // Expression functionFieldExpression = functionField.getExpression();
                //
                // if(functionFieldExpression != null)
                // {
                // params.add(functionFieldExpression);
                // }
                // }
                // }
                //
                // boolean validSymbolFlag = (params.size() == functionName.getArgumentCount());
                // if(validSymbolFlag)
                // {
                // expression.setParameters(params);
                // }
                //
                // String key = String.format("%s@%s", field.getClass().getName(),
                // Integer.toHexString(field.hashCode()));
                // SelectedSymbol.getInstance().setValidSymbol(key, validSymbolFlag);
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
}
