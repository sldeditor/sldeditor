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

import java.util.Map;
import javax.swing.JPanel;

/**
 * The Interface DataSourceConnectorInterface.
 *
 * @author Robert Ward (SCISYS)
 */
public interface DataSourceConnectorInterface {

    /**
     * Gets the display name.
     *
     * @return the display name
     */
    public String getDisplayName();

    /**
     * Gets the panel.
     *
     * @return the panel
     */
    public JPanel getPanel();

    /**
     * Accept if data source filename.
     *
     * @param filename the filename
     * @return true, if successful
     */
    public Map<String, Object> accept(String filename);

    /**
     * Accept if data source connector supports filename type.
     *
     * @param propertyMap the property map
     * @return true, if successful
     */
    public boolean accept(Map<String, Object> propertyMap);

    /**
     * Gets the data source properties.
     *
     * @param propertyMap the property map
     * @return the data source properties
     */
    public DataSourcePropertiesInterface getDataSourceProperties(Map<String, Object> propertyMap);

    /**
     * Populate.
     *
     * @param dataSourceProperties the data source properties
     */
    public void populate(DataSourcePropertiesInterface dataSourceProperties);

    /**
     * Checks if data source is empty.
     *
     * @return true, if data source is empty
     */
    public boolean isEmpty();

    /**
     * Gets the connection properties.
     *
     * @param dataSourceProperties the data source properties
     * @return the connection properties
     */
    public Map<String, Object> getConnectionProperties(
            DataSourcePropertiesInterface dataSourceProperties);

    /** Reset ui. */
    public void reset();
}
