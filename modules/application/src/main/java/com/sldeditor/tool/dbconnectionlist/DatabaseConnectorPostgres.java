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
package com.sldeditor.tool.dbconnectionlist;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.sldeditor.common.data.DatabaseConnection;
import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.ui.detail.BasePanel;

/**
 * Database connector for Postgres data sources.
 * 
 * @author Robert Ward (SCISYS)
 */
public class DatabaseConnectorPostgres implements DatabaseConnectionConfigInterface {

    /** The Constant DEFAULT_SCHEMA. */
    private static final String DEFAULT_SCHEMA = "public";

    /** The Constant DEFAULT_PORT. */
    private static final String DEFAULT_PORT = "5432";

    /** The Constant FIELD_PASSWORD. */
    public static final String FIELD_PASSWORD = "passwd";

    /** The Constant FIELD_USER. */
    public static final String FIELD_USER = "user";

    /** The Constant FIELD_DATABASE. */
    public static final String FIELD_DATABASE = "database";

    /** The Constant FIELD_PORT. */
    public static final String FIELD_PORT = "port";

    /** The Constant FIELD_SERVER. */
    public static final String FIELD_SERVER = "host";

    /** The Constant FIELD_SCHEMA. */
    public static final String FIELD_SCHEMA = "schema";

    /** The text field map. */
    private Map<String, JTextField> textFieldMap = new HashMap<String, JTextField>();

    /** The accept field list. */
    private List<String> acceptFieldList = new ArrayList<String>();

    /** The panel. */
    private JPanel panel = null;

    /** The default data. */
    private static Map<String, String> DEFAULT_DATA = null;

    /** The label x. */
    private static final int LABEL_X = 5;

    /** The label width. */
    private static final int LABEL_WIDTH = 90;

    /** The field height. */
    private static final int FIELD_HEIGHT = BasePanel.WIDGET_HEIGHT;

    /** The row height. */
    private static final int ROW_HEIGHT = FIELD_HEIGHT + 2;

    /** The field x. */
    private static final int FIELD_X = 100;

    /** The field width. */
    private static final int FIELD_WIDTH = 300;

    /**
     * Default constructor
     */
    public DatabaseConnectorPostgres() {
        if (DEFAULT_DATA == null) {
            DEFAULT_DATA = new HashMap<String, String>();
            DEFAULT_DATA.put(FIELD_PORT, DEFAULT_PORT);
            DEFAULT_DATA.put(FIELD_SCHEMA, DEFAULT_SCHEMA);
        }
        createUI();
    }

    /**
     * Creates the ui.
     */
    private void createUI() {
        panel = new JPanel();
        panel.setLayout(null);

        createField(FIELD_SERVER, Localisation.getField(DatabaseConnectorPostgres.class,
                "DatabaseConnectorPostgres.server"), true, false);
        createField(FIELD_PORT, Localisation.getField(DatabaseConnectorPostgres.class,
                "DatabaseConnectorPostgres.port"), true, false);
        createField(FIELD_DATABASE, Localisation.getField(DatabaseConnectorPostgres.class,
                "DatabaseConnectorPostgres.database"), true, false);
        createField(FIELD_SCHEMA, Localisation.getField(DatabaseConnectorPostgres.class,
                "DatabaseConnectorPostgres.schema"), true, false);
        createField(FIELD_USER, Localisation.getField(DatabaseConnectorPostgres.class,
                "DatabaseConnectorPostgres.username"), false, false);
        createField(FIELD_PASSWORD, Localisation.getField(DatabaseConnectorPostgres.class,
                "DatabaseConnectorPostgres.password"), false, true);

        panel.setPreferredSize(
                new Dimension(FIELD_X + FIELD_WIDTH, (textFieldMap.size() + 1) * ROW_HEIGHT));
    }

    /**
     * Creates the field.
     *
     * @param key the key
     * @param labelString the label string
     * @param addToAcceptList the add to accept list
     * @param encrypted the encrypted
     */
    private void createField(String key, String labelString, boolean addToAcceptList,
            boolean encrypted) {
        int row = textFieldMap.size();
        int y = row * ROW_HEIGHT;

        JLabel label = new JLabel(labelString);
        label.setBounds(LABEL_X, y, LABEL_WIDTH, FIELD_HEIGHT);
        panel.add(label);

        JTextField textField = null;

        if (!encrypted) {
            textField = new JTextField();
        } else {
            textField = new JPasswordField();
        }

        textField.setBounds(FIELD_X, y, FIELD_WIDTH, FIELD_HEIGHT);
        textFieldMap.put(key, textField);
        panel.add(textField);

        if (addToAcceptList) {
            acceptFieldList.add(key);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.tool.dbconnectionlist.DatabaseConnectionConfigInterface#getName()
     */
    @Override
    public String getName() {
        return "Postgres";
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.tool.dbconnectionlist.DatabaseConnectionConfigInterface#setConnection(com.sldeditor.common.data.DatabaseConnection)
     */
    @Override
    public void setConnection(DatabaseConnection connection) {
        Map<String, String> properties = null;
        if (connection == null) {
            properties = DEFAULT_DATA;

        } else {
            properties = connection.getConnectionDataMap();
        }

        for (String fieldName : textFieldMap.keySet()) {
            JTextField textField = textFieldMap.get(fieldName);
            textField.setText(properties.get(fieldName));
        }

        if (connection != null) {
            JTextField textField = textFieldMap.get(FIELD_USER);
            textField.setText(connection.getUserName());
            textField = textFieldMap.get(FIELD_PASSWORD);
            textField.setText(connection.getPassword());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.tool.dbconnectionlist.DatabaseConnectionConfigInterface#getConnection()
     */
    @Override
    public DatabaseConnection getConnection() {
        DatabaseConnection connectionData = new DatabaseConnection();

        Map<String, String> sourceMap = new HashMap<String, String>();
        for (String fieldName : textFieldMap.keySet()) {
            if (!(fieldName.equals(FIELD_USER) || fieldName.equals(FIELD_PASSWORD))) {
                JTextField textField = textFieldMap.get(fieldName);
                sourceMap.put(fieldName, textField.getText());
            }
        }

        JTextField textField = textFieldMap.get(FIELD_USER);
        connectionData.setUserName(textField.getText());
        textField = textFieldMap.get(FIELD_PASSWORD);
        connectionData.setPassword(textField.getText());

        String connectionName = String.format("%s/%s@%s:%s", sourceMap.get(FIELD_SCHEMA),
                sourceMap.get(FIELD_DATABASE), sourceMap.get(FIELD_SERVER),
                sourceMap.get(FIELD_PORT));

        connectionData.setConnectionName(connectionName);
        connectionData.setConnectionDataMap(sourceMap);
        return connectionData;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.tool.dbconnectionlist.DatabaseConnectionConfigInterface#getPanel()
     */
    @Override
    public JPanel getPanel() {
        return panel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.tool.dbconnectionlist.DatabaseConnectionConfigInterface#accept(com.sldeditor.common.data.DatabaseConnection)
     */
    @Override
    public boolean accept(DatabaseConnection connection) {
        if (connection != null) {
            Map<String, String> propertyMap = connection.getConnectionDataMap();
            if (propertyMap != null) {
                int count = 0;
                for (String fieldName : acceptFieldList) {
                    for (String key : propertyMap.keySet()) {
                        if (key.compareToIgnoreCase(fieldName) == 0) {
                            count++;
                            break;
                        }
                    }
                }

                return (count == acceptFieldList.size());
            }
        }
        return false;
    }
}
