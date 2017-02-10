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

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Vector;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.sldeditor.common.Controller;
import com.sldeditor.common.data.DatabaseConnection;
import com.sldeditor.common.localisation.Localisation;

/**
 * The panel to allow a user to enter data database connection details.
 * 
 * @author Robert Ward (SCISYS)
 */
public class DBConnectorDetailsPanel extends JPanel {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The ok. */
    private boolean ok = false;

    /** The database connector combo box. */
    private JComboBox<String> databaseConnectorComboBox = null;

    /** The database connection map. */
    private Map<String, DatabaseConnectionConfigInterface> databaseConnectionMap = new LinkedHashMap<String, DatabaseConnectionConfigInterface>();

    /** The connection panel. */
    private JPanel connectionPanel = null;

    /**
     * Instantiates a new database connector panel.
     *
     * @param frame the frame
     */
    public DBConnectorDetailsPanel(JDialog frame) {

        populateDatabaseConnections();

        setLayout(new BorderLayout(0, 0));

        JPanel panelOkCancel = new JPanel();
        add(panelOkCancel, BorderLayout.SOUTH);
        FlowLayout flPanelOkCancel = (FlowLayout) panelOkCancel.getLayout();
        flPanelOkCancel.setAlignment(FlowLayout.TRAILING);

        JButton btnOk = new JButton(
                Localisation.getString(DBConnectorDetailsPanel.class, "common.ok"));
        btnOk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ok = true;
                frame.setVisible(false);
            }
        });
        panelOkCancel.add(btnOk);

        JButton btnCancel = new JButton(
                Localisation.getString(DBConnectorDetailsPanel.class, "common.cancel"));
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ok = false;
                frame.setVisible(false);
            }
        });
        panelOkCancel.add(btnCancel);

        JPanel panelSelection = new JPanel();
        JLabel label = new JLabel(Localisation.getField(DBConnectorDetailsPanel.class,
                "DBConnectorDetailsPanel.field"));
        panelSelection.add(label);

        Vector<String> nameList = new Vector<String>();
        for (String name : databaseConnectionMap.keySet()) {
            nameList.add(name);
        }
        ComboBoxModel<String> model = new DefaultComboBoxModel<String>(nameList);
        databaseConnectorComboBox = new JComboBox<String>(model);
        databaseConnectorComboBox.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent event) {
                String selectedItem = (String) databaseConnectorComboBox.getSelectedItem();

                CardLayout cl = (CardLayout) (connectionPanel.getLayout());
                cl.show(connectionPanel, selectedItem);
            }
        });
        panelSelection.add(databaseConnectorComboBox);
        add(panelSelection, BorderLayout.NORTH);

        connectionPanel = new JPanel();
        add(connectionPanel, BorderLayout.CENTER);
        connectionPanel.setLayout(new CardLayout());

        for (String key : databaseConnectionMap.keySet()) {
            DatabaseConnectionConfigInterface dsConnector = databaseConnectionMap.get(key);
            JPanel panelToAdd = dsConnector.getPanel();
            connectionPanel.add(panelToAdd, dsConnector.getName());
        }
    }

    /**
     * Populate database connections.
     */
    private void populateDatabaseConnections() {
        DatabaseConnectorPostgres postgres = new DatabaseConnectorPostgres();

        databaseConnectionMap.put(postgres.getName(), postgres);
    }

    /**
     * Show dialog.
     *
     * @param parentPanel the parent panel
     * @param connectionDetails the connection details
     * @return the connector details panel
     */
    public static DatabaseConnection showDialog(JDialog parentPanel,
            DatabaseConnection connectionDetails) {
        JDialog dialog = new JDialog(parentPanel, Localisation
                .getString(DBConnectorDetailsPanel.class, "DBConnectorDetailsPanel.title"), true);
        dialog.setResizable(false);

        DBConnectorDetailsPanel panel = new DBConnectorDetailsPanel(dialog);

        dialog.getContentPane().add(panel);

        panel.populate(connectionDetails);
        dialog.setSize(500, 250);

        Controller.getInstance().centreDialog(dialog);

        dialog.setVisible(true);

        if (panel.okButtonPressed()) {
            return panel.getConnectionDetails();
        }
        return null;
    }

    /**
     * Gets the connection details.
     *
     * @return the connection details
     */
    private DatabaseConnection getConnectionDetails() {
        String selectedItem = (String) databaseConnectorComboBox.getSelectedItem();

        DatabaseConnectionConfigInterface panel = databaseConnectionMap.get(selectedItem);

        DatabaseConnection connectionDetails = null;

        if (panel != null) {
            connectionDetails = panel.getConnection();
        }

        return connectionDetails;
    }

    /**
     * Ok button pressed.
     *
     * @return true, if successful
     */
    private boolean okButtonPressed() {
        return ok;
    }

    /**
     * Populate.
     *
     * @param connectionDetails the connection details
     */
    private void populate(DatabaseConnection connectionDetails) {
        if (connectionDetails != null) {
            for (String name : databaseConnectionMap.keySet()) {
                DatabaseConnectionConfigInterface panel = databaseConnectionMap.get(name);

                if (panel != null) {
                    if (panel.accept(connectionDetails)) {
                        databaseConnectorComboBox.setSelectedItem(name);
                        panel.setConnection(connectionDetails);
                    }
                }
            }
        } else {
            String selectedItem = (String) databaseConnectorComboBox.getSelectedItem();

            DatabaseConnectionConfigInterface panel = databaseConnectionMap.get(selectedItem);

            if (panel != null) {
                panel.setConnection(connectionDetails);
            }
        }
    }
}
