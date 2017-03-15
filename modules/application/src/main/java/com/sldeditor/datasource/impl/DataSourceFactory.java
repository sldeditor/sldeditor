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
package com.sldeditor.datasource.impl;

import com.sldeditor.datasource.DataSourceInterface;
import com.sldeditor.datasource.example.ExampleLineInterface;
import com.sldeditor.datasource.example.ExamplePointInterface;
import com.sldeditor.datasource.example.ExamplePolygonInterface;
import com.sldeditor.datasource.example.impl.ExampleLineImpl;
import com.sldeditor.datasource.example.impl.ExamplePointImpl;
import com.sldeditor.datasource.example.impl.ExamplePolygonImplIOM;

/**
 * A factory for creating DataSource objects.
 * 
 * @author Robert Ward (SCISYS)
 */
public class DataSourceFactory {

    /** The data source implementation. */
    private static DataSourceInterface dataSource = null;

    /** The example polygon implementation. */
    private static ExamplePolygonInterface examplePolygonImpl = null;

    /** The example line implementation. */
    private static ExampleLineInterface exampleLineImpl = null;

    /** The example point implementation. */
    private static ExamplePointInterface examplePointImpl = null;

    /** The internal data source. */
    private static CreateDataSourceInterface internalDataSource = new CreateInternalDataSource();

    /** The external data source. */
    private static CreateDataSourceInterface externalDataSource = new CreateExternalDataSource();

    /** The inline data source. */
    private static CreateDataSourceInterface inlineDataSource = new CreateInlineDataSource();

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
                dataSource.setDataSourceCreation(internalDataSource, externalDataSource, inlineDataSource);
            }
        }
        else
        {
            if(dataSource == null)
            {
                dataSource = new DataSourceImpl();
                dataSource.setDataSourceCreation(internalDataSource, externalDataSource, inlineDataSource);
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

    /**
     * Creates a new DataSource object.
     *
     * @param hint the hint
     * @return the example polygon interface
     */
    public static synchronized ExamplePolygonInterface createExamplePolygon(String hint)
    {
        if(examplePolygonImpl == null)
        {
            examplePolygonImpl = new ExamplePolygonImplIOM();
        }
        return examplePolygonImpl;
    }

    /**
     * Creates a new DataSource object.
     *
     * @param object the object
     * @return the example line interface
     */
    public static synchronized ExampleLineInterface createExampleLine(Object object) {
        if(exampleLineImpl == null)
        {
            exampleLineImpl = new ExampleLineImpl();
        }
        return exampleLineImpl;
    }

    /**
     * Creates a new DataSource object.
     *
     * @param object the object
     * @return the example point interface
     */
    public static synchronized ExamplePointInterface createExamplePoint(Object object) {
        if(examplePointImpl == null)
        {
            examplePointImpl = new ExamplePointImpl();
        }
        return examplePointImpl;
    }
}
