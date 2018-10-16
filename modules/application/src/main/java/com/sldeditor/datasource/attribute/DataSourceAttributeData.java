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

package com.sldeditor.datasource.attribute;

/**
 * Class that encapsulates data about data source attributes : name, type, value.
 *
 * @author Robert Ward (SCISYS)
 */
public class DataSourceAttributeData {
    /** The name. */
    private String name;

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
    public DataSourceAttributeData(String name, Class<?> type, Object value) {
        super();
        this.name = name;
        this.type = type;
        this.value = value;
    }

    /**
     * Instantiates a new render attribute data.
     *
     * @param objectToCopy the object to copy
     */
    public DataSourceAttributeData(DataSourceAttributeData objectToCopy) {
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
    public String getName() {
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
    public void setName(String name) {
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

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        DataSourceAttributeData other = (DataSourceAttributeData) obj;
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        if (type == null) {
            if (other.type != null) {
                return false;
            }
        } else if (!type.equals(other.type)) {
            return false;
        }

        // Ignore value

        return true;
    }
}
