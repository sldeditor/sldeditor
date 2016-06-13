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

/**
 * Class that encapsulates process function parameter values.
 */
public class ProcessFunctionParameterValue
{
    
    /**
     * Default constructor
     */
    public ProcessFunctionParameterValue()
    {
    }

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
        value = valueToCopy.value;
        minOccurences = valueToCopy.minOccurences;
        maxOccurences = valueToCopy.maxOccurences;
        enumValueList = valueToCopy.enumValueList;
    }

    /** The name. */
    String name;

    /** The data type. */
    String dataType;

    /** The type. */
    Class<?> type = null;

    /** The optional flag. */
    Boolean optional = false;

    /** The included flag. */
    Boolean included = false;

    /** The value. */
    Object value = null;

    /** The min occurrences. */
    int minOccurences = 1;

    /** The max occurrences. */
    int maxOccurences = 1;

    /** The enum value list. */
    List<String> enumValueList = null;
}