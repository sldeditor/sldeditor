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

import com.sldeditor.rendertransformation.types.RenderTransformValueInterface;

/** Class that encapsulates process function parameter values. */
public class ProcessFunctionParameterValue {

    /** Default constructor. */
    public ProcessFunctionParameterValue() {}

    /**
     * Instantiates a new value.
     *
     * @param valueToCopy the value to copy
     */
    public ProcessFunctionParameterValue(ProcessFunctionParameterValue valueToCopy) {
        name = valueToCopy.name;
        dataType = valueToCopy.dataType;
        type = valueToCopy.type;
        optional = valueToCopy.optional;
        included = valueToCopy.included;
        minOccurences = valueToCopy.minOccurences;
        maxOccurences = valueToCopy.maxOccurences;
        objectValue = valueToCopy.objectValue;
    }

    /** The name. */
    private String name;

    /** The data type. */
    private String dataType;

    /** The type. */
    private Class<?> type = null;

    /** The optional flag. */
    private boolean optional = false;

    /** The included flag. */
    private boolean included = false;

    /** The min occurrences. */
    private int minOccurences = 1;

    /** The max occurrences. */
    private int maxOccurences = 1;

    /** The object value. */
    private RenderTransformValueInterface objectValue = null;

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name.
     *
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the data type.
     *
     * @return the dataType
     */
    public String getDataType() {
        return dataType;
    }

    /**
     * Sets the data type.
     *
     * @param dataType the dataType to set
     */
    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    /**
     * Gets the type.
     *
     * @return the type
     */
    public Class<?> getType() {
        return type;
    }

    /**
     * Sets the type.
     *
     * @param type the type to set
     */
    public void setType(Class<?> type) {
        this.type = type;
    }

    /**
     * Checks if is optional.
     *
     * @return the optional
     */
    public boolean isOptional() {
        return optional;
    }

    /**
     * Sets the optional.
     *
     * @param optional the optional to set
     */
    public void setOptional(boolean optional) {
        this.optional = optional;
    }

    /**
     * Checks if is included.
     *
     * @return the included
     */
    public boolean isIncluded() {
        return included;
    }

    /**
     * Sets the included.
     *
     * @param included the included to set
     */
    public void setIncluded(boolean included) {
        this.included = included;
    }

    /**
     * Gets the min occurences.
     *
     * @return the minOccurences
     */
    public int getMinOccurences() {
        return minOccurences;
    }

    /**
     * Sets the min occurences.
     *
     * @param minOccurences the minOccurences to set
     */
    public void setMinOccurences(int minOccurences) {
        this.minOccurences = minOccurences;
    }

    /**
     * Gets the max occurences.
     *
     * @return the maxOccurences
     */
    public int getMaxOccurences() {
        return maxOccurences;
    }

    /**
     * Sets the max occurences.
     *
     * @param maxOccurences the maxOccurences to set
     */
    public void setMaxOccurences(int maxOccurences) {
        this.maxOccurences = maxOccurences;
    }

    /**
     * Gets the object value.
     *
     * @return the objectValue
     */
    public RenderTransformValueInterface getObjectValue() {
        return objectValue;
    }

    /**
     * Sets the object value.
     *
     * @param objectValue the objectValue to set
     */
    public void setObjectValue(RenderTransformValueInterface objectValue) {
        this.objectValue = objectValue;
    }
}
