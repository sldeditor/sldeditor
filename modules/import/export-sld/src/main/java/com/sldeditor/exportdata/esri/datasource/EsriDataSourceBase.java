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
package com.sldeditor.exportdata.esri.datasource;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Base class for Esri data source classes.
 * 
 * @author Robert Ward (SCISYS)
 */
public class EsriDataSourceBase
{
    /** The translation map. */
    private Map<String, String> translationMap = new LinkedHashMap<String, String>();
    
    /** The defaults map. */
    private Map<String, String> defaultsMap = new LinkedHashMap<String, String>();

    /**
     * Adds the field translation.
     *
     * @param esriField the esri field
     * @param newField the new field
     */
    protected void addFieldTranslation(String esriField, String newField)
    {
        translationMap.put(esriField, newField);
    }
    
    /**
     * Adds the default value.
     *
     * @param field the field
     * @param defaultValue the default value
     */
    protected void addDefaultValue(String field, String defaultValue)
    {
        defaultsMap.put(field, defaultValue);
    }

    /**
     * Process the data source properties by translating them and adding default values
     *
     * @param propertyMap the property map
     * @return the map
     */
    protected Map<String, String> process(Map<String, String> propertyMap)
    {
        Map<String, String> newPropertyMap = new LinkedHashMap<String, String>();

        // Translate the fields
        for(String esriField : translationMap.keySet())
        {
            String value = propertyMap.get(esriField);
            
            newPropertyMap.put(translationMap.get(esriField), value);
        }
        
        // Set any default values
        for(String field : defaultsMap.keySet())
        {
            newPropertyMap.put(field, defaultsMap.get(field));
        }
        
        return newPropertyMap;
    }
}
