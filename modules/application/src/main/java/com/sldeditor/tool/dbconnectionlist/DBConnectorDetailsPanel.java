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

import com.sldeditor.common.Controller;
import com.sldeditor.common.data.DatabaseConnection;
import com.sldeditor.common.localisation.Localisation;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

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

    /** The database connection map. */
    private Map<String, DatabaseConnectionConfigInterface> databaseConnectionMap =
            new LinkedHashMap<String, DatabaseConnectionConfigInterface>();

    /** The connection panel. */
    private JPanel connectionPanel = null;

    private DatabaseConnectionConfigInterface selectedPanel;

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

        JButton btnOk =
                new JButton(Localisation.getString(DBConnectorDetailsPanel.class, "common.ok"));
        btnOk.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        ok = true;
                        frame.setVisible(false);
                    }
                });
        panelOkCancel.add(btnOk);

        JButton btnCancel =
                new JButton(Localisation.getString(DBConnectorDetailsPanel.class, "common.cancel"));
        btnCancel.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        ok = false;
                        frame.setVisible(false);
                    }
                });
        panelOkCancel.add(btnCancel);

        JPanel panelSelection = new JPanel();
        JLabel label =
                new JLabel(
                        Localisation.getField(
                                DBConnectorDetailsPanel.class, "DBConnectorDetailsPanel.field"));
        panelSelection.add(label);

        connectionPanel = new JPanel();
        add(connectionPanel, BorderLayout.CENTER);
        connectionPanel.setLayout(new CardLayout());

        for (String key : databaseConnectionMap.keySet()) {
            DatabaseConnectionConfigInterface dsConnector = databaseConnectionMap.get(key);
            JPanel panelToAdd = dsConnector.getPanel();
            connectionPanel.add(panelToAdd, key);
        }
    }

    /** Populate database connections. */
    private void populateDatabaseConnections() {
        List<String> dbConnectorNameList = DatabaseConnectionFactory.getNames();

        for (String name : dbConnectorNameList) {
            DatabaseConnector dbConnector = new DatabaseConnector();
            databaseConnectionMap.put(name, dbConnector);
        }
    }

    /**
     * Show dialog.
     *
     * @param parentPanel the parent panel
     * @param connectionDetails the connection details
     * @return the connector details panel
     */
    public static DatabaseConnection showDialog(
            JDialog parentPanel, DatabaseConnection connectionDetails) {
        JDialog dialog = new JDialog(parentPanel, connectionDetails.getDatabaseTypeLabel(), true);
        dialog.setResizable(false);

        DBConnectorDetailsPanel panel = new DBConnectorDetailsPanel(dialog);

        dialog.getContentPane().add(panel);

        panel.populate(connectionDetails);
        dialog.setSize(500, 300);

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

        DatabaseConnection connectionDetails = null;

        if (selectedPanel != null) {
            connectionDetails = selectedPanel.getConnection();
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
                selectedPanel = databaseConnectionMap.get(name);

                if (selectedPanel != null) {
                    CardLayout cl = (CardLayout) (connectionPanel.getLayout());
                    cl.show(connectionPanel, name);
                    selectedPanel.setConnection(connectionDetails);
                }
            }
        }
    }
}
