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
import com.sldeditor.common.DataSourcePropertiesInterface;
import com.sldeditor.common.localisation.Localisation;
import java.awt.Font;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Data source connector for a no connection.
 *
 * @author Robert Ward (SCISYS)
 */
public class DataSourceConnectorEmpty implements DataSourceConnectorInterface {

    /** The Constant KEY. */
    public static final String KEY = "No data source";

    /** The panel. */
    private JPanel panel = new JPanel();

    /** Instantiates a new data source connector empty. */
    public DataSourceConnectorEmpty() {
        createUI();
    }

    /** Creates the UI. */
    private void createUI() {
        JLabel label =
                new JLabel(
                        Localisation.getString(
                                DataSourceConnectorEmpty.class,
                                "DataSourceConnectorEmpty.internalDataSource"));
        Font font = label.getFont();
        Font f = new Font(font.getName(), font.getStyle(), 24);
        label.setFont(f);
        panel.add(label);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.datasource.connector.DataSourceConnectorInterface#getDisplayName()
     */
    @Override
    public String getDisplayName() {
        return KEY;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.datasource.connector.DataSourceConnectorInterface#getPanel()
     */
    @Override
    public JPanel getPanel() {
        return panel;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.datasource.connector.DataSourceConnectorInterface#accept(java.util.Map)
     */
    @Override
    public boolean accept(Map<String, Object> propertyMap) {
        return false;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.common.DataSourceConnectorInterface#accept(java.lang.String)
     */
    @Override
    public Map<String, Object> accept(String filename) {
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.datasource.connector.DataSourceConnectorInterface#getDataSourceProperties(java.util.Map)
     */
    @Override
    public DataSourcePropertiesInterface getDataSourceProperties(Map<String, Object> propertyMap) {
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.datasource.connector.DataSourceConnectorInterface#populate(com.sldeditor.datasource.impl.DataSourceProperties)
     */
    @Override
    public void populate(DataSourcePropertiesInterface dataSourceProperties) {
        // Do nothing
    }

    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.datasource.connector.DataSourceConnectorInterface#isEmpty()
     */
    @Override
    public boolean isEmpty() {
        return true;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.datasource.connector.DataSourceConnectorInterface#getConnectionProperties(com.sldeditor.datasource.impl.DataSourceProperties)
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
        // Do nothing
    }
}
