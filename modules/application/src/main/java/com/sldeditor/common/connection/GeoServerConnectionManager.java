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

package com.sldeditor.common.connection;

import com.sldeditor.common.console.ConsoleManager;
import com.sldeditor.common.data.GeoServerConnection;
import com.sldeditor.common.property.PropertyManagerFactory;
import com.sldeditor.extension.filesystem.geoserver.GeoServerInput;
import com.sldeditor.extension.filesystem.geoserver.GeoServerReadProgress;
import com.sldeditor.extension.filesystem.geoserver.client.GeoServerClient;
import com.sldeditor.extension.filesystem.geoserver.client.GeoServerClientInterface;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The Class GeoServerConnectionManager.
 *
 * @author Robert Ward (SCISYS)
 */
public class GeoServerConnectionManager implements GeoServerConnectionManagerInterface {

    /** The Constant GEOSERVER_CONNECTION_FIELD. */
    private static final String GEOSERVER_CONNECTION_FIELD = "GeoServer.connection";

    /**
     * Gets the singleton instance of GeoServerConnectionManager.
     *
     * @return singleton instance of GeoServerConnectionManager
     */
    private static GeoServerConnectionManagerInterface instance = null;

    /** The GeoServerClientInterface class to create. */
    public static Class<?> geoServerClientClass = GeoServerClient.class;

    /** The connection map. */
    private Map<GeoServerConnection, GeoServerClientInterface> connectionMap =
            new LinkedHashMap<GeoServerConnection, GeoServerClientInterface>();

    /**
     * Gets the singleton instance of GeoServerConnectionManager.
     *
     * @return singleton instance of GeoServerConnectionManager
     */
    public static GeoServerConnectionManagerInterface getInstance() {
        if (instance == null) {
            instance = new GeoServerConnectionManager();
        }

        return instance;
    }

    /**
     * Gets the connection list.
     *
     * @return the connection list
     */
    @Override
    public List<GeoServerConnection> getConnectionList() {
        List<GeoServerConnection> connectionList = new ArrayList<GeoServerConnection>();

        List<String> valueList =
                PropertyManagerFactory.getInstance().getMultipleValues(GEOSERVER_CONNECTION_FIELD);

        for (String connectionString : valueList) {
            GeoServerConnection connection = GeoServerConnection.decodeString(connectionString);
            if (connection != null) {
                connectionList.add(connection);
            }
        }

        return connectionList;
    }

    /** Update connection list. */
    @Override
    public void updateList() {
        Set<GeoServerConnection> keySet = connectionMap.keySet();
        int count = 0;
        PropertyManagerFactory.getInstance().clearValue(GEOSERVER_CONNECTION_FIELD, true);
        for (GeoServerConnection connection : keySet) {
            count++;
            PropertyManagerFactory.getInstance()
                    .updateValue(GEOSERVER_CONNECTION_FIELD, count, connection.encodeAsString());
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.common.connection.GeoServerConnectionManagerInterface#getConnection(java.lang.String)
     */
    @Override
    public GeoServerConnection getConnection(String connectionDataName) {
        if (connectionDataName != null) {
            List<GeoServerConnection> connectionList = getConnectionList();
            for (GeoServerConnection existingConnectionData : connectionList) {
                if (existingConnectionData.getConnectionName().compareTo(connectionDataName) == 0) {
                    return existingConnectionData;
                }
            }
        }
        return null;
    }

    /**
     * (non-Javadoc)
     *
     * @see
     *     com.sldeditor.common.connection.GeoServerConnectionManagerInterface#readPropertyFile(com.sldeditor.extension.filesystem.geoserver.GeoServerReadProgress)
     */
    public void readPropertyFile(GeoServerReadProgress progress) {
        List<GeoServerConnection> connectionList = getConnectionList();

        for (GeoServerConnection connection : connectionList) {
            connectionMap.put(connection, createGeoServerClient(progress, connection));
        }
    }

    /**
     * Creates the GeoServer client.
     *
     * @param progress the progress
     * @param connection the connection
     * @return the GeoServer client
     */
    private GeoServerClientInterface createGeoServerClient(
            GeoServerReadProgress progress, GeoServerConnection connection) {
        GeoServerClientInterface client = null;
        try {
            client =
                    (GeoServerClientInterface)
                            Class.forName(GeoServerConnectionManager.geoServerClientClass.getName())
                                    .newInstance();
            client.initialise(progress, connection);
        } catch (InstantiationException e) {
            ConsoleManager.getInstance().exception(GeoServerInput.class, e);
        } catch (IllegalAccessException e) {
            ConsoleManager.getInstance().exception(GeoServerInput.class, e);
        } catch (ClassNotFoundException e) {
            ConsoleManager.getInstance().exception(GeoServerInput.class, e);
        }

        return client;
    }

    /**
     * Gets the connection map.
     *
     * @return the connectionMap
     */
    public Map<GeoServerConnection, GeoServerClientInterface> getConnectionMap() {
        return connectionMap;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.common.connection.GeoServerConnectionManagerInterface#removeConnection(com.sldeditor.common.data.GeoServerConnection)
     */
    @Override
    public void removeConnection(GeoServerConnection connection) {
        connectionMap.remove(connection);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.common.connection.GeoServerConnectionManagerInterface#addNewConnection(com.sldeditor.extension.filesystem.geoserver.
     * GeoServerReadProgress, com.sldeditor.common.data.GeoServerConnection)
     */
    @Override
    public void addNewConnection(
            GeoServerReadProgress progress, GeoServerConnection newConnectionDetails) {
        connectionMap.put(
                newConnectionDetails, createGeoServerClient(progress, newConnectionDetails));
    }

    /** Destroy instance. */
    public static void destroyInstance() {
        instance = null;
    }
}
