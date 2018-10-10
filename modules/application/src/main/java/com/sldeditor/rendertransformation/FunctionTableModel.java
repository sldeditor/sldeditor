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

package com.sldeditor.rendertransformation;

import com.sldeditor.common.localisation.Localisation;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import net.opengis.wps10.ProcessBriefType;
import net.opengis.wps10.ProcessDescriptionType;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.FunctionFactory;
import org.geotools.process.function.ProcessFunction;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.capability.FunctionName;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Function;

/**
 * Table model that displays/edits process functions.
 *
 * @author Robert Ward (SCISYS)
 */
public class FunctionTableModel extends AbstractTableModel {

    /**
     * The Constant PARAMETER. @TODO Should be ParameterFunction.NAME.getName() but
     * ParameterFunction is not publicly accessible.
     */
    private static final String PARAMETER = "parameter";

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The Constant COL_PARAMETER. */
    private static final int COL_PARAMETER = 0;

    /** The Constant COL_TYPE. */
    private static final int COL_TYPE = 1;

    /** The Constant COL_OPTIONAL. */
    public static final int COL_OPTIONAL = 2;

    /** The Constant COL_VALUE. */
    private static final int COL_VALUE = 3;

    /** The selected process function. */
    private SelectedProcessFunction selectedFunction = new SelectedProcessFunction();

    /** The column list. */
    private List<String> columnList = new ArrayList<String>();

    /** The value list. */
    private List<ProcessFunctionParameterValue> valueList =
            new ArrayList<ProcessFunctionParameterValue>();

    /** The filter factory. */
    private static FilterFactory ff = CommonFactoryFinder.getFilterFactory();

    /** Instantiates a new function table model. */
    public FunctionTableModel() {
        columnList.add(
                Localisation.getString(FunctionTableModel.class, "FunctionTableModel.parameter"));
        columnList.add(Localisation.getString(FunctionTableModel.class, "FunctionTableModel.type"));
        columnList.add(
                Localisation.getString(FunctionTableModel.class, "FunctionTableModel.optional"));
        columnList.add(
                Localisation.getString(FunctionTableModel.class, "FunctionTableModel.value"));
    }

    /**
     * Gets the row count.
     *
     * @return the row count
     */
    @Override
    public int getRowCount() {
        return valueList.size();
    }

    /**
     * Gets the column count.
     *
     * @return the column count
     */
    @Override
    public int getColumnCount() {
        return columnList.size();
    }

    /**
     * Gets the value at.
     *
     * @param rowIndex the row index
     * @param columnIndex the column index
     * @return the value at
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        ProcessFunctionParameterValue value = valueList.get(rowIndex);

        switch (columnIndex) {
            case COL_PARAMETER:
                return value.name;
            case COL_TYPE:
                return value.dataType;
            case COL_OPTIONAL:
                if (value.optional) {
                    return value.included;
                }
                break;
            case COL_VALUE:
                if (value.objectValue != null) {
                    Expression expression = value.objectValue.getExpression();
                    if (expression != null) {
                        return expression.toString();
                    }
                }
                break;
            default:
                break;
        }
        return null;
    }

    /**
     * Gets the value column.
     *
     * @return the value column
     */
    public static int getValueColumn() {
        return COL_VALUE;
    }

    /**
     * Gets the column name.
     *
     * @param column the column
     * @return the column name
     */
    @Override
    public String getColumnName(int column) {
        return columnList.get(column);
    }

    /**
     * Checks if is cell editable.
     *
     * @param rowIndex the row index
     * @param columnIndex the column index
     * @return true, if is cell editable
     */
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        ProcessFunctionParameterValue value = valueList.get(rowIndex);

        switch (columnIndex) {
            case COL_OPTIONAL:
                return value.optional;
            case COL_VALUE:
            case COL_PARAMETER:
            case COL_TYPE:
            default:
                return false;
        }
    }

    /**
     * Sets the value at.
     *
     * @param aValue the a value
     * @param rowIndex the row index
     * @param columnIndex the column index
     */
    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        ProcessFunctionParameterValue value = valueList.get(rowIndex);

        if (columnIndex == COL_OPTIONAL) {
            value.included = (Boolean) aValue;
        } else {
            if (value.objectValue != null) {
                value.objectValue.setValue(aValue);
            }
        }
    }

    /**
     * Gets the value.
     *
     * @param row the row
     * @return the value
     */
    public ProcessFunctionParameterValue getValue(int row) {
        if ((row >= 0) && (row < valueList.size())) {
            return valueList.get(row);
        }

        return null;
    }

    /**
     * Gets the optional column.
     *
     * @return the optional column
     */
    public static int getOptionalColumn() {
        return COL_OPTIONAL;
    }

    /**
     * Gets the number of occurrences.
     *
     * @param value the value
     * @return the no of occurrences
     */
    public int getNoOfOccurences(ProcessFunctionParameterValue value) {
        int count = 0;
        if (value != null) {
            for (ProcessFunctionParameterValue v : valueList) {
                if (v.name.compareTo(value.name) == 0) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * Adds the new value.
     *
     * @param row the row
     */
    public void addNewValue(int row) {
        ProcessFunctionParameterValue value = getValue(row);

        if (value != null) {
            ProcessFunctionParameterValue newValue = new ProcessFunctionParameterValue(value);

            valueList.add(row, newValue);

            this.fireTableDataChanged();
        }
    }

    /**
     * Removes the value.
     *
     * @param row the row
     */
    public void removeValue(int row) {
        valueList.remove(row);
        this.fireTableDataChanged();
    }

    /**
     * Gets the expression.
     *
     * @param factory the factory
     * @return the expression
     */
    public ProcessFunction getExpression(FunctionFactory factory) {
        List<Expression> overallParameterList = new ArrayList<Expression>();

        for (ProcessFunctionParameterValue value : valueList) {
            List<Expression> parameterList = new ArrayList<Expression>();
            parameterList.add(ff.literal(value.name));

            boolean setValue = true;
            if (value.optional) {
                setValue = value.included;
            }

            if (setValue) {
                if (value.objectValue != null) {
                    Expression expression = value.objectValue.getExpression();
                    if (expression != null) {
                        parameterList.add(expression);
                    }
                }
            }

            if (setValue) {
                Function function = factory.function(PARAMETER, parameterList, null);

                overallParameterList.add(function);
            }
        }

        if (this.selectedFunction.getFunctionName() == null) {
            return null;
        }
        Function processFunction =
                factory.function(
                        this.selectedFunction.getFunctionName(), overallParameterList, null);

        return (ProcessFunction) processFunction;
    }

    /**
     * Populate using built in functions.
     *
     * @param functionName the function name
     * @param existingProcessFunction the existing process function
     */
    public void populate(FunctionName functionName, ProcessFunction existingProcessFunction) {
        this.selectedFunction.setBuiltInProcessFunction(functionName, existingProcessFunction);

        valueList = this.selectedFunction.extractParameters();
    }

    /**
     * Populate using a custom function.
     *
     * @param selectedFunction the selected function
     */
    public void populate(ProcessBriefType selectedFunction) {
        ProcessDescriptionType selectedCustomFunction = (ProcessDescriptionType) selectedFunction;
        this.selectedFunction.setSelectedCustomFunction(selectedCustomFunction);
        valueList = this.selectedFunction.extractParameters();
    }

    /**
     * Update value, sets optional fields to be included.
     *
     * @param expression the expression
     * @param row the row
     */
    public void update(Expression expression, int row) {
        ProcessFunctionParameterValue value = valueList.get(row);

        if (value.optional) {
            value.included = true;
        }

        if (value.objectValue != null) {
            value.objectValue.setValue(expression);
        }
    }
}
