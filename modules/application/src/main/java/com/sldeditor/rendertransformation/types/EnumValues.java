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
import java.util.Arrays;
import java.util.List;

import org.geotools.filter.AttributeExpressionImpl;
import org.geotools.filter.FunctionExpressionImpl;
import org.geotools.filter.LiteralExpressionImpl;
import org.geotools.filter.MathExpressionImpl;
import org.opengis.filter.expression.Expression;

import com.sldeditor.ui.detail.config.FieldConfigBase;
import com.sldeditor.ui.detail.config.FieldConfigCommonData;
import com.sldeditor.ui.detail.config.symboltype.SymbolTypeConfig;

/**
 * The Class EnumValues.
 *
 * @author Robert Ward (SCISYS)
 */
public class EnumValues extends BaseValue implements RenderTransformValueInterface {

    /** The class type. */
    private Class<?> classType = StringBuilder.class;

    /** The enum list. */
    private List<String> enumValueList = new ArrayList<String>();

    /** The value. */
    private String value = null;

    /**
     * Instantiates a new enum values.
     *
     * @param classType the class type
     */
    public EnumValues(Class<?> classType) {
        this.classType = classType;
    }

    /**
     * Instantiates a new enum values from an existing instance.
     *
     * @param objToCopy the obj to copy
     */
    public EnumValues(EnumValues objToCopy) {
        this.classType = objToCopy.classType;
        this.enumValueList = objToCopy.enumValueList;
        this.value = objToCopy.value;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.rendertransformation.types.RenderTransformValueInterface#setDefaultValue(java.lang.Object)
     */
    @Override
    public void setDefaultValue(Object defaultValue) {
        this.value = (String) defaultValue;
    }

    /**
     * Populate.
     * 
     * @param enumList the enum list
     */
    public void populate(List<?> enumList) {

        for (Object enumValue : enumList) {
            if (enumValue != null) {
                String stringValue = enumValue.toString();

                enumValueList.add(stringValue);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.rendertransformation.types.BaseValue#populateSymbolType(com.sldeditor.ui.detail.config.symboltype.SymbolTypeConfig)
     */
    protected void populateSymbolType(SymbolTypeConfig symbolTypeConfig) {
        for (String enumValue : enumValueList) {
            symbolTypeConfig.addOption(enumValue, enumValue);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.rendertransformation.types.RenderTransformValueInterface#getExpression()
     */
    @Override
    public Expression getExpression() {
        if (expression != null) {
            return expression;
        }
        return filterFactory.literal(value);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.rendertransformation.types.RenderTransformValueInterface#setValue(java.lang.Object)
     */
    @Override
    public void setValue(Object aValue) {
        this.value = null;
        this.expression = null;

        if (aValue instanceof LiteralExpressionImpl) {
            value = ((Expression) aValue).toString();
        }
        else if((aValue instanceof AttributeExpressionImpl) ||
                (aValue instanceof FunctionExpressionImpl) ||
                (aValue instanceof MathExpressionImpl))
        {
            this.expression = (Expression) aValue;
        }
        else {
            if (aValue instanceof String) {
                value = ((String) aValue);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.rendertransformation.types.RenderTransformValueInterface#getType()
     */
    @Override
    public List<Class<?>> getType() {
        return Arrays.asList(classType);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.rendertransformation.types.RenderTransformValueInterface#getField(com.sldeditor.ui.detail.config.FieldConfigCommonData)
     */
    @Override
    public FieldConfigBase getField(FieldConfigCommonData commonData) {
        return createEnum(commonData);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.rendertransformation.types.RenderTransformValueInterface#createInstance()
     */
    @Override
    public RenderTransformValueInterface createInstance() {
        return new EnumValues(this);
    }
}
