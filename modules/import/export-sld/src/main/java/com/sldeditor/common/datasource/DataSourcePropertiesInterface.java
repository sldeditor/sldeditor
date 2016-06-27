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

import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * The Interface DataSourcePropertiesInterface.
 * 
 * @author Robert Ward (SCISYS)
 */
public interface DataSourcePropertiesInterface {

    /**
     * Gets the connection properties.
     *
     * @return the connection properties
     */
    Map<String, String> getConnectionProperties();

    /**
     * Gets all the connection properties.
     *
     * @return all the connection properties
     */
    Map<String, String> getAllConnectionProperties();

    /**
     * Sets the property map.
     *
     * @param propertyMap the property map
     */
    void setPropertyMap(Map<String, String> propertyMap);

    /**
     * Sets the filename.
     *
     * @param filename the new filename
     */
    void setFilename(String filename);

    /**
     * Gets the data source connector.
     *
     * @return the data source connector
     */
    DataSourceConnectorInterface getDataSourceConnector();

    /**
     * Populate.
     */
    void populate();

    /**
     * Gets the filename.
     *
     * @return the filename
     */
    String getFilename();

    /**
     * Encode the data source properties to XML.
     *
     * @param doc the doc
     * @param root the root
     * @param elementName the element name
     */
    void encodeXML(Document doc, Element root, String elementName);

    /**
     * Checks if is empty.
     *
     * @return true, if is empty
     */
    boolean isEmpty();

    /**
     * Checks for password.
     *
     * @return true, if successful
     */
    boolean hasPassword();

    /**
     * Gets the password.
     *
     * @return the password
     */
    String getPassword();

    /**
     * Sets the password.
     *
     * @param password the new password
     */
    void setPassword(String password);

    /**
     * Gets the debug connection string.
     *
     * @return the debug connection string
     */
    String getDebugConnectionString();

}