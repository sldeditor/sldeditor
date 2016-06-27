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

import com.sldeditor.common.xml.ui.FieldIdEnum;

/**
 * Class to uniquely identify a field.<p>
 * Not sure the index field is needed anymore.
 *
 * @author Robert Ward (SCISYS)
 */
public class FieldId implements Comparable<FieldId> {

    /** The field id. */
    private FieldIdEnum fieldId = FieldIdEnum.UNKNOWN;
    
    /** The index. */
    private int index = 0;

    /** The unknown value. */
    private static FieldId unknownValue = new FieldId();

    /**
     * Instantiates a new field id.
     */
    public FieldId() {
        super();
    }
    
    /**
     * Instantiates a new field id.
     *
     * @param fieldId the field id
     */
    public FieldId(FieldIdEnum fieldId) {
        super();
        this.fieldId = fieldId;
    }

    /**
     * Instantiates a new field id.
     *
     * @param fieldId the field id
     * @param index the index
     */
    public FieldId(FieldIdEnum fieldId, int index) {
        super();
        this.fieldId = fieldId;
        this.index = index;
    }

    /**
     * Gets the field id.
     *
     * @return the fieldId
     */
    public FieldIdEnum getFieldId() {
        return fieldId;
    }

    /**
     * Sets the index.
     *
     * @param index the new index
     */
    public void setIndex(int index)
    {
        this.index = index;
    }

    /**
     * Gets the index.
     *
     * @return the index
     */
    public int getIndex() {
        return index;
    }

    /**
     * Compare to.
     *
     * @param o the o
     * @return the int
     */
    @Override
    public int compareTo(FieldId o) {
        if(o.fieldId == fieldId)
        {
            if(o.index == index)
            {
                return 0;
            }
            
            return (o.index > index) ? 1 : -1;
        }
        
        return (o.fieldId.ordinal() > fieldId.ordinal()) ? 1 : -1;
    }

    /**
     * Hash code.
     *
     * @return the int
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((fieldId == null) ? 0 : fieldId.hashCode());
        result = prime * result + index;
        return result;
    }

    /**
     * Equals.
     *
     * @param obj the obj
     * @return true, if successful
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        FieldId other = (FieldId) obj;
        if (fieldId != other.fieldId)
            return false;
        if (index != other.index)
            return false;
        return true;
    }

    /**
     * To string.
     *
     * @return the string
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(fieldId);
        sb.append(".");
        sb.append(index);
        return sb.toString();
    }

    /**
     * Gets the unknown value.
     *
     * @return the unknown value
     */
    public static FieldId getUnknownValue() {
        return unknownValue;
    }

    /**
     * Sets the field id.
     *
     * @param fieldId the new field id
     */
    public void setFieldId(FieldIdEnum fieldId) {
        this.fieldId = fieldId;
    }
}
