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

import com.sldeditor.common.NodeInterface;
import com.sldeditor.common.SLDDataInterface;
import com.sldeditor.common.data.DatabaseConnection;
import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.datasource.extension.filesystem.DatabaseConnectUpdateInterface;
import com.sldeditor.datasource.extension.filesystem.node.database.DatabaseNode;
import com.sldeditor.datasource.extension.filesystem.node.database.DatabaseOverallNode;
import com.sldeditor.tool.ToolButton;
import com.sldeditor.tool.ToolInterface;
import com.sldeditor.tool.ToolPanel;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * Tool that manages all the database connections.
 *
 * @author Robert Ward (SCISYS)
 */
public class DatabaseConnectionListTool implements ToolInterface {

    /** The Constant PANEL_WIDTH. */
    private static final int PANEL_WIDTH = 225;

    /** The btn new. */
    protected JButton btnNew;

    /** The btn duplicate. */
    protected JButton btnDuplicate;

    /** The btn edit. */
    protected JButton btnEdit;

    /** The btn delete. */
    protected JButton btnDelete;

    /** The panel. */
    private JPanel panel;

    /** The database connect update object. */
    private DatabaseConnectUpdateInterface databaseConnectUpdate = null;

    /** The connection list. */
    private List<DatabaseConnection> connectionList = new ArrayList<>();

    /** The selected database type. */
    private String selectedDatabaseType = null;

    /**
     * Instantiates a new database connection list tool.
     *
     * @param databaseConnectUpdate the database connect update
     */
    public DatabaseConnectionListTool(DatabaseConnectUpdateInterface databaseConnectUpdate) {
        super();

        this.databaseConnectUpdate = databaseConnectUpdate;

        createUI();
    }

    /** Creates the ui. */
    private void createUI() {
        panel = new JPanel();
        panel.setBorder(
                BorderFactory.createTitledBorder(
                        Localisation.getString(
                                DatabaseConnectionListTool.class,
                                "DatabaseConnectionListTool.title")));
        FlowLayout flowLayout = (FlowLayout) panel.getLayout();
        flowLayout.setVgap(0);
        flowLayout.setHgap(0);

        btnNew =
                new ToolButton(
                        Localisation.getString(
                                DatabaseConnectionListTool.class, "DatabaseConnectionListTool.new"),
                        "tool/newconnection.png");
        btnNew.setEnabled(true);
        btnNew.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        newButtonPressed();
                    }
                });

        panel.add(btnNew);

        btnDuplicate =
                new ToolButton(
                        Localisation.getString(
                                DatabaseConnectionListTool.class,
                                "DatabaseConnectionListTool.duplicate"),
                        "tool/duplicateconnection.png");
        btnDuplicate.setEnabled(false);
        btnDuplicate.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        duplicateButtonPressed();
                    }
                });

        panel.add(btnDuplicate);

        btnEdit =
                new ToolButton(
                        Localisation.getString(
                                DatabaseConnectionListTool.class,
                                "DatabaseConnectionListTool.edit"),
                        "tool/editconnection.png");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        editButtonPressed();
                    }
                });

        panel.add(btnEdit);

        btnDelete =
                new ToolButton(
                        Localisation.getString(
                                DatabaseConnectionListTool.class,
                                "DatabaseConnectionListTool.delete"),
                        "tool/deleteconnection.png");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        deleteButtonPressed();
                    }
                });

        panel.add(btnDelete);
        panel.setPreferredSize(new Dimension(PANEL_WIDTH, ToolPanel.TOOL_PANEL_HEIGHT));
    }

    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.tool.ToolInterface#getPanel()
     */
    @Override
    public JPanel getPanel() {
        return panel;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.tool.ToolInterface#setSelectedItems(java.util.List, java.util.List)
     */
    @Override
    public void setSelectedItems(
            List<NodeInterface> nodeTypeList, List<SLDDataInterface> sldDataList) {
        connectionList.clear();
        selectedDatabaseType = null;

        boolean databaseNodesSelected = false;
        boolean canDuplicate = true;

        if (nodeTypeList != null) {
            for (NodeInterface nodeType : nodeTypeList) {
                if (nodeType instanceof DatabaseOverallNode) {
                    DatabaseOverallNode databaseOverallNode = (DatabaseOverallNode) nodeType;
                    selectedDatabaseType = databaseOverallNode.toString();
                    canDuplicate = false;
                } else if (nodeType instanceof DatabaseNode) {
                    DatabaseNode databaseNode = (DatabaseNode) nodeType;
                    connectionList.add(databaseNode.getConnection());
                    databaseNodesSelected = true;
                }
            }
        }

        btnDuplicate.setEnabled(
                canDuplicate && databaseNodesSelected && (connectionList.size() == 1));
        btnEdit.setEnabled(databaseNodesSelected && (connectionList.size() == 1));
        btnDelete.setEnabled(databaseNodesSelected);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.tool.ToolInterface#getToolName()
     */
    @Override
    public String getToolName() {
        return getClass().getName();
    }

    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.tool.ToolInterface#supports(java.util.List, java.util.List)
     */
    @Override
    public boolean supports(
            List<Class<?>> uniqueNodeTypeList,
            List<NodeInterface> nodeTypeList,
            List<SLDDataInterface> sldDataList) {
        return ((uniqueNodeTypeList != null) && (uniqueNodeTypeList.size() == 1));
    }

    /** New button pressed. */
    protected void newButtonPressed() {
        if (databaseConnectUpdate != null) {
            DatabaseConnection connectionDetails =
                    DatabaseConnectionFactory.getNewConnection(selectedDatabaseType);

            DatabaseConnection newConnectionDetails =
                    DBConnectorDetailsPanel.showDialog(null, connectionDetails);

            if (newConnectionDetails != null) {
                databaseConnectUpdate.addNewConnection(newConnectionDetails);
            }
        }
    }

    /** Duplicate button pressed. */
    protected void duplicateButtonPressed() {
        if (databaseConnectUpdate != null) {
            if (!connectionList.isEmpty()) {
                DatabaseConnection selectedConnectionDetails = connectionList.get(0);

                DatabaseConnection duplicateItem = selectedConnectionDetails.duplicate();

                databaseConnectUpdate.addNewConnection(duplicateItem);
            }
        }
    }

    /** Edit button pressed. */
    protected void editButtonPressed() {
        if (databaseConnectUpdate != null) {
            if (!connectionList.isEmpty()) {
                DatabaseConnection selectedConnectionDetails = connectionList.get(0);
                DatabaseConnection newConnectionDetails =
                        DBConnectorDetailsPanel.showDialog(null, selectedConnectionDetails);

                if (newConnectionDetails != null) {
                    databaseConnectUpdate.updateConnectionDetails(
                            selectedConnectionDetails, newConnectionDetails);
                }
            }
        }
    }

    /** Delete button pressed. */
    protected void deleteButtonPressed() {
        if (databaseConnectUpdate != null) {
            databaseConnectUpdate.deleteConnections(connectionList);
        }
    }
}
