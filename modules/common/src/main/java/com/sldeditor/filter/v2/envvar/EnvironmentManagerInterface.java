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
package com.sldeditor.filter.v2.envvar;

import java.util.List;

import org.opengis.filter.expression.Expression;

/**
 * The Class EnvironmentManagerInterface.
 *
 * @author Robert Ward (SCISYS)
 */
public interface EnvironmentManagerInterface {

    /**
     * Adds the new environment variable.
     *
     * @param name the name
     * @param type the type
     * @param value the value
     * @return the env var
     */
    EnvVar addNewEnvVar(String name, Class<?> type, String value);

    /**
     * Gets the environment variable list.
     *
     * @return the envVarList
     */
    List<EnvVar> getEnvVarList();

    /**
     * Creates the expression.
     *
     * @param envVar the env var
     * @return the expression
     */
    Expression createExpression(EnvVar envVar);

    /**
     * Removes the env var.
     *
     * @param envVar the env var
     */
    void removeEnvVar(EnvVar envVar);

    /**
     * Update.
     *
     * @param dataList the data list
     */
    void update(List<EnvVar> dataList);

    /**
     * Gets the environment variable type list.
     *
     * @return the env var type list
     */
    List<Class<?>> getEnvVarTypeList();

    /**
     * Gets the data type of the given environment variable
     *
     * @param envVarLiteral the env var literal
     * @return the data type
     */
    Class<?> getDataType(Expression envVarLiteral);

    /**
     * Sets the WMS environment variable values.
     *
     * @param wmsEnvVarValues the wmsEnvVarValues to set
     */
    void setWMSEnvVarValues(WMSEnvVarValues wmsEnvVarValues);

    /**
     * Show dialog.
     *
     * @return true, if successful
     */
    boolean showDialog();

    /**
     * Adds the env var updated listener.
     *
     * @param listener the listener
     */
    void addEnvVarUpdatedListener(EnvVarUpdateInterface listener);
}
