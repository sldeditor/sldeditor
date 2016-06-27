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
package com.sldeditor.common.datasource.attribute;

import org.opengis.feature.type.Name;

/**
 * Class that encapsulates data about data source attributes : name, type, value.
 * 
 * @author Robert Ward (SCISYS)
 */
public class DataSourceAttributeData implements Cloneable
{
    /** The name. */
    private Name name;

    /** The type. */
    private Class<?> type;

    /** The value. */
    private Object value;

    /**
     * Instantiates a new render attribute data.
     *
     * @param name the name
     * @param type the type
     * @param value the value
     */
    public DataSourceAttributeData(Name name, Class<?> type, Object value) {
        super();
        this.name = name;
        this.type = type;
        this.value = value;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#clone()
     */
    @Override
    public DataSourceAttributeData clone() {
        DataSourceAttributeData newObj = new DataSourceAttributeData(name, type, value);

        return newObj;
    }

    /**
     * Instantiates a new render attribute data.
     *
     * @param objectToCopy the object to copy
     */
    public DataSourceAttributeData(DataSourceAttributeData objectToCopy)
    {
        super();
        this.name = objectToCopy.name;
        this.type = objectToCopy.type;
        this.value = objectToCopy.value;
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    public Name getName() {
        return name;
    }

    /**
     * Gets the type.
     *
     * @return the type
     */
    public Class<?> getType() {
        return type;
    }

    /**
     * Gets the value.
     *
     * @return the value
     */
    public Object getValue() {
        return value;
    }

    /**
     * Sets the name.
     *
     * @param name the new name
     */
    public void setName(Name name) {
        this.name = name;
    }

    /**
     * Sets the type.
     *
     * @param type the new type
     */
    public void setType(Class<?> type) {
        this.type = type;
    }

    /**
     * Sets the value.
     *
     * @param value the new value
     */
    public void setValue(Object value) {
        this.value = value;
    }
}
