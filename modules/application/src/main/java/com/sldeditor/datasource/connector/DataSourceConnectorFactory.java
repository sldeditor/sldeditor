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

package com.sldeditor.datasource.connector;

import com.sldeditor.common.DataSourceConnectorInterface;
import com.sldeditor.common.DataSourcePropertiesInterface;
import com.sldeditor.datasource.connector.instance.DataSourceConnector;
import com.sldeditor.datasource.connector.instance.DataSourceConnectorEmpty;
import com.sldeditor.datasource.impl.DataSourceProperties;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A factory for creating DataSourceConnector objects.
 *
 * @author Robert Ward (SCISYS)
 */
public class DataSourceConnectorFactory {
    /** The data connector map. */
    private static Map<Class<?>, DataSourceConnectorInterface> dataConnectorMap =
            new LinkedHashMap<Class<?>, DataSourceConnectorInterface>();

    /** The no data source. */
    private static DataSourceConnectorEmpty noDataSource = null;

    /** The data source. */
    private static DataSourceConnector dataSource = null;

    /**
     * Gets the data source connector list.
     *
     * @return the data source connector list
     */
    public static Map<Class<?>, DataSourceConnectorInterface> getDataSourceConnectorList() {
        if (dataConnectorMap.isEmpty()) {
            populate();
        }

        return dataConnectorMap;
    }

    /** Populate. */
    private static void populate() {
        noDataSource = new DataSourceConnectorEmpty();
        dataSource = new DataSourceConnector();

        populate_internal(noDataSource);
        populate_internal(dataSource);
    }

    /**
     * Populate_internal.
     *
     * @param dataConnector the data connector
     */
    private static void populate_internal(DataSourceConnectorInterface dataConnector) {
        dataConnectorMap.put(dataConnector.getClass(), dataConnector);
    }

    /**
     * Gets the data source properties.
     *
     * @param filename the filename
     * @return the data source properties
     */
    public static DataSourcePropertiesInterface getDataSourceProperties(String filename) {
        if (dataConnectorMap.isEmpty()) {
            populate();
        }

        for (DataSourceConnectorInterface dc : dataConnectorMap.values()) {
            Map<String, Object> propertyMap = dc.accept(filename);
            if (propertyMap != null) {
                return getDataSourceProperties(propertyMap);
            }
        }

        return null;
    }

    /**
     * Gets the data source properties.
     *
     * @param propertyMap the property map
     * @return the data source properties
     */
    public static DataSourcePropertiesInterface getDataSourceProperties(
            Map<String, Object> propertyMap) {
        if (dataConnectorMap.isEmpty()) {
            populate();
        }

        for (DataSourceConnectorInterface dc : dataConnectorMap.values()) {
            if (dc.accept(propertyMap)) {
                return dc.getDataSourceProperties(propertyMap);
            }
        }
        return null;
    }

    /**
     * Gets the no data source.
     *
     * @return the no data source
     */
    public static DataSourcePropertiesInterface getNoDataSource() {
        return new DataSourceProperties(noDataSource);
    }

    /**
     * Gets the file extension.
     *
     * @param filename the filename
     * @return the file extension
     */
    public static String getFileExtension(String filename) {
        String fileExtension = null;

        if (filename != null) {
            int i = filename.lastIndexOf('.');
            if (i > 0) {
                fileExtension = filename.substring(i + 1);
            }
        }
        return fileExtension;
    }

    /**
     * Gets the data source for the given class type.
     *
     * @param classType the class type
     * @return the data source
     */
    public static DataSourceConnectorInterface getDataSource(Class<?> classType) {
        if (dataConnectorMap.isEmpty()) {
            populate();
        }

        return dataSource;
    }
}
