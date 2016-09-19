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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.LiteralExpressionImpl;
import org.geotools.filter.function.EnvFunction;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Function;

import com.sldeditor.common.console.ConsoleManager;
import com.sldeditor.common.xml.ParseXML;
import com.sldeditor.common.xml.envvar.EnvironmentVariables;
import com.sldeditor.common.xml.envvar.XMLBuiltInEnvVarList;
import com.sldeditor.common.xml.envvar.XMLEnvVar;
import com.sldeditor.common.xml.envvar.XMLEnvVarType;
import com.sldeditor.common.xml.envvar.XMLEnvVarTypeList;
import com.sldeditor.filter.v2.envvar.dialog.EnvVarDlg;

/**
 * The Class EnvironmentVariableManager.
 *
 * @author Robert Ward (SCISYS)
 */
public class EnvironmentVariableManager implements EnvironmentManagerInterface {

    /** The Constant WMS_BBOX. */
    private static final String WMS_BBOX = "wms_bbox";

    /** The Constant WMS_WIDTH. */
    private static final String WMS_WIDTH = "wms_width";

    /** The Constant WMS_HEIGHT. */
    private static final String WMS_HEIGHT = "wms_height";

    /** The Constant ENV_VAR_XML. */
    private static final String ENV_VAR_XML = "/envvar/EnvVar.xml";

    /** The singleton instance. */
    private static EnvironmentManagerInterface instance = null;

    /** The environment variable list. */
    private List<EnvVar> envVarList = new ArrayList<EnvVar>();

    /** The built in env var map. */
    private Map<String, EnvVar> builtInEnvVarMap = new HashMap<String, EnvVar>();

    /** The env var type list. */
    private List<Class<?>> envVarTypeList = new ArrayList<Class<?>>();

    /** The filter factory. */
    private static FilterFactory2 ff = (FilterFactory2) CommonFactoryFinder.getFilterFactory( null );

    /** The Constant OUTPUT_SCHEMA_RESOURCE. */
    private static final String OUTPUT_SCHEMA_RESOURCE = "/xsd/envvar.xsd";

    /** The wms env var values. */
    private WMSEnvVarValues wmsEnvVarValues = null;

    /** The Constant wmsEnvVarList, the list of WMS map environment variable names. */
    private static final List<String> wmsEnvVarList = Arrays.asList(WMS_BBOX, WMS_HEIGHT, WMS_WIDTH);

    /** The env var updated listener list. */
    private List<EnvVarUpdateInterface> listenerList = new ArrayList<EnvVarUpdateInterface>();

    /**
     * Instantiates a new environment variable manager.
     */
    private EnvironmentVariableManager()
    {
        populate();
    }

    /**
     * Adds the env var updated listener.
     *
     * @param listener the listener
     */
    @Override
    public void addEnvVarUpdatedListener(EnvVarUpdateInterface listener)
    {
        if(!listenerList.contains(listener))
        {
            listenerList.add(listener);
        }
    }

    /**
     * Populate object with built in environment variables.
     */
    private void populate() {

        EnvironmentVariables envVarXML = (EnvironmentVariables) ParseXML.parseFile("", EnvironmentVariableManager.ENV_VAR_XML, OUTPUT_SCHEMA_RESOURCE, EnvironmentVariables.class);

        // Read built in types
        XMLBuiltInEnvVarList xmlBuiltInList = envVarXML.getBuiltInEnvVarList();

        for(XMLEnvVar xmlEnvVar : xmlBuiltInList.getEnvVar())
        {
            Class<?> type;
            try {
                type = Class.forName(xmlEnvVar.getType());
                EnvVar envVar = new EnvVar(xmlEnvVar.getName(), type, true);
                envVarList.add(envVar);
                builtInEnvVarMap.put(envVar.getName(), envVar);
            }
            catch (ClassNotFoundException e) {
                ConsoleManager.getInstance().exception(this, e);
            }
        }

        // Read allowed data types
        XMLEnvVarTypeList xmlEnvVarTypeList = envVarXML.getEnvVarTypeList();

        for(XMLEnvVarType envVarType : xmlEnvVarTypeList.getEnvVarType())
        {
            Class<?> type;
            try {
                type = Class.forName(envVarType.getName());
                envVarTypeList.add(type);
            }
            catch (ClassNotFoundException e) {
                ConsoleManager.getInstance().exception(this, e);
            }
        }

        setEnvironmentVariableValues();
    }

    /**
     * Gets the singleton instance of EnvironmentVariableManager.
     *
     * @return singleton instance of EnvironmentVariableManager
     */
    public static EnvironmentManagerInterface getInstance()
    {
        if(instance == null)
        {
            instance = new EnvironmentVariableManager();
        }

        return instance;
    }

    /**
     * Adds the new environment variable.
     *
     * @param name the name
     * @param type the type
     * @param value the value
     * @return the env var, null is the environment variable already exists
     */
    @Override
    public EnvVar addNewEnvVar(String name, Class<?> type, String value)
    {
        EnvVar envVar = null;

        if(!exist(name))
        {
            envVar = new EnvVar(name, type, false);
            envVar.setValue(value);

            envVarList.add(envVar);
        }

        return envVar;
    }

    /**
     * Check if environment variable name exists already.
     *
     * @param name the environment variable name to check
     * @return true, if environment variable name exists
     */
    private boolean exist(String name) {
        for(EnvVar envVar : envVarList)
        {
            if(envVar.getName().compareTo(name) == 0)
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the environment variable list.
     *
     * @return the envVarList
     */
    @Override
    public List<EnvVar> getEnvVarList() {

        List<EnvVar> newEnvVarList = new ArrayList<EnvVar>();
        for(EnvVar envVar : envVarList)
        {
            newEnvVarList.add(new EnvVar(envVar));
        }
        return newEnvVarList;
    }

    /**
     * Creates the expression.
     *
     * @param envVar the env var
     * @return the expression
     */
    @Override
    public Expression createExpression(EnvVar envVar) {
        if(envVar == null)
        {
            return null;
        }

        Function function = ff.function("env", ff.literal(envVar.getName()));

        return function;
    }

    /**
     * Removes the environment variable.
     *
     * @param envVar the environment variable
     */
    @Override
    public void removeEnvVar(EnvVar envVar) {
        if(envVar != null)
        {
            envVarList.remove(envVar);
        }
    }

    /**
     * Update the environment variables.
     *
     * @param dataList the data list
     */
    @Override
    public void update(List<EnvVar> dataList) {
        envVarList.clear();

        if(dataList == null)
        {
            // Assign the built in environment variables
            for(EnvVar envVar : builtInEnvVarMap.values())
            {
                envVarList.add(envVar);
            }
        }
        else
        {
            // Make sure predefined flags are set correctly
            for(EnvVar envVar : dataList)
            {
                if(builtInEnvVarMap.containsKey(envVar.getName()))
                {
                    envVarList.add(new EnvVar(envVar, true));
                }
                else
                {
                    envVarList.add(new EnvVar(envVar, false));
                }
            }
        }
        setEnvironmentVariableValues();
    }

    /**
     * Gets the environment variable type list.
     *
     * @return the env var type list
     */
    @Override
    public List<Class<?>> getEnvVarTypeList() {
        return envVarTypeList;
    }

    /**
     * Gets the data type.
     *
     * @param envVarLiteral the env var literal
     * @return the data type
     */
    /* (non-Javadoc)
     * @see com.sldeditor.filter.v2.envvar.EnvironmentManagerInterface#getDataType(org.opengis.filter.expression.Expression)
     */
    @Override
    public Class<?> getDataType(Expression envVarLiteral) {
        if(envVarLiteral instanceof LiteralExpressionImpl)
        {
            LiteralExpressionImpl literal = (LiteralExpressionImpl) envVarLiteral;
            for(EnvVar envVar : envVarList)
            {
                if(envVar.getName().compareTo((String) literal.getValue()) == 0)
                {
                    return envVar.getType();
                }
            }
        }
        return Object.class;
    }

    /**
     * Notify rest of the system when environment variable updates occur.
     */
    private void setEnvironmentVariableValues()
    {
        EnvFunction.clearGlobalValues();

        for(EnvVar envVar : envVarList)
        {
            if(envVar != null)
            {
                if(!wmsEnvVarList.contains(envVar.getName()))
                {
                    EnvFunction.setGlobalValue(envVar.getName(), envVar.getValue());
                }
            }
        }

        internal_setWMSEnvVarValues();

        notifyUpdates();
    }

    /**
     * Notify rest of application of env var updates.
     */
    private void notifyUpdates()
    {
        for(EnvVarUpdateInterface listener : listenerList)
        {
            listener.envVarsUpdated(this.envVarList);
        }
    }

    /**
     * Internal method to set WMS environment variable values.
     */
    private void internal_setWMSEnvVarValues() {
        if(wmsEnvVarValues == null)
        {
            return;
        }

        EnvVar imageWidth = getEnvVar(WMS_WIDTH);
        if(imageWidth != null)
        {
            // Check to see if value has been overridden
            if(imageWidth.getValue() == null)
            {
                // Not overridden use default
                EnvFunction.setGlobalValue(WMS_WIDTH, wmsEnvVarValues.getImageWidth());
            }
            else
            {
                EnvFunction.setGlobalValue(WMS_WIDTH, imageWidth.getValue());
            }
        }

        EnvVar imageheight = getEnvVar(WMS_HEIGHT);
        if(imageheight != null)
        {
            // Check to see if value has been overridden
            if(imageheight.getValue() == null)
            {
                // Not overridden use default
                EnvFunction.setGlobalValue(WMS_HEIGHT, wmsEnvVarValues.getImageHeight());
            }
            else
            {
                EnvFunction.setGlobalValue(WMS_HEIGHT, imageheight.getValue());
            }
        }

        EnvVar bbox = getEnvVar(WMS_BBOX);
        if(bbox != null)
        {
            // Check to see if value has been overridden
            if(bbox.getValue() == null)
            {
                // Not overridden use default
                EnvFunction.setGlobalValue(WMS_BBOX, wmsEnvVarValues.getMapBounds());
            }
            else
            {
                EnvFunction.setGlobalValue(WMS_BBOX, bbox.getValue());
            }
        }
    }

    /**
     * Gets the env var with the specified name.
     *
     * @param envVarName the env var name
     * @return the env var, null if not found
     */
    private EnvVar getEnvVar(String envVarName) {
        for(EnvVar envVar : envVarList)
        {
            if(envVar.getName().compareTo(envVarName) == 0)
            {
                return envVar;
            }
        }
        return null;
    }

    /**
     * Sets the WMS environment variable values.
     *
     * @param wmsEnvVarValues the wmsEnvVarValues to set
     */
    public void setWMSEnvVarValues(WMSEnvVarValues wmsEnvVarValues) {
        this.wmsEnvVarValues = wmsEnvVarValues;

        internal_setWMSEnvVarValues();
    }

    /**
     * Show dialog to allow the editing of environment variables and their values.
     *
     * @return true, if successful
     */
    @Override
    public boolean showDialog() {
        EnvVarDlg envVarDlg = new EnvVarDlg(this);

        return envVarDlg.showDialog();
    }
}
