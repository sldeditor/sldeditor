/*
 * SLD Editor - The Open Source Java SLD Editor
 *
 * Copyright (C) 2017, SCISYS UK Limited
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

package com.sldeditor.rendertransformation.types;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sldeditor.rendertransformation.ProcessFunctionParameterValue;

import net.opengis.ows11.impl.ValueTypeImpl;
import net.opengis.wps10.LiteralInputType;

/**
 * A factory for creating RenderTransformValue objects.
 *
 * @author Robert Ward (SCISYS)
 */
public class RenderTransformValueFactory {

    /** The value list. */
    private Map<Class<?>, RenderTransformValueInterface> valueList = new HashMap<Class<?>, RenderTransformValueInterface>();

    /** The instance. */
    private static RenderTransformValueFactory instance = null;

    /**
     * Gets the single instance of RenderTransformValueFactory.
     *
     * @return single instance of RenderTransformValueFactory
     */
    public static RenderTransformValueFactory getInstance() {
        if (instance == null) {
            instance = new RenderTransformValueFactory();
        }

        return instance;
    }

    /**
     * Instantiates a new render transform value factory.
     */
    private RenderTransformValueFactory() {
        populate();
    }

    /**
     * Populate.
     */
    private void populate() {
        internal_populate(new InterpolationValues());
        internal_populate(new CoordinateReferenceSystemValues());
        internal_populate(new BooleanValues());
        internal_populate(new DateValues());
        internal_populate(new DoubleValues());
        internal_populate(new GeometryValues());
        internal_populate(new IntegerValues());
        internal_populate(new NumberValues());
        internal_populate(new ReferencedEnvelopeValues());
        internal_populate(new StringValues());
        internal_populate(new UnitValues());
        internal_populate(new FeatureCollectionValues());
        internal_populate(new KernelJAIValues());
        internal_populate(new JAIToolsRangeValues());
        internal_populate(new JAIExtRangeValues());
        internal_populate(new FloatValues());
        internal_populate(new StyleValues());
        internal_populate(new FilterValues());
    }

    /**
     * Internal populate.
     *
     * @param values the values
     */
    private void internal_populate(RenderTransformValueInterface values) {
        List<Class<?>> supportedClasses = values.getType();

        for (Class<?> supportedClass : supportedClasses) {
            valueList.put(supportedClass, values);
        }
    }

    /**
     * Gets the value object and creates a new instance.
     *
     * @param classToFind the class to find
     * @return the value
     */
    public RenderTransformValueInterface getValue(Class<?> classToFind) {
        RenderTransformValueInterface value = valueList.get(classToFind);
        if (value == null) {
            System.err.println("Failed to find object for :" + ((classToFind != null) ? classToFind.getName() : "null"));
            return null;
        }
        return value.createInstance();
    }

    /**
     * Gets the enum.
     *
     * @param classType the class type
     * @param enumList the enum list
     * @return the enum
     */
    public RenderTransformValueInterface getEnum(Class<?> classType, List<?> enumList) {
        EnumValues enumValues = new EnumValues(classType);

        enumValues.populate(enumList);

        internal_populate(enumValues);

        return enumValues;
    }

    /**
     * Gets the value custom process.
     *
     * @param value the value
     * @param literal the literal
     */
    public void getValueCustomProcess(ProcessFunctionParameterValue value,
            LiteralInputType literal) {
        if (literal != null) {
            String defaultValue = literal.getDefaultValue();
            if (literal.getDataType() != null) {
                value.dataType = literal.getDataType().getValue();
                if (defaultValue != null) {
                    if (value.dataType.compareTo("xs:int") == 0) {
                        value.objectValue = new IntegerValues();
                        value.objectValue.setDefaultValue(Integer.valueOf(defaultValue));
                    } else if (value.dataType.compareTo("xs:boolean") == 0) {
                        value.objectValue = new BooleanValues();
                        value.objectValue.setDefaultValue(Boolean.valueOf(defaultValue));
                    } else if (value.dataType.compareTo("xs:double") == 0) {
                        value.objectValue = new DoubleValues();
                        value.objectValue.setDefaultValue(Double.valueOf(defaultValue));
                    } else if (value.dataType.compareTo("xs:float") == 0) {
                        value.objectValue = new FloatValues();
                        value.objectValue.setDefaultValue(Float.valueOf(defaultValue));
                    } else if (value.dataType.compareTo("xs:long") == 0) {
                        value.objectValue = new LongValues();
                        value.objectValue.setDefaultValue(Long.valueOf(defaultValue));
                    } else {
                        value.type = String.class;
                        value.objectValue = new StringValues();
                        value.objectValue.setDefaultValue(defaultValue);
                    }
                }
            } else if (literal.getAllowedValues() != null) {
                EnumValues enumValues = new EnumValues(StringBuilder.class);
                value.objectValue = enumValues;
                value.objectValue.setDefaultValue(defaultValue);
                List<String> enumValueList = new ArrayList<String>();

                for (Object valueTypeObj : literal.getAllowedValues().getValue()) {
                    ValueTypeImpl valueType = (ValueTypeImpl) valueTypeObj;
                    enumValueList.add(valueType.getValue());
                }
                enumValues.populate(enumValueList);
            }
        }
    }
}
