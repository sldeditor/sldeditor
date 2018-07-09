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

import com.sldeditor.common.coordinate.CoordManager;
import com.sldeditor.ui.detail.config.FieldConfigBase;
import com.sldeditor.ui.detail.config.FieldConfigCommonData;
import com.sldeditor.ui.detail.config.symboltype.SymbolTypeConfig;
import com.sldeditor.ui.widgets.ValueComboBoxData;
import java.util.Arrays;
import java.util.List;
import org.geotools.filter.AttributeExpressionImpl;
import org.geotools.filter.FunctionExpressionImpl;
import org.geotools.filter.LiteralExpressionImpl;
import org.geotools.filter.MathExpressionImpl;
import org.opengis.filter.expression.Expression;

/**
 * The Class CoordinateReferenceSystemValues.
 *
 * @author Robert Ward (SCISYS)
 */
public class CoordinateReferenceSystemValues extends BaseValue
        implements RenderTransformValueInterface {

    /** The crs value list. */
    private static List<ValueComboBoxData> crsValueList = null;

    /** The value. */
    private String value = null;

    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.rendertransformation.types.RenderTransformValueInterface#getType()
     */
    @Override
    public List<Class<?>> getType() {
        return Arrays.asList(org.opengis.referencing.crs.CoordinateReferenceSystem.class);
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

    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.rendertransformation.types.BaseValue#populateSymbolType(com.sldeditor.ui.detail.config.symboltype.SymbolTypeConfig)
     */
    protected void populateSymbolType(SymbolTypeConfig symbolTypeConfig) {
        if (crsValueList == null) {
            crsValueList = CoordManager.getInstance().getCRSList();
        }

        for (ValueComboBoxData valueComboData : crsValueList) {
            symbolTypeConfig.addOption(valueComboData.getKey(), valueComboData.getText());
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
            LiteralExpressionImpl literal = (LiteralExpressionImpl) aValue;
            if (literal.getValue() != null) {
                value = literal.toString();
            }
        } else if ((aValue instanceof AttributeExpressionImpl)
                || (aValue instanceof FunctionExpressionImpl)
                || (aValue instanceof MathExpressionImpl)) {
            this.expression = (Expression) aValue;
        } else {
            if (aValue instanceof String) {
                value = ((String) aValue);
            }
        }
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
        return new CoordinateReferenceSystemValues();
    }
}
