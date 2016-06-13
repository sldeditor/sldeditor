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

import java.util.Map;

import com.sldeditor.common.DataSourcePropertiesInterface;
import com.sldeditor.datasource.connector.DataSourceConnectorFactory;
import com.sldeditor.datasource.connector.instance.DataSourceConnectorFileGDB;

/**
 * Converts an Esri File GDB data source to its SLD Editor equivalent.
 * 
 * @author Robert Ward (SCISYS)
 */
public class EsriFileGDB extends EsriDataSourceBase implements EsriDataSourceInterface
{
    
    /**
     * Instantiates a new Esri file gdb.
     */
    public EsriFileGDB()
    {
        addFieldTranslation("DATABASE", DataSourceConnectorFileGDB.FIELD_DATABASE);
    }
    
    /* (non-Javadoc)
     * @see com.sldeditor.extension.convert.esri.datasource.EsriDataSourceInterface#getName()
     */
    @Override
    public String getName()
    {
        return this.getClass().getSimpleName();
    }

    /* (non-Javadoc)
     * @see com.sldeditor.extension.convert.esri.datasource.EsriDataSourceInterface#getType()
     */
    @Override
    public int getType()
    {
        return 1;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.extension.convert.esri.datasource.EsriDataSourceInterface#convert(java.util.Map)
     */
    @Override
    public DataSourcePropertiesInterface convert(Map<String, String> propertyMap)
    {
        Map<String, String> newPropertyMap = process(propertyMap);
        
        return DataSourceConnectorFactory.getDataSourceProperties(newPropertyMap);
    }

}
