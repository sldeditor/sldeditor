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

package com.sldeditor.ui.detail.config;

import com.sldeditor.common.xml.TestValueVisitor;
import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.filter.v2.function.temporal.TimePeriod;
import com.sldeditor.ui.widgets.ValueComboBoxData;
import java.util.Date;
import java.util.List;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.process.function.ProcessFunction;
import org.geotools.styling.ColorMap;
import org.geotools.styling.FeatureTypeConstraint;
import org.geotools.styling.Font;
import org.geotools.styling.UserLayer;
import org.jaitools.numeric.Range;
import org.opengis.filter.Id;
import org.opengis.filter.expression.Expression;

/**
 * The Class FieldConfigPopulate is a base class for all derived FieldConfigxxx classes.
 *
 * <p>All FieldConfigValuePopulateInterface, TestValueVisitor interface methods grouped into this
 * class.
 *
 * @author Robert Ward (SCISYS)
 */
public abstract class FieldConfigPopulate extends FieldConfigCommonData
        implements FieldConfigValuePopulateInterface, TestValueVisitor {

    /**
     * Instantiates a new field data types.
     *
     * @param commonData the common data
     */
    public FieldConfigPopulate(FieldConfigCommonData commonData) {
        super(commonData);
    }

    /**
     * Populate.
     *
     * @param expression the expression
     */
    public abstract void populate(Expression expression);

    /**
     * Populate string field, overridden if necessary.
     *
     * @param value the value
     */
    @Override
    public void populateField(String value) {
        // Do nothing
    }

    /**
     * Populate integer field, overridden if necessary.
     *
     * @param value the value
     */
    @Override
    public void populateField(Integer value) {
        // Do nothing
    }

    /**
     * Populate double field, overridden if necessary.
     *
     * @param value the value
     */
    @Override
    public void populateField(Double value) {
        // Do nothing
    }

    /**
     * Populate range field, overridden if necessary.
     *
     * @param value the value
     */
    @SuppressWarnings("rawtypes")
    @Override
    public void populateField(Range value) {
        // Do nothing
    }

    /**
     * Populate date field, overridden if necessary.
     *
     * @param value the value
     */
    @Override
    public void populateField(Date value) {
        // Do nothing
    }

    /**
     * Populate field.
     *
     * @param value the value
     */
    public void populateField(ReferencedEnvelope value) {
        // Do nothing
    }

    /**
     * Populate field.
     *
     * @param value the value
     */
    public void populateField(UserLayer value) {
        // Do nothing
    }

    /**
     * Populate string field, overridden if necessary.
     *
     * @param value the value
     */
    @Override
    public void populateField(Id value) {
        // Do nothing
    }

    /**
     * Populate time period field, overridden if necessary.
     *
     * @param value the value
     */
    @Override
    public void populateField(TimePeriod value) {
        // Do nothing
    }

    /**
     * Populate process function field, overridden if necessary.
     *
     * @param value the value
     */
    @Override
    public void populateField(ProcessFunction value) {
        // Do nothing
    }

    /**
     * Populate boolean field, overridden if necessary.
     *
     * @param value the value
     */
    @Override
    public void populateField(Boolean value) {
        // Do nothing
    }

    /**
     * Populate colourmap field, overridden if necessary.
     *
     * @param value the value
     */
    @Override
    public void populateField(ColorMap value) {
        // Do nothing
    }

    /**
     * Populate font field, overridden if necessary.
     *
     * @param value the value
     */
    public void populateField(Font value) {
        // Do nothing
    }

    /**
     * Populate feature type constraint map field, overridden if necessary.
     *
     * @param value the value
     */
    public void populateField(List<FeatureTypeConstraint> value) {
        // Do nothing
    }

    /**
     * Gets the feature type constraint.
     *
     * @return the feature type constraint
     */
    public List<FeatureTypeConstraint> getFeatureTypeConstraint() {
        return null;
    }

    /**
     * Sets the test value, overridden if necessary.
     *
     * @param fieldId the field id
     * @param testValue the test value
     */
    @Override
    public void setTestValue(FieldIdEnum fieldId, String testValue) {
        // Do nothing
    }

    /**
     * Sets the test value, overridden if necessary.
     *
     * @param fieldId the field id
     * @param testValue the test value
     */
    @Override
    public void setTestValue(FieldIdEnum fieldId, int testValue) {
        // Do nothing
    }

    /**
     * Sets the test value, overridden if necessary.
     *
     * @param fieldId the field id
     * @param testValue the test value
     */
    @Override
    public void setTestValue(FieldIdEnum fieldId, double testValue) {
        // Do nothing
    }

    /**
     * Sets the test value, overridden if necessary.
     *
     * @param fieldId the field id
     * @param testValue the test value
     */
    @Override
    public void setTestValue(FieldIdEnum fieldId, boolean testValue) {
        // Do nothing
    }

    /**
     * Sets the test value, overridden if necessary.
     *
     * @param fieldId the field id
     * @param testValue the test value
     */
    @Override
    public void setTestValue(FieldIdEnum fieldId, ColorMap testValue) {
        // Do nothing
    }

    /**
     * Sets the test value, overridden if necessary.
     *
     * @param fieldId the field id
     * @param testValue the test value
     */
    @Override
    public void setTestValue(FieldIdEnum fieldId, List<FeatureTypeConstraint> testValue) {
        // Do nothing
    }

    /**
     * Sets the test expression value.
     *
     * @param fieldId the field id
     * @param testValue the test value
     */
    @Override
    public void setTestValue(FieldIdEnum fieldId, Expression testValue) {
        populate(testValue);
    }

    /**
     * Sets the test value, overridden if necessary.
     *
     * @param fieldId the field id
     * @param testValue the test value
     */
    @Override
    public void setTestValue(FieldIdEnum fieldId, ReferencedEnvelope testValue) {
        // Do nothing
    }

    /**
     * Gets the double value, overridden if necessary.
     *
     * @return the double value
     */
    @Override
    public double getDoubleValue() {
        // Do nothing
        return 0.0;
    }

    /**
     * Gets the integer value, overridden if necessary.
     *
     * @return the int value
     */
    @Override
    public int getIntValue() {
        // Do nothing
        return 0;
    }

    /**
     * Gets the boolean value, overridden if necessary.
     *
     * @return the boolean value
     */
    @Override
    public boolean getBooleanValue() {
        // Do nothing
        return false;
    }

    /**
     * Gets the font value, overridden if necessary.
     *
     * @return the font
     */
    @Override
    public Font getFont() {
        // Do nothing
        return null;
    }

    /**
     * Gets the range value, overridden if necessary.
     *
     * @return the font
     */
    @SuppressWarnings("rawtypes")
    @Override
    public Range getRange() {
        // Do nothing
        return null;
    }

    /**
     * Gets the enum value, overridden if necessary.
     *
     * @return the enum value
     */
    @Override
    public ValueComboBoxData getEnumValue() {
        // Do nothing
        return null;
    }

    /**
     * Gets the process function, overridden if necessary.
     *
     * @return the process function
     */
    @Override
    public ProcessFunction getProcessFunction() {
        // Do nothing
        return null;
    }

    /**
     * Gets the colour map.
     *
     * @return the colour map
     */
    @Override
    public ColorMap getColourMap() {
        // Do nothing
        return null;
    }
}
