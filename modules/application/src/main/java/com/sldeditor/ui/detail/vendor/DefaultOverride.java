/*
 * SLD Editor - The Open Source Java SLD Editor
 *
 * Copyright (C) 2018, SCISYS UK Limited
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

package com.sldeditor.ui.detail.vendor;

import com.sldeditor.common.xml.ui.FieldIdEnum;
import java.util.Arrays;
import java.util.List;

/**
 * The Class DefaultOverride.
 *
 * @author Robert Ward (SCISYS)
 */
public class DefaultOverride {

    /** The field. */
    private FieldIdEnum field;

    /** The legal values. */
    private List<String> legalValues;

    /**
     * Instantiates a new default override.
     *
     * @param field the field
     * @param legalValues the legal values
     */
    public DefaultOverride(FieldIdEnum field, String[] legalValues) {
        super();

        this.field = field;

        if (legalValues != null) {
            this.legalValues = Arrays.asList(legalValues);
        }
    }

    /**
     * Gets the field.
     *
     * @return the field
     */
    public FieldIdEnum getField() {
        return field;
    }

    /**
     * Gets the legal values.
     *
     * @return the legalValues
     */
    public List<String> getLegalValues() {
        return legalValues;
    }
}
