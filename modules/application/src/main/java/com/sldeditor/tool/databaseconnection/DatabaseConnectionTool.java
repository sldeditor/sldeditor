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
package com.sldeditor.tool.databaseconnection;

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
import com.sldeditor.common.data.DatabaseConnection;
import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.datasource.extension.filesystem.node.database.DatabaseNode;
import com.sldeditor.tool.ToolButton;
import com.sldeditor.tool.ToolInterface;

/**
 * Tool that manages the database connections (connect/disconnect).
 * 
 * @author Robert Ward (SCISYS)
 */
public class DatabaseConnectionTool implements ToolInterface
{

    /** The connect button. */
    private JButton connectButton;

    /** The disconnect button. */
    private JButton disconnectButton;

    /** The panel. */
    private JPanel panel;

    /** The database connect state. */
    private DatabaseConnectStateInterface databaseConnectState = null;

    /** The connection list. */
    private List<DatabaseConnection> connectionList = new ArrayList<DatabaseConnection>();

    /**
     * Instantiates a new database connection state tool.
     *
     * @param geoServerConnectState the geo server connect state
     */
    public DatabaseConnectionTool(DatabaseConnectStateInterface geoServerConnectState)
    {
        super();

        this.databaseConnectState = geoServerConnectState;

        createUI();
    }

    /**
     * Creates the ui.
     */
    private void createUI()
    {
        panel = new JPanel();
        FlowLayout flowLayout = (FlowLayout) panel.getLayout();
        flowLayout.setVgap(0);
        flowLayout.setHgap(0);
        panel.setBorder(BorderFactory.createTitledBorder(Localisation.getString(DatabaseConnectionTool.class, "DatabaseConnectionTool.title")));

        //
        // Connect button
        //
        connectButton = new ToolButton(Localisation.getString(DatabaseConnectionTool.class, "DatabaseConnectionTool.connect"),
                "tool/connect.png");
        connectButton.setEnabled(true);
        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(databaseConnectState != null)
                {
                    connectButton.setEnabled(false);
                    disconnectButton.setEnabled(false);
                    databaseConnectState.connect(connectionList);

                    for(DatabaseConnection connection : connectionList)
                    {
                        if(!databaseConnectState.isConnected(connection))
                        {
                            connectButton.setEnabled(true);
                        }
                    }
                }
            }
        });

        panel.add(connectButton);

        //
        // Disconnect button
        //
        disconnectButton = new ToolButton(Localisation.getString(DatabaseConnectionTool.class, "DatabaseConnectionTool.disconnect"),
                "tool/disconnect.png");
        disconnectButton.setEnabled(false);
        disconnectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(databaseConnectState != null)
                {
                    connectButton.setEnabled(false);
                    disconnectButton.setEnabled(false);
                    databaseConnectState.disconnect(connectionList);
                }
            }
        });

        panel.add(disconnectButton);
    }

    /* (non-Javadoc)
     * @see com.sldeditor.tool.ToolInterface#getPanel()
     */
    @Override
    public JPanel getPanel()
    {
        return panel;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.tool.ToolInterface#setSelectedItems(java.util.List, java.util.List)
     */
    @Override
    public void setSelectedItems(List<NodeInterface> nodeTypeList, List<SLDDataInterface> sldDataList)
    {
        connectionList.clear();

        for(NodeInterface node : nodeTypeList)
        {
            if(node instanceof DatabaseNode)
            {
                DatabaseNode geoserverNode = (DatabaseNode)node;

                connectionList.add(geoserverNode.getConnection());
            }
        }

        updateButtonState();
    }

    /**
     * Update button state.
     */
    private void updateButtonState()
    {
        int connected = 0;
        int disconnected = 0;

        if(databaseConnectState != null)
        {
            for(DatabaseConnection connection : connectionList)
            {
                if(databaseConnectState.isConnected(connection))
                {
                    connected ++;
                }
                else
                {
                    disconnected ++;
                }
            }
        }

        boolean connectedEnabled = false;
        boolean disconnectedEnabled = false;

        if((connected == 0) || (disconnected == 0))
        {
            if(connected > 0)
            {
                disconnectedEnabled = true;
            }
            else if(disconnected > 0)
            {
                connectedEnabled = true;
            }
        }

        connectButton.setEnabled(connectedEnabled);
        disconnectButton.setEnabled(disconnectedEnabled);
    }

    /* (non-Javadoc)
     * @see com.sldeditor.tool.ToolInterface#getToolName()
     */
    @Override
    public String getToolName()
    {
        return getClass().getName();
    }

    /* (non-Javadoc)
     * @see com.sldeditor.tool.ToolInterface#supports(java.util.List, java.util.List)
     */
    @Override
    public boolean supports(List<Class<?>> uniqueNodeTypeList, List<NodeInterface> nodeTypeList, List<SLDDataInterface> sldDataList)
    {
        if(uniqueNodeTypeList.size() == 1)
        {
            return true;
        }
        return false;
    }

    /**
     * Populate complete.
     *
     * @param connection the connection
     */
    public void populateComplete(DatabaseConnection connection)
    {
        updateButtonState();
    }
}
