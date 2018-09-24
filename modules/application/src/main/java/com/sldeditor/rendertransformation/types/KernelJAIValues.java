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

import com.sldeditor.ui.detail.config.FieldConfigBase;
import com.sldeditor.ui.detail.config.FieldConfigCommonData;
import com.sldeditor.ui.detail.config.symboltype.SymbolTypeConfig;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.media.jai.KernelJAI;
import org.geotools.filter.AttributeExpressionImpl;
import org.geotools.filter.FunctionExpressionImpl;
import org.geotools.filter.LiteralExpressionImpl;
import org.geotools.filter.MathExpressionImpl;
import org.opengis.filter.expression.Expression;

/**
 * The Class KernelJAIValues.
 *
 * @author Robert Ward (SCISYS)
 */
public class KernelJAIValues extends BaseValue implements RenderTransformValueInterface {

    /** The kernel JAI map. */
    private static Map<KernelJAI, String> kernelJAIMap = null;

    /** The value. */
    private KernelJAI value = null;

    /** Instantiates a new kernel JAI values. */
    public KernelJAIValues() {
        populateKernelKAI();
    }

    /** Populate interpolation map. */
    private static synchronized void populateKernelKAI() {
        if (kernelJAIMap == null) {
            kernelJAIMap = new HashMap<KernelJAI, String>();
            kernelJAIMap.put(
                    KernelJAI.GRADIENT_MASK_SOBEL_HORIZONTAL, "GRADIENT_MASK_SOBEL_HORIZONTAL");
            kernelJAIMap.put(
                    KernelJAI.GRADIENT_MASK_SOBEL_VERTICAL, "GRADIENT_MASK_SOBEL_VERTICAL");
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.sldeditor.rendertransformation.types.RenderTransformValueInterface#setDefaultValue(java.
     * lang.Object)
     */
    @Override
    public void setDefaultValue(Object defaultValue) {
        this.value = (KernelJAI) defaultValue;
    }

    /**
     * Populate symbol type.
     *
     * @param symbolTypeConfig the symbol type config
     */
    protected void populateSymbolType(SymbolTypeConfig symbolTypeConfig) {
        if (symbolTypeConfig != null) {
            for (String enumValue : kernelJAIMap.values()) {
                symbolTypeConfig.addOption(enumValue, enumValue);
            }
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.rendertransformation.types.RenderTransformValueInterface#getExpression()
     */
    @Override
    public Expression getExpression() {
        if (value != null) {
            return filterFactory.literal(value.getClass().getSimpleName());
        }

        if (expression != null) {
            return expression;
        }
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.sldeditor.rendertransformation.types.RenderTransformValueInterface#setValue(java.lang.
     * Object)
     */
    @Override
    public void setValue(Object aValue) {
        this.value = null;
        this.expression = null;

        if (aValue instanceof KernelJAI) {
            this.value = (KernelJAI) aValue;
        } else if ((aValue instanceof AttributeExpressionImpl)
                || (aValue instanceof LiteralExpressionImpl)
                || (aValue instanceof FunctionExpressionImpl)
                || (aValue instanceof MathExpressionImpl)) {
            this.expression = (Expression) aValue;
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.rendertransformation.types.RenderTransformValueInterface#getType()
     */
    @Override
    public List<Class<?>> getType() {
        return Arrays.asList(KernelJAI.class);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.sldeditor.rendertransformation.types.RenderTransformValueInterface#getField(com.sldeditor
     * .ui.detail.config.FieldConfigCommonData)
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
        return new KernelJAIValues();
    }
}
