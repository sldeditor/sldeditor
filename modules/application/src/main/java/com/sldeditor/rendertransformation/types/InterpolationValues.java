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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.media.jai.Interpolation;
import javax.media.jai.InterpolationBicubic;
import javax.media.jai.InterpolationBicubic2;
import javax.media.jai.InterpolationBilinear;
import javax.media.jai.InterpolationNearest;
import org.geotools.filter.AttributeExpressionImpl;
import org.geotools.filter.FunctionExpressionImpl;
import org.geotools.filter.LiteralExpressionImpl;
import org.geotools.filter.MathExpressionImpl;
import org.opengis.filter.expression.Expression;

/**
 * The Class InterpolationValues.
 *
 * @author Robert Ward (SCISYS)
 */
public class InterpolationValues extends BaseValue implements RenderTransformValueInterface {

    /** The interpolation map. */
    private static Map<Class<? extends Interpolation>, String> interpolationMap = null;

    /** The value. */
    private Interpolation value = null;

    /** The Constant DEFAULT_SAMPLE_BITS. */
    private static final int DEFAULT_SAMPLE_BITS = 8;

    /** The interpolation bicubic pattern match. */
    private static final Pattern INTERPOLATION_BICUBIC_PATTERN_MATCH =
            Pattern.compile("InterpolationBicubic\\(\\d+\\)");

    private static final Pattern INTERPOLATION_BICUBIC2_PATTERN_MATCH =
            Pattern.compile("InterpolationBicubic2\\(\\d+\\)");

    private static final Pattern INTERPOLATION_BICUBIC_PATTERN_EXTRACT = Pattern.compile("\\d+");

    /** The sample bits. */
    private int sampleBits = DEFAULT_SAMPLE_BITS;

    /** Instantiates a new interpolation values. */
    public InterpolationValues() {
        populateInterpolation();
    }

    /** Populate interpolation map. */
    private static synchronized void populateInterpolation() {
        if (interpolationMap == null) {
            interpolationMap = new LinkedHashMap<>();
            interpolationMap.put(InterpolationNearest.class, "Nearest Neighbour");
            interpolationMap.put(InterpolationBicubic.class, "Bicubic");
            interpolationMap.put(InterpolationBicubic2.class, "Bicubic2");
            interpolationMap.put(InterpolationBilinear.class, "Bilinear");
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
        this.value = (Interpolation) defaultValue;
    }

    /**
     * Populate symbol type.
     *
     * @param symbolTypeConfig the symbol type config
     */
    protected void populateSymbolType(SymbolTypeConfig symbolTypeConfig) {
        if (symbolTypeConfig != null) {
            for (Entry<Class<? extends Interpolation>, String> entry :
                    interpolationMap.entrySet()) {
                symbolTypeConfig.addOption(entry.getKey().getSimpleName(), entry.getValue());
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
            if ((value.getClass() == InterpolationBicubic.class)
                    || (value.getClass() == InterpolationBicubic2.class)) {
                String string =
                        String.format("%s(%d)", value.getClass().getSimpleName(), sampleBits);
                return filterFactory.literal(string);
            } else {
                return filterFactory.literal(value.getClass().getSimpleName());
            }
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

        if (aValue instanceof LiteralExpressionImpl) {
            String displayName = ((Expression) aValue).toString();

            if (InterpolationNearest.class.getSimpleName().compareTo(displayName) == 0) {
                value = new InterpolationNearest();
            } else if (InterpolationBilinear.class.getSimpleName().compareTo(displayName) == 0) {
                value = new InterpolationBilinear();
            } else if (displayName.startsWith(InterpolationBicubic2.class.getSimpleName())) {
                sampleBits = extractSampleBits(INTERPOLATION_BICUBIC2_PATTERN_MATCH, displayName);
                value = new InterpolationBicubic2(sampleBits);
            } else if (displayName.startsWith(InterpolationBicubic.class.getSimpleName())) {
                sampleBits = extractSampleBits(INTERPOLATION_BICUBIC_PATTERN_MATCH, displayName);
                value = new InterpolationBicubic(sampleBits);
            }
        } else if ((aValue instanceof AttributeExpressionImpl)
                || (aValue instanceof FunctionExpressionImpl)
                || (aValue instanceof MathExpressionImpl)) {
            this.expression = (Expression) aValue;
        }
    }

    /**
     * Extract sample bits.
     *
     * @param displayName the display name
     * @return the int
     */
    private int extractSampleBits(Pattern patternMatch, String displayName) {
        // match the string
        Matcher matcher = patternMatch.matcher(displayName);
        if (matcher.matches()) {
            // extract subsample bits
            matcher = INTERPOLATION_BICUBIC_PATTERN_EXTRACT.matcher(displayName);
            matcher.matches();
            if (matcher.find()) {
                // get the match and parse it
                final String subsBitsString = matcher.group();

                return Integer.parseInt(subsBitsString);
            }
        }
        // unable to parse
        return DEFAULT_SAMPLE_BITS;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.rendertransformation.types.RenderTransformValueInterface#getType()
     */
    @Override
    public List<Class<?>> getType() {
        return Arrays.asList(Interpolation.class);
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
        return new InterpolationValues();
    }
}
