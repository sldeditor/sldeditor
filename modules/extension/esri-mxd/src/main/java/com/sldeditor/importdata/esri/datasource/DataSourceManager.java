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
package com.sldeditor.importdata.esri.datasource;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.sldeditor.common.DataSourcePropertiesInterface;
import com.sldeditor.common.console.ConsoleManager;
import com.sldeditor.importdata.esri.keys.DatasourceKeys;

/**
 * Class that converts Esri data sources to SLD Editor data sources.
 * 
 * @author Robert Ward (SCISYS)
 */
public class DataSourceManager
{
    /** The singleton instance. */
    private static DataSourceManager instance = null;

    /** The data source map. */
    private Map<Integer, EsriDataSourceInterface> dataSourceMap = new LinkedHashMap<Integer, EsriDataSourceInterface>();

    /** The logger. */
    private static Logger logger = Logger.getLogger(DataSourceManager.class);

    /**
     * Gets the single instance of DataSourceManager.
     *
     * @return single instance of DataSourceManager
     */
    public static DataSourceManager getInstance()
    {
        if(instance == null)
        {
            instance = new DataSourceManager();
        }

        return instance;
    }

    /**
     * Default constructor
     */
    private DataSourceManager()
    {
        initialise();
    }

    /**
     * Initialise.
     */
    private void initialise()
    {
        registerDataSources();
    }

    /**
     * Adds the data source.
     *
     * @param dataSourceObj the data source obj
     */
    private void addDataSource(EsriDataSourceInterface dataSourceObj)
    {
        logger.info("\t" + dataSourceObj.getName());
        dataSourceMap.put(dataSourceObj.getType(), dataSourceObj);
    }

    /**
     * Register data source converters.
     */
    private void registerDataSources() {

        logger.debug("Data sources supported:");

        addDataSource(new EsriFileGDB());
        addDataSource(new EsriOracleArcSDE());
    }

    /**
     * Convert and Esri data source to an SLDEditor data source.
     *
     * @param propertyMap the property map
     * @return the data source properties
     */
    public DataSourcePropertiesInterface convert(Map<String, String> propertyMap)
    {
        String type = propertyMap.get(DatasourceKeys.TYPE);
        if(type != null)
        {
            Integer dsType = 0;

            try
            {
                dsType = Integer.valueOf(type);
            }
            catch(NumberFormatException e)
            {
                ConsoleManager.getInstance().exception(this, e);
                return null;
            }

            EsriDataSourceInterface ds = dataSourceMap.get(dsType);

            if(ds != null)
            {
                return ds.convert(propertyMap);
            }
        }

        return null;
    }

}
