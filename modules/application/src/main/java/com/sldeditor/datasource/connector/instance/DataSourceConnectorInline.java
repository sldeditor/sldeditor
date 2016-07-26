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
package com.sldeditor.datasource.connector.instance;

import java.awt.FlowLayout;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

import com.sldeditor.common.DataSourceConnectorInterface;
import com.sldeditor.common.DataSourcePropertiesInterface;
import com.sldeditor.datasource.connector.DataSourceConnectorFactory;
import com.sldeditor.datasource.impl.DataSourceProperties;

/**
 * Data source connector for inline data sources.
 * 
 * @author Robert Ward (SCISYS)
 */
public class DataSourceConnectorInline implements DataSourceConnectorInterface
{
    /** The supported file type list. */
    private List<String> supportedFileTypeList = null;

    /** The data source field panel. */
    private JPanel dataSourceFieldPanel;

    /**
     * Default constructor
     */
    public DataSourceConnectorInline()
    {
        createUI();
    }

    /* (non-Javadoc)
     * @see com.sldeditor.datasource.connector.DataSourceConnectorInterface#getDisplayName()
     */
    @Override
    public String getDisplayName()
    {
        return "Inline";
    }

    /* (non-Javadoc)
     * @see com.sldeditor.datasource.connector.DataSourceConnectorInterface#getPanel()
     */
    @Override
    public JPanel getPanel()
    {
        return dataSourceFieldPanel;
    }

    /**
     * Creates the ui.
     */
    private void createUI()
    {
        dataSourceFieldPanel = new JPanel();
        FlowLayout flowLayout = (FlowLayout) dataSourceFieldPanel.getLayout();
        flowLayout.setVgap(0);
        flowLayout.setHgap(0);
    }

    /* (non-Javadoc)
     * @see com.sldeditor.datasource.connector.DataSourceConnectorInterface#accept(java.lang.String)
     */
    @Override
    public boolean accept(Map<String, String> propertyMap)
    {
        if(propertyMap == null)
        {
            return false;
        }

        String filename = DataSourceProperties.decodeFilename(propertyMap);
        String fileExtension = DataSourceConnectorFactory.getFileExtension(filename);

        if(fileExtension == null)
        {
            return false;
        }

        return (supportedFileTypeList.contains(fileExtension));
    }

    /* (non-Javadoc)
     * @see com.sldeditor.common.DataSourceConnectorInterface#accept(java.lang.String)
     */
    @Override
    public Map<String, String> accept(String filename) {
        if(filename == null)
        {
            return null;
        }

        String fileExtension = DataSourceConnectorFactory.getFileExtension(filename);

        if(fileExtension == null)
        {
            return null;
        }

        if(supportedFileTypeList.contains(fileExtension))
        {
            Map<String, String> propertyMap = new HashMap<String,String>();

            propertyMap.put(DataSourceConnectorInterface.FILE_MAP_KEY, filename);

            return propertyMap;
        }
        else
        {
            return null;
        }
    }

    /* (non-Javadoc)
     * @see com.sldeditor.datasource.connector.DataSourceConnectorInterface#getDataSourceProperties()
     */
    @Override
    public DataSourcePropertiesInterface getDataSourceProperties(Map<String, String> propertyMap)
    {
        DataSourcePropertiesInterface properties = new DataSourceProperties(this);

        properties.setPropertyMap(propertyMap);

        return properties;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.datasource.connector.DataSourceConnectorInterface#populate(com.sldeditor.datasource.impl.DataSourceProperties)
     */
    @Override
    public void populate(DataSourcePropertiesInterface dataSourceProperties)
    {
//      No data displayed
    }

    /* (non-Javadoc)
     * @see com.sldeditor.datasource.connector.DataSourceConnectorInterface#isEmpty()
     */
    @Override
    public boolean isEmpty()
    {
        return false;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.datasource.connector.DataSourceConnectorInterface#getConnectionProperties(com.sldeditor.datasource.impl.DataSourceProperties)
     */
    @Override
    public Map<String, String> getConnectionProperties(DataSourcePropertiesInterface dataSourceProperties)
    {
        if(dataSourceProperties != null)
        {
            return dataSourceProperties.getAllConnectionProperties();
        }
        return null;
    }

    /**
     * Checks if data source is inline.
     *
     * @return true, if data source is inline
     */
    @Override
    public boolean isInLine() {
        return true;
    }
}
