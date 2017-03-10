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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.sldeditor.common.data.DatabaseConnection;
import com.sldeditor.common.data.DatabaseConnectionField;
import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.datasource.SLDEditorFile;
import com.sldeditor.ui.detail.BasePanel;

/**
 * Database connector for database data sources.
 * 
 * @author Robert Ward (SCISYS)
 */
public class DatabaseConnector implements DatabaseConnectionConfigInterface {

    /** The text field map. */
    private Map<String, JTextField> textFieldMap = new HashMap<String, JTextField>();

    /** The accept field list. */
    private List<String> acceptFieldList = new ArrayList<String>();

    /** The panel. */
    private JPanel panel = null;

    /** The database connection. */
    private DatabaseConnection databaseConnection = null;

    /** The user key. */
    private String userKey = null;

    /** The password key. */
    private String passwordKey = null;

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
     * Default constructor.
     */
    public DatabaseConnector() {
        createUI();
    }

    /**
     * Creates the ui.
     */
    private void createUI() {
        panel = new JPanel();
        panel.setLayout(null);

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
     * @param isFilename the is filename
     */
    private void createField(DatabaseConnectionField field) {
        int row = textFieldMap.size();
        int y = row * ROW_HEIGHT;

        JLabel label = new JLabel(
                Localisation.getField(DatabaseConnector.class, field.getFieldName()));
        label.setBounds(LABEL_X, y, LABEL_WIDTH, FIELD_HEIGHT);
        panel.add(label);

        JTextField textField = null;

        if (!field.isPassword()) {
            textField = new JTextField();
        } else {
            textField = new JPasswordField();
        }

        textField.setBounds(FIELD_X, y, FIELD_WIDTH, FIELD_HEIGHT);
        textFieldMap.put(field.getKey(), textField);
        panel.add(textField);

        if (!(field.isPassword() || field.isUsername())) {
            acceptFieldList.add(field.getKey());
        }

        if (field.isFilename()) {
            JButton button = new JButton("File");
            button.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    JFileChooser fileChooser = new JFileChooser();
                    FileNameExtensionFilter fileExtensionFilter = field.getFileExtensionFilter();

                    fileChooser.setFileFilter(fileExtensionFilter);

                    File f = SLDEditorFile.getInstance().getSldEditorFile();
                    if ((f != null) && f.exists()) {
                        fileChooser.setSelectedFile(f);
                    }

                    int result = fileChooser.showOpenDialog(panel);
                    if (result == JFileChooser.APPROVE_OPTION) {
                        File selectedFile = fileChooser.getSelectedFile();
                        textFieldMap.get(field.getKey()).setText(selectedFile.getAbsolutePath());
                    }
                }
            });
            button.setBounds(textField.getX() + textField.getWidth() + 5, y,
                    BasePanel.WIDGET_BUTTON_WIDTH, FIELD_HEIGHT);
            panel.add(button);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.tool.dbconnectionlist.DatabaseConnectionConfigInterface#setConnection(com.sldeditor.common.data.DatabaseConnection)
     */
    @Override
    public void setConnection(DatabaseConnection connection) {
        this.databaseConnection = connection;

        if (connection != null) {
            for (DatabaseConnectionField field : databaseConnection.getDetailList()) {
                createField(field);

                if (field.isUsername()) {
                    userKey = field.getKey();
                } else if (field.isPassword()) {
                    passwordKey = field.getKey();
                }
            }

            Map<String, String> properties = connection.getConnectionDataMap();

            for (String fieldName : textFieldMap.keySet()) {
                JTextField textField = textFieldMap.get(fieldName);
                textField.setText(properties.get(fieldName));
            }

            if (userKey != null) {
                JTextField textField = textFieldMap.get(userKey);
                textField.setText(connection.getUserName());
            }

            if (passwordKey != null) {
                JTextField textField = textFieldMap.get(passwordKey);
                textField.setText(connection.getPassword());
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.tool.dbconnectionlist.DatabaseConnectionConfigInterface#getConnection()
     */
    @Override
    public DatabaseConnection getConnection() {
        DatabaseConnection connectionData = DatabaseConnectionFactory
                .getNewConnection(databaseConnection);

        if (connectionData != null) {
            Map<String, String> sourceMap = new HashMap<String, String>();
            for (String fieldName : textFieldMap.keySet()) {
                boolean isUserName = false;
                if (userKey != null) {
                    isUserName = fieldName.equals(userKey);
                }
                boolean isPassword = false;
                if (passwordKey != null) {
                    isPassword = fieldName.equals(passwordKey);
                }
                if (!(isUserName || isPassword)) {
                    JTextField textField = textFieldMap.get(fieldName);
                    sourceMap.put(fieldName, textField.getText());
                }
            }

            if (userKey != null) {
                JTextField textField = textFieldMap.get(userKey);
                connectionData.setUserName(textField.getText());
            }
            if (passwordKey != null) {
                JTextField textField = textFieldMap.get(passwordKey);
                connectionData.setPassword(textField.getText());
            }

            connectionData.setConnectionDataMap(sourceMap);
        }
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
}
