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
package com.sldeditor.common.datasource.impl;

import java.util.LinkedHashMap;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sldeditor.common.datasource.DataSourceConnectorInterface;
import com.sldeditor.common.datasource.DataSourcePropertiesInterface;
import com.sldeditor.common.datasource.connector.DataSourceConnectorFactory;

/**
 * Class that encapsulates the properties of a data source,
 * includes data source connection details.
 * <p>Can read and write it self to a string.
 * <p>Passwords are encoded.
 * 
 * @author Robert Ward (SCISYS)
 */
public class DataSourceProperties implements DataSourcePropertiesInterface
{
    /** The Constant PASSWORD_KEY. */
    private static final String PASSWORD_KEY = "password";

    /** The property map. */
    private Map<String, String> propertyMap = new LinkedHashMap<String, String>();

    /**  The data source connector. */
    private DataSourceConnectorInterface dsc = null;

    /**
     * Instantiates a new data source properties.
     *
     * @param dsc the dsc
     */
    public DataSourceProperties(DataSourceConnectorInterface dsc)
    {
        this.dsc = dsc;
    }

    /**
     * Gets the connection properties.
     *
     * @return the connection properties
     */
    @Override
    public Map<String, String> getConnectionProperties()
    {
        if(this.dsc != null)
        {
            return dsc.getConnectionProperties(this);
        }
        return propertyMap;
    }

    /**
     * Gets all the connection properties.
     *
     * @return all the connection properties
     */
    @Override
    public Map<String, String> getAllConnectionProperties()
    {
        return propertyMap;
    }

    /**
     * Sets the property map.
     *
     * @param propertyMap the property map
     */
    @Override
    public void setPropertyMap(Map<String, String> propertyMap)
    {
        this.propertyMap = propertyMap;
    }

    /**
     * Sets the filename.
     *
     * @param filename the new filename
     */
    @Override
    public void setFilename(String filename)
    {
        propertyMap.put(DataSourceConnectorInterface.FILE_MAP_KEY, filename);
    }

    /**
     * Gets the data source connector.
     *
     * @return the data source connector
     */
    @Override
    public DataSourceConnectorInterface getDataSourceConnector()
    {
        return dsc;
    }

    /**
     * Populate.
     */
    @Override
    public void populate()
    {
        if(dsc != null)
        {
            dsc.populate(this);
        }
    }

    /**
     * Gets the filename.
     *
     * @return the filename
     */
    @Override
    public String getFilename()
    {
        return propertyMap.get(DataSourceConnectorInterface.FILE_MAP_KEY);
    }

    /**
     * Encode the data source properties to XML.
     *
     * @param doc the doc
     * @param root the root
     * @param elementName the element name
     */
    @Override
    public void encodeXML(Document doc, Element root, String elementName)
    {
        if((doc == null) || (root == null) || (elementName == null))
        {
            return;
        }

        Element dataSourceElement = doc.createElement(elementName);

        for(String key : propertyMap.keySet())
        {
            Element element = doc.createElement(key);
            element.appendChild(doc.createTextNode(propertyMap.get(key)));

            dataSourceElement.appendChild(element);
        }

        root.appendChild(dataSourceElement);
    }

    /**
     * Decode the data source properties from XML.
     *
     * @param document the document
     * @param elementName the element name
     * @return the data source properties
     */
    public static DataSourcePropertiesInterface decodeXML(Document document, String elementName)
    {
        if((document == null) || (elementName == null))
        {
            return null;
        }

        Map<String, String> map = new LinkedHashMap<String, String>();

        NodeList nodeList = document.getElementsByTagName(elementName);
        if(nodeList.getLength() > 0)
        {
            Node node = nodeList.item(0);

            Node child = node.getFirstChild();

            while(child != null)
            {
                if(child.getNodeType() == Node.ELEMENT_NODE)
                {
                    map.put(child.getNodeName(), child.getTextContent());
                }
                child = child.getNextSibling();
            }
            return DataSourceConnectorFactory.getDataSourceProperties(map);
        }
        else
        {
            return DataSourceConnectorFactory.getNoDataSource();
        }
    }

    /**
     * Checks if is empty.
     *
     * @return true, if is empty
     */
    @Override
    public boolean isEmpty()
    {
        if(dsc != null)
        {
            return dsc.isEmpty();
        }
        else
        {
            return true;
        }
    }

    /**
     * Encode filename.
     *
     * @param filename the filename
     * @return the map
     */
    public static Map<String, String> encodeFilename(String filename)
    {
        Map<String, String> propertyMap = new LinkedHashMap<String, String>();

        propertyMap.put(DataSourceConnectorInterface.FILE_MAP_KEY, filename);

        return propertyMap;
    }

    /**
     * Decode filename.
     *
     * @param map the property map
     * @return the string
     */
    public static String decodeFilename(Map<String, String> map)
    {
        if(map != null)
        {
            return map.get(DataSourceConnectorInterface.FILE_MAP_KEY);
        }
        
        return null;
    }

    /**
     * Checks for password.
     *
     * @return true, if successful
     */
    @Override
    public boolean hasPassword()
    {
        return propertyMap.containsKey(PASSWORD_KEY);
    }

    /**
     * Gets the password.
     *
     * @return the password
     */
    @Override
    public String getPassword()
    {
        return propertyMap.get(PASSWORD_KEY);
    }

    /**
     * Sets the password.
     *
     * @param password the new password
     */
    @Override
    public void setPassword(String password)
    {
        propertyMap.put(PASSWORD_KEY, password);
    }

    /**
     * Gets the debug connection string.
     *
     * @return the debug connection string
     */
    @Override
    public String getDebugConnectionString()
    {
        Map<String, String> properties = getConnectionProperties();

        properties.remove(PASSWORD_KEY);

        return properties.toString();
    }
}
