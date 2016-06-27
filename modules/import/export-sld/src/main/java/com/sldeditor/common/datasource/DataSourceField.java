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
package com.sldeditor.common.datasource;

/**
 * Class that encapsulated a data source field.
 * 
 * @author Robert Ward (SCISYS)
 */
public class DataSourceField implements DataSourceFieldInterface
{

    /** The name. */
    private String name;

    /** The field type. */
    private Class<?> fieldType = null;

    /**
     * Instantiates a new data source field.
     *
     * @param name the name
     * @param fieldType the field type
     */
    public DataSourceField(String name, Class<?> fieldType) {
        super();
        this.name = name;
        this.fieldType = fieldType;
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    @Override
    public String getName()
    {
        return name;
    }

    /**
     * Sets the name.
     *
     * @param name the new name
     */
    @Override
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Gets the field type.
     *
     * @return the field type
     */
    @Override
    public Class<?> getFieldType()
    {
        return fieldType;
    }

    /**
     * Sets the field type.
     *
     * @param fieldType the new field type
     */
    @Override
    public void setFieldType(Class<?> fieldType)
    {
        this.fieldType = fieldType;
    }

}
