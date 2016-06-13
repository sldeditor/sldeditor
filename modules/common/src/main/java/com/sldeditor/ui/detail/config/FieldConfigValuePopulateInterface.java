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

import java.util.Date;

import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.process.function.ProcessFunction;
import org.geotools.styling.ColorMap;
import org.opengis.filter.Id;

import com.sldeditor.filter.v2.function.temporal.TimePeriod;
import com.sldeditor.ui.widgets.ValueComboBoxData;

/**
 * The Interface FieldConfigValuePopulateInterface, part of a visitor pattern for populating data in fields.
 *
 * @author Robert Ward (SCISYS)
 */
public interface FieldConfigValuePopulateInterface {

    /**
     * Populate field.
     *
     * @param value the value
     */
    public void populateField(String value);

    /**
     * Populate field.
     *
     * @param value the value
     */
    public void populateField(Integer value);

    /**
     * Populate field.
     *
     * @param value the value
     */
    public void populateField(Double value);

    /**
     * Populate field.
     *
     * @param value the value
     */
    public void populateField(Boolean value);

    /**
     * Populate field.
     *
     * @param value the value
     */
    public void populateField(Date value);

    /**
     * Populate field.
     *
     * @param value the value
     */
    public void populateField(TimePeriod value);

    /**
     * Populate field.
     *
     * @param value the value
     */
    public void populateField(ReferencedEnvelope value);

    /**
     * Populate field.
     *
     * @param value the value
     */
    public void populateField(Id value);

    /**
     * Populate colour map field, overridden if necessary.
     *
     * @param value the value
     */
    public void populateField(ColorMap value);

    /**
     * Populate field.
     *
     * @param value the value
     */
    public void populateField(ProcessFunction value);

    /**
     * Gets the double value.
     *
     * @return the double value
     */
    public double getDoubleValue();

    /**
     * Gets the int value.
     *
     * @return the int value
     */
    public int getIntValue();

    /**
     * Gets the boolean value.
     *
     * @return the boolean value
     */
    public boolean getBooleanValue();

    /**
     * Gets the string value.
     *
     * @return the string value
     */
    public String getStringValue();

    /**
     * Gets the enum value.
     *
     * @return the string value
     */
    public ValueComboBoxData getEnumValue();

    /**
     * Gets the colour map.
     *
     * @return the colour map
     */
    public ColorMap getColourMap();
    
    /**
     * Gets the process function.
     *
     * @return the process function
     */
    public ProcessFunction getProcessFunction();

}
