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
package com.sldeditor.tool.geoserverconnection;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

import com.sldeditor.common.NodeInterface;
import com.sldeditor.common.SLDDataInterface;
import com.sldeditor.common.console.ConsoleManager;
import com.sldeditor.common.data.GeoServerConnection;
import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.datasource.extension.filesystem.node.geoserver.GeoServerNode;
import com.sldeditor.tool.ToolButton;
import com.sldeditor.tool.ToolInterface;

/**
 * Tool that manages the GeoServer connections (connect/disconnect).
 * 
 * @author Robert Ward (SCISYS)
 */
public class GeoServerConnectionTool implements ToolInterface
{

    /** The connect button. */
    private JButton connectButton;

    /** The disconnect button. */
    private JButton disconnectButton;

    /** The panel. */
    private JPanel panel;

    /** The geo server connect state. */
    private GeoServerConnectStateInterface geoServerConnectState = null;

    /** The connection list. */
    private List<GeoServerConnection> connectionList = new ArrayList<GeoServerConnection>();

    /**
     * Instantiates a new geo server connection state tool.
     *
     * @param geoServerConnectState the geo server connect state
     */
    public GeoServerConnectionTool(GeoServerConnectStateInterface geoServerConnectState)
    {
        super();

        this.geoServerConnectState = geoServerConnectState;

        createUI();
    }

    /**
     * Creates the ui.
     */
    private void createUI()
    {
        panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder(Localisation.getString(GeoServerConnectionTool.class, "GeoServerConnectionTool.title")));

        //
        // Connect button
        //
        connectButton = new ToolButton(Localisation.getString(GeoServerConnectionTool.class, "GeoServerConnectionTool.connect"),
                "tool/connect.png");
        connectButton.setEnabled(true);
        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(geoServerConnectState != null)
                {
                    connectButton.setEnabled(false);
                    disconnectButton.setEnabled(false);
                    geoServerConnectState.connect(connectionList);

                    for(GeoServerConnection connection : connectionList)
                    {
                        if(!geoServerConnectState.isConnected(connection))
                        {
                            String errorMessage = String.format("%s : %s (%s)",
                                    Localisation.getString(GeoServerConnectionTool.class, "GeoServerConnectionTool.failedToConnect"),
                                    connection.getConnectionName(),
                                    connection.getUrl().toExternalForm() );
                            ConsoleManager.getInstance().error(GeoServerConnectionTool.class, errorMessage);
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
        disconnectButton = new ToolButton(Localisation.getString(GeoServerConnectionTool.class, "GeoServerConnectionTool.disconnect"),
                "tool/disconnect.png");
        disconnectButton.setEnabled(false);
        disconnectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(geoServerConnectState != null)
                {
                    connectButton.setEnabled(false);
                    disconnectButton.setEnabled(false);
                    geoServerConnectState.disconnect(connectionList);
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
            if(node instanceof GeoServerNode)
            {
                GeoServerNode geoserverNode = (GeoServerNode)node;

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

        if(geoServerConnectState != null)
        {
            for(GeoServerConnection connection : connectionList)
            {
                if(geoServerConnectState.isConnected(connection))
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
    public void populateComplete(GeoServerConnection connection)
    {
        updateButtonState();
    }
}
