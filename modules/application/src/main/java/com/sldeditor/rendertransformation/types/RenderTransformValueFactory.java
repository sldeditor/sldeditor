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

import com.sldeditor.rendertransformation.ProcessFunctionParameterValue;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.opengis.ows11.impl.ValueTypeImpl;
import net.opengis.wps10.LiteralInputType;

/**
 * A factory for creating RenderTransformValue objects.
 *
 * @author Robert Ward (SCISYS)
 */
public class RenderTransformValueFactory {

    /** The value list. */
    private Map<Class<?>, RenderTransformValueInterface> valueList = new HashMap<>();

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

    /** Instantiates a new render transform value factory. */
    private RenderTransformValueFactory() {
        populate();
    }

    /** Populate. */
    private void populate() {
        internalPopulate(new InterpolationValues());
        internalPopulate(new CoordinateReferenceSystemValues());
        internalPopulate(new BooleanValues());
        internalPopulate(new DateValues());
        internalPopulate(new DoubleValues());
        internalPopulate(new GeometryValues());
        internalPopulate(new IntegerValues());
        internalPopulate(new NumberValues());
        internalPopulate(new ReferencedEnvelopeValues());
        internalPopulate(new StringValues());
        internalPopulate(new UnitValues());
        internalPopulate(new FeatureCollectionValues());
        internalPopulate(new KernelJAIValues());
        internalPopulate(new JAIToolsRangeValues());
        internalPopulate(new JAIExtRangeValues());
        internalPopulate(new FloatValues());
        internalPopulate(new StyleValues());
        internalPopulate(new FilterValues());
        internalPopulate(new TextAreaValues());
        internalPopulate(new TimePeriodValues());
    }

    /**
     * Internal populate.
     *
     * @param values the values
     */
    private void internalPopulate(RenderTransformValueInterface values) {
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
            System.err.println(
                    "Failed to find object for :"
                            + ((classToFind != null) ? classToFind.getName() : "null"));
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

        internalPopulate(enumValues);

        return enumValues;
    }

    /**
     * Gets the value custom process.
     *
     * @param value the value
     * @param literal the literal
     */
    public void getValueCustomProcess(
            ProcessFunctionParameterValue value, LiteralInputType literal) {
        if (literal != null) {
            String defaultValue = literal.getDefaultValue();
            if (literal.getDataType() != null) {
                value.setDataType(literal.getDataType().getValue());
                if (defaultValue != null) {
                    if (value.getDataType().compareTo("xs:int") == 0) {
                        value.setObjectValue(new IntegerValues());
                        value.getObjectValue().setDefaultValue(Integer.valueOf(defaultValue));
                    } else if (value.getDataType().compareTo("xs:boolean") == 0) {
                        value.setObjectValue(new BooleanValues());
                        value.getObjectValue().setDefaultValue(Boolean.valueOf(defaultValue));
                    } else if (value.getDataType().compareTo("xs:double") == 0) {
                        value.setObjectValue(new DoubleValues());
                        value.getObjectValue().setDefaultValue(Double.valueOf(defaultValue));
                    } else if (value.getDataType().compareTo("xs:float") == 0) {
                        value.setObjectValue(new FloatValues());
                        value.getObjectValue().setDefaultValue(Float.valueOf(defaultValue));
                    } else if (value.getDataType().compareTo("xs:long") == 0) {
                        value.setObjectValue(new LongValues());
                        value.getObjectValue().setDefaultValue(Long.valueOf(defaultValue));
                    } else {
                        value.setType(String.class);
                        value.setObjectValue(new StringValues());
                        value.getObjectValue().setDefaultValue(defaultValue);
                    }
                }
            } else if (literal.getAllowedValues() != null) {
                EnumValues enumValues = new EnumValues(StringBuilder.class);
                value.setObjectValue(enumValues);
                value.getObjectValue().setDefaultValue(defaultValue);
                List<String> enumValueList = new ArrayList<>();

                for (Object valueTypeObj : literal.getAllowedValues().getValue()) {
                    ValueTypeImpl valueType = (ValueTypeImpl) valueTypeObj;
                    enumValueList.add(valueType.getValue());
                }
                enumValues.populate(enumValueList);
            }
        }
    }
}
