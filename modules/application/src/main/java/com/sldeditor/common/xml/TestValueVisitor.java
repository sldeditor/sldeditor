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

package com.sldeditor.common.xml;

import com.sldeditor.common.xml.ui.FieldIdEnum;
import java.util.List;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.styling.ColorMap;
import org.geotools.styling.FeatureTypeConstraint;
import org.opengis.filter.expression.Expression;

/**
 * The Interface TestValueVisitor, part of a visitor pattern to allow test values to be applied to
 * fields.
 *
 * @author Robert Ward (SCISYS)
 */
public interface TestValueVisitor {

    /**
     * Sets the test string value.
     *
     * @param fieldId the field id
     * @param testValue the test value
     */
    public void setTestValue(FieldIdEnum fieldId, String testValue);

    /**
     * Sets the test integer value.
     *
     * @param fieldId the field id
     * @param testValue the test value
     */
    public void setTestValue(FieldIdEnum fieldId, int testValue);

    /**
     * Sets the test double value.
     *
     * @param fieldId the field id
     * @param testValue the test value
     */
    public void setTestValue(FieldIdEnum fieldId, double testValue);

    /**
     * Sets the test boolean value.
     *
     * @param fieldId the field id
     * @param testValue the test value
     */
    public void setTestValue(FieldIdEnum fieldId, boolean testValue);

    /**
     * Sets the test colour map value.
     *
     * @param fieldId the field id
     * @param testValue the test value
     */
    public void setTestValue(FieldIdEnum fieldId, ColorMap testValue);

    /**
     * Sets the test feature type constraint map value.
     *
     * @param fieldId the field id
     * @param testValue the test value
     */
    public void setTestValue(FieldIdEnum fieldId, List<FeatureTypeConstraint> testValue);

    /**
     * Sets the test attribute/expression value.
     *
     * @param fieldId the field id
     * @param testValue the test value
     */
    public void setTestValue(FieldIdEnum fieldId, Expression testValue);

    /**
     * Sets the test reference envelope value.
     *
     * @param fieldId the field id
     * @param testValue the test value
     */
    public void setTestValue(FieldIdEnum fieldId, ReferencedEnvelope testValue);
}
