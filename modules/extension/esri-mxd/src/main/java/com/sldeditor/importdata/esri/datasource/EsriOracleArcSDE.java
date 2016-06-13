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
import com.sldeditor.datasource.connector.instance.DataSourceConnectorArcSDE;

/**
 * Converts an Esri Oracle ArcSDE data source to its SLD Editor equivalent.
 * 
 * @author Robert Ward (SCISYS)
 */
public class EsriOracleArcSDE extends EsriDataSourceBase implements EsriDataSourceInterface
{
    private static final String SDE_INSTANCE_PREFIX = "SDE:";

    /**
     * Instantiates a new esri oracle arc sde.
     */
    public EsriOracleArcSDE()
    {
        addFieldTranslation("INSTANCE", DataSourceConnectorArcSDE.FIELD_PORT);
        addFieldTranslation("VERSION", DataSourceConnectorArcSDE.FIELD_VERSION);
        addFieldTranslation("USER", DataSourceConnectorArcSDE.FIELD_USER);

        addDefaultValue(DataSourceConnectorArcSDE.FIELD_DB_TYPE, "arcsde");
        addDefaultValue(DataSourceConnectorArcSDE.FIELD_SERVER, "none");
        addDefaultValue(DataSourceConnectorArcSDE.FIELD_INSTANCE, "none");
        addDefaultValue(DataSourceConnectorArcSDE.FIELD_PASSWORD, null);
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
        return 2;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.extension.convert.esri.datasource.EsriDataSourceInterface#convert(java.util.Map)
     */
    @Override
    public DataSourcePropertiesInterface convert(Map<String, String> propertyMap)
    {
        Map<String, String> newPropertyMap = process(propertyMap);

        String portString = newPropertyMap.get(DataSourceConnectorArcSDE.FIELD_PORT);

        if(portString.startsWith(SDE_INSTANCE_PREFIX))
        {
            portString = SDE_INSTANCE_PREFIX.toLowerCase() + portString.substring(SDE_INSTANCE_PREFIX.length());
            newPropertyMap.put(DataSourceConnectorArcSDE.FIELD_PORT, portString);
        }

        return DataSourceConnectorFactory.getDataSourceProperties(newPropertyMap);
    }

}
