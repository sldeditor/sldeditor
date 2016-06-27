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
package com.sldeditor.common.datasource.connector.instance;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.sldeditor.common.datasource.DataSourceConnectorInterface;
import com.sldeditor.common.datasource.DataSourceFactory;
import com.sldeditor.common.datasource.DataSourceInterface;
import com.sldeditor.common.datasource.DataSourcePropertiesInterface;
import com.sldeditor.common.datasource.connector.DataSourceConnectorFactory;
import com.sldeditor.common.datasource.impl.DataSourceProperties;
import com.sldeditor.common.localisation.Localisation;

/**
 * Data source connector for shape file data sources.
 * 
 * @author Robert Ward (SCISYS)
 */
public class DataSourceConnectorShapeFile implements DataSourceConnectorInterface
{
    /** The supported file types. */
    private static String[] supportedFileTypes = {"shp"};

    /** The supported file type list. */
    private List<String> supportedFileTypeList = Arrays.asList(supportedFileTypes);

    /** The data source text field. */
    private JTextField dataSourceTextField;

    /** The data source field panel. */
    private JPanel dataSourceFieldPanel;

    /** The data source. */
    private DataSourceInterface dataSource = DataSourceFactory.createDataSource(null);

    /**
     * Default constructor
     */
    public DataSourceConnectorShapeFile()
    {
        createUI();
    }

    /* (non-Javadoc)
     * @see com.sldeditor.datasource.connector.DataSourceConnectorInterface#getDisplayName()
     */
    @Override
    public String getDisplayName()
    {
        return "Shape File";
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

        JButton btnNewData = new JButton(Localisation.getString(DataSourceConnectorShapeFile.class, "DataSourceConnectorShapeFile.data"));
        dataSourceFieldPanel.add(btnNewData);

        dataSourceTextField = new JTextField();
        dataSourceFieldPanel.add(dataSourceTextField);
        dataSourceTextField.setColumns(50);

        btnNewData.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            }
        });
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
        if(dataSourceProperties != null)
        {
            dataSourceTextField.setText(dataSourceProperties.getFilename());
        }
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
}
