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

package com.sldeditor.tool.connectionlist;

import com.sldeditor.common.Controller;
import com.sldeditor.common.data.GeoServerConnection;
import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.ui.detail.BasePanel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

/**
 * The panel to allow a user to enter data GeoServer connection details.
 *
 * @author Robert Ward (SCISYS)
 */
public class ConnectorDetailsPanel extends JPanel {

    /** The Constant FIELD_X_OFFSET. */
    private static final int FIELD_X_OFFSET = 10;

    /** The Constant FIELD_HEIGHT. */
    private static final int FIELD_HEIGHT = 20;

    /** The Constant LABEL_X_START. */
    private static final int LABEL_X_START = 20;

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The Constant LABEL_WIDTH. */
    private static final int LABEL_WIDTH = 80;

    /** The text field name. */
    private JTextField textFieldName;

    /** The text field url. */
    private JTextField textFieldURL;

    /** The text field username. */
    private JTextField textFieldUsername;

    /** The text field password. */
    private JPasswordField textFieldPassword;

    /** The ok. */
    private boolean ok = false;

    /**
     * Instantiates a new geo server connector panel.
     *
     * @param frame the frame
     */
    public ConnectorDetailsPanel(JDialog frame) {

        setLayout(new BorderLayout(0, 0));

        JPanel panel = new JPanel();
        add(panel, BorderLayout.CENTER);
        panel.setLayout(new BorderLayout(0, 0));

        JPanel connectionPanel = new JPanel();
        panel.add(connectionPanel, BorderLayout.CENTER);
        connectionPanel.setBorder(
                new TitledBorder(
                        null,
                        Localisation.getString(
                                ConnectorDetailsPanel.class,
                                "ConnectorDetailsPanel.connectionDetail"),
                        TitledBorder.LEADING,
                        TitledBorder.TOP,
                        null,
                        null));

        connectionPanel.setLayout(new BoxLayout(connectionPanel, BoxLayout.PAGE_AXIS));

        //
        // Name
        //
        textFieldName = new JTextField();
        createTextField(
                connectionPanel,
                Localisation.getField(ConnectorDetailsPanel.class, "ConnectorDetailsPanel.name"),
                textFieldName,
                150);

        //
        // URL
        //
        textFieldURL = new JTextField();
        createTextField(
                connectionPanel,
                Localisation.getField(ConnectorDetailsPanel.class, "ConnectorDetailsPanel.url"),
                textFieldURL,
                250);

        //
        // Username
        //
        textFieldUsername = new JTextField();
        createTextField(
                connectionPanel,
                Localisation.getField(
                        ConnectorDetailsPanel.class, "ConnectorDetailsPanel.userName"),
                textFieldUsername,
                150);

        //
        // Password
        //
        textFieldPassword = new JPasswordField();
        createTextField(
                connectionPanel,
                Localisation.getField(
                        ConnectorDetailsPanel.class, "ConnectorDetailsPanel.password"),
                textFieldPassword,
                150);

        JPanel panelOkCancel = new JPanel();
        panel.add(panelOkCancel, BorderLayout.SOUTH);
        FlowLayout flPanelOkCancel = (FlowLayout) panelOkCancel.getLayout();
        flPanelOkCancel.setAlignment(FlowLayout.TRAILING);

        JButton btnOk =
                new JButton(Localisation.getString(ConnectorDetailsPanel.class, "common.ok"));
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
                new JButton(Localisation.getString(ConnectorDetailsPanel.class, "common.cancel"));
        btnCancel.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        ok = false;
                        frame.setVisible(false);
                    }
                });
        panelOkCancel.add(btnCancel);
    }

    /**
     * Creates the text field.
     *
     * @param parentPanel the parent panel
     * @param label the label
     * @param textField the text field
     * @param fieldWidth the field width
     */
    private void createTextField(
            JPanel parentPanel, String label, JTextField textField, int fieldWidth) {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        parentPanel.add(panel);

        JLabel lblName = new JLabel(label);
        lblName.setHorizontalAlignment(SwingConstants.TRAILING);
        lblName.setBounds(LABEL_X_START, 0, LABEL_WIDTH, FIELD_HEIGHT);
        panel.add(lblName);

        textField.setBounds(
                LABEL_X_START + LABEL_WIDTH + FIELD_X_OFFSET, 0, fieldWidth, FIELD_HEIGHT);
        textField.setColumns(30);
        panel.add(textField);
    }

    /**
     * Show dialog.
     *
     * @param parentPanel the parent panel
     * @param connectionDetails the connection details
     * @return the connector details panel
     */
    public static GeoServerConnection showDialog(
            JDialog parentPanel, GeoServerConnection connectionDetails) {
        JDialog dialog =
                new JDialog(
                        parentPanel,
                        Localisation.getString(
                                ConnectorDetailsPanel.class, "ConnectorDetailsPanel.title"),
                        true);
        dialog.setResizable(false);

        ConnectorDetailsPanel panel = new ConnectorDetailsPanel(dialog);

        dialog.getContentPane().add(panel);

        panel.populate(connectionDetails);
        dialog.pack();
        dialog.setSize(BasePanel.FIELD_PANEL_WIDTH, 175);

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
    private GeoServerConnection getConnectionDetails() {
        GeoServerConnection newConnectionDetails = new GeoServerConnection();

        newConnectionDetails.setConnectionName(textFieldName.getText());
        newConnectionDetails.setUserName(textFieldUsername.getText());
        URL url = null;

        try {
            url = new URL(textFieldURL.getText());
        } catch (MalformedURLException e) {
            // Do nothing
        }
        newConnectionDetails.setUrl(url);
        newConnectionDetails.setPassword(new String(textFieldPassword.getPassword()));

        return newConnectionDetails;
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
    private void populate(GeoServerConnection connectionDetails) {
        textFieldName.setText(connectionDetails.getConnectionName());
        URL url = connectionDetails.getUrl();
        if (url != null) {
            textFieldURL.setText(url.toString());
        } else {
            textFieldURL.setText("http://");
        }
        textFieldUsername.setText(connectionDetails.getUserName());
        textFieldPassword.setText(connectionDetails.getPassword());
    }
}
