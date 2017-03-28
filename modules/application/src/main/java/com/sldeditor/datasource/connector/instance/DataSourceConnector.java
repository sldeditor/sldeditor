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

import java.awt.BorderLayout;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.geotools.jdbc.JDBCDataStoreFactory;

import com.sldeditor.common.DataSourceConnectorInterface;
import com.sldeditor.common.DataSourcePropertiesInterface;
import com.sldeditor.datasource.connector.DataSourceConnectorFactory;
import com.sldeditor.datasource.impl.DataSourceProperties;

/**
 * Data source connector for data sources.
 * 
 * @author Robert Ward (SCISYS)
 */
public class DataSourceConnector implements DataSourceConnectorInterface {

    /** The data source field panel. */
    private JPanel dataSourceFieldPanel;

    /** The property map. */
    private Map<String, Object> propertyMap = new HashMap<String, Object>();

    /** The table. */
    private JTable table;

    /** The model. */
    private DefaultTableModel model = new DefaultTableModel();

    /**
     * Default constructor.
     */
    public DataSourceConnector() {
        createUI();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.datasource.connector.DataSourceConnectorInterface#getDisplayName()
     */
    @Override
    public String getDisplayName() {
        return "Database";
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.datasource.connector.DataSourceConnectorInterface#getPanel()
     */
    @Override
    public JPanel getPanel() {
        return dataSourceFieldPanel;
    }

    /**
     * Creates the ui.
     */
    private void createUI() {
        dataSourceFieldPanel = new JPanel();
        dataSourceFieldPanel.setLayout(new BorderLayout(0, 0));

        model.addColumn("Field");
        model.addColumn("Value");

        table = new JTable(model);

        JScrollPane scrollPane = new JScrollPane(table);
        dataSourceFieldPanel.add(scrollPane);

        dataSourceFieldPanel.add(table, BorderLayout.NORTH);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.datasource.connector.DataSourceConnectorInterface#accept(java.lang.String)
     */
    @Override
    public boolean accept(Map<String, Object> propertyMap) {
        if (propertyMap == null) {
            return false;
        }

        this.propertyMap = propertyMap;

        displayPropertyMap(propertyMap);
        return true;
    }

    /**
     * Display property map.
     *
     * @param propertyMap the property map
     */
    private void displayPropertyMap(Map<String, Object> propertyMap) {
        reset();
        for (String key : propertyMap.keySet()) {
            if (!key.equals(JDBCDataStoreFactory.PASSWD.key)) {
                model.addRow(new String[] { key, propertyMap.get(key).toString() });
            }
        }
        model.fireTableDataChanged();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.common.DataSourceConnectorInterface#accept(java.lang.String)
     */
    @Override
    public Map<String, Object> accept(String filename) {
        if (filename == null) {
            return null;
        }

        String fileExtension = DataSourceConnectorFactory.getFileExtension(filename);

        if (fileExtension == null) {
            return null;
        }

        Map<String, Object> propertyMap = new HashMap<String, Object>();

        propertyMap.put(DataSourceConnectorInterface.FILE_MAP_KEY, filename);

        return propertyMap;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.datasource.connector.DataSourceConnectorInterface#getDataSourceProperties()
     */
    @Override
    public DataSourcePropertiesInterface getDataSourceProperties(Map<String, Object> propertyMap) {
        DataSourcePropertiesInterface properties = new DataSourceProperties(this);

        properties.setPropertyMap(propertyMap);

        return properties;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.datasource.connector.DataSourceConnectorInterface#populate(com.sldeditor.datasource.impl.DataSourceProperties)
     */
    @Override
    public void populate(DataSourcePropertiesInterface dataSourceProperties) {
        if (dataSourceProperties != null) {

            this.propertyMap = dataSourceProperties.getAllConnectionProperties();

            displayPropertyMap(propertyMap);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.datasource.connector.DataSourceConnectorInterface#isEmpty()
     */
    @Override
    public boolean isEmpty() {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.sldeditor.datasource.connector.DataSourceConnectorInterface#getConnectionProperties(com.sldeditor.datasource.impl.DataSourceProperties)
     */
    @Override
    public Map<String, Object> getConnectionProperties(
            DataSourcePropertiesInterface dataSourceProperties) {
        if (dataSourceProperties != null) {
            return dataSourceProperties.getAllConnectionProperties();
        }
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.common.DataSourceConnectorInterface#reset()
     */
    @Override
    public void reset() {
        while (model.getRowCount() > 0) {
            model.removeRow(0);
        }

        model.fireTableDataChanged();
    }
}
