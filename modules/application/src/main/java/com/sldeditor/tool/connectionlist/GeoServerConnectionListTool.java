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

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

import com.sldeditor.common.NodeInterface;
import com.sldeditor.common.SLDDataInterface;
import com.sldeditor.common.data.GeoServerConnection;
import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.datasource.extension.filesystem.GeoServerConnectUpdateInterface;
import com.sldeditor.datasource.extension.filesystem.node.geoserver.GeoServerNode;
import com.sldeditor.tool.ToolButton;
import com.sldeditor.tool.ToolInterface;

/**
 * Tool that manages all the GeoServer connections.
 * 
 * @author Robert Ward (SCISYS)
 */
public class GeoServerConnectionListTool implements ToolInterface {

    /** The btn new. */
    private JButton btnNew;

    /** The btn duplicate. */
    private JButton btnDuplicate;

    /** The btn edit. */
    private JButton btnEdit;

    /** The btn delete. */
    private JButton btnDelete;

    /** The panel. */
    private JPanel panel;

    /** The geo server connect update object. */
    private GeoServerConnectUpdateInterface geoServerConnectUpdate = null;

    /** The connection list. */
    private List<GeoServerConnection> connectionList = new ArrayList<GeoServerConnection>();

    /**
     * Instantiates a new geo server connection list tool.
     *
     * @param geoServerConnectUpdate the geo server connect update
     */
    public GeoServerConnectionListTool(GeoServerConnectUpdateInterface geoServerConnectUpdate) {
        super();

        this.geoServerConnectUpdate = geoServerConnectUpdate;

        createUI();
    }

    /**
     * Creates the ui.
     */
    private void createUI() {
        panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder(Localisation.getString(
                GeoServerConnectionListTool.class, "GeoServerConnectionListTool.title")));
        FlowLayout flowLayout = (FlowLayout) panel.getLayout();
        flowLayout.setVgap(0);
        flowLayout.setHgap(0);

        btnNew = new ToolButton(Localisation.getString(GeoServerConnectionListTool.class,
                "GeoServerConnectionListTool.new"), "tool/newconnection.png");
        btnNew.setEnabled(true);
        btnNew.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (geoServerConnectUpdate != null) {
                    GeoServerConnection connectionDetails = new GeoServerConnection();

                    GeoServerConnection newConnectionDetails = ConnectorDetailsPanel
                            .showDialog(null, connectionDetails);

                    if (newConnectionDetails != null) {
                        geoServerConnectUpdate.addNewConnection(newConnectionDetails);
                    }
                }
            }
        });

        panel.add(btnNew);

        btnDuplicate = new ToolButton(Localisation.getString(GeoServerConnectionListTool.class,
                "GeoServerConnectionListTool.duplicate"), "tool/duplicateconnection.png");
        btnDuplicate.setEnabled(false);
        btnDuplicate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (geoServerConnectUpdate != null) {
                    if (!connectionList.isEmpty()) {
                        GeoServerConnection selectedConnectionDetails = connectionList.get(0);

                        GeoServerConnection duplicateItem = selectedConnectionDetails.duplicate();

                        geoServerConnectUpdate.addNewConnection(duplicateItem);
                    }
                }
            }
        });

        panel.add(btnDuplicate);

        btnEdit = new ToolButton(Localisation.getString(GeoServerConnectionListTool.class,
                "GeoServerConnectionListTool.edit"), "tool/editconnection.png");
        btnEdit.setEnabled(false);
        btnEdit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (geoServerConnectUpdate != null) {
                    if (!connectionList.isEmpty()) {
                        GeoServerConnection selectedConnectionDetails = connectionList.get(0);
                        GeoServerConnection newConnectionDetails = ConnectorDetailsPanel
                                .showDialog(null, selectedConnectionDetails);

                        if (newConnectionDetails != null) {
                            geoServerConnectUpdate.updateConnectionDetails(
                                    selectedConnectionDetails, newConnectionDetails);
                        }
                    }
                }
            }
        });

        panel.add(btnEdit);

        btnDelete = new ToolButton(Localisation.getString(GeoServerConnectionListTool.class,
                "GeoServerConnectionListTool.delete"), "tool/deleteconnection.png");
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (geoServerConnectUpdate != null) {
                    geoServerConnectUpdate.deleteConnections(connectionList);
                }
            }
        });

        panel.add(btnDelete);
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
    public void setSelectedItems(List<NodeInterface> nodeTypeList,
            List<SLDDataInterface> sldDataList) {
        connectionList.clear();

        boolean geoServerNodesSelected = false;

        for (NodeInterface nodeType : nodeTypeList) {
            if (nodeType instanceof GeoServerNode) {
                GeoServerNode geoserverNode = (GeoServerNode) nodeType;

                connectionList.add(geoserverNode.getConnection());
                geoServerNodesSelected = true;
            }
        }

        btnDuplicate.setEnabled(geoServerNodesSelected && (connectionList.size() == 1));
        btnEdit.setEnabled(geoServerNodesSelected && (connectionList.size() == 1));
        btnDelete.setEnabled(geoServerNodesSelected);
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
    public boolean supports(List<Class<?>> uniqueNodeTypeList, List<NodeInterface> nodeTypeList,
            List<SLDDataInterface> sldDataList) {
        if (uniqueNodeTypeList.size() == 1) {
            return true;
        }
        return false;
    }
}
