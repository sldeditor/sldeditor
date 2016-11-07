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

import java.awt.Dimension;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.sldeditor.common.DataSourceConnectorInterface;
import com.sldeditor.common.DataSourcePropertiesInterface;
import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.datasource.impl.DataSourceProperties;

/**
 * Data source connector for Postgres data sources.
 * 
 * @author Robert Ward (SCISYS)
 */
public class DataSourceConnectorPostgres implements DataSourceConnectorInterface
{
    /** The Constant FIELD_PASSWORD. */
    public static final String FIELD_PASSWORD = "password";

    /** The Constant FIELD_USER. */
    public static final String FIELD_USER = "user";

    /** The Constant FIELD_DATABASE. */
    public static final String FIELD_DATABASE = "database";

    /** The Constant FIELD_PORT. */
    public static final String FIELD_PORT = "port";

    /** The Constant FIELD_SERVER. */
    public static final String FIELD_SERVER = "server";

    /** The text field map. */
    private Map<String, JTextField> textFieldMap = new HashMap<String, JTextField>();

    /** The panel. */
    private JPanel panel = null;

    /** The label x. */
    private static final int LABEL_X = 5;

    /** The label width. */
    private static final int LABEL_WIDTH = 90;

    /** The field height. */
    private static final int FIELD_HEIGHT = 16;

    /** The row height. */
    private static final int ROW_HEIGHT = 18;

    /** The field x. */
    private static final int FIELD_X = 100;

    /** The field width. */
    private static final int FIELD_WIDTH = 300;

    /**
     * Default constructor
     */
    public DataSourceConnectorPostgres()
    {
        createUI();
    }

    /**
     * Creates the ui.
     */
    private void createUI()
    {
        panel = new JPanel();
        panel.setLayout(null);

        createField(FIELD_SERVER, Localisation.getField(DataSourceConnectorPostgres.class, "DataSourceConnectorPostgres.server"));
        createField(FIELD_PORT, Localisation.getField(DataSourceConnectorPostgres.class, "DataSourceConnectorPostgres.port"));
        createField(FIELD_DATABASE, Localisation.getField(DataSourceConnectorPostgres.class, "DataSourceConnectorPostgres.database"));
        createField(FIELD_USER, Localisation.getField(DataSourceConnectorPostgres.class, "DataSourceConnectorPostgres.username"));
        createField(FIELD_PASSWORD, Localisation.getField(DataSourceConnectorPostgres.class, "DataSourceConnectorPostgres.password"));

        panel.setPreferredSize(new Dimension(FIELD_X + FIELD_WIDTH, textFieldMap.size() * ROW_HEIGHT));
    }

    /**
     * Creates the field.
     *
     * @param key the key
     * @param labelString the label string
     */
    private void createField(String key, String labelString)
    {
        int row = textFieldMap.size();
        int y = row * ROW_HEIGHT;

        JLabel label = new JLabel(labelString);
        label.setBounds(LABEL_X, y, LABEL_WIDTH, FIELD_HEIGHT);
        panel.add(label);

        JTextField textField = new JTextField();
        textField.setBounds(FIELD_X, y, FIELD_WIDTH, FIELD_HEIGHT);
        textFieldMap.put(key, textField);
        panel.add(textField);
    }

    /**
     * Gets the display name.
     *
     * @return the display name
     */
    /* (non-Javadoc)
     * @see com.sldeditor.datasource.connector.DataSourceConnectorInterface#getDisplayName()
     */
    @Override
    public String getDisplayName()
    {
        return "Postgres";
    }

    /**
     * Gets the panel.
     *
     * @return the panel
     */
    /* (non-Javadoc)
     * @see com.sldeditor.datasource.connector.DataSourceConnectorInterface#getPanel()
     */
    @Override
    public JPanel getPanel()
    {
        return panel;
    }

    /**
     * Accept.
     *
     * @param propertyMap the property map
     * @return true, if successful
     */
    /* (non-Javadoc)
     * @see com.sldeditor.datasource.connector.DataSourceConnectorInterface#accept(java.util.Map)
     */
    @Override
    public boolean accept(Map<String, String> propertyMap)
    {
        if(propertyMap != null)
        {
            int count = 0;
            for(String fieldName : textFieldMap.keySet())
            {
                for(String key : propertyMap.keySet())
                {
                    if(key.compareToIgnoreCase(fieldName) == 0)
                    {
                        count ++;
                        break;
                    }
                }
            }

            return (count == textFieldMap.keySet().size());
        }
        return false;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.common.DataSourceConnectorInterface#accept(java.lang.String)
     */
    @Override
    public Map<String, String> accept(String filename) {
        return null;
    }

    /**
     * Gets the data source properties.
     *
     * @param propertyMap the property map
     * @return the data source properties
     */
    /* (non-Javadoc)
     * @see com.sldeditor.datasource.connector.DataSourceConnectorInterface#getDataSourceProperties(java.util.Map)
     */
    @Override
    public DataSourcePropertiesInterface getDataSourceProperties(Map<String, String> propertyMap)
    {
        DataSourcePropertiesInterface properties = new DataSourceProperties(this);

        properties.setPropertyMap(propertyMap);

        return properties;
    }

    /**
     * Populate.
     *
     * @param dataSourceProperties the data source properties
     */
    /* (non-Javadoc)
     * @see com.sldeditor.datasource.connector.DataSourceConnectorInterface#populate(com.sldeditor.datasource.impl.DataSourceProperties)
     */
    @Override
    public void populate(DataSourcePropertiesInterface dataSourceProperties)
    {
        if(dataSourceProperties != null)
        {
            for(String fieldName : textFieldMap.keySet())
            {
                JTextField textField = textFieldMap.get(fieldName);

                Map<String,String> properties = dataSourceProperties.getAllConnectionProperties();

                textField.setText(properties.get(fieldName));
            }
        }
    }

    /**
     * Checks if is empty.
     *
     * @return true, if is empty
     */
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
        if(dataSourceProperties == null)
        {
            return null;
        }

        Map<String, String> connectPropertyMap = new LinkedHashMap<String, String>();

        Map<String, String> sourceMap = dataSourceProperties.getAllConnectionProperties();
        for(String key : sourceMap.keySet())
        {
            connectPropertyMap.put(key, sourceMap.get(key));
        }
        return connectPropertyMap;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.common.DataSourceConnectorInterface#reset()
     */
    @Override
    public void reset() {
        for(JTextField field : textFieldMap.values())
        {
            field.setText("");
        }
    }
}
