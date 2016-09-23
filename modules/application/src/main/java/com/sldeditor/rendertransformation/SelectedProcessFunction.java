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

import java.util.List;

import org.geotools.feature.NameImpl;
import org.geotools.process.function.ProcessFunction;
import org.opengis.feature.type.Name;
import org.opengis.filter.capability.FunctionName;

import net.opengis.wps10.ProcessDescriptionType;

/**
 * Class that abstracts away which which process function was selected, built-in or custom.
 *
 * @author Robert Ward (SCISYS)
 */
public class SelectedProcessFunction {
    /** The selected custom function. */
    private ProcessDescriptionType selectedCustomFunction = null;

    /** The built in process function. */
    private FunctionName builtInProcessFunction = null;

    /** The selected process function. */
    private ProcessFunction selectedProcessFunctionData = null;

    /** The built in process function selected flag. */
    private boolean builtInSelected = true;

    /** The built in process function. */
    private static BuiltInProcessFunction builtIn = new BuiltInProcessFunction();

    /** The custom process function. */
    private static CustomProcessFunction custom = new CustomProcessFunction();

    /**
     * Extract parameters.
     *
     * @return the list
     */
    public List<ProcessFunctionParameterValue> extractParameters()
    {
        if(builtInSelected)
        {
            return builtIn.extractParameters(builtInProcessFunction, selectedProcessFunctionData);
        }
        else
        {
            return custom.extractParameters(selectedCustomFunction);
        }
    }

    /**
     * Sets the selected custom function.
     *
     * @param selectedCustomFunction the selectedCustomFunction to set
     */
    public void setSelectedCustomFunction(ProcessDescriptionType selectedCustomFunction) {
        this.selectedCustomFunction = selectedCustomFunction;
        this.builtInProcessFunction = null;
        this.selectedProcessFunctionData = null;
        this.builtInSelected = false;
    }

    /**
     * Sets the built in process function.
     *
     * @param builtInProcessFunction the builtInProcessFunction to set
     * @param existingProcessFunction the existing process function
     */
    public void setBuiltInProcessFunction(FunctionName builtInProcessFunction, ProcessFunction existingProcessFunction) {
        this.builtInProcessFunction = builtInProcessFunction;
        this.selectedProcessFunctionData = existingProcessFunction;
        this.selectedCustomFunction = null;
        this.builtInSelected = true;
    }

    /**
     * Checks if is built in selected.
     *
     * @return the builtInSelected
     */
    public boolean isBuiltInSelected() {
        return builtInSelected;
    }

    /**
     * Gets the row count.
     *
     * @return the row count
     */
    public int getRowCount() {
        if(builtInSelected)
        {
            if(builtInProcessFunction == null)
            {
                return 0;
            }
            return builtInProcessFunction.getArguments().size();
        }
        else
        {
            if(selectedCustomFunction == null)
            {
                return 0;
            }
            return selectedCustomFunction.getDataInputs().getInput().size();
        }
    }

    /**
     * Gets the function name.
     *
     * @return the function name
     */
    public Name getFunctionName() {
        if(builtInSelected)
        {
            if(builtInProcessFunction == null)
            {
                return null;
            }
            return builtInProcessFunction.getFunctionName();
        }
        else
        {
            if(selectedCustomFunction == null)
            {
                return null;
            }
            return new NameImpl(selectedCustomFunction.getTitle().getValue());
        }
    }

    /**
     * Extract local function name.
     *
     * @param functionName the function name
     * @return the string
     */
    public static String extractLocalFunctionName(String functionName) {
        String functionNameString = null;
        if(functionName != null)
        {
            String[] components = functionName.split(":");
            if(components.length == 2)
            {
                functionNameString = components[1];
            }
            else
            {
                functionNameString = functionName;
            }
        }
        return functionNameString;
    }
}
