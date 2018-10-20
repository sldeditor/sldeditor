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

import com.sldeditor.common.DataSourceConnectorInterface;
import com.sldeditor.common.DataSourceConstants;
import com.sldeditor.common.DataSourcePropertiesInterface;
import com.sldeditor.datasource.connector.DataSourceConnectorFactory;
import com.sldeditor.datasource.impl.DataSourceProperties;
import java.awt.BorderLayout;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import org.geotools.jdbc.JDBCDataStoreFactory;

/**
 * Data source connector for data sources.
 *
 * @author Robert Ward (SCISYS)
 */
public class DataSourceConnector implements DataSourceConnectorInterface {

    /** The data source field panel. */
    private JPanel dataSourceFieldPanel;

    /** The property map. */
    private Map<String, Object> propertyMap = new HashMap<>();

    /** The model. */
    private DefaultTableModel model = new DefaultTableModel();

    /** Default constructor. */
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

    /** Creates the ui. */
    private void createUI() {
        dataSourceFieldPanel = new JPanel();
        dataSourceFieldPanel.setLayout(new BorderLayout(0, 0));

        model.addColumn("Field");
        model.addColumn("Value");

        JTable table = new JTable(model);

        JScrollPane scrollPane = new JScrollPane(table);
        dataSourceFieldPanel.add(scrollPane);

        dataSourceFieldPanel.add(table, BorderLayout.NORTH);
    }

    /**
     * Display property map.
     *
     * @param propertyMap the property map
     */
    private void displayPropertyMap(Map<String, Object> propertyMap) {
        reset();
        for (Entry<String, Object> entry : propertyMap.entrySet()) {
            if (!JDBCDataStoreFactory.PASSWD.key.equals(entry.getKey())) {
                model.addRow(new String[] {entry.getKey(), entry.getValue().toString()});
            }
        }
        model.fireTableDataChanged();
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

        Map<String, Object> localPropertyMap = new HashMap<>();

        localPropertyMap.put(DataSourceConstants.FILE_MAP_KEY, filename);

        return localPropertyMap;
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

    /* (non-Javadoc)
     * @see com.sldeditor.common.DataSourceConnectorInterface#getConnectionProperties(com.sldeditor.common.DataSourcePropertiesInterface)
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
