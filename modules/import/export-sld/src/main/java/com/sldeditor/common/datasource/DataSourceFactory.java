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

import com.sldeditor.common.datasource.impl.DataSourceImpl;

/**
 * A factory for creating DataSource objects.
 * 
 * @author Robert Ward (SCISYS)
 */
public class DataSourceFactory {

    /** The data source implementation. */
    private static DataSourceInterface dataSource = null;

    /**
     * Creates a new DataSource object.
     *
     * @param override the data source to override in the factory
     * @return the data source interface
     */
    public static DataSourceInterface createDataSource(DataSourceInterface override)
    {
        if(override != null)
        {
            if((dataSource == null) || override.getClass() != dataSource.getClass())
            {
                dataSource = override;
            }
        }
        else
        {
            if(dataSource == null)
            {
                dataSource = new DataSourceImpl();
            }
        }
        return dataSource;
    }

    /**
     * Gets the data source.
     *
     * @return the data source
     */
    public static DataSourceInterface getDataSource()
    {
        return dataSource;
    }
}
