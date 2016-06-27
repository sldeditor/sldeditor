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
package com.sldeditor.common;

/**
 * The Class EnvVar.
 *
 * @author Robert Ward (SCISYS)
 */
public class EnvVar {

    /** The name. */
    private String name;

    /** The type. */
    private Class<?> type;

    /** The predefined flag. */
    private boolean predefined = false;

    /** The value. */
    private Object value = null;

    /**
     * Instantiates a new environment variable.
     *
     * @param name the name
     * @param type the type
     * @param predefined the predefined
     */
    public EnvVar(String name, Class<?> type, boolean predefined) {
        super();
        this.name = name;
        this.type = type;
        this.predefined = predefined;
    }

    /**
     * Instantiates a new env var.
     *
     * @param envVar the env var
     */
    public EnvVar(EnvVar envVar) {
        this.name = envVar.name;
        this.type = envVar.type;
        this.value = envVar.value;
        this.predefined = envVar.predefined;
    }

    /**
     * Instantiates a new env var.
     *
     * @param envVar the env var
     * @param predefined the predefined
     */
    public EnvVar(EnvVar envVar, boolean predefined) {
        this.name = envVar.name;
        this.type = envVar.type;
        this.value = envVar.value;
        this.predefined = predefined;
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
     * Returns the is predefined flag.
     *
     * @return true, if is predefined
     */
    public boolean isPredefined() {
        return predefined;
    }

    /**
     * Sets the name.
     *
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the type.
     *
     * @param type the type to set
     */
    public void setType(Class<?> type) {
        this.type = type;
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
     * Sets the value.
     *
     * @param value the value to set
     */
    public void setValue(Object value) {
        this.value = value;
    }
}
